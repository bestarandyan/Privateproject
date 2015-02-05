package com.zhihuigu.sosoOffice.model;

public class SoSoPushInfo {
	/**author by Ring
	 * ������Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_pushinfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"pushid text,"+//����id
			"pushdate text,"+//��������
			"pushtarget integer,"+//����Ŀ��1��վ��2�н�
			"pushstate integer,"+//����״̬0�ɹ���1ʧ��
			"reviceuserid text,"+//�����Ƽ����û�id
			"userid text,"+//���͵��û�id
			"officeid integer,"+//��Դid
			"officemc text,"+//��Դ����
			"officestate text,"+//��Դ״̬
			"username text,"+//�û���
			"areaup text,"+//�������
			"areadown text,"+//�������
			"address text,"+//��Դ��ַ
			"telephone text,"+//��ϵ�÷�Դ�ĵ绰
			"priceup text,"+//�۸�����
			"pricedown text,"+//�۸�����
			"imageid text,"+//��Դ�б�չʾͼƬ
			"ispushed text,"+//0δ���ͣ�1������
			"isrent text,"+//0δ����1�ѳ���
			"ischecked text,"+//0Ϊͨ����1��ͨ��
			"pushtag integer,"+//0�������飬1��Ч����֤������б�
			"thumb_sdpath text," + //����ͼ ����sd��λ��
			"buildid text,"+//¥��id
			"buildmc text"+//¥������
			")";
	
	
	
	private String OfficeID;
	private String OfficeMC;
	private String UserID;
	private String UserName;
	private String AreaUp;
	private String AreaDown;
	private String Address;
	private String TelePhone;
	private String PriceUp;
	private String Pricedown;
	private String BuildID;
	private String BuildMC;
	private String PushID;
	private String ReviceUserID;
	private int PushTarget;
	private String PushDate;
	private int PushState;
	private int IsUsed;
	private int OfficeState;
	private String ShowImageID;
	public SoSoPushInfo(){
		setAddress("");
		setAreaDown("");
		setAreaUp("");
		setBuildMC("");
		setBuildID("");
		setOfficeID("");
		setOfficeMC("");
		setPricedown("");
		setPriceUp("");
		setPushDate("");
		setPushID("");
		setPushState(0);
		setPushTarget(1);
		setReviceUserID("");
		setTelePhone("");
		setUserID("");
		setUserName("");
		setIsUsed(0);
		setOfficeState(2);
		setShowImageID("");
	}
	public String getOfficeID() {
		return OfficeID;
	}
	public void setOfficeID(String officeID) {
		OfficeID = officeID;
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
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getTelePhone() {
		return TelePhone;
	}
	public void setTelePhone(String telePhone) {
		TelePhone = telePhone;
	}
	public String getPriceUp() {
		return PriceUp;
	}
	public void setPriceUp(String priceUp) {
		PriceUp = priceUp;
	}
	public String getPricedown() {
		return Pricedown;
	}
	public void setPricedown(String pricedown) {
		Pricedown = pricedown;
	}
	public String getBuildMC() {
		return BuildMC;
	}
	public void setBuildMC(String buildMC) {
		BuildMC = buildMC;
	}
	public String getPushID() {
		return PushID;
	}
	public void setPushID(String pushID) {
		PushID = pushID;
	}
	public String getReviceUserID() {
		return ReviceUserID;
	}
	public void setReviceUserID(String reviceUserID) {
		ReviceUserID = reviceUserID;
	}
	public int getPushTarget() {
		return PushTarget;
	}
	public void setPushTarget(int pushTarget) {
		PushTarget = pushTarget;
	}
	public String getPushDate() {
		return PushDate;
	}
	public void setPushDate(String pushDate) {
		PushDate = pushDate;
	}
	public int getPushState() {
		return PushState;
	}
	public void setPushState(int pushState) {
		PushState = pushState;
	}
	public int getIsUsed() {
		return IsUsed;
	}
	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}
	public int getOfficeState() {
		return OfficeState;
	}
	public void setOfficeState(int officeState) {
		OfficeState = officeState;
	}
	public String getShowImageID() {
		return ShowImageID;
	}
	public void setShowImageID(String showImageID) {
		ShowImageID = showImageID;
	}
	public String getOfficeMC() {
		return OfficeMC;
	}
	public void setOfficeMC(String officeMC) {
		OfficeMC = officeMC;
	}
	public String getBuildID() {
		return BuildID;
	}
	public void setBuildID(String buildID) {
		BuildID = buildID;
	}
	

	
	
	
	
	
}
