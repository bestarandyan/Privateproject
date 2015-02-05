package com.qingfengweb.baoqi.insuranceShow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.qingfengweb.baoqi.bean.CustomerDataBean;
import com.qingfengweb.baoqi.ext.QueryAdapter;
import com.qingfengweb.baoqi.insuranceShow.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CardSearchActivity extends Activity implements OnClickListener {
	private TextView t1 = null;
	private TextView t2 = null;
	private TextView t3 = null;
	private int tab = 1;
	private Button confirm = null;
	private ProgressDialog readWaitProgressDialog;
	private ListView listView = null;
	private TextView view = null;
	private QueryAdapter adapter = null;
	private Spinner s1 = null;
	private Spinner s2 = null;
	private Spinner s3 = null;
	private Spinner s4 = null;
	private Spinner s5 = null;
	private Spinner s6 = null;

	public void confirmShowWaitDialog() {
		if (readWaitProgressDialog == null) {
			readWaitProgressDialog = new ProgressDialog(this);
			readWaitProgressDialog
					.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			readWaitProgressDialog.setMessage("正在查询数据...");
			readWaitProgressDialog.setCancelable(true);
			readWaitProgressDialog.show();
		} else {
			readWaitProgressDialog.show();
		}

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
				setContentView(R.layout.query_list_layout);
				t1 = (TextView) CardSearchActivity.this
						.findViewById(R.id.search_1);
				t2 = (TextView) CardSearchActivity.this
						.findViewById(R.id.search_2);
				t3 = (TextView) CardSearchActivity.this
						.findViewById(R.id.search_3);
				t2.setBackgroundResource(R.drawable.single_tag01);
				t1.setBackgroundResource(R.drawable.single_tag01);
				t3.setBackgroundResource(R.drawable.single_tag01);
				if (tab == 1) {
					tab =11;
					t1.setBackgroundResource(R.drawable.single_tag01_on);
				} else if (tab == 2) {
					tab =12;
					t2.setBackgroundResource(R.drawable.single_tag01_on);
				} else if (tab == 3) {
					tab =13;
					t3.setBackgroundResource(R.drawable.single_tag01_on);
				}
				t1.setTextColor(Color.BLACK);
				t1.setOnClickListener(CardSearchActivity.this);
				t2.setOnClickListener(CardSearchActivity.this);
				t3.setOnClickListener(CardSearchActivity.this);
				listView = (ListView) findViewById(R.id.list1);
				adapter = null;
				adapter = new QueryAdapter(CardSearchActivity.this, getList());
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new NewsListItemListener());
				adapter.notifyDataSetChanged();
				
				
				break;
			}
		}
	};

	public void closeConfirmWaitDialog() {
		// if(readWaitProgressDialog !=null){
		// readWaitProgressDialog.dismiss();
		// }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.card_search_layout);
		t1 = (TextView) this.findViewById(R.id.search_1);
		t2 = (TextView) this.findViewById(R.id.search_2);
		t3 = (TextView) this.findViewById(R.id.search_3);
		t1.setBackgroundResource(R.drawable.single_tag01_on);
		t1.setOnClickListener(this);
		t2.setOnClickListener(this);
		t3.setOnClickListener(this);
		t1.setTextColor(Color.BLACK);
		confirm = (Button) this.findViewById(R.id.confirm);
		tab = 1;
		confirm.setOnClickListener(this);
		String[] province = { "分红险", "万能险", "投连险", "少儿险", "健康险", "保障险", "养老险" };
		ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, province);
		s1 = (Spinner) this.findViewById(R.id.s1);
		s1.setAdapter(arrayadapter);
		String[] province2 = { "全部", "1个月", "2个月", "半年", "一年", "两年", "三年" };
		arrayadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, province2);
		s2 = (Spinner) this.findViewById(R.id.s2);
		s2.setAdapter(arrayadapter);
		String[] province1 = { "15", "25", "35", "45", "55", "65", "75" };
		arrayadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, province1);
		s3 = (Spinner) this.findViewById(R.id.s3);
		s3.setAdapter(arrayadapter);
		s4 = (Spinner) this.findViewById(R.id.s4);
		s4.setAdapter(arrayadapter);
		s5 = (Spinner) this.findViewById(R.id.s5);
		s5.setAdapter(arrayadapter);
		s6 = (Spinner) this.findViewById(R.id.s6);
		s6.setAdapter(arrayadapter);
		// setAdapter();
	}

	public void setAdapter() {
		String[] province = { "分红险", "万能险", "投连险", "少儿险", "健康险", "保障险", "养老险" };
		ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, province);
		s1 = (Spinner) this.findViewById(R.id.s1);
		s1.setAdapter(arrayadapter);
		String[] province2 = { "全部", "1个月", "2个月", "半年", "一年", "两年", "三年" };
		arrayadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, province2);
		s2 = (Spinner) this.findViewById(R.id.s2);
		s2.setAdapter(arrayadapter);
		String[] province1 = { "15", "25", "35", "45", "55", "65", "75" };
		arrayadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, province1);
		s3 = (Spinner) this.findViewById(R.id.s3);
		s3.setAdapter(arrayadapter);
		s4 = (Spinner) this.findViewById(R.id.s4);
		s4.setAdapter(arrayadapter);
		s5 = (Spinner) this.findViewById(R.id.s5);
		s5.setAdapter(arrayadapter);
		s6 = (Spinner) this.findViewById(R.id.s6);
		s6.setAdapter(arrayadapter);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.search_1) {
			t1.setBackgroundResource(R.drawable.single_tag01_on);
			this.setContentView(R.layout.card_search_layout);
			t1 = (TextView) this.findViewById(R.id.search_1);
			t2 = (TextView) this.findViewById(R.id.search_2);
			t3 = (TextView) this.findViewById(R.id.search_3);
			t2.setBackgroundResource(R.drawable.single_tag01);
			t1.setBackgroundResource(R.drawable.single_tag01_on);
			t3.setBackgroundResource(R.drawable.single_tag01);
			t1.setTextColor(Color.BLACK);
			t1.setOnClickListener(this);
			t2.setOnClickListener(this);
			t3.setOnClickListener(this);
			tab = 1;
			view = (TextView) this.findViewById(R.id.search_23);
			view.setText("保单查询");
			confirm = (Button) this.findViewById(R.id.confirm);
			confirm.setOnClickListener(this);
			String[] province = { "分红险", "万能险", "投连险", "少儿险", "健康险", "保障险",
					"养老险" };
			ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, province);
			s1 = (Spinner) this.findViewById(R.id.s1);
			s1.setAdapter(arrayadapter);
			String[] province2 = { "全部", "1个月", "2个月", "半年", "一年", "两年", "三年" };
			arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, province2);
			s2 = (Spinner) this.findViewById(R.id.s2);
			s2.setAdapter(arrayadapter);
			String[] province1 = { "15", "25", "35", "45", "55", "65", "75" };
			arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, province1);
			s3 = (Spinner) this.findViewById(R.id.s3);
			s3.setAdapter(arrayadapter);
			s4 = (Spinner) this.findViewById(R.id.s4);
			s4.setAdapter(arrayadapter);
			s5 = (Spinner) this.findViewById(R.id.s5);
			s5.setAdapter(arrayadapter);
			s6 = (Spinner) this.findViewById(R.id.s6);
			s6.setAdapter(arrayadapter);
		} else if (v.getId() == R.id.search_2) {
			t1.setBackgroundResource(R.drawable.single_tag01_on);
			this.setContentView(R.layout.card_search_layout);
			t1 = (TextView) this.findViewById(R.id.search_1);
			t2 = (TextView) this.findViewById(R.id.search_2);
			t3 = (TextView) this.findViewById(R.id.search_3);
			t2.setBackgroundResource(R.drawable.single_tag01_on);
			t1.setBackgroundResource(R.drawable.single_tag01);
			t3.setBackgroundResource(R.drawable.single_tag01);
			t1.setOnClickListener(this);
			t2.setOnClickListener(this);
			t3.setOnClickListener(this);
			t2.setTextColor(Color.BLACK);
			tab = 2;
			view = (TextView) this.findViewById(R.id.search_23);
			view.setText("新契约状态查询");
			confirm = (Button) this.findViewById(R.id.confirm);
			confirm.setOnClickListener(this);
			String[] province = { "分红险", "万能险", "投连险", "少儿险", "健康险", "保障险", "养老险" };
			ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, province);
			s1 = (Spinner) this.findViewById(R.id.s1);
			s1.setAdapter(arrayadapter);
			String[] province2 = { "全部", "1个月", "2个月", "半年", "一年", "两年", "三年" };
			arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, province2);
			s2 = (Spinner) this.findViewById(R.id.s2);
			s2.setAdapter(arrayadapter);
			String[] province1 = { "15", "25", "35", "45", "55", "65", "75" };
			arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, province1);
			s3 = (Spinner) this.findViewById(R.id.s3);
			s3.setAdapter(arrayadapter);
			s4 = (Spinner) this.findViewById(R.id.s4);
			s4.setAdapter(arrayadapter);
			s5 = (Spinner) this.findViewById(R.id.s5);
			s5.setAdapter(arrayadapter);
			s6 = (Spinner) this.findViewById(R.id.s6);
			s6.setAdapter(arrayadapter);
		} else if (v.getId() == R.id.search_3) {
			this.setContentView(R.layout.card_search_layout);
			t1 = (TextView) this.findViewById(R.id.search_1);
			t2 = (TextView) this.findViewById(R.id.search_2);
			t3 = (TextView) this.findViewById(R.id.search_3);
			t3.setBackgroundResource(R.drawable.single_tag01_on);
			t2.setBackgroundResource(R.drawable.single_tag01);
			t1.setBackgroundResource(R.drawable.single_tag01);
			t1.setOnClickListener(this);
			t2.setOnClickListener(this);
			t3.setOnClickListener(this);
			t3.setTextColor(Color.BLACK);
			tab = 3;
			view = (TextView) this.findViewById(R.id.search_23);
			view.setText("理赔状态查询");
			confirm = (Button) this.findViewById(R.id.confirm);
			confirm.setOnClickListener(this);
//			setAdapter();
			String[] province = { "分红险", "万能险", "投连险", "少儿险", "健康险", "保障险", "养老险" };
			ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, province);
			s1 = (Spinner) this.findViewById(R.id.s1);
			s1.setAdapter(arrayadapter);
			String[] province2 = { "全部", "1个月", "2个月", "半年", "一年", "两年", "三年" };
			arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, province2);
			s2 = (Spinner) this.findViewById(R.id.s2);
			s2.setAdapter(arrayadapter);
			String[] province1 = { "15", "25", "35", "45", "55", "65", "75" };
			arrayadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, province1);
			s3 = (Spinner) this.findViewById(R.id.s3);
			s3.setAdapter(arrayadapter);
			s4 = (Spinner) this.findViewById(R.id.s4);
			s4.setAdapter(arrayadapter);
			s5 = (Spinner) this.findViewById(R.id.s5);
			s5.setAdapter(arrayadapter);
			s6 = (Spinner) this.findViewById(R.id.s6);
			s6.setAdapter(arrayadapter);
		} else if (v.getId() == R.id.confirm) {
			new Thread() {
				public void run() {
					try {
						Thread.sleep(2000);
						handler.sendEmptyMessage(1);
						handler.sendEmptyMessage(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
			confirmShowWaitDialog();
		}

	}

	public List<CustomerDataBean> getList() {
		List<CustomerDataBean> list = new ArrayList<CustomerDataBean>();
		CustomerDataBean bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("华谊兄弟");
		bean.setByPeople("甄子丹");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("华谊兄弟");
		bean.setByPeople("吴彦祖");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("华谊兄弟");
		bean.setByPeople("古天乐");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("华谊兄弟");
		bean.setByPeople("葛优优");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("联想控股");
		bean.setByPeople("柳传志");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("阿里巴巴");
		bean.setByPeople("马云云");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("soho公司");
		bean.setByPeople("张朝阳");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("太平洋投");
		bean.setByPeople("格罗斯");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("华谊兄弟");
		bean.setByPeople("吴彦祖");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("华谊兄弟");
		bean.setByPeople("古天乐");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("华谊兄弟");
		bean.setByPeople("葛优优");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("联想控股");
		bean.setByPeople("柳传志");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("阿里巴巴");
		bean.setByPeople("马云云");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("soho公司");
		bean.setByPeople("张朝阳");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("太平洋投");
		bean.setByPeople("格罗斯");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("华谊兄弟");
		bean.setByPeople("吴彦祖");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("华谊兄弟");
		bean.setByPeople("古天乐");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("华谊兄弟");
		bean.setByPeople("葛优优");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("联想控股");
		bean.setByPeople("柳传志");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("阿里巴巴");
		bean.setByPeople("马云云");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("soho公司");
		bean.setByPeople("张朝阳");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		bean = new CustomerDataBean();
		bean.setId("201201010");
		bean.setPeople("太平洋投");
		bean.setByPeople("格罗斯");
		bean.setCardState("维持有效");
		bean.setTime("10");
		bean.setBaoFei("2000w");
		bean.setStartTime("2012-01-01");
		bean.setsF("实收付");
		list.add(bean);
		return list;
	}

	private class NewsListItemListener implements OnItemClickListener {
		public NewsListItemListener() {

		}

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// getList().get(position);
			setContentView(R.layout.query_detail_layout);
			t1 = (TextView) CardSearchActivity.this.findViewById(R.id.search_1);
			t2 = (TextView) CardSearchActivity.this.findViewById(R.id.search_2);
			t3 = (TextView) CardSearchActivity.this.findViewById(R.id.search_3);
			t2.setBackgroundResource(R.drawable.single_tag01);
			t1.setBackgroundResource(R.drawable.single_tag01);
			t3.setBackgroundResource(R.drawable.single_tag01);
			if (tab == 11) {
				tab =21;
				t1.setBackgroundResource(R.drawable.single_tag01_on);
			} else if (tab == 12) {
				tab =22;
				t2.setBackgroundResource(R.drawable.single_tag01_on);
			} else if (tab == 13) {
				tab =23;
				t3.setBackgroundResource(R.drawable.single_tag01_on);
			}
			t1.setOnClickListener(CardSearchActivity.this);
			t2.setOnClickListener(CardSearchActivity.this);
			t3.setOnClickListener(CardSearchActivity.this);
			// confirm = (Button)findViewById(R.id.confirm);
//			id = 1;
			// confirm.setOnClickListener(CardSearchActivity.this);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
       if(tab==11 || tab==21){
    		this.setContentView(R.layout.card_search_layout);
    		t1 = (TextView) this.findViewById(R.id.search_1);
    		t2 = (TextView) this.findViewById(R.id.search_2);
    		t3 = (TextView) this.findViewById(R.id.search_3);
    		t1.setBackgroundResource(R.drawable.single_tag01_on);
    		t1.setOnClickListener(this);
    		t2.setOnClickListener(this);
    		t3.setOnClickListener(this);
    		t1.setTextColor(Color.BLACK);
    		confirm = (Button) this.findViewById(R.id.confirm);
    		tab = 1;
    		confirm.setOnClickListener(this);
    		String[] province = { "分红险", "万能险", "投连险", "少儿险", "健康险", "保障险", "养老险" };
    		ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
    				android.R.layout.simple_dropdown_item_1line, province);
    		s1 = (Spinner) this.findViewById(R.id.s1);
    		s1.setAdapter(arrayadapter);
    		String[] province2 = { "全部", "1个月", "2个月", "半年", "一年", "两年", "三年" };
    		arrayadapter = new ArrayAdapter<String>(this,
    				android.R.layout.simple_dropdown_item_1line, province2);
    		s2 = (Spinner) this.findViewById(R.id.s2);
    		s2.setAdapter(arrayadapter);
    		String[] province1 = { "15", "25", "35", "45", "55", "65", "75" };
    		arrayadapter = new ArrayAdapter<String>(this,
    				android.R.layout.simple_dropdown_item_1line, province1);
    		s3 = (Spinner) this.findViewById(R.id.s3);
    		s3.setAdapter(arrayadapter);
    		s4 = (Spinner) this.findViewById(R.id.s4);
    		s4.setAdapter(arrayadapter);
    		s5 = (Spinner) this.findViewById(R.id.s5);
    		s5.setAdapter(arrayadapter);
    		s6 = (Spinner) this.findViewById(R.id.s6);
    		s6.setAdapter(arrayadapter);
    		return true;
       }else if(tab==12|| tab==22){
    	   t1.setBackgroundResource(R.drawable.single_tag01_on);
			this.setContentView(R.layout.card_search_layout);
			t1 = (TextView) this.findViewById(R.id.search_1);
			t2 = (TextView) this.findViewById(R.id.search_2);
			t3 = (TextView) this.findViewById(R.id.search_3);
			t2.setBackgroundResource(R.drawable.single_tag01_on);
			t1.setBackgroundResource(R.drawable.single_tag01);
			t3.setBackgroundResource(R.drawable.single_tag01);
			t1.setOnClickListener(this);
			t2.setOnClickListener(this);
			t3.setOnClickListener(this);
			t2.setTextColor(Color.BLACK);
			tab = 2;
			view = (TextView) this.findViewById(R.id.search_23);
			view.setText("新契约状态查询");
			confirm = (Button) this.findViewById(R.id.confirm);
			confirm.setOnClickListener(this);
			setAdapter();
			return true;
       }else if(tab==13|| tab==23){
    	   this.setContentView(R.layout.card_search_layout);
			t1 = (TextView) this.findViewById(R.id.search_1);
			t2 = (TextView) this.findViewById(R.id.search_2);
			t3 = (TextView) this.findViewById(R.id.search_3);
			t3.setBackgroundResource(R.drawable.single_tag01_on);
			t2.setBackgroundResource(R.drawable.single_tag01);
			t1.setBackgroundResource(R.drawable.single_tag01);
			t1.setOnClickListener(this);
			t2.setOnClickListener(this);
			t3.setOnClickListener(this);
			t3.setTextColor(Color.BLACK);
			tab = 3;
			view = (TextView) this.findViewById(R.id.search_23);
			view.setText("理赔状态查询");
			confirm = (Button) this.findViewById(R.id.confirm);
			confirm.setOnClickListener(this);
			setAdapter();
			return true;
       }else{
		super.onKeyDown(keyCode, event);
		Intent intent = new Intent(this, InsuranceShowMainActivity.class);
		this.startActivity(intent);
		this.finish();
		return true;
       }
//       return true;
	}

}
