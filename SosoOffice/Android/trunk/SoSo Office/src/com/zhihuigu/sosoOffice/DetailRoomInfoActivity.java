/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.DetailRoominfoImageGalleryAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.OfficeType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoImageInfo;
import com.zhihuigu.sosoOffice.model.SoSoOfficeInfo;
import com.zhihuigu.sosoOffice.model.SoSoUserInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.BitmapCache;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.FileTools;
import com.zhihuigu.sosoOffice.utils.ImageDownloaderId;
import com.zhihuigu.sosoOffice.utils.ImageDownloaderUrl;
import com.zhihuigu.sosoOffice.utils.MD5;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

/**
 * @author ������
 * @createDate 2013/1/26 ��Դ������
 * 
 */
public class DetailRoomInfoActivity extends BaseActivity implements
		Activity_interface, OnGestureListener {

//	private ViewFlipper viewFlipper;// ͼƬԤ��ҳ
//	private GestureDetector detector;// �ؼ��Ĵ����¼�����
	private LinearLayout bottomLinear;// �ײ�СͼƬ����
	ImageView image;// ��ԴͼƬ
	private Button backBtn;// ���ؼ�
	private TextView roomNameT;// ��Դ����
	private LinearLayout connectionYeZhuLayout;// ��ϵҵ��
	private LinearLayout collectRoomLayout;// �ղط�Դ
	private TextView baoJia;// ����
	private TextView danJia;// ����
	private TextView wuye;// ��ҵ��
	private TextView roomType;// ����
	private TextView acreage;// ���
	private TextView layer;// ¥��
	private TextView roomNumber;// �����
	private TextView address;// ��ַ
	private TextView jianJie;// ���
	private Gallery gallery;//��ԴͼƬ

	private SoSoUploadData uploaddata;// �������������
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private ProgressDialog progressdialog;
	private String officeid = "";// ��Դid
	private String createuserid = "";// ������Դ���û�id
	private String createusername = "";// ������Դ���û�����
//	private ImageDownloaderId imageDownloader = null;
	private ArrayList<HashMap<String, Object>> list;// ��ԴͼƬ�б�����ͼ
	private ArrayList<HashMap<String, Object>> list2;// ��ԴͼƬ�б��ͼ
	private int tag = 1;//1ִ�л�ȡ��Դ����ͷ�ԴͼƬ�б������0ִ���ղط�Դ������
//	private LinearLayout btnLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailroominfo);
//		imageDownloader = new ImageDownloaderId(this,10);
		officeid = MyApplication.getInstance().getOfficeid();
		findView();
		initData();
		initView();
		new Thread(runnable).start();
	}

	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		roomNameT = (TextView) findViewById(R.id.loupanNameText);
		connectionYeZhuLayout = (LinearLayout) findViewById(R.id.connectionYeZhu);
		collectRoomLayout = (LinearLayout) findViewById(R.id.shoucangLayout);
		baoJia = (TextView) findViewById(R.id.baojiaText);
		danJia = (TextView) findViewById(R.id.danjiaText);
		wuye = (TextView) findViewById(R.id.wuyeText);
		roomType = (TextView) findViewById(R.id.typeText);
		acreage = (TextView) findViewById(R.id.acreageText);
		layer = (TextView) findViewById(R.id.layerText);
		roomNumber = (TextView) findViewById(R.id.roomNumberText);
		address = (TextView) findViewById(R.id.addressText);
		jianJie = (TextView) findViewById(R.id.jianJieText);
//		viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
		bottomLinear = (LinearLayout) findViewById(R.id.bottomGunLinear);
		gallery = (Gallery) findViewById(R.id.gallery);
//		btnLayout = (LinearLayout) findViewById(R.id.btnLayout);
	}
	
	public String getaddressText(String buildid,String buildmc) {
		StringBuffer address = new StringBuffer("");
		if (buildid != null && !buildid.equals("")) {
			String sql = "select areamc,districtmc,buildmc from soso_buildinfo where buildid='"
					+ buildid + "'";
			List<Map<String, Object>> selectresult = DBHelper.getInstance(this)
					.selectRow(sql, null);
			if (selectresult != null && selectresult.size() > 0) {
				address.delete(0, address.length());
//				address.append(selectresult.get(selectresult.size() - 1).get(
//						"areamc"));
//				address.append(selectresult.get(selectresult.size() - 1).get(
//						"districtmc"));
				address.append(selectresult.get(selectresult.size() - 1).get(
						"buildmc"));
			}else{
				address.append(buildmc);
			}
		}
		return address.toString();
	}
	
	/* (non-Javadoc)
		 * @see com.zhihuigu.sosoOffice.BaseActivity#onResume()
		 */
		@Override
		protected void onResume() {
			initView();
			super.onResume();
		}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			CommonUtils.hideSoftKeyboard(this);
			if(!MyApplication.getInstance().isCollectRoom()){
				MyApplication.getInstance().setCollectRoom(true);
			}
			MyApplication.getInstance().setRoomManagerFlag(2);
			finish();
		} else if (v == connectionYeZhuLayout) {
			if((MyApplication.getInstance().getSosouserinfo(this)==null
					||MyApplication.getInstance().getSosouserinfo(this).getUserID()==null)){
				return;
			}
			if(!createuserid.equals("")
					&&!createusername.equals("")){
				Bundle bundle = new Bundle();
				bundle.putString("shoujianrenid", createuserid);
				bundle.putString("shoujianren", createusername);
				bundle.putString("theme", "");
				bundle.putString("content", "");
				bundle.putString("WhoUserID", "");
				Intent intent  = new Intent(DetailRoomInfoActivity.this,WriteNewLetterActivity.class);
				intent.putExtra("bundle", bundle);
				DetailRoomInfoActivity.this.startActivity(intent);
			}else{
				Toast.makeText(this, "���û�������", Toast.LENGTH_SHORT).show();
			}
		} else if (v == collectRoomLayout) {
			if((MyApplication.getInstance().getSosouserinfo(this)==null
					||MyApplication.getInstance().getSosouserinfo(this).getUserID()==null)){
				return;
			}
			tag = 0;//ִ���ղط�Դ������
			new Thread(runnable).start();
		}
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(!MyApplication.getInstance().isCollectRoom()){
				 MyApplication.getInstance().setCollectRoom(true);
			}
			 MyApplication.getInstance().setRoomManagerFlag(2);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void initView() {
		backBtn.setOnClickListener(this);
		connectionYeZhuLayout.setOnClickListener(this);
		collectRoomLayout.setOnClickListener(this);
		switch(MyApplication.getInstance(this).getRoleid()){
		case Constant.TYPE_AGENCY://�н�
			if(!MyApplication.getInstance().isCollectRoom()){//�����ǲ�������ʾ�ղط�Դ�Ŀؼ�  
				collectRoomLayout.setVisibility(View.GONE);
//				MarginLayoutParams mp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  //item�Ŀ��
//		        mp.setMargins(0, 0, 10, 0);//�ֱ���margin_top���ĸ�����
//		        LayoutParams lp = new LayoutParams(mp);
//		        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		        lp.addRule(RelativeLayout.CENTER_VERTICAL);
//		        connectionYeZhuLayout.setLayoutParams(lp);
			}
			break;
		case Constant.TYPE_CLIENT:////�ͻ�
			if(!MyApplication.getInstance().isCollectRoom()){//�����ǲ�������ʾ�ղط�Դ�Ŀؼ�  
				collectRoomLayout.setVisibility(View.GONE);
//				 MarginLayoutParams mp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  //item�Ŀ��
//			        mp.setMargins(0, 0, 10, 0);//�ֱ���margin_top���ĸ�����
//			        LayoutParams lp = new LayoutParams(mp);
//			        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			        lp.addRule(RelativeLayout.CENTER_VERTICAL);
//			        connectionYeZhuLayout.setLayoutParams(lp);
			}
			break;
		case Constant.TYPE_PROPRIETOR://ҵ��
//			btnLayout.setVisibility(View.GONE);
			connectionYeZhuLayout.setVisibility(View.GONE);
			collectRoomLayout.setVisibility(View.GONE);
			break;
		}
		gallery.setOnItemClickListener(new GalleryItemListener());
		gallery.setOnItemSelectedListener(new GalleryItemSelectListener());
	}
	/**
	 * 
	 * @author ������
	 * @createDate 2013/3/20
	 * ��Դ�������ķ�ԴͼƬ����¼�
	 *
	 */
	class GalleryItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

//			Toast.makeText(DetailRoomInfoActivity.this, ""+v.getId(), 3000).show();
			File file = (File) list.get(position).get("file");
			if (file.exists()) {// �ж�ͼƬ�Ƿ����
//				Intent intent = new Intent(DetailRoomInfoActivity.this,
//						ImagePreViewActivity.class);
//				intent.putExtra("map", list2.get(position));
//				startActivityForResult(intent, 6);
				
				ArrayList<String> al = new ArrayList<String>();
				al.clear();
				al.add(file.getAbsolutePath());
				Intent it = new Intent(DetailRoomInfoActivity.this,
						ImageSwitcher.class);
				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				it.putStringArrayListExtra("pathes", al);
				it.putExtra("map", list2.get(position));
				it.putExtra("index", 0);
				startActivity(it);
			} else {// ͼƬ������ ��ɾ���б��е�ͼƬ ��ˢ�¿ؼ�
				Toast.makeText(DetailRoomInfoActivity.this, "ͼƬ�����ڣ�", 5000)
						.show();
//				roomImgList.remove(arg2);
//				notifiView();
			}
		}
	}
	/**
	 * ��Դ������ͼƬѡ��ʱ�������¼�
	 * @author ������
	 *@createDate 2013/3/20
	 */
	class GalleryItemSelectListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			for (int i = 0; i < list.size(); i++) {
				if(bottomLinear.getChildAt(i)!=null){
					if (i == position) {
						bottomLinear.getChildAt(i).setBackgroundResource(
								R.drawable.stores_dot_on);
					} else {
						bottomLinear.getChildAt(i).setBackgroundResource(
								R.drawable.stores_dot);
					}
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
		
	}
	@Override
	public void initData() {
		List<Map<String, Object>> selectresult = DBHelper.getInstance(
				DetailRoomInfoActivity.this).selectRow(
				"select * from soso_imageinfo where xgid = '" + officeid
						+ "'", null);
		if(list==null){
			list = new ArrayList<HashMap<String,Object>>();
		}
		list.clear();
		if(list2==null){
			list2 = new ArrayList<HashMap<String,Object>>();
		}
		list2.clear();
		if (selectresult != null && selectresult.size() > 0) {
			int i;
			HashMap<String, Object> map = null;
			String name1 = "";//����ͼ����
			String name2 = "";//��ͼ����
			File file1 = null;//����ͼ�ļ�
			File file2 = null;//��ͼ�ļ�
			for(i=0;i<selectresult.size();i++){
				if(selectresult.get(i).get("imageid")!=null
						&&!selectresult.get(i).get("imageid").toString().equals("")){
					if(selectresult.get(i).get("imageid")!=null){
						name1 = MD5.getMD5(selectresult.get(i).get("imageid")
								.toString()
								+ "����ͼ.jpg")
								+ ".jpg";
						name2 = MD5.getMD5(selectresult.get(i).get("imageid")
								.toString()
								+ ".jpg")
								+ ".jpg";
					}
					if (selectresult.get(i).get("xiaotusd") != null) {
						file1 = new File(selectresult.get(i).get("xiaotusd")
								.toString());
						if (!(file1.exists() && file1.isFile())) {
							file1 = FileTools
									.getFile(getResources().getString(
													R.string.root_directory),
													getResources().getString(
															R.string.room_image), name1);
						}
					}else{
						file1 = FileTools
								.getFile(
										getResources().getString(
												R.string.root_directory),
												getResources().getString(
														R.string.room_image), name1);
					}
					if (selectresult.get(i).get("datusd") != null) {
						file2 = new File(selectresult.get(i).get("datusd")
								.toString());
						if (!(file2.exists() && file2.isFile())) {
							file2 = FileTools
									.getFile(getResources().getString(
													R.string.root_directory),
													getResources().getString(
															R.string.room_image), name2);
						}
					}else{
						file2 = FileTools
								.getFile(
										getResources().getString(
												R.string.root_directory),
												getResources().getString(
														R.string.room_image), name2);
					}
					map = new HashMap<String, Object>();
					if(selectresult.get(i).get("imageid")!=null){
						map.put("id", selectresult.get(i).get("imageid")
								.toString());
					}else{
						map.put("id", "");
					}
					map.put("file", file1);
					map.put("pixelswidth", (MyApplication.getInstance(this).getScreenWidth()/3-10));
					map.put("pixelsheight", (MyApplication.getInstance(this).getScreenWidth()/3-10));
					map.put("sql", "update soso_imageinfo set xiaotusd='"+file1.getAbsolutePath()+"' where imageid='"+selectresult.get(i).get("imageid")
							.toString()+"'");
					map.put("request_name", "ImageFileCutForCustom.aspx");
					
					list.add(map);
					
					//---------------------------------------------//
					map = new HashMap<String, Object>();
					if(selectresult.get(i).get("imageid")!=null){
						map.put("id", selectresult.get(i).get("imageid")
								.toString());
					}else{
						map.put("id", "");
					}
					map.put("file", file2);
					map.put("pixelswidth", (MyApplication.getInstance(this).getScreenWidth()));
					map.put("pixelsheight", (MyApplication.getInstance(this).getScreenHeight()));
					map.put("sql", "update soso_imageinfo set datusd='"+file2.getAbsolutePath()+"' where imageid='"+selectresult.get(i).get("imageid")
							.toString()+"'");
					map.put("request_name", "ImageFileDownload.aspx");
					
					list2.add(map);
				}
			}
		}
//		detector = new GestureDetector(this);
		bottomLinear.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
//			viewFlipper.addView(addView(i));
//			viewFlipper.getChildAt(i).setId(i);
			if(list.size()>1){//��ͼƬ����2��ʱ��û��Ҫ���С��ť��
				bottomLinear.addView(addBottomImageView());
				bottomLinear.getChildAt(i).setBackgroundResource(
						R.drawable.stores_dot);
			}
			
		}
		if (bottomLinear.getChildCount() > 0) {
			bottomLinear.getChildAt(0).setBackgroundResource(
					R.drawable.stores_dot_on);
		}

		List<Map<String, Object>> selectresult1 = DBHelper.getInstance(
				DetailRoomInfoActivity.this).selectRow(
				"select * from soso_officeinfo where officeid = '" + officeid
						+ "'", null);
		if (selectresult1 != null && selectresult1.size() > 0) {
			// if(selectresult1.get(selectresult1.size()-1).get("")==null){
			// baoJia.setText("0");
			// }else{
			// baoJia.setText(selectresult1.get(selectresult1.size()-1).get("").toString());
			// }
			double price_danjia = 0;
			double price_wuye = 0 ;
			double price_baojia = 0 ;
			double area_total = 0 ;
			if (selectresult1.get(selectresult1.size() - 1).get("priceup") != null) {
				danJia.setText(selectresult1.get(selectresult1.size() - 1)
						.get("priceup").toString());
				try {
					price_danjia = Double.parseDouble(selectresult1
							.get(selectresult1.size() - 1).get("priceup")
							.toString());
				} catch (Exception e) {
				}
			} else {
				danJia.setText("0");
			}
			if (selectresult1.get(selectresult1.size() - 1).get(
					"wymanagementfees") != null) {
				wuye.setText(selectresult1.get(selectresult1.size() - 1)
						.get("wymanagementfees").toString());
				try {
					price_wuye = Double.parseDouble(selectresult1
							.get(selectresult1.size() - 1).get("wymanagementfees")
							.toString());
				} catch (Exception e) {
				}
			} else {
				wuye.setText("0");
			}
			if (selectresult1.get(selectresult1.size() - 1).get("areaup") != null) {
				acreage.setText(selectresult1.get(selectresult1.size() - 1)
						.get("areaup").toString());
				try {
					area_total = Double.parseDouble(selectresult1
							.get(selectresult1.size() - 1).get("areaup")
							.toString());
				} catch (Exception e) {
				}
			} else {
				acreage.setText("0");
			}
			price_baojia = price_danjia*area_total*30+price_wuye*area_total;
			baoJia.setText(price_baojia+"");
			if (selectresult1.get(selectresult1.size() - 1).get(
					"createuserid") != null) {
				createuserid=selectresult1.get(selectresult1.size() - 1)
						.get("createuserid").toString();
			} else {
				createuserid="";
			}
			if (selectresult1.get(selectresult1.size() - 1).get(
					"createusername") != null) {
				createusername=selectresult1.get(selectresult1.size() - 1)
						.get("createusername").toString();
			} else {
				createusername="";
			}
			if (selectresult1.get(selectresult1.size() - 1).get("officetype") != null) {
				for (OfficeType accidentType : OfficeType.values()) {
					try {
						int officetype = Integer.parseInt(selectresult1
								.get(selectresult1.size() - 1).get("officetype")
								.toString());
						if (accidentType.getValue() == officetype) {
							roomType.setText(accidentType.getName());
							break;
						}
					} catch (Exception e) {
					}
				}
			} else {
				roomType.setText("");
			}
			if (selectresult1.get(selectresult1.size() - 1).get("floors") != null) {
				layer.setText(selectresult1.get(selectresult1.size() - 1)
						.get("floors").toString());
			} else {
				layer.setText("0");
			}
			if (selectresult1.get(selectresult1.size() - 1).get("officemc") != null) {
				roomNumber.setText(selectresult1.get(selectresult1.size() - 1)
						.get("officemc").toString());
			} else {
				roomNumber.setText("");
			}
			if (selectresult1.get(selectresult1.size() - 1).get("address") != null) {
				address.setText(selectresult1.get(selectresult1.size() - 1)
						.get("address").toString());
			} else {
				
				address.setText("");
				address.setVisibility(View.GONE);
			}
			if (selectresult1.get(selectresult1.size() - 1).get("fyjj") != null) {
//				jianJie.setText("��Դ��飺"+selectresult1.get(selectresult1.size() - 1)
//						.get("fyjj").toString());
				final String sText = "<font color=\"#666666\">��Դ��飺</font>"+selectresult1.get(selectresult1.size() - 1)
						.get("fyjj").toString();
				jianJie.setText(Html.fromHtml(sText, null, null));
			} else {
				jianJie.setText("");
			}
			if (selectresult1.get(selectresult1.size() - 1).get("buildid") != null
					&&selectresult1.get(selectresult1.size() - 1).get("buildmc") != null) {
				String titleStr = getaddressText(selectresult1.get(selectresult1.size() - 1).get("buildid").toString(),
						selectresult1.get(selectresult1.size() - 1).get("buildmc").toString());
				/*if(connectionYeZhuLayout.getVisibility() == View.GONE && collectRoomLayout.getVisibility() == View.GONE){
					if(titleStr!=null && titleStr.length()>9){
						titleStr = titleStr.substring(0, 9)+"...";
					}
				}*/
				roomNameT.setText(titleStr);
			} else {
				roomNameT.setText("");
			}
			
		}
		notifiView();
		// private TextView baoJia;//����
		// private TextView danJia;//����
		// private TextView wuye;//��ҵ��
		// private TextView roomType;//����
		// private TextView acreage;//���
		// private TextView layer;//¥��
		// private TextView roomNumber;//�����
		// private TextView address;//��ַ
		// private TextView jianJie;//���

	}

	/**
	 * �õ�һ���ײ���СͼƬ��ť ��̬���
	 * 
	 * @return
	 */
	private ImageView addBottomImageView() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 0, 0, 0);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(lp);
		return imageView;
	}

	@Override
	public void notifiView() {
		DetailRoominfoImageGalleryAdapter adapter = new DetailRoominfoImageGalleryAdapter(this, list);
		gallery.setAdapter(adapter);
		
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {/*
		if(list.size()<=1){
			return false;
		}
		if (e1.getX() - e2.getX() > 120) {
			this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			this.viewFlipper.showNext();
			for (int i = 0; i < list.size(); i++) {
				if (i == this.viewFlipper.getCurrentView().getId()) {
					bottomLinear.getChildAt(i).setBackgroundResource(
							R.drawable.stores_dot_on);
				} else {
					bottomLinear.getChildAt(i).setBackgroundResource(
							R.drawable.stores_dot);
				}
			}
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			this.viewFlipper.showPrevious();
			for (int i = 0; i < list.size(); i++) {
				if (i == this.viewFlipper.getCurrentView().getId()) {
					bottomLinear.getChildAt(i).setBackgroundResource(
							R.drawable.stores_dot_on);
				} else {
					bottomLinear.getChildAt(i).setBackgroundResource(
							R.drawable.stores_dot);
				}
			}
			return true;
		}*/
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { return
	 * this.detector.onTouchEvent(event); }
	 */
//	/**
//	 * ����list�����±�����ҳ��
//	 * 
//	 * @param position
//	 *            list���±�
//	 * @return
//	 */
//	private View addView(int position) {
//		image = new ImageView(this);
//		image.setId(position);
//		boolean b = true;
//		File file = (File) list.get(position).get("file");
//		if(file.exists()&&file.isFile()){
//			Bitmap bitmap = BitmapCache.getInstance().getBitmap(file, this);
////			//�жϷ�ԴͼƬ�Ƿ���ֻ��ֱ��ʴ�С��ͬ
////			if(bitmap.getWidth() == MyApplication.getInstance().getScreenWidth() && bitmap.getHeight() == MyApplication.getInstance().getScreenHeight()){
////				bitmap = CommonUtils.cutImg(bitmap, 0, Integer.parseInt(list.get(position).get("pixelsheight").toString())/3,Integer.parseInt(list.get(position)
////						.get("pixelswidth").toString()),Integer.parseInt(list.get(position).get("pixelsheight").toString())/3);
////			}else{//����ͬ
////				if(bitmap.getWidth()>MyApplication.getInstance().getScreenWidth()){//����ڷֱ��ʵĿ�
////					if(bitmap.getHeight()>(MyApplication.getInstance().getScreenHeight()/3)){//�߶ȴ��ڷֱ��ʵ�
////						bitmap = CommonUtils.cutImg(bitmap, 0, 0,MyApplication.getInstance().getScreenWidth(),MyApplication.getInstance().getScreenHeight()/3);
////					}else{
////						bitmap = CommonUtils.cutImg(bitmap, 0, 0,MyApplication.getInstance().getScreenWidth(),bitmap.getHeight());
////					}
////				}else{
////					if(bitmap.getHeight()>(MyApplication.getInstance().getScreenHeight()/3)){
////						bitmap = CommonUtils.cutImg(bitmap, 0, 0,bitmap.getWidth(),MyApplication.getInstance().getScreenHeight()/3);
////					}else{
////						bitmap = CommonUtils.cutImg(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight());
////					}
////				}
////				
////			}
////			
//			if(bitmap!=null){
//				image.setImageBitmap(bitmap);
//				b=false;
//			}
//		}
//		if(b){
//			MyApplication.getInstance(this).getImageDownloaderId().download((File) list.get(position).get("file"), list
//					.get(position).get("sql").toString(),
//					list.get(position).get("id").toString(), list.get(position)
//							.get("pixelswidth").toString(),
//					list.get(position).get("pixelsheight").toString(),
//					list.get(position).get("request_name").toString(),
//					image,"0", "0","150","150");
//		}
//		image.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				Toast.makeText(DetailRoomInfoActivity.this, ""+v.getId(), 3000).show();
//				File file = (File) list.get(v.getId()).get("file");
//				if (file.exists()) {// �ж�ͼƬ�Ƿ����
//					Intent intent = new Intent(DetailRoomInfoActivity.this,
//							ImagePreViewActivity.class);
//					intent.putExtra("path", file.getAbsolutePath());
//					startActivityForResult(intent, 6);
//				} else {// ͼƬ������ ��ɾ���б��е�ͼƬ ��ˢ�¿ؼ�
//					Toast.makeText(DetailRoomInfoActivity.this, "ͼƬ�����ڣ�", 5000)
//							.show();
////					roomImgList.remove(arg2);
////					notifiView();
//				}
//			}
//		});
//		/*image.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				return DetailRoomInfoActivity.this.detector.onTouchEvent(event);
//			}
//		});*/
//		return image;
//	}

	/**
	 * ��ȡ��Դ������Ϣ
	 * 
	 * ���ߣ�Ring �����ڣ�2013-2-19
	 */
	public void getOfficeDetail() {
		// params ����Ĳ����б�
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("OfficeID", officeid));
		uploaddata = new SoSoUploadData(this, "OfficeSelect.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse();
		params.clear();
		params = null;
	}

	/**
	 * �����������Ӧֵ�������صķ�Դ������Ϣ��������
	 * 
	 * �����ڣ�2013-2-19
	 * 
	 */
	private void dealReponse() {
		if (StringUtils.CheckReponse(reponse)) {
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			SoSoOfficeInfo sosoofficeinfo = null;
			try {
				sosoofficeinfo = gson.fromJson(reponse, SoSoOfficeInfo.class);// ����json����
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sosoofficeinfo != null && sosoofficeinfo.getOfficeID() != null) {
				values.put("officeid", sosoofficeinfo.getOfficeID());
				values.put("createusername", sosoofficeinfo.getCreateUserName());
				values.put("createuserid", sosoofficeinfo.getCreateUserID());
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
				values.put("officestate", sosoofficeinfo.getOfficeState());
				values.put("officestatus", "");
				values.put("isprice", "");
				values.put("offadddate", "");
				values.put("roomrate", sosoofficeinfo.getRoomRate());
				values.put("nextalertdate", "");

				if (DBHelper
						.getInstance(DetailRoomInfoActivity.this)
						.selectRow(
								"select * from soso_officeinfo where officeid = '"
										+ sosoofficeinfo.getOfficeID() + "'",
								null).size() <= 0) {
					DBHelper.getInstance(DetailRoomInfoActivity.this).insert(
							"soso_officeinfo", values);
				} else {
					DBHelper.getInstance(DetailRoomInfoActivity.this).update(
							"soso_officeinfo", values, "officeid = ?",
							new String[] { sosoofficeinfo.getOfficeID() });
				}
			}
		}
	}
/**
 * ��ȡ��ԴͼƬid
 *
 * ���ߣ�Ring
 * �����ڣ�2013-2-20
 */
	public void getofficeimages() {
		// params ����Ĳ����б�
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("XgID", officeid));
		params.add(new BasicNameValuePair("ImageType", ""));//��ֵ�����ȡȫ��ͼƬ
		uploaddata = new SoSoUploadData(this, "ImageSelect.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse1();
		params.clear();
		params = null;
	}

	/**
	 * �����������Ӧֵ�������ص�ͼƬ������Ϣ��������
	 * 
	 * �����ڣ�2013-2-19
	 * 
	 */
	private void dealReponse1() {
		if (StringUtils.CheckReponse(reponse)) {
			Type listType = new TypeToken<LinkedList<SoSoImageInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoImageInfo> soSoImageInfos = null;
			ContentValues values = new ContentValues();
			SoSoImageInfo soSoImageInfo = null;
			soSoImageInfos = gson.fromJson(reponse, listType);
			if (soSoImageInfos != null && soSoImageInfos.size() > 0) {
				for (Iterator<SoSoImageInfo> iterator = soSoImageInfos
						.iterator(); iterator.hasNext();) {
					soSoImageInfo = (SoSoImageInfo) iterator.next();
					if (soSoImageInfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("soso_imageinfo",
								"imageid = ?", new String[] { soSoImageInfo
								.getImageID() });
						continue;
					}
					if (soSoImageInfo != null
							&& soSoImageInfo.getImageID() != null) {
						values.put("imageid", soSoImageInfo.getImageID());
						values.put("xgid", soSoImageInfo.getXgID());
						values.put("datuurl", "http://114.141.181.215:99/uploadedFiles/office/300@300/"+soSoImageInfo.getUrl());
						values.put("xiaotuurl", "http://114.141.181.215:99/uploadedFiles/office/70@60/"+soSoImageInfo.getUrl());
						values.put("xiaotusd", "");
						values.put("imagetype", "0");
						if (DBHelper
								.getInstance(DetailRoomInfoActivity.this)
								.selectRow(
										"select * from soso_imageinfo where imageid = '"
												+ soSoImageInfo
												.getImageID() + "'", null).size() <= 0) {
							DBHelper.getInstance(DetailRoomInfoActivity.this)
									.insert("soso_imageinfo", values);
						} else {
							DBHelper.getInstance(DetailRoomInfoActivity.this)
									.update("soso_imageinfo",
											values,
											"imageid = ?",
											new String[] { soSoImageInfo
													.getImageID() });
						}
					}
				}
				if (soSoImageInfos != null) {
					soSoImageInfos.clear();
					soSoImageInfos = null;
				}

				if (values != null) {
					values.clear();
					values = null;
				}
				DBHelper.getInstance(this).close();
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
			if (NetworkCheck.IsHaveInternet(DetailRoomInfoActivity.this)) {
				if(tag==1){
					handler.sendEmptyMessage(5);// ����������
					getOfficeDetail();
					getofficeimages();
					handler.sendEmptyMessage(6);// �رս�����
					handler.sendEmptyMessage(1);// ˢ���б�
				}else if(tag==0){
					boolean b = false;
					handler.sendEmptyMessage(5);// ����������
					b = collectRoom();
					handler.sendEmptyMessage(6);// �رս�����
					if(b){
						handler.sendEmptyMessage(7);// ��Դ�ղسɹ�
					}else{
						handler.sendEmptyMessage(3);// ��Դ�ղ�ʧ��
					}
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
			case 1:// ����ˢ������
				initData();
				break;
			case 2:// �ӵ�¼������ת��ע�����
				break;
			case 3:// ��Դ�ղ�ʧ��
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				}else if (StringUtils.getErrorState(reponse).equals(
						ErrorType.CollectAgain.getValue())) {
					errormsg = "�÷�Դ����ӹ���";
				}else {
					errormsg = getResources().getString(
							R.string.collectroom_failure);
				}
				MessageBox.CreateAlertDialog(errormsg,
						DetailRoomInfoActivity.this);
				break;
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(DetailRoomInfoActivity.this);
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(DetailRoomInfoActivity.this);
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
			case 7:// ��Դ�ղسɹ�
				MessageBox.CreateAlertDialog(getResources().getString(
						R.string.collectroom_success),
						DetailRoomInfoActivity.this);
				break;
			case 8:// ��Դ�ղ�ʧ��
				MessageBox.CreateAlertDialog(getResources().getString(
						R.string.collectroom_failure),
						DetailRoomInfoActivity.this);
				break;
			}
		};
	};
	
	/**
	 * �ղط�Դ
	 *
	 * ���ߣ�Ring
	 * �����ڣ�2013-2-21
	 * @return
	 */
	public boolean collectRoom(){
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UserID", MyApplication.getInstance(this).getSosouserinfo(this).getUserID()));
		params.add(new BasicNameValuePair("OfficeID", officeid));//��Դid
		params.add(new BasicNameValuePair("Type", "1"));//�ղ�����1��Դ�ղأ�2�û��ղ�
		params.add(new BasicNameValuePair("GroupID", "0"));//����id
		uploaddata = new SoSoUploadData(this, "FavoritesAdd.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
//		dealReponse2();
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
//	/**
//	 * �����ղط��������ص�ֵ
//	 *
//	 * ���ߣ�Ring
//	 * �����ڣ�2013-2-21
//	 */
//	public void dealReponse2(){
//		if (StringUtils.CheckReponse(reponse)) {
//			
//		} 
//	}

}
