/**
 * 
 */
package com.piaoguanjia.chargeclient;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author ������
 * @createdate 2013/5/7
 * ��ֵ��¼������
 */
public class ChargeRecordAdapter extends BaseAdapter{
	Context context;
	ArrayList<HashMap<String,String>> list;
	private boolean flag = false;//�ж��Ƿ�Ϊ��ʷ 
	public ChargeRecordAdapter(Context context,ArrayList<HashMap<String,String>> list,boolean flag) {
		this.context = context;
		this.list = list;
		this.flag = flag;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_chargerecord, null);
			holder = new ViewHolder();
			holder.idTv = (TextView) convertView.findViewById(R.id.idTv);
			holder.moneyTv = (TextView) convertView.findViewById(R.id.moneyTv);
			holder.dateTv = (TextView) convertView.findViewById(R.id.dateTv);
			holder.stateTv = (TextView) convertView.findViewById(R.id.stateTv);
			holder.date1Tv = (TextView) convertView.findViewById(R.id.date1Tv);
			holder.state1Tv = (TextView) convertView.findViewById(R.id.state1Tv);
			holder.typeTv = (TextView) convertView.findViewById(R.id.typeTv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> map = list.get(position);
		String idString = map.get("username");
		String moneyString = map.get("amount");
		String dateString = map.get("createTime");
		String stateString = map.get("status");
		String date1String = map.get("auditTime");
		String typeString = map.get("accountTypeDis");
		if(stateString.equals("0")){
			stateString = "�����";
		}else if(stateString.equals("1")){
			stateString = "���ͨ��";
		}else if(stateString.equals("2")){
			stateString = "��˲�ͨ��";
		}
		holder.idTv.setText(idString);
		holder.moneyTv.setText("��"+moneyString);
		holder.dateTv.setText(dateString);
		holder.date1Tv.setText(date1String);
		holder.stateTv.setText("�����");
		holder.state1Tv.setText(stateString);
		holder.typeTv.setText(typeString);
		if(flag){//��ʷ
			holder.stateTv.setTextColor(Color.GRAY);
		}else{
			holder.date1Tv.setVisibility(View.GONE);
			holder.state1Tv.setVisibility(View.GONE);
		}
		return convertView;
	}
	class ViewHolder{
		TextView idTv;//�û�ID
		TextView moneyTv;//��ֵ���
		TextView dateTv;//���������
		TextView stateTv;//�����״̬
		TextView date1Tv;//���������
		TextView state1Tv;//�����״̬
		TextView typeTv;//��ֵ����
	}
}
