package com.qingfengweb.piaoguanjia.orderSystem.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "productinfo")
// 建议加上注解， 混淆后表名不受影响
public class ProductInfo extends EntityBase {
	@NoAutoIncrement
	@Id
	@Column(column = "productid")
	public String productid;
	@Column(column = "productname")
	public String productName;
	@Column(column = "labela")
	public String labelA;
	@Column(column = "istoday")
	public String isToday;
	@Column(column = "address")
	public String address;
	
	@Column(column = "regionname")
	public String regionName;
	@Column(column = "days")
	public String days;
	@Column(column = "producttype")
	public String productType;
	@Column(column = "pricemarket")
	public String priceMarket;
	@Column(column = "pricevip")
	public String priceVip;
	@Column(column = "status")
	public String status;
	@Column(column = "rankingnewa")
	public String rankingNewA;
	
	@Column(column = "logo")
	public String logo;
	@Column(column = "cityid")
	public String cityid;
	@Column(column = "isdistributorlimit")
	public String isDistributorLimit;
	
	
	
	

}
