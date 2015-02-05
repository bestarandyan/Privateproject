/**
 * 
 */
package com.qingfengweb.weddingideas.beans;

/**
 * @author 刘星星
 * 影楼实体类
 *
 */
public class YingLouBean {
	public static String tbName = "aboutyinglou";
	public static String tbCreateSql ="create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"id text," +
			"province text," +
			"city text," +
			"district text," +
			"address text," +
			"ask_tel text," +
			"lon text," +
			"lat text," +
			"wed_desc text," +
			"opt_time text)";
	public String id="";
	public String province="";
	public String city="";
	public String district="";
	public String address="";
	public String ask_tel="";
	public String lon="";
	public String lat="";
	public String wed_desc="";
	public String opt_time="";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAsk_tel() {
		return ask_tel;
	}
	public void setAsk_tel(String ask_tel) {
		this.ask_tel = ask_tel;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getWed_desc() {
		return wed_desc;
	}
	public void setWed_desc(String wed_desc) {
		this.wed_desc = wed_desc;
	}
	public String getOpt_time() {
		return opt_time;
	}
	public void setOpt_time(String opt_time) {
		this.opt_time = opt_time;
	}
	
	
}
