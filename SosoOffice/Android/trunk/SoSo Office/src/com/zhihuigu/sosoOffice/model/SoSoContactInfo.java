package com.zhihuigu.sosoOffice.model;

public class SoSoContactInfo {

	/***
	 * author By Ring
	 */
	// SQL Command for creating the table
	public static String TABLE_CREATE = "create table sosocontactinfo("
			+ "_id integer primary key autoincrement," + 
			"contactid text," + // ��ϵ��id
			"contactuserid text," + // ��ϵ���û�id
			"contactroleid text," + // ��ϵ���û���ɫid
			"userid text,"+//�û�id
			"username text,"+//�û���
			"realname text," + // ��������
			"headimage text," + // �û�ͼ������λ��
			"headimagesd text," + // �û�ͼ��sd��λ��
			"dictid text," + // ����id
			"adddate text," + // ���ʱ��
			"sex integer," + // �Ա�1��2Ů
			"birthday text," + // ��������
			"company text," + // ��˾����
			"isused text" + // �Ƿ�ɾ����1ɾ��
			")";
	private String ContactID;
	private String UserID;
	private String ContactUserID;
	private String UserName;
	private String RealName;
	private String HeadImage;
	private String DictID;
	private String Adddate;
	private int IsUsed;
	private int RoleID;
	private int Sex;
	private String Birthday;
	private String Company;
	
	public SoSoContactInfo(){
		setAdddate("");
		setContactID("");
		setContactUserID("");
		setDictID("");
		setHeadImage("");
		setIsUsed(0);
		setRealName("");
		setUserID("");
		setUserName("");
		setRoleID(0);
		setSex(1);
		setBirthday("");
		setCompany("");
	}
	public String getContactID() {
		return ContactID;
	}
	public void setContactID(String contactID) {
		ContactID = contactID;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getContactUserID() {
		return ContactUserID;
	}
	public void setContactUserID(String contactUserID) {
		ContactUserID = contactUserID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getRealName() {
		return RealName;
	}
	public void setRealName(String realName) {
		RealName = realName;
	}
	public String getHeadImage() {
		return HeadImage;
	}
	public void setHeadImage(String headImage) {
		HeadImage = headImage;
	}
	public String getDictID() {
		return DictID;
	}
	public void setDictID(String dictID) {
		DictID = dictID;
	}
	public String getAdddate() {
		return Adddate;
	}
	public void setAdddate(String adddate) {
		Adddate = adddate;
	}
	public int getIsUsed() {
		return IsUsed;
	}
	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}
	public int getRoleID() {
		return RoleID;
	}
	public void setRoleID(int roleID) {
		RoleID = roleID;
	}
	public int getSex() {
		return Sex;
	}
	public void setSex(int sex) {
		Sex = sex;
	}
	public String getBirthday() {
		return Birthday;
	}
	public void setBirthday(String birthday) {
		Birthday = birthday;
	}
	public String getCompany() {
		return Company;
	}
	public void setCompany(String company) {
		Company = company;
	}
	

	

}
