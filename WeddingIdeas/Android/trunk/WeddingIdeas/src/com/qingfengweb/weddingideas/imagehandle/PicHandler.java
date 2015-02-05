package com.qingfengweb.weddingideas.imagehandle;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

public class PicHandler {
	/**
	 * 检查存储卡是否可用
	 * 
	 * @author 刘星星
	 * @Data 2012年1月6日
	 */
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据当前日期加时间定义图片的名字，可确保每一次拍摄的照片的名字都不一样
	 * 
	 * @author 刘星星
	 * @return 图片的名字
	 */
	public static String getFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("'img'yyyyMMddHHmmss");
		return sdf.format(date) + ".jpg";
	}
	
	/**
	 * 根据路径获取图片 ，并压缩图片
	 * 
	 * @param pathName
	 *            图片路径
	 * @return
	 */
	public static Bitmap getDrawable(String pathName, ImageView imageView) {
		File file = new File(pathName);
		if(!file.exists()){
			return null;
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bmp = null;
		try {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeFile(pathName, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 1000 * 1000);
			opts.inJustDecodeBounds = false;
			if (imageView != null) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView
						.getDrawable();
				if (bitmapDrawable != null
						&& !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
			}
			// 如果图片还未回收，先强制回收该图片
			bmp = BitmapFactory.decodeFile(pathName, opts);
			bmp = RotateImg(pathName, bmp);
			if (bmp != null){
				bmp = scaleImg(bmp, bmp.getWidth(), bmp.getHeight());
			}else{
				return null;
			}
				
		} catch (OutOfMemoryError e) {
			bmp = BitmapFactory.decodeFile(pathName, opts);
			bmp = scaleImg(bmp, 800, 600);
		}

		return bmp;
	}
		/**
		 * 获得指定文件的byte数组
		 */
		public static byte[] fileToByte(String filePath){
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
		public static byte[] fileToByte1(String filePath){
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
		/**
		 * 根据byte数组，生成文件
		 * @param filePath 文件路径
		 * @param 文件名称
		 * @author 刘星星
		 */
		public static void byteToFile(byte[] bfile, String filePath,String fileName) {
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = null;
			try {
				File dir = new File(filePath);
				if(!dir.exists()){//判断文件目录是否存在
					dir.mkdirs();
				}
				file = new File(filePath+"/"+fileName);
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				bos.write(bfile);
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
	/**
	 * 根据字节数组获取图片 ，并压缩图片
	 * 
	 * @param pathName
	 *            图片路径
	 * @return
	 */
	public static Bitmap getDrawable(byte[] data, View view) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bmp = null;
		try {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeByteArray(data, 0, 0, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 1000 * 1000);
			opts.inJustDecodeBounds = false;
			if (view != null) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) view.getBackground();
				if (bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
			}
			// 如果图片还未回收，先强制回收该图片
			bmp = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
			if (bmp != null){
				bmp = scaleImg(bmp, bmp.getWidth(), bmp.getHeight());
			}else{
				return null;
			}
				
		} catch (OutOfMemoryError e) {
			bmp = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
			bmp = scaleImg(bmp, 800, 600);
		}

		return bmp;
	}
	
	
	/**
	 * 根据输入流获取图片 ，并压缩图片
	 * 
	 * @param pathName
	 *            图片路径
	 * @return
	 */
	public static Bitmap getDrawable(InputStream is, View view,Rect rect) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bmp = null;
		try {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
			BitmapFactory.decodeStream(is, rect, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 1000 * 1000);
			opts.inJustDecodeBounds = false;
			if (view != null) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) view.getBackground();
				if (bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
			}
			// 如果图片还未回收，先强制回收该图片
			bmp = BitmapFactory.decodeStream(is, rect, opts);
			if (bmp != null){
				bmp = scaleImg(bmp, bmp.getWidth(), bmp.getHeight());
			}else{
				return null;
			}
		} catch (OutOfMemoryError e) {
			bmp = BitmapFactory.decodeStream(is, rect, opts);
			bmp = scaleImg(bmp, 800, 600);
		}
		return bmp;
	}
	public static Bitmap zoomDrawable(BitmapDrawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	public static  Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	
	
	/**
	 * 将倒掉的图片旋转90°
	 * @author 刘星星
	 * @param fileName 文件路径
	 * @param oldBitmap 位图
	 * @return 新的正立的图片
	 */
public static Bitmap RotateImg(String fileUrl,Bitmap oldBitmap){
	ExifInterface exifInterface = null;
	try {
		exifInterface = new ExifInterface(fileUrl);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	int tag = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
	int degree = 0;
	if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
		degree = 90;
	} else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
			degree = 180;
	} else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
				degree = 270;
	}
	if (degree != 0 && oldBitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degree, (float) oldBitmap.getWidth() / 2, (float) oldBitmap.getHeight() / 2);
			try {
				Bitmap bm1 = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), m, true);
				return bm1;
				} catch (OutOfMemoryError ex) {
				}
	}
	return oldBitmap;
}
	/**
	 * 根据路径获取图片 ，并压缩图片
	 * 
	 * @param pathName
	 *            图片路径
	 * @param w
	 *            图片的宽
	 * @param h
	 *            图片的高
	 * @return 大小为150*150的图片
	 * @author 刘星星
	 */
	public static Bitmap getDrawable(String pathName, int w, int h) {
		File file = new File(pathName);
		if(!file.exists()){
			return null;
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bmp = null;
		try {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
			BitmapFactory.decodeFile(pathName, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, w * h);
			opts.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(pathName, opts);
			bmp = RotateImg(pathName, bmp);
			if(bmp!=null){
				if(w == 0 || h == 0){
					bmp = scaleImg(bmp, bmp.getWidth(), bmp.getHeight());
				}else{
					if(bmp.getWidth()>bmp.getHeight()){
						bmp = scaleImg(bmp, w, h);
					}else{
						bmp = scaleImg(bmp, w, h);
					}
					
				}
				
			}else{
				return null;
			}
		} catch (OutOfMemoryError e) {
			bmp = scaleImg(bmp, w, h);
		}
		return bmp;
	}
	/**
	 * 根据路径获取图片 ，并压缩图片
	 * 
	 * @param pathName
	 *            图片路径
	 * @param w
	 *            图片的宽
	 * @param h
	 *            图片的高
	 * @return 大小为150*150的图片
	 * @author 刘星星
	 */
	public static Bitmap getDrawable(Resources resouce,int resId, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bmp = null;
		try {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeResource(resouce, resId, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 900 * 900);
			opts.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeResource(resouce, resId, opts);
			if(bmp!=null){
				if(w == 0 || h == 0){
					bmp = scaleImg(bmp, bmp.getWidth(), bmp.getHeight());
				}else{
					if(bmp.getWidth()>bmp.getHeight()){
						bmp = scaleImg(bmp, w, h);
					}else{
						bmp = scaleImg(bmp, w, h);
					}
				}
			}else{
				return null;
			}
		} catch (OutOfMemoryError e) {
			bmp = scaleImg(bmp, w, h);
		}
		return bmp;
	}
	
	/**
	 *返回一张正方形图片
	 * @author 刘星星
	 */
	public static Bitmap getSquareImg(String pathName, int size) {
		File file = new File(pathName);
		if(!file.exists()){
			return null;
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bmp = null;
		try {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeFile(pathName, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 2000 * 2000);
			opts.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(pathName, opts);
			bmp = RotateImg(pathName, bmp);
			if(bmp!=null){
				if(size == 0){
					bmp = scaleImg(bmp, bmp.getWidth(), bmp.getHeight());
				}else{
						bmp = scaleImg(bmp, size, size);
					
				}
			}else{
				return null;
			}
		} catch (OutOfMemoryError e) {
			bmp = scaleImg(bmp, size, size);
		}
		return bmp;
	}
	
	/**
	 * 从View中得到Bitmap
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view){
	   view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        view.layout(0,0, view.getMeasuredWidth(), view.getMeasuredHeight());
	        view.buildDrawingCache();
	        Bitmap bitmap = view.getDrawingCache();

	 return bitmap;
	}
	
	public static Bitmap cutImg(Bitmap bm){
		int height = bm.getHeight();
		int width = bm.getWidth();
		if (height > width) {
			int x=0;
			int y = (height-width)/2;
			int w = width;
			int h = width*2/3;
			// 得到新的图片
			Bitmap scaleBit = Bitmap.createBitmap(bm, x, y, w, h);
			return scaleBit;
		} else {
			// 得到新的图片
			Bitmap scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height);
			return scaleBit;
		}
	}
	/**
	 * 切一个正方形  尺寸大小为size
	 * 切的是正中间的一部分
	 * @param bm
	 * @param size
	 * @return
	 */
	public static Bitmap cutImg(Bitmap bm,int size){
		int height = bm.getHeight();
		int width = bm.getWidth();
		int x=0;
		int y = 0;
		int w = 0;
		int h = 0;
		if (height > size) {
			if(width > size){
				x=(width-size)/2;
				y=(height-size)/2;
				w=h=size;
			}else{
				x=0;
				y=(height-size)/2;
				w=h=width;
			}
			
		} else {
			if(width > size){
				y=0;
				x=(width-size)/2;
				w=h=height;
			}else{
				y=0;
				x=0;
				w=h=height>width?width:height;
			}
		}
		if(w<=0 || h<=0){
			return bm;
		}
		// 得到新的图片
		Bitmap scaleBit = Bitmap.createBitmap(bm, x, y, w, h);
		return scaleBit;
	}
	
	
	/**
	 * 切一个指定尺寸大小的长方形
	 * 切的是正中间的一部分
	 * @param bm
	 * @param size
	 * @return
	 */
	public static Bitmap cutImg(Bitmap bm,int towidth,int toheight){
		int height = bm.getHeight();
		int width = bm.getWidth();
		int x=0;
		int y = 0;
		int w = 0;
		int h = 0;
		if (height > toheight) {
			if(width > towidth){
				x=(width-towidth)/2;
				y=(height-toheight)/2;
				w = towidth;
				h = toheight;
			}else{
				x=0;
				y=(height-toheight)/2;
				w=width;
				h=toheight;
			}
			
		} else {
			if(width > towidth){
				y=0;
				x=(width-towidth)/2;
				w=towidth;
				h=height;
			}else{
				y=0;
				x=0;
				w=width;
				h=height;
			}
		}
		// 得到新的图片
		Bitmap scaleBit = Bitmap.createBitmap(bm, x, y, w, h);
		return scaleBit;
	}
/**
 * 压缩并剪切图片，图片可能不会完全显示出来
 * @param bm 
 * @param newWidth
 * @param newHeight
 * @return
 * @author 刘星星
 * @createDate 2013/2/20
 */
	public static Bitmap scaleImgFrirst(Bitmap bm, int newWidth, int newHeight) {
		// 图片源
		// Bitmap bm = BitmapFactory.decodeStream(getResources()
		// .openRawResource(id));
		// 获得图片的宽高
		// Bitmap newbm = null;
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		float newWidth1 = newWidth;
		float newHeight1 = newHeight;
		/*
		 * if(width<newWidth1 && height <newHeight1){ return bm; }
		 */
//		int unitSize = MyApplication.getInstance().getScreenWidth() / 2 - 30;
		// 计算缩放比例
		if (height > width) {
			float scaleHeight = ((float) newHeight1) / height;
			newWidth1 = ((float) (width * (float) newHeight1) / height);
			float scaleWidth = ((float) newWidth1) / width;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			Bitmap scaleBit = Bitmap.createBitmap(bm, 0, 0, width, width,
					matrix, true);
			/*int imgWidth = scaleBit.getWidth();
			int imgHeight = scaleBit.getHeight();
			int x = 0, y = 0, w = 0, h = 0;

			if (imgWidth <= unitSize) {
				x = 0;
				w = imgWidth;
			} else {
				x = (imgWidth - unitSize) / 2;
				w = unitSize;
			}
			if (imgHeight <= unitSize) {
				y = 0;
				h = imgHeight;
			} else {
				y = (imgHeight - unitSize) / 2;
				h = unitSize;
			}
			scaleBit = Bitmap.createBitmap(scaleBit, x, y, w, h, null, true);*/
			return scaleBit;
		} else {
			float scaleWidth = ((float) newWidth1) / width;
			newHeight1 = ((float) (height * (float) newWidth1) / width);
			float scaleHeight = ((float) newHeight1) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片

			Bitmap scaleBit = Bitmap.createBitmap(bm, 0, 0, height, height,
					matrix, true);
			/*int imgWidth = scaleBit.getWidth();
			int imgHeight = scaleBit.getHeight();
			int x = 0, y = 0, w = 0, h = 0;
			if (imgWidth <= unitSize) {
				x = 0;
				w = imgWidth;
			} else {
				x = (imgWidth - unitSize) / 2;
				w = unitSize;
			}
			if (imgHeight <= unitSize) {
				y = 0;
				h = imgHeight;
			} else {
				y = (imgHeight - unitSize) / 2;
				h = unitSize;
			}
			scaleBit = Bitmap.createBitmap(scaleBit, x, y, w, h, null, true);*/
			return scaleBit;
		}

	}
	/**
	 * 压缩并剪切图片，图片可能不会完全显示出来
	 * @param bm 
	 * @param newWidth
	 * @param newHeight
	 * @return
	 * @author 刘星星
	 * @createDate 2013/11/8
	 */
		public static Bitmap cutImage(Bitmap bm, int newWidth, int newHeight) {
			// 图片源
			// Bitmap bm = BitmapFactory.decodeStream(getResources()
			// .openRawResource(id));
			// 获得图片的宽高
			// Bitmap newbm = null;
			int width = bm.getWidth();
			int height = bm.getHeight();
			// 设置想要的大小
			float newWidth1 = newWidth;
			float newHeight1 = newHeight;
			/*
			 * if(width<newWidth1 && height <newHeight1){ return bm; }
			 */
			// 计算缩放比例
			if (height > width) {
				float scaleHeight = ((float) newHeight1) / height;
				newWidth1 = ((float) (width * (float) newHeight1) / height);
				float scaleWidth = ((float) newWidth1) / width;
				// 取得想要缩放的matrix参数
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				int x=0;
				int y = (height-width)/2;
				int w = width;
				int h = width;
				// 得到新的图片
				Bitmap scaleBit = Bitmap.createBitmap(bm, x, y, w, h,matrix, true);
				return scaleBit;
			} else {
				float scaleWidth = ((float) newWidth1) / width;
				newHeight1 = ((float) (height * (float) newWidth1) / width);
				float scaleHeight = ((float) newHeight1) / height;
				// 取得想要缩放的matrix参数
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				// 得到新的图片

				Bitmap scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height,matrix, true);
				return scaleBit;
			}

		}
	/**
	 * 等比压缩图片
	 * 
	 * @param bm
	 *            被压缩的图片
	 * @param newWidth
	 *            压缩后的宽度
	 * @param newHeight
	 *            压缩后的图片高度
	 * @return 一张新的图片
	 * @author 刘星星
	 */

	public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		
		if((bm.getWidth()<=newWidth && bm.getHeight() <= newHeight) || (newWidth==0 || newHeight == 0)){
			return bm;
		}
		// 图片源
		// Bitmap bm = BitmapFactory.decodeStream(getResources()
		// .openRawResource(id));
		// 获得图片的宽高
		// Bitmap newbm = null;
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		float newWidth1 = newWidth;
		float newHeight1 = newHeight;
		/*
		 * if(width<newWidth1 && height <newHeight1){ return bm; }
		 */
		// 计算缩放比例
		if (height > width) {
			float scaleHeight = ((float) newHeight1) / height;
			newWidth1 = ((float) (width * (float) newHeight1) / height);
			float scaleWidth = ((float) newWidth1) / width;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			Bitmap scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height,
					matrix, true);
			return scaleBit;
		} else {
			float scaleWidth = ((float) newWidth1) / width;
			newHeight1 = ((float) (height * (float) newWidth1) / width);
			float scaleHeight = ((float) newHeight1) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片

			Bitmap scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height,
					matrix, true);
			// Bitmap bmp = scaleBit;
			return scaleBit;
		}

	}

	/**
	 * 将图片保存到本地ＳＤ卡
	 * 
	 * @param file
	 *            　路径
	 * @return
	 * @author 刘星星
	 */
	public static boolean OutPutImage(File file, Bitmap bitmap) {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			if(bos!=null){
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				fos.flush();
				return true;
			}else{
				return false;
			}
		} catch (IOException e) {
			return false;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	/**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @return
     */  
    public static  Bitmap readBitMap(Context context, int resId){  
        BitmapFactory.Options opt = new BitmapFactory.Options();  
        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
        opt.inPurgeable = true;  
        opt.inInputShareable = true;  
          //获取资源图片  
       InputStream is = context.getResources().openRawResource(resId);  
      return BitmapFactory.decodeStream(is,null,opt);  
   }
    
	/**
	 * 动态计算出图片的inSampleSize
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return 返回图片的最佳inSampleSize
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 隐藏键盘的函数
	 * 
	 * @author 刘星星
	 * @createDate 2013/2/19
	 * @param activity
	 */
	public static void hideSoftKeyboard(Activity activity) {
		try {
			((InputMethodManager) activity
					.getSystemService(activity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(activity.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
	}

}
