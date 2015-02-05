package com.zhihuigu.sosoOffice.constant;

import com.zhihuigu.sosoOffice.R;

/**
 * 此类为常量类，包括访问后台的接口地址，以及一些不变的常量定义。
 * 
 * @author 刘星星 武国庆
 * 
 */
public class Constant {
	public static final int TYPE_CLIENT = 4;// 客户
	public static final int TYPE_AGENCY = 2;// 中介
	public static final int TYPE_PROPRIETOR = 1;// 业主
	//以下为业主所使用的底部图标
	public static final int[] proprietor = {R.drawable.soso_bottom_ico1,R.drawable.soso_bottom_ico2,R.drawable.soso_bottom_ico3,
    		R.drawable.soso_bottom_ico5,R.drawable.soso_bottom_ico7};
	public static final int[] proprietor_on = {R.drawable.soso_bottom_ico1_on,R.drawable.soso_bottom_ico2_on,R.drawable.soso_bottom_ico3_on,
    		R.drawable.soso_bottom_ico5_on,R.drawable.soso_bottom_ico7_on};
	//以下为客户所使用的底部图标
	public static final int[] client = {R.drawable.soso_bottom_ico1,R.drawable.soso_bottom_ico2,R.drawable.soso_bottom_ico4,
		R.drawable.soso_bottom_ico6,R.drawable.soso_bottom_ico7};
	public static final int[] client_on = {R.drawable.soso_bottom_ico1_on,R.drawable.soso_bottom_ico2_on,R.drawable.soso_bottom_ico4_on,
			R.drawable.soso_bottom_ico6_on,R.drawable.soso_bottom_ico7_on};
	//以下为中介所使用的底部图标
	public static final int[] agency = {R.drawable.soso_bottom_ico1,R.drawable.soso_bottom_ico2,R.drawable.soso_bottom_ico3,
		R.drawable.soso_bottom_ico6,R.drawable.soso_bottom_ico7};
	public static final int[] agency_on = {R.drawable.soso_bottom_ico1_on,R.drawable.soso_bottom_ico2_on,R.drawable.soso_bottom_ico3_on,
		R.drawable.soso_bottom_ico6_on,R.drawable.soso_bottom_ico7_on};
	public static final String[]  py= { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
		"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
		"X", "Y", "Z" };
	public static final int ROOM_IMG_THUMBNAIL_SIZE = 250;//房源图片的缩略图的大小      一个正方形
	/**
	 * 房源照片的最大照片数量，这里实际的最大数是10张，因为用来获取图片的图片也占用了一个值
	 */
	public static final int GALLERY_IMAGE_MAX_IMAGE_NUMBER = 11;
	public static final float DEMAND_PRICE_MAX_VALUE =  20f;//发布需求中的最大房源价格（元\平米\天）
	public static final float DEMAND_ACREAGE_MAX_VALUE = 15000f;//发布需求中的最大房源面积
	
}
