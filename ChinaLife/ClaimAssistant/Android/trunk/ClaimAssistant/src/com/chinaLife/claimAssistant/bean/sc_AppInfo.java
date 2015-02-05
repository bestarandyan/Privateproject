package com.chinaLife.claimAssistant.bean;

public class sc_AppInfo {
	/**
	 * 创建版本信息表
	 */
	public static String TABLE_CREATE = "create table " + "appinfo" + // 表名
			"(" + "_id integer primary key autoincrement," + // 自动增长_id
			"name text default ''," + // 应用程序名称
			"version text default ''," + // 当前版本号
			"url text default ''," + // 版本信息下载地址
			"error_value text default ''," + // 当前服务器时间
			"update_time text default ''," + // 版本最后更新时间
			"lengend_update_time text default ''," + // 图例最后更新时间
			"lengend_update_time1 text default ''," + // 图例上一次更新时间
			"secret_update_time text default ''," + // 密钥的更新时间
			"secret_system text default ''," + // 服务端密钥
			"secret_client text default ''," + // 客户端密钥
			"isupdate integer" + // 是否强制更新版本0非强制，1强制
			")";

	private String name;
	private String url;
	private String servertime;
	private String updatetime;
	private String version;
	private int wetherupdate;

	public int getWetherupdate() {
		return wetherupdate;
	}

	public void setWetherupdate(int wetherupdate) {
		this.wetherupdate = wetherupdate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getServertime() {
		return servertime;
	}

	public void setServertime(String servertime) {
		this.servertime = servertime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
