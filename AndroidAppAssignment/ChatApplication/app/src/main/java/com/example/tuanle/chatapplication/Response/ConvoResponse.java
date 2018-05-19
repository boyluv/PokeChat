package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConvoResponse {
    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("rep_message")
    @Expose
    private String rep_message;

    @SerializedName("convo_id")
    @Expose
    private String ref_convo_id;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRep_message() {
        return rep_message;
    }

    public void setRep_message(String rep_message) {
        this.rep_message = rep_message;
    }

    public String getRef_convo_id() {
        return ref_convo_id;
    }

    public void setRef_convo_id(String related_to_convo) {
        this.ref_convo_id = related_to_convo;
    }
}
