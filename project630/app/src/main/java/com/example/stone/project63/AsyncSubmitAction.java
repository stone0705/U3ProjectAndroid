package com.example.stone.project63;

import android.app.ProgressDialog;
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
    final int LONGTIME = 80000;
    ProgressDialog dialog;
    public AsyncSubmitAction(ProgressDialog dialog){
        this.dialog = dialog;
    }
    @Override
    protected void onPreExecute() {
        //onPreExecute是在UI THREAD進行
        // TODO Auto-generated method stub
        super.onPreExecute();
        // 背景工作處理"前"需作的事
        dialog.show();
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    while(!AsyncSubmitAction.this.getStatus().equals(AsyncTask.Status.FINISHED)&&!AsyncSubmitAction.this.isCancelled())
                        Thread.sleep(500);

                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally{
                    dialog.dismiss();
                }
            }
        }).start();
    }
    @Override
    protected Integer doInBackground(String... params) {
        //doInBackground是在Background Thread進行
        int result = 0;
        try{
            Socket socket = new Socket(InetAddress.getByName("10.0.2.2"),5050);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(1000+":"+params[0]+":"+params[1]+":"+params[2]+":\n");
            bw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int time = 0;
            while(!br.ready()){
                if(time > LONGTIME)
                    throw new Exception("long time");
                time++;
            }
            String answer = br.readLine();
            socket.close();
            StringRule sr = new StringRule(answer);
            submit.response = StringRule.responseString(sr.dString[0]);
            submit.pass = StringRule.isSucces(sr.dString[0]);
        }
        catch (Exception ex){
            System.out.println(ex.toString());
            result = 0;
        }
        System.out.println("thread"+result);
        submit.asyncfin = true;
        return result;
    }

}
