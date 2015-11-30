package com.example.stone.project63;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
    ANT ant;
    public AsyncMeetingAction(Context mContext){
        this.mContext = mContext;
        ant = new ANT(mContext);
    }
    @Override
    protected void onPreExecute() {
        //onPreExecute是在UI THREAD進行
        // TODO Auto-generated method stub
        super.onPreExecute();
        // 背景工作處理"前"需作的事
    }
    @Override
    protected Integer doInBackground(final String... params) {
        //doInBackground是在Background Thread進行
        int result = 0;
        try{
            System.out.println("start async");
            Socket socket = new Socket(InetAddress.getByName(mContext.getString(R.string.myip)),5050);
            newInMeetingActivity.socket = socket;
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(StringRule.standard("1031",params[0],params[1],params[2],params[3],params[4]));
            bw.flush();
            final BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
            int statecount = 0;
            while(socket.isConnected()){
                if((answer = br.readLine())==null){break;}
                divide = StringRule.divide(answer);
                if(divide[0].equals("0000")||divide[0].equals("2077")||divide[0].equals("2078")||divide[0].equals("2079")||divide[0].equals("2197")||divide[0].equals("2198")){
                    pass = StringRule.isSucces(divide[0]);
                    response = StringRule.responseString(divide[0]);
                    if(divide[0].equals("2077")){
                        backtologin = true;
                    }
                    if(divide[0].equals("2197")){
                        newInMeetingActivity.timeState = -1;
                    }
                    if(divide[0].equals("2198")){
                        newInMeetingActivity.timeState = 1;
                    }
                    if(divide[0].equals("0000")&&statecount < 1){
                        statecount++;
                    }else{
                        break;
                    }
                }
                switch (divide[0]){
                    case"2031":{
                        if(params[0].equals(divide[1])){
                            newInMeetingActivity.mHandler.post(new update(new meetingMsg(true,divide[2],divide[1])));
                        }else{
                            newInMeetingActivity.mHandler.post(new update(new meetingMsg(false,divide[2],divide[1])));
                        }
                        break;
                    }
                    case"2030":{
                        if(params[0].equals(divide[1])){
                            tempMsg.add(new meetingMsg(true,divide[2],divide[1]));
                        }else{
                            tempMsg.add(new meetingMsg(false,divide[2],divide[1]));
                        }
                        break;
                    }
                    case"2032":{
                        ant.putTable(divide[1],divide[2]);
                    }
                }
            }
            for (int i = 0 ;i<tempMsg.size();i++){
                newInMeetingActivity.mHandler.post(new update(tempMsg.get(i)));
            }
            new Thread(new Runnable(){
                @Override
                public void run(){
                    String answer;
                    try{
                        while((answer = br.readLine())!=null){
                            String[] divide = StringRule.divide(answer);
                            if(params[0].equals(divide[1])){
                                newInMeetingActivity.mHandler.post(new update(new meetingMsg(true,divide[2],divide[1])));
                            }else{
                                newInMeetingActivity.mHandler.post(new update(new meetingMsg(false,divide[2],divide[1])));
                            }
                        }
                    }catch (Exception ex){
                        System.out.println("receive Thread"+ex.toString());
                    }
                }
            }).start();
            //socket.close();
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
            if(!pass){
                try {
                    newInMeetingActivity.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alertDialog.setMessage(response);
                alertDialog.show();
            }
        }

    }

    private class update implements Runnable{

        meetingMsg a;
        public update(meetingMsg a){
            this.a = a;
        }
        @Override
        public void run() {
            newInMeetingActivity.mAdapter.additem(a);
            newInMeetingActivity.mRecyclerView.scrollToPosition(newInMeetingActivity.mAdapter.getItemCount()-1);
        }
    }

}

