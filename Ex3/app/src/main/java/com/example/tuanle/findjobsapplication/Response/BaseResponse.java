package com.example.tuanle.findjobsapplication.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BaseResponse {
    @SerializedName("jobs")
    @Expose
    private ArrayList<JobDetail> jobs;

    public ArrayList<JobDetail> getJobs() {
        return jobs;
    }
}
