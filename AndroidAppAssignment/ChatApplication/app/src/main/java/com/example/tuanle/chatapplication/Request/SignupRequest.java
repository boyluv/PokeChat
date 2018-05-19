package com.example.tuanle.chatapplication.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tuan Le on 7/26/2017.
 */

public class SignupRequest {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("pb_key")
    @Expose
    private String pb_key;

    @SerializedName("ref_cat_id")
    @Expose
    private int ref_cat_id;

    public SignupRequest(String name, String password, String pb_key, int ref_cat_id) {
        this.name = name;
        this.password = password;
        this.pb_key = pb_key;
        this.ref_cat_id = ref_cat_id;
    }
}
