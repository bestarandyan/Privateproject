package com.qingfengweb.lottery.bean;

public class UserBean {
	public static String tbName = "userinfo";
	public static String tbCreateSql = "create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"token varchar(20)," +
			"member_id varchar(20)," +
			"username text," +
			"password text," +
			"avatar text," +
			"nick_name text," +
			"real_name text," +
			"identity_card text," +
			"mobile varchar(30)," +
			"email text," +
			"balance text," +
			"actived varchar(10)," +
			"freeze_balance text," +
			"reg_ip text," +
			"reg_stmp text," +
			"active_stmp text," +
			"enabled varchar(10)," +
			"islastuser varchar(10)," +
			"headimg_localurl text," +
			"server_stmp text)";
	
	public String token = "";
	public String member_id = "";
	public String username = "";
	public String password = "";
	public String avatar = "";
	public String nick_name = "";
	public String real_name = "";
	public String identity_card = "";
	public String mobile = "";
	public String email = "";
	public String balance = "";
	public String actived = "";
	public String freeze_balance = "";
	public String reg_ip = "";
	public String reg_stmp = "";
	public String active_stmp = "";
	public String enabled = "";
	public String server_stmp = "";
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getIdentity_card() {
		return identity_card;
	}
	public void setIdentity_card(String identity_card) {
		this.identity_card = identity_card;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getActived() {
		return actived;
	}
	public void setActived(String actived) {
		this.actived = actived;
	}
	public String getFreeze_balance() {
		return freeze_balance;
	}
	public void setFreeze_balance(String freeze_balance) {
		this.freeze_balance = freeze_balance;
	}
	public String getReg_ip() {
		return reg_ip;
	}
	public void setReg_ip(String reg_ip) {
		this.reg_ip = reg_ip;
	}
	public String getReg_stmp() {
		return reg_stmp;
	}
	public void setReg_stmp(String reg_stmp) {
		this.reg_stmp = reg_stmp;
	}
	public String getActive_stmp() {
		return active_stmp;
	}
	public void setActive_stmp(String active_stmp) {
		this.active_stmp = active_stmp;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getServer_stmp() {
		return server_stmp;
	}
	public void setServer_stmp(String server_stmp) {
		this.server_stmp = server_stmp;
	}
	
}
