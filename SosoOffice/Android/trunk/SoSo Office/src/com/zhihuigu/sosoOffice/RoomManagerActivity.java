/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.RoomLayerAdapter;
import com.zhihuigu.sosoOffice.Adapter.RoomManagerAdatper;
import com.zhihuigu.sosoOffice.Adapter.RoomManagerForListAdapter;
import com.zhihuigu.sosoOffice.Adapter.RoomManagerGridViewAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.View.XListView;
import com.zhihuigu.sosoOffice.View.XListView.IXListViewListener;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoBuildUserInfo;
import com.zhihuigu.sosoOffice.model.SoSoOfficeInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.FileTools;
import com.zhihuigu.sosoOffice.utils.MD5;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

/**
 * @author ������
 * @createDate 2013/1/18 ��Դ������
 */
public class RoomManagerActivity extends BaseActivity implements
		Activity_interface, OnItemClickListener,IXListViewListener{
	private Button backBtn, switchBtn, addRoomBtn;// ���ذ�ť �л��༭��Ǳ༭��ť ��ӷ�Դ��ť
	private RelativeLayout roomListLayout, daiShenHeLayout;// ���ͨ���ķ�Դ�б�    ����˵ķ�Դ�б��л�
	private TextView roomListText, daiShenHeText;
	private ImageView roomListImg, daiShenHeImg;
	private TextView addressText;
	private GridView layerGv;
	public static XListView roomLV;
	private Button bianJi;// �༭��ť
	private ArrayList<String> layerList = new ArrayList<String>();
	private ArrayList<HashMap<String, Object>> roomList = new ArrayList<HashMap<String, Object>>();// ͼ���б�ķ�Դ���
	private ArrayList<HashMap<String, Object>> roomListForLinear = new ArrayList<HashMap<String, Object>>();// ��Դ������ʽ�б�
	private ArrayList<HashMap<String, Object>> roomListForDaiShenHe = new ArrayList<HashMap<String, Object>>();// //����˵ķ�Դ�б����ݼ���
//	public static int list_type = 0;// 0����Ϊͼ���б���ʾ 1����Ϊ�����б���ʾ
	private int bianji_state = 0;// 0����༭��ť�Ĺ���Ϊ�л����༭ģʽ�� 1�����л����Ǳ༭ģʽ

	private SoSoUploadData uploaddata;// �������������
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private ProgressDialog progressdialog;
	private String buildid = "";// ¥��id
	private String buildmc = "";// ¥������
	private RoomManagerGridViewAdapter roomadapter = null;// �����б�������

	private LinearLayout layerLayout;
	private int tag = 0;//1��¥���б�����������ģ�2����ӷ�Դ��ת������
	private int tag1 = 0;//��һ�ν���activity
	TextView layerNumber;
	private boolean dialogDisplay = false;//ר�Ŵ��������·�Դ�״ν��뷿Դ�������ʱ�����ʾͼƬʱˢ���б�
	
	private LinearLayout noDataLayout;//û������ʱ��ʾ�Ĳ���
	private ImageView noDataImg;//û������ʱ��ͼƬ���ˢ��
	private TextView noDataText;//û������ʱ����ʾ����
	private LinearLayout floorLayout;//����ص�¥���б���
	private ImageView floorImg;//�ص�¥���б��ͼƬ
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_roommanager);
		findView();
		initView();
		initData();
		tag = getIntent().getIntExtra("tag", 0);
		if (tag == 1||tag == 2) {
			new Thread(runnable).start();
		}
	}
	/**
	 * ���غ���
	 * @author ������
	 * @createDate 2013/4/9
	 */
	private void backFun(){
		if(MyApplication.getInstance().getRoom_state_for_examine() == 1){
//			MyApplication.getInstance().setRoomManagerStateFlag(0);
//			MyApplication.getInstance().setRoomManagerForm(list_type);
		}else{
//			MyApplication.getInstance().setRoomManagerStateFlag(1);
		}
		MyApplication.getInstance(this).setRoom_list_state(1);// ���õ�ǰ��Դ�б�Ϊ�Ǳ༭״̬
//		MyApplication.getInstance(this).setRoom_state_for_examine(1);// ���õ�ǰ�б�Ϊ���ͨ���ķ�Դ�б�
		finish();
	}
	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			CommonUtils.hideSoftKeyboard(this);
			backFun();
		} else if (v == switchBtn) {
			if (MyApplication.getInstance().getRoomManagerForm() == 0) {// �л���������ʽ
				roomLV.setVisibility(View.VISIBLE);
				layerGv.setVisibility(View.GONE);
				switchBtn.setBackgroundResource(R.drawable.soso_sekuai_btn);
				bianJi.setVisibility(View.VISIBLE);
				floorImg.setVisibility(View.VISIBLE);
				floorLayout.setOnClickListener(this);
				notifiViewForList(roomListForLinear);
				MyApplication.getInstance().setRoomManagerForm(1);
			} else if (MyApplication.getInstance().getRoomManagerForm() == 1) {// �л���ͼ���б���ʽ
				roomLV.setVisibility(View.GONE);
				layerGv.setVisibility(View.VISIBLE);
				notifyAdapter();
				MyApplication.getInstance().setRoomManagerForm(0);
				switchBtn.setBackgroundResource(R.drawable.soso_list_btn);
				bianJi.setVisibility(View.GONE);
				floorImg.setVisibility(View.GONE);
				floorLayout.setOnClickListener(null);
			}
		} else if (v == addRoomBtn) {//��ӷ�Դ
			MyApplication.getInstance().setAddRoomFlag(1);
			Intent intent = new Intent(this, ReleaseNewRoomActivity.class);
			intent.putExtra("activityFlag", 1);
			intent.putExtra("buildmc", buildmc);
			intent.putExtra("buildid", buildid);
			startActivity(intent);
		} else if (v == roomListLayout) {//���ͨ���ķ�Դ�б�
			shenHeEd();
		} else if (v == daiShenHeLayout) {//����˵ķ�Դ�б�
			if (MyApplication.getInstance().getRoomManagerForm() == 1) {
				roomLV.setVisibility(View.VISIBLE);
				layerGv.setVisibility(View.GONE);
			}
			floorImg.setVisibility(View.GONE);
			floorLayout.setOnClickListener(null);
			roomListLayout.setClickable(true);
			daiShenHeLayout.setClickable(false);
			bianji_state = 0;// �Ǳ༭״̬
			bianJi.setText("�༭");
			MyApplication.getInstance(this).setRoom_state_for_examine(2);// ���õ�ǰ�б�Ϊ����˵ķ�Դ�б�
			MyApplication.getInstance(this).setRoom_list_state(1);// ���õ�ǰ��Դ�б�Ϊ�Ǳ༭״̬
			switchBtn.setVisibility(View.GONE);
			bianJi.setVisibility(View.VISIBLE);
			daiShenHeText.setTextColor(Color.WHITE);
			roomListText.setTextColor(Color.rgb(102, 102, 102));
			daiShenHeImg.setImageResource(R.drawable.soso_house_sanjiao);
			roomListImg.setImageResource(R.drawable.soso_house_sanjiao1);
			roomListLayout
					.setBackgroundResource(R.drawable.soso_email_title_bg2);
			daiShenHeLayout
					.setBackgroundResource(R.drawable.soso_email_title_bg1);
			 getDataForDaiShenHe();// ���ش���˵ļ�������
			notifiViewForList(roomListForLinear);

		} else if (v == bianJi) {
			int room_state = MyApplication.getInstance(this)
					.getRoom_state_for_examine();
			if (bianji_state == 0) {
				// ���������б���ʽΪ�༭״̬����һ�д������д��notifiViewForList();֮ǰ,����༭��Ҫ�İ�ť���������
				MyApplication.getInstance(this).setRoom_list_state(2);
				if (room_state == 1) {
					notifiViewForList(roomListForLinear);// ˢ�������б���ʾ�༭��Ҫ�İ�ť
				} else {
					notifiViewForList(roomListForLinear);// ˢ�´���˵ķ�Դ�������б���ʾ�༭��Ҫ�İ�ť
				}
				bianji_state = 1;
				bianJi.setText("���");
			} else if (bianji_state == 1) {
				// ���������б���ʽΪ�༭״̬����һ�д������д��notifiViewForList();֮ǰ,����༭��Ҫ�İ�ť���������
				MyApplication.getInstance(this).setRoom_list_state(1);
				if (room_state == 1) {
					notifiViewForList(roomListForLinear);// ˢ�������б���ʾ�༭��Ҫ�İ�ť
				} else {
					notifiViewForList(roomListForLinear);// ˢ�´���˵ķ�Դ�������б���ʾ�༭��Ҫ�İ�ť
				}
				bianji_state = 0;
				bianJi.setText("�༭");
			}
		} else if (v == layerLayout) {//����ص�¥���б�
			floorFlag = false;
			notifyAdapter();
		}else if(v == displayImgBtn){//��ʾ��ԴͼƬ
			MyApplication.getInstance().setDisplayRoomPhoto(1);
			ContentValues values = new ContentValues();
			values.put("isshowimage", 1);
			DBHelper.getInstance(this).update(
					"sososettinginfo",
					values,
					"userid = ?",
					new String[] { MyApplication.getInstance(this)
							.getSosouserinfo(this).getUserID() });
			if (values != null) {
				values.clear();
				values = null;
			}
//			listAdapter.notifyDataSetChanged();
			if(dialogDisplay){
				notifiViewForList(roomListForLinear);
			}
			dismiss();
		}else if(v == hideImgBtn){//����ʾ��ԴͼƬ
			MyApplication.getInstance().setDisplayRoomPhoto(2);
			ContentValues values = new ContentValues();
			values.put("isshowimage", 2);
			DBHelper.getInstance(this).update(
					"sososettinginfo",
					values,
					"userid = ?",
					new String[] { MyApplication.getInstance(this)
							.getSosouserinfo(this).getUserID() });
			if (values != null) {
				values.clear();
				values = null;
			}
//			listAdapter.notifyDataSetChanged();
			if(dialogDisplay){
				notifiViewForList(roomListForLinear);
			}
			dismiss();
		}else if(v == floorLayout){//����ص�¥���б�
			MyApplication.getInstance().setRoomManagerForm(0);
			switchBtn.setBackgroundResource(R.drawable.soso_list_btn);
			bianJi.setVisibility(View.GONE);
			notifyAdapter();
			floorFlag = false;
		}else if(v == noDataImg){//û������ʱ���ͼƬˢ��
			new Thread(runnable).start();
		}
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			backFun();
		}
		return true;
	}

	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		switchBtn = (Button) findViewById(R.id.switchoverBtn);
		addRoomBtn = (Button) findViewById(R.id.addRoomBtn);
		roomListLayout = (RelativeLayout) findViewById(R.id.roomListLayout);
		daiShenHeLayout = (RelativeLayout) findViewById(R.id.daishenheLayout);
		roomListText = (TextView) findViewById(R.id.roomListText);
		daiShenHeText = (TextView) findViewById(R.id.daishenheText);
		roomListImg = (ImageView) findViewById(R.id.roomListImg);
		daiShenHeImg = (ImageView) findViewById(R.id.daiShenHeImg);
		addressText = (TextView) findViewById(R.id.address);
		roomLV = (XListView) findViewById(R.id.roomListView);
		bianJi = (Button) findViewById(R.id.bianjiBtn);
		layerGv = (GridView) findViewById(R.id.layerGridview);
		layerLayout = (LinearLayout) findViewById(R.id.layerLayout);
		layerNumber = (TextView) findViewById(R.id.layerNumber);
		noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		noDataText = (TextView) findViewById(R.id.noDataMsg);
		noDataImg.setOnClickListener(this);
		floorLayout = (LinearLayout) findViewById(R.id.floor_layout);
		floorImg = (ImageView) findViewById(R.id.floorImg);
	}

	@Override
	public void initView() {
		roomLV.setPullLoadEnable(false);
		roomLV.setPullRefreshEnable(true);
		roomLV.setXListViewListener(this);
		backBtn.setOnClickListener(this);
		switchBtn.setOnClickListener(this);
		addRoomBtn.setOnClickListener(this);
		roomListLayout.setOnClickListener(this);
		daiShenHeLayout.setOnClickListener(this);
		bianJi.setOnClickListener(this);
		layerGv.setOnItemClickListener(this);
		layerLayout.setOnClickListener(this);
	}

	@Override
	public void initData() {
	}
	
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ContentValues values = new ContentValues();
		values.put("room_state_for_examine", MyApplication.getInstance()
				.getRoom_state_for_examine());
		values.put("roommanagerform", MyApplication.getInstance().getRoomManagerForm());
		DBHelper.getInstance(this).update(
				"sososettinginfo",
				values,
				"userid = ?",
				new String[] { MyApplication.getInstance(this)
						.getSosouserinfo(this).getUserID() });
		if (values != null) {
			values.clear();
			values = null;
		}
	}

	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		//0�����¥���б���뷿Դ�������   1�������ӷ�Դ�ɹ����뷿Դ����Ĵ�����б�
		buildid = MyApplication.getInstance().getBuildid();
		addressText.setText(getaddressText(buildid));
		if(MyApplication.getInstance().getRoomManagerFlag() ==  1){
		if (MyApplication.getInstance().getRoomManagerForm() == 1) {
			roomLV.setVisibility(View.VISIBLE);
			layerGv.setVisibility(View.GONE);
		}
		roomListLayout.setClickable(true);
		daiShenHeLayout.setClickable(false);
		bianji_state = 0;// �Ǳ༭״̬
		bianJi.setText("�༭");
		MyApplication.getInstance(this).setRoom_state_for_examine(2);// ���õ�ǰ�б�Ϊ����˵ķ�Դ�б�
		MyApplication.getInstance(this).setRoom_list_state(1);// ���õ�ǰ��Դ�б�Ϊ�Ǳ༭״̬
		switchBtn.setVisibility(View.GONE);
		bianJi.setVisibility(View.VISIBLE);
		daiShenHeText.setTextColor(Color.WHITE);
		roomListText.setTextColor(Color.rgb(102, 102, 102));
		daiShenHeImg.setImageResource(R.drawable.soso_house_sanjiao);
		roomListImg.setImageResource(R.drawable.soso_house_sanjiao1);
		roomListLayout
				.setBackgroundResource(R.drawable.soso_email_title_bg2);
		daiShenHeLayout
				.setBackgroundResource(R.drawable.soso_email_title_bg1);
		 getDataForDaiShenHe();// ���ش���˵ļ�������
		notifiViewForList(roomListForLinear);
		MyApplication.getInstance().setRoomManagerFlag(0);
		dialogDisplay = true;
		}else if(MyApplication.getInstance().getRoomManagerFlag() ==  2){
			
		}else if(MyApplication.getInstance().getRoomManagerFlag() ==  3){
			 shenHeEd();
		}
		
		if(MyApplication.getInstance().getRoom_state_for_examine() == 1){//���ͨ���ķ�Դ�б�
			floorLayout.setOnClickListener(this);
			if(MyApplication.getInstance().getRoomManagerForm() == 1){
				getRoomListFromLayer("");// ��ѯ�����еķ�Դ����
				notifiViewForList(roomListForLinear);
			}else if(MyApplication.getInstance().getRoomManagerForm() == 0){
				roomLV.setVisibility(View.GONE);
				layerGv.setVisibility(View.VISIBLE);
				notifyAdapter();
				MyApplication.getInstance().setRoomManagerForm(0);
				switchBtn.setBackgroundResource(R.drawable.soso_list_btn);
				bianJi.setVisibility(View.GONE);
				floorImg.setVisibility(View.GONE);
				floorLayout.setOnClickListener(null);
			}
		}else if(MyApplication.getInstance().getRoom_state_for_examine() == 2){//����˵ķ�Դ�б�
			floorImg.setVisibility(View.GONE);
		}
		super.onResume();
	}
	/**
	 * �л������ͨ���ķ�Դ�б�
	 * @author ������
	 * @createDate 2013/4/2
	 */
	public void shenHeEd(){
		roomListLayout.setClickable(false);
		daiShenHeLayout.setClickable(true);
		switchBtn.setVisibility(View.VISIBLE);
		MyApplication.getInstance(this).setRoom_state_for_examine(1);// ���õ�ǰ�б�Ϊ���ͨ���ķ�Դ�б�
		MyApplication.getInstance(this).setRoom_list_state(1);// ���õ�ǰ��Դ�б�Ϊ�Ǳ༭״̬
		roomListText.setTextColor(Color.WHITE);
		daiShenHeText.setTextColor(Color.rgb(102, 102, 102));
		roomListImg.setImageResource(R.drawable.soso_house_sanjiao);
		daiShenHeImg.setImageResource(R.drawable.soso_house_sanjiao1);
		roomListLayout
				.setBackgroundResource(R.drawable.soso_email_title_bg1);
		daiShenHeLayout
				.setBackgroundResource(R.drawable.soso_email_title_bg2);
		bianji_state = 0;// �Ǳ༭״̬
		bianJi.setText("�༭");
		if (MyApplication.getInstance().getRoomManagerForm() == 1) {// �л���������ʽ
			getRoomListFromLayer("");// ��ѯ�����еķ�Դ����
			switchBtn.setBackgroundResource(R.drawable.soso_sekuai_btn);
			bianJi.setVisibility(View.VISIBLE);
			floorImg.setVisibility(View.VISIBLE);
			floorLayout.setOnClickListener(this);
			notifiViewForList(roomListForLinear);
		} else if (MyApplication.getInstance().getRoomManagerForm() == 0) {// �л���ͼ���б���ʽ
			if(floorFlag){//������ͼ
				floorFlag = true;
				getRoomListFromLayer(currentLayer);
				layerNumber.setText(currentLayer);
				notifiView();
				switchBtn.setBackgroundResource(R.drawable.soso_list_btn);
				bianJi.setVisibility(View.GONE);
				floorImg.setVisibility(View.VISIBLE);
				floorLayout.setOnClickListener(this);
			}else{
				floorFlag = false;
				notifyAdapter();
				switchBtn.setBackgroundResource(R.drawable.soso_list_btn);
				bianJi.setVisibility(View.GONE);
				floorImg.setVisibility(View.GONE);
				floorLayout.setOnClickListener(null);
			}
			
		}
	}
	public String getaddressText(String buildid) {
		StringBuffer address = new StringBuffer("");
		if (buildid != null && !buildid.equals("")) {
			String sql = "select areamc,districtmc,buildmc from soso_buildinfo where buildid='"
					+ buildid + "'";
			List<Map<String, Object>> selectresult = DBHelper.getInstance(this)
					.selectRow(sql, null);
			if (selectresult != null && selectresult.size() > 0) {
				address.delete(0, address.length());
				address.append(selectresult.get(selectresult.size() - 1).get(
						"areamc"));
				address.append(selectresult.get(selectresult.size() - 1).get(
						"districtmc"));
				address.append(selectresult.get(selectresult.size() - 1).get(
						"buildmc"));
				buildmc = selectresult.get(selectresult.size() - 1).get(
						"buildmc").toString();
			}
		}
		return address.toString();
	}

	/**
	 * ��¥�����ݽ��д�������ͬ���ų�
	 * 
	 * @param oldList
	 *            ���з�Դ���ڵ�¥�㼯��
	 * @return ����һ���µ����ظ���¥�����ݼ���
	 * @author ������
	 * @createDate 2013/2/3
	 */
	private ArrayList<String> getLayerListData(ArrayList<String> oldList) {
		ArrayList<String> newList = new ArrayList<String>();
		for (int i = 0; i < oldList.size(); i++) {
			String oldStr = oldList.get(i);
			boolean boo = false;
			for (int j = 0; j < newList.size(); j++) {
				String newLayer = newList.get(j);
				if (newLayer.equals(oldStr)) {
					boo = true;
					break;
				}
			}
			if (!boo) {
				newList.add(oldStr);
			}
		}
		for (int a = 0; a < newList.size() - 1; a++) {// �����ǽ���ȡ����¥��������򡣡���������
														// �õ��Ǽ򵥵�ð�����򷽷�
			for (int b = 0; b < newList.size() - a - 1; b++) {
				String temp1 = newList.get(b);
				String temp2 = newList.get(b + 1);
				if (Integer.parseInt(temp2) < Integer.parseInt(temp1)) {
					newList.remove(b);
					newList.add(b, temp2);
					newList.remove(b + 1);
					newList.add(b + 1, temp1);
				}
			}
		}
		return newList;
	}

	/**
	 * ��ȡ¥�̶�Ӧ������¥��
	 * 
	 * @author ������
	 * @createDate 2013/2/3
	 */
	public void getFloorsListData() {
		List<Map<String, Object>> selectresult = null;
		String sql = "select floors from soso_officeinfo where buildid = '"
					+ buildid
					+ "'and createuserid='"
					+ MyApplication.getInstance(this).getSosouserinfo(this)
							.getUserID() + "' and ischecked <> 0 order by cast(floors as Integer)";
		selectresult = DBHelper.getInstance(this).selectRow(sql, null);
		if (selectresult != null) {
			for (int i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("floors") != null
						&& !selectresult.get(i).get("floors").toString()
								.equals("")) {
					try {
						Integer.parseInt(selectresult.get(i).get("floors")
								.toString());
						layerList.add(selectresult.get(i).get("floors")
								.toString());
					} catch (Exception e) {
					}
				}
				// else{
				// layerList.add("");
				// }
			}
		}
		layerList = getLayerListData(layerList);
	}

	@Override
	public void notifiView() {// ͼ����ʽ�ķ�Դ�б�
		roomLV.setVisibility(View.GONE);
		layerGv.setVisibility(View.VISIBLE);
		layerLayout.setVisibility(View.VISIBLE);

		roomadapter = new RoomManagerGridViewAdapter(this, roomList);
		layerGv.setNumColumns(3);
		layerGv.setAdapter(roomadapter);
		layerGv.setCacheColorHint(0);
		layerGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(roomList.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
		
	}

	/**
	 * ˢ��listView�б�����
	 * 
	 * ���ߣ�Ring �����ڣ�2013-1-31
	 */
	public void notifyAdapter() {
		roomList.clear();
		getRoomListFromLayer("");// ��ѯ�����еķ�Դ����
		getFloorsListData();
		roomLV.setVisibility(View.GONE);
		layerGv.setVisibility(View.VISIBLE);
		layerLayout.setVisibility(View.GONE);
		layerGv.setNumColumns(4);
		layerGv.setAdapter(new RoomLayerAdapter(this, layerList));
		layerGv.setCacheColorHint(0);
		layerGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(layerList.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
		floorImg.setVisibility(View.GONE);
		floorLayout.setOnClickListener(null);
		/*
		 * if(roomadapter!=null){ roomadapter.notifyDataSetChanged(); }
		 */
	}

	/**
	 * ��Դ����������б���ʽ
	 * 
	 * @createDate 2013/1/23
	 * @author ������
	 */
	private void notifiViewForList(ArrayList<HashMap<String, Object>> list) {
		if(MyApplication.getInstance().getRoom_state_for_examine() == 1){//���ͨ���ķ�Դ�б�
			noDataText.setText("�������κη�Դ���ݣ��������ϽǵļӺţ��Ͻ��������ķ�Դ��");
		}else{
			noDataText.setText("�����޴���˷�Դ����!");
		}
		roomLV.setVisibility(View.VISIBLE);
		layerGv.setVisibility(View.GONE);
		roomLV.setAdapter(new RoomManagerForListAdapter(this, list));// ����������
		roomLV.setCacheColorHint(0);
		roomLV.setDivider(getResources().getDrawable(R.drawable.soso_zc_line));
		roomLV.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(MyApplication.getInstance().getRoom_list_state() == 1){//�Ǳ༭״̬��
			roomLV.setOnItemClickListener(this);
		}else{//�༭״̬��
			roomLV.setOnItemClickListener(null);
		}
		if(list.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
	}

//	/**
//	 * @author ������
//	 * @createDate 2013/1/23 ��ȡ������ʽ�б������,��������ݾ��Ǵӱ��صļ���roomList�л�ȡ
//	 */
//	private void getDataForLinearList() {
//		roomListForLinear.clear();
//		for (int i = 0; i < roomList.size(); i++) {
//			HashMap<String, Object> map = roomList.get(i);// ��ȡ������е�ÿһ���Ӽ�
//			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) map
//					.get("list");// ��ȡÿһ���Ӽ��е�list����
//			for (int j = 0; j < list.size(); j++) {
//				HashMap<String, Object> map1 = list.get(j);// �ٻ�ȡ�Ӽ��е�list�е��Ӽ�
//				map1.put("layer", map.get("layer"));
//				roomListForLinear.add(map1);// ��ȡ�õ���С��Ԫ����������ʽ�ļ�����
//			}
//		}
//	}

	/**
	 * ��ȡ����˵ķ�Դ�б�����
	 * 
	 * @createDate 2013��1��24
	 * @author ������
	 */
	private void getDataForDaiShenHe() {
		roomListForDaiShenHe.clear();
		getRoomListFromLayer("");
		roomListForDaiShenHe = roomListForLinear;
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
			if (NetworkCheck.IsHaveInternet(RoomManagerActivity.this)) {
				boolean b = false;
				handler.sendEmptyMessage(5);// ����������
				b=getUseroffices();
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
				if (tag == 1) {
					if (MyApplication.getInstance().getRoom_state_for_examine() == 1) {//���ͨ��
						shenHeEd();
					} else if (MyApplication.getInstance().getRoom_state_for_examine() == 2) {//�����
						bianji_state = 0;// �Ǳ༭״̬
						bianJi.setText("�༭");
						MyApplication.getInstance().setRoom_state_for_examine(2);// ���õ�ǰ�б�Ϊ����˵ķ�Դ�б�
						MyApplication.getInstance().setRoom_list_state(1);// ���õ�ǰ��Դ�б�Ϊ�Ǳ༭״̬
						switchBtn.setVisibility(View.GONE);
						bianJi.setVisibility(View.VISIBLE);
						daiShenHeText.setTextColor(Color.WHITE);
						roomListText.setTextColor(Color.rgb(102, 102, 102));
						daiShenHeImg.setImageResource(R.drawable.soso_house_sanjiao);
						roomListImg.setImageResource(R.drawable.soso_house_sanjiao1);
						roomListLayout
								.setBackgroundResource(R.drawable.soso_email_title_bg2);
						daiShenHeLayout
								.setBackgroundResource(R.drawable.soso_email_title_bg1);
						getDataForDaiShenHe();// ���ش���˵ļ�������
						notifiViewForList(roomListForLinear);
					} else {
						notifyAdapter();
					}
				} else if (tag == 2) {
					getDataForDaiShenHe();// ���ش���˵ļ�������
					notifiViewForList(roomListForLinear);
					MyApplication.getInstance().setRoomManagerFlag(0);
				}
				handler.sendEmptyMessage(7);
				roomLV.stopRefresh();
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
					if(MyApplication.getInstance().getRoom_state_for_examine() == 1){//���ͨ���ķ�Դ�б�
						errormsg = "�������κη�Դ���ݣ��������ϽǵļӺţ��Ͻ��������ķ�Դ��";
					}else if(MyApplication.getInstance().getRoom_state_for_examine() == 2){//����˵ķ�Դ�б�
						errormsg = "�����޴���˷�Դ����!";
					}
				} else {
					errormsg = getResources().getString(
							R.string.toast_message_failure);
				}
//				noDataText.setText(errormsg);
				roomLV.stopRefresh();
				// Toast.makeText(RoomManagerActivity.this, errormsg,
				// Toast.LENGTH_SHORT).show();
				break;
			case 4:// û������ʱ���û���ʾ
				roomLV.stopRefresh();
				MessageBox.CreateAlertDialog(RoomManagerActivity.this);
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(RoomManagerActivity.this);
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
			case 7:// �Ƿ����ù���ʾͼƬ
				if (MyApplication.getInstance().getDisplayRoomPhoto() == 0) {
					if (tag1 == 0) {
						displayRoomImgDialog();
						tag1 = 1;
					}
				}
				break;
			}
		};
	};
	
	/**
	 * ��ȡ�û�����¥��
	 * 
	 * ���ߣ�Ring �����ڣ�2013-1-31
	 */
	public boolean getUseroffices() {
		if(MyApplication.getInstance(this).getSosouserinfo(this)==null
				||MyApplication.getInstance(this).getSosouserinfo(this).getUserID()==null){
			return false;
		}
		// params ����Ĳ����б�
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name=''",
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
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UpdateDate", updatedate));
		String whereclause = "";
		whereclause = "CreateUserID="
				+ MyApplication.getInstance(this).getSosouserinfo(this)
						.getUserID() + " and BuildID=" + buildid;
		params.add(new BasicNameValuePair("whereClause", whereclause));
		params.add(new BasicNameValuePair("orderBy", "Floors"));
		params.add(new BasicNameValuePair("pageIndex", "0"));
		params.add(new BasicNameValuePair("pageLength", "10000"));
		uploaddata = new SoSoUploadData(this, "OfficeGetPaged.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(MyApplication.getInstance(this).getSosouserinfo(this)
				.getUserID());
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		}else{
			return false;
		}
	}

	/**
	 * �����������Ӧֵ�������صķ�Դ������Ϣ��������
	 * 
	 * ���ߣ�Ring �����ڣ�2013-1-31
	 * 
	 * @param districtid
	 */
	private void dealReponse(String userid) {
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name=''");
			Type listType = new TypeToken<LinkedList<SoSoOfficeInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoOfficeInfo> sosoofficeinfos = null;
			SoSoOfficeInfo sosoofficeinfo = null;
			ContentValues values = new ContentValues();
			try {
				JSONObject json = new JSONObject(reponse);
				String officedatas = json.getString("data");
				sosoofficeinfos = gson.fromJson(officedatas, listType);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (sosoofficeinfos != null && sosoofficeinfos.size() > 0) {
				for (Iterator<SoSoOfficeInfo> iterator = sosoofficeinfos
						.iterator(); iterator.hasNext();) {
					sosoofficeinfo = (SoSoOfficeInfo) iterator.next();
					if (sosoofficeinfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("soso_officeinfo",
								"officeid = ?", new String[] { sosoofficeinfo
								.getOfficeID() });
						continue;
					}
					if (sosoofficeinfo != null
							&& sosoofficeinfo.getOfficeID() != null) {
						values.put("officeid", sosoofficeinfo.getOfficeID());
						values.put("createuserid",
								sosoofficeinfo.getCreateUserID());
						values.put("areaup", sosoofficeinfo.getAreaUp());
						values.put("areadown", sosoofficeinfo.getAreaDown());
						values.put("address", sosoofficeinfo.getAddress());
						values.put("telephone", sosoofficeinfo.getTelePhone());
						values.put("storey", sosoofficeinfo.getStorey());
						values.put("floors", sosoofficeinfo.getFloors());
						values.put("priceup", sosoofficeinfo.getPriceUp());
						values.put("pricedown", sosoofficeinfo.getPriceDown());
						values.put("officemc", sosoofficeinfo.getOfficeMC());
						values.put("wymanagementfees",
								sosoofficeinfo.getWYManagmentFees());
						values.put("officetype", sosoofficeinfo.getOfficeType());
						values.put("keywords", "");
						values.put("fycx", sosoofficeinfo.getFYCX());
						values.put("zcyh", sosoofficeinfo.getZCYH());
						values.put("tsyh", sosoofficeinfo.getTSYH());
						values.put("fyjj", sosoofficeinfo.getFYJJ());
						values.put("updatedate", "");
						values.put("buildid", sosoofficeinfo.getBuildID());
						values.put("buildmc", sosoofficeinfo.getBuildMC());
						values.put("officestate",
								sosoofficeinfo.getOfficeState());
						values.put("officestatus", "");
						values.put("isprice", "");
						values.put("offadddate", "");
						values.put("roomrate", sosoofficeinfo.getRoomRate());
						values.put("nextalertdate", "");
						values.put("ispushed", sosoofficeinfo.getIsPushed());
						values.put("pushstate", sosoofficeinfo.getPushState());
						values.put("ischecked", sosoofficeinfo.getIsChecked());
						values.put("isrent", sosoofficeinfo.getIsRent());
						values.put("imageid", sosoofficeinfo.getShowImageID());

						if (DBHelper
								.getInstance(RoomManagerActivity.this)
								.selectRow(
										"select * from soso_officeinfo where officeid = '"
												+ sosoofficeinfo.getOfficeID()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(RoomManagerActivity.this)
									.insert("soso_officeinfo", values);
						} else {
							DBHelper.getInstance(RoomManagerActivity.this)
									.update("soso_officeinfo",
											values,
											"officeid = ?",
											new String[] { sosoofficeinfo
													.getOfficeID() });
						}
						values.clear();
					}
				}
				if (sosoofficeinfos != null) {
					sosoofficeinfos.clear();
					sosoofficeinfos = null;
				}
				if(values!=null){
					values.clear();
					values = null;
				}
			}
		}
	}

	/**
	 * ����¥��Ż�ȡ¥��ķ�Դ����
	 * 
	 * @author ������
	 * @createDate 2013/2/3
	 * @param layer
	 *            ¥���
	 * �÷���Ring�޸��� 2013/2/19����Ӳ���ischeck�Ƿ����� 
	 */
	public void getRoomListFromLayer(String layer) {
		String name= "";
		String sdpath="";
		File file1;//����ͼ�ļ�
		List<Map<String, Object>> selectresult = null;
		HashMap<String, Object> map = null;
		String sql = null;
		if (layer.equals("")) {
			roomListForLinear.clear();
			if(MyApplication.getInstance().getRoom_state_for_examine()!=1){
				sql = "select * from soso_officeinfo where buildid = '"
						+ buildid
						+ "' and createuserid='"
						+ MyApplication.getInstance(this).getSosouserinfo(this)
								.getUserID() + "' and ischecked=0 order by cast(officeid as Integer) desc";
			}else{
				sql = "select * from soso_officeinfo where buildid = '"
						+ buildid
						+ "' and createuserid='"
						+ MyApplication.getInstance(this).getSosouserinfo(this)
								.getUserID() + "' and ischecked <> 0 order by cast(floors as Integer)";
			}
		} else {
			roomList.clear();
			if(MyApplication.getInstance().getRoom_state_for_examine()!=1){
				sql = "select * from soso_officeinfo where floors = '"
						+ layer
						+ "' and buildid = '"
						+ buildid
						+ "'and createuserid='"
						+ MyApplication.getInstance(this).getSosouserinfo(this)
								.getUserID() + "' and ischecked = 0  order by cast(officeid as Integer) desc";
			}else{
				sql = "select * from soso_officeinfo where floors = '"
						+ layer
						+ "' and buildid = '"
						+ buildid
						+ "'and createuserid='"
						+ MyApplication.getInstance(this).getSosouserinfo(this)
								.getUserID() + "' and ischecked <> 0  order by cast(officeid as Integer) desc";
			}
			
		}
		selectresult = DBHelper.getInstance(this).selectRow(sql, null);
		if (selectresult != null) {
			for (int i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("officeid") != null
						&&!selectresult.get(i).get("floors").toString().equals("")) {// //
					try {
						// "officeid text,"+//��Դid
						Integer.parseInt(selectresult.get(i).get("floors")
								.toString());
					} catch (Exception e) {
						continue;
					}
					map = new HashMap<String, Object>();
					if (selectresult.get(i).get("offadddate") != null) {// "offadddate text,"+//���ʱ��
						map.put("offadddate",
								selectresult.get(i).get("offadddate")
										.toString());
					} else {
						map.put("offadddate", "");
					}
					if (selectresult.get(i).get("fyjj") != null) {// "fyjj text,"+//��Դ���
						map.put("fyjj", selectresult.get(i).get("fyjj")
								.toString());
					} else {
						map.put("fyjj", "");
					}
					if (selectresult.get(i).get("roomrate") != null) {// "fyjj text,"+//��Դ���
						map.put("roomrate", selectresult.get(i).get("roomrate")
								.toString());
					} else {
						map.put("roomrate", "");
					}
					if (selectresult.get(i).get("pushstate") != null) {// "areadown text,"+//�������
						map.put("pushstate", selectresult.get(i).get("pushstate")
								.toString());
					} else {
						map.put("pushstate", 1);
					}
					if (selectresult.get(i).get("areadown") != null) {// "areadown text,"+//�������
						map.put("areadown", selectresult.get(i).get("areadown")
								.toString());
					} else {
						map.put("areadown", "");
					}
					if (selectresult.get(i).get("floors") != null) {// "floors text,"+//¥��
						map.put("floors", selectresult.get(i).get("floors")
								.toString());
					} else {
						map.put("floors", "");
					}
					if (selectresult.get(i).get("ispushed") != null) {//0δ���ͣ�1������
						map.put("ispushed", selectresult.get(i).get("ispushed")
								.toString());
					} else {
						map.put("ispushed", "");
					}
					if (selectresult.get(i).get("fycx") != null) {// "fycx integer,"+//��Դ����
						map.put("fycx", selectresult.get(i).get("fycx")
								.toString());
					} else {
						map.put("fycx", "");
					}
					if (selectresult.get(i).get("isprice") != null) {// "isprice integer,"+//0����������ۣ�1�������
						map.put("isprice", selectresult.get(i).get("isprice")
								.toString());
					} else {
						map.put("isprice", "");
					}
					if (selectresult.get(i).get("zcyh") != null) {// "zcyh text,"+//�����Ż�
						map.put("zcyh", selectresult.get(i).get("zcyh")
								.toString());
					} else {
						map.put("zcyh", "");
					}
					if (selectresult.get(i).get("pricedown") != null) {// "pricedown text,"+//��������
						map.put("pricedown",
								selectresult.get(i).get("pricedown").toString());
					} else {
						map.put("pricedown", "");
					}
					if (selectresult.get(i).get("keywords") != null) {// "keywords text,"+//��Դ�ؼ���
						map.put("keywords", selectresult.get(i).get("keywords")
								.toString());
					} else {
						map.put("keywords", "");
					}
					if (selectresult.get(i).get("isrent") != null) {// "officestate integer,"+//��Դ״̬
																			// 1�����ͨ��/δ�⣬2������3��˽��������4����Դ����5����ɾ��
						map.put("officestate",
								selectresult.get(i).get("isrent")
										.toString());
					} else {
						map.put("officestate", 0);
					}
					if (selectresult.get(i).get("priceup") != null) {// "priceup text,"+//��������
						map.put("priceup", selectresult.get(i).get("priceup")
								.toString());
					} else {
						map.put("priceup", "");
					}
					if (selectresult.get(i).get("wymanagementfees") != null) {// "wymanagementfees text,"+//��ҵ�����
						map.put("wymanagementfees",
								selectresult.get(i).get("wymanagementfees")
										.toString());
					} else {
						map.put("wymanagementfees", "");
					}

					if (selectresult.get(i).get("officestatus") != null) {// "officestatus integer,"+//0��������1���ɷָ�2���ɺϲ�
						map.put("officestatus",
								selectresult.get(i).get("officestatus")
										.toString());
					} else {
						map.put("officestatus", "");
					}
					map.put("officeid", selectresult.get(i).get("officeid")
							.toString());
					if (selectresult.get(i).get("areaup") != null) {// "areaup text,"+//�������
						map.put("areaup", selectresult.get(i).get("areaup")
								.toString());
					} else {
						map.put("areaup", "");
					}

					if (selectresult.get(i).get("nextalertdate") != null) {// "nextalertdate text"+//�´�����ʱ��
						map.put("nextalertdate",
								selectresult.get(i).get("nextalertdate")
										.toString());
					} else {
						map.put("nextalertdate", "");
					}

					if (selectresult.get(i).get("tsyh") != null) {// "tsyh text,"+//�����Ż�
						map.put("tsyh", selectresult.get(i).get("tsyh")
								.toString());
					} else {
						map.put("tsyh", "");
					}

					if (selectresult.get(i).get("officetype") != null) {// "officetype integer,"+//��Դ����
																		// 0��д��¥��1��ס¥2���Ƶ�ʽ��Ԣ��3԰����4��������
						map.put("officetype",
								selectresult.get(i).get("officetype")
										.toString());
					} else {
						map.put("officetype", i + 1);
					}

					if (selectresult.get(i).get("officemc") != null) {// "officemc text,"+//��Դ����
						map.put("officemc", selectresult.get(i).get("officemc")
								.toString());
					} else {
						map.put("officemc", "");
					}

					if (selectresult.get(i).get("address") != null) {// "address text,"+//��ַ
						map.put("address", selectresult.get(i).get("address")
								.toString());
					} else {
						map.put("address", "");
					}

					if (selectresult.get(i).get("storey") != null) {// "storey text,"+//���
						map.put("storey", selectresult.get(i).get("storey")
								.toString());
					} else {
						map.put("storey", "");
					}

					if (selectresult.get(i).get("createuserid") != null) {// "createuserid text,"+//������id
						map.put("createuserid",
								selectresult.get(i).get("createuserid")
										.toString());
					} else {
						map.put("createuserid", "");
					}

					if (selectresult.get(i).get("updatedate") != null) {// "updatedate text,"+//����ʱ��
						map.put("updatedate",
								selectresult.get(i).get("updatedate")
										.toString());
					} else {
						map.put("updatedate", "");
					}

					if (selectresult.get(i).get("buildid") != null) {// "buildid text,"+//����¥��id
						map.put("buildid", selectresult.get(i).get("buildid")
								.toString());
					} else {
						map.put("buildid", "");
					}

					if (selectresult.get(i).get("telephone") != null) {// "telephone text,"+//��ϵ�绰
						map.put("telephone",
								selectresult.get(i).get("telephone").toString());
					} else {
						map.put("telephone", "");
					}
					
					
					if(selectresult.get(i).get("imageid")!=null){
						name = MD5.getMD5(selectresult.get(i).get("imageid")
								.toString()
								+ "����ͼ.jpg")
								+ ".jpg";
					}
					if (selectresult.get(i).get("thumb_sdpath") != null) {
						file1 = new File(selectresult.get(i).get("thumb_sdpath")
								.toString());
						if (!(file1.exists() && file1.isFile())) {
							file1 = FileTools
									.getFile(getResources().getString(
													R.string.root_directory),
													getResources().getString(
															R.string.room_image), name);
						}
					}else{
						file1 = FileTools
								.getFile(
										getResources().getString(
												R.string.root_directory),
												getResources().getString(
														R.string.room_image), name);
					}
					if(selectresult.get(i).get("imageid")!=null){
						map.put("id", selectresult.get(i).get("imageid")
								.toString());
					}else{
						map.put("id", "");
					}
					map.put("file", file1);
					map.put("pixelswidth", (MyApplication.getInstance(this).getScreenWidth() / 3 - 10));
					map.put("pixelsheight", (MyApplication.getInstance(this).getScreenWidth() / 3 - 10));
					map.put("sql", "update soso_pushinfo set thumb_sdpath='"+file1.getAbsolutePath()+"' where officeid='"+selectresult.get(i).get("officeid")
							.toString()+"'");
					map.put("request_name", "ImageFileCutForCustom.aspx");
					
					
					if (layer.equals("")) {
						roomListForLinear.add(map);
					} else {
						roomList.add(map);
					}
				}
			}
		}
	}
	private String currentLayer = "";//��ǰ¥��
	private boolean floorFlag = false;//�����ж��Ƿ�Ϊ¥���б��еĿ���ʾͼ��
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == layerGv) {
			String layer = layerList.get(arg2);
			floorFlag = true;
			currentLayer = layer;
			getRoomListFromLayer(layer);
			layerNumber.setText(layer);
			notifiView();
			floorImg.setVisibility(View.VISIBLE);
			floorLayout.setOnClickListener(this);
		} else if (arg0 == roomLV) {
			MyApplication.getInstance().setOfficeid(roomListForLinear.get(arg2-1).get("officeid").toString());
			Intent intent = new Intent(this, DetailRoomInfoActivity.class);
			startActivity(intent);
		}
	}

	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		String updatedate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		updatedate = StringUtils.formatCalendar1(sdf.format(new Date()),
				MyApplication.error_value);
		roomLV.setRefreshTime(updatedate);
		// if (tag == 1||tag == 2) {
		new Thread(runnable).start();
		// }

	}

	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onLoadMore()
	 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

//	/**
//	 * ��ȡ����˵ķ�Դ��Ϣ
//	 * 
//	 * ���ߣ�Ring �����ڣ�2013-2-19
//	 */
//	public void getOfficeGetByState() {
//		// params ����Ĳ����б�
//		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
//				this).getAPPID()));
//		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
//				this).getAPPKEY()));
//		String whereclause = "";
//		whereclause = "CreateUserID=+"
//				+ MyApplication.getInstance(this).getSosouserinfo(this)
//						.getUserID() + " and BuildID=" + buildid;
//		params.add(new BasicNameValuePair("whereClause", whereclause));
//		params.add(new BasicNameValuePair("orderBy", "Floors"));
//		params.add(new BasicNameValuePair("pageIndex", "0"));
//		params.add(new BasicNameValuePair("pageLength", "100"));
//		uploaddata = new SoSoUploadData(this, "OfficeGetPaged.aspx", params);
//		uploaddata.Post();
//		reponse = uploaddata.getReponse();
//		dealReponse(MyApplication.getInstance(this).getSosouserinfo(this)
//				.getUserID());
//		params.clear();
//		params = null;
//	}
}
