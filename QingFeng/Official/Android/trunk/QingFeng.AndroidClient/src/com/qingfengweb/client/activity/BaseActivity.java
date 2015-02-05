package com.qingfengweb.client.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.qingfengweb.android.R;
import com.qingfengweb.client.fragmengs.AboutUsFragment;
import com.qingfengweb.client.fragmengs.ClientCaseFragment;
import com.qingfengweb.client.fragmengs.CompanyIntrocductionFragment;
import com.qingfengweb.client.fragmengs.ConnectUsFragment;
import com.qingfengweb.client.fragmengs.SampleListFragment;
import com.qingfengweb.client.fragmengs.ServicesFragment;

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
	CompanyIntrocductionFragment companyF = null;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	@SuppressLint("NewApi") 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		companyF = CompanyIntrocductionFragment.newInstance("CompanyIntrocductionFragment");
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
		list.add(companyF);
		list.add(AboutUsFragment.newInstance("aboutUsFragmemt"));
		list.add(ServicesFragment.newInstance("services"));
		list.add(ClientCaseFragment.newInstance("clientcase"));
		list.add(ConnectUsFragment.newInstance("connectus"));
	}
}
