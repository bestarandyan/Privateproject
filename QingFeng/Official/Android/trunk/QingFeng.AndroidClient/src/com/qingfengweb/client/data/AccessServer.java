/**
 * 
 */
package com.qingfengweb.client.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import android.os.Bundle;

/**
 * @author 刘星星
 * @createDate 2013/7/17
 *访问服务器类
 */
public class AccessServer {
	public static final String APPID = "100001";
	public static final String APPKEY = "7ED7AE79D7D54EDE2CBA27CD302405C9";
	public static final String SERVICE_INTERFACE = "http://www.qingfengweb.com";//服务器接口总地址
//	public static final String SERVICE_INTERFACE = "http://192.168.1.130:7001/QingFeng.WebSite";//服务器接口总地址
	public static final String DEVICE_INTERFACE = SERVICE_INTERFACE+"/interface/device";//设备接口地址
	public static final String CONTENT_INTERFACE = SERVICE_INTERFACE+"/interface/content";//内容接口地址
	public static final String SYSTEM_INTERFACE = SERVICE_INTERFACE+"/interface/system";//系统更新接口地址
	public static final String FILE_UPLOAD_INTERFACE = SERVICE_INTERFACE+"/interface/upload";//上传文件的接口地址
	public static final String DEVICE_ACTION = "register";//设备注册动作标记
	public static final String LOCATION_ACTION = "location";//设备提交位置信息标记
	public static final String SERVICES_ACTION = "services";//获取服务项目内容动作标记
	public static final String LIST_ACTION = "list";//获取信息列表动作
	public static final String TEAMMEMBER_ACTION = "team";//团队介绍动作标记
	public static final String CASE_ACTION = "case";//客户案例动作标记 
	public static final String JOB_ACTION = "job";//招聘信息动作标记
	public static final String APPLY_JOB_ACTION = "apply_job";//申请职位动作
	public static final String SUBMIT_JOBFILE_ACTION = "submit_requirements";//上传文件动作
	public static final String UPDATE_ACTION = "update";//更新内容动作标么记
	public static final String GETVERSION_ACTION = "version";//获取版本动作标记
	public static final String SERVICES_DETAIL_ACTION = "detail";//服务项目详情动作标记
	public static final String DOWNLOAD_IMAGE_DETAIL_ACTION = "download_image";//服务项目详情动作标记
	public static final String HOME_IMAGE_ACTION = "home_images";//首页获取图片的动作标记
	
	
	
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
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
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
     * 注册设备访问服务器函数
     * @author 刘星星
     * @createDate 2013/7/17
     * @return
     */
	public static String registerDevice(){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", DEVICE_ACTION));
		list.add(new BasicNameValuePair("brand", MyApplication.getInstant().getDevice_brand()));
		list.add(new BasicNameValuePair("model", MyApplication.getInstant().getDevice_model()));
		list.add(new BasicNameValuePair("resolution", MyApplication.getInstant().getScreenW()+"*"+MyApplication.getInstant().getScreenH()));
		list.add(new BasicNameValuePair("osname", MyApplication.getInstant().getDevice_osname()));
		list.add(new BasicNameValuePair("osversion", MyApplication.getInstant().getDevice_osversion()));
		list.add(new BasicNameValuePair("token", MyApplication.getInstant().getDevice_token()));
		msgString = getData(DEVICE_INTERFACE, list);
		return msgString;
	}
	/**
     * 上传位置信息。
     * @author 刘星星
     * @createDate 2013/7/17
     * @return
     */
	public static String uploadLocation(String type,String location){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", LOCATION_ACTION));
		list.add(new BasicNameValuePair("token", MyApplication.getInstant().getDevice_token()));
		list.add(new BasicNameValuePair("type", type));
		list.add(new BasicNameValuePair("location", location));
		msgString = getData(DEVICE_INTERFACE, list);
		return msgString;
	}
	
	/**
     * 获取内容
     * @author 刘星星
     * @createDate 2013/7/22
     * @return
     */
	public static String getContents(String lastUpdateTime,String action){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", action));
		list.add(new BasicNameValuePair("update_time", lastUpdateTime));
		msgString = getData(CONTENT_INTERFACE, list);
		return msgString;
	}
	/**
	 * 判断服务器返回值是否为无数据的格式
	 * @param str
	 * @return
	 */
	public static boolean isNoData(String str){
		String format = "\\d+\\,20[1,2]\\d-((0?[0-9])|1[0-2])-(([0-2][0-9])|3[0-1])(\\s([0-1][0-9]|2[0-4]):([0-5][0-9])(:([0-5][0-9]))?)?";
		Matcher matcher = Pattern.compile(format).matcher(str);
		while(matcher.find()){
			return true;
		}
		return false;
	}
	/**
     * 获取服务项目详情
     * @author 刘星星
     * @createDate 2013/7/23
     * @return
     */
	public static String getContentDetail(String type){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", SERVICES_DETAIL_ACTION));
		list.add(new BasicNameValuePair("type", type));
		msgString = getData(CONTENT_INTERFACE, list);
		return msgString;
	}
	/**
	 * 获取团队信息
	 * @return
	 */
	public static String getTeamMember(){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", "register"));
		list.add(new BasicNameValuePair("brand", MyApplication.getInstant().getDevice_brand()));
		list.add(new BasicNameValuePair("model", MyApplication.getInstant().getDevice_model()));
		list.add(new BasicNameValuePair("resolution", MyApplication.getInstant().getScreenW()+"*"+MyApplication.getInstant().getScreenH()));
		list.add(new BasicNameValuePair("osname", MyApplication.getInstant().getDevice_osname()));
		list.add(new BasicNameValuePair("osversion", MyApplication.getInstant().getDevice_osversion()));
		list.add(new BasicNameValuePair("token", MyApplication.getInstant().getDevice_token()));
		msgString = getData(DEVICE_INTERFACE, list);
		return msgString;
	}
	/**
	 * 	提交职位申请
	 * @return
	 */
	public static String submitJobAsk(Bundle bundle){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", APPLY_JOB_ACTION));
		list.add(new BasicNameValuePair("jobid", bundle.getString("jobid")));
		list.add(new BasicNameValuePair("name", bundle.getString("name")));
		list.add(new BasicNameValuePair("gender", bundle.getString("gender")));
		list.add(new BasicNameValuePair("age", bundle.getString("age")));
		list.add(new BasicNameValuePair("phone_number", bundle.getString("phone_number")));
		list.add(new BasicNameValuePair("email", bundle.getString("email")));
		list.add(new BasicNameValuePair("resume", bundle.getString("resume")));
		list.add(new BasicNameValuePair("message", bundle.getString("message")));
		msgString = getData(CONTENT_INTERFACE, list);
		return msgString;
	}
/*	*//**
	 * 	上传文件
	 * @return
	 *//*
	public static String submitJobFile(Bundle bundle){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", SUBMIT_JOBFILE_ACTION));
		list.add(new BasicNameValuePair("type", bundle.getString("type")));
		list.add(new BasicNameValuePair("id", bundle.getString("id")));
		list.add(new BasicNameValuePair("filesize", bundle.getString("filesize")));
		list.add(new BasicNameValuePair("start", bundle.getString("start")));
		list.add(new BasicNameValuePair("length", bundle.getString("length")));
		msgString = getData(FILE_UPLOAD_INTERFACE, list);
		return msgString;
	}*/
	/**
	 *  	提交开发需求
	 * @return
	 */
	public static String submitDevelopDemand(Bundle bundle){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", SUBMIT_JOBFILE_ACTION));
		list.add(new BasicNameValuePair("type", bundle.getString("typeStr")));
		list.add(new BasicNameValuePair("name", bundle.getString("nameStr")));
		list.add(new BasicNameValuePair("phone_number", bundle.getString("phoneStr")));
		list.add(new BasicNameValuePair("email", bundle.getString("emailStr")));
		list.add(new BasicNameValuePair("qq", bundle.getString("qqStr")));
		list.add(new BasicNameValuePair("description", bundle.getString("detailStr")));
		list.add(new BasicNameValuePair("budget", bundle.getString("budgetStr")));
		list.add(new BasicNameValuePair("time_required", bundle.getString("timeStr")));
		list.add(new BasicNameValuePair("attachment", bundle.getString("fileIdStr")));
		msgString = getData(CONTENT_INTERFACE, list);
		return msgString;
	}
	
	/**
	 * 获取更新内容
	 * @param update_time 最后更新时间，必须为服务器时间
	 * @return
	 */
	public static String getUpdateContent(String update_time){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", UPDATE_ACTION));
		list.add(new BasicNameValuePair("update_time", update_time));
		msgString = getData(SYSTEM_INTERFACE, list);
		return msgString;
	}
	
	/**
	 * 获取更新内容
	 * @param update_time 最后更新时间，必须为服务器时间
	 * @return
	 */
	public static String getUpdateVersion(String version){
		String msgString = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", APPID));
		list.add(new BasicNameValuePair("appkey", APPKEY));
		list.add(new BasicNameValuePair("action", GETVERSION_ACTION));
		list.add(new BasicNameValuePair("type", "Android"));
		list.add(new BasicNameValuePair("version", version));
		msgString = getData(SYSTEM_INTERFACE, list);
		return msgString;
	}
}
