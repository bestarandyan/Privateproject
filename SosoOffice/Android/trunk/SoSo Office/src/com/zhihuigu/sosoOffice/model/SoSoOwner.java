package com.zhihuigu.sosoOffice.model;

public class SoSoOwner {
	/**author by Ring
	 * ҵ��������
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_owner" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"soso_ownerid text,"+//ҵ��id
			"soso_userid text,"+//�û�id
			"soso_ownername text,"+//����
			"soso_ownerphone text,"+//ҵ���绰
			"soso_legalperson text,"+//ҵ������
			"soso_businesslicenseurl text,"+//ҵ��Ӫҵִ��url
			"soso_businesslicensesd text,"+//ҵ��Ӫҵִ��sd��λ��
			"soso_owneraddress text,"+//ҵ����ַ
		    "soso_isused integer" +	//����״̬ 0��ʾ��1����Ĭ�ϣ�0
			")";
	
	
	private String soso_ownerid;
	private String soso_userid;
	private String soso_ownername;
	private String soso_ownerphone;
	private String soso_legalperson;
	private String soso_businesslicenseurl;
	private String soso_owneraddress;
	private String soso_isused;
	
	public SoSoOwner(){
		
	}

	public String getSoso_ownerid() {
		return soso_ownerid;
	}

	public void setSoso_ownerid(String soso_ownerid) {
		this.soso_ownerid = soso_ownerid;
	}

	public String getSoso_userid() {
		return soso_userid;
	}

	public void setSoso_userid(String soso_userid) {
		this.soso_userid = soso_userid;
	}

	public String getSoso_ownername() {
		return soso_ownername;
	}

	public void setSoso_ownername(String soso_ownername) {
		this.soso_ownername = soso_ownername;
	}

	public String getSoso_ownerphone() {
		return soso_ownerphone;
	}

	public void setSoso_ownerphone(String soso_ownerphone) {
		this.soso_ownerphone = soso_ownerphone;
	}

	public String getSoso_legalperson() {
		return soso_legalperson;
	}

	public void setSoso_legalperson(String soso_legalperson) {
		this.soso_legalperson = soso_legalperson;
	}

	public String getSoso_businesslicenseurl() {
		return soso_businesslicenseurl;
	}

	public void setSoso_businesslicenseurl(String soso_businesslicenseurl) {
		this.soso_businesslicenseurl = soso_businesslicenseurl;
	}

	public String getSoso_owneraddress() {
		return soso_owneraddress;
	}

	public void setSoso_owneraddress(String soso_owneraddress) {
		this.soso_owneraddress = soso_owneraddress;
	}

	public String getSoso_isused() {
		return soso_isused;
	}

	public void setSoso_isused(String soso_isused) {
		this.soso_isused = soso_isused;
	}
	
	
	
}
