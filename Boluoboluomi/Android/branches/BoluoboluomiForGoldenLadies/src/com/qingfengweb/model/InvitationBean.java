/**
 * 
 */
package com.qingfengweb.model;

/**
 * @author 刘星星
 * @createDate 2013/12/19
 *
 */
public class InvitationBean {
	public static String tbName = "invitationinfos";
	public static String tbCreateSQL = "create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"id text," +//电子请帖编号
			"name text," +//电子请帖名称
			"thumb text," +//模板缩略图
			"ranking Integer," +//排列顺序
			"storeid Integer," +//店铺编号
			"deleted varchar(10))";//是否已经删除
	public String id = "";
	public String name = "";
	public String thumb = "";
	public String ranking ="";
	public String deleted ="";
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
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
}
