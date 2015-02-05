package com.qingfengweb.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
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
	 * 19
	 * 
	 * @param 将图片内容解析成字节数组
	 *            20
	 * @param inStream
	 *            21
	 * @return byte[] 22
	 * @throws Exception
	 *             23
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.flush();
		outStream.close();
		inStream.close();
		return data;
	}
	public static byte[] fileToByte(String filePath){
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
	 * 根据当前日期加时间定义图片的名字，可确保每一次拍摄的照片的名字都不一样
	 * 
	 * @author 刘星星
	 * @return 图片的名字
	 */
	public static String getFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("'img'_yyyyMMdd_HHmmssSSS");
		return sdf.format(date) + ".jpg";
	}
	/**
	 * 获取当前时间   这里的时间为手机时间
	 * @return
	 */
	public static String getCurrentTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * 获取新的文件名称   没有后缀名的
	 * @return
	 */
	public static String getFileNameNoType() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("'file'_yyyyMMdd_HHmmss");
		return sdf.format(date);
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
			BitmapDrawable bitmapDrawable = null;
			if (imageView != null) {
				bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
			}
			// 如果图片还未回收，先强制回收该图片
			if (bitmapDrawable != null
					&& !bitmapDrawable.getBitmap().isRecycled()) {
				bitmapDrawable.getBitmap().recycle();
			}
			bmp = BitmapFactory.decodeFile(pathName, opts);
			// if (bmp != null) {
			// bmp = scaleImg(bmp, bmp.getWidth(), bmp.getHeight());
			// }
		} catch (OutOfMemoryError e) {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeFile(pathName, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 500 * 500);
			opts.inJustDecodeBounds = false;
			BitmapDrawable bitmapDrawable = null;
			if (imageView != null) {
				bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
			}
			// 如果图片还未回收，先强制回收该图片
			if (bitmapDrawable != null
					&& !bitmapDrawable.getBitmap().isRecycled()) {
				bitmapDrawable.getBitmap().recycle();
			}
			try {
				bmp = BitmapFactory.decodeFile(pathName, opts);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

		return bmp;
	}

	/**
	 * 根据路径获取图片 ，并压缩图片
	 * 
	 * @param pathName
	 *            图片路径
	 * @return
	 */
	public static Bitmap getDrawable(Resources res, int resid) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bmp = null;
		try {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeResource(res, resid,opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 1000 * 1000);
			opts.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeResource(res, resid, opts);
			bmp = scaleImg(bmp, bmp.getWidth(), bmp.getHeight());
		} catch (OutOfMemoryError e) {
			if (bmp != null) {
				bmp = scaleImg(bmp, 800, 600);
			}
		}

		return bmp;
	}
	/**
	 * BitMap  转化成为 drawable
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Context context , Bitmap bitmap){
		BitmapDrawable bd= new BitmapDrawable(context.getResources(), bitmap);  
		Drawable drawable = (Drawable)bd; 
		return drawable;
	}
	/**
	 * 根据路径获取图片 ，并压缩图片
	 * 
	 * @param pathName
	 *            图片路径
	 * @return
	 */
	public static Bitmap getDrawable(String pathName, ImageView imageView,
			int width, int height) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bmp = null;
		try {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeFile(pathName, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 1000 * 1000);
			opts.inJustDecodeBounds = false;
			BitmapDrawable bitmapDrawable = null;
			if (imageView != null) {
				bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
			}
			// 如果图片还未回收，先强制回收该图片
			if (bitmapDrawable != null
					&& !bitmapDrawable.getBitmap().isRecycled()) {
				bitmapDrawable.getBitmap().recycle();
			}
			bmp = BitmapFactory.decodeFile(pathName, opts);
			if (bmp != null) {
				if (bmp.getWidth() > width && bmp.getHeight() > height) {
					bmp = scaleImg(bmp, width, height);
				}
			}
		} catch (OutOfMemoryError e) {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeFile(pathName, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 500 * 500);
			opts.inJustDecodeBounds = false;
			BitmapDrawable bitmapDrawable = null;
			if (imageView != null) {
				bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
			}
			// 如果图片还未回收，先强制回收该图片
			if (bitmapDrawable != null
					&& !bitmapDrawable.getBitmap().isRecycled()) {
				bitmapDrawable.getBitmap().recycle();
			}
			bmp = BitmapFactory.decodeFile(pathName, opts);
			if (bmp != null) {
				if (bmp.getWidth() > width && bmp.getHeight() > height) {
					bmp = scaleImg(bmp, width, height);
				}
			}
		}

		return bmp;
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
//			if (bm != null) {
//				bm.recycle();
//				bm = null;
//			}
			// Bitmap bmp = scaleBit;
			return scaleBit;
		}

	}
	/**
	 * 根据路径获取图片 ，并压缩图片
	 * 
	 * @param pathName
	 *            图片路径
	 * @return 大小为150*150的图片
	 * @author 刘星星
	 */
	public static Bitmap getDrawable(String pathName,int cutSize) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bmp = null;
		try {
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeFile(pathName, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 1000 * 1000);
			opts.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(pathName, opts);
			if (bmp != null) {
				bmp = scaleImgFrirst(bmp, cutSize,cutSize);
			} else {
				return null;
			}

		} catch (OutOfMemoryError e) {
			bmp = BitmapFactory.decodeFile(pathName, opts);
			bmp = scaleImgFrirst(bmp, cutSize,cutSize);
		}
		return bmp;
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
	
}
