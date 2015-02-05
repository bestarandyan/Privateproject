/**
 * 
 */
package com.qingfengweb.model;

/**
 * @author 刘星星
 * @createDate 2013、9、16
 * 商家列表实体类
 *
 */
public class MerchanInfo {
	public static final String TableName = "merchanInfo";
	public static final String CreateTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"typeid Integer," +
			"name text," +
			"logo varchar(30)," +
			"storeid varchar(30)," +
			"description text," +
			"deleted varchar(10))";
	
	public String id = "";
	public String typeid = "";
	public String name = "";
	public String logo = "";
	public String description = "";
	public String deleted = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
	
	
}
