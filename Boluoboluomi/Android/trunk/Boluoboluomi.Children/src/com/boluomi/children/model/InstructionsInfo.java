package com.boluomi.children.model;

public class InstructionsInfo {
	/**
	 * author:Ring
	 * 使用说明信息表
	 */
	public static String TABLE_CREATE = "create table " + "instructionsinfo" + // 表名helpinfo
			"(" + 
			"_id integer primary key autoincrement," + // 自动增长_id
			"instructionid text," + // 说明编号
			"type integer,"+//帮助类型，枚举值 1,系统帮助2积分商城3点评帮助4定制帮助5电子请帖6查件7二维码
			"title text," + // 说明title
			"content text," + // 说明内容
			"ranking integer,"+//帮助排序
			"createtime text"+//添加时间
			")";
	
	private String id;
	private int type;
	private String title;
	private String content;
	private int ranking;
	private String createtime;
	private int deleted;
	public InstructionsInfo(){
		setContent("");
		setCreatetime("");
		setId("");
		setRanking(0);
		setTitle("");
		setType(0);
		setDeleted(0);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
}
