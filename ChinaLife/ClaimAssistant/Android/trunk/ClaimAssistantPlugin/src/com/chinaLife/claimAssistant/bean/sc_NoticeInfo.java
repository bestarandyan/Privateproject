package com.chinaLife.claimAssistant.bean;

public class sc_NoticeInfo {
	/**
	 * 通知信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"noticeinfo" +//表名noticeinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"noticeid text,"+//通知编号
			"caseid text,"+//案件编号
			"claimid text,"+//理赔编号
			"content text,"+//消息内容
			"status text,"+//消息状态
			"claimstatus integer,"+//理赔状态
			"create_time text"+//创建时间
			")";
	
	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS noticeinfo";
	
	
	private String noticeid;
	private String claimid;
	private String content;
	private String createtime;
	private String status;
	private int claimstatus;
	
	public int getClaimstatus() {
		return claimstatus;
	}
	public void setClaimstatus(int claimstatus) {
		this.claimstatus = claimstatus;
	}
	public String getNoticeid() {
		return noticeid;
	}
	public void setNoticeid(String noticeid) {
		this.noticeid = noticeid;
	}
	public String getClaimid() {
		return claimid;
	}
	public void setClaimid(String claimid) {
		this.claimid = claimid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
