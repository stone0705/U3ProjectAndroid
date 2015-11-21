package com.example.stone.project63;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class firstActivity extends Activity {

    final String STORE_NAME = "Settings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        final SharedPreferences settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        String account = settings.getString("account", "");
        Intent intent = new Intent();
        if(account.equals("")){
            intent.setClass(this,MainActivity.class);
            startActivity(intent);
        }else{
            intent.setClass(this,MasterTabActivity.class);
            startActivity(intent);
        }
    }

}
