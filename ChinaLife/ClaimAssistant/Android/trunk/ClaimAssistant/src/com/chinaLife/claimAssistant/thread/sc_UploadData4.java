package com.chinaLife.claimAssistant.thread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.tools.sc_AESKeyGenerator;
import com.chinaLife.claimAssistant.tools.sc_DESKeyGenerator;


public class sc_UploadData4 {
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
	
	public sc_UploadData4(String url, List<BasicNameValuePair> params) {
		this.url = url;
		this.params = params;
	}
	
	private List<BasicNameValuePair> encodeParams(
			List<BasicNameValuePair> params2) throws Exception {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		String key = DESKeyGenerator.initKey("123");
		String key  = sc_MyApplication.getInstance().getSecretclient();
		for (BasicNameValuePair par : params2) {
			byte[] inputData = par.getValue().getBytes("utf-8");
			inputData = sc_DESKeyGenerator.encrypt(inputData, key);
			params.add(new BasicNameValuePair(par.getName(), new BASE64Encoder().encode(inputData)));
		}
		params.add(new BasicNameValuePair("encryption", 1 + ""));
		return params;
	}
	/***
	 * 向服务器提交请求
	 */
	public void Post() {
		if(sc_MyApplication.getInstance().getSecretclient()==null||
				sc_MyApplication.getInstance().getSecretclient().equals("")
				||sc_MyApplication.getInstance().getSecretsystem() == null
				||sc_MyApplication.getInstance().getSecretsystem().equals("")){
			List<Map<String, Object>> selectresult = null;
			selectresult = sc_DBHelper.getInstance().selectRow(
					"select * from appinfo", null);
			System.out.println(selectresult);
			if (selectresult != null && selectresult.size() > 0) {
				if (selectresult.get(selectresult.size() - 1) != null
						&& selectresult.get(selectresult.size() - 1).get(
								"secret_client") != null) {
					String secret_client = (selectresult.get(selectresult.size() - 1).get(
							"secret_client").toString());
					sc_MyApplication.getInstance().setSecretclient(secret_client);
				}
				
				if (selectresult.get(selectresult.size() - 1) != null
						&& selectresult.get(selectresult.size() - 1).get(
								"secret_system") != null) {
					String secret_system = (selectresult.get(selectresult.size() - 1).get(
							"secret_system").toString());
					sc_MyApplication.getInstance().setSecretsystem(secret_system);
				}
				
			}
			return;
		}
		System.out.println("服务器开始请求时的url---"+url);
		System.out.println("服务器开始请求时的参数列表---"+ params);
		
		try {
			params = encodeParams(params);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		request = new HttpPost(url);
		HttpParams httpParameters = new BasicHttpParams();
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
			HttpConnectionParams.setSoTimeout(httpParameters, 10000);
			request.setParams(httpParameters);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			this.reponse = "-1000";
		}
		reponse = "";
		reponse = connServerForResult(request);
		if(reponse.equals("-32")){
			if(sc_GetKey.getKey(sc_MyApplication.getInstance().getContext2())){
				Post();
				return;
			}else{
				reponse = "-1000";
			}
		}
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
		String strResult = "-1000";
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
				// 取得返回的数据
				String outputStr = EntityUtils.toString(httpResponse.getEntity());
				if(outputStr.equals("-32")){
					return "-32";
				}
				String key = sc_MyApplication.getInstance().getSecretsystem();
				byte[] outputData = sc_AESKeyGenerator.decrypt(new BASE64Decoder().decodeBuffer(outputStr), key);
				strResult = new String(outputData,"utf-8");
			}
			System.out.println("服务器请求完毕后的返回值为----"+strResult);
			reponse = strResult;
		} catch (final Exception e) {
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
