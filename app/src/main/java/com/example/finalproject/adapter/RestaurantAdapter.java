package com.example.finalproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<Restaurant> restaurantList;
    private Context context;
    private RestaurantClickListener restaurantClickListener;

    public interface RestaurantClickListener {
        void onRestaurantClick(Restaurant restaurant);
    }

    public RestaurantAdapter(List<Restaurant> restaurantList, Context context, RestaurantClickListener restaurantClickListener) {
        this.restaurantList = restaurantList;
        this.context = context;
        this.restaurantClickListener = restaurantClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.textName.setText(restaurant.getName());
        holder.textDistance.setText(restaurant.getDistance());
        String imageRes = restaurant.getImageUrl();
        String imageUrl = "android.resource://com.example.finalproject/drawable/" + imageRes;
        // Load and display the image using Picasso
        Picasso.get().load(Uri.parse(imageUrl)).into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantClickListener.onRestaurantClick(restaurant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textName;
        private TextView textDistance;
        private ImageView imageView;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textDistance = itemView.findViewById(R.id.textDistance);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
