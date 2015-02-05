/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.utils.ChineseToEnglish;
/**
 * @author 刘星星
 * @createDate 2013/3/13
 * 城市列表的适配器
 *
 */
public class CityListAdapter extends BaseAdapter{
	private ArrayList<Map<String, Object>> list;
	private Context context;
	public CityListAdapter(Context context,	ArrayList<Map<String, Object>> webNameArr) {
		this.list = webNameArr;
		this.context = context;
	}
	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_citylist, null);
			holder.city = (TextView) convertView.findViewById(R.id.cityItemText);
			holder.zimuText = (TextView) convertView.findViewById(R.id.text_first_char_hint);
			convertView.setTag(holder);
		}else{
			holder =  (ViewHolder) convertView.getTag();
		}
		holder.setViewContent(position);
		return convertView;
	}

	// 继承BaseAdapter来设置ListView每行的内容
	public final class ViewHolder{
		public TextView zimuText;
		public TextView city;
		public void setViewContent(int position){
			Map<String,Object> map = list.get(position);
			city.setText(map.get("city").toString());
			String zimu = map.get("zimu").toString();//ChineseToEnglish.toEnglish(map.get("city").toString().charAt(0)).substring(0,1).toUpperCase();
			zimuText.setText(zimu);
			//控制相同的首字母相同的控件隐藏
			if(position>0){
				String zimu1 = list.get(position-1).get("zimu").toString();//ChineseToEnglish.toEnglish(list.get(position-1).get("city").toString().charAt(0)).substring(0,1).toUpperCase();
				if(zimu.equals(zimu1)){
					zimuText.setVisibility(View.GONE);
				}else{
					zimuText.setVisibility(View.VISIBLE);
				}
			}else if(position == 0){
				zimuText.setVisibility(View.VISIBLE);
			}
			
	}
	}
}
