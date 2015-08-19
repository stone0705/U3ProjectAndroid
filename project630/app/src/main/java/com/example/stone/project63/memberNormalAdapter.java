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
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stone on 2015/8/19.
 */
public class memberNormalAdapter extends RecyclerView.Adapter<memberNormalViewHolder> {
    ArrayList<memberNormalItem> datalist = new ArrayList<memberNormalItem>();
    Context mContext;
    Activity mActivity;
    public memberNormalAdapter(ArrayList<memberNormalItem> datalist, Context mContext, Activity mActivity){
        this.datalist = datalist;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }
    @Override
    public memberNormalViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_normal_card, viewGroup, false);
        memberNormalViewHolder vh = new memberNormalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(memberNormalViewHolder holder, final int position) {
        memberNormalItem item = datalist.get(position);
        holder.name.setText("帳號："+item.name);
        holder.nickname.setText("暱稱："+item.nickname);
        holder.time.setText("申請時間："+item.entertime);
        final View shareView = holder.card;
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("member",datalist.get(position));
                intent.setClass(mContext,LetHeInActivity.class);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(mActivity, shareView , "memberNormalCard");
                mContext.startActivity(intent, transitionActivityOptions.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    public void additem(memberNormalItem item) {
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
class memberNormalViewHolder extends RecyclerView.ViewHolder{
    TextView name;
    TextView nickname;
    TextView time;
    CardView card;
    public memberNormalViewHolder(View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.memberName);
        nickname = (TextView)itemView.findViewById(R.id.memberNickName);
        time = (TextView)itemView.findViewById(R.id.memberEnterTime);
        card = (CardView)itemView.findViewById(R.id.memberCard);
    }
}
class memberNormalItem implements Serializable  {
    String name;
    String nickname;
    String entertime;
    public memberNormalItem(String name,String nickname,String entertime){
        this.name = name;
        this.nickname = nickname;
        this.entertime = entertime;
    }
}