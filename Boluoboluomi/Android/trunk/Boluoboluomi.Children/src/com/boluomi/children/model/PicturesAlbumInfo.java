package com.boluomi.children.model;

public class PicturesAlbumInfo {
	/**
	 * author:Ring
	 * 美图欣赏套图信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"picturesalbuminfo" +//表名 picturesalbuminfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"id text,"+//相册编号
			"name text,"+//相册名称
			"description text,"+//相册描述
			"sdpath text," + //大图本地sd卡位置
			"thumbsdpath text," + //缩略图本地sd卡位置
			"value1 integer,"+//评分1
			"value2 integer,"+//评分2
			"value3 integer"+//评分3
			")";
	
	private String id;
	private String name;
	private String description;
	private int ranking;
	private int deleted;
	
	public PicturesAlbumInfo(){
		setId("");
		setName("");
		setRanking(0);
		setDescription("");
		setDeleted(0);
	}
	
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
	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	
	
}
