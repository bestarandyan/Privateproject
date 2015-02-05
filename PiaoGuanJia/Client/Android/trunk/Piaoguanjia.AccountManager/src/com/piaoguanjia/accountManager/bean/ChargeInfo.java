package com.piaoguanjia.accountManager.bean;

public class ChargeInfo {
	private String chargeid;// 充值编号
	private String source;// 来源
	private String username;// 充值用户名
	private String type;// 充值方式 字符串，直接显示
	private String accountType;// 账户类型 1总账户，2专用账户
	private String auditTime;// 充值时间 仅当status=1时，才不为空
	private String productName;// 产品名称 仅当accountType=2时不为空
	private String productid;// 产品编号 仅当accountType=2时不为空
	private String status;// 状态 当前状态，0未审核，1审核通过，2审核不通过，3已取消（只有status=0才可以审核）
	private String amount;// 充值金额
	private String totalAmount;// 累计充值金额
	private String balance;// 账户余额
	private String balanceSource;// 资金来源 字符串，直接显示
	private String isReceipt;// 是否可以申请发票 1是，0不是
	private String remark;// 备注
	private String warrantor;// 授权账户
	private String isCertificate;// 是否有凭证 1有，0没有
	private String createTime;// 创建时间

	

	public String getChargeid() {
		return chargeid;
	}

	public void setChargeid(String chargeid) {
		this.chargeid = chargeid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBalanceSource() {
		return balanceSource;
	}

	public void setBalanceSource(String balanceSource) {
		this.balanceSource = balanceSource;
	}

	public String getIsReceipt() {
		return isReceipt;
	}

	public void setIsReceipt(String isReceipt) {
		this.isReceipt = isReceipt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsCertificate() {
		return isCertificate;
	}

	public void setIsCertificate(String isCertificate) {
		this.isCertificate = isCertificate;
	}


	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getWarrantor() {
		return warrantor;
	}

	public void setWarrantor(String warrantor) {
		this.warrantor = warrantor;
	}
	

}
