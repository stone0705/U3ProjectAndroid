package com.example.stone.project63;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by A7 on 2015/10/12.
 */
public class ANT extends Activity{
    private SharedPreferences ANTtable;
    private SharedPreferences.Editor antEditor;
    private Context context;
    public ANT(Context context){
        this.context = context;
        ANTtable = context.getSharedPreferences("ANT", MODE_PRIVATE);
        antEditor = ANTtable.edit();
    }
    String Transfer(String account){
        return ANTtable.getString(account,"not found");
    }
    boolean isANT(){
        SharedPreferences settings = context.getSharedPreferences("Settings", MODE_PRIVATE);
        return settings.getBoolean("isant", true);
    }
    void putTable(String account,String nickname){
        antEditor.putString(account,nickname);
        antEditor.commit();
    }
}
