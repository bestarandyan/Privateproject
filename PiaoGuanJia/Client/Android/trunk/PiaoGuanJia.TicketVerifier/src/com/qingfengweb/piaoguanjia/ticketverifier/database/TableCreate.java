package com.qingfengweb.piaoguanjia.ticketverifier.database;

import com.sqlcrypt.database.sqlite.SQLiteDatabase;

public class TableCreate {
	public static final String TABLENAME_USERINFO = "userinfo";// 用户表
	public static final String TABLENAME_VALIDATEINFO = "validateinfo";// 验证表
	public static final String TABLENAME_VALIDATEINFOHISTORY = "validateinfohistory";// 历史表
	public static final String TABLENAME_INFO = "info";// 详细信息表

	/**
	 * 用户表
	 */
	public static String TABLECREATE_USERINFO = "create table "
			+ TABLENAME_USERINFO + //
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"userid text," + // 用户编号
			"username text," + // 用户名
			"password text," + // 用户密码
			"productname text," + // 关联的产品供应商关联的产品名称（如果为空，则不能输入券码）
			"quitpassword text," + // 退出密码经过sha1加密
			"permissions integer," + // 用户权限
			"iskeep integer" + // 是否保存用户名，密码 1 保存，2不保存
			")";

	/**
	 * 验证表
	 */
	public static String TABLECREATE_VALIDATEINFO = "create table "
			+ TABLENAME_VALIDATEINFO + //
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"userid text," + // 用户编号
			"orderid text," + // 订单编号 获取详情使用
			"ordernumber text	," + // 订单号 显示在列表上
			"ordertime text," + // 预定时间 yyyy-MM-dd
			"validatetime text," + // 验证时间
			"productname text," + // 票种
			"totalamount text," + // 预定数量
			"name text" + // 姓名
			")";
	
	/**
	 * 详细信息表
	 */
	public static String TABLECREATE_INFO = "create table "
			+ TABLENAME_INFO + //
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"userid text," + // 用户编号
			"orderid text," + // 订单编号 获取详情使用
			"ordernumber text	," + // 订单号 显示在列表上
			"ordertime text," + // 预定时间 yyyy-MM-dd
			"productname text," + // 票种
			"totalamount text," + // 预定数量
			"validatetime text," + // 验证时间
			"parentname text," + // 接口
			"name text," + // 姓名
			"createtime text," + // 创建时间
			"phonenumber text," + // 手机号码
			"credentialsnumber text" + // 身份证号码
			")";
	
	/**
	 * 验证历史记录表
	 */
	public static String TABLECREATE_VALIDATEINFOHISTORY = "create table "
			+ TABLENAME_VALIDATEINFOHISTORY + //
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"userid text," + // 用户编号
			"orderid text," + // 订单编号 获取详情使用
			"ordernumber text	," + // 订单号 显示在列表上
			"validatetime text," + // 验证时间
			"ordertime text," + // 预定时间 yyyy-MM-dd
			"productname text," + // 票种
			"totalamount text," + // 预定数量
			"name text" + // 姓名
			")";
	/**
	 * 
	 * @param db
	 */
	public static void makeTable(SQLiteDatabase db) {
		db.execSQL(TABLECREATE_USERINFO);
		db.execSQL(TABLECREATE_VALIDATEINFOHISTORY);
		db.execSQL(TABLECREATE_VALIDATEINFO);
		db.execSQL(TABLECREATE_INFO);
	}

}
