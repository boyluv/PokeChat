package com.example.tuanle.chatapplication.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageRequest {

    @SerializedName("rep_message")
    @Expose
    private String rep_message;

    @SerializedName("ref_convo_id")
    @Expose
    private int ref_convo_id;

    @SerializedName("rep_by")
    @Expose
    private int rep_by;
}
