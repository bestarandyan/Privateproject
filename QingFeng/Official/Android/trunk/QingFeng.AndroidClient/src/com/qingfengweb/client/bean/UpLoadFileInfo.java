/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author ¡ı–«–«
 * @createDate 2013°¢8°¢8
 *
 */
public class UpLoadFileInfo {
	public static String TableName = "fileUpload";
	public static String CreateTabSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"progress varchar(20))";
	public String id = "";
	public String progress = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	
	
}
