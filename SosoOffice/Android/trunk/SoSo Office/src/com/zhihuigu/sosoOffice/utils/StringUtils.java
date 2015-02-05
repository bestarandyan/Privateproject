package com.zhihuigu.sosoOffice.utils;

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

import com.google.gson.Gson;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.model.SoSoUserInfo;
import com.zhihuigu.sosoOffice.model.StateInfo;
/**
 * �ַ���������
 * 
 * @author GuoLiang
 * 
 */
public class StringUtils {
	/**
	 * ������ȡ�ַ���
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
	 * �ֽ�����ת�ַ���
	 * 
	 * @param bytes
	 *            �ֽ�����
	 * @return ���ر�ת�����ַ���
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
	 * ת��Ϊ�շ�������
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
	 * �����ַ���
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
	 * ������ַ���
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
	 * ���Ҷ���ַ����е�һ�������±꣬�ҵ�һ��֮��Ͳ��ڼ�������
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
	 * ���Ҷ���ַ����е�һ�������±꣬�ҵ�һ��֮��Ͳ��ڼ�������
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
	
	public static String formatDate1(String date, int day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - day);
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
	
	public static String formatCalendar1(String date, long haomiao) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long l = d.getTime()+haomiao;
		Date d1 = new Date(l);
		return sdf.format(d1);
	}

	/**
	 * ���һ��绰���Ƿ�Ϸ�
	 * @param phone
	 * @return
	 */
	public static String checkphone(String phone) {
		if (null != phone) {
			String reisphoto = phone.replace("��", ",").replace(";", ",")
					.replace("��", ",").replace("��", ",").replace(" ", ",")
					.replace("/", ",").replace("\\", ",");
			String[] photo1 = reisphoto.split(",");
			String[] photo2 = new String[photo1.length];
			boolean isfirst;
			if (null != photo1 && photo1.length > 0) {
				for (int i = 0; i < photo1.length; i++) {
					isfirst = false;
					if (photo1[i]
							.matches("(^[0-9]{3,4}-?[0-9]{3,8}$)|^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|2|3|5|6|7|8|9])\\d{8}$")) {
						photo2[i] = photo1[i];
						isfirst = true;
					}
					// �ڶ����� ��-��+����λ������ ���ֻ���ǰ���86�����Ҳ����
					if (!isfirst) {
						if (photo1[i]
								.matches("(^[0-9]{3,4}-?[0-9]{3,8}-?[0-9]{0,100}$)|^((\\+86)|(86))?(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|2|3|5|6|7|8|9])\\d{8}$")) {
							photo2[i] = photo1[i];
						}
					}
				}
				// ��������绰 ֻ��һ��
				if (photo2.length > 0) {
					return photo2[0];
				}
			}
		}
		return null;
	}
	/**
	 * ����ֻ����Ƿ�Ϸ�
	 * @param str
	 * @return
	 */
	public static boolean isCellphone(String str) {
		Pattern pattern = Pattern.compile("1[3,4,5,7,8]\\d{9}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * ����ֻ����Ƿ�Ϸ�
	 * @param str
	 * @return
	 */
	public static boolean istelephone(String str) {
		Pattern pattern = Pattern.compile("^(\\d{11})|(\\d{2,4}\\-?\\d{6,8}(\\-?\\d{1,6})?)$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * ��������Ƿ�Ϸ�
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
	 * ����û����ĺϷ���
	 * @param str
	 * @return
	 */
	public static boolean checkusername(String str) {
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * ����ǲ�������
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
	 * ����ֻ������Ƿ�Ϸ�
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	/**
	 * ��Ǯ��ʽ��
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
	 * �ַ�����ʽ��
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
	/***
	 * author by Ring
	 * �жϷ��������صĽ���Ƿ���ȷ
	 * true ��ȷ
	 */
	
	public static boolean CheckReponse(String reponse){
		if(reponse!=null){
			if(reponse.startsWith("{")){
				String str=getErrorState(reponse);
				if(str.equals(ErrorType.Success.getValue())){
					return true;
				}else if(str.equals("")){
					return true;
				}
			}else if(reponse.startsWith("[")){
				return true;
			}
		}
		
		return false;
		
	}
	
	/***
	 * author by Ring
	 * ��������ֵ
	 * String 
	 */
	
	public static String getErrorState(String reponse){
		if(reponse!=null){
			if(reponse.contains("{")){
				Gson gson = new Gson();// ����Gson����
				StateInfo stateinfo = null;
				try {
					stateinfo = gson.fromJson(reponse, StateInfo.class);//����json����
				} catch (Exception e) {
//					e.printStackTrace();
				}
				if(stateinfo!=null&&!stateinfo.getState().equals("")){
					return stateinfo.getState();
				}
			}else if(reponse.equals(ErrorType.SoSoTimeOut.getValue())
					||reponse.equals(ErrorType.NotFound.getValue())){
				return reponse;
			}
		}
		return "";
	}
	
	/**
	 * author by Ring
	 * ��ʽ���������յĸ�ʽ
	 * @param str1  Ҫת���ĸ�ʽ
	 * @param str2 ��ǰ�����ַ����ĸ�ʽ
	 * @param str3 ��ǰ�ַ���
	 * @return
	 */
	public static String MyformatDate(String str1,String str2,String str3){
		try {
			return ""+ new SimpleDateFormat(str1)
			.format(new SimpleDateFormat(str2).parse(str3));
		} catch (ParseException e) {
			return str3;
		}
	}
}
