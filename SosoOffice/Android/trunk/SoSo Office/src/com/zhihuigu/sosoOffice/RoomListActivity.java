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
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.View.XListView;
import com.zhihuigu.sosoOffice.View.XListView.IXListViewListener;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoBuildInfo;
import com.zhihuigu.sosoOffice.model.SoSoBuildUserInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.ProgressDialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * @author ������
 * @createDate 2013/1/18
 * ��Դ�б����
 */
public class RoomListActivity extends BaseActivity implements Activity_interface,IXListViewListener{
	private Button backBtn,addRoomBtn;
	private XListView roomListView;
	private SimpleAdapter sa = null;//�б��������
	private ArrayList<HashMap<String, Object>>  roomList;
	
	private SoSoUploadData uploaddata;//�������������
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private ProgressDialog progressdialog;
	
	private LinearLayout noDataLayout;//û������ʱ��ʾ�Ĳ���
	private ImageView noDataImg;//û������ʱ��ͼƬ���ˢ��
	private TextView noDataText;//û������ʱ����ʾ����
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_roomlist);
		findView();
		initView();
		initData();
		MyApplication.getInstance().setRoomListBack(false);
		if(getIntent().getIntExtra("tag", 0)==1){
			new Thread(runnable).start();
		}
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			Intent intent = new Intent(this, MainActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Window w = MainFirstTab.group.getLocalActivityManager()
					.startActivity("activityid",intent);
		    View view = w.getDecorView();
		    //��View��Ӵ�ActivityGroup��
		    MainFirstTab.group.setContentView(view);
		} else if (v == addRoomBtn) {//��ӷ�Դ
			MyApplication.getInstance().setAddRoomFlag(0);
			Intent intent = new Intent(this, ReleaseNewRoomActivity.class);
			intent.putExtra("activityFlag", 0);
			startActivity(intent);
		}else if(v == noDataImg){//û������ʱ���ͼƬˢ��
			new Thread(runnable).start();
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(this, MainActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			MainTabActivity.mTabHost.setCurrentTab(0);
			Window w = MainFirstTab.group.getLocalActivityManager()
					.startActivity("MainActivity",intent);
		    View view = w.getDecorView();
		    MainFirstTab.group.setContentView(view);
		}
		return false;
	}
	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		roomListView = (XListView) findViewById(R.id.roomList);
		addRoomBtn = (Button) findViewById(R.id.addRoomBtn);
		noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		noDataText = (TextView) findViewById(R.id.noDataMsg);
	}
	@Override
	public void initView() {
		roomListView.setPullLoadEnable(false);
		roomListView.setPullRefreshEnable(true);
		roomListView.setXListViewListener(this);
		backBtn.setOnClickListener(this);
		addRoomBtn.setOnClickListener(this);
	}
	@Override
	public void initData() {
//		roomList = new ArrayList<HashMap<String,Object>>();
//		HashMap<String,Object> map = new HashMap<String, Object>();
//		map.put("name", "������ҵ�칫����");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "�̵ش���");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "���޳�");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "����������");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "���ش���");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "��ï����");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "����������Ĵ���");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "���Ǵ���");
//		roomList.add(map);
//		notifiView();
	}
	
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		notifiView();
		super.onResume();
	}
	
	@Override
	public void notifiView() {
		getUserBuildsList();
		sa = new SimpleAdapter(this, roomList, R.layout.item_city_list,
				new String[]{"name"}, new int[]{R.id.cityItemText});
		roomListView.setAdapter(sa);
		roomListView.setOnItemClickListener(new listItemClickListener());
		roomListView.setCacheColorHint(0);
		roomListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(roomList.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
			noDataImg.setOnClickListener(this);
		}else{
			noDataLayout.setVisibility(View.GONE);
			noDataImg.setOnClickListener(null);
		}
	}
	/**
	 * ˢ��listView�б�����
	 *
	 * ���ߣ�Ring
	 * �����ڣ�2013-1-31
	 */
	public void notifyAdapter(){
		getUserBuildsList();
		if(sa!=null){
			sa.notifyDataSetChanged();
		}
	}
	
	/**
	 * �����û�id��ȡ¥���б�
	 *
	 * ���ߣ�Ring
	 * �����ڣ�2013-1-31
	 */
	
	public void getUserBuildsList(){
		//�������е�������գ����»�ȡ����
		if(roomList==null){
			roomList = new ArrayList<HashMap<String,Object>>();
		}
		roomList.clear();
		SoSoBuildUserInfo soSoBuildUserInfo = new SoSoBuildUserInfo();
		HashMap<String, Object> map = null;
		String sql = null;
		if(MyApplication.getInstance(this).getSosouserinfo()!=null){
			sql = "select distinct buildid,address,userid,buildmc,buildtype from soso_builduserinfo where userid='" + MyApplication.getInstance(this).getSosouserinfo().getUserID()
				+ "'";
		}else{
			sql = "select distinct buildid,address,userid,buildmc, buildtype from soso_builduserinfo";
		}
		LinkedList<SoSoBuildUserInfo> soSoBuildUserInfos = soSoBuildUserInfo.SelectBuildInfo(this, sql);
		if(soSoBuildUserInfos!=null){
			for (Iterator<SoSoBuildUserInfo> iterator = soSoBuildUserInfos
					.iterator(); iterator.hasNext();) {
				soSoBuildUserInfo = (SoSoBuildUserInfo) iterator.next();
				if(soSoBuildUserInfo.getBuildID()!=null
						&&!soSoBuildUserInfo.getBuildID().equals("")
						&&soSoBuildUserInfo.getBuildMC()!=null
						&&!soSoBuildUserInfo.getBuildMC().equals("")){
					map = new HashMap<String, Object>();
					map.put("id", soSoBuildUserInfo.getBuildID());
					map.put("name", soSoBuildUserInfo.getBuildMC());
					roomList.add(map);
				}
			}
		}
		
	}
	
	/**
	 * ��Դ�б�ĵ���¼�����
	 * @author ������
	 * @createDate 2013/1/18
	 *
	 */
	class listItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(RoomListActivity.this,RoomManagerActivity.class);
			intent.putExtra("tag", 1);
			MyApplication.getInstance().setBuildid(roomList.get(arg2-1).get("id").toString());
			MyApplication.getInstance().setRoomBack(true);
			MyApplication.getInstance().isRoomBack();
			startActivity(intent);
		}
		
	}
	

	/**
	 * �����ʱ����
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
			if (NetworkCheck.IsHaveInternet(RoomListActivity.this)) {
				boolean b = false;
				handler.sendEmptyMessage(5);//����������
				b=getUserBuilds();
				handler.sendEmptyMessage(6);// �رս�����
				if(b){
					handler.sendEmptyMessage(1);//ˢ���б�
				}else{
					handler.sendEmptyMessage(3);//ˢ���б�
				}
				
				if(runnable_tag){
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
	 * @author Ring
	 * @since 2013-01-31
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// ˢ���б�
				notifiView();
				roomListView.stopRefresh();
				break;
			case 2:// �ӵ�¼������ת��ע�����
				break;
			case 3:// ��¼ʧ�ܸ��û���ʾ
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				} else if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoNoData.getValue())) {
					errormsg = "�������κη�Դ���ݣ��������ϽǵļӺţ��Ͻ��������ķ�Դ��";
				} else {
					errormsg = getResources().getString(
							R.string.toast_message_failure);
				}
				roomListView.stopRefresh();
				noDataText.setText(errormsg);
//				Toast.makeText(RoomListActivity.this.getParent(), errormsg, Toast.LENGTH_SHORT).show();
				break;
			case 4:// û������ʱ���û���ʾ
				roomListView.stopRefresh();
				MessageBox.CreateAlertDialog(RoomListActivity.this.getParent());
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(RoomListActivity.this.getParent());
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
	 * ��ȡ�û�����¥��
	 *
	 * ���ߣ�Ring
	 * �����ڣ�2013-1-31
	 */
	public boolean getUserBuilds() {
		if(MyApplication.getInstance(this).getSosouserinfo(this)==null
				||MyApplication.getInstance(this).getSosouserinfo(this).getUserID()==null){
			return false;
		}
		// params ����Ĳ����б�
//		if(DBHelper
//				.getInstance(this)
//				.selectRow(
//						"select * from soso_configurationinfo where name='TBUILD_TIME' and updatedate = value and value<>'' and userid = '"
//				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
//						null).size()>0){
//			return true;
//		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='TBUILD_TIME' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
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

		//params ����Ĳ����б�
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UpdateDate", ""));
		params.add(new BasicNameValuePair("UserID", MyApplication.getInstance(this).getSosouserinfo().getUserID()));
		uploaddata = new SoSoUploadData(this, "BuildSelectByUserId.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(MyApplication.getInstance(this).getSosouserinfo().getUserID());
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
	 * ���ߣ�Ring
	 * �����ڣ�2013-1-31
	 * @param districtid
	 */
	private void dealReponse(String userid) {
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TBUILD_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
		}
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TBUILD_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
			Type listType = new TypeToken<LinkedList<SoSoBuildUserInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoBuildUserInfo> soSoBuildUserInfos = null;
			SoSoBuildUserInfo soSoBuildUserInfo = new SoSoBuildUserInfo();
			soSoBuildUserInfos = gson.fromJson(reponse, listType);
			if (soSoBuildUserInfos != null && soSoBuildUserInfos.size() > 0) {
				soSoBuildUserInfo.InsertBuildInfos(this, soSoBuildUserInfos,userid);
			}
			if (soSoBuildUserInfos != null) {
				soSoBuildUserInfos.clear();
				soSoBuildUserInfos = null;
			}
			
			Type listType1 = new TypeToken<LinkedList<SoSoBuildInfo>>() {
			}.getType();
			LinkedList<SoSoBuildInfo> soSoBuildInfos = null;
			SoSoBuildInfo soSoBuildInfo = new SoSoBuildInfo();
			soSoBuildInfos = gson.fromJson(reponse, listType1);
			if (soSoBuildInfos != null && soSoBuildInfos.size() > 0) {
				soSoBuildInfo.InsertBuildInfos(this, soSoBuildInfos);
			}
			if (soSoBuildInfos != null) {
				soSoBuildInfos.clear();
				soSoBuildInfos = null;
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='TBUILD_TIME' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
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
		roomListView.setRefreshTime(updatedate);
		new Thread(runnable).start();
	}
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onLoadMore()
	 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
}
