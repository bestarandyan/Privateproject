package com.qingfengweb.lottery.adapter;

import java.util.List;
import java.util.Map;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.data.ConstantValues;
import com.qingfengweb.lottery.util.DeviceTool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class LotteryListViewAdapter extends BaseAdapter{
	public Context context;
	public List<Map<String,Object>> list;
	public LotteryListViewAdapter(Context context,List<Map<String,Object>> list) {
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
		TextView currentQiTv/*,dateTv*/,hezhiTv,type1Tv,type2Tv/*,xingtaiTv*/,num1,num2,num3;
		ImageView img1,img2,img3,dotImg;
		View lineView;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_lotteryinfo, null);
			holder = new ViewHolder();
			holder.currentQiTv = (TextView) convertView.findViewById(R.id.qishu);
//			holder.dateTv = (TextView) convertView.findViewById(R.id.dateTv);
			holder.hezhiTv = (TextView) convertView.findViewById(R.id.hezhiTv);
			holder.type1Tv = (TextView) convertView.findViewById(R.id.type1);
			holder.type2Tv = (TextView) convertView.findViewById(R.id.type2);
//			holder.xingtaiTv = (TextView) convertView.findViewById(R.id.xingTaiTv);
			holder.img1 = (ImageView) convertView.findViewById(R.id.imgSz1);
			holder.img2 = (ImageView) convertView.findViewById(R.id.imgSz2);
			holder.img3 = (ImageView) convertView.findViewById(R.id.imgSz3);
			holder.dotImg = (ImageView) convertView.findViewById(R.id.dotImg);
			holder.num1 = (TextView) convertView.findViewById(R.id.num1);
			holder.num2 = (TextView) convertView.findViewById(R.id.num2);
			holder.num3 = (TextView) convertView.findViewById(R.id.num3);
			holder.lineView = convertView.findViewById(R.id.lineView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object> map = list.get(position);
		String currentQi = map.get("ticket_no").toString();
		if(currentQi!=null && currentQi.length()>3){
			currentQi = currentQi.substring(currentQi.length()-3, currentQi.length())+"期";
		}
		String date = DeviceTool.getStrTime(map.get("open_stmp").toString());
		date = date.substring(0, 4)+"-"+date.substring(4, 6)+"-"+date.substring(6, 8);
		String orgResults = map.get("org_results").toString();
		int sz1 = Integer.parseInt(orgResults.substring(0,1));
		int sz2 = Integer.parseInt(orgResults.substring(1,2));
		int sz3 = Integer.parseInt(orgResults.substring(2,3));
		int hezhi = (sz1+sz2+sz3);
		String type1 = "";
		String type2 = "";
		if(hezhi<12){
			type2="小";
			if(hezhi%2==0){
				type1 = "双";
			}else{
				type1 = "单";
			}
		}else{
			type2="大";
			if(hezhi%2==0){
				type1 = "双";
			}else{
				type1 = "单";
			}
		}
		String xingtai = "";
		int temp = 0;
		if(sz1>sz2){
			temp = sz1;
			sz1 = sz2;
			sz2 = temp;
			if(sz3<sz2 && sz3>sz1){
				temp = sz2;
				sz2 = sz3;
				sz3 = temp;
			}else if(sz3<=sz1){
				temp = sz1;
				sz1 = sz3;
				sz3 = sz2;
				sz2 = temp;
			}
		}else{
			if(sz3<sz2 && sz3>sz1){
				temp = sz2;
				sz2 = sz3;
				sz3 = temp;
			}else if(sz3<=sz1){
				temp = sz1;
				sz1 = sz3;
				sz3 = sz2;
				sz2 = temp;
			}
		}
		if((sz3-sz2) ==1 && (sz2-sz1) == 1){//三连号
			xingtai = xingtai+"三不同号/三连号通选/二不同号";
			holder.dotImg.setImageResource(R.drawable.kaijiang_dot3);
		}else if(sz1!=sz2&& sz2!=sz3 &&((sz3-sz2) >1 || (sz2-sz1) > 1)){//三不同 而且不是三连号
			xingtai = xingtai+"三不同号/二不同号";
			holder.dotImg.setImageResource(R.drawable.kaijiang_dot1);
		}else if((sz1 == sz2 && sz2!=sz3) || (sz2 == sz3 && sz1!=sz2)){//有两个数相同
			xingtai = xingtai+"二同号复选/二同号单选/二不同号";
			holder.dotImg.setImageResource(R.drawable.kaijiang_dot4);
		}else if(sz1 == sz2 && sz2 == sz3){//三个数相同
			holder.dotImg.setImageResource(R.drawable.kaijiang_dot2);
			xingtai = xingtai+"三同号/三同号通选/二同号复选";
		}
//		if(position == list.size()-1){
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(2,LayoutParams.MATCH_PARENT);
//			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//			params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.dotImg);
//			holder.lineView.setLayoutParams(params);
//		}else{
			Message msg = new Message();
			msg.obj = holder.lineView;
			msg.what =0;
			msg.arg1 = convertView.getHeight();
			handler.sendMessageDelayed(msg, 100);
//		}
		holder.currentQiTv.setText(currentQi);
//		holder.dateTv.setText(date);
		holder.img1.setImageResource(ConstantValues.SZ_IMG_ARRAY[sz1-1]);
		holder.img2.setImageResource(ConstantValues.SZ_IMG_ARRAY[sz2-1]);
		holder.img3.setImageResource(ConstantValues.SZ_IMG_ARRAY[sz3-1]);
		holder.hezhiTv.setText(hezhi+"");
		holder.type1Tv.setText(type1);
		holder.type2Tv.setText(type2);
		holder.num1.setText(sz1+"");
		holder.num2.setText(sz2+"");
		holder.num3.setText(sz3+"");
//		holder.xingtaiTv.setText(xingtai);
		
		return convertView;
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(2,((View)msg.obj).getHeight());
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			((View)msg.obj).setLayoutParams(params);
			super.handleMessage(msg);
		}
	};
}
