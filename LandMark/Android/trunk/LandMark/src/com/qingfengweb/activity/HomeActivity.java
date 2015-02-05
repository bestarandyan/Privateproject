/**
 * 
 */
package com.qingfengweb.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CacheManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengweb.constant.ConstantClass;
import com.qingfengweb.database.MyAppLication;

/**
 * @author 刘星星
 * @createDate 2013、8、5
 *
 */
@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
public class HomeActivity extends Activity implements OnClickListener{
	public static WebView detailWvView;
	static LinearLayout proLayout;
	private static TextView failText;
	static boolean loadSuccess = true;
	public static DisplayMetrics metrics = null;
	ImageButton leftBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_home);
		metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
		detailWvView = (WebView) findViewById(R.id.homeWebView);
		proLayout = (LinearLayout) findViewById(R.id.proLayout1);
		failText = (TextView) findViewById(R.id.connectFailText);
		leftBtn = (ImageButton) findViewById(R.id.backBtn);
		leftBtn.setOnClickListener(this);
		loadWebView();//加载webView
		
		failText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadSuccess = true;
				failText.setVisibility(View.GONE);
				proLayout.setVisibility(View.VISIBLE);
				detailWvView.loadUrl(ConstantClass.HOME_URL);
			}
		});
		
		
	}
	public void deleteCache(){
		File file = CacheManager.getCacheFileBaseDir();

		   if (file != null && file.exists() && file.isDirectory()) {
		    for (File item : file.listFiles()) {
		     item.delete();
		    }
		    file.delete();
		   }
		   
		  this.deleteDatabase("webview.db");
		  this.deleteDatabase("webviewCache.db");
	}
	
	public static void loadWebView(){
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
//        int mDensity = metrics.densityDpi;
//        if (mDensity == 240) { 
//        	detailWvView.getSettings().setDefaultZoom(ZoomDensity.FAR);
//        } else if (mDensity == 160) {
//        	detailWvView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
//        } else if(mDensity == 120) {
//        	detailWvView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
//        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
//        	detailWvView.getSettings().setDefaultZoom(ZoomDensity.FAR); 
//        }else if (mDensity == DisplayMetrics.DENSITY_TV){
//        	detailWvView.getSettings().setDefaultZoom(ZoomDensity.FAR); 
//        }
//    	detailWvView.loadDataWithBaseURL(null, "","text/html", "utf-8",null);  
    	
    	detailWvView.clearHistory();
		int sysVersion = Integer.parseInt(VERSION.SDK);  
		if(sysVersion>=11){
			detailWvView.getSettings().setDisplayZoomControls(false);
		}
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
	        		loadSuccess = false;
	        		failText.setVisibility(View.VISIBLE);
	        		failText.setBackgroundColor(Color.WHITE);
					super.onReceivedError(view, errorCode, description, failingUrl);
				}


				@Override
	        	public void onPageFinished(WebView view, String url) {
					if(loadSuccess){
						failText.setVisibility(View.GONE);
					}else{
						failText.setVisibility(View.VISIBLE);
					}
					proLayout.setVisibility(View.GONE);
	        	super.onPageFinished(view, url);
	        	
	        	
	        	}
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
//					view.loadUrl(url);
					return super.shouldOverrideUrlLoading(view, url);
				}
	        	
	        });
		System.out.println(detailWvView.getWidth()+"---height == "+detailWvView.getHeight());
		detailWvView.loadUrl(ConstantClass.HOME_URL);
		
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		System.out.println();
		super.onWindowFocusChanged(hasFocus);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(!detailWvView.canGoBack() || detailWvView.getUrl().equals(ConstantClass.HOME_URL)){
				showDialog();
			}else{
				detailWvView.setInitialScale(25);//为25%，最小缩放等级 
				detailWvView.goBack();
			}
		}
		return true;
	}
	private void showDialog(){
    	AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
		alerBuilder.setTitle("提示！");
		alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		alerBuilder.setMessage("确定要退出程序吗？");
		alerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteCache();
				android.os.Process.killProcess(android.os.Process.myPid());
				finish();
				System.gc();
			}
		});
		alerBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return ;
			}
		});
			Dialog dialog = alerBuilder.create();
			dialog.show();
    }
	@Override
	public void onClick(View v) {
	if(v == leftBtn){
		if(!detailWvView.canGoBack() || detailWvView.getUrl().equals(ConstantClass.HOME_URL)){
			showDialog();
		}else{
			detailWvView.setInitialScale(25);//为25%，最小缩放等级 
			detailWvView.goBack();
		}
	}
		
	}
}
