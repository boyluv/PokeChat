package com.example.tuanle.chatapplication.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tuanle.chatapplication.Adapters.DetailConservationAdapter;
import com.example.tuanle.chatapplication.Algorithm.CrAES;
import com.example.tuanle.chatapplication.Algorithm.CrRSA;
import com.example.tuanle.chatapplication.R;
import com.example.tuanle.chatapplication.Request.MessageRequest;
import com.example.tuanle.chatapplication.Response.DetailConvoResponse;
import com.example.tuanle.chatapplication.Response.Message;
import com.example.tuanle.chatapplication.Retrofit.ApiUtils;
import com.example.tuanle.chatapplication.Retrofit.SOService;
import com.example.tuanle.chatapplication.Utils.Constants.ExtraKey;
import com.example.tuanle.chatapplication.Utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailConservationActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private DetailConservationAdapter mAdapter;
    private SOService mService;
    private ImageButton imgBtnSend;
    private EditText mMessage;
    private int curConvoId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_conservation);
        mRecyclerView = findViewById(R.id.rv_conservation);
        imgBtnSend = findViewById(R.id.imgbtn_send);
        mMessage = findViewById(R.id.edt_message);
        imgBtnSend.setOnClickListener(this);

        curConvoId = Integer.parseInt(getIntent().getStringExtra(ExtraKey.CONSERVATION_ID));
        userId = Integer.parseInt(PreferenceUtils.getStringPref(getBaseContext(),ExtraKey.USER_ID,"-1"));

        Log.d("DetailConvo", "This is convo Id " + curConvoId);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setStackFromEnd(true); //always show list from bottom
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new DetailConservationAdapter(getBaseContext(),new ArrayList<Message>());
        mRecyclerView.setAdapter(mAdapter);

//        loadMessage();

        //GET Key
        //PreferenceUtils.saveStringPref(mContext,""+userId+"_"+idAdmin,keyAES);

        //String keyAES = PreferenceUtils.getStringPref(getBaseContext(),)
//        try{
//            CrRSA.generateKey();
//            String a = CrRSA.getPublicKey(CrRSA.publicKey);
//            String b = CrRSA.getPrivateKey(CrRSA.privateKey);
//
//
//            //TODO -- Tuan --Save to server
//            Log.d("KeyCryp Public","Pub a: "+a);
//            //TODO -- Tuan --Save sharepreference with AES encrypt
//            Log.d("KeyCryp Private","Pri b: "+b);
//
//
//            //Sender
//            String msg = "hello, Doraemon";
//            //TODO -- Tuan --Get public key a from server
//            String e = CrRSA.encryptRSA(msg, a);
//            Log.d("Encrypted = ",e);
//
//
//            //TODO--Receiver private key b from mobile with AES decrypt
//            String d = CrRSA.decryptRSA(e, b);
//            Log.d("Decrypted = ", d);
//
//        }
//        catch (Exception e){
//            Log.d("Encryt",e.toString());
//        }

        new ListenConversation().execute("","","");

    }
    private Boolean isHaveNotification;
    private class ListenConversation extends AsyncTask<String, String, String> {
        protected String doInBackground(String... strings) {
            isHaveNotification = false;
            Log.d("Request","Start");

            mService = ApiUtils.getSOService();
            //This is your id
            int idUser = Integer.parseInt(PreferenceUtils.getStringPref(getBaseContext(),ExtraKey.USER_ID,"1"));
            mService.getDetailConvo(curConvoId).enqueue(new Callback<DetailConvoResponse>() {
                @Override
                public void onResponse(Call<DetailConvoResponse> call, Response<DetailConvoResponse> response) {
                    if(response.isSuccessful()) {
                        if(response.body().isEmpty()){
                            Log.d("listConvo", "Get Inside");
                        }
                        else {
                            Log.d("listConvo", response.body().getData().get(0).getUser_name());
                            mAdapter.setConservation(new ArrayList<Message>(response.body().getData()));
                            mAdapter.notifyDataSetChanged();
                        }
                        new ListenConversation().execute("","","");

                    }
                }

                @Override
                public void onFailure(Call<DetailConvoResponse> call, Throwable t) {

                }
            });
            Log.d("Request","End");
            return null;
            //else
            //    return null;
        }

        protected void onProgressUpdate(String... String) {

        }

        protected void onPostExecute(String message) {

        }

    }


    private void loadMessage(){
        mService = ApiUtils.getSOService();
        mService.getDetailConvo(curConvoId).enqueue(new Callback<DetailConvoResponse>() {
            @Override
            public void onResponse(Call<DetailConvoResponse> call, Response<DetailConvoResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().isEmpty()){
                        Log.d("listConvo", "Get Inside");
                    }
                    else {
                        Log.d("listConvo", response.body().getData().get(0).getUser_name());
                        mAdapter.setConservation(new ArrayList<Message>(response.body().getData()));
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailConvoResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgbtn_send:
//                mAdapter.setConservation(new ArrayList<Message>());
//                mAdapter.notifyDataSetChanged();
                final String mes = mMessage.getText().toString();
                String dencrypted = "";
                String key = PreferenceUtils.getStringPref(getBaseContext(),ExtraKey.KEY_AES,"");
                try{
                    dencrypted = CrAES.encryptAES(key,mes);

                }
                catch (Exception e){
                    Log.d("AES","Decrypt failed");
                }

                mService =ApiUtils.getSOService();
                //TODO------Update and remove id Message
                mService.addMessage(dencrypted ,curConvoId,userId).enqueue(new Callback<MessageRequest>() {
                    @Override
                    public void onResponse(Call<MessageRequest> call, Response<MessageRequest> response) {
                        if(response.isSuccessful()){
//                            Toast.makeText(getBaseContext(), "Send success " + encrypted,
//                                    Toast.LENGTH_SHORT).show();
                            mMessage.setText("");
                            loadMessage();
                        }

                        else
                            Toast.makeText(getBaseContext(), "Send failed",
                                    Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MessageRequest> call, Throwable t) {

                    }
                });
                break;
            default:
                break;
        }
    }
}
