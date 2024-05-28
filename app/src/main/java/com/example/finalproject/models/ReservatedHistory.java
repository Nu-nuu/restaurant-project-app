package com.example.finalproject.models;
import com.google.gson.annotations.SerializedName;

//full string reservation
public class ReservatedHistory {
    @SerializedName("_id")
    private String id;
    private String reservation;

    public ReservatedHistory(String id, String reservation) {
        this.id = id;
        this.reservation = reservation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReservation() {
        return reservation;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    @Override
    public String toString() {
        return "ReservatedHistory{" +
                "id='" + id + '\'' +
                ", reservation='" + reservation + '\'' +
                '}';
    }
}
