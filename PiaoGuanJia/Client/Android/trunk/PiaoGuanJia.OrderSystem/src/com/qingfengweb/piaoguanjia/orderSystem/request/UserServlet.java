package com.qingfengweb.piaoguanjia.orderSystem.request;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.qingfengweb.piaoguanjia.orderSystem.MyApplication;
import com.qingfengweb.piaoguanjia.orderSystem.model.UserInfo;



public class UserServlet extends SimpleServlet {
	public static final String SUFFIXINTERFACE_USER = "/interface/order/user";// 用户接口
	// ---------------------------------------------------------------------
	public static final String ACTION_LOGIN = "login";// 创建登录接口
	public static final String ACTION_UPDATEPASSWORD = "updatePassword";// 修改密码
	public static final String ACTION_USER = "user";// 获取个人信息
	public static final String ACTION_UPDATEUSER = "updateUser";// 修改个人信息

	/**
	 * 用户登录接口
	 * 
	 */
	public static String actionLogin() {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_LOGIN);
		UploadData uploaddata = new UploadData(SERVER_ADDRESS+SUFFIXINTERFACE_USER, params);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		if (params != null) {
			params.clear();
		}
		return msgString;
	}
	
	/**
	 * 修改密码
	 * 
	 */
	public static String actionUpdatepassword(String oldPassword,String newPassword) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_UPDATEPASSWORD);
		params.add(new BasicNameValuePair("oldPassword", oldPassword));
		params.add(new BasicNameValuePair("newPassword", newPassword));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS+SUFFIXINTERFACE_USER, params);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		if (params != null) {
			params.clear();
		}
		return msgString;
	}
	
	/**
	 * 获取个人信息
	 * 
	 */
	public static String actionUser() {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_USER);
		UploadData uploaddata = new UploadData(SERVER_ADDRESS+SUFFIXINTERFACE_USER, params);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		if (params != null) {
			params.clear();
		}
		return msgString;
	}
	
	/**
	 * 更新个人信息
	 * 
	 */
	public static String actionUpdateuser(UserInfo userinfo) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_UPDATEUSER);
		params.add(new BasicNameValuePair("enterpriseName", userinfo.getEnterpriseName()));
		params.add(new BasicNameValuePair("departmentName", userinfo.getDepartmentName()));
		params.add(new BasicNameValuePair("position", userinfo.getPosition()));
		params.add(new BasicNameValuePair("name", userinfo.getName()));
		params.add(new BasicNameValuePair("phoneNumber", userinfo.getPhoneNumber()));
		params.add(new BasicNameValuePair("cdiNumber", userinfo.getCdiNumber()));
		params.add(new BasicNameValuePair("email", userinfo.getEmail()));
		params.add(new BasicNameValuePair("qq", userinfo.getQq()));
		params.add(new BasicNameValuePair("msn", userinfo.getMsn()));
		params.add(new BasicNameValuePair("alipay", userinfo.getAlipay()));
		params.add(new BasicNameValuePair("accountName", userinfo.getAccountName()));
		params.add(new BasicNameValuePair("accountNumber", userinfo.getAccountNumber()));
		params.add(new BasicNameValuePair("bankName", userinfo.getBankName()));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS+SUFFIXINTERFACE_USER, params);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		if (params != null) {
			params.clear();
		}
		return msgString;
	}
	

}
