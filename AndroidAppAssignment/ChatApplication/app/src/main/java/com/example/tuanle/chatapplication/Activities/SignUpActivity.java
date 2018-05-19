package com.example.tuanle.chatapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tuanle.chatapplication.Algorithm.CrDES;
import com.example.tuanle.chatapplication.Algorithm.CrRSA;
import com.example.tuanle.chatapplication.R;
import com.example.tuanle.chatapplication.Request.SignupRequest;
import com.example.tuanle.chatapplication.Response.BaseResponse;
import com.example.tuanle.chatapplication.Retrofit.ApiUtils;
import com.example.tuanle.chatapplication.Retrofit.SOService;
import com.example.tuanle.chatapplication.Utils.Constants.ExtraKey;
import com.example.tuanle.chatapplication.Utils.PreferenceUtils;
import com.example.tuanle.chatapplication.Utils.ValidationUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnSignUp;
    private EditText edtUserName;
    private EditText edtPass;
    private SOService mService;
    private String mKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnSignUp = findViewById(R.id.signup_btn);
        edtUserName = findViewById(R.id.edt_name);
        edtPass = findViewById(R.id.edt_password);
        mKey = null;
        getKey();
        btnSignUp.setOnClickListener(this);
    }
    private void getKey(){
        mService = ApiUtils.getSOService();
        mService.getKey().enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if(response.isSuccessful()){
                    mKey = response.body().getData();
                    Toast.makeText(getBaseContext(), mKey,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                Toast.makeText(getBaseContext(), mKey,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup_btn:
                if(ValidationUtils.isValidUser(edtUserName.getText().toString()) && ValidationUtils.isValidPassword(edtPass.getText().toString())){
                    //TODO -- Huy
                    if(mKey == null)
                        getKey();
                    mService = ApiUtils.getSOService();
                    //TODO-----Binh
                    String name = edtUserName.getText().toString();
                    String pass = edtPass.getText().toString();
                    //Use DES encrypt password
                    String encryptedPass = CrDES.encryptDES(mKey,pass);
                    String pbKey ="";
                    String pvKey ="";

                    //Start create RSA
                    try{
                        CrRSA.generateKey();
                        pbKey = CrRSA.getPublicKey(CrRSA.publicKey);
                        pvKey = CrRSA.getPrivateKey(CrRSA.privateKey);

                        //TODO -- Tuan --Save to server
                        Log.d("RSA","Public key: "+pbKey);
                        //TODO -- Tuan --Save sharepreference with AES encrypt
                        Log.d("RSA","Private key: "+pvKey);

                        PreferenceUtils.saveStringPref(getBaseContext(), ExtraKey.PB_KEY,pbKey);
                        PreferenceUtils.saveStringPref(getBaseContext(),ExtraKey.PV_KEY,pvKey);

                    }catch (Exception e){
                        Log.d("RSA",e.toString());
                    }
                    //End
                    int userCat = Integer.parseInt(PreferenceUtils.getStringPref(getBaseContext(),ExtraKey.USER_CAT,"1"));
                    //TODO--DONE -- Fix ref_cat_id for admin
                    mService.signUp(name,encryptedPass,pbKey,userCat).enqueue(new Callback<SignupRequest>() {
//                                    mService.signUp("Me","Me",pbKey,1).enqueue(new Callback<SignupRequest>() {

                        @Override
                        public void onResponse(Call<SignupRequest> call, Response<SignupRequest> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(getBaseContext(), "Sign up success ",
                                        Toast.LENGTH_SHORT).show();

                            }

                            else
                                Toast.makeText(getBaseContext(), "Sign up failed",
                                        Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<SignupRequest> call, Throwable t) {

                        }
                    });
                }
                else {
                    //Show warning
                    if(!ValidationUtils.isValidPassword(edtPass.getText().toString()))
                        Toast.makeText(getBaseContext(),"Password must contain 4 to 8 characters including letters and numbers only", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getBaseContext(),"Username must start with a letter including only letters and numbers", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                    break;
        }
    }
}
