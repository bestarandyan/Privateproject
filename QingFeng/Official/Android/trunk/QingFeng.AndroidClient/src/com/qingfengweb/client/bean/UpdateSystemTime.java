package com.qingfengweb.client.bean;

public class UpdateSystemTime {
	public int type = 0;
	public String update_time = "";//系统更新时间
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public static final String tableName = "systemTime";
	public static final String TABLE_CREATE_SQL ="create table if not exists "+tableName+"" +
			"(_id Integer primary key autoincrement," +
			"type varchar(10)," +
			"localTime varchar(40)," +
			"sysTime varchar(40))";
}
