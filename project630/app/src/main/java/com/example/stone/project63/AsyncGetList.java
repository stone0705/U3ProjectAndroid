package com.example.stone.project63;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by stone on 2015/8/12.
 */
public class AsyncGetList extends AsyncTask<String,Integer,Integer> {
    final int LONGTIME = 8;
    static int time;
    ProgressDialog dialog;
    boolean pass;
    boolean backtologin = false;
    MasterRecycler MR;
    String response;
    Context mContext;
    ArrayList<masterItem> list;
    public AsyncGetList(Context mContext, MasterRecycler MR){
        this.mContext = mContext;
        dialog = new ProgressDialog(mContext);
        this.MR = MR;
    }
    @Override
    protected void onPreExecute() {
        //onPreExecute是在UI THREAD進行
        // TODO Auto-generated method stub
        super.onPreExecute();
        // 背景工作處理"前"需作的事
        MR.mAdapter.removeAll();
        dialog.setMessage("請等待");
        dialog.setTitle("讀取中");
        dialog.setCancelable(false);
        //dialog.show();
    }
    @Override
    protected Integer doInBackground(String... params) {
        //doInBackground是在Background Thread進行
        int result = 0;
        try{
            Socket socket = new Socket(InetAddress.getByName(mContext.getString(R.string.myip)),5050);
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
                if(time > LONGTIME){
                    socket.close();
                    throw new Exception("long time");
                }
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
                switch (divide[0]){
                    case("2070"):{
                        parentItem item = new parentItem(new childItem(divide[3],divide[4]),divide[1],divide[2],divide[5]);
                        update updateRemindItem =new update(item);
                        MR.mHandler.post(updateRemindItem);
                        break;
                    }
                    case("2071"):{
                        masterItem item = new masterItem(divide[1],divide[2]
                                , Timestamp.valueOf(divide[3]),Timestamp.valueOf(divide[4])
                                ,"titleText",true,newInMeetingActivity.class);
                        update updateMeetingItem =new update(item);
                        MR.mHandler.post(updateMeetingItem);
                        break;
                    }
                    case("2072"):{
                        voteItem item = new voteItem(divide[1],divide[2] ,divide[5], Timestamp.valueOf(divide[3]),Timestamp.valueOf(divide[4]),"inVoteCard",invoteActivity.class);
                        update updateVoteItem =new update(item);
                        MR.mHandler.post(updateVoteItem);
                        break;
                    }
                    case("2073"):{
                        break;
                    }
                }
            }
            System.out.println(answer);
            socket.close();
        }
        catch (Exception ex){
            System.out.println(ex.toString());
            pass=false;
            response = ex.toString();
            result = 0;
        }
        return result;
    }
    protected void onPostExecute(Integer result) {
        // TODO Auto-generated method stub
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        final Intent intent = new Intent();
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(backtologin){
                            intent.setClass(mContext,MasterTabActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
                });
        alertDialog.setMessage(response);
        if(!pass){
            alertDialog.setMessage(response);
            if(!MasterTabActivity.isShowAlertDialog){
                alertDialog.show();
                MasterTabActivity.isShowAlertDialog = true;
            }
        }
    }
    private class update implements Runnable {
        parentItem a;
        masterItem b;
        voteItem c;
        int type = -1;
        public update(parentItem a){
            this.a = a;
            type = 0;
        }
        public update(masterItem b){
            this.b = b;
            type = 1;
        }
        public update(voteItem c){
            this.c = c;
            type = 2;
        }
        @Override
        public void run() {
            switch(type){
                case(0):{
                    MR.mAdapter.additem(a);
                    break;
                }
                case(1):{
                    MR.mAdapter.additem(b);
                    break;
                }
                case(2):{
                    MR.mAdapter.additem(c);
                    break;
                }
            }
        }
    }
}

