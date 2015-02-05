/**
 * 
 */
package com.qingfengweb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 刘星星
 * @createDate 2013、8、7
 * 正则表达式匹配特殊的字段。
 *
 */
public class RegularExpression {
	  
	  
	/**
	 * 匹配电话号码和手机号码
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phoneNumber){
	     boolean isValid = false;
	     String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		   /*
		    * 可接受的电话格式有：
		    */
		   String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
	     CharSequence inputStr = phoneNumber;
	     Pattern pattern = Pattern.compile(expression);
	     Matcher matcher = pattern.matcher(inputStr);
	     Pattern pattern2 =Pattern.compile(expression2);
	     Matcher matcher2= pattern2.matcher(inputStr);
	     if(matcher.matches()||matcher2.matches()){
	    	 isValid = true;
	     }
	     return isValid; 
	   }
	/**
	 * 判断是否为有效的邮件地址
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
		}
}
