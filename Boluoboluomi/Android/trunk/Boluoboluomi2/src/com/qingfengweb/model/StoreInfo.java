package com.qingfengweb.model;

public class StoreInfo {
	public static String TableName = "storeinfo";
	/**
	 * author:Ring
	 * 商店信息表
	 */
	public static String TABLE_CREATE = "create table if not exists " +
			"storeinfo" +//表名 pointsinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"id text,"+//商店id
			"name text,"+//商店名称
			"cityid text,"+//所属城市
			"city text,"+//所属城市
			"introduce text,"+//商店描述
			"district text,"+//所属区域
			"districtid text,"+//所属区域 ID
			"provinceid text,"+//所属身份ID
			"province text,"+//所属省份名称
			"phonenumber text,"+//联系电话
			"store_logo text,"+//门店图片
			"store_home_logo text,"+//店家首页图片
			"qq text,"+//qq
			"contact text,"+//联系方式
			"shop_activity_image text,"+//积分商城图片
			"shop_activity_text text,"+//积分商城内容
			"shop_activity_link text,"+//积分商城连接
			"qrcode1 text,"+//联系方式
			"qrcode2 text,"+//联系方式
			"validate_required text,"+//联系方式
			"deleted text"+//是否删除
			")";
	private int shop_activity_image;
	private int shop_activity_text;
	private int shop_activity_link;
	private int qrcode1;
	private int qrcode2;
	private int validate_required;
	private String id;
	private String name;
	public String district = "";
	private int cityid;
	private int city;
	private int introduce;
	private int districtid;
	private int provinceid;
	private int province;
	private int phonenumber;
	private int store_logo;
	private int store_home_logo;
	private int qq;
	private int contact;
	private int deleted;

	public int getShop_activity_image() {
		return shop_activity_image;
	}
	public void setShop_activity_image(int shop_activity_image) {
		this.shop_activity_image = shop_activity_image;
	}
	public int getShop_activity_text() {
		return shop_activity_text;
	}
	public void setShop_activity_text(int shop_activity_text) {
		this.shop_activity_text = shop_activity_text;
	}
	public int getShop_activity_link() {
		return shop_activity_link;
	}
	public void setShop_activity_link(int shop_activity_link) {
		this.shop_activity_link = shop_activity_link;
	}
	public int getQrcode1() {
		return qrcode1;
	}
	public void setQrcode1(int qrcode1) {
		this.qrcode1 = qrcode1;
	}
	public int getQrcode2() {
		return qrcode2;
	}
	public void setQrcode2(int qrcode2) {
		this.qrcode2 = qrcode2;
	}
	public int getValidate_required() {
		return validate_required;
	}
	public void setValidate_required(int validate_required) {
		this.validate_required = validate_required;
	}
	public int getCityid() {
		return cityid;
	}
	public void setCityid(int cityid) {
		this.cityid = cityid;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public int getIntroduce() {
		return introduce;
	}
	public void setIntroduce(int introduce) {
		this.introduce = introduce;
	}
	public int getDistrictid() {
		return districtid;
	}
	public void setDistrictid(int districtid) {
		this.districtid = districtid;
	}
	public int getProvinceid() {
		return provinceid;
	}
	public void setProvinceid(int provinceid) {
		this.provinceid = provinceid;
	}
	public int getProvince() {
		return province;
	}
	public void setProvince(int province) {
		this.province = province;
	}
	public int getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(int phonenumber) {
		this.phonenumber = phonenumber;
	}
	public int getStore_logo() {
		return store_logo;
	}
	public void setStore_logo(int store_logo) {
		this.store_logo = store_logo;
	}
	public int getStore_home_logo() {
		return store_home_logo;
	}
	public void setStore_home_logo(int store_home_logo) {
		this.store_home_logo = store_home_logo;
	}
	public int getQq() {
		return qq;
	}
	public void setQq(int qq) {
		this.qq = qq;
	}
	public int getContact() {
		return contact;
	}
	public void setContact(int contact) {
		this.contact = contact;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
	
}
