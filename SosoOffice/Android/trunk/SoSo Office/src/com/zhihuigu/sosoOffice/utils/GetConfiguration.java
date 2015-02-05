package com.zhihuigu.sosoOffice.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.VersionInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;

public class GetConfiguration {
	
	private Context context;
	private SoSoUploadData uploaddata=null;
	private String reponse = "";
	public GetConfiguration(Context context){
		this.context = context;
	}
	
	/**
	 *根据商圈来获取楼盘详情
	 * @param districtid
	 */

	public boolean getConfigurations() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				context).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				context).getAPPKEY()));
		uploaddata = new SoSoUploadData(context, "SystemUpdateSelect.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse2();
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		}
		return false;
	}
	/***
	 * author by Ring 将获取的区域数据保存到本地数据库
	 */
	private void dealReponse2() {
		if (StringUtils.CheckReponse(reponse)) {
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			VersionInfo VersionInfo = null;
			VersionInfo = gson.fromJson(reponse, VersionInfo.class);

			if (VersionInfo != null) {
				values.put("ismustupdate", VersionInfo.getIsMustUpdate());
				values.put("version_number", VersionInfo.getVERSION_NUMBER());
				values.put("url", VersionInfo.getURL());
				DBHelper.getInstance(context).execSql("delete from soso_versioninfo");
				DBHelper.getInstance(context).insert("soso_versioninfo", values);
			}
			values.clear();
			values = null;
			DBHelper.getInstance(context).close();
		}
	}
	
	/***
	 * 中断请求
	 */
	public void overReponse() {
		if(uploaddata!=null){
			uploaddata.overReponse();
		}
		
	}
}
