package com.example.finalproject.activity.admin;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.AllFoodAdapter;
import com.example.finalproject.adapter.CategorySpinnerAdapter;
import com.example.finalproject.api.ApiService;
import com.example.finalproject.models.Category;
import com.example.finalproject.models.Food;
import com.example.finalproject.models.Fooded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodManagerActivity extends AppCompatActivity implements AllFoodAdapter.FoodClickListener {
    private RecyclerView recyclerView;
    private AllFoodAdapter foodAdapter;
    private List<Fooded> foodList;
    private ApiService apiService;
    private Button createButton;
    private List<Category> categoryList;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_food);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodList = new ArrayList<>();
        foodAdapter = new AllFoodAdapter(foodList, this);
        recyclerView.setAdapter(foodAdapter);

        apiService = ApiService.createApiService();

        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open create food dialog
                dialogCreateFood();
            }
        });

        categorySpinner = findViewById(R.id.categorySpinner);

        loadCategories();
        loadFoods();

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void dialogCreateFood() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_food);

        final EditText editName = dialog.findViewById(R.id.editName);
        final EditText editPrice = dialog.findViewById(R.id.editPrice);
        final EditText editDescription = dialog.findViewById(R.id.editDescription);
        Button buttonCreate = dialog.findViewById(R.id.buttonCreate);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        // Set up the category spinner
        Spinner categorySpinner = dialog.findViewById(R.id.categorySpinner);
        CategorySpinnerAdapter categoryAdapter = new CategorySpinnerAdapter(this, categoryList);
        categorySpinner.setAdapter(categoryAdapter);

        // set up image list
        final MultiAutoCompleteTextView editImageList = dialog.findViewById(R.id.editImageList);
        editImageList.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                String priceStr = editPrice.getText().toString().trim();
                String description = editDescription.getText().toString().trim();
                Category selectedCategory = (Category) categorySpinner.getSelectedItem();
                String imageListInput = editImageList.getText().toString().trim();
                List<String> imageList = Arrays.asList(imageListInput.split("\\s*,\\s*"));

                if (!name.isEmpty() && !priceStr.isEmpty() && !description.isEmpty()) {
                    double price = Double.parseDouble(priceStr);

                    Food food = new Food( name, selectedCategory.getId(), price, description, imageList);
                    createFood(food);
                    dialog.dismiss();
                } else {
                    Toast.makeText(FoodManagerActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void dialogUpdateFood(Fooded food) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_food);

        final EditText editName = dialog.findViewById(R.id.editName);
        final EditText editPrice = dialog.findViewById(R.id.editPrice);
        final EditText editDescription = dialog.findViewById(R.id.editDescription);
        Button buttonUpdate = dialog.findViewById(R.id.buttonCreate);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        editName.setText(food.getName());
        editPrice.setText(String.valueOf(food.getPrice()));
        editDescription.setText(food.getDescription());

        // Set up the category spinner
        Spinner categorySpinner = dialog.findViewById(R.id.categorySpinner);
        CategorySpinnerAdapter categoryAdapter = new CategorySpinnerAdapter(this, categoryList);
        categorySpinner.setAdapter(categoryAdapter);

        // Set the selected category in the spinner
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId().equals(food.getCategory().getId())) {
                categorySpinner.setSelection(i);
                break;
            }
        }

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                String priceStr = editPrice.getText().toString().trim();
                String description = editDescription.getText().toString().trim();
                Category selectedCategory = (Category) categorySpinner.getSelectedItem();

                if (!name.isEmpty() && !priceStr.isEmpty() && !description.isEmpty()) {
                    double price = Double.parseDouble(priceStr);

                    food.setName(name);
                    food.setPrice(price);
                    food.setDescription(description);
                    food.setCategory(selectedCategory);

                    updateFood(food);
                    dialog.dismiss();
                } else {
                    Toast.makeText(FoodManagerActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void loadFoods() {
        Call<List<Fooded>> call = apiService.getAllFoods();
        call.enqueue(new Callback<List<Fooded>>() {
            @Override
            public void onResponse(Call<List<Fooded>> call, Response<List<Fooded>> response) {
                if (response.isSuccessful()) {
                    List<Fooded> foods = response.body();
                    if (foods != null) {
                        foodList.clear();
                        foodList.addAll(foods);
                        foodAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Fooded>> call, Throwable t) {
                Toast.makeText(FoodManagerActivity.this, "Failed to load foods: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createFood(Food food) {
        Call<Food> call = apiService.addFood(food);
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful()) {
                    Food createdFood = response.body();
                    if (createdFood != null) {
                        foodAdapter.notifyDataSetChanged();
                        loadFoods();
                        Toast.makeText(FoodManagerActivity.this, "Food created successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FoodManagerActivity.this, "Failed to create food", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Toast.makeText(FoodManagerActivity.this, "Failed to create food: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFood(Fooded food) {
        Call<Food> call = apiService.updateFood(food.getId(), convertToFood(food));
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful()) {
                    foodAdapter.notifyDataSetChanged();
                    Toast.makeText(FoodManagerActivity.this, "Food updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FoodManagerActivity.this, "Failed to update food", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Toast.makeText(FoodManagerActivity.this, "Failed to update food: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFood(Fooded food) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this food?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> call = apiService.deleteFood(food.getId());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    foodList.remove(food);
                                    foodAdapter.notifyDataSetChanged();
                                    Toast.makeText(FoodManagerActivity.this, "Food deleted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FoodManagerActivity.this, "Failed to delete food", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(FoodManagerActivity.this, "Failed to delete food: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onUpdateClick(Fooded food) {
        // Open update food dialog
        dialogUpdateFood(food);
    }

    @Override
    public void onDeleteClick(Fooded food) {
        // Delete food
        deleteFood(food);
    }

    private void loadCategories() {
        Call<List<Category>> call = apiService.getAllCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body();
                    if (categories != null) {
                        categoryList = categories;
                        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(FoodManagerActivity.this, categories);
                        categorySpinner.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(FoodManagerActivity.this, "Failed to load categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Food convertToFood(Fooded fooded) {
        String id = fooded.getId();
        String name = fooded.getName();
        double price = fooded.getPrice();
        String description = fooded.getDescription();

        // Retrieve the Category object based on the categoryId
        String category = fooded.getCategory().getId();

        // Create a new Food object
        Food food = new Food(id, name, category, price, description, null);

        return food;
    }


}
