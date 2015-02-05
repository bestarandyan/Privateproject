package com.qingfengweb.weddingideas.share;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

/**
 * 新浪微博扩展接口
 * @author QingFeng
 *
 */

public class SinaWeibo extends ShareSDK {
	private StatusesAPI weiboapi = null;
	private Weibo weibo = null;
	public Oauth2AccessToken accessToken;
	public Context context = null;
	private RequestListener listener = null;
	private Handler handler = null;
	
	/**
	 * 
	 * @param context，上下文
	 * @param handler 验证完成后的回调函数
	 */
	public SinaWeibo(Context context,RequestListener listener,Handler handler) {
		this.context = context;
		this.listener = listener;
		this.handler = handler;
		System.out.println("SinaWeibo");
	}
	/**
	 * 只验证一次并保存
	 */
	@Override
	public void authorize() {
		if(!isAuthorize()){
			weibo = Weibo.getInstance(ConstantsValuesShare.SINA_CONSUMER_KEY,
					ConstantsValuesShare.SINA_REDIRECT_URL);
			weibo.authorize(context, new AuthDialogListener());
		}
	}
	/**
	 * 切换账号
	 */
	public void changeUser(){
		SharedPreferences pref = AccessTokenKeeper.readAccessToken(context, AccessTokenKeeper.SINA_PREFERENCES_NAME);
		if(pref!=null){
			accessToken.setToken("");
			accessToken.setExpiresTime(0);
		}
		weibo = null;
		weibo = Weibo.getInstance(ConstantsValuesShare.SINA_CONSUMER_KEY,
				ConstantsValuesShare.SINA_REDIRECT_URL);
		weibo.authorize(context, new AuthDialogListener());
	}
	/**
	 * 每次都需要验证
	 */
	public void authorizes() {
		weibo = Weibo.getInstance(ConstantsValuesShare.SINA_CONSUMER_KEY,
				ConstantsValuesShare.SINA_REDIRECT_URL);
		weibo.authorize(context, new AuthDialogListener());
	}
	

	@Override
	public boolean sendMsg(String msg,String picurl) {
		System.out.println("sendMsg__start");
		weiboapi = new StatusesAPI(accessToken);
		if(picurl!=null&&!picurl.equals("")){
			weiboapi.upload(msg, picurl, "", "", listener);
		}else{
			weiboapi.update(msg, "", "", listener);
		}
		System.out.println("sendMsg__end");
		return false;
	}

	@Override
	public boolean isAuthorize() {
		accessToken = new Oauth2AccessToken();
		SharedPreferences pref = AccessTokenKeeper.readAccessToken(context, AccessTokenKeeper.SINA_PREFERENCES_NAME);
		if(pref==null){
			return false;
		}
		accessToken.setToken(pref.getString("token", ""));
		accessToken.setExpiresTime(pref.getLong("expiresTime", 0));
		if (accessToken != null && accessToken.isSessionValid()
				&&(AccessTokenKeeper.getUid(context, AccessTokenKeeper.SINA_PREFERENCES_NAME)!=null
				&&!AccessTokenKeeper.getUid(context, AccessTokenKeeper.SINA_PREFERENCES_NAME).equals(""))) {
			return true;
		}
		return false;
	}
	/**
	 * 绑定账号的处理类和回调处理
	 * @author QingFeng
	 *
	 */
	class AuthDialogListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			accessToken = new Oauth2AccessToken(token, expires_in);
			AccessTokenKeeper.keepAccessToken(context,
					AccessTokenKeeper.SINA_PREFERENCES_NAME,
					accessToken.getToken(), values.getString("uid"),
					accessToken.getExpiresTime());

			if (!isAuthorize()){
				handler.sendEmptyMessage(ERROR);
			}else{
				handler.sendEmptyMessage(SUCCESS);
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			handler.sendEmptyMessage(ERROR);
			e.printStackTrace();
		}

		@Override
		public void onCancel() {
			Toast.makeText(context, "已取消账号绑定", Toast.LENGTH_SHORT).show();
			System.out.println("已取消");
		}

		@Override
		public void onWeiboException(WeiboException e) {
			System.out.println("onWeiboException__start");
			handler.sendEmptyMessage(ERROR);
			e.printStackTrace();
			System.out.println("onWeiboException__end");
		}

	}
}
