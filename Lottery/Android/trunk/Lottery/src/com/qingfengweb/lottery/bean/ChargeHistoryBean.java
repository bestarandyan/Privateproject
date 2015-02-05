package com.qingfengweb.lottery.bean;

public class ChargeHistoryBean {
	public static String tbName = "chargeHistory";
	public static String tbCreateSQL = "create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"trade_id text," +
			"trade_no text," +
			"member_id text," +
			"trade_type varchar(10)," +
			"amount varchar(50)," +
			"trade_status varchar(10)," +
			"username text," +
			"trade_info text," +
			"trade_desc text," +
			"create_stmp text," +
			"balance text," +
			"update_stmp text)";
	public String trade_id = "";
	public String trade_no = "";
	public String member_id = "";
	public String trade_type = "";
	public String amount = "";
	public String trade_status = "";
	public String trade_info = "";
	public String trade_desc = "";
	public String create_stmp = "";
	public String update_stmp = "";
	public String balance = "";
	
	
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTrade_status() {
		return trade_status;
	}
	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}
	public String getTrade_info() {
		return trade_info;
	}
	public void setTrade_info(String trade_info) {
		this.trade_info = trade_info;
	}
	public String getTrade_desc() {
		return trade_desc;
	}
	public void setTrade_desc(String trade_desc) {
		this.trade_desc = trade_desc;
	}
	public String getCreate_stmp() {
		return create_stmp;
	}
	public void setCreate_stmp(String create_stmp) {
		this.create_stmp = create_stmp;
	}
	public String getUpdate_stmp() {
		return update_stmp;
	}
	public void setUpdate_stmp(String update_stmp) {
		this.update_stmp = update_stmp;
	}
	
}
