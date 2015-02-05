/**
 * 
 */
package com.qingfengweb.weddingideas.activity;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.customview.MyVerticalScrollView;
import com.qingfengweb.weddingideas.data.MyApplication;
import com.qingfengweb.weddingideas.data.RequestServerFromHttp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 刘星星
 *
 */
@TargetApi(Build.VERSION_CODES.ECLAIR) @SuppressLint("SetJavaScriptEnabled") public class AboutUsActivity extends BaseActivity implements OnTouchListener{
	WebView dspView = null;
	Button goumaiBtn,shouhou;
	TextView titleTv,addressTv,phoneTv,trafficTv,introTv;
	//UI相关
		//地图相关
		MapView mMapView = null;	// 地图View
		//搜索相关
		MKSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用
		MyVerticalScrollView scrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_aboutus);
		 initView();
		 initMapData();
		 initData();
	}
	private void initData(){
//		new Thread(getDataRunnable).start();
	}
	@SuppressWarnings("deprecation")
	private void initView(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		goumaiBtn = (Button) findViewById(R.id.goumaiBtn);
		goumaiBtn.setOnClickListener(this);
		shouhou = (Button) findViewById(R.id.shouhouBtn);
		shouhou.setOnClickListener(this);
		scrollView = (MyVerticalScrollView) findViewById(R.id.scrollView);
		scrollView.setOnTouchListener(this);
		titleTv = (TextView) findViewById(R.id.topView);
		addressTv= (TextView) findViewById(R.id.addressTv);
		phoneTv= (TextView) findViewById(R.id.phoneTv);
		trafficTv= (TextView) findViewById(R.id.trafficTv);
		introTv= (TextView) findViewById(R.id.introTv);
		titleTv.setText("关于"+LoadingActivity.configList.get(0).get("store_name"));
		addressTv.setText(LoadingActivity.configList.get(0).get("store_address"));
		phoneTv.setText(LoadingActivity.photoOne+"   "+LoadingActivity.photoTwo);
		trafficTv.setText(LoadingActivity.configList.get(0).get("store_traffic"));
		introTv.setText(LoadingActivity.configList.get(0).get("store_intro"));
		LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(MyApplication.getInstance().getScreenW()/2, LayoutParams.WRAP_CONTENT);
		param1.setMargins(0, 10, 0, 0);
		LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(MyApplication.getInstance().getScreenW()/2, LayoutParams.WRAP_CONTENT);
		param2.setMargins(0, 10, 0, 0);
		goumaiBtn.setLayoutParams(param1);
		shouhou.setLayoutParams(param2);
//		dspView = (WebView) findViewById(R.id.description);
//		dspView.setBackgroundColor(0);
//		dspView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
//		dspView.getSettings().setJavaScriptEnabled(true);
//		dspView.loadDataWithBaseURL(null,dspStr, "text/html",  "utf-8", null);
	}
	/**
	 * 获取影楼数据
	 */
	Runnable getDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msgStr = RequestServerFromHttp.getYinglouData("", "");
			
		}
	};
	private void initMapData(){
		DemoApplication app = (DemoApplication)this.getApplication();
		   //地图初始化
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.getController().enableClick(true);
        mMapView.getController().setZoom(20);
        mMapView.setOnTouchListener(this);
        mMapView.setOnClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener() {
            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
            }
            
			public void onGetAddrResult(MKAddrInfo res, int error) {
				if (error != 0) {
					String str = String.format("错误号：%d", error);
					Toast.makeText(AboutUsActivity.this, str, Toast.LENGTH_LONG).show();
					return;
				}
				//地图移动到该点
				mMapView.getController().animateTo(res.geoPt);	
				if (res.type == MKAddrInfo.MK_GEOCODE){
					//地理编码：通过地址检索坐标点
					String strInfo = String.format("纬度：%f 经度：%f", res.geoPt.getLatitudeE6()/1e6, res.geoPt.getLongitudeE6()/1e6);
					Toast.makeText(AboutUsActivity.this, strInfo, Toast.LENGTH_LONG).show();
				}
				if (res.type == MKAddrInfo.MK_REVERSEGEOCODE){
					//反地理编码：通过坐标点检索详细地址及周边poi
					String strInfo = res.strAddr;
					Toast.makeText(AboutUsActivity.this, strInfo, Toast.LENGTH_LONG).show();
					
				}
				//生成ItemizedOverlay图层用来标注结果点
				ItemizedOverlay<OverlayItem> itemOverlay = new ItemizedOverlay<OverlayItem>(null, mMapView);
				//生成Item
				OverlayItem item = new OverlayItem(res.geoPt, "", null);
				//得到需要标在地图上的资源
				Drawable marker = getResources().getDrawable(R.drawable.dingwei);  
				//为maker定义位置和边界
				marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
				//给item设置marker
				item.setMarker(marker);
				//在图层上添加item
				itemOverlay.addItem(item);
				
				//清除地图其他图层
				mMapView.getOverlays().clear();
				//添加一个标注ItemizedOverlay图层
				mMapView.getOverlays().add(itemOverlay);
				//执行刷新使生效
				mMapView.refresh();
			}
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				
			}
			public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
			}
			public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
			}
			public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
			}
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}
			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub
				
			}

        });
      //  31.1602382590,121.4456978307 影楼的经纬度
        int latitude =(int)(31.1602382590*1e6);
        String la = LoadingActivity.configList.get(0).get("store_latitude");
        if(la!=null && !la.equals("") && !la.equals("null")){
        	latitude = (int)(Float.valueOf(la)*1e6);
        }
        
        
        int longitude =(int)( 121.4456978307*1e6);
        String lg = LoadingActivity.configList.get(0).get("store_longitude");
        if(lg!=null && !lg.equals("") && !lg.equals("null")){
        	longitude = (int)(Float.valueOf(lg)*1e6);
        }
        GeoPoint ptCenter = new GeoPoint(latitude, longitude);
		//反Geo搜索
		mSearch.reverseGeocode(ptCenter);
	}
	@Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        mMapView.destroy();
        mSearch.destory();
        super.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v.getId() == R.id.goumaiBtn){//购买咨询
//			showCallDialog(getString(R.string.call_number));
			showCallDialog(LoadingActivity.photoOne);
		}else if(v.getId() == R.id.shouhouBtn){//售后咨询
//			showCallDialog(getString(R.string.call_number));
			showCallDialog(LoadingActivity.photoTwo);
		}else if(v.getId() == R.id.bmapView){
			/*Intent intent = new Intent(this,BaiDuMapActivity.class);
			startActivity(intent);*/
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		return true;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v == mMapView){
			if(event.getAction() == MotionEvent.ACTION_UP){
				Intent intent = new Intent(this,BaiDuMapActivity.class);
				intent.putExtra("latitude", LoadingActivity.configList.get(0).get("store_latitude"));
				intent.putExtra("longitude",LoadingActivity.configList.get(0).get("store_longitude"));
				intent.putExtra("title", "影楼地址");
				startActivity(intent);
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			}
		}else{
//			scrollView.arrawScroll();
		}
		return v.onTouchEvent(event);
	}
}
