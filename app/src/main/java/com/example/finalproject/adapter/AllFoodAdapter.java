package com.example.finalproject.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Fooded;

import java.util.List;

public class AllFoodAdapter extends RecyclerView.Adapter<AllFoodAdapter.FoodViewHolder> {
    private List<Fooded> foodList;
    private FoodClickListener foodClickListener;

    public AllFoodAdapter(List<Fooded> foodList, FoodClickListener foodClickListener) {
        this.foodList = foodList;
        this.foodClickListener = foodClickListener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_manager, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Fooded food = foodList.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, categoryTextView, priceTextView, descriptionTextView;

        private ImageButton updateButton, deleteButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Fooded food = foodList.get(position);
                        foodClickListener.onUpdateClick(food);
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Fooded food = foodList.get(position);
                        foodClickListener.onDeleteClick(food);
                    }
                }
            });
        }

        public void bind(Fooded food) {
            nameTextView.setText(food.getName());
            categoryTextView.setText(food.getCategory().getTitle());
            priceTextView.setText(String.valueOf(food.getPrice()));
            descriptionTextView.setText(food.getDescription());
        }
    }

    public interface FoodClickListener {
        void onUpdateClick(Fooded food);

        void onDeleteClick(Fooded food);
    }
}
