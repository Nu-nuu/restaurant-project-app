package com.example.finalproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.activity.member.ProfileMemberActivity;
import com.example.finalproject.activity.member.ReservationHistoryActivity;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;


    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText fullNameEditText;
    private EditText addressEditText;
    private Button registerButton;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // Initialize ApiService
        apiService = ApiService.createApiService();

        // Bind UI elements
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        registerButton = findViewById(R.id.registerButton);

        // Set click listener for registerButton
        registerButton.setOnClickListener(v -> registerUser());

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        // Retrieve user input from EditText fields
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String phoneString = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String fullName = fullNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String role = "member"; // Assuming the role is fixed for registration

        // Validate input fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneString.isEmpty()
                || email.isEmpty() || fullName.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!isEmailValid(email)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate phone number format
        if (!isPhoneValid(phoneString)) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password and confirm password match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert the phone number string to a numeric value
        int phone = Integer.parseInt(phoneString);

        // Create a User object with the retrieved data
        User user = new User(username, password, phone, email, fullName, address, role);

        // Call the API to register the user
        Call<User> call = apiService.addUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // User registration successful
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity and go back
                } else {
                    // User registration failed
                    Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // API call failed
                Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isEmailValid(String email) {
        // Use a regular expression to validate email format
        // Adapt the regular expression pattern as per your requirements
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isPhoneValid(String phoneString) {
        // Use a regular expression to validate phone number format
        // Adapt the regular expression pattern as per your requirements
        String phonePattern = "[0-9]{10}"; // Assuming a 10-digit phone number
        return phoneString.matches(phonePattern);
    }


}
