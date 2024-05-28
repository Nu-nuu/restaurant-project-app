package com.example.finalproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;
    private ApiService apiService;

    public AllUserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
        apiService = ApiService.createApiService();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_manager, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.usernameTextView.setText("Username: " + user.getUsername());
        holder.emailTextView.setText("Email: " + user.getEmail());
        holder.phoneTextView.setText("Phone (+84): " + user.getPhone());
        holder.fullNameTextView.setText("Full Name: " + user.getFullname());
        holder.addressTextView.setText("Address: " + user.getAddress());
        holder.roleTextView.setText("Role: " + user.getRole());

        holder.buttonDelete.setOnClickListener(v -> {
            String userId = user.getId();
            deleteUser(userId);
        });
    }
    private void deleteUser(String userId) {
        Call<Void> call = apiService.deleteUser(userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // User deleted successfully
                    // Reset the UI by getting the updated list of users
                    getAllUsers();
                } else {
                    // Handle error response
                    Log.e("AllUserAdapter", "Failed to delete user: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Log.e("AllUserAdapter", "Error deleting user: " + t.getMessage());
            }
        });
    }

    private void getAllUsers() {
        Call<List<User>> call = apiService.getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                   userList = response.body();
                   notifyDataSetChanged();
                    // Update the adapter with the new user list
                } else {
                    Log.e("AllUserAdapter", "Failed to get users: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("AllUserAdapter", "Error getting users: " + t.getMessage());
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, emailTextView, phoneTextView;
        TextView fullNameTextView, addressTextView, roleTextView;
        Button buttonDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            // You can also set additional attributes or listeners for the views if needed
        }
    }
}

