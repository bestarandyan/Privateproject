package com.qingfengweb.client.database;

import com.qingfengweb.client.bean.CaseInfo;
import com.qingfengweb.client.bean.CompanyInfo;
import com.qingfengweb.client.bean.ConnectWayInfo;
import com.qingfengweb.client.bean.ContentUpdateInfo;
import com.qingfengweb.client.bean.JobListInfo;
import com.qingfengweb.client.bean.ListCaseInfo;
import com.qingfengweb.client.bean.DetailServicesInfo;
import com.qingfengweb.client.bean.DeviceInfo;
import com.qingfengweb.client.bean.HomeImageInfo;
import com.qingfengweb.client.bean.ImageInfo;
import com.qingfengweb.client.bean.PositionInfo;
import com.qingfengweb.client.bean.ServicesInfo;
import com.qingfengweb.client.bean.StaffInfo;
import com.qingfengweb.client.bean.TeamInfo;
import com.qingfengweb.client.bean.UpLoadFileInfo;
import com.qingfengweb.client.bean.UpdateSystemTime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author
 * database 管理
 */
public class SQLiteHelper extends SQLiteOpenHelper {
	/**
	 * 数据库名称
	 */
	private static final String DATABASENAME = "qingfengweb.db";
	/**
	 *数据库版本
	 */
	private static final int VERSION = 1;
	/**
	 * 构造数据库
	 * @param context
	 */
	public SQLiteHelper(Context context) {
		super(context, DATABASENAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DeviceInfo.TABLE_CREATE_SQL);//创建设备表
		db.execSQL(UpdateSystemTime.TABLE_CREATE_SQL);//创建更新机制表
		db.execSQL(DetailServicesInfo.createTableSQL);//创建服务详情表
		db.execSQL(HomeImageInfo.createTableSQL);//创建首页图片信息表
		db.execSQL(ContentUpdateInfo.createTableSQL);//创建内容更新表
		db.execSQL(CompanyInfo.createTableSQL);//创建公司介绍表
		db.execSQL(TeamInfo.createTableSQL);//创建团队成员信息表
		db.execSQL(CaseInfo.createTableSQL);//创建客户案例列表信息表
		db.execSQL(JobListInfo.createTableSQL);//创建职位列表信息表
		db.execSQL(UpLoadFileInfo.CreateTabSQL);//创建文件上传表
		db.execSQL(ServicesInfo.createTabSQL);//创建服务项目表
		db.execSQL(ConnectWayInfo.createTabSQL);
//		db.execSQL(StaffInfo.TABLE_CREATE);//员工表
//		db.execSQL(ListCaseInfo.CREATE_TABLE_SQL);//案例表
//		db.execSQL(ImageInfo.TABLE_CREATE);//图片信息表
//		db.execSQL(PositionInfo.TABLE_CREATE);//职位表
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
