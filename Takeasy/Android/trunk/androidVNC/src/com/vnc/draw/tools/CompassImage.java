package com.vnc.draw.tools;

import java.io.File;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

public class CompassImage {
	public Context c;
	
	public CompassImage(Context c) {
		// TODO Auto-generated constructor stub
		this.c = c;
	}
	public static final String imageDir = "/DCIM/Takeasy/";
	public static Bitmap scaleImg(Bitmap bm, float newWidth, float newHeight) {
	    // 图片源
	    // Bitmap bm = BitmapFactory.decodeStream(getResources()
	    // .openRawResource(id));
	    // 获得图片的宽高
		Bitmap newbm = null;
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    // 设置想要的大小
	    float newWidth1 = newWidth;
	    float newHeight1 = newHeight;
	  /*  if(width<newWidth1 && height <newHeight1){
	    	return bm;
	    }*/
	 // 计算缩放比例
	    if(height > width){
	    	float scaleHeight = ((float) newHeight1) / height;
	    	newWidth1 = ((float)(width*(float)newHeight1)/height);
	    	float scaleWidth = ((float) newWidth1) / width;
	    	 // 取得想要缩放的matrix参数
		    Matrix matrix = new Matrix();
		    matrix.postScale(scaleWidth, scaleHeight);
		    // 得到新的图片
		     try{
			    	if(newbm!=null){
			    		newbm.recycle();
			    		newbm = null;
			    	}
			    newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
			    }catch(Exception e){
			    	
			    }
			    Bitmap bmp = newbm;
		    return bmp;
	    }else{
	    	float scaleWidth = ((float) newWidth1) / width;
	    	newHeight1 = ((float)(height*(float)newWidth1)/width);
	    	float scaleHeight = ((float) newHeight1) / height;
	    	 // 取得想要缩放的matrix参数
		    Matrix matrix = new Matrix();
		    matrix.postScale(scaleWidth, scaleHeight);
		    // 得到新的图片
		    try{
		    	if(newbm!=null){
		    		newbm.recycle();
		    		newbm = null;
		    	}
		    newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
		    }catch(Exception e){
		    	
		    }
		    Bitmap bmp = newbm;
		    return bmp;
	    }
	   }
    public static int computeSampleSize(BitmapFactory.Options options,int minSideLength,int maxNumOfPixels) {
        int  initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);  
        int  roundedSize;
        if  (initialSize <=  8 ) {
                roundedSize =  1 ;
                while  (roundedSize < initialSize) {
                    roundedSize <<=  1 ;
                		}
            }else{
                roundedSize = (initialSize +  7 ) /  8  *  8 ;
             }
            return  roundedSize;
    }   			
    public  static  int  computeInitialSampleSize(BitmapFactory.Options options,
            int  minSideLength,  int  maxNumOfPixels) {
            double  w = options.outWidth;
            double  h = options.outHeight;
            int  lowerBound = (maxNumOfPixels == - 1 ) ?  1  :
                    ( int ) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
            int  upperBound = (minSideLength == - 1 ) ?  128  :
                    ( int ) Math.min(Math.floor(w / minSideLength),
                    Math.floor(h / minSideLength));
             if  (upperBound < lowerBound) {
               // return the larger one when there is no overlapping zone.
                 return  lowerBound;
             	}
             if  ((maxNumOfPixels == - 1 ) &&	(minSideLength == - 1 )) {
            	 return  1 ;
             	}else  if(minSideLength == - 1 ) {
                   return  lowerBound;
             	}else{
                   return  upperBound;
             	}
       }   
    public static String getStrokeFileName()//给文件起名字
    {
        String strFileName = "";
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONDAY);
        int date = rightNow.get(Calendar.DATE);
        int hour = rightNow.get(Calendar.HOUR);
        int minute = rightNow.get(Calendar.MINUTE);
        int second = rightNow.get(Calendar.SECOND);
        strFileName = String.format("%02d%02d%02d%02d%02d%02d.jpg", year, month, date, hour, minute, second);
        return strFileName;
    }
    public static String getStrokeFilePath(String path)
    {
        File sdcarddir = android.os.Environment.getExternalStorageDirectory();
        String strDir = sdcarddir.getPath() + imageDir+path+"/";
        String strFileName = getStrokeFileName();
        File file = new File(strDir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        String strFilePath = strDir + strFileName;
        return strFilePath;
    }
    public static boolean createFilePath(String path){
    	File sdcarddir = android.os.Environment.getExternalStorageDirectory();
        String strDir = sdcarddir.getPath() + imageDir+path;
    	File file = new File(strDir);
    	if(file.exists()){
    		return true;
    	}else{
    		return file.mkdirs();
    	}
    }
    public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}
}
