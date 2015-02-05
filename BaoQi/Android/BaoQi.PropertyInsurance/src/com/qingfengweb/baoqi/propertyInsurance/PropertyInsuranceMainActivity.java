package com.qingfengweb.baoqi.propertyInsurance;

import com.qingfengweb.baoqi.carinsurance.CarInsuranceMainActivity;
import com.qingfengweb.baoqi.collectInfo.CollectInfoMainActivity;
import com.qingfengweb.baoqi.electroncollection.ElectronMainActivity;
import com.qingfengweb.baoqi.gereninfo.PrivateMessage;
import com.qingfengweb.baoqi.hospitalquery.HospitalQuery;
import com.qingfengweb.baoqi.mytask.MyTaskMainActivity;
import com.qingfengweb.baoqi.propertyInsurance.ext.ImageAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

public class PropertyInsuranceMainActivity extends Activity {
	
	
	
	private ListView listview1;
	private ListView listview2;
	private GridView gridView;
	
	private String[] newsinfo ={
			 "[����]�й��ڲ�������Ʊ���.......................2012-03-28"
			,"[����]�й����ٿ�չ�ۺ����������󵼹���..2012-03-25"
			,"[����]�й����ٹ��ڹ�����Ա��ְ�Ĺ���.....2012-03-12"
			,"[����]�й����ٷ�ϴǮ�������������б���..2012-03-05"
	};
	private String[] tasksinfo ={
			 "[������]��ɽ������·55����С�㹺������"
			,"[������]�����÷¤�ߴ�����������Э��"
			,"[������]������������·1555Ū��С�㹺������"
			,"[������]�����÷¤�ߴ�����������Э��"
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//ȥ���ֻ��ϵı�����
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.l_showmain);
		gridView=(GridView)findViewById(R.id.gridview);
		gridView.setAdapter(new ImageAdapter(this));
		listview1 = (ListView)findViewById(R.id.listview);
		listview1.setAdapter(new ArrayAdapter<String>(this, 
				R.layout.infoitem, R.id.text, newsinfo));
		listview1.setDivider(null);
        listview1.setDividerHeight(5);
        listview2 = (ListView)findViewById(R.id.listview2);
		listview2.setAdapter(new ArrayAdapter<String>(this, 
				R.layout.infoitem, R.id.text, tasksinfo));
		listview2.setDivider(null);
        listview2.setDividerHeight(5);
        
        listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("flag", 0);
				intent.setClass(PropertyInsuranceMainActivity.this, IntroduceActivity.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
			}
		});
        listview2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(PropertyInsuranceMainActivity.this, MyTaskMainActivity.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
			}
		});
		//����GridViewԪ�ص���Ӧ
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Intent intent = new Intent();
				switch(position){ 

				case 0: intent.setClass(PropertyInsuranceMainActivity.this, CarInsuranceMainActivity.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;
				case 1: intent.setClass(PropertyInsuranceMainActivity.this, ProductCenter.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;	
				case 2: intent.setClass(PropertyInsuranceMainActivity.this, FramlyInsuranceActivity.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;			
				case 3: intent.setClass(PropertyInsuranceMainActivity.this, ProductCenter2.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;
				case 4:
					intent.setClass(PropertyInsuranceMainActivity.this, CollectInfoMainActivity.class);
					PropertyInsuranceMainActivity.this.startActivity(intent);
					finish();
					break;
				case 5:
					intent.setClass(PropertyInsuranceMainActivity.this, MyTaskMainActivity.class);
					PropertyInsuranceMainActivity.this.startActivity(intent);

					finish();
					break;
		
				case 6: intent.setClass(PropertyInsuranceMainActivity.this, PrivateMessage.class);

				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;
				case 8: intent.setClass(PropertyInsuranceMainActivity.this, ElectronMainActivity.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;
				case 9:
					intent.setClass(PropertyInsuranceMainActivity.this, CustomerActivity.class);
					PropertyInsuranceMainActivity.this.startActivity(intent);
					finish();
					break;
				case 7: intent.setClass(PropertyInsuranceMainActivity.this, CalendarActivity.class);//������ѯ
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				return ;
				case 10: intent.setClass(PropertyInsuranceMainActivity.this, LetterActivity.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;
				case 11: intent.setClass(PropertyInsuranceMainActivity.this, SupportActivity.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;
				case 12: intent.setClass(PropertyInsuranceMainActivity.this, IntroduceActivity.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;
			
				case 13: intent.setClass(PropertyInsuranceMainActivity.this, StudyOnlineActivity.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;
				case 14: intent.setClass(PropertyInsuranceMainActivity.this, SalesmanshipActivity.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;
				case 15: intent.setClass(PropertyInsuranceMainActivity.this, InstructionActivity.class);
				PropertyInsuranceMainActivity.this.startActivity(intent);
				PropertyInsuranceMainActivity.this.finish();
				break;
				}
			}
		});
		}
	
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		  new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("�Ƿ��˳�ϵͳ��")
			.setPositiveButton("��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						PropertyInsuranceMainActivity.this.finish();
						android.os.Process.killProcess(android.os.Process.myPid());
						
					}
				}).setNegativeButton("��", null).show();
	  }
	  return true;
	}

}
