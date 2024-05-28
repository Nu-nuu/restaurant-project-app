package com.example.finalproject.models;
import com.google.gson.annotations.SerializedName;
import java.util.List;

//Food have Category is Object
public class Fooded {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private Category category;

    @SerializedName("price")
    private double price;

    @SerializedName("description")
    private String description;

    @SerializedName("imageList")
    private List<String> imageList;

    private boolean isSelected;

    public Fooded(String id, String name, Category category, double price, String description, List<String> imageList) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.imageList = imageList;
    }

    public Fooded(String id, String name, Category category, double price, String description, List<String> imageList, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.imageList = imageList;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
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
        return "Fooded{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imageList=" + imageList +
                '}';
    }
}
