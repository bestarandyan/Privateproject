/**
 * 
 */
package com.qingfengweb.model;
public class HelpInfo {
	public static String TbName = "helpInfo";
	public static String TbCreateSQL = "create table if not exists "+TbName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"type varchar(10)," +
			"title text," +
			"content text," +
			"ranking varchar(10)," +
			"createtime text," +
			"storeid Integer," +
			"deleted varchar(10))";
	
	public String id ="";
	public String type ="";
	public String title ="";
	public String content ="";
	public String ranking ="";
	public String createtime ="";
	public String deleted ="";
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
	
	
}
