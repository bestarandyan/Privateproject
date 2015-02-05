package com.qingfengweb.model;

public class ClassInfo {
	/**
	 * author:Ring
	 * 积分商城商品类别表
	 */
	public static String TABLE_CREATE = "create table " +
			"classinfo" +//表名 goodsclass
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"id text,"+//类别编号
			"name text,"+//类别名称
			"thumbsdpath text"+//缩略图sd位置
			")";
	
	private String typeid;
	private String name;
	private int ranking;
	private int deleted;
	
	
	public ClassInfo(){
		setName("");
		setRanking(0);
		setTypeid("");
		setDeleted(0);
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
	
	
}
