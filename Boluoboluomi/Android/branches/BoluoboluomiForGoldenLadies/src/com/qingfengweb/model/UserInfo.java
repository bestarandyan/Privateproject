package com.qingfengweb.model;

public class UserInfo {
	public static final String TableName = "userInfo";//用户表名
	public static final String CreateTableSQL = "create table if not exists "+TableName+"(_id Integer primary key autoincrement," +
			"userid varchar(20)," +
			"storeid varchar(20)," +
			"username varchar(40)," +
			"password varchar(40)," +
			"realname varchar(20)," +
			"points varchar(40)," +
			"islastuser varchar(10)," +//是否为最新登录用户
			"lastlogintime varchar(40)," +//最后登录时间
			"storename text," +//所选门店 名称
			"isautologin varchar(10)," +//是否为自动登陆
			"picture_count Integer)";
	public String userid ="";
	public String storeid ="";
	public String username ="";
	public String realname ="";
	public String points ="";
	public String picture_count ="";
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getStoreid() {
		return storeid;
	}
	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getPicture_count() {
		return picture_count;
	}
	public void setPicture_count(String picture_count) {
		this.picture_count = picture_count;
	}
	
	
	
	
}
