package com.zhihuigu.sosoOffice.model;

public class SoSoFavoriteInfo {
	/**author by Ring
	 * 收藏信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_favoriteinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"favoriteid text,"+//收藏id
			"userid text,"+//用户id
			"username text,"+//用户名称
			"officeid text,"+//房源id
			"officemc text,"+//房源名称
			"officestate text,"+//房源状态
			"type integer,"+//收藏类型1：房源收藏，2：用户收藏
			"groupid text,"+//分组id
			"adddate text,"+//收藏日期
			"oprice text,"+//房源价格
			"oarea text,"+//房源面积
			"buildmc text,"+//楼盘名称
			"buildid text,"+//楼盘id
			"imageid text,"+//房源列表展示图片
			"ispushed text,"+//0未推送，1已推送
			"isrent text,"+//0未出租1已出租
			"ischecked text,"+//0为通过，1已通过
			"thumb_sdpath text," + //缩略图 本地sd卡位置
			"ocreateuserid text,"+//房源所属人的id
			"ocreateusername text,"+//房源所属人的username
			"ocreateuserphone text"+//房源所属人的电话
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
