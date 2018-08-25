package com.example.hanh.ava_hackathon18.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Map {


    @SerializedName("Local")
    @Expose
    private Local local;

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

}
