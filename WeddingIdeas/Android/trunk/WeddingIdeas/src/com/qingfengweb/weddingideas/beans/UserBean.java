/**
 * 
 */
package com.qingfengweb.weddingideas.beans;

/**
 * @author qingfeng
 *
 */
public class UserBean {
	public static String tbName="userTable";
	public static String createTbSQL = "create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"userid text," +
			"username text," +
			"password text," +
			"type varchar(10)," +
			"name text," +
			"islogin varchar(10)" +
			")";
	
}
