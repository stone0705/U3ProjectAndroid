package com.example.stone.project63;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by stone on 2015/8/19.
 */
public class AsyncGetJoinMember extends AsyncTask<String,Integer,Integer> {
    final int LONGTIME = 8;
    static int time;
    ProgressDialog dialog;
    boolean pass;
    String response;
    Context mContext;
    public AsyncGetJoinMember(Context mContext){
        this.mContext = mContext;
        dialog = new ProgressDialog(mContext);
    }
    @Override
    protected void onPreExecute() {
        //onPreExecute是在UI THREAD進行
        // TODO Auto-generated method stub
        super.onPreExecute();
        // 背景工作處理"前"需作的事
        dialog.setMessage("請等待");
        dialog.setTitle("取得成員清單中");
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
            bw.write(StringRule.standard("1104",params[0],params[1]));
            bw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
            String answer;
            while(socket.isConnected()){
                answer = br.readLine();
                String[] dString = StringRule.divide(answer);
                System.out.println(dString[0]);
                if(dString[0].equals("0000")){
                    pass = true;
                    break;
                }else{
                    if(dString[0].equals("2106")){
                        ManagementGroupActivity.mHandler.post(new update(new memberRightItem(dString[1],dString[2],Boolean.valueOf(dString[3]),Boolean.valueOf(dString[4]),Boolean.valueOf(dString[5])
                        ,Boolean.valueOf(dString[6]),Boolean.valueOf(dString[7]),Boolean.valueOf(dString[8]),Boolean.valueOf(dString[9]),dString[10])));
                    }
                }
            }
            socket.close();
        }
        catch (Exception ex){
            System.out.println(ex.toString());
            response = ex.toString();
            pass = false;
            result = 0;
        }
        return result;
    }
    @Override
    protected void onPostExecute(Integer result) {
        // TODO Auto-generated method stub
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(response);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        if(!pass){
            alertDialog.show();
        }
    }
    private class update implements Runnable{
        memberRightItem a;
        public update(memberRightItem a){
            this.a = a;
        }
        @Override
        public void run() {
            ManagementGroupActivity.mAdapter.additem(a);
        }
    }
}