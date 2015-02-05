package com.qingfengweb.piaoguanjia.orderSystem;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.qingfengweb.piaoguanjia.orderSystem.adapter.AdsAdapter;
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

public class AdsListActivity extends MainFrameActivity implements
		IXListViewListener {

	@ViewInject(R.id.xlist)
	private XListView xlist;
	private BaseAdapter adapter;
	private ArrayList<String> list;
	@ViewInject(R.id.parent)
	private View parent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_adslist, null);
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
		tv_title.setText("票管家公告");
	}

	private void notifyAdapter() {
		getlist();
		if (adapter == null) {
			adapter = new AdsAdapter(this, list);
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
		Intent i = new Intent(this,AdsdetailActivity.class);
		startActivity(i);
	}
}
