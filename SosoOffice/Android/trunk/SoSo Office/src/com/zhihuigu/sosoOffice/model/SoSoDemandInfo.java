package com.zhihuigu.sosoOffice.model;

public class SoSoDemandInfo {
	/**author by Ring
	 * �ż���Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_demandinfo" +//���� soso_demandinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"officeid text,"+//��Դid
			"createuserid text,"+//�����û���id
			"usertype integer,"+//�û�����1˭�ղ��ҵķ�Դ2˭�����ҵķ�Դ3���������������û�
			"areaup text,"+//�������
			"areadown text,"+//�������
			"address text,"+//��ַ
			"priceup text,"+//��������
			"pricedown text,"+//��������
			"officemc text,"+//��Դ����
			"officetype integer,"+//��Դ���� 0��д��¥��1��ס¥2���Ƶ�ʽ��Ԣ��3԰����4��������
			"buildid text,"+//����¥��id
			"buildmc text,"+//����¥������
			"officestate integer,"+//��Դ״̬ :0��δ��ˣ�1�����ͨ��/δ�⣬2������3��˽��������4����Դ����5����ɾ��
			"username text,"+//�û���
			"userid text,"+//userid
			"roleid text,"+//roleid
			"email text,"+//����
			"imageid text,"+//��Դ�б�չʾͼƬ
			"thumb_sdpath text," + //����ͼ ����sd��λ��
			"telephone text"+//�绰
			")";
	
	
//	{ "OfficeId" : 3 , 
//		"OfficeMC" : "\u6D4B\u8BD5" , 
//		"UserName" : "zhangsan" , 
//		"UserId" : 20 , 
//		"Email" : "471551790@qq.com" , 
//		"TelePhone" : "18662872686" , 
//		"IsUsed" : 0 , "Officestate" : 2 , 
//		"ShowImageID" : 0 , 
//		"AreaUp" : 200 , 
//		"Address" : "\u4E0A\u6D77\u5E02\u5F90\u6C47\u533A" ,
//		"PriceUp" : 5 , 
//		"BuildId" : 1 , 
//		"BuildMC" : "\u9F0E\u5929\u5546\u52A1\u516C\u5BD3" , 
//		"RoleId" : 3 }
	private String OfficeId;
	private String OfficeMC;
	private String UserName;
	private String UserId;
	private String Email;
	private String Telephone;
	private String ShowImageID;
	private String AreaUp;
	private String Address;
	private String PriceUp;
	private String BuildId;
	private String BuildMC;
	private String RoleId;
	
	private int IsUsed;
	
	
	public SoSoDemandInfo(){
		setEmail("");
		setIsUsed(0);
		setOfficeId("");
		setTelephone("");
		setUserId("");
		setUserName("");
		setAddress("");
		setShowImageID("");
		setBuildId("");
		setBuildMC("");
		setAreaUp("");
		setPriceUp("");
		setRoleId("");
		setOfficeMC("");
	}


	public String getOfficeId() {
		return OfficeId;
	}


	public void setOfficeId(String officeId) {
		OfficeId = officeId;
	}


	public String getOfficeMC() {
		return OfficeMC;
	}


	public void setOfficeMC(String officeMC) {
		OfficeMC = officeMC;
	}


	public String getUserName() {
		return UserName;
	}


	public void setUserName(String userName) {
		UserName = userName;
	}


	public String getUserId() {
		return UserId;
	}


	public void setUserId(String userId) {
		UserId = userId;
	}


	public String getEmail() {
		return Email;
	}


	public void setEmail(String email) {
		Email = email;
	}


	public String getTelephone() {
		return Telephone;
	}


	public void setTelephone(String telephone) {
		Telephone = telephone;
	}


	public int getIsUsed() {
		return IsUsed;
	}


	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}


	public String getShowImageID() {
		return ShowImageID;
	}


	public void setShowImageID(String showImageID) {
		ShowImageID = showImageID;
	}


	public String getAreaUp() {
		return AreaUp;
	}


	public void setAreaUp(String areaUp) {
		AreaUp = areaUp;
	}


	public String getAddress() {
		return Address;
	}


	public void setAddress(String address) {
		Address = address;
	}


	public String getPriceUp() {
		return PriceUp;
	}


	public void setPriceUp(String priceUp) {
		PriceUp = priceUp;
	}


	public String getBuildId() {
		return BuildId;
	}


	public void setBuildId(String buildId) {
		BuildId = buildId;
	}


	public String getBuildMC() {
		return BuildMC;
	}


	public void setBuildMC(String buildMC) {
		BuildMC = buildMC;
	}


	public String getRoleId() {
		return RoleId;
	}


	public void setRoleId(String roleId) {
		RoleId = roleId;
	}
	
	
	
	
}
