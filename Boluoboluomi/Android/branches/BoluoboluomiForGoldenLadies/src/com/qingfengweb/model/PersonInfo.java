package com.qingfengweb.model;

public class PersonInfo {
	/**
	 * @author 刘星星
	 * 摄影师化妆师信息表
	 * 
	 */
	public static String TableName = "valuationPersons";//被点评的人的表
	public static String TABLE_CREATE = "create table if not exists " +TableName +//表名 appinfo
			"(_id integer primary key autoincrement,"+//自动增长_id
			"id text,"+//点评id
			"employeeid text,"+//工号
			"position integer,"+//职称1摄影师2化妆师
			"name text,"+//姓名
			"level text,"+//职级
			"style text,"+//风格手法
			"photoid text,"+//缩略图
			"declaration text,"+//职业宣言
			"value1 text,"+//当前评分1
			"value2 text,"+//当前评分2
			"value3 text,"+//当前评分3
			"storeid text,"+//当前评分3
			"ranking varchar(10),"+//当前评分3
			"deleted varchar(10)"+//当前评分3
			")";
	
	private String id = "";
	private String position = "";
	private String employeeid = "";
	private String name = "";
	private String level = "";
	private String style = "";
	private String photoid = "";
	private String declaration = "";
	private String value1 = "";
	private String value2 = "";
	private String value3 = "";
	private String ranking = "";
	private String deleted = "";
	
	public String getPhotoid() {
		return photoid;
	}

	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getRanking() {
		return ranking;
	}

	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

	public String getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
	
}
