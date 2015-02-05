package com.zhihuigu.sosoOffice.model;

public class MapNumberInfo {
	/***
	 * author by Ring
	 * 解析错误值得数据模型
	 */
	
	private String Id;//相关id
	private String Name;//相关name
	private String Number;//数字
	private String Longitude;//经度
	private String Latitude;//纬度
	public MapNumberInfo(){
		setId("");
		setLatitude("");
		setLongitude("");
		setName("");
		setNumber("");
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getNumber() {
		return Number;
	}
	public void setNumber(String number) {
		Number = number;
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
	
	
	
	
	
}
