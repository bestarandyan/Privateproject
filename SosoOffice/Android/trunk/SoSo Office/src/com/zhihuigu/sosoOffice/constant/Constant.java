package com.zhihuigu.sosoOffice.constant;

import com.zhihuigu.sosoOffice.R;

/**
 * ����Ϊ�����࣬�������ʺ�̨�Ľӿڵ�ַ���Լ�һЩ����ĳ������塣
 * 
 * @author ������ �����
 * 
 */
public class Constant {
	public static final int TYPE_CLIENT = 4;// �ͻ�
	public static final int TYPE_AGENCY = 2;// �н�
	public static final int TYPE_PROPRIETOR = 1;// ҵ��
	//����Ϊҵ����ʹ�õĵײ�ͼ��
	public static final int[] proprietor = {R.drawable.soso_bottom_ico1,R.drawable.soso_bottom_ico2,R.drawable.soso_bottom_ico3,
    		R.drawable.soso_bottom_ico5,R.drawable.soso_bottom_ico7};
	public static final int[] proprietor_on = {R.drawable.soso_bottom_ico1_on,R.drawable.soso_bottom_ico2_on,R.drawable.soso_bottom_ico3_on,
    		R.drawable.soso_bottom_ico5_on,R.drawable.soso_bottom_ico7_on};
	//����Ϊ�ͻ���ʹ�õĵײ�ͼ��
	public static final int[] client = {R.drawable.soso_bottom_ico1,R.drawable.soso_bottom_ico2,R.drawable.soso_bottom_ico4,
		R.drawable.soso_bottom_ico6,R.drawable.soso_bottom_ico7};
	public static final int[] client_on = {R.drawable.soso_bottom_ico1_on,R.drawable.soso_bottom_ico2_on,R.drawable.soso_bottom_ico4_on,
			R.drawable.soso_bottom_ico6_on,R.drawable.soso_bottom_ico7_on};
	//����Ϊ�н���ʹ�õĵײ�ͼ��
	public static final int[] agency = {R.drawable.soso_bottom_ico1,R.drawable.soso_bottom_ico2,R.drawable.soso_bottom_ico3,
		R.drawable.soso_bottom_ico6,R.drawable.soso_bottom_ico7};
	public static final int[] agency_on = {R.drawable.soso_bottom_ico1_on,R.drawable.soso_bottom_ico2_on,R.drawable.soso_bottom_ico3_on,
		R.drawable.soso_bottom_ico6_on,R.drawable.soso_bottom_ico7_on};
	public static final String[]  py= { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
		"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
		"X", "Y", "Z" };
	public static final int ROOM_IMG_THUMBNAIL_SIZE = 250;//��ԴͼƬ������ͼ�Ĵ�С      һ��������
	/**
	 * ��Դ��Ƭ�������Ƭ����������ʵ�ʵ��������10�ţ���Ϊ������ȡͼƬ��ͼƬҲռ����һ��ֵ
	 */
	public static final int GALLERY_IMAGE_MAX_IMAGE_NUMBER = 11;
	public static final float DEMAND_PRICE_MAX_VALUE =  20f;//���������е����Դ�۸�Ԫ\ƽ��\�죩
	public static final float DEMAND_ACREAGE_MAX_VALUE = 15000f;//���������е����Դ���
	
}
