package com.chinaLife.claimAssistant.bean;

public class sc_ClaimInfo {
	/**
	 * 理赔信息表
	 */
	public static String TABLE_CREATE = "create table " + "claiminfo" + // 表名claiminfo
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"claimid text," + // 自助理赔任务编号
			"caseid text," + // 案件编号
			"userid text," + // 理赔专员客户编号
			"service_type integer," + // 服务方式：1：自助查勘，2：自助理赔
			"claim_mode integer," + // 赔案模式：1-简易赔案2-一般赔案
			"status integer," + //
			"status_text text," + // 理赔状态描述
			"update_time text," + // 最后更新时间
			"create_time text," + // 理赔任务创建时间
			"claim_amount text," + // 理赔金额
			"claim_overview text," + // 赔款概述
			"legends text," + // 补全单证列表
			"certificates text," + // 补全单证列表
			"bank_name text," + // 开户行
			"bankcode text," + // 总行
			"bankphonenumber  text," + // 电话
			"disagreereason text,"+//不同意的原因
			"disagreereasonid integer,"+//不同意的选项
			"claim_type_select integer," + // 是否选择过赔案类型1单车，2多车
			"account_name text," + // 账户名
			"iscomfirm integer," + // 是否将款项划到账号上；0不选，1选
			"insuredname text," + // 被保人
			"account_number text" + // 账号
			")";
	
	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS claiminfo";
	private String uaid;
	private String reason;
	private String claimid;
	private String caseid;
	private String userid;
	private String servicetype;
	private String claimmode;
	private String status;
	private String statustext;
	private String updatetime;
	private String createtime;
	private String claimamount;
	private String claimoverview;
	private String bankname;
	private String bankcode;
	private String bankphonenumber ;
	private String accountname;
	private String accountnumber;
	private String certificates;
	private String isconfirmbankinfo;
	
	
	public sc_ClaimInfo(){
		setBankcode("");
		setIsconfirmbankinfo("0");
	}
	
	public String getUaid() {
		return uaid;
	}
	public void setUaid(String uaid) {
		this.uaid = uaid;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getClaimid() {
		return claimid;
	}
	public void setClaimid(String claimid) {
		this.claimid = claimid;
	}
	public String getCaseid() {
		return caseid;
	}
	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getServicetype() {
		return servicetype;
	}
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}
	public String getClaimmode() {
		return claimmode;
	}
	public void setClaimmode(String claimmode) {
		this.claimmode = claimmode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatustext() {
		return statustext;
	}
	public void setStatustext(String statustext) {
		this.statustext = statustext;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getClaimamount() {
		return claimamount;
	}
	public void setClaimamount(String claimamount) {
		this.claimamount = claimamount;
	}
	public String getClaimoverview() {
		return claimoverview;
	}
	public void setClaimoverview(String claimoverview) {
		this.claimoverview = claimoverview;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	public String getAccountnumber() {
		return accountnumber;
	}
	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}
	public String getCertificates() {
		return certificates;
	}
	public void setCertificates(String certificates) {
		this.certificates = certificates;
	}
	public String getBankcode() {
		return bankcode;
	}
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}
	public String getBankphonenumber() {
		return bankphonenumber;
	}
	public void setBankphonenumber(String bankphonenumber) {
		this.bankphonenumber = bankphonenumber;
	}

	public String getIsconfirmbankinfo() {
		return isconfirmbankinfo;
	}

	public void setIsconfirmbankinfo(String isconfirmbankinfo) {
		this.isconfirmbankinfo = isconfirmbankinfo;
	}


}
