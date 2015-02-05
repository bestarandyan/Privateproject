package com.chinaLife.claimAssistant.bean;

public class sc_UploadInfo {
	/**
	 * 文件上传信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"uploadinfo" +//表名uploadinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"uploadid text,"+//上传编号
			"legendid text,"+//图例编号
			"claimid text,"+//理赔任务编号
			"filename text,"+//文件名
			"savepath text,"+//保存路径
			"filesize text,"+//文件大小：字节
			"block_count integer,"+//分块数量
			"update_time text,"+//上传时间
			"complete_time text,"+//完成时间
			"status integer,"+//状态：1-上传中2-上传完成3-上传失败
			"remark text,"+//备注
			"type integer,"+//照片类型：1-现场拍照2-补全单证
			"longitude text,"+//经度
			"latitude text,"+//纬度
			"location text,"+//拍摄地址
			"time text"+//拍摄时间
			")";
	
	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS uploadinfo";
}
