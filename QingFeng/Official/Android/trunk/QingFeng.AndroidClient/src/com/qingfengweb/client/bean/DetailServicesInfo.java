package com.qingfengweb.client.bean;

public class DetailServicesInfo {
	public String type ="";
	public String content =  "";
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public static final String TableName = "servicesDetail";//服务详情表
	public  static final String createTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"type varchar(10)," +
			"content varchar(1000))";
}
