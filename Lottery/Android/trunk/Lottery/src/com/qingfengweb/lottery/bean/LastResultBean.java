/**
 * 
 */
package com.qingfengweb.lottery.bean;

/**
 * @author 刘星星
 * 最新开奖结果信息
 *
 */
public class LastResultBean {
	public static String tbName = "last_result_info";//开奖结果表
	public static String tbCreateSQl = "create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"ticket_no text," +//开奖期号
			"result varchar(20)," +//开奖结果
			"code varchar(20)," +//冷号
			"hot text)";//热号
	public String ticket_no = "";
	public String result = "";
	public String code = "";
	public String hot = "";
	public String getTicket_no() {
		return ticket_no;
	}
	public void setTicket_no(String ticket_no) {
		this.ticket_no = ticket_no;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getHot() {
		return hot;
	}
	public void setHot(String hot) {
		this.hot = hot;
	}
	
	
	
}
