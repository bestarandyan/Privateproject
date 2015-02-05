package android.androidVNC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vnc.draw.activity.ExitApplication;
import com.vnc.draw.activity.MainActivity;

import android.androidVNC.ext.ConnectAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

public class AndroidVNCEntry extends Activity {
	ListView listview = null;
	ConnectAdapter conAdapter = null;
	public  VncDatabase database;
	public  ArrayList<ConnectionBean> connections=new ArrayList<ConnectionBean>();
	private ConnectionBean bean=null;
	public  ArrayList<ConnectionBean> connections1=new ArrayList<ConnectionBean>();
	List<Map<String,Object>> listitems = null;
	ArrayList<ConnectionBean> connectionMap=new ArrayList<ConnectionBean>();
	public Button btnadd = null;
	Map<String,Object> map =null;
	private List<Map<String,Object>> listitems1=null;
	public List<Map<String,Object>> getListItem(){
		 listitems1 = new ArrayList<Map<String,Object>>();
		if(connections.size() == 0){
			return null;
		}else{
			for(int i = 0 ; i<connections.size();i++){
				//过滤数据库中为空的数据
				if(connections.get(i).getAddress().trim().equals("")||connections.get(i).getAddress() == null){
					
				}else{
					map = new HashMap<String, Object>();
					map.put("nickname", connections.get(i).getNickname());
					map.put("address", connections.get(i).getAddress());
					map.put("password",connections.get(i).getPassword());
					map.put("port", connections.get(i).getPort());
					map.put("defaultconnect", connections.get(i).getdefaultconnection());
					map.put("colormodle", connections.get(i).getColorModel());
					System.out.println(connections.get(i).getdefaultconnection());
					listitems1.add(map);
					connections1.add(connections.get(i));
				}
			}
		}
		return listitems1;
	}
	private boolean isFlag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		//设置全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_ui);
		database = new VncDatabase(this);
		ConnectionBean.getAll(database.getReadableDatabase(), ConnectionBean.GEN_TABLE_NAME, connections, ConnectionBean.newInstance);
		Collections.sort(connections);
		connections.add(0, new ConnectionBean());
		ExitApplication.getInstance().context = AndroidVNCEntry.this;
		ExitApplication.getInstance().addActivity(AndroidVNCEntry.this);
		listitems = getListItem();
		listview = (ListView)findViewById(R.id.main_ui_listview);
		btnadd = (Button)findViewById(R.id.main_ui_btnadd);
		if(MyApplication.getInstance().ismIsSymbol()){
		for(int i=0;i<connections.size();i++){
			bean=connections.get(i);
			if(bean.getdefaultconnection()){
				isFlag=true;
				break ;
			}
		}
		}
		btnadd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("nickname", "");
				bundle.putString("address", "");
				bundle.putString("password", "");
				bundle.putInt("port", 5900);
				bundle.putInt("position", -1);
				Intent intent = new Intent();
				intent.putExtra("bundle_item", bundle);
				bundle.putString("colormodle", COLORMODEL.C256.nameString());
				intent.putExtra("flag_update", 2);
				intent.setClass(AndroidVNCEntry.this, InterActivity.class); // 跳转到修改页面的Activity
				AndroidVNCEntry.this.startActivity(intent);
				AndroidVNCEntry.this.finish();
			}
		});
		
		notificationAdapter(-1);
		MyApplication.getInstance().setConnections(connections1);
		MyApplication.getInstance().setDatabase(database);	
		if(isFlag){
			MyApplication.getInstance().setmIsSymbol(false);
		forwardActivity();
		}
		
	}
	public void forwardActivity(){
		new Thread(){
			public void run(){
		Intent intent = new Intent();
		intent.putExtra(VncConstants.CONNECTION,bean.Gen_getValues());
		intent.setClass(AndroidVNCEntry.this, VncCanvasActivity.class); // 跳转到修改页面的Activity
		AndroidVNCEntry.this.startActivity(intent);
		AndroidVNCEntry.this.finish();
			}
		}.start();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		database.close();
		super.onDestroy();
		
	}
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event)

	{ // TODO Auto-generated method stub

	  if(KeyEvent.KEYCODE_HOME==keyCode||KeyEvent.KEYCODE_BACK==keyCode)
	  
		  new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(R.string.system_out)
			.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
//						dismissDialog(0);
						AndroidVNCEntry.this.finish();
						if(com.vnc.draw.activity.MyApplication.getInstance().getMainActivity()!=null){
							com.vnc.draw.activity.MyApplication.getInstance().getMainActivity().finish();
						}
						ExitApplication.getInstance().showExitDialog(AndroidVNCEntry.this);
						android.os.Process.killProcess(android.os.Process.myPid());
						
					}
				}).setNegativeButton(R.string.no, null).show();

	     return true;

	  }

	 

	@Override

	 public void onAttachedToWindow()

	 { // TODO Auto-generated method stub

	    this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);

	    super.onAttachedToWindow();

	 }

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode==KeyEvent.KEYCODE_HOME&&event.getRepeatCount()==0){
//		new AlertDialog.Builder(this)
//		.setIcon(android.R.drawable.ic_dialog_alert)
//		.setTitle(R.string.system_out)
//		.setPositiveButton(R.string.yes,
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog,
//							int which) {
//						dismissDialog(0);
//						AndroidVNCEntry.this.finish();
//						
//					}
//				}).setNegativeButton(R.string.no, null).show();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
	/*
	 * 更新listview视图
	 */
	public void notificationAdapter(int position){
		
		if(position >= 0){
			listitems.remove(position);
		}
		conAdapter = new ConnectAdapter(AndroidVNCEntry.this, listitems,connections,connections1);
		listview.setAdapter(conAdapter);
	}
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		super.onBackPressed();
//		new AlertDialog.Builder(this)
//		.setIcon(android.R.drawable.ic_dialog_alert)
//		.setTitle(R.string.system_out)
//		.setPositiveButton(R.string.yes,
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog,
//							int which) {
//						dismissDialog(0);
//						AndroidVNCEntry.this.finish();
//					}
//				}).setNegativeButton(R.string.no, null).show();
//	}

	
}
