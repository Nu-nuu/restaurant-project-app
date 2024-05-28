package com.example.finalproject.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.activity.member.CreateReservationActivity;
import com.example.finalproject.activity.member.ReservationHistoryActivity;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.models.FoodHistory;
import com.example.finalproject.models.Reservate;
import com.example.finalproject.models.Reservation;
import com.example.finalproject.models.ReservationHistory;
import com.example.finalproject.models.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationHistoryAdapter extends RecyclerView.Adapter<ReservationHistoryAdapter.ViewHolder> {

    private List<ReservationHistory> reservationHistoryList;
    private String currentUserFullName;
    private ReservationUpdateListener reservationUpdateListener;
    private ApiService apiService;
    public ReservationHistoryAdapter(String currentUserFullName, ApiService apiService, ReservationUpdateListener listener) {
        this.currentUserFullName = currentUserFullName;
        this.apiService = apiService;
        this.reservationUpdateListener = listener;
    }
    public interface ReservationUpdateListener {
        void onReservationUpdated();
    }


    public void setReservationHistoryList(List<ReservationHistory> reservationHistoryList) {
        this.reservationHistoryList = reservationHistoryList;
        notifyDataSetChanged();
    }
    private String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy  HH:mm");
        try {
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String formatDateStart(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String formatTimeStart(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
        try {
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReservationHistory reservationHistory = reservationHistoryList.get(position);
        Reservation reservation = reservationHistory.getReservation();
        holder.fullNameTextView.setText("Full Name: " + currentUserFullName);
        holder.statusTextView.setText("Status Reservation: " + reservation.getStatus());
        holder.guestNumberTextView.setText("Guests: " + reservation.getGuestsNumber() + " People");

        holder.startDateTextView.setText("Reservation " + formatDateStart(reservation.getStartDate()));
        holder.startTimeTextView.setText("Start time " + formatTimeStart(reservation.getStartDate()));

        holder.endDateTextView.setText("End Date: " + formatDate(reservation.getEndDate()));
        holder.foodsTextView.setText("Foods Order: " + getFoodsListAsString(reservation.getFoods()));
        holder.tableNumberTextView.setText("Table Number: " + reservation.getTable().getTableNumber());
        holder.capacityTextView.setText("Capacity: " + reservation.getTable().getCapacity() + " Slots");

        holder.tableStatusTextView.setText("Status: " + reservation.getTable().getStatus() + " by " + currentUserFullName);

        if (reservation.getStatus().equals("Cancelled") || reservation.getStatus().equals("Finished")) {
            holder.buttonCancel.setVisibility(View.GONE);
            holder.tableStatusTextView.setVisibility(View.GONE);
        } else {
            holder.buttonCancel.setVisibility(View.VISIBLE);
            holder.tableStatusTextView.setVisibility(View.VISIBLE);
            holder.buttonCancel.setOnClickListener(v -> {
                String reservationId = reservationHistory.getReservation().getId();
                int guestsNumber = reservationHistory.getReservation().getGuestsNumber();
                updateReservation(reservationId, guestsNumber, "Cancelled");
            });
        }
    }

    private void updateReservation(String reservationId, int guestsNumber, String status) {

        Reservate updatedReservate = new Reservate();
        updatedReservate.setGuestsNumber(guestsNumber);
        updatedReservate.setStatus(status);

        Call<Reservate> call = apiService.updateReservation(reservationId, updatedReservate);
        call.enqueue(new Callback<Reservate>() {
            @Override
            public void onResponse(Call<Reservate> call, Response<Reservate> response) {
                if (response.isSuccessful()) {

                        Reservate reservate = response.body();
                        String tableId = reservate.getTable();

                        Call<Table> callTable = apiService.getTableByID(tableId);
                        callTable.enqueue(new Callback<Table>() {
                            @Override
                            public void onResponse(Call<Table> call, Response<Table> response) {
                                if (response.isSuccessful()) {
                                    Table table = response.body();
                                    String tableId = table.getId();
                                    int tableNumber = table.getTableNumber();
                                    int tableCapacity = table.getCapacity();
                                    updateTableStatus(tableId, "available", tableNumber, tableCapacity);
                                    Log.d("Reservate Table", "Success" );
                                    getTables();
                                }else {
                                    Log.e("API Error", "Response code: " + response.code() + ", Error: " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Table> call, Throwable t) {
                                Log.e("API Failure", "Error: " + t.getMessage(), t);
                            }
                        });


                    // Refresh the reservation list
                    reservationUpdateListener.onReservationUpdated();


                } else {
                    // Handle unsuccessful response
                    // You can display an error message or take appropriate action
                    Log.e("API Error", "Response code: " + response.code() + ", Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Reservate> call, Throwable t) {
                // Handle network or API call failure
                // You can display an error message or take appropriate action
                Log.e("API Failure", "Error: " + t.getMessage(), t);
            }
        });
    }

    private void updateTableStatus(String selectedTableId, String reserved, int tableNumber, int tableCapacity) {
        Table updatedTable = new Table(reserved);
        updatedTable.setStatus(reserved);
        updatedTable.setTableNumber(tableNumber);
        updatedTable.setCapacity(tableCapacity);

        // Make the API call to update the table
        Call<Table> call = apiService.updateTable(selectedTableId, updatedTable);
        call.enqueue(new Callback<Table>() {
            @Override
            public void onResponse(Call<Table> call, Response<Table> response) {
                if (response.isSuccessful()) {

                } else {
                    // API call failed, handle the error
                    Log.e("updateTableStatus", "Failed to update table status. Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Table> call, Throwable t) {
                // API call failed due to network error or other issues
                Log.e("updateTableStatus", "Failed to update table status. Error: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationHistoryList != null ? reservationHistoryList.size() : 0;
    }

    private String getFoodsListAsString(List<FoodHistory> foods) {
        StringBuilder foodsBuilder = new StringBuilder();
        for (int i = 0; i < foods.size(); i++) {
            if (i > 0) {
                foodsBuilder.append(", ");
            }
            foodsBuilder.append(foods.get(i).getFood().getName());
        }
        return foodsBuilder.toString();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTextView;
        TextView guestNumberTextView, startDateTextView, endDateTextView, statusTextView, startTimeTextView;
        TextView foodsTextView;
        TextView tableNumberTextView, capacityTextView, tableStatusTextView;
        Button buttonCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            guestNumberTextView = itemView.findViewById(R.id.guestNumberTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            startTimeTextView = itemView.findViewById(R.id.startTimeTextView);

            endDateTextView = itemView.findViewById(R.id.endDateTextView);
            foodsTextView = itemView.findViewById(R.id.foodsTextView);
            tableNumberTextView = itemView.findViewById(R.id.tableNumberTextView);
            capacityTextView = itemView.findViewById(R.id.capacityTextView);
            tableStatusTextView = itemView.findViewById(R.id.tableStatusTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);


            buttonCancel = itemView.findViewById(R.id.buttonCancel);

        }
    }

    private void getTables() {

        Call<List<Table>> call = apiService.getAllTables();
        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>> call, Response<List<Table>> response) {
                if (response.isSuccessful()) {

                } else {
                    Log.e("get all tables", "Failed to get all tables. Error: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                // Handle failure
                Log.e("updateTableStatus", "Failed to fetch table. Error: " + t.getMessage());

            }
        });
    }
}
