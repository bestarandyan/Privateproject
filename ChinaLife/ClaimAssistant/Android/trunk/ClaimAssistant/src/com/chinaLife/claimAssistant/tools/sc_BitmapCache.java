package com.chinaLife.claimAssistant.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

import com.chinaLife.claimAssistant.sc_MyApplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class sc_BitmapCache {
	static private sc_BitmapCache cache;/** 用于Chche内容的存储 */
	private Hashtable hashRefs; /** 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中） */
	private ReferenceQueue q;/*** 继承SoftReference，使得每一个实例都具有可识别的标识。*/
	private class MySoftRef extends SoftReference{
		private Integer _key = 0;
		public MySoftRef(Bitmap bmp, ReferenceQueue q, int key){
			super(bmp, q);
			_key = key;
		}
	}
	private sc_BitmapCache()
	{
		hashRefs = new Hashtable();
		q = new ReferenceQueue();
	}
	/**
	* 取得缓存器实例
	*/
	public static sc_BitmapCache getInstance()
	{
		if (cache == null){
		cache = new sc_BitmapCache();
		}
		return cache;
	}
	/**
	* 以软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
	*/
	private void addCacheBitmap(Bitmap bmp, Integer key){
		cleanCache();// 清除垃圾引用
		MySoftRef ref = new MySoftRef(bmp, q, key);
		hashRefs.put(key, ref);
	}
	/**
	* 依据所指定的drawable下的图片资源ID号（可以根据自己的需要从网络或本地path下获取），重新获取相应Bitmap对象的实例
	*/
	Bitmap bmp = null; // 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
	 public Bitmap getBitmap(int resId, Context context,BitmapFactory.Options opts){
		 if(bmp!=null){
			 bmp.recycle();
			 bmp = null;
			 System.gc();
		 }
		if (hashRefs.containsKey(resId)){
			MySoftRef ref = (MySoftRef) hashRefs.get(resId);
			bmp = (Bitmap) ref.get();
		}
		// 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
		// 并保存对这个新建实例的软引用
		if (bmp == null){
		// 传说decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，
		 // 无需再使用java层的createBitmap，从而节省了java层的空间。
			try{
				bmp = BitmapFactory.decodeStream(context.getResources().openRawResource(resId),null,opts);
			}catch(final OutOfMemoryError e){
				new Thread(new Runnable() {
					@Override
					public void run() {
						sc_LogUtil.sendLog(2, "处理图片内存溢出："+e.getMessage());
					}
				}).start();
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
	 
	// Bitmap bmp2 = null; // 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
	 /*	// public Bitmap getBitmap(String file, Context context,BitmapFactory.Options opts){
		 if(bmp2!=null){
			 bmp2.recycle();
			 bmp2 = null;
			 System.gc();
		 }
		if (hashRefs.containsKey(file.length())){
			MySoftRef ref = (MySoftRef) hashRefs.get(file);
			bmp2 = (Bitmap) ref.get();
		}
		// 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
		// 并保存对这个新建实例的软引用
		if (bmp2 == null){
		// 传说decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，
		 // 无需再使用java层的createBitmap，从而节省了java层的空间。
			try{
				bmp2 = BitmapFactory.decodeStream(context.getResources().openRawResource(resId),null,opts);
			}catch(OutOfMemoryError e){
				if(MyApplication.opLogger!=null){
					MyApplication.opLogger.error(e);
				}
				if(bmp2!=null){
					bmp2.recycle();
					bmp2 = null;
					System.gc();
				}
				bmp2 = BitmapFactory.decodeStream(context.getResources().openRawResource(resId));
			}
		 this.addCacheBitmap(bmp2, file);
		}
		return bmp2;
	}*/
	 Bitmap bmp1 = null; // 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
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
		// 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
		// 并保存对这个新建实例的软引用
		if (bmp1 == null){
		// 传说decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，
		 // 无需再使用java层的createBitmap，从而节省了java层的空间。
			InputStream is = null;
			try {
				is = byteTOInputStream(data);
			} catch (final Exception e1) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						sc_LogUtil.sendLog(2, "处理图片出现错误："+e1.getMessage());
					}
				}).start();
				e1.printStackTrace();
				if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				}
			}
		 try {
			bmp1 = BitmapFactory.decodeStream(is,null,opts);
			
		} catch (final OutOfMemoryError e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "处理图片出现内存溢出："+e.getMessage());
				}
			}).start();
			if(bmp1!=null){
				bmp1.recycle();
				bmp1 = null;
				System.gc();
			}
			bmp1 = BitmapFactory.decodeStream(is);
		}finally{
			if(is!=null){
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		}
		 this.addCacheBitmap(bmp1, data.length+data[1]);
		}
		return bmp1;
	}
	 
	 /**  
	     * 将byte数组转换成InputStream  
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
	* 清除Cache内的全部内容
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
