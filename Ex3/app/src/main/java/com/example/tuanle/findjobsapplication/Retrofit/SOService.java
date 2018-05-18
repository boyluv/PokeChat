package com.example.tuanle.findjobsapplication.Retrofit;

import com.example.tuanle.findjobsapplication.Response.BaseResponse;
import com.example.tuanle.findjobsapplication.Response.JobDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tuanle on 3/4/18.
 */

public interface SOService {
    @GET("/ex3/jobs/all")
    Call<BaseResponse> getAllJobs();

    @GET("/ex3/jobs")
    Call<BaseResponse> getJobsWithTitle(@Query("title") String kwTitle);

    @GET("/ex3/jobs/favorite/")
    Call<BaseResponse> getFavoriteJob(@Query("myarray") String listFavorite);
}
