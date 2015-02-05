/**
 * 
 */
package com.qingfengweb.id.blm_goldenLadies;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.ActiveListInfo;

/**
 * @author 刘星星 武国庆 
 * @createDate 2013/8/26
 * 活动分享详情界面
 *
 */
public class DetailActiveActivity extends BaseActivity{
	private RelativeLayout parent;
	public String activeId = "";
	private WebView detailWvView;
	private TextView titleTv;
	DBHelper dbHelper = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailactive);
		dbHelper = DBHelper.getInstance(this);
		findViewById(R.id.backBtn).setOnClickListener(this);
		parent = (RelativeLayout) findViewById(R.id.parent);
		findViewById(R.id.bottomBackBtn).setOnClickListener(this);
		findViewById(R.id.zixunBtn).setOnClickListener(this);
		findViewById(R.id.ShareBtn).setOnClickListener(this);
		titleTv = (TextView) findViewById(R.id.titleTv);
		titleTv.setText(getIntent().getStringExtra("title"));
		activeId = getIntent().getStringExtra("activeid");
		detailWvView = (WebView) findViewById(R.id.homeWebView);
		detailWvView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  
		detailWvView.getSettings().setJavaScriptEnabled(true);
    	detailWvView.getSettings().setDefaultTextEncodingName("utf-8");
    	detailWvView.getSettings().setAllowFileAccess(true);
    	detailWvView.getSettings().setBuiltInZoomControls(false);
    	detailWvView.getSettings().setSupportZoom(true);
    	detailWvView.getSettings().setDefaultZoom(ZoomDensity.FAR);
    	detailWvView.getSettings().setUseWideViewPort(true);
    	detailWvView.setInitialScale(10);
    	detailWvView.getSettings().setDomStorageEnabled(true);
    	detailWvView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
    	detailWvView.setWebViewClient(new WebViewClient(){
        	@SuppressLint("NewApi")
			@Override
        	public void onReceivedSslError(WebView view,
        			SslErrorHandler handler, SslError error) {
        		
        		super.onReceivedSslError(view, handler, error);
        	}
        	
        	
        	/* (non-Javadoc)
        	 * @see android.webkit.WebViewClient#onReceivedError(android.webkit.WebView, int, java.lang.String, java.lang.String)
        	 */
        	@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}


			@Override
        	public void onPageFinished(WebView view, String url) {
        	super.onPageFinished(view, url);
        	
        	
        	}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
        	
        });
//    	detailWvView.loadUrl("http://baike.baidu.com/view/1434.htm");
		new Thread(getActiveDetailRunnable).start();
	}
	Runnable getActiveDetailRunnable = new Runnable() {
		@Override
		public void run() {
			System.out.println("活动编号："+activeId);
			String sql = "select *from "+ActiveListInfo.TableName+" where id="+activeId;
			List<Map<String,Object>> list = dbHelper.selectRow(sql, null);
			if(list !=null &&list.size()>0){
				if(list.get(0).get("activedetail")!=null){
					String content = list.get(0).get("activedetail").toString();
					Message msg = new Message();
					msg.what = 0 ;
					msg.obj = content;
					handler.sendMessage(msg);
				}
			}
				String msgStr = RequestServerFromHttp.getActiveDetail(UserBeanInfo.getInstant().getCurrentStoreId(), activeId);
				System.out.println("活动分享详情："+msgStr);
				if(msgStr.length()>5){
					ContentValues values = new ContentValues();
					values.put("activedetail", msgStr);
					dbHelper.update(ActiveListInfo.TableName, values, "id=?", new String[]{activeId});
					Message msg = new Message();
					msg.what = 0 ;
					msg.obj = msgStr;
					handler.sendMessage(msg);
				}else{
					handler.sendEmptyMessage(1);
				}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				detailWvView.loadDataWithBaseURL("", msg.obj.toString(),"text/html", "utf-8", "");
			}else if(msg.what == 1){
				
			}
			
			super.handleMessage(msg);
		}
		
	};
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}else if(v.getId() == R.id.bottomBackBtn){
    		finish();
    	}else if(v.getId() == R.id.zixunBtn){
    		
    	}else if(v.getId() == R.id.ShareBtn){
    		showShareDialog(parent);
    	}else if(v.getId() == R.id.sharelayout1){
			
		}else if(v.getId() == R.id.sharelayout2){
			
		}else if(v.getId() == R.id.sharelayout3){
			
		}else if(v.getId() == R.id.sharelayout4){
			
		}/*else if(v.getId() == R.id.sharelayout5){
			
		}*/else if(v.getId() == R.id.sharelayout6){
			showMoreShareDialog();
		}else if(v.getId() == R.id.closeWindowBtn){
			dismiss();
		}else if(v.getId() == R.id.closeWindowBtn1){
			dismiss();
		}else if(v.getId() == R.id.shareWeixin){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareSina){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareTenxun){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareKongjian){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareDuanxin){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareEmail){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareMoreClose){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.telLayout){
			Intent intent = new Intent(Intent.ACTION_CALL,             //创建一个意图                     
					Uri.parse("tel:"+getResources().getString(R.string.dianjia_phone)));
			startActivity(intent);//开始意图(及拨打电话)
		}
		super.onClick(v);
	}
}
