package com.chinaLife.claimAssistant.adapter;

import java.util.HashMap;
import java.util.List;

import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.activity.Sc_HelpActivity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
public class sc_ArticleListAdapter extends BaseAdapter {
	private Sc_HelpActivity context; // 承接上文内容
	private List<HashMap<String, String>> list; // listview中的数据项

	/**
	 * 构造函数ConnectAdapter
	 */

	public sc_ArticleListAdapter(Sc_HelpActivity context, List<HashMap<String, String>> listItems) {
		this.context = context;
		this.list = listItems;

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

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout l = (LinearLayout) inflater.inflate(R.layout.sc_item_help,null);
		if (position % 2 == 1) {
			l.setBackgroundColor(Color.parseColor("#ededed"));
		}

		TextView tv1 = (TextView) l.findViewById(R.id.t_newscontent);
		tv1.setText(list.get(position).get("title").toString());
		return l;
	}

}