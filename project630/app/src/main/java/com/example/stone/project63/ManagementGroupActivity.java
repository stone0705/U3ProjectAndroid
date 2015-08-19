package com.example.stone.project63;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class ManagementGroupActivity extends Activity {

    public static memberRightCardAdapter mAdapter;
    RecyclerView mRecyclerView;
    ArrayList<memberRightItem> datalist;
    private RecyclerView.LayoutManager mLayoutManager;
    public static Handler mHandler = new Handler();
    SharedPreferences settings;
    final String STORE_NAME = "Settings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_group);
        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        mRecyclerView = (RecyclerView)findViewById(R.id.management_recycler_view);
        datalist = new ArrayList<memberRightItem>();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new memberRightCardAdapter(datalist,this,this);
        mRecyclerView.setAdapter(mAdapter);
        AsyncGetJoinMember action = new AsyncGetJoinMember(this);
        action.execute(settings.getString("group",""),settings.getString("founder",""));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_management_group, menu);
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
