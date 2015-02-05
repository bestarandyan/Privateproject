package com.piaoguanjia.chargeclient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.string;

public class Constant {
	//����Ϊ���Ӻ�̨�����ֶ�
	public static final String CONNECT =  "http://www.piaoguanjia.com/";
//	public static final String CONNECT =  "http://192.168.1.122:7001/";
	public static final String APPKEY = "abcd1234";
	public static final String APPID = "100002";
	public static final String USERLOGIN_INTEGERFACE = "interface/chargeUser";
	public static final String ADDCHARGE_INTEGERFACE = "interface/charge";
	
	//����Ϊ���ݿ���������
	public static final String DBNAME = "chargedb";
	public static final String USER_TABLE_NAME = "user";
	public static final String CHARGE_RECORD_TABLE_NAME = "chargerecord";
	public static final String LAST_UPDATE_TIME_TAB_STRING = "updatetime";
	public static final String IMAGE_TABNAME_STRING = "images";//���ͼƬ·���ı�
	
	//�����û���
	public static final String USERSQL_STRING = "create table if not exists "+USER_TABLE_NAME+"(_id integer primary key autoincrement," +
			"username varchar(40)," +
			"hasAddPerm varchar(20)," +
			"hasAuditPerm varchar(20)," +
			"userid varchar(40)," +
			"password varchar(60))";
	//������ֵ��¼��
		public static final String CHARGESQL_STRING = "create table if not exists "+CHARGE_RECORD_TABLE_NAME+"(_id integer primary key autoincrement," +
				"username varchar(40)," +
				"chargeid varchar(20)," +
				"amount varchar(20)," +
				"accountTypeDis varchar(40)," +
				"chargeTypeDistription varchar(40)," +
				"objectName varchar(40)," +
				"totalAmount varchar(40)," +
				"balance varchar(40)," +
				"auditTime varchar(40)," +
				"operator varchar(40)," +
				"auditor varchar(40)," +
				"remark varchar(200)," +
				"reason varchar(200)," +
				"image varchar(40)," +
				"status varchar(40)," +
				"createTime varchar(60))";
		//������ʱ���
		public static final String UPDATESQL_STRING = "create table if not exists "+LAST_UPDATE_TIME_TAB_STRING+"(_id integer primary key autoincrement," +
				"username varchar(40)," +//����û���
				"chargetype varchar(10)," +//��ֵ����  ����Ļ�����ʷ�� 
				"updatetime varchar(40))"//����ʱ��
				;
		//������ʱ���
		public static final String IMAGESQL_STRING = "create table if not exists "+IMAGE_TABNAME_STRING+"(_id integer primary key autoincrement," +
				"chargeid varchar(40)," +//��ֵID
				"imageurl varchar(40))"//����ʱ��
				;
//	public static final String pattern = "^-?((0(\\.{1,1}\\d+)?$)|([1-9]+(\\.\\d+)?$))";
	public static final String pattern = "^\\-?\\d+(\\.\\d+)?$";
	

	public static boolean isNumber(String str){
		return match(pattern , str);
	}
	/**
	  * ִ��������ʽ
	  * 
	  * @param pattern
	  *            ���ʽ
	  * @param str
	  *            ����֤�ַ���
	  * @return ���� <b>true </b>,����Ϊ <b>false </b>
	  */
	 private static boolean match(String pattern, String str) {
	  Pattern p = Pattern.compile(pattern);
	  Matcher m = p.matcher(str);
	  return m.matches();
	 }

}
