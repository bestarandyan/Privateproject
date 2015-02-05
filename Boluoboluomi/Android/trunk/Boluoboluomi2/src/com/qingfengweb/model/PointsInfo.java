package com.qingfengweb.model;

public class PointsInfo {
	/**
	 * author:Ring
	 * 积分信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"pointsinfo" +//表名 pointsinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"userid text,"+//用户id
			"pointtotal text,"+//总的积分数
			"pointregister text,"+//注册获取的积分数
			"pointrecommend text,"+//推荐好友获取的积分数
			"pointrecommendsuccess text,"+//推荐好友成功获取的积分数
			"pointrecommendfail text,"+//推荐虚假好友扣去的积分数
			"pointshare text,"+//微博分享获取的积分数
			"pointcomments text,"+//点评获取的积分数
			"pointexchange text"+//积分兑换扣除的分数
			")";

}
