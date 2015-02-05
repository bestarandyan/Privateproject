package com.qingfengweb.weddingideas.data;

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

import com.qingfengweb.weddingideas.filedownload.CallbackImpl;
import com.qingfengweb.weddingideas.filedownload.ImageLoadFromUrlOrId;

import android.content.Context;
import android.widget.ImageView;
public class RequestServerFromHttp {
	public static  String SERVER_ADDRESS = "http://www.weddingideas.com.cn";//外网服务器总接口地址
//	public static final String SERVER_ADDRESS = "http://192.168.1.114:1367";//内网服务器总接口地址
//	public static final String SERVER_ADDRESS = "http://222.73.173.53:99";//外网服务器总接口地址
	public static  String APPID = "100001";//
	public static  String APPKEY = "c81e728d9d4c2f636f067f89cc14862c";//
	public static  String Interface_user = SERVER_ADDRESS+"/app/app.do";//用户接口地址
	public static  String madeid = "";//制作人id在这个类里面看起来是没有值   但是其实在loadingActivity中赋值了
	
	
	/**
	 * 亲友特惠
	 * @return
	 */
	public static String getPreferenceData(String storeid,String madeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "ticketinfo"));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	
	
	/**
	 * 我要赴宴接口
	 * @return
	 */
	public static String sendFuYan(String username,String userno){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "gustgo"));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("userno", userno));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	
	/**
	 * 发送祝福接口
	 * @return
	 */
	public static String sendZhuFu(String usersay,String gustname,String gustmobile){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "gustsay"));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("usersay", usersay));
		params.add(new BasicNameValuePair("gustname", gustname));
		params.add(new BasicNameValuePair("gustmobile", gustmobile));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	/**
	 * 获取系统时间
	 * @return
	 */
	public static String getSystemTime(){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "serverTime"));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	/**
	 * 签收接口
	 * @param storeid  影楼编号
	 * @param madeid 制作人编号
	 * @param usermob 用户手机号码
	 * @param username 用户姓名
	 * @return
	 */
	public static String userSign(String storeid,String usermob,String username){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "userSign"));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("usermob", usermob));
		params.add(new BasicNameValuePair("username", username));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	
	/**
	 * 主人登陆接口
	 * @param storeid 影楼编号
	 * @param madeid 制作人编号
	 * @param usermob 用户手机号码
	 * @param userpwd 用户密码
	 * @param updatetime 更新时间
	 * @return
	 */
	public static String userLogin(String storeid,String usermob,String userpwd,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "masterLogin"));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("usermob", usermob));
		params.add(new BasicNameValuePair("userpwd", userpwd));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(Interface_user, params);
		return msgString;
	}

	/**
	 * 婚嫁日志
	 * @param storeid
	 * @param madeid
	 * @param updatetime
	 * @return
	 */
	public static String getweddingLog(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "wedUserLog"));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	/**
	 * 亲友特惠接口
	 * @param storeid
	 * @param madeid
	 * @param updatetime
	 * @return
	 */
	public static String relativesPrice(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "ticketInfo"));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	/**
	 * 获取样照数据
	 * @param storeid
	 * @param madeid
	 * @param updatetime
	 * @return
	 */
	public static String getyangzhaoData(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "samplephoto"));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	/**
	 * 获取套系接口数据
	 * @param storeid
	 * @param madeid
	 * @param updatetime
	 * @return
	 */
	public static String gettaoxiData(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "setinfo"));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	/**
	 * 获取活动数据
	 * @param storeid
	 * @param madeid
	 * @param updatetime
	 * @return
	 */
	public static String getactiveData(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "activeinfo"));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	
	/**
	 * 获取影楼数据
	 * @param storeid
	 * @param madeid
	 * @param updatetime
	 * @return
	 */
	public static String getYinglouData(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "aboutinfo"));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	/**
	 * 消息推送接口
	 * @param storeid
	 * @param madeid
	 * @param updatetime
	 * @return
	 */
	public static String pushMsg(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "msginfo"));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(Interface_user, params);
		return msgString;
	}
	
	public static String updateAppVersion(String storeid,String updatetime){
		String msgString = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", APPID));
		params.add(new BasicNameValuePair("appkey", APPKEY));
		params.add(new BasicNameValuePair("action", "updateInfo"));
		params.add(new BasicNameValuePair("storeid", storeid));
		params.add(new BasicNameValuePair("madeid", madeid));
		params.add(new BasicNameValuePair("updatetime", updatetime));
		msgString = getData(Interface_user, params);
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
		list.add(new BasicNameValuePair("action", ""));
		list.add(new BasicNameValuePair("imageid", imgid));
		list.add(new BasicNameValuePair("image_type",image_type));
		list.add(new BasicNameValuePair("download_type", download_type));
		list.add(new BasicNameValuePair("width", width));
		list.add(new BasicNameValuePair("height", height));
		ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
//		imageLoad.loadImageFromId(context, INTERFACE_SYSTEM,
//				list, defId, imgUrl, callbackImpl,false,isBackground,MyApplication.getInstant().getWidthPixels()+20,0,0);
	}
}
