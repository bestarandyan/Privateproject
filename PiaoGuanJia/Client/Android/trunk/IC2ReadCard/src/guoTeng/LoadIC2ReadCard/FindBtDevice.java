package guoTeng.LoadIC2ReadCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FindBtDevice extends Activity  {
	private ListView listView;
	private BluetoothAdapter btAdapt; 	
	MyAdapter adapter = new MyAdapter();
	private ProgressDialog progress ;
	private Button back,refresh;
	private boolean registered = false;
	private ArrayList<DeviceInfo> mDeviceInfoList = new ArrayList<DeviceInfo>();
	private Thread refreshThread;
//	private BluetoothAdapter mAdapter;
    protected void onDestroy() {
        super.onDestroy();  
        if(registered == true){
			btAdapt.cancelDiscovery();
		    unregisterReceiver(searchDevices);
		}
    }  	
    
    private BroadcastReceiver searchDevices = new BroadcastReceiver() {   
        public void onReceive(Context context, Intent intent) {   
            String action = intent.getAction();     
            // �����豸ʱ��ȡ���豸��MAC��ַ   
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {   
                BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                //int iState = btDevice.getBondState();
                //if (iState == BluetoothDevice.BOND_NONE || iState == BluetoothDevice.BOND_BONDING) {  
                	String s = null;
                	if(btDevice.getName()!=null && btDevice.getName().length()>0 && !btDevice.getName().equals("")){
                		s = btDevice.getName();
                	}else{
                		s="error";
                	}
                	if (s.indexOf("GoldTel") == 0){
                		mDeviceInfoList.add(new DeviceInfo(btDevice.getName(), btDevice.getAddress(), true));                		
                		Collections.sort(mDeviceInfoList, new ComparatorValues());
                		adapter.notifyDataSetChanged();
                	}else{
                		//Toast.makeText(FindBtDevice.this, "δ֪�豸["+ s + "]", 2000).show(); 
                	}
                //}                 
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { 
            	if (mDeviceInfoList.size() == 0){
	            	Message msg = new Message();
	            	msg.what = 0;
	            	myHandler.sendMessage(msg);
            	}else{ 
            		Message msg = new Message();
                	msg.what = 1;
                	myHandler.sendMessage(msg);
            	}
            	
            	btAdapt.cancelDiscovery();
            }
        }   
    };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			setContentView(R.layout.other);
        }else{
        	setContentView(R.layout.portrite_other);
        }
		//mAdapter= BluetoothAdapter.getDefaultAdapter(); 

		back = (Button) findViewById(R.id.back);
		btAdapt = BluetoothAdapter.getDefaultAdapter();// ��ʼ��������������   
		progress = new ProgressDialog(FindBtDevice.this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == KeyEvent.KEYCODE_BACK){
					if(registered == true){
						btAdapt.cancelDiscovery();
						unregisterReceiver(searchDevices);
						registered = false;
					}
					progress.dismiss();
					refreshThread.interrupt();
				}
				return false;
			}
		});
		if (btAdapt == null) {
			dialogWronFun("δ���������豸",FindBtDevice.this);
			return;
		}
        if (btAdapt.getState() == BluetoothAdapter.STATE_OFF){// ������   
        	openB();
        }
        
           
		back.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 if (btAdapt.getState() == BluetoothAdapter.STATE_OFF){// ������   
			        	openB();
			        }else{
						if(registered == true){
							btAdapt.cancelDiscovery();
						    unregisterReceiver(searchDevices);
						    registered = false;
						}
						Intent intent = new Intent(FindBtDevice.this,LoadIC2ReadCard.class);
						intent.putExtra("activity_flag", 1);
						startActivity(intent);
						finish();
					        }
					}
		});
		refresh = (Button) findViewById(R.id.refresh);
		refresh.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ע��Receiver����ȡ�����豸��صĽ�� 
				if (btAdapt.getState() == BluetoothAdapter.STATE_OFF){// ������   
		        	openB();
		        }else{
						mDeviceInfoList.clear();
				        progress.setMessage("����ɨ�������豸�����Ժ򡣡���");
				        progress.show();
				        refreshThread = new Thread(myRunnable);
				        refreshThread.start();
		        }
			}
		});
		listView = (ListView) this.findViewById(R.id.list);
		listView.setCacheColorHint(0);
        Object[] lstDevice = btAdapt.getBondedDevices().toArray();   
        for (int i = 0; i < lstDevice.length; i++) {   
            BluetoothDevice btDevice=(BluetoothDevice)lstDevice[i];    
            mDeviceInfoList.add(new DeviceInfo(btDevice.getName(), btDevice.getAddress(), true));
        }
        listView.setAdapter(adapter);
     // ע��Receiver����ȡ�����豸��صĽ��   
        /*if(mDeviceInfoList.size() ==0 ){
        	progress = new ProgressDialog(FindBtDevice.this);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage("����ɨ�������豸�����Ժ򡣡���");
            progress.show();
            Thread thread = new Thread(myRunnable);
            thread.start();
        }*/
        
	}
	Runnable benRunnable = new Runnable(){

		public void run() {
			// TODO Auto-generated method stub
			while(!btAdapt.isEnabled()){
				btAdapt.enable();   
			}
			Message msg = new Message();
			msg.what = 2;
			myHandler.sendMessage(msg);
		}
		
	};
	public void openB(){
      	AlertDialog.Builder alert=new AlertDialog.Builder(FindBtDevice.this);
      	alert.setMessage("���������豸δ�������뿪����");
      	alert.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					 	progress.setMessage("���ڴ򿪱��������豸�����Ժ򡣡���");
				        progress.show();
				        Thread thread = new Thread(benRunnable);
				        thread.start();
				}
			}).create().show();
      }
	Runnable myRunnable  = new Runnable(){

		public void run() {
			// TODO Auto-generated method stub
			    IntentFilter intent = new IntentFilter();   
		        intent.addAction(BluetoothDevice.ACTION_FOUND);   
		        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); 
		        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		        registerReceiver(searchDevices, intent);
		        registered = true;
				btAdapt.startDiscovery(); 
		}
		
	};
	Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 0:
				progress.dismiss();
				setTitle("�����豸�������,δ���ֶ����豸");
				break;
			case 1:
				progress.dismiss();
				setTitle("�����豸�������");
				break;
			case 2:
				progress.dismiss();
				dialogWronFun1("���������Ѵ򿪣�������ɨ�����������豸������",FindBtDevice.this);
			}
			super.handleMessage(msg);
		}
		
	};
	private class MyAdapter extends BaseAdapter{
		private int temp = -1;

		public int getCount() {
		// TODO Auto-generated method stub
			return mDeviceInfoList.size();
		}
	
		public Object getItem(int position) {
			return mDeviceInfoList.get(position);
		}
	
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = FindBtDevice.this.getLayoutInflater().inflate(R.layout.item, null); //����취�� ÿ�ζ����»�ȡView
			TextView tv = (TextView) convertView.findViewById(R.id.tv);
			
			DeviceInfo info = mDeviceInfoList.get(position); //��������ݣ�ģ�ͣ� 
			if (info != null) {  
				tv.setText(info.name + "\n" + info.address);
				RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.radioButton);
				
				radioButton.setId(position); //��position��ΪradioButton��id				
				radioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {					
						if(isChecked){
							//��δ�����ʵ�ֵ�ѡ����
							if(temp != -1){
								RadioButton tempButton = (RadioButton) FindBtDevice.this.findViewById(temp);
								if(tempButton != null){
									tempButton.setChecked(false);
								}						
							}
								
							temp = buttonView.getId();
							
							DeviceInfo info = mDeviceInfoList.get(temp); //��������ݣ�ģ�ͣ� 
					    	SharedPreferences sp = getSharedPreferences("BindDevice", Activity.MODE_PRIVATE);
				        	SharedPreferences.Editor edit = sp.edit();	        	
				        	edit.putString("Name", info.name);
				        	edit.putString("Address", info.address);
				        	edit.commit();	
				        	
							//Toast.makeText(FindBtDevice.this, "�ѽ�["+ info.name + "]��Ϊ�����豸", 2000).show(); 
							//Log.i("test","you are women- - " + isChecked + " " + temp);
						}
					}
				});
				
	    		SharedPreferences sp = getSharedPreferences("BindDevice", Activity.MODE_PRIVATE);
	    		String szAddress = sp.getString("Address", "");	
	    		
				if (info.address.equalsIgnoreCase(szAddress) && !radioButton.isChecked()){
					radioButton.setChecked(true);
				}
					
				//����ʵ�ֵ�ѡ��ѡ�Ļ��ԣ�����˵�ѡ���Ƴ���Ļ��Χδѡ��״̬
				if(temp == position){
					radioButton.setChecked(true);
					/*
			    	SharedPreferences sp = getSharedPreferences("BindDevice", Activity.MODE_PRIVATE);
		        	SharedPreferences.Editor edit = sp.edit();	        	
		        	edit.putString("Name", info.name);
		        	edit.putString("Address", info.address);
		        	edit.commit();			
		        	*/		
				}
			}
			return convertView;
		}
	}
	public void dialogWronFun(CharSequence str,Context context){
      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
      	alert.setMessage(str);
      	alert.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			}).create().show();
      }
	public void dialogWronFun1(CharSequence str,Context context){
      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
      	alert.setMessage(str);
      	alert.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					mDeviceInfoList.clear();
				    progress.setMessage("����ɨ�������豸�����Ժ򡣡���");
				    progress.show();
				    Thread thread = new Thread(myRunnable);
				    thread.start();
				}
			}).create().show();
      }
	public class DeviceInfo {
	    public String name;  //�ļ�·��
	    public String address;  //�ļ���
	    public boolean bonded;  //�Ƿ�ѡ��
	    public DeviceInfo(String name,String address,boolean bonded) {  
	        this.name = name;  
	        this.address = address;  
	        this.bonded=bonded;
	    }   
	}
	
	public static final class ComparatorValues implements Comparator<DeviceInfo>{        
		public int compare(DeviceInfo o1, DeviceInfo o2) {                    
			return o1.name.compareTo(o2.name);       
		}                    
	}	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			 if (btAdapt.getState() == BluetoothAdapter.STATE_OFF){// ������   
		        	openB();
		        }else{
					if(registered == true){
						btAdapt.cancelDiscovery();
					    unregisterReceiver(searchDevices);
					    registered = false;
					}
						Intent intent = new Intent(FindBtDevice.this,LoadIC2ReadCard.class);
						intent.putExtra("activity_flag", 1);
						startActivity(intent);
						this.finish();
		        }
			break;
		case KeyEvent.KEYCODE_MENU:return true;
		case KeyEvent.KEYCODE_HOME:return true;
		case KeyEvent.KEYCODE_CALL:return true;
		case KeyEvent.KEYCODE_SYM: return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
		case KeyEvent.KEYCODE_VOLUME_UP: return true;
		case KeyEvent.KEYCODE_STAR: return true;
		case KeyEvent.KEYCODE_CAMERA: return true;
		case KeyEvent.KEYCODE_POWER: return true;
		}
		return super.onKeyDown(keyCode, event);
	}   
	

	/*@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onAttachedToWindow();
	}*/
}
