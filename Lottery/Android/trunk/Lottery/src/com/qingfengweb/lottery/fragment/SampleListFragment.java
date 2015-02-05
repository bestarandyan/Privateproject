package com.qingfengweb.lottery.fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.activity.BaseActivity;
import com.qingfengweb.lottery.activity.LoginActivity;
import com.qingfengweb.lottery.activity.MainActivity;
import com.qingfengweb.lottery.data.ConstantValues;
import com.qingfengweb.lottery.data.MyApplication;

public class SampleListFragment extends ListFragment  implements OnItemSelectedListener{

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}
	public static SampleAdapter adapter;
	public static ListView listView= null;
	public static boolean flag = false;//标志是否是从其他界面跳进来的  而不是从首页进来的
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		 	adapter = new SampleAdapter(getActivity());
			adapter.add(new SampleItem(getResources().getString(R.string.menu_tab1_str), 
					getResources().getString(R.string.menu_tab1_Bom_str),
					R.drawable.cai_menu01));
			adapter.add(new SampleItem(getResources().getString(R.string.menu_tab2_str), 
					getResources().getString(R.string.menu_tab2_Bom_str),
					R.drawable.cai_menu2));
			adapter.add(new SampleItem(getResources().getString(R.string.menu_tab3_str), 
					getResources().getString(R.string.menu_tab3_Bom_str),
					R.drawable.cai_menu3));
			adapter.add(new SampleItem(getResources().getString(R.string.menu_tab4_str), 
					getResources().getString(R.string.menu_tab4_Bom_str),
					R.drawable.cai_menu4));
			/*adapter.add(new SampleItem(getResources().getString(R.string.menu_tab5_str), 
					getResources().getString(R.string.menu_tab5_Bom_str),
					R.drawable.qf_menu_ico5));*/
			listView = getListView();
			listView.setAdapter(adapter);
			listView.setCacheColorHint(0);
			listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			MainActivity.openUserBtn.setVisibility(View.VISIBLE);
			MainActivity.rightLine.setVisibility(View.VISIBLE);
			
	}
	private class SampleItem {
		public String topStr;
		public int iconRes;
		public String bottomStr;
		public SampleItem(String topStr,String bottom, int iconRes) {
			this.topStr = topStr; 
			this.iconRes = iconRes;
			this.bottomStr = bottom;
		}
	}
	
	@Override
	public void setSelection(int position) {
		// TODO Auto-generated method stub
		super.setSelection(position);
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(position == 2){
			String username = MyApplication.getInstance().getCurrentUserName();
			String token = MyApplication.getInstance().getCurrentToken();
			if(username==null || username.equals("") || token == null || token.equals("")){
				Intent intent = new Intent(this.getActivity(),LoginActivity.class);
				intent.putExtra("activity", "FragmentForMyLottery");
				startActivity(intent);
				return;
//				Message msg = new Message();
//				msg.arg1 = position;
//				msg.what = 0;
//				handler.sendMessageDelayed(msg, 500);
			}else{
				replaceFragment(BaseActivity.list.get(position),position);
			}
		}
		for (int i = 0; i < l.getCount(); i++) {
			if(position == i){
				v.setBackgroundColor(getResources().getColor(ConstantValues.MENU_TAB_BG_ARRAY[i]));
				((ImageView)v.findViewById(R.id.img)).setImageResource(ConstantValues.MENU_IMG_SELECTED[i]);
			}else{
				l.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
				((ImageView)l.getChildAt(i).findViewById(R.id.img)).setImageResource(ConstantValues.MENU_IMG_NOT_SELECTED[i]);
			}
		}
		if(position == 0){
			MainActivity.titleTv.setVisibility(View.GONE);
			MainActivity.topLogoImg.setVisibility(View.VISIBLE);
			MainActivity.topLayout.setBackgroundColor(Color.parseColor("#023F30"));
			MainActivity.leftLine.setBackgroundResource(R.drawable.cai_yaoyiyao_line);
			MainActivity.openUserBtn.setVisibility(View.VISIBLE);
			MainActivity.rightLine.setVisibility(View.VISIBLE);
			replaceFragment(BaseActivity.list.get(position),position);
		}else if(position == 1){
			MainActivity.titleTv.setText("开奖信息");
			MainActivity.titleTv.setVisibility(View.VISIBLE);
			MainActivity.topLogoImg.setVisibility(View.GONE);
			MainActivity.topLayout.setBackgroundColor(Color.parseColor("#023F30"));
			MainActivity.leftLine.setBackgroundResource(R.drawable.cai_yaoyiyao_line);
			MainActivity.openUserBtn.setVisibility(View.GONE);
			MainActivity.rightLine.setVisibility(View.GONE);
			replaceFragment(BaseActivity.list.get(position),position);
		}else if(position == 2){
			MainActivity.titleTv.setText("我的彩票");
			MainActivity.titleTv.setVisibility(View.VISIBLE);
			MainActivity.topLogoImg.setVisibility(View.GONE);
			MainActivity.topLayout.setBackgroundResource(R.drawable.cai_top_bg);
			MainActivity.leftLine.setBackgroundResource(R.drawable.cai_top_line);
			MainActivity.openUserBtn.setVisibility(View.GONE);
			MainActivity.rightLine.setVisibility(View.GONE);
		}else if(position == 3){
			MainActivity.titleTv.setText("玩法规则");
			MainActivity.titleTv.setVisibility(View.VISIBLE);
			MainActivity.topLogoImg.setVisibility(View.GONE);
			MainActivity.topLayout.setBackgroundResource(R.drawable.guize_top_bg);
			MainActivity.leftLine.setBackgroundResource(R.drawable.guize_top_line);
			MainActivity.openUserBtn.setVisibility(View.GONE);
			MainActivity.rightLine.setVisibility(View.GONE);
			replaceFragment(BaseActivity.list.get(position),position);
		}
		
		
		super.onListItemClick(l, v, position, id);
	}
	public static Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				replaceFragment(BaseActivity.list.get(msg.arg1),msg.arg1);
			}else if(msg.what == 1){//登陆成功后
				MainActivity.titleTv.setText("我的彩票");
				MainActivity.titleTv.setVisibility(View.VISIBLE);
				MainActivity.topLogoImg.setVisibility(View.GONE);
				MainActivity.topLayout.setBackgroundResource(R.drawable.cai_top_bg);
				MainActivity.leftLine.setBackgroundResource(R.drawable.cai_top_line);
				MainActivity.openUserBtn.setVisibility(View.GONE);
				MainActivity.rightLine.setVisibility(View.GONE);
				replaceFragment(BaseActivity.list.get(msg.arg1),msg.arg1);
				listView.getChildAt(2).setBackgroundColor(Color.parseColor("#3A967D"));
				((ImageView)listView.getChildAt(2).findViewById(R.id.img)).setImageResource(R.drawable.cai_menu03);
				listView.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
				((ImageView)listView.getChildAt(0).findViewById(R.id.img)).setImageResource(R.drawable.cai_menu1);
				listView.getChildAt(1).setBackgroundColor(Color.TRANSPARENT);
				((ImageView)listView.getChildAt(1).findViewById(R.id.img)).setImageResource(R.drawable.cai_menu2);
				listView.getChildAt(3).setBackgroundColor(Color.TRANSPARENT);
				((ImageView)listView.getChildAt(3).findViewById(R.id.img)).setImageResource(R.drawable.cai_menu4);
			}
			super.handleMessage(msg);
		}
		
	};
	public static void replaceFragment(Fragment fragment,int id){
		BaseActivity.sm.showContent(true);
//		BaseActivity.fragmentManager.findFragmentByTag("").isAdded()
		if(!fragment.isAdded()){
			BaseActivity.fragmentManager.beginTransaction().hide(BaseActivity.currentFragment).commitAllowingStateLoss();
			BaseActivity.fragmentManager
			.beginTransaction()
//			.add(R.id.content_frame, fragment)
//			.show(fragment)
			.add(R.id.content_frame, fragment)
			.commitAllowingStateLoss();
		}else{
			int a = BaseActivity.fragmentManager.beginTransaction().hide(BaseActivity.currentFragment).commitAllowingStateLoss();
			BaseActivity.fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
		}
		BaseActivity.currentFragment = fragment;
		if(id == 1){
//			CustomViewAbove.currentPage = AboutUsFragment.pageValue;
		}else if(id == 4){
//			CustomViewAbove.currentPage = ConnectUsFragment.pageValue;
		}else{
			CustomViewAbove.currentPage = -1;
		}
	}
	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}
		@Override
		public SampleItem getItem(int position) {
			// TODO Auto-generated method stub
			return super.getItem(position);
		}
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.item_main_menu, null);
            }
            TextView topTv = (TextView) v.findViewById(R.id.topTv);
            TextView bottomTv = (TextView) v.findViewById(R.id.bottomTv);
            ImageView img = (ImageView) v.findViewById(R.id.img);
            topTv.setText(getItem(position).topStr);
            bottomTv.setText(getItem(position).bottomStr);
//            topTv.setCompoundDrawablesWithIntrinsicBounds(((Item) item).mIconRes, 0, 0, 0);
            img.setImageResource(getItem(position).iconRes);
//            v.setTag(R.id.mdActiveViewPosition, position);
            
	        if(position == 0 && !flag){
	        	v.setBackgroundColor(getContext().getResources().getColor(ConstantValues.MENU_TAB_BG_ARRAY[position]));
	        }
	        img.requestFocus();
	        img.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for (int i = 0; i < SampleListFragment.this.getListView().getCount(); i++) {
						if(position == i){
							listView.getChildAt(i).setBackgroundColor(getResources().getColor(ConstantValues.MENU_TAB_BG_ARRAY[i]));
						}else{
							listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
						}
					}
					replaceFragment(BaseActivity.list.get(position),position);
				}
			});
			return v;
		}

	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		for (int i = 0; i < arg0.getCount(); i++) {
			if(arg2 == i){
				arg1.setBackgroundColor(getResources().getColor(ConstantValues.MENU_TAB_BG_ARRAY[i]));
				((ImageView)arg1.findViewById(R.id.img)).setImageResource(ConstantValues.MENU_IMG_SELECTED[i]);
			}else{
				arg0.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
				((ImageView)arg0.getChildAt(i).findViewById(R.id.img)).setImageResource(ConstantValues.MENU_IMG_NOT_SELECTED[i]);
			}
		}
		if(arg2 == 0){
			MainActivity.titleTv.setText("656彩票行");
			Toast.makeText(getActivity(), "656彩票行", 3000).show();
		}else if(arg2 == 1){
			MainActivity.titleTv.setText("开奖信息");
		}else if(arg2 == 2){
			MainActivity.titleTv.setText("我的彩票");
		}else if(arg2 == 3){
			MainActivity.titleTv.setText("玩法规则");
		}
		/*if(position == 0){
			CustomViewAbove.isHaveRight = true;
			
		}else {
			CustomViewAbove.isHaveRight = false;
		}*/
		
		replaceFragment(BaseActivity.list.get(arg2),arg2);
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
