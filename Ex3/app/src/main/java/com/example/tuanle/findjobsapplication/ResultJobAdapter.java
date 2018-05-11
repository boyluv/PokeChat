package com.example.tuanle.findjobsapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ResultJobAdapter extends RecyclerView.Adapter<ResultJobAdapter.JobResultHolder>{
    private ArrayList<String> listJob;
    public ResultJobAdapter(ArrayList<String> listJob){
        this.listJob = listJob;
    }

    @NonNull
    @Override
    public JobResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobResultHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_result,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull JobResultHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listJob.size();
    }

    public class JobResultHolder extends RecyclerView.ViewHolder{

        public JobResultHolder(View itemView) {
            super(itemView);
        }
    }
}
