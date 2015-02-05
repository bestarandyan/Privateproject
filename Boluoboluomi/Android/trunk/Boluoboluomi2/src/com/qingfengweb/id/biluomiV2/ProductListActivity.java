/**
 * 
 */
package com.qingfengweb.id.biluomiV2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qingfengweb.adapter.ProductListAdapter;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.TreasureProductInfo;

/**
 * @author 刘星星 武国庆
 * @createDate 2013/8/27
 * 产品列表界面
 *
 */
public class ProductListActivity extends BaseActivity{
	private ListView listView;
	private List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	private DBHelper dbHelper = null;
	private String providerid = "";//商家编号
	private String typeid = "";//类型编号
	private TextView refreshDataTv;
	private LinearLayout refreshLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_productlist);
		findViewById(R.id.backBtn).setOnClickListener(this);
		refreshDataTv = (TextView) findViewById(R.id.refreshDataTv);
		refreshLayout = (LinearLayout) findViewById(R.id.refreshLayout);
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		dbHelper = DBHelper.getInstance(this);
		providerid = getIntent().getStringExtra("providerid");
		typeid = getIntent().getStringExtra("typeid");
		new Thread(getMerchanListRunnable).start();
	}
	/**
	 * 获取商家列表数据线程
	 * @createDate 2013、9、16
	 * @author 刘星星
	 */
	Runnable getMerchanListRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select *from "+TreasureProductInfo.TableName +" where typeid='"+typeid+"' and providerid='"+providerid+"'";
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(0);
			}
			String msgStr = RequestServerFromHttp.getMerchanProductList(UserBeanInfo.getInstant().getCurrentStoreId(), providerid, "");
			if(msgStr.startsWith("[") && msgStr.length()>3){
				JsonData.jsonMerchanProductListData(msgStr, dbHelper.open(),UserBeanInfo.getInstant().getCurrentStoreId() );
				list = dbHelper.selectRow(sql, null);
				if(list!=null && list.size()>0){
					handler.sendEmptyMessage(0);
				}
			}else if(msgStr.equals("404")){
				handler.sendEmptyMessage(1);
			}else if(msgStr.length()<5){
				handler.sendEmptyMessage(2);
			}else if(JsonData.isNoData(msgStr)){
				handler.sendEmptyMessage(3);
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				notifyApdater();
			}else if(msg.what == 1){//访问服务器失败
				refreshLayout.setVisibility(View.GONE);
			}else if(msg.what == 2){//获取数据失败或者没有数据
				refreshLayout.setVisibility(View.GONE);
			}else if(msg.what == 3){//没有数据
				refreshLayout.setVisibility(View.GONE);
			}
			super.handleMessage(msg);
		}
		
	};
	private void notifyApdater(){
		refreshLayout.setVisibility(View.GONE);
		ProductListAdapter adapter = new ProductListAdapter(this, list);
		listView.setAdapter(adapter);
		
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}
		super.onClick(v);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		super.onItemClick(arg0, arg1, arg2, arg3);
	}
	@Override
	protected void onResume() {
		if(list!=null && list.size()>0){
			handler.sendEmptyMessage(0);
		}
		super.onResume();
	}
}
