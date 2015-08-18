package com.example.stone.project63;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class FindGroupActivity extends Activity {
    public static findGroupAdapter mAdapter;
    RecyclerView mRecyclerView;
    ArrayList<findGroupItem> datalist;
    private RecyclerView.LayoutManager mLayoutManager;
    public static Handler mHandler = new Handler();
    Button findgroup;
    EditText findtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);
        datalist = new ArrayList<findGroupItem>();
        findgroup = (Button)findViewById(R.id.findbutton);
        findtext = (EditText)findViewById(R.id.findtext);
        mRecyclerView = (RecyclerView)findViewById(R.id.findGroup_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new findGroupAdapter(datalist,this,this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        findgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.removeAll();
                AsyncFindGroup action = new AsyncFindGroup(FindGroupActivity.this);
                action.execute(findtext.getText().toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_group, menu);
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
