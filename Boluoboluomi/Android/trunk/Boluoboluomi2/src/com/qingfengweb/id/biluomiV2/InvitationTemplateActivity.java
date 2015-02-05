package com.qingfengweb.id.biluomiV2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.qingfengweb.adapter.InvitationTemplateAdapter;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.data.ZipUtils;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.InvitationBean;
import com.tencent.tauth.Tencent;

public class InvitationTemplateActivity extends BaseActivity{
	private Button backBtn;
	private GridView gv;
	public List<Map<String,Object>> list;
	String Currenttemplateid = "";
	String currentTemplateFile = "";
	private int shareType = Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
	private int mExtarFlag = 0x00;
	DBHelper dbHelper;
	ProgressDialog dialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_invatationtemplate);
		 findView();
		 initData();
//		 notifyGv();
		 
	}
	private void findView(){
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		gv = (GridView) findViewById(R.id.gv);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}
	
	private void showProgressDialog(){
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在下载请帖模板，请稍候...");
		dialog.show();
	}
	private void initData(){
		dbHelper = DBHelper.getInstance(this);
		list = new ArrayList<Map<String,Object>>();
		new Thread(getListDataRunnable).start();
//		int image[] ={R.drawable.invitation_tu1,R.drawable.invitation_tu2,R.drawable.invitation_tu3,R.drawable.invitation_tu4,
//				R.drawable.invitation_tu5,R.drawable.invitation_tu6,R.drawable.invitation_tu7,R.drawable.invitation_tu8,};
//		String text[] ={"结婚光荣","Yes,i do!","我们结婚啦","快乐人生","喜结良缘","我家有喜","甜甜蜜蜜","爱的航线"};
//		for(int i=0;i<image.length;i++){
//			HashMap<String,Object> map = new HashMap<String,Object>();
//			map.put("image", BitmapFactory.decodeResource(getResources(), image[i]));
//			map.put("title", text[i]);
//			list.add(map);
//		}
	}
	/**
	 * 获取电子请帖模板数据
	 */
	Runnable getListDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select * from "+InvitationBean.tbName+" where storeid = "+UserBeanInfo.getInstant().getCurrentStoreId();
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(1);
			}
			String  msg = RequestServerFromHttp.getInvitationList("");
			if(msg!=null && msg.length()>3 && msg.startsWith("[")){//获取到了有效的数据
				JsonData.jsonInvitationData(msg, dbHelper.open());
				List<Map<String,Object>> list1 = dbHelper.selectRow(sql, null);
				if(list1!=null && list1.size()>0 && !list.equals(list1)){
					list = list1;
					handler.sendEmptyMessage(1);
				}
			}else if(msg.equals("404")){//访问服务器失败
				
			}else if(JsonData.isNoData(msg)){//没数据
				
			}else{//获取数据失败
				
			}
		}
	};
	/**
	 *下载电子模板
	 */
	Runnable downLoadRunnable = new Runnable() {
		
		@Override
		public void run() {
			File file = new File(ConstantsValues.SDCARD_ROOT_PATH+"/"+ConstantsValues.INVITATION_IMG_URL+Currenttemplateid+".zip");
			if(!file.exists()){//如果本地文件不存在的话  就去网上下载
				int state = RequestServerFromHttp.downloadInvitation(InvitationTemplateActivity.this, Currenttemplateid, 
						ConstantsValues.INVITATION_IMG_URL.substring(1), Currenttemplateid+".zip");
				if(state==0){
					handler.sendEmptyMessage(2);
					return;
				}
			}
				try {
					ZipUtils.upZipFile(file, ConstantsValues.SDCARD_ROOT_PATH+"/"+ConstantsValues.INVITATION_IMG_URL);
					handler.sendEmptyMessage(0);
				} catch (ZipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				if(dialog!=null){
					dialog.dismiss();
				}
				Intent i = new Intent(InvitationTemplateActivity.this,TemplateInfoActivity.class);
				startActivity(i);
			}else if(msg.what == 1){//获取模板数据成功
				notifyGv();
			}else if(msg.what == 2){
				if(dialog!=null){
					dialog.dismiss();
				}
				Toast.makeText(getApplicationContext(), "模板不存在", 3000).show();
			}else if(msg.what == 3){
				
			}
			super.handleMessage(msg);
		}
		
	};
	private void notifyGv(){
		gv.setAdapter(new InvitationTemplateAdapter(this, list));
		gv.setOnItemClickListener(new GvListener());
	}
	
	class GvListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			showProgressDialog();
			Currenttemplateid = list.get(position).get("id").toString();
			MyApplication.getInstant().setTemplateid(Currenttemplateid);
			new Thread(downLoadRunnable).start();
		}
		
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}
		super.onClick(v);
	}
}
