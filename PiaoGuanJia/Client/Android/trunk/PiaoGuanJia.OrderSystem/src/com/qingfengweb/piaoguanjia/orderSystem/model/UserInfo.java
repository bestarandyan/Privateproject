package com.qingfengweb.piaoguanjia.orderSystem.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

@Table(name = "userinfo")
// 建议加上注解， 混淆后表名不受影响
public class UserInfo extends EntityBase {
	@NoAutoIncrement
	@Id
	@Column(column = "userid")
	public String userid;
	@Column(column = "username")
	public String username;
	@Column(column = "password")
	public String password;
	@Column(column = "type")
	public String type;
	@Column(column = "enterprisename")
	public String enterpriseName;
	@Column(column = "departmentname")
	public String departmentName;
	@Column(column = "position")
	public String position;
	@Column(column = "name")
	public String name;
	@Column(column = "email")
	public String email;
	@Column(column = "phonenumber")
	public String phoneNumber;//手机号码
	@Column(column = "cdinumber")
	public String cdiNumber;//身份证号码
	@Column(column = "qq")
	public String qq;//QQ号码
	@Column(column = "msn")
	public String msn;//msn号码
	@Column(column = "alipay")
	public String alipay;//支付宝账号
	@Column(column = "accountname")
	public String accountName;//银行账户
	@Column(column = "accountnumber")
	public String accountNumber;
	@Column(column = "bankname")
	public String bankName;//开户行
	@Column(column = "islimitorder")
	public int isLimitOrder;
	@Column(column = "autologin")
	public int autoLogin; //自动登陆 默认0 ，1 自动登陆
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCdiNumber() {
		return cdiNumber;
	}
	public void setCdiNumber(String cdiNumber) {
		this.cdiNumber = cdiNumber;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getMsn() {
		return msn;
	}
	public void setMsn(String msn) {
		this.msn = msn;
	}
	public String getAlipay() {
		return alipay;
	}
	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public int getIsLimitOrder() {
		return isLimitOrder;
	}
	public void setIsLimitOrder(int isLimitOrder) {
		this.isLimitOrder = isLimitOrder;
	}
	public int getAutoLogin() {
		return autoLogin;
	}
	public void setAutoLogin(int autoLogin) {
		this.autoLogin = autoLogin;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	// @Foreign(column = "parentId", foreign = "id")
	// public ForeignLazyLoader<Parent> parent;
	// @Foreign(column = "parentId", foreign = "isVIP")
	// public List<Parent> parent;
	// @Foreign(column = "parentId", foreign = "id")
	// public Parent parent;

	// // Transient使这个列被忽略，不存入数据库
	// @Transient
	// public String willIgnore;
	// public static String staticFieldWillIgnore; // 静态字段也不会存入数据库

}
