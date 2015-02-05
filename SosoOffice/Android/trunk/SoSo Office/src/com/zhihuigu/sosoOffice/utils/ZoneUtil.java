package com.zhihuigu.sosoOffice.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhihuigu.sosoOffice.database.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ZoneUtil {
//	static SQLiteDatabase db;
	private static final String TAG = "region.sqlite.sqlite";
	
	private static String DATABASE_FILENAME = "region.sqlite.sqlite";

	// 初始化数据库
	private static SQLiteDatabase openDatabase(Context context) {
		final String DATABASE_PATH = android.os.Environment
				.getDataDirectory().getAbsolutePath() + "/data/"+context.getPackageName();
		try {
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			File dir = new File(databaseFilename);
			if(!dir.exists()&&!dir.isFile()){
//				dir.mkdirs();
			}
			if (!dir.exists()&&!dir.isFile()) {
				InputStream is = context.getAssets().open(DATABASE_FILENAME);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			SQLiteDatabase db = null;
			db = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
//			importData(db,context);
			return db;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
//	/**
//	 * 将本地数据库的商圈数据导入外部数据库
//	 */
//	
//	public static void importData(SQLiteDatabase db,Context context){
//		List<Map<String, Object>> selectresult = null;
//		selectresult = DBHelper.getInstance(context).selectRow(
//				"select * from soso_districtinfo",
//				null);
//		ContentValues values = new ContentValues();
//		if (selectresult != null) {
//			if (selectresult.size()> 0) {
//				int i;
//				List<Map<String, Object>> selectresult1 = null;
//				for(i=0;i<selectresult.size();i++){
//					if(selectresult.get(i).get("districtid")!=null
//							&&selectresult.get(i).get("districtmc")!=null
//							&&selectresult.get(i).get("areaid")!=null){
//						values.put("id", selectresult.get(i).get("districtid").toString());
//						values.put("name",selectresult.get(i).get("districtmc").toString());
//						values.put("regionid",selectresult.get(i).get("areaid").toString());
//						try{
//							values.put("longitude", selectresult.get(i).get("longitude").toString());
//						}catch(Exception e){
//							values.put("longitude", "");
//						}
//						try{
//							values.put("latitude", selectresult.get(i).get("latitude").toString());
//						}catch(Exception e){
//							values.put("latitude", "");
//						}
//						selectresult1 = selectRow(db,"select * from district where id='"+selectresult.get(i).get("districtid").toString()+"'",
//								null);
//						if (selectresult1 != null) {
//							if (selectresult1.size() <= 0) {
//								db.insert("district", null, values);
//							} else {
//								db.update("district", values, "id=?", new String[] {selectresult.get(i).get("districtid").toString()});
//							}
//							selectresult1.clear();
//							selectresult1 = null;
//						}
//						values.clear();
//					}
//					
//				}
//			}
//			if (values != null) {
//				values.clear();
//				values = null;
//			}
//			if (selectresult != null) {
//				selectresult.clear();
//				selectresult = null;
//			}
//		}
//	}
//	
//	/***
//	 * 根据sql语句获取数据；
//	 * 
//	 * @param sql
//	 *            语句
//	 * @param selectionArgs
//	 *            显示的字段
//	 * @return List<Map<String, Object>>
//	 */
//
//	public static List<Map<String, Object>> selectRow(SQLiteDatabase db,String sql,
//			String[] selectionArgs) {
//		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//		Cursor mCursor = null;
//		try {
//			mCursor = db.rawQuery(sql, selectionArgs);
//			Map<String, Object> map = null;
//			int iColumnCount = mCursor.getColumnCount();
//			while (mCursor.moveToNext()) {
//				map = new HashMap<String, Object>();
//				for (int i = 0; i < iColumnCount; i++) {
//					map.put(mCursor.getColumnName(i).toLowerCase(),
//							mCursor.getString(i));
//				}
//				result.add(map);
//			}
//		} catch (Exception e) {
//			Log.e(TAG,
//					"selectRow error:" + sql + "\nException:" + e.getMessage());
//		} finally {
//			if (mCursor != null) {
//				mCursor.close();
//			}
//		}
//		return result;
//	}

	/**
	 * 查询省市区，和商圈楼盘信息
	 * 
	 * @param str
	 *            查询的sql语句
	 * @param context
	 *            上下文
	 * @return 查询结果（name ,code,parentid）
	 */
	public static List<Map<String, Object>> getregion(Context context, String sql) {

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Cursor mCursor = null;
		SQLiteDatabase db = null;
		try {
			db = openDatabase(context);
			mCursor = db.rawQuery(sql, null);
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
			Log.e("bank_dbhelp",
					"selectRow error:" + sql + "\nException:" + e.getMessage());
		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return result;
	}
	
	/**
	 * 查询省市区，和商圈楼盘信息联动查询
	 * 
	 * @param str
	 *            查询的sql语句
	 * @param context
	 *            上下文
	 * @return 查询结果（name ,code,parentid）
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<Integer, List> getregionlist(Context context, String sql) {

		SQLiteDatabase db = null;
		Cursor c = null;
		Map<Integer, List> provinceData = new HashMap<Integer, List>();
		// List provinceList = null;
		try {
			db = openDatabase(context);
			c = db.rawQuery(sql, null);
			List provinceList1 = new ArrayList();
			List provinceList2 = new ArrayList();
			while (c.moveToNext()) {
				Map provinceMap = new HashMap();
				provinceMap.put(c.getString(1), c.getInt(0));
				provinceList1.add(provinceMap);
				provinceList2.add(c.getString(1));
			}
			provinceData.put(0, provinceList1);
			provinceData.put(1, provinceList2);
		} catch (Exception e) {
			Log.d("WineStock", "getProvince:" + e.getMessage());
		} finally {
			if (c != null) {
				c.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return provinceData;
	}
	
	public static String getName(Context context, String id) {

		Cursor mCursor = null;
		String sql = "select name from district where id = "+id;
		String name = "";
		SQLiteDatabase db = null;
		try {
			db = openDatabase(context);
			mCursor = db.rawQuery(sql, null);
			if(mCursor!=null&&mCursor.getColumnCount()>0){
				while (mCursor.moveToNext()) {
					name = mCursor.getString(0);
					break;
				}
			}
		} catch (Exception e) {
			Log.e("bank_dbhelp",
					"selectRow error:" + sql + "\nException:" + e.getMessage());
		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return name;
	}
	
	public static String getName(Context context, String id,int type) {

		Cursor mCursor = null;
		String sql = "select name from region where type = '"+type+"' and id = "+id;
		String name = "";
		SQLiteDatabase db = null;
		try {
			db = openDatabase(context);
			mCursor = db.rawQuery(sql, null);
			if(mCursor!=null&&mCursor.getColumnCount()>0){
				while (mCursor.moveToNext()) {
					name = mCursor.getString(0);
					break;
				}
			}
		} catch (Exception e) {
			Log.e("bank_dbhelp",
					"selectRow error:" + sql + "\nException:" + e.getMessage());
		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return name;
	}
}
