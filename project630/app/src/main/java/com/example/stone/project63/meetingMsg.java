package com.example.stone.project63;

/**
 * Created by stone on 2015/8/5.
 */
public class meetingMsg {
    boolean isSelf;
    String msg;
    String account;
    public meetingMsg(boolean isSelf,String msg,String account){
        this.msg = msg;
        this.isSelf = isSelf;
        this.account = account;
    }
}
