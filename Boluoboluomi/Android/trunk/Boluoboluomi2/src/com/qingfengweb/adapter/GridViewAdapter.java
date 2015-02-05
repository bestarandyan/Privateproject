
package com.qingfengweb.adapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qingfengweb.id.biluomiV2.PhotoListActivity;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.imagehandle.PicHandler;
import com.qingfengweb.model.UserPhotoInfo;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.util.CommonUtils;

@SuppressLint("NewApi")
public class GridViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list;
	LayoutInflater layoutInflater;
	DBHelper dbHelper;
	int w = 0;//图片的最大宽度
	int type = 0;//0代表我的照片列表    1代表美图相册列表
	Bitmap bitmap = null;
	public GridViewAdapter(Context context,	List<Map<String, Object>> list,int daW,int type) {
		this.mContext = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
		this.w = (daW-5)/2;
		this.type = type;
		dbHelper = DBHelper.getInstance(context);
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
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(w, w);
		holder.imageView.setLayoutParams(param);
//		if(position == 0 || position == 1 || position == 4 || position == 5){//特殊区域 不放图片
//			convertView.setBackgroundColor(Color.TRANSPARENT);
//		}else{
			Object imgId = list.get(position).get("imageid");
			Object imgBitmap = list.get(position).get("bitmap");
			if(imgBitmap!=null){//如果列表中这个位置传递的是一个已经存在的bitmap则直接设值
				holder.imageView.setImageBitmap((Bitmap) imgBitmap);
			}else{//如果列表中的这个位置传递的是一个图片的id
				String firstPhoto = "";
				if(imgId!=null){
					firstPhoto = imgId.toString();
				}
					String imgUrl = (String) list.get(position).get("imgurl");
					if(imgUrl!=null && new File(imgUrl).exists()){
						try {
							BitmapFactory.Options opts = new BitmapFactory.Options();
							opts.inJustDecodeBounds = true;
							opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
							BitmapFactory.decodeFile(imgUrl, opts);
							opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,500 * 500);
							opts.inJustDecodeBounds = false;
							bitmap = BitmapFactory.decodeFile(imgUrl, opts);
							if (bitmap != null) {
								int width = bitmap.getWidth();
								int height = bitmap.getHeight();
								int newWidth = w;
								// 计算缩放比例
								float scaleWidth = ((float) newWidth) / width;
								// 取得想要缩放的matrix参数
								Matrix matrix = new Matrix();
								matrix.postScale(scaleWidth, scaleWidth);
								// 得到新的图片
								bitmap= Bitmap.createBitmap(bitmap, 0, 0, width, width>height?height:width,matrix, true);
							}
							System.gc();
						} catch (OutOfMemoryError e) {
							System.out.println("在本地文件存在的情况下内存溢出了--------------------------------------------");
							e.printStackTrace();
						} 
						if(bitmap!=null){
							holder.imageView.setImageBitmap(bitmap);
						}
				}else{//如果id存在
					String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.MyALBUM_IMG_URL_THUMB+firstPhoto+".png";
					if(type == 0){
						firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.MyALBUM_IMG_URL_THUMB+firstPhoto+".png";
					}else if(type == 1){
						firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.BEAUTYPHOTOS_IMG_URL_THUMB+firstPhoto+".png";
					}
					if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
						try {
							BitmapFactory.Options opts = new BitmapFactory.Options();
							opts.inJustDecodeBounds = true;
							opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
							BitmapFactory.decodeFile(firstPhotoUrl, opts);
							opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,500 * 500);
							opts.inJustDecodeBounds = false;
							bitmap = BitmapFactory.decodeFile(firstPhotoUrl, opts);
							if (bitmap != null) {
								int width = bitmap.getWidth();
								int height = bitmap.getHeight();
								int newWidth = w;
								// 计算缩放比例
								float scaleWidth = ((float) newWidth) / width;
								// 取得想要缩放的matrix参数
								Matrix matrix = new Matrix();
								matrix.postScale(scaleWidth, scaleWidth);
								// 得到新的图片
								bitmap= Bitmap.createBitmap(bitmap, 0, 0, width, width,matrix, true);
							}
							System.gc();
						} catch (OutOfMemoryError e) {
							System.out.println("在本地文件存在的情况下内存溢出了--------------------------------------------");
							e.printStackTrace();
						} 
						if(bitmap!=null){
							holder.imageView.setImageBitmap(bitmap);
						}
					}else{//如果不存在 则去服务器下载
						holder.imageView.setImageResource(R.drawable.photolist_defimg);
						if(firstPhoto!=null && !firstPhoto.equals("")){
							if(type == 0){
										downImage(position,holder.imageView,firstPhoto,ImageType.UserPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
												MyApplication.getInstant().getWidthPixels()+"",MyApplication.getInstant().getHeightPixels()+"",
												ConstantsValues.MyALBUM_IMG_URL_THUMB);
//								RequestServerFromHttp.downImage(mContext,holder.imageView,firstPhoto,ImageType.UserPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
//										MyApplication.getInstant().getWidthPixels()+"","0",false,ConstantsValues.MyALBUM_IMG_URL_THUMB,R.drawable.photolist_defimg);
							}else if(type == 1){
								downImage(position,holder.imageView,firstPhoto,ImageType.beautyPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
										MyApplication.getInstant().getWidthPixels()+"",MyApplication.getInstant().getHeightPixels()+"",
										ConstantsValues.BEAUTYPHOTOS_IMG_URL_THUMB);
//								RequestServerFromHttp.downImage(mContext,holder.imageView,firstPhoto,ImageType.beautyPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
//										MyApplication.getInstant().getWidthPixels()+"","0",false,ConstantsValues.BEAUTYPHOTOS_IMG_URL_THUMB,R.drawable.photolist_defimg);
							}
						}
					}
				}
			}
//		}
		
		return convertView;
	}
	/**
	 * 图片下载
	 * @param context 
	 * @param imageView 装载图片的控件
	 * @param imgid 图片id
	 * @param image_type 图片类型 
	 * @param download_type 下载类型 大图或者缩略图
	 * @param width 想要的宽度
	 * @param height 想要的图片的高度
	 * @param isBackground 是否为背景图片
	 * @param imgUrl 图片的本地保存路劲
	 */
	public void downImage(final int position,final ImageView imageView,final String imgid,final String image_type,final String download_type,final String width,final String height,final String sd){
		//如果图片在本地不存在，则根据id准备去服务器下载。
		new Thread(new Runnable() {
			
			@Override
			public void run() {
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("appid", RequestServerFromHttp.APPID));
					list.add(new BasicNameValuePair("appkey", RequestServerFromHttp.APPKEY));
					list.add(new BasicNameValuePair("action", RequestServerFromHttp.ACTION_DOWNLOAD));
					list.add(new BasicNameValuePair("imageid", imgid));
					list.add(new BasicNameValuePair("image_type",image_type));
					list.add(new BasicNameValuePair("download_type", download_type));
					list.add(new BasicNameValuePair("width", width));
					list.add(new BasicNameValuePair("height", height));
					try{
					HttpPost httpPost = new HttpPost(RequestServerFromHttp.INTERFACE_SYSTEM);
					HttpEntity httpEntity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
					httpPost.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity entity = httpResponse.getEntity();
					InputStream isInputStream = entity.getContent();
					try{
						bitmap = null;
						bitmap = BitmapFactory.decodeStream(isInputStream);
					}catch(OutOfMemoryError error){
						System.out.println("内存溢出");
					}
						if(bitmap!=null){
							Map<String,Object> map = new HashMap<String, Object>();
							map.put("bitmap", bitmap);
							map.put("imageView", imageView);
							Message msg = new Message();
							msg.what = 0;
							msg.obj = map;
							msg.arg1 = position;
							handler.sendMessage(msg);
							File file = new File(ConstantsValues.SDCARD_ROOT_PATH+sd+imgid+".png");
							boolean b = OutPutImage(file,bitmap);
							System.out.println("图片存储========================"+(b?"成功":"失败"));
							if(b && image_type.equals(ImageType.UserPhotos.getValue())){
								ContentValues values = new ContentValues();
								values.put("imgurl", file.getAbsolutePath());
								values.put("isUpload", "1");
								dbHelper.update(UserPhotoInfo.TableName, values, "imageid=? and username=?", new String[]{imgid,UserBeanInfo.getInstant().getUserName()});
							}
						}
						isInputStream.close();
			//		}
				} catch (IOException e) {
			
				}
			}
		}).start();
	}
	
	
	Handler handler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			Map<String,Object> map = (Map<String, Object>) msg.obj;
			ImageView imageView = (ImageView) map.get("imageView");
			bitmap = null;
			bitmap = (Bitmap) map.get("bitmap");
			imageView.setImageBitmap(bitmap);
			super.handleMessage(msg);
		}
	};
	public boolean OutPutImage(File file, Bitmap bitmap) {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(file.getAbsolutePath());
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			fos.flush();
			return true;
		} catch (IOException e) {
			return false;
		}catch(NullPointerException e){
			return false;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public long getItemId(int position) {
		return position;
	}
	class ViewHolder {
		ImageView imageView;
		private void setImageView(Bitmap bm) {
			imageView.setImageBitmap(bm);
		}
	}
}