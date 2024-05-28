package com.example.finalproject.models;
import com.google.gson.annotations.SerializedName;

//food full string
public class FoodsHistory {
    @SerializedName("_id")
    private String id;
    @SerializedName("food")
    private String food;

    public FoodsHistory(String id, String food) {
        this.id = id;
        this.food = food;
    }

    public FoodsHistory() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    @Override
    public String toString() {
        return "FoodsHistory{" +
                "id='" + id + '\'' +
                ", food='" + food + '\'' +
                '}';
    }
}
