package com.chinaLife.claimAssistant.bean;

public class sc_UserInfo {
	/**
	 * 登录信息
	 */
	public static String TABLE_CREATE = "create table " +
			"userinfo" +//表名userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"contact_mobile_number text,"+//联系人手机号
			"plate_number text collate nocase,"+//联系人车牌号码
			"contact_mobile_number_keep integer,"+//是否记住联系人手机号
			"plate_number_keep integer,"+//是否记住车牌号
			"password text"+//密码
			")";
	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS userinfo";
}
