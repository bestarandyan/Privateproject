package com.qingfengweb.model;

public class OwnerPhotoInfo {
	/**
	 * author:Ring 
	 * 我的照片信息表
	 */
	public static String TABLE_CREATE = 
			"create table " 
	+ "ownerphotoinfo" 
					+ // 表名
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"id text," + // 照片编号
			"name text," + // 照片名称
			"userid," + // 用户id
			"sdpath text," + // 大图本地sd卡位置
			"thumb_sdpath text" + // 缩略图 本地sd卡位置
			")";

	private String id;
	private String name;
	private int deleted;

	public OwnerPhotoInfo() {
		setId("");
		setName("");
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

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

}
