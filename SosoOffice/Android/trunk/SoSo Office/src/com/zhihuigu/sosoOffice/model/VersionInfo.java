package com.zhihuigu.sosoOffice.model;

public class VersionInfo {
	/***
	 * author by Ring
	 * ��������ֵ������ģ��
	 */
	public VersionInfo(){
		setIsMustUpdate("0");
		setURL("");
		setVERSION_NUMBER("");
	}
	
	
	public static String TABLE_CREATE = "create table " +
			"soso_versioninfo" +//���� sosoconfigurationinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"ismustupdate text,"+//������keyֵ
			"version_number text,"+//�����е�valueֵ
			"url text"+//�������õ�ʱ��
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
