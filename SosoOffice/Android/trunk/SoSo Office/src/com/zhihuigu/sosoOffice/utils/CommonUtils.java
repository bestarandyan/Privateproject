package com.zhihuigu.sosoOffice.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.MyApplication;

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
	 * ���洢���Ƿ����
	 * 
	 * @author ������
	 * @Data 2012��1��6��
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

	/**
	 * ����·����ȡͼƬ ����ѹ��ͼƬ
	 * 
	 * @param pathName
	 *            ͼƬ·��
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
						&& !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
			}
			// ���ͼƬ��δ���գ���ǿ�ƻ��ո�ͼƬ
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
	 * ��������ͼƬ��ת90��
	 * @author ������
	 * @param fileName �ļ�·��
	 * @param oldBitmap λͼ
	 * @return �µ�������ͼƬ
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
	 * ����·����ȡͼƬ ����ѹ��ͼƬ
	 * 
	 * @param pathName
	 *            ͼƬ·��
	 * @param w
	 *            ͼƬ�Ŀ�
	 * @param h
	 *            ͼƬ�ĸ�
	 * @return ��СΪ150*150��ͼƬ
	 * @author ������
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
	 * ��View�еõ�Bitmap
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
	/**
	 * ����·����ȡͼƬ ����ѹ��ͼƬ
	 * 
	 * @param pathName
	 *            ͼƬ·��
	 * @return ��СΪ150*150��ͼƬ
	 * @author ������
	 */
	public static Bitmap getDrawable(String pathName) {
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
				bmp = scaleImgFrirst(bmp, Constant.ROOM_IMG_THUMBNAIL_SIZE,
						Constant.ROOM_IMG_THUMBNAIL_SIZE);
			} else {
				return null;
			}

		} catch (OutOfMemoryError e) {
			bmp = BitmapFactory.decodeFile(pathName, opts);
			bmp = scaleImgFrirst(bmp, Constant.ROOM_IMG_THUMBNAIL_SIZE,
					Constant.ROOM_IMG_THUMBNAIL_SIZE);
		}
		return bmp;
	}
	public static Bitmap cutImg(Bitmap bm,int x,int y, int newWidth, int newHeight){
		bm = Bitmap.createBitmap(bm, x, y, newWidth, newHeight);
		
		return bm;
	}
/**
 * ѹ��������ͼƬ��ͼƬ���ܲ�����ȫ��ʾ����
 * @param bm 
 * @param newWidth
 * @param newHeight
 * @return
 * @author ������
 * @createDate 2013/2/20
 */
	public static Bitmap scaleImgFrirst(Bitmap bm, int newWidth, int newHeight) {
		// ͼƬԴ
		// Bitmap bm = BitmapFactory.decodeStream(getResources()
		// .openRawResource(id));
		// ���ͼƬ�Ŀ��
		// Bitmap newbm = null;
		int width = bm.getWidth();
		int height = bm.getHeight();
		// ������Ҫ�Ĵ�С
		float newWidth1 = newWidth;
		float newHeight1 = newHeight;
		/*
		 * if(width<newWidth1 && height <newHeight1){ return bm; }
		 */
		int unitSize = MyApplication.getInstance().getScreenWidth() / 2 - 30;
		// �������ű���
		if (height > width) {
			float scaleHeight = ((float) newHeight1) / height;
			newWidth1 = ((float) (width * (float) newHeight1) / height);
			float scaleWidth = ((float) newWidth1) / width;
			// ȡ����Ҫ���ŵ�matrix����
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// �õ��µ�ͼƬ
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
			// ȡ����Ҫ���ŵ�matrix����
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// �õ��µ�ͼƬ

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
	 * �ȱ�ѹ��ͼƬ
	 * 
	 * @param bm
	 *            ��ѹ����ͼƬ
	 * @param newWidth
	 *            ѹ����Ŀ��
	 * @param newHeight
	 *            ѹ�����ͼƬ�߶�
	 * @return һ���µ�ͼƬ
	 * @author ������
	 */

	public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		if(bm.getWidth()<=newWidth && bm.getHeight() <= newHeight){
			return bm;
		}
		// ͼƬԴ
		// Bitmap bm = BitmapFactory.decodeStream(getResources()
		// .openRawResource(id));
		// ���ͼƬ�Ŀ��
		// Bitmap newbm = null;
		int width = bm.getWidth();
		int height = bm.getHeight();
		// ������Ҫ�Ĵ�С
		float newWidth1 = newWidth;
		float newHeight1 = newHeight;
		/*
		 * if(width<newWidth1 && height <newHeight1){ return bm; }
		 */
		// �������ű���
		if (height > width) {
			float scaleHeight = ((float) newHeight1) / height;
			newWidth1 = ((float) (width * (float) newHeight1) / height);
			float scaleWidth = ((float) newWidth1) / width;
			// ȡ����Ҫ���ŵ�matrix����
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// �õ��µ�ͼƬ
			Bitmap scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height,
					matrix, true);
			return scaleBit;
		} else {
			float scaleWidth = ((float) newWidth1) / width;
			newHeight1 = ((float) (height * (float) newWidth1) / width);
			float scaleHeight = ((float) newHeight1) / height;
			// ȡ����Ҫ���ŵ�matrix����
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// �õ��µ�ͼƬ

			Bitmap scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height,
					matrix, true);
			// Bitmap bmp = scaleBit;
			return scaleBit;
		}

	}

	/**
	 * ��ͼƬ���浽���أӣĿ�
	 * 
	 * @param file
	 *            ��·��
	 * @return
	 * @author ������
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
	 * ��̬�����ͼƬ��inSampleSize
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return ����ͼƬ�����inSampleSize
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
	 * ���ؼ��̵ĺ���
	 * 
	 * @author ������
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
