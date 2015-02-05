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
		"�³�������ϵͳ�����ֲᡪ��¼"
			,"�³�������ϵͳ�����ֲᡪ�ĵ��嵥"
		,"�³�������ϵͳ�����ֲᡪϵͳ����"
		,"�³�������ϵͳ�����ֲᡪϵͳά��"
		,"��������ʵ��2012�棩"
		,"���㹫ʽ"
	};
	private String[] newstr3 = {
			"�������Ƚڵ�"
			,"����֤����"
			,"���ۺ���ڵ�"
			,"�鿱������ؽڵ�"
			,"�����ڵ�","����ڵ�"
			,"�᰸��ؽڵ�"
			,"����׷����ؽڵ�"
			,"���˸��١�ҽ����˽ڵ�","ͨ��ڵ�"
			,"�����ռ�����Ԥ����ؽڵ�"
			,"�ۺϽ᰸�����д���ڵ�"
			,"�ڵ��嵥"
		};
	private String[] newstr4 = {
			"���ó����켰����ʶ��1"
			,"���ó����켰����ʶ��2"
			,"���ó����켰����ʶ��3"
			,"���ó����켰����ʶ��4"
			,"������������ѵ"
			,"�Ʋ��������ⲿר����ҵ���ձ�����"
			,"�����ͻ������ⰸ�������յ����"
			,"������Ϣ��"
			,"Ȩ�����÷���(���ִ����"
			,"��Ӱ��ϵͳ"
			,"�������졢�����������ͺ���ѵ�̲�"
			,"��θ���ѧϰ����"
		};
	private String[] newstr5 = {
			"��������δ�����Ժ֧����˾"
			,"�����ʧ���⳥"
			,"��ǿ����Ʋ���"
			,"��ҵ�ձ��չ�˾��ֱ���������⸶"
			,"���ݲ�֧���󹤷�"
			,"δǩ���������Ժ�ж���Ч"
			,"��֤��ʻʤ��"
			,"���ǩ������"
		};
	private String[] newstr7 = {
			"�й�����ȫ�����뿪չ�ۺ����������󵼹���","�й����ٱ��չɷ����޹�˾��������һһ�����ҵ��(A��)"
			,"�й����ٷ�ϴǮ�������������б���","��ǣ�ֹ��� �̶��й����ٶ��滭��Ʒչ��Ļ"
			,"���������Ļ� ֧��ԭ������ ���й����ٱ���������������ѧ��������������ԭ����","�����ͻ�Ȩ�� ������ҵ�·�"
			,"�й����ٷ�ϴǮ�������������б���","�����б���,�����б���-�й��������š�С��ȫ�Ҹ����������Ĵ�����ǩ��"
			,"���١���»���𡱡��������ǡ������������з���","�й���������Ӧ��Ԥ������Ϊ�����������"
			,"�ຣ������ǫ���й����ٶ�ͯ����Ժ��������","�й������ڹ��һ�������¡�ؾٰ조�й��������б���ʮ������䡱���"
		};
	
	private String[] newstr6 = {
			"������������ѵ"
			,"�Ʋ��������ⲿר����ҵ���ձ�����"
			,"�����ͻ������ⰸ�������յ����"
			,"������Ϣ��"
			,"Ȩ�����÷���(���ִ����"
			,"��Ӱ��ϵͳ"
			,"�������졢�����������ͺ���ѵ�̲�"
			,"��θ���ѧϰ����"
		};
	private String[] newstr8 = {
			"�³�������ϵͳ�����ֲᡪͨ�ⰸ��"
			,"�³�������ϵͳ�����ֲᡪ���ⰸ��"
			,"��������δ�����Ժ֧����˾"
			,"�����ʧ���⳥"
			,"��ǿ����Ʋ���"
			,"��ҵ�ձ��չ�˾��ֱ���������⸶"
			,"���ݲ�֧���󹤷�"
			,"δǩ���������Ժ�ж���Ч"
			,"��֤��ʻʤ��"
			,"���ǩ������"
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
	private List<HashMap<String,String>> listItems = new ArrayList<HashMap<String,String>>(); // listview�е�������
	
	private Button h_backhomebtn;
	private Button homebtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//ȥ���ֻ��ϵı�����
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
//		map1.put("content", "�й����ٱ��չɷ����޹�˾��������һһ�����ҵ��(A��)");
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
