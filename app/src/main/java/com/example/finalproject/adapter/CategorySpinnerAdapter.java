package com.example.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproject.models.Category;

import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {
    private LayoutInflater inflater;

    public CategorySpinnerAdapter(@NonNull Context context, @NonNull List<Category> categoryList) {
        super(context, 0, categoryList);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        TextView textView = view.findViewById(android.R.id.text1);
        Category category = getItem(position);
        if (category != null) {
            textView.setText(category.getTitle());
        }
        return view;
    }
}
