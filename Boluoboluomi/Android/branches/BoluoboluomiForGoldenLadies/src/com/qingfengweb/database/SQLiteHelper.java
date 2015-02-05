package com.qingfengweb.database;
import com.qingfengweb.model.ActiveListInfo;
import com.qingfengweb.model.AppInfo;
import com.qingfengweb.model.CustomTypeInfo;
import com.qingfengweb.model.GoodsInfo;
import com.qingfengweb.model.HelpInfo;
import com.qingfengweb.model.ImgLocalData;
import com.qingfengweb.model.InstructionsInfo;
import com.qingfengweb.model.IntegralTypeInfo;
import com.qingfengweb.model.InvitationBean;
import com.qingfengweb.model.MerchanInfo;
import com.qingfengweb.model.MyCustomInfo;
import com.qingfengweb.model.MyIntegralInfo;
import com.qingfengweb.model.PersonInfo;
import com.qingfengweb.model.PictureInfo;
import com.qingfengweb.model.PictureThemes;
import com.qingfengweb.model.RecommendInfo;
import com.qingfengweb.model.SDKInfo;
import com.qingfengweb.model.StoreInfo;
import com.qingfengweb.model.SystemUpdateInfo;
import com.qingfengweb.model.TreasureInfo;
import com.qingfengweb.model.TreasureProductInfo;
import com.qingfengweb.model.UpLoadFileInfo;
import com.qingfengweb.model.UserInfo;
import com.qingfengweb.model.UserPhotoInfo;

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
		db.execSQL(ActiveListInfo.CreateTabSQL);
		db.execSQL(UserPhotoInfo.createTableSQL);
		db.execSQL(UpLoadFileInfo.CreateTabSQL);
		db.execSQL(TreasureInfo.CreateDBSQL);
		db.execSQL(MerchanInfo.CreateTableSQL);
		db.execSQL(IntegralTypeInfo.CreateTabSQL);
		db.execSQL(GoodsInfo.CreateTableSQL);
		db.execSQL(MyIntegralInfo.CreateTableSql);
		db.execSQL(TreasureProductInfo.CreateTableSQL);
		db.execSQL(PictureThemes.CreateTabelSQL);
		db.execSQL(PictureInfo.TABLE_CREATE);
		db.execSQL(RecommendInfo.CreateTableSQL);
		db.execSQL(PersonInfo.TABLE_CREATE);
		db.execSQL(CustomTypeInfo.CreateTableSQL);
		db.execSQL(MyCustomInfo.CreateTableSQL);
		db.execSQL(HelpInfo.TbCreateSQL);
		db.execSQL(ImgLocalData.createTbSQl);
		db.execSQL(InvitationBean.tbCreateSQL);
		
		db.execSQL(InstructionsInfo.TABLE_CREATE);
		db.execSQL(AppInfo.TABLE_CREATE);
		
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
