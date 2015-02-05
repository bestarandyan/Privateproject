package com.chinaLife.claimAssistant.thread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

import com.chinaLife.claimAssistant.activity.Sc_MyApplication;
import com.chinaLife.claimAssistant.tools.sc_AESKeyGenerator;
import com.chinaLife.claimAssistant.tools.sc_DESKeyGenerator;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;


public class sc_UploadData2 {
	/**
	 * author by Ring
	 * 处理带参数的请求
	 */
	// 向服务器请求的动作参数
	private String url = "";
	// 上传数据参数列表
	private List<BasicNameValuePair> params = null;
	// 服务器响应值
	private String reponse = "";
	// 上下文
	// 服务器请求
	private HttpPost request = null;
	
	public sc_UploadData2(String url, List<BasicNameValuePair> params) {
		this.url = url;
		this.params = params;
	}
	
	/***
	 * 向服务器提交请求
	 */
	public void Post() {
		System.out.println("服务器开始请求时的url---"+url);
		System.out.println("服务器开始请求时的参数列表---"+ params);
		request = new HttpPost(url);
		HttpParams httpParameters = new BasicHttpParams();
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpConnectionParams.setConnectionTimeout(httpParameters, 2000);
			HttpConnectionParams.setSoTimeout(httpParameters, 2000);
			request.setParams(httpParameters);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			this.reponse = "-1000";
		}
		reponse = "";
		reponse = connServerForResult(request);
		params.clear();
		params = null;
		System.out.println("服务器响应值为---" + reponse);
	}
	/***
	 * 向服务器提交请求
	 * @param request
	 * @return
	 */
	private String connServerForResult(HttpPost request) {
		String strResult = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			//连接超时  
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000*2);  
            //请求超时  
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2*1000); 
			HttpResponse httpResponse = httpClient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return "-404";
			}
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的数据
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
			System.out.println("服务器请求完毕后的返回值为----"+strResult);
			reponse = strResult;
		} catch (final Exception e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "服务器请求失败："+e.getMessage());
				}
			}).start();
			e.printStackTrace();
			return "-1000";
		}
		return strResult;
	}
	/***
	 * 取得服务器获取的响应值
	 */
	public String getReponse() {
		return reponse;
	}
	
	/***
	 * 中断请求
	 */
	public void overReponse() {
		if(request!=null&&!request.isAborted()){
			request.abort();
		}
	}
}
