package com.example.finalproject.models;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("_id")
    private String id;

    @SerializedName("title")
    private String title;


    public Category(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Category(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
