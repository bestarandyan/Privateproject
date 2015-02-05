package com.qingfengweb.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

public class DownImage {
	
	/**
	 * 下载网络图片
	 * @param 上下午
	 * @param 图片路径
	 * @param 默认图片的资源id
	 * @return 位图
	 */
	public static Bitmap getBitmap(Context c,String url,int defImgId){
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			bitmap = BitmapFactory.decodeResource(c.getResources(), defImgId);
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
				bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
			}
		} catch (IOException e) {
			bitmap = BitmapFactory.decodeResource(c.getResources(), defImgId);
			return bitmap;
		}
		return bitmap;
	}
	public static Drawable downloadImg(String url){
		URL myFileUrl = null;
		Drawable drawable = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
//			bitmap = BitmapFactory.decodeResource(c.getResources(), defImgId);
			return null;
		}
		try {
			drawable = Drawable.createFromStream(myFileUrl.openStream(), "src");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return drawable;
	}
}
