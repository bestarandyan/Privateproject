package com.zhihuigu.sosoOffice.model;


public class SoSoCountInfo {
	/**author by Ring
	 * �ż���Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_countinfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"userid text,"+//�û�id
			"lettercount integer default 0,"+//վ��������
			"pushcount integer default 0,"+//��������
			"pushcount1 integer default 0,"+//��������
			"rcount integer default 0,"+//��������
			"rcount1 integer default 0,"+//��������
			"updatedate text"+//����ʱ��
			")";
	
	
	private int LetterCount;
	private int PushCount;
	private int RequirementCount;
	
	public SoSoCountInfo(){
		setLetterCount(0);
		setPushCount(0);
		setRequirementCount(0);
	}

	public int getLetterCount() {
		return LetterCount;
	}

	public void setLetterCount(int letterCount) {
		LetterCount = letterCount;
	}

	public int getPushCount() {
		return PushCount;
	}

	public void setPushCount(int pushCount) {
		PushCount = pushCount;
	}

	public int getRequirementCount() {
		return RequirementCount;
	}

	public void setRequirementCount(int requirementCount) {
		RequirementCount = requirementCount;
	}

	

	
	
}
