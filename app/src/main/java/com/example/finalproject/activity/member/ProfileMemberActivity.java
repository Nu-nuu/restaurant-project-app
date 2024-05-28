package com.example.finalproject.activity.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.activity.LoginActivity;
import com.example.finalproject.activity.admin.AdminHomePageActivity;
import com.example.finalproject.activity.admin.UserManagerActivity;
import com.example.finalproject.manager.UserManager;
import com.example.finalproject.models.User;

public class ProfileMemberActivity extends AppCompatActivity {

    private UserManager userManager;

    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView fullnameTextView;
    private TextView addressTextView;
    private Button logoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userManager = new UserManager(this);

        phoneTextView = findViewById(R.id.phoneTextView);
        emailTextView = findViewById(R.id.emailTextView);
        fullnameTextView = findViewById(R.id.fullnameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        logoutButton = findViewById(R.id.logoutButton);

        LinearLayout linearLayoutHistory = findViewById(R.id.linear_layout_history);
        LinearLayout linearLayoutSetting = findViewById(R.id.linear_layout_setting);
        LinearLayout linearLayoutNotification = findViewById(R.id.linear_layout_notification);



        User currentUser = userManager.getCurrentUser();

        phoneTextView.setText(String.valueOf(currentUser.getPhone()));
        emailTextView.setText(currentUser.getEmail());
        fullnameTextView.setText(currentUser.getFullname());
        addressTextView.setText(currentUser.getAddress());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.clearCurrentUser();
                Intent intent = new Intent(ProfileMemberActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        linearLayoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMemberActivity.this, ReservationHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        linearLayoutSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMemberActivity.this, ModifyProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        linearLayoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMemberActivity.this, NewsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button buttonChange = findViewById(R.id.buttonChange);
        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMemberActivity.this, ModifyProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMemberActivity.this, MemberHomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
