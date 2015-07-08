package com.example.stone.project63;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
    Intent intent;
    EditText account;
    EditText password;
    public static int result;
    public static boolean asyncfin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        account = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);
        intent = new Intent();
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = 0;
                asyncfin = false;
                ProgressDialog dialog = ProgressDialog.show(MainActivity.this,"讀取中", "請等待3秒...", true);
                AsyncLoginAction logina = new AsyncLoginAction(dialog);
                logina.execute(account.getText().toString(),password.getText().toString());
                while (!asyncfin){}
                System.out.println("main  "+result);
                if(result == 1){
                    intent.setClass(MainActivity.this,masterpage.class);
                    startActivity(intent);
                }
                /*
                logina = new loginaction(account.getText().toString(),password.getText().toString());
                Thread thread = new Thread(logina);
                thread.start();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(3000);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        finally{
                            dialog.dismiss();
                        }
                    }
                }).start();
                while(!logina.finish){}
                logina.finish = false;
                intent.setClass(MainActivity.this,masterpage.class);
                System.out.println("main"+logina.pass);
                if(logina.pass){
                    logina.pass = false;
                    startActivity(intent);
                }
                */
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
