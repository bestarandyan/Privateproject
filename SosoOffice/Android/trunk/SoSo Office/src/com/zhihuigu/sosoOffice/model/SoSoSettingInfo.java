package com.zhihuigu.sosoOffice.model;

public class SoSoSettingInfo {
	/***
	 * author By Ring
	 */
	// SQL Command for creating the table
	public static String TABLE_CREATE = "create table sososettinginfo("
			+ "_id integer primary key autoincrement," + 
			"userid text,"+//用户id
			"username text,"+//用户名
			"isshowimage integer," + // 是否显示图片，0提示，1显示,2不显示
			"ismapdisplay integer," + // 是否显示列表还是地图  0代表应该显示地图  1代表应该显示列表
			"room_state_for_examine integer," + //1代码审核通过的房源  2代表待审核房源
			"roomManagerForm integer," + //0:审核通过的房源中图形列表    1文字列表的状态
			"isnotice integer," + // 是否消息通知
			"noticestarttime text," + // 消息通知开始时间
			"cityid text,"+//城市id
			"roleid text,"+//角色
			"noticeendtime text" + // 消息通知结束时间
			")";
}
