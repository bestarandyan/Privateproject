package com.zhihuigu.sosoOffice.thread;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
public class SoSoUploadFile {
	/**
	 * ͨ��ƴ�ӵķ�ʽ�����������ݣ�ʵ�ֲ��������Լ��ļ�����
	 * 
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @return
	 * @throws IOException
	 */
	
	private  boolean stop = false;
	private HttpURLConnection conn = null;
	private String reponse = "��ʼֵ";
	public void post(String actionUrl, Map<String, String> params,
			Map<String, File> files) throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		System.out.println("�����������ַ"+actionUrl);
		URL uri = new URL(actionUrl);
		conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(60 * 2000); // ������ʱ��
		conn.setConnectTimeout(60*2000);
		conn.setDoInput(true);// ��������
		conn.setDoOutput(true);// �������
		conn.setUseCaches(false); // ������ʹ�û���
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);
		// ������ƴ�ı����͵Ĳ���
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET
					+ LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		InputStream in = null;
		// �����ļ�����
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\""
						+ file.getValue().getName() + "\"; filename=\""
						+ file.getKey() + "\"" + LINEND);
				sb1.append("Content-Type: " + MULTIPART_FROM_DATA
						+ "; charset=" + CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				in = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					if(stop){
						stop = false;
						reponse = "��ʼ��";
					}
					outStream.write(buffer, 0, len);
				}
				in.close();
				outStream.write(LINEND.getBytes());
				in = null;
			}
			// ���������־
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND)
					.getBytes();
			outStream.write(end_data);
			outStream.flush();
			// �õ���Ӧ��
			int res = conn.getResponseCode();
			if (res == 200) {
				in = conn.getInputStream();
				int ch;
				StringBuilder sb2 = new StringBuilder();
				while ((ch = in.read()) != -1) {
					sb2.append((char) ch);
				}
				reponse = sb2.toString();
			}
			outStream.close();
			conn.disconnect();
			conn = null;
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
	public  void overReponse() {
		if(conn!=null){
			conn.disconnect();
		}
	}

}
