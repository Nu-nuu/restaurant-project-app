package com.example.finalproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardCategoryAdapter extends RecyclerView.Adapter<CardCategoryAdapter.CardCategoryViewHolder> {

    private List<Category> categoryList;
    private Context context;


    public CardCategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;

    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_category, parent, false);
        return new CardCategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardCategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        //set image
        String title = category.getTitle();
        String imageUrl = "android.resource://com.example.finalproject/drawable/" + title;
        Picasso.get().load(Uri.parse(imageUrl)).into(holder.categoryImageView);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CardCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImageView;
        public CardCategoryViewHolder(View itemView) {
            super(itemView);
            categoryImageView = itemView.findViewById(R.id.categoryImageView);

        }
    }
}
