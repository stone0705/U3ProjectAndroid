package com.example.stone.project63;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

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
        viewHolder.right.setText(i+datalist.get(i).msg);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        if(datalist.get(i).isSelf){
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }else{
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        viewHolder.right.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public void additem(meetingMsg item) {
        datalist.add(item);
        this.notifyItemInserted(datalist.size());
    }
}
class MyViewHolder extends RecyclerView.ViewHolder {
    Button right;
    public MyViewHolder(View itemView,View popupWindow) {
        super(itemView);
        right = (Button)itemView.findViewById(R.id.rightbutton);
        final PopupWindow mPopupWindow;
        mPopupWindow = new PopupWindow(popupWindow, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                mPopupWindow.showAsDropDown(right);
            }
        });
    }
}
class meetingMsg {
    boolean isSelf;
    String msg;
    String account;
    public meetingMsg(boolean isSelf,String msg,String account){
        this.msg = msg;
        this.isSelf = isSelf;
        this.account = account;
    }
}
