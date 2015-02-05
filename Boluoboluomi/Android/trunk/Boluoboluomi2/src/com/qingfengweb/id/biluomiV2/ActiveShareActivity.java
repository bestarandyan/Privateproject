/**
 * 
 */
package com.qingfengweb.id.biluomiV2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.qingfengweb.adapter.ActiveShareAdapter;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UpdateType;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.ActiveListInfo;
import com.qingfengweb.model.SystemUpdateInfo;
import com.qingfengweb.util.MessageBox;

/**
 * @author 刘星星
 *
 */
public class ActiveShareActivity extends BaseActivity implements OnItemClickListener{
	private ListView listView;
	private List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	DBHelper dbHelper = null;
	public String currentStoreId = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_activeshare);
		findViewById(R.id.backBtn).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.activeShareList);
		listView.setOnItemClickListener(this);
		dbHelper = DBHelper.getInstance(this);
		currentStoreId = UserBeanInfo.getInstant().getCurrentStoreId();
		new Thread(getListDataRunnable).start();
	}
	/**
	 * 获取活动分享列表数据
	 */
	Runnable getListDataRunnable = new Runnable() {
		@Override
		public void run() {
			String sql = "select *from "+ActiveListInfo.TableName +" where storeid="+currentStoreId;
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(0);
			}
			
			//检查系统更新机制表  看该用户的相册是否有更新
			String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+currentStoreId
					+"' and type = " + UpdateType.Activity.getValue();
			List<Map<String,Object>> systemList = dbHelper.selectRow(sqlTime, null);
			String systemTimeStr = "";//最新更新时间
			String localTimeStr = "";//历史更新数据时间
			if(systemList!=null && systemList.size()>0){
				Map<String,Object> map = systemList.get(0);
				systemTimeStr = map.get("updatetime").toString();
				localTimeStr = (map.get("localtime") == null)?"":map.get("localtime").toString();
				if(!systemTimeStr.equals(localTimeStr) || (systemTimeStr.equals("") && localTimeStr.equals(""))){//如果系统最新更新时间和历史更新时间不相同 则代表有更新  则向服务器获取更新的数据
					String msgStr = RequestServerFromHttp.getActiveData(currentStoreId, "");//想服务器获取最新数据
					if(msgStr.startsWith("[")){//成功
						JsonData.jsonActive(msgStr, dbHelper.open(), currentStoreId);
						list = dbHelper.selectRow(sql, null);
						if(list!=null && list.size()>0){
							ContentValues values = new ContentValues();
							values.put("localtime", systemTimeStr);
							dbHelper.update(SystemUpdateInfo.TableName, values, "storeid=? and type =?", new String[]{currentStoreId,UpdateType.Activity.getValue()});
							handler.sendEmptyMessage(0);
						}
					}else if(msgStr.equals("404")){//访问服务器失败
						handler.sendEmptyMessage(1);
					}else {//更新失败
						handler.sendEmptyMessage(2);
					}
				}
			}
			
		}
	};
	/**
	 * UI处理
	 */
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//刷新列表布局
				notifyLV();
			}else if(msg.what == 1){//访问服务器失败
				
			} else if(msg.what == 2){//更新失败
				
			}
			super.handleMessage(msg);
		}
		
	};
	private void notifyLV(){
		ActiveShareAdapter adatper = new ActiveShareAdapter(this, list);
		listView.setAdapter(adatper);
		listView.setCacheColorHint(0);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
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
		Intent intent = new Intent(this,DetailActiveActivity.class);
		intent.putExtra("activeid", list.get(arg2).get("id").toString());
		intent.putExtra("title", list.get(arg2).get("title").toString());
		startActivity(intent);
	}
}
