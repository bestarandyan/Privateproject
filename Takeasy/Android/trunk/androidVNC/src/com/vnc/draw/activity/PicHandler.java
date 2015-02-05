package com.vnc.draw.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.location.Location;
import android.media.ExifInterface;
public class PicHandler {
	public byte[] imagedata = null;
	public Bitmap commBit = null;
	public PicHandler(byte[] imagedata) {
		this.imagedata = imagedata;
	}

	public PicHandler() {
		// TODO Auto-generated constructor stub
	}

	public Bitmap handlePic() {
		if(commBit!=null){
			commBit.recycle();
			commBit = null;
			System.gc();
		}
		try {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length, opts);
				opts.inSampleSize = computeSampleSize(opts, -1, 1000 * 1000);
				opts.inJustDecodeBounds = false;
				try{
					commBit = BitmapFactory.decodeByteArray(imagedata, 0,imagedata.length, opts);
					commBit = scaleImg(commBit, commBit.getWidth()-1, commBit.getHeight()-1);
				}catch(OutOfMemoryError e){
					if(commBit!=null){
						commBit.recycle();
						commBit = null;
						System.gc();
					}
				return null;
				}
		} catch (Exception e) {
		
			// Toast.makeText(context, "内存溢出了、。。。。。", 3000).show();
		}
	
		return commBit;
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
	 * @author  刘星星
	 */
	public Bitmap scaleBit = null;
	public Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		// 图片源
		// Bitmap bm = BitmapFactory.decodeStream(getResources()
		// .openRawResource(id));
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth1 = newWidth;
		int newHeight1 = newHeight;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
//		Bitmap newbm = null;
		if(scaleBit!=null && !scaleBit.isRecycled()){
			scaleBit.recycle();
			scaleBit = null;
			System.gc();
		}
		try {
			scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
			/*bm.recycle();
			bm = null;*/
			System.gc();
		} catch (OutOfMemoryError e) {
			/*AlertDialog.Builder callDailog = new AlertDialog.Builder(MyApplication.getInstance().getContext());
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("提示")
					.setMessage("处理图片过程中出险异常，请稍后重试！").setPositiveButton("确定", null)
					.show();*/
			return bm;
		}
		System.gc();
		return scaleBit;

	}

	/**
	 * 动态计算出图片的inSampleSize
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return 返回图片的最佳inSampleSize
	 */
	public int computeSampleSize(BitmapFactory.Options options,
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

	
public static int reckonThumbnail(int oldWidth, int oldHeight,
		int newWidth, int newHeight) {
	if ((oldHeight > newHeight && oldWidth > newWidth)
			|| (oldHeight <= newHeight && oldWidth > newWidth)) {
		int be = (int) (oldWidth / (float) newWidth);
		if (be <= 1)
			be = 1;
		return be;
	} else if (oldHeight > newHeight && oldWidth <= newWidth) {
		int be = (int) (oldHeight / (float) newHeight);
		if (be <= 1)
			be = 1;
		return be;
	}
	return 1;
}

public static Bitmap PicZoom(Bitmap bmp, int width, int height) {
	int bmpWidth = bmp.getWidth();
	int bmpHeght = bmp.getHeight();
	Matrix matrix = new Matrix();
	matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);
	return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
}

}
