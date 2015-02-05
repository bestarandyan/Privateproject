/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.AutoCompleteAdapter;
import com.zhihuigu.sosoOffice.Adapter.InputSelectAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.model.KeywordInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.StringUtils;

/**
 * @author 刘星星
 * @createDate 2013/4/18
 * 输入搜素类
 *
 */
public class InputSelectActivity extends BaseActivity implements Activity_interface,OnItemClickListener{
	private Button topBtn;
	private EditText et;
	private ListView listView;
	private LinearLayout searchLayout;
	private boolean exitActivity = true;//判断topBtn的功能是否是退出该界面
	private boolean searchBolean = true;//判断是否进行搜索
	ArrayList<String> mOriginalValues = new ArrayList<String>();// 关键字集合
	private SoSoUploadData uploaddata;// 服务器请求对象
	private String reponse = "";// 从服务器获取响应值
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_inputselect);
		findView();
		initView();
	}
	@Override
	public void findView() {
		topBtn = (Button) findViewById(R.id.topBtn);
		et = (EditText) findViewById(R.id.cityEt);
		listView = (ListView) findViewById(R.id.searchList);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
	}
	/***
	 * 监听文本改变
	 */
	class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			if(searchBolean){
				if(et.getText().toString().toString().length()>0  && et.isFocusable() ){
					if(getIntent().getBooleanExtra("back", false)){
						topBtn.setText("确定");
					}else{
						topBtn.setText("搜索");
					}
					
					exitActivity = false;
					progressBar.setVisibility(View.VISIBLE);
					searchBolean = false;
					new Thread(runnable1).start();
				}else{
					topBtn.setText("取消");
					exitActivity = true;
					mOriginalValues.clear();
					notifiView();
				}
			}
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

	}
	
	/**
	 * 关键字的获取
	 * 
	 * 作者：Ring 创建于：2013-2-23 wherec
	 */
	public boolean getKeyword() {
		// params 请求的参数列表
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance().getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance().getAPPKEY()));
		params.add(new BasicNameValuePair("KeyWord", et.getText().toString()));
		uploaddata = new SoSoUploadData(this, "KeyWordSelect.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse();
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 处理服务器响应值，将关键字封装到list中
	 * 
	 * 作者：Ring 创建于：2013-1-31
	 * 
	 * @param districtid
	 */
	private void dealReponse() {
		mOriginalValues.clear();
		if (StringUtils.CheckReponse(reponse)) {
			Type listType = new TypeToken<LinkedList<KeywordInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<KeywordInfo> keywordInfos = null;
			KeywordInfo keywordInfo = null;
			keywordInfos = gson.fromJson(reponse, listType);
			if (keywordInfos != null && keywordInfos.size() > 0) {
				for (Iterator<KeywordInfo> iterator = keywordInfos.iterator(); iterator
						.hasNext();) {
					keywordInfo = (KeywordInfo) iterator.next();
					if (keywordInfo != null && keywordInfo.getKeyWord() != null) {
						// if(mOriginalValues.size()>20){
						// return;
						// }
						mOriginalValues.add(keywordInfo.getKeyWord());
					}
				}
				if (keywordInfos != null) {
					keywordInfos.clear();
					keywordInfos = null;
				}
			}
		}
	}
	/**
	 * author by Ring 处理用户名唯一性验证耗时操作
	 */
	public Runnable runnable1 = new Runnable() {

		@Override
		public void run() {
			if (uploaddata != null) {
				uploaddata.overReponse();
			}
			getKeyword();
			if(mOriginalValues.size()>0){
				handler.sendEmptyMessage(7);
			}else{
				handler.sendEmptyMessage(1);
			}
			
		}
	};
	/**
	 * 处理逻辑业务
	 * 
	 * @author Ring
	 * @since 2013-01-31
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1://无数据
				progressBar.setVisibility(View.GONE);
				searchBolean = true;
				break;
			case 7://
				notifiView();
				searchBolean = true;
				break;
			}
		}
	};

	@Override
	public void initView() {
		topBtn.setOnClickListener(this);
		et.addTextChangedListener(new MyTextWatcher());
		listView.setOnItemClickListener(this);
		et.setOnClickListener(this);
		searchLayout.setOnClickListener(this);
		if(getIntent().getStringExtra("content")!=null){
			et.setText(getIntent().getStringExtra("content"));
		}
		
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}
	InputSelectAdapter keytAdapter = null;
	@Override
	public void notifiView() {
		 keytAdapter = new InputSelectAdapter(this,
				mOriginalValues);
		listView.setAdapter(keytAdapter);
		listView.setCacheColorHint(0);
		progressBar.setVisibility(View.GONE);
		if(mOriginalValues.size() == 1 && mOriginalValues.get(0).toString().equals(et.getText().toString())){
			mOriginalValues.clear();
			keytAdapter.notifyDataSetChanged();
		}
		if(et.getText().toString().trim().equals("") || et.getText().toString().trim().length()==0){
			mOriginalValues.clear();
			keytAdapter.notifyDataSetChanged();
		}
	}
	@Override
	public void onClick(View v) {
		if(v == topBtn){
			CommonUtils.hideSoftKeyboard(this);
			if(exitActivity){
				finish();
			}else{
				if(getIntent().getBooleanExtra("back", false)){
					MyApplication.getInstance().setCurrentKeyt("");
					Intent intent = new Intent();
					intent.putExtra("keyt", et.getText().toString());
					MyApplication.getInstance().setCurrentKeyt(et.getText().toString());
					setResult(RESULT_OK, intent);
					
					Bundle b = MyApplication.getInstance().getSearchbundle();
					if(b!=null){
						b.putString("keywordstr", et.getText().toString());
					}
					finish();
				}else{
					Intent intent = new Intent(this, SearchRoomsListActivity.class);
					intent.putExtra("whereclause", getWhereClause(et.getText().toString()));
					intent.putExtra("districtid", "");
					intent.putExtra("officetype", "");
					intent.putExtra("keyword", et.getText().toString());
					intent.putExtra("cityid", MyApplication.getInstance().getCityid());
					intent.putExtra("priceup", "20");
					intent.putExtra("pricedown", "0");
					intent.putExtra("areaup", "0");
					intent.putExtra("areadown", "0");
					intent.putExtra("businessname","") ;
					intent.putExtra("longitude","");
					intent.putExtra("latitude","") ;
					startActivity(intent);
					finish();
				}
			}
		}
		super.onClick(v);
	}
	

	/**
	 * 整理的搜索字段字符
	 *
	 * 作者：Ring
	 * 创建于：2013-2-28
	 * @return
	 */
	public String getWhereClause(String keystring){
		String cityid = MyApplication.getInstance().getCityid();
		StringBuffer whereclause = new StringBuffer("");
		whereclause.append(" ischecked=1 and isrent=0 and");
		whereclause.append(" BuildID in ( select BuildID from TBuild where");
		if(cityid!=null&&!cityid.trim().equals("")){
			whereclause.append(" CityID='"+cityid+"') and");
		}
		if (keystring != null && !keystring.trim().equals("")) {
			whereclause.append(" ((Keywords like '%" + keystring + "%' or");
			whereclause.append(" FYJJ like '%" + keystring + "%') or");
			whereclause.append(" BuildID in ( select BuildID from TBuild where");
			whereclause.append(" (BuildMC like '%" + keystring + "%' or");
			whereclause.append(" Address like '%" + keystring + "%')) and");
		}
		String whereString = "1=1";
		if(whereclause!=null&&whereclause.length()>3){
			whereString=whereclause.subSequence(0, whereclause.length()-3).toString()+")";
		}
		return whereString;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		searchBolean = false;
		CommonUtils.hideSoftKeyboard(this);
		if(getIntent().getBooleanExtra("back", false)){
			Intent intent = new Intent();
			intent.putExtra("keyt", mOriginalValues.get(position).toString());
			MyApplication.getInstance().setCurrentKeyt(mOriginalValues.get(position).toString());
			setResult(RESULT_OK, intent);
			Bundle b = MyApplication.getInstance().getSearchbundle();
			if(b!=null){
				b.putString("keywordstr", mOriginalValues.get(position).toString());
			}
			finish();
			return;
		}
		if(mOriginalValues!=null&&mOriginalValues.size()>0&&position<mOriginalValues.size()){
			
		}else{
			return;
		}
		if(!et.getText().toString().equals(mOriginalValues.get(position).toString())){
			et.setText(mOriginalValues.get(position).toString());
		}
		Intent intent = new Intent(this, SearchRoomsListActivity.class);
		intent.putExtra("whereclause", getWhereClause(mOriginalValues.get(position).toString()));
		intent.putExtra("districtid", "");
		intent.putExtra("officetype", "");
		intent.putExtra("cityid", MyApplication.getInstance().getCityid());
		intent.putExtra("priceup", "20");
		intent.putExtra("pricedown", "0");
		intent.putExtra("areaup", "0");
		intent.putExtra("areadown", "0");
		intent.putExtra("businessname","") ;
		intent.putExtra("longitude","");
		intent.putExtra("latitude","") ;
		startActivity(intent);
		finish();
//		et.clearFocus();
//		et.setFocusable(false);
	}
}
