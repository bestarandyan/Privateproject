package com.qingfengweb.model;

public class ImgLocalData {
	public static String tbName = "imglocaldata";
	public static String createTbSQl = "create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"imgurl text," +//图片路劲
			"isUpload verchar(10)," +//图片是否已经成功上传 0代表未上传成功  1代表已上传成功
			"state verchar(10)," +//图片状态 0：已删除  1存在（这都是站在对数据库的操作上，并不是外力删除）
			"uploadid text," +//上传时服务器返回的id
			"name text," +//图片名称
			"createtime text" +//图片获取的时间
			")";
}
