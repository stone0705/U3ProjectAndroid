package com.example.stone.project63;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * Created by stone on 2015/5/20.
 */
public class nbut extends Button {
    public nbut(final Context context,String a,final PopupWindow mPopupWindow) {
        super(context);
        super.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        super.setText(a);
        super.setTextSize(12);
        super.setClickable(true);
        super.setFocusable(true);
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                mPopupWindow.showAsDropDown(nbut.this);
            }
        });
    }

}