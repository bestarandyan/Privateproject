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

public class PerfectInfo {

	private Context context;
	private String reponse = "";
	private SoSoUploadFile uploadfile;

	/****
	 * author by Ring 完善用户资料 传入完善的字段
	 */

	public PerfectInfo(Context context) {
		this.context = context;
	}

	public boolean perfectUserInfo(String headimagepath,
			String realname, String sex, String birthday, String region,
			String email, String telephone, String roleid) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> filesmap = new HashMap<String, File>();
		params.put("appid", MyApplication.getInstance(context).getAPPID());
		params.put("appkey", MyApplication.getInstance(context).getAPPKEY());
		params.put("UserID", MyApplication.getInstance(context)
				.getSosouserinfo().getUserID());
		params.put("username", MyApplication.getInstance(context)
				.getSosouserinfo().getUserName());
		params.put("RealName", realname);
		String headimage = "";
		if (headimagepath != null && !headimagepath.equals("")) {
			File file = new File(headimagepath);
			if (file.exists() && file.isFile()) {
				headimage = file.getName();
				filesmap.put(file.getName(), file);
			}
		}
		params.put("file", headimage);
		if (sex.equals("")) {
			params.put("Sex", MyApplication.getInstance(context)
					.getSosouserinfo().getSex()
					+ "");
		} else {
			params.put("Sex", sex);
		}
		if (birthday.equals("")) {
			params.put("Birthday", MyApplication.getInstance(context).getSosouserinfo().getBirthday());
		} else {
			params.put("Birthday", birthday);
		}
		if (region.equals("")) {
			params.put("Region", MyApplication.getInstance(context).getSosouserinfo().getRegion());
		} else {
			params.put("Region", region);
		}
		if (email.equals("")) {
			params.put("Email", MyApplication.getInstance(context).getSosouserinfo().getEmail());
		} else {
			params.put("Email", email);
		}
		if (telephone.equals("")) {
			params.put("Telephone", MyApplication.getInstance(context).getSosouserinfo().getTelephone());
		} else {
			params.put("Telephone", telephone);
		}
		if (roleid.equals("")) {
			params.put("roleid", MyApplication.getInstance(context).getSosouserinfo().getRoleID()+"");
		} else {
			params.put("roleid", roleid);
		}
//		params.put("ZJMC", "");
//		params.put("ZJAddress", "");
//		params.put("ZJTele", "");
//		params.put("file1", "");
		System.out.println("请求服务器地址:"+MyApplication.getInstance(context).getURL()
					+ "UpdateUser.aspx");
		System.out.println("请求服务器的参数为："+params);
		try {
			uploadfile = new SoSoUploadFile();
			uploadfile.post(MyApplication.getInstance(context).getURL()
					+ "UpdateUser.aspx", params, filesmap);
			reponse = uploadfile.getReponse();
		} catch (IOException e) {
			reponse = "初始值";
			e.printStackTrace();
		}
		System.out.println("服务器的返回值为：" + reponse);
		dealReponse(headimagepath, realname, sex, birthday,
				region, email, telephone, roleid);
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

	public void dealReponse(String headimagepath,
			String realname, String sex, String birthday, String region,
			String email, String telephone, String roleid) {
		if (StringUtils.CheckReponse(reponse)
				&& MyApplication.getInstance(context).getSosouserinfo() != null
				&& MyApplication.getInstance(context).getSosouserinfo()
						.getUserID() != null) {
			ContentValues values = new ContentValues();
			values.put("soso_realname", realname);
			if (headimagepath != null && !headimagepath.equals("")) {
				values.put("soso_userimagesd", headimagepath);
			}
			if(!sex.equals("")){
				values.put("soso_sex", sex);
			}
			if(!birthday.equals("")){
				values.put("soso_birthday", birthday);
			}
			if(!region.equals("")){
				values.put("soso_region", region);
			}
			if(!email.equals("")){
				values.put("soso_email", email);
			}
			if(!telephone.equals("")){
				values.put("soso_telephone", telephone);
			}
			DBHelper.getInstance(context).update(
					"soso_userinfo",
					values,
					"soso_userid = ?",
					new String[] { MyApplication.getInstance(context)
							.getSosouserinfo().getUserID() });
			MyApplication.getInstance(context).getSosouserinfo().updateUserInfo(context, MyApplication.getInstance(context).getSosouserinfo().getUserID());
		}
	}

	public void overReponse() {
		if(uploadfile!=null){
			uploadfile.overReponse();
		}
	}

}
