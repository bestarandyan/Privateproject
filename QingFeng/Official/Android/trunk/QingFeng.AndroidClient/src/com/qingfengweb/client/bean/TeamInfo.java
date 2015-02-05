/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author ¡ı–«–«
 * @createDate 2013/8/5
 *
 */
public class TeamInfo {
	public static String TableName = "teamInfo";
	public static String createTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id varchar(20)," +
			"name varchar(20)," +
			"position varchar(40)," +
			"photoid varchar(20)," +
			"motto text," +
			"sequence Integer," +
			"updatetime varchar(40)," +
			"status varchar(10))";
	public String id = "";
	public String name  = "";
	public String position  = "";
	public String photoid  = "";
	public String motto  = "";
	public String sequence  = "";
	public String updatetime  = "";
	public String status  = "";
	
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
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getPhotoid() {
		return photoid;
	}
	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}
	public String getMotto() {
		return motto;
	}
	public void setMotto(String motto) {
		this.motto = motto;
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
