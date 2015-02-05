package com.qingfengweb.piaoguanjia.orderSystem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingfengweb.piaoguanjia.orderSystem.adapter.ContactAdapter;
import com.qingfengweb.piaoguanjia.orderSystem.model.PlayerInfo;
import com.qingfengweb.piaoguanjia.orderSystem.request.PlayerServlet;
import com.qingfengweb.piaoguanjia.orderSystem.view.XListView;
import com.qingfengweb.piaoguanjia.orderSystem.view.XListView.IXListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class ContactActivity extends MainFrameActivity implements
		IXListViewListener {

	@ViewInject(R.id.parent)
	private View parent;

	@ViewInject(R.id.xlist)
	private XListView xlist;
	private BaseAdapter adapter;
	private List<PlayerInfo> list;

	@ViewInject(R.id.linear_btn1)
	private LinearLayout linear_btn1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_contact, null);
		addCenterView(view);
		ViewUtils.inject(this);
		parent.setOnTouchListener(this);
		initview();
		notifyAdapter();

		new Thread(runnableList).start();
	}

	private void notifyAdapter() {
		getlist();
		if (adapter == null) {
			adapter = new ContactAdapter(this, list);
			xlist.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
	}

	private void getlist() {
		if(list==null){
			list = new ArrayList<PlayerInfo>();
		}
		list.clear();
		try {
			list = MyApplication.dbuser.findAll(Selector.from(PlayerInfo.class));
		} catch (DbException e1) {
			e1.printStackTrace();
		}
	}

	@OnClick(R.id.linear_btn1)
	public void btnOnclick(View v) {
		Intent i = new Intent(this, AddContactActivity.class);
		startActivity(i);
	}

	private void initview() {
		tv_title.setText("我的游玩人");
		xlist.setDivider(null);
		xlist.setPullLoadEnable(true);
		xlist.setPullRefreshEnable(true);
		xlist.setXListViewListener(this);
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

	/**
	 * 获取列表
	 */

	public Runnable runnableList = new Runnable() {

		@Override
		public void run() {
			PROGRESSMSG = "请稍等...";
			handler.sendEmptyMessage(PROGRESSSTART_HANDLER);
			String response = PlayerServlet.actionPlayerList(2, 20, "");
			handler.sendEmptyMessage(PROGRESSEND_HANDLER);
			if (checkResponse(response)) {
//				LinkedList<PlayerInfo> playerinfos = JsonUtils
//						.jsonObjectList(PlayerInfo.class,response);
				Type listType = new TypeToken<LinkedList<PlayerInfo>>() {
				}.getType();
				Gson gson = new Gson();
				LinkedList<PlayerInfo> objectarray = null;
				objectarray = gson.fromJson(response, listType);
				PlayerInfo playerinfo = null;
				for (Iterator<PlayerInfo> iterator = objectarray.iterator(); iterator
						.hasNext();) {
					playerinfo = (PlayerInfo) iterator.next();
					try {
						MyApplication.dbuser.saveOrUpdate(playerinfo);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
}
