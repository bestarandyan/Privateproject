package com.qingfengweb.client.activity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.qingfengweb.android.R;
import com.qingfengweb.share.AccessTokenKeeper;
import com.qingfengweb.share.ConstantsValues;
import com.qingfengweb.share.ShareSDK;
import com.qingfengweb.share.SinaWeibo;
import com.qingfengweb.share.TencentWeibo;
import com.qingfengweb.share.TencentZone;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.IRequestListener;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WeiBoActivity extends Activity implements android.view.View.OnClickListener {
	/**
	 * author by Ring
	 * ���ڷ���΢������
	 */
	private ProgressDialog progressdialog;
	private int type = 0;// 1���ˣ�2��Ѷ��3����
	private Bitmap bitmap = null;
	private String newstate = "˵һЩ��˵�Ļ�������";
	private ImageView image = null;
//	private TextView text = null;
	private TextView title = null;
	private Button button = null;
//	private Button button_zhuxiao = null;
	private EditText edit = null;
	private boolean click_limit = true;
	public String picPath = "";
	
	public ShareSDK shareSDK = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.umeng_share_update);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		findview();
		initData();
	}
	private void findview(){
		image = (ImageView) findViewById(R.id.image);
		title = (TextView) findViewById(R.id.title);
		type = getIntent().getIntExtra("type", ConstantsValues.SINA_TYPE);
//		text = (TextView) findViewById(R.id.nickName);
		edit = (EditText) findViewById(R.id.edit);
		edit.setText(R.string.company_intro);
		button = (Button) findViewById(R.id.btnSend);
//		button_zhuxiao = (Button) findViewById(R.id.zhuxiao);
		button.setOnClickListener(this);
//		button_zhuxiao.setOnClickListener(this);
	}
	
	public void initData(){
		File file = null;
		try {
			File fileDir = new File(Environment.getExternalStorageDirectory()+"/qingfengweb/");
			if (!fileDir.exists())
				fileDir.mkdirs();
			file = new File(fileDir,"pic.jpg");
			if (!file.exists()||!file.isFile()) {
				file.delete();
				file.createNewFile();
				InputStream inputStream = WeiBoActivity.class
						.getResourceAsStream("/res/drawable/pic.jpg");
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ins;
				while ((ins = inputStream.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ins);
				}
				inputStream.close();
				fileOutputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(file!=null&&file.exists()&&file.isFile()){
			picPath = file.getAbsolutePath();
			bitmap = BitmapFactory.decodeFile(picPath);
		}
		if (bitmap != null) {
			image.setImageBitmap(bitmap);
			image.setVisibility(View.VISIBLE);
		}
		if (type == ConstantsValues.SINA_TYPE) {
			title.setText("��������΢��");
			shareSDK = new SinaWeibo(this, requestlistener,handler);
		} else if (type == ConstantsValues.TENCENTWEIBO_TYPE) {
			title.setText("������Ѷ΢��");
			shareSDK = new TencentWeibo(this);
		} else if (type == ConstantsValues.TENCENT_TYPE) {
			shareSDK = new TencentZone(this,handler,new BaseApiListener("add_share", true));
			title.setText("����QQ");
		}
		shareSDK.authorize();
		if(shareSDK.isAuthorize()){
			button.setText("����");
		}
	}
	@Override
	protected void onResume() {
		System.out.println("onResume");
		super.onResume();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				button.setText("����");
				break;
			case -1:
				AlertDialog.Builder callDailog2 = new AlertDialog.Builder(
						WeiBoActivity.this);
				callDailog2.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("��ʾ").setMessage("�˺Ű�ʧ�ܣ�")
						.setPositiveButton("�ر�", null);
				callDailog2.show();
				break;
			case 1:
				AlertDialog.Builder callDailog = new AlertDialog.Builder(
						WeiBoActivity.this);
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("��ʾ").setMessage("����ɹ�")
						.setPositiveButton("�ر�", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								WeiBoActivity.this.finish();
							}
						});
				callDailog.show();
				break;
			case 2:
				AlertDialog.Builder callDailog1 = new AlertDialog.Builder(
						WeiBoActivity.this);
				callDailog1.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("��ʾ").setMessage("����ʧ��")
						.setPositiveButton("�ر�", null);
				callDailog1.show();
				break;
			case 3:// �򿪽�����
				progressdialog = new ProgressDialog(WeiBoActivity.this);
				progressdialog.setMessage("�����ύ��Ϣ�����Ե�");
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.show();
				break;
			case 4:// �رս�����
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		};
	};
	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(bitmap!=null){
			bitmap.recycle();
			bitmap = null;
		}
	}
	/***
	 * author by Ring
	 * ����΢��
	 * @return
	 */

	public boolean sendWeibo() {
		File file = null;
		try {
			File fileDir = new File(Environment.getExternalStorageDirectory()+"/qingfengweb/");
			if (!fileDir.exists())
				fileDir.mkdirs();
			file = new File(fileDir,"pic.jpg");
			if (!file.exists()&&!file.isFile()) {
				file.createNewFile();
				InputStream inputStream = WeiBoActivity.class
						.getResourceAsStream("/res/drawable/pic.jpg");
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ins;
				while ((ins = inputStream.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ins);
				}
				inputStream.close();
				fileOutputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(file!=null&&file.exists()&&file.isFile()){
			picPath = file.getAbsolutePath();
			bitmap = BitmapFactory.decodeFile(picPath);
		}
		
		if(type == ConstantsValues.TENCENTWEIBO_TYPE){
			return shareSDK.sendMsg(edit.getText().toString(), picPath);
//			return TencentWeibo.sendMSG(edit.getText().toString(), picPath);
		}else if(type == 3){
			
		}
		return false;
	}

	/***
	 * author By Ring
	 * �����ʱ����
	 */
	
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if(click_limit){
				click_limit = false;
			}else{
				return;
			}
			handler.sendEmptyMessage(3);
			if(sendWeibo()){
				handler.sendEmptyMessage(1);
			}else{
				handler.sendEmptyMessage(2);
			}
			handler.sendEmptyMessage(4);
			click_limit = true;
		}
	};
	
	

	public void onClick(android.view.View v) {
		if(v == button){
			if(edit.getText().toString().trim().equals("")){
				AlertDialog.Builder callDailog1 = new AlertDialog.Builder(
						WeiBoActivity.this);
				callDailog1.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("��ʾ").setMessage("�㻹δ��д��������Ŷ��")
						.setPositiveButton("֪����", null);
				callDailog1.show();
				return;
			}
			if (type == ConstantsValues.SINA_TYPE) {
				if(click_limit){
					click_limit = false;
				}else{
					return;
				}
				if(shareSDK.isAuthorize()){
					handler.sendEmptyMessage(3);
					shareSDK.sendMsg(edit.getText().toString(),picPath);
				}else{
					click_limit = true;
					shareSDK.authorize();
				}
//				SinaWeibo sina = new SinaWeibo(this,null);
//				sina.sendMSG(edit.getText().toString(), picPath, );
			} else if (type == ConstantsValues.TENCENT_TYPE) {
				
				if(click_limit){
					click_limit = false;
				}else{
					return;
				}
				if(shareSDK.isAuthorize()){
					handler.sendEmptyMessage(3);
					shareSDK.sendMsg(edit.getText().toString(),picPath);
				}else{
					click_limit = true;
					shareSDK.authorize();
				}
			}else{
				if(shareSDK.isAuthorize()){
					new Thread(runnable).start();
				}else{
					shareSDK.authorize();
				}
			}
		}
	};
	
	
	private RequestListener requestlistener = new RequestListener() {
		
		@Override
		public void onIOException(IOException e) {
			handler.sendEmptyMessage(2);
			handler.sendEmptyMessage(4);
			click_limit = true;
		}
		
		@Override
		public void onError(WeiboException e) {
			handler.sendEmptyMessage(2);
			handler.sendEmptyMessage(4);
			click_limit = true;
		}
		
		@Override
		public void onComplete(String response) {
			handler.sendEmptyMessage(1);
			handler.sendEmptyMessage(4);
			click_limit = true;
			
		}
	};
	
	private class BaseApiListener implements IRequestListener {
//        private String mScope = "all";
//        private Boolean mNeedReAuth = false;

        public BaseApiListener(String scope, boolean needReAuth) {
//            mScope = scope;
//            mNeedReAuth = needReAuth;
        }

        @Override
        public void onComplete(final JSONObject response, Object state) {
            doComplete(response, state);
        }

        protected void doComplete(JSONObject response, Object state) {
            try {
                int ret = response.getInt("ret");
                if(ret==0){
                	handler.sendEmptyMessage(1);
    				handler.sendEmptyMessage(4);
                }else{
                	handler.sendEmptyMessage(2);
    				handler.sendEmptyMessage(4);
                }
                click_limit = true;
//                if (ret == 100030) {
//                    if (mNeedReAuth) {
//                        Runnable r = new Runnable() {
//                            public void run() {
//                                mTencent.reAuth(context, mScope, new BaseUiListener());
//                            }
//                        };
//                        context.runOnUiThread(r);
//                    }
//                }
                // azrael 2/1ע�͵���, ����Ϊ��Ҫ��api���ص�ʱ������token��,
                // ���cgi���ص�ֵû��token, ������ԭ����token
                // String token = response.getString("access_token");
                // String expire = response.getString("expires_in");
                // String openid = response.getString("openid");
                // mTencent.setAccessToken(token, expire);
                // mTencent.setOpenId(openid);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("toddtest", response.toString());
            }

        }

        @Override
        public void onIOException(final IOException e, Object state) {
        	handler.sendEmptyMessage(2);
			handler.sendEmptyMessage(4);
			click_limit = true;
        	Log.e("IRequestListener.onIOException:", e.getMessage());
        }

        @Override
        public void onMalformedURLException(final MalformedURLException e,
                Object state) {
        	handler.sendEmptyMessage(2);
			handler.sendEmptyMessage(4);
			click_limit = true;
        	Log.e("IRequestListener.onMalformedURLException", e.toString());
        }

        @Override
        public void onJSONException(final JSONException e, Object state) {
        	handler.sendEmptyMessage(2);
			handler.sendEmptyMessage(4);
			click_limit = true;
        	Log.e("IRequestListener.onJSONException:", e.getMessage());
        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException arg0,
                Object arg1) {
        	handler.sendEmptyMessage(2);
			handler.sendEmptyMessage(4);
			click_limit = true;
        	Log.e("IRequestListener.onConnectTimeoutException:", arg0.getMessage());

        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException arg0,
                Object arg1) {
        	handler.sendEmptyMessage(2);
			handler.sendEmptyMessage(4);
			click_limit = true;
        	Log.e("IRequestListener.SocketTimeoutException:", arg0.getMessage());
        }

        @Override
        public void onUnknowException(Exception arg0, Object arg1) {
        	handler.sendEmptyMessage(2);
			handler.sendEmptyMessage(4);
			click_limit = true;
        	Log.e("IRequestListener.onUnknowException:", arg0.getMessage());
        }

        @Override
        public void onHttpStatusException(HttpStatusException arg0, Object arg1) {
        	handler.sendEmptyMessage(2);
			handler.sendEmptyMessage(4);
			click_limit = true;
        	Log.e("IRequestListener.HttpStatusException:", arg0.getMessage());
        }

        @Override
        public void onNetworkUnavailableException(NetworkUnavailableException arg0, Object arg1) {
        	handler.sendEmptyMessage(2);
			handler.sendEmptyMessage(4);
			click_limit = true;
        	Log.e("IRequestListener.onNetworkUnavailableException:", arg0.getMessage());
        }
    }
	
	
	/*
     * ͨ����ȡOAuthV2AuthorizeWebView���ص�Intent����ȡ�û���Ȩ��Ϣ
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   {
        if (requestCode==ConstantsValues.TENCENTWEIBO_TYPE) {
            if (resultCode==OAuthV2AuthorizeWebView.RESULT_CODE)    {
            	OAuthV2 oAuth=(OAuthV2) data.getExtras().getSerializable("oauth");
                if(oAuth.getStatus()==0){
                	AccessTokenKeeper.keepAccessToken(this,
        					AccessTokenKeeper.TENCENT_PREFERENCES_NAME,
        					oAuth.getAccessToken(), oAuth.getOpenid(),
        					oAuth.getExpiresIn(),oAuth.getOpenkey());
                	button.setText("����");
//                	Intent i = new Intent();
//        			i.setClass(this, WeiBoActivity.class);
//        			i.putExtra("type", ConstantsValues.TENCENTWEIBO_TYPE);
//        			startActivity(i);
//                	Toast.makeText(getApplicationContext(), "��½�ɹ�", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
