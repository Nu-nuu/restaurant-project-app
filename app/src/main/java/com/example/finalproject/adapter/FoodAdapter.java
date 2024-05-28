package com.example.finalproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Fooded;
import com.example.finalproject.models.FoodesHistory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends ArrayAdapter<Fooded> {
    private List<Fooded> foodList;
    private SparseBooleanArray selectedItems;

    public FoodAdapter(Context context, List<Fooded> foodList) {
        super(context, 0, foodList);
        this.foodList = foodList;
        this.selectedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_food, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.minusButton = convertView.findViewById(R.id.minusButton);
            holder.plusButton = convertView.findViewById(R.id.plusButton);
            holder.quantityTextView = convertView.findViewById(R.id.quantityTextView);
            holder.priceTextView = convertView.findViewById(R.id.priceTextView);
            holder.categoryTextView = convertView.findViewById(R.id.categoryTextView);
            holder.descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
            holder.imageRecyclerView = convertView.findViewById(R.id.imageRecyclerView);
            holder.imageRecyclerView.setLayoutManager(new LinearLayoutManager(convertView.getContext(), LinearLayoutManager.HORIZONTAL, false));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final Fooded foodItem = foodList.get(position);
        final int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
        holder.nameTextView.setText(foodItem.getName());
        holder.quantityTextView.setText(String.valueOf(quantity));
        holder.categoryTextView.setText("Category: " + foodItem.getCategory().getTitle());
        holder.descriptionTextView.setText("Description: " + foodItem.getDescription());

        //Price
        double price = foodItem.getPrice();
        int roundedPrice = (int) price;
        String formattedPrice = String.format("%,d đ", roundedPrice);
        holder.priceTextView.setText(formattedPrice);

        // Bind images
        ImageAdapter imageAdapter = new ImageAdapter(foodItem.getImageList());
        holder.imageRecyclerView.setAdapter(imageAdapter);

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
                if (quantity > 0) {
                    quantity--;
                    holder.quantityTextView.setText(String.valueOf(quantity));
                    updateSelection(position, quantity > 0);
                }
            }
        });

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
                quantity++;
                holder.quantityTextView.setText(String.valueOf(quantity));
                updateSelection(position, quantity > 0);
            }
        });

        updateSelection(position, quantity > 0);

        return convertView;
    }
    private void updateSelection(int position, boolean isSelected) {
        if (isSelected) {
            selectedItems.put(position, true);
        } else {
            selectedItems.delete(position);
        }
    }

    public List<FoodesHistory> getSelectedFoodIds() {
        List<FoodesHistory> selectedFoodIds = new ArrayList<>();

        for (int i = 0; i < selectedItems.size(); i++) {
            int position = selectedItems.keyAt(i);
            Fooded foodItem = foodList.get(position);

            // Create a FoodesHistory object with the food ID and add it to the list
            FoodesHistory foodesHistory = new FoodesHistory();
            foodesHistory.setFood(foodItem.getId());
            selectedFoodIds.add(foodesHistory);
        }

        return selectedFoodIds;
    }




    private static class ViewHolder {
        TextView nameTextView, priceTextView, categoryTextView, descriptionTextView;
        Button minusButton, plusButton;
        TextView quantityTextView;
        RecyclerView imageRecyclerView;
    }

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
        private List<String> imageList;

        public ImageAdapter(List<String> imageList) {
            this.imageList = imageList;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_food, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            String imageName = imageList.get(position);
            holder.bind(imageName);
        }

        @Override
        public int getItemCount() {
            return imageList != null ? imageList.size() : 0;
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }

            public void bind(String imageName) {
                // Load the image using Picasso
                String imageUrl = "android.resource://com.example.finalproject/drawable/" + imageName;
                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);
            }
        }
    }
}



