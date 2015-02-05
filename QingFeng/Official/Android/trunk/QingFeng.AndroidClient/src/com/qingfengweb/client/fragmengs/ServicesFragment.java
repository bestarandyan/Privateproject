/**
 * 
 */
package com.qingfengweb.client.fragmengs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.qingfengweb.android.R;
import com.qingfengweb.android.R.layout;
import com.qingfengweb.client.activity.BaseActivity;
import com.qingfengweb.client.activity.DetailServiceActivity;
import com.qingfengweb.client.activity.SubmitDemandActivity;
import com.qingfengweb.client.bean.ServicesInfo;
import com.qingfengweb.client.bean.TeamInfo;
import com.qingfengweb.client.bean.UpdateSystemTime;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.JsonData;
import com.qingfengweb.client.database.DBHelper;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * @author ������
 * @createDate 2013��6��14
 * ������Ŀ��
 */
public class ServicesFragment extends Fragment implements OnClickListener{
	public static String ARG_TEXT = "services";
	public static ServicesFragment newInstance(String text) {
		ServicesFragment f = new ServicesFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_TEXT, text);
		f.setArguments(bundle);
		return f;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return view;
	}
	private View view;
	private ImageButton openButton;
	private LinearLayout iosLayout,layout1,layout2,layout3,layout4,layout5,layout6;
	
	public String servicesLastUpdateTime = "";//�Ŷ����ݵ�������ʱ��
	public String sysTime = "";//ϵͳ�ṩ��������ʱ��  ���ʱ�䲢��һ�����ڱ��ظ��¹���ʱ�䡣
	private List<Map<String, Object>> servicesList=new ArrayList<Map<String,Object>>();
	private DBHelper dbHelper = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		findview();
		dbHelper = DBHelper.getInstance(getActivity());
		new Thread(getServicesLocalRunnable).start();//�����̻߳�ȡ������Ŀ����
	}
	/**
	 * �򱾵����ݿ��ȡ�Ŷӳ�Ա����
	 */
	Runnable getServicesLocalRunnable = new Runnable() {
		
		@Override
		public void run() {
			servicesList = dbHelper.selectAllRows(ServicesInfo.tableName, "*", null);
			//��ѯ���±��е�����Ϊ3�� ���Ŷӳ�Ա��������  �ж��ϴθ��µ�ʱ���ϵͳ���µ�ʱ���Ƿ���ȣ�
			String sql = "select * from "+UpdateSystemTime.tableName+" where type='1'";
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
			if(servicesList!=null && servicesList.size()>0){
//				handler.sendEmptyMessage(0);//ˢ���б�
				//�����������̨�������ȥ�������ݡ�
				if(updateList!=null && updateList.size()>0){
					if(!localTime.equals(sysTime) || (localTime.equals("") && sysTime.equals(""))){////�����������̨�������ȥ�������ݡ�
						servicesLastUpdateTime = localTime;//����������ʱ��Ϊ��һ�θ��µ�ʱ�䣬
						handler.sendEmptyMessage(0);//ȥ��������ȡ����
					}
				}
			}else{
				servicesLastUpdateTime = "";//����������ʱ��Ϊ�գ���������ݿ�û���Ŷӳ�Ա���ݣ����ܸ��±��е�localTime��sysTime�Ƿ����  ��ȥ��������ȡ����
				handler.sendEmptyMessage(0);//û�����ݿ����߳�ȥ��������ȡ�Ŷӳ�Ա
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				new Thread(getServicesRunnable).start();//�����̻߳�ȡ������Ŀ����
			}
			super.handleMessage(msg);
		}
		
	};
	/**
	 * ��ȡ������Ŀ�����߳�
	 */
	Runnable getServicesRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msgStr = AccessServer.getContents(servicesLastUpdateTime, AccessServer.LIST_ACTION);
			System.out.println("��ȡ������Ŀ���صĲ���ֵ ===="+msgStr);
			if(AccessServer.isNoData(msgStr)){//������
				System.out.println("��ȡ������Ŀ����ʱ�����������������ݻ��߲������󡣷���ֵΪ��"+msgStr);
			}else if(msgStr.equals("404")){
				System.out.println("��ȡ������Ŀ����ʱ�����ʷ�����ʧ��");
			}else{
				System.out.println("��ȡ������Ŀ���ݳɹ���");
				JsonData.jsonServicesInfoData(msgStr, getActivity(), dbHelper.open());
				servicesList = dbHelper.selectAllRows(ServicesInfo.tableName, "*", null);
				ContentValues contentValues = new ContentValues();
				contentValues.put("localTime", sysTime);
				dbHelper.update(UpdateSystemTime.tableName, contentValues, "type=?", new String[]{"1"});
//				handler.sendEmptyMessage(0);
			}
		}
	};
	private void findview(){
		view = LayoutInflater.from(getActivity()).inflate(R.layout.a_services, null);
		openButton = (ImageButton) view.findViewById(R.id.leftBtn);
		openButton.setOnClickListener(this);
		iosLayout = (LinearLayout) view.findViewById(R.id.iosLayout);
		iosLayout.setOnClickListener(this);
		layout1 = (LinearLayout) view.findViewById(R.id.layout1);
		layout2 = (LinearLayout) view.findViewById(R.id.layout2);
		layout3 = (LinearLayout) view.findViewById(R.id.layout3);
		layout4 = (LinearLayout) view.findViewById(R.id.layout4);
		layout5 = (LinearLayout) view.findViewById(R.id.layout5);
		layout6 = (LinearLayout) view.findViewById(R.id.layout6);
		layout1.setOnClickListener(this);
		layout2.setOnClickListener(this);
		layout3.setOnClickListener(this);
		layout4.setOnClickListener(this);
		layout5.setOnClickListener(this);
		layout6.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if(v == openButton){
			BaseActivity.sm.showMenu();
		}else if(v == iosLayout){
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "1");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);

		}else if (v == layout1) {
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "2");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == layout2) {
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "3");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == layout3) {
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "4");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == layout4) {
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "5");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == layout5) {
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "6");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == layout6) {
			Intent intent = new Intent(getActivity(),SubmitDemandActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}
		
	}

}
