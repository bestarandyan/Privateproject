/**
 * 
 */
package com.qingfengweb.share;

import java.util.ArrayList;


import org.json.JSONObject;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * @author qingfeng
 *
 */
public class TencentZoneShare {
	Tencent tencent;
	Activity activity;
		public TencentZoneShare(Activity activity) {
			this.activity = activity;
		}
	
	public void tencentShare(){
		 tencent = Tencent.createInstance(ConstantsValues.TENCENT_ID, activity);
		 final Bundle params = new Bundle();
         params.putString("title", "这是一个测试连接");
         params.putString("summary", "链接地址是我们公司的网址");
         params.putString("targetUrl", "http://www.qingfengweb.com");
         // 支持传多个imageUrl
         ArrayList<String> imageUrls = new ArrayList<String>();
//         for (int i = 0; i < mImageContainerLayout.getChildCount(); i++) {
//         	LinearLayout addItem = (LinearLayout)mImageContainerLayout.getChildAt(i);
//         	EditText editText = (EditText)addItem.getChildAt(1);
         String url = Environment.getExternalStorageDirectory()+"/boluomi_children/beautyPhoto/beautyPhotoThemes/709.png";
         	imageUrls.add(url);
//         }
         params.putStringArrayList("imageUrl", imageUrls);
         doShareToQzone(params);
	}
	
	 /**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQzone(final Bundle params) {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
            	tencent.shareToQQ(activity, params, new IUiListener() {

                    @Override
                    public void onCancel() {
                        Util.toastMessage(activity, "onCancel: ");
                    }

                    @Override
                    public void onComplete(JSONObject response) {
                        // TODO Auto-generated method stub
                        Util.toastMessage(activity, "onComplete: " + response.toString());
                    }

                    @Override
                    public void onError(UiError e) {
                        // TODO Auto-generated method stub
                        Util.toastMessage(activity, "onComplete: " + e.errorMessage, "e");
                    }

                });
            }
        }).start();
    }
}
