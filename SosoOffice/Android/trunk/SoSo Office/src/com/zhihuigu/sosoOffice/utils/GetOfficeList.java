package com.zhihuigu.sosoOffice.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.model.SoSoBuildInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;

public class GetOfficeList {

	private Context context;
	private SoSoUploadData uploaddata = null;
	private String reponse = "";

	public GetOfficeList(Context context) {
		this.context = context;
	}

	/**
	 * 根据条件查询房源
	 * 
	 * @param whereClause  查询条件
	 * @param orderBy 排序方式
	 * @param pageIndex   索引
	 * @param pageLength  显示数量
	 */

//	public void getOfficeList(String orderby, String pageIndex,
//			String pageLength, String whereClause) {
//		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
//				context).getAPPID()));
//		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
//				context).getAPPKEY()));
//		params.add(new BasicNameValuePair("whereClause", whereClause));
//		params.add(new BasicNameValuePair("orderBy", orderby));
//		params.add(new BasicNameValuePair("pageLength", pageLength));
//		params.add(new BasicNameValuePair("pageIndex", pageIndex));
//		uploaddata = new SoSoUploadData(context, "BuildSelect.aspx", params);
//		uploaddata.Post();
//		reponse = uploaddata.getReponse();
//		dealReponse2();
//		params.clear();
//		params = null;
//	}
//
//	/***
//	 * author by Ring 将获取的区域数据保存到本地数据库
//	 */
//	private void dealReponse2() {
//		if (StringUtils.CheckReponse(reponse)) {
//			Type listType = new TypeToken<LinkedList<SoSoBuildInfo>>() {
//			}.getType();
//			Gson gson = new Gson();
//			LinkedList<SoSoBuildInfo> sosobuildinfos = null;
//			SoSoBuildInfo sosobuildinfo = new SoSoBuildInfo();
//			sosobuildinfos = gson.fromJson(reponse, listType);
//			if (sosobuildinfos != null && sosobuildinfos.size() > 0) {
//			}
//			if (sosobuildinfos != null) {
//				sosobuildinfos.clear();
//				sosobuildinfos = null;
//			}
//		}
//	}
}
