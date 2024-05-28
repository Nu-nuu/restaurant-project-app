package com.example.finalproject.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.models.FoodedHistory;
import com.example.finalproject.models.Reservate;
import com.example.finalproject.models.Reservated;
import com.example.finalproject.models.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllReservationAdapter extends RecyclerView.Adapter<AllReservationAdapter.ReservationViewHolder> {

    private List<Reservated> reservationList;
    private Context context;
    private ApiService apiService;

    public AllReservationAdapter(List<Reservated> reservationList, Context context) {
        this.reservationList = reservationList;
        this.context = context;
        apiService = ApiService.createApiService();
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_status, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservated reservated = reservationList.get(position);

        // Check if the reservation has exceeded the end date and time
        boolean isReservationFinished = isReservationExpired(reservated.getEndDate());
        if (isReservationFinished){
        String reservationId = reservated.getId();
        int guestsNumber = reservated.getGuestsNumber();
        updateReservation(reservationId, guestsNumber, "Finished");
    }

        holder.statusTextView.setText("Status: " + reservated.getStatus());
        holder.fullNameTextView.setText("Full Name: " + reservated.getUsered().getFullname());
        holder.emailTextView.setText("Email: " + reservated.getUsered().getEmail());
        holder.phoneTextView.setText("Phone (+84): " + reservated.getUsered().getPhone());
        holder.guestNumberTextView.setText("Guests Number: " + reservated.getGuestsNumber());
        holder.startDateTextView.setText("Start Date: " + formatDateShow(reservated.getStartDate()));
        holder.endDateTextView.setText("End Date: " + formatDateShow(reservated.getEndDate()));
        holder.foodsTextView.setText("Foods: " + getFoodsListAsString(reservated.getFoods()));
        holder.tableNumberTextView.setText("Table Number: " + reservated.getTable().getTableNumber());
        holder.capacityTextView.setText("Capacity: " + reservated.getTable().getCapacity());
        holder.tableStatusTextView.setText("Table Status: " + reservated.getTable().getStatus());


        holder.buttonConfirm.setOnClickListener(v -> {
            String reservationId = reservated.getId();
            int guestsNumber = reservated.getGuestsNumber();
            updateReservation(reservationId, guestsNumber, "Confirmed");
        });

        holder.buttonCancel.setOnClickListener(v -> {
            String reservationId = reservated.getId();
            int guestsNumber = reservated.getGuestsNumber();
            updateReservation(reservationId, guestsNumber, "Cancelled");
        });

        holder.buttonFinished.setOnClickListener(v -> {
            String reservationId = reservated.getId();
            int guestsNumber = reservated.getGuestsNumber();
            updateReservation(reservationId, guestsNumber, "Finished");
        });

        holder.buttonDelete.setOnClickListener(v -> {
            String reservationId = reservated.getId();
            deleteReservation(reservationId);
        });
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
                    if (reservate.getStatus().equals("Cancelled") || reservate.getStatus().equals("Finished")) {
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
                                }else {
                                    Log.e("API Error", "Response code: " + response.code() + ", Error: " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Table> call, Throwable t) {
                                Log.e("API Failure", "Error: " + t.getMessage(), t);
                            }
                        });

                    }
                    // Refresh the reservation list
                    getAllReservations();
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

    private void deleteReservation(String reservationId) {
        Call<Void> call = apiService.deleteReservation(reservationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful response after deleting the reservation
                    // For example, show a success message or update the UI

                    // Refresh the reservation list
                    getAllReservations();
                } else {
                    // Handle unsuccessful response
                    // You can display an error message or take appropriate action
                    Log.e("API Error", "Response code: " + response.code() + ", Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network or API call failure
                // You can display an error message or take appropriate action
                Log.e("API Failure", "Error: " + t.getMessage(), t);
            }
        });
    }

    private void getAllReservations() {
        Call<List<Reservated>> call = apiService.getAllReservations();
        call.enqueue(new Callback<List<Reservated>>() {
            @Override
            public void onResponse(Call<List<Reservated>> call, Response<List<Reservated>> response) {
                if (response.isSuccessful()) {
                    reservationList = response.body();
                    getTables();
                    notifyDataSetChanged(); // Notify the adapter that the data has changed
                } else {
                    Log.e("API Error", "Response code: " + response.code() + ", Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Reservated>> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage(), t);
            }
        });
    }

    private String getFoodsListAsString(List<FoodedHistory> food) {
        StringBuilder foodsBuilder = new StringBuilder();
        for (int i = 0; i < food.size(); i++) {
            if (i > 0) {
                foodsBuilder.append(", ");
            }
            foodsBuilder.append(food.get(i).getFood().getName());
        }
        return foodsBuilder.toString();
    }


    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTextView, emailTextView, phoneTextView;
        TextView guestNumberTextView, startDateTextView, endDateTextView, statusTextView;
        TextView foodsTextView, tableNumberTextView, capacityTextView, tableStatusTextView;
        Button buttonConfirm, buttonCancel, buttonFinished, buttonDelete;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            guestNumberTextView = itemView.findViewById(R.id.guestNumberTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            endDateTextView = itemView.findViewById(R.id.endDateTextView);
            foodsTextView = itemView.findViewById(R.id.foodsTextView);
            tableNumberTextView = itemView.findViewById(R.id.tableNumberTextView);
            capacityTextView = itemView.findViewById(R.id.capacityTextView);
            tableStatusTextView = itemView.findViewById(R.id.tableStatusTextView);

            buttonConfirm = itemView.findViewById(R.id.buttonConfirm);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);
            buttonFinished = itemView.findViewById(R.id.buttonFinished);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            // You can also set additional attributes or listeners for the views if needed
        }
    }
    private String formatDateShow(String date) {
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
    private boolean isReservationExpired(String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date currentDate = new Date();
            Date parsedEndDate = dateFormat.parse(endDate);

            // Compare the current date and time with the end date and time
            return currentDate.after(parsedEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
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
