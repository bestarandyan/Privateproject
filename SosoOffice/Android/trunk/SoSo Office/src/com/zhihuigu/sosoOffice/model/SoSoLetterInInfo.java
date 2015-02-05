package com.zhihuigu.sosoOffice.model;


public class SoSoLetterInInfo {
	/**author by Ring
	 * 信件信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_letterininfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"letterid text,"+//信息id
			"senduserid text,"+//发送者id
			"sendusername text,"+//发送者username
			"receiveuserid text,"+//接受者id
			"receiveusername text,"+//接受者username
			"senddate text,"+//发送时间
			"letterstate integer,"+//私信状态0，未查看 1已查看
			"whouserid text,"+//归属人id
			"title text,"+//标题
			"content text"+//内容
			")";
	
	
	private String LetterId;
	private String SendUserId;
	private String SendUserName;
	private String ReceiveUserName;
	private String ReceiveUserid;
	private String SendDate;
	private int LetterState;
	private String WhoUserID;
	private String Title;
	private String Content;
	private int IsUsed;
	
	public SoSoLetterInInfo(){
		setContent("");
		setLetterId("");
		setLetterState(0);
		setReceiveUserid("");
		setSendDate("");
		setSendUserId("");
		setReceiveUserName("");
		setTitle("");
		setWhoUserID("");
		setIsUsed(0);
		setSendUserName("");
	}

	public String getLetterId() {
		return LetterId;
	}

	public void setLetterId(String letterId) {
		LetterId = letterId;
	}

	public String getSendUserId() {
		return SendUserId;
	}

	public void setSendUserId(String sendUserId) {
		SendUserId = sendUserId;
	}

	public String getReceiveUserName() {
		return ReceiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		ReceiveUserName = receiveUserName;
	}

	public String getReceiveUserid() {
		return ReceiveUserid;
	}

	public void setReceiveUserid(String receiveUserid) {
		ReceiveUserid = receiveUserid;
	}

	public String getSendDate() {
		return SendDate;
	}

	public void setSendDate(String sendDate) {
		SendDate = sendDate;
	}

	public int getLetterState() {
		return LetterState;
	}

	public void setLetterState(int letterState) {
		LetterState = letterState;
	}

	public String getWhoUserID() {
		return WhoUserID;
	}

	public void setWhoUserID(String whoUserID) {
		WhoUserID = whoUserID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public int getIsUsed() {
		return IsUsed;
	}

	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}

	public String getSendUserName() {
		return SendUserName;
	}

	public void setSendUserName(String sendUserName) {
		SendUserName = sendUserName;
	}
	

	
}
