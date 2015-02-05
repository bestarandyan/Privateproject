/**
 * 
 */
package com.boluomi.children.adapter;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.boluomi.children.R;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.data.ImageType;
import com.boluomi.children.data.ImgDownType;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.RequestServerFromHttp;

/**
 * @author 刘星星
 * @createDate 2013/11/11
 *
 */
public class OtherGrowUpPhotoGvAdapter  extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list;
	LayoutInflater layoutInflater;
	public OtherGrowUpPhotoGvAdapter(Context context,	List<Map<String, Object>> list) {
		this.mContext = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {

		return list.get(position);
	}

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {
		// 定义一个ImageView,显示在GridView里
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.item_gradview, null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(MyApplication.getInstant().getWidthPixels()/4,MyApplication.getInstant().getWidthPixels()/4);
		holder.imageView.setLayoutParams(param);
		Map<String,Object> map = list.get(position);
		int imgid = (Integer) map.get("imgid");
		holder.imageView.setImageResource(imgid);
		holder.imageView.setAdjustViewBounds(true);
//		if(position == 0 || position == 1 || position == 4 || position == 5){//特殊区域 不放图片
//			convertView.setBackgroundColor(Color.TRANSPARENT);
//		}else{
			/*Object imgId = list.get(position).get("imageid");
			Object imgBitmap = list.get(position).get("bitmap");
			
			if(imgBitmap!=null){//如果列表中这个位置传递的是一个已经存在的bitmap则直接设值
				holder.imageView.setImageBitmap((Bitmap) imgBitmap);
			}else{//如果列表中的这个位置传递的是一个图片的id
				String firstPhoto = imgId.toString();
				if(firstPhoto==null){//判断id是否存在 如果不存在  则直接设置默认图片
					holder.imageView.setImageResource(R.drawable.photolist_defimg);
				}else{//如果id存在
					String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.MyALBUM_IMG_URL_THUMB+firstPhoto+".png";
					
					if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
						holder.imageView.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
					}else{//如果不存在 则去服务器下载
						holder.imageView.setImageResource(R.drawable.photolist_defimg);
						if(firstPhoto!=null && !firstPhoto.equals("")){
								RequestServerFromHttp.downImage(mContext,holder.imageView,firstPhoto,ImageType.beautyPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
										MyApplication.getInstant().getWidthPixels()+"","0",false,ConstantsValues.BEAUTYPHOTOS_IMG_URL_THUMB,R.drawable.photolist_defimg);
						}
					}
				}
			}*/
//		}
		
		return convertView;
	}

	public long getItemId(int position) {
		return position;
	}
	class ViewHolder {
		ImageView imageView;
	}
}