/**
 * 
 */
/**
 * 
 */
package com.qingfengweb.FileUpLoad;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import android.content.Context;
/**
 * @author qingfeng
 *
 */
public class HttpPostUploadFile {
	public static boolean change = false;
	public static int reponsecode = 0;
	public static String post(Context context,String actionUrl, Map<String, String> params,
			Map<String, File> files, int start, int end,String photokey)
			throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "utf-8";
		String reponse = "";
		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(2*60 * 1000); // 缓存的最长时间
		conn.setConnectTimeout(2*60 * 1000);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestProperty("Accept-Encoding", "gzip");
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
		System.setProperty("http.keepAlive", "false");
		// if(change){
		// conn.setRequestProperty("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		if (reponsecode == -16 || reponsecode == -1) {
			conn.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		}
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.setRequestProperty("Accept-Language",
				"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		// }
		conn.setRequestProperty("Charsert", CHARSET);//设置上传编码方式
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);
		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			if (change) {
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			}
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		InputStream in = null;
		// System.out.println("发送文件头文件：\r\n"+sb.toString());
		// 发送文件数据
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\""
						+ photokey + "\"; "+"filename"+"=\""
						+ file.getKey() + "\"" + LINEND);
				sb1.append("Content-Type: " + MULTIPART_FROM_DATA
						+ "; charset=" + CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				in = new FileInputStream(file.getValue());
				byte[] bytes = new byte[256];
				in.skip(start);
				int tem = -1;
				int temlength = 0;
				while ((tem = in.read(bytes)) != -1) {
					if ((end - start - temlength) <= 0) {
						break;
					}
					if ((end - start - temlength) < 256) {
						outStream.write(bytes, 0, (end - start - temlength));
						break;
					}
					temlength += tem;
					outStream.write(bytes, 0, tem);
					outStream.flush();
				}
				in.close();
				outStream.write(LINEND.getBytes());
				in = null;
			}
			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// System.out.println("等待获取响应");
			// 得到响应码
			int res = conn.getResponseCode();
			 System.out.println("获取的响应码为："+res);
			if (res == 200) {
				String contentEncoding = conn.getContentEncoding();
				System.out.println("服务器返回的数据的编码方式为："+contentEncoding);
				if(contentEncoding!=null && contentEncoding.equals("gzip")){
					reponse = getContentFromIn(conn,CHARSET);
					System.out.println(reponse);
				}else{
					in = conn.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buff = new byte[1024];
					int ch;
					while ((ch = in.read(buff)) != -1) {
						baos.write(buff, 0, ch);
					}
					reponse = baos.toString(CHARSET);
					System.out.println("上传文件的最终返回值 =="+reponse);
				}
			}
			outStream.close();
			conn.disconnect();
		}
		
		return reponse;
	}
	
	public static String getContentFromIn(HttpURLConnection urlConn, String charset) {
        BufferedReader br = null;
        StringBuilder content = new StringBuilder(200);
        InputStream in = null;
        try {
            if(null == urlConn){
                return "";
            }           
//            if (StringTools.isNotEmpty(urlConn.getContentEncoding())) {
                String encode = urlConn.getContentEncoding().toLowerCase();
                if (/*StringTools.isNotEmpty(encode) && */encode.indexOf("gzip") >= 0) {
                    in = new GZIPInputStream(urlConn.getInputStream());                   
                }
//            }

            if (null == in) {
                in = urlConn.getInputStream();
            }
            if (null != in) {
                br = new BufferedReader(new InputStreamReader(in, charset));
                String line = "";
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
        }
        return content.toString();
    } 

}
