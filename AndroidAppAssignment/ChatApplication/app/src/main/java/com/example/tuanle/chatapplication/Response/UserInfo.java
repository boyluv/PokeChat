package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("ref_cat_id")
    @Expose
    private int ref_cat_id;

    public String getUser_id() {
        return user_id;
    }

    public int getRef_cat_id() {
        return ref_cat_id;
    }
}
