package com.example.hanh.ava_hackathon18.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerInsrtuct {
    @SerializedName("Instruct")
    @Expose
    private Instruct instruct;

    public Instruct getInstruct() {
        return instruct;
    }

    public void setInstruct(Instruct instruct) {
        this.instruct = instruct;
    }
}
