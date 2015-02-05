package com.chinaLife.claimAssistant.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinaLife.claimAssistant.activity.Sc_MyApplication;
import com.chinaLife.claimAssistant.bean.sc_AppInfo;
import com.chinaLife.claimAssistant.bean.sc_Caseinfo;
import com.chinaLife.claimAssistant.bean.sc_ClaimInfo;
import com.chinaLife.claimAssistant.bean.sc_ClaimPhotoInfo;
import com.chinaLife.claimAssistant.bean.sc_HelpInfo;
import com.chinaLife.claimAssistant.bean.sc_LegendInfo;
import com.chinaLife.claimAssistant.bean.sc_LogInfo;
import com.chinaLife.claimAssistant.bean.sc_MessageInfo;
import com.chinaLife.claimAssistant.bean.sc_NoticeInfo;
import com.chinaLife.claimAssistant.bean.sc_SynDataTime;
import com.chinaLife.claimAssistant.bean.sc_UploadBlockInfo;
import com.chinaLife.claimAssistant.bean.sc_UploadInfo;
import com.chinaLife.claimAssistant.bean.sc_UserInfo;
import com.chinaLife.claimAssistant.content.sc_Contentvalues;
import com.sqlcrypt.database.AssetsDatabaseManager;
import com.sqlcrypt.database.ContentValues;
import com.sqlcrypt.database.Cursor;
import com.sqlcrypt.database.sqlite.SQLiteDatabase;

import android.util.Log;

public class sc_DBHelper {
	private static final String TAG = "DBHelper";
	private static sc_DBHelper instance;
	private SQLiteDatabase db = null;
	/**
	 * 数据操作锁
	 */
	private static Object OBJECTLOCK = null;
	public static String DATABASESECRET = "sffasjskiidnfsfjkfsdfdkjfdsfids";

	/**
	 * 构造数据库实例
	 */
	private sc_DBHelper() {
		AssetsDatabaseManager.initManager(Sc_MyApplication.getInstance()
				.getContext2());
		OBJECTLOCK = new Object();
		try {
			AssetsDatabaseManager manager = AssetsDatabaseManager.getManager();
			if (db == null || !db.isOpen()) {
				db = manager.getDatabase("chinalife.db.sqlite", DATABASESECRET);
			}
			if (db == null || !db.isOpen()) {
				db = manager.getDatabase("chinalife.db.sqlite", "");
				db.resetPassword(DATABASESECRET);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			db.execSQL(sc_AppInfo.TABLE_CREATE);
			db.execSQL(sc_UserInfo.TABLE_CREATE);
			db.execSQL(sc_Caseinfo.TABLE_CREATE);
			db.execSQL(sc_ClaimInfo.TABLE_CREATE);
			db.execSQL(sc_ClaimPhotoInfo.TABLE_CREATE);
			db.execSQL(sc_UploadInfo.TABLE_CREATE);
			db.execSQL(sc_UploadBlockInfo.TABLE_CREATE);
			db.execSQL(sc_LegendInfo.TABLE_CREATE);
			db.execSQL(sc_SynDataTime.TABLE_CREATE);
			db.execSQL(sc_MessageInfo.TABLE_CREATE);
			db.execSQL(sc_NoticeInfo.TABLE_CREATE);
			db.execSQL(sc_HelpInfo.TABLE_CREATE);
			db.execSQL(sc_LogInfo.TABLE_CREATE);
			int i;
			ContentValues values = new ContentValues();
			for (i = 1; i <= 13; i++) {
				values.clear();
				values.put("legendid", i);
				values.put("legendimageurl", "");
				values.put("legendimagesd", sc_Contentvalues.LegendImage[i - 1]);
				values.put("legendtext", sc_Contentvalues.Legendtext[i - 1]);
				values.put("maskimageurl", "");
				values.put("maskimagesd", sc_Contentvalues.Legend1Image[i - 1]);
				values.put("masktext", sc_Contentvalues.Legendtext1[i - 1]);
				db.insert("legendinfo", null, values);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 获取dbhelper实例
	 * 
	 * @return DBHelper数据库操作类
	 */
	public static sc_DBHelper getInstance() {
		if (instance == null) {
			instance = new sc_DBHelper();
		}
		return instance;
	}

	/**
	 * 执行sql
	 * 
	 * @param sql
	 * @return boolean
	 */
	public boolean execSql(String sql) {
		boolean isSuccess = false;
		try {
			if (sql != null && sql.length() > 0) {
				open();
				db.execSQL(sql);
				isSuccess = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "exec sql error:" + e.getMessage());
		}
		return isSuccess;
	}

	/**
	 * 执行sql
	 * 
	 * @param sql
	 *            sql语句
	 * @param bindArgs
	 *            sql参数
	 * @return boolean
	 */
	public boolean execSql(String sql, Object[] bindArgs) {
		boolean isSuccess = false;
		try {
			if (sql != null && sql.length() > 0) {
				open();
				db.execSQL(sql, bindArgs);
				isSuccess = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "exec sql error:" + e.getMessage());
		}
		return isSuccess;
	}

	/**
	 * 删除表
	 * 
	 * @param tableName
	 *            表名
	 * @return booolean 是否执行成功
	 */
	public boolean dropTable(String tableName) {
		return execSql("DROP TABLE IF EXISTS " + tableName);
	}

	/**
	 * 查询所有数据
	 * 
	 * @param tableName
	 *            表名称
	 * @param fieldList
	 *            查询的字段以","分隔
	 * @param orderBy
	 *            排序的字段 return List<Map<String, Object>> 返回结果集
	 */
	public List<Map<String, Object>> selectAllRows(String tableName,
			String fieldList, String orderBy) {
		open();
		List<Map<String, Object>> list = null;
		String[] fieldArray = null;
		Cursor cursor = null;
		try {
			if ((tableName != null && tableName.length() > 0)
					&& (fieldList != null && fieldList.length() > 0)) {
				list = new ArrayList<Map<String, Object>>();
				if (!fieldList.equals("*")) {
					fieldArray = fieldList.split(",");
				}
				cursor = db.query(tableName, fieldArray, null, null, null,
						null, orderBy);
				cursor.moveToFirst();
				int iColumnCount = cursor.getColumnCount();
				while (cursor.moveToNext()) {
					Map<String, Object> columuValues = null;
					columuValues = new HashMap<String, Object>();
					for (int i = 0; i < iColumnCount; i++) {
						columuValues.put(cursor.getColumnName(i).toLowerCase(),
								cursor.getString(i));
					}
					list.add(columuValues);
				}
			}
			// Log.i(TAG, "TableName:" + tableName + ", fieldList:" + fieldList
			// + " order by " + orderBy);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "select data error:" + e.getMessage());
		} finally {
			cursor.close();
		}
		return list;
	}

	/**
	 * 添加数据
	 * 
	 * @param tableName
	 *            表名称
	 * @param contentValues
	 *            封装值key-value
	 * @return long 返回编号
	 */
	public long insert(String tableName, ContentValues contentValues) {
		open();
		long isSuccess = -1;
		try {
			if ((tableName != null && tableName.length() > 0)
					&& (contentValues != null && contentValues.size() > 0)) {
				isSuccess = db.insert(tableName, null, contentValues);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "create Data error:" + e.getMessage());
		}
		return isSuccess;
	}

	/**
	 * @param table
	 *            表名称
	 * @param whereClause
	 *            where 语句？
	 * @param whereArgs
	 *            对应where语句参数 return boolean 是否执行成功
	 * 
	 */
	public boolean delete(String table, String whereClause, String[] whereArgs) {
		open();
		boolean isSuccess = false;
		try {
			if ((table != null && table.length() > 0)
					&& (whereClause != null && whereClause.length() > 0)
					&& (whereArgs != null && whereArgs.length > 0)) {
				db.delete(table, whereClause, whereArgs);
				isSuccess = true;
			}
			// Log.i(TAG, "TableName:" + table + ",fieldList:" + whereClause
			// + ",values:" + whereArgs.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "delete data error:" + e.getMessage());
		}
		return isSuccess;
	}

	/***
	 * 更新内容
	 * 
	 * @param table
	 *            表名
	 * @param values
	 *            值
	 * @param whereClause
	 *            where 条件
	 * @param whereArgs
	 *            where参数
	 * @return boolean 是否执行成功
	 */
	public boolean update(String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		open();
		boolean isSuccess = false;
		try {
			if (table != null && table.length() > 0) {
				db.update(table, values, whereClause, whereArgs);
				isSuccess = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "update data error:" + e.getMessage());
		}
		return isSuccess;
	}

	/***
	 * 根据sql语句获取数据；
	 * 
	 * @param sql
	 *            语句
	 * @param selectionArgs
	 *            显示的字段
	 * @return List<Map<String, Object>>
	 */

	public List<Map<String, Object>> selectRow(String sql,
			String[] selectionArgs) {
		open();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Cursor mCursor = null;
		try {
			mCursor = db.rawQuery(sql, selectionArgs);
			Map<String, Object> map = null;
			int iColumnCount = mCursor.getColumnCount();
			while (mCursor.moveToNext()) {
				map = new HashMap<String, Object>();
				for (int i = 0; i < iColumnCount; i++) {
					map.put(mCursor.getColumnName(i).toLowerCase(),
							mCursor.getString(i));
				}
				result.add(map);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"selectRow error:" + sql + "\nException:" + e.getMessage());
		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
		}
		return result;
	}

	public void close() {
		synchronized (OBJECTLOCK) {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}

	public void open() {
		synchronized (OBJECTLOCK) {
			try {
				AssetsDatabaseManager manager = AssetsDatabaseManager.getManager();
				if (db == null || !db.isOpen()) {
					db = manager.getDatabase("chinalife.db.sqlite", DATABASESECRET);
				}
				if (db == null || !db.isOpen()) {
					db = manager.getDatabase("chinalife.db.sqlite", "");
					db.resetPassword(DATABASESECRET);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
