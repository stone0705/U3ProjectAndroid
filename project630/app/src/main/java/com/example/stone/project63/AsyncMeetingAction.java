package com.example.stone.project63;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by stone on 2015/8/5.
 */
public class AsyncMeetingAction extends AsyncTask<String,Integer,Integer> {
    final int LONGTIME = 8;
    static int time;
    ArrayList<meetingMsg> tempMsg = new ArrayList<meetingMsg>();
    boolean pass;
    boolean backtologin = false;
    String response;
    Context mContext;
    public AsyncMeetingAction(Context mContext){
        this.mContext = mContext;
    }
    @Override
    protected void onPreExecute() {
        //onPreExecute是在UI THREAD進行
        // TODO Auto-generated method stub
        super.onPreExecute();
        // 背景工作處理"前"需作的事
    }
    @Override
    protected Integer doInBackground(String... params) {
        //doInBackground是在Background Thread進行
        int result = 0;
        try{
            System.out.println("start async");
            Socket socket = new Socket(InetAddress.getByName("10.0.2.2"),5050);
            newInMeetingActivity.socket = socket;
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(StringRule.standard(params[0],params[1],params[2],params[3]));
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
                if(divide.equals("2031")){
                    if(params[1].equals(divide[1])){
                        newInMeetingActivity.msglist.add(new meetingMsg(true,divide[2],divide[1]));
                    }else{
                        newInMeetingActivity.msglist.add(new meetingMsg(false,divide[2],divide[1]));
                    }
                }else{
                    if(params[1].equals(divide[1])){
                        tempMsg.add(new meetingMsg(true,divide[2],divide[1]));
                    }else{
                        tempMsg.add(new meetingMsg(false,divide[2],divide[1]));
                    }
                }
            }
            for (int i = 0 ;i<tempMsg.size();i++){
                newInMeetingActivity.msglist.add(tempMsg.get(i));
            }
            while((answer = br.readLine())==null){
                divide = StringRule.divide(answer);
                if(params[1].equals(divide[1])){
                    newInMeetingActivity.msglist.add(new meetingMsg(true,divide[2],divide[1]));
                }else{
                    newInMeetingActivity.msglist.add(new meetingMsg(false,divide[2],divide[1]));
                }
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
        if(!newInMeetingActivity.selfdisconnect){
            // TODO Auto-generated method stub
            final Intent intent = new Intent();
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
        }

    }

}
