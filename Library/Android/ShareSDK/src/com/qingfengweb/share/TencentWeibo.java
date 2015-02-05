package com.qingfengweb.share;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;


public class TencentWeibo /*extends ShareSDK*/{/*
	//上下文
	public Activity context = null;
	//认证对象
	public OAuthV2 oAuth;

	public TencentWeibo(Activity context) {
		// 关闭OAuthV2Client中的默认开启的QHttpClient。
		OAuthV2Client.getQHttpClient().shutdownConnection();
		this.context = context;
	}
	
	
	@Override
	public void authorize() {
		if(oAuth==null){
			oAuth = new OAuthV2(ConstantsValues.TENCENT_REDIRECT_URL);
			oAuth.setClientId(ConstantsValues.TENCENT_CONSUMER_KEY);
			oAuth.setClientSecret(ConstantsValues.TENCENT_CONSUMER_SECRET);
		}
		if(!isAuthorize()){
			Intent intent = new Intent(context, OAuthV2AuthorizeWebView.class);//创建Intent，使用WebView让用户授权
            intent.putExtra("oauth", oAuth);
            context.startActivityForResult(intent,ConstantsValues.TENCENTWEIBO_TYPE);
		}
	}

	@Override
	public boolean sendMsg(String msg, String picurl) {
		TAPI tAPI = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
		boolean b = false;
		try {
			String reponse = "";
			if(picurl!=null&&!picurl.equals("")){
				reponse = tAPI.addPic(oAuth, "json", msg, "127.0.0.1",
						picurl);//发送带图片的微博内容
			}else{
				reponse = tAPI.add(oAuth, "json", msg, "127.0.0.1");//发送不带图片的微博内容
			}
			JSONObject json = new JSONObject(reponse);
			if (Integer.parseInt(json.get("ret").toString())==0) {
				b = true;
			}
			System.out.println(reponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		tAPI.shutdownConnection();
		return b;
	}


	@Override
	public boolean isAuthorize() {
		if(oAuth==null){
			oAuth = new OAuthV2(ConstantsValues.TENCENT_REDIRECT_URL);
			oAuth.setClientId(ConstantsValues.TENCENT_CONSUMER_KEY);
			oAuth.setClientSecret(ConstantsValues.TENCENT_CONSUMER_SECRET);
		}
		SharedPreferences pref = AccessTokenKeeper.readAccessToken(context, AccessTokenKeeper.TENCENT_PREFERENCES_NAME);
		oAuth.setAccessToken(pref.getString("token", ""));
		oAuth.setExpiresIn(pref.getString("expiresTime", ""));
		oAuth.setOpenid(pref.getString("uid", ""));
		oAuth.setOpenkey(pref.getString("key", ""));
		if(oAuth!=null
				&&oAuth.getAccessToken()!=null
				&&!oAuth.getAccessToken().equals("")
				&&(AccessTokenKeeper.getUid(context, AccessTokenKeeper.TENCENT_PREFERENCES_NAME)!=null
				&&!AccessTokenKeeper.getUid(context, AccessTokenKeeper.TENCENT_PREFERENCES_NAME).equals(""))){
			return true;
		}
		return false;
	
	}

	
*/}
