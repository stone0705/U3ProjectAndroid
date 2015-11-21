package com.example.stone.project63;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateRemindActivity extends Activity{
    private TextView tvStartDate, tvStartTime, tvTitle,content;
    private Button btStartDate, btStartTime, createButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int sYear, sMonth, sDay, sHour, sMinute;
    SharedPreferences settings;
    final String STORE_NAME = "Settings";
    private Timestamp sts;
    private Calendar scalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_remind);

        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvTitle = (TextView) findViewById(R.id.remindTitle);
        content = (TextView)findViewById(R.id.remindContent);

        btStartDate = (Button) findViewById(R.id.btStartDate);
        btStartTime = (Button) findViewById(R.id.btStartTime);
        createButton = (Button) findViewById(R.id.createButton);

        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);

        btStartDate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        btStartTime.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRemind();
            }
        });
    }

    public void showDatePickerDialog() {
        // 設定初始日期
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // 跳出日期選擇器
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
    }
    public void showTimePickerDialog() {
        // 設定初始時間
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        // 跳出時間選擇器
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
    }

    public void createRemind(){
        String response = "";
        AlertDialog alertDialog = new AlertDialog.Builder(CreateRemindActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(response);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if(tvStartDate.getText().toString().equals("")||
                tvStartTime.getText().toString().equals("")||tvTitle.getText().toString().equals("")){
            response = "請輸入完所有項目";
            alertDialog.setMessage(response);
            alertDialog.show();
        }else{
            scalendar = new GregorianCalendar(sYear,sMonth,sDay,sHour,sMinute);
            sts = new Timestamp(scalendar.getTimeInMillis());
            AsyncCreateRemind action = new AsyncCreateRemind(CreateRemindActivity.this);
            action.execute(settings.getString("account",""),settings.getString("android_id",""),settings.getString("group","")
                    ,settings.getString("founder",""),tvTitle.getText().toString(),content.getText().toString(),sts.toString());
        }
    }

}
