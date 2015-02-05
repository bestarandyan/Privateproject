package com.chinaLife.claimAssistant.bean;

public class sc_Caseinfo {
	/**
	 * 
	 */
	public static String TABLE_CREATE = "create table " + "caseinfo" + 
			"(" + "_id integer primary key autoincrement," + 
			"caseid text," +
			"case_number text," + 
			"accident_address text," + 
			"contact_name text," + 
			"contact_mobile_number text," + 
			"plate_number text collate nocase," + 
			"claim_userid text," + 
			"claim_name text," + 
			"claim_phone_number text," + 
			"summary text," + 
			"status integer," + 
			"status_text text," + 
			"remark text," + 
			"report_time text," + 
			"accident_time text," + 
			"case_reason text," + 
			"case_remark text," + 
			"update_time text," +
			"create_time text," + 
			"imei text" +
			")";
	
	/**
	 * ɾ��ñ�
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
