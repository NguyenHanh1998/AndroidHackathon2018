package com.example.hanh.ava_hackathon18.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class EndLocation {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    public EndLocation(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public EndLocation(JSONObject jsonObject) {
        lat = jsonObject.optDouble("lat");
        lng = jsonObject.optDouble("lng");
    }
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }


}
