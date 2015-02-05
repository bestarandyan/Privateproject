package com.qingfengweb.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;

public class IntegralExchangeAdapter extends BaseAdapter{
	Activity context;
	List<Map<String,Object>> list;
//	private ImageDownloader imageDownloader = null;
	int width = MyApplication.getInstant().getWidthPixels()/5;
	int height = width;
	public IntegralExchangeAdapter(Activity context,List<Map<String,Object>> arrayList) {
		this.list = arrayList;
		this.context = context;
//		imageDownloader = new ImageDownloader(context,20);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_integralexchange, null);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.image =  (ImageView) convertView.findViewById(R.id.image);
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.linear);
			holder.money = (TextView) convertView.findViewById(R.id.money);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.type = (TextView) convertView.findViewById(R.id.type);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object> map = list.get(position);
		holder.content.setText(map.get("description").toString());
		holder.name.setText(map.get("name").toString());
		holder.money.setText(map.get("price").toString());
		if(position>0){
			String typeid = map.get("typeid").toString();
			String typeidUp = list.get(position-1).get("typeid").toString();
			if(typeid.equals(typeidUp)){
				holder.type.setVisibility(View.GONE);
			}else{
				holder.type.setVisibility(View.VISIBLE);
				holder.type.setText(map.get("typename").toString());
			}
		}else{
			holder.type.setVisibility(View.VISIBLE);
			holder.type.setText(map.get("typename").toString());
		}
		
		String logo = map.get("imageid").toString();
		if(logo==null || logo.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
			holder.image.setImageResource(R.drawable.photolist_defimg);
		}else{//如果id存在
			String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.INTEGRAL_IMG_URL_THUMB+logo+".png";
			if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
				holder.image.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
			}else{//如果不存在 则去服务器下载
				holder.image.setImageResource(R.drawable.photolist_defimg);
				RequestServerFromHttp.downImage(context,holder.image,logo,ImageType.integral.getValue(),ImgDownType.ThumbBitmap.getValue(),
						 width+"",height+"",false,ConstantsValues.INTEGRAL_IMG_URL_THUMB,R.drawable.photolist_defimg);
			}
		}
		return convertView;
	}
	class ViewHolder{
		RelativeLayout layout;
		TextView type;
		ImageView image;
		TextView name;
		TextView content;
		TextView money;
	}
}
