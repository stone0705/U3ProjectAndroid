package com.example.stone.project63;

import java.util.HashMap;

//split with ":" and first is rule number 
public class StringRule {
    String[] dString;
    static HashMap rulemap = new HashMap<String,Integer>();
    public StringRule(String msg){
        String[] findrule = msg.split(":");
        rulemap.put("n1010", 3);
        rulemap.put("n1030", 5);
        rulemap.put("n1031", 5);
        rulemap.put("n2030", 3);
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

}