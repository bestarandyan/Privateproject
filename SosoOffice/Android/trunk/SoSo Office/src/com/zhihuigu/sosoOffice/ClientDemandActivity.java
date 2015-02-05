/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.DemandManagerAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.View.XListView;
import com.zhihuigu.sosoOffice.View.XListView.IXListViewListener;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoCusReleaseInfo;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * @author ������
 * @createDate 2013/4/9
 * ҵ���Ŀͻ�����ҳ
 *
 */
public class ClientDemandActivity extends BaseActivity implements Activity_interface,OnItemClickListener,IXListViewListener{
	private Button backBtn;//���ذ�ť   �������ť
	private XListView listView;//�����б�ؼ�
	private ArrayList<HashMap<String,Object>> listData;//װ���б�����ݵļ���
	private DemandManagerAdapter adapter;
	private LinearLayout noDataLayout;//û������ʱ��ʾ�Ĳ���
	private ImageView noDataImg;//û������ʱ��ͼƬ���ˢ��
	private TextView noDataText;//û������ʱ����ʾ����
	private SoSoUploadData uploaddata;// �������������
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private ProgressDialog progressdialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_clientdemand);
		findView();
		initView() ;
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(backBtn.getVisibility() == View.VISIBLE){
				MyApplication.getInstance().setClientDemandBackBtnVisiblity(false);
				MainTabActivity.mTabHost.setCurrentTab(0);
			}else{
				showExitDialog();
			}
		}
		return true;
	}
	@Override
	protected void onResume() {
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			super.onResume();
			return;
		}
		initData();
		new Thread(runnable).start();
		DBHelper.getInstance(this).execSql(
				"update soso_countinfo set rcount=0 where userid = '"
						+ MyApplication.getInstance().getSosouserinfo()
								.getUserID() + "'");
		MyApplication.getInstance().setRcount(0);
		if (MyApplication.getInstance().isClientDemandBackBtnVisiblity()) {
			backBtn.setVisibility(View.VISIBLE);
		} else {
			backBtn.setVisibility(View.GONE);
		}
		switch (MyApplication.getInstance().getRoleid()) {
		case Constant.TYPE_AGENCY:
			// MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget.getChildAt(1),MyApplication.getInstance().getLettercount());
			// setButtonNumber(mTabWidget.getChildAt(2),MyApplication.getInstance().getPushcount());
			break;
		case Constant.TYPE_CLIENT:
			// setButtonNumber(mTabWidget.getChildAt(1),MyApplication.getInstance().getLettercount());
			// // setButtonNumber(mTabWidget.getChildAt(2),number);
			break;
		case Constant.TYPE_PROPRIETOR:
			// setButtonNumber(mTabWidget.getChildAt(1),MyApplication.getInstance().getLettercount());
			// MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget.getChildAt(2),
			// MyApplication.getInstance().getPushcount());
			MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget
					.getChildAt(3), MyApplication.getInstance().getRcount());
			break;
		}
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			MyApplication.getInstance().setClientDemandBackBtnVisiblity(false);
			MainTabActivity.mTabHost.setCurrentTab(0);
		}else if(v == noDataImg){//û������ʱ���ͼƬˢ��
			new Thread(runnable).start();
		}
		
	}
	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		listView = (XListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		noDataText = (TextView) findViewById(R.id.noDataMsg);
		noDataImg.setOnClickListener(this);
		
	}
	@Override
	public void initView() {
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		backBtn.setOnClickListener(this);
		
	}
	@Override
	public void initData() {
		listData = new ArrayList<HashMap<String,Object>>();
		getListData();
		notifiView();
		
	}
	public void getListData(){
//		if(listData!=null){
//		listData.clear();
//		for(int i=0;i<20;i++){
//			HashMap<String,Object> map = new HashMap<String, Object>();
//			map.put("name", "�������");
//			map.put("address","����·8��");
//			map.put("money", "��3.5-5.0");
//			map.put("acreage", "1000ƽ��");
//			listData.add(map);
//		}
//	}
	if (listData == null) {
		listData = new ArrayList<HashMap<String, Object>>();
	}
	listData.clear();
//	"releaseid text,"+//����id
//	"userid text,"+//������id
//	"username integer,"+//�������û���
//	"title integer,"+//�������
//	"tele text,"+//�����˵ĵ绰
//	"contact text,"+//��������ϵ��
//	"areaup integer,"+//�������
//	"areadown text,"+//�������
//	"priceup text,"+//�۸�����
//	"pricedown text,"+//�۸�����
//	"description text,"+//��������
//	"officetype integer,"+//��Դ����
//	"unit text"+//��λ
	List<Map<String, Object>> selectresult = null;
	String sql = "select * from soso_cusreleaseinfo where userid = '"
			+
			MyApplication.getInstance(this).getSosouserinfo(this)
					.getUserID() 
					+ "' order by releaseid desc";
	selectresult = DBHelper.getInstance(this).selectRow(sql, null);
	if (selectresult != null) {
		HashMap<String, Object> map = null;
		for (int i = 0; i < selectresult.size(); i++) {
			if(selectresult.get(i).get("releaseid")!=null&&!
					selectresult.get(i).get("releaseid").toString().equals("")){
				map = new HashMap<String, Object>();
				map.put("releaseid", selectresult.get(i).get("releaseid")
						.toString());
				if (selectresult.get(i).get("title") != null) {
					map.put("name", selectresult.get(i).get("title")
							.toString());
				} else {
					map.put("name", "");
				}
				if (selectresult.get(i).get("userid") != null) {
					map.put("userid", selectresult.get(i).get("userid").toString());
				} else {
					map.put("userid", "");
				}
				if (selectresult.get(i).get("username") != null) {
					map.put("username", selectresult.get(i).get("username")
							.toString());
				} else {
					map.put("username", "");
				}
				if (selectresult.get(i).get("buildid") != null) {
					map.put("buildid", selectresult.get(i).get("buildid")
							.toString());
				} else {
					map.put("buildid", "");
				}
				if (selectresult.get(i).get("contact") != null) {
					map.put("contact",
							selectresult.get(i).get("contact").toString());
				} else {
					map.put("contact", "");
				}
				if (selectresult.get(i).get("description") != null) {
					map.put("description", selectresult.get(i).get("description").toString());
				} else {
					map.put("description", "");
				}
				if (selectresult.get(i).get("priceup") != null) {
					map.put("priceup", selectresult.get(i).get("priceup").toString());
				} else {
					map.put("priceup", "");
				}
				if (selectresult.get(i).get("pricedown") != null) {
					map.put("pricedown", selectresult.get(i).get("pricedown").toString());
				} else {
					map.put("pricedown", "");
				}
				if (selectresult.get(i).get("areaup") != null) {
					map.put("areaup", selectresult.get(i).get("areaup").toString());
				} else {
					map.put("areaup", "");
				}
				if (selectresult.get(i).get("areadown") != null) {
					map.put("areadown", selectresult.get(i).get("areadown").toString());
				} else {
					map.put("areadown", "");
				}
				if (selectresult.get(i).get("tele") != null) {
					map.put("phone", selectresult.get(i).get("tele")
							.toString());
				} else {
					map.put("phone", "");
				}
				if (selectresult.get(i).get("officetype") != null) {
					map.put("officetype", selectresult.get(i).get("officetype")
							.toString());
				} else {
					map.put("officetype", 1);
				}
				listData.add(map);	
			}
			
		}
	}
	
}
	
	/**
	 * ˢ��adapter
	 * 
	 * ���ߣ�Ring �����ڣ�2013-2-20
	 */
	public void notifyAdapter() {
		getListData();
		notifiView();
	}
	@Override
	public void notifiView() {
		adapter = new DemandManagerAdapter(this, listData);
		listView.setAdapter(adapter);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(listData.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(MyApplication.getInstance().isClientDemandBackBtnVisiblity()){
			
		}
		Intent intent = new Intent(this,DetailDemandActivity.class);
		HashMap<String,Object> map = listData.get(arg2-1);
		Bundle bundle = new Bundle();
		bundle.putString("phone", map.get("phone").toString());
		bundle.putString("areaup", map.get("areaup").toString());
		bundle.putString("pricedown", map.get("pricedown").toString());
		bundle.putString("username",map.get("username").toString() );
		bundle.putString("areadown", map.get("areadown").toString());
		bundle.putString("releaseid", map.get("releaseid").toString());
		bundle.putString("description", map.get("description").toString());
		bundle.putString("name", map.get("name").toString());
		bundle.putString("priceup",map.get("priceup").toString() );
		bundle.putString("userid",map.get("userid").toString() );
		bundle.putString("contact", map.get("contact").toString());
		bundle.putString("buildid", map.get("buildid").toString());
		bundle.putString("officetype", map.get("officetype").toString());
		intent.putExtra("bundle", bundle);
		startActivity(intent);
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
			if (NetworkCheck.IsHaveInternet(ClientDemandActivity.this)) {
				boolean b = false;
				handler.sendEmptyMessage(5);// ����������
				b = getCurRelease();
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
				listView.stopRefresh();
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
					errormsg = "����ʱ��û���յ��ͻ��������������з��ϵķ�Դ�������ǻᾡ��Ϊ����������";
				} else {
					errormsg = getResources().getString(
							R.string.toast_message_failure);
				}
				listView.stopRefresh();
				noDataText.setText(errormsg);
//				Toast.makeText(DemandManagerActivity.this.getParent(), errormsg, Toast.LENGTH_SHORT).show();
				break;
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(ClientDemandActivity.this
						.getParent());
				listView.stopRefresh();
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(
						ClientDemandActivity.this.getParent());
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
	public boolean getCurRelease() {
		if(MyApplication.getInstance(this).getSosouserinfo(this)==null
				||MyApplication.getInstance(this).getSosouserinfo(this).getUserID()==null){
			return false;
		}
//		if(DBHelper
//				.getInstance(this)
//				.selectRow(
//						"select * from soso_configurationinfo where name='TCUSRELEASE_TIME' and updatedate = value and value<>'' and userid = '"
//				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
//						null).size()>0){
//			return true;
//		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='TCUSRELEASE_TIME' and userid = '"
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

		// params ����Ĳ����б�
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UpdateDate", ""));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = StringUtils.formatCalendar1(sdf.format(new Date()),MyApplication.error_value);
		time = StringUtils.formatDate1(time,3);
		params.add(new BasicNameValuePair("whereClause", "Adddate > "+"'"+time+"'"));
		params.add(new BasicNameValuePair("OrderBy", "Adddate"));
		uploaddata = new SoSoUploadData(this, "CurReleaseSelect.aspx",
				params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(MyApplication
				.getInstance(this).getSosouserinfo().getUserID());
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
	 * @param districtid
	 */
	private void dealReponse(String userid) {
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TCUSRELEASE_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
		}
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TCUSRELEASE_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
			Type listType = new TypeToken<LinkedList<SoSoCusReleaseInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoCusReleaseInfo> soSoCusReleaseInfos = null;
			SoSoCusReleaseInfo soSoCusReleaseInfo = null;
			ContentValues values = new ContentValues();
			soSoCusReleaseInfos = gson.fromJson(reponse, listType);
			if (soSoCusReleaseInfos != null && soSoCusReleaseInfos.size() > 0) {
				for (Iterator<SoSoCusReleaseInfo> iterator = soSoCusReleaseInfos
						.iterator(); iterator.hasNext();) {
					soSoCusReleaseInfo = (SoSoCusReleaseInfo) iterator.next();
					if (soSoCusReleaseInfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("soso_cusreleaseinfo",
								"releaseid = ?", new String[] { soSoCusReleaseInfo
								.getReleaseID() });
						continue;
					}
					
//					"releaseid text,"+//����id
//					"userid text,"+//������id
//					"username integer,"+//�������û���
//					"title integer,"+//�������
//					"tele text,"+//�����˵ĵ绰
//					"contact text,"+//��������ϵ��
//					"areaup integer,"+//�������
//					"areadown text,"+//�������
//					"priceup text,"+//�۸�����
//					"pricedown text,"+//�۸�����
//					"description text,"+//��������
//					"officetype integer,"+//��Դ����
//					"unit text"+//��λ
					if (soSoCusReleaseInfo != null
							&& soSoCusReleaseInfo.getReleaseID() != null) {
						values.put("releaseid", soSoCusReleaseInfo.getReleaseID());
						values.put("userid", userid);
						values.put("username", soSoCusReleaseInfo.getUserName());
						values.put("title", soSoCusReleaseInfo.getTitle());
						values.put("tele", soSoCusReleaseInfo.getTele());
						values.put("contact", soSoCusReleaseInfo.getContact());
						values.put("areaup", soSoCusReleaseInfo.getAreaUp());
						values.put("areadown", soSoCusReleaseInfo.getAreaDown());
						values.put("priceup", soSoCusReleaseInfo.getPriceUp());
						values.put("pricedown", soSoCusReleaseInfo.getPriceDown());
						values.put("description",
								soSoCusReleaseInfo.getDescription());
						values.put("officetype",
								soSoCusReleaseInfo.getOfficeType());
						values.put("unit", soSoCusReleaseInfo.getUnit());
						if (DBHelper
								.getInstance(ClientDemandActivity.this)
								.selectRow(
										"select * from soso_cusreleaseinfo where releaseid = '"
												+ soSoCusReleaseInfo.getReleaseID()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(ClientDemandActivity.this)
									.insert("soso_cusreleaseinfo", values);
						} else {
							DBHelper.getInstance(ClientDemandActivity.this)
									.update("soso_cusreleaseinfo",
											values,
											"releaseid = ?",
											new String[] { soSoCusReleaseInfo
													.getReleaseID() });
						}

						values.clear();
					}
				}
				if (soSoCusReleaseInfos != null) {
					soSoCusReleaseInfos.clear();
					soSoCusReleaseInfos = null;
				}
				if (values != null) {
					values.clear();
					values = null;
				}

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
						"select updatedate from soso_configurationinfo where name='TCUSRELEASE_TIME' and userid = '"
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
		listView.setRefreshTime(updatedate);
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
