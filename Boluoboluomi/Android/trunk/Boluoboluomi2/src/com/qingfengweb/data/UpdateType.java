/**
 * 
 */
package com.qingfengweb.data;

/**
 * @author 刘星星
 * @createDate 2013、9、12
 *
 */
public enum UpdateType {

	/// <summary>
    /// 门店信息
    /// </summary>
    Store("1"),
    /// <summary>
    /// 我的相册
    /// </summary>
    UserAlbum("2"),
    /// <summary>
    /// 美图欣赏
    /// </summary>
    StoreAlbum("3"),
    /// <summary>
    /// 积分商城
    /// </summary>
    Shop("4"),
    /// <summary>
    /// 我要定制
    /// </summary>
    UserCustom("5"),
    /// <summary>
    /// 推荐套系
    /// </summary>
    RecommendProduct("6"),
    /// <summary>
    /// 活动分享
    /// </summary>
    Activity("7"),
    /// <summary>
    /// 百宝箱
    /// </summary>
    Partner("8"),
    /// <summary>
    /// 点评系统
    /// </summary>
    StaffEvaluation("9"),
    /// <summary>
    /// 帮助说明
    /// </summary>
    Help ("10"),
    /// <summary>
    /// 电子请帖
    /// </summary>
    Invitation("11"),
    /// <summary>
    /// 广告
    /// </summary>
    Advertisement("12"),
    /// <summary>
    /// 服务器时间
    /// </summary>
    ServerTime("13"),
    /// <summary>
    /// 无类型
    /// </summary>
    None("99");


	private final String value;

	/**
	 * 构造函数
	 * 
	 * @param value 值
	 */
	UpdateType(String value) {
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


