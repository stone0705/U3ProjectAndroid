package com.example.stone.project63;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by stone on 2015/8/11.
 */
public class AsyncSentAction extends AsyncTask<String,Integer,Integer> {
    Context mContext;
    public AsyncSentAction(Context mContext){
        this.mContext = mContext;
    }
    @Override
    protected Integer doInBackground(String[] params) {
        try{
            Socket socket = new Socket(InetAddress.getByName(mContext.getString(R.string.myip)),5050);
            BufferedWriter bw;
            bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            String selfstring = StringRule.standard(params[0],params[1],params[2],params[3],params[4]);
            bw.write(selfstring);
            bw.flush();
            socket.close();
        }catch (Exception ex){
            System.out.println("sent async"+ex.toString());
        }
        return null;
    }
    @Override
    protected void onPostExecute(Integer result){
        newInMeetingActivity.msg.setText("");
    }
}
