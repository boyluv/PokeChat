package com.example.tuanle.findjobsapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class JobResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_result);
        String type = getIntent().getStringExtra(ExtraKey.TYPE_RESULT);
        String title = type;
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        RecyclerView rv_result_jobs = (RecyclerView) findViewById(R.id.rv_job_result);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_result_jobs.setLayoutManager(layoutManager);

        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");


        ResultJobAdapter resultJobAdapter = new ResultJobAdapter(list);
        rv_result_jobs.setAdapter(resultJobAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
