/**
 * 
 */
package com.qingfengweb.model;

/**
 * @author 相册列表信息表
 *
 */
public class PictureThemes {
	public static final String TableName = "pictureThemes";
	public static final String CreateTabelSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"name text," +
			"thumb text," +
			"description text," + 
			"ranking Integer," +
			"commend Integer," +
			"storeid Integer," +
			"updatetime text,"+
			"deleted Integer)";
	
	public String id = "";
	public String name = "";
	public String thumb = "";
	public String description = "";
	public String ranking = "";
	public String commend = "";
	public String deleted = "";
	public String updatetime = "";
	
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
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
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public String getCommend() {
		return commend;
	}
	public void setCommend(String commend) {
		this.commend = commend;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
	
}
