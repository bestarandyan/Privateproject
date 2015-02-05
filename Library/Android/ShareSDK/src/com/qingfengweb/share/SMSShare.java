package com.qingfengweb.share;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * 短信分享
 * 
 * @author QingFeng
 * 
 */
public class SMSShare {
	public Activity activity;

	public SMSShare(Activity activity) {
		this.activity = activity;
	}

	public void startSMSShare(String msg, String picurl) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", msg);
		activity.startActivity(intent);
	}
}
