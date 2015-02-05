/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author qingfeng
 *
 */
public class ConnectWayInfo {
	public static final String tableName = "connectWay";
	public static final String createTabSQL = "create table if not exists "+tableName+"" +
			"(_id Integer," +
			"content text)";
}
