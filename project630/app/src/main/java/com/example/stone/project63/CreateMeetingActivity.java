package com.example.stone.project63;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


public class CreateMeetingActivity extends Activity {
    private TextView tvStartDate, tvStartTime, tvEndTime, tvEndDate, tvTitle;
    private Button btStartDate, btStartTime, btEndDate, btEndTime, createButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int sYear, sMonth, sDay, sHour, sMinute;
    private int eYear, eMonth, eDay, eHour, eMinute;
    SharedPreferences settings;
    final String STORE_NAME = "Settings";
    private Timestamp sts,ets;
    private Calendar scalendar, ecalendar;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        tvTitle = (TextView) findViewById(R.id.meetingTitle);

        btStartDate = (Button) findViewById(R.id.btStartDate);
        btStartTime = (Button) findViewById(R.id.btStartTime);
        btEndDate = (Button) findViewById(R.id.btEndDate);
        btEndTime = (Button) findViewById(R.id.btEndTime);
        createButton = (Button) findViewById(R.id.createButton);

        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);

        btStartDate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });
        btStartTime.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(true);
            }
        });
        btEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });
        btEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(false);
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMeeting();
            }
        });
    }

    public void showDatePickerDialog(final boolean start) {
        // 設定初始日期
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // 跳出日期選擇器
        if(start){
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // 完成選擇，顯示日期
                            //monthOfYear從0開始

                            tvStartDate.setText(year + "-" + (monthOfYear + 1) + "-"
                                    + dayOfMonth);
                            sYear = year;
                            sMonth = monthOfYear;
                            sDay = dayOfMonth;

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }else{
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // 完成選擇，顯示日期
                            //monthOfYear從0開始
                            tvEndDate.setText(year + "-" + (monthOfYear + 1) + "-"
                                    + dayOfMonth);
                            eYear = year;
                            eMonth = monthOfYear;
                            eDay = dayOfMonth;
                        }
                    }, sYear, sMonth, sDay);
            dpd.show();
        }
    }

    public void showTimePickerDialog(boolean start) {
        // 設定初始時間
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        // 跳出時間選擇器
        if(start){
            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            // 完成選擇，顯示時間
                            tvStartTime.setText(hourOfDay + ":" + minute + ":0");
                            sHour = hourOfDay;
                            sMinute = minute;
                        }
                    }, mHour, mMinute, false);
            tpd.show();
        }else{
            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            // 完成選擇，顯示時間
                            tvEndTime.setText(hourOfDay + ":" + minute + ":0");
                            eHour = hourOfDay;
                            eMinute = minute;
                        }
                    }, sHour, sMinute, false);
            tpd.show();
        }
    }
    public void createMeeting(){
        String response = "";
        AlertDialog alertDialog = new AlertDialog.Builder(CreateMeetingActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(response);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if(tvStartDate.getText().toString().equals("")||tvEndDate.getText().toString().equals("")
                ||tvStartTime.getText().toString().equals("")||tvEndTime.getText().toString().equals("")||tvTitle.getText().toString().equals("")){
            response = "請輸入完所有項目";
            alertDialog.setMessage(response);
            alertDialog.show();
        }else{
            scalendar = new GregorianCalendar(sYear,sMonth,sDay,sHour,sMinute);
            sts = new Timestamp(scalendar.getTimeInMillis());
            ecalendar = new GregorianCalendar(eYear,eMonth,eDay,eHour,eMinute);
            ets = new Timestamp(ecalendar.getTimeInMillis());
            if(ets.after(sts)){
                AsyncCreateMeeting action = new AsyncCreateMeeting(CreateMeetingActivity.this);
                action.execute(settings.getString("account",""),settings.getString("android_id",""),settings.getString("group","")
                        ,settings.getString("founder",""),tvTitle.getText().toString(),sts.toString(),ets.toString());
            }else{
                response = "結束時間必須在開始時間之後";
                alertDialog.setMessage(response);
                alertDialog.show();
            }
        }
    }

}
