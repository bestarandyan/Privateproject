package com.zhihuigu.sosoOffice.model;

public class SoSoPushInfo {
	/**author by Ring
	 * 推送信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_pushinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"pushid text,"+//推送id
			"pushdate text,"+//推送日期
			"pushtarget integer,"+//推送目标1网站，2中介
			"pushstate integer,"+//推送状态0成功，1失败
			"reviceuserid text,"+//接受推荐的用户id
			"userid text,"+//推送的用户id
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
			"pushtag integer,"+//0推送详情，1有效性验证里面的列表
			"thumb_sdpath text," + //缩略图 本地sd卡位置
			"buildid text,"+//楼盘id
			"buildmc text"+//楼盘名称
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
