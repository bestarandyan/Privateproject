/**
 * 
 */
package com.qingfengweb.client.bean;


/**
 * @author Ring
 * Ա����Ϣģ��
 *
 */
public class StaffInfo {
	/**author by Ring
	 * Ա����Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"staffinfo" +//���� 
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"staffid text,"+//Ա��id
			"name text,"+//Ա������
			"birthday text,"+//Ա������
			"sex integer,"+//Ա���Ա�1,�У�2Ů
			"positionid text,"+//ְλid
			"imageid text,"+//����ͷ��
			"tag integer default 0,"+//Ĭ�ϴ��ڵ�����Ϊ1
			"declaration text"+//��������
			")";
}
