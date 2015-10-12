package com.example.stone.project63;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by stone on 2015/8/5.
 */
public class RVAdapter extends RecyclerView.Adapter<MyViewHolder> {
    ArrayList<meetingMsg> datalist;
    ANT ant;
    public RVAdapter(ArrayList<meetingMsg> datalist,Context context){
        this.datalist = datalist;
        ant = new ANT(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.msg_card, viewGroup, false);
        View popupWindow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popup_window, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(v,popupWindow);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        viewHolder.msg.setText(datalist.get(i).msg);
        if(ant.isANT()){
            viewHolder.account.setText(ant.Transfer(datalist.get(i).account));
        }else{
            viewHolder.account.setText(datalist.get(i).account);
        }
        LinearLayout.LayoutParams linear = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(datalist.get(i).isSelf){
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            linear.gravity = Gravity.RIGHT;
        }else{
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            linear.gravity = Gravity.LEFT;
        }
        viewHolder.card.setLayoutParams(params);
        viewHolder.account.setLayoutParams(linear);
        viewHolder.msg.setLayoutParams(linear);
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
    TextView account;
    TextView msg;
    CardView card;
    public MyViewHolder(View itemView,View popupWindow) {
        super(itemView);
        account = (TextView)itemView.findViewById(R.id.account);
        msg = (TextView)itemView.findViewById(R.id.msg);
        card = (CardView)itemView.findViewById(R.id.msgCard);
        final PopupWindow mPopupWindow;
        mPopupWindow = new PopupWindow(popupWindow, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                mPopupWindow.showAsDropDown(card);
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
