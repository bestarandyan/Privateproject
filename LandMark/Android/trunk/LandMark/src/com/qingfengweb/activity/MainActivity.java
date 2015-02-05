package com.qingfengweb.activity;


import java.io.File;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.CacheManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.qingfengweb.constant.ConstantClass;
import com.qingfengweb.database.MyAppLication;

@SuppressLint("NewApi")
public class MainActivity extends TabActivity implements OnClickListener,OnTabChangeListener{
    public static TabHost mHost;
    private Button tab0Btn,tab1Btn,tab2Btn,tab3Btn,tab4Btn,tab5Btn,tab6Btn;
	private Intent tab0Intent,tab1Intent,tab2Intent,tab3Intent,tab4Intent,tab5Intent,tab6Intent;
//	public  ImageButton leftBtn;
//	public static  TextView titleTv;
	RelativeLayout topLayout;
	Bitmap btiampBitmap = null;
	public  HorizontalScrollView scrollView;
	ImageView leftImg,rightImg;
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
//    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        findview();
        initTabs();
	    setupIntent();
    }
    @SuppressWarnings("deprecation")
	private void initTabs() {
	    mHost = (TabHost) findViewById(android.R.id.tabhost);
	    mHost.setOnTabChangedListener(this);
	    mHost.setup(this.getLocalActivityManager());
	    tab0Intent = new Intent(this, HomeActivity.class);
	    tab1Intent = new Intent(this, CuXiaoActivity.class);
	    tab2Intent = new Intent(this, SpecialGoodsActivity.class);
	    tab3Intent = new Intent(this, ShoppingGuideActivity.class);
	    tab4Intent = new Intent(this, FoodsActivity.class);
	    tab5Intent = new Intent(this, MemberAreaActivity.class);
	    tab6Intent = new Intent(this, PaymentSettleActivity.class);
	    }
	private void setupIntent() {
		mHost.addTab(buildTabSpec("tab0","homePage",
		        R.drawable.bottom_btn1,tab0Intent));
		
		mHost.addTab(buildTabSpec("tab1","aaaa",
		        R.drawable.bottom_btn1,tab1Intent));
		
		mHost.addTab(buildTabSpec("tab2","bbbb",
		        R.drawable.bottom_btn2,tab2Intent));
		
		mHost.addTab(buildTabSpec("tab3","cccc",
		        R.drawable.bottom_btn3,tab3Intent));
		
		mHost.addTab(buildTabSpec("tab4","dddd",
		        R.drawable.bottom_btn4,tab4Intent));
		
		mHost.addTab(buildTabSpec("tab5","ffff",
		        R.drawable.bottom_btn5,tab5Intent));
		
		mHost.addTab(buildTabSpec("tab6","gggg",
		        R.drawable.bottom_btn6,tab6Intent));
		
		}
	@Override
	public void onTabChanged(String tabId) {
		if(tabId.equals("tab0")){
			tab0Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        scrollView.scrollTo(0, 0);
	        leftImg.setVisibility(View.GONE);
	        rightImg.setVisibility(View.VISIBLE);
	        if(HomeActivity.detailWvView!=null && HomeActivity.detailWvView.getUrl()!=null && !HomeActivity.detailWvView.getUrl().equals(ConstantClass.HOME_URL)){
	        	 HomeActivity.loadWebView();//加载webView
	        }
		}else if(tabId.equals("tab1")){
			tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}else if(tabId.equals("tab2")){
			this.mHost.setCurrentTabByTag("tab2");
	        tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}else if(tabId.equals("tab3")){
			this.mHost.setCurrentTabByTag("tab3");
	        tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}else if(tabId.equals("tab4")){
			this.mHost.setCurrentTabByTag("tab4");
	        tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}else if(tabId.equals("tab5")){
			tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}else if(tabId.equals("tab6")){
			tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}
	}

	
	private TabHost.TabSpec buildTabSpec(String tag, String resLabel, int resIcon,
		    final Intent content) {
		return this.mHost
		        .newTabSpec(tag)
		        .setIndicator(resLabel,
		                getResources().getDrawable(resIcon))
		        .setContent(content);
		}
    private void findview(){
         scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
         leftImg = (ImageView) findViewById(R.id.leftImg);
         rightImg = (ImageView) findViewById(R.id.rightImg);
         scrollView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(scrollView.getScrollX()>10){
						leftImg.setVisibility(View.VISIBLE);
					}else{
						leftImg.setVisibility(View.GONE);
					}
					int subWidth = scrollView.getChildAt(0).getRight();
					int x = scrollView.getScrollX();
					if(subWidth - x - 	scrollView.getWidth() == 0){
						rightImg.setVisibility(View.GONE);
					}else{
						rightImg.setVisibility(View.VISIBLE);
					}
				}else if(event.getAction() == MotionEvent.ACTION_MOVE){
					if(scrollView.getScrollX()>10){
						leftImg.setVisibility(View.VISIBLE);
					}else{
						leftImg.setVisibility(View.GONE);
					}
					int subWidth = scrollView.getChildAt(0).getRight();
					int x = scrollView.getScrollX();
					if(subWidth - x - 	scrollView.getWidth() == 0){
						rightImg.setVisibility(View.GONE);
					}else{
						rightImg.setVisibility(View.VISIBLE);
					}
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					if(scrollView.getScrollX()>10){
						leftImg.setVisibility(View.VISIBLE);
					}else{
						leftImg.setVisibility(View.GONE);
					}
					int subWidth = scrollView.getChildAt(0).getRight();
					int x = scrollView.getScrollX();
					if(subWidth - x - 	scrollView.getWidth() == 0){
						rightImg.setVisibility(View.GONE);
					}else{
						rightImg.setVisibility(View.VISIBLE);
					}
				}
				return false;
			}
		});
         tab0Btn = (Button) findViewById(R.id.radio_button0);
         tab1Btn = (Button) findViewById(R.id.radio_button1);
         tab2Btn = (Button) findViewById(R.id.radio_button2);
         tab3Btn = (Button) findViewById(R.id.radio_button3);
         tab4Btn = (Button) findViewById(R.id.radio_button4);
         tab5Btn = (Button) findViewById(R.id.radio_button5);
         tab6Btn = (Button) findViewById(R.id.radio_button6);
         tab0Btn.setOnClickListener(this);
         tab1Btn.setOnClickListener(this);
         tab2Btn.setOnClickListener(this);
         tab3Btn.setOnClickListener(this);
         tab4Btn.setOnClickListener(this);
         tab5Btn.setOnClickListener(this);
         tab6Btn.setOnClickListener(this);
         LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MyAppLication.getInstant().getScreenW()/5, 
        		 LayoutParams.WRAP_CONTENT);
         tab0Btn.setLayoutParams(params);
         tab1Btn.setLayoutParams(params);
         tab2Btn.setLayoutParams(params);
         tab3Btn.setLayoutParams(params);
         tab4Btn.setLayoutParams(params);
         tab5Btn.setLayoutParams(params);
         tab6Btn.setLayoutParams(params);
//         RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(MyAppLication.getInstant().getScreenW()/5, 
//        		 LayoutParams.WRAP_CONTENT);
//         leftBtn = (ImageButton)findViewById(R.id.backBtn);
//	     leftBtn.setOnClickListener(this);
	     topLayout = (RelativeLayout) findViewById(R.id.topLayout);
    }
	@Override
	public void onClick(View v) {
		if(v == tab0Btn){
			this.mHost.setCurrentTabByTag("tab0");
			tab0Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        if(HomeActivity.detailWvView!=null && HomeActivity.detailWvView.getUrl()!=null && !HomeActivity.detailWvView.getUrl().equals(ConstantClass.HOME_URL)){
	        	File file = CacheManager.getCacheFileBaseDir();

	 		   if (file != null && file.exists() && file.isDirectory()) {
	 		    for (File item : file.listFiles()) {
	 		     item.delete();
	 		    }
	 		    file.delete();
	 		   }
//	 		   
////	 		  this.deleteDatabase("webview.db");
//	 		  this.deleteDatabase("webviewCache.db");
	        	HomeActivity.loadWebView();//加载webView
	        }
		}else if(v == tab1Btn){
			this.mHost.setCurrentTabByTag("tab1");
			tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}else if(v == tab2Btn){
			this.mHost.setCurrentTabByTag("tab2");
			tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}else if(v == tab3Btn){
			this.mHost.setCurrentTabByTag("tab3");
			tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}else if(v == tab4Btn){
			this.mHost.setCurrentTabByTag("tab4");
			tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}else if(v == tab5Btn){
			this.mHost.setCurrentTabByTag("tab5");
			tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}else if(v == tab6Btn){
			this.mHost.setCurrentTabByTag("tab6");
			tab0Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
	        tab6Btn.setBackgroundResource(R.drawable.bottom_bg1);
	        while(HomeActivity.detailWvView.canGoForward()){
	        	 HomeActivity.detailWvView.goForward();//加载webView
	        }
		}/*else if(v == leftBtn){
		if(this.mHost.getCurrentTab() == 0){//首页
			if(!HomeActivity.detailWvView.canGoBack() || HomeActivity.detailWvView.getUrl().equals(ConstantClass.HOME_URL)){
				showDialog();
			}else{
				HomeActivity.detailWvView.goBack();
			}
		}if(this.mHost.getCurrentTab() == 2){//特惠商品
				if(SpecialGoodsActivity.detailWv!=null && (SpecialGoodsActivity.detailWv.getVisibility() == View.VISIBLE)){
					SpecialGoodsActivity.detailWv.setVisibility(View.GONE);
				}else{
					MainActivity.titleTv.setText("上海置地广场商厦");
					this.mHost.setCurrentTabByTag("tab0");
					tab0Btn.setBackgroundResource(R.drawable.bottom_bg1);
			        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
				}
			}else if(this.mHost.getCurrentTab() == 5){//会员专区
				if(MemberAreaActivity.webView!=null && MemberAreaActivity.webView.canGoBack()){
					MemberAreaActivity.webView.goBack();
				}else{
					MainActivity.titleTv.setText("上海置地广场商厦");
					this.mHost.setCurrentTabByTag("tab0");
					tab0Btn.setBackgroundResource(R.drawable.bottom_bg1);
			        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
				}
			}else if(this.mHost.getCurrentTab() == 6){//微博
				if(PaymentSettleActivity.webView!=null && PaymentSettleActivity.webView.canGoBack()){
					PaymentSettleActivity.webView.goBack();
				}else{
					MainActivity.titleTv.setText("上海置地广场商厦");
					this.mHost.setCurrentTabByTag("tab0");
					tab0Btn.setBackgroundResource(R.drawable.bottom_bg1);
			        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
			        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
				}
			}else{
				this.mHost.setCurrentTabByTag("tab0");
				MainActivity.titleTv.setText("上海置地广场商厦");
				tab0Btn.setBackgroundResource(R.drawable.bottom_bg1);
		        tab1Btn.setBackgroundResource(R.drawable.bottom_bg);
		        tab2Btn.setBackgroundResource(R.drawable.bottom_bg);
		        tab3Btn.setBackgroundResource(R.drawable.bottom_bg);
		        tab4Btn.setBackgroundResource(R.drawable.bottom_bg);
		        tab5Btn.setBackgroundResource(R.drawable.bottom_bg);
		        tab6Btn.setBackgroundResource(R.drawable.bottom_bg);
			}
			
    	}*/
		
	}
	
	private void showDialog(){
    	AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
		alerBuilder.setTitle("提示！");
		alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		alerBuilder.setMessage("确定要退出程序吗？");
		alerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
	/*	if(keyCode == KeyEvent.KEYCODE_BACK && mainWv.canGoBack()){
			mainWv.goBack();
//			mainWv.clearHistory();
//			mainWv.clearFocus();
//			mainWv.clearMatches();
//			mainWv.loadDataWithBaseURL(null, "","text/html", "utf-8",null);  
//			mainWv.setVisibility(View.INVISIBLE);
			return true;
		}else */if(keyCode == KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
			alerBuilder.setTitle("提示！");
			alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
			alerBuilder.setMessage("确定要退出程序吗？");
			alerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					File file = CacheManager.getCacheFileBaseDir();

					   if (file != null && file.exists() && file.isDirectory()) {
					    for (File item : file.listFiles()) {
					    	System.out.println(item.getAbsoluteFile());
					     item.delete();
					    }
					    file.delete();
					   }
					   
					   MainActivity.this.deleteDatabase("webview.db");
					   MainActivity.this.deleteDatabase("webviewCache.db");
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
		return super.onKeyDown(keyCode, event);
	}
	
    
}
