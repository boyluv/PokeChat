package com.example.tuanle.findjobsapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuanle.findjobsapplication.Response.JobDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultJobAdapter extends RecyclerView.Adapter<ResultJobAdapter.JobResultHolder>{
    private ArrayList<JobDetail> listJob;
    private Context context;
    private ArrayList<Integer> listFavorie;
    public ResultJobAdapter(Context context){
        listJob = new ArrayList<>();
        this.context = context;
        this.listFavorie = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String favorite = preferences.getString(context.getString(R.string.listFavorite), null);
        String[] parts = favorite.split(",");
        for(int i=0;i<parts.length;i++)
        {
            listFavorie.add(Integer.parseInt(parts[i]));
        }
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
    public void onBindViewHolder(@NonNull final JobResultHolder holder, final int position) {
        holder.title.setText(listJob.get(position).getTitle());
        holder.place.setText(listJob.get(position).getPlace());
        holder.id = Integer.parseInt(listJob.get(position).getId());

        if(listFavorie.contains(holder.id))
            holder.checkBoxFavorite.setChecked(true);
        else
            holder.checkBoxFavorite.setChecked(false);

        holder.checkBoxFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//                String favorite = preferences.getString(context.getString(R.string.listFavorite), null);

                if(((CheckBox)v).isChecked()){
                    listFavorie.add(Integer.parseInt(listJob.get(position).getId()));
//                    String[] parts = favorite.split(",");
//                    String temp = parts[0];
//                    for(int i=1;i<parts.length;i++)
//                    {
//                        if(!parts[i].equals(listJob.get(position).getId()))
//                        temp += "," +parts[i];
//                    }
////                    temp += "," + listJob.get(position).getId();
//                    favorite = temp;
                }
                else{
                    listFavorie.remove((Integer)Integer.parseInt(listJob.get(position).getId()));
//                    if(favorite!=null)
//                        favorite = favorite + "," + position;
//                    else
//                        favorite = ""+position;
                }
                String favorite = "";
                favorite += listFavorie.get(0);
                if (listFavorie.size()>1){
                    for(int i=1;i<listFavorie.size();i++){
                        favorite += "," + listFavorie.get(i);
                    }
                }

                SharedPreferences newPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = newPreferences.edit();
                editor.putString(context.getString(R.string.listFavorite),favorite);
                editor.commit();
            }
        });
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
        private CheckBox checkBoxFavorite;
        private Integer id;
        public JobResultHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.job_title);
            place = itemView.findViewById(R.id.job_location);
            img_job = itemView.findViewById(R.id.img_job);
            checkBoxFavorite = itemView.findViewById(R.id.job_bookmark);
        }
    }
}
