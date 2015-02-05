package com.chinaLife.claimAssistant.tools;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import com.chinaLife.claimAssistant.activity.Sc_MyApplication;

public class sc_IOPro {
	/**
	 * 用当前时间给拍摄的图片命名
	 * 
	 * @author  刘星星
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'img'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	/**  
     * 获得指定文件的byte数组  
     */  
    public static byte[] getBytes(String filePath){   
        byte[] buffer = null;   
        try {   
            File file = new File(filePath);   
            FileInputStream fis = new FileInputStream(file);   
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);   
            byte[] b = new byte[1000];   
            int n;   
            while ((n = fis.read(b)) != -1) {   
                bos.write(b, 0, n);   
            }   
            fis.close();   
            bos.close();   
            buffer = bos.toByteArray();   
        } catch (FileNotFoundException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
        return buffer;   
    }   
    /***
     * 压缩GZip
     * 
     * @param data
     * @return
     */
    public static byte[] gZip(byte[] data) {
     byte[] b = null;
     try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      GZIPOutputStream gzip = new GZIPOutputStream(bos);
      gzip.write(data);
      gzip.finish();
      gzip.close();
      b = bos.toByteArray();
      bos.close();
     } catch (Exception ex) {
      ex.printStackTrace();
     }
     return b;
    }
    /**  
     * 根据byte数组，生成文件  
     */  
    public static  void getFile(byte[] bfile, String filePath,String fileName) {   
    //	bfile = gZip(bfile);
//    	Bitmap bitmap = BitmapCache.getInstance().getBitmap(bfile, context, opts)
        BufferedOutputStream bos = null;   
        FileOutputStream fos = null;   
        File file = null;   
        try {   
            File dir = new File(filePath);   
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在   
                dir.mkdirs();   
            }   
            file = new File(filePath+"/"+fileName);   
            fos = new FileOutputStream(file);   
            bos = new BufferedOutputStream(fos);   
            bos.write(bfile);  
            if(file.exists())
				Sc_MyApplication.getInstance().setFile(file);
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally {   
            if (bos != null) {   
                try {   
                    bos.close();   
                } catch (IOException e1) {   
                    e1.printStackTrace();   
                }   
            }   
            if (fos != null) {   
                try {   
                    fos.close();   
                } catch (IOException e1) {   
                    e1.printStackTrace();   
                }   
            }   
        }   
    }   

}
