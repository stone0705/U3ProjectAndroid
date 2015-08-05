package com.example.stone.project63;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by stone on 2015/8/5.
 */
public class RVAdapter extends RecyclerView.Adapter<MyViewHolder> {
    ArrayList<String> datalist;
    public RVAdapter(ArrayList<String> datalist){
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
        viewHolder.b.setText(datalist.get(i));
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}
