package com.boluomi.children.model;

public class GrowUpInfo {
	public static String tablename = "growupinfo";//成长经历表
	public static String createTableSQL = "create table if not exists "+tablename+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"nicheng text," +
			"age Integer," +
			"userid text," +
			"remark text)";
	
	public String id = "";
	public String nicheng = "";
	public String age = "";
	public String remark = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNicheng() {
		return nicheng;
	}
	public void setNicheng(String nicheng) {
		this.nicheng = nicheng;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
