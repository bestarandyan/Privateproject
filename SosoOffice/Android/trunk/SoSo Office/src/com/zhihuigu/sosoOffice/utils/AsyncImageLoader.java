package com.zhihuigu.sosoOffice.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.zhihuigu.sosoOffice.database.DBHelper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncImageLoader {// SoftReference是软引用，是为了更好的为了系统回收变量
	private HashMap<String, SoftReference<Drawable>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public Drawable loadDrawable(final String imageUrl,
			final ImageView imageView, final ImageCallback imageCallback,
			final File file, final Context context, final String sql) {
		if (imageCache.containsKey(imageUrl)) {
			// 从缓存中获取
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageView,
						imageUrl);
			}
		};
		// 建立新一个新的线程下载图片
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl, file, context,
						sql);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public Drawable loadImageFromUrl(String url, File file, Context context,
			String sql) {
		downloadImage(url, file, context, sql);
		Drawable d = null;
		d = Drawable.createFromPath(file.getAbsolutePath());
		return d;
	}

	// 回调接口
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, ImageView imageView,
				String imageUrl);
	}

	// 得到服务器响应数据
	private void downloadImage(String URL, File file, Context context,
			String sql) {
		try {
			HttpGet request = new HttpGet(URL);
			HttpClient httpClient = new DefaultHttpClient();
			// 连接超时
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 1000 * 10);
			// 请求超时
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 10 * 1000);

			// 获得HttpResponse对象
			HttpResponse httpResponse = httpClient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return;
			}
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream in = null;
				in = httpResponse.getEntity().getContent();
				// 取得返回的数据
				if (file.exists() && file.isDirectory()) {
					file.delete();
				}
				FileOutputStream fileout = new FileOutputStream(file);
				int tem = 0;
				byte[] bytes = new byte[1024];
				while ((tem = in.read(bytes)) != -1) {
					fileout.write(bytes, 0, tem);
					fileout.flush();
				}
				fileout.close();
				fileout = null;
				dealreponse(context, sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dealreponse(Context context, String sql) {
		DBHelper.getInstance(context).execSql(sql);
	}
}
