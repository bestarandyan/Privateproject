package com.qingfengweb.piaoguanjia.orderSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyApplication.widthPixels = dm.widthPixels;
		MyApplication.heightPixels = dm.heightPixels;
//		Intent intent = new Intent();  
//		ComponentName comp = new ComponentName("com.qingfengweb.share","com.qingfengweb.share.MainActivity");  
//		intent.setComponent(comp);  
//		intent.setAction("android.intent.action.MAIN");  
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//		startActivity(intent);
		System.out.println("宽：" + dm.widthPixels + "-高：" + dm.heightPixels);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(SplashActivity.this, LoginActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
			}
		}, 2000);
//
//		if (copyApkFromAssets(this, "ShareProject.apk", Environment
//				.getExternalStorageDirectory().getAbsolutePath() + "/ShareProject.apk")) {
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.setDataAndType(
//					Uri.parse("file://"
//							+ Environment.getExternalStorageDirectory()
//									.getAbsolutePath() + "/ShareProject.apk"),
//					"application/vnd.android.package-archive");
//			this.startActivity(intent);
//		}
	}

	public boolean copyApkFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open(fileName);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}
}
