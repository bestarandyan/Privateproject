/**
 * 
 */
package com.qingfengweb.data;

/**
 * @author 刘星星
 * @createDate 2013/9/12
 * 图片类型枚举
 *
 */
public enum ImageType {
	//软件首页图片类型
	HomeImage("1"),
	//门店首页图片类型
	StoreHomeImage("2"),
	//用户照片图片类型
	UserPhotos("3"),
	//活动分享图片类型
	ActiveImg("4"),
	//百宝箱商家列表图片类型
	MerchanImg("5"),
	//百宝箱商家的产品列表图片类型
	MerchanProductImg("6"),
	//优惠券图片类型
	couponImg("7"),
	//美图相册列表主题缩略图
	beautyPhotoThemes("8"),
	//美图照片图片类型
	beautyPhotos("9"),
	//推荐套系图片类型
	recommendSeries("10"),
	//我要点评图片下载类型
	valuationPersons("11"),
	//积分商城图片类型
	integral("12"),
	//积分活动
	integralActive("13"),
	//我要定制类型
	customType("14"),
	//场景/道具/服装
	customImage("15"),
	//请帖模板
	invitationCard("16");
	private final String value;

	/**
	 * 构造函数
	 * 
	 * @param value 值
	 */
	ImageType(String value) {
		this.value = value;
	}

	/**
	 * 获取值
	 * 
	 * @return 返回值
	 */
	public String getValue() {
		return value;
	}
}
