/**
 * 
 */
package com.qingfengweb.lottery.bean;

/**
 * @author 刘星星
 * @createDate 2013、12、9
 *用户余额表
 */
public class UserBalanceBean {
	public static String tabName = "userbalance";
	public static String tabCreateSql = "create table if not exists "+tabName+"" +
			"(_id Integer primary key autoincrement," +
			"username text," +
			"total_balance varchar(30)," +
			"freeze_balance varchar(30)," +
			"available_balance varchar(30))";
	
	public String total_balance = "";
	public String freeze_balance = "";
	public String available_balance = "";
	public String getTotal_balance() {
		return total_balance;
	}
	public void setTotal_balance(String total_balance) {
		this.total_balance = total_balance;
	}
	public String getFreeze_balance() {
		return freeze_balance;
	}
	public void setFreeze_balance(String freeze_balance) {
		this.freeze_balance = freeze_balance;
	}
	public String getAvailable_balance() {
		return available_balance;
	}
	public void setAvailable_balance(String available_balance) {
		this.available_balance = available_balance;
	}
	
	
	
	
}
