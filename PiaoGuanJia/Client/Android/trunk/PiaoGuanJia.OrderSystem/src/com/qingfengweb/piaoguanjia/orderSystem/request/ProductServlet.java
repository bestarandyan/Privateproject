package com.qingfengweb.piaoguanjia.orderSystem.request;

import java.util.List;

import org.apache.http.NameValuePair;

public class ProductServlet extends SimpleServlet{

	public static final String SUFFIXINTERFACE_USER = "/interface/order/product";// 产品接口
	// ---------------------------------------------------------------------
	public static final String ACTION_NEWPRODUCTLIST = "newProductList";// 最新产品列表
	public static final String ACTION_SHORTCUTPRODUCTLIST = "shortcutProductList";// 快捷产品列表
	public static final String ACTION_PRODUCTLIST = "productList";// 产品列表
	public static final String ACTION_GETSCENICPAGE = "getScenicPage";//获取景区下单页

	
	
	/**
	 * 获取景区下单页
	 */
	
	public static String actionGetScenicpage() {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_GETSCENICPAGE);
		UploadData uploaddata = new UploadData(SERVER_ADDRESS+SUFFIXINTERFACE_USER, params);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		if (params != null) {
			params.clear();
		}
		return msgString;
	}
	
	/**
	 * 获取门票列表的价格，单日，总数限制
	 */
	
	
	/**
	 * 添加景区订单
	 */
	
	
	/**
	 * 
	 */
}
