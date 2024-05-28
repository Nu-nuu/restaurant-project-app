package com.example.finalproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.activity.admin.AdminHomePageActivity;
import com.example.finalproject.activity.member.MemberHomePageActivity;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.manager.UserManager;
import com.example.finalproject.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private ApiService apiService;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiService = ApiService.createApiService();
        userManager = new UserManager(LoginActivity.this);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> loginUser());

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Make sure username and password are not empty
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the getAllUsers API to retrieve the user list
        Call<List<User>> call = apiService.getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body();
                    Log.d("User","" + userList.toString());
                    // Check if the entered username and password match any user from the API response
                    for (User user : userList) {
                        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                            userManager.saveCurrentUser(user);
                            handleUserLogin(user);
                            return;
                        }
                    }

                    // No matching user found
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                } else {
                    // Error occurred while fetching user list
                    Toast.makeText(LoginActivity.this, "Failed to fetch user list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // An error occurred
                Log.e("LoginActivity", "Error: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Error occurred. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void handleUserLogin(User user) {
        if (user.getRole().equals("admin")) {
            // Redirect to AdminHomePageActivity
            startActivity(new Intent(LoginActivity.this, AdminHomePageActivity.class));
        } else if (user.getRole().equals("member")) {
            // Redirect to MemberHomePageActivity
            startActivity(new Intent(LoginActivity.this, MemberHomePageActivity.class));
        } else {
            // Invalid role
            Toast.makeText(LoginActivity.this, "Invalid user role", Toast.LENGTH_SHORT).show();
        }

        // Finish the LoginActivity to prevent the user from navigating back to it
        finish();
    }

}
