package com.example.stone.project63;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;

/**
 * Created by stone on 2015/6/30.
 */
public class loginaction implements Runnable{
    String acc;
    String password;
    static boolean finish = false;
    static boolean pass = false;
    static boolean badconnect = false;
    final int LONGTIME = 800000;
    loginaction(String acc,String password){
        this.acc = acc;
        this.password = password;
    }
    @Override
    public void run(){
        try{
            Socket socket = new Socket(InetAddress.getByName("10.0.2.2"),5050);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write("login:"+acc+":"+password+"\n");
            bw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int time = 0;
            while(!br.ready()){
                if(time > LONGTIME)
                throw new Exception("long time");
                time++;
            }
            String answer = br.readLine();
            if(answer.equals("pass")){
                pass = true;
                System.out.println("thread"+pass);
            }
            finish = true;
        }
        catch (Exception ex){
            System.out.println(ex.toString());
            badconnect = true;
            finish = true;
        }
    }
}
