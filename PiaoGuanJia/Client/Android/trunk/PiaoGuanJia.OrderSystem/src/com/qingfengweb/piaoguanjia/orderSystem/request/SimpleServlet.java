package com.qingfengweb.piaoguanjia.orderSystem.request;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.lidroid.xutils.http.RequestParams;
import com.qingfengweb.piaoguanjia.orderSystem.MyApplication;
import com.qingfengweb.piaoguanjia.orderSystem.util.MD5;

public class SimpleServlet {
	public static final String SERVER_ADDRESS = "http://192.168.1.107:7001/piaoguanjia";
	// 内网服务器总接口地址
	// public static final String SERVER_ADDRESS =
	// "http://192.168.1.107:7001/piaoguanjia/interface/charge";
	// 内网服务器总接口地址

	public static final String PIAOGUANJIAAPP = "PiaoGuanJiaAPP";
	private final static String[] str = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "q", "w", "e", "r", "t", "y", "u", "i", "o", "p",
			"a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v",
			"b", "n", "m" };

	/**
	 * 创建上传的参数集合
	 * 
	 * @param action
	 * @return
	 */
	public static List<NameValuePair> getParams(String action) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String random = getRandomStr();
		params.add(new BasicNameValuePair("username",MyApplication.username));
		params.add(new BasicNameValuePair("password", MyApplication.password));
		params.add(new BasicNameValuePair("action", action));
		params.add(new BasicNameValuePair("random", random));
		params.add(new BasicNameValuePair("sign", MD5.getMD5(PIAOGUANJIAAPP
				+ random + action)));
		return params;
	}

	/***
	 * 随机6位数字加字幕的组合
	 * 
	 * @return
	 */
	public static String getRandomStr() {
		String s = "";
		for (int i = 0; i < 6; i++) {
			int a = (int) (Math.random() * 36);
			s += str[a];
		}
		return s;
	}

}
