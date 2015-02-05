package com.qingfengweb.client.fragmengs;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.qingfengweb.android.R;
import com.qingfengweb.client.activity.BaseActivity;
import com.qingfengweb.client.data.ConstantsValues;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SampleListFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
			adapter.add(new SampleItem(getResources().getString(R.string.menu_tab1_str), 
					getResources().getString(R.string.menu_tab1_Bom_str),
					R.drawable.qf_menu_ico1));
			adapter.add(new SampleItem(getResources().getString(R.string.menu_tab2_str), 
					getResources().getString(R.string.menu_tab2_Bom_str),
					R.drawable.qf_menu_ico2));
			adapter.add(new SampleItem(getResources().getString(R.string.menu_tab3_str), 
					getResources().getString(R.string.menu_tab3_Bom_str),
					R.drawable.qf_menu_ico3));
			adapter.add(new SampleItem(getResources().getString(R.string.menu_tab4_str), 
					getResources().getString(R.string.menu_tab4_Bom_str),
					R.drawable.qf_menu_ico4));
			adapter.add(new SampleItem(getResources().getString(R.string.menu_tab5_str), 
					getResources().getString(R.string.menu_tab5_Bom_str),
					R.drawable.qf_menu_ico5));
		setListAdapter(adapter);
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
	public void onListItemClick(ListView l, View v, int position, long id) {
		for (int i = 0; i < l.getCount(); i++) {
			if(position == i){
				v.setBackgroundColor(getResources().getColor(ConstantsValues.MENU_TAB_BG_ARRAY[i]));
			}else{
				l.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
			}
		}
		/*if(position == 0){
			CustomViewAbove.isHaveRight = true;
			
		}else {
			CustomViewAbove.isHaveRight = false;
		}*/
		
		replaceFragment(BaseActivity.list.get(position),position);
		super.onListItemClick(l, v, position, id);
	}
	public void replaceFragment(Fragment fragment,int id){
		BaseActivity.sm.showContent(true);
//		BaseActivity.fragmentManager.findFragmentByTag("").isAdded()
		if(!fragment.isAdded()){
			BaseActivity.fragmentManager.beginTransaction().hide(BaseActivity.currentFragment).commit();//隐藏掉之前的Fragment
			BaseActivity.fragmentManager
			.beginTransaction()
//			.add(R.id.content_frame, fragment)
//			.show(fragment)
			.add(R.id.content_frame, fragment)
			.commit();
		}else{
			int a = BaseActivity.fragmentManager.beginTransaction().hide(BaseActivity.currentFragment).commit();//隐藏掉之前的Fragment
			BaseActivity.fragmentManager.beginTransaction().show(fragment).commit();//显示当前要显示的Fragment
			System.out.println(a+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		}
		BaseActivity.currentFragment = fragment;
		if(id == 1){//当当前要显示的Fragment为关于我们时，因为“关于我们”界面中是有多个内页的，所以必须让显示与否的标记值等于离开“关于我们”界面时的值
			CustomViewAbove.currentPage = AboutUsFragment.pageValue;
		}else if(id == 4){//同上所述
			CustomViewAbove.currentPage = ConnectUsFragment.pageValue;
		}else{//其他的因为只有一个内页，所以可以直接将左边侧边栏菜单变成可以出现的值。
			CustomViewAbove.currentPage = -1;
		}
	}
	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
        	Object item = getItem(position);
            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.item_main_menu, parent, false);
            }
            TextView topTv = (TextView) v.findViewById(R.id.topTv);
            TextView bottomTv = (TextView) v.findViewById(R.id.bottomTv);
            ImageView img = (ImageView) v.findViewById(R.id.img);
            topTv.setText(getItem(position).topStr);
            bottomTv.setText(getItem(position).bottomStr);
//            topTv.setCompoundDrawablesWithIntrinsicBounds(((Item) item).mIconRes, 0, 0, 0);
            img.setImageResource(getItem(position).iconRes);
//            v.setTag(R.id.mdActiveViewPosition, position);
            
	        if(position == 0){
	        	v.setBackgroundColor(getContext().getResources().getColor(ConstantsValues.MENU_TAB_BG_ARRAY[position]));
	        }
	        img.requestFocus();
	        img.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for (int i = 0; i < SampleListFragment.this.getListView().getCount(); i++) {
						if(position == i){
							SampleListFragment.this.getListView().getChildAt(i).setBackgroundColor(getResources().getColor(ConstantsValues.MENU_TAB_BG_ARRAY[i]));
						}else{
							SampleListFragment.this.getListView().getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
						}
					}
					replaceFragment(BaseActivity.list.get(position),position);
				}
			});
			return v;
		}

	}
}
