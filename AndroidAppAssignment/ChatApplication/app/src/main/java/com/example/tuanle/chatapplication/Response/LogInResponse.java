package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tuanle on 3/4/18.
 */
public class LogInResponse extends BaseResponse<List<UserInfo>>{

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
