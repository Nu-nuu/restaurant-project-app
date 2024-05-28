package com.example.finalproject.models;
import com.google.gson.annotations.SerializedName;
import java.util.List;

//full string reservation history
public class Usered {
    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("phone")
    private int phone;

    @SerializedName("email")
    private String email;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("address")
    private String address;

    @SerializedName("role")
    private String role;
    @SerializedName("reservationHistory")
    private List<ReservatedHistory> reservationHistory;

    public Usered(String id, String username, String password, int phone, String email, String fullname, String address, String role, List<ReservatedHistory> reservationHistory) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.fullname = fullname;
        this.address = address;
        this.role = role;
        this.reservationHistory = reservationHistory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<ReservatedHistory> getReservationHistory() {
        return reservationHistory;
    }

    public void setReservationHistory(List<ReservatedHistory> reservationHistory) {
        this.reservationHistory = reservationHistory;
    }

    @Override
    public String toString() {
        return "Usered{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", fullname='" + fullname + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", reservationHistory=" + reservationHistory +
                '}';
    }
}
