package com.example.tuanle.chatapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCategoryResponse extends BaseResponse<Object>{
//    @SerializedName("status")
//    @Expose
//    private String status;
//
//    @SerializedName("message")
//    @Expose
//    private String message;

    @SerializedName("cat_id")
    @Expose
    private int cat_id;

//    public String getStatus() {
//        return status;
//    }
//
//    public String getMessage() {
//        return message;
//    }

    public int getCat_id() {
        return cat_id;
    }
}
