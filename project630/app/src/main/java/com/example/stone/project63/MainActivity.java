package com.example.stone.project63;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {
    Button login;
    Button newuser;
    Intent intent;
    EditText account;
    EditText password;
    public static boolean pass;
    public static boolean asyncfin;
    final String STORE_NAME = "Settings";
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        account = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);
        intent = new Intent();
        SharedPreferences settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        editor = settings.edit();
        login = (Button)findViewById(R.id.login);
        newuser = (Button)findViewById(R.id.newuser);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = false;
                asyncfin = false;
                ProgressDialog dialog = ProgressDialog.show(MainActivity.this,"讀取中", "請等待3秒...", true);
                AsyncLoginAction logina = new AsyncLoginAction(dialog);
                logina.execute(account.getText().toString(),password.getText().toString());
                while (!asyncfin){}
                System.out.println("main  "+pass);
                if(pass){
                    editor.putString("account",account.getText().toString());
                    String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    editor.putString("android_id",androidId);
                    editor.commit();
                    intent.setClass(MainActivity.this, masterpage.class);
                    startActivity(intent);
                }
            }
        });
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MainActivity.this,submit.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(MainActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
