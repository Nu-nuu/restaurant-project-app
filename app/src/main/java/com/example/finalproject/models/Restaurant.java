package com.example.finalproject.models;

public class Restaurant {
    private String name;
    private String distance;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private String title;
    private String content;
    private String time;
    private String author;

    public Restaurant(String name, String distance, String imageUrl, double latitude, double longitude) {
        this.name = name;
        this.distance = distance;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Restaurant() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

