package com.example.finalproject.activity.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.activity.RegisterActivity;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.manager.UserManager;
import com.example.finalproject.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyProfileActivity extends AppCompatActivity {
    private UserManager userManager;
    private ApiService apiService;

    private LinearLayout linearPass, linearConfirm, linearButton,
            linearPhone, linearEmail, linearName, linearAddress,
            linearPho, linearEma, linearFull, linearAdd;

    private EditText passwordEdit, confirmEdit,
            phoneEdit, emailEdit, fullnameEdit, addressEdit;
    private Button changePasswordBtn, savePasswordBtn, cancelPasswordBtn,
            changePhoneBtn, savePhoneBtn, cancelPhoneBtn,
            changeEmailBtn, saveEmailBtn, cancelEmailBtn,
            changeFullnameBtn, saveFullnameBtn, cancelFullnameBtn,
            changeAddressBtn, saveAddressBtn, cancelAddressBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);

        apiService = ApiService.createApiService();

        userManager = new UserManager(this);
        User currentUser = userManager.getCurrentUser();
        String userId = currentUser.getId();

        linearPass = findViewById(R.id.linearPass);
        linearConfirm = findViewById(R.id.linearConfirm);
        linearButton = findViewById(R.id.linearButton);

        linearPho = findViewById(R.id.linearPho);
        linearEma = findViewById(R.id.linearEma);
        linearFull = findViewById(R.id.linearFull);
        linearAdd = findViewById(R.id.linearAdd);

        linearPhone = findViewById(R.id.linearPhone);
        linearEmail = findViewById(R.id.linearEmail);
        linearName = findViewById(R.id.linearName);
        linearAddress = findViewById(R.id.linearAddress);


//password
        passwordEdit = findViewById(R.id.password_edit);
        confirmEdit = findViewById(R.id.confirm_edit);
        changePasswordBtn = findViewById(R.id.change_password_btn);
        savePasswordBtn = findViewById(R.id.save_password_btn);
        cancelPasswordBtn = findViewById(R.id.cancel_password_btn);
//phone
        phoneEdit = findViewById(R.id.phone_edit);
        changePhoneBtn = findViewById(R.id.change_phone_btn);
        savePhoneBtn = findViewById(R.id.save_phone_btn);
        cancelPhoneBtn = findViewById(R.id.cancel_phone_btn);
//email
        emailEdit = findViewById(R.id.email_edit);
        changeEmailBtn = findViewById(R.id.change_email_btn);
        saveEmailBtn = findViewById(R.id.save_email_btn);
        cancelEmailBtn = findViewById(R.id.cancel_email_btn);
//fullname
        fullnameEdit = findViewById(R.id.fullname_edit);
        changeFullnameBtn = findViewById(R.id.change_fullname_btn);
        saveFullnameBtn = findViewById(R.id.save_fullname_btn);
        cancelFullnameBtn = findViewById(R.id.cancel_fullname_btn);
//address
        addressEdit = findViewById(R.id.address_edit);
        changeAddressBtn = findViewById(R.id.change_address_btn);
        saveAddressBtn = findViewById(R.id.save_address_btn);
        cancelAddressBtn = findViewById(R.id.cancel_address_btn);

        // Set text for the EditText fields
        String password = currentUser.getPassword();
        int passwordLength = password.length();
        String asterisks = new String(new char[passwordLength]).replace("\0", "*");
        passwordEdit.setText(asterisks);
        phoneEdit.setText(String.valueOf(currentUser.getPhone()));
        emailEdit.setText(currentUser.getEmail());
        fullnameEdit.setText(currentUser.getFullname());
        addressEdit.setText(currentUser.getAddress());

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyProfileActivity.this, ProfileMemberActivity.class);
                startActivity(intent);
                finish();
            }
        });
//password
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password edit texts and save/cancel buttons
                changePasswordBtn.setVisibility(View.GONE);
                linearConfirm.setVisibility(View.VISIBLE);
                linearButton.setVisibility(View.VISIBLE);
            }
        });
        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle saving the password and show password text and change button, hide edit texts and save/cancel buttons
                String password = passwordEdit.getText().toString().trim();
                String confirmPassword = confirmEdit.getText().toString().trim();

                // Validate input fields
                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(ModifyProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate password and confirm password match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(ModifyProfileActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update password using API call
                updatePassword(userId, password);

                changePasswordBtn.setVisibility(View.VISIBLE);
                linearConfirm.setVisibility(View.GONE);
                linearButton.setVisibility(View.GONE);
            }
        });
        cancelPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password text and change button, hide edit texts and save/cancel buttons
                changePasswordBtn.setVisibility(View.VISIBLE);
                linearConfirm.setVisibility(View.GONE);
                linearButton.setVisibility(View.GONE);
            }
        });

//phone
        changePhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password edit texts and save/cancel buttons
                changePhoneBtn.setVisibility(View.GONE);
                linearPho.setVisibility(View.VISIBLE);
            }
        });
        savePhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneText = phoneEdit.getText().toString().trim();

                if (phoneText.isEmpty()) {
                    Toast.makeText(ModifyProfileActivity.this, "Please enter a phone number.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate phone number format
                if (!isPhoneValid(phoneText)) {
                    Toast.makeText(ModifyProfileActivity.this, "Invalid phone number format. Please enter a 10-digit number.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int phone = Integer.parseInt(phoneText);

                // Update phone number using API call
                updatePhone(userId, phone);

                changePhoneBtn.setVisibility(View.VISIBLE);
                linearPho.setVisibility(View.GONE);
            }
        });
        cancelPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password text and change button, hide edit texts and save/cancel buttons
                changePhoneBtn.setVisibility(View.VISIBLE);
                linearPho.setVisibility(View.GONE);
            }
        });

//email
        changeEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password edit texts and save/cancel buttons
                changeEmailBtn.setVisibility(View.GONE);
                linearEma.setVisibility(View.VISIBLE);
            }
        });
        saveEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdit.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ModifyProfileActivity.this, "Please enter an email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate email format
                if (!isEmailValid(email)) {
                    Toast.makeText(ModifyProfileActivity.this, "Invalid email format. Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update email using API call
                updateEmail(userId, email);

                changeEmailBtn.setVisibility(View.VISIBLE);
                linearEma.setVisibility(View.GONE);
            }
        });
        cancelEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password text and change button, hide edit texts and save/cancel buttons
                changeEmailBtn.setVisibility(View.VISIBLE);
                linearEma.setVisibility(View.GONE);
            }
        });


//fullname
        changeFullnameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password edit texts and save/cancel buttons
                changeFullnameBtn.setVisibility(View.GONE);
                linearFull.setVisibility(View.VISIBLE);
            }
        });
        saveFullnameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = fullnameEdit.getText().toString().trim();

                if (fullname.isEmpty()) {
                    Toast.makeText(ModifyProfileActivity.this, "Please enter a fullname.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update fullname using API call
                updateFullname(userId, fullname);

                changeFullnameBtn.setVisibility(View.VISIBLE);
                linearFull.setVisibility(View.GONE);
            }
        });
        cancelFullnameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password text and change button, hide edit texts and save/cancel buttons
                changeFullnameBtn.setVisibility(View.VISIBLE);
                linearFull.setVisibility(View.GONE);
            }
        });

//address
        changeAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password edit texts and save/cancel buttons
                changeAddressBtn.setVisibility(View.GONE);
                linearAdd.setVisibility(View.VISIBLE);
            }
        });
        saveAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressEdit.getText().toString().trim();

                if (address.isEmpty()) {
                    Toast.makeText(ModifyProfileActivity.this, "Please enter an address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update address using API call
                updateAddress(userId, address);

                changeAddressBtn.setVisibility(View.VISIBLE);
                linearAdd.setVisibility(View.GONE);
            }
        });
        cancelAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password text and change button, hide edit texts and save/cancel buttons
                changeAddressBtn.setVisibility(View.VISIBLE);
                linearAdd.setVisibility(View.GONE);
            }
        });

    }
//API method
// @PUT("/users/{id}")
//      Call<User> updateUser(@Path("id") String id, @Body User user);
private void updatePassword(String userId, String password) {
    User user = new User();
    user.setPassword(password);

    Call<User> updateCall = apiService.updateUser(userId, user);
    updateCall.enqueue(new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (response.isSuccessful()) {
                // User password updated successfully
                Toast.makeText(ModifyProfileActivity.this, "Your password has been changed.", Toast.LENGTH_SHORT).show();
            } else {
                // Error occurred while updating password
                Toast.makeText(ModifyProfileActivity.this, "Failed to change password. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            Toast.makeText(ModifyProfileActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
        }
    });
}
    private void updatePhone(String userId, int phone) {
        User user = new User();
        user.setPhone(phone);

        Call<User> updateCall = apiService.updateUser(userId, user);
        updateCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // User phone number updated successfully
                    Toast.makeText(ModifyProfileActivity.this, "Your phone number has been updated.", Toast.LENGTH_SHORT).show();
                } else {
                    // Error occurred while updating phone number
                    Toast.makeText(ModifyProfileActivity.this, "Failed to update phone number. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ModifyProfileActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmail(String userId, String email) {
        User user = new User();
        user.setEmail(email);

        Call<User> updateCall = apiService.updateUser(userId, user);
        updateCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // User email updated successfully
                    Toast.makeText(ModifyProfileActivity.this, "Your email address has been updated.", Toast.LENGTH_SHORT).show();
                } else {
                    // Error occurred while updating email address
                    Toast.makeText(ModifyProfileActivity.this, "Failed to update email address. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ModifyProfileActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFullname(String userId, String fullname) {
        User user = new User();
        user.setFullname(fullname);

        Call<User> updateCall = apiService.updateUser(userId, user);
        updateCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // User fullname updated successfully
                    Toast.makeText(ModifyProfileActivity.this, "Your full name has been updated.", Toast.LENGTH_SHORT).show();
                } else {
                    // Error occurred while updating full name
                    Toast.makeText(ModifyProfileActivity.this, "Failed to update full name. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ModifyProfileActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAddress(String userId, String address) {
        User user = new User();
        user.setAddress(address);

        Call<User> updateCall = apiService.updateUser(userId, user);
        updateCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // User address updated successfully
                    Toast.makeText(ModifyProfileActivity.this, "Your address has been updated.", Toast.LENGTH_SHORT).show();
                } else {
                    // Error occurred while updating address
                    Toast.makeText(ModifyProfileActivity.this, "Failed to update address. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ModifyProfileActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
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

