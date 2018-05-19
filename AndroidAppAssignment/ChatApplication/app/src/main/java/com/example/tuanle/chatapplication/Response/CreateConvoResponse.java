package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateConvoResponse {
    @SerializedName("convo_id")
    @Expose
    private int convo_id;

    @SerializedName("convo_by")
    @Expose
    private int convo_by;

    @SerializedName("convo_cat")
    @Expose
    private int convo_cat;

    public int getConvo_id() {
        return convo_id;
    }

    public int getConvo_by() {
        return convo_by;
    }

    public int getConvo_cat() {
        return convo_cat;
    }
}
