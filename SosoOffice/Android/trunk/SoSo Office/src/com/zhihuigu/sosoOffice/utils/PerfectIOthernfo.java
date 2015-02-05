package com.zhihuigu.sosoOffice.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;

import com.zhihuigu.sosoOffice.RegisterThirdActivity;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.SoSoUploadFile;

public class PerfectIOthernfo {

	private Context context;
	private String reponse = "";
	private SoSoUploadFile uploadfile;

	/****
	 * author by Ring 完善用户资料 传入完善的字段
	 */

	public PerfectIOthernfo(Context context) {
		this.context = context;
	}

	public boolean perfectUserInfo(String jobimagepath, String zjmc,
			String zjaddress, String zjtele,String name) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> filesmap = new HashMap<String, File>();
		params.put("appid", MyApplication.getInstance(context).getAPPID());
		params.put("appkey", MyApplication.getInstance(context).getAPPKEY());
		params.put("username", MyApplication.getInstance(context)
				.getSosouserinfo().getUserName());
		params.put("UserID", MyApplication.getInstance(context)
				.getSosouserinfo().getUserID());
		if (zjmc.equals("")) {
			params.put("ZJMC", MyApplication.getInstance(context).getSosouserinfo().getZJMC());
		} else {
			params.put("ZJMC", zjmc);
		}

		if (zjaddress.equals("")) {
			params.put("ZJAddress", MyApplication.getInstance(context).getSosouserinfo().getZJAddress());
		} else {
			params.put("ZJAddress", zjaddress);
		}
		if (zjtele.equals("")) {
			params.put("ZJTele", MyApplication.getInstance(context).getSosouserinfo().getZJTele());
		} else {
			params.put("ZJTele", zjtele);
		}
		if (zjtele.equals("")) {
			params.put("RealName", MyApplication.getInstance(context).getSosouserinfo().getRealName());
		} else {
			params.put("RealName", name);
		}
		String jobsimage = "";
		if (jobimagepath != null && !jobimagepath.equals("")) {
			File file = new File(jobimagepath);
			if (file.exists() && file.isFile()) {
				jobsimage = file.getName();
				filesmap.put(file.getName(), file);
			}
		}
		params.put("file1", jobsimage);
		System.out.println("请求服务器地址:"+MyApplication.getInstance(context).getURL()
				+ "UpdateZJUser.aspx");
		System.out.println("请求服务器的参数为："+params);
		try {
			uploadfile = new SoSoUploadFile();
			uploadfile.post(MyApplication.getInstance(context).getURL()
					+ "UpdateZJUser.aspx", params, filesmap);
			reponse = uploadfile.getReponse();
		} catch (IOException e) {
			reponse = "初始值";
			e.printStackTrace();
		}
		System.out.println("服务器的返回值为：" + reponse);
		dealReponse(jobimagepath, zjmc, zjaddress, zjtele,name);
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}

	/***
	 * author by Ring 当用户信息修改成功后将相关数据保存到本地数据库
	 * 
	 */

	public void dealReponse(String jobimagepath, String zjmc,
			String zjaddress, String zjtele,String name) {
		if (StringUtils.CheckReponse(reponse)
				&& MyApplication.getInstance(context).getSosouserinfo() != null
				&& MyApplication.getInstance(context).getSosouserinfo()
						.getUserID() != null) {
			ContentValues values = new ContentValues();
			if(!zjtele.equals("")){
				values.put("soso_ownerphone", zjtele);
			}
			if(!zjmc.equals("")){
				values.put("soso_legalperson", zjmc);
			}
			if(!zjaddress.equals("")){
				values.put("soso_owneraddress", zjaddress);
			}
			if (jobimagepath != null && !jobimagepath.equals("")) {
				values.put("soso_businesslicensesd", jobimagepath);
			}
			if(name != null && !name.equals("")){
				values.put("soso_realname", name);
			}
			MyApplication.getInstance(context).getSosouserinfo().setZJMC(zjmc);
			MyApplication.getInstance(context).getSosouserinfo().setZJAddress(zjaddress);
			MyApplication.getInstance(context).getSosouserinfo().setZJTele(zjtele);
			MyApplication.getInstance(context).getSosouserinfo().setRealName(name);
			
			if(values.size()>0){
				DBHelper.getInstance(context).update(
						"soso_userinfo",
						values,
						"soso_userid = ?",
						new String[] { MyApplication.getInstance(context)
								.getSosouserinfo().getUserID() });
			}
		}
	}

	public void overReponse() {
		if(uploadfile!=null){
			uploadfile.overReponse();
		}
	}

}
