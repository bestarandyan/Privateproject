package com.zhihuigu.sosoOffice.database;

import com.zhihuigu.sosoOffice.model.SoSoBuildInfo;
import com.zhihuigu.sosoOffice.model.SoSoBuildUserInfo;
import com.zhihuigu.sosoOffice.model.SoSoConfigurationInfo;
import com.zhihuigu.sosoOffice.model.SoSoContactInfo;
import com.zhihuigu.sosoOffice.model.SoSoCountInfo;
import com.zhihuigu.sosoOffice.model.SoSoCusReleaseInfo;
import com.zhihuigu.sosoOffice.model.SoSoDemandInfo;
import com.zhihuigu.sosoOffice.model.SoSoDistrictInfo;
import com.zhihuigu.sosoOffice.model.SoSoEffectiveInfo;
import com.zhihuigu.sosoOffice.model.SoSoFavoriteInfo;
import com.zhihuigu.sosoOffice.model.SoSoHelpInfo;
import com.zhihuigu.sosoOffice.model.SoSoImageInfo;
import com.zhihuigu.sosoOffice.model.SoSoLetterInInfo;
import com.zhihuigu.sosoOffice.model.SoSoLetterOutInfo;
import com.zhihuigu.sosoOffice.model.SoSoMetroInfo;
import com.zhihuigu.sosoOffice.model.SoSoOfficeInfo;
import com.zhihuigu.sosoOffice.model.SoSoPushInfo;
import com.zhihuigu.sosoOffice.model.SoSoSettingInfo;
import com.zhihuigu.sosoOffice.model.SoSoSmallintermediary;
import com.zhihuigu.sosoOffice.model.SoSoUserInfo;
import com.zhihuigu.sosoOffice.model.SoSoZoneInfo;
import com.zhihuigu.sosoOffice.model.VersionInfo;

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
	private static final String DATABASENAME = "sosooffice.db";
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
		db.execSQL(SoSoUserInfo.TABLE_CREATE);
		db.execSQL(SoSoZoneInfo.TABLE_CREATE);
		db.execSQL(SoSoDistrictInfo.TABLE_CREATE);
		db.execSQL(SoSoContactInfo.TABLE_CREATE);
		db.execSQL(SoSoLetterInInfo.TABLE_CREATE);
		db.execSQL(SoSoLetterOutInfo.TABLE_CREATE);
		db.execSQL(SoSoSettingInfo.TABLE_CREATE);
		db.execSQL(SoSoSmallintermediary.TABLE_CREATE);
		db.execSQL(SoSoBuildInfo.TABLE_CREATE);
		db.execSQL(SoSoOfficeInfo.TABLE_CREATE);
		db.execSQL(SoSoBuildUserInfo.TABLE_CREATE);
		db.execSQL(SoSoImageInfo.TABLE_CREATE);
		db.execSQL(SoSoFavoriteInfo.TABLE_CREATE);
		db.execSQL(SoSoPushInfo.TABLE_CREATE);
		db.execSQL(SoSoCusReleaseInfo.TABLE_CREATE);
		db.execSQL(SoSoDemandInfo.TABLE_CREATE);
		db.execSQL(SoSoMetroInfo.TABLE_CREATE);
		db.execSQL(SoSoHelpInfo.TABLE_CREATE);
		db.execSQL(SoSoCountInfo.TABLE_CREATE);
		db.execSQL(SoSoConfigurationInfo.TABLE_CREATE);
		db.execSQL(SoSoEffectiveInfo.TABLE_CREATE);
		db.execSQL(VersionInfo.TABLE_CREATE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
