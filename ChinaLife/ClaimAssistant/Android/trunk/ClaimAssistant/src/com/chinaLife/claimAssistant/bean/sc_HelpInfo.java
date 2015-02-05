package com.chinaLife.claimAssistant.bean;

public class sc_HelpInfo {
	/**
	 * 帮助文章
	 */
	public static String TABLE_CREATE = "create table " + "helpinfo" + // 表名helpinfo
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"helpid text," + // 通知编号
			"title text," + // 案件编号
			"content text," + // 消息内容
			"ranking integer" + // 创建时间
			")";
	
	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS helpinfo";
	

	private String helpid;
	private String title;
	private String content;

	public String getHelpid() {
		return helpid;
	}

	public void setHelpid(String helpid) {
		this.helpid = helpid;
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

}
