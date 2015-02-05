package com.qingfengweb.piaoguanjia.orderSystem;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.qingfengweb.piaoguanjia.orderSystem.adapter.ProduceAdapter;
import com.qingfengweb.piaoguanjia.orderSystem.util.MessageBox;
import com.qingfengweb.piaoguanjia.orderSystem.view.XListView;
import com.qingfengweb.piaoguanjia.orderSystem.view.XListView.IXListViewListener;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewProductActivity extends MainFrameActivity implements IXListViewListener {

	@ViewInject(R.id.xlist)
	private XListView xlist;
	private BaseAdapter adapter;
	private ArrayList<String> list;
	@ViewInject(R.id.parent)
	private View parent;
	
	@ViewInject(R.id.linear_view)
	private LinearLayout linear_view;
	
	@ViewInject(R.id.linear_address)
	private LinearLayout linear_address;
	private int type = 0;//0最新产品，1自由行2酒店3景点门票4跟团游,5收藏
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type = getIntent().getIntExtra("type", 0);
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_main_list, null);
		addCenterView(view);
		ViewUtils.inject(this);
		parent.setOnTouchListener(this);
		initview();
		notifyAdapter();
	}
	private void initview() {
		xlist.setDivider(null);
		xlist.setPullLoadEnable(true);
		xlist.setPullRefreshEnable(true);
		xlist.setXListViewListener(this);
		switch (type) {
		case 0:
			tv_title.setText("最新产品");
			break;
		case 1:
			tv_title.setText("自由行");
			break;
		case 2:
			tv_title.setText("酒店");
			break;
		case 3:
			tv_title.setText("景点门票");
			break;
		case 4:
			tv_title.setText("跟团游");
		case 5:
			linear_view.setVisibility(View.GONE);
			tv_title.setText("我的收藏");
			break;
		}
	}
	private void notifyAdapter() {
		getlist();
		if(adapter==null){
			adapter = new ProduceAdapter(this, list);
			xlist.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
	}
	private void getlist() {
		if(list==null){
			list = new ArrayList<String>();
		}
		list.clear();
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
	}
	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				xlist.stopLoadMore();
				xlist.stopRefresh();
			}
		}, 2000);
	}
	@Override
	public void onLoadMore() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				xlist.stopLoadMore();
				xlist.stopRefresh();
			}
		}, 2000);
	}
	
	@OnItemClick(R.id.xlist)
	public void onListItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent(this,ProduceDetailActivity.class);
		startActivity(i);
	}
	
	@OnClick(R.id.linear_address)
	public void btnonClick(View v) {
		Intent i = new Intent(this, AddressActivity.class);
		startActivity(i);
		super.onClick(v);
	}
}
