/**
 * 
 */
package com.qingfengweb.client.bean;

/**
 * @author Ring
 *ͼƬ��Ϣ
 */
public class ImageInfo {
	/**author by Ring
	 * ͼƬ��Ϣ��
	 */
	public static String TABLE_CREATE = "create table " +
			"imageinfo" +//���� 
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"imageid text,"+//����id
			"xid text,"+//ͼƬ���id
			"tag integer default 0,"+//Ĭ�ϴ��ڵ�����Ϊ1
			"sd text,"+//��ͼλ��
			"sd_thumb text,"+//����ͼλ��
			"createtime text,"+//����ʱ��
			"describe text"+//ͼƬ����
			")";
	
}
