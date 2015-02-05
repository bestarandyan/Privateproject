/**
 * 
 */
package com.qingfengweb.data;
import com.qingfengweb.id.blm_goldenLadies.R;
/**
 *
 */
public class ConstantsValues {
	public static final int[] ANIMRESIN = { R.anim.in_left_right
		,R.anim.in_right_left
		,R.anim.push_left_in
		,R.anim.push_down_in
		,R.anim.in_anim
		,R.anim.push_right_in};
	
	public static final int[] ANIMRESOUT = { R.anim.out_left_right
		,R.anim.out_right_left
		,R.anim.push_left_out
		,R.anim.push_down_out
		,R.anim.out_anim
		,R.anim.push_right_out};
	
	public static final int[] TreasureImg = {R.drawable.id_baoxiang_ico1,R.drawable.id_baoxiang_ico2,R.drawable.id_baoxiang_ico3,R.drawable.id_baoxiang_ico4,
			R.drawable.id_baoxiang_ico5,R.drawable.id_baoxiang_ico6,R.drawable.id_baoxiang_ico7,R.drawable.id_baoxiang_ico8,
			R.drawable.id_baoxiang_ico9,R.drawable.id_baoxiang_ico10};//百宝箱首页类型图片
	
	public static final String[] weeks = {
		"一","二","三","四","五","六","日"
	};
	public static final String cityDBName = "region_no_country_district.sqlite";//城市数据库名
	public static final String SAVE_PATH_IN_SDCARD = "/boluomiversion2/"; //图片及其他数据保存文件夹   
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//路径   
	public static final String HOME_IMG_URL = SAVE_PATH_IN_SDCARD+"homeimg/";//首页图片存储路径
	public static final String MYALBUM_IMG_URL = SAVE_PATH_IN_SDCARD+"myalbum/";//我的相册图片存储路劲
	public static final String TREASURE_IMG_URL = SAVE_PATH_IN_SDCARD+"treasure/";//百宝箱图片存储路径
	public static final String TREASURE_MERCHAN_IMG_URL = TREASURE_IMG_URL+"merchan/";//商家列表图片存储路径
	public static final String TREASURE_MERCHAN_PRODUCT_IMG_URL = TREASURE_MERCHAN_IMG_URL+"product/";//商家列表图片存储路径
	public static final String TREASURE_MERCHAN_COUPON_IMG_URL = TREASURE_MERCHAN_IMG_URL+"coupon/";//商家列表优惠券图片存储路径
	public static final String STORE_HOME_IMG_URL = SAVE_PATH_IN_SDCARD+"storeHome/";//店家首页图片存储路劲
	public static final String MyALBUM_IMG_URL_THUMB =MYALBUM_IMG_URL + "thumb/";//我的相册缩略图
	
	public static final String ACTIVE_IMG_URL = SAVE_PATH_IN_SDCARD+"activeimages/";//活动分享图片存储路劲
	
	//以下为美图欣赏图片的存储路径
	public static final String BEAUTYPHOTO_IMG_URL = SAVE_PATH_IN_SDCARD+"beautyPhoto/";//美图欣赏图片存储路劲
	public static final String BEAUTYPHOTO_THEMES_IMG_URL = BEAUTYPHOTO_IMG_URL+"beautyPhotoThemes/";//相册列表缩略图
	public static final String BEAUTYPHOTOS_IMG_URL_THUMB = BEAUTYPHOTO_IMG_URL+"beautyPhotos/thumbs/";//美图缩略图存储路径
	public static final String BEAUTYPHOTOS_IMG_URL = BEAUTYPHOTO_IMG_URL+"beautyPhotos/";//美图缩略图存储路径
	
	//以下为推荐套系的图片存储路劲
	public static final String RECOMMENDSERIES_IMG_URL = SAVE_PATH_IN_SDCARD+"recommendSeries/";
	public static final String RECOMMENDSERIES_IMG_URL_THUMB = SAVE_PATH_IN_SDCARD+"recommendSeries/thumbs/";
	
	//以下为我要点评的图片存储路劲
	public static final String VALUATION_IMG_URL = SAVE_PATH_IN_SDCARD+"valuationPersons/";
	public static final String VALUATION_IMG_URL_THUMB = SAVE_PATH_IN_SDCARD+"valuationPersons/thumbs/";
	
	//以下为积分商城中商品的图片存储路劲
	public static final String INTEGRAL_IMG_URL = SAVE_PATH_IN_SDCARD+"integral/";//积分商城图片存储路劲目录
	public static final String INTEGRAL_IMG_URL_THUMB = INTEGRAL_IMG_URL+"/thumbs/";//美图欣赏图片存储路劲
	
	//以下为我要定制中的图片存储路劲
	public static final String CUSTOM_IMG_URL = SAVE_PATH_IN_SDCARD+"custom/";//积分商城图片存储路劲目录
	public static final String CUSTOM_IMG_URL_TYPE = CUSTOM_IMG_URL+"/type/";//美图欣赏图片存储路劲
	
	//一下为电子请帖保存路径
	public static final String INVITATION_IMG_URL = SAVE_PATH_IN_SDCARD+"invitations/";//电子请帖本地路径
	public static final String INVITATION_IMG_URL_THUMB = INVITATION_IMG_URL+"thumbs/";//电子请帖缩略图本地路径
	
}


