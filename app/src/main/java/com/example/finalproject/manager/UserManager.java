package com.example.finalproject.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.finalproject.models.User;

public class
UserManager {
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ROLE = "role";

    private Context context;
    private SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_USERNAME);
    }

    public void saveCurrentUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putInt(KEY_PHONE, user.getPhone());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_FULLNAME, user.getFullname());
        editor.putString(KEY_ADDRESS, user.getAddress());
        editor.putString(KEY_ROLE, user.getRole());
        editor.apply();

        // Log the user properties after saving
        Log.d("UserManager", "Saved User: " + user.toString());
    }

    public User getCurrentUser() {
        String id = sharedPreferences.getString(KEY_USER_ID, "");
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        String password = sharedPreferences.getString(KEY_PASSWORD, "");
        int phone = sharedPreferences.getInt(KEY_PHONE, 0);
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String fullname = sharedPreferences.getString(KEY_FULLNAME, "");
        String address = sharedPreferences.getString(KEY_ADDRESS, "");
        String role = sharedPreferences.getString(KEY_ROLE, "");

        User user = new User(id, username, password, phone, email, fullname, address, role);

        // Log the user properties after retrieving
        Log.d("UserManager", "Retrieved User: " + user.toString());

        return user;
    }

    public void clearCurrentUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_PHONE);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_FULLNAME);
        editor.remove(KEY_ADDRESS);
        editor.remove(KEY_ROLE);
        editor.apply();
    }

    // Other methods...

}
