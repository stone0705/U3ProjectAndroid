package com.example.stone.project63;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by stone on 2015/11/12.
 */
public class CreateVoteAdapter extends RecyclerView.Adapter<CreateVoteViewHolder> {
    ArrayList<CreateVoteItem> datalist = new ArrayList<CreateVoteItem>();
    public CreateVoteAdapter(ArrayList<CreateVoteItem> datalist){
        this.datalist = datalist;
    }

    @Override
    public CreateVoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.create_vote_option_item, viewGroup, false);
        CreateVoteViewHolder vh = new CreateVoteViewHolder(v,new MyCustomEditTextListener());
        return vh;
    }

    @Override
    public void onBindViewHolder(final CreateVoteViewHolder holder, final int position) {
        holder.delcheck.setChecked(false);
        if(datalist.get(position).isOpen){
            holder.no.setVisibility(View.VISIBLE);
            holder.input1.setVisibility(View.VISIBLE);
            holder.delcheck.setVisibility(View.VISIBLE);
            holder.myCustomEditTextListener.updatePosition(position);
            holder.content.setText(datalist.get(position).content);
            holder.open.setVisibility(View.GONE);
            if(datalist.get(position).hide == true){
                holder.no.setVisibility(View.GONE);
                holder.input1.setVisibility(View.GONE);
                holder.delcheck.setVisibility(View.GONE);
                holder.open.setVisibility(View.GONE);
            }
        }else{
            holder.no.setVisibility(View.GONE);
            holder.input1.setVisibility(View.GONE);
            holder.delcheck.setVisibility(View.GONE);
            holder.open.setVisibility(View.VISIBLE);
        }
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datalist.get(position).isOpen = true;
                holder.no.setVisibility(View.VISIBLE);
                holder.input1.setVisibility(View.VISIBLE);
                holder.delcheck.setVisibility(View.VISIBLE);
                holder.open.setVisibility(View.GONE);
                boolean allOpen = true;
                for (int i = 0;i<datalist.size();i++){
                    if(datalist.get(i).isOpen == false){
                        allOpen = false;
                    }
                }
                if (allOpen){
                    datalist.add(new CreateVoteItem());
                    CreateVoteAdapter.this.notifyItemChanged(datalist.size());
                }
            }
        });
        holder.delcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.no.setVisibility(View.GONE);
                holder.input1.setVisibility(View.GONE);
                holder.delcheck.setVisibility(View.GONE);
                holder.open.setVisibility(View.GONE);
                datalist.get(position).hide = true;
            }

        });
    }
    @Override
    public int getItemCount() {
        return datalist.size();
    }
    public void hide(int position){
            datalist.remove(position);
            this.notifyItemRemoved(position);
    }
    class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            datalist.get(position).content = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
class CreateVoteViewHolder extends RecyclerView.ViewHolder{
    TextView no;
    EditText content;
    TextInputLayout input1;
    Button open;
    CheckBox delcheck;
    CreateVoteAdapter.MyCustomEditTextListener myCustomEditTextListener;
    public CreateVoteViewHolder(View itemView, CreateVoteAdapter.MyCustomEditTextListener myCustomEditTextListener) {
        super(itemView);
        no = (TextView)itemView.findViewById(R.id.option_no);
        content = (EditText)itemView.findViewById(R.id.option_editText);
        input1 = (TextInputLayout)itemView.findViewById(R.id.input1);
        open = (Button)itemView.findViewById(R.id.option_open);
        delcheck = (CheckBox)itemView.findViewById(R.id.delcheck);
        this.myCustomEditTextListener = myCustomEditTextListener;
        content.addTextChangedListener(myCustomEditTextListener);
    }
}
class CreateVoteItem{
    boolean hide = false;
    String content = "";
    boolean isOpen = false;
}
