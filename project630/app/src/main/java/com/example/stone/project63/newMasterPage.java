package com.example.stone.project63;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class newMasterPage extends Activity {
    Intent intent;
    Button note;
    Button vote;
    Button meeting;
    Button schdule;
    Button setting;
    ColorFilter cf;
    ColorFilter high = new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000);
    int state = 5;
    SharedPreferences settings;
    final String STORE_NAME = "Settings";
    SharedPreferences.Editor editor;
    private RecyclerView.LayoutManager mLayoutManager;
    public static RecyclerView mRecyclerView;
    public static masterAdapter mAdapter;
    ArrayList<masterItem> datalist;
    public static Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_master_page);
        mRecyclerView = (RecyclerView)findViewById(R.id.master_recycler_view);
        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        editor = settings.edit();
        LayoutInflater inflater =  getLayoutInflater();
        note = (Button)findViewById(R.id.note);
        vote = (Button)findViewById(R.id.vote);
        meeting = (Button)findViewById(R.id.chat);
        schdule = (Button)findViewById(R.id.sched);
        setting = (Button)findViewById(R.id.personal);
        cf = note.getBackground().getColorFilter();
        setting.getBackground().setColorFilter(high);
        datalist = new ArrayList<masterItem>();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new masterAdapter(datalist,this,settings,this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        setupWindowAnimations();
        listener();
        change(5, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_masterpage, menu);
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final AlertDialog alertDialog = new AlertDialog.Builder(newMasterPage.this).create();
            alertDialog.setTitle("登出");
            alertDialog.setMessage("確定要登出嗎");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"確定",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(newMasterPage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"取消",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();

            //當返回按鈕，按下所需要去觸發的動作，如:彈跳出對話視窗

        }
        return false;
    }
    void listener(){
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state != 1) {
                    state = 1;
                    change(state, "1070");
                }
            }
        });
        meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state != 2) {
                    state = 2;
                    change(state, "1071");
                }
            }
        });
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state != 3) {
                    state = 3;
                    change(state, "1072");
                }
            }
        });
        schdule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state != 4) {
                    state = 4;
                    change(state, "1073");
                }
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state != 5) {
                    state = 5;
                    change(state, "");
                }
            }
        });
    }
    void change(int state,String rule){
        setting.getBackground().setColorFilter(cf);
        note.getBackground().setColorFilter(cf);
        vote.getBackground().setColorFilter(cf);
        schdule.getBackground().setColorFilter(cf);
        meeting.getBackground().setColorFilter(cf);
        intent = new Intent();
        mAdapter.removeAll();
        switch(state){
            case 1:{
                note.getBackground().setColorFilter(high);
                break;
            }
            case 2:{
                meeting.getBackground().setColorFilter(high);
                newAsyncGetList async = new newAsyncGetList(newMasterPage.this);
                async.execute(rule,settings.getString("account",""),settings.getString("android_id",""),settings.getString("group",""),settings.getString("founder",""));
                break;
            }
            case 3:{
                vote.getBackground().setColorFilter(high);
                break;
            }
            case 4:{
                schdule.getBackground().setColorFilter(high);
                break;
            }
            case 5:{
                setting.getBackground().setColorFilter(high);
                mAdapter.additem(new masterItem("","創建群組","",false,createGroupActivity.class));
                mAdapter.additem(new masterItem("","尋找群組","",false,FindGroupActivity.class));
                mAdapter.additem(new masterItem("","切換群組","",false,SelectGroupActivity.class));
                mAdapter.additem(new masterItem("","管理群組","",false,ManagementGroupActivity.class));
                mAdapter.additem(new masterItem("","審核成員","",false,GetNotJoinListActivity.class));
                break;
            }
        }
    }
    private void setupWindowAnimations() {
        Explode explode = new Explode();
        explode.setDuration(2000);
        getWindow().setExitTransition(explode);

        Fade fade = new Fade();
        getWindow().setReenterTransition(fade);
    }
}
