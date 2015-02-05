/**
 * 
 */
package com.qingfengweb.lottery.bean;

/**
 * @author 刘星星
 * @createDate 2013、12、12
 * 开奖结果查询Bean实体类
 *
 */
public class ResultInfo {
	public static String tbName = "result_info";//开奖结果表
	public static String tbCreateSQl = "create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"ticket_no text," +
			"org_results varchar(20)," +
			"results varchar(20)," +
			"open_stmp text)";
	public String ticket_no = "";
	public String org_results = "";
	public String results = "";
	public String open_stmp = "";
	public String getTicket_no() {
		return ticket_no;
	}
	public void setTicket_no(String ticket_no) {
		this.ticket_no = ticket_no;
	}
	public String getOrg_results() {
		return org_results;
	}
	public void setOrg_results(String org_results) {
		this.org_results = org_results;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public String getOpen_stmp() {
		return open_stmp;
	}
	public void setOpen_stmp(String open_stmp) {
		this.open_stmp = open_stmp;
	}
	
	
	
	
	
}
