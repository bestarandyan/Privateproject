package com.qingfengweb.utils;

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
//		//�õ���ǰ�ⲿ�洢�豸��Ŀ¼
//		// /SDCARD
//		SDPATH = Environment.getExternalStorageDirectory() + "/";
//	}
	/**
	 * ��SD���ϴ����ļ�
	 * 
	 * @throws IOException
	 */
	public static File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}
	
	/**
	 * ��SD���ϴ���Ŀ¼
	 * 
	 * @param dirName
	 */
	public static File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * �ж�SD���ϵ��ļ����Ƿ����
	 */
	public static boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}
	
	/**
	 * ��һ��InputStream���������д�뵽SD����
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
	 * ���ݵ�ǰ���ڼ�ʱ�䶨��ͼƬ�����֣���ȷ��ÿһ���������Ƭ�����ֶ���һ��
	 * 
	 * @author ������
	 * @return ͼƬ������
	 */
	public static String getFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("'img'yyyyMMddHHmmss");
		return sdf.format(date) + ".jpg";
	}
}