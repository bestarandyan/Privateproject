package com.zhihuigu.sosoOffice.model;

public class SoSoDemandInfo {
	/**author by Ring
	 * 信件信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_demandinfo" +//表名 soso_demandinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"officeid text,"+//房源id
			"createuserid text,"+//创建用户的id
			"usertype integer,"+//用户类型1谁收藏我的房源2谁看过我的房源3满足搜索条件的用户
			"areaup text,"+//面积上限
			"areadown text,"+//面积下线
			"address text,"+//地址
			"priceup text,"+//单价上限
			"pricedown text,"+//单价下限
			"officemc text,"+//房源名称
			"officetype integer,"+//房源类型 0纯写字楼，1商住楼2，酒店式公寓，3园区，4商务中心
			"buildid text,"+//所属楼盘id
			"buildmc text,"+//所属楼盘名称
			"officestate integer,"+//房源状态 :0；未审核，1：审核通过/未租，2：已租3：私信提醒中4：房源导入5：待删除
			"username text,"+//用户名
			"userid text,"+//userid
			"roleid text,"+//roleid
			"email text,"+//邮箱
			"imageid text,"+//房源列表展示图片
			"thumb_sdpath text," + //缩略图 本地sd卡位置
			"telephone text"+//电话
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
