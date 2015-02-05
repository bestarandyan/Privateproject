package com.zhihuigu.sosoOffice.constant;

/**
 * 客户类型
 * @author Ring
 *
 */
public enum UserType {
	
	/**
	 * 注册后未选择用户类型时,无用户类型
	 */
	UserTypeNone ((short)0),
	/**
	 * 业主类型用户
	 */
	UserTypeOwner((short)1),
	/**
	 * 中介类型用户
	 */
	UserTypeIntermediary((short)2),
	/**
	 * 客户类型用户
	 */
	UserTypeCustomer((short)4);
	
	
	
	private final short value;
	
	/**
     * 构造函数
     * @param value 值
     */
	UserType(short value) {
        this.value = value;
    }
	/**
	 * 获取值
	 * @return 返回值
	 */
	public short getValue() {
        return value;
    }
	
	/**
	 * 查找指定值的枚举成员
	 * @param value 指定的值
	 * @return
	 */
	public static UserType valueOf(short value)
	{
		for(UserType accidentType:UserType.values())
		{
			if(accidentType.getValue()== value)
				return accidentType;
		}
		return UserType.UserTypeNone;
	}
}
