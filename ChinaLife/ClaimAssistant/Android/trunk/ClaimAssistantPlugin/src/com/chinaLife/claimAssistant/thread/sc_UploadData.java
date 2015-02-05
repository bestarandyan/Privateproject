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

import com.chinaLife.claimAssistant.activity.Sc_MainActivity;
import com.chinaLife.claimAssistant.activity.Sc_MyApplication;
import com.chinaLife.claimAssistant.tools.sc_AESKeyGenerator;
import com.chinaLife.claimAssistant.tools.sc_DESKeyGenerator;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_VisiteTimes2;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import android.os.Bundle;
import android.os.Message;

public class sc_UploadData{

	// private static UploadData mInstance = null;
	// 服务器url
	private  String url = null;
	// 上传数据参数列表
	private  List<BasicNameValuePair> params = null;
	// 类型
	private  int type;
	
	private String reponse = "";

	public sc_UploadData(String url, List<BasicNameValuePair> params, int type) {
		Sc_MainActivity.timeron = false;
		Sc_MyApplication.switch_tag = false;
		this.url = url;
		this.params = params;
		this.type = type;
	}

	public void Post() {
		
		if(Sc_MyApplication.getInstance().getSecretclient()==null||
				Sc_MyApplication.getInstance().getSecretclient().equals("")
				||Sc_MyApplication.getInstance().getSecretsystem() == null
				||Sc_MyApplication.getInstance().getSecretsystem().equals("")){
			if(sc_GetKey.getKey(Sc_MyApplication.getInstance().getContext2())){
				Post();
			}else{
				reponse = "-1000";
				checkresponse("-1000");
			}
			return;
		}
		
		System.out.println("服务器开始请求地址---" + url);
		System.out.println("服务器开始请求时的参数列表---" + params);
		
		
		try {
			params = encodeParams(params);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		

		HttpPost request = new HttpPost(url);
		HttpParams httpParameters = new BasicHttpParams();
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpConnectionParams
					.setConnectionTimeout(httpParameters, 10000);
			request.setParams(httpParameters);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("servermsg", "找不到地址！");
			msg.what = -1;
			data.putInt("type", type);
			msg.setData(data);
			sc_MyHandler.getInstance().sendMessage(msg);
		}
		// 发送请求
		try {
			// // 得到应答的字符串，这是一个 JSON 格式保存的数据
			String retSrc = connServerForResult(request);
			if(retSrc.equals("-32")){
				if(sc_GetKey.getKey(Sc_MyApplication.getInstance().getContext2())){
					Post();
					return;
				}else{
					retSrc = "-1000";
				}
			}
			// // 生成 JSON 对象
			checkresponse(retSrc);
			if (params != null) {
				params.clear();
				params = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("servermsg", "服务器无法响应！");
			msg.what = -1;
			data.putInt("type", type);
			msg.setData(data);
			sc_MyHandler.getInstance().sendMessage(msg);
		}

	
	}

	private List<BasicNameValuePair> encodeParams(
			List<BasicNameValuePair> params2) throws Exception {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		String key = DESKeyGenerator.initKey("123");
		String key  = Sc_MyApplication.getInstance().getSecretclient();
		for (BasicNameValuePair par : params2) {
			byte[] inputData = par.getValue().getBytes("utf-8");
			inputData = sc_DESKeyGenerator.encrypt(inputData, key);
			params.add(new BasicNameValuePair(par.getName(), new BASE64Encoder().encode(inputData)));
		}
		params.add(new BasicNameValuePair("encryption", 1 + ""));
		return params;
	}

	// 得到服务器响应数据
	private String connServerForResult(HttpPost request) {
		// HttpGet对象
		String strResult = "-1000";
		try {
			// 获得HttpResponse对象
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				System.out.println("服务器请求完毕后的返回值为----"+"出现-404");
				reponse = "-404";
				return "-404";
			}
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的数据
				String outputStr = EntityUtils.toString(httpResponse.getEntity());
				String key = Sc_MyApplication.getInstance().getSecretsystem();
				byte[] outputData = sc_AESKeyGenerator.decrypt(new BASE64Decoder().decodeBuffer(outputStr), key);
				strResult = new String(outputData,"utf-8");
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
			sc_VisiteTimes2.getInstance().count();
			if(sc_VisiteTimes2.getInstance().isOut()){
				sc_MyHandler.getInstance().sendEmptyMessage(-9);
				
			}else{
				if(sc_VisiteTimes2.getInstance().isInit()){
					sc_VisiteTimes2.getInstance().init();
				}
			}
			e.printStackTrace();
			System.out.println("服务器请求完毕后的返回值为----"+"获取响应值有问题");
			reponse = "-1000";
			return "-1000";
		}
		return strResult;
	}

	private void checkresponse(String token) {
		System.out.println("服务器请求完毕后的返回值为----"+token);
		Sc_MyApplication.switch_tag = true;
		if (token.endsWith("}")) {
			Sc_MyApplication.getInstance().setResponseword(token);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putInt("type", type);
			data.putString("response", token);
			msg.what = 0;
			msg.setData(data);
			sc_MyHandler.getInstance().sendMessage(msg);
		} else if (token.endsWith("]")) {
			Sc_MyApplication.getInstance().setResponseword(token);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("response", token);
			data.putInt("type", type);
			msg.what = 0;
			msg.setData(data);
			sc_MyHandler.getInstance().sendMessage(msg);
		} else if (token.contains(",")) {
			Sc_MyApplication.getInstance().setResponseword(token);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("response", token);
			data.putInt("type", type);
			msg.what = 0;
			msg.setData(data);
			sc_MyHandler.getInstance().sendMessage(msg);
		} else if (token.equals("")) {
			Sc_MyApplication.getInstance().setResponseword(token);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("servermsg", "数据交互失败！");
			data.putInt("type", type);
			msg.what = -1;
			msg.setData(data);
			sc_MyHandler.getInstance().sendMessage(msg);
		}	else if (token.equals("null")) {
			Sc_MyApplication.getInstance().setResponseword(token);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("servermsg", "数据交互失败！");
			data.putInt("type", type);
			msg.what = -1;
			msg.setData(data);
			sc_MyHandler.getInstance().sendMessage(msg);
		} else if (Integer.parseInt(token) >= 0) {
			Sc_MyApplication.getInstance().setResponseword(token);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("response", token);
			data.putInt("type", type);
			msg.what = 0;
			msg.setData(data);
			sc_MyHandler.getInstance().sendMessage(msg);
		} else {
			int error = Integer.parseInt(token);
			Message msg = new Message();
			Bundle data = new Bundle();
//			if (error == -1) {
//				data.putString("servermsg", "用户名无效！");
//			} else if (error == -2) {
//				data.putString("servermsg", "密码无效！");
//			} else if (error == -3) {
//				data.putString("servermsg", "用户名或密码错误！");
//			} else if (error == -4) {
//				data.putString("servermsg", "手机号码无效！");
//			} else if (error == -5) {
//				data.putString("servermsg", "经度参数为空！");
//			} else if (error == -6) {
//				data.putString("servermsg", "经度参数不正确！");
//			} else if (error == -7) {
//				data.putString("servermsg", "纬度参数为空！");
//			} else if (error == -8) {
//				data.putString("servermsg", "纬度参数不正确！");
//			} else if (error == -9) {
//				data.putString("servermsg", "用户id为空！");
//			} else if (error == -10) {
//				data.putString("servermsg", "用户id参数不正确！");
//			} else if (error == -11) {
//				data.putString("servermsg", "时间参数为空！");
//			} else if (error == -12) {
//				data.putString("servermsg", "签到类型为空！");
//			} else if (error == -13) {
//				data.putString("servermsg", "企业id为空！");
//			} else if (error == -14) {
//				data.putString("servermsg", "企业id参数错误！");
//			} else if (error == -15) {
//				data.putString("servermsg", "任务编号为空！");
//			} else if (error == -16) {
//				data.putString("servermsg", "任务编号参数不正确！");
//			} else if (error == -17) {
//				data.putString("servermsg", "标题为空！");
//			} else if (error == -18) {
//				data.putString("servermsg", "任务开始时间为空！");
//			} else if (error == -19) {
//				data.putString("servermsg", "任务开始时间参数格式错误！");
//			} else if (error == -20) {
//				data.putString("servermsg", "任务结束时间为空！");
//			} else if (error == -21) {
//				data.putString("servermsg", "任务结束时间参数格式不正确！");
//			} else if (error == -22) {
//				data.putString("servermsg", "客户id为空！");
//			} else if (error == -23) {
//				data.putString("servermsg", "客户的id参数格式错误！");
//			} else if (error == -24) {
//				data.putString("servermsg", "状态为空！");
//			} else if (error == -25) {
//				data.putString("servermsg", "状态的参数不正确！");
//			} else if (error == -26) {
//				data.putString("servermsg", "提醒时间转化成毫秒数异常！");
//			} else if (error == -27) {
//				data.putString("servermsg", "客户姓名为空！");
//			}else if (error == -998) {
//				data.putString("servermsg", "操作失败！");
//			} else if (error == -999) {
//				data.putString("servermsg", "操作失败！");
//			} else if (error == -404) {
//				data.putString("servermsg", "找不到网址！");
//			} else if (error == -1000) {
//				data.putString("servermsg", "服务器连接超时，请稍后重试！");
//			}else {
//				data.putString("servermsg", "服务器连接超时，请稍后重试！");
//			}
			data.putString("servermsg", error+"");
			msg.what = -1;
			data.putInt("type", type);
			msg.setData(data);
			sc_MyHandler.getInstance().sendMessage(msg);
		}
	}
	
	/**
	 * 获取相应值
	 */
	
	public String getReponse(){
		return reponse;
	}
}
