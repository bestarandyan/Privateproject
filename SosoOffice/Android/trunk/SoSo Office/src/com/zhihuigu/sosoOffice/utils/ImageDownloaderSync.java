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
import java.io.FileOutputStream;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.DownloadData;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
public class ImageDownloaderSync {
	Handler handler;
	Context context;
	Bitmap bitmap = null;
	int resid = R.drawable.soso_gray_logo;
	DownloadData downloaddata= null;
	
	
	private boolean iszhongduan = false;// «∑Ò÷–∂œ

	public ImageDownloaderSync(Context context,Handler handler) {
		this.handler = handler;
		this.context = context;
	}
	public boolean postData(File file, String sql, String id, String pixelswidth,String pixelsheight,String request_name) {
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
		downloaddata = new DownloadData(context, request_name,
				params1, file);
		downloaddata.Post();
		String reponse = downloaddata.getReponse();
		if (reponse.equals("0")) {
			dealreponse(sql);
			if(!iszhongduan){
				handler.sendEmptyMessage(0);
			}
			return true;
		} else {
			if(!iszhongduan){
				handler.sendEmptyMessage(1);
			}
			return false;
		}
	}

	private void dealreponse(String sql) {
		DBHelper.getInstance(context).execSql(sql);
	}

	public void overReponse(){
		iszhongduan = true;
		if(downloaddata!=null){
			downloaddata.overReponse();
		}
	}
}
