/**
 * 
 */
package com.boluomi.children.model;

/**
 * @author 刘星星
 * @createDate 2013、9、3
 * 系统更新机制Bean
 *
 */
public class SystemUpdateInfo {
	public static final String TableName = "systemUpdate";//系统更新表
	public static final String createSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"type Integer," +
			"updatetime text," +
			"localtime text," +
			"storeid text," +
			"userid text," +
			"number Integer)";//
	public String type = "";
	public String updatetime = "";
	public String number = "";
	public String storeid = "";
	
	public String getStoreid() {
		return storeid;
	}
	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
}
