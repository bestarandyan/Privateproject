/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author Ring
 *ְλ��Ϣ��
 */
public class PositionInfo {
	/**author by Ring
	 * ְλ��Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"positioninfo" +//���� 
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"positionid text,"+//ְλid
			"name text,"+//ְλ����
			"tag integer default 0,"+//Ĭ�ϴ��ڵ�����Ϊ1
			"workadress text,"+//�����ص�
			"createtime text,"+//����ʱ��
			"describe text"+//ְλ����
			")";
	
}
