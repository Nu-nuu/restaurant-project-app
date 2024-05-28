package com.example.finalproject.models;
import com.google.gson.annotations.SerializedName;

//Reservation user String
public class ReservationHistory {
    @SerializedName("_id")
    private String id;
    @SerializedName("reservation")
    private Reservation reservation;

    public ReservationHistory(String id, Reservation reservation) {
        this.id = id;
        this.reservation = reservation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public String toString() {
        return "ReservationHistory{" +
                "id='" + id + '\'' +
                ", reservation=" + reservation +
                '}';
    }
}
