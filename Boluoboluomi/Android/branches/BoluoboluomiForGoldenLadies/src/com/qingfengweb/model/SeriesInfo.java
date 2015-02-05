package com.qingfengweb.model;

public class SeriesInfo {
	/**
	 * author:Ring
	 * 美图欣赏信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"seriesinfo" +//表名 picturesinfo
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"id text," + // 照片id
			"name text," + // 照片名称
			"price text," + // 价格
			"promotesales text," + // 促销价格
			"sdpath text," + // 大图本地sd卡位置
			"thumbsdpath text," + //缩略图 本地sd卡位置
			"description text" + // 照片描述
			")";
	
	private String id;
	private String albumid;
	private String name;
	private int ranking;
	private String description;
	private int deleted;
	
	public SeriesInfo(){
		setAlbumid("");
		setDescription("");
		setId("");
		setName("");
		setRanking(0);
		setDeleted(0);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlbumid() {
		return albumid;
	}

	public void setAlbumid(String albumid) {
		this.albumid = albumid;
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
