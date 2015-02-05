/**
 * 
 */
package com.qingfengweb.weddingideas.beans;

/**
 * @author 刘星星
 * @createDate 2014、1、11
 * 活动实体类
 *
 */
public class HuoDongBean {
	public static String tbName="huodongdata";
	public static String tbCreateSql = "create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"id text," +
			"storeid text," +
			"a_name text," +
			"s_time text," +
			"e_time text," +
			"set_desc text," +
			"pic_active text," +
			"opt_time text," +
			"opt_status varchar(10))";
	public String id="";
	public String a_name ="";
	public String s_time ="";
	public String e_time ="";
	public String set_desc ="";
	public String opt_time ="";
	public String opt_status ="";
	public String pic_active = "";
	
	public String getPic_active() {
		return pic_active;
	}
	public void setPic_active(String pic_active) {
		this.pic_active = pic_active;
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
	public String getOpt_time() {
		return opt_time;
	}
	public void setOpt_time(String opt_time) {
		this.opt_time = opt_time;
	}
	public String getOpt_status() {
		return opt_status;
	}
	public void setOpt_status(String opt_status) {
		this.opt_status = opt_status;
	}
	
	
}
