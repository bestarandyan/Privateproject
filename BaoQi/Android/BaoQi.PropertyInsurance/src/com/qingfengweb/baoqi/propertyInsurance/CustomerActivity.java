package com.qingfengweb.baoqi.propertyInsurance;
import java.util.ArrayList;
import java.util.List;
import com.qingfengweb.baoqi.bean.CustomerBean;
import com.qingfengweb.baoqi.propertyInsurance.ext.CustomerAdapter;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
public class CustomerActivity extends Activity implements OnClickListener {
	private TextView t1 = null;
	private TextView t2 = null;
	private TextView t3 = null;
	private TextView t4 = null;
	private TextView t5 = null;
	private CustomerAdapter adapter=null;
	private ListView listView=null;
	private Button query=null;
	private Button clear=null;
	private int tab=1;
	private Spinner s1=null;
	private Spinner s2=null;
	private Spinner s3=null;
	private Spinner s4=null;
	private Spinner s5=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.customer_list_layout);
		t1 = (TextView) this.findViewById(R.id.search_1);
		t2 = (TextView) this.findViewById(R.id.search_2);
		t3 = (TextView) this.findViewById(R.id.search_3);
		t4 = (TextView) this.findViewById(R.id.search_4);
		t5 = (TextView) this.findViewById(R.id.search_5);
		t1.setBackgroundResource(R.drawable.single_tag01_on);
		t1.setTextColor(Color.BLACK);
		listView=(ListView) this.findViewById(R.id.list2);
		adapter=new CustomerAdapter(this,getList());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new NewsListItemListener());
		adapter.notifyDataSetChanged();
		t1.setOnClickListener(this);
		t2.setOnClickListener(this);
		t3.setOnClickListener(this);
		t4.setOnClickListener(this);
		tab=1;
		t5.setOnClickListener(this);
	}
	private class NewsListItemListener implements OnItemClickListener {
		public NewsListItemListener() {
		
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
//			 getList().get(position);
//			setContentView(R.layout.query_detail_layout); 
//			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
//			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
//			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
//			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
//			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
//			t1.setBackgroundResource(R.drawable.single_tag01_on);
//			t1.setOnClickListener(CustomerActivity.this);
//			t2.setOnClickListener(CustomerActivity.this);
//			t3.setOnClickListener(CustomerActivity.this);
//			t4.setOnClickListener(CustomerActivity.this);
//			t5.setOnClickListener(CustomerActivity.this);
////			confirm = (Button)findViewById(R.id.confirm);
//			id = 1;
////			confirm.setOnClickListener(CardSearchActivity.this);	
//			Intent intent=new Intent(CustomerActivity.this,CustomerInfoActivity.class);
//			CustomerActivity.this.startActivity(intent);
//			CustomerActivity.this.finish();
			setContentView(R.layout.customer_layout);
			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
			t1.setBackgroundResource(R.drawable.single_tag01);
			t3.setBackgroundResource(R.drawable.single_tag01);
			t4.setBackgroundResource(R.drawable.single_tag01);
			t5.setBackgroundResource(R.drawable.single_tag01);
			t2.setBackgroundResource(R.drawable.single_tag01);
			
			int a=tab;
			switch(tab){
			case 1:
				tab=11;
				t1.setBackgroundResource(R.drawable.single_tag01_on);
				t1.setTextColor(Color.parseColor("#000000"));
				t2.setTextColor(Color.parseColor("#FFFFFF"));
				t3.setTextColor(Color.parseColor("#FFFFFF"));
				t4.setTextColor(Color.parseColor("#FFFFFF"));
				t5.setTextColor(Color.parseColor("#FFFFFF"));
				
				break;
			case 2:
				tab=12;
				t2.setBackgroundResource(R.drawable.single_tag01_on);
				t2.setTextColor(Color.parseColor("#000000"));
				t1.setTextColor(Color.parseColor("#FFFFFF"));
				t3.setTextColor(Color.parseColor("#FFFFFF"));
				t4.setTextColor(Color.parseColor("#FFFFFF"));
				t5.setTextColor(Color.parseColor("#FFFFFF"));
				break;
			case 3:
				tab=13;
				t3.setBackgroundResource(R.drawable.single_tag01_on);
				t3.setTextColor(Color.parseColor("#000000"));
				t2.setTextColor(Color.parseColor("#FFFFFF"));
				t1.setTextColor(Color.parseColor("#FFFFFF"));
				t4.setTextColor(Color.parseColor("#FFFFFF"));
				t5.setTextColor(Color.parseColor("#FFFFFF"));
				break;
			case 4:
				tab=14;
				t4.setBackgroundResource(R.drawable.single_tag01_on);
				t4.setTextColor(Color.parseColor("#000000"));
				t2.setTextColor(Color.parseColor("#FFFFFF"));
				t3.setTextColor(Color.parseColor("#FFFFFF"));
				t1.setTextColor(Color.parseColor("#FFFFFF"));
				t5.setTextColor(Color.parseColor("#FFFFFF"));
				break;
			case 5:
				tab=25;
				t5.setBackgroundResource(R.drawable.single_tag01_on);
				t5.setTextColor(Color.parseColor("#000000"));
				t2.setTextColor(Color.parseColor("#FFFFFF"));
				t3.setTextColor(Color.parseColor("#FFFFFF"));
				t4.setTextColor(Color.parseColor("#FFFFFF"));
				t1.setTextColor(Color.parseColor("#FFFFFF"));
				break;
			}
			t1.setOnClickListener(CustomerActivity.this);
			t2.setOnClickListener(CustomerActivity.this);
			t3.setOnClickListener(CustomerActivity.this);
			t4.setOnClickListener(CustomerActivity.this);
			t5.setOnClickListener(CustomerActivity.this);
		}
	};
	
	public List<CustomerBean> getList(){
		 List<CustomerBean> list=new ArrayList<CustomerBean>();
		 CustomerBean bean=new CustomerBean();
		 bean.setId("01");
		 bean.setName("柳东升");
		 bean.setPhone("13765478974");
		 bean.setCardID("6101233545345321");
		 bean.setPj("1");
		 bean.setSex("男");
		 bean.setSr("500w");
		 list.add(bean);
		 
		 bean=new CustomerBean();
		 bean.setId("02");
		 bean.setName("柳小燕");
		 bean.setPhone("186215478974");
		 bean.setCardID("6101233256345321");
		 bean.setPj("1");
		 bean.setSex("女");
		 bean.setSr("123w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("03");
		 bean.setName("杨书星");
		 bean.setPhone("13765478232");
		 bean.setCardID("6101233235645321");
		 bean.setPj("1");
		 bean.setSex("男");
		 bean.setSr("254w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("04");
		 bean.setName("杨国忠");
		 bean.setPhone("186421234536");
		 bean.setCardID("6101233256343698");
		 bean.setPj("1");
		 bean.setSex("男");
		 bean.setSr("365w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("05");
		 bean.setName("李娟丽");
		 bean.setPhone("13764864589");
		 bean.setCardID("6101233256347895");
		 bean.setPj("1");
		 bean.setSex("女");
		 bean.setSr("589w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("06");
		 bean.setName("李静欢");
		 bean.setPhone("13765434564");
		 bean.setCardID("6101233256695565");
		 bean.setPj("1");
		 bean.setSex("女");
		 bean.setSr("856w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("07");
		 bean.setName("郭小庆");
		 bean.setPhone("13765434123");
		 bean.setCardID("6101233256342546");
		 bean.setPj("1");
		 bean.setSex("男");
		 bean.setSr("356w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("08");
		 bean.setName("李冰冰");
		 bean.setPhone("13765478974");
		 bean.setCardID("6101233256354895");
		 bean.setPj("1");
		 bean.setSex("女");
		 bean.setSr("370w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("09");
		 bean.setName("柳传志");
		 bean.setPhone("13765478974");
		 bean.setCardID("61012332563256895");
		 bean.setPj("1");
		 bean.setSex("男");
		 bean.setSr("507w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("10");
		 bean.setName("周小建");
		 bean.setPhone("13765478974");
		 bean.setCardID("6101233256345875");
		 bean.setPj("1");
		 bean.setSex("男");
		 bean.setSr("356w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("11");
		 bean.setName("高圆圆");
		 bean.setPhone("13765478974");
		 bean.setCardID("6101256256345321");
		 bean.setPj("1");
		 bean.setSex("女");
		 bean.setSr("106w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("12");
		 bean.setName("周凤娇");
		 bean.setPhone("13765478974");
		 bean.setCardID("6101568956345321");
		 bean.setPj("1");
		 bean.setSex("男");
		 bean.setSr("500w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("13");
		 bean.setName("张国立");
		 bean.setPhone("13765478974");
		 bean.setCardID("6101233256256921");
		 bean.setPj("1");
		 bean.setSex("男");
		 bean.setSr("155w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("14");
		 bean.setName("杨元庆");
		 bean.setPhone("13765478974");
		 bean.setCardID("6101233252568321");
		 bean.setPj("1");
		 bean.setSex("男");
		 bean.setSr("206w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("15");
		 bean.setName("周华建");
		 bean.setPhone("13865478654");
		 bean.setCardID("6101233256346589");
		 bean.setPj("1");
		 bean.setSex("男");
		 bean.setSr("422w");
		 list.add(bean);
		 bean=new CustomerBean();
		 bean.setId("16");
		 bean.setName("周杰伦");
		 bean.setPhone("13765471253");
		 bean.setCardID("6152633256345321");
		 bean.setPj("1");
		 bean.setSex("男");
		 bean.setSr("232w");
		 list.add(bean);
		 return list;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.search_1){
			tab=1;
			setContentView(R.layout.customer_list_layout); 
			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
			t1.setBackgroundResource(R.drawable.single_tag01_on);
			t1.setOnClickListener(CustomerActivity.this);
			t2.setOnClickListener(CustomerActivity.this);
			t3.setOnClickListener(CustomerActivity.this);
			t4.setOnClickListener(CustomerActivity.this);
			t5.setOnClickListener(CustomerActivity.this);
			t1.setTextColor(Color.parseColor("#000000"));
			t2.setTextColor(Color.parseColor("#FFFFFF"));
			t3.setTextColor(Color.parseColor("#FFFFFF"));
			t4.setTextColor(Color.parseColor("#FFFFFF"));
			t5.setTextColor(Color.parseColor("#FFFFFF"));
			adapter=null;
			listView=(ListView) this.findViewById(R.id.list2);
			adapter=new CustomerAdapter(this,getList());
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new NewsListItemListener());
			adapter.notifyDataSetChanged();
		}else if(v.getId()==R.id.search_2){
			setContentView(R.layout.nocustomer_layout); 
			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
			t2.setBackgroundResource(R.drawable.single_tag01_on);	
			t1.setBackgroundResource(R.drawable.single_tag01);
			t3.setBackgroundResource(R.drawable.single_tag01);
			t4.setBackgroundResource(R.drawable.single_tag01);
			t5.setBackgroundResource(R.drawable.single_tag01);
			t2.setTextColor(Color.parseColor("#000000"));
			t1.setTextColor(Color.parseColor("#FFFFFF"));
			t3.setTextColor(Color.parseColor("#FFFFFF"));
			t4.setTextColor(Color.parseColor("#FFFFFF"));
			t5.setTextColor(Color.parseColor("#FFFFFF"));
			tab=2;
			t1.setOnClickListener(CustomerActivity.this);
			t2.setOnClickListener(CustomerActivity.this);
			t3.setOnClickListener(CustomerActivity.this);
			t4.setOnClickListener(CustomerActivity.this);
			t5.setOnClickListener(CustomerActivity.this);
			s1= (Spinner) CustomerActivity.this.findViewById(R.id.s1);
			s2= (Spinner) CustomerActivity.this.findViewById(R.id.s2);
			s3= (Spinner) CustomerActivity.this.findViewById(R.id.s3);
			s4= (Spinner) CustomerActivity.this.findViewById(R.id.s4);
			s5= (Spinner) CustomerActivity.this.findViewById(R.id.s5);
			s1.setPrompt("请选择");
		     String[] province={"准客户","客户"};
		     ArrayAdapter  adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,province);   
			s1.setAdapter(adapter);
		     adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,new String[]{"男","女"});  
		     s2.setPrompt("请选择");
		     s2.setAdapter(adapter);
			adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,new String[]{"中国","其它"});  
			s3.setPrompt("请选择");
			s3.setAdapter(adapter);
			adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,new String[]{"汉族","苗族"});  
			s4.setPrompt("请选择");
			s4.setAdapter(adapter);
			adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,new String[]{"本科","博士"});  
			s5.setPrompt("请选择");
			s5.setAdapter(adapter);
			query=(Button) this.findViewById(R.id.query);
			clear=(Button) this.findViewById(R.id.clear);
			clear.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
				
			});
			query.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new Thread(){
						public void run(){
					try {
						Thread.sleep(2000);
						handler.sendEmptyMessage(1);
						handler.sendEmptyMessage(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
						}
					}.start();
					saveShowWaitDialog();
				}
				
			});
//			adapter=null;
//			listView=(ListView) this.findViewById(R.id.list2);
//			adapter=new CustomerAdapter(this,getList());
//			listView.setAdapter(adapter);
//			listView.setOnItemClickListener(new NewsListItemListener());
//			adapter.notifyDataSetChanged();
		}else if(v.getId()==R.id.search_3){
			setContentView(R.layout.customer_list_layout); 
			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
			t3.setBackgroundResource(R.drawable.single_tag01_on);	
			t2.setBackgroundResource(R.drawable.single_tag01);
			t1.setBackgroundResource(R.drawable.single_tag01);
			t4.setBackgroundResource(R.drawable.single_tag01);
			t3.setTextColor(Color.parseColor("#000000"));
			t2.setTextColor(Color.parseColor("#FFFFFF"));
			t1.setTextColor(Color.parseColor("#FFFFFF"));
			t4.setTextColor(Color.parseColor("#FFFFFF"));
			t5.setTextColor(Color.parseColor("#FFFFFF"));
			tab=3;
			t5.setBackgroundResource(R.drawable.single_tag01);
			t1.setOnClickListener(CustomerActivity.this);
			t2.setOnClickListener(CustomerActivity.this);
			t3.setOnClickListener(CustomerActivity.this);
			t4.setOnClickListener(CustomerActivity.this);
			t5.setOnClickListener(CustomerActivity.this);
			
			adapter=null;
			listView=(ListView) this.findViewById(R.id.list2);
			adapter=new CustomerAdapter(this,getList());
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new NewsListItemListener());
			adapter.notifyDataSetChanged();
		}else if(v.getId()==R.id.search_4){
			tab=4;
			setContentView(R.layout.customer_list_layout); 
			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
			t4.setTextColor(Color.parseColor("#000000"));
			t2.setTextColor(Color.parseColor("#FFFFFF"));
			t3.setTextColor(Color.parseColor("#FFFFFF"));
			t1.setTextColor(Color.parseColor("#FFFFFF"));
			t5.setTextColor(Color.parseColor("#FFFFFF"));
			t4.setBackgroundResource(R.drawable.single_tag01_on);	
			t1.setBackgroundResource(R.drawable.single_tag01);
			t3.setBackgroundResource(R.drawable.single_tag01);
			t2.setBackgroundResource(R.drawable.single_tag01);
			t5.setBackgroundResource(R.drawable.single_tag01);
			
			t1.setOnClickListener(CustomerActivity.this);
			t2.setOnClickListener(CustomerActivity.this);
			t3.setOnClickListener(CustomerActivity.this);
			t4.setOnClickListener(CustomerActivity.this);
			t5.setOnClickListener(CustomerActivity.this);
			
			adapter=null;
			listView=(ListView) this.findViewById(R.id.list2);
			adapter=new CustomerAdapter(this,getList());
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new NewsListItemListener());
			adapter.notifyDataSetChanged();
		}else if(v.getId()==R.id.search_5){
			tab=5;
			setContentView(R.layout.h_query_customer_layout); 
			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
			t5.setTextColor(Color.parseColor("#000000"));
			t2.setTextColor(Color.parseColor("#FFFFFF"));
			t3.setTextColor(Color.parseColor("#FFFFFF"));
			t4.setTextColor(Color.parseColor("#FFFFFF"));
			t1.setTextColor(Color.parseColor("#FFFFFF"));
			t5.setBackgroundResource(R.drawable.single_tag01_on);	
			t1.setBackgroundResource(R.drawable.single_tag01);
			t3.setBackgroundResource(R.drawable.single_tag01);
			t2.setBackgroundResource(R.drawable.single_tag01);
			t4.setBackgroundResource(R.drawable.single_tag01);

			
			t1.setOnClickListener(CustomerActivity.this);
			t2.setOnClickListener(CustomerActivity.this);
			t3.setOnClickListener(CustomerActivity.this);
			t4.setOnClickListener(CustomerActivity.this);
			t5.setOnClickListener(CustomerActivity.this);
			query=(Button) this.findViewById(R.id.query);
			clear=(Button) this.findViewById(R.id.clear);
			clear.setOnClickListener(this);
			query.setOnClickListener(this);
			s1= (Spinner) CustomerActivity.this.findViewById(R.id.ss1);
			s2= (Spinner) CustomerActivity.this.findViewById(R.id.ss2);
			s3= (Spinner) CustomerActivity.this.findViewById(R.id.ss3);
			s1.setPrompt("请选择");
		     String[] province={"准客户","客户"};
		     ArrayAdapter  adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,province);   
			s1.setAdapter(adapter);
		     adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,province);  
		     String[] province1={"直接交费","委托交费"};
		       adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,province1);  
		     s2.setPrompt("请选择");
		     s2.setAdapter(adapter);
		     String[] province2={"一年","两年","三年","五年","半年"};
			adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,province2);  
			s3.setPrompt("请选择");
			s3.setAdapter(adapter);
//			adapter=null;
//			listView=(ListView) this.findViewById(R.id.list2);
//			adapter=new CustomerAdapter(this,getList());
//			listView.setAdapter(adapter);
//			listView.setOnItemClickListener(new NewsListItemListener());
//			adapter.notifyDataSetChanged();
		}else if(v.getId()==R.id.query){
			new Thread(){
				public void run(){
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
		}else if(v.getId()==R.id.clear){
//			tab=5;
//			setContentView(R.layout.h_query_customer_layout); 
//			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
//			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
//			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
//			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
//			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
//
//			t5.setBackgroundResource(R.drawable.single_tag01_on);	
//			t1.setBackgroundResource(R.drawable.single_tag01);
//			t3.setBackgroundResource(R.drawable.single_tag01);
//			t2.setBackgroundResource(R.drawable.single_tag01);
//			t4.setBackgroundResource(R.drawable.single_tag01);
//
//			
//			t1.setOnClickListener(CustomerActivity.this);
//			t2.setOnClickListener(CustomerActivity.this);
//			t3.setOnClickListener(CustomerActivity.this);
//			t4.setOnClickListener(CustomerActivity.this);
//			t5.setOnClickListener(CustomerActivity.this);
//			query=(Button) this.findViewById(R.id.query);
//			clear=(Button) this.findViewById(R.id.clear);
//			clear.setOnClickListener(this);
//			query.setOnClickListener(this);
		}
		
		
	}
	private ProgressDialog readWaitProgressDialog;
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(readWaitProgressDialog!=null){
				readWaitProgressDialog.dismiss();
				}
				break;
			case 2:
				setContentView(R.layout.customer_list_layout); 
				t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
				t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
				t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
				t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
				t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
				
				t5.setBackgroundResource(R.drawable.single_tag01_on);	
				t1.setBackgroundResource(R.drawable.single_tag01);
				t3.setBackgroundResource(R.drawable.single_tag01);
				t2.setBackgroundResource(R.drawable.single_tag01);
				t4.setBackgroundResource(R.drawable.single_tag01);
				t5.setTextColor(Color.parseColor("#000000"));
				t2.setTextColor(Color.parseColor("#FFFFFF"));
				t3.setTextColor(Color.parseColor("#FFFFFF"));
				t4.setTextColor(Color.parseColor("#FFFFFF"));
				t1.setTextColor(Color.parseColor("#FFFFFF"));
				t1.setOnClickListener(CustomerActivity.this);
				t2.setOnClickListener(CustomerActivity.this);
				t3.setOnClickListener(CustomerActivity.this);
				t4.setOnClickListener(CustomerActivity.this);
				t5.setOnClickListener(CustomerActivity.this);
				
				adapter=null;
				listView=(ListView) CustomerActivity.this.findViewById(R.id.list2);
				adapter=new CustomerAdapter(CustomerActivity.this,getList());
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new NewsListItemListener());
				adapter.notifyDataSetChanged();
				break;
			case 3:
				Toast.makeText(CustomerActivity.this, "已保存数据", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
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
	public void saveShowWaitDialog() {
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(tab==11||tab==21){
			this.setContentView(R.layout.customer_list_layout);
			t1 = (TextView) this.findViewById(R.id.search_1);
			t2 = (TextView) this.findViewById(R.id.search_2);
			t3 = (TextView) this.findViewById(R.id.search_3);
			t4 = (TextView) this.findViewById(R.id.search_4);
			t5 = (TextView) this.findViewById(R.id.search_5);
			t1.setBackgroundResource(R.drawable.single_tag01_on);
			t1.setTextColor(Color.parseColor("#000000"));
			t2.setTextColor(Color.parseColor("#FFFFFF"));
			t3.setTextColor(Color.parseColor("#FFFFFF"));
			t4.setTextColor(Color.parseColor("#FFFFFF"));
			t5.setTextColor(Color.parseColor("#FFFFFF"));
			listView=(ListView) this.findViewById(R.id.list2);
			adapter=new CustomerAdapter(this,getList());
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new NewsListItemListener());
			adapter.notifyDataSetChanged();
			t1.setOnClickListener(this);
			t2.setOnClickListener(this);
			t3.setOnClickListener(this);
			t4.setOnClickListener(this);
			tab=1;
			t5.setOnClickListener(this);
			return true;
		}else if (tab==12||tab==22){
			
		}else if(tab==13||tab==23){
			setContentView(R.layout.customer_list_layout); 
			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
			t3.setBackgroundResource(R.drawable.single_tag01_on);	
			t2.setBackgroundResource(R.drawable.single_tag01);
			t1.setBackgroundResource(R.drawable.single_tag01);
			t4.setBackgroundResource(R.drawable.single_tag01);
			t3.setTextColor(Color.parseColor("#000000"));
			t2.setTextColor(Color.parseColor("#FFFFFF"));
			t1.setTextColor(Color.parseColor("#FFFFFF"));
			t4.setTextColor(Color.parseColor("#FFFFFF"));
			t5.setTextColor(Color.parseColor("#FFFFFF"));
			tab=3;
			t5.setBackgroundResource(R.drawable.single_tag01);
			t1.setOnClickListener(CustomerActivity.this);
			t2.setOnClickListener(CustomerActivity.this);
			t3.setOnClickListener(CustomerActivity.this);
			t4.setOnClickListener(CustomerActivity.this);
			t5.setOnClickListener(CustomerActivity.this);
			
			adapter=null;
			listView=(ListView) this.findViewById(R.id.list2);
			adapter=new CustomerAdapter(this,getList());
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new NewsListItemListener());
			adapter.notifyDataSetChanged();
			return true;
		}else if(tab==14||tab==24){
			tab=4;
			setContentView(R.layout.customer_list_layout); 
			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
			t4.setTextColor(Color.parseColor("#000000"));
			t2.setTextColor(Color.parseColor("#FFFFFF"));
			t3.setTextColor(Color.parseColor("#FFFFFF"));
			t1.setTextColor(Color.parseColor("#FFFFFF"));
			t5.setTextColor(Color.parseColor("#FFFFFF"));
			t4.setBackgroundResource(R.drawable.single_tag01_on);	
			t1.setBackgroundResource(R.drawable.single_tag01);
			t3.setBackgroundResource(R.drawable.single_tag01);
			t2.setBackgroundResource(R.drawable.single_tag01);
			t5.setBackgroundResource(R.drawable.single_tag01);
			
			t1.setOnClickListener(CustomerActivity.this);
			t2.setOnClickListener(CustomerActivity.this);
			t3.setOnClickListener(CustomerActivity.this);
			t4.setOnClickListener(CustomerActivity.this);
			t5.setOnClickListener(CustomerActivity.this);
			
			adapter=null;
			listView=(ListView) this.findViewById(R.id.list2);
			adapter=new CustomerAdapter(this,getList());
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new NewsListItemListener());
			adapter.notifyDataSetChanged();
			return true;
		}else if(tab==25){
			tab=15;
			setContentView(R.layout.customer_list_layout); 
			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
			
			t5.setBackgroundResource(R.drawable.single_tag01_on);	
			t1.setBackgroundResource(R.drawable.single_tag01);
			t3.setBackgroundResource(R.drawable.single_tag01);
			t2.setBackgroundResource(R.drawable.single_tag01);
			t4.setBackgroundResource(R.drawable.single_tag01);
			t5.setTextColor(Color.parseColor("#000000"));
			t2.setTextColor(Color.parseColor("#FFFFFF"));
			t3.setTextColor(Color.parseColor("#FFFFFF"));
			t4.setTextColor(Color.parseColor("#FFFFFF"));
			t1.setTextColor(Color.parseColor("#FFFFFF"));
			t1.setOnClickListener(CustomerActivity.this);
			t2.setOnClickListener(CustomerActivity.this);
			t3.setOnClickListener(CustomerActivity.this);
			t4.setOnClickListener(CustomerActivity.this);
			t5.setOnClickListener(CustomerActivity.this);
			
			adapter=null;
			listView=(ListView) CustomerActivity.this.findViewById(R.id.list2);
			adapter=new CustomerAdapter(CustomerActivity.this,getList());
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new NewsListItemListener());
			adapter.notifyDataSetChanged();
		}else if(tab==15){
			tab=5;
			setContentView(R.layout.h_query_customer_layout); 
			t1 = (TextView) CustomerActivity.this.findViewById(R.id.search_1);
			t2 = (TextView)  CustomerActivity.this.findViewById(R.id.search_2);
			t3 = (TextView)  CustomerActivity.this.findViewById(R.id.search_3);
			t4 = (TextView)  CustomerActivity.this.findViewById(R.id.search_4);
			t5 = (TextView)  CustomerActivity.this.findViewById(R.id.search_5);
			t5.setTextColor(Color.parseColor("#000000"));
			t2.setTextColor(Color.parseColor("#FFFFFF"));
			t3.setTextColor(Color.parseColor("#FFFFFF"));
			t4.setTextColor(Color.parseColor("#FFFFFF"));
			t1.setTextColor(Color.parseColor("#FFFFFF"));
			t5.setBackgroundResource(R.drawable.single_tag01_on);	
			t1.setBackgroundResource(R.drawable.single_tag01);
			t3.setBackgroundResource(R.drawable.single_tag01);
			t2.setBackgroundResource(R.drawable.single_tag01);
			t4.setBackgroundResource(R.drawable.single_tag01);

			
			t1.setOnClickListener(CustomerActivity.this);
			t2.setOnClickListener(CustomerActivity.this);
			t3.setOnClickListener(CustomerActivity.this);
			t4.setOnClickListener(CustomerActivity.this);
			t5.setOnClickListener(CustomerActivity.this);
			query=(Button) this.findViewById(R.id.query);
			clear=(Button) this.findViewById(R.id.clear);
			clear.setOnClickListener(this);
			query.setOnClickListener(this);
			s1= (Spinner) CustomerActivity.this.findViewById(R.id.ss1);
			s2= (Spinner) CustomerActivity.this.findViewById(R.id.ss2);
			s3= (Spinner) CustomerActivity.this.findViewById(R.id.ss3);
			s1.setPrompt("请选择");
		     String[] province={"准客户","客户"};
		     ArrayAdapter  adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,province);   
			s1.setAdapter(adapter);
		     adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,province);  
		     String[] province1={"直接交费","委托交费"};
		       adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,province1);  
		     s2.setPrompt("请选择");
		     s2.setAdapter(adapter);
		     String[] province2={"一年","两年","三年","五年","半年"};
			adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,province2);  
			s3.setPrompt("请选择");
			s3.setAdapter(adapter);
			return true;
		}else{
		super.onKeyDown(keyCode, event);
		Intent intent=new Intent(this,PropertyInsuranceMainActivity.class);
		this.startActivity(intent);
		this.finish();
		return true;
		}
		return true;
	}
	
	
}
