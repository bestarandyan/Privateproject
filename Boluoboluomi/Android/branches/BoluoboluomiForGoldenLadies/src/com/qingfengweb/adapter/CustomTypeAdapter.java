/**
 * 
 */
package com.qingfengweb.adapter;

import java.io.File;
import java.util.List;
import java.util.Map;


import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.id.blm_goldenLadies.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/11/4
 *
 */
public class CustomTypeAdapter extends BaseAdapter{
	private Context context;
	private List<Map<String,Object>> list;
	int width = MyApplication.getInstant().getWidthPixels()/5;
	int height = width;
	public ImageView imageView;
	public TextView couponTv;
	String couponid =  "";
	public CustomTypeAdapter(Context context, List<Map<String,Object>> list) {
		this.context = context;
		this.list = list;
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
		ViewHolder holder = null;
		LayoutInflater layout = null;
		if(convertView == null){
			holder = new ViewHolder();
			layout = LayoutInflater.from(context);
			convertView = layout.inflate(R.layout.item_custommain, null);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			RelativeLayout.LayoutParams param= new RelativeLayout.LayoutParams(width, height);
			holder.image.setLayoutParams(param);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object> map = list.get(position);
		 String logo = map.get("imageid").toString();
			if(logo==null || logo.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
				holder.image.setImageResource(R.drawable.photolist_defimg);
			}else{//如果id存在
				String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.CUSTOM_IMG_URL_TYPE+logo+".png";
				if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
					holder.image.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
				}else{//如果不存在 则去服务器下载
					holder.image.setImageResource(R.drawable.photolist_defimg);
					RequestServerFromHttp.downImage(context,holder.image,logo,ImageType.customType.getValue(),ImgDownType.ThumbBitmap.getValue(),
							 width+"",height+"",false,ConstantsValues.CUSTOM_IMG_URL_TYPE,R.drawable.photolist_defimg);
				}
			}
		holder.title.setText(map.get("name").toString());
		holder.content.setText(map.get("description").toString());
		return convertView;
	}
	class ViewHolder{
		ImageView image;
		TextView title;
		TextView content;
	}
}
