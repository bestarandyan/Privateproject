package com.zhihuigu.sosoOffice.model;

public class SoSoZoneInfo {
	/**author by Ring
	 * 省市区信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_zoneinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"id text,"+//id
			"name text,"+//名称
			"typeid text,"+//1省份，2城市，3区域
			"parentid text"+//父id
			")";
	
	
	private String id;
	private String name;
	private String typeid;
	private String parentid;
	
	public SoSoZoneInfo(){
		setId("");
		setName("");
		setParentid("");
		setTypeid("");
	}

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

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	
	
	
}
