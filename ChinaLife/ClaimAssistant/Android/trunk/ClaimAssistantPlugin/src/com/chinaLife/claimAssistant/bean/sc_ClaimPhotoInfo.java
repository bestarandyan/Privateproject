package com.chinaLife.claimAssistant.bean;

public class sc_ClaimPhotoInfo {
	/**
	 * 理赔图片信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"claimphotoinfo" +//表名claimphotoinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"photoid text,"+//照片编号
			"claimid text,"+//理赔任务编号
			"legendid text,"+//图例编号
			"uploadid text,"+//上传编号
			"filename text,"+//访问路径
			"review_result integer,"+//-1 已拍摄，审核结果：1-合格2-不合格,3-已上传未审核
			"review_reason text,"+//不合格的原因
			"type integer"+//照片类型：1-现场拍照2-补全单证
			")";
	
	
	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS claimphotoinfo";
	
	
	
	private String photoid;
	private int status;
	private int type;
	private String legendid;
	private String filename;
	private String savepath;
	private String reviewreason;
	
	
	public String getLegendid() {
		return legendid;
	}
	public void setLegendid(String legendid) {
		this.legendid = legendid;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getSavepath() {
		return savepath;
	}
	public void setSavepath(String savepath) {
		this.savepath = savepath;
	}
	public String getReviewreason() {
		return reviewreason;
	}
	public void setReviewreason(String reviewreason) {
		this.reviewreason = reviewreason;
	}
	public String getPhotoid() {
		return photoid;
	}
	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
