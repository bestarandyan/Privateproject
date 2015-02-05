package com.chinaLife.claimAssistant.bean;

public class sc_LogInfo {
	/**
	 * 创建日志信息表
	 * 0-所有（All打印所有的日志记录）
	 * 1-调试（Debug）
	 * 2-信息（Info）
	 * 3-警告(Warn)
	 * 4-一般错误(Error)
	 * 5-严重错误(Fatal这种级别的日志将会导致程序退出)
	 * 6-关闭（Off用于关闭所有的日志记录）
	 */
	
	
	public static String TABLE_CREATE = "create table " +
			"loginfo" +//表名 appinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"plateNumber text,"+//车牌号
			"phoneNumber text,"+//手机号
			"imei integer,"+//手机imei
			"createtime integer,"+//日志创建时间
			"uploadtime integer,"+//上传时间
			"type integer,"+//日志类型
			"content integer,"+//日志内容
			"status integer"+//日志状态，0未上传，1上传
			")";
	
	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS loginfo";
}
