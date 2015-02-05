package com.qingfengweb.adapter;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;

public class CommentMainActiviyAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> bitmaplist;
	LayoutInflater layoutInflater;
//	private ImageDownloader imageDownloader = null;
	int width = MyApplication.getInstant().getWidthPixels() / 4 - 10;
	int height = width;
	public CommentMainActiviyAdapter(Context context,
			List<Map<String, Object>> bitmaplist) {
		this.mContext = context;
		this.bitmaplist = bitmaplist;
		layoutInflater = LayoutInflater.from(context);
//		imageDownloader = new ImageDownloader(context,20);
	}

	public int getCount() {
		return bitmaplist.size();
	}

	public Object getItem(int position) {

		return bitmaplist.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// 定义一个ImageView,显示在GridView里
		ViewHolder holder = null;
		if (convertView == null) {
			// imageView = new ImageView(mContext);
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.item_gradview, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
					width, height);
			holder.imageView.setLayoutParams(param);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String thumb = bitmaplist.get(position).get("photoid").toString();
		if(thumb==null || thumb.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
			holder.imageView.setImageResource(R.drawable.photolist_defimg);
		}else{//如果id存在
			String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.VALUATION_IMG_URL+thumb+".png";
			if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
				holder.imageView.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
			}else{//如果不存在 则去服务器下载
				holder.imageView.setImageResource(R.drawable.photolist_defimg);
				RequestServerFromHttp.downImage(mContext,holder.imageView,thumb,ImageType.valuationPersons.getValue(),ImgDownType.ThumbBitmap.getValue(),
						 width+"",height+"",false,ConstantsValues.VALUATION_IMG_URL,R.drawable.photolist_defimg);
			}
		}
		return convertView;
	}

	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		ImageView imageView;
	}
}
