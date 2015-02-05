package com.zhihuigu.sosoOffice.model;

public class SoSoFavoriteInfo {
	/**author by Ring
	 * �ղ���Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_favoriteinfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"favoriteid text,"+//�ղ�id
			"userid text,"+//�û�id
			"username text,"+//�û�����
			"officeid text,"+//��Դid
			"officemc text,"+//��Դ����
			"officestate text,"+//��Դ״̬
			"type integer,"+//�ղ�����1����Դ�ղأ�2���û��ղ�
			"groupid text,"+//����id
			"adddate text,"+//�ղ�����
			"oprice text,"+//��Դ�۸�
			"oarea text,"+//��Դ���
			"buildmc text,"+//¥������
			"buildid text,"+//¥��id
			"imageid text,"+//��Դ�б�չʾͼƬ
			"ispushed text,"+//0δ���ͣ�1������
			"isrent text,"+//0δ����1�ѳ���
			"ischecked text,"+//0Ϊͨ����1��ͨ��
			"thumb_sdpath text," + //����ͼ ����sd��λ��
			"ocreateuserid text,"+//��Դ�����˵�id
			"ocreateusername text,"+//��Դ�����˵�username
			"ocreateuserphone text"+//��Դ�����˵ĵ绰
			")";
	
	
	
	private String FavId;
	private String UserId;
	private String UserName;
	private String OfficeId;
	private String OfficeMc;
	private int Type;
	private String Adddate;
	private String OCreateUserID;
	private String OCreateUserName;
	private String OCreateUserPhone;
	private String OPrice;
	private String OArea;
	private String BuildMC;
	private String BuildID;
	private int IsUsed;
	private int OfficeState;
	private String ShowImageID;
	
	public SoSoFavoriteInfo(){
		setAdddate("");
		setBuildID("");
		setBuildMC("");
		setFavId("");
		setIsUsed(0);
		setOArea("");
		setOCreateUserID("");
		setOCreateUserPhone("");
		setOfficeId("");
		setOfficeMc("");
		setOPrice("");
		setType(1);
		setUserId("");
		setUserName("");
		setOfficeState(0);
		setShowImageID("");
		setOCreateUserName("");
	}
	
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getOfficeMc() {
		return OfficeMc;
	}
	public void setOfficeMc(String officeMc) {
		OfficeMc = officeMc;
	}
	public String getAdddate() {
		return Adddate;
	}
	public void setAdddate(String adddate) {
		Adddate = adddate;
	}
	public String getOCreateUserID() {
		return OCreateUserID;
	}
	public void setOCreateUserID(String oCreateUserID) {
		OCreateUserID = oCreateUserID;
	}
	public String getOCreateUserPhone() {
		return OCreateUserPhone;
	}
	public void setOCreateUserPhone(String oCreateUserPhone) {
		OCreateUserPhone = oCreateUserPhone;
	}
	public String getOPrice() {
		return OPrice;
	}
	public void setOPrice(String oPrice) {
		OPrice = oPrice;
	}
	public String getOArea() {
		return OArea;
	}
	public void setOArea(String oArea) {
		OArea = oArea;
	}
	public String getBuildMC() {
		return BuildMC;
	}
	public void setBuildMC(String buildMC) {
		BuildMC = buildMC;
	}
	public String getBuildID() {
		return BuildID;
	}
	public void setBuildID(String buildID) {
		BuildID = buildID;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public int getIsUsed() {
		return IsUsed;
	}

	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}

	public String getFavId() {
		return FavId;
	}

	public void setFavId(String favId) {
		FavId = favId;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getOfficeId() {
		return OfficeId;
	}

	public void setOfficeId(String officeId) {
		OfficeId = officeId;
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

	public String getOCreateUserName() {
		return OCreateUserName;
	}

	public void setOCreateUserName(String oCreateUserName) {
		OCreateUserName = oCreateUserName;
	}
	
	
	
	
	
}
