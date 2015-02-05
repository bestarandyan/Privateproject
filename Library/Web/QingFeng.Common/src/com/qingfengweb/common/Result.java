package com.qingfengweb.common;

/**
 * 操作结果类
 * @author GuoLiang
 *
 */
public class Result<T> {
	
	/**
	 * 操作是否成功
	 */
	private boolean isSuccess;
	/**
	 * 操作返回的状态码
	 */
	private Long statusCode;
	/**
	 * 操作返回的状态文本
	 */
	private String message;
	/**
	 * 操作返回的数据
	 */
	private T returnValue;
	/**
	 * 构造函数(默认)
	 */
	public Result() {
		
	}
	
	/**
	 * 构造函数，一般用于只返回操作是否成功
	 * @param isSuccess 操作是否成功
	 */
	public Result(boolean isSuccess) {
		super();
		this.isSuccess = isSuccess;
	}

	
	
	/**
	 * 构造函数，一般用于操作成功时返回数据
	 * @param isSuccess 操作是否成功
	 * @param returnValue 返回值
	 */
	public Result(boolean isSuccess, T returnValue) {
		super();
		this.isSuccess = isSuccess;
		this.returnValue = returnValue;
	}
	
	
	

	/**
	 * 构造函数，一般用于操作失败时，返回状态码和错误信息
	 * @param isSuccess 操作是否成功
	 * @param statusCode 状态码
	 * @param message 操作相关信息
	 */
	public Result(boolean isSuccess, Long statusCode, String message) {
		super();
		this.isSuccess = isSuccess;
		this.statusCode = statusCode;
		this.message = message;
	}
	/**
	 * 构造函数（完整）
	 * @param isSuccess 操作是否成功
	 * @param statusCode 状态码
	 * @param message 操作相关信息
	 * @param returnValue 返回值
	 */
	public Result(boolean isSuccess, Long statusCode, String message,
			T returnValue) {
		super();
		this.isSuccess = isSuccess;
		this.statusCode = statusCode;
		this.message = message;
		this.returnValue = returnValue;
	}
	/**
	 * 构造函数，是否成功，状态码，和返回值
	 * @param isSuccess
	 * @param statusCode
	 * @param returnValue
	 */
	public Result(boolean isSuccess, Long statusCode, T returnValue) {
		super();
		this.isSuccess = isSuccess;
		this.statusCode = statusCode;
		this.returnValue = returnValue;
	}

	/**
	 * 获取操作过程中返回的信息
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * 设置操作过程中返回的信息
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * 获取返回值
	 * @return the returnValue
	 */
	public T getReturnValue() {
		return returnValue;
	}
	/**
	 * 设置返回值
	 * @param returnValue the returnValue to set
	 */
	public void setReturnValue(T returnValue) {
		this.returnValue = returnValue;
	}
	/**
	 * 获取操作是否成功
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return isSuccess;
	}
	/**
	 * 设置操作是否成功
	 * @param isSuccess the isSuccess to set
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	/**
	 * 获取状态码
	 * @return the statusCode
	 */
	public Long getStatusCode() {
		return statusCode;
	}
	/**
	 * 设置状态码
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(Long statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * 转换成JSON字符串
	 * @return
	 */
	public String toJSONString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append(String.format("\"isSucess\":%s,",this.isSuccess()?"true":"false"));
		builder.append(String.format("\"message\":\"%s\",",this.getMessage()));
		builder.append(String.format("\"code\":%d", this.getStatusCode()));
		builder.append("}");
		return builder.toString();
	}
}
