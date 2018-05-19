package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RootCheckConnectResponse extends BaseResponse<List<CheckConnectResponse>>{

    @SerializedName("haveConnect")
    @Expose
    private boolean haveConnect;

    public boolean isHaveConnect() {
        return haveConnect;
    }
}
