package com.example.tuanle.findjobsapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobDetail {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("place")
    @Expose
    private String place;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("description")
    @Expose
    private String description;

    public String getTitle() {
        return title;
    }

    public String getPlace() {
        return place;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
