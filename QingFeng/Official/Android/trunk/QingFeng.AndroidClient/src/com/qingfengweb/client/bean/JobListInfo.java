/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author 刘星星
 * @createDate 2013、8、7
 *
 */
public class JobListInfo {
	public static String TableName = "jobInfo";
	public static String createTableSQL = "create table if not exists "+TableName+"" +
			"(_id Integer primary key autoincrement," +
			"id Integer," +
			"name varchar(40)," +
			"number Integer," +
			"summary text," +
			"requirements text," +
			"sequence Integer," +
			"endtime varchar(40)," +
			"updatetime varchar(40)," +
			"description text," +
			"responsibility text," +
			"status Integer)";
	
	public String id = "";
	public String name = "";//职位名称
	public String number = "";//招聘人数
	public String summary = "";//要求概述
	public String requirements = "";//招聘要求
	public String sequence = "";//排列顺序(倒序)
	public String endtime = "";//截至时间
	public String updatetime = "";//更新时间
	public String description = "";//岗位描述
	public String responsibility = "";//岗位职责
	public String status = "";//1-已删除
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getRequirements() {
		return requirements;
	}
	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResponsibility() {
		return responsibility;
	}
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
