package com.zhihuigu.sosoOffice.model;

public class SoSoMetroInfo {
	/**author by Ring
	 * 业主附带表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_metroinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"metroid text,"+//地铁线路id
			"cityid text,"+//城市id
			"metroname text,"+//地铁名称
			"updatetime text"+//最新更新时间
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
