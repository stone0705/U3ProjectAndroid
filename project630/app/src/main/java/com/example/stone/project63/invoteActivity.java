package com.example.stone.project63;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class invoteActivity extends Activity {
    public static Handler mHandler = new Handler();
    RecyclerView mRecyclerView;
    TextView title,sts,ets,createman;
    Button voteButton;
    SharedPreferences settings;
    public static invoteAdapter mAdapter;
    public static ArrayList<invoteItem> datalist;
    final String STORE_NAME = "Settings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invote);
        title = (TextView)findViewById(R.id.title);
        sts = (TextView)findViewById(R.id.sts);
        ets = (TextView)findViewById(R.id.ets);
        createman = (TextView)findViewById(R.id.createman);
        voteButton = (Button)findViewById(R.id.vote);
        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        mRecyclerView = (RecyclerView)findViewById(R.id.invote_recycler_view);
        title.setText("投票主題："+settings.getString("vote_title",""));
        createman.setText("發起人："+settings.getString("vote_createman",""));
        sts.setText(settings.getString("vote_sts",""));
        ets.setText(settings.getString("vote_ets",""));
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncVote action = new AsyncVote(invoteActivity.this);
                if(mAdapter.lastChecked!=null){
                    action.execute(settings.getString("account",""),settings.getString("android_id",""),settings.getString("vote_id",""),datalist.get(mAdapter.lastCheckedPos).id);
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<invoteItem>();
        mAdapter = new invoteAdapter(datalist);
        mRecyclerView.setAdapter(mAdapter);
        AsyncEnterVote async = new AsyncEnterVote(invoteActivity.this);
        async.execute(settings.getString("account",""),settings.getString("android_id",""),settings.getString("vote_id",""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invote, menu);
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
