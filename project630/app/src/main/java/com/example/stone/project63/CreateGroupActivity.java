package com.example.stone.project63;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateGroupActivity extends Activity {
    Button create;
    EditText group;
    static String response = "";
    SharedPreferences settings;
    final String STORE_NAME = "Settings";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        create = (Button)findViewById(R.id.creategroup);
        group = (EditText)findViewById(R.id.groupname);
        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (group.getText().toString().equals("")) {
                    response = "請輸入完所有項目";
                    AlertDialog alertDialog = new AlertDialog.Builder(CreateGroupActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage(response);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    AsyncCreateGroup sub = new AsyncCreateGroup(CreateGroupActivity.this,settings);
                    sub.execute(settings.getString("account",""),settings.getString("android_id",""),group.getText().toString());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
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
