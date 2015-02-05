package com.boluomi.children.model;

public class PictureInfo {
	/**
	 * @author 刘星星
	 * 美图欣赏照片列表信息表
	 */
	public static String TableName = "beautyPhotos";
	public static String TABLE_CREATE = "create table if not exists " +TableName+
			"(_id Integer primary key autoincrement," + // 自动增长_id
			"id Integer," + // 照片id
			"name text,"+// 照片名称
			"imageid text," + // 照片id 供下载
			"description text," + // 照片描述
			"ranking Integer," + //排列顺序
			"deleted Integer," + // 是否删除
			"themeid Integer," + // 是否删除
			"storeid Integer" + // 是否删除
			")";
	
	private String id = "";
	private String name = "";
	private String imageid = "";
	private String description = "";
	private String ranking = "";
	private String deleted = "";
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
