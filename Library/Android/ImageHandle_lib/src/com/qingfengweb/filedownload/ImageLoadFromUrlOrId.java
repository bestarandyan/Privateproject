package com.qingfengweb.filedownload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import com.qingfengweb.imagehandle.PicHandler;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

@SuppressLint("HandlerLeak")
public class ImageLoadFromUrlOrId {
	private Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	Bitmap bitmap = null;
	/**
	 * 下载图片，此方法只�?合于本程序，因为有字段的依�?�?
	 * @param defId 默认的图片资源ID
	 * @param imageUrl 图片的网络地�?
	 * @param toSD 保存到SD卡的位置
	 * @param callback 回调接口
	 * @return 下载到的图片位图
	 */
	public Bitmap loadImageFromUrl(final Context context,final int defId,final String imageUrl,
			final String toSD,final ImageCallback callback,final boolean height,final int screenW){
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				return softReference.get();
			}
		}
		final Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				callback.setViewDrawable((Bitmap) msg.obj,height,screenW);
				super.handleMessage(msg);
			}
		};
		new Thread(){
			public void run() {
//				Drawable drawable = loaDrawable(imageUrl);
				bitmap = loadImageFromUrl(imageUrl,toSD,context,defId,screenW);
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
	 * 下载图片，并保存到sd�? 将图片在本地的路径保存在数据�?
	 * @param imageUrl
	 * @param database
	 * @param tabNameString
	 * @param info_id
	 * @return
	 */
	protected Drawable loadImageFromUrl(String imageUrl,String toSD,SQLiteDatabase database,String tabNameString,String info_id) {
		try {
			URL url = new URL(imageUrl);
			String nameString = url.getFile().substring(1);
//			System.out.println("这一次的网络下载文件名为-------------"+nameString);
			InputStream inputStream = url.openStream();
			if(inputStream!=null){
				String fileName = nameString;//给图片取�?��新的名字
				FileUtils.write2SDFromInput(toSD, fileName, inputStream);//将文件保存到sd卡中
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
	/**
	 * 通过网络地址下载图片
	 * @param imageUrl
	 * @param toSD
	 * @param context
	 * @param defId
	 * @return
	 */
	private  Bitmap loadImageFromUrl(String imageUrl,String toSD,Context context,int defId,int screenW){
		URL myFileUrl = null;
		
		String nameString = "";
		try {
			myFileUrl = new URL(imageUrl);
			 nameString = myFileUrl.getFile().substring(1);
			 String[] fileStrings = imageUrl.trim().split("/");
			 nameString = fileStrings[fileStrings.length-1];
			 File file = new File(FileUtils.SDPATH+toSD+nameString);
			 if(file.exists()){
				 bitmap = PicHandler.getDrawable(file.getAbsolutePath(), null);
				 if(bitmap!=null){
					 return bitmap;
				 }
			 }
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
			int length = connection.getContentLength();
			if (length!=-1) {
				byte[] imgData = new byte[length];
				byte[] temp = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = isInputStream.read(temp))>0) {
					System.arraycopy(temp, 0, imgData, destPos, readLen);
					destPos += readLen;
				}
//			bitmap = BitmapFactory.decodeStream(isInputStream);
//				bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
				BitmapFactory.decodeByteArray(imgData, 0, imgData.length, opts);
				opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,1000 * 1000);
				opts.inJustDecodeBounds = false;
				bitmap =BitmapFactory.decodeByteArray(imgData, 0, imgData.length, opts);
				if (bitmap != null) {
					int width = bitmap.getWidth();
					int height = bitmap.getHeight();
					int newWidth = screenW;
					// 计算缩放比例
					float scaleWidth = ((float) newWidth) / width;
					// 取得想要缩放的matrix参数
					Matrix matrix = new Matrix();
					matrix.postScale(scaleWidth, scaleWidth);
					// 得到新的图片
					bitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
				}
				File file = new File(FileUtils.SDPATH+toSD+nameString);
				if(bitmap==null){
					return null;
				}
				boolean b = OutPutImage(file,bitmap);
//				System.out.println("图片存储========================"+(b?"成功":"失败"));
			}
		} catch (IOException e) {
			if(defId!=0){
				bitmap = BitmapFactory.decodeResource(context.getResources(), defId);
			}
			return bitmap;
		}
		return bitmap;
	}
	
	
	/**
	 * 
	 * @param defId 默认的图片资源ID
	 * @param imageUrl 图片的网络地�?
	 * @param toSD 保存到SD卡的位置
	 * @param callback 回调接口
	 * @return 下载到的图片位图
	 */
	public Bitmap loadImageFromId(final Context context,final String interfaceStr,final List<NameValuePair> list,
			final int defId,final String toSD,final ImageCallback callback,final boolean height,final boolean isBackground,
			final int screenWidth,final int imgW,final int imgH){
		if(imageCache.containsKey(defId+"")){
			SoftReference<Bitmap> softReference = imageCache.get(defId);
			if (softReference!=null && softReference.get() != null) {
				return softReference.get();
			}
		}
		final Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(isBackground){
					callback.setViewBackgroundDrawable((Bitmap) msg.obj);
				}else{
					callback.setViewDrawable((Bitmap) msg.obj,height,screenWidth);
				}
				super.handleMessage(msg);
			}
		};
		new Thread(){
			public void run() {
				bitmap = loadImageFromId(context,interfaceStr,list,toSD,defId,imgW,imgH);
				if(bitmap!=null){
					imageCache.put(defId+"", new SoftReference<Bitmap>(bitmap));
					Message message = handler.obtainMessage(0,bitmap);
					handler.sendMessage(message);
				}
				
			};
		}.start();
		return null;
	}
	/**
	 * 通过图片id下载图片
	 * @param imageUrl
	 * @param toSD
	 * @param context
	 * @param defId
	 * @return
	 */
	private  Bitmap loadImageFromId(Context context,String interfaceStr,List<NameValuePair> list,String toSD,int defId,final int imgW,final int imgH){
		String nameString = list.get(3).getValue()+".png";
		System.out.println("imageID--------------------------"+list.get(3).getValue());
		try {
			HttpPost httpPost = new HttpPost(interfaceStr);
			HttpEntity httpEntity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
			httpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			InputStream isInputStream = entity.getContent();
//			long length =  entity.getContentLength();
//			if (length!=-1) {
//				byte[] imgData = new byte[(int) length];
//				byte[] temp = new byte[512];
//				int readLen = 0;
//				int destPos = 0;
//				while ((readLen = isInputStream.read(temp))>0) {
//					System.arraycopy(temp, 0, imgData, destPos, readLen);
//					destPos += readLen;
//				}
			try{
				bitmap = BitmapFactory.decodeStream(isInputStream);
			}catch(OutOfMemoryError error){
				System.out.println("内存溢出");
				return null;
			}
//				bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
				File file = new File(FileUtils.SDPATH+toSD+nameString);
				if(bitmap!=null && !file.exists()){
					bitmap = PicHandler.scaleImg(bitmap, imgW,imgH);
					boolean b = OutPutImage(file,bitmap);
					System.out.println("图片存储========================"+(b?"成功":"失败"));
				}else if(bitmap!=null && file.exists()){
					bitmap = PicHandler.getDrawable(file.getAbsolutePath(), imgW, imgH);
				}else if(bitmap ==null && !file.exists()){
					bitmap = BitmapFactory.decodeResource(context.getResources(), defId);
				}
				isInputStream.close();
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
	 * 将图片保存到本地ＳＤ�?
	 * 
	 * @param file
	 *            �?���?
	 * @return
	 * @author 刘星�?
	 */
	public static boolean OutPutImage(File file, Bitmap bitmap) {
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
