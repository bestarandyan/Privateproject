/**
 * 
 */
package com.qingfengweb.lottery.adapter;

import java.util.List;
import java.util.Map;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.util.DeviceTool;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013、11、28
 * 我的彩票的listView的Adapter
 *
 */
public class MyCaiPiaoListViewAdapter extends BaseAdapter{
	public Context context;
	public List<Map<String,Object>> list;
	public MyCaiPiaoListViewAdapter(Context context,List<Map<String,Object>> list) {
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
		TextView monthTv;
		TextView dayTv;
		TextView typeCaipiao;
		TextView payMoney;
		TextView typeDingDan;
		TextView stateTv;
		TextView qihaoTv;
		TextView timeTv;
		ImageView jiantouIv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_mycaipiao, null);
			holder = new ViewHolder();
			holder.monthTv = (TextView) convertView.findViewById(R.id.monthTv);
			holder.dayTv = (TextView) convertView.findViewById(R.id.dayTv);
			holder.typeCaipiao = (TextView) convertView.findViewById(R.id.typeCaipiaoTv);
			holder.payMoney = (TextView) convertView.findViewById(R.id.payMoneyTv);
			holder.typeDingDan = (TextView) convertView.findViewById(R.id.typeDingDanTv);
			holder.stateTv = (TextView) convertView.findViewById(R.id.stateTv);
			holder.jiantouIv = (ImageView) convertView.findViewById(R.id.jiantouIv);
			holder.qihaoTv = (TextView) convertView.findViewById(R.id.qihaoTv);
			holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object> map = list.get(position);
		Object createStmp = map.get("create_stmp");
		Object amount = map.get("amount");
		Object ispaid = map.get("ispaid");
		Object iswin = map.get("iswin");
		Object isdeal = map.get("isdeal");
		Object qihao = map.get("ticket_no");
		if(createStmp!=null){
			String createTime = DeviceTool.getStrTime1(createStmp.toString());
			String month = createTime.substring(5, 7)+"月";
			String day = createTime.substring(8, 10);
			holder.monthTv.setText(month);
			holder.dayTv.setText(day);
			holder.timeTv.setText(createTime.substring(10));
		}
		if(qihao!=null){
			holder.qihaoTv.setText(qihao.toString());
		}
		holder.typeCaipiao.setText("上海快3");
		holder.payMoney.setText(amount!=null?"￥"+amount.toString():"");
		holder.typeDingDan.setText("普通订单");
		if(ispaid!=null && ispaid.toString().equals("0")){//未付款
			holder.stateTv.setTextColor(Color.parseColor("#FEAE03"));
			holder.stateTv.setText("等待付款");
			holder.jiantouIv.setVisibility(View.VISIBLE);
		}else if(ispaid!=null && ispaid.toString().equals("1")){ //已付款
			holder.jiantouIv.setVisibility(View.GONE);
			holder.stateTv.setTextColor(Color.parseColor("#8194AA"));
			if(isdeal!=null && isdeal.toString().equals("0")){//未开奖
				holder.stateTv.setText("已付款，待开奖");
			}else if(isdeal!=null && isdeal.toString().equals("1")){//已开奖
				if(iswin!=null && iswin.toString().equals("0")){//未中奖
					holder.stateTv.setText("未中奖");
				}else if(iswin!=null && iswin.toString().equals("1")){//已中奖
					holder.stateTv.setText("已中奖");
				}
			}
			
		}
		
		return convertView;
	}

}
