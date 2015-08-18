package com.example.stone.project63;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by stone on 2015/8/12.
 */
public class masterAdapter extends RecyclerView.Adapter<masterViewHolder> {
    ArrayList<masterItem> datalist;
    Context mContext;
    SharedPreferences.Editor editor;
    Activity mActivity;
    public masterAdapter(ArrayList<masterItem> datalist,Context mContext,SharedPreferences settings,Activity mActivity){
        this.datalist = datalist;
        this.mContext = mContext;
        editor = settings.edit();
        this.mActivity = mActivity;

    }
    @Override
    public masterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.masteritem, viewGroup, false);
        masterViewHolder vh = new masterViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final masterViewHolder ViewHolder, int i) {
        final Intent intent = new Intent();
        final masterItem item = datalist.get(i);
        ViewHolder.listButton.setText(datalist.get(i).title);
        ViewHolder.listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("meeting_id",item.id);
                editor.putString("meeting_title",item.title);
                editor.commit();
                intent.setClass(mContext,item.cls);
                ViewHolder.listButton.setElevation(5);
                ViewHolder.listButton.setTranslationZ(8);
                View sharedView = ViewHolder.listButton;
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
class masterViewHolder extends RecyclerView.ViewHolder {
    Button listButton;
    public masterViewHolder(View itemView){
        super(itemView);
        listButton = (Button)itemView.findViewById(R.id.itembutton);
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