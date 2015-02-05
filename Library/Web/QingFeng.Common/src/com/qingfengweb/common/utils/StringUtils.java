package com.qingfengweb.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 字符串工具类
 * @author GuoLiang
 *
 */
public class StringUtils {
	/**
	 * 从流读取字符串
	 * @param stream
	 * @param encoding
	 * @return
	 */
	 public static String stringFrom(InputStream stream,String encoding)
	 {
		StringBuilder builder = new StringBuilder();
		try
		{   
		    BufferedReader reader = new BufferedReader(new InputStreamReader(stream,encoding));
		    while (reader.ready())
		    	builder.append(reader.readLine());
		    reader.close();
		} catch (IOException ie){
		}
	    return builder.toString();
	}
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final String md5(String s)
	{
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("md5");
			return toHexString(md.digest(s.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 格式系Json字符串
	 * @param s
	 */
	public static final String formatJSONString(String s) {
		if(isNullOrEmpty(s)) return "";
		StringBuilder builder = new StringBuilder();
        for(char c:s.toCharArray())
        {
            switch (c)
            {
                case '\t': builder.append("\\t"); break;
                case '\r': builder.append("\\r"); break;
                case '\n': builder.append("\\n"); break;
                case '"':
                case '\'':
                case '\\': builder.append("\\" + c); break;
                default:
                    {
                        if (c >= ' ' && c < 128)
                        	builder.append(c);
                        else
                        	builder.append("\\u" + (Integer.toHexString((int)c)));
                    }
                    break;
            }
        }
        return builder.toString();
	}
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final String sha1(String s)
	{
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("sha-1");
			return toHexString(md.digest(s.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 字节数组转字符串
	 * @param bytes 字节数组
	 * @return 返回被转换的字符串
	 */
	public static final String toHexString(byte[] bytes) 
	{
		StringBuffer buffer = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) 
		{
			if (((int) bytes[i] & 0xff) < 0x10) 
				buffer.append("0");
			buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buffer.toString();
	}
	/**
	 * 转换为驼峰命名法
	 * @param source
	 * @return
	 */
	public static String toCamelCase(String source) {
		source = source.toLowerCase();
		while(source.indexOf("_") >=0)
		{
			int index = source.indexOf("_");
			char c = source.charAt(index+1);
			source = source.substring(0, index).concat(String.valueOf(c).toUpperCase()).concat(source.substring(index+2));
		}
		return source;
	}
	/**
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isNullOrEmpty(String source) {
		return source == null || source.length()==0;
	}
	/**
	 * 连接字符串
	 * @param array
	 * @param seperator
	 * @return
	 */
	public static String join(Object[] array,String seperator) {
		if(array == null || array.length == 0) return "";
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<array.length; i++) {
			builder.append(array[i].toString());
			if(i<array.length-1)
				builder.append(seperator);
		}
		return builder.toString();
	}
	
	public static String join(HashSet array,String seperator) {
		if(array == null || array.size() == 0) 
			return "";
		StringBuilder builder = new StringBuilder();
		for(Iterator iterator=array.iterator();iterator.hasNext();) {
			builder.append(iterator.next());
			if(iterator.hasNext())
				builder.append(seperator);
		}
		return builder.toString();
	}
	/**
	 * 左填充字符串
	 * @param source
	 * @param size
	 * @param s
	 * @return
	 */
	public static String leftPad(String source,int size,String s) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<size; i++)
			builder.append(s);
		builder.append(source);
		return builder.toString();
	}
	/**
	 * 查找多个字符其中的一个所在下标，找到一个之后就不在继续查找
	 * @param src
	 * @param strs
	 * @return
	 */
	public static int indexOfEnhanced(String src,String... strs){
		
		return indexOfEnhanced(src,false,true,strs);
	}
	
public static int lastIndexOfEnhanced(String src,String... strs){
		return indexOfEnhanced(src,true,true,strs);
	}
	
	
	/**
	 * 查找多个字符其中的一个所在下标，找到一个之后就不在继续查找
	 * @param src
	 * @param strs
	 * @return
	 */
	public static int indexOfEnhanced(String src,boolean isLast, boolean ignoreCase,String... strs){
		String str=src;
		if(ignoreCase){
			str=src.toLowerCase();
		}
		for(String s:strs){
			if(ignoreCase)s=s.toLowerCase();
			if(isLast){
				System.out.println(str.lastIndexOf(s));
				if(str.lastIndexOf(s)!=-1)
					{
						return str.lastIndexOf(s);
					}
			}else{
				if(str.indexOf(s)!=-1){
					System.out.println(str.indexOf(s));
					return str.indexOf(s);
				}
			}
		}
		return -1;
	}
	
	
	/**
	 * 查找多个字符其中的一个所在下标，找到一个之后就不在继续查找
	 * @param src
	 * @param fromIndex
	 * @param strs
	 * @return
	 */
	public static int indexOfEnhanced(String src,int fromIndex,String... strs){
		for(String s:strs){
			if(src.indexOf(s,fromIndex)!=-1)return src.indexOf(s);
		}
		return -1;
	}
	public static int indexOfEnhanced(String src,String str,int several){
		if(several<1)several=1;
		int result=-1;
		while(several>0){
			result=src.indexOf(str,result+1);
			several--;
		}
		return result;
	}
	public static List<Integer> getBitsFromIntagerNumber(Integer num){
		String binary=Integer.toBinaryString(num);
		List<Integer> list=new ArrayList<Integer>(0);
		if(num==0)list.add(num);
		for (int i = 0; i < binary.length(); i++) {
			if((int)binary.charAt(i)==49)list.add((int)Math.pow(2, binary.length()-1-i));
		}
		return list;
	}
	
	public static String join(String[] arr,String separator){
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<arr.length;i++){
			builder.append(arr[i]);
			if(i<arr.length-1)
				builder.append(separator);
		}
		return builder.toString();
	}
	
	public static String join(long[] arr,String separator){
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<arr.length;i++){
			builder.append(arr[i]);
			if(i<arr.length-1)
				builder.append(separator);
		}
		return builder.toString();
	}
	
	public static String join(int[] arr,String separator){
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<arr.length;i++){
			builder.append(arr[i]);
			if(i<arr.length-1)
				builder.append(separator);
		}
		return builder.toString();
	}
	
	public static String join(List<?> arr,String separator){
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<arr.size();i++){
			builder.append(arr.get(i));
			if(i<arr.size()-1)
				builder.append(separator);
		}
		return builder.toString();
	}
	
	/**
	 * 判断一个数里面是否包含了另一个数
	 * @param arg1 包含数
	 * @param arg2 被包含数
	 * @return
	 */
	public static boolean containStatus(Integer arg1,Integer arg2){
		return (arg1&arg2)==arg2;
	}
	
	/**
	 * 判断一个数里面是否包含了另一个数
	 * @param arg1 包含数
	 * @param arg2 被包含数组
	 * @return
	 */
	public static boolean containStatus(Integer arg1,Integer[] arg2){
		for(Integer i:arg2){
			if((arg1&i)==i)
				return true;
		}
		return false;
	}
	
	/**
	 * 判断一个数里面是否包含了另一个数
	 * @param arg1 包含数
	 * @param arg2 被包含数
	 * @return
	 */
	public static boolean containStatus(Short arg1,Short arg2){
		return (arg1&arg2)==arg2;
	}
	
	/**
	 * 判断一个数里面是否包含了另一个数
	 * @param arg1 包含数
	 * @param arg2 被包含数组
	 * @return
	 */
	public static boolean containStatus(Short arg1,Short[] arg2){
		for(Short i:arg2){
			if((arg1&i)==i)
				return true;
		}
		return false;
	}
	
	public static String format(String format,Object targetObc){
		String target=String.valueOf(targetObc);
		if(!isNullOrEmpty(format)){
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
