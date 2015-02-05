/**
 * 
 */
package com.boluomi.children.adapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boluomi.children.R;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.data.ImageType;
import com.boluomi.children.data.ImgDownType;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.RequestServerFromHttp;

/**
 * @author 刘星星 武国庆
 * @createDate 2013/8/27
 * 商家列表界面
 */
public class MerchantListAdapter extends BaseAdapter{
	private Context context;
	private List<Map<String,Object>> list;
	int width = MyApplication.getInstant().getWidthPixels()/4;
	int height = width;
	public MerchantListAdapter(Context context, List<Map<String,Object>> list) {
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
			convertView = layout.inflate(R.layout.item_merchant, null);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			RelativeLayout.LayoutParams param= new RelativeLayout.LayoutParams(width, height);
			holder.image.setLayoutParams(param);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object> map = list.get(position);
		holder.title.setText(map.get("name").toString());
		 String logo = map.get("logo").toString();
		 System.out.println("商家列表中的第"+position+"张图片的id为idididididididididididid"+logo);
			if(logo==null || logo.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
				holder.image.setImageResource(R.drawable.photolist_defimg);
			}else{//如果id存在
				String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.TREASURE_MERCHAN_IMG_URL+logo+".png";
				if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
					holder.image.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
				}else{//如果不存在 则去服务器下载
					holder.image.setImageResource(R.drawable.photolist_defimg);
					RequestServerFromHttp.downImage(context,holder.image,logo,ImageType.MerchanImg.getValue(),ImgDownType.ThumbBitmap.getValue(),
							 width+"",height+"",false,ConstantsValues.TREASURE_MERCHAN_IMG_URL,R.drawable.photolist_defimg);
				}
			}
		 
		 holder.content.setText(map.get("description").toString());
		return convertView;
	}
	class ViewHolder{
		ImageView image;
		TextView title,content;
		Map<String,Object> map = null;
	}
}
