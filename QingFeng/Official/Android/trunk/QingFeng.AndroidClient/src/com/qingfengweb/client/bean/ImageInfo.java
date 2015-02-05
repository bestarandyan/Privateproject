/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author Ring
 *图片信息
 */
public class ImageInfo {
	/**author by Ring
	 * 图片信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"imageinfo" +//表名 
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"imageid text,"+//案件id
			"xid text,"+//图片相关id
			"tag integer default 0,"+//默认存在的数据为1
			"sd text,"+//大图位置
			"sd_thumb text,"+//缩略图位置
			"createtime text,"+//创建时间
			"describe text"+//图片描述
			")";
	
}
