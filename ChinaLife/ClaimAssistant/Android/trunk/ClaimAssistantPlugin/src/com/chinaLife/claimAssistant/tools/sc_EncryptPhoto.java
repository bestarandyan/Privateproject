package com.chinaLife.claimAssistant.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class sc_EncryptPhoto {

	
	public static String key = "jaskfjioajdflkafiwerfd";
	
	/**
	 * 解密
	 */
	public static Bitmap decode(File file){
		Bitmap bitmap = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] b = new byte[key.getBytes().length];
			in.read(b);
			bitmap = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}
		
	public static void Encrypt(File file) {
		File newFile = new File(file.getParent()+"temp.jpg");
		FileOutputStream out = null;
		FileInputStream in = null;
		try {
			if (newFile.exists() && newFile.isDirectory()) {
				newFile.delete();
			}
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			out = new FileOutputStream(newFile);
			in = new FileInputStream(file);
			out.write(key.getBytes());
			int tem = 0;
			byte[] bytes = new byte[1024];
			while ((tem = in.read(bytes)) != -1) {
				out.write(bytes, 0, tem);
				out.flush();
			}
			file.delete();
			newFile.renameTo(file);
		} catch (Exception e) {
		}finally{
			if(out!=null){
				try {
					out.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}