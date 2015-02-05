package com.chinaLife.claimAssistant.tools;

import java.util.Date;

public class sc_VisiteTimes2 {
	private int times;
	private static sc_VisiteTimes2 mInstance = null;
	public static sc_VisiteTimes2 getInstance() {
		if (mInstance == null) {
			mInstance = new sc_VisiteTimes2();
		}
		return mInstance;
	}
	private Long currentTime;

	private int timeGap=60000;//一分钟

	public sc_VisiteTimes2(){
		this.currentTime=new Date().getTime();
		this.times=1;
	}
	
	/**
	 * 初始化方法
	 */
	public void init(){
		this.currentTime=new Date().getTime();
		this.times=1;
	}
	
	/**
	 * 访问次数累加
	 */
	public void count(){
		times++;
	}
	
	/**
	 * 在规定时间内访问是否超过
	 * @return
	 */
	public boolean isOut(){
		long a=new Date().getTime();
		if(times>20&&a<currentTime+timeGap){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否初始化
	 * @return
	 */
	public boolean isInit(){
		long a=new Date().getTime();
		if(a>currentTime+timeGap)
			return true;
		return false;
	}
	
	/**
	 * @return the times
	 */
	public int getTimes() {
		return times;
	}

	/**
	 * @param times the times to set
	 */
	public void setTimes(int times) {
		this.times = times;
	}

	/**
	 * @return the currentTime
	 */
	public Long getCurrentTime() {
		return currentTime;
	}

	/**
	 * @param currentTime the currentTime to set
	 */
	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}

	/**
	 * @param timeGap the timeGap to set
	 */
	public void setTimeGap(int timeGap) {
		this.timeGap = timeGap;
	}
	
	
}
