package com.zhihuigu.sosoOffice.constant;

/**
 * 客户类型
 * @author Ring
 *
 */
public enum ErrorType {
	
	/**
	 * 成功
	 */
	Success ("0"),
	/**
	 * 连接超时
	 */
	SoSoTimeOut("-5211314"),
	/**
	 * 无数据
	 */
	SoSoNoData("-1000"),
	
	/**
	 * 已添加过收藏
	 */
	CollectAgain("-23"),
	
	
	/**
	 * 已添加过该联系人
	 */
	AddLinkAgain("-258"),
	
	/**
	 * 找不到网址
	 */
	NotFound("-404"),
	/**
	 * 用户名不存在
	 */
	UserNameNotFound("-151"),
	/**
	 * 用户名已存在
	 */
	UserNameAgain("-2222"),
	/**
	 * 电话号码已存在
	 */
	PhoneAgain("-4444"),
	/**
	 * email已存在
	 */
	EmailAgain("-3333"),
	/**
	 * 密码错误
	 */
	PassWordError("-153");
	
	
	private final String value;
	
	/**
     * 构造函数
     * @param value 值
     */
	ErrorType(String value) {
        this.value = value;
    }
	/**
	 * 获取值
	 * @return 返回值
	 */
	public String getValue() {
        return value;
    }
	
	/**
	 * 查找指定值的枚举成员
	 * @param value 指定的值
	 * @return
	 */
	public static ErrorType ValueOf(String value)
	{
		for(ErrorType accidentType:ErrorType.values())
		{
			if(accidentType.getValue().equals(value))
				return accidentType;
		}
		return ErrorType.Success;
	}
}
