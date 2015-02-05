package com.zhihuigu.sosoOffice.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;

import com.zhihuigu.sosoOffice.database.DBHelper;

public class SoSoBuildInfo {
	/**author by Ring
	 * 楼盘信息表
	 */
	

	public static String TABLE_CREATE = "create table " +
			"soso_buildinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"buildid text,"+//楼盘id
			"buildmc text,"+//楼盘名称
			"provinceid text,"+//省份ID
			"provincemc text,"+//省份名称
			"cityid text,"+//城市ID
			"citymc text,"+//城市名称
			"areaid text,"+//区域ID
			"areamc text,"+//区域名称
			"districtid text,"+//商圈ID
			"districtmc text,"+//商圈名称
			"buildtype integer,"+//楼盘类型 0,写字楼，1商铺，2工业地产
			"address text,"+//地址
			"areaup text,"+//面积上限
			"areadown text,"+//面积下限
			"telephone text,"+//联系电话
			"storey text,"+//层数
			"priceup text,"+//单价上限
			"pricedown text,"+//单价下限
			"metro text,"+//附近轻轨
			"bus text,"+//附近公交
			"zwss text,"+//周围设施
			"zcyh text,"+//政策优惠
			"tsyh text,"+//特殊优惠
			"fyjj text,"+//房源简介
			"longitude text,"+//经度
			"latitude text,"+//纬度
			"updatedate text,"+//更新时间
			"adddate text,"+//加入时间
			"buildstate integer"+//楼盘状态 0待审核，1：审核通过
			")";
	
	private String BuildID;
	private String BuildMC;
	private String ProvinceID;
	private String ProvMC;
	private String CityID;
	private String CityMC;
	private String AreaID;
	private String AreaMC;
	private String DistrictID;
	private String DIstrictMC;
	private int BuildType;
	private String AreaUp;
	private String AreaDown;
	private String Address;
	private String TelePhone;
	private String Storey;
	private String PriceUp;
	private String PriceDown;
	private String Metro;
	private String Bus;
	private String ZWSS;
	private String ZCYH;
	private String TSYH;
	private String FYJJ;
	private String Longitude;
	private String Latitude;
	private int IsUsed;
	
	public SoSoBuildInfo(){
		setAddress("");
		setAreaDown("");
		setAreaID("");
		setAreaMC("");
		setAreaUp("");
		setBuildID("");
		setBuildMC("");
		setBuildType(0);
		setBus("");
		setCityID("");
		setCityMC("");
		setDistrictID("");
		setDIstrictMC("");
		setFYJJ("");
		setLatitude("");
		setLongitude("");
		setPriceDown("");
		setPriceUp("");
		setProvinceID("");
		setProvMC("");
		setStorey("");
		setTelePhone("");
		setTSYH("");
		setMetro("");
		setZCYH("");
		setZWSS("");
		setIsUsed(0);
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
	public String getProvinceID() {
		return ProvinceID;
	}
	public void setProvinceID(String provinceID) {
		ProvinceID = provinceID;
	}
	public String getProvMC() {
		return ProvMC;
	}
	public void setProvMC(String provMC) {
		ProvMC = provMC;
	}
	public String getCityID() {
		return CityID;
	}
	public void setCityID(String cityID) {
		CityID = cityID;
	}
	public String getCityMC() {
		return CityMC;
	}
	public void setCityMC(String cityMC) {
		CityMC = cityMC;
	}
	public String getAreaID() {
		return AreaID;
	}
	public void setAreaID(String areaID) {
		AreaID = areaID;
	}
	public String getAreaMC() {
		return AreaMC;
	}
	public void setAreaMC(String areaMC) {
		AreaMC = areaMC;
	}
	public String getDistrictID() {
		return DistrictID;
	}
	public void setDistrictID(String districtID) {
		DistrictID = districtID;
	}
	public String getDIstrictMC() {
		return DIstrictMC;
	}
	public void setDIstrictMC(String dIstrictMC) {
		DIstrictMC = dIstrictMC;
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
	public String getStorey() {
		return Storey;
	}
	public void setStorey(String storey) {
		Storey = storey;
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
	public String getMetro() {
		return Metro;
	}
	public void setMetro(String metro) {
		Metro = metro;
	}
	public String getBus() {
		return Bus;
	}
	public void setBus(String bus) {
		Bus = bus;
	}
	public String getZWSS() {
		return ZWSS;
	}
	public void setZWSS(String zWSS) {
		ZWSS = zWSS;
	}
	public String getZCYH() {
		return ZCYH;
	}
	public void setZCYH(String zCYH) {
		ZCYH = zCYH;
	}
	public String getTSYH() {
		return TSYH;
	}
	public void setTSYH(String tSYH) {
		TSYH = tSYH;
	}
	public String getFYJJ() {
		return FYJJ;
	}
	public void setFYJJ(String fYJJ) {
		FYJJ = fYJJ;
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
	public int getBuildType() {
		return BuildType;
	}
	public void setBuildType(int buildType) {
		BuildType = buildType;
	}
	
	public int getIsUsed() {
		return IsUsed;
	}
	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}
	/**
	 * 将楼盘信息插入数据表中
	 * @param context 上下文
	 * @param soSoBuildInfo 楼盘对象
	 * @return true 插入成功，false 插入失败
	 */
	
	
	public boolean InsertBuildInfo(Context context,SoSoBuildInfo soSoBuildInfo){
		ContentValues values = new ContentValues();
		List<Map<String, Object>> selectresult = null;
		values.put("buildid", soSoBuildInfo.getBuildID());
		values.put("buildmc", soSoBuildInfo.getBuildMC());
		values.put("provinceid", soSoBuildInfo.getProvinceID());
		values.put("provincemc", soSoBuildInfo.getProvMC());
		values.put("cityid", soSoBuildInfo.getCityID());
		values.put("citymc", soSoBuildInfo.getCityMC());
		values.put("areaid", soSoBuildInfo.getAreaID());
		values.put("areamc", soSoBuildInfo.getAreaMC());
		values.put("districtid", soSoBuildInfo.getDistrictID());
		values.put("districtmc", soSoBuildInfo.getDIstrictMC());
		values.put("buildtype", soSoBuildInfo.getBuildType());
		values.put("address", soSoBuildInfo.getAddress());
		values.put("areaup", soSoBuildInfo.getAreaUp());
		values.put("areadown", soSoBuildInfo.getAreaDown());
		values.put("telephone", soSoBuildInfo.getTelePhone());
		values.put("storey", soSoBuildInfo.getStorey());
		values.put("priceup", soSoBuildInfo.getPriceUp());
		values.put("pricedown", soSoBuildInfo.getPriceDown());
		values.put("metro", soSoBuildInfo.getMetro());
		values.put("bus", soSoBuildInfo.getBus());
		values.put("zwss", soSoBuildInfo.getZWSS());
		values.put("zcyh", soSoBuildInfo.getZCYH());
		values.put("tsyh", soSoBuildInfo.getTSYH());
		values.put("fyjj", soSoBuildInfo.getFYJJ());
		values.put("longitude", soSoBuildInfo.getLongitude());
		values.put("latitude", soSoBuildInfo.getLatitude());
		selectresult = DBHelper.getInstance(context).selectRow(
				"select * from soso_buildinfo where buildid = '"
						+ soSoBuildInfo.getBuildID() + "'", null);
		boolean b = false;
		if (selectresult != null) {
			if (selectresult.size() <= 0) {
				if (DBHelper.getInstance(context).insert("soso_buildinfo",
						values)>0) {
					b =true ;
				}
			} else {
				b=DBHelper.getInstance(context)
						.update("soso_buildinfo",
								values,
								"buildid = ?",
								new String[] { soSoBuildInfo
										.getBuildID() });
			}
			selectresult.clear();
			selectresult = null;
		}
		values.clear();
		values=null;
		return b;
	}
	
	
	/**
	 * 将楼盘信息插入楼盘表中
	 * @param context 上下文
	 * @param soSoBuildInfo 楼盘对象列表
	 * @return true 插入成功，false 插入失败
	 */
	public void InsertBuildInfos(Context context,LinkedList<SoSoBuildInfo> soSoBuildInfos){
		if (soSoBuildInfos != null && soSoBuildInfos.size() > 0) {
			SoSoBuildInfo soSoBuildInfo = null;
			for (Iterator<SoSoBuildInfo> iterator = soSoBuildInfos
					.iterator(); iterator.hasNext();) {
				soSoBuildInfo = (SoSoBuildInfo) iterator.next();
				if (soSoBuildInfo.getIsUsed() == 1) {
					DBHelper.getInstance(context).delete("soso_buildinfo",
							"buildid = ?", new String[] { soSoBuildInfo.getBuildID() });
					continue;
				}
				soSoBuildInfo.InsertBuildInfo(context, soSoBuildInfo);
			}
			if (soSoBuildInfos != null) {
				soSoBuildInfos.clear();
				soSoBuildInfos = null;
			}
		}
		
		DBHelper.getInstance(context).close();
	}
	
	/**
	 * 查出楼盘对象
	 * @param context 上下文
	 * @param soSoBuildInfo 楼盘对象
	 * @return true 插入成功，false 插入失败
	 */
	public LinkedList<SoSoBuildInfo> SelectBuildInfo(Context context,String sql){
		List<Map<String, Object>> selectresult = null;
		LinkedList<SoSoBuildInfo> soSoBuildInfos = null;
		SoSoBuildInfo soSoBuildInfo = null;
		selectresult = DBHelper.getInstance(context).selectRow(sql, null);
		if(selectresult!=null&&selectresult.size()>0){
			soSoBuildInfos = new LinkedList<SoSoBuildInfo>();
			int i;
			for(i=0;i<selectresult.size();i++){
				soSoBuildInfo = new SoSoBuildInfo();
				soSoBuildInfo.setAddress(selectresult.get(i).get("address").toString());
				soSoBuildInfo.setAreaDown(selectresult.get(i).get("areadown").toString());
				soSoBuildInfo.setAreaID(selectresult.get(i).get("areaid").toString());
				soSoBuildInfo.setAreaMC(selectresult.get(i).get("areamc").toString());
				soSoBuildInfo.setAreaUp(selectresult.get(i).get("areaup").toString());
				soSoBuildInfo.setBuildID(selectresult.get(i).get("buildid").toString());
				soSoBuildInfo.setBuildMC(selectresult.get(i).get("buildmc").toString());
				try {
					soSoBuildInfo.setBuildType(Integer.parseInt(selectresult
							.get(i).get("buildtype").toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				soSoBuildInfo.setBus(selectresult.get(i).get("bus").toString());
				soSoBuildInfo.setCityID(selectresult.get(i).get("cityid").toString());
				soSoBuildInfo.setCityMC(selectresult.get(i).get("citymc").toString());
				soSoBuildInfo.setDistrictID(selectresult.get(i).get("districtid").toString());
				soSoBuildInfo.setDIstrictMC(selectresult.get(i).get("districtmc").toString());
				soSoBuildInfo.setFYJJ(selectresult.get(i).get("fyjj").toString());
				soSoBuildInfo.setLatitude(selectresult.get(i).get("latitude").toString());
				soSoBuildInfo.setLongitude(selectresult.get(i).get("longitude").toString());
				soSoBuildInfo.setPriceDown(selectresult.get(i).get("pricedown").toString());
				soSoBuildInfo.setPriceUp(selectresult.get(i).get("priceup").toString());
				soSoBuildInfo.setProvinceID(selectresult.get(i).get("provinceid").toString());
				soSoBuildInfo.setProvMC(selectresult.get(i).get("provincemc").toString());
				soSoBuildInfo.setStorey(selectresult.get(i).get("storey").toString());
				soSoBuildInfo.setTelePhone(selectresult.get(i).get("telephone").toString());
				soSoBuildInfo.setTSYH(selectresult.get(i).get("tsyh").toString());
				soSoBuildInfo.setMetro(selectresult.get(i).get("metro").toString());
				soSoBuildInfo.setZCYH(selectresult.get(i).get("zcyh").toString());
				soSoBuildInfo.setZWSS(selectresult.get(i).get("zwss").toString());
				soSoBuildInfos.add(soSoBuildInfo);
			}
		}
		return soSoBuildInfos;
	}
	

}
