/**
 * 
 */
package com.qingfengweb.weddingideas.beans;

/**
 * @author 刘星星
 * @createDate 2014/1/13
 *
 */
public class WeddingLogBean {
	public static String tbName = "weddinglog";
	public static String tbCreateSql = "create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"id text," +
			"userId text," +
			"stimeString text," +
			"logTypeOne text," +
			"logTypeTwo text," +
			"isOk varchar(10)," +
			"alertDateString text," +
			"logContent text," +
			"optTimeString text" +
			")";
	public String id="";
	public String userId="";
	public String logContent ="";
	public String isOk = "";
	public String optTimeString =""; 
	public String stimeString ="";
	
	public String getStimeString() {
		return stimeString;
	}
	public void setStimeString(String stimeString) {
		this.stimeString = stimeString;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	public String getIsOk() {
		return isOk;
	}
	public void setIsOk(String isOk) {
		this.isOk = isOk;
	}
	public String getOptTimeString() {
		return optTimeString;
	}
	public void setOptTimeString(String optTimeString) {
		this.optTimeString = optTimeString;
	}
	
	
}
