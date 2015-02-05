package com.qingfengweb.boluomi.share;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;

public class EmailShare {
	public Activity activity;
	public EmailShare(Activity activity){
		this.activity = activity;
	}
	/***
	 * author by Ring 发送邮件
	 */

	public void startSendToEmailIntent(String msg, String picurl) {

		Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
		intent.setType("text/plain");
		intent.setType("image/*"); // 分享图片信息类型
		List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(
				intent, 0);
		if (!resInfo.isEmpty()) {
			List<Intent> targetedShareIntents = new ArrayList<Intent>();
			for (ResolveInfo info : resInfo) {
				Intent targeted = new Intent(Intent.ACTION_SEND);
				targeted.setType("image/*"); // 分享图片信息类型
				targeted.setType("text/plain");
				targeted.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(picurl)));
				targeted.putExtra(Intent.EXTRA_TEXT, msg);
				targeted.putExtra(Intent.EXTRA_SUBJECT, "主题");
//				targeted.putExtra(Intent.EXTRA_EMAIL, addresses);
				ActivityInfo activityInfo = info.activityInfo;
				if (activityInfo.packageName.contains("bluetooth")
						|| activityInfo.name.contains("bluetooth")) {
					continue; // 过滤蓝牙应用
				}
				if (activityInfo.packageName.contains("com.my.activity")
						|| activityInfo.name.contains("com.my.activity")) {
					continue; // 过滤自己的应用包
				}
				if (activityInfo.packageName.contains("gm")
						|| activityInfo.name.contains("mail")) {
					targeted.putExtra(Intent.EXTRA_TEXT, msg);
				} else if (activityInfo.packageName.contains("zxing")) {
					continue; // 过滤自己的应用包
				} else {
					continue; // 过滤自己的应用包
				}
				targeted.setPackage(activityInfo.packageName);
				targetedShareIntents.add(targeted);
			}
			// 分享框标题
			if (targetedShareIntents != null && targetedShareIntents.size() > 0) {
				Intent chooserIntent = Intent.createChooser(
						targetedShareIntents.remove(0), "选择程序分享");
				if (chooserIntent == null) {
					return;
				}
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						targetedShareIntents.toArray(new Parcelable[] {}));
				try {
					activity.startActivity(chooserIntent);
				} catch (android.content.ActivityNotFoundException ex) {
					Intent i2 = new Intent(Intent.ACTION_VIEW,
							Uri.parse("mailto:" + "simple@163.com"));
					activity.startActivity(i2);
				}
			} else {
				Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"
						+ "simple@163.com"));
				activity.startActivity(i3);
			}

		}
	}

}
