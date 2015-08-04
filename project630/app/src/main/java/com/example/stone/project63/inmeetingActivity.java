package com.example.stone.project63;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Bundle;
import android.transition.Transition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

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
    TextView teamname;
    EditText text;
    Button sent;
    ScrollView scrollView;
    Socket socket;
    String tmp;
    String selfstring;
    PopupWindow mPopupWindow;
    SharedPreferences settings;
    final String STORE_NAME = "Settings";
    private static final long ANIM_DURATION = 1000;
    private View bgViewGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inmeeting);
        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        main = (LinearLayout)findViewById(R.id.linear);
        Button a = new Button(this);
        teamname = (TextView)findViewById(R.id.teamname);
        scrollView = (ScrollView)findViewById(R.id.addscrollView);
        text = (EditText)findViewById(R.id.EditText02);
        sent = (Button)findViewById(R.id.Button01);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View popupWindow = layoutInflater.inflate(R.layout.popup_window, null);
        //popwindow 設定大小符合內容 點擊popwindow以外的部分會使popwindow消失
        mPopupWindow = new PopupWindow(popupWindow, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        setupLayout();
        setupWindowAnimations();
        teamname.setText(settings.getString("group","")+"="+settings.getString("meeting_title",""));
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(socket.isConnected()&&!text.getText().toString().equals("")){
                        BufferedWriter bw;
                        bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                        selfstring = StringRule.standard("1032",settings.getString("account",""),text.getText().toString(),settings.getString("meeting_id",""),settings.getString("android_id",""));
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

    @Override
    public void onPause(){
        super.onPause();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        Thread t = new Thread(readData);
        t.start();
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
            String[] dString = StringRule.divide(tmp);
            System.out.println(dString[2]);
            if(dString[0].equals("2077")){
                final Intent intent = new Intent();
                AlertDialog alertDialog = new AlertDialog.Builder(inmeetingActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(StringRule.responseString("2077"));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                    intent.setClass(inmeetingActivity.this,MainActivity.class);
                                    inmeetingActivity.this.startActivity(intent);
                            }
                        });
                alertDialog.show();
            }else{
                if(dString[1].equals(settings.getString("account",""))){
                    main.addView(new nbut(inmeetingActivity.this,dString[2],mPopupWindow),self);
                }else{
                    main.addView(new nbut(inmeetingActivity.this,dString[2],mPopupWindow));
                }
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }
    };

    private Runnable readData = new Runnable() {
        public void run() {
            InetAddress serverIp;
            try {
                serverIp = InetAddress.getByName("10.0.2.2");
                int serverPort = 5050;
                socket = new Socket(serverIp, serverPort);
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bw;
                bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                selfstring = StringRule.standard("1031",settings.getString("account",""),settings.getString("android_id",""),settings.getString("meeting_id",""));
                bw.write(selfstring);
                bw.flush();
                while (socket.isConnected()) {
                    if((tmp = br.readLine())!=null){
                        mHandler.post(updateText);
                    }else{
                        break;
                    }
                }

            } catch (IOException e) {

            }
        }
    };
    private void setupLayout() {
        bgViewGroup = findViewById(R.id.back);
    }

    private void setupWindowAnimations() {
        setupEnterAnimations();
        setupExitAnimations();
    }

    private void setupEnterAnimations() {
        Transition enterTransition = getWindow().getSharedElementEnterTransition();
        enterTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {}

            @Override
            public void onTransitionEnd(Transition transition) {
                animateRevealShow(bgViewGroup);
                }

            @Override
            public void onTransitionCancel(Transition transition) {}

            @Override
            public void onTransitionPause(Transition transition) {}

            @Override
            public void onTransitionResume(Transition transition) {}
        });
    }

    private void setupExitAnimations() {
        Transition sharedElementReturnTransition = getWindow().getSharedElementReturnTransition();
        sharedElementReturnTransition.setStartDelay(ANIM_DURATION);


        Transition returnTransition = getWindow().getReturnTransition();
        returnTransition.setDuration(ANIM_DURATION);
        returnTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                animateRevealHide(bgViewGroup);
            }

            @Override
            public void onTransitionEnd(Transition transition) {}

            @Override
            public void onTransitionCancel(Transition transition) {}

            @Override
            public void onTransitionPause(Transition transition) {}

            @Override
            public void onTransitionResume(Transition transition) {}
        });
    }

    private void animateRevealShow(View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setVisibility(View.VISIBLE);
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    private void animateRevealHide(final View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int initialRadius = viewRoot.getWidth();

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                viewRoot.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }
}
