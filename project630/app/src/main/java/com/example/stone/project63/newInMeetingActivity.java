package com.example.stone.project63;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class newInMeetingActivity extends Activity {
    public static RecyclerView mRecyclerView;
    public static Handler mHandler = new Handler();
    public static RVAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static ArrayList<meetingMsg> msglist = new ArrayList<meetingMsg>();
    SharedPreferences settings;
    private Button sent;
    public static EditText msg;
    final String STORE_NAME = "Settings";
    public static Socket socket;
    static boolean selfdisconnect;
    boolean isenter = false;
    private static final long ANIM_DURATION = 1000;
    private View bgViewGroup;
    TextView teamname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_in_meeting);
        teamname = (TextView)findViewById(R.id.teamname);
        mRecyclerView = (RecyclerView) findViewById(R.id.meeting_recycler_view);
        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        sent = (Button)findViewById(R.id.sent);
        msg = (EditText)findViewById(R.id.msg);
        mLayoutManager = new LinearLayoutManager(this);
        bgViewGroup = findViewById(R.id.background);
        setupWindowAnimations();
        teamname.setText(settings.getString("group", "") + "=" + settings.getString("meeting_title", ""));
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mAdapter = new RVAdapter(msglist,this);
        //mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncSentAction sentAction = new AsyncSentAction();
                sentAction.execute("1032",settings.getString("account",""),msg.getText().toString(),settings.getString("meeting_id", ""),settings.getString("android_id",""));
            }
        });
    }
    @Override
    public void onPause(){
        super.onPause();
        try {
            selfdisconnect = true;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        selfdisconnect = false;
        msglist = new ArrayList<meetingMsg>();
        mAdapter = new RVAdapter(msglist,this);
        mRecyclerView.setAdapter(mAdapter);
        AsyncMeetingAction action = new AsyncMeetingAction(newInMeetingActivity.this);
        action.execute("1031",settings.getString("account",""),settings.getString("android_id",""),settings.getString("meeting_id",""),settings.getString("group",""),settings.getString("founder",""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_in_meeting, menu);
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

    private void setupWindowAnimations() {
        setupExitAnimations();
        setupEnterAnimations();
    }

    private void setupEnterAnimations() {
        Transition enterTransition = getWindow().getSharedElementEnterTransition();
        enterTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                    animateRevealShow(bgViewGroup);
                    isenter = true;
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
                if(isenter)
                animateRevealHide(bgViewGroup);
                bgViewGroup.setVisibility(View.INVISIBLE);
                isenter = false;
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
        anim.setDuration(ANIM_DURATION);
        anim.start();
        viewRoot.setVisibility(View.VISIBLE);
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
