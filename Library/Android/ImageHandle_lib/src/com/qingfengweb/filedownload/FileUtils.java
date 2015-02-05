package com.qingfengweb.filedownload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

public class FileUtils {
	public static  String SDPATH = Environment.getExternalStorageDirectory() + "/";

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
		OutputStream output = null;
		try{
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			
			output = new FileOutputStream(file);
			byte buffer [] = new byte[4 * 1024];
			while((input.read(buffer)) != -1){
				output.write(buffer);
			}
			output.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				output.close();
			}
			catch(Exception e){
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