/**
 * 
 */
package com.boluomi.children.data;

/**
 * @author 刘星星
 * @createDate 2013/9/12
 * 图片下载类型枚举   
 * type 1 大图
 * type 2 小图
 *
 */
public enum ImgDownType {
	//大图  原图
	BigBitmap("1"),
	//小图  缩略图
	ThumbBitmap("2");
	public final String value;
	public String getValue(){
		return value;
	}
	ImgDownType(String value){
		this.value = value;
	}
}
