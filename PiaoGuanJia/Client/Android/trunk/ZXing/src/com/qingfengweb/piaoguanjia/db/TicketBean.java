package com.qingfengweb.piaoguanjia.db;

import com.qingfengweb.piaoguanjia.content.DbContent;


public class TicketBean {
public String	orderid;
public String	ordertime;
public String	username;
public String	tickettype;
public String	ordercount;
public String	createtime;
public String   idnumber;
public String phonenumber;
public String scenicname;
public String validatetime;

public String getValidatetime() {
	return validatetime;
}
public void setValidatetime(String validatetime) {
	this.validatetime = validatetime;
}
public String getScenicname() {
	return scenicname;
}
public void setScenicname(String scenicname) {
	this.scenicname = scenicname;
}
public String getPhonenumber() {
	return phonenumber;
}
public void setPhonenumber(String phonenumber) {
	this.phonenumber = phonenumber;
}
public String getIdnumber() {
	return idnumber;
}
public void setIdnumber(String idnumber) {
	this.idnumber = idnumber;
}
public String getOrderid() {
	return orderid;
}
public void setOrderid(String orderid) {
	this.orderid = orderid;
}
public String getOrdertime() {
	return ordertime;
}
public void setOrdertime(String ordertime) {
	this.ordertime = ordertime;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getTickettype() {
	return tickettype;
}
public void setTickettype(String tickettype) {
	this.tickettype = tickettype;
}
public String getOrdercount() {
	return ordercount;
}
public void setOrdercount(String ordercount) {
	this.ordercount = ordercount;
}
public String getCreatetime() {
	return createtime;
}
public void setCreatetime(String createtime) {
	this.createtime = createtime;
}
public String sql="create table if not exists "+DbContent.HISTORY_TICKET+"(_id integer primary key autoincrement," +
			"orderid varchar(40)," +
			"ordertime varchar(40)," +
			"username varchar(40)," +
			"tickettype varchar(40)," +
			"ordercount varchar(40)," +
			"phonenumber varchar(40)," +
			"idnumber varchar(40)," +
			"scenicname varchar(80)," +
			"validatetime varchar(80)," +
			"createtime varchar(40))";
}
