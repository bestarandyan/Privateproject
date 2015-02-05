package guoTeng.LoadIC2ReadCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Text;

import com.google.zxing.client.android.R;
import com.qingfengweb.piaoguanjia.content.DbContent;
import com.qingfengweb.piaoguanjia.db.ProjectDBHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryTicket extends Activity implements OnClickListener,OnItemClickListener{
private ListView listView;
private Button backBtn;
private List<Map<String,String>> list;
private List<Map<String,String>> lastList;
private List<Map<String,String>> searchList;
private ProjectDBHelper dbHelper;
private SQLiteDatabase database;
private EditText et;
private Button search;
private TextView noText;
private TextView lastTextView;
private TextView historyTextView;
private ImageView lastImageView;
private ImageView historyImageView;
private RelativeLayout lastLayout;
private RelativeLayout historyLayout;
private LockLayer lockLayer = null;
	View lock = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
//			setContentView(R.layout.a_history);
		lock = View.inflate(this, R.layout.a_history, null);
        lockLayer = new LockLayer(this);
        lockLayer.setLockView(lock);
        lockLayer.lock();
		findView();
		initData(true);
		initView();
	}
	
	
	@Override
	protected void onDestroy() {
		lockLayer.unlock();
		super.onDestroy();
	}


	/*private Handler mHandler = new Handler();
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
	}*/
	private void initData(boolean allData){
		if(list == null){
			list = new ArrayList<Map<String,String>>();
		}else{
			list.clear();
		}
		if(lastList == null){
			lastList = new ArrayList<Map<String,String>>();
		}else{
			lastList.clear();
		}
		if(searchList == null){
			searchList = new ArrayList<Map<String,String>>();
		}else{
			searchList.clear();
		}
		searchList = new ArrayList<Map<String,String>>();
		dbHelper  = new ProjectDBHelper(HistoryTicket.this);
		database = dbHelper.getReadableDatabase();
		Cursor c = null;
		if(allData){
			c = database.query(DbContent.HISTORY_TICKET, null, null, null, null, null, "createtime desc");
		}else{
			
		}
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
			map.put("validatetime", c.getString(c.getColumnIndex("validatetime")));
			list.add(map);
			if(lastList.size() == 0){
				lastList.add(map);
			}else if(lastList.size() > 0){
				String date1 = lastList.get(0).get("ordertime").toString();
				String date2 = map.get("ordertime").toString();
				String[] date1Array = date1.split("-");
				String[] date2Array = date2.split("-");
				date1 = "";
				date2 = "";
				for(int i=0;i<date1Array.length;i++){
					date1 += date1Array[i];
					date2 += date2Array[i];
				}
				int lastDate = Integer.parseInt(date1);
				int currentDate = Integer.parseInt(date2);
				if((currentDate - lastDate) <=7){
					lastList.add(map);
				}
			}
			
			}
		c.close();
		database.close();
		notifiAdapter(lastList);
		
	}
	private void notifiAdapter(List<Map<String,String>> list){
		Adapter adapter = new Adapter(list);
		listView.setAdapter(adapter);
		listView.setHeaderDividersEnabled(false);
    	listView.setCacheColorHint(0);
    	if(list.size() == 0){
    		noText.setVisibility(View.VISIBLE);
    	}else{
    		noText.setVisibility(View.GONE);
    	}
	}
	class Adapter extends BaseAdapter{
		List<Map<String,String>> adapterList;
		public Adapter(List<Map<String,String>> list) {
			this.adapterList = list;
		}
		public int getCount() {
			// TODO Auto-generated method stub
			return adapterList.size();
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
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(HistoryTicket.this).inflate(R.layout.his_item, null);
				holder.dingdanhao = (TextView) convertView.findViewById(R.id.dingdanhao);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.phonenumber = (TextView) convertView.findViewById(R.id.phonenumber);
				holder.ordertime = (TextView) convertView.findViewById(R.id.ordertime);
				holder.typeTv = (TextView) convertView.findViewById(R.id.type);
				holder.amoutTv = (TextView) convertView.findViewById(R.id.amount);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			 holder.setcontent(position);
			
			/*Button operate = (Button) convertView.findViewById(R.id.operate);
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
					bundle.putString("validatetime", list.get(position).get("validatetime"));
					i.putExtra("bundle", bundle);
					startActivity(i);
					finish();
				}
			});*/
			return convertView;
		}
		class ViewHolder{
			TextView dingdanhao;
			TextView name;
			TextView phonenumber;
			TextView ordertime;
			TextView typeTv ;
			TextView amoutTv ;
			public void setcontent(int position){
				String username = adapterList.get(position).get("username").toString();
				String orderid = adapterList.get(position).get("orderid").toString();
				String 	phonenumberstr = adapterList.get(position).get("phonenumber");
				String ordertimestr = adapterList.get(position).get("ordertime");
				String tickettype = adapterList.get(position).get("tickettype");
				String ordercount = adapterList.get(position).get("ordercount");
				if(orderid!=null && orderid.length()>0){
					dingdanhao.setText(orderid);
				}else{
					dingdanhao.setText("");
				}
				if(username!=null && username.length()>0){
					name.setText(username);
				}else{
					name.setText("");
				}
				if(phonenumberstr!=null && phonenumberstr.length()>0){
					phonenumber.setText("["+phonenumberstr+"]");
				}else{
					phonenumber.setText("");
				}
				if(ordertimestr!=null && ordertimestr.length()>0){
					ordertime.setText(ordertimestr);
				}else{
					ordertime.setText("");
				}
				if(tickettype!=null && tickettype.length()>0){
					typeTv.setText(tickettype);
				}else{
					typeTv.setText("");
				}
				if(ordercount!=null && ordercount.length()>0){
					amoutTv.setText(ordercount+"уе");
				}else{
					amoutTv.setText("");
				}
			}
		}
		
	}
	private void initView(){
		historyLayout.setOnClickListener(this);
		lastLayout.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		et.addTextChangedListener(EtChangeListener);
		search.setOnClickListener(this);
		listView.setOnItemClickListener(this);
	}
	private void findView(){
		historyTextView = (TextView) lock.findViewById(R.id.historyTv);
		lastTextView = (TextView) lock.findViewById(R.id.lastTv);
		historyImageView = (ImageView) lock.findViewById(R.id.historyIV);
		lastImageView = (ImageView) lock.findViewById(R.id.lastIV);
		et = (EditText) lock.findViewById(R.id.et1);
		search = (Button) lock.findViewById(R.id.search);
		listView = (ListView) lock.findViewById(R.id.his_listview);
		backBtn = (Button) lock.findViewById(R.id.backBtn);
		noText = (TextView) lock.findViewById(R.id.noText);
		historyLayout = (RelativeLayout) lock.findViewById(R.id.historyLayout);
		lastLayout = (RelativeLayout) lock.findViewById(R.id.lastLayout);
	}

	TextWatcher EtChangeListener = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(et.getText().toString().length() == 0){
					notifiAdapter(list);
				}
			}
		};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		}
		return false;
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
//			Intent i = new Intent(HistoryTicket.this,MainActivity.class);
//			startActivity(i);
			finish();
		}else if(v == search){
			if(et.getText().toString().length()>0){
				searchList.clear();
				database = dbHelper.getReadableDatabase();
				Cursor c = database.query(DbContent.HISTORY_TICKET, null, "(phonenumber=?) or (orderid=?) or (idnumber=?) or (username=?)",
						new String[]{et.getText().toString(),et.getText().toString(),et.getText().toString(),et.getText().toString()}, 
						null, null, "validatetime desc");
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
					map.put("validatetime", c.getString(c.getColumnIndex("validatetime")));
					searchList.add(map);
					}
				c.close();
				database.close();
				notifiAdapter(searchList);
			}else{
			}
		}else if(v == historyLayout){
			historyImageView.setVisibility(View.VISIBLE);
			lastImageView.setVisibility(View.GONE);
			historyTextView.setTextColor(Color.rgb(254, 254, 254));
			lastTextView.setTextColor(Color.rgb(151, 151, 151));
			notifiAdapter(list);
		}else if(v == lastLayout){
			historyImageView.setVisibility(View.GONE);
			lastImageView.setVisibility(View.VISIBLE);
			historyTextView.setTextColor(Color.rgb(151, 151, 151));
			lastTextView.setTextColor(Color.rgb(254, 254, 254));
			notifiAdapter(lastList);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent(HistoryTicket.this,DetailCheckingActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("orderid", list.get(arg2).get("orderid"));
		bundle.putString("ordertime", list.get(arg2).get("ordertime"));
		bundle.putString("username", list.get(arg2).get("username"));
		bundle.putString("tickettype", list.get(arg2).get("tickettype"));
		bundle.putString("ordercount", list.get(arg2).get("ordercount"));
		bundle.putString("createtime", list.get(arg2).get("createtime"));
		bundle.putString("idnumber", list.get(arg2).get("idnumber"));
		bundle.putString("phonenumber", list.get(arg2).get("phonenumber"));
		bundle.putString("scenicname", list.get(arg2).get("scenicname"));
		bundle.putString("validatetime", list.get(arg2).get("validatetime"));
		i.putExtra("bundle", bundle);
		startActivity(i);
	}      
  
	
}
