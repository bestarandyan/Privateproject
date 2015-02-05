/**
 * 
 */
package com.qingfengweb.client.fragmengs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.qingfengweb.android.R;
import com.qingfengweb.client.activity.BaseActivity;
import com.qingfengweb.client.adapter.TeamAdapter;
import com.qingfengweb.client.adapter.ViewPagerAdapter;
import com.qingfengweb.client.bean.CompanyInfo;
import com.qingfengweb.client.bean.TeamInfo;
import com.qingfengweb.client.bean.UpdateSystemTime;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.JsonData;
import com.qingfengweb.client.database.DBHelper;
import com.qingfengweb.util.CustomListView;
import com.qingfengweb.util.CustomViewPager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author ������
 * @createdate 2013/6/8
 * ����������
 *
 */
@SuppressLint("NewApi")
public class AboutUsFragment extends Fragment implements OnClickListener{
	public static final String ARG_TEXT = "aboutusfragment_key";
	public static int pageValue = -1;//��ʾ����һҳ��ҳֵ
    @SuppressLint("NewApi")
	public static AboutUsFragment newInstance(String text) {
    	AboutUsFragment f = new AboutUsFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        f.setArguments(args);

        return f;
    }
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return viewGroup;
	}
	private ViewGroup viewGroup;
	private ArrayList<View> pageViews;  
	private ImageButton btn = null;
	public  CustomViewPager viewPager;
	private List<Map<String, Object>> teamList=new ArrayList<Map<String,Object>>();
	RelativeLayout teamLayout,cultureLayout,companyLayout;
	ImageView introIv,cultureIv,companyIv;
	TextView introTv,cultureTv,companyTv;
	DBHelper dbHelper = null;
	TextView companyContent;
	public String teamLastUpdateTime = "";//�Ŷ����ݵ�������ʱ��
	public String sysTime = "";//ϵͳ�ṩ��������ʱ��  ���ʱ�䲢��һ�����ڱ��ظ��¹���ʱ�䡣
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		findview();
		initData();
		
	}
	/**
	 * �򱾵����ݿ��ȡ��˾�������ݵ��߳�
	 */
	private Runnable getlocalRunnable = new Runnable() {
			
			@Override
			public void run() {
				String sql = "select * from "+CompanyInfo.TableName;
				List<Map<String,Object>> list = dbHelper.selectRow(sql, null);
				if(list!=null && list.size()>0){//�����ݿ��ȡ����ȷ����
					Message msg = new Message();
					msg.obj = list;
					msg.what = 3;
					handler.sendMessage(msg);
				}else{//���ݿ���ֵ
					handler.sendEmptyMessage(4);
				}
			}
		};
	/**
	 * ���������ȡ��˾�������ݵ��߳�
	 */
	private Runnable getServiceRunnable = new Runnable() {
			
			@Override
			public void run() {
				String msgStr = AccessServer.getContentDetail("7");
				if(AccessServer.isNoData(msgStr)){//����������������
					handler.sendEmptyMessage(1);
				}else if(msgStr.equals("404")){//���ʷ�����ʧ��
					handler.sendEmptyMessage(2);
				}else{//�ӷ������õ�����ȷ������
					ContentValues contentValues = new ContentValues();
					contentValues.put("content", msgStr);
					dbHelper.insert(CompanyInfo.TableName, contentValues);
					Message msg = new Message();
					msg.what = 5;
					msg.obj = msgStr;
					handler.sendMessage(msg);
				}
				
			}
		};
		/**
		 * �򱾵����ݿ��ȡ�Ŷӳ�Ա����
		 */
		Runnable getTeamLocalRunnable = new Runnable() {
			
			@Override
			public void run() {
				teamList = dbHelper.selectAllRows(TeamInfo.TableName, "*", null);
				//��ѯ���±��е�����Ϊ3�� ���Ŷӳ�Ա��������  �ж��ϴθ��µ�ʱ���ϵͳ���µ�ʱ���Ƿ���ȣ�
				String sql = "select * from "+UpdateSystemTime.tableName+" where type='3'";
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
				if(teamList!=null && teamList.size()>0){
					handler.sendEmptyMessage(0);//ˢ���б�
					//�����������̨�������ȥ�������ݡ�
					if(updateList!=null && updateList.size()>0){
						if(!localTime.equals(sysTime) || (localTime.equals("") && sysTime.equals(""))){////�����������̨�������ȥ�������ݡ�
							teamLastUpdateTime = localTime;//����������ʱ��Ϊ��һ�θ��µ�ʱ�䣬
							handler.sendEmptyMessage(6);//ȥ��������ȡ�Ŷӳ�Ա����
						}
					}
				}else{
					teamLastUpdateTime = "";//����������ʱ��Ϊ�գ���������ݿ�û���Ŷӳ�Ա���ݣ����ܸ��±��е�localTime��sysTime�Ƿ����  ��ȥ��������ȡ����
					handler.sendEmptyMessage(6);//û�����ݿ����߳�ȥ��������ȡ�Ŷӳ�Ա
				}
			}
		};
		/**
		 * ���������ȡ�Ŷӳ�Ա����
		 */
		Runnable getTeamRunnable = new Runnable() {
			
			@Override
			public void run() {
				String msgStr = AccessServer.getContents(teamLastUpdateTime, AccessServer.TEAMMEMBER_ACTION);
				if(AccessServer.isNoData(msgStr)){//������
					System.out.println("��ȡ�Ŷӳ�Ա����ʱ�����������������ݻ��߲������󡣷���ֵΪ��"+msgStr);
				}else if(msgStr.equals("404")){
					System.out.println("��ȡ�Ŷӳ�Ա����ʱ�����ʷ�����ʧ��");
				}else{
					System.out.println("��ȡ�Ŷӳ�Ա���ݳɹ���");
					JsonData.jsonTeamData(msgStr, getActivity(), dbHelper.open());
					teamList = dbHelper.selectAllRows(TeamInfo.TableName, "*", null);
					ContentValues contentValues = new ContentValues();
					contentValues.put("localTime", sysTime);
					dbHelper.update(UpdateSystemTime.tableName, contentValues, "type=?", new String[]{"3"});
					handler.sendEmptyMessage(0);
				}
				
			}
		};
		
		Handler handler = new Handler(){

			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				if(msg.what ==0){
					if(teamAdapter!=null){
						teamAdapter.notifyDataSetChanged();
					}else{
						notifyView();
					}		
				}else if(msg.what == 1){//����������������
					
				}else if(msg.what == 2){//���ʷ�����ʧ��
					
				}else if(msg.what == 3){//����������
					companyContent.setText(((List<Map<String,Object>>)msg.obj).get(0).get("content").toString());
				}else if(msg.what == 4){//����������  �������������ȡ���ݵ��̡߳�
					new Thread(getServiceRunnable).start();
				}else if(msg.what == 5){//�õ����ݵĴ��� �������صĺͷ�����������
					companyContent.setText(msg.obj.toString());
				}else if(msg.what == 6){//ȥ��������ȡ�Ŷӳ�Ա����
					new Thread(getTeamRunnable).start();
				}
				super.handleMessage(msg);
			}
			
		};
	private void findview(){
		viewGroup = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.a_aboutus, null);
		btn = (ImageButton) viewGroup.findViewById(R.id.LeftBtn);
		btn.setOnClickListener(this);
		viewPager = (CustomViewPager) viewGroup.findViewById(R.id.aboutUs_vp);
		teamLayout = (RelativeLayout) viewGroup.findViewById(R.id.teamLayout);
		cultureLayout = (RelativeLayout) viewGroup.findViewById(R.id.cultureLayout);
		companyLayout = (RelativeLayout) viewGroup.findViewById(R.id.companyLayout);
		companyLayout.setOnClickListener(this);
		teamLayout.setOnClickListener(this);
		companyLayout.setClickable(false);
		cultureLayout.setOnClickListener(this);
		companyIv = (ImageView) viewGroup.findViewById(R.id.companyIv);
		companyTv = (TextView) viewGroup.findViewById(R.id.companyTv);
		introIv = (ImageView) viewGroup.findViewById(R.id.introTeamIv);
		cultureIv = (ImageView) viewGroup.findViewById(R.id.cultureIv);
		introTv = (TextView) viewGroup.findViewById(R.id.introTeamTv);
		cultureTv = (TextView) viewGroup.findViewById(R.id.cultureTv);
		
	}
	int oldx = 0;
	int nowx = 0;
	private void notifyPager(){
		viewPager.setAdapter(new ViewPagerAdapter(pageViews));  
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());  
	}
	private void initData(){
		dbHelper = DBHelper.getInstance(getActivity());
		pageViews = new ArrayList<View>();  
		pageViews.add(getCompanyView());
        pageViews.add(getListView());
        pageViews.add(getCultureView());
        teamList.clear();
        notifyPager();
        new Thread(getlocalRunnable).start();//��ȡ��˾���
		new Thread(getTeamLocalRunnable).start();//��ȡ�Ŷӳ�Ա
	}
	
    // ָ��ҳ������¼�������
    class GuidePageChangeListener implements OnPageChangeListener {  
    	  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
        	if(arg0 == 0){
        		showCompanyLayout();
//        		CustomViewAbove.mEnabled = false;
        		CustomViewAbove.currentPage =0;
        		pageValue = 0;
        	}else if(arg0 == 1){
        		showTeamLayout();
//        		CustomViewAbove.mEnabled = false;
        		CustomViewAbove.currentPage =1;
        		pageValue = 1;
        	}else if(arg0 == 2){
        		showCultureLayout();
//        		CustomViewAbove.mEnabled = false;
        		CustomViewAbove.currentPage =2;
        		pageValue = 2;
        	}
        }  
    }  
	int oldX = 0;
	int newX = 0;
	ListView teamLv = null;
	TeamAdapter teamAdapter = null;
	/**
	 * ��ȡ�Ŷӽ��ܲ���
	 * @return
	 */
	private View getListView(){
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.aboutus_team, null);
		 teamLv = (ListView) view.findViewById(R.id.teamListView);
		return view;
	}
	/**
	 * ˢ���ŶӲ���
	 */
	private void notifyView(){
		teamAdapter = new TeamAdapter(getActivity(), teamList);
		teamLv.setAdapter(teamAdapter);
	}
	/**
	 *  ��ȡ��˾������
	 * @return
	 */
	private View getCompanyView(){
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.company_intro, null);
		companyContent = (TextView) view.findViewById(R.id.introTv);
		return view;
	}
	/**
	 * ��ȡ����ҵ�Ļ�����
	 * @return
	 */
	private View getCultureView(){
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.aboutus_culture, null);		
		return view;
	}
	@Override
	public void onClick(View v) {
		if(v == btn){
			BaseActivity.sm.showMenu();
		}else if(v == companyLayout){//�����˾�����ת����˾���ҳ
			showCompanyLayout();
			viewPager.setCurrentItem(0, true);
		}else if(v == teamLayout){//�л����Ŷӽ��ܽ���
			showTeamLayout();
			viewPager.setCurrentItem(1, true);
		}else if (v == cultureLayout) {//�л�����ҵ�Ļ�����
			showCultureLayout();
			viewPager.setCurrentItem(2, true);
		}
		    
	}
	private void showCompanyLayout(){
		companyIv.setVisibility(View.VISIBLE);
		introIv.setVisibility(View.INVISIBLE);
		cultureIv.setVisibility(View.INVISIBLE);
		companyTv.setTextColor(Color.WHITE);
		introTv.setTextColor(getResources().getColor(R.color.team_text_2));
		cultureTv.setTextColor(getResources().getColor(R.color.team_text_2));
		companyLayout.setClickable(false);
		teamLayout.setClickable(true);
		cultureLayout.setClickable(true);
	}
	private void showTeamLayout(){
		companyIv.setVisibility(View.INVISIBLE);
		introIv.setVisibility(View.VISIBLE);
		cultureIv.setVisibility(View.INVISIBLE);
		companyTv.setTextColor(getResources().getColor(R.color.team_text_2));
		introTv.setTextColor(Color.WHITE);
		cultureTv.setTextColor(getResources().getColor(R.color.team_text_2));
		companyLayout.setClickable(true);
		teamLayout.setClickable(false);
		cultureLayout.setClickable(true);
	}
	
	private void showCultureLayout(){
		companyIv.setVisibility(View.INVISIBLE);
		introIv.setVisibility(View.INVISIBLE);
		cultureIv.setVisibility(View.VISIBLE);
		companyTv.setTextColor(getResources().getColor(R.color.team_text_2));
		introTv.setTextColor(getResources().getColor(R.color.team_text_2));
		cultureTv.setTextColor(Color.WHITE);
		companyLayout.setClickable(true);
		teamLayout.setClickable(true);
		cultureLayout.setClickable(false);
	}
	
}

















