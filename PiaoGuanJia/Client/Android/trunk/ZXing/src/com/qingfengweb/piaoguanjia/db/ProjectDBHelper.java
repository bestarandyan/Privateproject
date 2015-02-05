package com.qingfengweb.piaoguanjia.db;
import com.qingfengweb.piaoguanjia.content.DbContent;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ProjectDBHelper extends SQLiteOpenHelper{

	private static final int DBVERSION=1;
	public ProjectDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public ProjectDBHelper(Context context, String name,
			int version) {
		this(context,name,null,version);
		
		// TODO Auto-generated constructor stub
	}
	public ProjectDBHelper(Context context, String name) {
		this(context, name, DBVERSION);
		// TODO Auto-generated constructor stub
	}
	public ProjectDBHelper(Context context) {
		this(context, DbContent.DBNAME);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//if(!tabIsExist(DbContent.USER_TABLE_NAME, db)){
			db.execSQL(UserBean.getInstence().sql);
		///}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
}
