/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.EmployHelpAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoHelpInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author ������
 * @createDate 2013/1/11
 * ʹ�ð����б���
 *
 */
public class EmployHelpActivity extends BaseActivity implements Activity_interface{
	private Button backBtn;
	private ListView helpList;
	private ArrayList<HashMap<String,Object>> list;
	
	
	private SoSoUploadData uploaddata;// �������������
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private ProgressDialog progressdialog;
	
	private EmployHelpAdapter employhelpadapter = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_employhelp);
		findView();
		initView();
		initData();
		new Thread(runnable).start();
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			setResult(RESULT_OK);
			finish();
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(RESULT_OK);
			finish();
		}
		return true;
	}
	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		helpList = (ListView) findViewById(R.id.helpList);
	}
	@Override
	public void initView() {
		backBtn.setOnClickListener(this);
	}
	@Override
	public void initData() {
		getListData();
		notifiView();
	}
	@Override
	public void notifiView() {
		helpList.setCacheColorHint(0);
		helpList.setSelector(new ColorDrawable(Color.TRANSPARENT));
		employhelpadapter = new EmployHelpAdapter(this, list);
		helpList.setAdapter(employhelpadapter);
		helpList.setOnItemClickListener(new ListOnItemClickListener());
	}
	class ListOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(EmployHelpActivity.this,DetailEmployHelpActivity.class);
			intent.putExtra("name", list.get(position).get("name").toString());
			intent.putExtra("content", list.get(position).get("content").toString());
			startActivityForResult(intent, 0);
		}
		
	}
	
	/**
	 * ˢ��adapter
	 * 
	 * ���ߣ�Ring �����ڣ�2013-2-20
	 */
	public void notifyAdapter() {
		getListData();
		if(employhelpadapter!=null){
			employhelpadapter.notifyDataSetChanged();
		}
	}
	
	public void getListData(){
		if (list == null) {
			list = new ArrayList<HashMap<String, Object>>();
		}
		list.clear();
		List<Map<String, Object>> selectresult = null;
		String sql = "select * from soso_helpinfo";
		selectresult = DBHelper.getInstance(this).selectRow(sql, null);
		if (selectresult != null) {
			HashMap<String, Object> map = null;
			for (int i = 0; i < selectresult.size(); i++) {
				if(selectresult.get(i).get("helpid")!=null&&!
						selectresult.get(i).get("helpid").toString().equals("")){
					map = new HashMap<String, Object>();
					if(selectresult.get(i).get("helpid")!=null){
						map.put("name", selectresult.get(i).get("helptitle"));
					}else{
						map.put("name", "");
					}
					if(selectresult.get(i).get("helpid")!=null){
						map.put("content", selectresult.get(i).get("helptext"));
					}else{
						map.put("content", "");
					}
					map.put("helpid", selectresult.get(i).get("helpid"));
					list.add(map);
				}
			}
		}
	}
	
	
	/**
	 * �����ʱ����
	 * 
	 * @author Ring
	 * @since 2013-01-31
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(EmployHelpActivity.this)) {
				boolean b = false;
				handler.sendEmptyMessage(5);// ����������
				b = getHelp();
				handler.sendEmptyMessage(6);// �رս�����
				if(b){
					handler.sendEmptyMessage(1);// ˢ���б�
				}else{
					handler.sendEmptyMessage(3);// ˢ���б�
				}
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
			} else {
				handler.sendEmptyMessage(4);// û������ʱ���û���ʾ

			}
			click_limit = true;
		}
	};
	/**
	 * �����߼�ҵ��
	 * 
	 * @author Ring
	 * @since 2013-01-31
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// ˢ���б�
				notifyAdapter();
				break;
			case 2:// �ӵ�¼������ת��ע�����
				break;
			case 3:// ��¼ʧ�ܸ��û���ʾ
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				} else if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoNoData.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_nodata);
				} else {
					errormsg = getResources().getString(
							R.string.toast_message_failure);
				}
				Toast.makeText(EmployHelpActivity.this, errormsg, Toast.LENGTH_SHORT).show();
				break;
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(EmployHelpActivity.this
						.getParent());
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(
						EmployHelpActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_loading));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						click_limit = true;
						if (uploaddata != null) {
							uploaddata.overReponse();
						}
						return false;
					}
				});
				progressdialog.show();
				break;
			case 6:// �رս�����
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		};
	};

	/**
	 * ��ȡ�û���������
	 * 
	 * ���ߣ�Ring �����ڣ�2013-1-31
	 */
	public boolean getHelp() {
		String updatedate = "1970-01-01 12:00:00";
		String userid = "";
		if((MyApplication.getInstance().getSosouserinfo()==null
				||MyApplication.getInstance().getSosouserinfo().getUserID()==null)){
			userid = "0";
			updatedate ="1970-01-01 12:00:00";
		}else{
			userid = MyApplication.getInstance(this).getSosouserinfo().getUserID();
		}
		if(DBHelper
				.getInstance(this)
				.selectRow(
						"select * from soso_configurationinfo where name='THELPDOCUMENT_TIME' and updatedate = value and value<>'' and userid = '"
				+userid+"'",
						null).size()>0){
			return true;
		}
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='THELPDOCUMENT_TIME' and userid = '"
				+userid+"'",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get("updatedate") != null) {
				updatedate = (selectresult.get(selectresult.size() - 1).get(
						"updatedate").toString());
			}
		}
		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}

		// params ����Ĳ����б�
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("HelpUpdateTime", updatedate));
		uploaddata = new SoSoUploadData(this, "HelpSelect.aspx",
				params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(userid);
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 
	 * 
	 * ���ߣ�Ring �����ڣ�2013-1-31
	 * 
	 */
	private void dealReponse(String userid) {
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='THELPDOCUMENT_TIME' and userid = '"
					+userid+"'");
		}
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='THELPDOCUMENT_TIME' and userid = '"
					+userid+"'");
			Type listType = new TypeToken<LinkedList<SoSoHelpInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoHelpInfo> soSoHelpInfos = null;
			SoSoHelpInfo soSoHelpInfo = null;
			ContentValues values = new ContentValues();
			soSoHelpInfos = gson.fromJson(reponse, listType);
			if (soSoHelpInfos != null && soSoHelpInfos.size() > 0) {
				for (Iterator<SoSoHelpInfo> iterator = soSoHelpInfos
						.iterator(); iterator.hasNext();) {
					soSoHelpInfo = (SoSoHelpInfo) iterator.next();
					if (soSoHelpInfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("soso_helpinfo",
								"helpid = ?", new String[] { soSoHelpInfo
								.getHelpId() });
						continue;
					}
					if (soSoHelpInfo != null
							&& soSoHelpInfo.getHelpId() != null) {
						values.put("helpid", soSoHelpInfo.getHelpId());
						values.put("helptitle", soSoHelpInfo.getHelpTitle());
						values.put("helptext", soSoHelpInfo.getHelpText());
						if (DBHelper
								.getInstance(EmployHelpActivity.this)
								.selectRow(
										"select * from soso_helpinfo where helpid = '"
												+ soSoHelpInfo.getHelpId()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(EmployHelpActivity.this)
									.insert("soso_helpinfo", values);
						} else {
							DBHelper.getInstance(EmployHelpActivity.this)
									.update("soso_helpinfo",
											values,
											"helpid = ?",
											new String[] { soSoHelpInfo
													.getHelpId() });
						}

						values.clear();
					}
				}
				if (soSoHelpInfos != null) {
					soSoHelpInfos.clear();
					soSoHelpInfos = null;
				}
				if (values != null) {
					values.clear();
					values = null;
				}

			}
		}
	}
}
