/**
 * 
 */
package com.qingfengweb.filehandler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author 刘星星
 * @createDate 2013、12 ，20
 *
 */
public class FileUtils {
	InputStream String2InputStream(String str){
		   ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		   return stream;
		}
	
	String inputStream2String(InputStream is){
		   BufferedReader in = new BufferedReader(new InputStreamReader(is));
		   StringBuffer buffer = new StringBuffer();
		   String line = "";
		   try {
			while ((line = in.readLine()) != null){
			     buffer.append(line);
			   }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return buffer.toString();
		}
	/**
	 * 文件转换成为输入流
	 * @param file
	 * @return
	 */
	public static InputStream file2InputStream(File file){
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;
	}
	/**
	 * 输入流转成文件
	 * @param ins
	 * @param file
	 */
	public void inputstreamtofile(InputStream ins,File file){
		OutputStream os;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
}
