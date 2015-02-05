package com.qingfengweb.weddingideas.share;


import org.json.JSONObject;

import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


public class TencentZone extends ShareSDK{
	//上下文
	public Activity context = null;
	//qq开放平台对象
	private Tencent mTencent;
	public static final String SCOPE = "all";//开放的接口
	private Handler handler = null;
	private IRequestListener listener = null;
	

	public TencentZone(Activity context,Handler handler,IRequestListener listener) {
		 mTencent = Tencent.createInstance(ConstantsValuesShare.TENCENT_ID, context);
		this.context = context;
		this.handler = handler;
		this.listener = listener;
	}
	/**
	 * 绑定账号的回调函数
	 * @author QingFeng
	 *
	 */
	private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(JSONObject response) {
            doComplete(response);
        }
        protected void doComplete(JSONObject values) {
        	AccessTokenKeeper.keepAccessToken(context, AccessTokenKeeper.TENCENTZONE_PREFERENCES_NAME,
            		mTencent.getAccessToken(), mTencent.getOpenId(), System.currentTimeMillis() + 7*24*60*60*1000);
        	if (!isAuthorize()){
				handler.sendEmptyMessage(ERROR);
			}else{
				handler.sendEmptyMessage(SUCCESS);
			}
        }

        @Override
        public void onError(UiError e) {
        	handler.sendEmptyMessage(ERROR);
        }

        @Override
        public void onCancel() {
        	Toast.makeText(context, "已取消账号绑定", Toast.LENGTH_SHORT).show();
			System.out.println("已取消");
        }
    }

	@Override
	public void authorize() {
//		if (!isAuthorize()) {
            mTencent.login(context, SCOPE, new BaseUiListener(){});
//        } 
	}

	@Override
	public boolean sendMsg(String msg, String picurl) {
		 Bundle parmas = new Bundle();
         parmas.putString("title", "上海擎风网络科技");// 必须。feeds的标题，最长36个中文字，超出部分会被截断。
         parmas.putString("url",
                 "http://www.qingfengweb.com" + "#" + System.currentTimeMillis());// 必须。分享所在网页资源的链接，点击后跳转至第三方网页，
                                                                         // 请以http://开头。
         parmas.putString("comment", "");// 用户评论内容，也叫发表分享时的分享理由。禁止使用系统生产的语句进行代替。最长40个中文字，超出部分会被截断。
         parmas.putString("summary", msg);// 所分享的网页资源的摘要内容，或者是网页的概要描述。
                                                          // 最长80个中文字，超出部分会被截断。
         parmas.putString("images",
                 "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");// 所分享的网页资源的代表性图片链接"，请以http://开头，长度限制255字符。多张图片以竖线（|）分隔，目前只有第一张图片有效，图片规格100*100为佳。
         parmas.putString("type", "4");// 分享内容的类型。4网站，5视频
         parmas.putString("site", "上海擎风网络科技");// 分享的来源网站名称
         parmas.putString("fromurl", "http://www.qingfengweb.com");// 分享内容的类型。
         parmas.putString(
                 "playurl",
                 "http://player.youku.com/player.php/Type/Folder/Fid/15442464/Ob/1/Pt/0/sid/XMzA0NDM2NTUy/v.swf");// 长度限制为256字节。仅在type=5的时候有效。

         mTencent.requestAsync(Constants.GRAPH_ADD_SHARE, parmas,
                 Constants.HTTP_POST, listener, null);
		return false;
	}

	@Override
	public boolean isAuthorize() {
		SharedPreferences pref = context.getSharedPreferences(AccessTokenKeeper.TENCENTZONE_PREFERENCES_NAME, Context.MODE_APPEND);
		if(pref==null){
			return false;
		}
		if(mTencent==null){
			mTencent = Tencent.createInstance(ConstantsValuesShare.TENCENT_ID, context);
		}
		mTencent.setOpenId(pref.getString("uid", ""));                          
		mTencent.setAccessToken(pref.getString("token", ""), pref.getLong("expiresTime", 0)+"");
		if (mTencent != null && mTencent.isSessionValid()
                && mTencent.getOpenId() != null
				&&(AccessTokenKeeper.getUid(context, AccessTokenKeeper.TENCENTZONE_PREFERENCES_NAME)!=null
				&&!AccessTokenKeeper.getUid(context, AccessTokenKeeper.TENCENTZONE_PREFERENCES_NAME).equals(""))) {
			return true;
		}
		return false;
	}
	
}
