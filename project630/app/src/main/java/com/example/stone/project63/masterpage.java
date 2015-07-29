package com.example.stone.project63;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;


public class masterpage extends Activity implements View.OnTouchListener {
    static HashMap Listmap = new HashMap<String,String[]>();
    static ArrayList<String> titleList = new ArrayList<String>();
    static boolean asyncfin = false;
    Intent intent;
    Button note;
    Button vote;
    Button meeting;
    Button schdule;
    Button setting;
    LinearLayout content;
    GestureDetector ges;
    ColorFilter cf;
    ColorFilter high = new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000);
    View set;
    int state = 5;
    SharedPreferences settings;
    final String STORE_NAME = "Settings";
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterpage);
        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        editor = settings.edit();
        LayoutInflater inflater =  getLayoutInflater();
        note = (Button)findViewById(R.id.note);
        vote = (Button)findViewById(R.id.vote);
        meeting = (Button)findViewById(R.id.chat);
        schdule = (Button)findViewById(R.id.sched);
        setting = (Button)findViewById(R.id.personal);
        set = inflater.inflate(R.layout.set,null);
        intent = new Intent();
        content = (LinearLayout)findViewById(R.id.contentlayout);
        content.setLongClickable(true);
        content.setOnTouchListener(this);
        cf = note.getBackground().getColorFilter();
        setting.getBackground().setColorFilter(high);
        setupWindowAnimations();
        listener();

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
    void listener(){

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state!=1){
                    state = 1;
                    change(state,"1070");
                }
            }
        });
        meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state != 2) {
                    state = 2;
                    change(state,"1071");
                }
            }
        });
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state != 3) {
                    state = 3;
                    change(state,"1072");
                }
            }
        });
        schdule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state!=4){
                    state = 4;
                    change(state,"1073");
                }
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state!=5){
                    state = 5;
                    change(state,"");
                }
            }
        });
    }
    void change(int state,String rule){
        asyncfin = false;
        setting.getBackground().setColorFilter(cf);
        note.getBackground().setColorFilter(cf);
        vote.getBackground().setColorFilter(cf);
        schdule.getBackground().setColorFilter(cf);
        meeting.getBackground().setColorFilter(cf);
        switch(state){
            case 1:{
                note.getBackground().setColorFilter(high);
                content.removeAllViews();
                intent.setClass(masterpage.this,innoteActivity.class);
                for(int i = 0;i<3;i++){
                    Button temp = new Button(masterpage.this);
                    temp.setText("note 1");
                    temp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });
                    content.addView(temp);
                }
                break;
            }
            case 2:{
                ProgressDialog dialog = ProgressDialog.show(masterpage.this,"註冊中", "請等待...", true);
                AsyncGetList sub = new AsyncGetList(dialog);
                sub.execute(rule,settings.getString("account",""),settings.getString("android_id",""),settings.getString("group",""),settings.getString("founder",""));
                while (!asyncfin){}
                meeting.getBackground().setColorFilter(high);
                content.removeAllViews();
                final Bundle bundle = new Bundle();
                for(int i = 0;i<titleList.size();i++){
                    String title = titleList.get(i);
                    final String[] divide = (String[]) Listmap.get(title);
                    final Button temp = new Button(masterpage.this);
                    temp.setText(title);
                    temp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editor.putString("meeting_id",divide[1]);
                            editor.putString("meeting_title",divide[2]);
                            editor.commit();
                            intent.setClass(masterpage.this,inmeetingActivity.class);
                            View sharedView = temp;
                            String transitionName = "temp";

                            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(masterpage.this, sharedView, "menu");
                            startActivity(intent, transitionActivityOptions.toBundle());
                            //startActivity(intent);
                        }
                    });
                    content.addView(temp);
                }
                break;
            }
            case 3:{
                vote.getBackground().setColorFilter(high);
                content.removeAllViews();
                intent.setClass(masterpage.this,invoteActivity.class);
                break;
            }
            case 4:{
                schdule.getBackground().setColorFilter(high);
                content.removeAllViews();
                intent.setClass(masterpage.this,inteamschduleActivity.class);
                break;
            }
            case 5:{
                setting.getBackground().setColorFilter(high);
                content.removeAllViews();
                content.addView(set);
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
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return this.ges.onTouchEvent(event);
    }

}
