package com.zhihuigu.sosoOffice.model;

public class SoSoOfficeInfo {
	/**author by Ring
	 * 信件信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_officeinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"officeid text,"+//房源id
			"createuserid text,"+//创建人id
			"createusername text,"+//创建人id
			"areaup text,"+//面积上限
			"areadown text,"+//面积下线
			"address text,"+//地址
			"telephone text,"+//联系电话
			"storey text,"+//层高
			"floors integer,"+//楼层
			"priceup text,"+//单价上限
			"pricedown text,"+//单价下限
			"officemc text,"+//房源名称
			"wymanagementfees text,"+//物业管理费
			"officetype integer,"+//房源类型 0纯写字楼，1商住楼2，酒店式公寓，3园区，4商务中心
			"keywords text,"+//房源关键字
			"fycx integer,"+//房源朝向
			"zcyh text,"+//政策优惠
			"tsyh text,"+//特殊优惠
			"fyjj text,"+//房源简介
			"updatedate text,"+//更新时间
			"buildid text,"+//所属楼盘id
			"buildmc text,"+//所属楼盘名称
			"officestate integer,"+//房源状态 :0；未审核，1：审核通过/未租，2：已租3：私信提醒中4：房源导入5：待删除
			"officestatus integer,"+//0：可整租1：可分割2：可合并
			"isprice integer,"+//0：不可以议价，1可以议价
			"offadddate text,"+//添加时间
			"roomrate text,"+//得房率
			"imageid text,"+//房源列表展示图片
			"ispushed text,"+//0未推送，1已推送
			"pushstate integer,"+//推送状态0推送中，1暂停
			"isrent text,"+//0未出租1已出租
			"ischecked text,"+//0为通过，1已通过
			"thumb_sdpath text," + //缩略图 本地sd卡位置
			"nextalertdate text"+//下次提醒时间
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
	private String NextAlertDate;
	private String OfficeState;
	private String FYJJ;
	private String TSYH;
	private String ZCYH;
	private String FYCX;
	private String RoomRate;
	private String WYManagmentFees;
	private String Storey;
	private String Longitude;
	private String Latitude;
	private String IsPushed;
	private String IsRent;
	private String IsChecked;
	private String ShowImageID;
	private int PushState;
	private int IsUsed;
	
	
	public SoSoOfficeInfo(){
		setAddress("");
		setAreaDown("");
		setAreaUp("");
		setBuildID("");
		setBuildMC("");
		setCreateUserID("");
		setCreateUserName("");
		setFloors("");
		setFYCX("");
		setFYJJ("");
		setIsUsed(0);
		setLatitude("");
		setLongitude("");
		setNextAlertDate("");
		setOfficeID("");
		setOfficeMC("");
		setOfficeState("");
		setOfficeType("");
		setPriceDown("");
		setPriceUp("");
		setRoomRate("");
		setStorey("");
		setTelePhone("");
		setTSYH("");
		setWYManagmentFees("");
		setZCYH("");
		setIsPushed("0");
		setIsChecked("0");
		setIsRent("0");
		setShowImageID("");
		setPushState(1);//0推送中，1暂停推送
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
	public String getNextAlertDate() {
		return NextAlertDate;
	}
	public void setNextAlertDate(String nextAlertDate) {
		NextAlertDate = nextAlertDate;
	}
	public String getOfficeState() {
		return OfficeState;
	}
	public void setOfficeState(String officeState) {
		OfficeState = officeState;
	}
	public String getFYJJ() {
		return FYJJ;
	}
	public void setFYJJ(String fYJJ) {
		FYJJ = fYJJ;
	}
	public String getTSYH() {
		return TSYH;
	}
	public void setTSYH(String tSYH) {
		TSYH = tSYH;
	}
	public String getZCYH() {
		return ZCYH;
	}
	public void setZCYH(String zCYH) {
		ZCYH = zCYH;
	}
	public String getFYCX() {
		return FYCX;
	}
	public void setFYCX(String fYCX) {
		FYCX = fYCX;
	}
	public String getWYManagmentFees() {
		return WYManagmentFees;
	}
	public void setWYManagmentFees(String wYManagmentFees) {
		WYManagmentFees = wYManagmentFees;
	}
	public String getStorey() {
		return Storey;
	}
	public void setStorey(String storey) {
		Storey = storey;
	}
	public String getRoomRate() {
		return RoomRate;
	}
	public void setRoomRate(String roomRate) {
		RoomRate = roomRate;
	}
	public int getIsUsed() {
		return IsUsed;
	}
	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getIsPushed() {
		return IsPushed;
	}
	public void setIsPushed(String isPushed) {
		IsPushed = isPushed;
	}
	public String getIsRent() {
		return IsRent;
	}
	public void setIsRent(String isRent) {
		IsRent = isRent;
	}
	public String getIsChecked() {
		return IsChecked;
	}
	public void setIsChecked(String isChecked) {
		IsChecked = isChecked;
	}
	public String getShowImageID() {
		return ShowImageID;
	}
	public void setShowImageID(String showImageID) {
		ShowImageID = showImageID;
	}
	public int getPushState() {
		return PushState;
	}
	public void setPushState(int pushState) {
		PushState = pushState;
	}
	


	
	
}
