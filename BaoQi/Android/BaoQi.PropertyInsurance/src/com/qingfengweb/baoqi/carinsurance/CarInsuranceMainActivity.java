package com.qingfengweb.baoqi.carinsurance;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.qingfengweb.baoqi.mytask.MyTaskMainActivity;
import com.qingfengweb.baoqi.propertyInsurance.CanvasWriteActivity;
import com.qingfengweb.baoqi.propertyInsurance.FreeInsuranceActivity;
import com.qingfengweb.baoqi.propertyInsurance.MyApplication;
import com.qingfengweb.baoqi.propertyInsurance.PropertyInsuranceMainActivity;
import com.qingfengweb.baoqi.propertyInsurance.R;
import com.qingfengweb.baoqi.propertyInsurance.WritePath;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CarInsuranceMainActivity extends Activity implements OnClickListener{
private	Bitmap b = null; 
private static Bitmap bb = null;
private	CanvasWriteActivity canvas;
private Spinner spinner1;
private ArrayAdapter<CharSequence> adapter1,step2_adapter,step2_adapter1,step1_adapter2,step1_adapter3,step1_adapter4,step1_adapter5;
private Button nextBtn;  
private int back_flag = 0;
private Button step2_backBtn,step2_nextBtn;
private Button step3_backBtn,step3_nextBtn;
private Button step4_backBtn,step4_nextBtn;
private Button p_backhome_btn;
private int e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11,e12,e13;
private EditText step2_e1,step2_e2,step2_e3,step2_e4,step2_e5,step2_e6,step2_e7,step2_e8,step2_e9,step2_e10,step2_e11,step2_e12,step2_e13;
private LinearLayout no1;
private TextView value_shangyexian,value_jiaoqiangxian,value_chechuanshui,value_jiaoche;
private TextView value_step4_shang,value_step4_jiaoqiang,value_step4_chechuanshui,value_step4_count,value_step4_yuan,value_step4_jieshen;
private LinearLayout step1,step2,step3,step4;
private LayoutInflater inflater;
private Spinner step1_spinner2,step1_spinner3,step1_spinner4,step1_spinner5;
private Spinner step2_spinner1,step2_spinner2,step2_spinner3,step2_spinner4,step2_spinner5,step2_spinner6,step2_spinner7,step2_spinner8
,step2_spinner9,step2_spinner10,step2_spinner11,step2_spinner12,step2_spinner13;
private String str1[]={"￥950","￥350","￥50","￥150","￥450","￥200","￥110","￥230","￥430","￥470"};
private String str2[]={"￥450","￥334","￥454","￥654","￥567","￥765","￥221","￥776","￥654","￥123"};
private RadioGroup step3_radopGroup1;
private int value_count=0;
private int toubao_flag=0;
private Button h_btn1,h_btn2,h_btn3,h_btn4,h_btn5,h_btn6;
private FrameLayout guo_tab5;
private ImageView guo_image;
private WritePath write = null;
public ProgressDialog progressdialog;
public CarInsuranceMainActivity ca;
private ProgressDialog readWaitProgressDialog;
private String[] bank = { "中国建设银行", "中国工商银行", "中国农业银行", "中国招商银行" };

private String[] cardtype = { "信用卡", "存储卡" };

public void confirmShowWaitDialog() {
	if (readWaitProgressDialog == null) {
		readWaitProgressDialog = new ProgressDialog(this);
		readWaitProgressDialog
				.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		readWaitProgressDialog.setMessage("正在执行支付,请稍后...");
		readWaitProgressDialog.setCancelable(true);
		readWaitProgressDialog.show();
	} else {
		readWaitProgressDialog.show();
	}
}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.carinsurancemain);
		initView();
		MyViewFun();
		no1.removeAllViews();
		no1.addView(step1);
		
		bb = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.cx_xinxi));
		
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
				}
				return true;
			}

		});
		
	}
	/*
	 * 初始化控件函数
	 */
	private void initView(){
		inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		h_btn1=(Button) findViewById(R.id.tab1);
		h_btn2=(Button) findViewById(R.id.tab2);
		h_btn3=(Button) findViewById(R.id.tab3);
		h_btn4=(Button) findViewById(R.id.tab4);
		h_btn5=(Button) findViewById(R.id.tab5);
		h_btn6=(Button) findViewById(R.id.tab6);
		step1 = (LinearLayout)inflater.inflate(R.layout.carinsurnce_step1, null);
		step2 = (LinearLayout)inflater.inflate(R.layout.carinsurance_step2, null);
		step3 = (LinearLayout)inflater.inflate(R.layout.carinsurance_step3, null);
		step4 = (LinearLayout)inflater.inflate(R.layout.carinsurance_step4, null);
		step3_radopGroup1=(RadioGroup) step3.findViewById(R.id.step3_radiogroup1);
		value_jiaoqiangxian=(TextView) step3.findViewById(R.id.value_jiaoqiangxian);
		value_chechuanshui=(TextView) step3.findViewById(R.id.value_chechuanshui);
		value_jiaoche=(TextView) step3.findViewById(R.id.value_jiaoche);
		value_step4_shang=(TextView) step4.findViewById(R.id.value_step4_shang);
		value_step4_jiaoqiang=(TextView) step4.findViewById(R.id.value_step4_jiaoqiang);
		value_step4_chechuanshui=(TextView) step4.findViewById(R.id.value_step4_chechuanshui);
		value_step4_count=(TextView) step4.findViewById(R.id.value_step4_count);
		value_step4_yuan=(TextView) step4.findViewById(R.id.value_step4_yuan);
		value_step4_jieshen=(TextView) step4.findViewById(R.id.value_step4_jieshen);
		p_backhome_btn=(Button) findViewById(R.id.p_backhome_btn);
		/*
		 * 适配器加载区
		 */
		adapter1=ArrayAdapter.createFromResource(CarInsuranceMainActivity.this,R.array.car_city, android.R.layout.simple_spinner_item);		
		step2_adapter=ArrayAdapter.createFromResource(CarInsuranceMainActivity.this,R.array.if_toubao, android.R.layout.simple_spinner_item);	
		step2_adapter1=ArrayAdapter.createFromResource(CarInsuranceMainActivity.this,R.array.step2_cunshi, android.R.layout.simple_spinner_item);	
		step1_adapter2=ArrayAdapter.createFromResource(CarInsuranceMainActivity.this,R.array.car_pinpai, android.R.layout.simple_spinner_item);			
		step1_adapter3=ArrayAdapter.createFromResource(CarInsuranceMainActivity.this,R.array.car_xinghao, android.R.layout.simple_spinner_item);	
		step1_adapter4=ArrayAdapter.createFromResource(CarInsuranceMainActivity.this,R.array.car_xing, android.R.layout.simple_spinner_item);	
		step1_adapter5=ArrayAdapter.createFromResource(CarInsuranceMainActivity.this,R.array.car_xingshilicheng, android.R.layout.simple_spinner_item);	
		spinner1=(Spinner) step1.findViewById(R.id.spinner1);
		nextBtn=(Button) step1.findViewById(R.id.nextBtn);
		no1=(LinearLayout) findViewById(R.id.no1);
		value_shangyexian=(TextView) step2.findViewById(R.id.value_shangyexian);
		step2_backBtn=(Button) step2.findViewById(R.id.backBtn);
		step2_nextBtn=(Button) step2.findViewById(R.id.nextBtn);
		step3_backBtn=(Button) step3.findViewById(R.id.backBtn);
		step3_nextBtn=(Button) step3.findViewById(R.id.nextBtn);
		step4_backBtn=(Button) step4.findViewById(R.id.backBtn);
		step4_nextBtn=(Button) step4.findViewById(R.id.nextBtn);
		
		step1_spinner2=(Spinner) step1.findViewById(R.id.step1_spinner2);
		step1_spinner3=(Spinner) step1.findViewById(R.id.step1_spinner3);
		step1_spinner4=(Spinner) step1.findViewById(R.id.step1_spinner4);
		step1_spinner5=(Spinner) step1.findViewById(R.id.step1_spinner5);
		step2_spinner1=(Spinner) step2.findViewById(R.id.step2_spinner1);
		step2_spinner2=(Spinner) step2.findViewById(R.id.step2_spinner2);
		step2_spinner3=(Spinner) step2.findViewById(R.id.step2_spinner3);
		step2_spinner4=(Spinner) step2.findViewById(R.id.step2_spinner4);
		step2_spinner5=(Spinner) step2.findViewById(R.id.step2_spinner5);
		step2_spinner6=(Spinner) step2.findViewById(R.id.step2_spinner6);
		step2_spinner7=(Spinner) step2.findViewById(R.id.step2_spinner7);
		step2_spinner8=(Spinner) step2.findViewById(R.id.step2_spinner8);
		step2_spinner9=(Spinner) step2.findViewById(R.id.step2_spinner9);
		step2_spinner10=(Spinner) step2.findViewById(R.id.step2_spinner10);
		step2_spinner11=(Spinner) step2.findViewById(R.id.step2_spinner11);
		step2_spinner12=(Spinner) step2.findViewById(R.id.step2_spinner12);
		step2_spinner13=(Spinner) step2.findViewById(R.id.step2_spinner13);
		
		step2_e1=(EditText) step2.findViewById(R.id.step2_e1);
		step2_e2=(EditText) step2.findViewById(R.id.step2_e2);
		step2_e3=(EditText) step2.findViewById(R.id.step2_e3);
		step2_e4=(EditText) step2.findViewById(R.id.step2_e4);
		step2_e5=(EditText) step2.findViewById(R.id.step2_e5);
		step2_e6=(EditText) step2.findViewById(R.id.step2_e6);
		step2_e7=(EditText) step2.findViewById(R.id.step2_e7);
		step2_e8=(EditText) step2.findViewById(R.id.step2_e8);
		step2_e9=(EditText) step2.findViewById(R.id.step2_e9);
		step2_e10=(EditText) step2.findViewById(R.id.step2_e10);
		step2_e11=(EditText) step2.findViewById(R.id.step2_e11);
		step2_e12=(EditText) step2.findViewById(R.id.step2_e12);
		step2_e13=(EditText) step2.findViewById(R.id.step2_e13);
	}
	private void MyViewFun(){
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		step1_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		step1_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		step1_adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		step2_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		step2_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		step1_adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter1);
		step1_spinner2.setAdapter(step1_adapter2);
		step1_spinner3.setAdapter(step1_adapter3);
		step1_spinner4.setAdapter(step1_adapter4);
		step1_spinner5.setAdapter(step1_adapter5);
		step2_spinner1.setAdapter(step2_adapter1);
		step2_spinner2.setAdapter(step2_adapter);
		step2_spinner3.setAdapter(step2_adapter1);
		step2_spinner4.setAdapter(step2_adapter);
		step2_spinner5.setAdapter(step2_adapter1);
		step2_spinner6.setAdapter(step2_adapter);
		step2_spinner7.setAdapter(step2_adapter);
		step2_spinner8.setAdapter(step2_adapter);
		step2_spinner9.setAdapter(step2_adapter);
		step2_spinner10.setAdapter(step2_adapter);
		step2_spinner11.setAdapter(step2_adapter);
		step2_spinner12.setAdapter(step2_adapter);
		step2_spinner13.setAdapter(step2_adapter);
		nextBtn.setOnClickListener(this);
		step2_nextBtn.setOnClickListener(this);
		step2_backBtn.setOnClickListener(this);
		step3_backBtn.setOnClickListener(this);
		step3_nextBtn.setOnClickListener(this);
		step4_backBtn.setOnClickListener(this);
		step4_nextBtn.setOnClickListener(this);
		step2_spinner1.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner2.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner3.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner4.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner5.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner6.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner7.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner8.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner9.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner10.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner11.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner12.setOnItemSelectedListener(new step2SpinnerListener());
		step2_spinner13.setOnItemSelectedListener(new step2SpinnerListener());
		step3_radopGroup1.setOnCheckedChangeListener(new step3Radio1Listener());
		p_backhome_btn.setOnClickListener(new BackHomeListener());
		
	}
	class step3Radio1Listener implements RadioGroup.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if(checkedId == R.id.radio_toubao){
				toubao_flag = 1; 
				value_jiaoqiangxian.setText("￥950");
				value_chechuanshui.setText("￥450");
				value_step4_jiaoqiang.setText("￥950");
				value_step4_chechuanshui.setText("￥450");
				value_step4_count.setText("￥"+(value_count+1400));
				value_jiaoche.setText("交强险和车船税：￥1400");
				value_step4_yuan.setText("￥10060");
				value_step4_jieshen.setText("￥"+(10060-(value_count+1400)));
				
			}else if(checkedId == R.id.radio_butoubao){
				toubao_flag = 0; 
				value_jiaoqiangxian.setText("￥0");
				value_chechuanshui.setText("￥0");
				value_step4_jiaoqiang.setText("￥0");
				value_step4_chechuanshui.setText("￥0");
				value_step4_count.setText("￥"+value_count+0);
				value_jiaoche.setText("交强险和车船税：￥0");
				value_step4_yuan.setText("￥10060");
				value_step4_jieshen.setText("￥"+(10060-(value_count+0)));
			}
		}

	

		
	}
	class BackHomeListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(CarInsuranceMainActivity.this,PropertyInsuranceMainActivity.class);
		    startActivity(intent);
		    finish();
		}
		
	}
	private void countFun(){
		String se1=step2_e1.getText().toString();
		String se2=step2_e2.getText().toString();
		String se3=step2_e3.getText().toString();
		String se4=step2_e4.getText().toString();
		String se5=step2_e5.getText().toString();
		String se6=step2_e6.getText().toString();
		String se7=step2_e7.getText().toString();
		String se8=step2_e8.getText().toString();
		String se9=step2_e9.getText().toString();
		String se10=step2_e10.getText().toString();
		String se11=step2_e11.getText().toString();
		String se12=step2_e12.getText().toString();
		String se13=step2_e13.getText().toString();
		
	
		e1=Integer.parseInt(se1.substring(1, se1.length()));
		e2=Integer.parseInt(se2.substring(1, se2.length()));
		e3=Integer.parseInt(se3.substring(1, se3.length()));
		e4=Integer.parseInt(se4.substring(1, se4.length()));
		e5=Integer.parseInt(se5.substring(1, se5.length()));
		e6=Integer.parseInt(se6.substring(1, se6.length()));
		e7=Integer.parseInt(se7.substring(1, se7.length()));
		e8=Integer.parseInt(se8.substring(1, se8.length()));
		e9=Integer.parseInt(se9.substring(1, se9.length()));
		e10=Integer.parseInt(se10.substring(1, se10.length()));
		e11=Integer.parseInt(se11.substring(1, se11.length()));
		e12=Integer.parseInt(se12.substring(1, se12.length()));
		e13=Integer.parseInt(se13.substring(1, se13.length()));
		value_count=e1+e2+e3+e4+e5+e6+e7+e8+e9+e10+e11+e12+e13;
		value_shangyexian.setText("￥"+value_count);
		value_step4_shang.setText("￥"+value_count);
		
	}
	class step2SpinnerListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			switch(arg0.getId()){
			case R.id.step2_spinner1:
				step2_e1.setText(str1[arg2+1]);
				break;
			case R.id.step2_spinner2:
				if(arg2 == 0){
					step2_e2.setText("￥0");
				}else{
				step2_e2.setText(str2[arg2+1]);
				}
				break;
			case R.id.step2_spinner3:
				step2_e3.setText(str1[arg2]);
				break;
			case R.id.step2_spinner4:
				if(arg2 == 0){
					step2_e4.setText("￥0");
				}else{
				step2_e4.setText(str2[arg2+3]);
				}
				break;
			case R.id.step2_spinner5:
				step2_e5.setText(str1[arg2+2]);
				break;
			case R.id.step2_spinner6:
				if(arg2 == 0){
					step2_e6.setText("￥0");
				}else{
				step2_e6.setText(str2[arg2+5]);
				}
				break;
			case R.id.step2_spinner7:
				if(arg2 == 0){
					step2_e7.setText("￥0");
				}else{
				step2_e7.setText(str2[arg2+2]);
				}
				break;
			case R.id.step2_spinner8:
				if(arg2 == 0){
					step2_e8.setText("￥0");
				}else{
				step2_e8.setText(str2[arg2+4]);
				}
				break;
			case R.id.step2_spinner9:
				if(arg2 == 0){
					step2_e9.setText("￥0");
				}else{
				step2_e9.setText(str2[arg2+5]);
				}
				break;
			case R.id.step2_spinner10:
				if(arg2 == 0){
					step2_e10.setText("￥0");
				}else{
				step2_e10.setText(str2[arg2+4]);
				}
				break;
			case R.id.step2_spinner11:
				if(arg2 == 0){
					step2_e11.setText("￥0");
				}else{
				step2_e11.setText(str2[arg2+7]);
				}
				break;
			case R.id.step2_spinner12:
				if(arg2 == 0){
					step2_e12.setText("￥0");
				}else{
				step2_e12.setText(str2[arg2+6]);
				}
				break;
			case R.id.step2_spinner13:
				if(arg2 == 0){
					step2_e13.setText("￥0");
				}else{
				step2_e13.setText(str2[arg2+3]);
				}
				break;
			}
			countFun();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			if(back_flag == 0){
				Intent intent = new Intent(CarInsuranceMainActivity.this,PropertyInsuranceMainActivity.class);
				startActivity(intent);
				finish();
			}else if(back_flag == 1){
				no1.removeAllViews();
				no1.addView(step1);
				back_flag = 0;
			}else if(back_flag == 2){
				no1.removeAllViews();
				no1.addView(step2);
				back_flag = 1;
			}else if(back_flag == 3){
				no1.removeAllViews();
				no1.addView(step3);
				back_flag = 2;
			}else if(back_flag == 4){
				no1.removeAllViews();
				no1.addView(step4);
				h_btn2.setBackgroundResource(R.drawable.leftbuttonbg);
				h_btn1.setBackgroundResource(R.drawable.bj_tag2);
				h_btn3.setBackgroundResource(R.drawable.leftbuttonbg);
				h_btn4.setBackgroundResource(R.drawable.leftbuttonbg);
				h_btn5.setBackgroundResource(R.drawable.leftbuttonbg);
				h_btn3.setTextColor(Color.parseColor("#808080"));
				h_btn1.setTextColor(Color.parseColor("#FFFFFF"));
				h_btn2.setTextColor(Color.parseColor("#808080"));
				h_btn4.setTextColor(Color.parseColor("#808080"));
				h_btn5.setTextColor(Color.parseColor("#808080"));
				back_flag = 3;
			}else if(back_flag == 5){
				guo_tab5();
				back_flag = 4;
			}
			break;
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == nextBtn){
			back_flag = 1;
			no1.removeAllViews();
			no1.addView(step2);
		}else if(v == step2_backBtn){
			back_flag = 0;
			no1.removeAllViews();
			no1.addView(step1);
		}else if(v == step2_nextBtn){
			back_flag = 2;
			no1.removeAllViews();
			no1.addView(step3);
		}else if(v == step3_backBtn){
			back_flag = 1;
			no1.removeAllViews();
			no1.addView(step2);
		}else if(v == step3_nextBtn){
			back_flag = 3;
			value_shangyexian.setText("￥"+value_count);
			value_step4_shang.setText("￥"+value_count);
			if(toubao_flag == 1){
				toubao_flag = 1; 
				value_jiaoqiangxian.setText("￥950");
				value_chechuanshui.setText("￥450");
				value_step4_jiaoqiang.setText("￥950");
				value_step4_chechuanshui.setText("￥450");
				value_step4_count.setText("￥"+(value_count+1400));
				value_jiaoche.setText("交强险和车船税：￥1400");
				value_step4_yuan.setText("￥10060");
				value_step4_jieshen.setText("￥"+(10060-(value_count+1400)));
				
			}else if(toubao_flag == 0){
				toubao_flag = 0; 
				value_jiaoqiangxian.setText("￥0");
				value_chechuanshui.setText("￥0");
				value_step4_jiaoqiang.setText("￥0");
				value_step4_chechuanshui.setText("￥0");
				value_step4_count.setText("￥"+(value_count+0));
				value_jiaoche.setText("交强险和车船税：￥0");
				value_step4_yuan.setText("￥10060");
				value_step4_jieshen.setText("￥"+(10060-(value_count+0)));
			}
			no1.removeAllViews();
			no1.addView(step4);
		}else if(v == step4_backBtn){
			back_flag = 2;
			no1.removeAllViews();
			no1.addView(step3);
		}else if(v == step4_nextBtn){
			guo_tab5();
			
		}
	}
	// 确认投保
	public void guo_tab5(){
		back_flag = 4;
		h_btn3.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn2.setBackgroundResource(R.drawable.bj_tag2);
		h_btn1.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn4.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn5.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn3.setTextColor(Color.parseColor("#808080"));
		h_btn2.setTextColor(Color.parseColor("#FFFFFF"));
		h_btn1.setTextColor(Color.parseColor("#808080"));
		h_btn4.setTextColor(Color.parseColor("#808080"));
		h_btn5.setTextColor(Color.parseColor("#808080"));
		
		
	/*	contents = (LinearLayout)findViewById(R.id.part2);
		contents.removeAllViews();*/
		no1.removeAllViews();
		
		LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		guo_tab5 = (FrameLayout)lay.inflate(R.layout.guo_tab5, null);
		final LinearLayout guoLayout1 = (LinearLayout)guo_tab5.findViewById(R.id.guo_linear1);
		final LinearLayout guobtnlayout = (LinearLayout)guo_tab5.findViewById(R.id.guo_btnlayout);
					
		guoLayout1.setVisibility(View.VISIBLE);
		guobtnlayout.setVisibility(View.INVISIBLE);
					
					Button guo_btn1 =(Button)guo_tab5.findViewById(R.id.guo_btn1);
					Button guo_btn2 =(Button)guo_tab5.findViewById(R.id.guo_btn2);
					Button guo_btn3 =(Button)guo_tab5.findViewById(R.id.guo_btn3);			
					Button guo_btn4 =(Button)guo_tab5.findViewById(R.id.guo_btn4);
					guo_image =(ImageView)guo_tab5.findViewById(R.id.guo_image);
					guo_image.setImageBitmap(bb);
					guo_btn1.setOnClickListener(new View.OnClickListener() {																		
						@Override
						public void onClick(View v) {
							guoLayout1.setVisibility(View.INVISIBLE);
							guo_tab5.requestFocus();
							guobtnlayout.setVisibility(View.VISIBLE);
//							WritePath write = new WritePath(FreeInsuranceActivity.this);
//							write.setFocusable(true);
							if(write!=null){
								write.clearFocus();
								write =null;
							}
							write = new WritePath(CarInsuranceMainActivity.this);
							guo_tab5.addView(write,1);
							guo_tab5.invalidate();
							
						}
					});
					guo_btn2.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							payInfo();
//							part2.setVisibility(View.VISIBLE);
//							contents.setVisibility(View.GONE);
//							part2.removeAllViews();
//							LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//							part2.addView((LinearLayout)lay.inflate(R.layout.sub_layout, null));
//							contents=(LinearLayout) part2.findViewById(R.id.contents);
//							right_new = (LinearLayout)  part2.findViewById(R.id.contents);
//							mViewTitle1 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.cusinfo);
//							mViewTitle2 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.insureItem);
//							mViewTitle3 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.auth);
//							mViewTitle4 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.notify);
//							mViewTitle5 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.payinfo);
//							mViewTitle6 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.sure);
//							mViewTitle1.setOnClickListener(FreeInsuranceActivity.this);
//							mViewTitle2.setOnClickListener(FreeInsuranceActivity.this);
//							mViewTitle3.setOnClickListener(FreeInsuranceActivity.this);
//							mViewTitle4.setOnClickListener(FreeInsuranceActivity.this);
//							mViewTitle5.setOnClickListener(FreeInsuranceActivity.this);
//							mViewTitle6.setOnClickListener(FreeInsuranceActivity.this);
//							FreeInsuranceActivity.this.payInfo();
						}
					});
					guo_btn3.setOnClickListener(new View.OnClickListener() {
			
						private String getPhotoFileName() {  
					        Date date = new Date(System.currentTimeMillis());  
					        SimpleDateFormat dateFormat = new SimpleDateFormat(  
					                "'IMG'_yyyyMMdd_HHmmss");  
					        return dateFormat.format(date) + ".jpg";  
					    } 
							@Override
							public void onClick(View v) {
								guo_tab5.getChildAt(1).setDrawingCacheEnabled(true);
								guo_tab5.getChildAt(1).buildDrawingCache();
								b = guo_tab5.getChildAt(1).getDrawingCache();
								MyApplication.getInstance().setBitmap1(b);
								File mCurrentPhotoFile;//照相机拍照得到的图片 
								String SCREEN_SHOTS_LOCATION = Environment
										.getExternalStorageDirectory()
										+ "/baoqi/baodan";
								File f = new File(SCREEN_SHOTS_LOCATION);
					            f.mkdirs();// 创建照片的存储目录  
					            mCurrentPhotoFile = new File(f, getPhotoFileName());// 给新照的照片文件命名  
					            MyApplication.getInstance().setmCurrentPhotoFile(mCurrentPhotoFile);
//								String fileName = "sbsc" + System.currentTimeMillis() + ".png";
								Bitmap bitmap = BitmapFactory.decodeStream(CarInsuranceMainActivity.this.getResources()
										.openRawResource(R.drawable.cx_xinxi));
								MyApplication.getInstance().setBitmap2(bitmap);
								canvas = new CanvasWriteActivity(MyApplication.getInstance().getBitmap2(), MyApplication.getInstance().getBitmap1(), mCurrentPhotoFile, CarInsuranceMainActivity.this
										,guo_tab5,guo_image,progressdialog);
								Thread thread = new Thread(canvas);
								thread.start();
								
								guoLayout1.setVisibility(View.VISIBLE);
								guobtnlayout.setVisibility(View.INVISIBLE);
								
								progressdialog.setMessage("系统正在处理中，请稍等");
					            progressdialog.show();
								
					            guo_tab5.setVisibility(View.INVISIBLE);
							}
					});
					guo_btn4.setOnClickListener(new View.OnClickListener() {
			
						@Override
						public void onClick(View v) {
							guo_tab5.removeViewAt(1);
							if(write!=null){
								write.clearFocus();
								write =null;
							}
							write = new WritePath(CarInsuranceMainActivity.this);
							guo_tab5.addView(write,1);
							guo_tab5.invalidate();
						}
					});
					
					no1.addView(guo_tab5);
	}
	
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (readWaitProgressDialog != null) {
					readWaitProgressDialog.dismiss();
				}
				View view = ((LayoutInflater) CarInsuranceMainActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.shua_card_layout, null);
				LayoutParams params = new LayoutParams(no1.getWidth(), no1.getHeight());
				view.setLayoutParams(params);
				no1.addView(view);
				Button button = (Button) CarInsuranceMainActivity.this
						.findViewById(R.id.confirm);
				break;
			case 2:
				no1.removeAllViews();
				View view1 = ((LayoutInflater) CarInsuranceMainActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.succes_layout, null);
				LayoutParams param = new LayoutParams(no1.getWidth(), no1.getHeight());
				view1.setLayoutParams(param);
				no1.addView(view1);
				Button btn = (Button) CarInsuranceMainActivity.this
						.findViewById(R.id.confirm);
				btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(CarInsuranceMainActivity.this,
								PropertyInsuranceMainActivity.class);
						CarInsuranceMainActivity.this.startActivity(intent);
						CarInsuranceMainActivity.this.finish();
					}
				});

				break;
			}
		}
	};
	
	public void payInfo(){
		back_flag = 5;
		h_btn3.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn5.setBackgroundResource(R.drawable.bj_tag2);
		h_btn1.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn4.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn2.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn3.setTextColor(Color.parseColor("#808080"));
		h_btn5.setTextColor(Color.parseColor("#FFFFFF"));
		h_btn1.setTextColor(Color.parseColor("#808080"));
		h_btn4.setTextColor(Color.parseColor("#808080"));
		h_btn2.setTextColor(Color.parseColor("#808080"));
		
		
		no1.removeAllViews();
		LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout  guo_tab6= (LinearLayout)lay.inflate(R.layout.pay_layout, null);
		
		LayoutParams params = new LayoutParams(no1.getWidth(), no1.getHeight());
		guo_tab6.setLayoutParams(params);
		no1.addView(guo_tab6);
		Button btn = (Button) guo_tab6.findViewById(R.id.zhifu);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				no1.removeAllViews();
				View view = ((LayoutInflater) CarInsuranceMainActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.next1_layout, null);
				LayoutParams params = new LayoutParams(no1.getWidth(), no1.getHeight());
				view.setLayoutParams(params);
				no1.addView(view);
				Button view1 = (Button) CarInsuranceMainActivity.this
						.findViewById(R.id.carsh);
				view1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						no1.removeAllViews();
						View view = ((LayoutInflater) CarInsuranceMainActivity.this
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
								.inflate(R.layout.wait_layout, null);
						LayoutParams params = new LayoutParams(no1.getWidth(), no1.getHeight());
						view.setLayoutParams(params);
						no1.addView(view);
						TextView view2 = (TextView) CarInsuranceMainActivity.this
								.findViewById(R.id.ka);
						view2.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								no1.removeAllViews();
								View view = ((LayoutInflater) CarInsuranceMainActivity.this
										.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
										.inflate(R.layout.shua_card_layout,
												null);
								LayoutParams params = new LayoutParams(no1.getWidth(), no1.getHeight());
								view.setLayoutParams(params);
								no1.addView(view);
								TextView view2 = (TextView) CarInsuranceMainActivity.this
										.findViewById(R.id.ka);
								view2.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										no1.removeAllViews();
										View view = ((LayoutInflater) CarInsuranceMainActivity.this
												.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
												.inflate(
														R.layout.confirm_layout,
														null);
										LayoutParams params = new LayoutParams(no1.getWidth(), no1.getHeight());
										view.setLayoutParams(params);
										no1.addView(view);
										Button button = (Button) CarInsuranceMainActivity.this
												.findViewById(R.id.confirm);
										Spinner s1 = (Spinner) CarInsuranceMainActivity.this
												.findViewById(R.id.s1);
										Spinner s2 = (Spinner) CarInsuranceMainActivity.this
												.findViewById(R.id.s2);
										ArrayAdapter<String> arrayadapter = new ArrayAdapter<String>(
												CarInsuranceMainActivity.this,
												android.R.layout.simple_dropdown_item_1line,
												cardtype);
										s1.setAdapter(arrayadapter);
										arrayadapter = new ArrayAdapter<String>(
												CarInsuranceMainActivity.this,
												android.R.layout.simple_dropdown_item_1line,
												bank);
										s2.setAdapter(arrayadapter);
										button.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												new Thread() {
													public void run() {
														try {
															sleep(3000);
															handler.sendEmptyMessage(1);
															handler.sendEmptyMessage(2);
														} catch (InterruptedException e) {
															e.printStackTrace();
														}
													}
												}.start();
												confirmShowWaitDialog();
											}
										});
									}

								});
							}
						});
					}
				});
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		if(bb != null){
			bb.recycle();
			bb = null;
		}
		
		if(CanvasWriteActivity.newb != null){
			CanvasWriteActivity.newb.recycle();
			CanvasWriteActivity.newb = null;
		}
		
		if(WritePath.mBitmap!= null){
			WritePath.mBitmap.recycle();
			WritePath.mBitmap = null;
		}
		
		if(WritePath.mCanvas!= null){
			WritePath.mCanvas = null;
		}
		System.gc();
		super.onDestroy();
	}
}
