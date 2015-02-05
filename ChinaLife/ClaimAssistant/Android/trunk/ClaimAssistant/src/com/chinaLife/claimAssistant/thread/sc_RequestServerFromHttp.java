package com.chinaLife.claimAssistant.thread;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;


public class sc_RequestServerFromHttp {

	
	
	/**
	 * 下载图片
	 */
	public static boolean downloadPhoto(File file,String photoid) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "image"));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(sc_MyApplication.getInstance().getContext2())));
		params.add(new BasicNameValuePair("photoid", photoid));
		sc_DownloadFile downloaddata = new sc_DownloadFile(sc_MyApplication.URL + "claim",params, file);
		downloaddata.Post();
		String reponse = downloaddata.getReponse();
		if (reponse.equals("0")) {
			return true;
		} else {
			return false;
		}
	}


	
}
