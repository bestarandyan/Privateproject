/**
 * 
 */
package com.qingfengweb.client.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengweb.android.R;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.FinalValues;
import com.qingfengweb.client.data.MyApplication;
import com.qingfengweb.filedownload.CallbackImpl;
import com.qingfengweb.filedownload.FileUtils;
import com.qingfengweb.filedownload.ImageLoadFromUrlOrId;
import com.qingfengweb.imagehandle.PicHandler;

/**
 * @author 刘星星
 * @createDate 2013/6/17
 * 客户案例适配器
 *
 */
public class ClientCaseAdapter extends BaseAdapter{
	private List<Map<String, Object>> list;
	private Context context;
	ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
	public ClientCaseAdapter(Context context,List<Map<String, Object>> list) {
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
		ImageView img;
		TextView name;
		TextView intro;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_clientcase, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.intro = (TextView) convertView.findViewById(R.id.intro);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map< String, Object> map = list.get(position);
		holder.name.setText((CharSequence) map.get("name"));
		holder.intro.setText((CharSequence) map.get("summary"));
//		holder.img.setBackgroundResource((Integer) map.get("img"));
		String thumb = map.get("thumb").toString();
		File file = new File(FileUtils.SDPATH+FinalValues.CASE_IMG_URL+thumb+".png");
		if(file.exists()){
			Bitmap bitmap = PicHandler.getDrawable(file.getAbsolutePath(), MyApplication.getInstant().getScreenW()-30, 100);
			if(bitmap!=null){
				holder.img.setImageBitmap(bitmap);
			}else{
				CallbackImpl callbackImpl = new CallbackImpl(context,holder.img);
				List<NameValuePair> list1 = new ArrayList<NameValuePair>();
				list1.add(new BasicNameValuePair("appid", AccessServer.APPID));
				list1.add(new BasicNameValuePair("appkey", AccessServer.APPKEY));
				list1.add(new BasicNameValuePair("action", AccessServer.DOWNLOAD_IMAGE_DETAIL_ACTION));
				list1.add(new BasicNameValuePair("imageid",thumb));
				list1.add(new BasicNameValuePair("type", "2"));//1代表下载原图
				imageLoad.loadImageFromId(context, AccessServer.CONTENT_INTERFACE, list1, R.drawable.soso, FinalValues.CASE_IMG_URL, 
						callbackImpl, false,false, MyApplication.getInstant().getScreenW(),0,0);
			}
		}else{
			CallbackImpl callbackImpl = new CallbackImpl(context,holder.img);
			List<NameValuePair> list1 = new ArrayList<NameValuePair>();
			list1.add(new BasicNameValuePair("appid", AccessServer.APPID));
			list1.add(new BasicNameValuePair("appkey", AccessServer.APPKEY));
			list1.add(new BasicNameValuePair("action", AccessServer.DOWNLOAD_IMAGE_DETAIL_ACTION));
			list1.add(new BasicNameValuePair("imageid",thumb));
			list1.add(new BasicNameValuePair("type", "2"));//1代表下载原图
			imageLoad.loadImageFromId(context, AccessServer.CONTENT_INTERFACE, list1, R.drawable.soso, FinalValues.CASE_IMG_URL, 
					callbackImpl, false,false, MyApplication.getInstant().getScreenW(),0,0);
		}
		
		
		return convertView;
	}

}
