package com.zhihuigu.sosoOffice.model;

public class SoSoMetroInfo {
	/**author by Ring
	 * ҵ��������
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_metroinfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"metroid text,"+//������·id
			"cityid text,"+//����id
			"metroname text,"+//��������
			"updatetime text"+//���¸���ʱ��
			")";
	
	
	private String MetroID;
	private String MetroName;
	private String UpdateTime;
	
	public SoSoMetroInfo(){
		setMetroID("");
		setMetroName("");
		setUpdateTime("");
	}

	public String getMetroID() {
		return MetroID;
	}

	public void setMetroID(String metroID) {
		MetroID = metroID;
	}

	public String getMetroName() {
		return MetroName;
	}

	public void setMetroName(String metroName) {
		MetroName = metroName;
	}

	public String getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(String updateTime) {
		UpdateTime = updateTime;
	}

	
	
	
	
}
