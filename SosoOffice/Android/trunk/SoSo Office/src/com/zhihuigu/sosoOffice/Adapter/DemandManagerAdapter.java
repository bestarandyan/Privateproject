/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.Adapter.PotentialDemandAdapter.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author 刘星星
 *@createDate 2013/2/1
 */
public class DemandManagerAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<HashMap<String,Object>> list;
	public DemandManagerAdapter(Context context,
			ArrayList<HashMap<String,Object>> list) {
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_demandmanager, null);
			holder.roomName = (TextView) convertView.findViewById(R.id.roomName);
			holder.roomAddress = (TextView) convertView.findViewById(R.id.roomAddress);
			holder.roomMoney = (TextView) convertView.findViewById(R.id.roomMoney);
			holder.roomAcreage = (TextView) convertView.findViewById(R.id.roomAcreage);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position%2==0){
			convertView.setBackgroundColor(Color.rgb(242,242,242));
		}else{
			convertView.setBackgroundColor(Color.rgb(248,248,248));
		}
		holder.setContent(position);
		return convertView;
	}

	class ViewHolder {
		TextView roomName;// 房源名称
		TextView roomAddress;// 房源地址
		TextView roomMoney;// 房间没平米米每天的价格
		TextView roomAcreage;// 房间面积

		public void setContent(int position) {
			HashMap<String, Object> map = list.get(position);// 根据position得到相应布局的数据
			String address = map.get("description").toString();
			String name = map.get("name").toString();
			String money = "￥" + map.get("pricedown").toString() + "-"
					+ map.get("priceup").toString() + "元/平米/天";
			String acreage = map.get("areadown").toString() + "-"
					+ map.get("areaup").toString() + "m²";
			if (address != null && address.length() > 10) {
				address = address.substring(0, 10) + "...";
			}
			roomAddress.setText(address);
			roomMoney.setText(money);
			if(name.equals("")){
				roomName.setText(address);
			}else{
				roomName.setText(name);
			}
			if (acreage.equals("-m²")) {
				roomAcreage.setText("");
			}else{
				roomAcreage.setText(acreage);
			}
		}
	}
	
}
