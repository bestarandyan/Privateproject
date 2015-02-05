/**
 * 
 */
package com.zhihuigu.sosoOffice.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.util.Log;

/**
 * @author ������
 * @createDate 2013/1/19 �ļ�������
 * 
 */
public class FileCopyUtil {
	public static void copyfile(File fromFile, File toFile,Boolean rewrite ){
		if (!fromFile.exists()) {
			return;
		}
		if (!fromFile.isFile()) {
			return ;
		}
		if (!fromFile.canRead()) {
			return ;
		}
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists() && rewrite) {
			toFile.delete();
		}
// if (!toFile.canWrite()) {
// MessageDialog.openError(new Shell(),"������Ϣ","���ܹ�д��Ҫ���Ƶ�Ŀ���ļ�" + toFile.getPath());
// Toast.makeText(this,"���ܹ�д��Ҫ���Ƶ�Ŀ���ļ�", Toast.LENGTH_SHORT);
// return ;
// }
		try {
			java.io.FileInputStream fosfrom = new java.io.FileInputStream(fromFile);
			java.io.FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c); //������д�����ļ�����
			}
			fosfrom.close();
			fosto.close();
		} catch (Exception ex) {
				Log.e("readfile", ex.getMessage());
		}
	}
}
