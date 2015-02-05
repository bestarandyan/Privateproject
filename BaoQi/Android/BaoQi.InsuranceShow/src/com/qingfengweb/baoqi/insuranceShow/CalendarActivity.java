package com.qingfengweb.baoqi.insuranceShow;

import java.util.ArrayList;
import java.util.List;
import com.qingfengweb.baoqi.bean.CustomerQueryDataBean;
import com.qingfengweb.baoqi.insuranceShow.ext.DataAdapter;
import com.qingfengweb.baoqi.insuranceShow.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarActivity extends Activity implements OnClickListener {
	private Button mQuery = null;
	private Button mAddview = null;
	private DataAdapter adapter = null;
	private ListView listView = null;
	private Spinner s1 = null;
	private Spinner s2 = null;
	private Spinner s3 = null;
	private Button mAdd = null;
	private Button mUpdate = null;
	private Button mDel = null;
	private Button mSave = null;
	private Button mClear = null;
	private int tab=1;
	private ProgressDialog readWaitProgressDialog;
    private Dialog dialog=null;
	private LinearLayout l1 = null;
	private LinearLayout l2 = null;
	private LinearLayout l3 = null;
	private LinearLayout l4 = null;
	private LinearLayout l5 = null;
	private LinearLayout l6 = null;
	private LinearLayout l7 = null;
	private LinearLayout l8 = null;
	private LinearLayout l9 = null;
	private LinearLayout l10 = null;
	private LinearLayout l11 = null;
	private LinearLayout l12 = null;

	private LinearLayout l13 = null;
	private LinearLayout l14 = null;
	private LinearLayout l15 = null;
	private LinearLayout l16 = null;
	private LinearLayout l17 = null;
	private LinearLayout l18 = null;
	private LinearLayout l19 = null;
	private LinearLayout l20 = null;

	private LinearLayout l21 = null;
	private LinearLayout l22 = null;
	private LinearLayout l23 = null;
	private LinearLayout l24 = null;

	private LinearLayout l25 = null;
	private LinearLayout l26 = null;

	private LinearLayout l27 = null;

	private LinearLayout l28 = null;

	private LinearLayout l29 = null;

	private LinearLayout l30 = null;

	private LinearLayout l31 = null;
	private LinearLayout l32 = null;
	private LinearLayout l33 = null;
	private LinearLayout l34 = null;
	private LinearLayout l35 = null;

	private  TextView view1=null;
	public void confirmShowWaitDialog() {
		if (readWaitProgressDialog == null) {
			readWaitProgressDialog = new ProgressDialog(this);
			readWaitProgressDialog
					.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			readWaitProgressDialog.setMessage("正在保存数据...");
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
		// 去掉手机上的标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calendar_layout);
		mQuery = (Button) this.findViewById(R.id.hquery);
		mAddview = (Button) this.findViewById(R.id.addviews);
		mAddview.setOnClickListener(this);
		mQuery.setOnClickListener(this);
		l1 = (LinearLayout) this.findViewById(R.id.layout1);
		l2 = (LinearLayout) this.findViewById(R.id.layout2);
		l3 = (LinearLayout) this.findViewById(R.id.layout3);
		l4 = (LinearLayout) this.findViewById(R.id.layout4);
		l5 = (LinearLayout) this.findViewById(R.id.layout5);
		l6 = (LinearLayout) this.findViewById(R.id.layout6);
		l7 = (LinearLayout) this.findViewById(R.id.layout7);
		l8 = (LinearLayout) this.findViewById(R.id.layout8);
		l9 = (LinearLayout) this.findViewById(R.id.layout9);
		l10 = (LinearLayout) this.findViewById(R.id.layout10);
		l11 = (LinearLayout) this.findViewById(R.id.layout11);
		l12 = (LinearLayout) this.findViewById(R.id.layout12);
		l13 = (LinearLayout) this.findViewById(R.id.layout13);
		l14 = (LinearLayout) this.findViewById(R.id.layout14);
		l15 = (LinearLayout) this.findViewById(R.id.layout15);
		l16 = (LinearLayout) this.findViewById(R.id.layout16);
		l17 = (LinearLayout) this.findViewById(R.id.layout17);
		l18 = (LinearLayout) this.findViewById(R.id.layout18);
		l19 = (LinearLayout) this.findViewById(R.id.layout19);
		l20 = (LinearLayout) this.findViewById(R.id.layout20);
		l21 = (LinearLayout) this.findViewById(R.id.layout21);
		l22 = (LinearLayout) this.findViewById(R.id.layout22);
		l23 = (LinearLayout) this.findViewById(R.id.layout23);
		l24 = (LinearLayout) this.findViewById(R.id.layout24);
		l25 = (LinearLayout) this.findViewById(R.id.layout25);
		l26 = (LinearLayout) this.findViewById(R.id.layout26);
		l27 = (LinearLayout) this.findViewById(R.id.layout27);
		l28 = (LinearLayout) this.findViewById(R.id.layout28);
		l29 = (LinearLayout) this.findViewById(R.id.layout29);
		l30 = (LinearLayout) this.findViewById(R.id.layout30);
		l31 = (LinearLayout) this.findViewById(R.id.layout31);
		l32 = (LinearLayout) this.findViewById(R.id.layout32);
		l33 = (LinearLayout) this.findViewById(R.id.layout33);
		l34 = (LinearLayout) this.findViewById(R.id.layout34);
		l35 = (LinearLayout) this.findViewById(R.id.layout35);
		// view=(CalendarView) this.findViewById(R.id.calendarView1);
		// view.setEnabled(true);
		l1.setOnClickListener(this);
		l2.setOnClickListener(this);
		l1.setOnClickListener(this);
		l3.setOnClickListener(this);
		l4.setOnClickListener(this);
		l5.setOnClickListener(this);
		l6.setOnClickListener(this);
		l7.setOnClickListener(this);
		l8.setOnClickListener(this);
		l9.setOnClickListener(this);
		l10.setOnClickListener(this);
		l11.setOnClickListener(this);
		l12.setOnClickListener(this);
		l13.setOnClickListener(this);
		l14.setOnClickListener(this);

		l15.setOnClickListener(this);
		l16.setOnClickListener(this);
		l17.setOnClickListener(this);

		l18.setOnClickListener(this);
		l19.setOnClickListener(this);

		l20.setOnClickListener(this);
		l1.setOnClickListener(this);

		l21.setOnClickListener(this);
		l22.setOnClickListener(this);

		l23.setOnClickListener(this);

		l24.setOnClickListener(this);
		l25.setOnClickListener(this);

		l26.setOnClickListener(this);
		l27.setOnClickListener(this);

		l28.setOnClickListener(this);
		l29.setOnClickListener(this);
		l30.setOnClickListener(this);
		l31.setOnClickListener(this);
		l32.setOnClickListener(this);
		l33.setOnClickListener(this);
		l34.setOnClickListener(this);
		l35.setOnClickListener(this);
		 tab=1;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(tab==1){
		Intent intent = new Intent(this, InsuranceShowMainActivity.class);
		this.startActivity(intent);
		this.finish();
		return true;
		}else if(tab==2){
			setContentView(R.layout.calendar_layout);
			mQuery = (Button) this.findViewById(R.id.hquery);
			mAddview = (Button) this.findViewById(R.id.addviews);
			mAddview.setOnClickListener(this);
			mQuery.setOnClickListener(this);
			l1 = (LinearLayout) this.findViewById(R.id.layout1);
			l2 = (LinearLayout) this.findViewById(R.id.layout2);
			l3 = (LinearLayout) this.findViewById(R.id.layout3);
			l4 = (LinearLayout) this.findViewById(R.id.layout4);
			l5 = (LinearLayout) this.findViewById(R.id.layout5);
			l6 = (LinearLayout) this.findViewById(R.id.layout6);
			l7 = (LinearLayout) this.findViewById(R.id.layout7);
			l8 = (LinearLayout) this.findViewById(R.id.layout8);
			l9 = (LinearLayout) this.findViewById(R.id.layout9);
			l10 = (LinearLayout) this.findViewById(R.id.layout10);
			l11 = (LinearLayout) this.findViewById(R.id.layout11);
			l12 = (LinearLayout) this.findViewById(R.id.layout12);
			l13 = (LinearLayout) this.findViewById(R.id.layout13);
			l14 = (LinearLayout) this.findViewById(R.id.layout14);
			l15 = (LinearLayout) this.findViewById(R.id.layout15);
			l16 = (LinearLayout) this.findViewById(R.id.layout16);
			l17 = (LinearLayout) this.findViewById(R.id.layout17);
			l18 = (LinearLayout) this.findViewById(R.id.layout18);
			l19 = (LinearLayout) this.findViewById(R.id.layout19);
			l20 = (LinearLayout) this.findViewById(R.id.layout20);
			l21 = (LinearLayout) this.findViewById(R.id.layout21);
			l22 = (LinearLayout) this.findViewById(R.id.layout22);
			l23 = (LinearLayout) this.findViewById(R.id.layout23);
			l24 = (LinearLayout) this.findViewById(R.id.layout24);
			l25 = (LinearLayout) this.findViewById(R.id.layout25);
			l26 = (LinearLayout) this.findViewById(R.id.layout26);
			l27 = (LinearLayout) this.findViewById(R.id.layout27);
			l28 = (LinearLayout) this.findViewById(R.id.layout28);
			l29 = (LinearLayout) this.findViewById(R.id.layout29);
			l30 = (LinearLayout) this.findViewById(R.id.layout30);
			l31 = (LinearLayout) this.findViewById(R.id.layout31);
			l32 = (LinearLayout) this.findViewById(R.id.layout32);
			l33 = (LinearLayout) this.findViewById(R.id.layout33);
			l34 = (LinearLayout) this.findViewById(R.id.layout34);
			l35 = (LinearLayout) this.findViewById(R.id.layout35);
			// view=(CalendarView) this.findViewById(R.id.calendarView1);
			// view.setEnabled(true);
			l1.setOnClickListener(this);
			l2.setOnClickListener(this);
			l1.setOnClickListener(this);
			l3.setOnClickListener(this);
			l4.setOnClickListener(this);
			l5.setOnClickListener(this);
			l6.setOnClickListener(this);
			l7.setOnClickListener(this);
			l8.setOnClickListener(this);
			l9.setOnClickListener(this);
			l10.setOnClickListener(this);
			l11.setOnClickListener(this);
			l12.setOnClickListener(this);
			l13.setOnClickListener(this);
			l14.setOnClickListener(this);

			l15.setOnClickListener(this);
			l16.setOnClickListener(this);
			l17.setOnClickListener(this);

			l18.setOnClickListener(this);
			l19.setOnClickListener(this);

			l20.setOnClickListener(this);
			l1.setOnClickListener(this);

			l21.setOnClickListener(this);
			l22.setOnClickListener(this);

			l23.setOnClickListener(this);

			l24.setOnClickListener(this);
			l25.setOnClickListener(this);

			l26.setOnClickListener(this);
			l27.setOnClickListener(this);

			l28.setOnClickListener(this);
			l29.setOnClickListener(this);
			l30.setOnClickListener(this);
			l31.setOnClickListener(this);
			l32.setOnClickListener(this);
			l33.setOnClickListener(this);
			l34.setOnClickListener(this);
			l35.setOnClickListener(this);
			 tab=1;
			 return true;
		}
		return true;
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
				break;
			case 2:
				setContentView(R.layout.search_calendar);
				s1 = (Spinner) CalendarActivity.this.findViewById(R.id.s1);
				s2 = (Spinner) CalendarActivity.this.findViewById(R.id.s2);
				s3 = (Spinner) CalendarActivity.this.findViewById(R.id.s3);
				String[] province = { "很重要", "重要", "一般" };
				ArrayAdapter arrayadapter = new ArrayAdapter<String>(
						CalendarActivity.this,
						android.R.layout.simple_spinner_item, province);
				s1.setAdapter(arrayadapter);
				String[] province1 = { "聚会", "商务活动", "客户生日" };
				arrayadapter = new ArrayAdapter<String>(CalendarActivity.this,
						android.R.layout.simple_spinner_item, province1);
				s2.setAdapter(arrayadapter);
				String[] province2 = { "完成", "未完成", "待处理" };
				arrayadapter = new ArrayAdapter<String>(CalendarActivity.this,
						android.R.layout.simple_spinner_item, province2);
				s3.setAdapter(arrayadapter);
				mAdd = (Button) CalendarActivity.this
						.findViewById(R.id.addcustomer);
				mUpdate = (Button) CalendarActivity.this
						.findViewById(R.id.update);
				mDel = (Button) CalendarActivity.this.findViewById(R.id.delete);
				;
				listView = (ListView) CalendarActivity.this
						.findViewById(R.id.list);
				adapter = new DataAdapter(CalendarActivity.this, getList());
				mAdd.setOnClickListener(CalendarActivity.this);
				mUpdate.setOnClickListener(CalendarActivity.this);
				mDel.setOnClickListener(CalendarActivity.this);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;
			case 3:
				Toast.makeText(CalendarActivity.this, "已删除数据",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.hquery) {
			 tab=2;
			setContentView(R.layout.search_calendar);
			s1 = (Spinner) this.findViewById(R.id.s1);
			s2 = (Spinner) this.findViewById(R.id.s2);
			s3 = (Spinner) this.findViewById(R.id.s3);
			String[] province = { "很重要", "重要", "一般" };
			ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, province);
			s1.setAdapter(arrayadapter);
			String[] province1 = { "聚会", "商务活动", "客户生日" };
			arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, province1);
			s2.setAdapter(arrayadapter);
			String[] province2 = { "完成", "未完成", "待处理" };
			arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, province2);
			s3.setAdapter(arrayadapter);
			mAdd = (Button) this.findViewById(R.id.addcustomer);
			mUpdate = (Button) this.findViewById(R.id.update);
			mDel = (Button) this.findViewById(R.id.delete);
			;
			listView = (ListView) this.findViewById(R.id.list);
			adapter = new DataAdapter(this, getList());
			mAdd.setOnClickListener(this);
			mUpdate.setOnClickListener(this);
			mDel.setOnClickListener(this);
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else if (v.getId() == R.id.addviews) {
			dialog = new Dialog(CalendarActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setLayout(400, 400);
			dialog.setContentView(R.layout.dialog_layout);
//			dialog.setContentView(R.layout.dialog_layout, new LayoutParams(400, 400));
			WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
			lp.width=700;
			dialog.show();
			Button bt=(Button) dialog.findViewById(R.id.save);
			Button bn=(Button) dialog.findViewById(R.id.clear);
			bt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Toast.makeText(CalendarActivity.this, "已保存数据", Toast.LENGTH_SHORT).show();
					
				}
				
			});
			bn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					//Toast.makeText(CalendarActivity.this, "已保存数据", Toast.LENGTH_SHORT).show();
					
				}
				
			});
		} else if (v.getId() == R.id.addcustomer) {
			setContentView(R.layout.add_customer_layout);
			s1 = (Spinner) this.findViewById(R.id.s1);
			s2 = (Spinner) this.findViewById(R.id.s2);
			String[] province = { "很重要", "重要", "一般" };
			ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, province);
			s1.setAdapter(arrayadapter);
			String[] province1 = { "聚会", "商务活动", "客户生日" };
			arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, province1);
			s2.setAdapter(arrayadapter);
			mSave = (Button) this.findViewById(R.id.save);
			mClear = (Button) this.findViewById(R.id.clear);
			mSave.setOnClickListener(this);
			mClear.setOnClickListener(this);
		} else if (v.getId() == R.id.update) {
			new Thread() {
				public void run() {
					try {
						sleep(2000);
						handler.sendEmptyMessage(1);
						handler.sendEmptyMessage(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			confirmShowWaitDialog();
		} else if (v.getId() == R.id.delete) {
			new Thread() {
				public void run() {
					try {
						sleep(2000);
						handler.sendEmptyMessage(1);
						handler.sendEmptyMessage(3);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
			}.start();
			confirmShowWaitDialog();
			readWaitProgressDialog.setMessage("正在删除数据...");
		} else if (v.getId() == R.id.save) {
			new Thread() {
				public void run() {
					try {
						sleep(2000);
						handler.sendEmptyMessage(1);
						handler.sendEmptyMessage(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			confirmShowWaitDialog();
			readWaitProgressDialog.setMessage("正在保存数据...");
		} else if (v.getId() == R.id.clear) {
			setContentView(R.layout.add_customer_layout);
			s1 = (Spinner) this.findViewById(R.id.s1);
			s2 = (Spinner) this.findViewById(R.id.s2);
			String[] province = { "很重要", "重要", "一般" };
			ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, province);
			s1.setAdapter(arrayadapter);
			String[] province1 = { "聚会", "商务活动", "客户生日" };
			arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, province1);
			s2.setAdapter(arrayadapter);
			mSave = (Button) this.findViewById(R.id.save);
			mClear = (Button) this.findViewById(R.id.clear);
			mSave.setOnClickListener(this);
			mClear.setOnClickListener(this);
		}else{
			dialog = new Dialog(CalendarActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setLayout(400, 400);
			dialog.setContentView(R.layout.dialog_layout);
//			dialog.setContentView(R.layout.dialog_layout, new LayoutParams(400, 400));
			WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
			lp.width=700;
			dialog.show();
			Button bt=(Button) dialog.findViewById(R.id.save);
			Button bn=(Button) dialog.findViewById(R.id.clear);
			Spinner s1=(Spinner) dialog.findViewById(R.id.s1);
			ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, new String[]{"客户生日","工作安排","其它记事"});
			s1.setAdapter(arrayadapter);
			Spinner s2=(Spinner) dialog.findViewById(R.id.s2);
			 arrayadapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, new String[]{"重要","一般"});
			 s2.setAdapter(arrayadapter);
			Spinner s3=(Spinner) dialog.findViewById(R.id.s3);
			 arrayadapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, new String[]{"客户事件","公司事件","个人事件"});
			 s3.setAdapter(arrayadapter);
			bt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Toast.makeText(CalendarActivity.this, "已保存数据", Toast.LENGTH_SHORT).show();
					
				}
				
			});
			bn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					//Toast.makeText(CalendarActivity.this, "已保存数据", Toast.LENGTH_SHORT).show();
					
				}
				
			});
		} 
		
		
//		else if (v.getId() == R.id.layout1) {
//			view1=(TextView)this.findViewById(R.id.v1);
//
//		} else if (v.getId() == R.id.layout2) {
//			view1=(TextView)this.findViewById(R.id.v2);
//
//		} else if (v.getId() == R.id.layout3) {
//			view1=(TextView)this.findViewById(R.id.v3);
//
//		} else if (v.getId() == R.id.layout4) {
//			view1=(TextView)this.findViewById(R.id.v4);
//
//		}
//		else if (v.getId() == R.id.layout5) {
//			view1=(TextView)this.findViewById(R.id.v5);
//
//		} else if (v.getId() == R.id.layout6) {
//			view1=(TextView)this.findViewById(R.id.v6);
//
//		} else if (v.getId() == R.id.layout7) {
//			view1=(TextView)this.findViewById(R.id.v7);
//
//		}
//
//		else if (v.getId() == R.id.layout8) {
//			view1=(TextView)this.findViewById(R.id.v8);
//
//		}
//
//		else if (v.getId() == R.id.layout9) {
//			view1=(TextView)this.findViewById(R.id.v9);
//
//		}
//
//		else if (v.getId() == R.id.layout10) {
//			view1=(TextView)this.findViewById(R.id.v10);
//
//		}
//
//		else if (v.getId() == R.id.layout11) {
//			view1=(TextView)this.findViewById(R.id.v11);
//
//		}
//
//		else if (v.getId() == R.id.layout12) {
//			view1=(TextView)this.findViewById(R.id.v12);
//
//		}
//
//		
//
//		else if (v.getId() == R.id.layout13) {
//			view1=(TextView)this.findViewById(R.id.v13);
//
//		}
//
//		else if (v.getId() == R.id.layout14) {
//			view1=(TextView)this.findViewById(R.id.v14);
//
//		}
//
//		else if (v.getId() == R.id.layout15) {
//			view1=(TextView)this.findViewById(R.id.v15);
//
//		}
//
//		else if (v.getId() == R.id.layout16) {
//			view1=(TextView)this.findViewById(R.id.v16);
//
//		}
//
//		else if (v.getId() == R.id.layout17) {
//			view1=(TextView)this.findViewById(R.id.v17);
//
//		} else if (v.getId() == R.id.layout18) {
//			view1=(TextView)this.findViewById(R.id.v18);
//
//		}
//
//		else if (v.getId() == R.id.layout19) {
//			view1=(TextView)this.findViewById(R.id.v19);
//
//		} else if (v.getId() == R.id.layout20) {
//			view1=(TextView)this.findViewById(R.id.v20);
//
//		} else if (v.getId() == R.id.layout21) {
//			view1=(TextView)this.findViewById(R.id.v21);
//
//		} else if (v.getId() == R.id.layout22) {
//			view1=(TextView)this.findViewById(R.id.v22);
//
//		} else if (v.getId() == R.id.layout23) {
//			view1=(TextView)this.findViewById(R.id.v23);
//
//		} else if (v.getId() == R.id.layout24) {
//			view1=(TextView)this.findViewById(R.id.v24);
//
//		} else if (v.getId() == R.id.layout25) {
//			view1=(TextView)this.findViewById(R.id.v25);
//
//		} else if (v.getId() == R.id.layout26) {
//			view1=(TextView)this.findViewById(R.id.v26);
//
//		} else if (v.getId() == R.id.layout27) {
//			view1=(TextView)this.findViewById(R.id.v27);
//
//		} else if (v.getId() == R.id.layout28) {
//			view1=(TextView)this.findViewById(R.id.v28);
//
//		} else if (v.getId() == R.id.layout29) {
//			view1=(TextView)this.findViewById(R.id.v29);
//
//		} else if (v.getId() == R.id.layout30) {
//			view1=(TextView)this.findViewById(R.id.v30);
//
//		} else if (v.getId() == R.id.layout31) {
//			view1=(TextView)this.findViewById(R.id.v31);
//
//		} else if (v.getId() == R.id.layout32) {
//			view1=(TextView)this.findViewById(R.id.v32);
//
//		} else if (v.getId() == R.id.layout33) {
//			view1=(TextView)this.findViewById(R.id.v33);
//
//		} else if (v.getId() == R.id.layout34) {
//			view1=(TextView)this.findViewById(R.id.v34);
//
//		} else if (v.getId() == R.id.layout35) {
//			view1=(TextView)this.findViewById(R.id.v35);
//
//		}

	}

	public List getList() {
		List<CustomerQueryDataBean> list = new ArrayList<CustomerQueryDataBean>();
		CustomerQueryDataBean bean = new CustomerQueryDataBean();
		bean.setId("1");
		bean.setAddress("上海市普陀区1555");
		bean.setFrom("系统");
		bean.setName("张国强");
		bean.setState("已完成");
		bean.setTel("021-882323");
		bean.setTopic("事故勘察");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("2");
		bean.setAddress("上海市长宁区1005");
		bean.setFrom("系统");
		bean.setName("周星杰");
		bean.setState("已完成");
		bean.setTel("021-852468");
		bean.setTopic("生日祝福");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("3");
		bean.setAddress("上海市普陀区3675");
		bean.setFrom("系统");
		bean.setName("刘国庆");
		bean.setState("已完成");
		bean.setTel("021-882347");
		bean.setTopic("事故勘察");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("4");
		bean.setAddress("上海市普陀区1215");
		bean.setFrom("系统");
		bean.setName("张丽媛");
		bean.setState("已完成");
		bean.setTel("021-884737");
		bean.setTopic("事故勘察");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("5");
		bean.setAddress("上海市普陀区0557");
		bean.setFrom("系统");
		bean.setName("李宁强");
		bean.setState("已完成");
		bean.setTel("021-824737");
		bean.setTopic("合同到期");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("6");
		bean.setAddress("上海市长宁区2124");
		bean.setFrom("系统");
		bean.setName("张挺坚");
		bean.setState("已完成");
		bean.setTel("021-824351");
		bean.setTopic("生日祝福");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("7");
		bean.setAddress("上海市宝山区4332");
		bean.setFrom("系统");
		bean.setName("马江珏");
		bean.setState("已完成");
		bean.setTel("021-824353");
		bean.setTopic("合同到期");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("8");
		bean.setAddress("上海市黄埔区1238");
		bean.setFrom("系统");
		bean.setName("李玉刚");
		bean.setState("已完成");
		bean.setTel("021-854348");
		bean.setTopic("合同到期");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("9");
		bean.setAddress("上海市普陀区3553");
		bean.setFrom("系统");
		bean.setName("鲁东升");
		bean.setState("已完成");
		bean.setTel("021-854365");
		bean.setTopic("拜访客户");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("10");
		bean.setAddress("上海市普陀区1566");
		bean.setFrom("系统");
		bean.setName("刘江强");
		bean.setState("已完成");
		bean.setTel("021-854961");
		bean.setTopic("产品反馈");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("11");
		bean.setAddress("上海市宝山区1350");
		bean.setFrom("系统");
		bean.setName("刘莉莉");
		bean.setState("已完成");
		bean.setTel("021-854967");
		bean.setTopic("生日祝福");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("12");
		bean.setAddress("上海市闵行区3242");
		bean.setFrom("系统");
		bean.setName("邓郭洪");
		bean.setState("已完成");
		bean.setTel("021-856947");
		bean.setTopic("拜访客户");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("13");
		bean.setAddress("上海市浦东区2131");
		bean.setFrom("系统");
		bean.setName("李云龙");
		bean.setState("已完成");
		bean.setTel("021-856047");
		bean.setTopic("生日祝福");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("14");
		bean.setAddress("上海市浦东区1897");
		bean.setFrom("系统");
		bean.setName("曹杰成");
		bean.setState("已完成");
		bean.setTel("021-826347");
		bean.setTopic("客户反馈");
		bean.setType("客户");
		list.add(bean);
		bean = new CustomerQueryDataBean();
		bean.setId("15");
		bean.setAddress("上海市杨浦区1235");
		bean.setFrom("系统");
		bean.setName("马龙龙");
		bean.setState("已完成");
		bean.setTel("021-826348");
		bean.setTopic("意见反馈");
		bean.setType("客户");
		list.add(bean);
		return list;

	}
}
