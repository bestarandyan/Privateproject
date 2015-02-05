package com.qingfengweb.baoqi.insuranceShow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.qingfengweb.baoqi.ext.GenerationAdapter;
import com.qingfengweb.baoqi.ext.LiYiListAdapter;
import com.qingfengweb.baoqi.ext.PlanDataAdapter;
import com.qingfengweb.baoqi.insuranceShow.ext.LiYiList2Adapter;
import com.qingfengweb.baoqi.insuranceShow.R;

import java.util.Calendar;
import java.util.Map;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
public class PlanActivity extends Activity implements OnClickListener{
	private TextView mTvCusInfo=null;//
	private TextView mTvType=null;
	private TextView mTvData=null;
	private TextView mTvSet=null;
	private TextView mShow=null;
	private TextView mPlan=null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int type_time=0;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	private TextView mMaintain=null;
	LinearLayout right,xiamian;
	RelativeLayout pritect_plan;
	private LayoutInflater layout;
	private EditText name_jihuan,name_baofu;
	private Button query_jihushu,time_zhizuo,time_jiezhi;
	private ListView tab_listView;
	 PAdapter padapter;
	 private Button copy,zhuan,xiugai,shanchu;
	 List<Map<String,String>> tab_list;
	//---------------guoqing
	 private TextView guoshow = null;
	private ListView listview = null; 
	private ListView listview2 = null; 
	private Button btn1 = null;
	private Button liyi_btn1,liyi_btn2,liyi_btn3;
	private LinearLayout guo_linear1;
	private LinearLayout guo_linear2;
	private Button liyi2_b1,liyi2_b2,li_xian1_btn1,li_xian1_btn2;
	private Spinner s1,s2,s3,s4,s5,liyi2s1;
	private int index = 0;
	private String[] str1 = {
			"分红险","万能险","投连险","少儿险","健康险"
			,"养老险","保障险","两全险","意外险","附加险","连生险"
			,"团体寿险","机构代理寿险"
		};
	
	
	private String[][] info1 = {
			{"1","21","4497","4497","185","80000","84385","36000","28.5"}
			,{"2","22","4485","13497","5000","65343","8700","36000","87.5"}
			,{"3","23","5497","7497","185","66000","87385","38000","58.5"}
			,{"4","24","8577","8997","155","64520","87385","33640","58.5"}
			,{"5","25","6497","4667","175","34000","87385","32500","63.5"}
			,{"6","26","4677","6597","165","23550","85635","38000","42.5"}
			,{"7","27","3497","7457","145","66000","87385","38000","35.5"}
	};
	private String[][] info2 = {
			{"464","李国华","男","1992-03-12","1","16554.00","16425.12","年","10","年交","年","3","","","","主险","1","464"}
			,{"465","王胜利","男","1982-08-22","2","6554.00","6425.12","年","10","年交","年","3","","","","意外险","1","465"}
			,{"466","张子庭","女","1990-06-04","1","18584.00","18495.12","年","10","年交","年","3","","","","主险","1","466"}
			,{"467","莫小棋","女","1988-04-18","1","16554.00","16425.12","年","10","年交","年","3","","","","健康险","1","467"}
			,{"468","吴建飞","男","1987-02-12","2","6984.00","6489.12","年","10","年交","年","3","","","","万能险","1","468"}
			,{"469","李在元","男","1991-03-27","1","20154.00","20425.12","年","10","年交","年","3","","","","保障险","1","469"}
			,{"470","许志伟","男","1980-02-14","1","9554.00","9425.10","年","10","年交","年","3","","","","分红险","1","470"}
			,{"471","肖勇波","男","1981-11-28","3","11584.00","11485.12","年","10","年交","年","3","","","","附加险","1","471"}

			,{"472","刘芊含","女","1990-09-24","1","16554.00","16425.12","年","10","年交","年","3","","","","连生险","1","472"}

			,{"473","李洋","女","1993-06-11","2","8554.00","10425.12","年","10","年交","年","3","","","","两全险","1","473"}

			
	};
	private String[] str2 = {"国寿福禄鑫尊两全保险","国寿安欣无忧两全保险"
			,"国国寿安欣无忧长期意外伤害保险","国寿附加安欣无忧提前给付重大疾病保险"
			,"国寿附加绿荫呵护少儿重大疾病保险","国寿绿荫呵护少儿两全保险"
			,"国寿松鹤颐年年金保险","国寿美满人生至尊版年金保险"
			,"国寿福禄宝宝两全保险","国寿福满一生两全保险"
			,"国寿附加重大疾病保险","国寿福禄呈祥两全保险"
			,"国寿福禄金尊两全保险","国寿新鸿泰两全保险","国寿附加康友重大疾病保险"
			,"国寿安鑫保险计划","国寿育才少儿两全保险","国寿新鸿泰金典版两全保险"
			,"国寿福禄尊享两全保险","国寿福瑞人生两全保险","国寿附加长久呵护意外伤害保险"
			,"国寿长久呵护住院费用补偿医疗保险","国寿长久呵护意外伤害费用补偿医疗保险"
			,"国寿长久呵护意外伤害定额给付医疗保险","国寿福禄满堂养老年金保险"
			,"国寿康宁定期重大疾病保险","国寿康宁终身重大疾病保险","附加关爱一生长期医疗保险"
			,"国寿关爱一生终身寿险","国寿附加定期寿险","国寿鸿盛终身寿险"
			,"国寿英才少儿两全保险","国寿附加定期寿险","国寿子女教育两全保险"
			,"国寿子女教育两全保险","国寿福星少儿两全保险","国寿附加康友重大疾病保险"
			,"国寿鸿康两全保险","国寿附加鸿康提前给付重大疾病保险","国寿智力人生两全保险"
			,"国寿鸿盈两全保险","国寿农村小额定期寿险","国寿农村小额定期寿险"
			,"国寿康馨长期护理保险","国寿安享一生两全保险","国寿瑞鑫两全保险"
			,"国寿附加瑞鑫提前给付重大疾病保险","国寿长久呵护住院定额给付医疗保险"
			,"国寿祥泰定期寿险","国寿祥泰终身寿险","国寿金彩明天两全保险"
			,"国寿金彩明天两全保险","国寿瑞丰两全保险","国寿裕丰投资连结保险"
			,"国寿鸿富两全保险","国寿新简易人身两全保险","国寿鸿丰B两全保险"
			,"国寿祥福定期寿险","国寿美满一生年金保险","国寿附加重大自然灾害意外伤害保险"
			,"国寿个人养老年金保险","国寿鸿丰两全保险","国寿美满人生年金保险"
			,"国寿鸿裕两全保险","国寿瑞祥终身寿险","国寿鸿禧年金保险","国寿瑞祥终身寿险"
			,"国寿附加安康提前给付重大疾病保险","国寿附加安泰意外伤害保险","国寿鸿丰两全保险"
			,"国寿附加安康提前给付重大疾病保险","国寿康恒重大疾病保险","国寿鸿泰两全保险"
			,"国寿鸿宇两全保险","国寿鸿鑫两全保险","国寿鸿祥两全保险","国寿鸿星少儿两全保险"
			,"国寿附加长久呵护残疾意外伤害保险","国寿鸿运少儿两全保险","国寿鸿寿年金保险"
			,"国寿鸿福相伴两全保险","国寿千禧理财两全保险","国寿关爱生命女性疾病保险"
			,"国寿关爱生命女性疾病保险","国寿金色夕阳养老年金保险","国寿99鸿福两全保险"};
		
		private String[] str32 = {"国寿福禄鑫尊两全保险","国寿安欣无忧两全保险"
				,"国国寿安欣无忧长期意外伤害保险","国寿附加安欣无忧提前给付重大疾病保险"
				,"国寿附加绿荫呵护少儿重大疾病保险","国寿绿荫呵护少儿两全保险"
				,"国寿松鹤颐年年金保险","国寿美满人生至尊版年金保险"
				,"国寿福禄宝宝两全保险","国寿福满一生两全保险"
				,"国寿附加重大疾病保险","国寿福禄呈祥两全保险"
				,"国寿福禄金尊两全保险","国寿新鸿泰两全保险","国寿附加康友重大疾病保险"
				,"国寿安鑫保险计划","国寿育才少儿两全保险","国寿新鸿泰金典版两全保险"
				,"国寿福禄尊享两全保险","国寿福瑞人生两全保险","国寿附加长久呵护意外伤害保险"
				,"国寿长久呵护住院费用补偿医疗保险","国寿长久呵护意外伤害费用补偿医疗保险"
				,"国寿长久呵护意外伤害定额给付医疗保险","国寿福禄满堂养老年金保险"
			};
		
		private String[] str4 = {"年交","分为一次性交付和分期交付（年交）两种，由投保人在投保时选择"};
		private String[] str52 = {"5年","10年","15年"};

	private List<HashMap<String, String>> lists = new ArrayList<HashMap<String,String>>();
	//--------------------------------------------------------
	private List<HashMap<String, String>> lists2 = new ArrayList<HashMap<String,String>>();
	
	
	
	private ProgressDialog readWaitProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.plan_layout);
		
	    mTvCusInfo=(TextView) this.findViewById(R.id.cusinfo);
		mTvType=(TextView) this.findViewById(R.id.type);
		mTvData=(TextView) this.findViewById(R.id.data);
		mTvSet=(TextView) this.findViewById(R.id.set); 
		mTvCusInfo.setOnClickListener(this);
		mTvType.setOnClickListener(this);
		mTvData.setOnClickListener(this);
		mTvSet.setOnClickListener(this);
		mShow=(TextView) this.findViewById(R.id.show);
		mPlan=(TextView) this.findViewById(R.id.plan);
		mMaintain=(TextView) this.findViewById(R.id.maintain);
		mShow.setOnClickListener(this);
		mPlan.setOnClickListener(this);
		mMaintain.setOnClickListener(this);
		LiuXXFun();
		liyi_Init();
		
		right.removeAllViews();
		right.addView(guo_linear1);
		right.addView(guo_linear2);
		
	}

	private void LiuXXFun(){
		tab_list=new ArrayList<Map<String,String>>();
		Map<String,String> map1=new HashMap<String,String>();
		map1.put("jihuashu", "中国人寿计划书");
		map1.put("time", "2012-3-3");
		map1.put("toubaoren", "梁先生");
		map1.put("nianling", "22");
		map1.put("beibaoren", "刘星星");
		map1.put("beibaonianling", "23");
		map1.put("xianzhong", "456/756");
		Map<String,String> map2=new HashMap<String,String>();
		map2.put("jihuashu", "中国人寿计划书");
		map2.put("time", "2012-3-4");
		map2.put("toubaoren", "刘星星");
		map2.put("nianling", "33");
		map2.put("beibaoren", "刘星星");
		map2.put("beibaonianling", "23");
		map2.put("xianzhong", "456/756");
		Map<String,String> map3=new HashMap<String,String>();
		map3.put("jihuashu", "中国人寿计划书");
		map3.put("time", "2012-3-6");
		map3.put("toubaoren", "毛豆豆");
		map3.put("nianling", "23");
		map3.put("beibaoren", "刘星星");
		map3.put("beibaonianling", "32");
		map3.put("xianzhong", "456/756");
		Map<String,String> map4=new HashMap<String,String>();
		map4.put("jihuashu", "中国人寿计划书");
		map4.put("time", "2012-3-15");
		map4.put("toubaoren", "翟先生");
		map4.put("nianling", "23");
		map4.put("beibaoren", "刘星星");
		map4.put("beibaonianling", "32");
		map4.put("xianzhong", "456/756");
		tab_list.add(map1);
		tab_list.add(map2);
		tab_list.add(map3);
		tab_list.add(map4);
		layout=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		right=(LinearLayout) findViewById(R.id.Ptab2);
		pritect_plan=(RelativeLayout) layout.inflate(R.layout.plan_protect_plan, null);
		xiamian=(LinearLayout) pritect_plan.findViewById(R.id.xiamian);
		copy=(Button) pritect_plan.findViewById(R.id.fuzhi);
		copy.setOnClickListener(this);
		zhuan=(Button) pritect_plan.findViewById(R.id.zhuan);
		zhuan.setOnClickListener(this);
		xiugai=(Button) pritect_plan.findViewById(R.id.xiugai);
		xiugai.setOnClickListener(this);
		shanchu=(Button) pritect_plan.findViewById(R.id.shanchu);
		shanchu.setOnClickListener(this);
		 mShow=(TextView) this.findViewById(R.id.show);
		 mPlan=(TextView) this.findViewById(R.id.plan);
		 name_jihuan=(EditText) pritect_plan.findViewById(R.id.name_jihuashu);
		 name_baofu=(EditText) pritect_plan.findViewById(R.id.name_baofu);
		 time_zhizuo=(Button) pritect_plan.findViewById(R.id.time_zhizuo);
		 time_jiezhi=(Button) pritect_plan.findViewById(R.id.time_jiezhi);
		 mMaintain=(TextView) this.findViewById(R.id.maintain);
		 query_jihushu=(Button) pritect_plan.findViewById(R.id.query_jihua);
		 tab_listView=(ListView) pritect_plan.findViewById(R.id.tab_list);
		  padapter=new PAdapter(tab_list);
		 tab_listView.setAdapter(padapter);
		 tab_listView.setCacheColorHint(0);
		 query_jihushu.setOnClickListener(new QueryJiHuaShuListener());
		 mMaintain.setOnClickListener(this);
		 Calendar c=Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
		 time_zhizuo.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					type_time=1;
					showDialog(DATE_DIALOG_ID);
				}
			});
		 time_jiezhi.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				type_time=2;
				showDialog(DATE_DIALOG_ID);
			}
		});
	}
	
	
	
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
	
		switch(id){
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDatePickDialog, mYear,mMonth,mDay);
		case TIME_DIALOG_ID:
			return null;
		}
		return null;
	}
	DatePickerDialog.OnDateSetListener mDatePickDialog=new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			 //系统当前时间，用来判断设置的提醒时间 是不是比当前时间早
		    Calendar cd=Calendar.getInstance();
		    cd.set(year, monthOfYear, dayOfMonth);
			String mon=null;
			String day=null;
				mYear=year;
				mMonth=monthOfYear ;
				mDay=dayOfMonth;
				if(mMonth+1<10){
					mon=0+""+(mMonth+1);
				}else{
					mon=(mMonth+1)+"";
				}
				if(mDay<10){
					day=0+""+mDay;
				}else{
					day=mDay+"";
				}
				if(type_time==1){
					time_zhizuo.setText(mYear + "-" + (mon) + "-" + day);
				}else if(type_time==2){
					time_jiezhi.setText(mYear + "-" + (mon) + "-" + day);
				}
				
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(this,InsuranceShowMainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
			
		return super.onKeyDown(keyCode, event);
	}
	
	
	
		class QueryJiHuaShuListener implements View.OnClickListener{
	
			@Override
			public void onClick(View v) {   
				// TODO Auto-generated method stub
				String jiezhi=time_zhizuo.getText().toString();
				int n=Integer.parseInt(jiezhi.substring(0, 4));
				int y=Integer.parseInt(jiezhi.substring(5, 7));
				int r=Integer.parseInt(jiezhi.substring(8, 10));
				Toast.makeText(getApplicationContext(), ""+y+""+r, Toast.LENGTH_LONG).show();
				
				if(n>2012){
					xiamian.removeAllViews();
					TextView tt=new TextView(PlanActivity.this);
					tt.setText("没有您要查询的计划书记录。。。。。");
					tt.setTextColor(Color.RED);
					tt.setTextSize(20);
					xiamian.setPadding(20, 20, 20, 20);
					xiamian.addView(tt);
					
				}else if(n==2012 && y>4){
					xiamian.removeAllViews();
					TextView tt=new TextView(PlanActivity.this);
					tt.setText("没有您要查询的计划书记录。。。。。");
					tt.setTextColor(Color.RED);
					tt.setTextSize(20);
					xiamian.setPadding(20, 20, 20, 20);
					xiamian.addView(tt);
				}else if(n==2012 && y==4 &&r>1){
					xiamian.removeAllViews();
					TextView tt=new TextView(PlanActivity.this);
					tt.setText("没有您要查询的计划书记录。。。。。");
					tt.setTextColor(Color.RED);
					tt.setTextSize(20);
					xiamian.setPadding(20, 20, 20, 20);
					xiamian.addView(tt);
				}else{
					xiamian.removeAllViews();
					xiamian.addView(tab_listView);
				}
				
			}
			
		}
	 LinearLayout mLayoutRight=null;
	 private String[] str3={"国寿福禄满堂养老年金保险"
				,"国寿康宁定期重大疾病保险","国寿康宁终身重大疾病保险","附加关爱一生长期医疗保险"
				,"国寿关爱一生终身寿险","国寿附加定期寿险"};

		private String[] str5 = {"年交","分为一次性交付和分期交付（年交）两种，由投保人在投保时选择"};
		private String[] str10 = {"160980","31","男","80000" ,"4500050","340000"};
		
		
		
		public String[] param={"刘国庆","方志华","刘玉静","方启红","房磊","王欢","王磊","杨磊","刘超","吴洁","马龙","郑玉树","刘媛媛","吴立媛"};
		
		private String sex="";
		private String shu="";
		private String classtype="";
		public String[] getListArgs(){
			String[] args={"1","丈夫","李丽","28","女","二级"};
			Random random=new Random();
			int i=random.nextInt(10);
			args[2]=param[i];
			if(i%2==0){
				sex="女";
			}else{
				sex="男";
			}
			i=i+2;
			if(i<3){
				classtype="一级";
			}else if(i<7){
				classtype="二级";
			}else if(i<8){
				classtype="三级";
			}else if(i>8){
				classtype="四级";
			}
			i=i-2+1;
			if(i<3){
				shu="丈夫";
			}else if(i<7){
				shu="妻子";
			}else if(i<8){
				shu="儿子";
			}else{
				shu="女儿";
			}
			args[1]=shu;
			args[5]=classtype;
			args[4]=sex;
			args[3]=(25+random.nextInt(10))+"";
			return args;
		}
		String[] args={"1","丈夫","李丽","28","女","二级"};
		List<String[]> arg=new ArrayList<String[]>();
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.cusinfo){//客户信息
//			LinearLayout layout=(LinearLayout) this.findViewById(R.id.contents);
//			mTvCusInfo.setBackgroundResource(R.drawable.single_tag01_on);
//			mTvType.setBackgroundResource(R.drawable.single_tag01);
//			mTvData.setBackgroundResource(R.drawable.single_tag01);
//			mLayoutRight=(LinearLayout) this.findViewById(R.id.right);
//			mLayoutRight.setVisibility(View.VISIBLE);
//			layout.removeAllViews();
//			View view=((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.add_plan_layout, null);
//			layout.addView(view);
//			Button add=(Button) this.findViewById(R.id.add);
//			Button del=(Button) this.findViewById(R.id.del);			
//			Spinner s1=(Spinner) this.findViewById(R.id.s1); 
//			s1.setPrompt("请选择");
//			String[] province = { "妻子", "丈夫", "女儿","儿子" };
//			ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, province);
//			s1.setAdapter(arrayadapter);
//			Spinner s2=(Spinner) this.findViewById(R.id.s2);
//			s2.setPrompt("请选择");
//			String[] province1 = { "一级", "二级", "三级","四级" };
//			arrayadapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, province1);
//			s2.setAdapter(arrayadapter);			
//			final ListView listView =(ListView) this.findViewById(R.id.listview);
//			final GenerationAdapter adapter=new GenerationAdapter(this,arg);
//			listView.setAdapter(adapter);
//			adapter.notifyDataSetChanged();
//		    mTvCusInfo=(TextView) this.findViewById(R.id.cusinfo);
//			mTvType=(TextView) this.findViewById(R.id.type);
//			mTvData=(TextView) this.findViewById(R.id.data);
//			mTvSet=(TextView) this.findViewById(R.id.set);
//			mTvCusInfo.setOnClickListener(this);
//			mTvType.setOnClickListener(this);
//			mTvData.setOnClickListener(this);
//			mTvSet.setOnClickListener(this);
//			add.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					arg.add(getListArgs());
//					GenerationAdapter adapter1=new GenerationAdapter(PlanActivity.this,arg);
//					listView.setAdapter(adapter1);
//					adapter1.notifyDataSetChanged();
//					setListViewHeightBasedOnChildren(listView);
//				}				
//			});
//			del.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					if(arg.size() == 0){
//						Toast.makeText(PlanActivity.this, "已经没有数据了", 2000).show();
//					}else{
//						arg.remove(arg.size()-1);
//						GenerationAdapter	 adapter1=new GenerationAdapter(PlanActivity.this,arg);
//						listView.setAdapter(adapter1);
//						adapter1.notifyDataSetChanged();
//						setListViewHeightBasedOnChildren(listView);
//					}
//					
//				}
//				
//			});
//			Button next=(Button) this.findViewById(R.id.next);
//			next.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
//					mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
//					layout.removeAllViews();
//					View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.next_layout, null);
//					layout.addView(view);
//					mTvType.setBackgroundResource(R.drawable.single_tag01_on);
//					ListView listView =(ListView) PlanActivity.this.findViewById(R.id.listview);
//					LiYiListAdapter adaper1=new LiYiListAdapter(PlanActivity. this, getData());
//					listView.setAdapter(adaper1);
//					adaper1.notifyDataSetChanged();
//					Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
//					ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str1);
//					s1.setAdapter(arrayadapter);
//					Spinner s2=(Spinner) PlanActivity.this.findViewById(R.id.s2);
//					 arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str2);
//					 s2.setAdapter(arrayadapter); 
//					 
//					 Spinner s3=(Spinner) PlanActivity.this.findViewById(R.id.s3);
//					 arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str3);
//					 s3.setAdapter(arrayadapter);
//					 
//					 
//					 Spinner s5=(Spinner) PlanActivity.this.findViewById(R.id.s5);
//					 arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str5);
//					 s5.setAdapter(arrayadapter);
//					 
//					 Spinner s4=(Spinner) PlanActivity.this.findViewById(R.id.s4);
//					 arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, new String[]{"5年","10年","15年"});
//					 s4.setAdapter(arrayadapter);
//					 Spinner s6=(Spinner) PlanActivity.this.findViewById(R.id.s6);
//					 arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, new String[]{"5年","10年","15年"});
//					 s6.setAdapter(arrayadapter);
//					 Button next=(Button) PlanActivity.this.findViewById(R.id.next);//下一步
////					 Button fx=(Button) PlanActivity.this.findViewById(R.id.liyi_btn1);//净风险
//					    mTvCusInfo=(TextView) PlanActivity.this.findViewById(R.id.cusinfo);
//						mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
//						mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
//						mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
//						mTvCusInfo.setOnClickListener(PlanActivity.this);
//						mTvType.setOnClickListener(PlanActivity.this);
//						mTvData.setOnClickListener(PlanActivity.this);
//						mTvSet.setOnClickListener(PlanActivity.this);
//					 next.setOnClickListener(new OnClickListener(){
//							@Override
//							public void onClick(View v) {
//								LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
//								mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
//								mTvType.setBackgroundResource(R.drawable.single_tag01);
//								layout.removeAllViews();
//								View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.data_layout, null);
//								layout.addView(view);
//								mTvData.setBackgroundResource(R.drawable.single_tag01_on);
//								Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
//								ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//										PlanActivity.this,
//										android.R.layout.simple_dropdown_item_1line, str1);
//								s1.setAdapter(arrayadapter);
//							    mTvCusInfo=(TextView)PlanActivity. this.findViewById(R.id.cusinfo);
//								mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
//								mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
//								mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
//								mTvCusInfo.setOnClickListener(PlanActivity.this);
//								mTvType.setOnClickListener(PlanActivity.this);
//								mTvData.setOnClickListener(PlanActivity.this);
//								mTvSet.setOnClickListener(PlanActivity.this);
//								List<String[]> list=new ArrayList<String[]>();
//								list.add(str10)	;			
//								PlanDataAdapter plan=new PlanDataAdapter(PlanActivity.this,list);
//								ListView listView =(ListView) PlanActivity.this.findViewById(R.id.listview);
//								listView.setAdapter(plan);
//								plan.notifyDataSetChanged();
//							}				
//						});
////					 fx.setOnClickListener(new OnClickListener(){
////							@Override
////							public void onClick(View v) {
////								
////								
////							}				
////						});
//					 
//				}				
//			});
			guo_kehu();
		}else if(v.getId()==R.id.type){	
//			LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
//	    mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
//		mTvType.setBackgroundResource(R.drawable.single_tag01_on);
//		mTvData.setBackgroundResource(R.drawable.single_tag01);
//		layout.removeAllViews();
//		View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.next_layout, null);
//		layout.addView(view);
//		mTvType.setBackgroundResource(R.drawable.single_tag01_on);
//		final ListView listView =(ListView) PlanActivity.this.findViewById(R.id.listview);
//		LiYiListAdapter adaper1=new LiYiListAdapter(PlanActivity. this, getData());
//		listView.setAdapter(adaper1);
//		adaper1.notifyDataSetChanged();
//		Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
//		ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//				PlanActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str1);
//		s1.setAdapter(arrayadapter);
//		Spinner s2=(Spinner) PlanActivity.this.findViewById(R.id.s2);
//		 arrayadapter = new ArrayAdapter<String>(
//				PlanActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str2);
//		 s2.setAdapter(arrayadapter); 
//		 
//		 Spinner s3=(Spinner) PlanActivity.this.findViewById(R.id.s3);
//		 arrayadapter = new ArrayAdapter<String>(
//				PlanActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str3);
//		 s3.setAdapter(arrayadapter);
//		 
//		 
//		 Spinner s5=(Spinner) PlanActivity.this.findViewById(R.id.s5);
//		 arrayadapter = new ArrayAdapter<String>(
//				PlanActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str5);
//		 s5.setAdapter(arrayadapter);
//		 
//		 Spinner s4=(Spinner) PlanActivity.this.findViewById(R.id.s4);
//		 arrayadapter = new ArrayAdapter<String>(
//				PlanActivity.this,
//				android.R.layout.simple_dropdown_item_1line, new String[]{"5年","10年","15年"});
//		 s4.setAdapter(arrayadapter);
//		 Spinner s6=(Spinner) PlanActivity.this.findViewById(R.id.s6);
//		 arrayadapter = new ArrayAdapter<String>(
//				PlanActivity.this,
//				android.R.layout.simple_dropdown_item_1line, new String[]{"5年","10年","15年"});
//		 s6.setAdapter(arrayadapter);
//		 Button next=(Button) PlanActivity.this.findViewById(R.id.next);//下一步
//		 TextView fx=(TextView) PlanActivity.this.findViewById(R.id.type);//净风险
//		    mTvCusInfo=(TextView) PlanActivity.this.findViewById(R.id.cusinfo);
//			mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
//			mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
//			mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
//			mTvCusInfo.setOnClickListener(PlanActivity.this);
//			mTvType.setOnClickListener(PlanActivity.this);
//			mTvData.setOnClickListener(PlanActivity.this);
//			mTvSet.setOnClickListener(PlanActivity.this);
//		 next.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
//					mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
//					mTvType.setBackgroundResource(R.drawable.single_tag01);
//					layout.removeAllViews();
//					View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.data_layout, null);
//					layout.addView(view);
//					mTvData.setBackgroundResource(R.drawable.single_tag01_on);
//					Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
//					ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str1);
//					s1.setAdapter(arrayadapter);
//				    mTvCusInfo=(TextView)PlanActivity. this.findViewById(R.id.cusinfo);
//					mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
//					mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
//					mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
//					mTvCusInfo.setOnClickListener(PlanActivity.this);
//					mTvType.setOnClickListener(PlanActivity.this);
//					mTvData.setOnClickListener(PlanActivity.this);
//					mTvSet.setOnClickListener(PlanActivity.this);
//				}				
//			});
//		 fx.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					arg.add(args);
//					GenerationAdapter	 adapter1=new GenerationAdapter(PlanActivity.this,arg);
//					listView.setAdapter(adapter1);
//					adapter1.notifyDataSetChanged();
//					setListViewHeightBasedOnChildren(listView);
//					
//				}				
//			});
			guo_tpye();
		 }else if(v.getId()==R.id.data){//数据
			 guo_data();
//				LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
//			mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
//			mTvType.setBackgroundResource(R.drawable.single_tag01);
//			mTvData.setBackgroundResource(R.drawable.single_tag01_on);
//			
//			layout.removeAllViews();
//			View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.data_layout, null);
//			layout.addView(view);
//			mTvData.setBackgroundResource(R.drawable.single_tag01_on);
//			Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
//			ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//					PlanActivity.this,
//					android.R.layout.simple_dropdown_item_1line, str1);
//			s1.setAdapter(arrayadapter);
//		    mTvCusInfo=(TextView)PlanActivity. this.findViewById(R.id.cusinfo);
//			mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
//			mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
//			mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
//			mTvCusInfo.setOnClickListener(PlanActivity.this);
//			mTvType.setOnClickListener(PlanActivity.this);
//			mTvData.setOnClickListener(PlanActivity.this);
//			mTvSet.setOnClickListener(PlanActivity.this);
		}else if(v.getId()==R.id.set){//设置
			
		}else if(v == guoshow){//利益演示
			right.removeAllViews();
			right.addView(guo_linear1);
			right.addView(guo_linear2);
		}else if(v.getId()==R.id.plan){//新增计划书
//			View view1=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.inflater_layout, null);
//			LinearLayout layout1=(LinearLayout) PlanActivity.this.findViewById(R.id.Ptab2);
//			layout1.removeAllViews();
//			layout1.addView(view1);
//			LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
//		    mTvCusInfo=(TextView) this.findViewById(R.id.cusinfo);
//			mTvCusInfo.setOnClickListener(this);
//			mTvCusInfo.setBackgroundResource(R.drawable.single_tag01_on);
//			mLayoutRight=(LinearLayout) this.findViewById(R.id.right);
//			mLayoutRight.setVisibility(View.VISIBLE);
//			layout.removeAllViews();
//			View view=((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.add_plan_layout, null);
//			layout.addView(view);
//			Button add=(Button) this.findViewById(R.id.add);
//			Spinner s1=(Spinner) this.findViewById(R.id.s1); 
//			s1.setPrompt("请选择");
//			String[] province = { "妻子", "丈夫", "女儿","儿子" };
//			ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//					this,
//					android.R.layout.simple_dropdown_item_1line, province);
//			s1.setAdapter(arrayadapter);
//			Spinner s2=(Spinner) this.findViewById(R.id.s2);
//			s2.setPrompt("请选择");
//			String[] province1 = { "一级", "二级", "三级","四级" };
//			 arrayadapter = new ArrayAdapter<String>(
//					this,
//					android.R.layout.simple_dropdown_item_1line, province1);
//			s2.setAdapter(arrayadapter);
//			final ListView listView =(ListView) this.findViewById(R.id.listview);
//			Button del=(Button) this.findViewById(R.id.del);
//			GenerationAdapter adapter=new GenerationAdapter(PlanActivity.this,arg);
//			listView.setAdapter(adapter);
//			adapter.notifyDataSetChanged();
//		    mTvCusInfo=(TextView) this.findViewById(R.id.cusinfo);
//			mTvType=(TextView) this.findViewById(R.id.type);
//			mTvData=(TextView) this.findViewById(R.id.data);
//			mTvSet=(TextView) this.findViewById(R.id.set);
//			mTvCusInfo.setOnClickListener(this);
//			mTvType.setOnClickListener(this);
//			mTvData.setOnClickListener(this);
//			mTvSet.setOnClickListener(this);
//			add.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					arg.add(getListArgs());
//					GenerationAdapter	 adapter1=new GenerationAdapter(PlanActivity.this,arg);
//					listView.setAdapter(adapter1);
//					adapter1.notifyDataSetChanged();
//					setListViewHeightBasedOnChildren(listView);	
//					
//				}				
//			});
//			del.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					if(arg.size() == 0){
//						Toast.makeText(PlanActivity.this, "已经没有数据了", 2000).show();
//					}else{
//						arg.remove(arg.size()-1);
//						GenerationAdapter	 adapter1=new GenerationAdapter(PlanActivity.this,arg);
//						listView.setAdapter(adapter1);
//						adapter1.notifyDataSetChanged();
//						setListViewHeightBasedOnChildren(listView);
//					}
//					
//					
//				}
//				
//			});
//			Button next=(Button) this.findViewById(R.id.next);
//			next.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
//					mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
//					layout.removeAllViews();
//					View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.next_layout, null);
//					layout.addView(view);
//					mTvType.setBackgroundResource(R.drawable.single_tag01_on);
//					ListView listView =(ListView) PlanActivity.this.findViewById(R.id.listview);
//					LiYiListAdapter adaper1=new LiYiListAdapter(PlanActivity. this, getData());
//					listView.setAdapter(adaper1);
//					adaper1.notifyDataSetChanged();
//					Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
//					ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str1);
//					s1.setAdapter(arrayadapter);
//					Spinner s2=(Spinner) PlanActivity.this.findViewById(R.id.s2);
//					 arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str2);
//					 s2.setAdapter(arrayadapter); 
//					 
//					 Spinner s3=(Spinner) PlanActivity.this.findViewById(R.id.s3);
//					 arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str3);
//					 s3.setAdapter(arrayadapter);
//					 
//					 
//					 Spinner s5=(Spinner) PlanActivity.this.findViewById(R.id.s5);
//					 arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str5);
//					 s5.setAdapter(arrayadapter);
//					 
//					 Spinner s4=(Spinner) PlanActivity.this.findViewById(R.id.s4);
//					 arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, new String[]{"5年","10年","15年"});
//					 s4.setAdapter(arrayadapter);
//					 Spinner s6=(Spinner) PlanActivity.this.findViewById(R.id.s6);
//					 arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, new String[]{"5年","10年","15年"});
//					 s6.setAdapter(arrayadapter);
//					 Button next=(Button) PlanActivity.this.findViewById(R.id.next);//下一步
//					 Button fx=(Button) PlanActivity.this.findViewById(R.id.liyi_btn1);//净风险
//					    mTvCusInfo=(TextView) PlanActivity.this.findViewById(R.id.cusinfo);
//						mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
//						mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
//						mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
//						mTvCusInfo.setOnClickListener(PlanActivity.this);
//						mTvType.setOnClickListener(PlanActivity.this);
//						mTvData.setOnClickListener(PlanActivity.this);
//						mTvSet.setOnClickListener(PlanActivity.this);
//					 next.setOnClickListener(new OnClickListener(){
//							@Override
//							public void onClick(View v) {
//								LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
//								mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
//								mTvType.setBackgroundResource(R.drawable.single_tag01);
//								layout.removeAllViews();
//								View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.data_layout, null);
//								layout.addView(view);
//								mTvData.setBackgroundResource(R.drawable.single_tag01_on);
//								Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
//								ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//										PlanActivity.this,
//										android.R.layout.simple_dropdown_item_1line, str1);
//								s1.setAdapter(arrayadapter);
//							    mTvCusInfo=(TextView)PlanActivity. this.findViewById(R.id.cusinfo);
//								mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
//								mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
//								mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
//								mTvCusInfo.setOnClickListener(PlanActivity.this);
//								mTvType.setOnClickListener(PlanActivity.this);
//								mTvData.setOnClickListener(PlanActivity.this);
//								mTvSet.setOnClickListener(PlanActivity.this);
//								List<String[]> list=new ArrayList<String[]>();
//								list.add(str10)	;			
//								PlanDataAdapter plan=new PlanDataAdapter(PlanActivity.this,list);
//								ListView listView =(ListView) PlanActivity.this.findViewById(R.id.listview);
//								listView.setAdapter(plan);
//								plan.notifyDataSetChanged();
//								 Button submit=(Button) PlanActivity.this.findViewById(R.id.submit);//提交数据
//								 Button back=(Button) PlanActivity.this.findViewById(R.id.back);//返回
//								 submit.setOnClickListener(new OnClickListener(){
//										@Override
//										public void onClick(View v) {
//											new Thread(){
//												public void run(){
//													try {
//														sleep(2000);
//														handler.sendEmptyMessage(1);
//														handler.sendEmptyMessage(2);
//													} catch (InterruptedException e) {
//														// TODO Auto-generated catch block
//														e.printStackTrace();
//													}
//												}
//											}.start();
//											confirmShowWaitDialog();
//											readWaitProgressDialog.setMessage("正在提交数据...");
//										}				
//									});
//								 back.setOnClickListener(new OnClickListener(){
//										@Override
//										public void onClick(View v) {
//											Intent intent=new Intent(PlanActivity.this,InsuranceShowMainActivity.class);
//											PlanActivity.this.startActivity(intent);
//											PlanActivity.this.finish();
//										}				
//									});
//							}				
//						});
////					 fx.setOnClickListener(new OnClickListener(){
////							@Override
////							public void onClick(View v) {
////								
////								
////							}				
////						});
//					 
//				}				
//			});
			
			guo_kehu();
		}else if(v == mMaintain){//维护计划书
			right.removeAllViews();
			right.addView(pritect_plan);
			
		}else if(v==copy){
			if(tab_list.size()!=0  && xiamian.getChildAt(0)==tab_listView){
				Map<String,String> map=new HashMap<String,String>();
				map.put("jihuashu", "中国人寿计划书");
				map.put("time", "2012-3-3");
				map.put("toubaoren", "梁次次");
				map.put("nianling", "22");
				map.put("beibaoren", "刘星星");
				map.put("beibaonianling", "23");
				map.put("xianzhong", "456/756");
				tab_list.add(map);
				padapter=new PAdapter(tab_list);
				tab_listView.setAdapter(padapter);
				xiamian.invalidate();
			}else{
				xiamian.removeAllViews();
				TextView tt=new TextView(PlanActivity.this);
				tt.setText("记录为空，没有能复制的记录。。。。");
				tt.setTextColor(Color.RED);
				tt.setTextSize(20);
				xiamian.setPadding(20, 20, 20, 20);
				xiamian.addView(tt);
				xiamian.invalidate();
			}
			
		}else if(v==zhuan){
			Intent intent=new Intent(PlanActivity.this,InsureActivity.class);
			startActivity(intent);
			finish();
		}else if(v==xiugai){
			right.removeAllViews();
			View view1=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.inflater_layout, null);
			right.addView(view1);
		}else if(v==shanchu){
			if(tab_list.size()!=0){
				tab_list.remove(0);
				padapter=new PAdapter(tab_list);
				tab_listView.setAdapter(padapter);
				xiamian.invalidate();
			}else{
				xiamian.removeAllViews();
				TextView tt=new TextView(PlanActivity.this);
				tt.setText("记录为空，没有能删除的记录。。。。");
				tt.setTextColor(Color.RED);
				tt.setTextSize(20);
				xiamian.setPadding(20, 20, 20, 20);
				xiamian.addView(tt);
				xiamian.invalidate();
			}
			
		}
		
	}
	
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
	public List<HashMap<String, String>> getLists1() {
		List<HashMap<String, String>> lists=new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("t1", str[0]);
			map.put("t2", "464");
			map.put("t3", "李国华");
			map.put("t4", "男");
			map.put("t5", "1992-03-12");
			map.put("t6", "1");
			map.put("t7", "16554.00");
			map.put("t8", "16425.12");
			map.put("t9", "年");
			map.put("t10", "10");
			map.put("t11", "年交");
			map.put("t12", "年");
			map.put("t13", "3");
			map.put("t14", "");
			map.put("t15", "");
			map.put("t16", "");
			map.put("t17", "主险");
			map.put("t18", "1");
			map.put("t19", "464");
			lists.add(map);
		return lists;
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
				Toast.makeText(PlanActivity.this, "已提交数据", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
		return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
		View listItem = listAdapter.getView(i, null, listView);
		listItem.measure(0, 0);
		totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.height += 5;//if without this statement,the listview will be a little short
		listView.setLayoutParams(params);
		}

	
	public void notifyAdapter(){
		getLists();
		LiYiListAdapter adapter = new LiYiListAdapter(this, lists);
		listview.setAdapter(adapter);
		setListViewHeightBasedOnChildren(listview);
	}
	
	public void notifyAdapter2(){
		getLists2();
		LiYiList2Adapter adapter = new LiYiList2Adapter(this, lists2);
		listview2.setAdapter(adapter);
		setListViewHeightBasedOnChildren(listview2);
	}
	
	public List<HashMap<String, String>> getData(){
		List <HashMap<String, String>> list=new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
//	map.put("t1", str[0]);
	map.put("t2", "464");
	map.put("t3", "李国华");
	map.put("t4", "男");
	map.put("t5", "1992-03-12");
	map.put("t6", "1");
	map.put("t7", "16554.00");
	map.put("t8", "16425.12");
	map.put("t9", "年");
	map.put("t10", "10");
	map.put("t11", "年交");
	map.put("t12", "年");
	map.put("t13", "3");
	map.put("t14", "");
	map.put("t15", "");
	map.put("t16", "");
	map.put("t17", "主险");
	map.put("t18", "1");
	map.put("t19", "464");
	list.add(map);
	return list;
	}
	
	public List<HashMap<String, String>> getLists() {
		lists.clear();
		
		int i ,j ;
		HashMap<String, String> map;
		for(i = 0; i<10;i++){
			if(i>=index){
				break;
			}
			map = new HashMap<String, String>();
			for(j=0;j<18;j++){
				
				map.put("t"+(j+2), info2[i][j]);
			}
			lists.add(map);
		}
//			HashMap<String, String> map = new HashMap<String, String>();
////			map.put("t1", str[0]);
//			map.put("t2", "464");
//			map.put("t3", "李国华");
//			map.put("t4", "男");
//			map.put("t5", "1992-03-12");
//			map.put("t6", "1");
//			map.put("t7", "16554.00");
//			map.put("t8", "16425.12");
//			map.put("t9", "年");
//			map.put("t10", "10");
//			map.put("t11", "年交");
//			map.put("t12", "年");
//			map.put("t13", "3");
//			map.put("t14", "");
//			map.put("t15", "");
//			map.put("t16", "");
//			map.put("t17", "主险");
//			map.put("t18", "1");
//			map.put("t19", "464");
//			int i;
//			for(i = 0; i<index;i++){
//				lists.add(map);
//			}
		
		return lists;
	}
	
	
	public List<HashMap<String, String>> getLists2() {
		lists2.clear();
		int i ,j ;
		HashMap<String, String> map;
		for(i = 0; i<7;i++){
			map = new HashMap<String, String>();
			for(j=0;j<9;j++){
				
				map.put("t"+(j+1), info1[i][j]);
			}
			lists2.add(map);
		}
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("t1", "1");
//			map.put("t2", "464");
//			map.put("t3", "李国华");
//			map.put("t4", "男");
//			map.put("t5", "1992-03-12");
//			map.put("t6", "1");
//			map.put("t7", "16554.00");
//			map.put("t8", "16425.12");
//			map.put("t9", "年");
//
//			int i;
//			for(i = 0; i<index;i++){
//				lists2.add(map);
//			}
		
		return lists2;
	}
	
	
	public void liyi_Init(){
		guoshow = (TextView)findViewById(R.id.show);
		LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		guo_linear1 = (LinearLayout)lay.inflate(R.layout.l_li_xian_1, null);
		guo_linear2 = (LinearLayout)lay.inflate(R.layout.l_li_xian_2, null);
		guoshow.setOnClickListener(this);
		listview = (ListView)guo_linear1.findViewById(R.id.listview);//----------guoqing
		listview2 = (ListView)guo_linear2.findViewById(R.id.listview2);//----------guoqing
		s1 = (Spinner)guo_linear1.findViewById(R.id.s1);
		s2 = (Spinner)guo_linear1.findViewById(R.id.s2);
		s3 = (Spinner)guo_linear1.findViewById(R.id.s3);
		s4 = (Spinner)guo_linear1.findViewById(R.id.s4);
		s5 = (Spinner)guo_linear1.findViewById(R.id.s5);
		liyi2s1 = (Spinner)guo_linear2.findViewById(R.id.liyi2s1);
		
		s1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str1));
		s2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str2));
		s3.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str3));
		s4.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str4));
		s5.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str5));
		liyi2s1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str1));
		btn1 = (Button)guo_linear1.findViewById(R.id.submit_baoxian);
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				index ++;
				Toast.makeText(PlanActivity.this, "投保成功", 2000).show();
				notifyAdapter();
			}
		});
		
		liyi_btn1 = (Button)guo_linear1.findViewById(R.id.liyi_btn1);
		liyi_btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				guo_linear1.setVisibility(View.GONE);
				guo_linear2.setVisibility(View.VISIBLE);
				notifyAdapter2();

				
			}
		});
		liyi_btn2 = (Button)guo_linear1.findViewById(R.id.liyi_btn2);
		liyi_btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		liyi_btn3 = (Button)guo_linear1.findViewById(R.id.liyi_btn3);
		liyi_btn3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				index -- ;
				if(index<0){
					index = 0;
				}else{
					Toast.makeText(PlanActivity.this, "删除成功", 2000).show();
				}
				notifyAdapter();
			}
		});
		liyi2_b1 = (Button)guo_linear2.findViewById(R.id.liyi2_b1);
		liyi2_b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				guo_linear1.setVisibility(View.VISIBLE);
				guo_linear2.setVisibility(View.GONE);
			}
		});
		liyi2_b2 = (Button)guo_linear2.findViewById(R.id.liyi2_b2);
		liyi2_b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				guo_linear2.setVisibility(View.VISIBLE);
				guo_linear1.setVisibility(View.GONE);
			}
		});
		li_xian1_btn1 = (Button)guo_linear1.findViewById(R.id.li_xian1_btn1);
		li_xian1_btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				guo_linear1.setVisibility(View.VISIBLE);
				guo_linear2.setVisibility(View.GONE);
			}
		});
		li_xian1_btn2 = (Button)guo_linear1.findViewById(R.id.li_xian1_btn2);
		li_xian1_btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				guo_linear2.setVisibility(View.VISIBLE);
				guo_linear1.setVisibility(View.GONE);
			}
		});
		
		notifyAdapter();
		notifyAdapter2();
	}
	class PAdapter extends BaseAdapter{
		List<Map<String,String>> list;
		public PAdapter(List<Map<String,String>> list1) {
			// TODO Auto-generated constructor stub
			this.list=list1;
		}
		@Override
		public int getCount() {
			
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LinearLayout li=(LinearLayout) layout.inflate(R.layout.tab_child, null);
			CheckBox ch=(CheckBox) li.findViewById(R.id.che_id);
			TextView t1=(TextView) li.findViewById(R.id.text_1);
			TextView t2=(TextView) li.findViewById(R.id.text_2);
			TextView t3=(TextView) li.findViewById(R.id.text_3);
			TextView t4=(TextView) li.findViewById(R.id.text_4);
			TextView t5=(TextView) li.findViewById(R.id.text_5);
			TextView t6=(TextView) li.findViewById(R.id.text_6);
			TextView t7=(TextView) li.findViewById(R.id.text_7);
			Map<String,String> map=list.get(position);
			t1.setText(map.get("jihuashu"));
			t2.setText(map.get("time"));
			t3.setText(map.get("toubaoren"));
			t4.setText(map.get("nianling"));
			t5.setText(map.get("beibaoren"));
			t6.setText(map.get("beibaonianling"));
			t7.setText(map.get("xianzhong"));
		return li;
		}
		
	}
	
	
	public void guo_data(){
		LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
		mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
		mTvType.setBackgroundResource(R.drawable.single_tag01);
		layout.removeAllViews();
		View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.data_layout, null);
		layout.addView(view);
		mTvData.setBackgroundResource(R.drawable.single_tag01_on);
		Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
		ArrayAdapter arrayadapter = new ArrayAdapter<String>(
				PlanActivity.this,
				android.R.layout.simple_dropdown_item_1line, str1);
		s1.setAdapter(arrayadapter);
	    mTvCusInfo=(TextView)PlanActivity. this.findViewById(R.id.cusinfo);
		mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
		mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
		mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
		mTvCusInfo.setOnClickListener(PlanActivity.this);
		mTvType.setOnClickListener(PlanActivity.this);
		mTvData.setOnClickListener(PlanActivity.this);
		mTvSet.setOnClickListener(PlanActivity.this);
		List<String[]> list=new ArrayList<String[]>();
		list.add(str10)	;			
		PlanDataAdapter plan=new PlanDataAdapter(PlanActivity.this,list);
		ListView listView =(ListView) PlanActivity.this.findViewById(R.id.listview);
		listView.setAdapter(plan);
		plan.notifyDataSetChanged();
		 Button submit=(Button) PlanActivity.this.findViewById(R.id.submit);//提交数据
		 Button back=(Button) PlanActivity.this.findViewById(R.id.back);//返回
		 submit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					new Thread(){
						public void run(){
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
					readWaitProgressDialog.setMessage("正在提交数据...");
				}				
			});
		 back.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(PlanActivity.this,InsuranceShowMainActivity.class);
					PlanActivity.this.startActivity(intent);
					PlanActivity.this.finish();
				}				
			});
	}
	
	
	public void guo_tpye(){
		LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
		mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
		layout.removeAllViews();
		View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.next_layout, null);
		layout.addView(view);
		mTvType.setBackgroundResource(R.drawable.single_tag01_on);
		ListView listView =(ListView) PlanActivity.this.findViewById(R.id.listview);
		LiYiListAdapter adaper1=new LiYiListAdapter(PlanActivity. this, getData());
		listView.setAdapter(adaper1);
		adaper1.notifyDataSetChanged();
		Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
		ArrayAdapter arrayadapter = new ArrayAdapter<String>(
				PlanActivity.this,
				android.R.layout.simple_dropdown_item_1line, str1);
		s1.setAdapter(arrayadapter);
		Spinner s2=(Spinner) PlanActivity.this.findViewById(R.id.s2);
		 arrayadapter = new ArrayAdapter<String>(
				PlanActivity.this,
				android.R.layout.simple_dropdown_item_1line, str2);
		 s2.setAdapter(arrayadapter); 
		 
		 Spinner s3=(Spinner) PlanActivity.this.findViewById(R.id.s3);
		 arrayadapter = new ArrayAdapter<String>(
				PlanActivity.this,
				android.R.layout.simple_dropdown_item_1line, str3);
		 s3.setAdapter(arrayadapter);
		 
		 
		 Spinner s5=(Spinner) PlanActivity.this.findViewById(R.id.s5);
		 arrayadapter = new ArrayAdapter<String>(
				PlanActivity.this,
				android.R.layout.simple_dropdown_item_1line, str5);
		 s5.setAdapter(arrayadapter);
		 
		 Spinner s4=(Spinner) PlanActivity.this.findViewById(R.id.s4);
		 arrayadapter = new ArrayAdapter<String>(
				PlanActivity.this,
				android.R.layout.simple_dropdown_item_1line, new String[]{"5年","10年","15年"});
		 s4.setAdapter(arrayadapter);
		 Spinner s6=(Spinner) PlanActivity.this.findViewById(R.id.s6);
		 arrayadapter = new ArrayAdapter<String>(
				PlanActivity.this,
				android.R.layout.simple_dropdown_item_1line, new String[]{"5年","10年","15年"});
		 s6.setAdapter(arrayadapter);
		 Button next=(Button) PlanActivity.this.findViewById(R.id.next);//下一步
		 Button fx=(Button) PlanActivity.this.findViewById(R.id.liyi_btn1);//净风险
		    mTvCusInfo=(TextView) PlanActivity.this.findViewById(R.id.cusinfo);
			mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
			mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
			mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
			mTvCusInfo.setOnClickListener(PlanActivity.this);
			mTvType.setOnClickListener(PlanActivity.this);
			mTvData.setOnClickListener(PlanActivity.this);
			mTvSet.setOnClickListener(PlanActivity.this);
		 next.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					guo_data();
//					LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
//					mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
//					mTvType.setBackgroundResource(R.drawable.single_tag01);
//					layout.removeAllViews();
//					View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.data_layout, null);
//					layout.addView(view);
//					mTvData.setBackgroundResource(R.drawable.single_tag01_on);
//					Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
//					ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//							PlanActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str1);
//					s1.setAdapter(arrayadapter);
//				    mTvCusInfo=(TextView)PlanActivity. this.findViewById(R.id.cusinfo);
//					mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
//					mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
//					mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
//					mTvCusInfo.setOnClickListener(PlanActivity.this);
//					mTvType.setOnClickListener(PlanActivity.this);
//					mTvData.setOnClickListener(PlanActivity.this);
//					mTvSet.setOnClickListener(PlanActivity.this);
//					List<String[]> list=new ArrayList<String[]>();
//					list.add(str10)	;			
//					PlanDataAdapter plan=new PlanDataAdapter(PlanActivity.this,list);
//					ListView listView =(ListView) PlanActivity.this.findViewById(R.id.listview);
//					listView.setAdapter(plan);
//					plan.notifyDataSetChanged();
//					 Button submit=(Button) PlanActivity.this.findViewById(R.id.submit);//提交数据
//					 Button back=(Button) PlanActivity.this.findViewById(R.id.back);//返回
//					 submit.setOnClickListener(new OnClickListener(){
//							@Override
//							public void onClick(View v) {
//								new Thread(){
//									public void run(){
//										try {
//											sleep(2000);
//											handler.sendEmptyMessage(1);
//											handler.sendEmptyMessage(2);
//										} catch (InterruptedException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										}
//									}
//								}.start();
//								confirmShowWaitDialog();
//								readWaitProgressDialog.setMessage("正在提交数据...");
//							}				
//						});
//					 back.setOnClickListener(new OnClickListener(){
//							@Override
//							public void onClick(View v) {
//								Intent intent=new Intent(PlanActivity.this,InsuranceShowMainActivity.class);
//								PlanActivity.this.startActivity(intent);
//								PlanActivity.this.finish();
//							}				
//						});
				}				
			});
//		 fx.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					
//					
//				}				
//			});
		 
	}
	
	
	public void guo_kehu(){
		View view1=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.inflater_layout, null);
		LinearLayout layout1=(LinearLayout) PlanActivity.this.findViewById(R.id.Ptab2);
		layout1.removeAllViews();
		layout1.addView(view1);
		LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
	    mTvCusInfo=(TextView) this.findViewById(R.id.cusinfo);
		mTvCusInfo.setOnClickListener(this);
		mTvCusInfo.setBackgroundResource(R.drawable.single_tag01_on);
		mLayoutRight=(LinearLayout) this.findViewById(R.id.right);
		mLayoutRight.setVisibility(View.VISIBLE);
		layout.removeAllViews();
		View view=((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.add_plan_layout, null);
		layout.addView(view);
		Button add=(Button) this.findViewById(R.id.add);
		Spinner s1=(Spinner) this.findViewById(R.id.s1); 
		s1.setPrompt("请选择");
		String[] province = { "妻子", "丈夫", "女儿","儿子" };
		ArrayAdapter arrayadapter = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_dropdown_item_1line, province);
		s1.setAdapter(arrayadapter);
		Spinner s2=(Spinner) this.findViewById(R.id.s2);
		s2.setPrompt("请选择");
		String[] province1 = { "一级", "二级", "三级","四级" };
		 arrayadapter = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_dropdown_item_1line, province1);
		s2.setAdapter(arrayadapter);
		final ListView listView =(ListView) this.findViewById(R.id.listview);
		Button del=(Button) this.findViewById(R.id.del);
		GenerationAdapter adapter=new GenerationAdapter(PlanActivity.this,arg);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	    mTvCusInfo=(TextView) this.findViewById(R.id.cusinfo);
		mTvType=(TextView) this.findViewById(R.id.type);
		mTvData=(TextView) this.findViewById(R.id.data);
		mTvSet=(TextView) this.findViewById(R.id.set);
		mTvCusInfo.setOnClickListener(this);
		mTvType.setOnClickListener(this);
		mTvData.setOnClickListener(this);
		mTvSet.setOnClickListener(this);
		add.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				arg.add(getListArgs());
				GenerationAdapter	 adapter1=new GenerationAdapter(PlanActivity.this,arg);
				listView.setAdapter(adapter1);
				adapter1.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(listView);	
				
			}				
		});
		del.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(arg.size() == 0){
					Toast.makeText(PlanActivity.this, "已经没有数据了", 2000).show();
				}else{
					arg.remove(arg.size()-1);
					GenerationAdapter	 adapter1=new GenerationAdapter(PlanActivity.this,arg);
					listView.setAdapter(adapter1);
					adapter1.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(listView);
				}
				
				
			}
			
		});
		Button next=(Button) this.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				guo_tpye();
//				LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
//				mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
//				layout.removeAllViews();
//				View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.next_layout, null);
//				layout.addView(view);
//				mTvType.setBackgroundResource(R.drawable.single_tag01_on);
//				ListView listView =(ListView) PlanActivity.this.findViewById(R.id.listview);
//				LiYiListAdapter adaper1=new LiYiListAdapter(PlanActivity. this, getData());
//				listView.setAdapter(adaper1);
//				adaper1.notifyDataSetChanged();
//				Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
//				ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//						PlanActivity.this,
//						android.R.layout.simple_dropdown_item_1line, str1);
//				s1.setAdapter(arrayadapter);
//				Spinner s2=(Spinner) PlanActivity.this.findViewById(R.id.s2);
//				 arrayadapter = new ArrayAdapter<String>(
//						PlanActivity.this,
//						android.R.layout.simple_dropdown_item_1line, str2);
//				 s2.setAdapter(arrayadapter); 
//				 
//				 Spinner s3=(Spinner) PlanActivity.this.findViewById(R.id.s3);
//				 arrayadapter = new ArrayAdapter<String>(
//						PlanActivity.this,
//						android.R.layout.simple_dropdown_item_1line, str3);
//				 s3.setAdapter(arrayadapter);
//				 
//				 
//				 Spinner s5=(Spinner) PlanActivity.this.findViewById(R.id.s5);
//				 arrayadapter = new ArrayAdapter<String>(
//						PlanActivity.this,
//						android.R.layout.simple_dropdown_item_1line, str5);
//				 s5.setAdapter(arrayadapter);
//				 
//				 Spinner s4=(Spinner) PlanActivity.this.findViewById(R.id.s4);
//				 arrayadapter = new ArrayAdapter<String>(
//						PlanActivity.this,
//						android.R.layout.simple_dropdown_item_1line, new String[]{"5年","10年","15年"});
//				 s4.setAdapter(arrayadapter);
//				 Spinner s6=(Spinner) PlanActivity.this.findViewById(R.id.s6);
//				 arrayadapter = new ArrayAdapter<String>(
//						PlanActivity.this,
//						android.R.layout.simple_dropdown_item_1line, new String[]{"5年","10年","15年"});
//				 s6.setAdapter(arrayadapter);
//				 Button next=(Button) PlanActivity.this.findViewById(R.id.next);//下一步
//				 Button fx=(Button) PlanActivity.this.findViewById(R.id.liyi_btn1);//净风险
//				    mTvCusInfo=(TextView) PlanActivity.this.findViewById(R.id.cusinfo);
//					mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
//					mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
//					mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
//					mTvCusInfo.setOnClickListener(PlanActivity.this);
//					mTvType.setOnClickListener(PlanActivity.this);
//					mTvData.setOnClickListener(PlanActivity.this);
//					mTvSet.setOnClickListener(PlanActivity.this);
//				 next.setOnClickListener(new OnClickListener(){
//						@Override
//						public void onClick(View v) {
//							LinearLayout layout=(LinearLayout) PlanActivity.this.findViewById(R.id.contents);
//							mTvCusInfo.setBackgroundResource(R.drawable.single_tag01);
//							mTvType.setBackgroundResource(R.drawable.single_tag01);
//							layout.removeAllViews();
//							View view=((LayoutInflater)PlanActivity. this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.data_layout, null);
//							layout.addView(view);
//							mTvData.setBackgroundResource(R.drawable.single_tag01_on);
//							Spinner s1=(Spinner) PlanActivity.this.findViewById(R.id.s1);
//							ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//									PlanActivity.this,
//									android.R.layout.simple_dropdown_item_1line, str1);
//							s1.setAdapter(arrayadapter);
//						    mTvCusInfo=(TextView)PlanActivity. this.findViewById(R.id.cusinfo);
//							mTvType=(TextView) PlanActivity.this.findViewById(R.id.type);
//							mTvData=(TextView) PlanActivity.this.findViewById(R.id.data);
//							mTvSet=(TextView) PlanActivity.this.findViewById(R.id.set);
//							mTvCusInfo.setOnClickListener(PlanActivity.this);
//							mTvType.setOnClickListener(PlanActivity.this);
//							mTvData.setOnClickListener(PlanActivity.this);
//							mTvSet.setOnClickListener(PlanActivity.this);
//							List<String[]> list=new ArrayList<String[]>();
//							list.add(str10)	;			
//							PlanDataAdapter plan=new PlanDataAdapter(PlanActivity.this,list);
//							ListView listView =(ListView) PlanActivity.this.findViewById(R.id.listview);
//							listView.setAdapter(plan);
//							plan.notifyDataSetChanged();
//							 Button submit=(Button) PlanActivity.this.findViewById(R.id.submit);//提交数据
//							 Button back=(Button) PlanActivity.this.findViewById(R.id.back);//返回
//							 submit.setOnClickListener(new OnClickListener(){
//									@Override
//									public void onClick(View v) {
//										new Thread(){
//											public void run(){
//												try {
//													sleep(2000);
//													handler.sendEmptyMessage(1);
//													handler.sendEmptyMessage(2);
//												} catch (InterruptedException e) {
//													// TODO Auto-generated catch block
//													e.printStackTrace();
//												}
//											}
//										}.start();
//										confirmShowWaitDialog();
//										readWaitProgressDialog.setMessage("正在提交数据...");
//									}				
//								});
//							 back.setOnClickListener(new OnClickListener(){
//									@Override
//									public void onClick(View v) {
//										Intent intent=new Intent(PlanActivity.this,InsuranceShowMainActivity.class);
//										PlanActivity.this.startActivity(intent);
//										PlanActivity.this.finish();
//									}				
//								});
//						}				
//					});
////				 fx.setOnClickListener(new OnClickListener(){
////						@Override
////						public void onClick(View v) {
////							
////							
////						}				
////					});
				 
			}				
		});
	}
	
}
