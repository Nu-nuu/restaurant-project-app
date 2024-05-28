    package com.example.finalproject.activity.member;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageButton;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.finalproject.R;
    import com.example.finalproject.activity.admin.AdminHomePageActivity;
    import com.example.finalproject.adapter.ReservationHistoryAdapter;
    import com.example.finalproject.api.ApiService;
    import com.example.finalproject.manager.UserManager;
    import com.example.finalproject.models.ReservationHistory;
    import com.example.finalproject.models.User;

    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class ReservationHistoryActivity extends AppCompatActivity  implements ReservationHistoryAdapter.ReservationUpdateListener{

        private ApiService apiService;
        private RecyclerView recyclerView;
        private ReservationHistoryAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_reservation_history);

            // Initialize ApiService
            apiService = ApiService.createApiService();

            ImageButton buttonBack = findViewById(R.id.buttonBack);
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReservationHistoryActivity.this, ProfileMemberActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            // Get the user ID of the current user
            UserManager userManager = new UserManager(this);
            User currentUser = userManager.getCurrentUser();
            String userId = currentUser.getId();

            // Initialize RecyclerView
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ReservationHistoryAdapter(currentUser.getFullname(), apiService, this);
            recyclerView.setAdapter(adapter);

            // Fetch reservation history for the user
            getReservationHistory(userId);
        }
        @Override
        public void onReservationUpdated() {
            refreshReservationHistoryList();
        }

        private void getReservationHistory(String userId) {

            apiService.getReservationHistoryByUserID(userId).enqueue(new Callback<List<ReservationHistory>>() {
                @Override
                public void onResponse(Call<List<ReservationHistory>> call, Response<List<ReservationHistory>> response) {
                    if (response.isSuccessful()) {
                        List<ReservationHistory> reservationHistoryList = response.body();
                        Log.d("ReservationHistory", "" + reservationHistoryList);

                        if (reservationHistoryList != null) {
                            // Update the adapter with the reservation history data
                            adapter.setReservationHistoryList(reservationHistoryList);
                        }
                    } else {
                        Toast.makeText(ReservationHistoryActivity.this, "Failed to get reservation history ", Toast.LENGTH_SHORT).show();
                        Log.e("ReservationHistory", "Failed to get reservation history. Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<ReservationHistory>> call, Throwable t) {
                    Log.e("ReservationHistory", "Failed on failure to get reservation history. Error: " + t);
                    Toast.makeText(ReservationHistoryActivity.this, "Failed to get reservation history", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void refreshReservationHistoryList() {
            UserManager userManager = new UserManager(this);
            User currentUser = userManager.getCurrentUser();
            String userId = currentUser.getId();
            apiService.getReservationHistoryByUserID(userId).enqueue(new Callback<List<ReservationHistory>>() {
                @Override
                public void onResponse(Call<List<ReservationHistory>> call, Response<List<ReservationHistory>> response) {
                    if (response.isSuccessful()) {
                        List<ReservationHistory> reservationHistoryList = response.body();
                        if (reservationHistoryList != null) {
                            adapter.setReservationHistoryList(reservationHistoryList);
                            Toast.makeText(ReservationHistoryActivity.this, "Reservation is Cancelled", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(ReservationHistoryActivity.this, "Failed to get reservation history", Toast.LENGTH_SHORT).show();
                        Log.e("ReservationHistory", "Failed to get reservation history. Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<ReservationHistory>> call, Throwable t) {
                    Log.e("ReservationHistory", "Failed to get reservation history. Error: " + t);
                    Toast.makeText(ReservationHistoryActivity.this, "Failed to get reservation history", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
