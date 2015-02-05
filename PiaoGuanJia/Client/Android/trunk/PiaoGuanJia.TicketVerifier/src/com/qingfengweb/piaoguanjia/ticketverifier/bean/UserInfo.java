package com.qingfengweb.piaoguanjia.ticketverifier.bean;

public class UserInfo {
	private String userid;
	private String permissions;
	private String productName;
	private String quitPassword;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getQuitPassword() {
		return quitPassword;
	}

	public void setQuitPassword(String quitPassword) {
		this.quitPassword = quitPassword;
	}

}
