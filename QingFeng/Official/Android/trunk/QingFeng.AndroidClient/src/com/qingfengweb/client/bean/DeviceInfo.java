/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author 刘星星
 * @createDate 2013/7/23
 *
 */
public class DeviceInfo {
	public static final String TABLE_NAME = "deviceInfo";//设备信息表名
	public static final String TABLE_CREATE_SQL ="create table if not exists "+TABLE_NAME+"" +
			"(_id Integer primary key autoincrement," +
			"state varchar(10))";
}
