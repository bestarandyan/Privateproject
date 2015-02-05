package com.zhihuigu.sosoOffice.constant;

/**
 * 客户类型
 * 
 * @author Ring
 * 
 */
public enum OfficeType {

	/**
	 * 纯写字楼
	 */
	OfficeBuilding((short) 0, "纯写字楼"),
	/**
	 * 商住楼
	 */
	Commerceresidencebuilding((short) 1, "商住楼"),
	/**
	 * 酒店式公寓
	 */
	Apartments((short) 2, "酒店式公寓"),
	/**
	 * 园区
	 */
	park((short) 3, "园区"),
	/**
	 * 商务中心
	 */
	BusinessCenter((short) 4, "商务中心");

	private final short value;
	private final String name;

	/**
	 * 构造函数
	 * 
	 * @param value 值
	 */
	OfficeType(short value,String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 获取值
	 * 
	 * @return 返回值
	 */
	public short getValue() {
		return value;
	}
	/**
	 * 获取值
	 * 
	 * @return 返回值
	 */
	public String getName() {
		return name;
	}

	/**
	 * 查找指定值的枚举成员
	 * 
	 * @param value
	 *            指定的值
	 * @return
	 */
	public static OfficeType valueOf(short value,String name) {
		for (OfficeType accidentType : OfficeType.values()) {
			if (accidentType.getValue() == value
					&&accidentType.getName().equals(name))
				return accidentType;
		}
		return OfficeType.OfficeBuilding;
	}
}
