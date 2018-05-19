package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ListAdminResponse {
    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("cat_name")
    @Expose
    private String cat_name;
    @SerializedName("cat_description")
    @Expose
    private String cat_description;

    public int getUser_id() {
        return user_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public String getCat_description() {
        return cat_description;
    }
}
