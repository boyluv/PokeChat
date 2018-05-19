package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestCommingResponse extends BaseResponse<List<RequestDetail>>{

    @SerializedName("haveNotification")
    @Expose
    private boolean haveNotification;

    public boolean isHaveNotification() {
        return haveNotification;
    }

    public void setHaveNotification(boolean haveNotification) {
        this.haveNotification = haveNotification;
    }

}
