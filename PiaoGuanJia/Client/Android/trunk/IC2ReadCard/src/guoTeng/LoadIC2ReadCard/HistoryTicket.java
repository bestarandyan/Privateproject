package guoTeng.LoadIC2ReadCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qingfengweb.content.DbContent;
import com.qingfengweb.db.ProjectDBHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryTicket extends Activity{
private ListView listView;
private Button backBtn;
private List<Map<String,String>> list;
private ProjectDBHelper dbHelper;
private SQLiteDatabase database;
private EditText et;
private Button search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
			setContentView(R.layout.history_ticket);
		findView();
		initData();
		ExitApplication.getInstance().addActivity(HistoryTicket.this);
		mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
	}
	private Handler mHandler = new Handler();
    public void disableHomeKey()
	   {
	   this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	   }
	   Runnable mDisableHomeKeyRunnable = new Runnable() {

		   public void run() {
		   disableHomeKey();

		   }
		   };
		   
	@Override
	protected void onResume() {
		mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
		super.onResume();
	}
	private void initData(){
		list = new ArrayList<Map<String,String>>();
		dbHelper  = new ProjectDBHelper(HistoryTicket.this);
		database = dbHelper.getReadableDatabase();
		Cursor c = database.query(DbContent.HISTORY_TICKET, null, null, null, null, null, "testingTime desc");
		while(c.moveToNext()){
			Map<String,String > map = new HashMap<String,String>();
			map.put("orderid", c.getString(c.getColumnIndex("orderid")));
			map.put("ordertime", c.getString(c.getColumnIndex("ordertime")));
			map.put("username", c.getString(c.getColumnIndex("username")));
			map.put("tickettype", c.getString(c.getColumnIndex("tickettype")));
			map.put("ordercount", c.getString(c.getColumnIndex("ordercount")));
			map.put("createtime", c.getString(c.getColumnIndex("createtime")));
			map.put("idnumber", c.getString(c.getColumnIndex("idnumber")));
			map.put("phonenumber", c.getString(c.getColumnIndex("phonenumber")));
			map.put("scenicname", c.getString(c.getColumnIndex("scenicname")));
			map.put("testingTime", c.getString(c.getColumnIndex("testingTime")));
			list.add(map);
			}
		c.close();
		database.close();
		notifiAdapter();
		
	}
	private void notifiAdapter(){
		Adapter adapter = new Adapter();
		listView.setAdapter(adapter);
		listView.setHeaderDividersEnabled(false);
    	listView.setCacheColorHint(0);
	}
	class Adapter extends BaseAdapter{
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater layout = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
			if(HistoryTicket.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
				convertView = layout.inflate(R.layout.his_item, null);
	        }else{
	        	convertView = layout.inflate(R.layout.portrite_his_item, null);
	        }
			
			TextView dingdanhao = (TextView) convertView.findViewById(R.id.dingdanhao);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView phonenumber = (TextView) convertView.findViewById(R.id.phonenumber);
			TextView ordertime = (TextView) convertView.findViewById(R.id.ordertime);
			TextView testtime = (TextView) convertView.findViewById(R.id.testtime);
			dingdanhao.setText(list.get(position).get("orderid"));
			name.setText(list.get(position).get("username"));
			phonenumber.setText(list.get(position).get("phonenumber"));
			ordertime.setText(list.get(position).get("ordertime").subSequence(0, 10));
			testtime.setText(list.get(position).get("testingTime"));
			Button operate = (Button) convertView.findViewById(R.id.operate);
			operate.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(HistoryTicket.this,LoadIC2ReadCard.class);
					i.putExtra("activity_flag", 3);
					Bundle bundle = new Bundle();
					bundle.putString("orderid", list.get(position).get("orderid"));
					bundle.putString("ordertime", list.get(position).get("ordertime"));
					bundle.putString("username", list.get(position).get("username"));
					bundle.putString("tickettype", list.get(position).get("tickettype"));
					bundle.putString("ordercount", list.get(position).get("ordercount"));
					bundle.putString("createtime", list.get(position).get("createtime"));
					bundle.putString("idnumber", list.get(position).get("idnumber"));
					bundle.putString("phonenumber", list.get(position).get("phonenumber"));
					bundle.putString("scenicname", list.get(position).get("scenicname"));
					bundle.putString("testingTime", list.get(position).get("testingTime"));
					i.putExtra("bundle", bundle);
					startActivity(i);
					finish();
				}
			});
			return convertView;
		}
		
	}
	private void findView(){
		et = (EditText) findViewById(R.id.et1);
		search = (Button) findViewById(R.id.search);
		listView = (ListView) findViewById(R.id.his_listview);
		backBtn = (Button) findViewById(R.id.back);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(HistoryTicket.this,MainActivity.class);
				startActivity(i);
				finish();
			}
		});
		search.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				list.clear();
				database = dbHelper.getReadableDatabase();
				Cursor c = database.query(DbContent.HISTORY_TICKET, null, "(phonenumber=?) or (orderid=?) or (idnumber=?)",
						new String[]{et.getText().toString(),et.getText().toString(),et.getText().toString()}, 
						null, null, "testingTime desc");
				while(c.moveToNext()){
					Map<String,String > map = new HashMap<String,String>();
					map.put("orderid", c.getString(c.getColumnIndex("orderid")));
					map.put("ordertime", c.getString(c.getColumnIndex("ordertime")));
					map.put("username", c.getString(c.getColumnIndex("username")));
					map.put("tickettype", c.getString(c.getColumnIndex("tickettype")));
					map.put("ordercount", c.getString(c.getColumnIndex("ordercount")));
					map.put("createtime", c.getString(c.getColumnIndex("createtime")));
					map.put("idnumber", c.getString(c.getColumnIndex("idnumber")));
					map.put("phonenumber", c.getString(c.getColumnIndex("phonenumber")));
					map.put("scenicname", c.getString(c.getColumnIndex("scenicname")));
					map.put("testingTime", c.getString(c.getColumnIndex("testingTime")));
					list.add(map);
					}
				c.close();
				database.close();
				notifiAdapter();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(HistoryTicket.this,MainActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case KeyEvent.KEYCODE_HOME:
			this.finish();
			return true;
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
  
	
}
