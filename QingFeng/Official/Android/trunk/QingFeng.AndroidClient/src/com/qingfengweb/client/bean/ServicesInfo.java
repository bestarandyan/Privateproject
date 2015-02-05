/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author qingfeng
 *
 */
public class ServicesInfo {
	public static final String tableName = "services";
	public static final String createTabSQL = "create table if not exists "+tableName+"" +
			"(_id Integer primary key autoincrement," +
			"type Integer," +
			"summary text," +
			"update_time varchar(40))";
	public String type = "";
	public String summary ="";
	public String update_time = "";
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	
}
