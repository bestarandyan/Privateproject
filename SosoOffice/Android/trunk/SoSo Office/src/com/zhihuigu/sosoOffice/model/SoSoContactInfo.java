package com.zhihuigu.sosoOffice.model;

public class SoSoContactInfo {

	/***
	 * author By Ring
	 */
	// SQL Command for creating the table
	public static String TABLE_CREATE = "create table sosocontactinfo("
			+ "_id integer primary key autoincrement," + 
			"contactid text," + // 联系人id
			"contactuserid text," + // 联系人用户id
			"contactroleid text," + // 联系人用户角色id
			"userid text,"+//用户id
			"username text,"+//用户名
			"realname text," + // 真是姓名
			"headimage text," + // 用户图像网络位置
			"headimagesd text," + // 用户图像sd卡位置
			"dictid text," + // 分组id
			"adddate text," + // 添加时间
			"sex integer," + // 性别1男2女
			"birthday text," + // 出生日期
			"company text," + // 公司名称
			"isused text" + // 是否删除，1删除
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
