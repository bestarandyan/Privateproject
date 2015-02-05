/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author ������
 * @createDate 2013��8��7
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
	public String name = "";//ְλ����
	public String number = "";//��Ƹ����
	public String summary = "";//Ҫ�����
	public String requirements = "";//��ƸҪ��
	public String sequence = "";//����˳��(����)
	public String endtime = "";//����ʱ��
	public String updatetime = "";//����ʱ��
	public String description = "";//��λ����
	public String responsibility = "";//��λְ��
	public String status = "";//1-��ɾ��
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
