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

public class GetBuilds {
	
	private Context context;
	private SoSoUploadData uploaddata=null;
	private String reponse = "";
	public GetBuilds(Context context){
		this.context = context;
	}
	
	/**
	 *根据商圈来获取楼盘详情
	 * @param districtid
	 */

	public void getBuildsBycity(String city) {
		if(city==null||
				city.equals("")){
			return;
		}
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				context).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				context).getAPPKEY()));
		params.add(new BasicNameValuePair("whereClause", "CityID ='"+city+"'"));
		params.add(new BasicNameValuePair("orderBy", "adddate"));
		uploaddata = new SoSoUploadData(context, "BuildSelect.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse2(city);
		params.clear();
		params = null;
	}
	/***
	 * author by Ring 将获取的区域数据保存到本地数据库
	 */
	private void dealReponse2(String city) {
		if (StringUtils.CheckReponse(reponse)) {
			Type listType = new TypeToken<LinkedList<SoSoBuildInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoBuildInfo> sosobuildinfos = null;
			SoSoBuildInfo sosobuildinfo = new SoSoBuildInfo();
			sosobuildinfos = gson.fromJson(reponse, listType);
			if (sosobuildinfos != null && sosobuildinfos.size() > 0) {
				sosobuildinfo.InsertBuildInfos(context, sosobuildinfos);
			}
			if (sosobuildinfos != null) {
				sosobuildinfos.clear();
				sosobuildinfos = null;
			}
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

	/**
	 *
	 * 作者：Ring
	 * 创建于：2013-2-18
	 * @param districtid
	 */
	public void getBuildsBydistrictid(String districtid) {
		if(districtid==null||
				districtid.equals("")){
			return;
		}
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				context).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				context).getAPPKEY()));
		params.add(new BasicNameValuePair("whereClause", "DistrictID ='"+districtid+"'"));
		params.add(new BasicNameValuePair("orderBy", "adddate"));
		params.add(new BasicNameValuePair("UpdateDate", ""));
		uploaddata = new SoSoUploadData(context, "BuildSelect.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse2(districtid);
		params.clear();
		params = null;
	}
}
