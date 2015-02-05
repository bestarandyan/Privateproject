/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.util.HashMap;
import java.util.List;

import com.zhihuigu.sosoOffice.R;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/1/7
 * 主界面商圈列表的适配器
 *
 */
public class MainListAdapter extends BaseExpandableListAdapter   
{
	public Activity context;
	List<HashMap<String,Object>> groupArray;
	List<List<HashMap<String,Object>>> childArray;
	public MainListAdapter(Activity context,List<HashMap<String,Object>> groupArray,List<List<HashMap<String,Object>>> childArray) {
		this.context = context;
		this.groupArray = groupArray;
		this.childArray = childArray;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childArray.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		String str = childArray.get(groupPosition).get(childPosition).get("name").toString();
		CViewHolder cHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_c_textview, null);
			cHolder = new CViewHolder();
			cHolder.cTv = (TextView) convertView.findViewById(R.id.cTv);
			convertView.setTag(cHolder);
		}else{
			cHolder = (CViewHolder) convertView.getTag();
		}
		cHolder.cTv.setTextColor(Color.rgb(130, 130, 130));
		cHolder.cTv.setTextSize(14);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.setMargins(20, 5, 0, 5);
		cHolder.cTv.setLayoutParams(param);
		cHolder.cTv.setText(str);
		return convertView;
//		return getGenericView(str,Color.rgb(130, 130, 130),20,16);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childArray.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupArray.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupArray.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String string = groupArray.get(groupPosition).get("name").toString();
		CViewHolder cHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_c_textview, null);
			cHolder = new CViewHolder();
			cHolder.cTv = (TextView) convertView.findViewById(R.id.cTv);
			convertView.setTag(cHolder);
		}else{
			cHolder = (CViewHolder) convertView.getTag();
		}
		cHolder.cTv.setTextColor(Color.rgb(142, 174, 3));
		cHolder.cTv.setTextSize(16);
		cHolder.cTv.setText(string);
		return convertView;
//		return getGenericView(string,Color.rgb(142, 176, 1),2,20);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	/**
	 * 
	 * @param string 要显示的字符串
	 * @param color 字符串的颜色
	 * @param left字符串隔左边的距离
	 * @param textSize 字符串的大小
	 * @return 文本
	 * @author 刘星星
	 * @crateDate 2012/1/7
	 */
    public  TextView getGenericView(String string,int color,int left,int textSize)   
    {   
        // Layout parameters for the ExpandableListView    
        AbsListView.LayoutParams layoutParams = new  AbsListView.LayoutParams(   
                ViewGroup.LayoutParams.FILL_PARENT, Integer.parseInt(context.getResources().
                		getString(R.string.main_expendable_height)));   
        TextView text = new  TextView(context);   
        text.setLayoutParams(layoutParams);   
        // Center the text vertically    
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);   
        // Set the text starting position    
        text.setPadding(left , 0 , 0 , 0 );  
        text.setTextColor(color);
        text.setText(string);   
        text.setTextSize(textSize);
        return  text;   
    }   
    	class CViewHolder{
    		TextView cTv;
    	}
    	class GViewHolder{
    		TextView gTv;
    	}
	}
