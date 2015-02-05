package com.qingfengweb.adapter;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;

public class BeautyPhotoAdapter extends BaseAdapter {
	Context context;
	List<Map<String, Object>> list;
	int width = MyApplication.getInstant().getWidthPixels()/4;
	int height = width;
	public BeautyPhotoAdapter(Context context,List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_beautyphoto,null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.photo);
			holder.title = (TextView) convertView
					.findViewById(R.id.helpListTitle);
			holder.content = (TextView) convertView
					.findViewById(R.id.helpListText);
			holder.xing = (RatingBar) convertView.findViewById(R.id.xing);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width, height);
		holder.imageView.setLayoutParams(param);
		String name = list.get(position).get("name").toString();
		float x = Float.parseFloat(list.get(position).get("commend").toString());
		String summary = list.get(position).get("description").toString();
		
		holder.title.setText(name);
		holder.content.setText(summary);
		holder.xing.setIsIndicator(true);
		holder.xing.setRating(x);
		
		String thumb = list.get(position).get("thumb").toString();
		if(thumb==null || thumb.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
			holder.imageView.setImageResource(R.drawable.photolist_defimg);
		}else{//如果id存在
			String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.BEAUTYPHOTO_THEMES_IMG_URL+thumb+".png";
			if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
				holder.imageView.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
			}else{//如果不存在 则去服务器下载
				holder.imageView.setImageResource(R.drawable.photolist_defimg);
				RequestServerFromHttp.downImage(context,holder.imageView,thumb,ImageType.beautyPhotoThemes.getValue(),ImgDownType.ThumbBitmap.getValue(),
						 width+"",height+"",true,ConstantsValues.BEAUTYPHOTO_THEMES_IMG_URL,R.drawable.photolist_defimg);
			}
		}
		if (position % 2 == 0) {
			convertView.setBackgroundColor(Color.rgb(255, 255, 255));
		} else {
			convertView.setBackgroundColor(Color.rgb(240, 240, 240));
		}
		return convertView;
	}

	public final class ViewHolder {
		ImageView imageView;
		TextView title;
		TextView content;
		RatingBar xing;
	}
}
