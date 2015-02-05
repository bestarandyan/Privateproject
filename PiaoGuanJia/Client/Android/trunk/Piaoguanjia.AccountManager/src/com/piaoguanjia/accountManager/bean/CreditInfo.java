package com.piaoguanjia.accountManager.bean;

public class CreditInfo {
	private String creditid;// 额度编号
	private String username;// 分销商用户名
	private String accountType;// 账户类型 1总账户，2专用账户
	private String createTime;// 创建时间
	private String productName;// 产品名称 仅当accountType=2时不为空
	private String auditTime;// 审核时间 仅当 (status&1) != 1时，才不为空
	private String status;// 状态 当前状态：1未审核，2审核通过，4审核失败（只有未审核才可以审核）
	private String productid;// 产品编号 仅当accountType=2时不为空
	private String creditLimit;// 额度
	private String name;//分销商姓名
	private String reason;// 添加理由
	public String getCreditid() {
		return creditid;
	}
	public void setCreditid(String creditid) {
		this.creditid = creditid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProductid() {
		return productid;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	public String getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
