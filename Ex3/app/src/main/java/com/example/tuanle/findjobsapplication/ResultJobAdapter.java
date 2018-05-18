package com.example.tuanle.findjobsapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuanle.findjobsapplication.Response.JobDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultJobAdapter extends RecyclerView.Adapter<ResultJobAdapter.JobResultHolder>{
    private ArrayList<JobDetail> listJob;
    public ResultJobAdapter(){
        listJob = new ArrayList<>();
    }

    public void setListJob(ArrayList<JobDetail> listJob) {
        this.listJob = listJob;
    }

    @NonNull
    @Override
    public JobResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobResultHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_result,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull JobResultHolder holder, int position) {
        holder.title.setText(listJob.get(position).getTitle());
        holder.place.setText(listJob.get(position).getPlace());
        Picasso.get().load(listJob.get(position).getImage()).into(holder.img_job);
    }

    @Override
    public int getItemCount() {
        return listJob.size();
    }

    public class JobResultHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView place;
        private ImageView img_job;
        public JobResultHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.job_title);
            place = itemView.findViewById(R.id.job_location);
            img_job = itemView.findViewById(R.id.img_job);
        }
    }
}
