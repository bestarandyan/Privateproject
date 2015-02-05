/**
 * 
 */
package com.qingfengweb.client.bean;


/**
 * @author Ring
 * 员工信息模型
 *
 */
public class StaffInfo {
	/**author by Ring
	 * 员工信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"staffinfo" +//表名 
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"staffid text,"+//员工id
			"name text,"+//员工名称
			"birthday text,"+//员工生日
			"sex integer,"+//员工性别1,男，2女
			"positionid text,"+//职位id
			"imageid text,"+//个人头像
			"tag integer default 0,"+//默认存在的数据为1
			"declaration text"+//个人宣言
			")";
}
