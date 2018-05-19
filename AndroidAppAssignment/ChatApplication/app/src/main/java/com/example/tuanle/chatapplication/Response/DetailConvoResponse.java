package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailConvoResponse extends BaseResponse<List<Message>>{

    @SerializedName("isEmpty")
    @Expose
    private boolean isEmpty;

    public boolean isEmpty() {
        return isEmpty;
    }

}
