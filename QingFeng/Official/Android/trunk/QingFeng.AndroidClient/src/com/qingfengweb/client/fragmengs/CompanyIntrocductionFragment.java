/**
 * 
 */
package com.qingfengweb.client.fragmengs;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.qingfengweb.android.R;
import com.qingfengweb.client.activity.BaseActivity;
import com.qingfengweb.client.activity.DetailCompanyIntroActivity;
import com.qingfengweb.client.activity.DetailServiceActivity;
import com.qingfengweb.client.activity.SubmitDemandActivity;
import com.qingfengweb.client.adapter.ViewPagerAdapter;
import com.qingfengweb.client.bean.ContentUpdateInfo;
import com.qingfengweb.client.bean.HomeImageInfo;
import com.qingfengweb.client.bean.UpdateSystemTime;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.FinalValues;
import com.qingfengweb.client.data.JsonData;
import com.qingfengweb.client.data.MyApplication;
import com.qingfengweb.client.database.DBHelper;
import com.qingfengweb.filedownload.CallbackImpl;
import com.qingfengweb.filedownload.FileUtils;
import com.qingfengweb.filedownload.ImageLoadFromUrlOrId;

/**
 * @author ������
 * @createDate 2013/6/8
 *
 */
@SuppressLint("NewApi")
public class CompanyIntrocductionFragment extends Fragment implements View.OnClickListener{
	 private static final String ARG_TEXT = "net.simonvt.menudrawer.samples.SampleFragment.text12";
	 View view = null;
	 ImageButton leftButton;//�򿪲˵���ť
	 ImageButton rightButton;//���ڰ�ť
	 LinearLayout pointsLayout;
	 ViewPager imagePager;
	 private ArrayList<View> pageViews = new ArrayList<View>();
	 EditText introView;//��˾���
	 LinearLayout item1Layout,item2Layout,item3Layout,item4Layout,item5Layout,item6Layout,item7Layout;//������Ŀ
//	 private ArrayList<String> photoList = new ArrayList<String>();//ͼƬ����
	 private DBHelper dbHelper;
	 List<Map<String, Object>> contentList;//��ʾ��˾�������ݡ�
	 ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
	 public String imgIdLastUpdateTime = "";//�Ŷ����ݵ�������ʱ��
		public String sysTime = "";//ϵͳ�ṩ��������ʱ��  ���ʱ�䲢��һ�����ڱ��ظ��¹���ʱ�䡣
     public static CompanyIntrocductionFragment newInstance(String text) {
    	 CompanyIntrocductionFragment f = new CompanyIntrocductionFragment();

         Bundle args = new Bundle();
         args.putString(ARG_TEXT, text);
         f.setArguments(args);

         return f;
     }
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LayoutInflater.from(getActivity()).inflate(R.layout.a_main, null);
		findview();
		initData();
	}
	
	private void initData(){
		dbHelper = DBHelper.getInstance(getActivity());
		pageViews.clear();
		new Thread(getContentInfoRunnable).start();//��ȡ�����б�������ݵ��߳�
		new Thread(getImageIdFromLocalRunnable).start();//��ȡ��ҳͼƬ���߳�
	}
	Runnable getContentInfoRunnable = new Runnable() {
		@Override
		public void run() {
			String msg = AccessServer.getContents("", AccessServer.LIST_ACTION);
			if(AccessServer.isNoData(msg)){//������
				
			}else if(msg.equals("404")){//���ʷ�����ʧ��
				
			}else{//���ʷ������ɹ���������
				JsonData.jsonContentUpdateData(msg, getActivity(), dbHelper.open());
				String sql = "select * from "+ContentUpdateInfo.tableName+" where type = 7";
				contentList = dbHelper.selectRow(sql, null);
				handler.sendEmptyMessage(4);
			}
		}
	};
	/**
	 * �ӱ��ز�ѯͼƬid����
	 */
	Runnable getImageIdFromLocalRunnable = new Runnable() {
		
		@Override
		public void run() {
			List<Map<String, Object>> list = dbHelper.selectAllRows(HomeImageInfo.TableName, "*", null);
			//��ѯ���±��е�����Ϊ3�� ���Ŷӳ�Ա��������  �ж��ϴθ��µ�ʱ���ϵͳ���µ�ʱ���Ƿ���ȣ�
			String sql = "select * from "+UpdateSystemTime.tableName+" where type='2'";
			List<Map<String,Object>> updateList = dbHelper.selectRow(sql, null);
			String localTime = "";
			if(updateList!=null && updateList.size()>0){
				if(updateList.get(0).get("localtime")!=null && updateList.get(0).get("localtime").toString().length()>0){
					localTime = updateList.get(0).get("localtime").toString();
				}
				if(updateList.get(0).get("systime")!=null && updateList.get(0).get("systime").toString().length()>0){
					sysTime = updateList.get(0).get("systime").toString();
				}
			}
			if(list!=null && list.size()>0){//����������ݿ���ֵ
				Message msg1 = new Message();
				msg1.obj = list;
				msg1.what = 3;
				handler.sendMessage(msg1);
				
				//�����������̨�������ȥ�������ݡ�
				if(updateList!=null && updateList.size()>0){
					if(!localTime.equals(sysTime) || (localTime.equals("") && sysTime.equals(""))){////�����������̨�������ȥ�������ݡ�
						imgIdLastUpdateTime = localTime;//����������ʱ��Ϊ��һ�θ��µ�ʱ�䣬
						handler.sendEmptyMessage(1);//����ϵͳ���±��е������ж��Ŷӳ�Ա�����Ƿ��и���
					}
				}
			}else{
				imgIdLastUpdateTime = "";//����������ʱ��Ϊ�գ���������ݿ�û���Ŷӳ�Ա���ݣ����ܸ��±��е�localTime��sysTime�Ƿ����  ��ȥ��������ȡ����
				handler.sendEmptyMessage(1);
			}
		}
	};
	/**
	 * �ӷ���������ͼƬ��ID
	 */
	Runnable getImageIdFromServiceRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msgStr = AccessServer.getContents(imgIdLastUpdateTime, AccessServer.HOME_IMAGE_ACTION);
			if(AccessServer.isNoData(msgStr)){
				System.out.println("��ȡ��ҳͼƬID����ʱ�����������������ݻ��߲������󡣷���ֵΪ��"+msgStr);
				handler.sendEmptyMessage(1);//������
			}else if(msgStr.equals("404")){
				System.out.println("��ȡ��ҳͼƬID����ʱ�����ʷ�����ʧ��");
			}else{
				System.out.println("��ȡ��ҳͼƬID���ݳɹ�");
				JsonData.jsonHomeImageData(msgStr, getActivity(), dbHelper.open());
				List<Map<String, Object>> list = dbHelper.selectAllRows(HomeImageInfo.TableName, "*", null);
				if(list!=null && list.size()>0){
					ContentValues contentValues = new ContentValues();
					contentValues.put("localTime", sysTime);
					dbHelper.update(UpdateSystemTime.tableName, contentValues, "type=?", new String[]{"2"});
					Message msg1 = new Message();
					msg1.obj = list;
					msg1.what = 3;
					handler.sendMessage(msg1);
				}else{
					handler.sendEmptyMessage(2);
				}
			}
			
		}
	};
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//���ز�ѯ���˺ϸ������
				if(adapter!=null){
					adapter.notifyDataSetChanged();
				}else{
					notifyImagePager();
				}		
			}else if(msg.what == 1){//��ʼ�ӷ�������ȡͼƬ�ɣ�
				new Thread(getImageIdFromServiceRunnable).start();
			}else if(msg.what == 2){//�ӷ����ȡ����ʱ   ������
				Toast.makeText(getActivity(), "������", 3000).show();
			}else if(msg.what == 3){//ȡ��ͼƬid����ʾͼƬ�ؼ�����ʼ����ͼƬ��
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = (List<Map<String, Object>>) msg.obj;
				if(list!=null && list.size()>0){
					addPageView(list);//���ͼƬҳ��
					if(pageViews!=null && pageViews.size()>0){//���ͼƬ�ؼ���ӳɹ�
						handler.sendEmptyMessage(0);
					}else{//���ͼƬ�ؼ����ʧ��
						handler.sendEmptyMessage(2);
					}
				}else{
					handler.sendEmptyMessage(2);
				}
				
			}else if(msg.what ==4){//��ȡ��˾���ɹ�
				if(contentList!=null && contentList.size()>0){
					String content = contentList.get(0).get("summary").toString();
					if(content!=null && content.length()>0){
						introView.setText(content);
//						introView.setMovementMethod(ScrollingMovementMethod.getInstance());
					}
					
				}
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * ���ݵõ�����ҳͼƬ���ݼ������ͼƬҳ
	 * @param list װ��ͼƬ���ݵļ���
	 */
	private void addPageView(List<Map<String, Object>> list){
		pageViews.clear();
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			if(map!=null){
				ImageView imageView = new ImageView(getActivity());
				int id = Integer.parseInt(map.get("id").toString());
				File file = new File(FileUtils.SDPATH+FinalValues.HOME_IMG_URL+id+".png");
				if(file.exists()){//ͼƬ�ļ�����
					Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
					if(bitmap==null){//����ͼƬ���ɹ�
						file.delete();
						downImage(imageView,id);
					}else{
						int h = bitmap.getHeight();
						int w = bitmap.getWidth();
						int currentW = MyApplication.getInstant().getScreenW();
						int currentH = h*currentW/w;
						RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, currentH);
						imagePager.setLayoutParams(param);
						imageView.setImageBitmap(bitmap);
					}
				}else{//ͼƬ�ļ�������
					downImage(imageView,id);
				}
				pageViews.add(imageView);
				if(list.size()>1){//��ӱ�ʾͼƬ������С��
					pointsLayout.addView(getPoint());
				}
			}
		}
		if (pointsLayout.getChildCount()>0) {//�����������1  ��ѵ�һ��ͼƬ����
			pointsLayout.getChildAt(0).setBackgroundResource(R.drawable.qf_dot2);
		}
	}
	/**
	 * ����һ���µ�ImageView
	 * @param id
	 * @return
	 */
	private ImageView getImageView(int id){
		ImageView imageView = new ImageView(getActivity());
//		imageView.setImageResource(R.drawable.qf_photo);
		File file = new File(FileUtils.SDPATH+FinalValues.HOME_IMG_URL+id+".png");
		if(file.exists()){//ͼƬ�ļ�����
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			if(bitmap==null){//����ͼƬ���ɹ�
				file.delete();
				downImage(imageView,id);
			}else{
				imageView.setImageBitmap(bitmap);
			}
		}else{//ͼƬ�ļ�������
			downImage(imageView,id);
		}
		return imageView;
	}
	/**
	 * ����ͼƬ
	 * @param imageView
	 * @param id
	 */
	private void downImage(ImageView imageView,int id){
		//���ͼƬ�ڱ��ز����ڣ������id׼��ȥ���������ء�
		CallbackImpl callbackImpl = new CallbackImpl(getActivity(),imageView);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", AccessServer.APPID));
		list.add(new BasicNameValuePair("appkey", AccessServer.APPKEY));
		list.add(new BasicNameValuePair("action", AccessServer.DOWNLOAD_IMAGE_DETAIL_ACTION));
		list.add(new BasicNameValuePair("imageid", String.valueOf(id)));
		list.add(new BasicNameValuePair("type", "1"));
		imageLoad.loadImageFromId(getActivity(), AccessServer.CONTENT_INTERFACE,
				list, R.drawable.qf_photo, FinalValues.HOME_IMG_URL, callbackImpl,true,false,MyApplication.getInstant().getScreenW()+20,0,0);
	}
	/**
	 * �õ�һ����ʾͼƬ�����ĵ�
	 * @return
	 */
	private ImageView getPoint(){
		ImageView imageView = new ImageView(getActivity());
		imageView.setImageResource(R.drawable.qf_dot1);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 5, 0);
		imageView.setLayoutParams(params);
		return imageView;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		if(v == leftButton){//���Ʋ˵���ť
			BaseActivity.sm.showMenu();
		}else if (v == rightButton) {//���ڰ�ť�¼�
			CustomViewAbove.isHaveRight = true;
//			BaseActivity.sm.setBehindOffsetRes(R.dimen.slidingmenu_offset_right);
			BaseActivity.sm.showSecondaryMenu();
		}else if (v == introView) {//��˾���
			Intent intent = new Intent(getActivity(),DetailCompanyIntroActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item1Layout) {//ios
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "1");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item2Layout) {//android
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "2");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item3Layout) {//window
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "3");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item4Layout) {//html5
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "4");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item5Layout) {//html5
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "5");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item6Layout) {//html5
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "6");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item7Layout) {//html5
			Intent intent = new Intent(getActivity(),SubmitDemandActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}
		
	}
	private void findview(){
		leftButton = (ImageButton) view.findViewById(R.id.intro_topLeftBtn);
		rightButton = (ImageButton) view.findViewById(R.id.intro_topRightBtn);
		imagePager =  (ViewPager) view.findViewById(R.id.MainImagePager);
		pointsLayout = (LinearLayout) view.findViewById(R.id.pointLayout);
		introView = (EditText) view.findViewById(R.id.introTv);
		item1Layout = (LinearLayout) view.findViewById(R.id.layoutItem1);
		item2Layout = (LinearLayout) view.findViewById(R.id.layoutItem2);
		item3Layout = (LinearLayout) view.findViewById(R.id.layoutItem3);
		item4Layout = (LinearLayout) view.findViewById(R.id.layoutItem4);
		item5Layout = (LinearLayout) view.findViewById(R.id.layoutItem5);
		item6Layout = (LinearLayout) view.findViewById(R.id.layoutItem6);
		item7Layout = (LinearLayout) view.findViewById(R.id.layoutItem7);
		introView.setOnClickListener(this);
		item1Layout.setOnClickListener(this);
		item2Layout.setOnClickListener(this);
		item3Layout.setOnClickListener(this);
		item4Layout.setOnClickListener(this);
		item5Layout.setOnClickListener(this);
		item6Layout.setOnClickListener(this);
		item7Layout.setOnClickListener(this);
		leftButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
	}
	/**
	 * ˢ��ͼƬ
	 */
	int oldx = 0;
	int nowx = 0;
	ViewPagerAdapter adapter = null;
	private void notifyImagePager(){
		adapter = new ViewPagerAdapter(pageViews);
		imagePager.setAdapter(adapter);
		imagePager.setOnPageChangeListener(new ImagePagerChangListener());
		imagePager.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					oldx = (int) event.getX();
				}else if(event.getAction() == MotionEvent.ACTION_MOVE){
					nowx = (int) event.getX();
					if(imagePager.getCurrentItem() == 0){
						if(nowx>oldx){//��ָ���һ��� ���ʱ��Ӧ����Ҫ�ò�߲˵����ֵ�ʱ��
							CustomViewAbove.currentPage =-1;
						}else{//��ָ������ƶ� ���ʱ���ǹ���ͼƬ��ʱ��
							CustomViewAbove.currentPage =0;
						}
		        		
		        	}else if(imagePager.getCurrentItem() == pageViews.size()-1){
		        		CustomViewAbove.currentPage =2;
		        	}else{
		        		CustomViewAbove.currentPage =1;
		        	}
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					CustomViewAbove.currentPage =-1;
				}
				return false;
			}
		});
	}
	/**
	 * ͼƬ�л����ļ�����
	 * @author ������
	 * 
	 *
	 */
	class ImagePagerChangListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			for(int i=0;i<pointsLayout.getChildCount();i++){
				if(i == arg0){//����ǰҳ��С���ɫ
					pointsLayout.getChildAt(i).setBackgroundResource(R.drawable.qf_dot2);
				}else{
					pointsLayout.getChildAt(i).setBackgroundResource(R.drawable.qf_dot1);
				}
			}
		}
		
	}
}
