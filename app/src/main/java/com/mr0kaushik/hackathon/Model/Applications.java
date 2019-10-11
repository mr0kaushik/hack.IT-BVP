package com.mr0kaushik.hackathon.Model;

public class Applications {
    private String category;
    private String date;
    private String img_url;
    private String description;
    private double lat;
    private double lng;
    private String user_id;

    public Applications() {
    }

    public Applications(String category, String date, String img_url, String description, double lat, double lng, String user_id) {
        super();
        this.category = category;
        this.date = date;
        this.img_url = img_url;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.user_id = user_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
