package com.qingfengweb.db;

public class UserBean {
	public static UserBean userBean = null;
	public UserBean() {
		// TODO Auto-generated constructor stub
	}
	public static UserBean getInstence(){
		if(userBean == null){
			userBean = new UserBean();
		}
		return userBean;
		
	}
	public String username;
	public String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String sql = "create table if not exists user(_id integer primary key autoincrement," +
			"username varchar(40)," +
			"password varchar(40))";
}
