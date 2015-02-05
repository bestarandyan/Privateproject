package com.zhihuigu.sosoOffice.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.utils.FileTools;
import com.zhihuigu.sosoOffice.utils.MD5;

import android.content.Context;

public class DownloadData {
	/**
	 * author by Ring ����ͼƬ����
	 */

	// url
	private String url = "";
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

	public DownloadData(Context context, String url,
			List<BasicNameValuePair> params, File file) {
		this.params = params;
		this.file = file;
		this.context = context;
		this.url = url;
	}

	/***
	 * ��������ύ����
	 */
	public void Post() {
		System.out.println("��������ʼ����ʱ��url---"+url);
		System.out.println("��������ʼ����ʱ�Ĳ����б�---" + params);
		request = new HttpPost(url);
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
		synchronized (MyApplication.getInstance().getImageDownloaderId()) {

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
					File filetemp = null;
					filetemp = FileTools.getFile(
							context.getResources().getString(
									R.string.root_directory),
									context.getResources().getString(
									R.string.temp), MD5.getMD5(new Date().getTime()+""));
					FileOutputStream fileout = new FileOutputStream(filetemp);
					int tem = 0;
					byte[] bytes = new byte[1024];
					while ((tem = in.read(bytes)) != -1) {
						try {
							fileout.write(bytes, 0, tem);
						} catch (Exception e) {
							return "-1000";
						}
						fileout.flush();
					}
					try {
						fileout.close();
					} catch (Exception e) {
						return "-1000";
					}
					try {
						in.close();
					} catch (Exception e) {
						return "-1000";
					}
					in = null;
					fileout = null;
					boolean b;
					b =FileTools.copyFile(filetemp.getAbsolutePath(), file.getAbsolutePath());
					if (filetemp.exists()) {
						filetemp.delete();
					}
					if (file.exists() && file.isFile()) {
						return "0";
					}
					
				}
				return "";
			} catch (Exception e) {
				e.printStackTrace();
				return "-1000";
			}
		
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
