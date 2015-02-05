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
import android.widget.TextView;

import com.boluomi.children.R;
import com.boluomi.children.adapter.GridViewAdapter.ViewHolder;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.data.ImageType;
import com.boluomi.children.data.ImgDownType;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.RequestServerFromHttp;
import com.qingfengweb.imagehandle.PicHandler;

/**
 * @author  刘星星
 * @createDate 2013/11/12
 *
 */
public class GrowUpPhotoGvAdapter  extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list;
	LayoutInflater layoutInflater;
	public GrowUpPhotoGvAdapter(Context context,	List<Map<String, Object>> list) {
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
			convertView = layoutInflater.inflate(R.layout.item_growupgv, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			holder.imgInfo = (TextView) convertView.findViewById(R.id.photoInfo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int size = (MyApplication.getInstant().getWidthPixels()-4)/3;
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(size, size);
		holder.imageView.setLayoutParams(param);
		Map<String,Object> map = list.get(position);
		Bitmap bitmap = (Bitmap) map.get("bm");
		if(bitmap!=null){
			holder.imageView.setImageBitmap(bitmap);
		}
		String info = map.get("photoname").toString();
		if(info==null || info.equals("")){
			holder.imgInfo.setVisibility(View.GONE);
		}else{
			holder.imgInfo.setVisibility(View.VISIBLE);
			holder.imgInfo.getBackground().setAlpha(100);
			holder.imgInfo.setText(info);
		}
		
//		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, w);
//		holder.imageView.setLayoutParams(param);
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
							if(type == 0){
								RequestServerFromHttp.downImage(mContext,holder.imageView,firstPhoto,ImageType.UserPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
										MyApplication.getInstant().getWidthPixels()+"","0",false,ConstantsValues.MyALBUM_IMG_URL_THUMB,R.drawable.photolist_defimg);
							}else if(type == 1){
								RequestServerFromHttp.downImage(mContext,holder.imageView,firstPhoto,ImageType.beautyPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
										MyApplication.getInstant().getWidthPixels()+"","0",false,ConstantsValues.BEAUTYPHOTOS_IMG_URL_THUMB,R.drawable.photolist_defimg);
							}
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
		TextView imgInfo;
	}
}