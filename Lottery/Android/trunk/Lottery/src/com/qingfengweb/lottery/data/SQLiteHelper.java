package com.qingfengweb.lottery.data;

import com.qingfengweb.lottery.bean.ChargeHistoryBean;
import com.qingfengweb.lottery.bean.LastResultBean;
import com.qingfengweb.lottery.bean.OrderBean;
import com.qingfengweb.lottery.bean.ResultInfo;
import com.qingfengweb.lottery.bean.UserBalanceBean;
import com.qingfengweb.lottery.bean.UserBean;

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
		db.execSQL(UserBean.tbCreateSql);
		db.execSQL(ChargeHistoryBean.tbCreateSQL);
		db.execSQL(OrderBean.tbCreateSql);
		db.execSQL(ResultInfo.tbCreateSQl);
		db.execSQL(UserBalanceBean.tabCreateSql);
		db.execSQL(LastResultBean.tbCreateSQl);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
