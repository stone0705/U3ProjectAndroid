package com.example.stone.project63;

/**
 * Created by stone on 2015/7/29.
 */
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class AsyncGetList extends AsyncTask<String,Integer,Integer> {
    final int LONGTIME = 8;
    static int time;
    HashMap Listmap = new HashMap<String,String[]>();
    ArrayList<String> titleList = new ArrayList<String>();
    ProgressDialog dialog;
    boolean pass;
    boolean backtologin = false;
    Activity mActivity;
    String response;
    Context mContext;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public AsyncGetList(Context mContext,SharedPreferences settings,Activity mActivity){
        this.mActivity = mActivity;
        this.mContext = mContext;
        this.settings = settings;
        dialog = new ProgressDialog(mContext);
    }
    @Override
    protected void onPreExecute() {
        //onPreExecute是在UI THREAD進行
        // TODO Auto-generated method stub
        super.onPreExecute();
        // 背景工作處理"前"需作的事
        dialog.setMessage("請等待");
        dialog.setTitle("讀取中");
        dialog.setCancelable(false);
        dialog.show();
    }
    @Override
    protected Integer doInBackground(String... params) {
        //doInBackground是在Background Thread進行
        int result = 0;
        try{
            Socket socket = new Socket(InetAddress.getByName("10.0.2.2"),5050);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(StringRule.standard(params[0],params[1],params[2],params[3],params[4]));
            bw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String[] divide;
            time = 0;
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        while(time <= LONGTIME){
                            Thread.sleep(1000);
                            time++;
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
            while(!br.ready()){
                if(time > LONGTIME)
                    throw new Exception("long time");
            }
            String answer = "";
            while(socket.isConnected()){
                if((answer = br.readLine())==null){break;}
                System.out.println(answer);
                divide = StringRule.divide(answer);
                if(divide[0].equals("0000")||divide[0].equals("2077")||divide[0].equals("2078")||divide[0].equals("2079")){
                    pass = StringRule.isSucces(divide[0]);
                    response = StringRule.responseString(divide[0]);
                    if(divide[0].equals("2077")){
                        backtologin = true;
                    }
                    break;}
                titleList.add(divide[2]);
                Listmap.put(divide[2],divide);
            }
            System.out.println(answer);
            //System.out.println(masterpage.titleList.get(0));
            socket.close();
        }
        catch (Exception ex){
            System.out.println(ex.toString());
            pass=false;
            response = ex.toString();
            result = 0;
        }
        System.out.println("thread" + result);
        return result;
    }
    protected void onPostExecute(Integer result) {
        // TODO Auto-generated method stub
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        final Intent intent = new Intent();
        editor = settings.edit();
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(backtologin){
                            intent.setClass(mContext,MainActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
                });
        alertDialog.setMessage(response);
        masterpage.content.removeAllViews();
        if(!pass){
            alertDialog.setMessage(response);
            alertDialog.show();
        }
        for(int i = 0;i<titleList.size();i++){
            String title = titleList.get(i);
            final String[] divide = (String[]) Listmap.get(title);
            final Button temp = new Button(mContext);
            temp.setText(title);
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("meeting_id",divide[1]);
                    editor.putString("meeting_title",divide[2]);
                    editor.commit();
                    intent.setClass(mContext,inmeetingActivity.class);
                    View sharedView = temp;
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(mActivity, sharedView, "menu");
                    mContext.startActivity(intent, transitionActivityOptions.toBundle());
                }
            });
            masterpage.content.addView(temp);
        }
    }

}
