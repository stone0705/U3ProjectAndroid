package com.example.stone.project63;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class submit extends Activity {
    Button submit;
    EditText account;
    EditText password;
    EditText nickname;
    static boolean pass;
    static String response = "";
    static boolean asyncfin = false;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        submit = (Button)findViewById(R.id.submitbutton);
        account = (EditText)findViewById(R.id.account);
        password = (EditText)findViewById(R.id.password);
        nickname =(EditText)findViewById(R.id.nickname);
        final AlertDialog alertDialog = new AlertDialog.Builder(submit.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(pass){
                            intent = new Intent();
                            intent.setClass(submit.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncfin = false;
                if(account.getText().toString().equals("") || password.getText().toString().equals("") || nickname.getText().toString().equals("")){
                    response = "請輸入完所有項目";
                    pass = false;
                }else{
                    ProgressDialog dialog = ProgressDialog.show(submit.this,"註冊中", "請等待...", true);
                    AsyncSubmitAction sub = new AsyncSubmitAction(dialog);
                    sub.execute(account.getText().toString(),password.getText().toString(),nickname.getText().toString());
                    System.out.println("1111");
                    while (!asyncfin){}
                    System.out.println("2222");
                }
                alertDialog.setMessage(response);
                alertDialog.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit, menu);
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
}
