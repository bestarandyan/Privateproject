/**
 * 
 */
package com.qingfengweb.filedownload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qingfengweb.imagehandle.PicHandler;

/**
 * @author ������
 * @createDate 2013/12/19
 *
 */
public class FileDownload {
	/**
	 * ͨ���ļ�ID���ط�����������ļ�
	 * @param imageUrl
	 * @param toSD
	 * @param context
	 * @param defId
	 * @return
	 */
	public static int downloadFileFromId(Context context,String interfaceStr,List<NameValuePair> list,String path,String fileName){
		try {
			HttpPost httpPost = new HttpPost(interfaceStr);
			HttpEntity httpEntity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
			httpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
//			String msgString = EntityUtils.toString(entity);
//			if(msgString!=null && msgString.equals("404")){
//				return 0;
//			}else{
				InputStream isInputStream = entity.getContent();
				FileUtils.write2SDFromInput(path, fileName, isInputStream);
				isInputStream.close();
				return 1;
//			}
		} catch (IOException e) {
		}
		return 0;
	}
}
