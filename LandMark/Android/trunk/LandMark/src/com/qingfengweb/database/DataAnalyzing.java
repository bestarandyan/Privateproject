package com.qingfengweb.database;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.qingfengweb.bean.BrandBean;
import com.qingfengweb.bean.BrandBean;
import com.qingfengweb.bean.FloorDetailBean;
import com.qingfengweb.bean.FoodBean;
import com.qingfengweb.bean.PromotionBean;
import com.qingfengweb.bean.ListResBean;
import com.qingfengweb.bean.SpecialBean;
import com.qingfengweb.constant.DatabaseSql;

public class DataAnalyzing {
	/**
	 * 根据服务器返回的json数据解析成两个对象
	 * 第一个为获取到的数据的属性
	 * 第二个为获取到的数据的对象的具体数据
	 * @return 将两个大的对象放在一个集合中返回给程序
	 */
	public static ArrayList<String> analyzingData(String msg){
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(msg);
			String res = jsonObject.getString("res");
			String itemsString = jsonObject.getString("items");
//			System.out.println(res);
//			System.out.println(itemsString);
			list.add(res);
			list.add(itemsString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
//	/**
//	 * 将表示对象的字符串解析并存入list
//	 * @return
//	 */
//	public static void jsonResArrayList(String msg,SQLiteDatabase database,String name){
//		ContentValues values = new ContentValues();
//		Gson gson = new Gson();
//		ListResBean listResBean = gson.fromJson(msg, ListResBean.class);
//		values.put("update_name", name);
//		values.put("update_time", listResBean.getLastupdate());
//		Cursor cursor = database.query(DatabaseSql.LAST_UPDATE_TIME_STRING, 
//				new String []{"update_name"}, "update_name=?", new String []{name}, null, null, null);
//		if(cursor==null || cursor.getCount() == 0){
//			database.insert(DatabaseSql.LAST_UPDATE_TIME_STRING, null, values);
//		}else{
//			database.update(DatabaseSql.LAST_UPDATE_TIME_STRING, values, "update_name=?", new String []{name});
//		}
//		
//	}
	/**
	 * 解析促销活动列表数据
	 * @param msg
	 * @param context
	 */
	public static void jsonPromotionData(String msg,Context context,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<PromotionBean>>() {}.getType();
		Gson gson = new Gson();
		LinkedList<PromotionBean> listItemsBeans = null;
		PromotionBean listItemsBean = null;
		listItemsBeans = gson.fromJson(msg, listType);
		if(listItemsBeans!=null && listItemsBeans.size()>0){
			database.delete(DatabaseSql.CUXIAO_TABLENAME_STRING, null, null);
			for(Iterator<PromotionBean> iterator = listItemsBeans.iterator();iterator.hasNext();){
				listItemsBean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("title", listItemsBean.getTitle());
				contentValues.put("brand_name", listItemsBean.getBrand_name());
				contentValues.put("floor_name", listItemsBean.getFloor_name());
				contentValues.put("brand_id", listItemsBean.getBrand_id());
				contentValues.put("floor_id", listItemsBean.getFloor_id());
				contentValues.put("start_date", listItemsBean.getStart_date());
				contentValues.put("end_date", listItemsBean.getEnd_date());
				contentValues.put("info_id", listItemsBean.getInfo_id());
				contentValues.put("promotion_type", listItemsBean.getPromotion_type());
				contentValues.put("promotion_url", listItemsBean.getPromotion_url());
//				Cursor cursor = database.query(DatabaseSql.CUXIAO_TABLENAME_STRING, 
//						new String []{"info_id"}, "info_id=?", new String []{listItemsBean.getInfo_id()},
//						null, null, null);
//				if(cursor == null || cursor.getCount() == 0){
					database.insert(DatabaseSql.CUXIAO_TABLENAME_STRING, null, contentValues);
//				}else{
//					database.update(DatabaseSql.CUXIAO_TABLENAME_STRING, 
//							contentValues, "info_id=?", new String []{listItemsBean.getInfo_id()});
//				}
			}
		}
	}
	/**
	 * 解析特惠商品列表数据
	 * @param msg
	 * @param context
	 */
	public static void jsonSpecialData(String msg,Context context,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<SpecialBean>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<SpecialBean> list = null;
		SpecialBean specialBean = null;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			database.delete(DatabaseSql.SPECIALGOODS_TABLENAME_STRING, null, null);
			for(Iterator<SpecialBean> iterator = list.iterator();iterator.hasNext();){
				specialBean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("title", specialBean.getTitle());
				contentValues.put("brand_name", specialBean.getBrand_name());
				contentValues.put("floor_name", specialBean.getFloor_name());
				contentValues.put("brand_id", specialBean.getBrand_id());
				contentValues.put("floor_id", specialBean.getFloor_id());
				contentValues.put("special_pic", specialBean.getSpecial_pic());
				contentValues.put("special_url", specialBean.getSpecial_url());
				contentValues.put("price", specialBean.getPrice());
				contentValues.put("goods_no", specialBean.getGoods_no());
				contentValues.put("start_date", specialBean.getStart_date());
				contentValues.put("end_date", specialBean.getEnd_date());
				contentValues.put("info_id", specialBean.getInfo_id());
//				Cursor cursor = database.query(DatabaseSql.SPECIALGOODS_TABLENAME_STRING,
//						new String[]{"info_id"}, "info_id=?", new String[]{specialBean.getInfo_id()}, null, null, null);
//				if(cursor == null || cursor.getCount()<=0){
					database.insert(DatabaseSql.SPECIALGOODS_TABLENAME_STRING, null, contentValues);
//				}else{
//					database.update(DatabaseSql.SPECIALGOODS_TABLENAME_STRING, contentValues, "info_id=?", 
//							new String[]{specialBean.getInfo_id()});
//				}
				
			}
		}
	}
	/**
	 * 解析美食速递列表数据
	 * @param msg
	 * @param context
	 */
	public static void jsonFoodData(String msg,Context context,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<FoodBean>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<FoodBean> list;
		FoodBean foodBean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			database.delete(DatabaseSql.FOOD_TABLENAME_STRING, null, null);
			for(Iterator<FoodBean> iterator = list.iterator();iterator.hasNext();){
				foodBean = iterator.next();

				ContentValues contentValues = new ContentValues();
				contentValues.put("condition", foodBean.getCondition());
				contentValues.put("brand_name", foodBean.getBrand_name());
				contentValues.put("brand_id", foodBean.getBrand_id());
				contentValues.put("floor_name", foodBean.getFloor_name());
				contentValues.put("floor_id", foodBean.getFloor_id());
				contentValues.put("food_tel", foodBean.getFood_tel());
				contentValues.put("food_tel2", foodBean.getFood_tel2());
				contentValues.put("food_pic", foodBean.getFood_pic());
				contentValues.put("delivery_time1", foodBean.getDelivery_time1());
				contentValues.put("delivery_time2", foodBean.getDelivery_time2());
				contentValues.put("delivery_time3", foodBean.getDelivery_time3());
				contentValues.put("info_id", foodBean.getInfo_id());
//				Cursor cursor = database.query(DatabaseSql.FOOD_TABLENAME_STRING,
//						new String[]{"info_id"}, "info_id=?", new String[]{foodBean.getInfo_id()}, null, null, null);
//				if(cursor == null ||  cursor.getCount() == 0){
					database.insert(DatabaseSql.FOOD_TABLENAME_STRING, null, contentValues);
//				}else{
//					database.update(DatabaseSql.FOOD_TABLENAME_STRING, contentValues, "info_id=?", new String[]{foodBean.getInfo_id()});
//				}
			}
		}
	}
	
	/**
	 * 解析特惠商品
	 * @param msg
	 */
	public static void jsonPromitionDetail(String msg){
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(msg).getJSONObject("detail");
			ContentValues contentValues = new ContentValues();
			contentValues.put("title", jsonObject.getString("title"));
			contentValues.put("brand", jsonObject.getString("brand"));
			contentValues.put("floor", jsonObject.getString("floor"));
			contentValues.put("content", jsonObject.getString("content"));
			contentValues.put("start_date", jsonObject.getString("start_date"));
			contentValues.put("end_date", jsonObject.getString("end_date"));
			contentValues.put("info_id", jsonObject.getString("info_id"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 解析特惠商品
	 * @param msg
	 */
	public static void jsonSpecialDetail(String msg){
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(msg).getJSONObject("detail");
			ContentValues contentValues = new ContentValues();
			contentValues.put("title", jsonObject.getString("title"));
			contentValues.put("brand", jsonObject.getString("brand"));
			contentValues.put("floor", jsonObject.getString("floor"));
			contentValues.put("special_pic", jsonObject.getString("special_pic"));
			contentValues.put("special_desc", jsonObject.getString("special_desc"));
			contentValues.put("price", jsonObject.getString("price"));
			contentValues.put("goods_no", jsonObject.getString("goods_no"));
			contentValues.put("start_date", jsonObject.getString("start_date"));
			contentValues.put("end_date", jsonObject.getString("end_date"));
			contentValues.put("info_id", jsonObject.getString("info_id"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析品牌详情数据 
	 * @param msg
	 */
	public static void jsonBrandDetail(String msg,SQLiteDatabase database){
//		ArrayList<HashMap<String, Object>>  returnList = new ArrayList<HashMap<String,Object>>();
		Type listType = new TypeToken<LinkedList<BrandBean>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<BrandBean> list = null;
		BrandBean brandBean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			database.delete(DatabaseSql.BRAND_TABLENAME_STRING, null, null);
			for(Iterator<BrandBean> iterator = list.iterator();iterator.hasNext();){
				brandBean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("brand_id", brandBean.getBrand_id());
				contentValues.put("floor_id", brandBean.getFloor_id());
				contentValues.put("brand_name", brandBean.getBrand_name());
				contentValues.put("floor_name", brandBean.getFloor_name());
				contentValues.put("floor_sort", brandBean.getFloor_sort());
				if(brandBean.getBrand_name().length()>0){
					database.insert(DatabaseSql.BRAND_TABLENAME_STRING, null, contentValues);
//					if(returnList.size() == 0){
//						HashMap<String, Object> map = new HashMap<String, Object>();
//						map.put("louceng", brandBean.getFloor_name());
//						ArrayList<String> pinpaiList = new ArrayList<String>();
//						pinpaiList.add(brandBean.getBrand_name());
//						map.put("brandList", pinpaiList);
//						returnList.add(map);
//					}else if(returnList.size()>=1){
//						if(returnList.get(returnList.size()-1).get("louceng").equals(brandBean.getFloor_name())){
//							((ArrayList<String>)(returnList.get(returnList.size()-1).get("brandList"))).add(brandBean.getBrand_name());
//						}else{
//							HashMap<String, Object> map = new HashMap<String, Object>();
//							map.put("louceng", brandBean.getFloor_name());
//							ArrayList<String> pinpaiList = new ArrayList<String>();
//							pinpaiList.add(brandBean.getBrand_name());
//							map.put("brandList", pinpaiList);
//							returnList.add(map);
//						}
//					}
					
				}
			}
		}
	}
	/**
	 * 解析楼层详情接口数据
	 * @param msg
	 */
	public static void jsonFloorData(String msg,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<FloorDetailBean>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<FloorDetailBean> list = null;
		FloorDetailBean floorDetailBean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			database.delete(DatabaseSql.FLOOR_BRAND_TABLENAME_STRING, null, null);
			for(Iterator<FloorDetailBean> iterator = list.iterator();iterator.hasNext();){
				floorDetailBean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("cat_id", floorDetailBean.getCat_id());
				contentValues.put("floor_id", floorDetailBean.getFloor_id());
				contentValues.put("cat_name", floorDetailBean.getCat_name());
				contentValues.put("floor_name", floorDetailBean.getFloor_name());
				contentValues.put("floor_sort", floorDetailBean.getFloor_sort());
				if(floorDetailBean.getCat_name().length()>0){
					database.insert(DatabaseSql.FLOOR_BRAND_TABLENAME_STRING, null, contentValues);
				}
				
			}
		}
	}
}
