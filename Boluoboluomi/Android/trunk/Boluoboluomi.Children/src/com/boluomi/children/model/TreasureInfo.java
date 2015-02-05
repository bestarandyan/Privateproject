/**
 * 
 */
package com.boluomi.children.model;

/**
 * @author 刘星星
 * @createDate 2013/9/16
 *
 */
public class TreasureInfo {
	public static final String TableName = "treasureinfos";//百宝箱表名
	public static final String CreateDBSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"name varchar(30)," +
			"icon varchar(30)," +
			"ranking Integer," +
			"number Integer," +
			"storeid varchar(30)," +
			"deleted varchar(10))";
	
	public String id = "";
	public String name = "";
	public String icon = "";
	public String ranking = "";
	public String number = "";
	public String deleted  = "";
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
	
	
	
}
