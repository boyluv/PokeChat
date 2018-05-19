package com.example.tuanle.chatapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tuanle.chatapplication.Activities.DetailConservationActivity;
import com.example.tuanle.chatapplication.Algorithm.CrRSA;
import com.example.tuanle.chatapplication.R;
import com.example.tuanle.chatapplication.Response.BaseResponse;
import com.example.tuanle.chatapplication.Response.ConvoResponse;
import com.example.tuanle.chatapplication.Response.ListAdminResponse;
import com.example.tuanle.chatapplication.Response.RequestDetail;
import com.example.tuanle.chatapplication.Response.RootCheckConnectResponse;
import com.example.tuanle.chatapplication.Retrofit.ApiUtils;
import com.example.tuanle.chatapplication.Retrofit.SOService;
import com.example.tuanle.chatapplication.Utils.Constants.ExtraKey;
import com.example.tuanle.chatapplication.Utils.PreferenceUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConservationListAdapter extends RecyclerView.Adapter<ConservationListAdapter.ItemViewHolder>{
    private ArrayList<ConvoResponse> listConvo;
    private Context mContext;
    private boolean isUser;
    private ArrayList<ListAdminResponse> listAdminResponses;
    public ConservationListAdapter(Context context, ArrayList<ConvoResponse> mine, ArrayList<ListAdminResponse> listAdmin, boolean isUser) {
        mContext = context;
        listConvo = mine;
        this.isUser = isUser;
        listAdminResponses = listAdmin;
        //Log.d("listConvo", "Adapter" + listConvo.get(0).getRep_message());

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conservation, parent, false);
        return new ItemViewHolder(view);
    }

    private void sendRequestToChat(final int idAdmin){
        final SOService mService;
        mService = ApiUtils.getSOService();
        final int idUser = Integer.parseInt(PreferenceUtils.getStringPref(mContext,ExtraKey.USER_ID,"1"));

        //Check if already have conversation
        mService.checkConnect(idAdmin,idUser).enqueue(new Callback<RootCheckConnectResponse>() {
            @Override
            public void onResponse(Call<RootCheckConnectResponse> call, Response<RootCheckConnectResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().isHaveConnect())
                    {
                        Intent intent = new Intent(mContext, DetailConservationActivity.class);
                        Log.d("DetailConvo", "This is convo Id " + response.body().getData().get(0).getConvo_id());
                        intent.putExtra(ExtraKey.CONSERVATION_ID, response.body().getData().get(0).getConvo_id()+"");
                        mContext.startActivity(intent);
                    }
                    else {

                        //Create key between two user

                        //Get public key from another one
                        mService.getPbKey(idAdmin).enqueue(new Callback<BaseResponse<String>>() {
                            @Override
                            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                                Log.d("PbKey",response.body().getData());
                                String pbKey = response.body().getData();
                                String message="";
                                String keyAES = "YACATEAM";
                                try{
                                    message = CrRSA.encryptRSA(keyAES, pbKey);
                                }
                                catch (Exception e){
                                    Log.d("Encryt",e.toString());
                                }
                                //TODO how to get idUser,idAdmin
                                PreferenceUtils.saveStringPref(mContext,ExtraKey.KEY_AES,keyAES);

                                //TODO--Get Receiver Id
                                mService.sendRequest(idUser,idAdmin,"First_"+message).enqueue(new Callback<RequestDetail>() {
                                    @Override
                                    public void onResponse(Call<RequestDetail> call, Response<RequestDetail> response) {
                                        Log.d("Request","Send success");
                                    }

                                    @Override
                                    public void onFailure(Call<RequestDetail> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

                            }
                        });
                        //Get


                    }
                }
            }

            @Override
            public void onFailure(Call<RootCheckConnectResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        if(!isUser){
            final ConvoResponse curResponse = listConvo.get(position);
            holder.userName.setText("Last message conversation "+curResponse.getRef_convo_id()+" from "+curResponse.getUser_name());
            holder.lastChat.setText("Message :"+curResponse.getRep_message());
            holder.userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Start Activity to Detail Conservation
                    Intent intent = new Intent(mContext, DetailConservationActivity.class);
                    Log.d("DetailConvo", "This is convo Id " + curResponse.getRef_convo_id());
                    intent.putExtra(ExtraKey.CONSERVATION_ID, curResponse.getRef_convo_id());
                    mContext.startActivity(intent);
                }
            });
        }
        else {
            //For list admin do sth
            final  ListAdminResponse curResponse = listAdminResponses.get(position);
            holder.userName.setText("Category name :"+curResponse.getCat_name());
            holder.lastChat.setText(curResponse.getCat_description());
            holder.userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequestToChat(listAdminResponses.get(position).getUser_id());
//                    //Start Activity to Detail Conservation
//                    Intent intent = new Intent(mContext, DetailConservationActivity.class);
                    Log.d("ListAdmin", "Request to chat" );
                    //intent.putExtra(ExtraKey.CONSERVATION_ID, curResponse.getRef_convo_id());
                    //mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(!isUser)
            return listConvo.size();
        else
            return listAdminResponses.size();
    }

    public void setListConvo(ArrayList<ConvoResponse> listConvo) {
        this.listConvo = listConvo;
    }

    public void setListAdminResponses(ArrayList<ListAdminResponse> listAdminResponses) {
        this.listAdminResponses = listAdminResponses;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView userName;
        private TextView lastChat;
        public ItemViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_user_name);
            lastChat = itemView.findViewById(R.id.tv_last_message);
        }
    }
}
