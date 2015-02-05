/**
 * 
 */
package com.qingfengweb.weddingideas.adapter;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.data.ConstantValues;
import com.qingfengweb.weddingideas.data.MyApplication;
import com.qingfengweb.weddingideas.filedownload.CallbackImpl;
import com.qingfengweb.weddingideas.filedownload.FileUtils;
import com.qingfengweb.weddingideas.filedownload.ImageLoadFromUrlOrId;
import com.qingfengweb.weddingideas.imagehandle.PicHandler;

/**
 * @author 刘星星
 *
 */
public class HuoDongListAdapter extends  BaseAdapter{
	public Context context;
	public List<Map<String,Object>> list;
	CallbackImpl callbackImpl = null;
	public Bitmap bitmap = null;
	public HuoDongListAdapter(Context context,List<Map<String,Object>> list) {
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
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_yangzhao, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.imageView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		bitmap = null;
		Map<String,Object> map = list.get(position);
		String imgUrl = "";
		if(map.get("pic_active")!=null){
			imgUrl = map.get("pic_active").toString();
		}
		if(map.get("bitmap") !=null){
			bitmap = (Bitmap) map.get("bitmap");
		}else{
			bitmap = null;
		}
		if(bitmap!=null){
			holder.img.setImageBitmap(bitmap);
		}else 	if(imgUrl!=null && imgUrl.length()>0){
			imgUrl = "http://img.weddingideas.cn"+imgUrl;
			String[] fileStrings = imgUrl.trim().split("/");
			String  nameString = fileStrings[fileStrings.length-1];
			 File file = new File(ConstantValues.sdcardUrl+ConstantValues.huodongImgUrl+nameString);
			 if(file.exists()){
				 try {
						BitmapFactory.Options opts = new BitmapFactory.Options();
						opts.inJustDecodeBounds = true;
						opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
						BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
						opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,1000 * 1000);
						opts.inJustDecodeBounds = false;
						// 如果图片还未回收，先强制回收该图片
						bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
						if (bitmap != null) {
							int width = bitmap.getWidth();
							int height = bitmap.getHeight();
							int newWidth = MyApplication.getInstance().getScreenW();
							// 计算缩放比例
							float scaleWidth = ((float) newWidth) / width;
							// 取得想要缩放的matrix参数
							Matrix matrix = new Matrix();
							matrix.postScale(scaleWidth, scaleWidth);
							// 得到新的图片
							bitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
						}
						System.gc();
					} catch (OutOfMemoryError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				 if(bitmap!=null){
					 holder.img.setImageBitmap(bitmap);
					 map.put("bitmap", bitmap);
					 this.notifyDataSetChanged();
				 }else{
					    callbackImpl = new CallbackImpl(context,holder.img);
						ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
						imageLoad.loadImageFromUrl(context, R.drawable.img_default, imgUrl, ConstantValues.huodongImgUrl, callbackImpl, false, MyApplication.getInstance().getScreenW());
				 }
			 }else{
				 	callbackImpl = new CallbackImpl(context,holder.img);
					ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
					imageLoad.loadImageFromUrl(context, R.drawable.img_default, imgUrl, ConstantValues.huodongImgUrl, callbackImpl, false, MyApplication.getInstance().getScreenW());
			 }
			
		}
		return convertView;
	}
}
