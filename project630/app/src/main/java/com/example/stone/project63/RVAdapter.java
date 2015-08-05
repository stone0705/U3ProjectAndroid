package com.example.stone.project63;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by stone on 2015/8/5.
 */
public class RVAdapter extends RecyclerView.Adapter<MyViewHolder> {
    ArrayList<meetingMsg> datalist;
    public RVAdapter(ArrayList<meetingMsg> datalist){
        this.datalist = datalist;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.testitem, viewGroup, false);
        View popupWindow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popup_window, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(v,popupWindow);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        if(datalist.get(i).isSelf){
            viewHolder.right.setText(datalist.get(i).msg);
            viewHolder.left.setVisibility(View.GONE);
        }else{
            viewHolder.left.setText(datalist.get(i).msg);
            viewHolder.right.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}
