package com.qingfengweb.lottery.bean;

public class OrderBean {
	public static String tbName = "orderinfo";
	public static String tbCreateSql = "create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"order_id text," +
			"member_id text," +
			"order_no text," +
			"amount text," +
			"bonus text," +
			"ispaid text," +
			"paid_stmp text," +
			"isdeal text," +
			"deal_stmp text," +
			"iswin text," +
			"is_trace text," +
			"trace_count text," +
			"stop_after_win text," +
			"create_stmp text," +
			"username text" +
			")";
	public String order_id = "";
	public String member_id = "";
	public String order_no = "";
	public String amount = "";
	public String bonus = "";
	public String ispaid = "";
	public String paid_stmp = "";
	public String isdeal = "";
	public String deal_stmp = "";
	public String iswin = "";
	public String is_trace = "";
	public String trace_count = "";
	public String stop_after_win = "";
	public String create_stmp = "";
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	public String getIspaid() {
		return ispaid;
	}
	public void setIspaid(String ispaid) {
		this.ispaid = ispaid;
	}
	public String getPaid_stmp() {
		return paid_stmp;
	}
	public void setPaid_stmp(String paid_stmp) {
		this.paid_stmp = paid_stmp;
	}
	public String getIsdeal() {
		return isdeal;
	}
	public void setIsdeal(String isdeal) {
		this.isdeal = isdeal;
	}
	public String getDeal_stmp() {
		return deal_stmp;
	}
	public void setDeal_stmp(String deal_stmp) {
		this.deal_stmp = deal_stmp;
	}
	public String getIswin() {
		return iswin;
	}
	public void setIswin(String iswin) {
		this.iswin = iswin;
	}
	public String getIs_trace() {
		return is_trace;
	}
	public void setIs_trace(String is_trace) {
		this.is_trace = is_trace;
	}
	public String getTrace_count() {
		return trace_count;
	}
	public void setTrace_count(String trace_count) {
		this.trace_count = trace_count;
	}
	public String getStop_after_win() {
		return stop_after_win;
	}
	public void setStop_after_win(String stop_after_win) {
		this.stop_after_win = stop_after_win;
	}
	public String getCreate_stmp() {
		return create_stmp;
	}
	public void setCreate_stmp(String create_stmp) {
		this.create_stmp = create_stmp;
	}
	
}
