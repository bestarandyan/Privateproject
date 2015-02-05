/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author 刘星星
 * 首页的图片信息
 *
 */
public class HomeImageInfo {
	public String id = "";
	public String status = "";
	public String update_time = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public static final String TableName = "homeImage";//首页的图片表名
	public static final String createTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id varchar(10)," +
			"status varchar(10)," +
			"update_time varchar(50))";
	
	
}
