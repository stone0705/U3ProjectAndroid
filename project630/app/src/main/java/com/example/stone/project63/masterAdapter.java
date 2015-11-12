package com.example.stone.project63;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by stone on 2015/8/12.
 */
public class masterAdapter extends RecyclerView.Adapter {
    ArrayList<Object> datalist;
    Context mContext;
    SharedPreferences.Editor editor;
    Activity mActivity;
    final int TYPE_VOTE = 1;
    final int TYPE_MEETING = 0;
    public masterAdapter(ArrayList<Object> datalist,Context mContext,SharedPreferences settings,Activity mActivity){
        this.datalist = datalist;
        this.mContext = mContext;
        editor = settings.edit();
        this.mActivity = mActivity;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (i){
            case(TYPE_MEETING):{
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.masteritem, viewGroup, false);
                meetingViewHolder vh = new meetingViewHolder(v);
                return vh;
            }
            case(TYPE_VOTE):{
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.voteitem, viewGroup, false);
                voteViewHolder vh = new voteViewHolder(v);
                return vh;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder ViewHolder, int i) {
        if(ViewHolder instanceof meetingViewHolder){
            onBindMeetingViewHolder((meetingViewHolder)ViewHolder,i,datalist.get(i));
        }
    }

    public void onBindMeetingViewHolder(final meetingViewHolder ViewHolder,int position,Object Meetingitem){
        final Intent intent = new Intent();
        final masterItem item = (masterItem)Meetingitem;
        if(item.startTime!=null){
            ViewHolder.title.setText("會議名稱："+item.title);
            ViewHolder.startTime.setText("開始時間："+item.startTime.toString());
            ViewHolder.endTime.setText("結束時間："+item.endTime.toString());
        }else{
            ViewHolder.title.setText(item.title);
            ViewHolder.startTime.setVisibility(View.GONE);
            ViewHolder.endTime.setVisibility(View.GONE);
        }
        ViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("meeting_id", item.id);
                editor.putString("meeting_title", item.title);
                editor.commit();
                intent.setClass(mContext, item.cls);
                View sharedView = ViewHolder.cardView;
                if(item.isAnimate){
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(mActivity, sharedView, item.transitionName);
                    mContext.startActivity(intent, transitionActivityOptions.toBundle());
                }else{
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if(datalist.get(position) instanceof masterItem){
            return TYPE_MEETING;
        }else{
            return TYPE_VOTE;
        }
    }
    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public void additem(masterItem item) {
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
class meetingViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView startTime;
    TextView endTime;
    CardView cardView;
    public meetingViewHolder(View itemView){
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.title);
        startTime = (TextView)itemView.findViewById(R.id.startTime);
        endTime = (TextView)itemView.findViewById(R.id.endTime);
        cardView = (CardView)itemView.findViewById(R.id.meetingCard);
    }
}
class voteViewHolder extends RecyclerView.ViewHolder{
    public voteViewHolder(View itemView) {
        super(itemView);
    }
}
class masterItem{
    Timestamp startTime;
    Timestamp endTime;
    String id;
    String title = "default";
    String transitionName;
    boolean isAnimate;
    Class<?> cls;
    public masterItem(String id,String title,Timestamp startTime,Timestamp endTime,String transitionName,boolean isAnimate,Class<?> cls){
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.transitionName = transitionName;
        this.isAnimate = isAnimate;
        this.cls = cls;
    }
    public masterItem(String id,String title,String transitionName,boolean isAnimate,Class<?> cls){
        this.id = id;
        this.title = title;
        this.transitionName = transitionName;
        this.isAnimate = isAnimate;
        this.cls = cls;
    }
}