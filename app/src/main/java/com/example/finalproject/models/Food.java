package com.example.finalproject.models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;
//Food have Category is String
public class Food {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private String category;

    @SerializedName("price")
    private double price;

    @SerializedName("description")
    private String description;

    @SerializedName("imageList")
    private List<String> imageList;

    // Constructors, getters, and setters
    // ...

    public Food(String id, String name, String category, double price, String description, List<String> imageList) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.imageList = imageList;
    }

    public Food(String name, String category, double price, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
    }

    public Food(String name, String category, double price, String description, List<String> imageList) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.imageList = imageList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imageList=" + imageList +
                '}';
    }
}
