package com.qingfengweb.pinhuo.datamanage;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class SQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "qingfengweb_lottery.db";
	private static final int VERSION = 1;
	public SQLiteHelper(Context context) {
		super(context, DATABASENAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL();
//		db.execSQL();
//		db.execSQL();
//		db.execSQL();
//		db.execSQL();
//		db.execSQL();
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
