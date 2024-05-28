package com.example.finalproject.models;
import com.google.gson.annotations.SerializedName;

//object food with category object
public class FoodedHistory {
    @SerializedName("_id")
    private String id;
    @SerializedName("food")
    private Fooded food;

    public FoodedHistory(String id, Fooded food) {
        this.id = id;
        this.food = food;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Fooded getFood() {
        return food;
    }

    public void setFood(Fooded food) {
        this.food = food;
    }

    @Override
    public String toString() {
        return "FoodedHistory{" +
                "id='" + id + '\'' +
                ", fooded=" + food +
                '}';
    }
}
