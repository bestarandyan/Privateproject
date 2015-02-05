package com.zhihuigu.sosoOffice.model;

public class SoSoImageInfo {
	/**
	 * @author Ring
	 * @since 2013-01-26 14:00
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_imageinfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"imageid text,"+//ͼƬid
			"xgid text,"+//����id���˴��Ƿ�Դid
			"datuurl text,"+//��ͼ����λ��
			"datusd text,"+//��ͼsd��λ��
			"xiaotuurl text,"+//����ͼ����λ��
			"xiaotusd text,"+//����ͼsd��λ��
			"imagetype integer"+//ͼƬ����
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
