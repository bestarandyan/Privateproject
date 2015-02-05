/**
 * 
 */
package com.qingfengweb.model;

/**
 * @author 刘星星
 * @createDate 2013/9/12
 * 活动分享列表实体类
 */
public class ActiveListInfo {
	public static final String TableName = "activeshare";//活动分享表
	public static final String CreateTabSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id varchar(20)," +
			"imageid varchar(20)," +
			"storeid varchar(40)," +
			"activedetail text," +
			"title text)";//创建表
	
	public String id = "";//照片id
	public String imageid = "";//图片id
	public String title = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageid() {
		return imageid;
	}
	public void setImageid(String imageid) {
		this.imageid = imageid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
