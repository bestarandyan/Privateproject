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
 * @author ������
 * @createDate 2013/1/15
 * ��list������Ĵ�����
 *
 */
public class ListDispose {
	/**
	 * @author ������
	 * @createDate 2013/1/15
	 * @param list ��Ҫ������ļ���
	 * @param str ������Ϊ�������ֶ�
	 * @return ��ƴ�������ļ���
	 */
	public static ArrayList<Map<String, Object>> sortList(ArrayList<Map<String, Object>> list,String str) {
		List<String> l = new ArrayList<String>();//��������ļ���
		ArrayList<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();//�µļ���
		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i).get(str).toString();
			StringBuffer sb = new StringBuffer();
			try {
				if (name.charAt(0) >= 0x4e00 && name.charAt(0) <= 0x9fa5) {//������ں���
					for (int j = 0; j < name.length(); j++) {
						sb.append(ChineseToEnglish.toEnglish(name.charAt(j)));
					}
				} else {//��������ں���
					sb.append(name);
				}
				l.add(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Comparator<Object> cmp = Collator.getInstance();
		Collections.sort(l, cmp);//����ƴ������
		for (int i = 0; i < l.size(); i++) {//�������
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
						list.remove(j);//��һ�����ǳ���Ҫ�������üӿ������ٶȣ��ֿ�����Ч�ı��⵱���ظ�������ʱ��ͬһ���û��������ε����⡣
						break;
				}
			}
		}
		list.clear();
		return newList;
	}

	
	/**
	 * @author ������
	 * @createDate 2013/3/13
	 * @param list ��Ҫ������ļ���
	 * @param str ������Ϊ�������ֶ�
	 * @return ��ƴ�������ļ���
	 */
	public static ArrayList<Map<String, Object>> sortList1(ArrayList<Map<String, Object>> list,ArrayList<String> zimulist,String str) {
		ArrayList<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();//�µļ���
		Comparator<Object> cmp = Collator.getInstance();
		Collections.sort(zimulist, cmp);//����ƴ������
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
