package com.example.finalproject.activity.member;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;

import com.example.finalproject.api.ApiService;
import com.example.finalproject.manager.UserManager;
import com.example.finalproject.models.Reservation;
import com.example.finalproject.models.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateReservationActivity extends AppCompatActivity {

    // Example code
    private EditText guestNumberEditText;
    private EditText startDateEditText;
    private EditText endDateEditText;

    private Button createReservationButton;


    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reservation);
        // Initialize ApiService
        apiService = ApiService.createApiService();

        // Initialize views
        guestNumberEditText = findViewById(R.id.guestNumberEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        createReservationButton = findViewById(R.id.createReservationButton);

        // Set up the button click listener
        createReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click event
                createReservation();
            }
        });

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
            }
        });
    }
    public void showStartDatePickerDialog(View view) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Code executed when the user selects a date
                String startDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                showStartTimePickerDialog(startDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    private void showStartTimePickerDialog(final String startDate) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Code executed when the user selects a time
                String startTime = hourOfDay + ":" + minute;
                String selectedDateTime = startDate + "T" + startTime + ":00.000Z";
                startDateEditText.setText(formatDateShow(selectedDateTime));
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }
    public void showEndDatePickerDialog(View view) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Code executed when the user selects a date
                String endDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                showEndTimePickerDialog(endDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    private void showEndTimePickerDialog(final String endDate) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Code executed when the user selects a time
                String endTime = hourOfDay + ":" + minute;
                String selectedDateTime = endDate + "T" + endTime + ":00.000Z";
                endDateEditText.setText(formatDateShow(selectedDateTime));
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }
    private String formatDateShow(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy  HH:mm");
        try {
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy  HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void createReservation() {
        EditText guestNumberEditText = findViewById(R.id.guestNumberEditText);
        EditText startDateEditText = findViewById(R.id.startDateEditText);
        EditText endDateEditText = findViewById(R.id.endDateEditText);

        // Get the input values from the EditText fields
//        int guestsNumber = Integer.parseInt(guestNumberEditText.getText().toString());
        int guestsNumber = 0;
        try {
            guestsNumber = Integer.parseInt(guestNumberEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            // Handle invalid input for guestsNumber (e.g., non-numeric input)
            Toast.makeText(this, "Invalid input for guests number", Toast.LENGTH_SHORT).show();
            return;
        }

        String startDate = formatDate(startDateEditText.getText().toString().trim());
        String endDate = formatDate(endDateEditText.getText().toString().trim());
        String status = "Confirming"; // Set the default status
        if(startDate == null ){
            Toast.makeText(this, "Invalid input for Start date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endDate == null ){
            Toast.makeText(this, "Invalid input End date", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a new Reservation object with the selected values
        Reservation reservation = new Reservation(guestsNumber, startDate, endDate, status);

        // Get the current user ID from the UserManager
        UserManager userManager = new UserManager(this);
        String userId = userManager.getCurrentUser().getId();

//         Make the API call to create the reservation
        apiService.addReservation(userId, reservation)
                .enqueue(new Callback<Reservation>() {
                    @Override
                    public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                        if (response.isSuccessful()) {
                            // Reservation created successfully
                            Toast.makeText(CreateReservationActivity.this, "Reservation created", Toast.LENGTH_SHORT).show();
                            Reservation createdReservation = response.body();
                            // TODO: Handle any further actions or UI updates
                            String reservationId = createdReservation.getId();
                            int guestsNumber = createdReservation.getGuestsNumber();
                            Log.d("Reservation ID current", "" + reservationId);

                            saveReservationId(reservationId, guestsNumber);

                            Intent intent = new Intent(CreateReservationActivity.this, ChooseTableActivity.class);
                            intent.putExtra("reservationId", reservationId);
                            intent.putExtra("guestsNumber", guestsNumber);
                            startActivity(intent);

                        } else {
                            // Handle API error
                            Toast.makeText(CreateReservationActivity.this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
                            Log.e("API", "Error: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Reservation> call, Throwable t) {
                        // Handle API call failure
                        Toast.makeText(CreateReservationActivity.this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
                        Log.e("API", "Failure: " + t.getMessage());

                        // Continue execution and display the "Reservation created" toast
                        Toast.makeText(CreateReservationActivity.this, "Reservation created", Toast.LENGTH_SHORT).show();
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

    private void showCancelDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(CreateReservationActivity.this)
                .setTitle("Confirm Cancel")
                .setMessage("Do you want to cancel?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CreateReservationActivity.this, MemberHomePageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .create();
        alertDialog.show();
    }


}
