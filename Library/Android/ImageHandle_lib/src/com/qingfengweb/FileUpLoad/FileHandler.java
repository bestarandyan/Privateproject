/**
 * 
 */
package com.qingfengweb.FileUpLoad;

import java.io.IOException;

/**
 * @author 刘星星
 * @createDate 文件处理
 *
 */
public class FileHandler {
	/**
	 * 将文件转成字节数组
	 * @param filePath
	 * @return
	 */
	public static byte[] fileToByte(String filePath){
		byte[] fileByte = null;
		try {
			java.io.RandomAccessFile ras = new java.io.RandomAccessFile(filePath,"r");
	        int total = (int)ras.length();
	        System.out.println(total);
	        fileByte = new byte[total];
	        int p=0;
	        while (total-p>0){
	            p+=ras.read(fileByte,p,total-p);
	        }
	        ras.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileByte;
    }
}
