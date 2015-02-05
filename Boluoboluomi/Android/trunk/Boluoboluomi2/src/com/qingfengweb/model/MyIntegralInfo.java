/**
 * 
 */
package com.qingfengweb.model;

/**
 * @author 刘星星
 * @createDate 2013、9、18
 * 我的积分实体类
 *
 */
public class MyIntegralInfo {
	public static final String TableName = "myintegralinfo";
	public static final String CreateTableSql = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"integralstr text," +
			"username text," +
			"password text)";
}
