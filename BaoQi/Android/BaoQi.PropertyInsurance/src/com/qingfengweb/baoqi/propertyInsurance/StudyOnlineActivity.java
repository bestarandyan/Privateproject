package com.qingfengweb.baoqi.propertyInsurance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.propertyInsurance.ext.IntroduceListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

public class StudyOnlineActivity extends Activity {
	
	
	private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8;
	private String[] newstr1 = {
		"新车险理赔系统操作手册—附录"
			,"新车险理赔系统操作手册—文档清单"
		,"新车险理赔系统操作手册—系统概述"
		,"新车险理赔系统操作手册—系统维护"
		,"车险理赔实务（2012版）"
		,"理算公式"
	};
	private String[] newstr3 = {
			"报案调度节点"
			,"报表单证管理"
			,"报价核损节点"
			,"查勘定损相关节点"
			,"公共节点","核赔节点"
			,"结案相关节点"
			,"立案追偿相关节点"
			,"人伤跟踪、医疗审核节点","通赔节点"
			,"资料收集理算预赔相关节点"
			,"综合结案、并行处理节点"
			,"节点清单"
		};
	private String[] newstr4 = {
			"商用车构造及车型识别1"
			,"商用车构造及车型识别2"
			,"商用车构造及车型识别3"
			,"商用车构造及车型识别4"
			,"货车定损技能培训"
			,"财产险理赔外部专家行业对照表及名单"
			,"大中型货车理赔案件及风险点分析"
			,"车型信息表"
			,"权限配置方案(险种代码表"
			,"新影像系统"
			,"车辆构造、汽车分类与型号培训教材"
			,"如何跟踪学习车型"
		};
	private String[] newstr5 = {
			"车辆过户未变更法院支持我司"
			,"间接损失不赔偿"
			,"交强险醉酒不赔"
			,"商业险保险公司不直接向三者赔付"
			,"退休不支持误工费"
			,"未签名免责条款法院判定无效"
			,"无证驾驶胜诉"
			,"因代签名败诉"
		};
	private String[] newstr7 = {
			"中国人寿全面深入开展综合治理销售误导工作","中国人寿保险股份有限公司公布二零一一年年度业绩(A股)"
			,"中国人寿反洗钱工作获人民银行表彰","“牵手国寿 绿动中国”少儿绘画作品展开幕"
			,"引领网络文化 支持原创歌曲 “中国人寿杯”第三届网络文学艺术大赛暨网络原创歌","保护客户权益 树立行业新风"
			,"中国人寿反洗钱工作获人民银行表彰","户户有保险,人人有保障-中国人寿首张“小额全家福”保单在四川资阳签订"
			,"国寿“福禄鑫尊”“安欣无忧”今起联袂上市发售","中国人寿启动应急预案积极为两起矿难理赔"
			,"青海玉树囊谦县中国人寿儿童福利院开工建设","中国人寿在国家会议中心隆重举办“中国人寿银行保险十周年庆典”大会"
		};
	
	private String[] newstr6 = {
			"货车定损技能培训"
			,"财产险理赔外部专家行业对照表及名单"
			,"大中型货车理赔案件及风险点分析"
			,"车型信息表"
			,"权限配置方案(险种代码表"
			,"新影像系统"
			,"车辆构造、汽车分类与型号培训教材"
			,"如何跟踪学习车型"
		};
	private String[] newstr8 = {
			"新车险理赔系统操作手册—通赔案例"
			,"新车险理赔系统操作手册—自赔案例"
			,"车辆过户未变更法院支持我司"
			,"间接损失不赔偿"
			,"交强险醉酒不赔"
			,"商业险保险公司不直接向三者赔付"
			,"退休不支持误工费"
			,"未签名免责条款法院判定无效"
			,"无证驾驶胜诉"
			,"因代签名败诉"
		};
	
	private String[] timestr = {
			 "2012-3-27","2012-3-27"
			,"2012-3-15","2012-3-09"
			,"2012-3-05","2012-2-25"
			,"2012-2-20","2012-2-15"
			,"2012-2-09","2012-2-09"
			,"2012-2-05","2012-3-01"
		};
	
	private ListView listView;
	private List<HashMap<String,String>> listItems = new ArrayList<HashMap<String,String>>(); // listview中的数据项
	
	private Button h_backhomebtn;
	private Button homebtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.l_studyonline);
        
        
        
        
        h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(StudyOnlineActivity.this, PropertyInsuranceMainActivity.class);
			  	StudyOnlineActivity.this.startActivity(intent);
			  	StudyOnlineActivity.this.finish();
			  	System.gc();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(StudyOnlineActivity.this, PropertyInsuranceMainActivity.class);
			  	StudyOnlineActivity.this.startActivity(intent);
			  	StudyOnlineActivity.this.finish();
			  	System.gc();
			}
		});
        
        listView = (ListView)findViewById(R.id.listview);
        
        
        notifyAdapter(newstr1);
        
        btn1 = (Button)findViewById(R.id.btn_1);
        btn2 = (Button)findViewById(R.id.btn_2);
        btn3 = (Button)findViewById(R.id.btn_3);
        btn4 = (Button)findViewById(R.id.btn_4);
        btn5 = (Button)findViewById(R.id.btn_5);
        btn6 = (Button)findViewById(R.id.btn_6);
        btn7 = (Button)findViewById(R.id.btn_7);
        btn8 = (Button)findViewById(R.id.btn_8);
        
        
        
        btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn1.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				btn7.setBackgroundResource(R.drawable.single_tag01);
				btn8.setBackgroundResource(R.drawable.single_tag01);				
				btn1.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				btn7.setTextColor(Color.WHITE);
				btn8.setTextColor(Color.WHITE);
				notifyAdapter(newstr1);
				
				
			}
		});
        
        btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn2.setBackgroundResource(R.drawable.single_tag01_on);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				btn7.setBackgroundResource(R.drawable.single_tag01);
				btn8.setBackgroundResource(R.drawable.single_tag01);				
				btn2.setTextColor(Color.BLACK);
				btn1.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				btn7.setTextColor(Color.WHITE);
				btn8.setTextColor(Color.WHITE);
				notifyAdapter(newstr1);
			}
		});

        btn3.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
				btn3.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				btn7.setBackgroundResource(R.drawable.single_tag01);
				btn8.setBackgroundResource(R.drawable.single_tag01);				
				btn3.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				btn7.setTextColor(Color.WHITE);
				btn8.setTextColor(Color.WHITE);
				
				notifyAdapter(newstr3);
        	}
        });
        btn4.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
				btn4.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				btn7.setBackgroundResource(R.drawable.single_tag01);
				btn8.setBackgroundResource(R.drawable.single_tag01);				
				btn4.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				btn7.setTextColor(Color.WHITE);
				btn8.setTextColor(Color.WHITE);
				
				notifyAdapter(newstr4);
        	}
        });
        btn5.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
				btn5.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				btn7.setBackgroundResource(R.drawable.single_tag01);
				btn8.setBackgroundResource(R.drawable.single_tag01);				
				btn5.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				btn7.setTextColor(Color.WHITE);
				btn8.setTextColor(Color.WHITE);
				
				notifyAdapter(newstr5);
        	}
        });
        
        btn6.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
				btn6.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn7.setBackgroundResource(R.drawable.single_tag01);
				btn8.setBackgroundResource(R.drawable.single_tag01);				
				btn6.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn7.setTextColor(Color.WHITE);
				btn8.setTextColor(Color.WHITE);
				
				notifyAdapter(newstr6);
        	}
        });
        btn7.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
				btn7.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn8.setBackgroundResource(R.drawable.single_tag01);				
				btn7.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn8.setTextColor(Color.WHITE);
				
				notifyAdapter(newstr7);
        	}
        });
        btn8.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
				btn8.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				btn7.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);				
				btn8.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				btn7.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				
				notifyAdapter(newstr8);
        	}
        });
        
	}
	
	public void notifyAdapter(String[] str){
		getListItems(str);
		IntroduceListAdapter adapter = new IntroduceListAdapter(this, listItems);
        listView.setDivider(null);
        listView.setAdapter(adapter);
		
	}
	
	public List<HashMap<String, String>> getListItems(String[] str) {
		
		listItems.clear();
		int i ;
		int index = 0;
		for(i=0;i<str.length;i++){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("content", str[i]);
			if(index>=timestr.length)
				index =0;
			map.put("time", timestr[index]);
			listItems.add(map);
			index++;
		}
		
//		HashMap<String, String> map1 = new HashMap<String, String>();
//		map1.put("content", "中国人寿保险股份有限公司公布二零一一年年度业绩(A股)");
//		map1.put("time", "2012-02-07");
//		listItems.add(map1);
//		listItems.add(map1);
//		listItems.add(map1);
//		listItems.add(map1);
//		listItems.add(map1);
//		listItems.add(map1);
//		listItems.add(map1);
		
		return listItems;
	}
	
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
			  	Intent intent = new Intent();
			  	intent.setClass(StudyOnlineActivity.this, PropertyInsuranceMainActivity.class);
			  	StudyOnlineActivity.this.startActivity(intent);
			  	StudyOnlineActivity.this.finish();
			  	System.gc();
		 
	  }
	  return true;
	}
}
