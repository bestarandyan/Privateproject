package com.qingfengweb.common.utils;

import java.text.DecimalFormat;

public class DoubleUtils {

	/**
	 * 保留4位小数
	 * 
	 * @param number
	 * @return
	 */
	public static double format4(double number) {
		DecimalFormat  format = new DecimalFormat("#.0000");
		return Double.parseDouble(format.format(number));
	}
	/**
	 * 保留两位小数
	 * @param number
	 * @return
	 */
	public static double format2(double number) {
		DecimalFormat  format = new DecimalFormat("#.00");
		return Double.parseDouble(format.format(number));
	}
	/**
	 * 四舍五入转化为int
	 * @param number
	 * @return
	 */
	public static int toInt(double number) {
		DecimalFormat  format = new DecimalFormat(".");
		return (int)Double.parseDouble(format.format(number));
	}
}
