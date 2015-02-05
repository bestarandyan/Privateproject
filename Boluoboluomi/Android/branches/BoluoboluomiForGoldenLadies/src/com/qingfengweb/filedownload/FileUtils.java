package com.qingfengweb.filedownload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
	public static  String SDPATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/";//路径   
//	public String getSDPATH() {
//		return SDPATH;
//	}
//	public FileUtils() {
//		//得到当前外部存储设备的目�?
//		// /SDCARD
//		SDPATH = Environment.getExternalStorageDirectory() + "/";
//	}
	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public static File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}
	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public static File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public static boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}
	
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public static File write2SDFromInput(String path,String fileName,InputStream input){
		File oldFile = new File(SDPATH+path+fileName);
		if(oldFile.exists()){
			return oldFile;
		}		
		File file = null;
			creatSDDir(path);
			 BufferedInputStream in = null;
			 BufferedOutputStream out  = null;
			try {
				file = creatSDFile(path + fileName);
			 byte[] buffer = new byte[1024*8];  
		        in = new BufferedInputStream(input, 1024*8);  
		        out  = new BufferedOutputStream(new FileOutputStream(file), 1024*8);  
		        int count =0,n=0;  
		            while((n=in.read(buffer, 0, 1024*8))!=-1){  
		                out.write(buffer, 0, n);  
		                count+=n;  
		            }  
		            out.flush();  
		        } catch (IOException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        }finally{  
		            try {  
		                out.close();  
		            } catch (IOException e) {  
		                // TODO Auto-generated catch block  
		                e.printStackTrace();  
		            }  
		            try {  
		                in.close();  
		            } catch (IOException e) {  
		                // TODO Auto-generated catch block  
		                e.printStackTrace();  
		            }  
		        }  
		return file;
	}
	/**
	 * 根据当前日期加时间定义图片的名字，可确保每一次拍摄的照片的名字都不一�?
	 * 
	 * @author 刘星�?
	 * @return 图片的名�?
	 */
	public static String getFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("'img'yyyyMMddHHmmss");
		return sdf.format(date) + ".jpg";
	}
}