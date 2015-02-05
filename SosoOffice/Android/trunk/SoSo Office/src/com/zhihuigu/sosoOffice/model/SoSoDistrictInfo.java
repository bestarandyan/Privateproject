package com.zhihuigu.sosoOffice.model;

public class SoSoDistrictInfo {
	/**author by Ring
	 * ��Ȧ��Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_districtinfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"areaid text,"+//����id
			"districtid text,"+//��Ȧid
			"districtmc text,"+//��Ȧ����
			"latitude text,"+//��Ȧγ��
			"longitude text"+//��Ȧ����
			")";
	
	
	private String DistrictID;
	private String DistrictMC;
	private String AreaID;
	private String Longitude;
	private String Latitude;
	private int IsUsed;
	
	public SoSoDistrictInfo(){
		setDistrictID("");
		setDistrictMC("");
		setAreaID("");
		setIsUsed(0);
		setLatitude("");
		setLongitude("");
	}

	public String getDistrictID() {
		return DistrictID;
	}

	public void setDistrictID(String districtID) {
		DistrictID = districtID;
	}

	public String getDistrictMC() {
		return DistrictMC;
	}

	public void setDistrictMC(String districtMC) {
		DistrictMC = districtMC;
	}

	public String getAreaID() {
		return AreaID;
	}

	public void setAreaID(String areaID) {
		AreaID = areaID;
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

	public int getIsUsed() {
		return IsUsed;
	}

	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}
	
	
}
