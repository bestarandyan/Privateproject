package com.qingfengweb.piaoguanjia.ticketverifier.bean;

public class ValidateResultInfo {
	private String successCount;	//成功数量
	private String failureCount;	//失败数量
	private String totalCount;	//传过来的总数量
	public String getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(String successCount) {
		this.successCount = successCount;
	}
	public String getFailureCount() {
		return failureCount;
	}
	public void setFailureCount(String failureCount) {
		this.failureCount = failureCount;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	
	
	
}
