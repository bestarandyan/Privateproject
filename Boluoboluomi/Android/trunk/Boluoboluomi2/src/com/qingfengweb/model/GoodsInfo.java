/**
 * 
 */
package com.qingfengweb.model;
public class GoodsInfo {
	public static final String TableName = "goodsinfo";
	public static final String CreateTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"typeid Integer," +
			"typename varchar(40)," +
			"name text," +
			"price Integer," +
			"imageid varchar(50)," +
			"description text," +
			"storeid Integer," +
			"ranking Integer," +
			"deleted Integer)";
	public String id = "";
	public String typeid = "";
	public String name = "";
	public String price = "";
	public String description = "";
	public String ranking ="";
	public String deleted = "";
	public String imageid = "";
	
	public String getImageid() {
		return imageid;
	}
	public void setImageid(String imageid) {
		this.imageid = imageid;
	}
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
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
