package com.example.stone.project63;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by stone on 2015/11/14.
 */
public class invoteAdapter extends RecyclerView.Adapter<invoteViewHolder> {
    ArrayList<invoteItem> datalist;
    CheckBox lastChecked  = null;
    int lastCheckedPos = 0;
    public invoteAdapter(ArrayList<invoteItem> datalist){
        this.datalist = datalist;
    }
    @Override
    public invoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invote_item, viewGroup, false);
        invoteViewHolder vh = new invoteViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(invoteViewHolder holder, int position) {
        final invoteItem item = datalist.get(position);
        holder.number.setText(""+item.number);
        holder.content.setText(item.content);
        holder.cb.setChecked(item.ischeck);
        holder.cb.setTag(new Integer(position));

        //for default check in first item
        if(position == 0 && datalist.get(0).ischeck && holder.cb.isChecked())
        {
            lastChecked = holder.cb;
            lastCheckedPos = 0;
        }

        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox)v;
                int clickedPos = ((Integer)cb.getTag()).intValue();
                if(cb.isChecked())
                {
                    if(lastChecked != null)
                    {
                        lastChecked.setChecked(false);
                        datalist.get(lastCheckedPos).ischeck = false;
                    }

                    lastChecked = cb;
                    lastCheckedPos = clickedPos;
                }
                else
                    lastChecked = null;

                datalist.get(clickedPos).ischeck = cb.isChecked();
            }
        });
    }
    public void additem(invoteItem item){
        datalist.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}
class invoteViewHolder extends RecyclerView.ViewHolder{
    CheckBox cb;
    TextView content;
    TextView number;
    public invoteViewHolder(View itemView) {
        super(itemView);
        cb = (CheckBox)itemView.findViewById(R.id.optionCB);
        content = (TextView)itemView.findViewById(R.id.optionText);
        number = (TextView)itemView.findViewById(R.id.optionNo);
    }
}
class invoteItem{
    int number;
    String content;
    String id;
    boolean ischeck = false;
    public invoteItem(String id,String content,int number){
        this.number = number;
        this.content = content;
        this.id = id;
    }
}
