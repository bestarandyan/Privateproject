package com.piaoguanjia.accountManager.bean;

public class AccountInfo {
	private String accountid;// 	专用账户编号
	private String username	;// 分销商用户名
	private String name;//分销商姓名
	private String productName;// 	产品名称
	private String chargeLimit;// 充值限制
	private String remindAmount	;// 余额提醒	
	private String remindType;// 	提醒方式	1短信，2通知
	private String onlineChargeLimit;// 	直冲额度	
	private String status;// 	状态	1未审核，2-审核通过，4审核失败（只有(status&1)==1时，才能审核）
	private String productid;// 	产品编号
	private String createTime;// 	创建时间
	private String auditTime;// 	审核时间
	public String getAccountid() {
		return accountid;
	}
	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getChargeLimit() {
		return chargeLimit;
	}
	public void setChargeLimit(String chargeLimit) {
		this.chargeLimit = chargeLimit;
	}
	public String getRemindAmount() {
		return remindAmount;
	}
	public void setRemindAmount(String remindAmount) {
		this.remindAmount = remindAmount;
	}
	public String getRemindType() {
		return remindType;
	}
	public void setRemindType(String remindType) {
		this.remindType = remindType;
	}
	public String getOnlineChargeLimit() {
		return onlineChargeLimit;
	}
	public void setOnlineChargeLimit(String onlineChargeLimit) {
		this.onlineChargeLimit = onlineChargeLimit;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
