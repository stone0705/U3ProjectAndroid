package com.example.stone.project63;

/**
 * Created by stone on 2015/7/7.
 */
//split with ":" and first is rule number
public class StringRule {
    String[] dString;
    public StringRule(String msg){
        String[] findrule = msg.split(":");
        dString = divide(msg,rule(findrule[0]));
    }
    String[] divide(String msg,int number){
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
        int answer = 0;
        switch(findrule){
            case "1010":{
                answer = 3;
                break;
            }
            case "1020":{
                answer = 3;
                break;
            }
        }
        return answer;
    }

}
