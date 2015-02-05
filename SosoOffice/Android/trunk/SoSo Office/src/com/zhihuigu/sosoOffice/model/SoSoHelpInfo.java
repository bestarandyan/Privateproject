package com.zhihuigu.sosoOffice.model;


public class SoSoHelpInfo {
	/**author by Ring
	 * �ż���Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_helpinfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"helpid text,"+//����id
			"helptitle text,"+//����title
			"helptext text"+//�����ı�
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
