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
 * Created by stone on 2015/8/18.
 */
public class findGroupAdapter extends RecyclerView.Adapter<findGroupViewHolder>{
    ArrayList<findGroupItem> datalist = new ArrayList<findGroupItem>();
    Context mContext;
    Activity mActivity;
    public findGroupAdapter(ArrayList<findGroupItem> datalist,Context mContext,Activity mActivity){
        this.datalist = datalist;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }
    @Override
    public findGroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.find_group_card, viewGroup, false);
        findGroupViewHolder vh = new findGroupViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(findGroupViewHolder holder, final int position) {
        holder.title.setText("群組名："+datalist.get(position).title);
        holder.founder.setText("創立者："+datalist.get(position).founder);
        holder.description.setText("簡介："+datalist.get(position).description);
        final View shareView = holder.card;
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("group",datalist.get(position));
                intent.setClass(mContext,groupCardActivity.class);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(mActivity, shareView , "groupCard");
                mContext.startActivity(intent, transitionActivityOptions.toBundle());
            }
        });
    }
    public void additem(findGroupItem item) {
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

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}
class findGroupViewHolder extends RecyclerView.ViewHolder{
    TextView title;
    TextView founder;
    TextView description;
    CardView card;
    public findGroupViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.groupTitle);
        founder = (TextView)itemView.findViewById(R.id.groupFounder);
        description = (TextView)itemView.findViewById(R.id.groupDes);
        card = (CardView)itemView.findViewById(R.id.groupCard);
    }
}
class findGroupItem implements Serializable {
    String title;
    String founder;
    String description;
    public findGroupItem(String title,String founder,String description){
        this.title = title;
        this.founder = founder;
        this.description = description;
    }
}
