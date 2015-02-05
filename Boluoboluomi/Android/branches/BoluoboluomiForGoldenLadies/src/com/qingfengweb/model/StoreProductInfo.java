package com.qingfengweb.model;

public class StoreProductInfo {
	/**
	 * author:Ring 
	 * 商家商品详情表
	 */
	public static String TABLE_CREATE = "create table " 
	 
			+ "storeproductinfo" + // 表名
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"id text," + // 详情id
			"partnerid text," + // 合作伙伴编号
			"name text," + // 图片名称
			"url text," + // 图片地址
			"link text,"+//链接
			"description text" + // 文字介绍
			")";

	private String partnerid;
	private String name;
	private String id;
	private String link;
	private String text;
	private int deleted;

	public StoreProductInfo() {
		setName("");
		setPartnerid("");
		setText("");
		setId("");
		setLink("");
		setDeleted(0);
		
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	

}
