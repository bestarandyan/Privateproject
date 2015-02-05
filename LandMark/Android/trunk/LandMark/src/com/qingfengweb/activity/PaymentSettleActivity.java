/**
 * 
 */
package com.qingfengweb.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.ImageButton;

/**
 * @author 刘星星
 * @createDate 2013/7/9
 *
 */
@SuppressLint("NewApi")
public class PaymentSettleActivity extends BaseActivity implements OnClickListener{
    public static WebView webView = null;
    ImageButton leftBtn;
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.a_paymentsettle);
    	leftBtn = (ImageButton) findViewById(R.id.backBtn);
		leftBtn.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.wv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) { 
        	webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
        } else if (mDensity == 160) {
        	webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
        	webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
        	webView.getSettings().setDefaultZoom(ZoomDensity.FAR); 
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
        	webView.getSettings().setDefaultZoom(ZoomDensity.FAR); 
        }


		int sysVersion = Integer.parseInt(VERSION.SDK);  
		if(sysVersion>=11){
			webView.getSettings().setDisplayZoomControls(false);
		}
        webView.setWebViewClient(new WebViewClient(){
        	@Override
        	public void onReceivedSslError(WebView view,
        			SslErrorHandler handler, SslError error) {
        		
        		super.onReceivedSslError(view, handler, error);
        	}
        });
//        webView.loadDataWithBaseURL("file:///"+url, null, "text/html", "utf-8", null);
//        webView.loadUrl("file:///"+url);
//        webView.loadUrl("http://sports.sina.com.cn/nba/");
        ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
        	dialogWronFun("未检测到网络，请检查网络连接！", PaymentSettleActivity.this);
        }else{
//        	webView.loadUrl("http://www.landmark-sh.com/m/hkjs/");
        	webView.loadUrl("http://m.weibo.cn/u/2477490290");
//        	webView.loadUrl("http://www.baidu.com");
        }
        
    }
//    @Override
//    protected void onResume() {
//    	 ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//         if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
//         	dialogWronFun("未检测到网络，请检查网络连接！", MemberAreaActivity.this);
//         }
//    	super.onResume();
//    }
   @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	   if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
			webView.goBack();
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_BACK  && !webView.canGoBack()){
//			AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
//			alerBuilder.setTitle("提示！");
//			alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//			alerBuilder.setMessage("确定要退出程序吗？");
//			alerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					android.os.Process.killProcess(android.os.Process.myPid());
//					finish();
//					System.gc();
//				}
//			});
//			alerBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					return ;
//				}
//			});
//			Dialog dialog = alerBuilder.create();
//			
//			dialog.show();
			MainActivity.mHost.setCurrentTab(0);
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		if(v == leftBtn){
			if(webView.canGoBack()){
				webView.goBack();
			}else{
				MainActivity.mHost.setCurrentTab(0);
			}
		}
	}
}
