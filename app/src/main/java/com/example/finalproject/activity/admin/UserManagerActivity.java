package com.example.finalproject.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.AllUserAdapter;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagerActivity extends AppCompatActivity {

    private ApiService apiService;
    private AllUserAdapter userAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_user);

        TextView titleTextView = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recyclerView);

        titleTextView.setText("Manager Users");

        apiService = ApiService.createApiService();

        getAllUsers();

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserManagerActivity.this, AdminHomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getAllUsers() {
        Call<List<User>> call = apiService.getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body();
                    userAdapter = new AllUserAdapter(userList, UserManagerActivity.this);
                    recyclerView.setAdapter(userAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(UserManagerActivity.this));
                } else {
                    Log.e("UserManagerActivity", "Failed to get users: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("UserManagerActivity", "Error getting users: " + t.getMessage());
            }
        });
    }

}

