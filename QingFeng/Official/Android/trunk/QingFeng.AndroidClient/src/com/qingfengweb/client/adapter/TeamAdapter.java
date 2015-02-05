/**
 * 
 */
package com.qingfengweb.client.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengweb.android.R;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.FinalValues;
import com.qingfengweb.filedownload.CallbackImpl;
import com.qingfengweb.filedownload.FileUtils;
import com.qingfengweb.filedownload.ImageLoadFromUrlOrId;
import com.qingfengweb.imagehandle.PicHandler;

/**
 * @author 刘星星
 * @createDate 2013、6、14
 * 关于我们的团队介绍适配器
 */
public class TeamAdapter extends BaseAdapter{
	private Context context;
	private List<Map<String, Object>> list=null;
	ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
	public TeamAdapter(Context context,List<Map<String, Object>> list) {
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
		ImageView photo;//照片
		TextView name;//姓名
		TextView job;//职位
		TextView manifasto;//职业宣言
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_team, null);
			holder = new ViewHolder();
			holder.photo = (ImageView) convertView.findViewById(R.id.photo);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.job = (TextView) convertView.findViewById(R.id.job);
			holder.manifasto = (TextView) convertView.findViewById(R.id.manifasto);
			convertView.setTag(holder);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.photo.setImageResource((Integer) list.get(position).get("photo"));
		holder.name.setText((CharSequence) list.get(position).get("name"));
		holder.job.setText((CharSequence) list.get(position).get("position"));
		holder.manifasto.setText((CharSequence) list.get(position).get("motto"));
		if(position%2 == 0){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.team_item_bg_1));
		}else{
			convertView.setBackgroundColor(context.getResources().getColor(R.color.team_item_bg_2));
		}
		File file = new File(FileUtils.SDPATH+FinalValues.TEAM_IMG_URL+list.get(position).get("photoid").toString()+".png");
		if(file.exists()){
			holder.photo.setImageBitmap(PicHandler.getDrawable(file.getAbsolutePath(), 100, 100));
		}else{
			CallbackImpl callbackImpl = new CallbackImpl(context,holder.photo);
			List<NameValuePair> list1 = new ArrayList<NameValuePair>();
			list1.add(new BasicNameValuePair("appid", AccessServer.APPID));
			list1.add(new BasicNameValuePair("appkey", AccessServer.APPKEY));
			list1.add(new BasicNameValuePair("action", AccessServer.DOWNLOAD_IMAGE_DETAIL_ACTION));
			list1.add(new BasicNameValuePair("imageid",list.get(position).get("photoid").toString()));
			list1.add(new BasicNameValuePair("type", "2"));//1代表下载原图
			imageLoad.loadImageFromId(context, AccessServer.CONTENT_INTERFACE, list1, 
					R.drawable.guoqing, FinalValues.TEAM_IMG_URL, callbackImpl, false,false, 100,100,100);
		}
		
		convertView.setOnClickListener(null);
		convertView.setOnTouchListener(null);
		return convertView;
	}

}
