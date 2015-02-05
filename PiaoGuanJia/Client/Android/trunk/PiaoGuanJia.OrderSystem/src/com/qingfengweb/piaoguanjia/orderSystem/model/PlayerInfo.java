package com.qingfengweb.piaoguanjia.orderSystem.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "playerinfo")
// 建议加上注解， 混淆后表名不受影响
public class PlayerInfo extends EntityBase {
	@NoAutoIncrement
	@Id
	@Column(column = "playerid")
	public String playerid;
	@Column(column = "name")
	public String name;
	@Column(column = "phonenumber")
	public String phoneNumber;
	@Column(column = "idcard")
	public String idCard;
	@Column(column = "address")
	public String address;
	@Column(column = "createtime")
	public String createTime;
	
	
	
	
//	"playerid": "4",
//    "name": "yys3",
//    "phoneNumber": "1500059703",
//    "address": "上海市普陀区3",
//    "idCard": "111111111111110",
//    "createTime": "2014-03-11 09:51:10"
    	
    	
	
	public String getPlayerid() {
		return playerid;
	}
	public void setPlayerid(String playerid) {
		this.playerid = playerid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	

}
