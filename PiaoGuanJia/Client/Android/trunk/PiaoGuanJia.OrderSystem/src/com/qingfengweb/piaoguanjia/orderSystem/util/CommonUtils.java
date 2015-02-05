package com.qingfengweb.piaoguanjia.orderSystem.util;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

public class CommonUtils {
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
						&&bitmapDrawable.getBitmap()!=null&& !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
			}
			// 如果图片还未回收，先强制回收该图片
			bmp = BitmapFactory.decodeFile(pathName, opts);
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
	 * 将倒掉的图片旋转90°
	 * @author 刘星星
	 * @param fileName 文件路径
	 * @param oldBitmap 位图
	 * @return 新的正立的图片
	 */
public static Bitmap RotateImg(String fileName,Bitmap oldBitmap){
	ExifInterface exifInterface = null;
	try {
		exifInterface = new ExifInterface(fileName);
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
		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bmp = null;
		try {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeFile(pathName, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 1000 * 1000);
			opts.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(pathName, opts);
			if(bmp!=null){
				if(w == 0 || h == 0){
					bmp = scaleImg(bmp, bmp.getWidth(), bmp.getHeight());
				}else{
					if(bmp.getWidth()>bmp.getHeight()){
						bmp = scaleImg(bmp, w, h);
					}else{
						bmp = scaleImg(bmp, h, w);
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
	public static Bitmap cutImg(Bitmap bm,int x,int y, int newWidth, int newHeight){
		bm = Bitmap.createBitmap(bm, x, y, newWidth, newHeight);
		
		return bm;
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
		if(bm.getWidth()<=newWidth && bm.getHeight() <= newHeight){
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
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(activity.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
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
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			fos.flush();
			return true;
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

}
