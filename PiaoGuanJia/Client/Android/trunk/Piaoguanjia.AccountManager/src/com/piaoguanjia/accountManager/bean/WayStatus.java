/**
 * 
 */
package com.piaoguanjia.accountManager.bean;

/**
 */
public enum WayStatus {

	/**
	 */
	ZHIFUBAO(1,"支付宝"),
	/**
	 */
	WANGYIN(2,"网银"),
	/**
	 */
	YOUHUI(3,"邮汇"),
	/**
	 */
	GUANLIYUANYUCHONG(4,"管理员预充"),
	
	TUIKUAN(5,"退款"),
	KOUKUAN(6,"扣款"),
	DIANPIN(7,"点评返现退款"),
	GUANLIYUANYUSHOU(8,"管理员预授");
	

	private int value;
	private String name;

	/**
	 * 
	 * @param value
	 *            值
	 */
	WayStatus(int value,String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 
	 * @return 返回值
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * 
	 * @return 返回值
	 */
	public String getName() {
		return name;
	}
	
	public static String getName(int id){
		for(WayStatus childstatus: WayStatus.values()){
			if(childstatus.value==id){
				return childstatus.name;
			}
		}
		return "";
	}
	
	public static int getId(String name){
		for(WayStatus childstatus: WayStatus.values()){
			if(childstatus.name.equals(name)){
				return childstatus.value;
			}
		}
		return -1;
	}
}
