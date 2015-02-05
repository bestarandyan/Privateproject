package com.zhihuigu.sosoOffice.model;


public class SoSoHelpInfo {
	/**author by Ring
	 * 信件信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_helpinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"helpid text,"+//帮助id
			"helptitle text,"+//帮助title
			"helptext text"+//帮助文本
			")";
	
	
	private String HelpId;
	private String HelpTitle;
	private String HelpText;
	private int IsUsed;
	
	public SoSoHelpInfo(){
		setHelpId("");
		setHelpTitle("");
		setHelpText("");
		setIsUsed(0);
	}

	public String getHelpId() {
		return HelpId;
	}

	public void setHelpId(String helpId) {
		HelpId = helpId;
	}

	public String getHelpTitle() {
		return HelpTitle;
	}

	public void setHelpTitle(String helpTitle) {
		HelpTitle = helpTitle;
	}

	public int getIsUsed() {
		return IsUsed;
	}

	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}

	public String getHelpText() {
		return HelpText;
	}

	public void setHelpText(String helpText) {
		HelpText = helpText;
	}
	
}
