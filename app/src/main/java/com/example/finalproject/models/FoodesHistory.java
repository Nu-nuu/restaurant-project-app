package com.example.finalproject.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodesHistory {
    @SerializedName("food")
    private String food;

    public FoodesHistory(String food) {
        this.food = food;
    }

    public FoodesHistory() {

    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    @Override
    public String   toString() {
        return "{" +
                "food='" + food + '\'' +
                '}';
    }
}
