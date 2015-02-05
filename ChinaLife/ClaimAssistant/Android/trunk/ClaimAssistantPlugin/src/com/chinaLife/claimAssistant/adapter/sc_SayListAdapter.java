package com.chinaLife.claimAssistant.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinaLife.claimAssistant.activity.R;

public class sc_SayListAdapter extends BaseAdapter {
	private Activity context; // 承接上文内容
	private ViewHolder vh = null;
	private List<HashMap<String, Object>> list; // listview中的数据项
	public ClipboardManager clip;
	private int sender = 0;
	private ArrayList<Button> Btnlist;
	private int index = 0;
	/**
	 * 构造函数ConnectAdapter
	 */

	public sc_SayListAdapter(Activity context,
			List<HashMap<String, Object>> listItems) {
		this.context = context;
		this.list = listItems;
		Btnlist = new ArrayList<Button>();

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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		index = position;
		try {
			if (convertView == null
					|| (convertView != null && convertView.getTag() == null)) {
				convertView = ((LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.sc_item_msg, null);
				vh = new ViewHolder(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.setContent(list.get(position));
		} catch (Exception e) {
		}
		return convertView;

		// LayoutInflater inflater = LayoutInflater.from(context);
		// LinearLayout l = (LinearLayout)
		// inflater.inflate(R.layout.item_msg,null);
		// LinearLayout layout3 = (LinearLayout)
		// l.findViewById(R.id.LinearLayout03);
		// TextView tv1 = (TextView) l.findViewById(R.id.TextView01);
		// TextView tv2 = (TextView) l.findViewById(R.id.TextView02);
		// tv1.setText(list.get(position).get("create_time").toString());
		// tv2.setText(list.get(position).get("content").toString());
		// tv2.setTextSize(14);
		// int sender =
		// Integer.parseInt(list.get(position).get("sender").toString());
		// if(sender == 1){
		// layout3.setGravity(Gravity.RIGHT);
		// tv2.setBackgroundResource(R.drawable.chatto_bg);
		//
		// }else{
		// layout3.setGravity(Gravity.LEFT);
		// tv2.setBackgroundResource(R.drawable.chatfrom_bg);
		// }
		// l.setOnClickListener(null);
		// return l;
	}

	private class ViewHolder {
		private TextView tv1, tv2;
		private LinearLayout layout3,layout4;
		private Button copyBtn;

		public ViewHolder(View layout) {
			tv1 = (TextView) layout.findViewById(R.id.TextView01);
			tv2 = (TextView) layout.findViewById(R.id.TextView02);
			layout3 = (LinearLayout) layout.findViewById(R.id.LinearLayout03);
			layout4 = (LinearLayout) layout.findViewById(R.id.LinearLayout04);
			copyBtn = (Button) layout.findViewById(R.id.copy);
			layout.setOnClickListener(null);
			tv2.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					int id = (Integer) v.getId();
					Button tagBtn = (Button) v.getTag();
					HashMap<String,Object> map = list.get(id);
					sender = Integer.parseInt(map.get("sender").toString());
						for(int i=0;i<Btnlist.size();i++){
							if(Btnlist.get(i) == tagBtn){
								Btnlist.get(i).setVisibility(View.VISIBLE);
							}else{
								Btnlist.get(i).setVisibility(View.GONE);
							}
						}
					if (sender == 1) {
						layout4.setGravity(Gravity.RIGHT);
					} else {
						layout4.setGravity(Gravity.LEFT);
					}
					return false;
				}
			});
			copyBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Button tagBtn =  (Button) v.getTag();
					 clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
					 //clip.getText(); // 粘贴
					 clip.setText(tv2.getText()); // 复制
					 tagBtn.setVisibility(View.GONE);
						
				}
			});
		}

		public void setContent(HashMap<String, Object> args) {
			tv1.setText(args.get("create_time").toString());
			tv2.setText(args.get("content").toString());
			tv2.setTextSize(14);
			copyBtn.setId(index);
			copyBtn.setTag(copyBtn);
			tv2.setTag(copyBtn);
			tv2.setId(index);
			Btnlist.add(copyBtn);
			sender = Integer.parseInt(args.get("sender").toString());
			if (sender == 1) {
				layout3.setGravity(Gravity.RIGHT);
				tv2.setBackgroundResource(R.drawable.sc_chatto_bg);
				layout4.setGravity(Gravity.RIGHT);
				
			} else {
				layout3.setGravity(Gravity.LEFT);
				tv2.setBackgroundResource(R.drawable.sc_chatfrom_bg);
				layout4.setGravity(Gravity.LEFT);
			}
		}
	}

}