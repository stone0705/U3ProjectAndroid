package com.example.stone.project63;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by stone on 2015/7/8.
 */
public class AsyncLoginAction extends AsyncTask<String,Integer,Integer> {
    final int LONGTIME = 8;
    static int time;
    ProgressDialog dialog;
    boolean pass;
    String response;
    Context mContext;
    String account;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public AsyncLoginAction(Context mContext,SharedPreferences settings){
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
        dialog.setTitle("登入中");
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
            bw.write(StringRule.standard("1010",params[0],params[1],params[2]));
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
                if(time > LONGTIME)
                    throw new Exception("long time");
            }
            String answer = br.readLine();
            socket.close();
            String[] dString = StringRule.divide(answer);
            pass = StringRule.isSucces(dString[0]);
            response = StringRule.responseString(dString[0]);
        }
        catch (Exception ex){
            System.out.println(ex.toString());
            response = ex.toString();
            result = 0;
        }
        account = params[0];
        return result;
    }
    @Override
    protected void onPostExecute(Integer result) {
        //UI THREAD
        // TODO Auto-generated method stub
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        final Intent intent = new Intent();
        editor = settings.edit();
        if(pass){
            editor.putString("account",account);
            String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            editor.putString("android_id",androidId);
            editor.putString("group","第一組");
            editor.putString("founder","qaz");
            editor.commit();
            intent.setClass(mContext, masterpage.class);
            mContext.startActivity(intent);
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Alert message to be shown");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if(pass){
                                intent.setClass(mContext,masterpage.class);
                                mContext.startActivity(intent);
                            }
                        }
                    });
            alertDialog.setMessage(response);
            alertDialog.show();
        }
    }

}
