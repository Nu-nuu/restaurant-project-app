package com.example.finalproject.activity.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.AllTableAdapter;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.models.Table;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableManagerActivity extends AppCompatActivity implements AllTableAdapter.TableClickListener {
    private RecyclerView recyclerView;
    private AllTableAdapter tableAdapter;
    private List<Table> tableList;
    private ApiService apiService;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_table);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tableList = new ArrayList<>();
        tableAdapter = new AllTableAdapter(tableList, this);
        recyclerView.setAdapter(tableAdapter);

        apiService = ApiService.createApiService();

        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open create table dialog
                dialogCreateTable();
            }
        });

        loadTables();

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableManagerActivity.this, AdminHomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void dialogCreateTable() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_table);

        final EditText editTableNumber = dialog.findViewById(R.id.editTableNumber);
        final EditText editCapacity = dialog.findViewById(R.id.editCapacity);
        final EditText editStatus = dialog.findViewById(R.id.editStatus);
        Button buttonCreate = dialog.findViewById(R.id.buttonCreate);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        editStatus.setText("available");
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableNumberStr = editTableNumber.getText().toString().trim();
                String capacityStr = editCapacity.getText().toString().trim();
                String status = editStatus.getText().toString().trim();

                if (!tableNumberStr.isEmpty() && !capacityStr.isEmpty() && !status.isEmpty()) {
                    int tableNumber = Integer.parseInt(tableNumberStr);
                    int capacity = Integer.parseInt(capacityStr);

                    Table table = new Table(tableNumber, capacity, status);
                    createTable(table);
                    dialog.dismiss();
                } else {
                    Toast.makeText(TableManagerActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void dialogUpdateTable(Table table) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_table);

        final EditText editTableNumber = dialog.findViewById(R.id.editTableNumber);
        final EditText editCapacity = dialog.findViewById(R.id.editCapacity);
        final EditText editStatus = dialog.findViewById(R.id.editStatus);
        Button buttonUpdate = dialog.findViewById(R.id.buttonCreate);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        editTableNumber.setText(String.valueOf(table.getTableNumber()));
        editCapacity.setText(String.valueOf(table.getCapacity()));
        editStatus.setText(table.getStatus());

        buttonUpdate.setText("Update");

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableNumberStr = editTableNumber.getText().toString().trim();
                String capacityStr = editCapacity.getText().toString().trim();
                String status = editStatus.getText().toString().trim();

                if (!tableNumberStr.isEmpty() && !capacityStr.isEmpty() && !status.isEmpty()) {
                    int tableNumber = Integer.parseInt(tableNumberStr);
                    int capacity = Integer.parseInt(capacityStr);

                    table.setTableNumber(tableNumber);
                    table.setCapacity(capacity);
                    table.setStatus(status);

                    updateTable(table);
                    dialog.dismiss();
                } else {
                    Toast.makeText(TableManagerActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void loadTables() {
        Call<List<Table>> call = apiService.getAllTables();
        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>> call, Response<List<Table>> response) {
                if (response.isSuccessful()) {
                    List<Table> tables = response.body();
                    if (tables != null) {
                        tableList.clear();
                        tableList.addAll(tables);
                        tableAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                Toast.makeText(TableManagerActivity.this, "Failed to load tables: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createTable(Table table) {
        Call<Table> call = apiService.addTable(table);
        call.enqueue(new Callback<Table>() {
            @Override
            public void onResponse(Call<Table> call, Response<Table> response) {
                if (response.isSuccessful()) {
                    Table createdTable = response.body();
                    if (createdTable != null) {
                        tableList.add(createdTable);
                        tableAdapter.notifyDataSetChanged();
                        Toast.makeText(TableManagerActivity.this, "Table created successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TableManagerActivity.this, "Failed to create table", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Table> call, Throwable t) {
                Toast.makeText(TableManagerActivity.this, "Failed to create table: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTable(Table table) {
        Call<Table> call = apiService.updateTable(table.getId(), table);
        call.enqueue(new Callback<Table>() {
            @Override
            public void onResponse(Call<Table> call, Response<Table> response) {
                if (response.isSuccessful()) {
                    tableAdapter.notifyDataSetChanged();
                    Toast.makeText(TableManagerActivity.this, "Table updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableManagerActivity.this, "Failed to update table", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Table> call, Throwable t) {
                Toast.makeText(TableManagerActivity.this, "Failed to update table: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteTable(Table table) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this table?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> call = apiService.deleteTable(table.getId());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    tableList.remove(table);
                                    tableAdapter.notifyDataSetChanged();
                                    Toast.makeText(TableManagerActivity.this, "Table deleted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TableManagerActivity.this, "Failed to delete table", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(TableManagerActivity.this, "Failed to delete table: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onTableClick(Table table) {
        // Handle table click event
        Toast.makeText(this, "Clicked on Table: " + table.getTableNumber(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateClick(Table table) {
        // Open update table dialog
        dialogUpdateTable(table);
    }

    @Override
    public void onDeleteClick(Table table) {
        // Delete table
        deleteTable(table);
    }
}
