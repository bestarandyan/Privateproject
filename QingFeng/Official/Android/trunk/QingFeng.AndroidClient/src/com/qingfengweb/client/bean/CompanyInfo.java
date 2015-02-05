/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author ¡ı–«–«
 * @createDate 2013°¢8°¢5
 *
 */
public class CompanyInfo {
	public static String TableName = "companyInfo";
	public static String createTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"content text)";
}
