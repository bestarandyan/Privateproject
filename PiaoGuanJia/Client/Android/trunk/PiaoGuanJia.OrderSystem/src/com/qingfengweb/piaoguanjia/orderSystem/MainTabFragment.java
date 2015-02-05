package com.qingfengweb.piaoguanjia.orderSystem;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.tab_main)
public class MainTabFragment extends FragmentActivity {
	@ViewInject(R.id.tabhost)
	private FragmentTabHost mTabHost;
	@SuppressWarnings("rawtypes")
	private Class fragmentArray[] = { MainTab01Fragment.class,
			MainTab02Fragment.class, MainTab03Fragment.class,
			MainTab04Fragment.class };
	private int iconArray[] = { R.drawable.tab_item01, R.drawable.tab_item02,
			R.drawable.tab_item03, R.drawable.tab_item04 };
	private String titleArray[] = { "首页", "搜索", "订单", "收藏" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.maintabactivity = this;
		//MOTO XT800 必须设置这里 否则软键盘会自动弹出来
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyApplication.widthPixels = dm.widthPixels;
		MyApplication.heightPixels = dm.heightPixels;
		System.out.println("宽：" + dm.widthPixels + "-高：" + dm.heightPixels);
		LogUtils.customTagPrefix = "OrderSystem"; // 方便调试时过滤 adb logcat 输出
		LogUtils.allowI = false; // 关闭 LogUtils.i(...) 的 adb log 输出
		ViewUtils.inject(this);
		MyApplication.userinfo.getAccountName();
		((MyApplication)getApplication()).setdata();
		setupTabView();
	}
	
	@Override
	protected void onResume() {
		//MOTO XT800 必须设置这里 否则软键盘会自动弹出来
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		MyApplication.clearActivity();
		super.onResume();
	}

	private void setupTabView() {
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.getTabWidget().setDividerDrawable(null);

		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titleArray[i])
					.setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
		}

	}

	private View getTabItemView(int index) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View view = layoutInflater.inflate(R.layout.tab_bottom_nav, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);
		imageView.setImageResource(iconArray[index]);

		TextView textView = (TextView) view.findViewById(R.id.tv_icon);
		textView.setText(titleArray[index]);
		return view;
	}
	
	private int exit_flg = 0;//初始化是0，按一次返回是1
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			if(exit_flg==0){
				exit_flg=1;
				Toast.makeText(MainTabFragment.this, "再次按返回键即可以退出软件", Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						exit_flg=0;
					}
				}, 2000);
			}else if(exit_flg==1){
				finish();
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	
}
