/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author Ring
 *职位信息表
 */
public class PositionInfo {
	/**author by Ring
	 * 职位信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"positioninfo" +//表名 
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"positionid text,"+//职位id
			"name text,"+//职位名称
			"tag integer default 0,"+//默认存在的数据为1
			"workadress text,"+//工作地点
			"createtime text,"+//创建时间
			"describe text"+//职位描述
			")";
	
}
