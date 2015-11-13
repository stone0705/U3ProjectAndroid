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
import java.util.ArrayList;

/**
 * Created by stone on 2015/11/12.
 */
public class AsyncCreateVote extends AsyncTask<String,Integer,Integer> {
    final int LONGTIME = 8;
    static int time;
    ProgressDialog dialog;
    boolean pass;
    String response;
    Context mContext;
    ArrayList<CreateVoteItem> datalist;
    public AsyncCreateVote(Context mContext,ArrayList<CreateVoteItem> datalist){
        this.mContext = mContext;
        this.datalist = datalist;
        dialog = new ProgressDialog(mContext);
    }
    @Override
    protected void onPreExecute() {
        //onPreExecute是在UI THREAD進行
        // TODO Auto-generated method stub
        super.onPreExecute();
        // 背景工作處理"前"需作的事
        dialog.setMessage("請等待");
        dialog.setTitle("創建中");
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
            bw.write(StringRule.standard("1040",params[0],params[1],params[2],params[3],params[4],params[5],params[6]));
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
            answer = br.readLine();
            String[] dString = StringRule.divide(answer);
            int total = 0;
            if(dString[0].equals("2040")){
                String option_String;
                for(int i = 0;i < datalist.size();i++){
                    option_String = datalist.get(i).content;
                    //選項字串不為空和選項沒被刪除
                    if(!option_String.equals("") && !datalist.get(i).hide){
                        bw.write(StringRule.standard("1041",params[0],params[1],params[2],params[3],dString[1],option_String));
                        bw.flush();
                        total++;
                    }
                }
                int count = 0;
                while (true){
                    answer = br.readLine();
                    System.out.println(answer);
                    count++;
                    dString = StringRule.divide(answer);
                    pass = StringRule.isSucces(dString[0]);
                    response = StringRule.responseString(dString[0]);
                    if(!pass){break;}
                    if(count >= total){break;};
                }
            }else{
                pass = StringRule.isSucces(dString[0]);
                response = StringRule.responseString(dString[0]);
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
        final Intent intent = new Intent();
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(pass){
                            intent.setClass(mContext,MasterTabActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
                });
        alertDialog.setMessage(response);
        alertDialog.show();
    }
}
