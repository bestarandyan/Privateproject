package com.zhihuigu.sosoOffice.model;


public class SoSoCountInfo {
	/**author by Ring
	 * 信件信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_countinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"userid text,"+//用户id
			"lettercount integer default 0,"+//站内信数字
			"pushcount integer default 0,"+//推送数字
			"pushcount1 integer default 0,"+//推送数字
			"rcount integer default 0,"+//需求数字
			"rcount1 integer default 0,"+//需求数字
			"updatedate text"+//更新时间
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
