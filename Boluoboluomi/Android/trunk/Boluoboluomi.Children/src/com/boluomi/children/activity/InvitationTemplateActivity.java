package com.boluomi.children.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.boluomi.children.R;
import com.boluomi.children.adapter.InvitationTemplateAdapter;
import com.boluomi.children.data.MyApplication;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

public class InvitationTemplateActivity extends BaseActivity{
	private Button backBtn;
	private GridView gv;
	public ArrayList<HashMap<String,Object>> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_invatationtemplate);
		 findView();
		 initData();
		 notifyGv();
	}
	private void findView(){
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		gv = (GridView) findViewById(R.id.gv);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}
	private void initData(){
		list = new ArrayList<HashMap<String,Object>>();
		int image[] ={R.drawable.invitation_tu1,R.drawable.invitation_tu2,R.drawable.invitation_tu3,R.drawable.invitation_tu4,
				R.drawable.invitation_tu5,R.drawable.invitation_tu6,R.drawable.invitation_tu7,R.drawable.invitation_tu8,};
		String text[] ={"结婚光荣","Yes,i do!","我们结婚啦","快乐人生","喜结良缘","我家有喜","甜甜蜜蜜","爱的航线"};
		for(int i=0;i<image.length;i++){
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("image", BitmapFactory.decodeResource(getResources(), image[i]));
			map.put("title", text[i]);
			list.add(map);
		}
	}
	private void notifyGv(){
		gv.setAdapter(new InvitationTemplateAdapter(this, list));
		gv.setOnItemClickListener(new GvListener());
	}
	
	class GvListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent i = new Intent(InvitationTemplateActivity.this,TemplateInfoActivity.class);
			MyApplication.getInstant().setTemplateid(position+"");
			i.putExtra("type", position);
			startActivity(i);
		}
		
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}
		super.onClick(v);
	}
}
