package com.example.hanh.ava_hackathon18.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Local {
    @SerializedName("start_location")
    @Expose
    private List<StartLocation> startLocation = null;
    @SerializedName("end_location")
    @Expose
    private List<EndLocation> endLocation = null;

    public List<StartLocation> getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(List<StartLocation> startLocation) {
        this.startLocation = startLocation;
    }

    public List<EndLocation> getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(List<EndLocation> endLocation) {
        this.endLocation = endLocation;
    }


}
