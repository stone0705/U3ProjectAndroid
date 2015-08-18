package com.example.stone.project63;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class groupCardActivity extends Activity {
    private TextView title;
    private TextView founder;
    private TextView description;
    SharedPreferences settings;
    final String STORE_NAME = "Settings";
    Button apply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_card);
        title = (TextView)findViewById(R.id.groupTitle);
        founder = (TextView)findViewById(R.id.groupFounder);
        description = (TextView)findViewById(R.id.groupDes);
        apply = (Button)findViewById(R.id.enterbutton);
        settings = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
        Intent intent = getIntent();
        final findGroupItem item = (findGroupItem)intent.getSerializableExtra("group");
        title.setText("群組名："+item.title);
        founder.setText("創立者："+item.founder);
        description.setText("簡介："+item.description);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncApplyAction action = new AsyncApplyAction(groupCardActivity.this);
                action.execute(settings.getString("account",""),settings.getString("android_id",""),item.title,item.founder);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_card, menu);
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
}
