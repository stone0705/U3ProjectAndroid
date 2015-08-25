package com.example.stone.project63;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;


public class ChangeRightActivity extends Activity {
    TextView name;
    TextView nickname;
    TextView enterTime;
    CheckBox addRight;
    CheckBox removeRight;
    CheckBox noteRight;
    CheckBox meetingRight;
    CheckBox voteRight;
    CheckBox schRight;
    RadioButton isFounder;
    Button change;
    SharedPreferences settings;
    final String STORE_NAME = "Settings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_right);
        name = (TextView)findViewById(R.id.memberName);
        nickname = (TextView)findViewById(R.id.memberNickName);
        enterTime = (TextView)findViewById(R.id.memberEnterTime);
        addRight = (CheckBox)findViewById(R.id.addRight);
        removeRight = (CheckBox)findViewById(R.id.removeRight);
        noteRight = (CheckBox)findViewById(R.id.noteRight);
        meetingRight = (CheckBox)findViewById(R.id.meetingRight);
        voteRight = (CheckBox)findViewById(R.id.voteRight);
        schRight = (CheckBox)findViewById(R.id.schRight);
        isFounder = (RadioButton)findViewById(R.id.isFounder);
        change = (Button) findViewById(R.id.change);
        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        final Intent intent = getIntent();
        final memberRightItem item = (memberRightItem)intent.getSerializableExtra("member");
        name.setText("帳號："+item.name);
        nickname.setText("暱稱："+item.nickname);
        enterTime.setText("加入時間："+item.enterTime);
        addRight.setChecked(item.addRight);
        removeRight.setChecked(item.removeRight);
        noteRight.setChecked(item.noteRight);
        meetingRight.setChecked(item.meetingRight);
        voteRight.setChecked(item.voteRight);
        schRight.setChecked(item.schRight);
        isFounder.setChecked(item.isFounder);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean add = addRight.isChecked();
                Boolean remove = removeRight.isChecked();
                Boolean note = noteRight.isChecked();
                Boolean meeting = meetingRight.isChecked();
                Boolean vote = voteRight.isChecked();
                Boolean sch = schRight.isChecked();
                AsyncChangeRight action = new AsyncChangeRight(ChangeRightActivity.this);
                action.execute(settings.getString("account",""),settings.getString("android_id",""),settings.getString("group",""),settings.getString("founder","")
                        ,item.name,add.toString(),remove.toString(),note.toString(),meeting.toString(),vote.toString(),sch.toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_right, menu);
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
