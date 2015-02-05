package com.chinaLife.claimAssistant.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class sc_UploadFile2 {

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static String post(String actionUrl, List<BasicNameValuePair> params,
			Map<String, File> files, int start, int end, String legendid)
			throws IOException {

		// String BOUNDARY = java.util.UUID.randomUUID().toString();
		// String PREFIX = "--", LINEND = "\r\n";
		// String MULTIPART_FROM_DATA = "multipart/form-data";
		// String CHARSET = "UTF-8";
		// URL uri = new URL(actionUrl);
		// HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		// conn.setReadTimeout(5 * 1000); // 缓存的最长时间
		// conn.setConnectTimeout(20*1000);
		// conn.setRequestMethod("POST");
		// conn.setDoInput(true);// 允许输入
		// conn.setDoOutput(true);// 允许输出
		// conn.setUseCaches(false); // 不允许使用缓存
		// conn.setRequestProperty(
		// "User-Agent",
		// "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
		// System.setProperty("http.keepAlive", "false");
		// conn.setRequestProperty("Charsert", "UTF-8");
		// conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
		// + ";boundary=" + BOUNDARY);
		// // 首先组拼文本类型的参数
		// StringBuilder sb = new StringBuilder();
		// for (Map.Entry<String, String> entry : params.entrySet()) {
		// sb.append(PREFIX);
		// sb.append(BOUNDARY);
		// sb.append(LINEND);
		// sb.append("Content-Disposition: form-data; name=\""
		// + entry.getKey() + "\"" + LINEND);
		// sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
		// sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
		// sb.append(LINEND);
		// sb.append(entry.getValue());
		// sb.append(LINEND);
		// }
		// DataOutputStream outStream = new DataOutputStream(
		// conn.getOutputStream());
		// outStream.write(sb.toString().getBytes());
		// InputStream in = null;
		// // 发送文件数据
		// if (files != null) {
		// for (Map.Entry<String, File> file : files.entrySet()) {
		// StringBuilder sb1 = new StringBuilder();
		// sb1.append(PREFIX);
		// sb1.append(BOUNDARY);
		// sb1.append(LINEND);
		// sb1.append("Content-Disposition: form-data; name=\""
		// + file.getValue().getName() + "\"; filename=\""
		// + file.getKey() + "\"" + LINEND);
		// sb1.append("Content-Type: " + MULTIPART_FROM_DATA
		// + "; charset=" + CHARSET + LINEND);
		// sb1.append(LINEND);
		// outStream.write(sb1.toString().getBytes());
		// in = new FileInputStream(file.getValue());
		// byte[] bytes = new byte[1024];
		// in.skip(start);
		// int tem = -1;
		// int temlength = 0;
		// while ((tem = in.read(bytes)) != -1) {
		// if((end - start - temlength) <= 0){
		//
		// break;
		// }
		// if ((end - start - temlength) < 1024) {
		// outStream.write(bytes, 0, (end - start - temlength));
		// break;
		// }
		// temlength += tem;
		// outStream.write(bytes, 0, tem);
		// outStream.flush();
		//
		// }
		// in.close();
		// outStream.write(LINEND.getBytes());
		// in = null;
		// }
		// // 请求结束标志
		// byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		// outStream.write(end_data);
		// outStream.flush();
		// //System.out.println("等待获取响应");
		// // 得到响应码
		// int res = conn.getResponseCode();
		// //System.out.println("获取的响应码为："+res);
		// if (res == 200) {
		// in = conn.getInputStream();
		// int ch;
		// StringBuilder sb2 = new StringBuilder();
		// while ((ch = in.read()) != -1) {
		// sb2.append((char) ch);
		// }
		// reponse = sb2.toString();
		// if(reponse.equals("0")){
		// //更新进度条
		// Message msg = new Message();
		// Bundle data = new Bundle();
		// data.putString("legendid", legendid);
		// msg.what = -4;
		// data.putInt("position", end);
		// msg.setData(data);
		// MyHandler.getInstance().sendMessage(msg);
		// }
		// }
		// outStream.close();
		// conn.disconnect();
		// }
		InputStream in = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(actionUrl);
//		//创建multiPart实体
//		MultiPartEntity entity=new MultiPartEntity();
//		//把参数和文件都装进去
//		entity.addPart(“name”,new StringBody(“veikr.com”));
//		entity.addPart(“file”,new FileBody(file))
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		// 发送文件数据
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {

				in = new FileInputStream(file.getValue());
				byte[] bytes = new byte[1024];
				in.skip(start);
				int tem = -1;
				int temlength = 0;
				while ((tem = in.read(bytes)) != -1) {
					if ((end - start - temlength) <= 0) {

						break;
					}
					if ((end - start - temlength) < 1024) {
						localByteArrayOutputStream.write(bytes, 0,
								(end - start - temlength));
						break;
					}
					temlength += tem;
					localByteArrayOutputStream.write(bytes, 0, tem);
					localByteArrayOutputStream.flush();

				}
				in.close();
				in = null;
			}
		}
		byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
		ByteArrayEntity localByteArrayEntity = new ByteArrayEntity(arrayOfByte);
		httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		httppost.setEntity(localByteArrayEntity);
		HttpResponse response;
		response = httpclient.execute(httppost);
		// 检验状态码，如果成功接收数据
		int code = response.getStatusLine().getStatusCode();
		String rev = "返回值";
		if (code == 200) {
			rev = EntityUtils.toString(response.getEntity());
		}
		localByteArrayOutputStream.close();
		localByteArrayOutputStream = null;
		httppost.abort();
		httpclient = null;
		httppost = null;
		return rev;
	}
}
