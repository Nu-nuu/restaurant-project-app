package com.example.finalproject.models;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

//Reservation have User is String
public class Reservation {
    @SerializedName("_id")
    private String id;

    @SerializedName("guestsNumber")
    private int guestsNumber;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("status")
    private String status;

    @SerializedName("user")
    private String user;

    @SerializedName("foods")
    private List<FoodHistory> foods;

    @SerializedName("table")
    private Table table;

    // Constructors, getters, and setters
    // ...


    public Reservation(String id, int guestsNumber, String startDate, String endDate, String status, String user, List<FoodHistory> foods, Table table) {
        this.id = id;
        this.guestsNumber = guestsNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.user = user;
        this.foods = foods;
        this.table = table;
    }

    public Reservation(int guestsNumber, String startDate, String endDate, String status) {
        this.guestsNumber = guestsNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Reservation() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGuestsNumber() {
        return guestsNumber;
    }

    public void setGuestsNumber(int guestsNumber) {
        this.guestsNumber = guestsNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<FoodHistory> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodHistory> foods) {
        this.foods = foods;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", guestsNumber=" + guestsNumber +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", status='" + status + '\'' +
                ", user=" + user +
                ", foods=" + foods +
                ", table=" + table +
                '}';
    }
}
