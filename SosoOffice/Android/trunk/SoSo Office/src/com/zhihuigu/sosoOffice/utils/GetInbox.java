package com.zhihuigu.sosoOffice.utils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoLetterInInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;

public class GetInbox {

	/***
	 * author by Ring 
	 * 请求服务器收件箱的详情
	 */
	
	private SoSoUploadData uploaddata;
	private String reponse = "";// 从服务器获取响应值
	
	public boolean getMyInbox(Activity context){
		if(MyApplication.getInstance(context).getSosouserinfo()!=null
				&&MyApplication.getInstance(context).getSosouserinfo().getUserID()!=null){
			
			if(DBHelper
					.getInstance(context)
					.selectRow(
							"select * from soso_configurationinfo where name='TLETTERS_TIME' and updatedate = value and value<>'' and userid = '"
					+MyApplication.getInstance(context).getSosouserinfo(context).getUserID()+"'",
							null).size()>0){
				return true;
			}
			String updatedate = "";
			List<Map<String, Object>> selectresult = null;
			selectresult = DBHelper
					.getInstance(context)
					.selectRow(
							"select updatedate from soso_configurationinfo where name='TLETTERS_TIME' and userid = '"
					+MyApplication.getInstance(context).getSosouserinfo(context).getUserID()+"'",
							null);
			if (selectresult != null && selectresult.size() > 0) {
				if (selectresult.get(selectresult.size() - 1) != null
						&& selectresult.get(selectresult.size() - 1).get("updatedate") != null) {
					updatedate = (selectresult.get(selectresult.size() - 1).get(
							"updatedate").toString());
				}
			}
			if (selectresult != null) {
				selectresult.clear();
				selectresult = null;
			}
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
					context).getAPPID()));
			params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
					context).getAPPKEY()));
			params.add(new BasicNameValuePair("UpdateDate", updatedate));
			params.add(new BasicNameValuePair("ReceiveUserID", MyApplication.getInstance(context).getSosouserinfo().getUserID()));
			uploaddata = new SoSoUploadData(context, "LettersInSelect.aspx", params);
			uploaddata.Post();
			reponse = uploaddata.getReponse();
			dealReponse1(context);
			if (StringUtils.CheckReponse(reponse)) {
				return true;
			}
		}
		return false;
	}
	
	/***
	 * author by Ring 将获取的站内信数据保存到本地数据库
	 */
	private void dealReponse1(Activity context) {
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(context).execSql("update soso_configurationinfo set updatedate = value where name='TLETTERS_TIME' and userid = '"
					+MyApplication.getInstance(context).getSosouserinfo().getUserID()+"'");
		}	
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(context).execSql("update soso_configurationinfo set updatedate = value where name='TLETTERS_TIME' and userid = '"
					+MyApplication.getInstance(context).getSosouserinfo().getUserID()+"'");
			Type listType = new TypeToken<LinkedList<SoSoLetterInInfo>>() {
			}.getType();
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			LinkedList<SoSoLetterInInfo> sosoletterinfos = null;
			SoSoLetterInInfo sosoletterinfo = null;
			sosoletterinfos = gson.fromJson(reponse, listType);
			List<Map<String, Object>> selectresult = null;
			if (sosoletterinfos != null && sosoletterinfos.size() > 0) {
				for (Iterator<SoSoLetterInInfo> iterator = sosoletterinfos.iterator(); iterator
						.hasNext();) {
					sosoletterinfo = (SoSoLetterInInfo) iterator.next();
					if (sosoletterinfo.getIsUsed() == 1) {
						DBHelper.getInstance(context).delete("soso_letterininfo",
								"letterid = ?", new String[] { sosoletterinfo.getLetterId()});
						continue;
					}
					values.put("letterid", sosoletterinfo.getLetterId());
					values.put("senduserid", sosoletterinfo.getSendUserId());
					values.put("receiveuserid", sosoletterinfo.getReceiveUserid());
					if(!sosoletterinfo.getSendUserName().equals("")){
						values.put("sendusername", sosoletterinfo.getSendUserName());
					}
					if(!sosoletterinfo.getReceiveUserName().equals("")){
						values.put("receiveusername", sosoletterinfo.getReceiveUserName());
					}else{
						values.put("receiveusername", MyApplication.getInstance(context).getSosouserinfo(context).getUserName());
					}
					try {
						values.put("senddate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sosoletterinfo.getSendDate())));
					} catch (ParseException e) {
						values.put("senddate", sosoletterinfo.getSendDate());
					}
					values.put("letterstate", sosoletterinfo.getLetterState());
					values.put("whouserid", sosoletterinfo.getWhoUserID());
					values.put("title", sosoletterinfo.getTitle());
					values.put("content", sosoletterinfo.getContent());
					selectresult = DBHelper.getInstance(context).selectRow(
							"select * from soso_letterininfo where letterid = '"
									+ sosoletterinfo.getLetterId() + "'", null);
					if (selectresult != null) {
						if (selectresult.size() <= 0) {
							DBHelper.getInstance(context).insert("soso_letterininfo",
									values);
						} else {
							DBHelper.getInstance(context).update("soso_letterininfo",
									values, "letterid = ?",
									new String[] { sosoletterinfo.getLetterId()});
						}
						selectresult.clear();
						selectresult = null;
					}
					values.clear();
				}
			}
			if (values != null) {
				values.clear();
				values = null;
			}
			if (sosoletterinfos != null) {
				sosoletterinfos.clear();
				sosoletterinfos = null;
			}
			DBHelper.getInstance(context).close();
		}
	}
	
	/***
	 * 取得服务器获取的响应值
	 */
	public String getReponse() {
		return reponse;
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
