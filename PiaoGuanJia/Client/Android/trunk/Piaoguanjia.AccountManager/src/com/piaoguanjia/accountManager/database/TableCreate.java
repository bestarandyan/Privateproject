package com.piaoguanjia.accountManager.database;

import com.sqlcrypt.database.sqlite.SQLiteDatabase;

public class TableCreate {
	public static final String TABLENAME_USERINFO = "userinfo";// 用户表
	public static final String TABLENAME_CHARGEINFO = "chargeinfo";// 充值表
	public static final String TABLENAME_ACCOUNTINFO = "accountinfo";// 专用账户表
	public static final String TABLENAME_CREDITINFO = "creditinfo";// 额度表
	public static final String TABLENAME_APPUSERINFO = "appuserinfo";// 用户配置表
	public static final String TABLENAME_SYSTEMINFO = "systeminfo";// 系统配置表

	/**
	 * 用户表
	 */
	public static String TABLECREATE_USERINFO = "create table "
			+ TABLENAME_USERINFO + //
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"userid text," + // 用户编号
			"username text," + // 用户名
			"password text," + // 用户密码
			"permissions integer," + // 用户权限
			"iskeep integer" + // 是否保存用户名，密码 1 保存，2不保存
			")";

	/**
	 * 充值表
	 */
	public static String TABLECREATE_CHARGEINFO = "create table "
			+ TABLENAME_CHARGEINFO + //
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"userid text," + // 用户编号
			"chargid text," + // 充值编号
			"source text	," + // 来源
			"username text," + // 充值用户名
			"type text," + // 充值方式 字符串，直接显示
			"accounttype text," + // 账户类型 1总账户，2专用账户
			"audittime text," + // 充值时间 仅当status=1时，才不为空
			"productname text," + // 产品名称 仅当accounttype=2时不为空
			"productid text," + // 产品编号 仅当accounttype=2时不为空
			"status integer," + // 当前状态，0未审核，1审核通过，2审核不通过，3已取消（只有status=0才可以审核）
			"amount text," + // 充值金额
			"totalamount text," + // 累计充值金额
			"balance text," + // 账户余额
			"balancesource text," + // 资金来源 字符串，1-支付宝，2-银行账户，3-现金
			"isreceipt integer," + // 是否可以申请发票 1是，0不是
			"remark text," + // 备注
			"warrantor text," + // 授权账户
			"createtime text," + // 创建时间
			"reason text," + // 添加理由
			"listtype integer," + // 列表类型	1待审核，2历史记录，3充值记录
			"iscertificate integer" + // 是否有凭证 1有，0没有
			")";

	/**
	 * 专用账户表
	 */
	public static String TABLECREATE_ACCOUNTINFO = "create table "
			+ TABLENAME_ACCOUNTINFO + //
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"userid text," + // 用户编号
			"accountid text," + // 专用账户编号
			"username text," + // 分销商用户名
			"productname text," + // 产品名称
			"productid text," + // 产品id
			"chargelimit text," + // 充值限制
			"remindamount text," + // 余额提醒
			"remindtype text," + // 提醒方式 1短信，2通知
			"onlinechargelimit text," + // 直冲额度
			"audittime text," + // 审核时间 仅当(status&2)==2时，不为空
			"createtime text," + // 创建时间
			"reason text," + // 添加理由
			"name text," + // 分销商姓名
			"status text" + // 状态 1未审核，2-审核通过，4审核失败（只有(status&1)==1时，才能审核）
			")";

	/**
	 * 额度表
	 */
	public static String TABLECREATE_CREDITINFO = "create table "
			+ TABLENAME_CREDITINFO + //
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"userid text," + // 用户编号
			"creditid text," + // 额度编号
			"username text," + // 分销商用户名
			"name text," + // 分销商姓名
			"accounttype text," + // 账户类型 1总账户，2专用账户
			"createtime text," + // 创建时间
			"productname text," + // 产品名称 仅当accounttype=2时不为空
			"audittime text," + // 审核时间 仅当 (status&1) != 1时，才不为空
			"status text," + // 状态 当前状态：1未审核，2审核通过，4审核失败（只有未审核才可以审核）
			"productid text," + // 产品编号 仅当accounttype=2时不为空
			"creditlimit text," + // 额度
			"reason text" + // 添加理由
			")";
	
	/**
	 * 个人配置表
	 */
	public static String TABLECREATE_APPUSERINFO = "create table "
			+ TABLENAME_APPUSERINFO + //
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"userid text," + // 用户编号
			"chargenum integer," + // 充值待审核数量
			"accountnum integer," + //专用账户待审核数量 
			"creditnum integer" + // 额度待审核数量
			")";
	
	/**
	 * 系统配置表
	 */
	public static String TABLECREATE_SYSTEMINFO = "create table "
			+ TABLENAME_SYSTEMINFO + //
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"systemtime text," + // 系统时间
			"firstopenapp integer" + // 是否是第一次打开软件
			")";

	/**
	 * 
	 * @param db
	 */
	public static void makeTable(SQLiteDatabase db) {
		db.execSQL(TABLECREATE_USERINFO);
		db.execSQL(TABLECREATE_ACCOUNTINFO);
		db.execSQL(TABLECREATE_CHARGEINFO);
		db.execSQL(TABLECREATE_CREDITINFO);
		db.execSQL(TABLECREATE_SYSTEMINFO);
		db.execSQL(TABLECREATE_APPUSERINFO);
	}

}
