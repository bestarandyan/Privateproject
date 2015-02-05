package com.piaoguanjia.accountManager.request;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piaoguanjia.accountManager.AccountApplication;
import com.piaoguanjia.accountManager.bean.AccountInfo;
import com.piaoguanjia.accountManager.bean.ChargeInfo;
import com.piaoguanjia.accountManager.bean.CreditInfo;
import com.piaoguanjia.accountManager.bean.UserInfo;
import com.piaoguanjia.accountManager.database.DBHelper;
import com.piaoguanjia.accountManager.database.TableCreate;
import com.sqlcrypt.database.ContentValues;

/**
 * 解析服务器返回的数据
 */
public class HandleData {

	private static ContentValues values = new ContentValues();

	/**
	 * 登陆接口
	 * 
	 * @param response
	 */
	public static void handleLogin(String response) {
		values.clear();
		Gson gson = new Gson();// 创建Gson对象
		UserInfo bean = null;
		List<Map<String, Object>> selectresult = null;
		try {
			bean = gson.fromJson(response, UserInfo.class);// 解析json对象
		} catch (Exception e) {
		}
		if (bean == null) {
			return;
		}
		selectresult = DBHelper.getInstance().selectRow(
				"select * from " + TableCreate.TABLENAME_USERINFO
						+ " where userid = '" + bean.getUserid() + "'", null);
		values.put("username", AccountApplication.username);
		values.put("password", AccountApplication.password);
		values.put("userid", bean.getUserid());
		values.put("permissions", bean.getPermissions());
		values.put("iskeep", 1);
		AccountApplication.userid = bean.getUserid();
		AccountApplication.permissions = Integer
				.parseInt(bean.getPermissions());
		DBHelper.getInstance().execSql(
				"update " + TableCreate.TABLENAME_USERINFO + " set iskeep = 2");
		if (selectresult != null && selectresult.size() > 0) {
			DBHelper.getInstance().update(TableCreate.TABLENAME_USERINFO,
					values, "userid=?", new String[] { bean.getUserid() });
		} else {
			DBHelper.getInstance().insert(TableCreate.TABLENAME_USERINFO,
					values);
		}
	}

	/**
	 * 充值列表
	 */

	public static boolean handleChargeList(String response,int type,int type1) {
		String liststr = "";
		int num = 0;// 待审核数量
		try {
			JSONObject jo = new JSONObject(response);
			liststr = jo.get("list").toString();
			num = jo.getInt("pendingCount");
			List<Map<String, Object>> selectresult = DBHelper.getInstance()
					.selectRow(
							"select * from "
									+ TableCreate.TABLENAME_APPUSERINFO
									+ " where userid = '"
									+ AccountApplication.userid + "'", null);
			values.clear();
			values.put("userid", AccountApplication.userid);
			values.put("chargenum", num);
			if (selectresult != null && selectresult.size() > 0) {
				DBHelper.getInstance().update(
						TableCreate.TABLENAME_APPUSERINFO, values, "userid=?",
						new String[] { AccountApplication.userid });
			} else {
				DBHelper.getInstance().insert(
						TableCreate.TABLENAME_APPUSERINFO, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (!liststr.startsWith("["))
			return false;
		Type listType = new TypeToken<LinkedList<ChargeInfo>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<ChargeInfo> beans = null;
		ChargeInfo bean = null;
		beans = gson.fromJson(liststr, listType);
		if (beans == null) {
			return false;
		}
		List<Map<String, Object>> selectresult = null;
		if (beans != null && beans.size() > 0) {
			if(type==2){
				String status = "";
				if(type1!=-1){
					status = (type1 == 0 ? " and status = 0" : " and status > 0");
				}
				DBHelper.getInstance().execSql("delete from "+TableCreate.TABLENAME_CHARGEINFO+ " where userid = '" + AccountApplication.userid + "' "
						+ status);
			}
			for (Iterator<ChargeInfo> iterator = beans.iterator(); iterator
					.hasNext();) {
				bean = iterator.next();
				if (bean != null) {
					selectresult = DBHelper.getInstance().selectRow(
							"select * from " + TableCreate.TABLENAME_CHARGEINFO
									+ " where chargid = '" + bean.getChargeid()
									+ "'", null);
					values.clear();
					values.put("userid", AccountApplication.userid);
					values.put("chargid", bean.getChargeid());
					values.put("username", bean.getUsername());
					values.put("accounttype", bean.getAccountType());
					values.put("type", bean.getType());
					values.put("createtime", bean.getCreateTime());
					values.put("productname", bean.getProductName());
					values.put("iscertificate", bean.getIsCertificate());
					values.put("audittime", bean.getAuditTime());
					values.put("status", bean.getStatus());
					values.put("amount", bean.getAmount());
					if (selectresult != null && selectresult.size() > 0) {
						DBHelper.getInstance().update(
								TableCreate.TABLENAME_CHARGEINFO, values,
								"chargid=?",
								new String[] { bean.getChargeid() });
					} else {
						DBHelper.getInstance().insert(
								TableCreate.TABLENAME_CHARGEINFO, values);
					}
				}
			}
		}
		return true;
	}

	/**
	 * 充值详情
	 * 
	 * @param response
	 */
	public static void handleChargeInfo(String response) {
		values.clear();
		Gson gson = new Gson();// 创建Gson对象
		ChargeInfo bean = null;
		List<Map<String, Object>> selectresult = null;
		try {
			bean = gson.fromJson(response, ChargeInfo.class);// 解析json对象
		} catch (Exception e) {
		}
		if (bean == null) {
			return;
		}
		selectresult = DBHelper.getInstance()
				.selectRow(
						"select * from " + TableCreate.TABLENAME_CHARGEINFO
								+ " where chargid = '" + bean.getChargeid()
								+ "'", null);
		values.clear();
		values.put("userid", AccountApplication.userid);
		values.put("chargid", bean.getChargeid());
		values.put("source", bean.getSource());
		values.put("username", bean.getUsername());
		values.put("type", bean.getType());
		values.put("accounttype", bean.getAccountType());
		values.put("audittime", bean.getAuditTime());
		values.put("productid", bean.getProductid());
		values.put("productname", bean.getProductName());
		values.put("status", bean.getStatus());
		values.put("amount", bean.getAmount());
		values.put("totalAmount", bean.getTotalAmount());
		values.put("balance", bean.getBalance());
		values.put("balanceSource", bean.getBalanceSource());
		values.put("isReceipt", bean.getIsReceipt());
		values.put("remark", bean.getRemark());
		values.put("warrantor", bean.getWarrantor());
		values.put("isCertificate", bean.getIsCertificate());
		if (selectresult != null && selectresult.size() > 0) {
			DBHelper.getInstance().update(TableCreate.TABLENAME_CHARGEINFO,
					values, "chargid=?", new String[] { bean.getChargeid() });
		} else {
			DBHelper.getInstance().insert(TableCreate.TABLENAME_CHARGEINFO,
					values);
		}
	}

	/**
	 * 更新充值状态
	 * 
	 * @param msg
	 * @param context
	 */
	public static void handleChargeStatus(String chargid, int status,
			String reason) {
		values.clear();
		values.put("status", status);
		values.put("reason", reason);
		DBHelper.getInstance().update(TableCreate.TABLENAME_CHARGEINFO, values,
				"chargid=?", new String[] { chargid });
	}

	/**
	 * 专用账户列表
	 */

	public static boolean handleAccountList(String response,int type,int type1) {
		String liststr = "";
		int num = 0;// 待审核数量
		try {
			JSONObject jo = new JSONObject(response);
			liststr = jo.get("list").toString();
			num = jo.getInt("pendingCount");
			List<Map<String, Object>> selectresult = DBHelper.getInstance()
					.selectRow(
							"select * from "
									+ TableCreate.TABLENAME_APPUSERINFO
									+ " where userid = '"
									+ AccountApplication.userid + "'", null);
			values.clear();
			values.put("userid", AccountApplication.userid);
			values.put("accountnum", num);
			if (selectresult != null && selectresult.size() > 0) {
				DBHelper.getInstance().update(
						TableCreate.TABLENAME_APPUSERINFO, values, "userid=?",
						new String[] { AccountApplication.userid });
			} else {
				DBHelper.getInstance().insert(
						TableCreate.TABLENAME_APPUSERINFO, values);
			}
		} catch (Exception e) {
			return false;
		}
		if (!liststr.startsWith("["))
			return false;
		Type listType = new TypeToken<LinkedList<AccountInfo>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<AccountInfo> beans = null;
		AccountInfo bean = null;
		beans = gson.fromJson(liststr, listType);
		if (beans == null) {
			return false;
		}
		List<Map<String, Object>> selectresult = null;
		if (beans != null && beans.size() > 0) {
			if(type==2){
				String status = "";
				if(type1!=-1){
					status = (type1 == 0 ? " and status = 0" : " and status > 0");
				}
				DBHelper.getInstance().execSql("delete from "+TableCreate.TABLENAME_ACCOUNTINFO+ " where userid = '" + AccountApplication.userid + "' "
						+ status);
			}
			for (Iterator<AccountInfo> iterator = beans.iterator(); iterator
					.hasNext();) {
				bean = iterator.next();
				if (bean != null) {
					selectresult = DBHelper.getInstance().selectRow(
							"select * from "
									+ TableCreate.TABLENAME_ACCOUNTINFO
									+ " where accountid = '"
									+ bean.getAccountid() + "'", null);
					values.clear();
					values.put("userid", AccountApplication.userid);
					values.put("accountid", bean.getAccountid());
					values.put("username", bean.getUsername());
					values.put("productid", bean.getProductid());
					values.put("productname", bean.getProductName());
					values.put("createtime", bean.getCreateTime());
					values.put("audittime", bean.getAuditTime());
					values.put("status", bean.getStatus());
					if (selectresult != null && selectresult.size() > 0) {
						DBHelper.getInstance().update(
								TableCreate.TABLENAME_ACCOUNTINFO, values,
								"accountid=?",
								new String[] { bean.getAccountid() });
					} else {
						DBHelper.getInstance().insert(
								TableCreate.TABLENAME_ACCOUNTINFO, values);
					}
				}
			}
		}
		return true;
	}

	/**
	 * 专用账户详情
	 * 
	 * @param response
	 */
	public static void handleAccountInfo(String response) {
		values.clear();
		Gson gson = new Gson();// 创建Gson对象
		AccountInfo bean = null;
		List<Map<String, Object>> selectresult = null;
		try {
			bean = gson.fromJson(response, AccountInfo.class);// 解析json对象
		} catch (Exception e) {
		}
		if (bean == null) {
			return;
		}
		selectresult = DBHelper.getInstance().selectRow(
				"select * from " + TableCreate.TABLENAME_ACCOUNTINFO
						+ " where accountid = '" + bean.getAccountid() + "'",
				null);
		values.clear();
		values.put("userid", AccountApplication.userid);
		values.put("accountid", bean.getAccountid());
		values.put("username", bean.getUsername());
		values.put("name", bean.getName());
		values.put("chargelimit", bean.getChargeLimit());
		values.put("remindamount", bean.getRemindAmount());
		values.put("remindtype", bean.getRemindType());
		values.put("onlinechargelimit", bean.getOnlineChargeLimit());
		values.put("status", bean.getStatus());
		if (selectresult != null && selectresult.size() > 0) {
			DBHelper.getInstance()
					.update(TableCreate.TABLENAME_ACCOUNTINFO, values,
							"accountid=?", new String[] { bean.getAccountid() });
		} else {
			DBHelper.getInstance().insert(TableCreate.TABLENAME_ACCOUNTINFO,
					values);
		}
	}

	/**
	 * 更新专用账户状态
	 * 
	 * @param msg
	 * @param context
	 */
	public static void handleAccountStatus(String accountid, int status,
			String reason) {
		values.clear();
		values.put("status", status);
		values.put("reason", reason);
		DBHelper.getInstance().update(TableCreate.TABLENAME_ACCOUNTINFO,
				values, "accountid=?", new String[] { accountid });
	}

	/**
	 * 额度列表
	 */

	public static boolean handleCreditList(String response,int type,int type1) {
		String liststr = "";
		int num = 0;// 待审核数量
		try {
			JSONObject jo = new JSONObject(response);
			liststr = jo.get("list").toString();
			num = jo.getInt("pendingCount");
			List<Map<String, Object>> selectresult = DBHelper.getInstance()
					.selectRow(
							"select * from "
									+ TableCreate.TABLENAME_APPUSERINFO
									+ " where userid = '"
									+ AccountApplication.userid + "'", null);
			values.clear();
			values.put("userid", AccountApplication.userid);
			values.put("creditnum", num);
			if (selectresult != null && selectresult.size() > 0) {
				DBHelper.getInstance().update(
						TableCreate.TABLENAME_APPUSERINFO, values, "userid=?",
						new String[] { AccountApplication.userid });
			} else {
				DBHelper.getInstance().insert(
						TableCreate.TABLENAME_APPUSERINFO, values);
			}
		} catch (Exception e) {
			return false;
		}
		if (!liststr.startsWith("["))
			return false;
		Type listType = new TypeToken<LinkedList<CreditInfo>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<CreditInfo> beans = null;
		CreditInfo bean = null;
		beans = gson.fromJson(liststr, listType);
		if (beans == null) {
			return false;
		}
		List<Map<String, Object>> selectresult = null;
		if (beans != null && beans.size() > 0) {
			if(type==2){
				String status = "";
				if(type1!=-1){
					status = (type1 == 0 ? " and status = 0" : " and status > 0");
				}
				DBHelper.getInstance().execSql("delete from "+TableCreate.TABLENAME_ACCOUNTINFO+ " where userid = '" + AccountApplication.userid + "' "
						+ status);
			}
			for (Iterator<CreditInfo> iterator = beans.iterator(); iterator
					.hasNext();) {
				bean = iterator.next();
				if (bean != null) {
					selectresult = DBHelper.getInstance().selectRow(
							"select * from " + TableCreate.TABLENAME_CREDITINFO
									+ " where creditid = '"
									+ bean.getCreditid() + "'", null);
					values.clear();
					values.put("userid", AccountApplication.userid);
					values.put("creditid", bean.getCreditid());
					values.put("username", bean.getUsername());
					values.put("name", bean.getName());
					values.put("accounttype", bean.getAccountType());
					values.put("createtime", bean.getCreateTime());
					values.put("productname", bean.getProductName());
					values.put("audittime", bean.getAuditTime());
					values.put("status", bean.getStatus());
					values.put("productid", bean.getProductid());
					values.put("creditlimit", bean.getCreditLimit());
					values.put("reason", bean.getReason());
					if (selectresult != null && selectresult.size() > 0) {
						DBHelper.getInstance().update(
								TableCreate.TABLENAME_CREDITINFO, values,
								"creditid=?",
								new String[] { bean.getCreditid() });
					} else {
						DBHelper.getInstance().insert(
								TableCreate.TABLENAME_CREDITINFO, values);
					}
				}
			}
		}
		return true;
	}

	/**
	 * 更新额度状态
	 * 
	 * @param msg
	 * @param context
	 */
	public static void handleCreditStatus(String creditid, int status,
			String reason) {
		values.clear();
		values.put("status", status);
		values.put("reason", reason);
		DBHelper.getInstance().update(TableCreate.TABLENAME_CREDITINFO, values,
				"creditid=?", new String[] { creditid });
	}

	/***
	 * 查询数字
	 */

	public static void selectNum() {
		List<Map<String, Object>> selectresult = DBHelper.getInstance()
				.selectRow(
						"select * from " + TableCreate.TABLENAME_APPUSERINFO
								+ " where userid = '"
								+ AccountApplication.userid + "'", null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(0).get("chargenum") != null) {
				try {
					AccountApplication.chargenum = Integer
							.parseInt(selectresult.get(0).get("chargenum")
									.toString());
				} catch (Exception e) {
				}
			}

			if (selectresult.get(0).get("accountnum") != null) {
				try {
					AccountApplication.accountnum = Integer
							.parseInt(selectresult.get(0).get("accountnum")
									.toString());
				} catch (Exception e) {
				}
			}

			if (selectresult.get(0).get("creditnum") != null) {
				try {
					AccountApplication.creditnum = Integer
							.parseInt(selectresult.get(0).get("creditnum")
									.toString());

				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 数字
	 * 
	 * @param response
	 */
	public static void handlePendingCount(String response) {
		values.clear();
		Gson gson = new Gson();// 创建Gson对象
		PendingInfo bean = null;
		List<Map<String, Object>> selectresult = null;
		try {
			bean = gson.fromJson(response, PendingInfo.class);// 解析json对象
		} catch (Exception e) {
		}
		if (bean == null) {
			return;
		}
		selectresult = DBHelper.getInstance()
				.selectRow(
						"select * from " + TableCreate.TABLENAME_APPUSERINFO
								+ " where userid = '"
								+ AccountApplication.userid + "'", null);
		values.clear();
		values.put("userid", AccountApplication.userid);
		values.put("chargenum", bean.getChargePendingCount());
		values.put("accountnum", bean.getAccountPendingCount());
		values.put("creditnum", bean.getCreditPendingCount());
		if (selectresult != null && selectresult.size() > 0) {
			DBHelper.getInstance().update(TableCreate.TABLENAME_APPUSERINFO,
					values, "userid=?",
					new String[] { AccountApplication.userid });
		} else {
			DBHelper.getInstance().insert(TableCreate.TABLENAME_APPUSERINFO,
					values);
		}
	}

	public class PendingInfo {
		private int chargePendingCount;
		private int accountPendingCount;
		private int creditPendingCount;

		public int getChargePendingCount() {
			return chargePendingCount;
		}

		public void setChargePendingCount(int chargePendingCount) {
			this.chargePendingCount = chargePendingCount;
		}

		public int getAccountPendingCount() {
			return accountPendingCount;
		}

		public void setAccountPendingCount(int accountPendingCount) {
			this.accountPendingCount = accountPendingCount;
		}

		public int getCreditPendingCount() {
			return creditPendingCount;
		}

		public void setCreditPendingCount(int creditPendingCount) {
			this.creditPendingCount = creditPendingCount;
		}

	}

}
