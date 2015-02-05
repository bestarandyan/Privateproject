package com.boluomi.children.database;
import com.boluomi.children.model.AppInfo;
import com.boluomi.children.model.GrowUpImgInfo;
import com.boluomi.children.model.GrowUpInfo;
import com.boluomi.children.model.HelpInfo;
import com.boluomi.children.model.InstructionsInfo;
import com.boluomi.children.model.MerchanInfo;
import com.boluomi.children.model.PictureInfo;
import com.boluomi.children.model.PictureThemes;
import com.boluomi.children.model.SDKInfo;
import com.boluomi.children.model.StoreInfo;
import com.boluomi.children.model.SystemUpdateInfo;
import com.boluomi.children.model.TreasureInfo;
import com.boluomi.children.model.TreasureProductInfo;
import com.boluomi.children.model.UpLoadFileInfo;
import com.boluomi.children.model.UserInfo;
import com.boluomi.children.model.UserPhotoInfo;

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
	private static final String DATABASENAME = "boluoboluomi.db";
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
		db.execSQL(SystemUpdateInfo.createSQL);
		db.execSQL(StoreInfo.TABLE_CREATE);
		db.execSQL(UserInfo.CreateTableSQL);
		db.execSQL(SDKInfo.TABLE_CREATE);
		db.execSQL(UserPhotoInfo.createTableSQL);
		db.execSQL(UpLoadFileInfo.CreateTabSQL);
		db.execSQL(TreasureInfo.CreateDBSQL);
		db.execSQL(MerchanInfo.CreateTableSQL);
		db.execSQL(TreasureProductInfo.CreateTableSQL);
		db.execSQL(PictureThemes.CreateTabelSQL);
		db.execSQL(PictureInfo.TABLE_CREATE);
		db.execSQL(HelpInfo.TbCreateSQL);
		db.execSQL(GrowUpImgInfo.createTableSQL);
		db.execSQL(GrowUpInfo.createTableSQL);
		
		db.execSQL(InstructionsInfo.TABLE_CREATE);
		db.execSQL(AppInfo.TABLE_CREATE);
		
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
