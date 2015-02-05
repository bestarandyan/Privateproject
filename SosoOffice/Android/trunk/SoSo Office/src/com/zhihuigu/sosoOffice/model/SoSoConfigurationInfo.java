package com.zhihuigu.sosoOffice.model;

public class SoSoConfigurationInfo {
	/*SERVER_TIME	������ʱ��
	TLETTERS_TIME	վ���ű����¸���ʱ��
	TFAVORITES_TIME	�ղر����¸���ʱ��
	TDISTRICT_TIME	��Ȧ�����¸���ʱ��
	TBUILD_TIME	¥�̱����¸���ʱ��
	THELPDOCUMENT_TIME	ϵͳ���������¸���ʱ��
	TMETRO_TIME	���������¸���ʱ��
	TOFFICE_TIME	��Դ�����¸���ʱ��
	TPUSH_TIME	���ͱ����¸���ʱ��
	TVISITOFFICE_TIME	��Դ���ʱ����¸���ʱ��
	TUSERCONACT_TIME	�û���ϵ�˱����¸���ʱ��
	TCUSRELEASE_TIME	�ͻ����������ʱ��*/


	public static String TABLE_CREATE = "create table " +
			"soso_configurationinfo" +//���� sosoconfigurationinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"userid text,"+//�û�id
			"name text,"+//������keyֵ
			"value text,"+//�����е�valueֵ
			"updatedate text,"+//�������õ�ʱ��
			"updatedate1 text,"+//����ʱ��1
			"updatedate2 text"+//����ʱ��2
			")";
	
	
	private String Name;
	private String Value;
	/**
	 * ģ�ͳ�ʼ��
	 * @return 
	 */
	public SoSoConfigurationInfo(){
		setName("");
		setValue("");
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
}
