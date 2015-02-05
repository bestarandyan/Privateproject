package com.vnc.draw.activity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapCache {
	static private BitmapCache cache;/** ����Chche���ݵĴ洢 */
	private Hashtable hashRefs; /** ����Reference�Ķ��У������õĶ����Ѿ������գ��򽫸����ô�������У� */
	private ReferenceQueue q;/*** �̳�SoftReference��ʹ��ÿһ��ʵ�������п�ʶ��ı�ʶ��*/
	private class MySoftRef extends SoftReference{
		private Integer _key = 0;
		public MySoftRef(Bitmap bmp, ReferenceQueue q, int key){
			super(bmp, q);
			_key = key;
		}
	}
	private BitmapCache()
	{
		hashRefs = new Hashtable();
		q = new ReferenceQueue();
	}
	/**
	* ȡ�û�����ʵ��
	*/
	public static BitmapCache getInstance()
	{
		if (cache == null){
		cache = new BitmapCache();
		}
		return cache;
	}
	/**
	* �������õķ�ʽ��һ��Bitmap�����ʵ���������ò����������
	*/
	private void addCacheBitmap(Bitmap bmp, Integer key){
		cleanCache();// �����������
		MySoftRef ref = new MySoftRef(bmp, q, key);
		hashRefs.put(key, ref);
	}
	/**
	* ������ָ����drawable�µ�ͼƬ��ԴID�ţ����Ը����Լ�����Ҫ������򱾵�path�»�ȡ�������»�ȡ��ӦBitmap�����ʵ��
	*/
	Bitmap bmp = null; // �������Ƿ��и�Bitmapʵ���������ã�����У�����������ȡ�á�
	 public Bitmap getBitmap(int resId, Context context){
		 if(bmp1!=null){
			 bmp1.recycle();
			 bmp1 = null;
			 System.gc();
		 }
		if (hashRefs.containsKey(resId)){
			MySoftRef ref = (MySoftRef) hashRefs.get(resId);
			bmp = (Bitmap) ref.get();
		}
		// ���û�������ã����ߴ��������еõ���ʵ����null�����¹���һ��ʵ����
		// �����������½�ʵ����������
		if (bmp == null){
		// ��˵decodeStreamֱ�ӵ���JNI>>nativeDecodeAsset()�����decode��
		 // ������ʹ��java���createBitmap���Ӷ���ʡ��java��Ŀռ䡣
			try{
				bmp = BitmapFactory.decodeStream(context.getResources().openRawResource(resId));
			}catch(OutOfMemoryError e){
				if(bmp!=null){
					bmp.recycle();
					bmp = null;
					System.gc();
				}
				bmp = BitmapFactory.decodeStream(context.getResources().openRawResource(resId));
			}
		 this.addCacheBitmap(bmp, resId);
		}
		return bmp;
	}
	 Bitmap bmp1 = null; // �������Ƿ��и�Bitmapʵ���������ã�����У�����������ȡ�á�
	 public Bitmap getBitmap(byte[] data, Context context,BitmapFactory.Options opts){
		 if(bmp1!=null){
			 bmp1.recycle();
			 bmp1 = null;
			 System.gc();
		 }
		if (hashRefs.containsKey(data.length+data[1])){
			MySoftRef ref = (MySoftRef) hashRefs.get(data.length+data[1]);
			bmp1 = (Bitmap) ref.get();
		}
		// ���û�������ã����ߴ��������еõ���ʵ����null�����¹���һ��ʵ����
		// �����������½�ʵ����������
		if (bmp1 == null){
		// ��˵decodeStreamֱ�ӵ���JNI>>nativeDecodeAsset()�����decode��
		 // ������ʹ��java���createBitmap���Ӷ���ʡ��java��Ŀռ䡣
			InputStream is = null;
			try {
				is = byteTOInputStream(data);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		 try {
			bmp1 = BitmapFactory.decodeStream(is,null,opts);
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (OutOfMemoryError e) {
			if(bmp1!=null){
				bmp1.recycle();
				bmp1 = null;
				System.gc();
			}
			bmp1 = BitmapFactory.decodeStream(is);
		}
		 this.addCacheBitmap(bmp1, data.length+data[1]);
		}
		return bmp1;
	}
	 
	 /**  
	     * ��byte����ת����InputStream  
	     * @param in  
	     * @return  
	     * @throws Exception  
	     */  
	    public static InputStream byteTOInputStream(byte[] in) throws Exception{  
	        ByteArrayInputStream is = new ByteArrayInputStream(in);  
	        is.close();
	        return is;  
	    }  

	private void cleanCache(){
		MySoftRef ref = null;
		while ((ref = (MySoftRef) q.poll()) != null){
			hashRefs.remove(ref._key);
		}
	}
	/**
	* ���Cache�ڵ�ȫ������
	*/
	public void clearCache(){
		cleanCache();
		hashRefs.clear();
		System.gc();
		System.runFinalization();
		if(bmp!=null){
			bmp.recycle();
			bmp = null;
		}
		if(bmp1!=null){
			bmp1.recycle();
			bmp1 = null;
		}
		System.gc();
	}
}

