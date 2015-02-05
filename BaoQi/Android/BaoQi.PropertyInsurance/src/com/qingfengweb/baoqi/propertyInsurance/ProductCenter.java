package com.qingfengweb.baoqi.propertyInsurance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.propertyInsurance.ext.ProductAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class ProductCenter extends Activity {

	private ListView listview;
	private List<HashMap<String,Object>> listItems = new ArrayList<HashMap<String,Object>>();
	
	private int class_baoxian = 0;
	
	//展示图片
			private Integer[] mThumbIds1 = {
					R.drawable.ywtu1,R.drawable.ywtu2
					,R.drawable.ywtu3,R.drawable.ywtu4
				};
	
	
	private String[] str1 = {"一年期综合意外保险","交通意外险"
			,"家庭综合保险","驾乘综合保险"};

	private String[] str2 = {"保障工作生活中的一般意外和交通意外伤害，还提供门诊与住院医疗保障，另有意外住院误工、护理津贴及紧急医疗救援服务，保额高达50万元，是给家人、企业员工的有力保障！"
			,"提供以乘客身份乘坐飞机、火车（含高铁、地铁）、轮船、汽车（含公交、出租车）等公共交通工具时的意外伤害身故及意外残疾保障。航空意外保额最高可达800万，还有航班延误保障！"
			,"全家人的综合保障计划，被保险人为两人以上，保障自己和配偶及子女在工作生活中的意外伤害，还提供门诊与住院医疗保障，另有意外住院收入补偿，以及24小时电话医疗咨询，一次购买，全家安心！"
			,"专为私家车驾驶人设计，为私家车驾驶人提供最高40万元驾乘人身意外、4万元医疗保障，还提供意外医疗垫付、道路救援服务，另有最高达200万元的交通意外伤害保障，开车坐车全能保。"};
	private String[] str3 = {"1-65周岁","80周岁以下，经常乘坐交通工具的人士","30天-60周岁","私家车主及其家庭成员"};
	private String[] str4 = {"1年","1-12个月任选"};
	private String[] str5 = {"电子保单","纸质保单"};
	private View homebtn;
	private View h_backhomebtn; 
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setContentView(R.layout.l_productcenter);
		
		h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductCenter.this, PropertyInsuranceMainActivity.class);
			  	ProductCenter.this.startActivity(intent);
			  	ProductCenter.this.finish();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductCenter.this, PropertyInsuranceMainActivity.class);
			  	ProductCenter.this.startActivity(intent);
			  	ProductCenter.this.finish();
			}
		});
		listview = (ListView)findViewById(R.id.listview);
		
		notifyAdapter();
	}
	
	
	
	public void notifyAdapter(){
		getlistitem();
		ProductAdapter adapter = new ProductAdapter(this, listItems);
		listview.setAdapter(adapter);
	}
	
	
	
	public void getlistitem(){
		listItems.clear();
		
		int i;
		int index = 0;
		int index1 = 0;
		for(i = 0; i<str1.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("text1", str1[i]);
			map.put("text2", str2[i]);
			map.put("text3", str3[i]);
			if(index >= str4.length){
				index = 0;
			}
			map.put("text4", str4[index]);
			index++;
			map.put("text5", str5[0]);
			
			if(index1 >= mThumbIds1.length){
				index1 = 0;
			}
			map.put("image", mThumbIds1[index1]);
			index1++;
			listItems.add(map);
		}
		
		
//		HashMap<String, Object> map1 = new HashMap<String, Object>();
//		map1.put("text1", str1[0]);
//		map1.put("text2", mThumbIds[class_baoxian]);
//		map1.put("text3", str2[0]);
//		map1.put("text4", str3[0]);
//		map1.put("text5", str4[0]);
//		map1.put("text6", str5[0]);
//		listItems.add(map1);
//		HashMap<String, Object> map2 = new HashMap<String, Object>();
//		map2.put("text1", str1[1]);
//		map2.put("text2", mThumbIds[class_baoxian]);
//		map2.put("text3", str2[0]);
//		map2.put("text4", str3[1]);
//		map2.put("text5", str4[1]);
//		map2.put("text6", str5[0]);
//		listItems.add(map2);
//		HashMap<String, Object> map3 = new HashMap<String, Object>();
//		map3.put("text1", str1[2]);
//		map3.put("text2", mThumbIds[class_baoxian]);
//		map3.put("text3", str2[1]);
//		map3.put("text4", str3[2]);
//		map3.put("text5", str4[2]);
//		map3.put("text6", str5[1]);
//		listItems.add(map3);
	}
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		 Intent intent = new Intent();
		 intent.setClass(ProductCenter.this, PropertyInsuranceMainActivity.class);
		 ProductCenter.this.startActivity(intent);
		 ProductCenter.this.finish();
	  }
	  return true;
	}
}
