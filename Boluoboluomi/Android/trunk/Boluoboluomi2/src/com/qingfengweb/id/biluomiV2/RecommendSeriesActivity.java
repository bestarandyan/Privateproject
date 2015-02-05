/**
 * 
 */
package com.qingfengweb.id.biluomiV2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.qingfengweb.adapter.RecommendSeriesAdapter;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UpdateType;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.RecommendInfo;
import com.qingfengweb.model.SystemUpdateInfo;

/**
 * @author 刘星星 武国庆
 * @createDate 2013/8/26
 * 推荐套系类
 *
 */
public class RecommendSeriesActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	private ListView listView = null;
	private DBHelper dbHelper = null;
	private LinearLayout refreshLayout;
	private TextView refreshDataTv;
	private TextView noDataTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_recommendseries);
		listView = (ListView) findViewById(R.id.recommendseriesList);
		listView.setOnItemClickListener(this);
		refreshDataTv = (TextView) findViewById(R.id.refreshDataTv);
		refreshLayout = (LinearLayout) findViewById(R.id.refreshLayout);
		findViewById(R.id.backBtn).setOnClickListener(this);
		noDataTv = (TextView) findViewById(R.id.noDataTv);
		noDataTv.setOnClickListener(this);
		dbHelper = DBHelper.getInstance(this);
		new Thread(getListDataRunnable).start();
	}
	Runnable getListDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select *from "+RecommendInfo.TableName+" where storeid="+UserBeanInfo.getInstant().getCurrentStoreId();
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(0);
			}
//			//检查系统更新机制表  看该用户的相册是否有更新
//			String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
//					+"' and type = " + UpdateType.RecommendProduct.getValue();
//			List<Map<String,Object>> systemList = dbHelper.selectRow(sqlTime, null);
//			String systemTimeStr = "";//最新更新时间
//			String localTimeStr = "";//历史更新数据时间
//			if(systemList!=null && systemList.size()>0){
//				Map<String,Object> map = systemList.get(0);
//				systemTimeStr = map.get("updatetime").toString();
//				localTimeStr = (map.get("localtime") == null)?"":map.get("localtime").toString();
//				if(!systemTimeStr.equals(localTimeStr) || (systemTimeStr.equals("") && localTimeStr.equals(""))){//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
					String msgStr = RequestServerFromHttp.getRecommendList(UserBeanInfo.getInstant().getCurrentStoreId(), "");
					if(msgStr.startsWith("[")){//获取推荐套系成功
						JsonData.jsonRecommendSeries(msgStr, dbHelper.open(), UserBeanInfo.getInstant().getCurrentStoreId());
						list = dbHelper.selectRow(sql, null);
						if(list!=null && list.size()>0){
							handler.sendEmptyMessage(0);
						}
					}else if(msgStr.equals("404")){//访问服务器失败
						
					}else if(JsonData.isNoData(msgStr)){//访问服务器失败
						handler.sendEmptyMessage(1);
					}else{//获取推荐套系失败
						
					}
//				}
//			}
			
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				notifyListView();
				refreshLayout.setVisibility(View.GONE);
				noDataTv.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
			}else if(msg.what == 1){
				refreshLayout.setVisibility(View.GONE);
				noDataTv.setVisibility(View.VISIBLE);
				noDataTv.setText("暂无数据！");
				listView.setVisibility(View.GONE);
			}
			super.handleMessage(msg);
		}
		
	};
	private void notifyListView(){
		RecommendSeriesAdapter adapter = new RecommendSeriesAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setCacheColorHint(0);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}else if(v == noDataTv){
			noDataTv.setText("正在刷新数据...");
			new Thread(getListDataRunnable).start();
		}
			
		super.onClick(v);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(RecommendSeriesActivity.this,DetailSeriesActivity.class);
		intent.putExtra("seriesMap", (Serializable)list.get(arg2));
		startActivity(intent);
	}

}
