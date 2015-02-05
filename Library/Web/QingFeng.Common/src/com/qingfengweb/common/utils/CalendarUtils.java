package com.qingfengweb.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 
 * @author ANDY
 *
 */
public class CalendarUtils {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 获得date以后的days天的日历，days为负数就是date签几天的日历（人寿项目用到）
	 * 这个方法用一次就产生一个新的日历对象和一个对象的引用
	 * @param date
	 * @param days
	 * @return
	 */
	public static Calendar getCalendar(Date date,int days){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar;
	}
	/**
	 * 将字符串转换为timestamp类型
	 * @param source
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp fomatStringToTimestamp(String time) throws ParseException{
		return new Timestamp(dateFormat.parse(time).getTime());
	}
	
}