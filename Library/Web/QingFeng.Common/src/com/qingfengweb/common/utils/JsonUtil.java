package com.qingfengweb.common.utils;



import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author  ck
 * @version 1.0
 */

public class JsonUtil {
	/**
	 * json数据中key以对象的字段显示
	 * value=0
	 */
	public static final byte KM_PROTOTYPE=(byte)0;
	/**
	 * json数据中key以对象的字段转换为小写显示
	 * value=1
	 */
	public static final byte KM_LOWER_CASE=(byte)1;
	/**
	 * json数据中key以对象的字段转换为大写显示
	 * value=2
	 */
	public static final byte KM_UPPER_CESE=(byte)2;
	
	/**
	 * json数据中key没有任何标点符号
	 * value=2
	 */
	public static final byte KS_NONE=(byte)0;
	/**
	 * json数据中key被单引号包围
	 * value=2
	 */
	public static final byte KS_SINGLE_QUOTATION_MARKS =(byte)-1;
	/**
	 * json数据中key被双引号包围
	 * value=2
	 */
	public static final byte KS_DOUBLE_QUOTATION_MARKS =(byte)-2;
	
	/**
	 * 获取对象中为private的属性的值
	 */
	public static final byte FIELD_MODIFIER_PRIVATE=(byte)-128;
	
	/**
	 * 获取对象中为public的属性的值
	 */
	public static final byte FIELD_MODIFIER_PUBLIC=(byte)-127;
	
	/**
	 * 获取对象中为protected的属性的值
	 */
	public static final byte FIELD_MODIFIER_PROTECTED=(byte)-126;
	
	
	public JsonUtil(){}
	
	
	/**
	 * 获取对象中所有的属性的值
	 */
	public static final byte FIELD_MODIFIER_ALL=(byte)0;
	/**
	 * 将对象转换为json字符串
	 * @param o 要转换的对象
	 * @param keyManifestation json字符串中key的显示形式(原型，大写，小写)
	 * @param keySign json字符串是key的包围形式(无，双引号，单引号)
	 * @param modifier 要获取对象中的字段的修饰符（all，public，private，protected）
	 * @param showNull 是否将值为null的字段也包含在json字符串中
	 * @param outFieldNames 指定json字符串的key与outFieldNames以一对应
	 * @param fieldNames 指定获取哪些字段然后凭借成json字符串
	 * @return 返回一个json字符串
	 */
	public String toJson(Object o,byte keyManifestation, byte keySign,byte modifier, boolean showNull,String[] outFieldNames, String...fieldNames){
		StringBuilder builder=new StringBuilder();
		builder.append("{");
		try {
			//按照修饰符获得对象o以及o所继承过来的字段
			List<Field> fields=JsonUtil.getSuperFields(o, modifier, fieldNames);
			//获取o以及o上层的父类的实例
			List<Object> objects=JsonUtil.getSuperClass(o,new ArrayList<Object>(0));
			int i=0;
			boolean isAccord=outFieldNames!=null&&outFieldNames.length>0&&fieldNames!=null&&fieldNames.length>0&&outFieldNames.length==fieldNames.length;
			boolean a=false;
			if(fieldNames!=null)
			for(int g=0;g<fieldNames.length;g++){
				if(fieldNames[g].indexOf("!")!=-1){
					a=true;
					break;
				}
			}
			for(Field field:fields){
				i++;
				field.setAccessible(true);
				String fieldName=null;
				if(isAccord&&fieldNames[i-1].equals(field.getName())){
					fieldName=outFieldNames[i-1];
				}else{
					if(fieldNames!=null&&fieldNames.length>0&&!a){
						String str=fieldNames[i-1];
							if(str.lastIndexOf("^")!=-1){
								if(str.lastIndexOf("-")!=-1){
									str=str.substring(str.lastIndexOf("-")+1,str.lastIndexOf("^"));
									fieldName=str;
								}else{
									fieldName=str.substring(0,str.lastIndexOf("^"));
								}
							}else{
								if(str.lastIndexOf("-")!=-1){
									str=str.substring(str.lastIndexOf("-")+1);
									fieldName=str;
								}else{
									fieldName=str;
								}
							}
							
						
					}else{
						fieldName=field.getName();
					}
				}
				
				Object value=null;
				for (int j = 0; j < objects.size(); j++) {
					try {
						value=field.get(objects.get(j));
						if(value!=null)break;
					} catch (Exception e) {
						continue;
					}
				}
				//field.get(srcObject);
				if(keyManifestation==this.KM_LOWER_CASE){//key以小写显示
					fieldName=fieldName.toLowerCase();
				}else if(keyManifestation==this.KM_UPPER_CESE){//可以以大写显示
					fieldName=fieldName.toUpperCase();
				}
				if(keySign==this.KS_SINGLE_QUOTATION_MARKS){//key被单引号包围
					fieldName="\'"+fieldName+"\'";
				}else if(keySign==this.KS_DOUBLE_QUOTATION_MARKS){//可以被双引号包围
					fieldName="\""+fieldName+"\"";
				}
				//要拼接到value上的值
				String add="";
				//是否拼在最前面，反之最后面
				boolean isF=true;
				String s="";
				if(fieldNames!=null&&fieldNames.length>0&&!a){
					s=fieldNames[i-1];
				}
				if(s.indexOf("^")!=-1){
					add=s.substring(s.indexOf("^")+1);
					if(add.indexOf("#")!=-1){
						add=add.substring(1);
						isF=false;
					}
				}
				
				if(showNull){
					if(value instanceof Date){
						if(isF)
							value=add+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value));
						else
							value=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value)+add;
					}else if(value!=null&&value.getClass().isEnum()){
						continue;
					}
					if(value instanceof List){
						value=toJsonArray((List)value);
						builder.append(fieldName+":"+(value==null?"[]":value));
					}else{
						
						if(value instanceof String){
							value=StringUtils.formatJSONString((String) value);
							//value=((String) value).replace("\"", "\\\"");
							//value=((String) value).replace("\\", "\\\\");
						}
						if(isF&&value!=null)
							value=add+value;
						if(keySign==this.KS_SINGLE_QUOTATION_MARKS){//key被单引号包围
							builder.append(fieldName+":"+(value==null?"''":"'"+value+"'"));
						}else if(keySign==this.KS_DOUBLE_QUOTATION_MARKS){//可以被双引号包围
							builder.append(fieldName+":"+(value==null?"\"\"":"\""+value+"\""));
						}
					}
					
					
				}else{
					if(value!=null){
						if(value instanceof Date){
							if(isF)
								value=add+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value));
							else
								value=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value)+add;
						}else if(value!=null&&value.getClass().isEnum()){
							continue;
						}
						if(value instanceof List){
							value=toJsonArray((List)value);
							builder.append(fieldName+":"+(value==null?"[]":value));
						}else{
							if(isF)
								value=add+value;
							else
								value =value+add;
							if(keySign==this.KS_SINGLE_QUOTATION_MARKS){//key被单引号包围
								builder.append(fieldName+":'"+value+"'");
							}else if(keySign==this.KS_DOUBLE_QUOTATION_MARKS){//key被双引号包围
								builder.append(fieldName+":\""+value+"\"");
							}
						}
					}else{
						continue;
					}
					
				}
				
				if(i<fields.size())builder.append(",");
			}
				if(builder.indexOf(",",builder.length()-1)!=-1)builder.deleteCharAt(builder.length()-1);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		builder.append("}");
		return builder.toString();
	}
	
	
	
	
	
	/**
	 * 将对象转换为json字符串
	 * @param o 要转换的对象
	 * @param keyManifestation json字符串中key的显示形式(原型，大写，小写)
	 * @param keySign json字符串是key的包围形式(无，双引号，单引号)
	 * @param modifier 要获取对象中的字段的修饰符（all，public，private，protected）
	 * @param showNull 是否将值为null的字段也包含在json字符串中
	 * @return 返回一个json字符串
	 */
	public String toJson(Object o,byte keyManifestation, byte keySign,byte modifier, boolean showNull){
		return toJson(o, keyManifestation, keySign, modifier, showNull, null,null);
	}
	/**
	 * 将对象转换为json字符串
	 * @param o 要转换的对象
	 * @param keyManifestation json字符串中key的显示形式(原型，大写，小写)
	 * @param keySign json字符串是key的包围形式(无，双引号，单引号)
	 * @param modifier 要获取对象中的字段的修饰符（all，public，private，protected）
	 * @return 返回一个json字符串
	 */
	public String toJson(Object o,byte keyManifestation, byte keySign,byte modifier){
		return toJson(o, keyManifestation, keySign, modifier, true);
	}
	
	public String toJson(Object o,boolean showNull){
		return toJson(o, JsonUtil.KM_LOWER_CASE,JsonUtil.KS_DOUBLE_QUOTATION_MARKS, JsonUtil.FIELD_MODIFIER_PRIVATE, showNull);
	}
	
	/**
	 * 将对象转换为json字符串
	 * @param o 要转换的对象
	 * @return 返回一个json字符串
	 */
	public String toJson(Object o){
		return toJson(o, JsonUtil.KM_LOWER_CASE, JsonUtil.KS_DOUBLE_QUOTATION_MARKS, JsonUtil.FIELD_MODIFIER_PRIVATE);
	}
	
	/**
	 * 将对象转换为json字符串
	 * @param o 要转换的对象
	 * @param fieldNames 指定获取哪些字段然后凭借成json字符串
	 * @return 返回一个json字符串
	 */
	public String toJson(Object o,String[] outFieldNames,String...fieldNames){
		return toJson(o, JsonUtil.KM_LOWER_CASE, JsonUtil.KS_DOUBLE_QUOTATION_MARKS, JsonUtil.FIELD_MODIFIER_PRIVATE, true,outFieldNames,fieldNames);
	}
	
	/**
	 * 将对象转换为json字符串
	 * @param o 要转换的对象
	 * @param fieldNames 指定获取哪些字段然后凭借成json字符串
	 * @return 返回一个json字符串
	 */
	public String toJson(Object o,String...fieldNames){
		return toJson(o, JsonUtil.KM_LOWER_CASE, JsonUtil.KS_DOUBLE_QUOTATION_MARKS, JsonUtil.FIELD_MODIFIER_PRIVATE, true,null,fieldNames);
	}
	
	public String toJson(Object o,boolean showNull,String...fieldNames){
		return toJson(o, JsonUtil.KM_LOWER_CASE, JsonUtil.KS_DOUBLE_QUOTATION_MARKS, JsonUtil.FIELD_MODIFIER_PRIVATE, showNull,null,fieldNames);
	}
	/*public String toJsonArray(List list,String...fieldNames){
		return toJsonArray(list,null,fieldNames);
	}*/
	
	
	public String toJsonArray(List<Object> list){
		return toJsonArray(list, null);
	}
	
	public String toJsonArray(List<Object> list,boolean showNull){
		return toJsonArray(list,showNull, null);
	}
	
	/**
	 * 将一个map里面的键值对转换为json字符串
	 * @param map
	 * @return json字符串
	 */
	public String toJson(Map<String, String> map){
		StringBuilder builder=new StringBuilder();
		builder.append("{");
		if(map!=null&&map.size()>0){
			Iterator<Map.Entry<String, String>> iterator=map.entrySet().iterator();
			Map.Entry<String, String> entry=null;
			while(iterator.hasNext()){
				try {
					entry=iterator.next();
					builder.append("\""+entry.getKey()+"\""+":"+"\""+entry.getValue()+"\"");
					if(iterator.hasNext())builder.append(",");
				} catch (Exception e) {
					continue;
				}
			}
			if(builder.indexOf(",",builder.length()-1)!=-1)builder.deleteCharAt(builder.length()-1);
		}
		builder.append("}");
		return builder.toString();
	}
	
	
	
	
	/**
	 * 将对象转换为json数组字符串
	 * @param args 要转换的对象数组
	 * @param keyManifestation json字符串中key的显示形式(原型，大写，小写)
	 * @param keySign json字符串是key的包围形式(无，双引号，单引号)
	 * @param modifier 要获取对象中的字段的修饰符（all，public，private，protected）
	 * @param showNull 是否将值为null的字段也包含在json字符串中
	 * @param fieldNames 指定获取哪些字段然后凭借成json字符串
	 * @return 返回一个json数组字符串
	 */
	public String toJsonArray(Object[] args,byte keyManifestation, byte keySign,byte modifier, boolean showNull,String[] outFieldNames,String...fieldNames){
		StringBuilder builder=new StringBuilder();
		builder.append("[");
		if (args!=null&&args.length>0) {
			int i=1;
			for(Object o:args){
				String json=this.toJson(o, keyManifestation, keySign,modifier,showNull,outFieldNames,fieldNames);
				if(!"{}".equals(json)){builder.append(json);if(i<args.length)builder.append(",");}
				i++;
			}
			if(builder.indexOf(",",builder.length()-1)!=-1)builder.deleteCharAt(builder.length()-1);
		}
		
		builder.append("]");
		return builder.toString();
	}
	
	
	
	/**
	 * 将对象转换为json数组字符串
	 * @param args 要转换的对象数组
	 * @param keyManifestation json字符串中key的显示形式(原型，大写，小写)
	 * @param keySign json字符串是key的包围形式(无，双引号，单引号)
	 * @param modifier 要获取对象中的字段的修饰符（all，public，private，protected）
	 * @param showNull 是否将值为null的字段也包含在json字符串中
	 * @return 返回一个json数组字符串
	 */
	public String toJsonArray(Object[] args,byte keyManifestation, byte keySign,byte modifier, boolean showNull){
		return toJsonArray(args, keyManifestation, keySign, modifier, showNull, null,null);
	}
	/**
	 * 将对象转换为json数组字符串
	 * @param args 要转换的对象数组
	 * @param keyManifestation json字符串中key的显示形式(原型，大写，小写)
	 * @param keySign json字符串是key的包围形式(无，双引号，单引号)
	 * @param modifier 要获取对象中的字段的修饰符（all，public，private，protected）
	 * @return 返回一个json数组字符串
	 */
	public String toJsonArray(Object[] args,byte keyManifestation, byte keySign,byte modifier){
		return toJsonArray(args, keyManifestation, keySign, modifier, true);
	}
	/**
	 * 将对象转换为json数组字符串
	 * @param args 要转换的对象数组
	 * @return 返回一个json数组字符串
	 */
	public String toJsonArray(Object[] args){
		return toJsonArray(args,JsonUtil.KM_LOWER_CASE, JsonUtil.KS_DOUBLE_QUOTATION_MARKS, JsonUtil.FIELD_MODIFIER_PRIVATE);
	}
	
	public String toJsonArray(Object[] args,boolean showNull){
		return toJsonArray(args,JsonUtil.KM_LOWER_CASE, JsonUtil.KS_DOUBLE_QUOTATION_MARKS, JsonUtil.FIELD_MODIFIER_PRIVATE,showNull);
	}
	/**
	 * 将一个map数组里面的键值对转换为json数组字符串
	 * @param maps 要转换的对象数组
	 * @return 返回一个json数组字符串
	 */
	public  String toJsonArray(Map<String, String>...maps){
		StringBuilder builder=new StringBuilder("[");
		if(maps!=null&&maps.length>0){
			for(int i=0;i<maps.length;++i){
				String json=this.toJson(maps[i]);
				if(!"{}".equals(json)){builder.append(json);if(i<maps.length-1)builder.append(",");}
			}
			if(builder.indexOf(",",builder.length()-1)!=-1)builder.deleteCharAt(builder.length()-1);
		}
		builder.append("]");
		return builder.toString();
	}
	/**
	 * 将对象转换为json数组字符串
	 * @param args 要转换的对象数组
	 * @param fieldNames 指定获取哪些字段然后凭借成json字符串
	 * @return 返回一个json数组字符串
	 */
	public String toJsonArray(Object[] args,String[] outFieldNames,String...fieldNames){
		return toJsonArray(args, JsonUtil.KM_LOWER_CASE, JsonUtil.KS_DOUBLE_QUOTATION_MARKS, JsonUtil.FIELD_MODIFIER_PRIVATE, true,outFieldNames, fieldNames);
	}
	
	public String toJsonArray(Object[] args,String...fieldNames){
		return toJsonArray(args, JsonUtil.KM_LOWER_CASE, JsonUtil.KS_DOUBLE_QUOTATION_MARKS, JsonUtil.FIELD_MODIFIER_PRIVATE, true,null, fieldNames);
	}
	public String toJsonArray(Object[] args,boolean showNull,String...fieldNames){
		return toJsonArray(args, JsonUtil.KM_LOWER_CASE, JsonUtil.KS_DOUBLE_QUOTATION_MARKS, JsonUtil.FIELD_MODIFIER_PRIVATE, showNull,null, fieldNames);
	}
	
	public String toJsonArray(List list,String[] outFieldNames,String...fieldNames){
		if (list==null||list.size()<1) {
			return "[]";
		}
		Object[] object=list.toArray();
		return toJsonArray(object,outFieldNames,fieldNames);
	}
	
	public String toJsonArray(List list,String...fieldNames){
		if (list==null||list.size()<1) {
			return "[]";
		}
		Object[] object=list.toArray();
		return toJsonArray(object,fieldNames);
	}
	
	public String toJsonArray(List list,boolean showNull,String...fieldNames){
		if (list==null||list.size()<1) {
			return "[]";
		}
		Object[] object=list.toArray();
		return toJsonArray(object,showNull,fieldNames);
	}
	
	
	
	
	/**
	 * 将一个json插入到另一个json作为key所对应的值
	 * @param insertStr 要插入的json字符串
	 * @param src 插入到哪里json字符串
	 * @param key 
	 * @param index 
	 * @return
	 */
	public static String insertIntoJson(String insertStr,String src,String key,int index,boolean isFirst,boolean isLast){
		StringBuilder builder=new StringBuilder(src);
		if(isFirst&&key!=null){
			key+=",";
		}else if(isLast&&key!=null){
			key=","+key;
		}
		builder.insert(index,key==null?insertStr:key+":"+insertStr);
		return builder.toString();
	}
	
	public static String inserIntoJson(String insertStr,String src,String key,boolean isFirst,boolean isLast){
		return insertIntoJson(insertStr,src,key,1,isFirst,isLast);
	}
	
	
	
	/**
	 * 将一个对象转为object类型（本方法可以解决基本数据类型问题）
	 * @param o
	 * @return
	 */
	public static Object toObject(Object o){
		return o;
	}
	/**
	 * 通过一个类来依次获取父类，直到hierarchy小于1为止
	 * @param o 
	 * @param hierarchy 查找到第几个父类
	 * @return
	 * @throws Exception
	 */
	public static List<Object> getSuperClass(Object o,int hierarchy,List<Object> list) throws Exception{
		if(list.size()==0){
			list.add(o);
		}
		if(hierarchy<1)return list;
		//通过对象o获得o的Class对象，然后再获得其父类的Class对象
		Class<?> classtype=o.getClass().getSuperclass();
		//获得其父类的无参构造
		if(classtype==null)return list;
		Constructor<?> constructor=classtype.getDeclaredConstructor(new Class<?>[]{});
		//设置其访问权限
		constructor.setAccessible(true);
		list.add(constructor.newInstance(new Object[]{}));
		hierarchy--;
		getSuperClass(constructor.newInstance(new Object[]{}),hierarchy,list);
		return list;
	}
	/**
	 * 通过一个类来依次获取父类，知道父类为Object为止
	 * @param o 
	 * @return
	 * @throws Exception
	 */
	public static List<Object> getSuperClass(Object o,List<Object> list) throws Exception{
		return getSuperClass(o,500,list);
	}
	
	public static List<Field> getSuperFields(Object o,byte modifier,String...fieldNames ) throws Exception{
		//访问修饰符：1-->public 2-->private 4-->protected  8-->static  16-->final
		List<Object>objects=getSuperClass(o,new ArrayList<Object>());
		List<Field> fieldList=new ArrayList<Field>(0);
		for(Object obj:objects){
			Field[] fields=null;
			
				fields=obj.getClass().getDeclaredFields();
				//只获修饰符为public的属性
				if(modifier==JsonUtil.FIELD_MODIFIER_PUBLIC){
					for(Field f:fields){
						if((f.getModifiers()&1)==1){
							fieldList.add(f);
						}
					}
					//只获修饰符为private的属性
				}else if(modifier==JsonUtil.FIELD_MODIFIER_PRIVATE){
					for(Field f:fields){
						if((f.getModifiers()&2)==2){
							fieldList.add(f);
						}
					}
					//只获修饰符为protected的属性
				}else if(modifier==JsonUtil.FIELD_MODIFIER_PROTECTED){
					for(Field f:fields){
						if((f.getModifiers()&2)==4){
							fieldList.add(f);
						}
					}
					//获取所有的属性
				}else if(modifier==JsonUtil.FIELD_MODIFIER_ALL){
					for(Field f:fields){
						fieldList.add(f);
					}
				}
			
		}
		if(fieldNames!=null&&fieldNames.length>0){
			List<Field> tempsFields=new ArrayList<Field>(0);
			boolean a=false;
			for (int i = 0; i < fieldNames.length; i++) {
				int j=0;
				if(a)break;
				for(Field f:fieldList){
					String str=fieldNames[i];
					if(str.indexOf("-")!=-1){
						str=str.substring(0, str.indexOf("-"));
					}else if(str.indexOf("!")!=-1){
						a=true;
						break;
					}
				
						if((f.getName().equals(str))){
							tempsFields.add(fieldList.get(j));
							break;
						}
					
					j++;
				}
			}
			if(a){
				tempsFields=new ArrayList<Field>(0);
				for(int i=0;i<fieldList.size();i++){
					boolean b=false;
					for(int j=0;j<fieldNames.length;j++){
						String str=fieldNames[j];
						if(str.indexOf("!")!=-1){
							str=str.substring(str.indexOf("!")+1);
						}else{
							str="";
						}
						
						if((fieldList.get(i).getName().equals(str))){
							b=true;
							break;
						}
					}
					if(!b)tempsFields.add(fieldList.get(i));
				}
				
			}
			fieldList=tempsFields;
		}
		
		return fieldList;
	}

	
	public static List<Field> getSuperFields(Object o,byte modifier ) throws Exception{
		return JsonUtil.getSuperFields(o,modifier,null);
	}
	
	
	
}
