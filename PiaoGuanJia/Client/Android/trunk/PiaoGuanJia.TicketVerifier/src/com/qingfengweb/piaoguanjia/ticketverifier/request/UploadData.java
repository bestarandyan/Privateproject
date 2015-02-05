package com.qingfengweb.piaoguanjia.ticketverifier.request;

import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
public class UploadData {
	/**
	 * author:Ring
	 * 处理带参数的请求
	 */
	// 向服务器请求的动作参数
	private String url = "";
	// 上传数据参数列表
	private List<NameValuePair> params = null;
	// 服务器响应值
	private String reponse = "";
	// 服务器请求
	private HttpPost request = null;
	
	public UploadData(String url, List<NameValuePair> params) {
		this.url = url;
		this.params = params;
	}
	
	/***
	 * 向服务器提交请求
	 */
	public void Post() {

		System.out.println("服务器开始请求时的url---"+url);
		System.out.println("服务器开始请求时的参数列表---" + params);
		request = new HttpPost(url);
		HttpParams httpParameters = new BasicHttpParams();
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
			HttpConnectionParams.setSoTimeout(httpParameters, 10000);
			request.setParams(httpParameters);
		} catch (UnsupportedEncodingException e) {
			reponse = "-1000";
		}
		reponse = connServerForResult(request);
		params.clear();
		params = null;
		System.out.println("服务器响应值为---->>>" + reponse);
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
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000*10);  
            //请求超时  
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10*1000); 
			HttpResponse httpResponse = httpClient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return "-404";
			}
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
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
