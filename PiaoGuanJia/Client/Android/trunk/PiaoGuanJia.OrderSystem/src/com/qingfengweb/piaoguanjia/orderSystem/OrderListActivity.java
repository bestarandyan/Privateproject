package com.qingfengweb.piaoguanjia.orderSystem;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.qingfengweb.piaoguanjia.orderSystem.adapter.OrderAdapter;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderListActivity extends MainFrameActivity implements
		IXListViewListener {

	@ViewInject(R.id.xlist)
	private XListView xlist;
	private BaseAdapter adapter;
	private ArrayList<String> list;
	@ViewInject(R.id.parent)
	private View parent;

	@ViewInject(R.id.linear_address)
	private LinearLayout linear_address;
	private int type = 0;// 0待处理订单，1景点门票订单2酒店订单3自由行订单4跟团游订单

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type = getIntent().getIntExtra("type", 0);
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_orderlist, null);
		addCenterView(view);
		ViewUtils.inject(this);
		parent.setOnTouchListener(this);
		initview();
		notifyAdapter();
	}

	private void initview() {
		xlist.setDivider(null);
		xlist.setPullLoadEnable(false);
		xlist.setPullRefreshEnable(true);
		xlist.setXListViewListener(this);
		switch (type) {
		case 0:
			tv_title.setText("待处理订单");
			break;
		case 1:
			tv_title.setText("景点门票订单");
			break;
		case 2:
			tv_title.setText("酒店订单");
			break;
		case 3:
			tv_title.setText("自由行订单");
			break;
		case 4:
			tv_title.setText("跟团游订单");
			break;
		}
	}

	private void notifyAdapter() {
		getlist();
		if (adapter == null) {
			adapter = new OrderAdapter(this, list);
			xlist.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
	}

	private void getlist() {
		if (list == null) {
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
		Intent i = new Intent(this,OrderDetailActivity.class);
		startActivity(i);
	}
}
