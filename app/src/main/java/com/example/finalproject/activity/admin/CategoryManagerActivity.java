    package com.example.finalproject.activity.admin;

    import android.app.AlertDialog;
    import android.app.Dialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.view.Window;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import com.example.finalproject.R;
    import com.example.finalproject.adapter.AllCategoryAdapter;
    import com.example.finalproject.api.ApiService;
    import com.example.finalproject.models.Category;
    import java.util.ArrayList;
    import java.util.List;
    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class CategoryManagerActivity extends AppCompatActivity implements AllCategoryAdapter.CategoryClickListener {
        private RecyclerView recyclerView;
        private AllCategoryAdapter categoryAdapter;
        private List<Category> categoryList;
        private ApiService apiService;
        private Button createButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_manager_category);

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            categoryList = new ArrayList<>();
            categoryAdapter = new AllCategoryAdapter(categoryList, this);
            recyclerView.setAdapter(categoryAdapter);

            apiService = ApiService.createApiService();

            createButton = findViewById(R.id.createButton);
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open create category dialog
                    dialogCreateCategory();
                }
            });

            loadCategories();

            Button buttonBack = findViewById(R.id.buttonBack);
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CategoryManagerActivity.this, AdminHomePageActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        private void dialogCreateCategory() {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_create_category);

            final EditText editTitle = dialog.findViewById(R.id.editTitle);
            Button buttonCreate = dialog.findViewById(R.id.buttonCreate);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = editTitle.getText().toString().trim();
                    if (!title.isEmpty()) {
                        Category category = new Category(title);
                        createCategory(category);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(CategoryManagerActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
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

        private void dialogUpdateCategory(Category category) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_update_category);

            final EditText editTitle = dialog.findViewById(R.id.editTitle);
            Button buttonUpdate = dialog.findViewById(R.id.buttonUpdate);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
            editTitle.setText(category.getTitle());
            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = editTitle.getText().toString().trim();
                    if (!title.isEmpty()) {
                        category.setTitle(title);
                        updateCategory(category);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(CategoryManagerActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
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

        private void dialogDeleteCategory(final Category category) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Category")
                    .setMessage("Are you sure you want to delete this Category?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCategory(category);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

        private void createCategory(Category category) {
            Call<Category> call = apiService.addCategory(category);
            call.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {
                    if (response.isSuccessful()) {
                        Category createdCategory = response.body();
                        if (createdCategory != null) {
                            categoryList.add(createdCategory);
                            categoryAdapter.notifyDataSetChanged();
                            Toast.makeText(CategoryManagerActivity.this, "Category created successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CategoryManagerActivity.this, "Failed to create category", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Category> call, Throwable t) {
                    Toast.makeText(CategoryManagerActivity.this, "Failed to create category", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void updateCategory(Category category) {
            Call<Category> call = apiService.updateCategory(category.getId(), category);
            call.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {
                    if (response.isSuccessful()) {
                        Category updatedCategory = response.body();
                        if (updatedCategory != null) {
                            int index = categoryList.indexOf(category);
                            if (index != -1) {
                                categoryList.set(index, updatedCategory);
                                categoryAdapter.notifyDataSetChanged();
                                Toast.makeText(CategoryManagerActivity.this, "Category updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(CategoryManagerActivity.this, "Failed to update category", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Category> call, Throwable t) {
                    Toast.makeText(CategoryManagerActivity.this, "Failed to update category", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void deleteCategory(final Category category) {
            Call<Void> call = apiService.deleteCategory(category.getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        categoryList.remove(category);
                        categoryAdapter.notifyDataSetChanged();
                        Toast.makeText(CategoryManagerActivity.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CategoryManagerActivity.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CategoryManagerActivity.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void loadCategories() {
            Call<List<Category>> call = apiService.getAllCategories();
            call.enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                    if (response.isSuccessful()) {
                        categoryList.clear();
                        categoryList.addAll(response.body());
                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CategoryManagerActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {
                    Toast.makeText(CategoryManagerActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            });
        }



        @Override
        public void onCategoryClick(Category category) {
            // Handle category item click event here
            Toast.makeText(this, "Clicked category: " + category.getTitle(), Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onUpdateClick(Category category) {
            // Handle update button click here
            dialogUpdateCategory(category);
        }

        @Override
        public void onDeleteClick(Category category) {
            // Handle delete button click here
            dialogDeleteCategory(category);
        }
    }
