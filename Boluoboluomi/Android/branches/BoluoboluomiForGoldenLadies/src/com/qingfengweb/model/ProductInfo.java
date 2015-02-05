package com.qingfengweb.model;

public class ProductInfo {
	/**
	 * author:Ring
	 * 积分商城商品信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"productinfo" +//表名 goodsinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"id text,"+//商品编号
			"parentid text,"+//商品类别编号
			"name text,"+//商品名称
			"price text,"+//商品价格(积分)
			"sdpath text," + // 大图本地sd卡位置
			"thumbsdpath text," + //缩略图 本地sd卡位置
			"description text"+//商品介绍
			")";
	
	
	private String id;
	private String typeid;
	private String name;
	private String price;
	private String description;
	private int ranking;
	private int deleted;
	
	public ProductInfo(){
		setDescription("");
		setId("");
		setName("");
		setPrice("");
		setRanking(0);
		setTypeid("");
		setDeleted(0);
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
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
	
}
