/**
 * 
 */
package com.boluomi.children.model;

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
			"username text," +
			"deleted varchar(10))";
	public String id  ="";
	public String type  ="";
	public String imageid  ="";
	public String name  ="";
	public String username  ="";
	public String deleted  ="";
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
