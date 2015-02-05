package com.qingfengweb.weddingideas.beans;

public class PreferenceBean {
	public static String tableName = "preferences";
	public static String createSql = "create table if not exists "+tableName+"" +
			"(_id Integer," +
			"id text," +
			"a_name text," +
			"s_time text," +
			"e_time text," +
			"set_desc text," +
			"t_pic text," +
			"storeid text," +
			"opt_time text)";
	public String id ="";
	public String storeid = "";
	public String a_name = "";
	public String s_time = "";
	public String e_time = "";
	public String set_desc = "";
	public String t_pic = "";
	public String opt_time = "";
	
	
	public String getStoreid() {
		return storeid;
	}
	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getA_name() {
		return a_name;
	}
	public void setA_name(String a_name) {
		this.a_name = a_name;
	}
	public String getS_time() {
		return s_time;
	}
	public void setS_time(String s_time) {
		this.s_time = s_time;
	}
	public String getE_time() {
		return e_time;
	}
	public void setE_time(String e_time) {
		this.e_time = e_time;
	}
	public String getSet_desc() {
		return set_desc;
	}
	public void setSet_desc(String set_desc) {
		this.set_desc = set_desc;
	}
	public String getT_pic() {
		return t_pic;
	}
	public void setT_pic(String t_pic) {
		this.t_pic = t_pic;
	}
	public String getOpt_time() {
		return opt_time;
	}
	public void setOpt_time(String opt_time) {
		this.opt_time = opt_time;
	}
	
}
