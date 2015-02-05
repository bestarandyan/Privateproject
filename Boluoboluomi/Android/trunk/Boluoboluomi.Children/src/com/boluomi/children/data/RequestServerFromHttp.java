package com.boluomi.children.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.widget.ImageView;
import com.qingfengweb.imagedownload.CallbackImpl;
import com.qingfengweb.imagedownload.ImageLoadFromUrlOrId;
public class RequestServerFromHttp {
	public static final String SERVER_ADDRESS = "http://192.168.1.101";//内网服务器总接口地址
//	public static final String SERVER_ADDRESS = "http://192.168.1.114:1367";//内网服务器总接口地址
//	public static final String SERVER_ADDRESS = "http://222.73.173.53:99";//外网服务器总接口地址
	public static final String APPID = "100001";//
	public static final String APPKEY = "c81e728d9d4c2f636f067f89cc14862c";//
	public static final String INTERFACE_SYSTEM =SERVER_ADDRESS + "/interfaces/system.aspx";//系统接口
	public static final String INTERFACE_USER =SERVER_ADDRESS + "/interfaces/user.aspx";//用户接口
	public static final String INTERFACE_ACTIVE = SERVER_ADDRESS+"/interfaces/activity.aspx";//活动分享模块接口地址
	public static final String INTERFACE_PARTNER = SERVER_ADDRESS+"/interfaces/partner.aspx";//百宝箱接口地址
	public static final String INTERFACE_INTEGRAL = SERVER_ADDRESS+"/interfaces/shop.aspx";//积分商城接口地址
	public static final String INTERFACE_BEAUTYPHOTO = SERVER_ADDRESS+"/interfaces/photo.aspx";//美图欣赏接口地址
	public static final String INTERFACE_RECOMMEND = SERVER_ADDRESS+"/interfaces/category.aspx";//推荐套系总接口 
	public static final String INTERFACE_VALUATION = SERVER_ADDRESS+"/interfaces/valuation.aspx";//我要点评总接口
	public static final String INTERFACE_SELECTPHOTOS = SERVER_ADDRESS+"/interfaces/order_query.aspx";//查件总接口地址
	public static final String INTERFACE_CUSTOM = SERVER_ADDRESS+"/interfaces/photo_custom.aspx";//我要定制总接口地址
	public static final String INTERFACE_WEXIN = SERVER_ADDRESS+"/interfaces/wechat.aspx";//微信总接口地址
	public static final String INTERFACE_HELP = SERVER_ADDRESS+"/interfaces/help.aspx";//帮助总接口地址
	public static final String INTERFACE_FEEDBACK = SERVER_ADDRESS+"/interfaces/feedback.aspx";//意见反馈总接口地址
	public static final String ACTION_SYSTEMUPDATE = "update";//系统更新动作
	public static final String ACTION_GETSOTRES = "store";//获取门店动作
	public static final String ACTION_GETSOTREDETAIL = "store_detail";//获取门店详情动作
	public static final String ACTION_DOWNLOADIMG = "download_image";//下载图片动作
	public static final String ACTION_GETMSGCODE = "get_verification_code";//下载图片动作
	public static final String ACTION_REGISTER = "register";//注册的动作
	public static final String ACTION_LOGIN = "login";//登录的动作
	public static final String ACTION_DOWNLOAD = "download_image";//下载图片动作
	public static final String ACTION_GETUSERPHOTO = "get_user_photo";//获取用户相册动作
	public static final String ACTION_GETBEAUTYPHOTO_THEMES = "album";//获取美图相册动作
	public static final String ACTION_GETBEAYTYPHOTOS = "photo";//获取美图相册图片
	public static final String ACTION_GETRECOMMEND = "list";//获取推荐套系的动作
	public static final String ACTION_VALUATION = "staff";//我要点评的人的动作
	public static final String ACTION_SUBMITVALUATION = "valuation";//提交点评动作
	public static final String ACTION_TUIJIAN_BEAYTYPHOTOS = "valuation";//获取美图相册图片
	public static final String ACTION_GETACTIVE_LIST = "list";//活动分享列表动作
	public static final String ACTION_GETACTIVE_DETILE = "detail";//活动详情动作
	public static final String ACTION_UPLOAD_IMG = "upload_photo";//上传图片动作
	public static final String ACTION_PARTNER = "type";//百宝箱类型列表动作
	public static final String ACTION_Merchan = "provider";//百宝箱商家列表
	public static final String ACTION_INTREGRAL_TYPE = "get_type";//积分商城列表动作
	public static final String ACTION_INTREGRAL_GOODS = "get_goods";//获取商品动作
	public static final String ACTION_INTREGRAL_EXCHANGE = "order";//兑换接口的动作
	public static final String ACTION_INTREGRAL_RECOMMENDFRIEND = "commend";//推荐好友
	public static final String ACTION_USER_GETMYPOINTS = "get_points";//获取我的积分
	public static final String ACTION_DELETEIMG = "delete_photo";//删除照片接口动作
	public static final String ACTION_SELECTPHOTOS = "query";//查件动作
	public static final String ACTION_CUSTOM_TYPE = "get_photo";//获取我要定制的类型的动作
	public static final String ACTION_SUBMIT_CUSTOM = "custom";//提交定制
	public static final String ACTION_GET_MYCUSTOM = "get_custom";//获取我要定制的类型的动作
	public static final String ACTION_SUBMIT_MYWEIXIN = "submit";//提交微信号的动作
	public static final String ACTION_GET_HELP = "get_help";//获取帮助的动作
	public static final String ACTION_SUBMIT_FEEDBACK = "submit";//提交意见反馈的动作
	
	
	/**
     * 提交意见反馈
     * @return
     */
 	public static String submitFeedback(String type,String points,String comment){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appid", APPID));
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("action", ACTION_SUBMIT_FEEDBACK));
 		params.add(new BasicNameValuePair("username", UserBeanInfo.getInstant().getUserName()));
 		params.add(new BasicNameValuePair("type", type));
 		params.add(new BasicNameValuePair("points", points));
 		params.add(new BasicNameValuePair("comment", comment));
 		msgString = getData(INTERFACE_FEEDBACK, params);
 		return msgString;
 	}
 	
	/**
     * 获取帮助
     * @param storeid 门店id
     * @param type 定制的类型
     * @param datetime 拍摄日期
     * @return
     */
 	public static String getHelpData(String storeid,String updatetime){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appid", APPID));
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("action", ACTION_GET_HELP));
 		params.add(new BasicNameValuePair("storeid", storeid));
 		params.add(new BasicNameValuePair("updatetime", updatetime));
 		msgString = getData(INTERFACE_HELP, params);
 		return msgString;
 	}
	/**
     * 提交微信号
     * @param storeid 门店id
     * @param type 定制的类型
     * @param datetime 拍摄日期
     * @return
     */
 	public static String submitWeiXin(String wechatid){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appid", APPID));
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("action", ACTION_SUBMIT_MYWEIXIN));
 		params.add(new BasicNameValuePair("username", UserBeanInfo.getInstant().getUserName()));
 		params.add(new BasicNameValuePair("password", UserBeanInfo.getInstant().getPassword()));
 		params.add(new BasicNameValuePair("wechatid", wechatid));
 		msgString = getData(INTERFACE_WEXIN, params);
 		return msgString;
 	}
	
	
	
	/**
     * 访问服务器的总函数
     * @author 刘星星
     * @param urlString 接口地址
     * @param list 参数集合
     * @return 服务器返回值
     */
    public static  String getData(String urlString,List<NameValuePair> list){
    	String msgString = "";
    	HttpPost httpPost = new HttpPost(urlString);
    	try {
			HttpEntity httpEntity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
			httpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			//设置超时时间
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			msgString = EntityUtils.toString(httpResponse.getEntity());
			System.out.println(msgString);
			if(msgString.length()>0 && msgString.substring(0, 1).equals("<")){
				msgString = "404";
			}
    	} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			msgString = "404";
			e.printStackTrace();
		}
    	return msgString;
    }
    
    /**
     * 获取我要定制的类型
     * @param storeid 门店id
     * @param type 定制的类型
     * @param datetime 拍摄日期
     * @return
     */
 	public static String getCustomType(String storeid,String type,String updatetime){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appid", APPID));
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("action", ACTION_CUSTOM_TYPE));
 		params.add(new BasicNameValuePair("storeid", storeid));
 		params.add(new BasicNameValuePair("type", type));
 		params.add(new BasicNameValuePair("updatetime", updatetime));
 		msgString = getData(INTERFACE_CUSTOM, params);
 		return msgString;
 	}
 	
 	/**
     * 提交定制
     * @param storeid 门店id
     * @param type 定制的类型
     * @param datetime 拍摄日期
     * @return
     */
 	public static String submitCustom(String sceneid,String propsid,String garmentid,String customtime,String comments){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appid", APPID));
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("action", ACTION_SUBMIT_CUSTOM));
 		params.add(new BasicNameValuePair("username", UserBeanInfo.getInstant().getUserName()));
 		params.add(new BasicNameValuePair("password", UserBeanInfo.getInstant().getPassword()));
 		params.add(new BasicNameValuePair("sceneid", sceneid));
 		params.add(new BasicNameValuePair("propsid", propsid));
 		params.add(new BasicNameValuePair("garmentid", garmentid));
 		params.add(new BasicNameValuePair("customtime", customtime));
 		params.add(new BasicNameValuePair("comments", comments));
 		msgString = getData(INTERFACE_CUSTOM, params);
 		return msgString;
 	}
 	
 	/**
     * 获取我的定制
     * @param storeid 门店id
     * @param type 定制的类型
     * @param datetime 拍摄日期
     * @return
     */
 	public static String getMyCustom(){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appid", APPID));
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("action", ACTION_GET_MYCUSTOM));
 		params.add(new BasicNameValuePair("username", UserBeanInfo.getInstant().getUserName()));
 		params.add(new BasicNameValuePair("password", UserBeanInfo.getInstant().getPassword()));
 		msgString = getData(INTERFACE_CUSTOM, params);
 		return msgString;
 	}
 	
   /**
    * 插件
    * @param username 用户名
    * @param password 密码
    * @param storeid 门店id
    * @param ordernumber 档案卡号
    * @param datetime 拍摄日期
    * @return
    */
	public static String selectMyPhotos(String username,String password,String storeid,String ordernumber,String datetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_SELECTPHOTOS));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("ordernumber", ordernumber));
		params.add(new BasicNameValuePair("datetime", datetime));
		msgString = getData(INTERFACE_SELECTPHOTOS, params);
		return msgString;
	}
    /**
     * 系统更新机制
     * @author 刘星星
     * @createDate 2013/9/2
     * @return
     */
	public static String systemUpdate(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", ACTION_SYSTEMUPDATE));
		list.add(new BasicNameValuePair("storeid", storeid));
		list.add(new BasicNameValuePair("userid", UserBeanInfo.getInstant().getUserid()));
		list.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(INTERFACE_SYSTEM, list);
		return msgString;
	}
	 /**
     * 获取门店列表数据
     * @author 刘星星
     * @createDate 2013/9/3
     * @return
     */
	public static String getStoresList(String parentid){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", ACTION_GETSOTRES));
		list.add(new BasicNameValuePair("regionid", parentid));
		msgString = getData(INTERFACE_SYSTEM, list);
		return msgString;
	}
	/**
     * 获取门店列表数据
     * @author 刘星星
     * @createDate 2013/9/3
     * @return
     */
	public static String getStoreDetail(String storeId){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", ACTION_GETSOTREDETAIL));
		list.add(new BasicNameValuePair("storeid", storeId));
		msgString = getData(INTERFACE_SYSTEM, list);
		return msgString;
	}
	/**
     * 图片下载接口
     * @author 刘星星
     * @createDate 2013/9/4
     * @return
     */
	public static String downloadImg(String imgId,String imgType,String download_type,int width,int height){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", ACTION_DOWNLOADIMG));
		list.add(new BasicNameValuePair("imageid", imgId));
		list.add(new BasicNameValuePair("image_type", imgType));
		list.add(new BasicNameValuePair("download_type", imgType));
		list.add(new BasicNameValuePair("width", String.valueOf(width)));
		list.add(new BasicNameValuePair("height", String.valueOf(height)));
		msgString = getData(INTERFACE_SYSTEM, list);
		return msgString;
	}
	/**
     * 获取短信验证码
     * @author 刘星星
     * @createDate 2013/9/4
     * @return
     */
	public static String getMsgCode(String phoneNumber){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", ACTION_GETMSGCODE));
		list.add(new BasicNameValuePair("storeid", UserBeanInfo.getInstant().getCurrentStoreId()));
		list.add(new BasicNameValuePair("mobilenumber", phoneNumber));
		msgString = getData(INTERFACE_USER, list);
		return msgString;
	}
	
	
	/**
     * 用户注册接口
     * @author 刘星星
     * @createDate 2013/9/5
     * @return
     */
	public static String registerUser(String password,String realname,String phonenumber,String qq,String gender,String email,String verification_code){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", ACTION_REGISTER));
		list.add(new BasicNameValuePair("storeid", UserBeanInfo.getInstant().getCurrentStoreId()));
		list.add(new BasicNameValuePair("username", phonenumber));
		list.add(new BasicNameValuePair("password", password));
		list.add(new BasicNameValuePair("realname", realname));
		list.add(new BasicNameValuePair("phonenumber", phonenumber));
		list.add(new BasicNameValuePair("qq", qq));
		list.add(new BasicNameValuePair("gender", gender));
		list.add(new BasicNameValuePair("email", email));
		list.add(new BasicNameValuePair("verification_code", verification_code));
		msgString = getData(INTERFACE_USER, list);
		return msgString;
	}
	/**
	 * 用户登录接口
	 * @return
	 */
	public static String loginUser(String username,String password){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_LOGIN));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		msgString = getData(INTERFACE_USER, params);
		return msgString;
	}
	/**
	 * 获取用户相册id
	 * @return
	 */
	public static String getUserPhoto(String username,String password,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_GETUSERPHOTO));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(INTERFACE_USER, params);
		return msgString;
	}
	/**
	 * 获取美图相册列表
	 * @return
	 */
	public static String getBeautyPhotoThemes(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_GETBEAUTYPHOTO_THEMES));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(INTERFACE_BEAUTYPHOTO, params);
		return msgString;
	}
	
	/**
	 * 获取美图相册图片列表
	 * @return
	 */
	public static String getBeautyPhotos(String albumid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_GETBEAYTYPHOTOS));
		params.add(new BasicNameValuePair("albumid", albumid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(INTERFACE_BEAUTYPHOTO, params);
		return msgString;
	}
	
	/**
	 * 推荐美图
	 * @return
	 */
	public static String tuiJianBeautyPhotos(String albumid,String valuation,String username,String password){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_TUIJIAN_BEAYTYPHOTOS));
		params.add(new BasicNameValuePair("albumid", albumid)); 
		params.add(new BasicNameValuePair("valuation", valuation));
		params.add(new BasicNameValuePair("username", username ));
		params.add(new BasicNameValuePair("password", password ));
		msgString = getData(INTERFACE_BEAUTYPHOTO, params);
		return msgString;
	}
	/**
	 * 获取活动分享列表数据
	 * @return
	 */
	public static String getActiveData(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_GETACTIVE_LIST));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(INTERFACE_ACTIVE, params);
		return msgString;
	}
	
	/**
	 * 获取活动分享列表数据
	 * @return
	 */
	public static String getActiveDetail(String storeid,String id){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_GETACTIVE_DETILE));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("id", id));
		msgString = getData(INTERFACE_ACTIVE, params);
		return msgString;
	}
	/**
	 * 获取百宝箱类型列表数据
	 * @return
	 */
	public static String getTreasureChestList(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_PARTNER));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(INTERFACE_PARTNER, params);
		return msgString;
	}
	/**
	 * 获取百宝箱中商家列表数据
	 * @return
	 */
	public static String getMerchanList(String storeid,String typeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_Merchan));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("typeid", typeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(INTERFACE_PARTNER, params);
		return msgString;
	}
	
	
	/**
	 * 获取百宝箱中商家的产品列表数据
	 * @return
	 */
	public static String getRecommendList(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_GETRECOMMEND));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(INTERFACE_RECOMMEND, params);
		return msgString;
	}
	
	/**
	 * 获取我要点评中的被点评人的数据
	 * @return
	 */
	public static String getValuationList(String storeid,String position,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_VALUATION));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("position", position));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(INTERFACE_VALUATION, params);
		return msgString;
	}
	
	
	/**
	 * 提交点评
	 * @return
	 */
	public static String submitValuation(String staffid,String value1,String value2,String value3,String remark){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_SUBMITVALUATION));
		params.add(new BasicNameValuePair("username", UserBeanInfo.getInstant().getUserName())); 
		params.add(new BasicNameValuePair("password", UserBeanInfo.getInstant().getPassword()));
		params.add(new BasicNameValuePair("staffid", staffid));
		params.add(new BasicNameValuePair("value1", value1));
		params.add(new BasicNameValuePair("value2", value2));
		params.add(new BasicNameValuePair("value3", value3));
		params.add(new BasicNameValuePair("remark", remark));
		msgString = getData(INTERFACE_VALUATION, params);
		return msgString;
	}
	
	
	/**
	 * 获取积分商城列表数据
	 * @return
	 */
	public static String getIntegralType(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_INTREGRAL_TYPE));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(INTERFACE_INTEGRAL, params);
		return msgString;
	}
	/**
	 * 获取积分商城商品列表数据
	 * @return
	 */
	public static String getGoodsList(String storeid,String typeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_INTREGRAL_GOODS));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("typeid", typeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(INTERFACE_INTEGRAL, params);
		return msgString;
	}
	
	/**
	 * 申请兑换接口实现函数
	 * @param goodsid 商品id
	 * @param phonenumber 收件人联系电话
	 * @param address 收件人地址
	 * @return
	 */
	public static String exchangeGoodsData(String goodsid,String phonenumber,String address){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_INTREGRAL_EXCHANGE));
		params.add(new BasicNameValuePair("username", UserBeanInfo.getInstant().getUserName()));
		params.add(new BasicNameValuePair("password", UserBeanInfo.getInstant().getPassword()));
		params.add(new BasicNameValuePair("goodsid", goodsid));
		params.add(new BasicNameValuePair("amount", "1"));
		params.add(new BasicNameValuePair("receiver_name", ""));
		params.add(new BasicNameValuePair("address", address));
		params.add(new BasicNameValuePair("phonenumber", phonenumber));
		params.add(new BasicNameValuePair("postcode", ""));
		msgString = getData(INTERFACE_INTEGRAL, params);
		return msgString;
	}
	
	/**
	 * 推荐好友接口实现函数
	 * @param friend_name 好友姓名
	 * @param age 好友年龄
	 * @param qq 好友QQ
	 * @param phone_number 好友电话
	 * @param address 好友地址
	 * @return
	 */
	public static String recommendFriend(String friend_name,String age,String qq,String phone_number,String address){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_INTREGRAL_RECOMMENDFRIEND));
		params.add(new BasicNameValuePair("username", UserBeanInfo.getInstant().getUserName()));
		params.add(new BasicNameValuePair("password", UserBeanInfo.getInstant().getPassword()));
		params.add(new BasicNameValuePair("friend_name", friend_name));
		params.add(new BasicNameValuePair("age", age));
		params.add(new BasicNameValuePair("qq", qq));
		params.add(new BasicNameValuePair("phone_number", phone_number));
		params.add(new BasicNameValuePair("address", address));
		msgString = getData(INTERFACE_USER, params);
		return msgString;
	}
	/**
	 *我的积分
	 * @param friend_name 好友姓名
	 * @param age 好友年龄
	 * @param qq 好友QQ
	 * @param phone_number 好友电话
	 * @param address 好友地址
	 * @return
	 */
	public static String getMyIntegral(){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_USER_GETMYPOINTS));
		params.add(new BasicNameValuePair("username", UserBeanInfo.getInstant().getUserName()));
		params.add(new BasicNameValuePair("password", UserBeanInfo.getInstant().getPassword()));
		msgString = getData(INTERFACE_USER, params);
		return msgString;
	}
	
	/**
	 * 推荐好友接口实现函数
	 * @param friend_name 好友姓名
	 * @param age 好友年龄
	 * @param qq 好友QQ
	 * @param phone_number 好友电话
	 * @param address 好友地址
	 * @return
	 */
	public static String deletePhoto(String photoId){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", ACTION_DELETEIMG));
		params.add(new BasicNameValuePair("username", UserBeanInfo.getInstant().getUserName()));
		params.add(new BasicNameValuePair("password", UserBeanInfo.getInstant().getPassword()));
		params.add(new BasicNameValuePair("photoid", photoId));
		msgString = getData(INTERFACE_USER, params);
		return msgString;
	}
	/**
	 * 图片下载
	 * @param context 
	 * @param imageView 装载图片的控件
	 * @param imgid 图片id
	 * @param image_type 图片类型 
	 * @param download_type 下载类型 大图或者缩略图
	 * @param width 想要的宽度
	 * @param height 想要的图片的高度
	 * @param isBackground 是否为背景图片
	 * @param imgUrl 图片的本地保存路劲
	 */
	public static void downImage(Context context,ImageView imageView,String imgid,String image_type,
			String download_type,String width,String height,boolean isBackground,String imgUrl,int defId){
		//如果图片在本地不存在，则根据id准备去服务器下载。
		CallbackImpl callbackImpl = new CallbackImpl(context,imageView);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", ACTION_DOWNLOAD));
		list.add(new BasicNameValuePair("imageid", imgid));
		list.add(new BasicNameValuePair("image_type",image_type));
		list.add(new BasicNameValuePair("download_type", download_type));
		list.add(new BasicNameValuePair("width", width));
		list.add(new BasicNameValuePair("height", height));
		ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
		imageLoad.loadImageFromId(context, INTERFACE_SYSTEM,
				list, defId, imgUrl, callbackImpl,false,isBackground,MyApplication.getInstant().getWidthPixels()+20,0,0);
	}
}
