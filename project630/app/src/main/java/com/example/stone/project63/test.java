package com.example.stone.project63;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class test extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<String> datalist = new ArrayList<String>();
        datalist.add("eee");
        datalist.add("eeedd");
        datalist.add("fee");
        datalist.add("aaa");
        mAdapter = new RVAdapter(datalist);
        mRecyclerView.setAdapter(mAdapter);
        System.out.println(mAdapter.getItemCount());
        datalist.add("ssss");
        datalist.add("eee");
        datalist.add("eeedd");
        datalist.add("fee");
        datalist.add("aaa");
        datalist.add("eee");
        datalist.add("eeedd");
        datalist.add("fee");
        datalist.add("aaa");
        datalist.add("eee");
        datalist.add("eeedd");
        datalist.add("fee");
        datalist.add("aaa");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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
