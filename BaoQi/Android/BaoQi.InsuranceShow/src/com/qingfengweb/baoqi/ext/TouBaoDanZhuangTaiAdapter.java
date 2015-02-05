package com.qingfengweb.baoqi.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qingfengweb.baoqi.insuranceShow.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TouBaoDanZhuangTaiAdapter extends BaseAdapter{
	List<Map<String,String>> list=new ArrayList<Map<String,String>>();;
	
	LayoutInflater layout;
	public TouBaoDanZhuangTaiAdapter(LayoutInflater layout) {
		// TODO Auto-generated constructor stub
		this.layout=layout;
		Map<String,String> map1=new HashMap<String,String>();
		map1.put("jihuashu", "1");
		map1.put("time", "20120304666");
		map1.put("toubaoren", "梁先生");
		map1.put("nianling", "刘星星");
		map1.put("beibaoren", "2012-2-2");
		map1.put("beibaonianling", "6666");
		map1.put("beibaoren1", "2012-3-15");
		map1.put("beibaonianling1", "未上传"); 	
		Map<String,String> map2=new HashMap<String,String>();
		map2.put("jihuashu", "2");
		map2.put("time", "20120677766");
		map2.put("toubaoren", "刘星星");
		map2.put("nianling", "刘星星");
		map2.put("beibaoren", "2012-3-4");
		map2.put("beibaonianling", "77777");
		map2.put("beibaoren1", "2012-3-15");
		map2.put("beibaonianling1", "已上传");
		Map<String,String> map3=new HashMap<String,String>();
		map3.put("jihuashu", "3");
		map3.put("time", "2014564666");
		map3.put("toubaoren", "毛豆豆");
		map3.put("nianling", "刘星星");
		map3.put("beibaoren", "2012-3-6");
		map3.put("beibaonianling", "44353");
		map3.put("beibaoren1", "2012-3-15");
		map3.put("beibaonianling1", "未上传");
		Map<String,String> map4=new HashMap<String,String>();
		map4.put("jihuashu", "4");
		map4.put("time", "201223266666");
		map4.put("toubaoren", "翟先生");
		map4.put("nianling", "刘星星");
		map4.put("beibaoren", "2012-3-15");
		map4.put("beibaonianling", "465678");
		map4.put("beibaoren1", "2012-3-15");
		map4.put("beibaonianling1", "已上传");
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
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
		TextView t8=(TextView) li.findViewById(R.id.text_8);
		Map<String,String> map=list.get(position);
		t1.setText(map.get("jihuashu"));
		t2.setText(map.get("time"));
		t3.setText(map.get("toubaoren"));
		t4.setText(map.get("nianling"));
		t5.setText(map.get("beibaoren"));
		t6.setText(map.get("beibaonianling"));
		t7.setText(map.get("beibaoren1"));
		t8.setText(map.get("beibaonianling1"));
		return li;
	}
	
}
