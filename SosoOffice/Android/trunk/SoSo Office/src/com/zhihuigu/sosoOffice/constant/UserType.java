package com.zhihuigu.sosoOffice.constant;

/**
 * �ͻ�����
 * @author Ring
 *
 */
public enum UserType {
	
	/**
	 * ע���δѡ���û�����ʱ,���û�����
	 */
	UserTypeNone ((short)0),
	/**
	 * ҵ�������û�
	 */
	UserTypeOwner((short)1),
	/**
	 * �н������û�
	 */
	UserTypeIntermediary((short)2),
	/**
	 * �ͻ������û�
	 */
	UserTypeCustomer((short)4);
	
	
	
	private final short value;
	
	/**
     * ���캯��
     * @param value ֵ
     */
	UserType(short value) {
        this.value = value;
    }
	/**
	 * ��ȡֵ
	 * @return ����ֵ
	 */
	public short getValue() {
        return value;
    }
	
	/**
	 * ����ָ��ֵ��ö�ٳ�Ա
	 * @param value ָ����ֵ
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
