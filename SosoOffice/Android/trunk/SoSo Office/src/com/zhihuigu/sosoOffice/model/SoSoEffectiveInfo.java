package com.zhihuigu.sosoOffice.model;

public class SoSoEffectiveInfo {
	/**author by Ring
	 * 信件信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_effectiveinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"createuserid text,"+//创建人id
			"createusername text,"+//创建人id
			"officeid integer,"+//房源id
			"officemc text,"+//房源名称
			"officestate text,"+//房源状态
			"username text,"+//用户名
			"areaup text,"+//面积上限
			"areadown text,"+//面积下线
			"address text,"+//房源地址
			"telephone text,"+//联系该房源的电话
			"priceup text,"+//价格上限
			"pricedown text,"+//价格下限
			"imageid text,"+//房源列表展示图片
			"ispushed text,"+//0未推送，1已推送
			"isrent text,"+//0未出租1已出租
			"ischecked text,"+//0为通过，1已通过
			"thumb_sdpath text," + //缩略图 本地sd卡位置
			"buildid text,"+//楼盘id
			"buildmc text"+//楼盘名称
			")";
	
	
	
	private String OfficeID;
	private String CreateUserID;
	private String CreateUserName;
	private String AreaUp;
	private String AreaDown;
	private String Address;
	private String TelePhone;
	private String Floors;
	private String PriceUp;
	private String PriceDown;
	private String OfficeMC;
	private String OfficeType;
	private String BuildID;
	private String BuildMC;	
	private String OfficeState;
	private String ShowImageID;
	private int IsUsed;
	
	
	public SoSoEffectiveInfo(){
		setAddress("");
		setAreaDown("");
		setAreaUp("");
		setBuildID("");
		setBuildMC("");
		setCreateUserID("");
		setCreateUserName("");
		setFloors("");
		setOfficeID("");
		setOfficeMC("");
		setOfficeState("");
		setOfficeType("");
		setPriceDown("");
		setPriceUp("");
		setTelePhone("");
		setIsUsed(0);
		setShowImageID("");
	}


	public String getOfficeID() {
		return OfficeID;
	}


	public void setOfficeID(String officeID) {
		OfficeID = officeID;
	}


	public String getCreateUserID() {
		return CreateUserID;
	}


	public void setCreateUserID(String createUserID) {
		CreateUserID = createUserID;
	}


	public String getCreateUserName() {
		return CreateUserName;
	}


	public void setCreateUserName(String createUserName) {
		CreateUserName = createUserName;
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


	public String getFloors() {
		return Floors;
	}


	public void setFloors(String floors) {
		Floors = floors;
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


	public String getOfficeMC() {
		return OfficeMC;
	}


	public void setOfficeMC(String officeMC) {
		OfficeMC = officeMC;
	}


	public String getOfficeType() {
		return OfficeType;
	}


	public void setOfficeType(String officeType) {
		OfficeType = officeType;
	}


	public String getBuildID() {
		return BuildID;
	}


	public void setBuildID(String buildID) {
		BuildID = buildID;
	}


	public String getBuildMC() {
		return BuildMC;
	}


	public void setBuildMC(String buildMC) {
		BuildMC = buildMC;
	}


	public String getOfficeState() {
		return OfficeState;
	}


	public void setOfficeState(String officeState) {
		OfficeState = officeState;
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
	
	
	
}
