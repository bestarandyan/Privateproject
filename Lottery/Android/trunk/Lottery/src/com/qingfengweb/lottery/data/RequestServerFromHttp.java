package com.qingfengweb.lottery.data;

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
public class RequestServerFromHttp {
//	public static final String SERVER_ADDRESS = "http://192.168.1.101";//内网服务器总接口地址
	public static final String SERVER_ADDRESS = "http://222.73.173.53:8003/api.php";//内网服务器总接口地址
//	public static final String SERVER_ADDRESS = "http://domain/api.php";//外网服务器总接口地址
	public static final String APPID = "100001";//
	public static final String APPKEY = "4b51f72fabc8f33e7cb1f84657ac7437";//
	public static final String INTERFACE_REGISTER =SERVER_ADDRESS + "/api/doreg";//注册接口
	public static final String INTERFACE_LOGIN =SERVER_ADDRESS + "/api/dologin";//登录接口
	public static final String INTERFACE_AMENDINFO =SERVER_ADDRESS + "/api/doidinfo";//完善信息接口
	public static final String INTERFACE_TRADE =SERVER_ADDRESS + "/api/trade";//充值接口
	public static final String INTERFACE_TRADE_HISTORY =SERVER_ADDRESS + "/api/trade";//充值历史接口
	public static final String INTERFACE_AMEND_PWD =SERVER_ADDRESS + "/api/changepwd";//修改密码接口
	public static final String INTERFACE_QUERY_MONEY =SERVER_ADDRESS + "/api/balance";//查询余额接口
	public static final String INTERFACE_ORDER =SERVER_ADDRESS + "/api/order";//订单查询接口
	public static final String INTERFACE_RESULT =SERVER_ADDRESS + "/api/doresult";//开奖结果查询接口
	public static final String INTERFACE_UPLOAD_HEADIMG =SERVER_ADDRESS + "/api/doavatar";//上传头像接口地址
	public static final String INTERFACE_SERVERTIME =SERVER_ADDRESS + "/api/servertime";//获取服务器时间接口
	public static final String INTERFACE_LASTINFO =SERVER_ADDRESS + "/api/lastinfo";//获取最新开奖结果信息
	
	
	/**
     * 最新开奖结果
     * @return
     */
 	public static String getLastResult(){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		msgString = getData(INTERFACE_LASTINFO, params);
 		return msgString;
 	}
	/**
     * 服务时间
     * @return
     */
 	public static String getServerTime(){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		msgString = getData(INTERFACE_SERVERTIME, params);
 		return msgString;
 	}
	
	/**
     * 开奖结果查询
     * @return
     */
 	public static String queryInfoResult(String start,String end){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("ticket_no_start", start));
 		params.add(new BasicNameValuePair("ticket_no_end", end));
 		msgString = getData(INTERFACE_RESULT, params);
 		return msgString;
 	}
 	/**
     * 生成订单成功
     * @return
     */
 	public static String getOrderSuccess(String order_no){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("token", MyApplication.getInstance().getCurrentToken()));
 		params.add(new BasicNameValuePair("action", "do_succ"));
 		params.add(new BasicNameValuePair("order_no", order_no));
 		msgString = getData(INTERFACE_ORDER, params);
 		return msgString;
 	}
	/**
     * 生成订单
     * @return
     */
 	public static String getOrder(String amount,String is_trace,String trace_count,
 			String stop_after_win,String multiple,String ticket_no,String result){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("token", MyApplication.getInstance().getCurrentToken()));
 		params.add(new BasicNameValuePair("action", "do"));
 		params.add(new BasicNameValuePair("amount", amount));
 		params.add(new BasicNameValuePair("is_trace", is_trace));
 		params.add(new BasicNameValuePair("trace_count", trace_count));
 		params.add(new BasicNameValuePair("stop_after_win", stop_after_win));
 		params.add(new BasicNameValuePair("multiple", multiple));
 		params.add(new BasicNameValuePair("ticket_no", ticket_no));
 		params.add(new BasicNameValuePair("result", result));
 		msgString = getData(INTERFACE_ORDER, params);
 		return msgString;
 	}
	/**
     * 订单查询
     * @return
     */
 	public static String queryOrder(String offset,String size){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("token", MyApplication.getInstance().getCurrentToken()));
 		params.add(new BasicNameValuePair("action", "query"));
 		params.add(new BasicNameValuePair("offset", offset));
 		params.add(new BasicNameValuePair("size", size));
 		msgString = getData(INTERFACE_ORDER, params);
 		return msgString;
 	}
	
	/**
     * 查询余额
     * @return
     */
 	public static String queryMoney(){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("token", MyApplication.getInstance().getCurrentToken()));
 		msgString = getData(INTERFACE_QUERY_MONEY, params);
 		return msgString;
 	}
	
	/**
     * 修改密码
     * @return
     */
 	public static String amendPWD(String oldpwd,String newpwd){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("token", MyApplication.getInstance().getCurrentToken()));
 		params.add(new BasicNameValuePair("oldpwd", oldpwd));
 		params.add(new BasicNameValuePair("newpwd", newpwd));
 		msgString = getData(INTERFACE_AMEND_PWD, params);
 		return msgString;
 	}
 	
	/**
     * 充值历史接口
     * @return
     */
 	public static String userTradeHistory(String offset,String size){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("token", MyApplication.getInstance().getCurrentToken()));
 		params.add(new BasicNameValuePair("action", "query"));
 		params.add(new BasicNameValuePair("offset", offset));
 		params.add(new BasicNameValuePair("size", size));
 		msgString = getData(INTERFACE_TRADE_HISTORY, params);
 		return msgString;
 	}
	/**
     * 充值接口
     * @return
     */
 	public static String userTrade(String amount,String desc,String type){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("token", MyApplication.getInstance().getCurrentToken()));
 		params.add(new BasicNameValuePair("action", "recharge"));
 		params.add(new BasicNameValuePair("amount", amount));
 		params.add(new BasicNameValuePair("trade_desc", desc));
 		params.add(new BasicNameValuePair("payment_type", type));
 		msgString = getData(INTERFACE_TRADE, params);
 		return msgString;
 	}
	
 	/**
     * 充值成功接口
     * @return
     */
 	public static String userTradeSuccess(String trade_no){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("token", MyApplication.getInstance().getCurrentToken()));
 		params.add(new BasicNameValuePair("action", "recharge_succ"));
 		params.add(new BasicNameValuePair("trade_no", trade_no));
 		msgString = getData(INTERFACE_TRADE, params);
 		return msgString;
 	}
 	
	/**
     * 用户注册接口
     * @return
     */
 	public static String registerUser(String userName,String password){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("username", userName));
 		params.add(new BasicNameValuePair("password", password));
 		msgString = getData(INTERFACE_REGISTER, params);
 		return msgString;
 	}
 	/**
     * 用户登录接口
     * @return
     */
 	public static String loginUser(String userName,String password){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("username", userName));
 		params.add(new BasicNameValuePair("password", password));
 		msgString = getData(INTERFACE_LOGIN, params);
 		return msgString;
 	}
 	/**
     * 用户完善信息接口
     * @return
     */
 	public static String amendUserInfo(String nickName,String realName,String identity,String phoneNumber){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("appkey", APPKEY));
 		params.add(new BasicNameValuePair("token", MyApplication.getInstance().getCurrentToken()));
 		params.add(new BasicNameValuePair("nick_name", nickName));
 		params.add(new BasicNameValuePair("real_name", realName));
 		params.add(new BasicNameValuePair("identity_card", identity));
 		params.add(new BasicNameValuePair("mobile", phoneNumber));
 		msgString = getData(INTERFACE_AMENDINFO, params);
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
}
