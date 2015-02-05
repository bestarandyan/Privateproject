/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.Adapter.RoomManagerGridViewAdapter.ViewHolder;
import com.zhihuigu.sosoOffice.View.ScrollLayoutView;
import com.zhihuigu.sosoOffice.model.Messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/1/21
 * 楼盘弹出框的listView的适配器类
 *
 */
public class LoupanListAdapter extends BaseAdapter{
	Context context;
	ArrayList<HashMap<String,String>> list;
	ViewHolder holder = null;
	boolean display = true;
	boolean check  = false;
	public ArrayList<Messages> messageList = null;
	public LoupanListAdapter(Context context,ArrayList<HashMap<String,String>> list,boolean display) {
		this.context = context;
		this.list = list;
		this.display = display;
	}
	public LoupanListAdapter(Context context,ArrayList<HashMap<String,String>> list,boolean display,boolean check,ArrayList<Messages> messageLists) {
		this.context = context;
		this.list = list;
		this.display = display;
		this.check = check;
		
		if(messageLists.size() == 0){
			messageList = new ArrayList<Messages>();
			for(int i=0;i<list.size();i++){
				messageList.add(new Messages());
			}
		}else{
			this.messageList = messageLists;
		}
		
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
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_loupan_list, null);
			holder.text = (TextView) convertView.findViewById(R.id.listText);
			holder.imageView = (ImageView) convertView.findViewById(R.id.dialogIV);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(check){
			final Messages msg = messageList.get(position);  
			holder.checkBox.setChecked(msg.isChecked);  
	        //注意这里设置的不是onCheckedChangListener，还是值得思考一下的  
			holder.checkBox.setOnClickListener(new OnClickListener() {  
	                  
	                public void onClick(View v) {  
	                        if(msg.isChecked){  
	                                msg.isChecked = false;  
	                        }else{  
	                                msg.isChecked = true;  
	                        }  
	                          
	                }  
	        });  
			convertView.setOnClickListener(null);
		}
		
		holder.setViewContent(position);
		return convertView;
	}
	class ViewHolder{
		TextView text;
		ImageView imageView;
		CheckBox checkBox;
		public void setViewContent(int position){
			text.setText(list.get(position).get("name").toString());
			checkBox.setTag(position);
			if(display){
				imageView.setVisibility(View.VISIBLE);
			}
			
			if(check){
				checkBox.setVisibility(View.VISIBLE);
				final Messages msg = messageList.get(position);  
				text.setTag(checkBox);
				text.setOnClickListener(new OnClickListener() {  
		            
		            public void onClick(View v) {  
		                    if(msg.isChecked){  
		                            msg.isChecked = false;  
		                            ((CompoundButton) v.getTag()).setChecked(msg.isChecked);  
		                    }else{  
		                            msg.isChecked = true;  
		                            ((CompoundButton) v.getTag()).setChecked(msg.isChecked);  
		                    }  
		                      
		            }  
		    });  
			}else{
				checkBox.setVisibility(View.GONE);
			}
			
			
		}
	}

}
