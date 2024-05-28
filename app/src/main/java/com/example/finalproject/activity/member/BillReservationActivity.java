package com.example.finalproject.activity.member;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.api.ApiService;

import com.example.finalproject.models.FoodedHistory;
import com.example.finalproject.models.Reservated;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillReservationActivity extends AppCompatActivity {
    private ApiService apiService;
    private TextView billTitleTextView;
    private TextView billTimeTextView;
    private TextView fullNameTextView;
    private TextView guestsTextView;
    private TextView endTimeTextView;
    private TextView tableNumberTextView;
    private TextView capacityTextView;
    private TextView tableStatusTextView;
    private TextView foodNameTextView;
    private TextView statusTextView;
    private TextView depositTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_reservation);

        apiService = ApiService.createApiService();
        String reservationId = getIntent().getStringExtra("reservationId");
        // Initialize TextViews
        billTitleTextView = findViewById(R.id.billTitleTextView);
        billTimeTextView = findViewById(R.id.billTimeTextView);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        guestsTextView = findViewById(R.id.guestsTextView);
        endTimeTextView = findViewById(R.id.endTimeTextView);
        tableNumberTextView = findViewById(R.id.tableNumberTextView);
        capacityTextView = findViewById(R.id.capacityTextView);
        tableStatusTextView = findViewById(R.id.tableStatusTextView);
        foodNameTextView = findViewById(R.id.foodNameTextView);
        statusTextView = findViewById(R.id.statusTextView);
        depositTextView = findViewById(R.id.depositTextView);

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
            }
        });

        Button paymentButton = findViewById(R.id.paymentButton);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillReservationActivity.this, ZaloPayActivity.class);
                intent.putExtra("reservationId", reservationId);
                startActivity(intent);
                finish();
            }
        });


        getReservationById();
    }

    private void cancelReservation() {
        String reservationId = getIntent().getStringExtra("reservationId");
        Call<Void> call = apiService.deleteReservation(reservationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(BillReservationActivity.this, MemberHomePageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("cancelReservation", "Failed to cancel Reservation: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("cancelReservation", "Error cancel Reservation: " + t.getMessage());
            }
        });
    }


    private void getReservationById() {
        String reservationId = getIntent().getStringExtra("reservationId");

        Call<Reservated> call = apiService.getReservationByID(reservationId);
        call.enqueue(new Callback<Reservated>() {
            @Override
            public void onResponse(Call<Reservated> call, Response<Reservated> response) {
                if (response.isSuccessful()) {
                    Reservated reservation = response.body();
                    if (reservation != null) {
                        // Set the reservation details to the TextViews
                        billTitleTextView.setText("Reservation " + formatDateStart(reservation.getStartDate()));
                        billTimeTextView.setText("Start time " + formatTimeStart(reservation.getStartDate()));
                        fullNameTextView.setText("Full Name: " + reservation.getUsered().getFullname());
                        guestsTextView.setText("Guests: " + reservation.getGuestsNumber() + " People");
                        endTimeTextView.setText("End Date: " + formatDate(reservation.getEndDate()));
                        tableNumberTextView.setText("Table Number: " + reservation.getTable().getTableNumber());
                        capacityTextView.setText("Capacity: " + reservation.getTable().getCapacity() + " Slots");
                        tableStatusTextView.setText("Status: " + reservation.getTable().getStatus() + " by " + reservation.getUsered().getFullname());
                        foodNameTextView.setText("Foods Order: " + getFoodsListAsString(reservation.getFoods()));
                        statusTextView.setText("Status: " + reservation.getStatus());
                        depositTextView.setText("You must deposit to get reservations.");
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Reservated> call, Throwable t) {
                // Handle failure
            }
        });
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
    private String getFoodsListAsString(List<FoodedHistory> foods) {
        StringBuilder foodsBuilder = new StringBuilder();
        for (int i = 0; i < foods.size(); i++) {
            if (i > 0) {
                foodsBuilder.append(", ");
            }
            foodsBuilder.append(foods.get(i).getFood().getName());
        }
        return foodsBuilder.toString();
    }
    private void showCancelDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(BillReservationActivity.this)
                .setTitle("Confirm Cancel")
                .setMessage("Do you want to cancel?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelReservation();
                    }
                })
                .setNegativeButton("No", null)
                .create();
        alertDialog.show();
    }

}
