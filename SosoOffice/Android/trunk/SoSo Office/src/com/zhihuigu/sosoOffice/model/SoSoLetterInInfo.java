package com.zhihuigu.sosoOffice.model;


public class SoSoLetterInInfo {
	/**author by Ring
	 * �ż���Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_letterininfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"letterid text,"+//��Ϣid
			"senduserid text,"+//������id
			"sendusername text,"+//������username
			"receiveuserid text,"+//������id
			"receiveusername text,"+//������username
			"senddate text,"+//����ʱ��
			"letterstate integer,"+//˽��״̬0��δ�鿴 1�Ѳ鿴
			"whouserid text,"+//������id
			"title text,"+//����
			"content text"+//����
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
