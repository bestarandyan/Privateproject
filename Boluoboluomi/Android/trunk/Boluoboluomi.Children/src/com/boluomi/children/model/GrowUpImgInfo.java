package com.boluomi.children.model;

public class GrowUpImgInfo {
	public static String tablename = "growupImgInfo";//成长经历表
	public static String createTableSQL = "create table if not exists "+tablename+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"growupid text," +
			"userid text," +
			"imglocalurl text," +
			"photoname text)";
}
