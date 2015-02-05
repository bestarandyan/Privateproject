/**
 * 
 */
package com.qingfengweb.model;
public class MyCustomInfo {
	public static String TableName = "myCustom";
	public static String CreateTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"sceneid text," +
			"propsid text," +
			"garmentid text," +
			"customtime text," +
			"comments text," +
			"username text," +
			"createtime text)";
	public String id = "";
	public String sceneid = "";
	public String propsid = "";
	public String garmentid = "";
	public String customtime = "";
	public String comments = "";
	public String createtime = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSceneid() {
		return sceneid;
	}
	public void setSceneid(String sceneid) {
		this.sceneid = sceneid;
	}
	public String getPropsid() {
		return propsid;
	}
	public void setPropsid(String propsid) {
		this.propsid = propsid;
	}
	public String getGarmentid() {
		return garmentid;
	}
	public void setGarmentid(String garmentid) {
		this.garmentid = garmentid;
	}
	public String getCustomtime() {
		return customtime;
	}
	public void setCustomtime(String customtime) {
		this.customtime = customtime;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
}
