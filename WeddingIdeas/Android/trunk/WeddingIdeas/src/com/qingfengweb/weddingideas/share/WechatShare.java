package com.qingfengweb.weddingideas.share;




import java.io.File;




import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.SendAuth.Req;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXEmojiObject;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class WechatShare {
	
public Activity context = null;
private static final int THUMB_SIZE = 150;
private IWXAPI api;

	public WechatShare(Activity context) {
		this.context = context;
		api = WXAPIFactory.createWXAPI(context,ConstantsValuesShare.WECHAT_CONSUMER_ID);
		boolean b = api.registerApp(ConstantsValuesShare.WECHAT_CONSUMER_ID);
	}
	public boolean isAuthorize(){
		if(api!=null&&api.isWXAppInstalled()){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param msg
	 * @param picurl 图片的sd卡路径
	 * @param type
	 * @return
	 */
	public  boolean sendMSG(String msg, String picurl,int type) {
		File file = new File(picurl);
		WXMediaMessage wxmsg = new WXMediaMessage();
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		if (file.exists()) {
			WXImageObject imgObj = new WXImageObject();
			imgObj.setImagePath(picurl);
			wxmsg.mediaObject = imgObj;
			
			Bitmap bmp = BitmapFactory.decodeFile(picurl);
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
			bmp.recycle();
			wxmsg.thumbData = Util.bmpToByteArray(thumbBmp, true);
			req.transaction = buildTransaction("img");
		}else{
			WXTextObject textObj = new WXTextObject();
			textObj.text = msg;
			// 用WXTextObject对象初始化一个WXMediaMessage对象
			wxmsg.mediaObject = textObj;
			// 发送文本类型的消息时，title字段不起作用
			// msg.title = "Will be ignored";
			wxmsg.description = msg;
			// 构造一个Req
			req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		}
		req.message = wxmsg;
		if(type == 1){
			req.scene = SendMessageToWX.Req.WXSceneSession;//分享给朋友
		}else{
			req.scene = SendMessageToWX.Req.WXSceneTimeline;//分享给朋友网
		}
		boolean b = api.sendReq(req);
		System.out.println(b);
		return b;
	}
	
	/**
	 * 
	 * @param title
	 * @param description
	 * @param picurl
	 * @param httpUrl
	 * @param type
	 * @return
	 */
	public  boolean shareMSG(String title,String description,int resId,String httpUrl,int type) {
		
		WXMediaMessage mediaMsg = new WXMediaMessage();
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		mediaMsg.description = description;
		mediaMsg.title = title;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
		if(bitmap!=null){
			bitmap = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
			if(bitmap!=null){
				mediaMsg.thumbData = Util.bmpToByteArray(bitmap, true);
			}
		}
		WXWebpageObject wxmsg = new WXWebpageObject();
		wxmsg.webpageUrl = httpUrl;
		mediaMsg.mediaObject = wxmsg;
		req.transaction = buildTransaction("img");
		req.message = mediaMsg;
		if(type == 1){
			req.scene = SendMessageToWX.Req.WXSceneSession;//分享给朋友
		}else{
			req.scene = SendMessageToWX.Req.WXSceneTimeline;//分享给朋友网
		}
		boolean b = api.sendReq(req);
		System.out.println(b);
		return b;
	}
	public boolean isAuthorize(Intent intent,IWXAPIEventHandler iwxapieventhandler){
		if(api!=null&&api.isWXAppInstalled()){
			SendAuth.Req req = new Req();
			req.scope = "post_timeline";
			req.state = "none";
			boolean b = api.sendReq(req);
			return b;
		}
		return false;
	}
	
	public static AlertDialog showAlert(final Context context, final String msg, final String title) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		final Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				dialog.cancel();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}
	
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
}
