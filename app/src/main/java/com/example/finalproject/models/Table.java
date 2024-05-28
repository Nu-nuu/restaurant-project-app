package com.example.finalproject.models;

import com.google.gson.annotations.SerializedName;

public class Table {
    @SerializedName("_id")
    private String id;

    @SerializedName("tableNumber")
    private int tableNumber;

    @SerializedName("capacity")
    private int capacity;

    @SerializedName("status")
    private String status;

    // Constructors, getters, and setters
    // ...

    public Table(String id, int tableNumber, int capacity, String status) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = status;
    }

    public Table() {
    }

    public Table(String status) {
        this.status = status;
    }

    public Table(int tableNumber, int capacity, String status) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Table{" +
                "id='" + id + '\'' +
                ", tableNumber=" + tableNumber +
                ", capacity=" + capacity +
                ", status='" + status + '\'' +
                '}';
    }
}
