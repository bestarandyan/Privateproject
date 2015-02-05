package com.qingfengweb.piaoguanjia.orderSystem.util;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;

public class JsonUtils {
	/***
	 * 解析单个对象
	 * @param objectclass
	 * @param jsonstr
	 * @return
	 */
	public static <T> T jsonObject(Class<T> Obclass,String jsonstr){
		Gson gson = new Gson();// 创建Gson对象
		T object = null;
		try {
			object = gson.fromJson(jsonstr, Obclass);// 解析json对象
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
	/**
	 * 解析一个对象组
	 * @param c
	 * @param jsonstr
	 * @return
	 */
	public static <T> LinkedList<T> jsonObjectList(Class<T> Obclass,String jsonstr){
		Type listType = new TypeToken<LinkedList<T>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<T> objectarray = null;
		objectarray = gson.fromJson(jsonstr, listType);
		return objectarray;
	}
}
