package com.qingfengweb.lottery.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.data.ConstantValues;
import com.qingfengweb.lottery.util.DeviceTool;

@SuppressLint("DefaultLocale")
public class ChargeHistoryAdapter  extends BaseAdapter{
	public Context context;
	public List<Map<String,Object>> list;
	public ChargeHistoryAdapter(Context context,List<Map<String,Object>> list) {
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	class ViewHolder{
		TextView timeTv,moneyTv,balanceTv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_chargehistory, null);
			holder = new ViewHolder();
			holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
			holder.moneyTv = (TextView) convertView.findViewById(R.id.moneyTv);
			holder.balanceTv = (TextView) convertView.findViewById(R.id.balanceTv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object> map = list.get(position);
		String time = DeviceTool.getStrTime(map.get("create_stmp").toString());
		time = time.substring(0, 4)+"-"+time.substring(4, 6)+"-"+time.substring(6, 8)+"  "+time.substring(8, 10)
				+":"+time.substring(10, 12)+":"+time.substring(12, 14);
		String money = map.get("amount").toString();
		String balance = map.get("balance").toString();
		if(money!=null){
			money = "￥"+String.format("%.2f", Float.parseFloat(money));
		}
		if(balance!=null){
			balance = "￥"+String.format("%.2f", Float.parseFloat(balance));
		}
		holder.timeTv.setText(time);
		holder.moneyTv.setText(money);
		holder.balanceTv.setText(balance);
		return convertView;
	}
}
