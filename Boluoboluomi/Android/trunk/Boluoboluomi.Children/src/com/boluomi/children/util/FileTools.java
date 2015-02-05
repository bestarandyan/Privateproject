package com.boluomi.children.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;

public class FileTools {
	/***
	 * author Ring
	 */
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "";

	/***
	 * 获取文件
	 * 
	 * @param str1文件名称
	 * @return
	 */
	public static File getFile(String filename) {
		File file = new File(SDPATH + "/" + filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 获取文件
	 * 
	 * @param str1父类文件夹
	 * @param str2子文件夹
	 * @return
	 */
	public static File getFile(String str1, String str2, String filename) {
		File file = new File(SDPATH + "/" + str1 + "/" + str2);
		if (!file.exists()) {
			file.mkdirs();
		}
		File file2 = new File(file, filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file2;
	}

	/**
	 * 是否有sd卡
	 */
	public static boolean isSDExist() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断文件是否存在
	 */
	public static boolean isFileExist(String path) {
		try {
			File file = new File(path);
			if (file.exists()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				File filetemp = new File(newPath);
				FileOutputStream fs = new FileOutputStream(filetemp);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
					fs.flush();
				}
				try {
					fs.close();
				} catch (Exception e) {
					return false;
				}
				try {
					inStream.close();
				} catch (Exception e) {
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");

		}
		return false;

	}

}
