package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListConvoResponse extends BaseResponse<List<ConvoResponse>>{

    @SerializedName("isSignin")
    @Expose
    private boolean isSignin;

    public boolean isSignin() {
        return isSignin;
    }

    public void setSignin(boolean signin) {
        isSignin = signin;
    }
}
