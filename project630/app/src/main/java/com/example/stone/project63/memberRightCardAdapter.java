package com.example.stone.project63;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stone on 2015/8/19.
 */
public class memberRightCardAdapter extends RecyclerView.Adapter<memberRightViewHolder> {
    ArrayList<memberRightItem> datalist = new ArrayList<memberRightItem>();
    Context mContext;
    Activity mActivity;
    public memberRightCardAdapter(ArrayList<memberRightItem> datalist, Context mContext, Activity mActivity){
        this.datalist = datalist;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }
    @Override
    public memberRightViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_right_card, viewGroup, false);
        memberRightViewHolder vh = new memberRightViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final memberRightViewHolder holder, final int position) {
        memberRightItem item = datalist.get(position);
        holder.isFounder.setChecked(item.isFounder);
        holder.addRight.setChecked(item.addRight);
        holder.removeRight.setChecked(item.removeRight);
        holder.noteRight.setChecked(item.noteRight);
        holder.meetingRight.setChecked(item.meetingRight);
        holder.voteRight.setChecked(item.voteRight);
        holder.schRight.setChecked(item.schRight);
        holder.name.setText("帳號：" + item.name);
        holder.nickname.setText("暱稱："+item.nickname);
        holder.enterTime.setText("加入時間："+item.enterTime);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("member",datalist.get(position));
                intent.setClass(mContext,ChangeRightActivity.class);
                View shareView = holder.card;
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(mActivity, shareView , "memberRightCard");
                mContext.startActivity(intent, transitionActivityOptions.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    public void additem(memberRightItem item) {
        datalist.add(item);
        this.notifyItemInserted(datalist.size());
    }
    public void removeAll(){
        if(!datalist.isEmpty()){
            for(int i = datalist.size()-1;i>=0;i--){
                datalist.remove(i);
                this.notifyItemRemoved(i);
            }
        }
    }
}
class memberRightViewHolder extends RecyclerView.ViewHolder{
    TextView name;
    TextView nickname;
    TextView enterTime;
    RadioButton addRight;
    RadioButton removeRight;
    RadioButton noteRight;
    RadioButton meetingRight;
    RadioButton voteRight;
    RadioButton schRight;
    RadioButton isFounder;
    CardView card;
    public memberRightViewHolder(View itemView) {
        super(itemView);
        card = (CardView)itemView.findViewById(R.id.memberCard);
        name = (TextView)itemView.findViewById(R.id.memberName);
        nickname = (TextView)itemView.findViewById(R.id.memberNickName);
        enterTime = (TextView)itemView.findViewById(R.id.memberEnterTime);
        addRight = (RadioButton)itemView.findViewById(R.id.addRight);
        removeRight = (RadioButton)itemView.findViewById(R.id.removeRight);
        noteRight = (RadioButton)itemView.findViewById(R.id.noteRight);
        meetingRight = (RadioButton)itemView.findViewById(R.id.meetingRight);
        voteRight = (RadioButton)itemView.findViewById(R.id.voteRight);
        schRight = (RadioButton)itemView.findViewById(R.id.schRight);
        isFounder = (RadioButton)itemView.findViewById(R.id.isFounder);
    }
}
class memberRightItem implements Serializable {
    boolean addRight;
    boolean removeRight;
    boolean noteRight;
    boolean meetingRight;
    boolean voteRight;
    boolean schRight;
    boolean isFounder;
    String enterTime;
    String name;
    String nickname;
    public memberRightItem(String name, String nickname, boolean addRight, boolean removeRight, boolean noteRight
            , boolean meetingMsg, boolean voteRight, boolean schRight, boolean isFounder, String enterTime){
        this.name = name;
        this.nickname = nickname;
        this.enterTime = enterTime;
        this.addRight = addRight;
        this.removeRight = removeRight;
        this.noteRight = noteRight;
        this.meetingRight = meetingMsg;
        this.voteRight = voteRight;
        this.schRight = schRight;
        this.isFounder = isFounder;
    }
}
