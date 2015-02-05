/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhihuigu.sosoOffice.utils;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;

import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.DownloadData;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

/**
 * This helper class download images from the Internet and binds those with the
 * provided ImageView.
 * 
 * <p>
 * It requires the INTERNET permission, which should be added to your
 * application's manifest file.
 * </p>
 * 
 * A local cache of downloaded images is maintained internally to improve
 * performance.
 */
public class ImageDownloaderId {
	private static final String LOG = "ImageDownloader";
	int connection_count = 0;
	int connection_count_totle = 0;
	Context context;
	Bitmap bitmap = null;
	int resid = R.drawable.soso_gray_logo;

	public ImageDownloaderId(Context context,int connection_count_totle) {
		this.context = context;
		this.connection_count_totle = connection_count_totle;
	}
	public ImageDownloaderId(Context context,int resid,int connection_count_totle) {
		this.context = context;
		this.resid = resid;
		this.connection_count_totle = connection_count_totle;
	}

	/**
	 * Allow a new delay before the automatic cache clear is done.
	 */
	private void resetPurgeTimer() {
		purgeHandler.removeCallbacks(purger);
		purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
	}

	/**
	 * Download the specified image from the Internet and binds it to the
	 * provided ImageView. The binding is immediate if the image is found in the
	 * cache and will be done asynchronously otherwise. A null bitmap will be
	 * associated to the ImageView if an error occurs.
	 * 
	 * @param url
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void download(File file, String sql, String id, String pixelswidth,
			String pixelsheight, String request_name, ImageView imageView,String x,String y,String width,String height) {
		resetPurgeTimer(); // 清空集合
		Bitmap bitmap = getBitmapFromCache(file.getAbsolutePath());
		if(id==null||id.trim().equals("")||id.trim().equals("0")){
			return;
		}
		if(connection_count>connection_count_totle){
			return;
		}
		if (bitmap == null) {
			System.out.println("1");
			forceDownload(file, sql, id, pixelswidth, pixelsheight,
					request_name, imageView,x,y,width,height);
		} else {
			System.out.println("2");
			cancelPotentialDownload(file.getAbsolutePath(), imageView);
			
			imageView.setImageBitmap(bitmap);
		}
	}

	/**
	 * Same as download but the image is always downloaded and the cache is not
	 * used. Kept private at the moment as its interest is not clear.
	 */
	private void forceDownload(File file, String sql, String id,
			String pixelswidth, String pixelsheight, String request_name,
			ImageView imageView,String x,String y,String width,String height) {
		// State sanity: url is guaranteed to never be null in
		// DownloadedDrawable and cache keys.
		if (file.getAbsolutePath() == null) {
//			imageView.setImageResource(resid);
			return;
		}
		Log.i("测试", imageView.toString()+"-----"+file.getAbsolutePath());
		if (cancelPotentialDownload(file.getAbsolutePath(), imageView)) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(
					task, context,resid);
			imageView.setImageDrawable(downloadedDrawable);

			task.execute(file, sql, id, pixelswidth, pixelsheight,
					request_name,x,y,width,height);
		}
	}

	/**
	 * Returns true if the current download has been canceled or if there was no
	 * download in progress on this image view. Returns false if the download in
	 * progress deals with the same url. The download is not stopped in that
	 * case.
	 */
	private static boolean cancelPotentialDownload(String filename,
			ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.filename;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(filename))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				// The same URL is already being downloaded.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active download task (if any) associated
	 *         with this imageView. null if there is no such task.
	 */
	private static BitmapDownloaderTask getBitmapDownloaderTask(
			ImageView imageView) {
		System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	private boolean postData(File file, String sql, String id, String pixelswidth,String pixelsheight,String request_name) {
		if(id==null||id.equals("")){
			return false;
		}
		List<BasicNameValuePair> params1 = new ArrayList<BasicNameValuePair>();
		params1.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				context).getAPPID()));
		params1.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				context).getAPPKEY()));
		params1.add(new BasicNameValuePair("ImageID", id));
		params1.add(new BasicNameValuePair("Height", pixelsheight));
		params1.add(new BasicNameValuePair("Width", pixelswidth));
		DownloadData downloaddata = new DownloadData(context, request_name,
				params1, file);
		downloaddata.Post();
		String reponse = downloaddata.getReponse();
		if (reponse.equals("0")) {
			dealreponse(sql);
			return true;
		} else {
			return false;
		}
	}

	private void dealreponse(String sql) {
		DBHelper.getInstance(context).execSql(sql);
	}

	public Bitmap downloadBitmap(File file, String sql, String id, String pixelswidth,String pixelsheight,
			String request_name,int x,int y,int width,int height) {
		System.out.println("进入到方法中区1");
		Bitmap bitmap = null;
		if (file.exists() && file.isFile()) {
			try {
				bitmap = BitmapCache.getInstance().getBitmap(file, context);
				if(bitmap !=null)
					bitmap = Bitmap.createBitmap(bitmap, x, 
						y, width, height);
//				//判断房源图片是否和手机分辨率大小相同
//				if(bitmap.getWidth() == MyApplication.getInstance().getScreenWidth() && bitmap.getHeight() == MyApplication.getInstance().getScreenHeight()){
//					bitmap = CommonUtils.cutImg(bitmap, 0, height,width,height);
//				}else{//不相同
//					if(bitmap.getWidth()>MyApplication.getInstance().getScreenWidth()){//宽大于分辨率的宽
//						if(bitmap.getHeight()>(MyApplication.getInstance().getScreenHeight()/3)){//高度大于分辨率的
//							bitmap = CommonUtils.cutImg(bitmap, 0, 0,MyApplication.getInstance().getScreenWidth(),MyApplication.getInstance().getScreenHeight()/3);
//						}else{
//							bitmap = CommonUtils.cutImg(bitmap, 0, 0,MyApplication.getInstance().getScreenWidth(),bitmap.getHeight());
//						}
//					}else{
//						if(bitmap.getHeight()>(MyApplication.getInstance().getScreenHeight()/3)){
//							bitmap = CommonUtils.cutImg(bitmap, 0, 0,bitmap.getWidth(),MyApplication.getInstance().getScreenHeight()/3);
//						}else{
//							bitmap = CommonUtils.cutImg(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight());
//						}
//					}
//					
//				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (bitmap != null) {
				return bitmap;
			}
		}
		System.out.println("进入到方法中区2");
		connection_count++;
		boolean b = postData(file, sql, id, pixelswidth,pixelsheight, request_name);
		System.out.println("进入到方法中区3");
		connection_count--;
		if (b) {
			try {
				bitmap = BitmapCache.getInstance().getBitmap(file, context);
				if(bitmap !=null)
					bitmap = Bitmap.createBitmap(bitmap, x, 
						y, width, height);
//				bitmap = CommonUtils.getDrawable(file.getAbsolutePath(), null);
//				//判断房源图片是否和手机分辨率大小相同
//				if(bitmap.getWidth() == MyApplication.getInstance().getScreenWidth() && bitmap.getHeight() == MyApplication.getInstance().getScreenHeight()){
//					bitmap = CommonUtils.cutImg(bitmap, 0, height,width,height);
//				}else{//不相同
//					if(bitmap.getWidth()>MyApplication.getInstance().getScreenWidth()){//宽大于分辨率的宽
//						if(bitmap.getHeight()>(MyApplication.getInstance().getScreenHeight()/3)){//高度大于分辨率的
//							bitmap = CommonUtils.cutImg(bitmap, 0, 0,MyApplication.getInstance().getScreenWidth(),MyApplication.getInstance().getScreenHeight()/3);
//						}else{
//							bitmap = CommonUtils.cutImg(bitmap, 0, 0,MyApplication.getInstance().getScreenWidth(),bitmap.getHeight());
//						}
//					}else{
//						if(bitmap.getHeight()>(MyApplication.getInstance().getScreenHeight()/3)){
//							bitmap = CommonUtils.cutImg(bitmap, 0, 0,bitmap.getWidth(),MyApplication.getInstance().getScreenHeight()/3);
//						}else{
//							bitmap = CommonUtils.cutImg(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight());
//						}
//					}
//					
//				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return bitmap;
	}

	/*
	 * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */
	// static class FlushedInputStream extends FilterInputStream {
	// public FlushedInputStream(InputStream inputStream) {
	// super(inputStream);
	// }
	//
	// @Override
	// public long skip(long n) throws IOException {
	// long totalBytesSkipped = 0L;
	// while (totalBytesSkipped < n) {
	// long bytesSkipped = in.skip(n - totalBytesSkipped);
	// if (bytesSkipped == 0L) {
	// int b = read();
	// if (b < 0) {
	// break; // we reached EOF
	// } else {
	// bytesSkipped = 1; // we read one byte
	// }
	// }
	// totalBytesSkipped += bytesSkipped;
	// }
	// return totalBytesSkipped;
	// }
	// }

	/**
	 * The actual AsyncTask that will asynchronously download the image.
	 */
	class BitmapDownloaderTask extends AsyncTask<Object, Void, Bitmap> {
		private String filename;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		/**
		 * Actual download method.
		 */
		@Override
		protected Bitmap doInBackground(Object... params) {
			filename = ((File) params[0]).getAbsolutePath();
			Bitmap b = downloadBitmap((File) params[0], (String) params[1],
					(String) params[2], (String) params[3], (String) params[4],(String) params[5]
							,Integer.parseInt((String) params[6])
							,Integer.parseInt((String) params[7])
							,Integer.parseInt((String) params[8])
							,Integer.parseInt((String) params[9]));
			System.out.println("----------------------------------------------");
//			System.out.println("---------------"+filename+"--------------------"+b==null?true:false);
			return b;
		}

		/**
		 * Once the image is downloaded, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			if(bitmap == null){
				return;
			}
			addBitmapToCache(filename, bitmap);
			
			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				if (imageView == null)
					return;
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				// Change bitmap only if this process is still associated with
				// it
				if ((this == bitmapDownloaderTask&&bitmap!=null)) {
					imageView.setImageBitmap(bitmap);
				} else {
//					imageView.setImageResource(resid);
				}
			}
		}
	}

	/**
	 * A fake Drawable that will be attached to the imageView while the download
	 * is in progress.
	 * 
	 * <p>
	 * Contains a reference to the actual download task, so that a download task
	 * can be stopped if a new binding is required, and makes sure that only the
	 * last started download process can bind its result, independently of the
	 * download finish order.
	 * </p>
	 */
	static class DownloadedDrawable extends BitmapDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask,
				Context context,int resid) {
			super(BitmapFactory.decodeResource(context.getResources(),
					resid));
			bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(
					bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}

	public void setMode() {
		clearCache();
	}

	/*
	 * Cache-related fields and methods.
	 * 
	 * We use a hard and a soft cache. A soft reference cache is too
	 * aggressively cleared by the Garbage Collector.
	 */

	private static final int HARD_CACHE_CAPACITY = 10;
	private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in milliseconds

	// Hard cache, with a fixed maximum capacity and a life duration
	private final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(
			HARD_CACHE_CAPACITY / 2, 0.75f, true) {
		@Override
		protected boolean removeEldestEntry(
				LinkedHashMap.Entry<String, Bitmap> eldest) {
			if (size() > HARD_CACHE_CAPACITY) {
				// Entries push-out of hard reference cache are transferred to
				// soft reference cache
				sSoftBitmapCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else
				return false;
		}
	};

	// Soft cache for bitmaps kicked out of hard cache
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			HARD_CACHE_CAPACITY / 2);

	private final Handler purgeHandler = new Handler();

	private final Runnable purger = new Runnable() {
		public void run() {
			clearCache();
		}
	};

	/**
	 * Adds this bitmap to the cache.
	 * 
	 * @param bitmap
	 *            The newly downloaded bitmap.
	 */
	private void addBitmapToCache(String url, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (sHardBitmapCache) {
				sHardBitmapCache.put(url, bitmap);
			}
		}
	}

	/**
	 * @param url
	 *            The URL of the image that will be retrieved from the cache.
	 * @return The cached bitmap or null if it was not found.
	 */
	private Bitmap getBitmapFromCache(String url) {
		// First try the hard reference cache
		synchronized (sHardBitmapCache) {
			final Bitmap bitmap = sHardBitmapCache.get(url);
			if (bitmap != null) {
				// Bitmap found in hard cache
				// Move element to first position, so that it is removed last
				sHardBitmapCache.remove(url);
				sHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}

		// Then try the soft reference cache
		SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				// Bitmap found in soft cache
				return bitmap;
			} else {
				// Soft reference has been Garbage Collected
				sSoftBitmapCache.remove(url);
			}
		}

		return null;
	}

	/**
	 * Clears the image cache used internally to improve performance. Note that
	 * for memory efficiency reasons, the cache will automatically be cleared
	 * after a certain inactivity delay.
	 */
	public void clearCache() {
		sHardBitmapCache.clear();
		sSoftBitmapCache.clear();
	}

}
