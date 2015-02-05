package com.zhihuigu.sosoOffice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DetailLetterActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	// btn1顶部返回按钮 btn2查看上一封 btn3 查看下一封 btn4回复 btn5删除 btn6转发
	private Button backBtn, writeNewBtn;
	private TextView textView;//表示是收件人还是发件人
	private RelativeLayout upLayout,nextLayout,replyLayout,transpondLayout,deleteLayout;
	// tv1发件人 tv2内容 tv3时间 tv4下载附件的控件收缩时上面显示的内容
	private TextView tv1, tv2, tv3;
	private LinearLayout bottomLinear;
	private DBHelper database = null;
	private ProgressDialog progressdialog = null;
	private ArrayList<Map<String, String>> list;
	private int index;
	private TextView content;// 显示邮件内容控件
	private final String mimeType = "text/html";
	private final String encoding = "UTF-8";
	private String url ="";
	private ArrayList<String> urls = new ArrayList<String>();
	
	private SoSoUploadData uploaddata;// 服务器请求对象
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.a_detailletter);
		findView();
		initData();
		ViewFun();
		notifiList();//更新联系人列表
		notifyView();//更新布局
	}
	@SuppressWarnings("unchecked")
	private void initData() {
		list = (ArrayList<Map<String, String>>) getIntent().getSerializableExtra("list");
		index = getIntent().getIntExtra("index", 0);
	}

	/**
	 * 更新信件状态 未读，已读状态
	 * true,更新成功，false 更新失败
	 */
	public void updateLetterStatus() {
		if(list!=null&&list.size()>=index+1&&list.get(index).get("status")!=null){
			int i = 1;
			try {
				i = Integer.parseInt(list.get(index).get("status").toString());
			} catch (Exception e) {
			}
			if(i==0){
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
						this).getAPPID()));
				params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
						this).getAPPKEY()));
				params.add(new BasicNameValuePair("LetterId", list.get(index).get("letterid").toString()));
				uploaddata = new SoSoUploadData(this, "LetterUpdateState.aspx", params);
				uploaddata.Post();
				reponse = uploaddata.getReponse();
				dealReponse(2,list.get(index).get("letterid").toString());
				params.clear();
				params = null;
			}
		}
	}

	/**
	 * 删除信件
	 * true,删除成功，false 删除失败
	 */
	public boolean deleteLetter() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("LetterId", list.get(index).get("letterid").toString()));
		uploaddata = new SoSoUploadData(this, "LetterDelete.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(1,list.get(index).get("letterid").toString());
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	/***
	 * author by Ring 处理删除信件请求后的响应值
	 */

	public void dealReponse(int type,String letterid) {
		boolean b = false;
		String str = "";
		if(MyApplication.getInstance().getLettertype()==1){
			str = "soso_letterininfo";
		}else{
			str = "soso_letteroutinfo";
		}
		if(type ==1){
			if (StringUtils.CheckReponse(reponse)) {
				b=DBHelper.getInstance(this).delete(str, "letterid=?", new String[]{letterid});
				if(b){
					list.remove(index);
				}
			}
		}else{
			ContentValues values = new ContentValues();
			values.put("letterstate", 1);
			b=DBHelper.getInstance(this).update(str, values, "letterid=?", new String[]{letterid});
			if(b){
				((HashMap<String, String>)list.get(index)).put("status","1");  
			}
			values.clear();
			values=null;
		}
	}

	/**
	 * 更新布局
	 */
	public void notifyView() {
//		content=654564, time=2013-1-8 9:39:43, status=0, letterid=127, name=19
		if(list.size()<=0){//当没有信件的时候返回
			setResult(RESULT_OK);
			finish();
			return;
		}
		if(index>list.size()-1){
			index=0;//当信件删除到最后一封的时候跳到第一份
		}
		tv1.setText(list.get(index).get("name").toString());
		tv2.setText(list.get(index).get("title").toString());
		tv3.setText(list.get(index).get("time").toString());
//		final String mimeType = "text/html";
//		final String encoding = "UTF-8";
//		content.getSettings().setDefaultTextEncodingName(encoding);
//		content.loadData(list.get(index).get("content").toString(),
//				mimeType, encoding);
//		content.loadDataWithBaseURL("", list.get(index).get("content").toString(), mimeType, encoding, "");
		content.setText(list.get(index).get("content").toString());
		
		new Thread(runnable1).start();
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		writeNewBtn = (Button) findViewById(R.id.writeNewLetter);
		upLayout = (RelativeLayout) findViewById(R.id.upLayout);
		nextLayout = (RelativeLayout) findViewById(R.id.nextLayout);
		replyLayout = (RelativeLayout) findViewById(R.id.replyLayout);
		transpondLayout = (RelativeLayout) findViewById(R.id.transpondLayout);
		deleteLayout = (RelativeLayout) findViewById(R.id.deleteLayout);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		content = (TextView) findViewById(R.id.mailContent);
		bottomLinear = (LinearLayout) findViewById(R.id.bottomLinear);
		textView = (TextView) findViewById(R.id.textView);
		
	}

	private void ViewFun() {
		backBtn.setOnClickListener(this);
		writeNewBtn.setOnClickListener(this);
		upLayout.setOnClickListener(this);
		nextLayout.setOnClickListener(this);
		replyLayout.setOnClickListener(this);
		transpondLayout.setOnClickListener(this);
		deleteLayout.setOnClickListener(this);
		bottomLinear.getBackground().setAlpha(180);
		content.setBackgroundColor(0);
		if(MyApplication.getInstance().getLettertype() == 1){
			textView.setText("发件人：");
		}else{
			textView.setText("收件人：");
		}
	}

	private void getListData() {
		for(int i=0;i<5;i++){
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("size",12+ "KB");
			map.put("content", "这是我房子的照片哦，你看看  多漂亮。");
			map.put("downloadpath", "sdgszdfgsdfzsdf");
		}
	}

	private void notifiList() {
		getListData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backBtn) {
			CommonUtils.hideSoftKeyboard(this);
			setResult(RESULT_OK);
			finish();
		} else if (v == upLayout) {//上一封
			if (index == 0) {
//				Toast.makeText(this, "这已经是第一封了", 2000).show();
			} else {
				index--;
				notifyView();
			}
		} else if (v == nextLayout) {//下一封
			if (index == list.size() - 1) {
//				Toast.makeText(this, "这已经是最后一封了", 2000).show();
			} else {
				index++;
				notifyView();
			}
		} else if (v == replyLayout) {//回复信息
			if(MyApplication.getInstance().getLettertype()==2){
				MessageBox.CreateAlertDialog("很抱歉，不能给本人回复信息！",
						DetailLetterActivity.this);
			}else{
				Intent i = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("tag", 1);
				bundle.putString("shoujianren", list.get(index).get("name").toString());
				bundle.putString("shoujianrenid", list.get(index).get("id").toString());
				bundle.putString("theme", "RE:"+list.get(index).get("title").toString());
				bundle.putString("content", list.get(index).get("content").toString());
				bundle.putString("WhoUserID", "");
				i.setClass(DetailLetterActivity.this, WriteNewLetterActivity.class);
				i.putExtra("bundle", bundle);
				i.putExtra("titleFlag", 1);
				DetailLetterActivity.this.startActivity(i);
			}

		} else if (v == deleteLayout) {//删除信件
			new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("提示")
			.setMessage("您确定要删除么？")
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							new Thread(runnable).start();
						}
					}).setNegativeButton("取消", null).show();
		} else if (v == transpondLayout) {//转发
			Intent i = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("shoujianren", "");
			bundle.putString("theme", "FWD:"+list.get(index).get("title").toString());
			bundle.putString("content", list.get(index).get("content").toString());
			bundle.putString("WhoUserID", "");
			i.putExtra("bundle", bundle);
			i.putExtra("titleFlag", 2);
			i.setClass(DetailLetterActivity.this, WriteNewLetterActivity.class);
			DetailLetterActivity.this.startActivity(i);
		} else if(v==writeNewBtn){//发新信件
			Intent i = new Intent(this, WriteNewLetterActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("type", 2);//写信
			bundle.putString("messageid", "");
			i.putExtra("bundle", bundle);
			startActivityForResult(i, 0);
		}
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			finish();
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * author by Ring 处理删除信件耗时操作
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(DetailLetterActivity.this)) {
				handler.sendEmptyMessage(5);
				boolean b = deleteLetter();
				handler.sendEmptyMessage(6);
				if (runnable_tag) {
					runnable_tag = true;
					click_limit = true;
					return;
				}
				if (b) {
					handler.sendEmptyMessage(1);// 从注册界面跳转到注册成功界面
				} else {
					handler.sendEmptyMessage(3);// 注册失败给用户提示
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}

			click_limit = true;
		}
	};
	
	/**
	 * author by Ring 处理更新信件状态
	 */
	public Runnable runnable1 = new Runnable() {

		@Override
		public void run() {
			int type = 0;////信件类型；1收件箱，2发件箱，3管理员信件
			type = MyApplication.getInstance(DetailLetterActivity.this).getLettertype();
			if (NetworkCheck.IsHaveInternet(DetailLetterActivity.this)) {
				if(type!=2){
					updateLetterStatus();
					if (runnable_tag) {
						runnable_tag = true;
						return;
					}
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}

		}
	};
	
	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 删除成功，跳出对话框提示用户
				MessageBox.CreateAlertDialog(getResources().getString(R.string.deletelettlesuccess),
						DetailLetterActivity.this);
				notifyView();
				break;
			case 2:// 从注册界面跳转到登录界面
				i.setClass(DetailLetterActivity.this, LoginActivity.class);
				DetailLetterActivity.this.startActivity(i);
				DetailLetterActivity.this.finish();
				break;
			case 3:// 注册失败给用户提示
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					message = getResources().getString(
							R.string.progress_timeout);
				} else {
					message = getResources().getString(R.string.deletelettleerror);
				}
				MessageBox.CreateAlertDialog(message,
						DetailLetterActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(DetailLetterActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(DetailLetterActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_submit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setCancelable(true);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						if (uploaddata != null) {
							uploaddata.overReponse();
						}
						return false;
					}
				});
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
}
