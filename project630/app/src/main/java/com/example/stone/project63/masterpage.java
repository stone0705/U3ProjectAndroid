package com.example.stone.project63;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;



public class masterpage extends Activity implements View.OnTouchListener {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterpage);
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
        ges = new GestureDetector(this,new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e2.getX()-e1.getX()>70){
                    if(state!=1){
                        state--;
                        change(state);
                    }
                }
                if(e1.getX()-e2.getX()>70){
                    if(state!=5){
                        state++;
                        change(state);
                    }
                }
                return true;
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state!=1){
                    state = 1;
                    change(state);
                }
            }
        });
        meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state != 2) {
                    state = 2;
                    change(state);
                }
            }
        });
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state != 3) {
                    state = 3;
                    change(state);
                }
            }
        });
        schdule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state!=4){
                    state = 4;
                    change(state);
                }
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state!=5){
                    state = 5;
                    change(state);
                }
            }
        });
    }
    void change(int state){
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
                meeting.getBackground().setColorFilter(high);
                content.removeAllViews();
                intent.setClass(masterpage.this,inmeetingActivity.class);
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
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return this.ges.onTouchEvent(event);
    }

}
