package com.chinaLife.claimAssistant.bean;

public class sc_MessageInfo {
	/**
	 * 及时通信信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"messageinfo" +//表名messageinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"messageid integer,"+//消息编号
			"claimid text,"+//理赔任务编号
			"sender integer,"+//发送方：1-客户2-客服3-管理员
			"content text,"+//消息内容
			"create_time text,"+//创建时间
			"status integer"+//消息的状态：1未读0已读
			")";
	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS messageinfo";
	
	private String claimid;
	private int messageid;
	private String content;
	private String createtime;
	private int status;
	private int sender;
	public String getClaimid() {
		return claimid;
	}
	public void setClaimid(String claimid) {
		this.claimid = claimid;
	}
	
	public int getMessageid() {
		return messageid;
	}
	public void setMessageid(int messageid) {
		this.messageid = messageid;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSender() {
		return sender;
	}
	public void setSender(int sender) {
		this.sender = sender;
	}
	
	
}
