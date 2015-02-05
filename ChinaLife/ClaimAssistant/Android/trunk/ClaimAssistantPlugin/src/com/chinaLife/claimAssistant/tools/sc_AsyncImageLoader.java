package com.chinaLife.claimAssistant.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import com.chinaLife.claimAssistant.activity.Sc_MyApplication;
import com.chinaLife.claimAssistant.thread.sc_ThreadDemo;

public class sc_AsyncImageLoader {

	private HashMap<String, SoftReference<Drawable>> imageCache;

	public sc_AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public Drawable loadDrawable(final String photoid,final String imageUrl,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable;
				try {
					drawable = loadImageFromUrl(photoid,imageUrl);
					if(drawable==null){
						return;
					}
					imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			}
		}.start();
		return null;
	}

	public static Drawable loadImageFromUrl(String photoid,String url) throws IOException{
		InputStream i = null;
		String name = sc_MD5.getMD5(url) + url.substring(url.lastIndexOf("."));
		File file = new File(Sc_MyApplication.cache, name);
		i = Post(photoid);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(sc_EncryptPhoto.key.getBytes());
		byte[] temp = new byte[512];
		int readLen = 0;
		while ((readLen = i.read(temp)) > 0) {
			fos.write(temp, 0, readLen);
		}
		try {
			i.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = null;
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
	
	public static InputStream Post(String photoid) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "image"));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(Sc_MyApplication.getInstance().getContext2())));
		params.add(new BasicNameValuePair("photoid", photoid));
		while (!sc_ThreadDemo.isstart) {
		}
		if(!Sc_MyApplication.getInstance().isUploadon()){
			System.out.println(params);
			HttpPost request = new HttpPost(Sc_MyApplication.URL + "claim");
			HttpParams httpParameters = new BasicHttpParams();
			try {
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
				request.setParams(httpParameters);
			} catch (final UnsupportedEncodingException e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						sc_LogUtil.sendLog(2, "请求参数错误："+e.getMessage());
					}
				}).start();
			}
			// 发送请求
			try {
				// // 得到应答的字符串，这是一个 JSON 格式保存的数据
				return connServerForResult(request);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		
		}else{
			return null;
		}
	}
	
	// 得到服务器响应数据
		private static InputStream connServerForResult(HttpPost request) {
			// HttpGet对象
			InputStream in = null;
			try {
				// 获得HttpResponse对象
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(request);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
					return null;
				}
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// 取得返回的数据
					in = httpResponse.getEntity().getContent();
				}
			} catch (final Exception e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						sc_LogUtil.sendLog(2, "异步下载图片时，请求服务器失败"+e.getMessage());
					}
				}).start();
				
				sc_VisiteTimes2.getInstance().count();
				if(sc_VisiteTimes2.getInstance().isOut()){
					if(sc_VisiteTimes2.getInstance().isInit()){
						sc_VisiteTimes2.getInstance().init();
					}
				}
				e.printStackTrace();
				return null;
			}
			return in;
		}

}
