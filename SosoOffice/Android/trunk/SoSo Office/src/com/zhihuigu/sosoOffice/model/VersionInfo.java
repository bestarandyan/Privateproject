package com.zhihuigu.sosoOffice.model;

public class VersionInfo {
	/***
	 * author by Ring
	 * 解析错误值得数据模型
	 */
	public VersionInfo(){
		setIsMustUpdate("0");
		setURL("");
		setVERSION_NUMBER("");
	}
	
	
	public static String TABLE_CREATE = "create table " +
			"soso_versioninfo" +//表名 sosoconfigurationinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"ismustupdate text,"+//配置中key值
			"version_number text,"+//配置中的value值
			"url text"+//更新配置的时间
			")";
	
	private String IsMustUpdate;
	private String VERSION_NUMBER;
	private String URL;
	public String getIsMustUpdate() {
		return IsMustUpdate;
	}
	public void setIsMustUpdate(String isMustUpdate) {
		IsMustUpdate = isMustUpdate;
	}
	public String getVERSION_NUMBER() {
		return VERSION_NUMBER;
	}
	public void setVERSION_NUMBER(String vERSION_NUMBER) {
		VERSION_NUMBER = vERSION_NUMBER;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}

	
	
}
