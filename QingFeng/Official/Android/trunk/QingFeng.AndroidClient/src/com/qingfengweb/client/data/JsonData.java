package com.qingfengweb.client.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingfengweb.client.bean.CaseInfo;
import com.qingfengweb.client.bean.ContentUpdateInfo;
import com.qingfengweb.client.bean.HomeImageInfo;
import com.qingfengweb.client.bean.JobListInfo;
import com.qingfengweb.client.bean.ServicesInfo;
import com.qingfengweb.client.bean.TeamInfo;
import com.qingfengweb.client.bean.UpLoadFileInfo;
import com.qingfengweb.client.bean.UpdateSystemTime;
public class JsonData {
	/**
	 * 解析更新时间数据
	 * @param msg
	 * @param context
	 */
	public static void jsonUpdateTimeData(String msg,Context context,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<UpdateSystemTime>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<UpdateSystemTime> list;
		UpdateSystemTime bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<UpdateSystemTime> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("type", bean.getType());
				contentValues.put("sysTime", bean.getUpdate_time());
				int a = database.update(UpdateSystemTime.tableName, contentValues, "type=?", new String[]{bean.type+""});
				if(a == 0){
					database.insert(UpdateSystemTime.tableName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	
	/**
	 * 首页图片数据
	 * @param msg
	 * @param context
	 */
	public static void jsonHomeImageData(String msg,Context context,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<HomeImageInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<HomeImageInfo> list;
		HomeImageInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<HomeImageInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("status", bean.getStatus());
				contentValues.put("update_time", bean.getUpdate_time());
//				Cursor cursor = database.query(DatabaseSql.FOOD_TABLENAME_STRING,
//						new String[]{"info_id"}, "info_id=?", new String[]{foodBean.getInfo_id()}, null, null, null);
//				if(cursor == null ||  cursor.getCount() == 0){
//					database.insert(DatabaseSql.FOOD_TABLENAME_STRING, null, contentValues);
				int a = database.update(HomeImageInfo.TableName, contentValues, "id=?", new String[]{bean.getId()+""});
				if(a == 0){
					database.insert(HomeImageInfo.TableName, null, contentValues);
				}
				System.out.println(a+"");
//				}else{
//					database.update(DatabaseSql.FOOD_TABLENAME_STRING, contentValues, "info_id=?", new String[]{foodBean.getInfo_id()});
//				}
			}
		}
	}
	/**
	 * 内容更新数据解析
	 * @param msg
	 * @param context
	 */
	public static void jsonContentUpdateData(String msg,Context context,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<ContentUpdateInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<ContentUpdateInfo> list;
		ContentUpdateInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<ContentUpdateInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("type", bean.getType());
				contentValues.put("summary", bean.getSummary());
//				Cursor cursor = database.query(DatabaseSql.FOOD_TABLENAME_STRING,
//						new String[]{"info_id"}, "info_id=?", new String[]{foodBean.getInfo_id()}, null, null, null);
//				if(cursor == null ||  cursor.getCount() == 0){
//					database.insert(DatabaseSql.FOOD_TABLENAME_STRING, null, contentValues);
				int a = database.update(ContentUpdateInfo.tableName, contentValues, "type=?", new String[]{bean.getType().trim()});
				if(a == 0){
					database.insert(ContentUpdateInfo.tableName, null, contentValues);
				}
				System.out.println(a+"");
//				}else{
//					database.update(DatabaseSql.FOOD_TABLENAME_STRING, contentValues, "info_id=?", new String[]{foodBean.getInfo_id()});
//				}
			}
		}
	}
	
	/**
	 * 团队成员数据解析
	 * @param msg
	 * @param context
	 */
	public static void jsonTeamData(String msg,Context context,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<TeamInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<TeamInfo> list;
		TeamInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<TeamInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("name", bean.getName());
				contentValues.put("position", bean.getPosition());
				contentValues.put("photoid", bean.getPhotoid());
				contentValues.put("motto", bean.getMotto());
				contentValues.put("sequence", bean.getSequence());
				contentValues.put("updatetime", bean.getUpdatetime());
				contentValues.put("status", bean.getStatus());
				int a = database.update(TeamInfo.TableName,contentValues, "id=?", new String[]{bean.getId()});
				if(a == 0){
					database.insert(TeamInfo.TableName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	
	/**
	 * 客户案例数据解析
	 * @param msg
	 * @param context
	 */
	public static void jsonCaseInfoData(String msg,Context context,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<CaseInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<CaseInfo> list;
		CaseInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<CaseInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("name", bean.getName());
				contentValues.put("summary", bean.getSummary());
				contentValues.put("photos", bean.getPhotos());
				contentValues.put("thumb", bean.getThumb());
				contentValues.put("type", bean.getType());
				contentValues.put("description", bean.getDescription());
				contentValues.put("sequence", bean.getSequence());
				contentValues.put("updatetime", bean.getUpdatetime());
				contentValues.put("status", bean.getStatus());
				int a = database.update(CaseInfo.TableName,contentValues, "id=?", new String[]{bean.getId()});
				if(a == 0){
					database.insert(CaseInfo.TableName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	
	/**
	 * 客户案例数据解析
	 * @param msg
	 * @param context
	 */
	public static void jsonJobInfoData(String msg,Context context,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<JobListInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<JobListInfo> list;
		JobListInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<JobListInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("id", bean.getId());
				contentValues.put("name", bean.getName());
				contentValues.put("number", bean.getNumber());
				contentValues.put("summary", bean.getSummary());
				contentValues.put("requirements", bean.getRequirements());
				contentValues.put("sequence", bean.getSequence());
				contentValues.put("endtime", bean.getEndtime());
				contentValues.put("updatetime", bean.getUpdatetime());
				contentValues.put("description", bean.getDescription());
				contentValues.put("responsibility", bean.getResponsibility());
				contentValues.put("status", bean.getStatus());
				int a = database.update(JobListInfo.TableName,contentValues, "id=?", new String[]{bean.getId()});
				if(a == 0){
					database.insert(JobListInfo.TableName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	
	/**
	 * 服务项目数据解析
	 * @param msg
	 * @param context
	 */
	public static void jsonServicesInfoData(String msg,Context context,SQLiteDatabase database){
		Type listType = new TypeToken<LinkedList<ServicesInfo>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<ServicesInfo> list;
		ServicesInfo bean;
		list = gson.fromJson(msg, listType);
		if(list!=null && list.size()>0){
			for(Iterator<ServicesInfo> iterator = list.iterator();iterator.hasNext();){
				bean = iterator.next();
				ContentValues contentValues = new ContentValues();
				contentValues.put("type", bean.getType());
				contentValues.put("summary", bean.getSummary());
				contentValues.put("update_time", bean.getUpdate_time());
				int a = database.update(ServicesInfo.tableName,contentValues, "type=?", new String[]{bean.getType()});
				if(a == 0){
					database.insert(ServicesInfo.tableName, null, contentValues);
				}
				System.out.println(a+"");
			}
		}
	}
	
	
	/**
	 * 文件上传数据解析
	 * @param msg
	 * @param context
	 */

	
	public static ArrayList<String> jsonFileUploadData(String msg,SQLiteDatabase database){
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(msg);
			String id = jsonObject.getString("id");
			String progress = jsonObject.getString("progress");
			ContentValues contentValues = new ContentValues();
			contentValues.put("id", id);
			contentValues.put("progress", progress);
			int a = database.update(UpLoadFileInfo.TableName,contentValues, "id=?", new String[]{id});
			if(a == 0){
				database.insert(UpLoadFileInfo.TableName, null, contentValues);
			}
			list.add(id);
			list.add(progress);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
