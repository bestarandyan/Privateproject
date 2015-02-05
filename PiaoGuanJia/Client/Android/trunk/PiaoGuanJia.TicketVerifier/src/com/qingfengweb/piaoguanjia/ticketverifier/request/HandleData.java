package com.qingfengweb.piaoguanjia.ticketverifier.request;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingfengweb.piaoguanjia.ticketverifier.TicketApplication;
import com.qingfengweb.piaoguanjia.ticketverifier.bean.ValidateInfo;
import com.qingfengweb.piaoguanjia.ticketverifier.bean.UserInfo;
import com.qingfengweb.piaoguanjia.ticketverifier.database.DBHelper;
import com.qingfengweb.piaoguanjia.ticketverifier.database.TableCreate;
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
		values.put("username", TicketApplication.username);
		values.put("password", TicketApplication.password);
		values.put("userid", bean.getUserid());
		values.put("permissions", bean.getPermissions());
		values.put("productname", bean.getProductName());
		values.put("quitpassword", bean.getQuitPassword());
		values.put("iskeep", 1);
		TicketApplication.userid = bean.getUserid();
		TicketApplication.permissions = Integer.parseInt(bean.getPermissions());
		TicketApplication.productname = bean.getProductName();
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
	 * 验证列表 0,验证，1历史
	 */

	public static String handleOrderNumber(String response, int state) {
		String type = "";
		if (!response.startsWith("["))
			return "-1";
		Type listType = new TypeToken<LinkedList<ValidateInfo>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<ValidateInfo> beans = null;
		ValidateInfo bean = null;
		beans = gson.fromJson(response, listType);
		if (beans == null) {
			return "-1";
		}
		String tablename = "";
		if (state == 1) {
			tablename = TableCreate.TABLENAME_VALIDATEINFOHISTORY;
		} else {
			tablename = TableCreate.TABLENAME_VALIDATEINFO;
			DBHelper.getInstance().execSql("delete from "+tablename);
		}
		List<Map<String, Object>> selectresult = null;
		if (beans != null && beans.size() > 0) {
			for (Iterator<ValidateInfo> iterator = beans.iterator(); iterator
					.hasNext();) {
				bean = iterator.next();
				if (bean != null) {
					selectresult = DBHelper.getInstance().selectRow(
							"select * from " + tablename + " where orderid = '"
									+ bean.getOrderid() + "'", null);
					values.clear();
					values.put("userid", TicketApplication.userid);
					values.put("orderid", bean.getOrderid());
					values.put("ordernumber", bean.getOrderNumber());
					values.put("ordertime", bean.getOrderTime());
					values.put("productname", bean.getProductName());
					values.put("validatetime", bean.getValidateTime());
					values.put("totalamount", bean.getTotalAmount());
					values.put("name", bean.getName());

					if (selectresult != null && selectresult.size() > 0) {
						DBHelper.getInstance()
								.update(tablename, values, "orderid=?",
										new String[] { bean.getOrderid() });
					} else {
						DBHelper.getInstance().insert(tablename, values);
					}
					type = bean.getOrderNumber()+","+bean.getOrderid();
				}
			}
		}
		if (beans.size() == 1) {
			return type;
		} else {
			return "0";
		}
	}

	/**
	 * 验证详情
	 * 
	 * @param response
	 */
	public static void handleInfo(String response) {
		values.clear();
		Gson gson = new Gson();// 创建Gson对象
		ValidateInfo bean = null;
		List<Map<String, Object>> selectresult = null;
		try {
			bean = gson.fromJson(response, ValidateInfo.class);// 解析json对象
		} catch (Exception e) {
		}
		if (bean == null) {
			return;
		}
		selectresult = DBHelper.getInstance().selectRow(
				"select * from " + TableCreate.TABLENAME_INFO
						+ " where orderid = '" + bean.getOrderid() + "'", null);
		values.clear();
		values.put("userid", TicketApplication.userid);
		values.put("orderid", bean.getOrderid());
		values.put("ordernumber", bean.getOrderNumber());
		values.put("ordertime", bean.getOrderTime());
		values.put("productname", bean.getProductName());
		values.put("totalamount", bean.getTotalAmount());
		values.put("validatetime", bean.getValidateTime());
		values.put("parentname", bean.getParentName());
		values.put("name", bean.getName());
		values.put("createtime", bean.getCreateTime());
		values.put("phonenumber", bean.getPhoneNumber());
		values.put("credentialsnumber", bean.getCredentialsNumber());

		if (selectresult != null && selectresult.size() > 0) {
			DBHelper.getInstance().update(TableCreate.TABLENAME_INFO,
					values, "orderid=?", new String[] { bean.getOrderid() });
		} else {
			DBHelper.getInstance().insert(TableCreate.TABLENAME_INFO,
					values);
		}
	}

	/**
	 * 更新验证状态
	 * 
	 * @param msg
	 * @param context
	 */
	public static void handleStatus(String ordernumbers) {
		if(ordernumbers.contains(",")){
			String[] ids = ordernumbers.split(",");
			for(int i = 0; i<ids.length;i++){
				DBHelper.getInstance().execSql("delete from "+TableCreate.TABLENAME_VALIDATEINFO+" where ordernumber='"+ids[i]+"'");
			}
		}else{
			DBHelper.getInstance().execSql("delete from "+TableCreate.TABLENAME_VALIDATEINFO+" where ordernumber='"+ordernumbers+"'");
		}
	}
}
