package com.example.hanh.ava_hackathon18.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Instruct {

    @SerializedName("step")
    @Expose
    private List<Step> step = null;
    @SerializedName("jams")
    @Expose
    private List<Jam> jams = null;

    public List<Step> getStep() {
        return step;
    }

    public void setStep(List<Step> step) {
        this.step = step;
    }

    public List<Jam> getJams() {
        return jams;
    }

    public void setJams(List<Jam> jams) {
        this.jams = jams;
    }
}
