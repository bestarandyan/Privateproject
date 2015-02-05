/**
 * 
 */
package com.qingfengweb.model;

/**
 * @author 刘星星
 * @createDate 2013/9/10
 * 用户照片实体类
 *
 */
public class UserPhotoInfo {
	public static final String TableName = "userPhotos";//
	public static final String createTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id varchar(40)," +
			"type Integer," +
			"imageid varchar(40)," +
			"name text," +
			"iscover varchar(10)," +
			"username text," +
			"createtime text," +//图片获取的时间
			"imgurl text," +//图片路劲 本地sd卡路径
			"isUpload verchar(10)," +//图片是否已经成功上传 0代表未上传成功  1代表已上传成功
			"state verchar(10)," +//图片状态 0：已删除  1存在（这都是站在对数据库的操作上，并不是外力删除）
			"deleted varchar(10))";
	public String id  ="";
	public String type  ="";
	public String imageid  ="";
	public String name  ="";
	public String username  ="";
	public String deleted  ="";
	public String iscover = "";
	
	public String getIscover() {
		return iscover;
	}
	public void setIscover(String iscover) {
		this.iscover = iscover;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
}
