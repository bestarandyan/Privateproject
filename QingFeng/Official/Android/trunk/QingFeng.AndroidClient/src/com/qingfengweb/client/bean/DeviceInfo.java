/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author ������
 * @createDate 2013/7/23
 *
 */
public class DeviceInfo {
	public static final String TABLE_NAME = "deviceInfo";//�豸��Ϣ����
	public static final String TABLE_CREATE_SQL ="create table if not exists "+TABLE_NAME+"" +
			"(_id Integer primary key autoincrement," +
			"state varchar(10))";
}
