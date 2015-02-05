package com.qingfengweb.lottery.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.view.Window;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.fragment.FragmentForHelp;
import com.qingfengweb.lottery.fragment.FragmentForLotteryInfo;
import com.qingfengweb.lottery.fragment.FragmentForLotteryType;
import com.qingfengweb.lottery.fragment.FragmentForMyLottery;
import com.qingfengweb.lottery.fragment.SampleListFragment;

public class BaseActivity extends SlidingFragmentActivity {
	private int mTitleRes;
	protected ListFragment mFrag;
	public static Fragment currentFragment = null;
	public static FragmentManager fragmentManager = null;
	public static SlidingMenu sm = null;
	public static List<Fragment> list = new ArrayList<Fragment>();
	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}
	FragmentForLotteryType mainFragment = null;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	@SuppressLint("NewApi") 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyApplication.getInstance().setScreenW(dm.widthPixels);
		MyApplication.getInstance().setScreenH(dm.heightPixels);
		System.out.println("手机屏幕宽度"+dm.widthPixels);
		System.out.println("手机屏幕高低"+dm.heightPixels);
		setTitle(mTitleRes);
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new SampleListFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}
		// customize the SlidingMenu
		fragmentManager = getSupportFragmentManager();
		sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset_left);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mainFragment = FragmentForLotteryType.newInstance("CompanyIntrocductionFragment");
		addFragment();//将菜单中的每一项指定一个Fragment
//		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
//	public  void replaceContent(Fragment f){
//		getSupportFragmentManager()
//		.beginTransaction().add(f, "TestFragment")
////		.replace(R.id.content_frame, f)
//		.commit();
//	}
	/**
	 * 给菜单中的每一项指定一个Fragment
	 */
	public void addFragment(){
		list.add(mainFragment);
		list.add(FragmentForLotteryInfo.newInstance("FragmentForLotteryInfo"));
		list.add(FragmentForMyLottery.newInstance("FragmentForMyLottery"));
		list.add(FragmentForHelp.newInstance("FragmentForHelp"));
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
