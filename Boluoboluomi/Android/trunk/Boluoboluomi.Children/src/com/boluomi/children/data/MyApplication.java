/**
 * 
 */
package com.boluomi.children.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Matrix;
import android.os.Environment;

import com.boluomi.children.model.UserInfo;


/**
 * @author 刘星星
 *
 */
public class MyApplication {
	public static MyApplication myApplication = null;
	public static String APPKEY = "";
	public static String APPID = "";
	public static String URL = "";
	public MyApplication() {
	}
	public static MyApplication getInstant(){
		if(myApplication == null){
			myApplication = new MyApplication();
		}
		return myApplication;
	}
	public static String PHOTOPATH = Environment.getExternalStorageDirectory()+ "/boluomi_children/myalbum";
	private int heightPixels = 0;//屏幕分辨率高度
	private int widthPixels = 0;//屏幕分辨率宽度
	private String albumid = "";//相册id为空时表示我的相册，不为空时为美图欣赏相册
	private String partnerid="";//合作伙伴id
//	private int position = 0;//点评类型：1摄影师，2化妆师
	private int type=0;//场景类型1：场景2：道具3服装
	private String goodid = "";//产品id，兑换时用到
	
	private UserInfo userinfo = null;
	public boolean isSelectCityFromMainActivity = false;
	public Matrix matrix = null;//照片的位置
	public String templateid = "";//电子请帖模板编号
	public String currentGrowupImgPath = "";//当前成长备忘录的图片路径
	
	
	public String getCurrentGrowupImgPath() {
		return currentGrowupImgPath;
	}
	public void setCurrentGrowupImgPath(String currentGrowupImgPath) {
		this.currentGrowupImgPath = currentGrowupImgPath;
	}
	public String getTemplateid() {
		return templateid;
	}
	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}
	public Matrix getMatrix() {
		return matrix;
	}
	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
	public boolean isSelectCityFromMainActivity() {
		return isSelectCityFromMainActivity;
	}
	public void setSelectCityFromMainActivity(boolean isSelectCityFromMainActivity) {
		this.isSelectCityFromMainActivity = isSelectCityFromMainActivity;
	}
	public int getHeightPixels() {
		return heightPixels;
	}
	public void setHeightPixels(int heightPixels) {
		this.heightPixels = heightPixels;
	}
	public int getWidthPixels() {
		return widthPixels;
	}
	public void setWidthPixels(int widthPixels) {
		this.widthPixels = widthPixels;
	}
	public String getAlbumid() {
		return albumid;
	}
	public void setAlbumid(String albumid) {
		this.albumid = albumid;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
//	public int getPosition() {
//		return position;
//	}
//	public void setPosition(int position) {
//		this.position = position;
//	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getGoodid() {
		return goodid;
	}
	public void setGoodid(String goodid) {
		this.goodid = goodid;
	}
	public UserInfo getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}
	
	public String path = "";
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
			
	
}
