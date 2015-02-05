package com.zhihuigu.sosoOffice.constant;

/**
 * �ͻ�����
 * @author Ring
 *
 */
public enum ErrorType {
	
	/**
	 * �ɹ�
	 */
	Success ("0"),
	/**
	 * ���ӳ�ʱ
	 */
	SoSoTimeOut("-5211314"),
	/**
	 * ������
	 */
	SoSoNoData("-1000"),
	
	/**
	 * ����ӹ��ղ�
	 */
	CollectAgain("-23"),
	
	
	/**
	 * ����ӹ�����ϵ��
	 */
	AddLinkAgain("-258"),
	
	/**
	 * �Ҳ�����ַ
	 */
	NotFound("-404"),
	/**
	 * �û���������
	 */
	UserNameNotFound("-151"),
	/**
	 * �û����Ѵ���
	 */
	UserNameAgain("-2222"),
	/**
	 * �绰�����Ѵ���
	 */
	PhoneAgain("-4444"),
	/**
	 * email�Ѵ���
	 */
	EmailAgain("-3333"),
	/**
	 * �������
	 */
	PassWordError("-153");
	
	
	private final String value;
	
	/**
     * ���캯��
     * @param value ֵ
     */
	ErrorType(String value) {
        this.value = value;
    }
	/**
	 * ��ȡֵ
	 * @return ����ֵ
	 */
	public String getValue() {
        return value;
    }
	
	/**
	 * ����ָ��ֵ��ö�ٳ�Ա
	 * @param value ָ����ֵ
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
