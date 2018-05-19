package com.example.tuanle.chatapplication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tuanle.chatapplication.Algorithm.CrAES;
import com.example.tuanle.chatapplication.Algorithm.CrDES;
import com.example.tuanle.chatapplication.R;
import com.example.tuanle.chatapplication.Response.BaseResponse;
import com.example.tuanle.chatapplication.Response.LogInResponse;
import com.example.tuanle.chatapplication.Retrofit.ApiUtils;
import com.example.tuanle.chatapplication.Retrofit.SOService;
import com.example.tuanle.chatapplication.Utils.Constants.ExtraKey;
import com.example.tuanle.chatapplication.Utils.PreferenceUtils;
import com.example.tuanle.chatapplication.Utils.ValidationUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_signIn;
    private Button btn_signUp;
    private SOService mService;
    private EditText userName;
    private EditText password;
    private String mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try{
            String mes = "Hello World";
            String key = "YACATEAM";
            Log.d("AES","Begin");
            String encrypt = CrAES.encryptAES(key,mes);
            Log.d("AES","Encrypt " + encrypt);
            String decrypted= CrAES.decryptAES(key,encrypt);
            Log.d("AES","Dencrypt " + decrypted);


        }
        catch (Exception e){
            Log.d("AES","Decrypt failed");
        }

        mKey = null;
        getKey();

        //String userId = PreferenceUtils.getStringPref(getBaseContext(),ExtraKey.USER_ID,null);
        userName = findViewById(R.id.edt_name);
        password = findViewById(R.id.edt_password);

        btn_signIn = findViewById(R.id.signin_btn);
        btn_signUp = findViewById(R.id.signup_btn);
        btn_signIn.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);
        //If already Log in change to inside
//        if(userId !=null){
//            showListConservation();
//        }
        PreferenceUtils.saveStringPref(getBaseContext(),ExtraKey.USER_CAT,"1");

        findViewById(R.id.signup_admin_btn).setOnClickListener(this);
    }
    private void signUp() {
        Log.d("bug","sign up");

        Intent intent = new Intent(this, SignUpActivity.class);
//        send Intent with message if you need
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private void showListConservation(){
        Intent intent = new Intent(getBaseContext(), ConservationListActivity.class);
        startActivity(intent);
    }

    private void getKey(){
        mService = ApiUtils.getSOService();
        mService.getKey().enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if(response.isSuccessful()){
                    //TODO--Binh this is Key
                    mKey = response.body().getData();
//                    Toast.makeText(getBaseContext(), mKey,
//                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup_btn:
                signUp();
                break;
            case R.id.signin_btn:
                if (mKey == null)
                    getKey();
                else if(ValidationUtils.isValidUser(userName.getText().toString()) && ValidationUtils.isValidPassword(password.getText().toString()))
                    {
                        String encryptedPass = CrDES.encryptDES(mKey,password.getText().toString());
                        mService = ApiUtils.getSOService();
                        mService.logIn(userName.getText().toString(),encryptedPass).enqueue(new Callback<LogInResponse>() {
                            @Override
                            public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                                if(response.isSuccessful()) {
                                    if(response.body().isSignin()){
                                        try{
                                            Log.d("UserLogin", response.body().getData().get(0).getUser_id());
                                            PreferenceUtils.saveStringPref(getBaseContext(),ExtraKey.USER_ID,response.body().getData().get(0).getUser_id());
                                            PreferenceUtils.saveStringPref(getBaseContext(),ExtraKey.USER_CAT,response.body().getData().get(0).getRef_cat_id()+"");
                                            PreferenceUtils.saveStringPref(getBaseContext(),ExtraKey.USER_NAME,userName.getText().toString());

                                            //Start new Activity , change to list conservation
                                            showListConservation();
                                        }
                                        catch (NullPointerException e){
                                            Toast.makeText(getBaseContext(), "Log in failed",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d("UserLogin", "Null add ress");

                                        }
                                    }
                                    else{
                                        Log.e("UserLogin","Wrong user or password");
                                        Toast.makeText(getBaseContext(), "Wrong username or password",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                    Log.d("UserLogin",response.toString());
                            }

                            @Override
                            public void onFailure(Call<LogInResponse> call, Throwable t) {
                                Log.d("UserLogin","Log in failed");
                            }
                        });
                    }
                break;
            case R.id.signup_admin_btn:
                Intent intent = new Intent(this, CreateCategory.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceUtils.saveStringPref(getBaseContext(),ExtraKey.USER_CAT,"1");

    }
}
