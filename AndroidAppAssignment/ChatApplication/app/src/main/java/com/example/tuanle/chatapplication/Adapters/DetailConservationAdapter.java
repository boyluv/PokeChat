package com.example.tuanle.chatapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuanle.chatapplication.Algorithm.CrAES;
import com.example.tuanle.chatapplication.R;
import com.example.tuanle.chatapplication.Response.Message;
import com.example.tuanle.chatapplication.Utils.Constants.ExtraKey;
import com.example.tuanle.chatapplication.Utils.PreferenceUtils;

import java.util.ArrayList;

public class DetailConservationAdapter extends RecyclerView.Adapter<DetailConservationAdapter.ItemHolder>{
    private ArrayList<Message> conservation;
    private String userName;
    private Context context;
    public DetailConservationAdapter(Context context,ArrayList<Message> listMsg) {
        this.context = context;
        this.conservation = listMsg;
        userName = PreferenceUtils.getStringPref(context,ExtraKey.USER_NAME,null);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_conservation,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final Message curMessage = conservation.get(position);
        String encryptedMes = curMessage.getRep_message();
        String key = PreferenceUtils.getStringPref(context,ExtraKey.KEY_AES,"");
        String encrypted = "";
        try{
            encrypted= CrAES.decryptAES(key,encryptedMes);
        }
        catch (Exception e){
            Log.d("AES","Decrypt failed");
        }
        holder.message.setText(encrypted);

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UserName","CurUserName " + userName +" CurMessage  " + curMessage.getUser_name());
            }
        });

        if(curMessage.getUser_name().equals(userName))
        {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

// Add all the rules you need
            params.addRule(RelativeLayout.ALIGN_PARENT_END);

            holder.message.setLayoutParams(params);
            holder.message.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
        else {

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

// Add all the rules you need
                params.addRule(RelativeLayout.ALIGN_PARENT_START);

                holder.message.setLayoutParams(params);
            holder.message.setBackgroundColor(Color.parseColor("#dddddd"));
        }
    }

    @Override
    public int getItemCount() {
        return conservation.size();
    }

    public void setConservation(ArrayList<Message> conservation) {
        this.conservation = conservation;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private TextView message;
        public ItemHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.tv_message);
        }
    }
}
