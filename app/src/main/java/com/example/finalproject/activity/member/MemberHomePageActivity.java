package com.example.finalproject.activity.member;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.CardCategoryAdapter;
import com.example.finalproject.adapter.CardFoodAdapter;
import com.example.finalproject.adapter.RestaurantAdapter;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.manager.UserManager;
import com.example.finalproject.models.Category;
import com.example.finalproject.models.Fooded;
import com.example.finalproject.models.Restaurant;
import com.example.finalproject.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberHomePageActivity extends AppCompatActivity implements RestaurantAdapter.RestaurantClickListener {
    private UserManager userManager;

    private ImageButton profileButton;
    private Button createReservationButton;
    private TextView usernameTextView;

    private RestaurantAdapter restaurantAdapter;
    private CardFoodAdapter cardFoodAdapter;
    private CardCategoryAdapter cardCategoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_home_page);

        userManager = new UserManager(this);
        User currentUser = userManager.getCurrentUser();

        usernameTextView = findViewById(R.id.usernameTextView);

        profileButton = findViewById(R.id.profileButton);
        createReservationButton = findViewById(R.id.createReservationButton);

        usernameTextView.setText(currentUser.getFullname()); // Replace with actual username

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberHomePageActivity.this, ProfileMemberActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Initialize the RecyclerView and RestaurantAdapter for restaurants
        restaurantAdapter = new RestaurantAdapter(new ArrayList<>(), this, this);
        RecyclerView recyclerViewRestaurant = findViewById(R.id.recyclerViewRestaurant);
        LinearLayoutManager layoutManagerRestaurant = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRestaurant.setLayoutManager(layoutManagerRestaurant);
        recyclerViewRestaurant.setAdapter(restaurantAdapter);

        // Initialize the RecyclerView and CardFoodAdapter for food
        cardFoodAdapter = new CardFoodAdapter(new ArrayList<>(), this);
        RecyclerView recyclerViewFood = findViewById(R.id.recyclerViewFood);
        LinearLayoutManager layoutManagerFood = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFood.setLayoutManager(layoutManagerFood);
        recyclerViewFood.setAdapter(cardFoodAdapter);

        // Initialize the RecyclerView and CardCategoryAdapter for Category
        cardCategoryAdapter = new CardCategoryAdapter(new ArrayList<>(), this);
        RecyclerView recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        LinearLayoutManager layoutManagerCategory = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManagerCategory);
        recyclerViewCategory.setAdapter(cardCategoryAdapter);

        // Call the methods to fetch the data
        fetchRestaurantData();
        fetchFoodData();
        fetchCategoryData();

        createReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberHomePageActivity.this, CreateReservationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void fetchRestaurantData() {
        List<Restaurant> restaurantList = createRestaurantData();
        restaurantAdapter.setRestaurantList(restaurantList);
    }

    private List<Restaurant> createRestaurantData() {
        List<Restaurant> restaurantList = new ArrayList<>();

        // Restaurant 1
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("MATSUMOTO");
        restaurant1.setDistance("Distance: 2Km");
        restaurant1.setLatitude(10.882269948265545);
        restaurant1.setLongitude(106.78251843884901);
        restaurant1.setImageUrl("restaurant1");
        restaurantList.add(restaurant1);


        // Restaurant 2
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("NADRI Restaurante");
        restaurant2.setDistance("Distance: 5Km");
        restaurant2.setLatitude(10.8786289);
        restaurant2.setLongitude(106.7997928);
        restaurant2.setImageUrl("restaurant2");
        restaurantList.add(restaurant2);

        // Restaurant 3
        Restaurant restaurant3 = new Restaurant();
        restaurant3.setName("CareFree Restaurant");
        restaurant3.setDistance("Distance: 3Km");
        restaurant3.setLatitude(10.8786289);
        restaurant3.setLongitude(106.7997928);
        restaurant3.setImageUrl("restaurant3");
        restaurantList.add(restaurant3);

        return restaurantList;
    }

    private void fetchFoodData() {
        ApiService apiService = ApiService.createApiService();
        Call<List<Fooded>> foodListCall = apiService.getAllFoods();
        foodListCall.enqueue(new Callback<List<Fooded>>() {
            @Override
            public void onResponse(Call<List<Fooded>> call, Response<List<Fooded>> response) {
                if (response.isSuccessful()) {
                    List<Fooded> foodList = response.body();
                    if (foodList != null) {
                        cardFoodAdapter.setFoodList(foodList);
                    }
                } else {
                    Toast.makeText(MemberHomePageActivity.this, "Failed to retrieve food list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Fooded>> call, Throwable t) {
                Log.e("API Error", "Failed to retrieve food list", t);
                Toast.makeText(MemberHomePageActivity.this, "Failed to retrieve food list", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchCategoryData() {
        ApiService apiService = ApiService.createApiService();
        Call<List<Category>> call = apiService.getAllCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categoryList = response.body();
                    if (categoryList != null) {
                        cardCategoryAdapter.setCategoryList(categoryList);
                    }
                } else {
                    Toast.makeText(MemberHomePageActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(MemberHomePageActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchMapRestaurantActivity(Restaurant restaurant) {
        Intent intent = new Intent(MemberHomePageActivity.this, MapRestaurantActivity.class);
        intent.putExtra("name", restaurant.getName());
        intent.putExtra("image", restaurant.getImageUrl());
        intent.putExtra("distance", restaurant.getDistance());
        intent.putExtra("latitude", restaurant.getLatitude());
        intent.putExtra("longitude", restaurant.getLongitude());
        startActivity(intent);
    }

    @Override
    public void onRestaurantClick(Restaurant restaurant) {
        launchMapRestaurantActivity(restaurant);
    }
}
