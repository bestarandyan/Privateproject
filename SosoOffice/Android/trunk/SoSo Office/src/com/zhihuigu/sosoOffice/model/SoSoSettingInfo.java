package com.zhihuigu.sosoOffice.model;

public class SoSoSettingInfo {
	/***
	 * author By Ring
	 */
	// SQL Command for creating the table
	public static String TABLE_CREATE = "create table sososettinginfo("
			+ "_id integer primary key autoincrement," + 
			"userid text,"+//�û�id
			"username text,"+//�û���
			"isshowimage integer," + // �Ƿ���ʾͼƬ��0��ʾ��1��ʾ,2����ʾ
			"ismapdisplay integer," + // �Ƿ���ʾ�б��ǵ�ͼ  0����Ӧ����ʾ��ͼ  1����Ӧ����ʾ�б�
			"room_state_for_examine integer," + //1�������ͨ���ķ�Դ  2�������˷�Դ
			"roomManagerForm integer," + //0:���ͨ���ķ�Դ��ͼ���б�    1�����б��״̬
			"isnotice integer," + // �Ƿ���Ϣ֪ͨ
			"noticestarttime text," + // ��Ϣ֪ͨ��ʼʱ��
			"cityid text,"+//����id
			"roleid text,"+//��ɫ
			"noticeendtime text" + // ��Ϣ֪ͨ����ʱ��
			")";
}
