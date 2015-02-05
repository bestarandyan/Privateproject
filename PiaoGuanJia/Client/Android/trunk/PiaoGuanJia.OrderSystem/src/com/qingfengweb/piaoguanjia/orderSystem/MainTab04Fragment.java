package com.qingfengweb.piaoguanjia.orderSystem;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingfengweb.piaoguanjia.orderSystem.adapter.ProduceAdapter;
import com.qingfengweb.piaoguanjia.orderSystem.view.XListView;
import com.qingfengweb.piaoguanjia.orderSystem.view.XListView.IXListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class MainTab04Fragment extends Fragment implements IXListViewListener {
	@ViewInject(R.id.xlist)
	private XListView xlist;
	private BaseAdapter adapter;
	private ArrayList<String> list;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main_list1, container, false);
		ViewUtils.inject(this, view);
		initview();
		notifyAdapter();
		return view;
	}
	private void initview() {
		xlist.setDivider(null);
		xlist.setPullLoadEnable(true);
		xlist.setPullRefreshEnable(true);
		xlist.setXListViewListener(this);
	}
	private void notifyAdapter() {
		getlist();
		if(adapter==null){
			adapter = new ProduceAdapter(getActivity(), list);
		}
		xlist.setAdapter(adapter);
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

}
