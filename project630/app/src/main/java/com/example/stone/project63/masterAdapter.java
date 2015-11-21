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
import android.widget.RelativeLayout;
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
    final int TYPE_MEETING = 0;
    final int TYPE_VOTE = 1;
    final int TYPE_PARENT = 2;
    final int TYPE_CHILD = 3;
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
            case(TYPE_PARENT):{
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parent,viewGroup,false);
                parentViewHolder vh = new parentViewHolder(v);
                return vh;
            }
            case(TYPE_CHILD):{
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child,viewGroup,false);
                childViewHolder vh = new childViewHolder(v);
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
        if(ViewHolder instanceof voteViewHolder){
            onBindVoteViewHolder((voteViewHolder)ViewHolder,i,datalist.get(i));
        }
        if(ViewHolder instanceof parentViewHolder){
            onBindParentViewHolder((parentViewHolder)ViewHolder,i,datalist.get(i));
        }
        if(ViewHolder instanceof childViewHolder){
            onBindChildViewHolder((childViewHolder)ViewHolder,i,datalist.get(i));
        }
    }

    public void onBindMeetingViewHolder(final meetingViewHolder ViewHolder,int position,Object meetingItem){
        final Intent intent = new Intent();
        final masterItem item = (masterItem)meetingItem;
        if(item.startTime!=null){
            ViewHolder.title.setText("會議名稱："+item.title);
            ViewHolder.startTime.setText("開始時間："+item.startTime.toString());
            ViewHolder.endTime.setText("結束時間：" + item.endTime.toString());
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

    public void onBindVoteViewHolder(final voteViewHolder ViewHolder,int position,Object voteItem){
        final Intent intent = new Intent();
        final voteItem item = (voteItem)voteItem;
        ViewHolder.title.setText("投票名稱："+item.title);
        ViewHolder.createman.setText("發起人："+item.createman);
        ViewHolder.startTime.setText("開始時間："+item.startTime.toString());
        ViewHolder.endTime.setText("結束時間："+item.endTime.toString());
        ViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("vote_id", item.id);
                editor.putString("vote_title", item.title);
                editor.putString("vote_sts", item.startTime.toString());
                editor.putString("vote_ets", item.endTime.toString());
                editor.putString("vote_createman", item.createman);
                editor.commit();
                intent.setClass(mContext, item.cls);
                View sharedView = ViewHolder.cardView;
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(mActivity, sharedView, item.transitionName);
                mContext.startActivity(intent, transitionActivityOptions.toBundle());
            }
        });
    }
    public void onBindParentViewHolder(final parentViewHolder vh, final int position,Object parentitem){
        final parentItem item = (parentItem)parentitem;
        vh.title.setText("標題："+item.title);
        vh.createman.setText("建立人："+item.createman);
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expand(item, vh.getPosition());
            }
        });
    }

    public void onBindChildViewHolder(childViewHolder vh,int position,Object childitem){
        childItem item = (childItem)childitem;
        vh.content.setText("內容："+item.content);
        vh.time.setText("預定時間：" + item.time);
    }
    public void expand(parentItem item,int position){
        if(item.isexapand){
            item.isexapand = false;
            datalist.remove(position+1);
            notifyItemRemoved(position+1);
        }else{
            item.isexapand = true;
            datalist.add(position+1,item.child);
            notifyItemInserted(position+1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(datalist.get(position) instanceof masterItem){
            return TYPE_MEETING;
        }
        if(datalist.get(position) instanceof voteItem){
            return TYPE_VOTE;
        }
        if(datalist.get(position) instanceof parentItem){
            return TYPE_PARENT;
        }
        if(datalist.get(position) instanceof childItem){
            return TYPE_CHILD;
        }
        System.out.println("ghujihjk");
        return -1;
    }
    @Override
    public int getItemCount() {
        return datalist.size();
    }
    public void additem(parentItem item) {
        datalist.add(item);
        this.notifyItemInserted(datalist.size());
    }
    public void additem(masterItem item) {
        datalist.add(item);
        this.notifyItemInserted(datalist.size());
    }
    public void additem(voteItem item) {
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
    TextView title;
    TextView createman;
    TextView startTime;
    TextView endTime;
    CardView cardView;
    public voteViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.title);
        createman = (TextView)itemView.findViewById(R.id.createman);
        startTime = (TextView)itemView.findViewById(R.id.startTime);
        endTime = (TextView)itemView.findViewById(R.id.endTime);
        cardView = (CardView)itemView.findViewById(R.id.meetingCard);
    }
}
class parentViewHolder extends RecyclerView.ViewHolder{
    TextView title;
    TextView createman;
    RelativeLayout layout;
    public parentViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.remindTitle);
        createman = (TextView)itemView.findViewById(R.id.remindCreatman);
        layout = (RelativeLayout)itemView.findViewById(R.id.remindLayout);

    }
}
class childViewHolder extends RecyclerView.ViewHolder{
    TextView content;
    TextView time;
    public childViewHolder(View itemView) {
        super(itemView);
        content = (TextView)itemView.findViewById(R.id.remindContent);
        time = (TextView)itemView.findViewById(R.id.remindTime);
    }
}
class voteItem{
    Timestamp startTime;
    Timestamp endTime;
    String id;
    String title = "default";
    String createman;
    String transitionName;
    boolean isAnimate;
    Class<?> cls;
    public voteItem(String id,String title,String createman,Timestamp startTime,Timestamp endTime,String transitionName,Class<?> cls){
        this.id = id;
        this.createman = createman;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.transitionName = transitionName;
        this.isAnimate = isAnimate;
        this.cls = cls;
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
class parentItem {
    childItem child;
    String id;
    String title;
    String createman;
    boolean isexapand = false;
    public parentItem(childItem child,String id,String title,String createman){
        this.child = child;
        this.title = title;
        this.id = id;
        this.createman = createman;
    }
}
class childItem {
    String content;
    String time;
    public childItem(String content,String time){
        this.content = content;
        this.time = time;
    }
}