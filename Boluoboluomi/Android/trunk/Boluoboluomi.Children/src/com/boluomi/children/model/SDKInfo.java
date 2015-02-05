package com.boluomi.children.model;

public class SDKInfo {
	public static String TableName = "sdkinfo";//软件附加信息表
	public static String TABLE_CREATE = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"ispassguide varchar(10)," +
			"storeid varchar(20)," +
			"store text," +
			"isselectstore varchar(10))";
}
