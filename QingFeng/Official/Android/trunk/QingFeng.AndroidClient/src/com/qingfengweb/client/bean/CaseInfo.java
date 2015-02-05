/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author ¡ı–«–«
 * @createDate 2013°¢8°¢6
 *
 */
public class CaseInfo {
	public static String TableName = "CasesInfo";
	public static String createTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"name varchar(50)," +
			"summary text," +
			"photos varchar(100)," +
			"thumb Integer," +
			"type varchar(10)," +
			"description text," +
			"sequence varchar(10)," +
			"updatetime varchar(40)," +
			"status Integer)";
	public String id = "";
	public String name = "";
	public String summary = "";
	public String photos = "";
	public String thumb = "";
	public String type = "";
	public String description = "";
	public String sequence = "";
	public String updatetime = "";
	public String status = "";
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
