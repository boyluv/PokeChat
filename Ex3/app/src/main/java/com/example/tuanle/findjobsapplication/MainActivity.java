package com.example.tuanle.findjobsapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.findjob_title));
        setSupportActionBar(myToolbar);
        title = (EditText) findViewById(R.id.main_job_title);
        Button btn_search = (Button) findViewById(R.id.main_find_job_button);
        btn_search.setOnClickListener(this);



//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(getString(R.string.listFavorite),"1,2,6");
//        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_favorite:

//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        String favorite = sharedPref.getString(getString(R.string.listFavorite), "");

                Toast.makeText(getBaseContext(),"This is favorite",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(),JobResultActivity.class);
                intent.putExtra(ExtraKey.TYPE_RESULT,EnumUtils.Favorite.toString());
                startActivity(intent);
                return true;

            case R.id.action_settings:
                Toast.makeText(getBaseContext(),"This is settings",Toast.LENGTH_SHORT).show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_find_job_button:
                Intent intent = new Intent(getBaseContext(),JobResultActivity.class);
                if(title.getText().toString().isEmpty() || title.getText().toString().equals(""))
                    intent.putExtra(ExtraKey.TYPE_RESULT,EnumUtils.AllJobs.toString());
                else{
                    intent.putExtra(ExtraKey.TYPE_RESULT,EnumUtils.FindJobs.toString());
                    intent.putExtra(ExtraKey.TITLE,title.getText().toString());
                }
                startActivity(intent);
                break;
                default:break;
        }
    }
}
