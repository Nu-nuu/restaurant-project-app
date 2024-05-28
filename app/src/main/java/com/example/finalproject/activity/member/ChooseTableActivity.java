package com.example.finalproject.activity.member;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.finalproject.R;
import com.example.finalproject.adapter.TableAdapter;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.models.Reservate;

import com.example.finalproject.models.Reservates;
import com.example.finalproject.models.Reservation;
import com.example.finalproject.models.Table;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.ListView;


public class ChooseTableActivity extends AppCompatActivity {

    private ListView tableListView;
    private List<Table> tableList;
    private TableAdapter tableAdapter;
    private ApiService apiService;
    private String selectedTableId;
    private int TableCapacity;
    private int TableNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_table);
    // Initialize ApiService
        apiService = ApiService.createApiService();
        // Initialize the ListView and TableAdapter

        tableListView = findViewById(R.id.table_list_view);
        tableList = new ArrayList<>();
        tableAdapter = new TableAdapter(this, tableList);
        tableListView.setAdapter(tableAdapter);

        // Set click listener for table selection
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Clear previous selection
                tableAdapter.setSelectedPosition(-1);
                tableAdapter.notifyDataSetChanged();

                // Set new selection
                tableAdapter.setSelectedPosition(position);
                tableAdapter.notifyDataSetChanged();

                selectedTableId = tableList.get(position).getId();
                TableCapacity = tableList.get(position).getCapacity();
                TableNumber = tableList.get(position).getTableNumber();

            }
        });

        Button confirmReservationButton = findViewById(R.id.confirmReservationButton);
        confirmReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTableId != null) {
                    addTableToReservation(selectedTableId, TableNumber,TableCapacity);
                    Intent intent = new Intent(ChooseTableActivity.this, MemberHomePageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ChooseTableActivity.this, "Please select a table", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button chooseFoodButton = findViewById(R.id.chooseFoodButton);
        chooseFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTableId != null) {
                    addTableToReservations(selectedTableId, TableNumber,TableCapacity);
                } else {
                    Toast.makeText(ChooseTableActivity.this, "Please select a table", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageButton buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
            }
        });
        // Fetch available tables
        fetchAvailableTables();
    }

    private void cancelReservation() {
        String reservationId = getIntent().getStringExtra("reservationId");
        Call<Void> call = apiService.deleteReservation(reservationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(ChooseTableActivity.this, MemberHomePageActivity.class);
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
    // order food
    private void addTableToReservations(String selectedTableId, int TableNumber, int TableCapacity) {
        // Retrieve the reservationId from the intent
        String reservationId = getIntent().getStringExtra("reservationId");
        int guestsNumber = getIntent().getIntExtra("guestsNumber", 0);

        // Create a Reservation object with the selected tableId
        Reservate reservate = new Reservate();
        reservate.setTable(selectedTableId);
        reservate.setGuestsNumber(guestsNumber);

        // Update the reservation with the selected table
        Call<Reservate> call = apiService.updateReservation(reservationId, reservate);
        call.enqueue(new Callback<Reservate>() {
            @Override
            public void onResponse(Call<Reservate> call, Response<Reservate> response) {
                if (response.isSuccessful()) {
                    // Handle successful update
                    updateTableStatus(selectedTableId, "Reserved", TableNumber, TableCapacity);
                    Reservate createdReservation = response.body();
                    String reservationId = createdReservation.getId();
                    int guestsNumber = createdReservation.getGuestsNumber();

                    saveReservationId(reservationId, guestsNumber);

                    Intent intent = new Intent(ChooseTableActivity.this, ChooseFoodActivity.class);
                    intent.putExtra("reservationId", reservationId);
                    intent.putExtra("guestsNumber", guestsNumber);
                    startActivity(intent);

                    Toast.makeText(ChooseTableActivity.this, "Table added to reservation", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle unsuccessful update
                    Toast.makeText(ChooseTableActivity.this, "Failed to add table to reservation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservate> call, Throwable t) {
                // Handle failure
                Toast.makeText(ChooseTableActivity.this, "Failed to add table to reservation: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //return home
    private void addTableToReservation(String selectedTableId, int TableNumber, int TableCapacity) {
        // Retrieve the reservationId from the intent
        String reservationId = getIntent().getStringExtra("reservationId");
        int guestsNumber = getIntent().getIntExtra("guestsNumber", 0);



        // Create a Reservation object with the selected tableId
        Reservate reservate = new Reservate();
        reservate.setTable(selectedTableId);
        reservate.setGuestsNumber(guestsNumber);

        // Update the reservation with the selected table
        Call<Reservate> call = apiService.updateReservation(reservationId, reservate);
        call.enqueue(new Callback<Reservate>() {
            @Override
            public void onResponse(Call<Reservate> call, Response<Reservate> response) {
                if (response.isSuccessful()) {
                    // Handle successful update
                    updateTableStatus(selectedTableId, "Reserved", TableNumber, TableCapacity);
                    Reservate createdReservation = response.body();
                    String reservationId = createdReservation.getId();

                    saveReservationIds(reservationId);
                    Intent intent = new Intent(ChooseTableActivity.this, BillReservationActivity.class);
                    intent.putExtra("reservationId", reservationId);
                    startActivity(intent);

                    Toast.makeText(ChooseTableActivity.this, "Table added to reservation", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle unsuccessful update
                    Toast.makeText(ChooseTableActivity.this, "Failed to add table to reservation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservate> call, Throwable t) {
                // Handle failure
                Toast.makeText(ChooseTableActivity.this, "Failed to add table to reservation: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTableStatus(String selectedTableId, String reserved, int TableNumber, int TableCapacity) {

        Table updatedTable = new Table();
        updatedTable.setStatus(reserved);
        updatedTable.setTableNumber(TableNumber);
        updatedTable.setCapacity(TableCapacity);

        // Make the API call to update the table
        Call<Table> call = apiService.updateTable(selectedTableId, updatedTable);
        call.enqueue(new Callback<Table>() {
            @Override
            public void onResponse(Call<Table> call, Response<Table> response) {
                if (response.isSuccessful()) {
                    Log.d("updateTableStatus", "Success");
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

    private void saveReservationId(String reservationId, int guestsNumber) {
        // Save the reservation ID in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reservationId", reservationId);
        editor.putInt("guestsNumber", guestsNumber);
        editor.apply();
    }
    private void saveReservationIds(String reservationId) {
        // Save the reservation ID in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reservationId", reservationId);
        editor.apply();
    }

    private void fetchAvailableTables() {
        int guestsNumber = getIntent().getIntExtra("guestsNumber", 0);

        Call<List<Table>> call = apiService.getAllTables();
        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>> call, Response<List<Table>> response) {
                if (response.isSuccessful()) {
                    List<Table> allTables = response.body();
                    List<Table> availableTables = new ArrayList<>();

                    // Filter the tables based on their status and capacity
                    for (Table table : allTables) {
                        if (table.getStatus().equals("available") && table.getCapacity() >= guestsNumber) {
                            availableTables.add(table);
                        }
                    }

                    // Update the tableList with available tables
                    tableList.clear();
                    tableList.addAll(availableTables);

                    // Notify the adapter that the data has changed
                    tableAdapter.notifyDataSetChanged();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(ChooseTableActivity.this, "Failed to fetch tables", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                // Handle failure
                Toast.makeText(ChooseTableActivity.this, "Failed to fetch tables: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showCancelDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(ChooseTableActivity.this)
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
