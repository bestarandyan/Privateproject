package com.qingfengweb.share;

public abstract class ShareSDK {
	
	
	public static final int SUCCESS=-213232422;
	public static final int ERROR = -421341313;
	/**
	 * 验证是否绑定账户
	 * @return
	 */
	public abstract boolean isAuthorize();
	/**
	 * 绑定账户
	 */
	public abstract void authorize();
	/**
	 * 发送信息
	 */
	public abstract boolean sendMsg(String msg,String picurl);
}
