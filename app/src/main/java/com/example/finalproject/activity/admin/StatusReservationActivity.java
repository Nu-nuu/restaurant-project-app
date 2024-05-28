package com.example.finalproject.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.AllReservationAdapter;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.models.Reservated;
import com.example.finalproject.models.Table;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusReservationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AllReservationAdapter reservationAdapter;
    private List<Reservated> reservationList;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_status);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationList = new ArrayList<>();
        reservationAdapter = new AllReservationAdapter(reservationList, this);
        recyclerView.setAdapter(reservationAdapter);

        // Initialize the ApiService
        apiService = ApiService.createApiService();

        // Call the API to fetch all reservations
        getAllReservations();
        getTables();

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatusReservationActivity.this, AdminHomePageActivity.class);
                startActivity(intent);
                finish();
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
                    Log.d("Reservated with object", "" + reservationList.toString());
                    reservationAdapter = new AllReservationAdapter(reservationList, StatusReservationActivity.this);
                    recyclerView.setAdapter(reservationAdapter);
                } else {
                    // Log the error message from the response
                    Log.e("API Error", "Response code: " + response.code() + ", Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Reservated>> call, Throwable t) {
                // Log the failure exception
                Log.e("API Failure", "Error: " + t.getMessage(), t);
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
                    // Handle unsuccessful response
                    Toast.makeText(StatusReservationActivity.this, "Failed to get tables", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                // Handle failure
                Toast.makeText(StatusReservationActivity.this, "Failed to fetch tables: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
