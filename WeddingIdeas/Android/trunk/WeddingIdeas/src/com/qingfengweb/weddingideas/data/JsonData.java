/**
 * 
 */
package com.qingfengweb.weddingideas.data;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.preference.Preference;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingfengweb.weddingideas.beans.HuoDongBean;
import com.qingfengweb.weddingideas.beans.PreferenceBean;
import com.qingfengweb.weddingideas.beans.TaoXiBean;
import com.qingfengweb.weddingideas.beans.WeddingLogBean;
import com.qingfengweb.weddingideas.beans.YangZhaoBean;
import com.qingfengweb.weddingideas.beans.YingLouBean;

/**
 * @author 刘星星
 * @createDate 2013、9、3
 * 用json
 *
 */
public class JsonData {
	
	/**
	 * 解析亲友特惠数据
	 * @param msg
	 * @param context
	 */
	public static void jsonProferenceData(String msg,SQLiteDatabase database,String storeid){
		Type listType = new TypeToken<LinkedList<PreferenceBean>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<PreferenceBean> list;
		PreferenceBean bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<PreferenceBean> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("storeid", bean.getStoreid());
				contentValues.put("a_name", bean.getA_name());
				contentValues.put("s_time", bean.getS_time());
				contentValues.put("e_time", bean.getE_time());
				contentValues.put("set_desc", bean.getSet_desc());
				contentValues.put("t_pic", bean.getT_pic());
				contentValues.put("opt_time", bean.getOpt_time());
				int a = database.update(PreferenceBean.tableName, contentValues, "id=?", new String[]{bean.getId()+""});
				if(a == 0){
					database.insert(PreferenceBean.tableName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	/**
	 * 解析样照数据
	 * @param msg
	 * @param context
	 */
	public static void jsonYZData(String msg,SQLiteDatabase database,String storeid){
		Type listType = new TypeToken<LinkedList<YangZhaoBean>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<YangZhaoBean> list;
		YangZhaoBean bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<YangZhaoBean> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("storeid", storeid);
				contentValues.put("s_name", bean.getS_name());
				contentValues.put("c_photo", bean.getC_photo());
				contentValues.put("log_type_two", bean.getLog_type_two());
				contentValues.put("photo_one", bean.getPhoto_one());
				contentValues.put("photo_two", bean.getPhoto_two());
				contentValues.put("photo_thr", bean.getPhoto_thr());
				contentValues.put("photo_for", bean.getPhoto_for());
				contentValues.put("photo_fiv", bean.getPhoto_fiv());
				contentValues.put("p_desc", bean.getP_desc());
				contentValues.put("opt_time", bean.getOpt_time());
				contentValues.put("opt_status", bean.getOpt_time());
				int a = database.update(YangZhaoBean.tbName, contentValues, "id=?", new String[]{bean.getId()+""});
				if(a == 0){
					database.insert(YangZhaoBean.tbName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	/**
	 * 解析套系数据
	 * @param msg
	 * @param context
	 */
	public static void jsonTaoXiData(String msg,SQLiteDatabase database,String storeid){
		Type listType = new TypeToken<LinkedList<TaoXiBean>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<TaoXiBean> list;
		TaoXiBean bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<TaoXiBean> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("storeid", storeid);
				contentValues.put("s_name", bean.getS_name());
				contentValues.put("c_photo", bean.getC_photo());
				contentValues.put("photo_c", bean.getPhoto_c());
				contentValues.put("ori_price", bean.getOri_price());
				contentValues.put("price_dis", bean.getPrice_dis());
				contentValues.put("price_cut", bean.getPrice_cut());
				contentValues.put("set_desc", bean.getSet_desc());
				contentValues.put("opt_time", bean.getOpt_time());
				int a = database.update(TaoXiBean.tbName, contentValues, "id=?", new String[]{bean.getId()+""});
				if(a == 0){
					database.insert(TaoXiBean.tbName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	/**
	 * 解析活动数据
	 * @param msg
	 * @param context
	 */
	public static void jsonHuoDongData(String msg,SQLiteDatabase database,String storeid){
		Type listType = new TypeToken<LinkedList<HuoDongBean>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<HuoDongBean> list;
		HuoDongBean bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<HuoDongBean> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("storeid", storeid);
				contentValues.put("a_name", bean.getA_name());
				contentValues.put("s_time", bean.getS_time());
				contentValues.put("e_time", bean.getE_time());
				contentValues.put("set_desc", bean.getSet_desc());
				contentValues.put("opt_time", bean.getOpt_time());
				contentValues.put("pic_active", bean.getPic_active());
				contentValues.put("opt_status", bean.getOpt_status());
				int a = database.update(HuoDongBean.tbName, contentValues, "id=?", new String[]{bean.getId()+""});
				if(a == 0){
					database.insert(HuoDongBean.tbName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	
	/**
	 * 解析活动数据
	 * @param msg
	 * @param context
	 */
	public static void jsonWeddingLogData(String msg,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<WeddingLogBean>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<WeddingLogBean> list;
		WeddingLogBean bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<WeddingLogBean> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("userId", bean.getUserId());
				contentValues.put("logContent", bean.getLogContent());
				contentValues.put("isOk", bean.getIsOk());
				contentValues.put("optTimeString", bean.getOptTimeString());
				contentValues.put("stimeString", bean.getStimeString());
				int a = database.update(WeddingLogBean.tbName, contentValues, "id=?", new String[]{bean.getId()+""});
				if(a == 0){
					database.insert(WeddingLogBean.tbName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	/**
	 * 门店详情数据解析
	 * @param msg
	 * @param context
	 */
	public static void jsonYingLouData(String msg,SQLiteDatabase database,String parentid){
		try {
			JSONObject jsonObject = new JSONObject(msg);
			ContentValues contentValues = new ContentValues();
			contentValues.put("id", jsonObject.getString("id"));
			contentValues.put("province", jsonObject.getString("province"));
			contentValues.put("district", jsonObject.getString("district"));
			contentValues.put("city", jsonObject.getString("city"));
			contentValues.put("address", jsonObject.getString("address"));
			contentValues.put("ask_tel", jsonObject.getString("ask_tel"));
			contentValues.put("lon", jsonObject.getString("lon"));
			contentValues.put("lat", jsonObject.getString("lat"));
			contentValues.put("wed_desc", jsonObject.getString("wed_desc"));
			contentValues.put("opt_time", jsonObject.getString("opt_time"));
			int a = database.update(YingLouBean.tbName,contentValues, "id=?", new String[]{jsonObject.getString("id")});
			if(a == 0){
				database.insert(YingLouBean.tbName, null, contentValues);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析是否成功数据
	 * @param msg
	 * @param context
	 */
	public static String jsonSuccessData(String msg){
		try {
			JSONObject jsonObject = new JSONObject(msg);
			String statuscode = jsonObject.getString("statuscode");
		return statuscode;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 判断服务器返回值是否为无数据的格式
	 * @param str
	 * @return
	 */
	public static boolean isNoData(String str){
		String format = "\\d+\\,20[1,2]\\d-((0?[0-9])|1[0-2])-(([0-2][0-9])|3[0-1])(\\s([0-1][0-9]|2[0-4]):([0-5][0-9])(:([0-5][0-9]))?)?";
		Matcher matcher = Pattern.compile(format).matcher(str);
		while(matcher.find()){
			return true;
		}
		return false;
	}
}
