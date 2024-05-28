package com.example.finalproject.models;
import com.google.gson.annotations.SerializedName;
import java.util.List;

//Reservated have User is Object
public class Reservated {

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
        private Usered usered;

        @SerializedName("foods")
        private List<FoodedHistory> foods;

        @SerializedName("table")
        private Table table;

    public Reservated(String id, int guestsNumber, String startDate, String endDate, String status, Usered usered, List<FoodedHistory> foods, Table table) {
        this.id = id;
        this.guestsNumber = guestsNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.usered = usered;
        this.foods = foods;
        this.table = table;
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

    public Usered getUsered() {
        return usered;
    }

    public void setUsered(Usered usered) {
        this.usered = usered;
    }

    public List<FoodedHistory> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodedHistory> foods) {
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
        return "Reservated{" +
                "id='" + id + '\'' +
                ", guestsNumber=" + guestsNumber +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", status='" + status + '\'' +
                ", usered=" + usered +
                ", foods=" + foods +
                ", table=" + table +
                '}';
    }
}
