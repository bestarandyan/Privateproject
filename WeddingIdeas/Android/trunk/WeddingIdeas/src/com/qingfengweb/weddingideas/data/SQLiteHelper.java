package com.qingfengweb.weddingideas.data;

import com.qingfengweb.weddingideas.activity.PreferenceActivity;
import com.qingfengweb.weddingideas.beans.HuoDongBean;
import com.qingfengweb.weddingideas.beans.PreferenceBean;
import com.qingfengweb.weddingideas.beans.TaoXiBean;
import com.qingfengweb.weddingideas.beans.UserBean;
import com.qingfengweb.weddingideas.beans.WeddingLogBean;
import com.qingfengweb.weddingideas.beans.YangZhaoBean;
import com.qingfengweb.weddingideas.beans.YingLouBean;

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
	private static final String DATABASENAME = "weddingideas.db";
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
		db.execSQL(UserBean.createTbSQL);
		db.execSQL(YingLouBean.tbCreateSql);
		db.execSQL(YangZhaoBean.tbCreateSql);
		db.execSQL(TaoXiBean.tbCreateSql);
		db.execSQL(HuoDongBean.tbCreateSql);
		db.execSQL(WeddingLogBean.tbCreateSql);
		db.execSQL(PreferenceBean.createSql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
