package com.example.stone.project63;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by stone on 2015/8/5.
 */
public class MyViewHolder extends RecyclerView.ViewHolder {
    Button b;
    public MyViewHolder(View itemView,View popupWindow) {
        super(itemView);
        b = (Button)itemView.findViewById(R.id.testbutton);
        final PopupWindow mPopupWindow;
        mPopupWindow = new PopupWindow(popupWindow, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                mPopupWindow.showAsDropDown(b);
            }
        });
    }
}
