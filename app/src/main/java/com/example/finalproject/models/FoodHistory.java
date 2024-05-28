package com.example.finalproject.models;
import com.google.gson.annotations.SerializedName;

//object food with category string
public class FoodHistory {
    @SerializedName("_id")
    private String id;
    @SerializedName("food")
    private Food food;

    public FoodHistory(String id, Food food) {
        this.id = id;
        this.food = food;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    @Override
    public String toString() {
        return "FoodHistory{" +
                "id='" + id + '\'' +
                ", food=" + food +
                '}';
    }
}
