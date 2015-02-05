/**
 * 
 */
package com.qingfengweb.lottery.data;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingfengweb.lottery.activity.MainActivity;
import com.qingfengweb.lottery.bean.ChargeHistoryBean;
import com.qingfengweb.lottery.bean.LastResultBean;
import com.qingfengweb.lottery.bean.OrderBean;
import com.qingfengweb.lottery.bean.ResultInfo;
import com.qingfengweb.lottery.bean.UserBalanceBean;
import com.qingfengweb.lottery.bean.UserBean;
/**
 * @author 刘星星
 * @createDate 2013、9、3
 * 用json
 *
 */
public class JsonData {
	/**
	 * 注册成功后的用户信息
	 * @param msg
	 * @param context
	 */

	
	public static void jsonBalanceData(String msg,SQLiteDatabase database,Handler handler){
		try {
			JSONObject jsonObject = new JSONObject(msg);
			ContentValues contentValues = new ContentValues();
			contentValues.put("total_balance", jsonObject.getString("total_balance"));
			contentValues.put("freeze_balance", jsonObject.getString("freeze_balance"));
			contentValues.put("available_balance", jsonObject.getString("available_balance"));
			contentValues.put("username", MyApplication.getInstance().getCurrentUserName());
			int a = database.update(UserBalanceBean.tabName,contentValues, "username=?", new String[]{ MyApplication.getInstance().getCurrentUserName()});
			if(a == 0){
				database.insert(UserBalanceBean.tabName, null, contentValues);
			}
			MyApplication.getInstance().setCurrentBalance(jsonObject.getString("available_balance"));
			handler.sendEmptyMessage(3);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 解析开奖结果数据
	 * @param msg
	 * @param context
	 */
	public static void jsonResultInfo(String msg,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<ResultInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<ResultInfo> list;
		ResultInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<ResultInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("ticket_no", bean.getTicket_no());
				contentValues.put("org_results", bean.getOrg_results());
				contentValues.put("results", bean.getResults());
				contentValues.put("open_stmp", bean.getOpen_stmp());
				int a = database.update(ResultInfo.tbName, contentValues, "ticket_no=?", new String[]{bean.getTicket_no()+""});
				if(a == 0){
					database.insert(ResultInfo.tbName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	
	/**
	 * 解析开奖结果数据
	 * @param msg
	 * @param context
	 */
	public static void jsonLastResultInfo(String msg,SQLiteDatabase database){
		
		try {
			JSONObject jsonObject = new JSONObject(msg);
			ContentValues contentValues = new ContentValues();
			contentValues.put("ticket_no", jsonObject.getString("ticket_no"));
			contentValues.put("result", jsonObject.getString("result"));
			contentValues.put("code", jsonObject.getString("code"));
			contentValues.put("hot", jsonObject.getString("hot"));
			int a = database.update(LastResultBean.tbName,contentValues, "_id=?", new String[]{"1"});
			if(a == 0){
				database.insert(LastResultBean.tbName, null, contentValues);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 解析充值记录数据
	 * @param msg
	 * @param context
	 */
	public static void jsonChargeHistoryData(String msg,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<ChargeHistoryBean>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<ChargeHistoryBean> list;
		ChargeHistoryBean bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<ChargeHistoryBean> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("trade_id", bean.getTrade_id());
				contentValues.put("trade_no", bean.getTrade_no());
				contentValues.put("member_id", bean.getMember_id());
				contentValues.put("trade_type", bean.getTrade_type());
				contentValues.put("amount", bean.getAmount());
				contentValues.put("trade_status", bean.getTrade_status());
				contentValues.put("trade_info", bean.getTrade_info());
				contentValues.put("balance", bean.getBalance());
				contentValues.put("trade_desc", bean.getTrade_desc());
				contentValues.put("username", MyApplication.getInstance().getCurrentUserName());
				contentValues.put("create_stmp", bean.getCreate_stmp());
				contentValues.put("update_stmp", bean.getUpdate_stmp());
				int a = database.update(ChargeHistoryBean.tbName, contentValues, "trade_id=?", new String[]{bean.getTrade_id()+""});
				if(a == 0){
					database.insert(ChargeHistoryBean.tbName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	
	/**
	 * 解析充值记录数据
	 * @param msg
	 * @param context
	 */
	public static void jsonOrderData(String msg,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<OrderBean>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<OrderBean> list;
		OrderBean bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<OrderBean> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("order_id", bean.getOrder_id());
				contentValues.put("member_id", bean.getMember_id());
				contentValues.put("order_no", bean.getOrder_no());
				contentValues.put("amount", bean.getAmount());
				contentValues.put("bonus", bean.getBonus());
				contentValues.put("ispaid", bean.getIspaid());
				contentValues.put("paid_stmp", bean.getPaid_stmp());
				contentValues.put("isdeal", bean.getIsdeal());
				contentValues.put("deal_stmp", bean.getDeal_stmp());
				contentValues.put("iswin", bean.getIswin());
				contentValues.put("is_trace", bean.getIs_trace());
				contentValues.put("trace_count", bean.getTrace_count());
				contentValues.put("stop_after_win", bean.getStop_after_win());
				contentValues.put("create_stmp", bean.getCreate_stmp());
				contentValues.put("ticket_no", bean.getTicket_no());
				contentValues.put("username", MyApplication.getInstance().getCurrentUserName());
				int a = database.update(OrderBean.tbName, contentValues, "order_id=?", new String[]{bean.getOrder_id()+""});
				if(a == 0){
					database.insert(OrderBean.tbName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	
	/**
	 * 注册成功后的用户信息
	 * @param msg
	 * @param context
	 */

	
	public static void jsonRegisterUserData(String msg,SQLiteDatabase database){
		try {
			ContentValues values = new ContentValues();
			values.put("islastuser", "0");
			database.update(UserBean.tbName, values, null, null);
			
			JSONObject jsonObject = new JSONObject(msg);
			ContentValues contentValues = new ContentValues();
			contentValues.put("token", jsonObject.getString("token"));
			contentValues.put("member_id", jsonObject.getString("member_id"));
			contentValues.put("username", jsonObject.getString("username"));
			contentValues.put("password", MyApplication.getInstance().getCurrentPassword());
			contentValues.put("avatar", jsonObject.getString("avatar"));
			contentValues.put("nick_name", jsonObject.getString("nick_name"));
			contentValues.put("real_name", jsonObject.getString("real_name"));
			contentValues.put("identity_card", jsonObject.getString("identity_card"));
			contentValues.put("mobile", jsonObject.getString("mobile"));
			contentValues.put("email", jsonObject.getString("email"));
			contentValues.put("balance", jsonObject.getString("balance"));
			contentValues.put("actived", jsonObject.getString("actived"));
			contentValues.put("freeze_balance", jsonObject.getString("freeze_balance"));
			contentValues.put("reg_ip", jsonObject.getString("reg_ip"));
			contentValues.put("reg_stmp", jsonObject.getString("reg_stmp"));
			contentValues.put("active_stmp", jsonObject.getString("active_stmp"));
			contentValues.put("enabled", jsonObject.getString("enabled"));
			contentValues.put("islastuser", "1");
			contentValues.put("server_stmp", jsonObject.getString("server_stmp"));
			int a = database.update(UserBean.tbName,contentValues, "member_id=?", new String[]{jsonObject.getString("member_id")});
			if(a == 0){
				database.insert(UserBean.tbName, null, contentValues);
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("token", jsonObject.getString("token"));
			map.put("member_id", jsonObject.getString("member_id"));
			map.put("username", jsonObject.getString("username"));
			map.put("password", jsonObject.getString("password"));
			map.put("avatar", jsonObject.getString("avatar"));
			map.put("nick_name", jsonObject.getString("nick_name"));
			map.put("real_name", jsonObject.getString("real_name"));
			map.put("identity_card", jsonObject.getString("identity_card"));
			map.put("mobile", jsonObject.getString("mobile"));
			map.put("email", jsonObject.getString("email"));
			map.put("balance", jsonObject.getString("balance"));
			map.put("actived", jsonObject.getString("actived"));
			map.put("freeze_balance", jsonObject.getString("freeze_balance"));
			map.put("reg_ip", jsonObject.getString("reg_ip"));
			map.put("reg_stmp", jsonObject.getString("reg_stmp"));
			map.put("active_stmp", jsonObject.getString("active_stmp"));
			map.put("enabled", jsonObject.getString("enabled"));
			map.put("islastuser", "1");
			map.put("headimg_localurl", "");
			map.put("server_stmp", jsonObject.getString("server_stmp"));
			MainActivity.userMap = map;
			MyApplication.getInstance().setCurrentToken(jsonObject.getString("token"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断服务器返回值是否为无数据的格式
	 * @param str
	 * @return
	 */
	public static boolean isNoData(String str){
		String format = "\\d+\\,20[1,2]\\d-((0?[0-9])|1[0-2])-(([0-2][0-9])|3[0-1])(\\s([0-1][0-9]|2[0-4]):([0-5][0-9])(:([0-5][0-9]))?)?";
		Matcher matcher = Pattern.compile(format).matcher(str);
		while(matcher.find()){
			return true;
		}
		return false;
	}
}
