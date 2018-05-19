package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestDetail {

    @SerializedName("req_sender")
    @Expose
    private String req_sender;

    @SerializedName("req_receiver")
    @Expose
    private String req_receiver;

    @SerializedName("message")
    @Expose
    private String messagedata;

    public String getReq_sender() {
        return req_sender;
    }

    public void setReq_sender(String req_sender) {
        this.req_sender = req_sender;
    }

    public String getReq_receiver() {
        return req_receiver;
    }

    public void setReq_receiver(String req_receiver) {
        this.req_receiver = req_receiver;
    }

    public String getMessagedata() {
        return messagedata;
    }

    public void setMessagedata(String messagedata) {
        this.messagedata = messagedata;
    }
}
