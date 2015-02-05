package com.qingfengweb.lottery.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 字符串工具类
 * 
 * @author GuoLiang
 * 
 */
public class StringUtils {
	/**
	 * 从流读取字符串
	 * 
	 * @param stream
	 * @param encoding
	 * @return
	 */
	public static String stringFrom(InputStream stream, String encoding) {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream, encoding));
			while (reader.ready())
				builder.append(reader.readLine());
			reader.close();
		} catch (IOException ie) {
		}
		return builder.toString();
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final String md5(String s) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("md5");
			return toHexString(md.digest(s.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//将毫秒转化为 时：分：秒 格式 ，例如  00:05:23
	 public static  String calculatTime(int milliSecondTime) {
	  
	  int hour = milliSecondTime /(60*60*1000);
	  int minute = (milliSecondTime - hour*60*60*1000)/(60*1000);
	  int seconds = (milliSecondTime - hour*60*60*1000 - minute*60*1000)/1000;
	  
	  if(seconds >= 60 )
	  {
	   seconds = seconds % 60;
	      minute+=seconds/60;
	  }
	  if(minute >= 60)
	  {
	    minute = minute % 60;
	    hour  += minute/60;
	  }
	  
	  String sh = "";
	  String sm ="";
	  String ss = "";
	  if(hour <10) {
	     sh = "0" + String.valueOf(hour);
	  }else {
	     sh = String.valueOf(hour);
	  }
	  if(minute <10) {
	     sm = "0" + String.valueOf(minute);
	  }else {
	     sm = String.valueOf(minute);
	  }
	  if(seconds <10) {
	     ss = "0" + String.valueOf(seconds);
	  }else {
	     ss = String.valueOf(seconds);
	  }
	  
	  return sh +":"+sm+":"+ ss;
	 }
	//将毫秒转化为 时：分：秒 格式 ，例如  00:05:23
	 public static  String calculatTimeNoHour(int milliSecondTime) {
	  int minute = (milliSecondTime)/(60*1000);
	  int seconds = (milliSecondTime- minute*60*1000)/1000;
	  
	  if(seconds >= 60 )
	  {
	   seconds = seconds % 60;
	      minute+=seconds/60;
	  }
	  if(minute >= 60)
	  {
	    minute = minute % 60;
	  }
	  
	  String sm ="";
	  String ss = "";
	  if(minute <10) {
	     sm = "0" + String.valueOf(minute);
	  }else {
	     sm = String.valueOf(minute);
	  }
	  if(seconds <10) {
	     ss = "0" + String.valueOf(seconds);
	  }else {
	     ss = String.valueOf(seconds);
	  }
	  
	  return sm+":"+ ss;
	 }
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final String sha1(String s) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("sha-1");
			return toHexString(md.digest(s.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 字节数组转字符串
	 * 
	 * @param bytes
	 *            字节数组
	 * @return 返回被转换的字符串
	 */
	public static final String toHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10)
				buffer.append("0");
			buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buffer.toString();
	}

	/**
	 * 转换为驼峰命名法
	 * 
	 * @param source
	 * @return
	 */
	public static String toCamelCase(String source) {
		source = source.toLowerCase();
		while (source.indexOf("_") >= 0) {
			int index = source.indexOf("_");
			char c = source.charAt(index + 1);
			source = source.substring(0, index)
					.concat(String.valueOf(c).toUpperCase())
					.concat(source.substring(index + 2));
		}
		return source;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	// public static boolean isNullOrEmpty(String source) {
	// return source == null || source.isEmpty();
	// }
	/**
	 * 连接字符串
	 * 
	 * @param array
	 * @param seperator
	 * @return
	 */
	public static String join(Object[] array, String seperator) {
		if (array == null || array.length == 0)
			return null;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i].toString());
			if (i < array.length - 1)
				builder.append(seperator);
		}
		return builder.toString();
	}

	/**
	 * 左填充字符串
	 * 
	 * @param source
	 * @param size
	 * @param s
	 * @return
	 */
	public static String leftPad(String source, int size, String s) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size; i++)
			builder.append(s);
		builder.append(source);
		return builder.toString();
	}

	/**
	 * 查找多个字符其中的一个所在下标，找到一个之后就不在继续查找
	 * 
	 * @param src
	 * @param strs
	 * @return
	 */
	public static int indexOfEnhanced(String src, String... strs) {
		for (String s : strs) {
			if (src.indexOf(s) != -1)
				return src.indexOf(s);
		}
		return -1;
	}

	/**
	 * 查找多个字符其中的一个所在下标，找到一个之后就不在继续查找
	 * 
	 * @param src
	 * @param fromIndex
	 * @param strs
	 * @return
	 */
	public static int indexOfEnhanced(String src, int fromIndex, String... strs) {
		for (String s : strs) {
			if (src.indexOf(s, fromIndex) != -1)
				return src.indexOf(s);
		}
		return -1;
	}

	public static List<Integer> getBitsFromIntagerNumber(Integer num) {
		String binary = Integer.toBinaryString(num);
		List<Integer> list = new ArrayList<Integer>(0);
		if (num == 0)
			list.add(num);
		for (int i = 0; i < binary.length(); i++) {
			if ((int) binary.charAt(i) == 49)
				list.add((int) Math.pow(2, binary.length() - 1 - i));
		}
		return list;
	}
	/***
	 * 
	 * @param date
	 * @param minutes
	 * @return
	 */

	public static String formatDate(String date, int minutes) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - minutes);
		return sdf.format(calendar.getTime());
	}

	/***
	 * 
	 * @param date
	 * @param haomiao
	 * @return
	 */
	public static String formatCalendar(String date, long haomiao) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		long l = d.getTime() + haomiao;
		Date d1 = new Date(l);
		return sdf.format(d1);
	}

	/**
	 * 检查一组电话号是否合法
	 * @param phone
	 * @return
	 */
	public static String checkphone(String phone) {
		if (null != phone) {
			String reisphoto = phone.replace("，", ",").replace(";", ",")
					.replace("；", ",").replace("　", ",").replace(" ", ",")
					.replace("/", ",").replace("\\", ",");
			String[] photo1 = reisphoto.split(",");
			String[] photo2 = new String[photo1.length];
			boolean isfirst;
			if (null != photo1 && photo1.length > 0) {
				for (int i = 0; i < photo1.length; i++) {
					isfirst = false;
					if (photo1[i]
							.matches("(^[0-9]{3,4}-[0-9]{3,8}$)|^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|2|3|5|6|7|8|9])\\d{8}$")) {
						photo2[i] = photo1[i];
						isfirst = true;
					}
					// 第二规则 “-”+数字位数不限 和手机号前面加86的情况也考虑
					if (!isfirst) {
						if (photo1[i]
								.matches("(^[0-9]{3,4}-[0-9]{3,8}-[0-9]{0,100}$)|^((\\+86)|(86))?(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|2|3|5|6|7|8|9])\\d{8}$")) {
							photo2[i] = photo1[i];
						}
					}
				}
				// 如果两个电话 只用一个
				if (photo2.length > 0) {
					return photo2[0];
				}
			}
		}
		return null;
	}
	
	public static boolean isIdentityNumber(String paramString){
		String value = paramString;
		String regExp = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(value);
		return m.matches();
	}
	/**
	 * 检查手机号是否合法
	 * @param str
	 * @return
	 */
	public static boolean isCellphone(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 检查邮箱是否合法
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 检查是不是中文
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		 for (int i = 0; i < str.length(); i++) { 
	            String bb = str.substring(i, i+1); 
	            Pattern pattern = Pattern.compile("[\u4E00-\u9FA5]");
	    		Matcher matcher = pattern.matcher(bb);
	    		if (!matcher.matches()) {
	    			return false;
	    		}
		 }
		 return true;
	}
	
	/**
	 * 检查手机号码是否合法
	 * @param mobiles
	 * @return
	 */
	public static boolean checkPhoneNumber(String paramString) {
		String value = paramString;
		String regExp = "^13[0-9]{1}[0-9]{8}$|15[0125689]{1}[0-9]{8}$|18[0-3,5-9]{1}[0-9]{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(value);
		return m.matches();
	}
	/**
	 * 价钱格式化
	 * @param pattern
	 * @param value
	 * @return
	 */
	public static String changeMoney(String pattern,String value){
		double va = 0;
		try {
			va = Double.parseDouble(value);
		} catch (Exception e) {
			return "";
		}
		DecimalFormat myFormatter = new DecimalFormat(pattern);
		myFormatter.applyPattern(pattern);
		return myFormatter.format(va);
	}
	/**
	 * 字符串格式化
	 * @param format
	 * @param targetObc
	 * @return
	 */
	public static String format(String format,Object targetObc){
		String target=String.valueOf(targetObc);
		if(format!=null && !format.equals("")){
			if(format.contains("#")){
				String head=format.substring(0,format.indexOf("#"));
				String body=format.substring(format.indexOf("#"));
				//String tail=format.substring(format.lastIndexOf("#")+1);
				StringBuilder result=new StringBuilder(head);
				int j=0;
				for(int i=0;i<target.length();i++){
					if(j>body.length()-1){
						j=0;
					}
					String s=String.valueOf(body.charAt(j));
					if("#".equals(s)){
						result.append(String.valueOf(target.charAt(i)));
					}else{
						result.append(String.valueOf(body.charAt(j)));
						//result.append(String.valueOf(target.charAt(i)));
						i--;
					}
					j++;
				}
				return result.toString();
			}else{
				return target;
			}
		}else{
			return target;
		}
	}


}
