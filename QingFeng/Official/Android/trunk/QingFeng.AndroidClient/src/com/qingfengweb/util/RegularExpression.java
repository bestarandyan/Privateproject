/**
 * 
 */
package com.qingfengweb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ������
 * @createDate 2013��8��7
 * ������ʽƥ��������ֶΡ�
 *
 */
public class RegularExpression {
	  
	  
	/**
	 * ƥ��绰������ֻ�����
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phoneNumber){
	     boolean isValid = false;
	     String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		   /*
		    * �ɽ��ܵĵ绰��ʽ�У�
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
	 * �ж��Ƿ�Ϊ��Ч���ʼ���ַ
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
