package com.qingfengweb.id.biluomiV2;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.adapter.CommentAdapter;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UpdateType;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.PersonInfo;
import com.qingfengweb.model.SystemUpdateInfo;
import com.qingfengweb.util.FileTools;
import com.qingfengweb.util.MD5;

public class CommentListActivity extends BaseActivity {
	private ListView m_listview;
	List<Map<String, Object>> list;
	private Button backBtn;
	private TextView title;
	private int position;// 职位
	private EditText searchEt;
	private Button searchBtn;
	private DBHelper dbHelper = null;
	boolean isEmpty = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_commentlist);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//		position = MyApplication.getInstant().getPosition();
		position = getIntent().getIntExtra("position", 1);
		findView();
		initData();
		new Thread(getListDataRunnable).start();
	}
	Runnable getListDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select *from "+PersonInfo.TableName +" where storeid="+UserBeanInfo.getInstant().getCurrentStoreId()+" and position="+position;
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(0);
			}
			
			//检查系统更新机制表  看该用户的相册是否有更新
			String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
					+"' and type = " + UpdateType.StaffEvaluation.getValue();
			List<Map<String,Object>> systemList = dbHelper.selectRow(sqlTime, null);
			String systemTimeStr = "";//最新更新时间
			String localTimeStr = "";//历史更新数据时间
			if(systemList!=null && systemList.size()>0){
				Map<String,Object> map = systemList.get(0);
				systemTimeStr = map.get("updatetime").toString();
				localTimeStr = (map.get("localtime") == null)?"":map.get("localtime").toString();
				if(!systemTimeStr.equals(localTimeStr) || (systemTimeStr.equals("") && localTimeStr.equals(""))){//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
					String msgStr = RequestServerFromHttp.getValuationList(UserBeanInfo.getInstant().getCurrentStoreId(),position+"", "");//这里直接找到所有点评人好了
					System.out.println(msgStr);
					if(msgStr.startsWith("[")){//获取推荐套系成功
						JsonData.jsonValuationData(msgStr, dbHelper.open(), UserBeanInfo.getInstant().getCurrentStoreId());
						list = dbHelper.selectRow(sql, null);
						if(list!=null && list.size()>0){
							handler.sendEmptyMessage(0);
						}
					}else if(msgStr.equals("404")){//访问服务器失败
						
					}else{//获取推荐套系失败
						
					}
				}
			}
		}
	};
	Runnable getSearchDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select *from "+PersonInfo.TableName +" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()+
					"' and position='"+position +"' and employeeid='"+searchEt.getText().toString()+"'";
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(0);
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				notifyAdapter();
			}else if(msg.what == 1){
				
			}
			super.handleMessage(msg);
		}
		
	};
	private void initData() {
		dbHelper = DBHelper.getInstance(this);
		list = new ArrayList<Map<String, Object>>();
	}
	private void notifyAdapter(){
		CommentAdapter adapter = new CommentAdapter(this, list);
		m_listview.setAdapter(adapter);
		m_listview.setCacheColorHint(0);
		m_listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		m_listview.setOnItemClickListener(new ListItemClickListener());
	}
	class ListItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(!UserBeanInfo.getInstant().isLogined){
				Intent intent = new Intent(CommentListActivity.this,LoginActivity.class);
    			intent.putExtra("activityType", 3);
    			intent.putExtra("map", (Serializable)list.get(position));
        		startActivity(intent);
        		finish();
			}else{
				Intent i = new Intent(CommentListActivity.this, CommentActivity.class);
				i.putExtra("map", (Serializable)list.get(position));
				startActivity(i);
				finish();
			}
		}

	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			Intent intent = new Intent(this,CommentMainActivity.class);
			startActivity(intent);
			finish();
		} else if(v==searchBtn){
			new Thread(getSearchDataRunnable).start();
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(this,CommentMainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void findView() {
		m_listview = (ListView) findViewById(R.id.m_listview);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(this);
		searchEt = (EditText) findViewById(R.id.et);
		searchEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				String searchStr = searchEt.getText().toString().trim();
				if(searchStr!=null && !searchStr.equals("")){
					isEmpty = false;
				}else{
					isEmpty = true;
				}
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(!isEmpty){
					if(searchEt.getText().toString().equals("")){
						new Thread(getListDataRunnable).start();
					}
				}
				
			}
		});
		if(position==1){
			title.setText("摄影师");
			searchEt.setHint("请输入摄影师的工号进行查询");
		}else{
			searchEt.setHint("请输入化妆师的工号进行查询");
			title.setText("化妆师");
		}
		
	}
}