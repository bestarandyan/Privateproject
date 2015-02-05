/**
 * 
 */
package com.zhihuigu.sosoOffice.utils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;

import com.zhihuigu.sosoOffice.constant.Constant;

/**
 * @author 刘星星
 * @createDate 2013/1/15
 * 对list集合类的处理类
 *
 */
public class ListDispose {
	/**
	 * @author 刘星星
	 * @createDate 2013/1/15
	 * @param list 需要被排序的集合
	 * @param str 用来做为条件的字段
	 * @return 按拼音排序后的集合
	 */
	public static ArrayList<Map<String, Object>> sortList(ArrayList<Map<String, Object>> list,String str) {
		List<String> l = new ArrayList<String>();//用来排序的集合
		ArrayList<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();//新的集合
		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i).get(str).toString();
			StringBuffer sb = new StringBuffer();
			try {
				if (name.charAt(0) >= 0x4e00 && name.charAt(0) <= 0x9fa5) {//如果属于汉字
					for (int j = 0; j < name.length(); j++) {
						sb.append(ChineseToEnglish.toEnglish(name.charAt(j)));
					}
				} else {//如果不属于汉字
					sb.append(name);
				}
				l.add(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Comparator<Object> cmp = Collator.getInstance();
		Collections.sort(l, cmp);//按照拼音排序
		for (int i = 0; i < l.size(); i++) {//重新组合
			for (int j = 0; j < list.size(); j++) {
				String name = list.get(j).get(str).toString();
				StringBuffer sb = new StringBuffer();
				if (name.charAt(0) >= 0x4e00 && name.charAt(0) <= 0x9fa5) {
					for (int o = 0; o < name.length(); o++) {
						sb.append(ChineseToEnglish.toEnglish(name.charAt(o)));
					}
				} else {
					sb.append(name);
				}
				if (l.get(i).toString().equals(sb.toString())) {
						newList.add(list.get(j));
						list.remove(j);//这一句代码非常重要，即可用加快排序速度，又可以有效的避免当有重复的名字时的同一个用户被加两次的问题。
						break;
				}
			}
		}
		list.clear();
		return newList;
	}

	
	/**
	 * @author 刘星星
	 * @createDate 2013/3/13
	 * @param list 需要被排序的集合
	 * @param str 用来做为条件的字段
	 * @return 按拼音排序后的集合
	 */
	public static ArrayList<Map<String, Object>> sortList1(ArrayList<Map<String, Object>> list,ArrayList<String> zimulist,String str) {
		ArrayList<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();//新的集合
		Comparator<Object> cmp = Collator.getInstance();
		Collections.sort(zimulist, cmp);//按照拼音排序
		for(int i=0;i<zimulist.size();i++){
			for(int j=0;j<list.size();j++){
				String name = list.get(j).get(str).toString();
//				String firstStr = ChineseToEnglish.toEnglish(name.charAt(0)).substring(0, 1);
				if(name!=null && name.equals(zimulist.get(i))){
					newList.add(list.get(j));
//					list.remove(list.get(j));
				}
			}
		}
		return newList;
	}
}
