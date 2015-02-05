/**
 * 
 */
package com.qingfengweb.model;

/**
 * @author 刘星星
 * @createDate 2013/9/16
 *
 */
public class IntegralTypeInfo {
	public static final String TableName = "integraltypeinfo";
	public static final String CreateTabSQL = "create table if not exists "+TableName +"" +
			"(_id Integer primary key autoincrement," +
			"typeid Integer," +
			"name varchar(30)," +
			"ranking varchar(10)," +
			"deleted varchar(10)," +
			"storeid Integer)";
	
	public String typeid = "";
	public String name = "";
	public String ranking = "";
	public String deleted = "";
	public String storeid = "";
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
	
	
}
