/**
 * 
 */
package com.boluomi.children.model;

/**
 * @author 刘星星
 * @createDate 2013、10、14
 * 百宝箱商品列表
 *
 */
public class TreasureProductInfo {
	public static final String TableName = "treasure_product";//产品列表产品表名
	public static final String CreateTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"storeid Integer," +
			"typeid Integer," +
			"providerid Integer," +
			"name text," +
			"imageid Integer," +
			"summary text," +
			"description text," +
			"couponid Integer," +
			"deleted varchar(10))";
	
	public String id = "";
	public String typeid = "";
	public String providerid = "";
	public String name = "";
	public String imageid = "";
	public String summary = "";
	public String description = "";
	public String couponid = "";
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
	public String getProviderid() {
		return providerid;
	}
	public void setProviderid(String providerid) {
		this.providerid = providerid;
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
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCouponid() {
		return couponid;
	}
	public void setCouponid(String couponid) {
		this.couponid = couponid;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
}
