package com.example.hanh.ava_hackathon18.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Step {
    @SerializedName("start_location")
    @Expose
    private StartLocation startLocation;
    @SerializedName("end_location")
    @Expose
    private EndLocation endLocation;

    public StartLocation getStartLocation() {
        return startLocation;
    }


//
//    public  Step(JSONObject jsonObject) {
//        parseStartLocation(jsonObject.optJSONObject("start_location"));
//        parseEndLocation(jsonObject.optJSONObject("end_location"));
//    }

    private void parseEndLocation(JSONObject jsonObject) {
        if(jsonObject == null)
            return;
        endLocation = new EndLocation(jsonObject);
    }

    private void parseStartLocation(JSONObject jsonObject) {
        if(jsonObject == null)
            return;
        startLocation = new StartLocation(jsonObject);
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
