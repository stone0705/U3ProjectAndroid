package com.example.stone.project63;

/**
 * Created by stone on 2015/7/29.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by stone on 2015/7/8.
 */
public class AsyncGetList extends AsyncTask<String,Integer,Integer> {
    final int LONGTIME = 80000;
    ProgressDialog dialog;
    public AsyncGetList(ProgressDialog dialog){
        this.dialog = dialog;
    }
    @Override
    protected void onPreExecute() {
        //onPreExecute是在UI THREAD進行
        // TODO Auto-generated method stub
        super.onPreExecute();
        // 背景工作處理"前"需作的事
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    while(!AsyncGetList.this.getStatus().equals(AsyncTask.Status.FINISHED)&&!AsyncGetList.this.isCancelled())
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
            int time = 0;
            String[] divide;
            while(!br.ready()){
                if(time > LONGTIME)
                    throw new Exception("long time");
                time++;
            }
            String answer = "123";
            masterpage.titleList = new ArrayList<String>();
            while(socket.isConnected()){
                if((answer = br.readLine())==null){break;}
                System.out.println(answer);
                divide = StringRule.divide(answer);
                if(divide[0].equals("0000")){break;}
                masterpage.titleList.add(divide[2]);
                masterpage.Listmap.put(divide[2],divide);
            }
            System.out.println(answer);
            System.out.println(masterpage.titleList.get(0));
            socket.close();
        }
        catch (Exception ex){
            System.out.println(ex.toString());
            result = 0;
        }
        System.out.println("thread"+result);
        masterpage.asyncfin = true;
        return result;
    }

}
