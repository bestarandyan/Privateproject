/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author 刘星星
 * @createDate 2013、8、2
 * 内容列表更新的数据信息类
 *
 */
public class ContentUpdateInfo {
	public static final String tableName = "contentUpdate";
	public static final String createTableSQL = "create table if not exists "+tableName+"" +
			"(_id Integer primary key autoincrement," +
			"type varchar(10)," +
			"summary varchar(1000))";
	public String type = "";
	public String summary = "";
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
	
}
