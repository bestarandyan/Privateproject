/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author Ring
 * 案件详情
 */

public class ListCaseInfo {
	/**author by Ring
	 * 客户案例列表
	 */
	public static String TableName = "listCaseInfo";//客户案例列表
	public static String CREATE_TABLE_SQL = "create table " +
			TableName +//表名 
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"id text,"+//案例编号
			"name text,"+//案例名称
			"summary text,"+//案例概述
			"photos varchar(100),"+//图片id集合
			"thumb varchar(10),"+//列表中要显示的图片ID
			"type varchar(40),"+//案例类型
			"description varchar(200),"+//案例描述
			"sequence varchar(10),"+//排列顺序
			"updatetime varchar(40),"+//更新时间
			"status varchar(40)"+//图片状态  1-以删除
			")";
	public  String id = "";
	public  String name = "";
	public  String summary = "";
	public  String photos = "";
	public  String thumb = "";
	public  String type = "";
	public  String description = "";
	public  String sequence = "";
	public  String updatetime = "";
	public  String status = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
