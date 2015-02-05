package com.qingfengweb.piaoguanjia.ticketverifier.bean;

public class ValidateInfo {
	private String orderid;//	订单编号	获取详情使用
	private String orderNumber;	//订单号	显示在列表上
	private String orderTime;	//预定时间	yyyy-MM-dd
	private String productName;	//票种	
	private String totalAmount;	//预定数量	
	private String validateTime;	//验证时间	
	private String parentName;	//接口	
	private String name;	//姓名	
	private String phoneNumber;	//手机号码	
	private String credentialsNumber;	//身份证号码	
	private String isValidate;	//是否验证（1已验证，0未验证）	用来客户端识别页面
	private String createTime;//创建时间
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getValidateTime() {
		return validateTime;
	}
	public void setValidateTime(String validateTime) {
		this.validateTime = validateTime;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
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
	public String getCredentialsNumber() {
		return credentialsNumber;
	}
	public void setCredentialsNumber(String credentialsNumber) {
		this.credentialsNumber = credentialsNumber;
	}
	public String getIsValidate() {
		return isValidate;
	}
	public void setIsValidate(String isValidate) {
		this.isValidate = isValidate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	
}
