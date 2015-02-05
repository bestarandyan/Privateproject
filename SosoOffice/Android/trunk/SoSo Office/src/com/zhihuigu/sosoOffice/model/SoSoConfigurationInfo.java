package com.zhihuigu.sosoOffice.model;

public class SoSoConfigurationInfo {
	/*SERVER_TIME	服务器时间
	TLETTERS_TIME	站内信表最新更新时间
	TFAVORITES_TIME	收藏表最新更新时间
	TDISTRICT_TIME	商圈表最新更新时间
	TBUILD_TIME	楼盘表最新更新时间
	THELPDOCUMENT_TIME	系统帮助表最新更新时间
	TMETRO_TIME	地铁表最新更新时间
	TOFFICE_TIME	房源表最新更新时间
	TPUSH_TIME	推送表最新更新时间
	TVISITOFFICE_TIME	房源访问表最新更新时间
	TUSERCONACT_TIME	用户联系人表最新更新时间
	TCUSRELEASE_TIME	客户需求表最新时间*/


	public static String TABLE_CREATE = "create table " +
			"soso_configurationinfo" +//表名 sosoconfigurationinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"userid text,"+//用户id
			"name text,"+//配置中key值
			"value text,"+//配置中的value值
			"updatedate text,"+//更新配置的时间
			"updatedate1 text,"+//辅助时间1
			"updatedate2 text"+//辅助时间2
			")";
	
	
	private String Name;
	private String Value;
	/**
	 * 模型初始化
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
