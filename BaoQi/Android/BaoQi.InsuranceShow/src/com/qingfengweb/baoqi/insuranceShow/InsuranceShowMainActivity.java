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
			 "[公告]中国内部控制审计报告.......................2012-03-28"
			,"[新闻]中国人寿开展综合治理销售误导工作..2012-03-25"
			,"[公告]中国人寿关于管理人员任职的公告.....2012-03-12"
			,"[新闻]中国人寿反洗钱工作获人民银行表彰..2012-03-05"
	};
	private String[] tasksinfo ={
			 "[新任务]宝山区淞滨路55号张小姐购买意向"
			,"[新任务]徐汇区梅陇七村李先生理赔协助"
			,"[新任务]普陀区凯旋北路1555弄张小姐购买意向"
			,"[新任务]徐汇区梅陇七村王先生理赔协助"
	};
	private String[] perforinfo ={
			 "你本月的佣金收入为50000元，在团队中排名第5"
			,"本月团佣金收入前三名为："
			,"张国立 100000元；洪小刚 88888元；章子怡 66666"
			,"今日团队成交订单："
			,"章子怡 2单；洪小刚 4单；范冰冰 3单"
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
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
		//单击GridView元素的响应
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
				case 7: intent.setClass(InsuranceShowMainActivity.this, CardSearchActivity.class);//保单查询
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
			.setTitle("是否退出系统？")
			.setPositiveButton("是",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						InsuranceShowMainActivity.this.finish();
						android.os.Process.killProcess(android.os.Process.myPid());
						
					}
				}).setNegativeButton("否", null).show();
	  }
	  return true;
	}

}
