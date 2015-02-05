package com.zhihuigu.sosoOffice.constant;

/**
 * �ͻ�����
 * 
 * @author Ring
 * 
 */
public enum OfficeType {

	/**
	 * ��д��¥
	 */
	OfficeBuilding((short) 0, "��д��¥"),
	/**
	 * ��ס¥
	 */
	Commerceresidencebuilding((short) 1, "��ס¥"),
	/**
	 * �Ƶ�ʽ��Ԣ
	 */
	Apartments((short) 2, "�Ƶ�ʽ��Ԣ"),
	/**
	 * ԰��
	 */
	park((short) 3, "԰��"),
	/**
	 * ��������
	 */
	BusinessCenter((short) 4, "��������");

	private final short value;
	private final String name;

	/**
	 * ���캯��
	 * 
	 * @param value ֵ
	 */
	OfficeType(short value,String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * ��ȡֵ
	 * 
	 * @return ����ֵ
	 */
	public short getValue() {
		return value;
	}
	/**
	 * ��ȡֵ
	 * 
	 * @return ����ֵ
	 */
	public String getName() {
		return name;
	}

	/**
	 * ����ָ��ֵ��ö�ٳ�Ա
	 * 
	 * @param value
	 *            ָ����ֵ
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
