package com.zhihuigu.sosoOffice.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

import com.zhihuigu.sosoOffice.constant.MyApplication;

import android.content.Context;

public class SoSoDownloadData {
	/**
	 * author by Ring ����ͼƬ����
	 */

	// �����������Ķ�������
	private String action = "";
	// �ϴ����ݲ����б�
	private List<BasicNameValuePair> params = null;
	// ��������Ӧֵ
	private String reponse = "";
	// ͼƬ�����ַ
	private File file = null;
	// ������
	private Context context = null;

	// ����������
	private HttpPost request = null;

	public SoSoDownloadData(Context context, String action,
			List<BasicNameValuePair> params, File file) {
		this.params = params;
		this.file = file;
		this.context = context;
		this.action = action;
	}

	/***
	 * ��������ύ����
	 */
	public void Post() {
		System.out.println("��������ʼ����ʱ��url---"
				+ MyApplication.getInstance(context).getURL() + action);
		System.out.println("��������ʼ����ʱ�Ĳ����б�---" + params);
		request = new HttpPost(MyApplication.getInstance(context).getURL()
				+ action);
		HttpParams httpParameters = new BasicHttpParams();
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					1000 * 60 * 2);
			HttpConnectionParams.setSoTimeout(httpParameters, 1000 * 60 * 2);
			request.setParams(httpParameters);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			this.reponse = "-1000";
		}
		reponse = connServerForResult(request);
		params.clear();
		params = null;
		System.out.println("��������ӦֵΪ---" + reponse);
	}

	/***
	 * ��������ύ����
	 * 
	 * @param request
	 * @return
	 */
	private String connServerForResult(HttpPost request) {
		InputStream in = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			// ���ӳ�ʱ
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 1000 * 10);
			// ����ʱ
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 10 * 1000);
			HttpResponse httpResponse = httpClient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return "-404";
			}
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				in = httpResponse.getEntity().getContent();
				if (file.exists() && file.isDirectory()) {
					file.delete();
				}
				FileOutputStream fileout = new FileOutputStream(file);
				int tem = 0;
				byte[] bytes = new byte[1024];
				while ((tem = in.read(bytes)) != -1) {
					fileout.write(bytes, 0, tem);
					fileout.flush();
				}
				in.close();
				fileout.close();
				in = null;
				fileout = null;
				return "0";
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "-201314";
		}
	}

	/***
	 * ȡ�÷�������ȡ����Ӧֵ
	 */
	public String getReponse() {
		return reponse;
	}

	/***
	 * �ж�����
	 */
	public void overReponse() {
		if(request!=null&&!request.isAborted()){
			request.abort();
		}
	}
}
