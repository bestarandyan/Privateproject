package com.qingfengweb.model;

public class UpLoadFileInfo {
	public static String TableName = "fileUpload";
	public static String CreateTabSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"uploadid Integer," +
			"type Integer," +
			"imageid Integer," +
			"localpath text," +//图片的本地路径
			"name text," +
			"successlocation varchar(50)," +//上传成功的位置 0-successlocation
			"imglength varchar(50)," +//文件的总长度   字节
			"progress varchar(20))";
	public String id = "";
	public String progress = "";
	public String type = "";
	public String imageid = "";
	public String name = "";
	public String uploadid = "";
	
	
	public String getUploadid() {
		return uploadid;
	}
	public void setUploadid(String uploadid) {
		this.uploadid = uploadid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getImageid() {
		return imageid;
	}
	public void setImageid(String imageid) {
		this.imageid = imageid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
}
