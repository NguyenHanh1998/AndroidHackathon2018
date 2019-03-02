package com.example.hanh.ava_hackathon18.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Jam {


    @SerializedName("start_location")
    @Expose
    private StartLocation startLocation;
    @SerializedName("end_location")
    @Expose
    private EndLocation endLocation;

    public StartLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(StartLocation startLocation) {
        this.startLocation = startLocation;
    }

    public EndLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation endLocation) {
        this.endLocation = endLocation;
    }

}
