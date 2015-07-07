package com.example.stone.project63;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;



public class inmeetingActivity extends Activity {
    public static Handler mHandler = new Handler();
    static LinearLayout main;
    EditText name;
    EditText text;
    Button sent;
    ScrollView scrollView;
    Socket socket;
    String tmp;
    String selfstring;
    LinearLayout linearLayout;
    PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inmeeting);
        main = (LinearLayout)findViewById(R.id.linear);
        Button a = new Button(this);
        scrollView = (ScrollView)findViewById(R.id.addscrollView);
        name = (EditText)findViewById(R.id.EditText01);
        text = (EditText)findViewById(R.id.EditText02);
        sent = (Button)findViewById(R.id.Button01);
        linearLayout = (LinearLayout)findViewById(R.id.FrameLayout01);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View popupWindow = layoutInflater.inflate(R.layout.popup_window, null);
        mPopupWindow = new PopupWindow(popupWindow, 100, 130);
        Thread t = new Thread(readData);
        t.start();
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(socket.isConnected()){
                        BufferedWriter bw;
                        bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                        selfstring = name.getText()+":"+text.getText()+"\n";
                        bw.write(selfstring);
                        bw.flush();
                    }
                }
                catch(Exception ex){
                }
                text.setText("");
            }
        });
    }

    public void onPause(){
        super.onPause();
        try{
            BufferedWriter bw;
            bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            bw.write("123");
            bw.flush();
            socket.close();
            finish();
        }
        catch(Exception ex){}

    }
    @Override
    public void onStop(){
        super.onStop();
        try{
            BufferedWriter bw;
            bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            bw.write("123");
            bw.flush();
            socket.close();
            finish();
        }
        catch(Exception ex){}

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            BufferedWriter bw;
            bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            bw.write("123");
            bw.flush();
            socket.close();
        }
        catch(Exception ex){}

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inmeeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Runnable updateText = new Runnable() {
        public void run() {
            LinearLayout.LayoutParams self = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            self.gravity = Gravity.RIGHT;
            if(tmp.equals("100")){
                main.addView(new nbut(inmeetingActivity.this,selfstring,mPopupWindow),self);
            }else{
                main.addView(new nbut(inmeetingActivity.this,tmp,mPopupWindow));
            }
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    };

    private Runnable readData = new Runnable() {
        public void run() {
            InetAddress serverIp;

            try {
                serverIp = InetAddress.getByName("220.129.74.90");
                int serverPort = 5050;
                socket = new Socket(serverIp, serverPort);

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));

                while (socket.isConnected()) {
                    tmp = br.readLine();

                    if(tmp!=null)
                        mHandler.post(updateText);
                }

            } catch (IOException e) {

            }
        }
    };

}