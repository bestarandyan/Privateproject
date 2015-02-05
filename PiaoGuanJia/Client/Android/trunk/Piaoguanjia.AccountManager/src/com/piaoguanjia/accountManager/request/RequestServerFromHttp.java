package com.piaoguanjia.accountManager.request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.piaoguanjia.accountManager.AccountApplication;
import com.piaoguanjia.accountManager.util.MD5;

public class RequestServerFromHttp {
	 public static final String SERVER_ADDRESS ="http://www.piaoguanjia.com/interface/charge";
	// 内网服务器总接口地址
//	public static final String SERVER_ADDRESS = "http://192.168.1.107:7001/piaoguanjia/interface/charge";
	// 内网服务器总接口地址

	/**
	 * -----------------------以下是每个接口的action值----------------------------------
	 * 
	 **/

	public static final String ACTION_LOGIN = "login";// 创建登录接口
	public static final String ACTION_ADDCHARGE = "addCharge";// 创建添加充值接口
	public static final String ACTION_LISTCHARGE = "listCharge";// 创建获取充值列表接口
	public static final String ACTION_CHARGE = "charge";// 创建获取充值详情接口
	public static final String ACTION_CERTIFICATE = "certificate";// 获取充值凭证接口
	public static final String ACTION_AUDITCHARGE = "auditCharge";// 创建获取审核充值接口

	public static final String ACTION_ADDACCOUNT = "addAccount";// 创建专用账户添加接口
	public static final String ACTION_LISTACCOUNT = "listAccount";// 创建获取专用账户列表接口
	public static final String ACTION_ACCOUNT = "account";// 创建获取专用账户详情接口
	public static final String ACTION_AUDITACCOUNT = "auditAccount";// 创建审核专用账户接口

	public static final String ACTION_LISTCREDIT = "listCredit";// 创建获取额度列表接口
	public static final String ACTION_ADDCREDIT = "addCredit";// 创建添加额度接口
	public static final String ACTION_AUDITCREDIT = "auditCredit";// 创建审核额度接口
	public static final String ACTION_PENDINGCOUNT = "pendingCount";// 审核数量
	public static final String ACTION_PRODUCTNAME = "productName";// 产品名称
	
	// ---------------------------------------------------------------------
	public static final String SUFFIXINTERFACE_USER = "/interface/charge";// 充值接口
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
				AccountApplication.username));
		params.add(new BasicNameValuePair("password", AccountApplication
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
	 * 充值接口，添加充值
	 * 
	 * @param isReceipt
	 * @param balanceSource
	 * @param warrantor
	 * @param remark
	 * @param isCertificate
	 * @param productid
	 * @param accountType
	 * @param amount
	 * @param chargeUsername
	 * 
	 */
	public static String addCharge(String chargeUsername, Double amount,
			int accountType, String productid, int isCertificate,
			String remark, String warrantor, File file, int balanceSource,
			int isReceipt) {
		String msgString = "";

		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> filesmap = new HashMap<String, File>();
		if (isCertificate == 1) {
			if (file.exists() && file.isFile()) {
				filesmap.put(file.getName(), file);
			}
		}
		String random = getRandomStr();
		params.put("username", AccountApplication.username);
		params.put("password",
				AccountApplication.sha1(AccountApplication.password));
		params.put("action", ACTION_ADDCHARGE);
		params.put("random", random);
		params.put("sign",
				MD5.getMD5(PIAOGUANJIAAPP + random + ACTION_ADDCHARGE));
		params.put("chargeUsername", chargeUsername);
		params.put("amount", amount + "");
		params.put("accountType", accountType + "");
		params.put("productid", productid);
		params.put("isCertificate", isCertificate + "");
		params.put("remark", remark);
		params.put("warrantor", warrantor);
		params.put("balanceSource", balanceSource + "");
		params.put("isReceipt", isReceipt + "");
		// params.put("file", file.getName());
		UploadFile uploaddata = new UploadFile();
		try {
			uploaddata.post(SERVER_ADDRESS, params, filesmap);
			msgString = uploaddata.getResponse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 充值接口，充值列表
	 * 
	 */
	public static String listCharge(int limit, String updatetime, int listType,
			int isUporDown) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_LISTCHARGE);
		params.add(new BasicNameValuePair("limit", limit + ""));
		params.add(new BasicNameValuePair("lastUpdateTime", updatetime));
		params.add(new BasicNameValuePair("listType", listType + ""));
		params.add(new BasicNameValuePair("isUpOrDown", isUporDown + ""));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_LISTCHARGE, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_LISTCHARGE);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 充值接口，充值详情
	 * 
	 */
	public static String charge(String chargid) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_CHARGE);
		params.add(new BasicNameValuePair("chargeid", chargid));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_CHARGE, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_CHARGE);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	// /**
	// * 充值接口，获取充值凭证
	// *
	// */
	// public static String certificate() {
	// String msgString = "";
	// List<NameValuePair> params = getParams(ACTION_CERTIFICATE);
	// UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
	// requestmap.put(ACTION_CERTIFICATE, uploaddata);
	// uploaddata.Post();
	// msgString = uploaddata.getReponse();
	// requestmap.remove(ACTION_CERTIFICATE);
	// if (params != null) {
	// params.clear();
	// }
	// return msgString;
	// }

	/**
	 * 充值接口，审核充值
	 * 
	 */
	public static String auditCharge(String id, int status, String reason) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_AUDITCHARGE);
		params.add(new BasicNameValuePair("chargeid", id));
		params.add(new BasicNameValuePair("status", status + ""));// 1 审核通过，2
																	// 审核不通
		params.add(new BasicNameValuePair("reason", reason));// 仅当（status=2）时，不能为空
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_AUDITCHARGE, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_AUDITCHARGE);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 充值接口，添加专用账户
	 * 
	 */
	public static String addAccount(String username, String productid,
			String chargelimit, String remindamount, int onlineChargeLimit,
			String remindType) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_ADDACCOUNT);
		params.add(new BasicNameValuePair("accountUsername", username));
		params.add(new BasicNameValuePair("productid", productid));
		params.add(new BasicNameValuePair("chargeLimit", chargelimit));
		params.add(new BasicNameValuePair("remindAmount", remindamount));
		params.add(new BasicNameValuePair("remindType", remindType));
		if (onlineChargeLimit > 0) {
			params.add(new BasicNameValuePair("onlineChargeLimit",
					onlineChargeLimit + ""));
		}
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_ADDACCOUNT, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_ADDACCOUNT);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 充值接口，获取专用账户的审核，历史记录列表
	 * 
	 */
	public static String listAccount(int limit, String updatetime,
			int listType, int isUporDown) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_LISTACCOUNT);
		params.add(new BasicNameValuePair("limit", limit + ""));
		params.add(new BasicNameValuePair("lastUpdateTime", updatetime));
		params.add(new BasicNameValuePair("listType", listType + ""));
		params.add(new BasicNameValuePair("isUpOrDown", isUporDown + ""));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_LISTACCOUNT, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_LISTACCOUNT);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 充值接口， 专用账户详情
	 * 
	 */
	public static String account(String accountid) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_ACCOUNT);
		params.add(new BasicNameValuePair("accountid", accountid));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_ACCOUNT, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_ACCOUNT);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 审核专用账户
	 * 
	 */
	public static String auditAccount(String id, int status, String reason) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_AUDITACCOUNT);
		params.add(new BasicNameValuePair("accountid", id));
		params.add(new BasicNameValuePair("status", status + ""));
		params.add(new BasicNameValuePair("reason", reason));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_ACCOUNT, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_ACCOUNT);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 添加额度
	 * 
	 */
	public static String addCredit(String username, int accountType,
			int creditLimit, String reason, String productid) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_ADDCREDIT);
		params.add(new BasicNameValuePair("accountType", accountType+""));
		params.add(new BasicNameValuePair("creditUsername", username));
		params.add(new BasicNameValuePair("creditLimit", creditLimit+""));
		params.add(new BasicNameValuePair("reason", reason));
		if(accountType==2){
			params.add(new BasicNameValuePair("productid", productid));
		}
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_ADDCREDIT, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_ADDCREDIT);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 额度列表
	 * 
	 */
	public static String listCredit(int limit, String updatetime, int listType,
			int isUporDown) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_LISTCREDIT);
		params.add(new BasicNameValuePair("limit", limit + ""));
		params.add(new BasicNameValuePair("lastUpdateTime", updatetime));
		params.add(new BasicNameValuePair("listType", listType + ""));
		params.add(new BasicNameValuePair("isUpOrDown", isUporDown + ""));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_LISTCREDIT, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_LISTCREDIT);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 审核额度
	 * 
	 */
	public static String auditCredit(String id, int status, String reason) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_AUDITCREDIT);
		params.add(new BasicNameValuePair("creditid", id));
		params.add(new BasicNameValuePair("status", status + ""));
		params.add(new BasicNameValuePair("reason", reason));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_AUDITCREDIT, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_AUDITCREDIT);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 下载图片
	 */
	public static boolean downloadPhoto(File file, String imageid) {
		List<NameValuePair> params = getParams(ACTION_CERTIFICATE);
		params.add(new BasicNameValuePair("chargeid", imageid));
		DownloadFile downloaddata = new DownloadFile(SERVER_ADDRESS, params,
				file);
		downloaddata.Post();
		String reponse = downloaddata.getReponse();
		if (reponse.equals("0")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 数量
	 * 
	 */
	public static String pendingCount () {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_PENDINGCOUNT);
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_PENDINGCOUNT, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_PENDINGCOUNT);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}
	
	/**
	 * 产品名称
	 * 
	 */
	public static String productname(String productid) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_PRODUCTNAME);
		params.add(new BasicNameValuePair("productid", productid));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS, params);
		requestmap.put(ACTION_PRODUCTNAME, uploaddata);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		requestmap.remove(ACTION_PRODUCTNAME);
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

}
