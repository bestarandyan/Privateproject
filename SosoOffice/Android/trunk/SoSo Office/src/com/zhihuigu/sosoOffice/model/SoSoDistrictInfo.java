package com.zhihuigu.sosoOffice.model;

public class SoSoDistrictInfo {
	/**author by Ring
	 * 商圈信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_districtinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"areaid text,"+//区域id
			"districtid text,"+//商圈id
			"districtmc text,"+//商圈名称
			"latitude text,"+//商圈纬度
			"longitude text"+//商圈经度
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
