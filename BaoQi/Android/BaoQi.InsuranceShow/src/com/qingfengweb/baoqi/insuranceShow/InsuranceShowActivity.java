package com.qingfengweb.baoqi.insuranceShow;

import com.qingfengweb.baoqi.gereninfo.PrivateMessage;
import com.qingfengweb.baoqi.insuranceShow.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class InsuranceShowActivity extends Activity {
    /** Called when the activity is first created. */
	private EditText userName,userPassword,userIp;
	private Button button;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        userName=(EditText) findViewById(R.id.userName);
        userPassword=(EditText) findViewById(R.id.userPassword);
        userIp=(EditText) findViewById(R.id.userip);
        button=(Button) findViewById(R.id.loginSubmit);
        button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(InsuranceShowActivity.this,InsuranceShowMainActivity.class);
				startActivity(intent);
				finish();
			}
		});

    }
 
}