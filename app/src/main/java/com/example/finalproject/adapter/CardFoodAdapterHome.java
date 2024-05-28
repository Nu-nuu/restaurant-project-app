package com.example.finalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.activity.LoginActivity;
import com.example.finalproject.models.Fooded;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardFoodAdapterHome extends RecyclerView.Adapter<CardFoodAdapterHome.CardFoodViewHolder> {

    private List<Fooded> foodList;
    private Context context;

    public CardFoodAdapterHome(List<Fooded> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
    }

    public void setFoodList(List<Fooded> foodList) {
        this.foodList = foodList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_food, parent, false);
        return new CardFoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardFoodViewHolder holder, int position) {
        Fooded fooded = foodList.get(position);

        holder.textName.setText(fooded.getName());

        // Set image
        if (fooded.getImageList() != null && !fooded.getImageList().isEmpty()) {
            String firstImage = fooded.getImageList().get(0);
            String imageUrl = "android.resource://com.example.finalproject/drawable/" + firstImage;
            Picasso.get().load(Uri.parse(imageUrl)).into(holder.foodImageView);
        } else {
            // If imageList is empty or null, you can set a default image
            holder.foodImageView.setImageResource(R.drawable.chuoi1);
        }

        //Price
        double price = fooded.getPrice();
        int roundedPrice = (int) price;
        String formattedPrice = String.format("%,d đ", roundedPrice);
        holder.textPrice.setText(formattedPrice);

        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                // Optionally, you can finish the current activity after starting the new one
                // ((YourActivity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class CardFoodViewHolder extends RecyclerView.ViewHolder {
        TextView textPrice, textName;
        ImageButton orderButton;
        ImageView foodImageView;


        public CardFoodViewHolder(View itemView) {
            super(itemView);
            textPrice = itemView.findViewById(R.id.textPrice);
            textName = itemView.findViewById(R.id.textName);
            orderButton = itemView.findViewById(R.id.orderButton);
            foodImageView = itemView.findViewById(R.id.foodImageView);

        }
    }
}
