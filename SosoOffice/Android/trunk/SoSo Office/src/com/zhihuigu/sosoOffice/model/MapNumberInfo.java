package com.zhihuigu.sosoOffice.model;

public class MapNumberInfo {
	/***
	 * author by Ring
	 * ��������ֵ������ģ��
	 */
	
	private String Id;//���id
	private String Name;//���name
	private String Number;//����
	private String Longitude;//����
	private String Latitude;//γ��
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
