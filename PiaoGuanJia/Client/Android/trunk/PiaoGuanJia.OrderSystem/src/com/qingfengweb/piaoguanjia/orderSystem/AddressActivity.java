package com.qingfengweb.piaoguanjia.orderSystem;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.qingfengweb.piaoguanjia.orderSystem.adapter.OptionAdapter;
import com.qingfengweb.piaoguanjia.orderSystem.request.SimpleServlet;
import com.qingfengweb.piaoguanjia.orderSystem.util.MessageBox;
import com.qingfengweb.piaoguanjia.orderSystem.util.RegionHelp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class AddressActivity extends MainFrameActivity {
	@ViewInject(R.id.parent)
	private View parent;
	private OptionAdapter optionAdapter1;
	private OptionAdapter optionAdapter2;
	private ArrayList<HashMap<String, Object>> list_province = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> list_city = new ArrayList<HashMap<String, Object>>();

	@ViewInject(R.id.listview1)
	private ListView listview1;
	@ViewInject(R.id.listview2)
	private ListView listview2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_address, null);
		addCenterView(view);
		ViewUtils.inject(this);
		parent.setOnTouchListener(this);

		initView();
	}

	private void initView() {
		// 设置自定义Adapter
		RegionHelp.initData(this, "022___", list_province);
		optionAdapter1 = new OptionAdapter(this, list_province, listview1,
				handler);
		optionAdapter1.setSelectedPosition(0);
		listview1.setAdapter(optionAdapter1);
		tv_title.setText("选择城市");
		try {
			String province = list_province.get(0).get("layer").toString();
			RegionHelp.initData(this, province + "___", list_city);
			// 设置自定义Adapter
			optionAdapter2 = new OptionAdapter(this, list_city, listview2,
					handler);
			optionAdapter2.setSelectedPosition(0);
			listview2.setAdapter(optionAdapter2);
		} catch (Exception e) {
		}
	}

	/**
	 * 页面逻辑处理
	 */
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 5:// 省被选中
					try {
						String province = list_province
								.get(msg.getData().getInt("id")).get("layer")
								.toString();
						RegionHelp.initData(AddressActivity.this, province
								+ "___", list_city);
						optionAdapter2.setSelectedPosition(0);
						optionAdapter2.notifyDataSetInvalidated();
					} catch (Exception e) {
						list_city.clear();
						optionAdapter2.notifyDataSetChanged();
					}

					break;
				case 6:// 市被选中
					try {
						String area = list_city.get(msg.getData().getInt("id"))
								.get("layer").toString();
					} catch (Exception e) {
					}
					break;
				}
			} catch (Exception e) {
			}
		}
	};
}
