package com.example.finalproject.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.activity.LoginActivity;
import com.example.finalproject.manager.UserManager;
import com.example.finalproject.models.User;

public class AdminHomePageActivity extends AppCompatActivity {
    private UserManager userManager;

    private TextView usernameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private Button logoutButton;
    private Button reservationButton;
    private Button userButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        userManager = new UserManager(this);

        usernameTextView = findViewById(R.id.usernameTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        emailTextView = findViewById(R.id.emailTextView);
        logoutButton = findViewById(R.id.logoutButton);
        reservationButton = findViewById(R.id.reservationButton);
        userButton = findViewById(R.id.userButton);


        User currentUser = userManager.getCurrentUser();

        usernameTextView.setText(currentUser.getUsername());
        phoneTextView.setText(String.valueOf(currentUser.getPhone()));
        emailTextView.setText(currentUser.getEmail());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.clearCurrentUser();
                Intent intent = new Intent(AdminHomePageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this, StatusReservationActivity.class);
                startActivity(intent);
                finish();
            }
        });
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this, UserManagerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button tableButton = findViewById(R.id.tableButton);
        tableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this, TableManagerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button categoryButton = findViewById(R.id.categoryButton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this, CategoryManagerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button foodButton = findViewById(R.id.foodButton);
        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this, FoodManagerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
