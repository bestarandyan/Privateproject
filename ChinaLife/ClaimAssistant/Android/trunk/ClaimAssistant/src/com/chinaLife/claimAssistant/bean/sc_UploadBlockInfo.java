package com.chinaLife.claimAssistant.bean;

public class sc_UploadBlockInfo {
	/**
	 * 文件上传块信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"uploadblockinfo" +//表名uploadblockinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"blockid text,"+//分块编号
			"uploadid text,"+//上传编号
			"indexid integer,"+//块索引
			"block_start integer,"+//开始位置
			"block_end integer,"+//块结束
			"status integer,"+//状态0-未上传1-正在上传2-上传成功3-上传失败
			"last_position integer"+//最后上传位置
			")";
	
	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS uploadblockinfo";
	
}
