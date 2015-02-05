/**
 * 
 */
package com.boluomi.children.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.boluomi.children.R;
import com.boluomi.children.adapter.GrowUpMsgAdapter;

/**
 * @author 刘星星
 *
 */
public class GrowupMsgActivity extends BaseActivity{
	private ListView listView;
	private List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	private Button backBtn;
	private TextView photoName,photoIntro;
	private EditText msgEt;
	private TextView sendMsgTv;
	GrowUpMsgAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_growupmsg);
		findview();
		initData();
	}
	private void initData(){
		for(int i=0;i<10;i++){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("key", "value");
			list.add(map);
		}
		notifyAdapter();
	}
	private void notifyAdapter(){
		 adapter = new GrowUpMsgAdapter(this, list);
		listView.setAdapter(adapter);
	}
	private void findview(){
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		backBtn = (Button) findViewById(R.id.backBtn);
		photoName = (TextView) findViewById(R.id.photoName);
		photoIntro = (TextView) findViewById(R.id.photoIntro);
		msgEt =  (EditText) findViewById(R.id.sendMsgEt);
		sendMsgTv = (TextView) findViewById(R.id.sendMsgBtn);
		backBtn.setOnClickListener(this);
		sendMsgTv.setOnClickListener(this);
		sendMsgTv.setTextColor(Color.GRAY);
		msgEt.addTextChangedListener(textWatcher);
	}
	TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(msgEt.getText().toString().trim().length()>0){
				sendMsgTv.setClickable(true);
				sendMsgTv.setTextColor(Color.WHITE);
			}else{
				sendMsgTv.setClickable(false);
				sendMsgTv.setTextColor(Color.GRAY);
			}
			
		}
	};
	@Override
	public void onClick(View v) {
		if(v == backBtn){// 返回按钮
			finish();
		}else if(v == sendMsgTv){//发送消息按钮
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("name", "");
			map.put("content", msgEt.getText().toString());
			list.add(map);
			adapter.notifyDataSetChanged();
			listView.setSelection(list.size()-1);
			msgEt.setText("");
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0); //强制隐藏键盘
		}
		super.onClick(v);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		super.onItemClick(arg0, arg1, arg2, arg3);
	}
}
