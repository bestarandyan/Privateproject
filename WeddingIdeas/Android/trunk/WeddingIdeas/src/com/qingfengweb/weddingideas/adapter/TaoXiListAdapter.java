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
import com.qingfengweb.weddingideas.filedownload.ImageLoadFromUrlOrId;
import com.qingfengweb.weddingideas.imagehandle.PicHandler;

/**
 * @author 刘星星
 *
 */
public class TaoXiListAdapter  extends  BaseAdapter{
	public Context context;
	public List<Map<String,Object>> list;
	Map<String ,Object> map = null;
	CallbackImpl callbackImpl = null;
	public Bitmap bitmap = null;
	public TaoXiListAdapter(Context context,List<Map<String,Object>> list) {
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
		TextView name,price;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_taoxi, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.imageView);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		bitmap = null;
		map = list.get(position);
		String name = "";
		String price_cut = "";
		String photo = "";
		if(map.get("s_name")!=null && !map.get("s_name").equals("null")){
			name = map.get("s_name").toString();
		}
		if(map.get("price_cut")!=null && !map.get("price_cut").equals("null")){
			price_cut = map.get("price_cut").toString();
			if(price_cut!=null && price_cut.length()>0){
				price_cut = "￥"+price_cut+"元";
			}
		}
		holder.name.setText(name);
		holder.price.setText(price_cut);
		if(map.get("bitmap")!=null){
			bitmap = (Bitmap) map.get("bitmap");
		}else{
			bitmap = null;
		}
		if(bitmap!=null){
			holder.img.setImageBitmap(bitmap);
		}else	if(map.get("photo_c")!=null && !map.get("photo_c").equals("null")){
			photo =  map.get("photo_c").toString();
			photo = "http://img.weddingideas.cn"+photo;
			String[] fileStrings = photo.trim().split("/");
			String  nameString = fileStrings[fileStrings.length-1];
			 File file = new File(ConstantValues.sdcardUrl+ConstantValues.taoxiImgUrl+nameString);
			 System.out.println(file.getAbsolutePath());
			 if(file.exists()){
//				 bitmap = PicHandler.getDrawable(file.getAbsolutePath(), null);
				 try {
	 					BitmapFactory.Options opts = new BitmapFactory.Options();
	 					opts.inJustDecodeBounds = true;
	 					opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
	 					BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
	 					opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,800 * 800);
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
	 					System.out.println("在本地文件存在的情况下内存溢出了--------------------------------------------");
	 					e.printStackTrace();
	 				} 
					 if(bitmap!=null){
						holder.img.setImageBitmap(bitmap);
						map.put("bitmap", bitmap);
						this.notifyDataSetChanged();
					 }else{
						    callbackImpl = new CallbackImpl(context,holder.img);
							ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
							imageLoad.loadImageFromUrl(context, R.drawable.img_default, photo, ConstantValues.taoxiImgUrl, callbackImpl, false, MyApplication.getInstance().getScreenW());
					 }
			 }else{
				 	callbackImpl = new CallbackImpl(context,holder.img);
					ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
					imageLoad.loadImageFromUrl(context, R.drawable.img_default, photo, ConstantValues.taoxiImgUrl, callbackImpl, false, MyApplication.getInstance().getScreenW());
			 }
		}
		
		return convertView;
	}
}
