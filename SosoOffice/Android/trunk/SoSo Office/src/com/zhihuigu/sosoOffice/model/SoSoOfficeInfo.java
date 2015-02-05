package com.zhihuigu.sosoOffice.model;

public class SoSoOfficeInfo {
	/**author by Ring
	 * �ż���Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_officeinfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"officeid text,"+//��Դid
			"createuserid text,"+//������id
			"createusername text,"+//������id
			"areaup text,"+//�������
			"areadown text,"+//�������
			"address text,"+//��ַ
			"telephone text,"+//��ϵ�绰
			"storey text,"+//���
			"floors integer,"+//¥��
			"priceup text,"+//��������
			"pricedown text,"+//��������
			"officemc text,"+//��Դ����
			"wymanagementfees text,"+//��ҵ�����
			"officetype integer,"+//��Դ���� 0��д��¥��1��ס¥2���Ƶ�ʽ��Ԣ��3԰����4��������
			"keywords text,"+//��Դ�ؼ���
			"fycx integer,"+//��Դ����
			"zcyh text,"+//�����Ż�
			"tsyh text,"+//�����Ż�
			"fyjj text,"+//��Դ���
			"updatedate text,"+//����ʱ��
			"buildid text,"+//����¥��id
			"buildmc text,"+//����¥������
			"officestate integer,"+//��Դ״̬ :0��δ��ˣ�1�����ͨ��/δ�⣬2������3��˽��������4����Դ����5����ɾ��
			"officestatus integer,"+//0��������1���ɷָ�2���ɺϲ�
			"isprice integer,"+//0����������ۣ�1�������
			"offadddate text,"+//���ʱ��
			"roomrate text,"+//�÷���
			"imageid text,"+//��Դ�б�չʾͼƬ
			"ispushed text,"+//0δ���ͣ�1������
			"pushstate integer,"+//����״̬0�����У�1��ͣ
			"isrent text,"+//0δ����1�ѳ���
			"ischecked text,"+//0Ϊͨ����1��ͨ��
			"thumb_sdpath text," + //����ͼ ����sd��λ��
			"nextalertdate text"+//�´�����ʱ��
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
		setPushState(1);//0�����У�1��ͣ����
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
