package com.zhihuigu.sosoOffice.model;

public class SoSoImageInfo {
	/**
	 * @author Ring
	 * @since 2013-01-26 14:00
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_imageinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"imageid text,"+//图片id
			"xgid text,"+//关联id，此处是房源id
			"datuurl text,"+//大图网络位置
			"datusd text,"+//大图sd卡位置
			"xiaotuurl text,"+//缩略图网络位置
			"xiaotusd text,"+//缩略图sd卡位置
			"imagetype integer"+//图片类型
			")";
	
	private String ImageID;
	private String XgID;
	private String Url;
	private int ImageType;
	private int IsUsed;
	
	
	public SoSoImageInfo(){
		setImageID("");
		setImageType(0);
		setIsUsed(0);
		setUrl("");
		setXgID("");
	}


	public String getImageID() {
		return ImageID;
	}


	public void setImageID(String imageID) {
		ImageID = imageID;
	}


	public String getXgID() {
		return XgID;
	}


	public void setXgID(String xgID) {
		XgID = xgID;
	}


	public String getUrl() {
		return Url;
	}


	public void setUrl(String url) {
		Url = url;
	}


	public int getImageType() {
		return ImageType;
	}


	public void setImageType(int imageType) {
		ImageType = imageType;
	}


	public int getIsUsed() {
		return IsUsed;
	}


	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}
	
	
	
}
