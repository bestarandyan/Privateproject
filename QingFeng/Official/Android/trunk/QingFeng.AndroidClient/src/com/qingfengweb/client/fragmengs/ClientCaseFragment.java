/**
 * 
 */
package com.qingfengweb.client.fragmengs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.qingfengweb.android.R;
import com.qingfengweb.client.activity.BaseActivity;
import com.qingfengweb.client.activity.DetailCaseActivity;
import com.qingfengweb.client.adapter.ClientCaseAdapter;
import com.qingfengweb.client.bean.CaseInfo;
import com.qingfengweb.client.bean.UpdateSystemTime;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.JsonData;
import com.qingfengweb.client.database.DBHelper;

/**
 * @author ������
 * @createDate 2013/6/17
 *
 */
public class ClientCaseFragment extends Fragment implements OnClickListener,OnItemClickListener{
	public static final String ARG_TEXT = "clientcase_key";
	public static ClientCaseFragment newInstance(String text) {
		ClientCaseFragment f = new ClientCaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        f.setArguments(args);
        return f;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return view;
	}
	private View view = null;
	private ListView caseListView;
	private ImageButton menuButton;
	public DBHelper dbHelper = null;
	public ImageButton refreshBtn;
	private List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	public String caseLastUpdateTime = "";//�Ŷ����ݵ�������ʱ��
	public String sysTime = "";//ϵͳ�ṩ��������ʱ��  ���ʱ�䲢��һ�����ڱ��ظ��¹���ʱ�䡣
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findview();
		initData();
	}
	
	private void findview(){
		view = LayoutInflater.from(getActivity()).inflate(R.layout.a_clientcase, null);
		caseListView = (ListView) view.findViewById(R.id.caseList);
		menuButton = (ImageButton) view.findViewById(R.id.clientcase_topLeftBtn);
		refreshBtn = (ImageButton) view.findViewById(R.id.topRightBtn);
		menuButton.setOnClickListener(this);
		caseListView.setOnItemClickListener(this);
		refreshBtn.setOnClickListener(this);
	}
	private void initData(){
		dbHelper = DBHelper.getInstance(getActivity());
		list.clear();
		new Thread(getLocalDataRunnable).start();
	}
	
	Runnable getLocalDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			list = dbHelper.selectAllRows(CaseInfo.TableName, "*", null);
			//��ѯ���±��е�����Ϊ4�� ���ͻ�������������  �ж��ϴθ��µ�ʱ���ϵͳ���µ�ʱ���Ƿ���ȣ�
			String sql = "select * from "+UpdateSystemTime.tableName+" where type='4'";
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
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(0);//����������  ˢ��������
				//�����������̨�������ȥ�������ݡ�
				if(updateList!=null && updateList.size()>0){
					if(!localTime.equals(sysTime) || (localTime.equals("") && sysTime.equals(""))){////�����������̨�������ȥ�������ݡ�
						caseLastUpdateTime = localTime;//����������ʱ��Ϊ��һ�θ��µ�ʱ�䣬
						handler.sendEmptyMessage(1);//����ϵͳ���±��е������ж������Ƿ��и���
					}
				}
			}else{
				caseLastUpdateTime = "";//����������ʱ��Ϊ�գ���������ݿ�û�����ݣ����ܸ��±��е�localTime��sysTime�Ƿ����  ��ȥ��������ȡ����
				handler.sendEmptyMessage(1);//���������� �����߳����������ȡ
			}
		}
	};
	Runnable getServicesRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = AccessServer.getContents(caseLastUpdateTime, AccessServer.CASE_ACTION);
			if(AccessServer.isNoData(msg)){
				System.out.println("��ȡ�ͻ���������ʱ�����������������ݻ��߲������󡣷���ֵΪ��"+msg);
			}else if(msg.equals("404")){
				System.out.println("��ȡ�ͻ���������ʱ�����ʷ�����ʧ��");
			}else{
				JsonData.jsonCaseInfoData(msg, getActivity(), dbHelper.open());
				list = dbHelper.selectAllRows(CaseInfo.TableName, "*", null);
				//����ϵͳ���±��е�ʱ��
				ContentValues contentValues = new ContentValues();
				contentValues.put("localTime", sysTime);
				dbHelper.update(UpdateSystemTime.tableName, contentValues, "type=?", new String[]{"4"});
				handler.sendEmptyMessage(0);
			}
			System.out.println(msg);
		}
	};
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//��ȡ���ݳɹ�
				if(adapter!=null){
					adapter.notifyDataSetChanged();
				}else{
					notifiView();
				}		
			}else if(msg.what == 1){//ȥ��������ȡ����
				new Thread(getServicesRunnable).start();
			}
			super.handleMessage(msg);
		}
		
	};
	ClientCaseAdapter adapter = null;
	private void notifiView(){
		 adapter = new ClientCaseAdapter(getActivity(), list);
		caseListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if(v == menuButton){
			BaseActivity.sm.showMenu();
		}else if(v == refreshBtn){
			new Thread(getServicesRunnable).start();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(getActivity(),DetailCaseActivity.class);
		intent.putExtra("id", list.get(arg2).get("id").toString());
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	}
}
