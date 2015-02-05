package com.qingfengweb.piaoguanjia.orderSystem.util;


import java.util.ArrayList;
import java.util.HashMap;

import com.qingfengweb.piaoguanjia.orderSystem.data.ConstantsValues;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RegionHelp {

	/***
	 * 省市区数据列表
	 * 
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> initData(Context context,
			String like,ArrayList<HashMap<String, Object>> list) {
		AssetsDatabaseManager.initManager(context);
		// // 获取管理对象，因为数据库需要通过管理对象才能够获取
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
		SQLiteDatabase db_region = mg.getDatabase(ConstantsValues.REGIONDBNAME);
		Cursor cursor = db_region.rawQuery(
				"SELECT * FROM region where layer like '" + like + "'", null);
		if(list==null){
			list = new ArrayList<HashMap<String, Object>>();
		}
		list.clear();
		int iColumnCount = cursor.getColumnCount();
		while (cursor.moveToNext()) {
			HashMap<String, Object> columuValues = new HashMap<String, Object>();
			for (int i = 0; i < iColumnCount; i++) {
				columuValues.put(cursor.getColumnName(i).toLowerCase(),
						cursor.getString(i));
			}
			list.add(columuValues);
		}
		return list;
	}
	
	
	/***
	 * 获取名字
	 * 
	 * @return
	 */
	public static String getName(Context context,String sql) {
		AssetsDatabaseManager.initManager(context);
		// // 获取管理对象，因为数据库需要通过管理对象才能够获取
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
		SQLiteDatabase db_region = mg.getDatabase(ConstantsValues.REGIONDBNAME);
		Cursor cursor = db_region.rawQuery(sql, null);
		int iColumnCount = cursor.getColumnCount();
		while (cursor.moveToNext()) {
			for (int i = 0; i < iColumnCount; i++) {
				if(cursor.getColumnName(i).equals("name")){
					return cursor.getString(i);
				}
			}
		}
		return "";
	}
	
	/***
	 * 获取名字
	 * 
	 * @return
	 */
	public static String getName1(Context context,String name) {
		AssetsDatabaseManager.initManager(context);
		// // 获取管理对象，因为数据库需要通过管理对象才能够获取
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
		SQLiteDatabase db_region = mg.getDatabase(ConstantsValues.REGIONDBNAME);
		Cursor cursor = db_region.rawQuery("SELECT layer FROM region where name like '%"+name+"%'", null);
		int iColumnCount = cursor.getColumnCount();
		while (cursor.moveToNext()) {
			for (int i = 0; i < iColumnCount; i++) {
				if(cursor.getColumnName(i).equals("layer")){
					return cursor.getString(i);
				}
			}
		}
		return "";
	}
	
	/***
	 * 省市区数据列表
	 * 
	 * @return
	 */
	public static int getID(Context context,String sql) {
		AssetsDatabaseManager.initManager(context);
		// // 获取管理对象，因为数据库需要通过管理对象才能够获取
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
		SQLiteDatabase db_region = mg.getDatabase(ConstantsValues.REGIONDBNAME);
		Cursor cursor = db_region.rawQuery(sql, null);
		int iColumnCount = cursor.getColumnCount();
		while (cursor.moveToNext()) {
			for (int i = 0; i < iColumnCount; i++) {
				if(cursor.getColumnName(i).equals("id")){
					return cursor.getInt(i);
				}
			}
		}
		return -1;
	}
	
	public static String NameToString(Context context,String layer){
		String s = "";
		if(layer.length()>=6){
			s+=getName(context, "SELECT name FROM region where layer = '" + layer.substring(0, 6) + "'")+" ";	
			if((s.trim().startsWith("上海"))
					||(s.trim().startsWith("北京"))
					||(s.trim().startsWith("天津"))
					||(s.trim().startsWith("重庆")))
				s="";
		}
		if(layer.length()>=9){
			s+=getName(context, "SELECT name FROM region where layer = '" + layer.substring(0, 9) + "'")+" ";;
		}
		if(layer.length()>=12)
			s+=getName(context, "SELECT name FROM region where layer = '" + layer.substring(0, 12) + "'");
		return s;
	}
	
	public static String NameToString1(Context context,String name){
		String layer = getName1(context, name);
		String s = "";
		if(layer.length()>=6){
			s+=getName(context, "SELECT name FROM region where layer = '" + layer.substring(0, 6) + "'");	
		}
		if(layer.length()>=9){
			s+=" "+getName(context, "SELECT name FROM region where layer = '" + layer.substring(0, 9) + "'");
		}
		if(layer.length()>=12)
			s+= " "+getName(context, "SELECT name FROM region where layer = '" + layer.substring(0, 12) + "'");
		return s;
	}
}
