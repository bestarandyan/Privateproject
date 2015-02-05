package android.androidVNC;

import java.util.ArrayList;

import com.vnc.draw.activity.ExitApplication;
import com.vnc.draw.activity.OpenPadActivity;

import android.androidVNC.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InterActivity extends Activity {
	
//	private TextView textview =null;
//	private EditText nickname =null;
	private EditText password =null;
	private EditText address =null;
	private EditText port =null;
//	private TextView tv1 = null;
//	private TextView tv2 = null;
	private Button btnsubmit =null;
	private Button btncancle = null;
	private CheckBox checkboxKeepPassword = null;
	private CheckBox checkBoxconnection =null;
	private VncDatabase database =null;
	private ConnectionBean selected = null;
	private int position;
	private boolean flag_btn=true;
	private Spinner colorSpinner;
	
	
	private String inter_password,inter_address;
	int inter_port;
	boolean defaultconnect;
	private String colormodle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inter_ui);
		ExitApplication.getInstance().context = InterActivity.this;
		ExitApplication.getInstance().addActivity(InterActivity.this);
//		textview = (TextView)findViewById(R.id.inter_ui_textview);
//		nickname = (EditText)findViewById(R.id.inter_ui_name);
		password = (EditText)findViewById(R.id.inter_ui_password);
		address = (EditText)findViewById(R.id.inter_ui_address);
		port = (EditText)findViewById(R.id.inter_ui_port);
//		tv1 = (TextView)findViewById(R.id.tv1);
//		tv2 = (TextView)findViewById(R.id.tv2);
		checkboxKeepPassword = (CheckBox)findViewById(R.id.checkboxKeepPassword);
		checkBoxconnection = (CheckBox)findViewById(R.id.checkboxconnection);
		btnsubmit = (Button)findViewById(R.id.submit_inter);
		btncancle = (Button)findViewById(R.id.submit_cancle);
		database = MyApplication.getInstance().getDatabase();
		colorSpinner = (Spinner)findViewById(R.id.colorformat);
		COLORMODEL[] models=COLORMODEL.values();
		ArrayAdapter<COLORMODEL> colorSpinnerAdapter = new ArrayAdapter<COLORMODEL>(this, android.R.layout.simple_spinner_item, models);
		checkboxKeepPassword = (CheckBox)findViewById(R.id.checkboxKeepPassword);
		colorSpinner.setAdapter(colorSpinnerAdapter);
		colorSpinner.setSelection(0);
		
		btnsubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				updatedata();
				
				if(
//						selected.getNickname().trim().equals("")||selected.getNickname()==null||
						selected.getPassword().trim().equals("")||selected.getPassword()==null||
						selected.getAddress().trim().equals("")||selected.getAddress()==null){
						Toast.makeText(InterActivity.this, "用户名，密码，IP地址不能为空", 3000).show();
				}else{
					saveAndWriteRecent();
					Intent intent = new Intent();
					intent.setClass(InterActivity.this, AndroidVNCEntry.class);
					InterActivity.this.startActivity(intent);
					InterActivity.this.finish();
				}
			}
		});
		
		btncancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(InterActivity.this, AndroidVNCEntry.class);
				InterActivity.this.startActivity(intent);
				InterActivity.this.finish();
			}
		});
		
		Intent intent =getIntent();
		Bundle bundle = intent.getBundleExtra("bundle_item");
//		inter_nickname = bundle.getString("nickname");
		inter_password = bundle.getString("password");
		inter_address = bundle.getString("address");
		inter_port = bundle.getInt("port");
		position = bundle.getInt("position");
		defaultconnect = bundle.getBoolean("defaultconnect");
		colormodle = bundle.getString("colormodle");
//		if(inter_nickname.trim().equals("")||inter_nickname == null){
//			textview.setVisibility(View.GONE);
//			tv1.setVisibility(View.GONE);
//			tv2.setText("名称");
//		}
		if(position==-1){
			selected = new ConnectionBean();
		}else{
			selected = MyApplication.getInstance().getConnections().get(position);
		}
		
		
		
//		inter_textview = inter_nickname+":"+inter_address;
		if(intent.getIntExtra("flag_update",0) == 1){
			flag_btn = false;
		}else if(bundle.getInt("flag_add") == 2){
			flag_btn = true;
		}
//		textview.setText(inter_textview);
//		nickname.setText(inter_nickname);
		password.setText(inter_password);
		address.setText(inter_address);
		port.setText(inter_port+"");
		checkBoxconnection.setChecked(defaultconnect);
		COLORMODEL cm = COLORMODEL.valueOf(colormodle);
		COLORMODEL[] colors=COLORMODEL.values();
		for (int i=0; i<colors.length; ++i)
		{
			if (colors[i] == cm) {
				colorSpinner.setSelection(i);
				break;
			}
		}
	}
	
	
	public void updatedata(){
		selected.setAddress(address.getText().toString());
//		selected.setNickname("");
		selected.setPort(Integer.parseInt(port.getText().toString()));
		selected.setPassword(password.getText().toString());
		selected.setKeepPassword(checkboxKeepPassword.isChecked());
		selected.setdefaultconnection(checkBoxconnection.isChecked());
		selected.setColorModel(((COLORMODEL)colorSpinner.getSelectedItem()).nameString());
	}
	
	public void saveAndWriteRecent()
	{
		SQLiteDatabase db = database.getWritableDatabase();
		db.beginTransaction();
		try
		{
			db.execSQL("update "+AbstractConnectionBean.GEN_TABLE_NAME +" set DEFAULTCONNECTION=0");
			if(flag_btn){
				selected.save(db);
			}else{
				selected.updatesave(db);
			}
			MostRecentBean mostRecent = getMostRecent(db);
			if (mostRecent == null)
			{
				mostRecent = new MostRecentBean();
				mostRecent.setConnectionId(selected.get_Id());
				mostRecent.Gen_insert(db);
			}
			else
			{
				//mostRecent.setConnectionId(AndroidVNCEntry.connections.get(position).get_Id());
				mostRecent.Gen_update(db);
			}
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}
	}
	static MostRecentBean getMostRecent(SQLiteDatabase db)
	{
		ArrayList<MostRecentBean> recents = new ArrayList<MostRecentBean>(1);
		MostRecentBean.getAll(db, MostRecentBean.GEN_TABLE_NAME, recents, MostRecentBean.GEN_NEW);
		if (recents.size() == 0)
			return null;
		return recents.get(0);
	}
	
	
	@Override
	protected void onDestroy() {
		database.close();
		super.onDestroy();
	}
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event)

	{ // TODO Auto-generated method stub

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		  Intent intent = new Intent();
		  intent.setClass(this, AndroidVNCEntry.class);
		  this.startActivity(intent);
		  this.finish();
	  }
	  return true;
	 }
	


}
