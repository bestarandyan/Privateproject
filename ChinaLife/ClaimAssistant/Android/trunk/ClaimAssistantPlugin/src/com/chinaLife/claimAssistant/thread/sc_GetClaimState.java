package com.chinaLife.claimAssistant.thread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

import com.chinaLife.claimAssistant.activity.Sc_MyApplication;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.tools.sc_AESKeyGenerator;
import com.chinaLife.claimAssistant.tools.sc_DESKeyGenerator;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.chinaLife.claimAssistant.tools.sc_VisiteTimes2;
import com.sqlcrypt.database.ContentValues;


public class sc_GetClaimState{

	private static sc_GetClaimState mInstance = null;
	// 服务器url
	private static String url = null;
	// 上传数据参数列表
	private static List<BasicNameValuePair> params = null;
	// 类型
	private static int type;

	public sc_GetClaimState(String url, List<BasicNameValuePair> params, int type) {
		sc_GetClaimState.url = url;
		sc_GetClaimState.params = params;
		sc_GetClaimState.type = type;
	}

	public sc_GetClaimState() {
	}

	public static sc_GetClaimState getInstance() {
		if (mInstance == null) {
			mInstance = new sc_GetClaimState();
		}
		return mInstance;
	}

	public static void startMyThread() {
		if (sc_NetworkCheck.IsHaveInternet(Sc_MyApplication.getInstance()
				.getContext())) {
			if (Sc_MyApplication.getInstance().getClaimid() != null
					&& !Sc_MyApplication.getInstance().getClaimid().equals("")) {
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
				params.add(new BasicNameValuePair("appkey",
						Sc_MyApplication.APPKEY));
				params.add(new BasicNameValuePair("action", "getCliamStatus"));
				params.add(new BasicNameValuePair("claimid", Sc_MyApplication
						.getInstance().getClaimid()));
				params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo
						.getIMEI(Sc_MyApplication.getInstance().getContext())));
				sc_GetClaimState.url = Sc_MyApplication.URL + "claim";
				sc_GetClaimState.params = params;
				sc_GetClaimState.type = 7;

				if (mInstance == null) {
					getInstance().Post();
				} else{
					getInstance().Post();
				}
			}
		}
	}

	private List<BasicNameValuePair> encodeParams(
			List<BasicNameValuePair> params2) throws Exception {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		String key  = Sc_MyApplication.getInstance().getSecretclient();
		for (BasicNameValuePair par : params2) {
			byte[] inputData = par.getValue().getBytes("utf-8");
			inputData = sc_DESKeyGenerator.encrypt(inputData, key);
			params.add(new BasicNameValuePair(par.getName(), new BASE64Encoder().encode(inputData)));
		}
		params.add(new BasicNameValuePair("encryption", 1 + ""));
		return params;
	}
	
	
	public void Post() {

		HttpPost request = new HttpPost(url);
		HttpParams httpParameters = new BasicHttpParams();
		try {
			params = encodeParams(params);
		} catch (Exception e1) {
		}
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
			request.setParams(httpParameters);
		} catch (final UnsupportedEncodingException e) {
		}
		// 发送请求
		try {
			// // 得到应答的字符串，这是一个 JSON 格式保存的数据
			String retSrc = connServerForResult(request);
			// // 生成 JSON 对象
			checkresponse(retSrc);
			if(params!=null){
				params.clear();
				params = null;
			}
		} catch (final Exception e) {
		}
	}

	// 得到服务器响应数据
	private String connServerForResult(HttpPost request) {
		// HttpGet对象
		String strResult = "";
		try {
			// 获得HttpResponse对象
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return "-404";
			}
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的数据
				String outputStr = EntityUtils.toString(httpResponse.getEntity());
				String key = Sc_MyApplication.getInstance().getSecretsystem();
				byte[] outputData = sc_AESKeyGenerator.decrypt(new BASE64Decoder().decodeBuffer(outputStr), key);
				strResult = new String(outputData,"utf-8");
			}
		} catch (final Exception e) {
			sc_VisiteTimes2.getInstance().count();
			if(sc_VisiteTimes2.getInstance().isOut()){
				sc_MyHandler.getInstance().sendEmptyMessage(-9);
				
			}else{
				if(sc_VisiteTimes2.getInstance().isInit()){
					sc_VisiteTimes2.getInstance().init();
				}
			}
			e.printStackTrace();
			return "-1000";
		}
		return strResult;
	}

	private void checkresponse(String token) {
		int state = 0;
		try{
			
			state = Integer.parseInt(token.split(",")[0]);
			if(state>0&&state != Sc_MyApplication.getInstance().getClaimidstate()){
				ContentValues values = new ContentValues();
				values.put("status", state);
				boolean b = sc_DBHelper.getInstance()
						.update("claiminfo",
								values,
								"claimid = ?",
								new String[] { Sc_MyApplication.getInstance()
										.getClaimid() });
				if (b) {
					Sc_MyApplication.getInstance().setClaimidstate(state);
				}
			}
		}catch (final Exception e) {	
		}
	}
}
