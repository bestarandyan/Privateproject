/**
 * 
 */
package com.qingfengweb.model;

/**
 * @author 刘星星
 * @createDate 2013、11、1
 *定制类型的实体类
 */
public class CustomTypeInfo {
	public static String TableName = "customType";
	public static String CreateTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"type varchar(10)," +
			"name varchar(30)," +
			"imageid Integer," +
			"description text," +
			"ranking varchar(10)," +
			"storeid varchar(10)," +
			"deleted varchar(10))";
	public String id ="";
	public String type ="";
	public String name ="";
	public String imageid ="";
	public String description ="";
	public String ranking ="";
	public String deleted ="";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageid() {
		return imageid;
	}
	public void setImageid(String imageid) {
		this.imageid = imageid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
