/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;

import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.UserType;
import com.zhihuigu.sosoOffice.database.DBHelper;

/**
 * @author Ring
 * 
 */
public class ApplicationSettingInfo {
	public ApplicationSettingInfo(){
	}
	/**
	 * 初始化配置信息 作者：Ring 创建于：2013-2-2 return true 用户第一次使用
	 */
	public static boolean initSetting(Context context, String userid) {
		List<Map<String, Object>> selectresult = DBHelper.getInstance(context)
				.selectRow(
						"select * from sososettinginfo where userid= '"
								+ userid + "'", null);
		List<Map<String, Object>> selectresult0 = DBHelper.getInstance(context)
				.selectRow(
						"select * from sososettinginfo where userid= '"
								+ 0 + "'", null);
		ContentValues values = new ContentValues();
		values.clear();
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1).get("isshowimage") == null
					|| selectresult.get(selectresult.size() - 1)
							.get("isshowimage").toString().equals("")) {
				values.put("isshowimage", "0");
				MyApplication.getInstance().setDisplayRoomPhoto(0);
			}else{
				int n = 0;
				try {
					n = Integer.parseInt(selectresult
							.get(selectresult.size() - 1).get("isshowimage")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				MyApplication.getInstance().setDisplayRoomPhoto(n);
			}
			
			if (selectresult.get(selectresult.size() - 1).get("ismapdisplay") == null
					|| selectresult.get(selectresult.size() - 1)
							.get("ismapdisplay").toString().equals("")) {
				values.put("ismapdisplay", "0");
				MyApplication.getInstance().setMap_display_flag(0);
			}else{
				int n = 0;
				try {
					n = Integer.parseInt(selectresult
							.get(selectresult.size() - 1).get("ismapdisplay")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				MyApplication.getInstance().setMap_display_flag(n);
			}
			if (selectresult.get(selectresult.size() - 1).get("room_state_for_examine") == null
					|| selectresult.get(selectresult.size() - 1)
							.get("room_state_for_examine").toString().equals("")) {
				values.put("room_state_for_examine", "1");
				MyApplication.getInstance().setRoom_state_for_examine(1);
			}else{
				int n = 0;
				try {
					n = Integer.parseInt(selectresult
							.get(selectresult.size() - 1).get("room_state_for_examine")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				MyApplication.getInstance().setRoom_state_for_examine(n);
			}
			
			if (selectresult.get(selectresult.size() - 1).get("roommanagerform") == null
					|| selectresult.get(selectresult.size() - 1)
							.get("roommanagerform").toString().equals("")) {
				values.put("roommanagerform", "0");
				MyApplication.getInstance().setRoomManagerForm(0);
			}else{
				int n = 0;
				try {
					n = Integer.parseInt(selectresult
							.get(selectresult.size() - 1).get("roommanagerform")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				MyApplication.getInstance().setRoomManagerForm(n);
			}
			
			if (selectresult.get(selectresult.size() - 1).get("isnotice") == null
					|| selectresult.get(selectresult.size() - 1)
							.get("isnotice").toString().equals("")) {
				values.put("isnotice", "0");
			}
			if (selectresult.get(selectresult.size() - 1)
					.get("noticestarttime") == null
					|| selectresult.get(selectresult.size() - 1)
							.get("noticestarttime").toString().equals("")) {
				values.put("noticestarttime", "00:00");
			}
			if (selectresult.get(selectresult.size() - 1).get("noticeendtime") == null
					|| selectresult.get(selectresult.size() - 1)
							.get("noticeendtime").toString().equals("")) {
				values.put("noticeendtime", "24:00");
			}
			if (selectresult.get(selectresult.size() - 1).get("cityid") == null) {
				values.put("cityid", "");
			}else{
				MyApplication.getInstance().setCityid(selectresult.get(selectresult.size() - 1).get("cityid").toString());
			}
			if (values.size() > 0) {
				DBHelper.getInstance(context).update("sososettinginfo", values,
						"userid = ?", new String[] { userid });
			}
			if (selectresult.get(selectresult.size() - 1).get("cityid") != null&&
					selectresult.get(selectresult.size() - 1).get("cityid")
					.toString().equals("")) {
				return true;
			}
			return false;
		} else {
			String cityid = "";
			if(selectresult0 != null && selectresult0.size() > 0){
				if (selectresult0.get(selectresult0.size() - 1).get("cityid") == null) {
					values.put("cityid", "");
				}else{
					cityid=selectresult0.get(selectresult0.size() - 1).get("cityid").toString();
					MyApplication.getInstance().setCityid(cityid);
					values.put("cityid", cityid);
				}
			}
			values.put("userid", userid);
			values.put("isshowimage", "0");
			values.put("ismapdisplay", "0");
			values.put("isnotice", "0");
			values.put("noticestarttime", "00:00");
			values.put("noticeendtime", "24:00");
			values.put("room_state_for_examine", "1");
			values.put("roomManagerForm", "0");
			MyApplication.getInstance().setRoomManagerForm(0);
			MyApplication.getInstance().setDisplayRoomPhoto(0);
			MyApplication.getInstance().setMap_display_flag(0);
			MyApplication.getInstance().setRoom_state_for_examine(1);
			DBHelper.getInstance(context).insert("sososettinginfo", values);
			if (!cityid.equals("")) {
				return false;
			}
			return true;
		}
	}
	
	/**
	 * 初始化配置信息 作者：Ring 创建于：2013-2-2 return true 用户第一次使用
	 */
	public static String getCitySetting(Context context, String userid) {
		List<Map<String, Object>> selectresult = DBHelper.getInstance(context)
				.selectRow(
						"select * from sososettinginfo where userid= '"
								+ userid + "'", null);
		String cityid = "1";
		ContentValues values = new ContentValues();
		values.clear();
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1).get("cityid") != null
					&&!selectresult.get(selectresult.size() - 1).get("cityid")
					.toString().equals("")) {
				cityid = selectresult.get(selectresult.size() - 1).get("cityid")
						.toString();
			}
		}
		return cityid;
	}
	
	/**
	 * 初始化配置信息 作者：Ring 创建于：2013-2-2 return true 用户第一次使用
	 */
	public static boolean isExistUser(Context context, String userid) {
		List<Map<String, Object>> selectresult = DBHelper.getInstance(context)
				.selectRow(
						"select roleid from sososettinginfo where userid= '"
								+ userid + "'", null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1).get("roleid") != null&&
					!selectresult.get(selectresult.size() - 1).get("roleid").toString().equals("")) {
				int roleid = 0;
				try{
					roleid = Integer.parseInt(selectresult.get(selectresult.size() - 1).get("roleid").toString());
				}catch(Exception e){
				}
				if(roleid==UserType.UserTypeCustomer.getValue()
						||roleid==UserType.UserTypeIntermediary.getValue()
						||roleid==UserType.UserTypeOwner.getValue())
				MyApplication.getInstance().setRoleid(roleid);
				return true;
			}
		}
		return false;
	}
}
