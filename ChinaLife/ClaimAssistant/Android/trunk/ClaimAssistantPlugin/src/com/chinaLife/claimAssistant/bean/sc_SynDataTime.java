package com.chinaLife.claimAssistant.bean;

public class sc_SynDataTime {
	// SQL Command for creating the table
	public static String TABLE_CREATE = "create table syndatatime("
			+ "_id integer primary key autoincrement," + "phonenumber text,"
			+ "platenumber text," + "tablename text," + "time text" + ")";

	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS syndatatime";
}
