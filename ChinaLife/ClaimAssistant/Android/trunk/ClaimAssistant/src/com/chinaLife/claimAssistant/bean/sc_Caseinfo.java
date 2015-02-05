package com.chinaLife.claimAssistant.bean;

public class sc_Caseinfo {
	/**
	 * 案件信息表
	 */
	public static String TABLE_CREATE = "create table " + "caseinfo" + // 表名caseinfo
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"caseid text," + // 案件编号
			"case_number text," + // 事故号
			"accident_address text," + // 出险地点
			"contact_name text," + // 联系人姓名
			"contact_mobile_number text," + // 联系人手机号码
			"plate_number text collate nocase," + // 车牌号码
			"claim_userid text," + // 理赔专员用户编号
			"claim_name text," + // 理赔专员姓名
			"claim_phone_number text," + // 客服热线
			"summary text," + // 出险摘要
			"status integer," + // 1-处理中2-已结案4-已撤销8-已删除
			"status_text text," + // 状态描述
			"remark text," + // 备注
			"report_time text," + // 报案时间
			"accident_time text," + // 出险时间
			"case_reason text," + // 案件撤销原因
			"case_remark text," + // 案件撤销描述
			"update_time text," + // 最后更新时间
			"create_time text," + // 创建时间
			"imei text" + // 手机设备号
			")";
	
	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS caseinfo";

	private String caseid;
	private String casenumber;
	private String accidentaddress;
	private String contactname;
	private String contactmobilenumber;
	private String platenumber;
	private String claimuserid;
	private String claimname;
	private String claimphonenumber;
	private String summary;
	private String status;
	private String statustext;
	private String remark;
	private String reporttime;
	private String accidenttime;
	private String updatetime;
	private String createtime;
	private String imei;
	private String casereason;
	private String caseremark;

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getCasenumber() {
		return casenumber;
	}

	public void setCasenumber(String casenumber) {
		this.casenumber = casenumber;
	}

	public String getAccidentaddress() {
		return accidentaddress;
	}

	public void setAccidentaddress(String accidentaddress) {
		this.accidentaddress = accidentaddress;
	}

	public String getContactname() {
		return contactname;
	}

	public void setContactname(String contactname) {
		this.contactname = contactname;
	}

	public String getContactmobilenumber() {
		return contactmobilenumber;
	}

	public void setContactmobilenumber(String contactmobilenumber) {
		this.contactmobilenumber = contactmobilenumber;
	}

	public String getPlatenumber() {
		return platenumber;
	}

	public void setPlatenumber(String platenumber) {
		this.platenumber = platenumber;
	}

	public String getClaimuserid() {
		return claimuserid;
	}

	public void setClaimuserid(String claimuserid) {
		this.claimuserid = claimuserid;
	}

	public String getClaimname() {
		return claimname;
	}

	public void setClaimname(String claimname) {
		this.claimname = claimname;
	}

	public String getClaimphonenumber() {
		return claimphonenumber;
	}

	public void setClaimphonenumber(String claimphonenumber) {
		this.claimphonenumber = claimphonenumber;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReporttime() {
		return reporttime;
	}

	public void setReporttime(String reporttime) {
		this.reporttime = reporttime;
	}

	public String getAccidenttime() {
		return accidenttime;
	}

	public void setAccidenttime(String accidenttime) {
		this.accidenttime = accidenttime;
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

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getCasereason() {
		return casereason;
	}

	public void setCasereason(String casereason) {
		this.casereason = casereason;
	}

	public String getCaseremark() {
		return caseremark;
	}

	public void setCaseremark(String caseremark) {
		this.caseremark = caseremark;
	}

}
