/**
 * 
 */
package com.qingfengweb.model;

/**
 * @author 刘星星
 * @createDate 2013、10、16
 * 推荐套系实体类
 *
 */
public class RecommendInfo {
	public static final String TableName = "recommendSeries";//推荐套系表名
	public static final String CreateTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"name text," +
			"price1 varchar(40)," +
			"price2 varchar(40)," +
			"imageid Integer," +
			"description text," +
			"storeid Integer," +
			"ranking Integer," +
			"deleted Integer)";
	public String id = "";
	public String name = "";
	public String price1 = "";
	public String price2 = "";
	public String imageid = "";
	public String description = "";
	public String ranking ="";
	public String deleted = "";
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
	public String getPrice1() {
		return price1;
	}
	public void setPrice1(String price1) {
		this.price1 = price1;
	}
	public String getPrice2() {
		return price2;
	}
	public void setPrice2(String price2) {
		this.price2 = price2;
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
