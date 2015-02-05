/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.utils.BitmapCache;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.ImageDownloaderId;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author ������
 * @createdate 2013/3/20
 * ��Դ�������з�ԴͼƬ��������
 *
 */
public class DetailRoominfoImageGalleryAdapter extends BaseAdapter{
	Context context;
	ArrayList<HashMap<String, Object>> list;
//	private ImageDownloaderId imageDownloader = null;
	public DetailRoominfoImageGalleryAdapter(Context context,ArrayList<HashMap<String, Object>> list) {
		this.context = context;
		this.list = list;
//		this.imageDownloader = imageDownloader;
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
		convertView = LayoutInflater.from(context).inflate(R.layout.item_detailroominfogallery, null);
		ImageView image = (ImageView) convertView.findViewById(R.id.galleryImg);
		boolean b = true;
		File file = (File) list.get(position).get("file");
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Integer.parseInt(list.get(position)
				.get("pixelswidth").toString()),Integer.parseInt(list.get(position)
						.get("pixelsheight").toString()));
		image.setLayoutParams(param);
		if(file.exists()&&file.isFile()){
			Bitmap bitmap = BitmapCache.getInstance().getBitmap(file, context);
			if(bitmap !=null){
				bitmap = CommonUtils.scaleImgFrirst(bitmap, Constant.ROOM_IMG_THUMBNAIL_SIZE,
						Constant.ROOM_IMG_THUMBNAIL_SIZE);
			}
				
//			//�жϷ�ԴͼƬ�Ƿ���ֻ��ֱ��ʴ�С��ͬ
//			if(bitmap.getWidth() == MyApplication.getInstance().getScreenWidth() && bitmap.getHeight() == MyApplication.getInstance().getScreenHeight()){
//				bitmap = CommonUtils.cutImg(bitmap, 0, Integer.parseInt(list.get(position).get("pixelsheight").toString())/3,Integer.parseInt(list.get(position)
//						.get("pixelswidth").toString()),Integer.parseInt(list.get(position).get("pixelsheight").toString())/3);
//			}else{//����ͬ
//				if(bitmap.getWidth()>MyApplication.getInstance().getScreenWidth()){//����ڷֱ��ʵĿ�
//					if(bitmap.getHeight()>(MyApplication.getInstance().getScreenHeight()/3)){//�߶ȴ��ڷֱ��ʵ�
//						bitmap = CommonUtils.cutImg(bitmap, 0, 0,MyApplication.getInstance().getScreenWidth(),MyApplication.getInstance().getScreenHeight()/3);
//					}else{
//						bitmap = CommonUtils.cutImg(bitmap, 0, 0,MyApplication.getInstance().getScreenWidth(),bitmap.getHeight());
//					}
//				}else{
//					if(bitmap.getHeight()>(MyApplication.getInstance().getScreenHeight()/3)){
//						bitmap = CommonUtils.cutImg(bitmap, 0, 0,bitmap.getWidth(),MyApplication.getInstance().getScreenHeight()/3);
//					}else{
//						bitmap = CommonUtils.cutImg(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight());
//					}
//				}
//				
//			}
			
			if(bitmap!=null){
				image.setImageBitmap(bitmap);
				b=false;
			}
		}
		if(b){
//			image.setImageResource(R.drawable.soso_gray_logo);
			MyApplication.getInstance(context).getImageDownloaderId().download((File) list.get(position).get("file"), list
					.get(position).get("sql").toString(),
					list.get(position).get("id").toString(), list.get(position)
							.get("pixelswidth").toString(),
					list.get(position).get("pixelsheight").toString(),
					list.get(position).get("request_name").toString(),
					image,"0"
					, "0",list.get(position)
					.get("pixelswidth").toString(),
			list.get(position).get("pixelsheight").toString());
		}
//		image.setImageBitmap((Bitmap) list.get(position).get("roomImage"));
		return convertView;
	}

}
