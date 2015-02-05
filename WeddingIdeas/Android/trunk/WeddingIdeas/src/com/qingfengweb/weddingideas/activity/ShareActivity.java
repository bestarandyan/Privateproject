package com.qingfengweb.weddingideas.activity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.share.ConstantsValuesShare;
import com.qingfengweb.weddingideas.share.EmailShare;
import com.qingfengweb.weddingideas.share.SMSShare;
import com.qingfengweb.weddingideas.share.ShareSDK;
import com.qingfengweb.weddingideas.share.SinaWeibo;
import com.qingfengweb.weddingideas.share.TencentZone;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.IRequestListener;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public class ShareActivity extends BaseActivity implements android.view.View.OnClickListener {
	/**
	 * author by Ring
	 * 用于分享微博功能
	 */
	private ProgressDialog progressdialog;
	private int type = 0;// 1新浪，2腾讯，3人人
	private Bitmap bitmap = null;
	private String newstate = "说一些想说的话。。。";
	private ImageView image = null;
//	private TextView text = null;
	private TextView title = null;
	private Button button = null;
//	private Button button_zhuxiao = null;
	private EditText edit = null;
	private boolean click_limit = true;
	public String picPath = "";
	public String msgContent = "";
	public ShareSDK shareSDK = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_shareactivity);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		findview();
		initData();
	}
	private void findview(){
		image = (ImageView) findViewById(R.id.image);
		title = (TextView) findViewById(R.id.title);
		type = getIntent().getIntExtra("type", ConstantsValuesShare.SINA_TYPE);
		msgContent = getIntent().getStringExtra("msgStr");
//		text = (TextView) findViewById(R.id.nickName);
		edit = (EditText) findViewById(R.id.edit);
		edit.setText(msgContent);
		button = (Button) findViewById(R.id.btnSend);
//		button_zhuxiao = (Button) findViewById(R.id.zhuxiao);
		button.setOnClickListener(this);
//		findViewById(R.id.changeUserBtn).setOnClickListener(this);
//		button_zhuxiao.setOnClickListener(this);
	}
	
	public void initData(){
		File file = null;
		try {
			file = new File(getIntent().getStringExtra("filePath"));
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
		if (type == ConstantsValuesShare.SINA_TYPE) {
			title.setText("分享到新浪微博");
			shareSDK = new SinaWeibo(this, requestlistener,handler);
		} else if (type == ConstantsValuesShare.TENCENTWEIBO_TYPE) {
			title.setText("分享到腾讯微博");
//			shareSDK = new TencentWeibo(this);
		} else if (type == ConstantsValuesShare.TENCENT_TYPE) {
			shareSDK = new TencentZone(this,handler,new BaseApiListener("add_share", true));
			title.setText("分享到QQ空间");
		}else if(type == ConstantsValuesShare.SMS_TYPE){
			SMSShare shareSDK = new SMSShare(this);
			shareSDK.startSMSShare(msgContent, picPath);
			title.setText("短信分享");
		}else if(type == ConstantsValuesShare.EMAIL_TYPE){
			EmailShare emailShare = new EmailShare(this);
			emailShare.startSendToEmailIntent("", picPath);
			title.setText("邮件共享");
		}
		if(shareSDK!=null){
			shareSDK.authorize();
			if(shareSDK.isAuthorize()){
				button.setText("发送");
			}
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
				button.setText("发送");
				break;
			case -1:
				AlertDialog.Builder callDailog2 = new AlertDialog.Builder(
						ShareActivity.this);
				callDailog2.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示").setMessage("账号绑定失败！")
						.setPositiveButton("关闭", null);
				callDailog2.show();
				break;
			case 1:
				AlertDialog.Builder callDailog = new AlertDialog.Builder(
						ShareActivity.this);
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示").setMessage("分享成功")
						.setPositiveButton("关闭", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								ShareActivity.this.finish();
							}
						});
				callDailog.show();
				break;
			case 2:
				AlertDialog.Builder callDailog1 = new AlertDialog.Builder(
						ShareActivity.this);
				callDailog1.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示").setMessage("分享失败")
						.setPositiveButton("关闭", null);
				callDailog1.show();
				break;
			case 3:// 打开进度条
				progressdialog = new ProgressDialog(ShareActivity.this);
				progressdialog.setMessage("正在提交信息，请稍等");
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.show();
				break;
			case 4:// 关闭进度条
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
	 * 发送微博
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
				InputStream inputStream = ShareActivity.class
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
		
		if(type == ConstantsValuesShare.TENCENTWEIBO_TYPE){
			return shareSDK.sendMsg(edit.getText().toString(), picPath);
//			return TencentWeibo.sendMSG(edit.getText().toString(), picPath);
		}else if(type == 3){
			
		}
		return false;
	}

	/***
	 * author By Ring
	 * 处理耗时动作
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
						ShareActivity.this);
				callDailog1.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示").setMessage("你还未填写分享内容哦！")
						.setPositiveButton("知道了", null);
				callDailog1.show();
				return;
			}
			if(shareSDK==null){
				return;
			}
			if (type == ConstantsValuesShare.SINA_TYPE) {
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
			} else if (type == ConstantsValuesShare.TENCENT_TYPE) {
				
				if(click_limit){
					click_limit = false;
				}else{
					return;
				}
				if(shareSDK!=null){
				if(shareSDK.isAuthorize()){
					handler.sendEmptyMessage(3);
					shareSDK.sendMsg(edit.getText().toString(),picPath);
				}else{
					click_limit = true;
					shareSDK.authorize();
				}
				}
			}else{
				if(shareSDK!=null){
					if(shareSDK.isAuthorize()){
						new Thread(runnable).start();
					}else{
						shareSDK.authorize();
					}
				}
				
			}
		}/*else if(v.getId() == R.id.changeUserBtn){
			SinaWeibo sinaWeibo = new SinaWeibo(this, requestlistener,handler);
			sinaWeibo.changeUser();
		}*/
	}
	
	
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
                // azrael 2/1注释掉了, 这里为何要在api返回的时候设置token呢,
                // 如果cgi返回的值没有token, 则会清空原来的token
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
     * 通过读取OAuthV2AuthorizeWebView返回的Intent，获取用户授权信息
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   {
        if (requestCode==ConstantsValuesShare.TENCENTWEIBO_TYPE) {
//            if (resultCode==OAuthV2AuthorizeWebView.RESULT_CODE)    {
//            	OAuthV2 oAuth=(OAuthV2) data.getExtras().getSerializable("oauth");
//                if(oAuth.getStatus()==0){
//                	AccessTokenKeeper.keepAccessToken(this,
//        					AccessTokenKeeper.TENCENT_PREFERENCES_NAME,
//        					oAuth.getAccessToken(), oAuth.getOpenid(),
//        					oAuth.getExpiresIn(),oAuth.getOpenkey());
//                	button.setText("发送");
////                	Intent i = new Intent();
////        			i.setClass(this, WeiBoActivity.class);
////        			i.putExtra("type", ConstantsValues.TENCENTWEIBO_TYPE);
////        			startActivity(i);
////                	Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
//                }
//            }
        }
    }
}
