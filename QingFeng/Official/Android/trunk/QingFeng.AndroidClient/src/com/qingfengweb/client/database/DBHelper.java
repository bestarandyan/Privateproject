package com.qingfengweb.client.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class DBHelper {
	private static final String TAG = "DBHelper";
	private static DBHelper instance;
	private SQLiteDatabase db = null;
	private SQLiteHelper mSQLiteHelper;
	/**
	 * ���ݲ�����
	 */
	private static Object OBJECTLOCK = null;

	/**
	 * �������ݿ�ʵ��
	 */
	private DBHelper(Context context) {
		mSQLiteHelper = new SQLiteHelper(context);
		OBJECTLOCK = new Object();
	}

	/**
	 * ��ȡdbhelperʵ��
	 * 
	 * @return DBHelper���ݿ������
	 */
	public static DBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DBHelper(context);
		}
		return instance;
	}

	/**
	 * ִ��sql
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
	 * ִ��sql
	 * 
	 * @param sql
	 *            sql���
	 * @param bindArgs
	 *            sql����
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
	 * ɾ����
	 * 
	 * @param tableName
	 *            ����
	 * @return booolean �Ƿ�ִ�гɹ�
	 */
	public boolean dropTable(String tableName) {
		return execSql("DROP TABLE IF EXISTS " + tableName);
	}

	/**
	 * ��ѯ��������
	 * 
	 * @param tableName
	 *            ������
	 * @param fieldList
	 *            ��ѯ���ֶ���","�ָ�
	 * @param orderBy
	 *            ������ֶ� return List<Map<String, Object>> ���ؽ����
	 */
	@SuppressLint("DefaultLocale")
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
				int iColumnCount = cursor.getColumnCount();
				while (cursor.moveToNext()) {
					Map<String, Object> columuValues = new HashMap<String, Object>();
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
			if(cursor!=null){
				cursor.close();
			}
		}
		return list;
	}

	/**
	 * �������
	 * 
	 * @param tableName
	 *            ������
	 * @param contentValues
	 *            ��װֵkey-value
	 * @return long ���ر��
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
	 *            ������
	 * @param whereClause
	 *            where ��䣿
	 * @param whereArgs
	 *            ��Ӧwhere������ return boolean �Ƿ�ִ�гɹ�
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
	public boolean delete(String table) {
		open();
		boolean isSuccess = false;
		try {
			if ((table != null && table.length() > 0)) {
				db.delete(table, null, null);
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
	 * ��������
	 * 
	 * @param table
	 *            ����
	 * @param values
	 *            ֵ
	 * @param whereClause
	 *            where ����
	 * @param whereArgs
	 *            where����
	 * @return boolean �Ƿ�ִ�гɹ�
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
	 * ����sql����ȡ���ݣ�
	 * 
	 * @param sql
	 *            ���
	 * @param selectionArgs
	 *            ��ʾ���ֶ�
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

	public SQLiteDatabase open() {
		synchronized (OBJECTLOCK) {
			db = mSQLiteHelper.getWritableDatabase();
		}
		return db;
	}
}
