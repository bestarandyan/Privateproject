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
	
	//չʾͼƬ
			private Integer[] mThumbIds1 = {
					R.drawable.ywtu1,R.drawable.ywtu2
					,R.drawable.ywtu3,R.drawable.ywtu4
				};
	
	
	private String[] str1 = {"һ�����ۺ����Ᵽ��","��ͨ������"
			,"��ͥ�ۺϱ���","�ݳ��ۺϱ���"};

	private String[] str2 = {"���Ϲ��������е�һ������ͽ�ͨ�����˺������ṩ������סԺҽ�Ʊ��ϣ���������סԺ�󹤡��������������ҽ�ƾ�Ԯ���񣬱���ߴ�50��Ԫ���Ǹ����ˡ���ҵԱ�����������ϣ�"
			,"�ṩ�Գ˿���ݳ����ɻ����𳵣������������������ִ��������������������⳵���ȹ�����ͨ����ʱ�������˺���ʼ�����м����ϡ��������Ᵽ����߿ɴ�800�򣬻��к��������ϣ�"
			,"ȫ���˵��ۺϱ��ϼƻ�����������Ϊ�������ϣ������Լ�����ż����Ů�ڹ��������е������˺������ṩ������סԺҽ�Ʊ��ϣ���������סԺ���벹�����Լ�24Сʱ�绰ҽ����ѯ��һ�ι���ȫ�Ұ��ģ�"
			,"רΪ˽�ҳ���ʻ����ƣ�Ϊ˽�ҳ���ʻ���ṩ���40��Ԫ�ݳ��������⡢4��Ԫҽ�Ʊ��ϣ����ṩ����ҽ�Ƶ渶����·��Ԯ����������ߴ�200��Ԫ�Ľ�ͨ�����˺����ϣ���������ȫ�ܱ���"};
	private String[] str3 = {"1-65����","80�������£�����������ͨ���ߵ���ʿ","30��-60����","˽�ҳ��������ͥ��Ա"};
	private String[] str4 = {"1��","1-12������ѡ"};
	private String[] str5 = {"���ӱ���","ֽ�ʱ���"};
	private View homebtn;
	private View h_backhomebtn; 
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//ȥ���ֻ��ϵı�����
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
