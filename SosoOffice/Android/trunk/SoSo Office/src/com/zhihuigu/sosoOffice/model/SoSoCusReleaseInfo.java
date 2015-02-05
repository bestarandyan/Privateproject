package com.zhihuigu.sosoOffice.model;

public class SoSoCusReleaseInfo {
	/**author by Ring
	 * �ͻ�������Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_cusreleaseinfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"releaseid text,"+//����id
			"userid text,"+//������id
			"username integer,"+//�������û���
			"title integer,"+//�������
			"tele text,"+//�����˵ĵ绰
			"contact text,"+//��������ϵ��
			"areaup text,"+//�������
			"areadown text,"+//�������
			"priceup text,"+//�۸�����
			"pricedown text,"+//�۸�����
			"description text,"+//��������
			"officetype text,"+//��Դ����
			"unit text"+//��λ
			")";
	
	
	
	private String ReleaseID;
	private String UserID;
	private String UserName;
	private String Title;
	private String Tele;
	private String Contact;
	private String AreaUp;
	private String AreaDown;
	private String PriceUp;
	private String PriceDown;
	private String Unit;
	private String Description;
	private String OfficeType;
	private int IsUsed;

	public SoSoCusReleaseInfo(){
		setAreaDown("");
		setAreaUp("");
		setContact("");
		setDescription("");
		setIsUsed(0);
		setOfficeType("");
		setPriceDown("");
		setPriceUp("");
		setReleaseID("");
		setTele("");
		setTitle("");
		setUnit("");
		setUserID("");
		setUserName("");
	}

	public String getReleaseID() {
		return ReleaseID;
	}

	public void setReleaseID(String releaseID) {
		ReleaseID = releaseID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getTele() {
		return Tele;
	}

	public void setTele(String tele) {
		Tele = tele;
	}

	public String getContact() {
		return Contact;
	}

	public void setContact(String contact) {
		Contact = contact;
	}

	public String getAreaUp() {
		return AreaUp;
	}

	public void setAreaUp(String areaUp) {
		AreaUp = areaUp;
	}

	public String getAreaDown() {
		return AreaDown;
	}

	public void setAreaDown(String areaDown) {
		AreaDown = areaDown;
	}

	public String getPriceUp() {
		return PriceUp;
	}

	public void setPriceUp(String priceUp) {
		PriceUp = priceUp;
	}

	public String getPriceDown() {
		return PriceDown;
	}

	public void setPriceDown(String priceDown) {
		PriceDown = priceDown;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getIsUsed() {
		return IsUsed;
	}

	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}

	public String getOfficeType() {
		return OfficeType;
	}

	public void setOfficeType(String officeType) {
		OfficeType = officeType;
	}
	
	

	
	
	
	
	
}
