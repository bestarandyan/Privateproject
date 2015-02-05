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
	 * ����ͼƬ���˷���ֻ�ʺ��ڱ�������Ϊ���ֶε�������
	 * @param imageUrl ͼƬ�������ַ
	 * @param callback �ص��ӿ�
	 * @param database ���ݿ�
	 * @param tabNameString ����
	 * @param info_id ��Ϣid
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
	 * ����ͼƬ�������浽sd��  ��ͼƬ�ڱ��ص�·�����������ݿ�
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
//			System.out.println("��һ�ε����������ļ���Ϊ-------------"+nameString);
			InputStream inputStream = url.openStream();
			if(inputStream!=null){
				String fileName = nameString;//��ͼƬȡһ���µ�����
				FileUtils.write2SDFromInput(ConstantClass.IMG_URL_STRING, fileName, inputStream);//���ļ����浽sd����
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
//				System.out.println("ͼƬ�洢========================"+(b?"�ɹ�":"ʧ��"));
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
	 * ��ͼƬ���浽���أӣĿ�
	 * 
	 * @param file
	 *            ��·��
	 * @return
	 * @author ������
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
