package com.qingfengweb.id.blm_goldenLadies;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.network.NetworkCheck;
import com.qingfengweb.network.UploadData;
import com.qingfengweb.util.CommonUtils;
import com.qingfengweb.util.MessageBox;

public class SelectPhotoActivity extends BaseActivity {
	private Button backBtn, submitBtn;
	private ImageButton selectBtn;
	private EditText et3 ;// 依次为：档案卡号 拍摄日期
	private TextView et1,et2,et4;//店名，会员账号
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private ProgressDialog progressdialog;
	private ArrayList<HashMap<String, String>> list = null;// 门店列表
	private int index;// list索引
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID  = 0;
	static final int DIANMIAN_ID = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_selectphoto);
		findView();
		initData();//初始化数据
		Calendar c=Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == DATE_DIALOG_ID){
			Dialog dialog = new DatePickerDialog(this, mDatePickDialog, mYear,mMonth,mDay);
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}
			return null;
	}
	DatePickerDialog.OnDateSetListener mDatePickDialog=new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
				mYear=year;
				mMonth=monthOfYear;
				mDay=dayOfMonth;	
				if(mMonth<9 && mDay < 10){
					et4.setText(mYear + "-0" + (mMonth+1) + "-0" + mDay);
				}else if(mDay < 10 && mMonth>=9){
					et4.setText(mYear + "-" + (mMonth+1) + "-0" + mDay);
				}else if(mDay >= 10 && mMonth<9){
					et4.setText(mYear + "-0" + (mMonth+1) + "-" + mDay);
				}else{
					et4.setText(mYear + "-" + (mMonth+1) + "-" + mDay);
				}
		}
	};
	/****
	 * author Ring
	 * 初始化界面数据
	 */
	private void initData() {
		List<Map<String, Object>> selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select id,name from storeinfo",
						null);

		if (list == null) {
			list = new ArrayList<HashMap<String, String>>();
		}
		list.clear();
		// /此处数据开发人员可以根据需要进行修改传输图片的方式。可以直接改，但是请跟适配器一起改。不需要问
		if (selectresult != null && selectresult.size() > 0) {
			int i;
			HashMap<String, String> map = null;
			for (i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("name") != null
						&& selectresult.get(i).get("id") != null) {
					map = new HashMap<String, String>();
					map.put("id", selectresult.get(i).get("id")
							.toString());
					map.put("name", selectresult.get(i).get("name").toString());
					list.add(map);
				}
			}
		}
		if(selectresult!=null){
			selectresult.clear();
			selectresult = null;
		}
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		et1 = (TextView) findViewById(R.id.dm);
		et2 = (TextView) findViewById(R.id.hyzh);
		et3 = (EditText) findViewById(R.id.dakh);
		et4 = (TextView) findViewById(R.id.psrq);
		selectBtn = (ImageButton) findViewById(R.id.selectBtn);
		et4.setHint("格式：2012-01-01");
		try {
			et2.setText(UserBeanInfo.getInstant().getUserName());
		} catch (Exception e) {
			et2.setText("请先登录");
		}
		et4.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		et1.setOnClickListener(this);
		selectBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (backBtn == v) {
			finish();
		} else if (submitBtn == v) {
			if (textValidate()) {
				new Thread(selectPhotoRunnable).start();
			}
		} else if (et1 == v){
			showDialog();
		} else if (selectBtn == v){
			showDialog();
		}else if(v == et4){
			showDialog(DATE_DIALOG_ID);
		}
		super.onClick(v);
	}


	/**
	 * author by Ring 查件前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if (et1.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.select_name_null),
					SelectPhotoActivity.this);
			return false;
		} else if (et2.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.select_username_null),
					SelectPhotoActivity.this);
			return false;
		} else if (et3.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.select_dangan_null),
					SelectPhotoActivity.this);
			return false;
		} else if (et4.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.select_date_null),
					SelectPhotoActivity.this);
			return false;
		} else if (!dateValidate(et4.getText().toString().trim())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.select_date_error),
					SelectPhotoActivity.this);
			return false;
		}else
			return true;
	}

	/***
	 * author Ring
	 * 
	 * @param date
	 * @return true 正确，false错误 判断日期格式是否正确（yyyy-MM-dd）
	 */
	public boolean dateValidate(String date) {
		String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
		String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
				+ "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
				+ "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
				+ "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if ((date != null)) {
			Pattern pattern = Pattern.compile(datePattern1);
			Matcher match = pattern.matcher(date);
			if (match.matches()) {
				pattern = Pattern.compile(datePattern2);
				match = pattern.matcher(date);
				return match.matches();
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * @author 刘星星
	 *  处理耗时操作
	 */
	public Runnable selectPhotoRunnable = new Runnable() {

		@Override
		public void run() {
			if (NetworkCheck.IsHaveInternet(SelectPhotoActivity.this)) {
				handler.sendEmptyMessage(5);
				String msgStr = RequestServerFromHttp.selectMyPhotos(UserBeanInfo.getInstant().getUserName(),UserBeanInfo.getInstant().getPassword(),
						UserBeanInfo.getInstant().getCurrentStoreId(), et3.getText().toString(),et4.getText().toString());
				if(msgStr.startsWith("0")){//查件成功
					handler.sendEmptyMessage(1);// 跳转到成功界面
				}else if(msgStr.startsWith("-999")){//查件成功  但无数据
					handler.sendEmptyMessage(2);// 无数据
				}else if(msgStr.equals("404")){//访问服务器失败
					handler.sendEmptyMessage(3);// 提交失败给用户提示
				}else{//查件失败
					handler.sendEmptyMessage(3);// 提交失败给用户提示
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}
			click_limit = true;
		}
	};
	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 有数据跳转
				handler.sendEmptyMessage(6);
				i.setClass(SelectPhotoActivity.this,DetailSelectPhotoActivity.class);
				i.putExtra("flag", true);
				SelectPhotoActivity.this.startActivity(i);
				SelectPhotoActivity.this.finish();
				break;
			case 2:// 无数据跳转
				handler.sendEmptyMessage(6);
				i.setClass(SelectPhotoActivity.this,
						DetailSelectPhotoActivity.class);
				i.putExtra("flag", false);
				SelectPhotoActivity.this.startActivity(i);
				SelectPhotoActivity.this.finish();
				break;
			case 3:// 提交失败给用户提示
				handler.sendEmptyMessage(6);
				String errormsg = "";
					errormsg = getResources().getString(R.string.select_error);
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						errormsg,
						SelectPhotoActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.select_error_net),
						SelectPhotoActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(SelectPhotoActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_sumbit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		};
	};

	/**
	 * 门店查询
	 */
	private void showDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(
				SelectPhotoActivity.this);
		LayoutInflater layout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = layout.inflate(R.layout.a_selectclient, null);
		final AlertDialog alertdialog = alert.setView(v).setCancelable(false)
				.create();
		ListView listView = (ListView) v.findViewById(R.id.selectclient);
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.selectclientitem, new String[] { "name" },
				new int[] { R.id.tv });
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				et1.setText(list.get(arg2).get("name"));
				index = arg2;
				alertdialog.dismiss();

			}
		});
		alertdialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					alertdialog.dismiss();
				}
				return false;
			}
		});
		alertdialog.show();
	}
}
