package com.qingfengweb.baoqi.propertyInsurance;

import com.qingfengweb.baoqi.propertyInsurance.ext.InstructionAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class InstructionActivity extends Activity {

	private GridView gridView;
	private Button h_backhomebtn;
	private Button homebtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.l_instruction);
		
		
		h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(InstructionActivity.this, PropertyInsuranceMainActivity.class);
			  	InstructionActivity.this.startActivity(intent);
			  	InstructionActivity.this.finish();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(InstructionActivity.this, PropertyInsuranceMainActivity.class);
			  	InstructionActivity.this.startActivity(intent);
			  	InstructionActivity.this.finish();
			}
		});
		gridView = (GridView)findViewById(R.id.gridview);
		gridView.setAdapter(new InstructionAdapter(this));
		//单击GridView元素的响应

				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position,
							long id) {
						Intent intent = new Intent();
						switch(position){
						case 0: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
						InstructionActivity.this.startActivity(intent);
						InstructionActivity.this.finish();
						break;
//						case 1: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 2: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 3: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 4: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 5: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 6: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 7: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 8: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 9: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 10: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 11: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 12: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 13: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 14: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 15: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 16: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						case 17: intent.setClass(InstructionActivity.this, InstructionDemoActivity.class);
//						InstructionActivity.this.startActivity(intent);
//						InstructionActivity.this.finish();
//						break;
//						
						
						}
					}
				});
				}
			
			
			@Override

			 public boolean onKeyDown(int keyCode, KeyEvent event){

			  if(KeyEvent.KEYCODE_BACK==keyCode){
				 Intent intent = new Intent();
				 intent.setClass(InstructionActivity.this, PropertyInsuranceMainActivity.class);
				 InstructionActivity.this.startActivity(intent);
				 InstructionActivity.this.finish();
			  }
			  return true;
			}
}
