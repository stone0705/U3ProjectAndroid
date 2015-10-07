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

/**
 * Created by stone on 2015/7/8.
 */
public class AsyncSubmitAction extends AsyncTask<String,Integer,Integer> {
    final int LONGTIME = 8;
    static int time;
    ProgressDialog dialog;
    boolean pass;
    String response;
    Context mContext;
    public AsyncSubmitAction(Context mContext){
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
        dialog.setTitle("註冊中");
        dialog.setCancelable(false);
        dialog.show();
    }
    @Override
    protected Integer doInBackground(String... params) {
        //doInBackground是在Background Thread進行
        int result = 0;
        try{
            Socket socket = new Socket(InetAddress.getByName(mContext.getString(R.string.myip)),5050);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(1000+":"+params[0]+":"+params[1]+":"+params[2]+":\n");
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
            String answer = br.readLine();
            socket.close();
            String[] dString = StringRule.divide(answer);
            response = StringRule.responseString(dString[0]);
            pass = StringRule.isSucces(dString[0]);
        }
        catch (Exception ex){
            System.out.println(ex.toString());
            response = ex.toString();
            pass = false;
            result = 0;
        }
        System.out.println("thread"+result);
        return result;
    }
    @Override
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
                        if(pass){
                            intent.setClass(mContext,MainActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
                });
        alertDialog.setMessage(response);
        alertDialog.show();
    }

}
