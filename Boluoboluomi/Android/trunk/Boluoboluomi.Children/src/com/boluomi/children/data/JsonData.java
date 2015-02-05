/**
 * 
 */
package com.boluomi.children.data;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boluomi.children.model.HelpInfo;
import com.boluomi.children.model.MerchanInfo;
import com.boluomi.children.model.PictureInfo;
import com.boluomi.children.model.PictureThemes;
import com.boluomi.children.model.StoreInfo;
import com.boluomi.children.model.SystemUpdateInfo;
import com.boluomi.children.model.TreasureInfo;
import com.boluomi.children.model.TreasureProductInfo;
import com.boluomi.children.model.UpLoadFileInfo;
import com.boluomi.children.model.UserInfo;
import com.boluomi.children.model.UserPhotoInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author 刘星星
 * @createDate 2013、9、3
 * 用json
 *
 */
public class JsonData {
	/**
	 * 解析更新时间数据
	 * @param msg
	 * @param context
	 */
	public static void jsonUpdateTimeData(String msg,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<SystemUpdateInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<SystemUpdateInfo> list;
		SystemUpdateInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<SystemUpdateInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("type", bean.getType());
				contentValues.put("updatetime", bean.getUpdatetime());
				Cursor c = database.query(SystemUpdateInfo.TableName, new String[]{"number"}, "type=? and storeid=?",
						new String[]{bean.getType()+"",UserBeanInfo.getInstant().getCurrentStoreId()}, null, null, null);
				String number ="0";
				if(c.getCount() == 0){
					number = bean.getNumber();
				}else{
					while(c.moveToNext()){
						number = c.getString(c.getColumnIndex("number"));
						if(number!=null){
							number = String.valueOf(Integer.parseInt(number)+Integer.parseInt(bean.getNumber()));
						}else{
							number = bean.getNumber();
						}
					}
				}
				contentValues.put("number", number);
				contentValues.put("storeid", UserBeanInfo.getInstant().getCurrentStoreId());
				contentValues.put("userid", UserBeanInfo.getInstant().getUserid());
				int a = database.update(SystemUpdateInfo.TableName, contentValues, "type=? and storeid=? and userid=?", 
						new String[]{bean.getType()+"",UserBeanInfo.getInstant().getCurrentStoreId(),UserBeanInfo.getInstant().getUserid()});
				if(a == 0){
					database.insert(SystemUpdateInfo.TableName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	/**
	 * 解析门店列表数据
	 * @param msg
	 * @param context
	 */
	public static void jsonStoresData(String msg,SQLiteDatabase database,String parentid){
		Type listType = new TypeToken<LinkedList<StoreInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<StoreInfo> list;
		StoreInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<StoreInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("name", bean.getName());
				contentValues.put("cityid", parentid);
				contentValues.put("district", bean.getDistrict());
				int a = database.update(StoreInfo.TableName, contentValues, "id=?", new String[]{bean.getId()+""});
				if(a == 0){
					database.insert(StoreInfo.TableName, null, contentValues);
				}
				System.out.println(a+"");
				if(!LocalStaticData.isSelectedStore.contains(parentid)){//将所选城市id保存到本地静态空间
					LocalStaticData.isSelectedStore.add(parentid);
				}
			}
		}
	}
	
	/**
	 * 门店详情数据解析
	 * @param msg
	 * @param context
	 */

	
	public static void jsonSotreDetailData(String msg,SQLiteDatabase database,String parentid){
		try {
			JSONObject jsonObject = new JSONObject(msg);
			ContentValues contentValues = new ContentValues();
			contentValues.put("id", jsonObject.getString("id"));
			contentValues.put("name", jsonObject.getString("name"));
			contentValues.put("cityid", parentid);
			contentValues.put("district", jsonObject.getString("district"));
			contentValues.put("city", jsonObject.getString("city"));
			contentValues.put("introduce", jsonObject.getString("introduce"));
			contentValues.put("districtid", jsonObject.getString("districtid"));
			contentValues.put("provinceid", jsonObject.getString("provinceid"));
			contentValues.put("province", jsonObject.getString("province"));
			contentValues.put("phonenumber", jsonObject.getString("phonenumber"));
			contentValues.put("store_logo", jsonObject.getString("store_logo"));
			contentValues.put("store_home_logo", jsonObject.getString("store_home_logo"));
			contentValues.put("qq", jsonObject.getString("qq"));
			contentValues.put("contact", jsonObject.getString("contact"));
			contentValues.put("shop_activity_image", jsonObject.getString("shop_activity_image"));
			contentValues.put("shop_activity_text", jsonObject.getString("shop_activity_text"));
			contentValues.put("shop_activity_link", jsonObject.getString("shop_activity_link"));
			contentValues.put("qrcode1", jsonObject.getString("qrcode1"));
			contentValues.put("qrcode2", jsonObject.getString("qrcode2"));
			contentValues.put("validate_required", jsonObject.getString("validate_required"));
			contentValues.put("deleted", jsonObject.getString("deleted"));
			int a = database.update(StoreInfo.TableName,contentValues, "id=?", new String[]{jsonObject.getString("id")});
			if(a == 0){
				database.insert(StoreInfo.TableName, null, contentValues);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 用户注册数据解析
	 * @param msg
	 * @param context
	 */
	public static void jsonUserData(String msg,SQLiteDatabase database,String password,String isautologin){
		try {
			JSONObject jsonObject = new JSONObject(msg);
			ContentValues contentValues = new ContentValues();
			contentValues.put("userid", jsonObject.getString("userid"));
			contentValues.put("storeid", jsonObject.getString("storeid"));
			contentValues.put("username", jsonObject.getString("username"));
			contentValues.put("password", password);
			contentValues.put("realname", jsonObject.getString("realname"));
			contentValues.put("points", jsonObject.getString("points"));
			contentValues.put("islastuser", "1");
			SimpleDateFormat d= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化时间
			String nowtime=d.format(new Date());//按以上格式 将当前时间转换成字符串
			contentValues.put("lastlogintime", nowtime);
			Cursor c = database.query(StoreInfo.TableName, new String[]{"name"}, "id=?", new String[]{jsonObject.getString("storeid")}, null, null, null);
			String storename = "";
			while(c.moveToNext()){
				storename = c.getString(c.getColumnIndex("name"));
			}
			contentValues.put("storename", storename);
			contentValues.put("isautologin", isautologin);//0为不是自动登陆  1为自动登陆
			contentValues.put("picture_count", jsonObject.getString("picture_count"));
			int a = database.update(UserInfo.TableName,contentValues, "userid=?", new String[]{jsonObject.getString("userid")});
			if(a == 0){
				database.insert(UserInfo.TableName, null, contentValues);
			}
			
			ContentValues otherValues = new ContentValues();
			otherValues.put("islastuser", "0");
			int updateNumber = database.update(UserInfo.TableName, otherValues, "userid!=?", new String[]{jsonObject.getString("userid")});
			System.out.println("本次登陆，更新了其他用户的登陆状态个数为："+updateNumber);
			UserBeanInfo.getInstant().setCurrentStoreId(jsonObject.getString("storeid"));
			UserBeanInfo.getInstant().setUserid(jsonObject.getString("userid"));
			UserBeanInfo.getInstant().setUserScore(jsonObject.getString("points"));
			UserBeanInfo.getInstant().setUserName(jsonObject.getString("username"));
			UserBeanInfo.getInstant().setCurrentStore(storename);
			UserBeanInfo.getInstant().setPassword(password);
			if(isautologin.equals("1")){
				UserBeanInfo.getInstant().setAutoLogin(true);
			}else{
				UserBeanInfo.getInstant().setAutoLogin(false);
			}
			UserBeanInfo.getInstant().setLogined(true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 解析用户照片列表数据
	 * @param msg
	 * @param database
	 * @param username
	 */
	public static void jsonUserPhotos(String msg,SQLiteDatabase database,String username){
		Type listType = new TypeToken<LinkedList<UserPhotoInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<UserPhotoInfo> list;
		UserPhotoInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<UserPhotoInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("type", bean.getType());
				contentValues.put("imageid", bean.getImageid());
				contentValues.put("name", bean.getName());
				contentValues.put("username", username);
				contentValues.put("deleted", bean.getDeleted());
				int a = database.update(UserPhotoInfo.TableName, contentValues, "id=?", new String[]{bean.getId()+""});
				if(a == 0){
					database.insert(UserPhotoInfo.TableName, null, contentValues);
				}
			}
		}
	}
	
	/**
	 * 解析美图相册列表数据
	 * @param msg
	 * @param database
	 * @param username
	 */
	public static void jsonBeautyPhotoThemes(String msg,SQLiteDatabase database,String storeid){
		Type listType = new TypeToken<LinkedList<PictureThemes>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<PictureThemes> list;
		PictureThemes bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<PictureThemes> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("name", bean.getName());
				contentValues.put("thumb", bean.getThumb());
				contentValues.put("description", bean.getDescription());
				contentValues.put("ranking", bean.getRanking());
				contentValues.put("commend", bean.getCommend());
				contentValues.put("deleted", bean.getDeleted());
				contentValues.put("updatetime", bean.getUpdatetime());
				contentValues.put("storeid", storeid);
				int a = database.update(PictureThemes.TableName, contentValues, "id=? and storeid=?", new String[]{bean.getId(),storeid});
				if(a == 0){
					database.insert(PictureThemes.TableName, null, contentValues);
				}
			}
		}
	}
	
	
	/**
	 * 解析美图相册照片列表数据
	 * @param msg
	 * @param database
	 * @param username
	 */
	public static void jsonBeautyPhotos(String msg,SQLiteDatabase database,String storeid,String themeid){
		Type listType = new TypeToken<LinkedList<PictureInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<PictureInfo> list;
		PictureInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<PictureInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("name", bean.getName());
				contentValues.put("imageid ", bean.getImageid());
				contentValues.put("description", bean.getDescription());
				contentValues.put("ranking", bean.getRanking());
				contentValues.put("deleted", bean.getDeleted());
				contentValues.put("storeid", storeid);
				contentValues.put("themeid", themeid);
				int a = database.update(PictureInfo.TableName, contentValues, "id=? and storeid=? and themeid=?", new String[]{bean.getId(),storeid,themeid});
				if(a == 0){
					database.insert(PictureInfo.TableName, null, contentValues);
				}
			}
		}
	}
	

	
	/**
	 * 解析获取活动分享列表数据
	 * @param msg
	 * @param database
	 * @param storeid
	 */
	public static void jsonTreasureListData(String msg,SQLiteDatabase database,String storeid){
		Type listType = new TypeToken<LinkedList<TreasureInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<TreasureInfo> list;
		TreasureInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<TreasureInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("name", bean.getName());
				contentValues.put("icon", bean.getIcon());
				contentValues.put("ranking", bean.getRanking());
				contentValues.put("number", bean.getNumber());
				contentValues.put("deleted", bean.getDeleted());
				contentValues.put("storeid", storeid);
				int a = database.update(TreasureInfo.TableName, contentValues, "id=? and storeid=?", new String[]{bean.getId()+"",storeid});
				if(a == 0){
					database.insert(TreasureInfo.TableName, null, contentValues);
				}
			}
		}
	}
	
	/**
	 * 解析获取商家列表数据
	 * @param msg
	 * @param database
	 * @param storeid
	 */
	public static void jsonMerchanListData(String msg,SQLiteDatabase database,String storeid){
		Type listType = new TypeToken<LinkedList<MerchanInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<MerchanInfo> list;
		MerchanInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<MerchanInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("name", bean.getName());
				contentValues.put("typeid", bean.getTypeid());
				contentValues.put("logo", bean.getLogo());
				contentValues.put("description", bean.getDescription());
				contentValues.put("deleted", bean.getDeleted());
				contentValues.put("storeid", storeid);
				int a = database.update(MerchanInfo.TableName, contentValues, "id=? and storeid=?", new String[]{bean.getId()+"",storeid});
				if(a == 0){
					database.insert(MerchanInfo.TableName, null, contentValues);
				}
			}
		}
	}
	
	
	
	/**
	 * 解析我的定制数据
	 * @param msg
	 * @param database
	 * @param storeid
	 */
	public static void jsonHelpData(String msg,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<HelpInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<HelpInfo> list;
		HelpInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<HelpInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("type", bean.getType());
				contentValues.put("title", bean.getTitle());
				contentValues.put("content", bean.getContent());
				contentValues.put("ranking", bean.getRanking());
				contentValues.put("createtime", bean.getCreatetime());
				contentValues.put("deleted", bean.getDeleted());
				int a = database.update(HelpInfo.TbName, contentValues, "id=?", new String[]{bean.getId()});
				if(a == 0){
					database.insert(HelpInfo.TbName, null, contentValues);
				}
			}
		}
	}
	
	
	/**
	 * 图片上传数据解析
	 * @param msg
	 * @param context
	 */
	public static ArrayList<String> jsonImageUploadData(String msg,SQLiteDatabase database,String path,String successlocation,String imglength){
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(msg);
			String id = "";
			try{
				id = jsonObject.getString("id").toString();
			}catch (JSONException e) {
				e.printStackTrace();
			}
			String uploadid = jsonObject.getString("uploadid");
			String progress = jsonObject.getString("progress");
			
			String type = "";
			try{
				type = jsonObject.getString("type");
			}catch (JSONException e) {
				e.printStackTrace();
			}
			String name = "";
			try{
				 name = jsonObject.getString("name");
			}catch (JSONException e) {
				e.printStackTrace();
			}
			String imageid = "";
			try{
				imageid = jsonObject.getString("imageid");
			}catch (JSONException e) {
				e.printStackTrace();
			}
			ContentValues contentValues = new ContentValues();
			contentValues.put("id", id);
			contentValues.put("uploadid", uploadid);
			contentValues.put("progress", progress);
			contentValues.put("type", type);
			contentValues.put("name", name);
			contentValues.put("imageid", imageid);
			contentValues.put("localpath", path);
			contentValues.put("successlocation", successlocation);
			contentValues.put("imglength", imglength);
			System.out.println("本次保存的进度值为：++++++++++++++++++++++++++"+progress);
			int a = database.update(UpLoadFileInfo.TableName,contentValues, "localpath=?", new String[]{path});
			if(a == 0){
				System.out.println("********************************************这里有一次没有更新成功，所以插入了一条新的数据");
				database.insert(UpLoadFileInfo.TableName, null, contentValues);
			}
			list.add(uploadid);
			list.add(progress);
			list.add(imageid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
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
