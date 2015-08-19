package com.example.stone.project63;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LetHeInActivity extends Activity {
    TextView name;
    TextView nickname;
    TextView time;
    Button inin;
    SharedPreferences settings;
    final String STORE_NAME = "Settings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_let_he_in);
        name = (TextView)findViewById(R.id.memberName);
        nickname = (TextView)findViewById(R.id.memberNickName);
        time = (TextView)findViewById(R.id.memberEnterTime);
        inin = (Button)findViewById(R.id.inin);
        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        final Intent intent = getIntent();
        final memberNormalItem item = (memberNormalItem)intent.getSerializableExtra("member");
        name.setText("帳號："+item.name);
        nickname.setText("暱稱："+item.name);
        time.setText("申請時間："+item.entertime);
        inin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncLetHeIn action = new AsyncLetHeIn(LetHeInActivity.this);
                action.execute(settings.getString("account",""),settings.getString("android_id","")
                        ,settings.getString("group",""),settings.getString("founder",""),item.name);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_let_he_in, menu);
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
