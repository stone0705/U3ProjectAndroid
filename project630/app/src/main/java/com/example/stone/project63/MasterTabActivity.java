package com.example.stone.project63;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MasterTabActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    CoordinatorLayout coord;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    static boolean ishorizon = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mastertab);
        coord = (CoordinatorLayout)findViewById(R.id.main_content);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        final ViewPagerAdapter vpa = new ViewPagerAdapter(getSupportFragmentManager());
        MasterRecycler r1 = MasterRecycler.newInstance(1);
        MasterRecycler r2 = MasterRecycler.newInstance(2);
        MasterRecycler r3 = MasterRecycler.newInstance(3);
        MasterRecycler r4 = MasterRecycler.newInstance(4);
        MasterRecycler r5 = MasterRecycler.newInstance(5);
        vpa.add(r1, "筆記");
        vpa.add(r2,"會議");
        vpa.add(r3,"投票");
        vpa.add(r4, "行事曆");
        vpa.add(r5, "設定");
        viewPager.setAdapter(vpa);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        //選單可滾動模式 預設固定
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        coord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case(R.id.nav_createGroup):{
                        Intent intent = new Intent();
                        intent.setClass(MasterTabActivity.this,CreateGroupActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case(R.id.nav_findGroup):{
                        Intent intent = new Intent();
                        intent.setClass(MasterTabActivity.this,FindGroupActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case(R.id.nav_switchGroup):{
                        Intent intent = new Intent();
                        intent.setClass(MasterTabActivity.this,SelectGroupActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case(R.id.nav_managerGroup):{
                        Intent intent = new Intent();
                        intent.setClass(MasterTabActivity.this,ManagementGroupActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case(R.id.nav_checkMember):{
                        Intent intent = new Intent();
                        intent.setClass(MasterTabActivity.this,GetNotJoinListActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case(R.id.nav_createMeeting):{
                        Intent intent = new Intent();
                        intent.setClass(MasterTabActivity.this,CreateMeetingActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case(R.id.nav_createVote):{
                        Intent intent = new Intent();
                        intent.setClass(MasterTabActivity.this,CreateVoteActivity.class);
                        startActivity(intent);
                    }
                }
                menuItem.setChecked(false);
                drawerLayout.closeDrawers();
                return false;
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final AlertDialog alertDialog = new AlertDialog.Builder(MasterTabActivity.this).create();
            alertDialog.setTitle("登出");
            alertDialog.setMessage("確定要登出嗎");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"確定",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MasterTabActivity.this, MainActivity.class);
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
}
