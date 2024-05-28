package com.example.finalproject.activity.member;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;

import java.util.Date;

public class SuccessfullyActivity extends AppCompatActivity {
    int NOTIFICATION_ID=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully);

        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
                Intent intent = new Intent(SuccessfullyActivity.this, MemberHomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.baseline_restaurant_24_nine);

        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = new Notification.Builder(this, "channel_id")
                .setContentTitle("Completed deposit reservation.")
                .setContentText("Successfully, wait a minute to see status Confirmed reservation")
                .setSmallIcon(R.drawable.baseline_restaurant_24_nine)
                .setLargeIcon(bitmap)
                .build();

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.notify(getNotificationId(), notification);
        }
    }
    private int getNotificationId(){
        return (int) new Date().getTime();
    }


}
