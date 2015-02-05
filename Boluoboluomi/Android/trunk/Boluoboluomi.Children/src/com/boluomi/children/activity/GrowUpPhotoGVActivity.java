/**
 * 
 */
package com.boluomi.children.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.boluomi.children.R;
import com.boluomi.children.adapter.GrowUpPhotoGvAdapter;
import com.boluomi.children.data.MyApplication;
import com.qingfengweb.imagehandle.PicHandler;

/**
 * @author 刘星星
 * @createDate 2013/11/12
 *
 */
public class GrowUpPhotoGVActivity extends BaseActivity{
	private Button backBtn;
	private ImageButton takePhotoBtn;
	private GridView gv = null;
	private List<Map<String,Object>> list;
	private TextView titleTv;
	private String growupName = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_growupphotogv);
		findview();
		initData();
	}
	@SuppressWarnings("unchecked")
	private void initData(){
		list = (List<Map<String, Object>>) getIntent().getSerializableExtra("photoList");
		growupName = getIntent().getStringExtra("growupName");
		titleTv.setText(growupName);
		Map<String,Object> map = null;
		int size = (MyApplication.getInstant().getWidthPixels()-4)/3;
		for(int i=0;i<list.size();i++){
			map = list.get(i);
			Bitmap bitmap = PicHandler.getSquareImg(map.get("imglocalurl").toString(),size*3);
			bitmap = PicHandler.cutImg(bitmap,size);
			map.put("bm", bitmap);
		}
		notifyAdapter();
	}
	private void notifyAdapter(){
		GrowUpPhotoGvAdapter adapter = new GrowUpPhotoGvAdapter(this, list);
		gv.setAdapter(adapter);
	}
	private void findview(){
		backBtn = (Button) findViewById(R.id.backBtn);
		takePhotoBtn = (ImageButton) findViewById(R.id.takePhoto);
		backBtn.setOnClickListener(this);
		takePhotoBtn.setOnClickListener(this);
		gv = (GridView) findViewById(R.id.photoGv);
		gv.setOnItemClickListener(this);
		titleTv = (TextView) findViewById(R.id.title);
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("imgpath", list.get(i).get("imglocalurl"));
			map.put("photoname", list.get(i).get("photoname"));
			map.put("imageid", "1");
			list1.add(map);
		}
		Intent intent = new Intent(this,GrowUpPhotoPreviewActivity.class);
		intent.putExtra("photoList", (Serializable)list1);
		intent.putExtra("growupName", growupName);
		intent.putExtra("index", arg2);
		startActivity(intent);
		super.onItemClick(arg0, arg1, arg2, arg3);
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}else if(v == takePhotoBtn){
			
		}
		super.onClick(v);
	}
}
