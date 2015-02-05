package com.qingfengweb.piaoguanjia.content;

public interface InterfaceContent {
	public static final String INTERFACE = "http://www.piaoguanjia.com/";////"http://192.168.1.188:8080/piaoguanjia/";//总接口地址
	public static final String USER_INTERFACE = "interface/user";//用户接口地址
	public static final String TICKET_INTERFACE = "interface/ticketorder";//票验证接口地址
	public static final String APP_ID = "100001";//应用程序
	public static final String APP_KEY = "abcd1234";//应用程序KEY
	public static final String USER_ACTION = "login";//用户登录Action动作标记
	public static final String TICKET_GET_ACTION = "validate_order";//票验证Action动作标记
	public static final String TICKET_SELECT_ACTION = "query_lastorder";
}
