package com.qingfengweb.baoqi.propertyInsurance;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class PetitionLetterActivity extends Activity {
	
	
	private Spinner s1,s2,s3,s4,s5;
	private String[] str1 = {
		"��ѡ��","����Ӫ��Ա","������չԱ"
		,"�����ͻ�����","������ƾ���","����ҵ��Ա�������ƣ�"
		,"����ҵ��Ա��Ա���ƣ�","�绰������Ա"
		,"�ͻ�","����Ӫ��Ա���ѽ�Լ��","������չԱ���ѽ�Լ��"
	};
	private String[] str2 = {
			"��ѡ��","��������"
			,"��������","��������"
			,"��������","����"
		};
	
	private String[] str3 = {
			"��ѡ��","�ܹ�˾"
			,"�����зֹ�˾"
		};
	
	private String[] str4 = {
			"��ѡ��","��Ӫ��λ������Ա"
			,"Ӫ��Ա","���������Ա"
			,"����"
		};
	
	private Button btn1 ,btn2,btn_confirm;
	private Button h_backhomebtn;
	private Button homebtn;
	
	private LinearLayout low_linear3;
	private LinearLayout low_linear;
	private ScrollView low_linear1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//ȥ���ֻ��ϵı�����
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.l_petitionletter);
		s1 = (Spinner)findViewById(R.id.s1);
		s2 = (Spinner)findViewById(R.id.s2);
		s3 = (Spinner)findViewById(R.id.s3);
		s4 = (Spinner)findViewById(R.id.s4);
		s5 = (Spinner)findViewById(R.id.s5);
		
		btn1 = (Button)findViewById(R.id.btn_1);
		btn2 = (Button)findViewById(R.id.btn_2);
		btn_confirm = (Button)findViewById(R.id.confirm);
		h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        low_linear3 = (LinearLayout)findViewById(R.id.lowlinear3);
        low_linear = (LinearLayout)findViewById(R.id.lowlinear);
        low_linear1 = (ScrollView)findViewById(R.id.lowlinear1);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(PetitionLetterActivity.this, PropertyInsuranceMainActivity.class);
			  	PetitionLetterActivity.this.startActivity(intent);
			  	PetitionLetterActivity.this.finish();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(PetitionLetterActivity.this, PropertyInsuranceMainActivity.class);
			  	PetitionLetterActivity.this.startActivity(intent);
			  	PetitionLetterActivity.this.finish();
			}
		});
		
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn1.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn1.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				low_linear3.setVisibility(View.GONE);
				low_linear.setVisibility(View.VISIBLE);
				low_linear1.setVisibility(View.VISIBLE);
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn2.setBackgroundResource(R.drawable.single_tag01_on);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn2.setTextColor(Color.BLACK);
				btn1.setTextColor(Color.WHITE);
				low_linear3.setVisibility(View.VISIBLE);
				low_linear.setVisibility(View.GONE);
				low_linear1.setVisibility(View.GONE);
			}
		});
		
		btn_confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(PetitionLetterActivity.this, "���������ύ�У����Ե�...", 2000).show();
			}
		});
		
		s1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str1));
		s2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str3));
		s3.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str2));
		s4.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str1));
		s5.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str4));
		
	}
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		 Intent intent = new Intent();
		 intent.setClass(PetitionLetterActivity.this, PropertyInsuranceMainActivity.class);
		 PetitionLetterActivity.this.startActivity(intent);
		 PetitionLetterActivity.this.finish();
	  }
	  return true;
	}

}
