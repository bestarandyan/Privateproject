package com.zhihuigu.sosoOffice.model;

public class SoSoZoneInfo {
	/**author by Ring
	 * ʡ������Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_zoneinfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"id text,"+//id
			"name text,"+//����
			"typeid text,"+//1ʡ�ݣ�2���У�3����
			"parentid text"+//��id
			")";
	
	
	private String id;
	private String name;
	private String typeid;
	private String parentid;
	
	public SoSoZoneInfo(){
		setId("");
		setName("");
		setParentid("");
		setTypeid("");
	}

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

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	
	
	
}
