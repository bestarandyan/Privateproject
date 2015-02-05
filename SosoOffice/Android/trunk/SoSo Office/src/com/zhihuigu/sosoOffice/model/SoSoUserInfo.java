package com.zhihuigu.sosoOffice.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.zhihuigu.sosoOffice.LoadActivity;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.utils.StringUtils;

public class SoSoUserInfo {
	/**author by Ring
	 * 用户信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"soso_userinfo" +//表名 soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"soso_userid text,"+//用户id
			"soso_username text,"+//用户名
			"soso_password text,"+//密码
			"soso_realname text,"+//真实姓名
			"soso_registerdate text,"+//注册时间
			"soso_userimageurl text,"+//用户图像url
			"soso_userimagesd text,"+// 用户图像sd卡保存位置
			"soso_logindate text,"+// 登录时间
			"soso_lastlogindate text,"+//上一次的登录时间
			"soso_sex integer,"+// 性别  1男2女
			"soso_roleid text,"+// 角色id
			"soso_rolemc text,"+// 角色名称
			"soso_region text,"+// 区域
			"soso_birthday text,"+// 生日
			"soso_email text,"+// 邮箱地址
			"soso_telephone text,"+// 电话
			"soso_bigintegral text,"+// 积分
			"soso_credibility text,"+// 用户信用度
			"soso_customerstate integer,"+// 用户状态 0正常，1黑名单
			"soso_ownerphone text,"+//业主电话
			"soso_legalperson text,"+//公司名称
			"soso_owneraddress text,"+//业主地址
			"soso_businesslicenseurl text,"+//业主营业执照url
			"soso_businesslicensesd text,"+//业主营业执照sd卡位置
			"loginlast integer,"+// 1代表最后登录
		    "autologin integer" +	//自动登录0否，1自动登录
			")";
	
	private String UserID;
	private String UserName = "";
	private String PassWord;
	private String RealName;
	private String RegisterDate;
	private String HeadImage;
	private String LoginDate;
	private int Sex;
	private int RoleID;
	private String RoleMC;
	private String Region;
	private String Birthday;
	private String Email;
	private String Telephone;
	private String Bigintegral;
	private String Credibility;
	private int CustomerState;
	private String ZJMC;
	private String ZJAddress;
	private String JobsImage;
	private String ZJTele;
	private String LastLoginDate;
	
	public SoSoUserInfo(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = StringUtils.formatCalendar1(sdf.format(new Date()),MyApplication.error_value);
		setUserID("");
		setUserName("");
		setPassWord("");
		setRealName("");
		setRegisterDate("");
		setHeadImage("");
		setLoginDate(time);
		setSex(0);
		setRoleID(0);
		setRoleMC("");
		setRegion("");
		setBirthday("");
		setEmail("");
		setTelephone("");
		setBigintegral("");
		setCredibility("");
		setCustomerState(0);
		setZJMC("");
		setZJAddress("");
		setJobsImage("");
		setZJTele("");
		setLastLoginDate(time);
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassWord() {
		return PassWord;
	}

	public void setPassWord(String passWord) {
		PassWord = passWord;
	}

	public String getRealName() {
		return RealName;
	}

	public void setRealName(String realName) {
		RealName = realName;
	}

	public String getRegisterDate() {
		return RegisterDate;
	}

	public void setRegisterDate(String registerDate) {
		RegisterDate = registerDate;
	}

	public String getHeadImage() {
		return HeadImage;
	}

	public void setHeadImage(String headImage) {
		HeadImage = headImage;
	}

	public String getLoginDate() {
		return LoginDate;
	}

	public void setLoginDate(String loginDate) {
		LoginDate = loginDate;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public String getRoleMC() {
		return RoleMC;
	}

	public void setRoleMC(String roleMC) {
		RoleMC = roleMC;
	}

	public String getRegion() {
		return Region;
	}

	public void setRegion(String region) {
		Region = region;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	public String getBigintegral() {
		return Bigintegral;
	}

	public void setBigintegral(String bigintegral) {
		Bigintegral = bigintegral;
	}

	public String getCredibility() {
		return Credibility;
	}

	public void setCredibility(String credibility) {
		Credibility = credibility;
	}

	public int getCustomerState() {
		return CustomerState;
	}

	public void setCustomerState(int customerState) {
		CustomerState = customerState;
	}

	public String getZJMC() {
		return ZJMC;
	}

	public void setZJMC(String zJMC) {
		ZJMC = zJMC;
	}

	public String getZJAddress() {
		return ZJAddress;
	}

	public void setZJAddress(String zJAddress) {
		ZJAddress = zJAddress;
	}

	public String getJobsImage() {
		return JobsImage;
	}

	public void setJobsImage(String jobsImage) {
		JobsImage = jobsImage;
	}

	public String getZJTele() {
		return ZJTele;
	}

	public void setZJTele(String zJTele) {
		ZJTele = zJTele;
	}

	public int getRoleID() {
		return RoleID;
	}

	public void setRoleID(int roleID) {
		RoleID = roleID;
	}

	public String getLastLoginDate() {
		return LastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		LastLoginDate = lastLoginDate;
	}

	/**
	 * 
	 * @param 上下文引用
	 * @param userid 用戶id
	 */
	public void updateUserInfo(Context context,String userid){

		List<Map<String, Object>> selectresult = DBHelper
				.getInstance(context)
				.selectRow(
						"select * from soso_userinfo where soso_userid= '"+userid+"'",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1)
							.get("soso_username") != null
					&& selectresult.get(selectresult.size() - 1)
					.get("soso_userid") != null
					&& selectresult.get(selectresult.size() - 1)
							.get("soso_password") != null) {
				SoSoUserInfo sosouserinfo = new SoSoUserInfo();
				try {
					sosouserinfo.setRealName(selectresult.get(selectresult.size() - 1)
							.get("soso_realname").toString());
				} catch (Exception e) {
					sosouserinfo.setRealName("");
				}
				try {
					sosouserinfo.setRegisterDate(selectresult.get(selectresult.size() - 1)
							.get("soso_registerdate").toString());
				} catch (Exception e) {
					sosouserinfo.setRegisterDate("");
				}
				try {
					sosouserinfo.setHeadImage(selectresult.get(selectresult.size() - 1)
							.get("soso_userimageurl").toString());
				} catch (Exception e) {
					sosouserinfo.setHeadImage("");
				}
				try {
					sosouserinfo.setLoginDate(selectresult.get(selectresult.size() - 1)
							.get("soso_logindate").toString());
				} catch (Exception e) {
					sosouserinfo.setLoginDate("");
				}
				try {
					sosouserinfo.setSex(Integer.parseInt(selectresult.get(selectresult.size() - 1)
							.get("soso_sex").toString()));
				} catch (Exception e) {
					sosouserinfo.setSex(0);
				}
				try {
					sosouserinfo.setRoleID(Integer.parseInt(selectresult.get(selectresult.size() - 1)
							.get("soso_roleid").toString()));
				} catch (Exception e) {
					sosouserinfo.setRoleID(0);
				}
				try {
					sosouserinfo.setRoleMC(selectresult.get(selectresult.size() - 1)
							.get("soso_rolemc").toString());
				} catch (Exception e) {
					sosouserinfo.setRoleMC("");
				}
				try {
					sosouserinfo.setRegion(selectresult.get(selectresult.size() - 1)
							.get("soso_region").toString());
				} catch (Exception e) {
					sosouserinfo.setRegion("");
				}
				try {
					sosouserinfo.setBirthday(selectresult.get(selectresult.size() - 1)
							.get("soso_birthday").toString());
				} catch (Exception e) {
					sosouserinfo.setBirthday("");
				}
				try {
					sosouserinfo.setEmail(selectresult.get(selectresult.size() - 1)
							.get("soso_email").toString());
				} catch (Exception e) {
					sosouserinfo.setEmail("");
				}
				try {
					sosouserinfo.setTelephone(selectresult.get(selectresult.size() - 1)
							.get("soso_telephone").toString());
				} catch (Exception e) {
					sosouserinfo.setTelephone("");
				}
				try {
					sosouserinfo.setBigintegral(selectresult.get(selectresult.size() - 1)
							.get("soso_bigintegral").toString());
				} catch (Exception e) {
					sosouserinfo.setBigintegral("");
				}
				try {
					sosouserinfo.setCredibility(selectresult.get(selectresult.size() - 1)
							.get("soso_credibility").toString());
				} catch (Exception e) {
					sosouserinfo.setCredibility("");
				}
				try {
					sosouserinfo.setCustomerState(Integer.parseInt(selectresult.get(selectresult.size() - 1)
							.get("soso_customerstate").toString()));
				} catch (Exception e) {
					sosouserinfo.setCustomerState(0);
				}
				sosouserinfo.setUserID(selectresult.get(selectresult.size() - 1)
						.get("soso_userid").toString());
				sosouserinfo.setUserName(selectresult.get(selectresult.size() - 1)
						.get("soso_username").toString());
				sosouserinfo.setPassWord(selectresult.get(selectresult.size() - 1)
						.get("soso_password").toString());
				MyApplication.getInstance(context).setSosouserinfo(sosouserinfo);
				MyApplication.getInstance(context).setRoleid(sosouserinfo.getRoleID());
			}
			if(selectresult!=null){
				selectresult.clear();
				selectresult = null;
			}
			DBHelper.getInstance(context).close();
		}

	
	}
	
	
	
}
