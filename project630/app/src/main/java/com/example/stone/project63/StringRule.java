package com.example.stone.project63;

import java.util.HashMap;

//split with ":" and first is rule number 
public class StringRule {
    String[] dString;
    static HashMap rulemap = new HashMap<String,Integer>();
    public StringRule(String msg){
        String[] findrule = msg.split(":");
        //登入  1010 帳號 密碼
        rulemap.put("n1010", 3);
        //進入 meeting 1030 帳號 android_id 群組 meeting_id
        rulemap.put("n1030", 5);
        //客戶端傳送訊息 1031 帳號 text 群組 meeting_id
        rulemap.put("n1031", 5);
        //伺服器端傳送訊息 2030 帳號 text
        rulemap.put("n2030", 3);
        //註冊成功 2000
        rulemap.put("n2000", 1);
        //註冊失敗 有相同的帳號 2001
        rulemap.put("n2001", 1);
        //登入成功 2010
        rulemap.put("n2010", 1);
        //登入失敗 帳號或密碼錯誤 2011
        rulemap.put("n2011", 1);
        dString = divide(msg,rule(findrule[0]));

    }
    private String[] divide(String msg,int number){
        String[] answer = new String[number];
        int state = 0;
        int index = 0;
        int temp  = 0;
        for(int i = 0;i<msg.length();i++){
            switch(msg.charAt(i)){
                case '/':{
                    if(state == 0){
                        state = 1;
                    }else{
                        state = 0;
                    }
                    break;
                }
                case ':':{
                    if(state == 0){
                        if(temp == 0){
                            answer[index] = msg.substring(temp, i);
                            temp = i;
                        }else{
                            answer[index] = msg.substring(temp+1, i);
                            temp = i;
                        }
                        index++;
                    }else if(state == 1){
                        state = 0;
                    }
                    break;
                }
            }
        }
        for(int i =0;i<answer.length;i++){
            answer[i] = answer[i].replaceAll("//", "/");
            answer[i] = answer[i].replaceAll("/:", ":");
        }
        return answer;
    }

    int rule(String findrule){
        return (int) rulemap.get("n"+findrule);
    }

    public static String standard(int number,String... word){
        StringBuilder answer = new StringBuilder();
        for(int i = 0;i<number;i++){
            String temp = word[i];
            temp=temp.replaceAll("/", "//");
            temp=temp.replaceAll(":", "/:");
            answer.append(temp+":");
        }
        return answer.toString()+"\n";
    }
    public static String responseString(String number){
        String answer = "";
        switch (number){
            case "2000":{
                answer = "註冊成功";
                break;
            }
            case "2001":{
                answer = "帳號重複";
                break;
            }
            case "2010":{
                answer = "登入成功";
                break;
            }
            case "2011":{
                answer = "帳號或密碼錯誤";
                break;
            }
        }
        return answer;
    }
    public  static boolean isSucces(String number){
        boolean answer = false;
        switch (number){
            case "2000":{
                answer = true;
                break;
            }
            case "2001":{
                answer = false;
                break;
            }
            case "2010":{
                answer = true;
                break;
            }
            case "2011":{
                answer = false;
                break;
            }
        }
        return answer;
    }

}