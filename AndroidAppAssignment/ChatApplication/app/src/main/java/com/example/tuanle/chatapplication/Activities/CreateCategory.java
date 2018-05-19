package com.example.tuanle.chatapplication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tuanle.chatapplication.R;
import com.example.tuanle.chatapplication.Response.CreateCategoryResponse;
import com.example.tuanle.chatapplication.Retrofit.ApiUtils;
import com.example.tuanle.chatapplication.Retrofit.SOService;
import com.example.tuanle.chatapplication.Utils.Constants.ExtraKey;
import com.example.tuanle.chatapplication.Utils.PreferenceUtils;
import com.example.tuanle.chatapplication.Utils.ValidationUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        final EditText edt_catName = findViewById(R.id.edt_catName);
        final EditText edt_catDescript = findViewById(R.id.edt_catDescript);

        findViewById(R.id.btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidationUtils.isValidCategories(edt_catName.getText().toString(),edt_catDescript.getText().toString()) == 1){
                    //TODO--HUY
                    SOService mSOService;
                    mSOService = ApiUtils.getSOService();
                    mSOService.createCategory(edt_catName.getText().toString(),edt_catDescript.getText().toString()).enqueue(new Callback<CreateCategoryResponse>() {
                        @Override
                        public void onResponse(Call<CreateCategoryResponse> call, Response<CreateCategoryResponse> response) {
                            PreferenceUtils.saveStringPref(getBaseContext(), ExtraKey.USER_CAT,response.body().getCat_id()+"");

                            Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<CreateCategoryResponse> call, Throwable t) {

                        }
                    });
                }
                else {
                    if (ValidationUtils.isValidCategories(edt_catName.getText().toString(),edt_catDescript.getText().toString()) == 2)
                        Toast.makeText(getBaseContext(),"Category name must contain at least 4 characters including only letters and no numbers", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getBaseContext(),"Category description must contain at least 10 characters including letters and numbers only", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
