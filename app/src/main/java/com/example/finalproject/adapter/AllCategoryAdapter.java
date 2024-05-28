package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.models.Category;
import java.util.List;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    private CategoryClickListener categoryClickListener;

    public AllCategoryAdapter(List<Category> categoryList, CategoryClickListener categoryClickListener) {
        this.categoryList = categoryList;
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_manager, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleTextView;
        private ImageButton updateButton;
        private ImageButton deleteButton;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(this);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Category category = categoryList.get(position);
                        categoryClickListener.onUpdateClick(category);
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Category category = categoryList.get(position);
                        categoryClickListener.onDeleteClick(category);
                    }
                }
            });
        }

        void bind(Category category) {
            titleTextView.setText(category.getTitle());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Category category = categoryList.get(position);
                categoryClickListener.onCategoryClick(category);
            }
        }
    }

    public interface CategoryClickListener {
        void onCategoryClick(Category category);
        void onUpdateClick(Category category);
        void onDeleteClick(Category category);
    }
}
