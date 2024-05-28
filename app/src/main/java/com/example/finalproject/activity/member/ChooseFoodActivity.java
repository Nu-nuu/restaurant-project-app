package com.example.finalproject.activity.member;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.adapter.FoodAdapter;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.models.Fooded;
import com.example.finalproject.models.FoodesHistory;
import com.example.finalproject.models.Reservate;
import com.example.finalproject.models.Reservates;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseFoodActivity extends AppCompatActivity {
    private ListView foodListView;
    private Button confirmReservationButton;
    private ImageButton buttonCancel;
    private ApiService apiService;
    private FoodAdapter foodAdapter;
    private List<Fooded> foodItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_food);

        // Initialize ApiService
        apiService = ApiService.createApiService();

        foodListView = findViewById(R.id.food_list_view);
        confirmReservationButton = findViewById(R.id.confirmReservationButton);
        buttonCancel = findViewById(R.id.buttonCancel);

        foodItems = new ArrayList<>(); // Initialize an empty food list

        foodAdapter = new FoodAdapter(this, foodItems);
        foodListView.setAdapter(foodAdapter);

        fetchFoodData(); // Fetch food data from API

        confirmReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<FoodesHistory> selectedFoodIds = foodAdapter.getSelectedFoodIds();
                Log.d("selectedFoodIds", " "+ selectedFoodIds);
                if (selectedFoodIds != null){
                    addFoodToReservation(selectedFoodIds);
                }
                // Do something with the selected food IDs
                // ...
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
            }
        });
    }

    private void cancelReservation() {
        String reservationId = getIntent().getStringExtra("reservationId");
        Call<Void> call = apiService.deleteReservation(reservationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(ChooseFoodActivity.this, MemberHomePageActivity.class);
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

    private void addFoodToReservation(List<FoodesHistory> selectedFoodIds) {
        String reservationId = getIntent().getStringExtra("reservationId");
        int guestsNumber = getIntent().getIntExtra("guestsNumber", 0);
        Log.d("reservationId", "" + reservationId);

        Log.d("Food list", "" + selectedFoodIds.toString());
        Reservates reservates = new Reservates();
        reservates.setGuestsNumber(guestsNumber);
        reservates.setFoods(selectedFoodIds);

        Log.d("Reservates" ,
                "" + reservates.toString());

        Call<Reservates> call = apiService.updateReservations(reservationId, reservates);
        call.enqueue(new Callback<Reservates>() {
            @Override
            public void onResponse(Call<Reservates> call, Response<Reservates> response) {
                if (response.isSuccessful()) {
                    // Handle successful update
                    Reservates createdReservation = response.body();
                    String reservationId = createdReservation.getId();

                    saveReservationIds(reservationId);
                    Intent intent = new Intent(ChooseFoodActivity.this, BillReservationActivity.class);
                    intent.putExtra("reservationId", reservationId);
                    startActivity(intent);

                    Toast.makeText(ChooseFoodActivity.this, "Table added to reservation", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle unsuccessful update
                    Toast.makeText(ChooseFoodActivity.this, "Failed to add table to reservation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservates> call, Throwable t) {
                // Handle failure
                Toast.makeText(ChooseFoodActivity.this, "Failed to add table to reservation: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFoodData() {
        ApiService apiService = ApiService.createApiService();
        Call<List<Fooded>> foodListCall = apiService.getAllFoods();
        foodListCall.enqueue(new Callback<List<Fooded>>() {
            @Override
            public void onResponse(Call<List<Fooded>> call, Response<List<Fooded>> response) {
                if (response.isSuccessful()) {
                    List<Fooded> foodList = response.body();
                    Log.d("Fooded list", "" + foodList.toString());
                    if (foodList != null) {
                        foodItems.clear();
                        foodItems.addAll(foodList);
                        foodAdapter.notifyDataSetChanged(); // Notify the adapter about the updated data
                    }
                } else {
                    Toast.makeText(ChooseFoodActivity.this, "Failed to retrieve food list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Fooded>> call, Throwable t) {
                Log.e("API Error", "Failed to retrieve food list", t);
                Toast.makeText(ChooseFoodActivity.this, "Failed to retrieve food list", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveReservationIds(String reservationId) {
        // Save the reservation ID in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reservationId", reservationId);
        editor.apply();
    }
    private void showCancelDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(ChooseFoodActivity.this)
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

