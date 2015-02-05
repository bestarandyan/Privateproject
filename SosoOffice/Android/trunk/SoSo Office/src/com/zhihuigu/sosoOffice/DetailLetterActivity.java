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
	// btn1�������ذ�ť btn2�鿴��һ�� btn3 �鿴��һ�� btn4�ظ� btn5ɾ�� btn6ת��
	private Button backBtn, writeNewBtn;
	private TextView textView;//��ʾ���ռ��˻��Ƿ�����
	private RelativeLayout upLayout,nextLayout,replyLayout,transpondLayout,deleteLayout;
	// tv1������ tv2���� tv3ʱ�� tv4���ظ����Ŀؼ�����ʱ������ʾ������
	private TextView tv1, tv2, tv3;
	private LinearLayout bottomLinear;
	private DBHelper database = null;
	private ProgressDialog progressdialog = null;
	private ArrayList<Map<String, String>> list;
	private int index;
	private TextView content;// ��ʾ�ʼ����ݿؼ�
	private final String mimeType = "text/html";
	private final String encoding = "UTF-8";
	private String url ="";
	private ArrayList<String> urls = new ArrayList<String>();
	
	private SoSoUploadData uploaddata;// �������������
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.a_detailletter);
		findView();
		initData();
		ViewFun();
		notifiList();//������ϵ���б�
		notifyView();//���²���
	}
	@SuppressWarnings("unchecked")
	private void initData() {
		list = (ArrayList<Map<String, String>>) getIntent().getSerializableExtra("list");
		index = getIntent().getIntExtra("index", 0);
	}

	/**
	 * �����ż�״̬ δ�����Ѷ�״̬
	 * true,���³ɹ���false ����ʧ��
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
	 * ɾ���ż�
	 * true,ɾ���ɹ���false ɾ��ʧ��
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
	 * author by Ring ����ɾ���ż���������Ӧֵ
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
	 * ���²���
	 */
	public void notifyView() {
//		content=654564, time=2013-1-8 9:39:43, status=0, letterid=127, name=19
		if(list.size()<=0){//��û���ż���ʱ�򷵻�
			setResult(RESULT_OK);
			finish();
			return;
		}
		if(index>list.size()-1){
			index=0;//���ż�ɾ�������һ���ʱ��������һ��
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
			textView.setText("�����ˣ�");
		}else{
			textView.setText("�ռ��ˣ�");
		}
	}

	private void getListData() {
		for(int i=0;i<5;i++){
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("size",12+ "KB");
			map.put("content", "�����ҷ��ӵ���ƬŶ���㿴��  ��Ư����");
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
		} else if (v == upLayout) {//��һ��
			if (index == 0) {
//				Toast.makeText(this, "���Ѿ��ǵ�һ����", 2000).show();
			} else {
				index--;
				notifyView();
			}
		} else if (v == nextLayout) {//��һ��
			if (index == list.size() - 1) {
//				Toast.makeText(this, "���Ѿ������һ����", 2000).show();
			} else {
				index++;
				notifyView();
			}
		} else if (v == replyLayout) {//�ظ���Ϣ
			if(MyApplication.getInstance().getLettertype()==2){
				MessageBox.CreateAlertDialog("�ܱ�Ǹ�����ܸ����˻ظ���Ϣ��",
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

		} else if (v == deleteLayout) {//ɾ���ż�
			new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("��ʾ")
			.setMessage("��ȷ��Ҫɾ��ô��")
			.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							new Thread(runnable).start();
						}
					}).setNegativeButton("ȡ��", null).show();
		} else if (v == transpondLayout) {//ת��
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
		} else if(v==writeNewBtn){//�����ż�
			Intent i = new Intent(this, WriteNewLetterActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("type", 2);//д��
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
	 * author by Ring ����ɾ���ż���ʱ����
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
					handler.sendEmptyMessage(1);// ��ע�������ת��ע��ɹ�����
				} else {
					handler.sendEmptyMessage(3);// ע��ʧ�ܸ��û���ʾ
				}
			} else {
				handler.sendEmptyMessage(4);// û������ʱ���û���ʾ

			}

			click_limit = true;
		}
	};
	
	/**
	 * author by Ring ��������ż�״̬
	 */
	public Runnable runnable1 = new Runnable() {

		@Override
		public void run() {
			int type = 0;////�ż����ͣ�1�ռ��䣬2�����䣬3����Ա�ż�
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
				handler.sendEmptyMessage(4);// û������ʱ���û���ʾ

			}

		}
	};
	
	/**
	 * author by Ring ����ҳ����ת����
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// ɾ���ɹ��������Ի�����ʾ�û�
				MessageBox.CreateAlertDialog(getResources().getString(R.string.deletelettlesuccess),
						DetailLetterActivity.this);
				notifyView();
				break;
			case 2:// ��ע�������ת����¼����
				i.setClass(DetailLetterActivity.this, LoginActivity.class);
				DetailLetterActivity.this.startActivity(i);
				DetailLetterActivity.this.finish();
				break;
			case 3:// ע��ʧ�ܸ��û���ʾ
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
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(DetailLetterActivity.this);
				break;
			case 5:// �򿪽�����
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
			case 6:// �رս�����
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		};
	};
}
