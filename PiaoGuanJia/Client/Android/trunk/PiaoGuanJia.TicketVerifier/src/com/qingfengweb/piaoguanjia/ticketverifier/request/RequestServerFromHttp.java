package com.qingfengweb.piaoguanjia.ticketverifier.request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.qingfengweb.piaoguanjia.ticketverifier.TicketApplication;
import com.qingfengweb.piaoguanjia.ticketverifier.util.MD5;

public class RequestServerFromHttp {
	 public static final String SERVER_ADDRESS ="http://211.144.85.82:7001/piaoguanjia3/interface/validate";
	// 内网服务器总接口地址
//	public static final String SERVER_ADDRESS = "http://192.168.1.107:7001/piaoguanjia/interface/validate";
	// 内网服务器总接口地址

	/**
	 * -----------------------以下是每个接口的action值----------------------------------
	 * 
	 **/

	public static final String ACTION_LOGIN = "login";// 创建登录接口
	public static final String ACTION_ORDERNUMBER = "orderNumber";// 根据手机号码，身份证号码，订单号码，券码获取订单列表
	public static final String ACTION_VALIDATE = "validate";// 验证券码
	public static final String ACTION_LIST = "list";// 获取验证列表
	public static final String ACTION_ORDER = "order";// 获取订单详情
	public static final String ACTION_QUIT = "quit";// 退出软件
	// ---------------------------------------------------------------------
	public static final String SUFFIXINTERFACE_USER = "/interface/validate";// 充值接口
	public static HashMap<String, UploadData> requestmap = new HashMap<String, UploadData>();
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
		params.add(new BasicNameValuePair("username",
				TicketApplication.username));
		params.add(new BasicNameValuePair("password", TicketApplication
				.getPassword()));
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

	/**
	 * 用户登录接口
	 * 
	 */
	public static String loginUser() {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_LOGIN);
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_LOGIN, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_LOGIN);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 验证券码
	 * 
	 */
	public static String validate(String orderNumbers) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_VALIDATE);
		params.add(new BasicNameValuePair("orderNumbers", orderNumbers));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_VALIDATE, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_VALIDATE);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 根据手机号码，身份证号码，订单号码，券码获取订单列表
	 * 
	 */
	public static String orderNumber(String orderNumber) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_ORDERNUMBER);
		params.add(new BasicNameValuePair("orderNumber", orderNumber));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_ORDERNUMBER, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_ORDERNUMBER);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 获取验证列表
	 * 
	 */
	public static String list(int limit, String updatetime, int listType,
			int isUporDown) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_LIST);
		params.add(new BasicNameValuePair("limit", limit + ""));
		params.add(new BasicNameValuePair("lastUpdateTime", updatetime));
		params.add(new BasicNameValuePair("listType", listType + ""));
		params.add(new BasicNameValuePair("isUpOrDown", isUporDown + ""));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_LIST, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_LIST);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 获取订单详情
	 * 
	 */
	public static String order(String orderid) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_ORDER);
		params.add(new BasicNameValuePair("orderid", orderid));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_ORDER, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_ORDER);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}
	
	/**
	 * 退出
	 * 
	 */
	public static String quit(String quitPassword) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_QUIT);
		params.add(new BasicNameValuePair("quitPassword", TicketApplication.sha1(quitPassword)));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_QUIT, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_QUIT);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}
}
