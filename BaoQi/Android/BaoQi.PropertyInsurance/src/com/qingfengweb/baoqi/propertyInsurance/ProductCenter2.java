package com.qingfengweb.baoqi.propertyInsurance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.propertyInsurance.ext.Product2Adapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class ProductCenter2 extends Activity {

	private ListView listview;
	private List<HashMap<String,Object>> listItems = new ArrayList<HashMap<String,Object>>();
	
	
	//չʾͼƬ
			private Integer[] mThumbIds1 = {
					R.drawable.ka1,R.drawable.ka2
					,R.drawable.ka3,R.drawable.ka4
					,R.drawable.ka5,R.drawable.ka6
					,R.drawable.ka7,R.drawable.ka8
					,R.drawable.ka9,R.drawable.ka10
					,R.drawable.ka11,R.drawable.ka12
				};
	
	
	private String[] str1 = {"����E��տ�","ȫ������E��տ�"
			,"�ž��鸣E��տ�","������ʹE��տ�"
			,"�Ϻ����������ۺϱ��տ�","��������E��տ�"
			,"�ػ���ʹE��տ�","����ƽ��E��տ�"
			,"ҽ�ƾ�ԮE��տ�","��������E��տ�"
			,"��ͨ����E��տ�","��������E��տ�"};

	private String[] str2 = {"һ��Ͷ�����ɻ�������Ʋ�˫�ر��ϣ�����סԺ������������ҽ�Ʊ���ͬʱ���У��������б��ϵ���Ч���䡣����Ϊ����͸����ˣ�Ͷ�����̼򵥣�ȫ���������⡣"
			,"һ��Ͷ����ȫ���������ǣ�ÿ����ͥ���ɱ�5���������ˡ�ͬʱ���ϼ�ͥ��Ա����ȫ����ͥ�Ʋ���ȫ������������֮�ǡ�����Ϊ����͸����ˣ�Ͷ�����̼򵥣�ȫ���������⡣"
			,"����ȫ�棬�������⡢��������ͨ���⡢������Ԯ���ϣ�����ߣ���������ʿ�����ϡ��ṩһ����ȫ������ҽ�ƾ�Ԯ������ֻ��Ҫ����һ��24Сʱ����绰��400-6506-119�������������������������ţ�����Ϊ����͸����ˣ�Ͷ�����̼򵥣�ȫ���������⡣"
			,"רΪѧ�����ƣ�100Ԫ����ѧ��ȫ�������˺����գ����ñ��ˡ����ṩ3��Ԫ����ҽ�ơ�Ͷ�����̼򵥣�ȫ���������⡣"
			,"30Ԫ���ϼ�����Ա�����˺������ñ��ˡ����������ڿ��µ�12333��������������Ա��"
			,"ר��Ϊ�����˶��Ƶ������˱��տ���Ϊ�������ճ������������ܵ����˺��ṩ���ϣ����ǹ�����ؽ���λ���⡢��ͨ���⡢һ���ճ�����ȣ����ṩסԺ���������רҵҽ�ƾ�Ԯ����"
			,"ר��ΪŮ�Զ����������ṩŮ���ճ�����������˺����ϣ������ӱ���Ů�Ը��ư�֢����Ů�Ե���ѱ���ѡ��"
			,"�ṩ15��Ԫ���������/�м�/�����˱��գ��Լ�1��Ԫ����ҽ�Ʊ���֮�⣬���ṩ�������񸽼��ײͣ�����������סԺ�渶������������������������ԤԼ�Ƚ�������"
			,"�ṩһ�������������ۺϱ��ϼ�ҽ�Ƶ渶�Ƚ���ҽ�ƾ�Ԯ������ֻ��Ҫ����һ��24Сʱ����绰��400-6506-119�������������������������ţ�Ͷ�����̼򵥣�ȫ���������⡣"
			,"�ṩȫ�꺽�����Ᵽ�ϣ�����ڻ�����æ�����յķ��ա�����һ��������Ᵽ�ϣ�ƽʱ���á��ڼ��ճ��У�������ʱ��ء�����Ϊ����͸����ˣ�Ͷ�����̼򵥣�ȫ���������⡣"
			,"�ṩһ�������������ͨ����ʱ�������˺���ʼ�����м����ϡ����á��ڼ��ճ��У�������ʱ��ء�����Ϊ����͸����ˣ�Ͷ�����̼򵥣�ȫ���������⡣"
			,"����Ϊ����˾�������յ���Ч���䣬����ʵ�ݡ����ݳ�ʱ��Ҳ���������Ᵽ�ϡ�����ȫ��������·��Ԯ����Ͷ�����̼򵥣�ȫ���������⡣"};
	private String[] str3 = {"16-69����"
			,"����֮�ҡ�����������������16-69���꣩"
			,"16-60����"
			,"1-22�����ѧ��"
			,"16��69������Ϻ���������������Ա��"
			,"80�������µ�������"
			,"18-40�����Ů��"
			,"18-65����"
			,"16-69����"
			,"18��65���꾭�����ε���ʿ��"
			,"18��65���꾭�����ε���ʿ��"
			,"18��65����"};
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
        
		setContentView(R.layout.l_productcenter2);
		
		h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductCenter2.this, PropertyInsuranceMainActivity.class);
			  	ProductCenter2.this.startActivity(intent);
			  	ProductCenter2.this.finish();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductCenter2.this, PropertyInsuranceMainActivity.class);
			  	ProductCenter2.this.startActivity(intent);
			  	ProductCenter2.this.finish();
			}
		});
		listview = (ListView)findViewById(R.id.listview);
		
		notifyAdapter();
	}
	
	
	
	public void notifyAdapter(){
		getlistitem();
		Product2Adapter adapter = new Product2Adapter(this, listItems);
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
		 intent.setClass(ProductCenter2.this, PropertyInsuranceMainActivity.class);
		 ProductCenter2.this.startActivity(intent);
		 ProductCenter2.this.finish();
	  }
	  return true;
	}
}
