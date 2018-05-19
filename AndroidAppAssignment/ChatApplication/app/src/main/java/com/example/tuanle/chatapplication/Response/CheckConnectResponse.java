package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckConnectResponse {
    @SerializedName("convo_id")
    @Expose
    private int convo_id;

    public int getConvo_id() {
        return convo_id;
    }
}
