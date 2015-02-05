package com.qingfengweb.constant;

public class DatabaseSql {
	public static final String BRAND_TABLENAME_STRING = "brand";//品牌罗列表名
	public static final String FLOOR_BRAND_TABLENAME_STRING = "floor_brand";//品牌罗列表名
	public static final String CUXIAO_TABLENAME_STRING = "cuxiao";//促销活动表名
	public static final String FOOD_TABLENAME_STRING = "foods";//美食速递表名
	public static final String SPECIALGOODS_TABLENAME_STRING = "specialGoods";//特惠商品表
//	public static final String LAST_UPDATE_TIME_STRING = "last_upadate_time";//最后更新时间表名
	
//	public static final String LAST_UPDATE_TIME_SQL_STRING ="create table if not exists "+LAST_UPDATE_TIME_STRING
//			+"(_id integer primary key autoincrement," +
//			"update_name varchar(40)," +
//			"update_time varchar(40))";
//	
	public static final String BRAND_SQL_STRING = "create table if not exists "+BRAND_TABLENAME_STRING
			+"(_id integer primary key autoincrement," +
			"brand_id varchar(40)," +
			"floor_id varchar(40)," +
			"brand_name varchar(60)," +
			"floor_sort integer," +
			"floor_name varchar(60))";
	
	public static final String FLOOR_BRAND_SQL_STRING = "create table if not exists "+FLOOR_BRAND_TABLENAME_STRING
			+"(_id integer primary key autoincrement," +
			"cat_id varchar(40)," +
			"floor_id varchar(40)," +
			"cat_name varchar(60)," +
			"floor_sort integer," +
			"floor_name varchar(60))";
	
	public static final String CUXIAO_SQL_STRING = "create table if not exists "+CUXIAO_TABLENAME_STRING
			+"(_id integer primary key autoincrement," +
			"title varchar(60)," +
			"brand_name varchar(40)," +
			"floor_name varchar(60)," +
			"brand_id varchar(40)," +
			"floor_id varchar(60)," +
			"promotion_type varchar(10)," +
			"promotion_url varchar(80)," +
			"info_id varchar(60)," +
			"end_date varchar(60)," +
			"start_date varchar(60))";
	
	public static final String FOOD_SQL_STRING = "create table if not exists "+FOOD_TABLENAME_STRING+
			"(_id integer primary key autoincrement," +
			"condition varchar(40)," +
			"brand_name varchar(40)," +
			"brand_id varchar(20)," +
			"floor_name varchar(20)," +
			"floor_id varchar(20)," +
			"food_tel varchar(30)," +
			"food_tel2 varchar(30)," +
			"food_pic varchar(80)," +
			"delivery_time1 varchar(40)," +
			"delivery_time2 varvhar(40)," +
			"delivery_time3 varchar(40)," +
			"info_id varchar(40)" +
			")";
	
	public static final String SPECIALGOODS_SQL_STRING = "create table if not exists "+SPECIALGOODS_TABLENAME_STRING+
			"(_id integer primary key autoincrement," +
			"title varchar(40)," +
			"brand_name varchar(40)," +
			"brand_id varchar(40)," +
			"floor_name varchar(20)," +
			"floor_id varchar(20)," +
			"special_pic varchar(80)," +
			"img_sd_url varchar(80)," +
			"special_url varchar(80)," +
			"price varchar(30)," +
			"goods_no varchar(40)," +
			"start_date varvhar(40)," +
			"end_date varchar(40)," +
			"info_id varchar(40)" +
			")";
}
