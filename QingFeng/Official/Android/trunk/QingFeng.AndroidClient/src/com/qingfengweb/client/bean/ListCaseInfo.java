/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author Ring
 * ��������
 */

public class ListCaseInfo {
	/**author by Ring
	 * �ͻ������б�
	 */
	public static String TableName = "listCaseInfo";//�ͻ������б�
	public static String CREATE_TABLE_SQL = "create table " +
			TableName +//���� 
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"id text,"+//�������
			"name text,"+//��������
			"summary text,"+//��������
			"photos varchar(100),"+//ͼƬid����
			"thumb varchar(10),"+//�б���Ҫ��ʾ��ͼƬID
			"type varchar(40),"+//��������
			"description varchar(200),"+//��������
			"sequence varchar(10),"+//����˳��
			"updatetime varchar(40),"+//����ʱ��
			"status varchar(40)"+//ͼƬ״̬  1-��ɾ��
			")";
	public  String id = "";
	public  String name = "";
	public  String summary = "";
	public  String photos = "";
	public  String thumb = "";
	public  String type = "";
	public  String description = "";
	public  String sequence = "";
	public  String updatetime = "";
	public  String status = "";
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
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
