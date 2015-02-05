package com.qingfengweb.network;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.qingfengweb.constant.ConstantClass;
import com.qingfengweb.constant.DatabaseSql;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.utils.FileUtils;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoad {
	private Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	
	/**
	 * 下载图片，此方法只适合于本程序，因为有字段的依耐性
	 * @param imageUrl 图片的网络地址
	 * @param callback 回调接口
	 * @param database 数据库
	 * @param tabNameString 表名
	 * @param info_id 信息id
	 * @return
	 */
	public Bitmap loadDrawable(final Context context,final int defId,final String imageUrl,
			final ImageCallback callback){
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				return softReference.get();
			}
		}
		final Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				callback.setViewDrawable((Bitmap) msg.obj);
				super.handleMessage(msg);
			}
		};
		new Thread(){
			public void run() {
//				Drawable drawable = loaDrawable(imageUrl);
				Bitmap bitmap = loaDrawable(imageUrl,context,defId);
				if(bitmap!=null){
					imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
					Message message = handler.obtainMessage(0,bitmap);
					handler.sendMessage(message);
				}
				
			};
		}.start();
		return null;
	}
	/**
	 * 下载图片，并保存到sd卡  将图片在本地的路径保存在数据库
	 * @param imageUrl
	 * @param database
	 * @param tabNameString
	 * @param info_id
	 * @return
	 */
	protected Drawable loadImageFromUrl(String imageUrl,SQLiteDatabase database,String tabNameString,String info_id) {
		try {
			URL url = new URL(imageUrl);
			String nameString = url.getFile().substring(1);
//			System.out.println("这一次的网络下载文件名为-------------"+nameString);
			InputStream inputStream = url.openStream();
			if(inputStream!=null){
				String fileName = nameString;//给图片取一个新的名字
				FileUtils.write2SDFromInput(ConstantClass.IMG_URL_STRING, fileName, inputStream);//将文件保存到sd卡中
				ContentValues values = new ContentValues();
				values.put("img_sd_url", fileName);
				database.update(tabNameString, values, "info_id=?", new String[]{info_id});
			}
			Drawable drawable = Drawable.createFromStream(inputStream, "src");
			return drawable;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public  Bitmap loaDrawable(String imageUrl,Context context,int defId){
		URL myFileUrl = null;
		Bitmap bitmap = null;
		String nameString = "";
		try {
			myFileUrl = new URL(imageUrl);
			 nameString = myFileUrl.getFile().substring(1);
			 String[] fileStrings = imageUrl.trim().split("/");
			 nameString = fileStrings[fileStrings.length-1];
		} catch (MalformedURLException e) {
			if(defId!=0){
				bitmap = BitmapFactory.decodeResource(context.getResources(), defId);
			}
			return bitmap;
		}
		try {
			HttpURLConnection connection = (HttpURLConnection) myFileUrl.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream isInputStream = connection.getInputStream();
//			int length = connection.getContentLength();
//			if (length!=-1) {
//				byte[] imgData = new byte[length];
//				byte[] temp = new byte[512];
//				int readLen = 0;
//				int destPos = 0;
//				while ((readLen = isInputStream.read(temp))>0) {
//					System.arraycopy(temp, 0, imgData, destPos, readLen);
//					destPos += readLen;
//				}
//				bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
				bitmap = BitmapFactory.decodeStream(isInputStream);
				File file = new File(FileUtils.SDPATH+ConstantClass.IMG_URL_STRING+nameString);
				boolean b = OutPutImage(file,bitmap);
//				System.out.println("图片存储========================"+(b?"成功":"失败"));
//			}
		} catch (IOException e) {
			if(defId!=0){
				bitmap = BitmapFactory.decodeResource(context.getResources(), defId);
			}
			return bitmap;
		}
		return bitmap;
	}
	
	/**
	 * 将图片保存到本地ＳＤ卡
	 * 
	 * @param file
	 *            　路径
	 * @return
	 * @author 刘星星
	 */
	@SuppressWarnings("unused")
	public static boolean OutPutImage(File file, Bitmap bitmap) {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			if(bos!=null){
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				fos.flush();
				return true;
			}else{
				return false;
			}
		} catch (IOException e) {
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

}
