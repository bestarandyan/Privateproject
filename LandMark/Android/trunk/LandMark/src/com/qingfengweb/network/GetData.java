package com.qingfengweb.network;

import com.qingfengweb.constant.ConstantClass;
import com.qingfengweb.database.MyAppLication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  刘星星 13-5-27.
 */
public class GetData {
    private static final String CONNECT_LIST_URL = ConstantClass.HTTP_CONNECT+"/Api/Info";
    private static final String CONNECT_INFORMATION_URL = ConstantClass.HTTP_CONNECT+"/Api/Infodetail";
    private static final String CONNECT_SHOPPINGGUIDE_URL = ConstantClass.HTTP_CONNECT+"/Api/Category";
    private static final String CONNECT_BRAND_URL = ConstantClass.HTTP_CONNECT+"/Api/Guide";
    private static final String CONNECT_GETBACKGROUND_URL = ConstantClass.HTTP_CONNECT+"/Api/Background";
    private static final String APPKEY = "ae2991341f8885dee314b3f0acc3da68";
    private static final String APPID = "1";
    /**
     * 根据不同类型获取列表数据
     * @param typeString promotion=促销活动 special=特惠商品 food=美食速递
     * @param lastUpdate
     * @return 服务器返回的数据
     */
    public static String getListData(String typeString/*,String lastUpdate*/){
        String data = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("appid",APPID));
        params.add(new BasicNameValuePair("appkey",APPKEY));
        params.add(new BasicNameValuePair("type",typeString));
//        params.add(new BasicNameValuePair("count","3"));
//        params.add(new BasicNameValuePair("lastupdate",lastUpdate));
        data = getData(CONNECT_LIST_URL,params);
        return data;
    }
    /**
     * 获取资讯详情数据
     * @param type 资讯类型
     * @param id 资讯id
     * @return 器返回的数据
     */
    public static String getInformationDetail(String type,String id){
    	String msgString = "";
    	List<NameValuePair> list = new ArrayList<NameValuePair>();
    	list.add(new BasicNameValuePair("appid", APPID));
    	list.add(new BasicNameValuePair("appkey", APPKEY));
    	list.add(new BasicNameValuePair("type", type));
    	list.add(new BasicNameValuePair("info_id", id));
    	msgString = getData(CONNECT_INFORMATION_URL,list);
    	return msgString;
    }
    
    /**
     * 获取品牌类别接口数据
     * @return
     */
    public static String getBrandTypeData(){
    	String msgString = "";
    	List<NameValuePair> list = new ArrayList<NameValuePair>();
    	list.add(new BasicNameValuePair("appid", APPID));
    	list.add(new BasicNameValuePair("appkey", APPKEY));
    	msgString = getData(CONNECT_SHOPPINGGUIDE_URL,list);
    	return msgString;
    }
    
    /**
     * 获取品牌类别接口数据
     * @return
     */
    public static String getBrandDetailData(String typeString){
    	String msgString = "";
    	List<NameValuePair> list = new ArrayList<NameValuePair>();
    	list.add(new BasicNameValuePair("appid", APPID));
    	list.add(new BasicNameValuePair("appkey", APPKEY));
    	list.add(new BasicNameValuePair("type", typeString));
//    	list.add(new BasicNameValuePair("cat_id", catId));
//    	list.add(new BasicNameValuePair("keyword", keyword));
    	msgString = getData(CONNECT_BRAND_URL,list);
//    	System.out.println(msgString);
    	return msgString;
    }
    /**
     * 访问网络的总函数
     * @param urlString
     * @param list
     * @return
     */
    public static  String getData(String urlString,List<NameValuePair> list){
    	String msgString = "";
    	HttpPost httpPost = new HttpPost(urlString);
    	try {
			HttpEntity httpEntity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
			httpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			msgString = EntityUtils.toString(httpResponse.getEntity());
//			System.out.println(msgString);
			if(msgString.substring(0, 1).equals("<")){
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
			e.printStackTrace();
		}
    	return msgString;
    }
    /**
     * 获取背景图片
     * @return
     */
    public static String getBackground(){
    	String msg = "";
    	List<NameValuePair> list = new ArrayList<NameValuePair>();
    	list.add(new BasicNameValuePair("appkey", APPKEY));
    	list.add(new BasicNameValuePair("appid", APPID));
    	list.add(new BasicNameValuePair("width", MyAppLication.getInstant().getScreenW()+""));
    	list.add(new BasicNameValuePair("height", MyAppLication.getInstant().getScreenH()+""));
    	msg = getData(CONNECT_GETBACKGROUND_URL, list);	
    	return msg;
    }
}
