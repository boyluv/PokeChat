package com.example.tuanle.findjobsapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tuanle.findjobsapplication.Response.BaseResponse;
import com.example.tuanle.findjobsapplication.Response.JobDetail;
import com.example.tuanle.findjobsapplication.Retrofit.ApiUtils;
import com.example.tuanle.findjobsapplication.Retrofit.SOService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobResultActivity extends AppCompatActivity {
    private SOService mSoService;
    private ArrayList<JobDetail> list;
    private ResultJobAdapter resultJobAdapter;
    private RecyclerView rv_result_jobs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_result);
        String type = getIntent().getStringExtra(ExtraKey.TYPE_RESULT);
        String title = type;
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        Toast.makeText(this,type,Toast.LENGTH_SHORT).show();
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        rv_result_jobs = (RecyclerView) findViewById(R.id.rv_job_result);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_result_jobs.setLayoutManager(layoutManager);
        resultJobAdapter = new ResultJobAdapter();
        list = new ArrayList<JobDetail>();
        mSoService = ApiUtils.getSOService();
        switch (type){
            case "AllJobs":
                mSoService.getAllJobs().enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        resultJobAdapter.setListJob(response.body().getJobs());
                        resultJobAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                    }
                });
                break;
            case "Favorite":
                break;
            case "FindJobs":
                mSoService.getJobsWithTitle(getIntent().getStringExtra(ExtraKey.TITLE)).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        resultJobAdapter.setListJob(response.body().getJobs());
                        resultJobAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                    }
                });
                break;

        }
        rv_result_jobs.setAdapter(resultJobAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
