package com.qingfengweb.piaoguanjia.orderSystem.request;

public class OrderServlet extends SimpleServlet {
	public static final String SUFFIXINTERFACE_USER = "/interface/order/product";// 产品接口
	// ---------------------------------------------------------------------
	public static final String ACTION_PENDINGORDERLIST = "pendingOrderList";// 待处理订单
	public static final String ACTION_ORDERLIST = "orderList";// 订单列表
	public static final String ACTION_ORDER = "order";// 订单详情
	public static final String ACTION_RESENDMESSAGE = "resendMessage";// 重发短信
	public static final String ACTION_CANCELORDER = "cancelOrder";// 取消订单
	public static final String ACTION_APPLYREFUNDORDER = "applyRefundOrder";// 申请退款
	
}
