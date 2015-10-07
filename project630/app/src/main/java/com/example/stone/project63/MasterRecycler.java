package com.example.stone.project63;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MasterRecycler extends Fragment {
        masterAdapter mAdapter;
    ArrayList<masterItem> datalset = new ArrayList<masterItem>();
    Handler mHandler = new Handler();
    private int type;
    SharedPreferences settings;
    final String STORE_NAME = "Settings";
    public static MasterRecycler newInstance(int type) {
        MasterRecycler fragment = new MasterRecycler();
        Bundle args = new Bundle();
        args.putInt("type",type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_master_recycler, container, false);
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.master_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        RecyclerView.ItemAnimator ia = new DefaultItemAnimator();
        ia.setAddDuration(200);
        ia.setRemoveDuration(200);
        recyclerView.setItemAnimator(ia);
        settings = recyclerView.getContext().getSharedPreferences(STORE_NAME, recyclerView.getContext().MODE_PRIVATE);
        String androidId = Settings.Secure.getString(recyclerView.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        mAdapter = new masterAdapter(datalset,recyclerView.getContext(),settings,(Activity)recyclerView.getContext());
        recyclerView.setAdapter(mAdapter);
        switch(type){
            case 1:{
                break;
            }
            case 2:{
                newAsyncGetList async = new newAsyncGetList(recyclerView.getContext(),MasterRecycler.this);
                async.execute("1071",settings.getString("account",""),settings.getString("android_id",""),settings.getString("group",""),settings.getString("founder",""));
                break;
            }
            case 3:{
                break;
            }
            case 4:{
                break;
            }
            case 5:{
                mAdapter.additem(new masterItem("","創建群組","",false,CreateGroupActivity.class));
                mAdapter.additem(new masterItem("","尋找群組","",false,FindGroupActivity.class));
                mAdapter.additem(new masterItem("","切換群組","",false,SelectGroupActivity.class));
                mAdapter.additem(new masterItem("","管理群組","",false,ManagementGroupActivity.class));
                mAdapter.additem(new masterItem("","審核成員","",false,GetNotJoinListActivity.class));
                mAdapter.additem(new masterItem("","創建會議","",false,CreateMeetingActivity.class));
                break;
            }
        }
        return v;
    }




}
