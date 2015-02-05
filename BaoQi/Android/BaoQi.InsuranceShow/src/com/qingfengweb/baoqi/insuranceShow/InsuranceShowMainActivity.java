package com.qingfengweb.baoqi.insuranceShow;

import com.qingfengweb.baoqi.collectInfo.CollectInfoMainActivity;
import com.qingfengweb.baoqi.gereninfo.PrivateMessage;
import com.qingfengweb.baoqi.hospitalquery.HospitalQuery;
import com.qingfengweb.baoqi.insuranceShow.ext.ImageAdapter;
import com.qingfengweb.baoqi.insuranceShow.R;
import com.qingfengweb.baoqi.mytask.MyTaskMainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

public class InsuranceShowMainActivity extends Activity {
	
	
	
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
	private String[] perforinfo ={
			 "�㱾�µ�Ӷ������Ϊ50000Ԫ�����Ŷ���������5"
			,"������Ӷ������ǰ����Ϊ��"
			,"�Ź��� 100000Ԫ����С�� 88888Ԫ�������� 66666"
			,"�����Ŷӳɽ�������"
			,"������ 2������С�� 4���������� 3��"
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
				intent.setClass(InsuranceShowMainActivity.this, IntroduceActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
			}
		});
        listview2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(InsuranceShowMainActivity.this, MyTaskMainActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
			}
		});
		//����GridViewԪ�ص���Ӧ
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Intent intent = new Intent();
				switch(position){ 

				case 0: intent.setClass(InsuranceShowMainActivity.this, ProductCenter.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;
				case 1: intent.setClass(InsuranceShowMainActivity.this, PlanActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;	
				case 2: intent.setClass(InsuranceShowMainActivity.this, InsureActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;			
				case 3: intent.setClass(InsuranceShowMainActivity.this, CollectInfoMainActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;
				case 4:
					intent.setClass(InsuranceShowMainActivity.this, MyTaskMainActivity.class);
					InsuranceShowMainActivity.this.startActivity(intent);
					finish();
					break;
				case 5:
					intent.setClass(InsuranceShowMainActivity.this, PrivateMessage.class);
					InsuranceShowMainActivity.this.startActivity(intent);

					finish();
					break;
		
				case 6: intent.setClass(InsuranceShowMainActivity.this, CalendarActivity.class);

				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;
				case 8: intent.setClass(InsuranceShowMainActivity.this, CustomerActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;
				case 9:
					intent.setClass(InsuranceShowMainActivity.this, HospitalQuery.class);
					InsuranceShowMainActivity.this.startActivity(intent);
					finish();
					break;
				case 7: intent.setClass(InsuranceShowMainActivity.this, CardSearchActivity.class);//������ѯ
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				return ;
				case 10: intent.setClass(InsuranceShowMainActivity.this, SalesmanshipActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;
				case 11: intent.setClass(InsuranceShowMainActivity.this, NoticeActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;
				case 12: intent.setClass(InsuranceShowMainActivity.this, LetterActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;
			
				case 13: intent.setClass(InsuranceShowMainActivity.this, SupportActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;
				case 14: intent.setClass(InsuranceShowMainActivity.this, IntroduceActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;
				case 15: intent.setClass(InsuranceShowMainActivity.this, JoinLifeActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;
				case 16: intent.setClass(InsuranceShowMainActivity.this, PetitionLetterActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
				break;

				case 17: intent.setClass(InsuranceShowMainActivity.this, InstructionActivity.class);
				InsuranceShowMainActivity.this.startActivity(intent);
				InsuranceShowMainActivity.this.finish();
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
						InsuranceShowMainActivity.this.finish();
						android.os.Process.killProcess(android.os.Process.myPid());
						
					}
				}).setNegativeButton("��", null).show();
	  }
	  return true;
	}

}
