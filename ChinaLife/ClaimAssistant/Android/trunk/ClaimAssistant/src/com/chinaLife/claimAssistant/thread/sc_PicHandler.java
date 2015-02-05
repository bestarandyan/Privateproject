package com.chinaLife.claimAssistant.thread;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sqlcrypt.database.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.media.ExifInterface;

import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_StringUtils;

public class sc_PicHandler {
	public byte[] imagedata = null;
	public Location imageLocation = null;
	public String address = "";
	public double latitude = 0.0f;
	public double longitude = 0.0f;
	public Bitmap commBit = null;

	public sc_PicHandler(byte[] imagedata) {
		this.imagedata = imagedata;
		this.address = sc_MyApplication.getInstance().getAddress();
	}

	public sc_PicHandler() {
	}

	public Bitmap handlePic() {
		File dir = new File(sc_MyApplication.getInstance().getURL_PIC());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File dir1 = new File(sc_MyApplication.getInstance().getURL_PIC1());
		if (!dir1.exists()) {
			dir1.mkdirs();
		}
		// Bitmap picBitmap = null;
		if (commBit != null) {
			commBit.recycle();
			commBit = null;
			System.gc();
		}
		try {
			int picFlag = sc_MyApplication.getInstance().getHandPicFlag();
			// if(picFlag == 1){// 当场拍摄上传的照片
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 640 * 480);
			opts.inJustDecodeBounds = false;
			try {
				commBit = BitmapFactory.decodeByteArray(imagedata, 0,
						imagedata.length, opts);
				if (commBit.getWidth() > 640 && commBit.getHeight() > 480) {
					commBit = scaleImg(commBit, 640, 480);
				} else {
					commBit = scaleImg(commBit, commBit.getWidth(),
							commBit.getHeight());
				}

			} catch (OutOfMemoryError e) {
				if (commBit != null) {
					commBit.recycle();
					commBit = null;
					System.gc();
				}
				return null;
			}
			String filename = getPhotoFileName();
			String dirtory = "";
			if (sc_MyApplication.getInstance().getCasedescription_tag() == 1) {
				dirtory = sc_MyApplication.getInstance().getURL_PIC1();
			} else {
				dirtory = sc_MyApplication.getInstance().getURL_PIC();
			}
			File file = new File(dirtory, filename);
			OutPutImage(file, commBit);// 将图片存储到指定文件
			/*
			 * Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath(),
			 * opts); int scale = reckonThumbnail(b.getWidth(), b.getHeight(),
			 * 179, 154); picBitmap = PicZoom(b, (int) (b.getWidth() / scale),
			 * (int) (b.getHeight() / scale));
			 */
			if (file.exists()) {
				sc_MyApplication.getInstance().setFile(file);
			} else {
				return null;
			}
			keepPictureInfo(file);
			File file1 = new File(sc_MyApplication.getInstance().getURL_PIC(), filename);
			try {
				if (imageLocation != null) {
					setFileExif(file1.getCanonicalPath(), imageLocation);
				}
			} catch (final IOException e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						sc_LogUtil.sendLog(2, "保存文件文件出现错误："+e.getMessage());
					}
				}).start();
				e.printStackTrace();
			}
			try {
				commBit = SecondZoomBitmap(file.getAbsolutePath());// 用来显示到列表
			} catch (OutOfMemoryError e) {
				if (commBit != null) {
					commBit.isRecycled();
					commBit = null;
					System.gc();
				}
				return null;
			}
			/*
			 * }else if(picFlag == 0){//导入以前拍摄的照片不需要打水印 也不需要重新复制到文件
			 * BitmapFactory.Options opts = new BitmapFactory.Options();
			 * opts.inJustDecodeBounds = true;
			 * BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length,
			 * opts); opts.inSampleSize = computeSampleSize(opts, -1, 130 *
			 * 130); opts.inJustDecodeBounds = false; // commBit =
			 * BitmapFactory.decodeByteArray(imagedata, 0,imagedata.length,
			 * opts); try{ commBit = BitmapFactory.decodeByteArray(imagedata,
			 * 0,imagedata.length, opts); }catch(OutOfMemoryError e){
			 * if(commBit!=null){ commBit.recycle(); commBit = null;
			 * System.gc(); } return null; } if
			 * (MyApplication.getInstance().getCasedescription_tag() != 1) {
			 * keepPictureInfo(MyApplication.getInstance().getFile()); }
			 * 
			 * commBit = scaleImg(commBit,
			 * commBit.getWidth()>120?120:commBit.getWidth(),
			 * commBit.getHeight()>120?120:commBit.getHeight()); }
			 */
		} catch (final Exception e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "保存文件文件出现错误："+e.getMessage());
				}
			}).start();
			// Toast.makeText(context, "内存溢出了、。。。。。", 3000).show();
		}

		return commBit;
	}

//	private int getOptionsSampleSize(BitmapFactory.Options options,
//			int newWidth, int newHeight) {
//		int radioWidth = (int) Math.ceil(options.outWidth / newWidth);
//		int radioHeight = (int) Math.ceil(options.outHeight / newHeight);
//		if (radioWidth > 1 || radioHeight > 1) {
//			return radioWidth > radioHeight ? radioWidth : radioHeight;
//		} else {
//			return 1;
//		}
//	}
//
//	// private void
//	/**
//	 * Create report file.
//	 * 
//	 * @param srcFile
//	 * @param dstFile
//	 */
//	private void copyFile1(String srcFile, String dstFile) {
//		int length = 1048891;
//		FileChannel inC = null;
//		FileChannel outC = null;
//		try {
//
//			FileInputStream in = new FileInputStream(srcFile);
//			FileOutputStream out = new FileOutputStream(dstFile);
//			inC = in.getChannel();
//			outC = out.getChannel();
//			ByteBuffer b = null;
//			while (inC.position() < inC.size()) {
//				if ((inC.size() - inC.position()) < length) {
//					length = (int) (inC.size() - inC.position());
//				} else
//					length = 1048891;
//				b = ByteBuffer.allocateDirect(length);
//				inC.read(b);
//				b.flip();
//				outC.write(b);
//				outC.force(false);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (inC != null && inC.isOpen()) {
//					inC.close();
//				}
//				if (outC != null && outC.isOpen()) {
//					outC.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public Bitmap SecondZoomBitmap(String str) {
		// Bitmap picBitmap = null;
		if (commBit != null) {
			commBit.recycle();
			commBit = null;
			System.gc();
		}
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			BitmapFactory.decodeFile(str, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 130 * 130);
			opts.inJustDecodeBounds = false;
			try {
				commBit = BitmapFactory.decodeFile(str, opts);
				commBit = scaleImg(commBit, commBit.getWidth() > 120 ? 120
						: commBit.getWidth(), commBit.getHeight() > 120 ? 120
						: commBit.getHeight());
			} catch (OutOfMemoryError e) {
				if (commBit != null) {
					commBit.recycle();
					commBit = null;
					System.gc();
				}
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commBit;
	}

	/**
	 * 将图片保存到本地ＳＤ卡
	 * 
	 * @param file
	 *            　路径
	 * @return
	 * @author 刘星星
	 */
	private boolean OutPutImage(File file, Bitmap bitmap) {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			int picFlag = sc_MyApplication.getInstance().getHandPicFlag();
			if (picFlag == 1) {// 当场拍摄上传的照片
				commBit = doodle(bitmap);// 给图片打上水印
			}
		} catch (OutOfMemoryError e) {
			return false;
		}
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			commBit.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			fos.flush();
			bitmap.recycle();
			bitmap = null;
			return true;
		} catch (final IOException e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "保存文件文件出现IO错误：" + e.getMessage());
				}
			}).start();
			return false;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 给图片打水印
	 * 
	 * @param src
	 *            被打水印的图片
	 * @return 打好水印后的图片
	 * @author 刘星星
	 */
	// SoftReference<Bitmap> sr = null;
	Bitmap shuiBit = null;

	public Bitmap doodle(Bitmap src) {
		try {
			if (shuiBit != null && !shuiBit.isRecycled()) {
				shuiBit.recycle();
				shuiBit = null;
				System.gc();
			}
			shuiBit = Bitmap.createBitmap(src.getWidth() - 5,
					src.getHeight() - 5, Config.RGB_565);// 创建一个新的和SRC长度宽度一样的位图
		} catch (OutOfMemoryError out) {
			shuiBit.recycle();
			return null;
		}
		Canvas canvas = new Canvas(shuiBit);
		canvas.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入原图片src
		Paint paint = new Paint();
		paint.setTextSize(18);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeCap(Paint.Cap.ROUND);
		Date dt = new Date(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置显示格式
		String nowTime = "";
		nowTime = df.format(dt);
		canvas.drawText(nowTime, 20, src.getHeight() - 45, paint);
		if (address != null && !address.equals("")) {
			canvas.drawText(address, 20, src.getHeight() - 15, paint);
		} else {
//			canvas.drawText("未获取到地址信息。。。", 20, src.getHeight() - 15, paint);
		}
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		src.recycle();
		src = null;
		shuiBit = scaleImg(shuiBit, shuiBit.getWidth() - 1,
				shuiBit.getHeight() - 1);
		System.gc();
		return shuiBit;
	}

	/**
	 * 给图片打水印
	 * 
	 * @param src
	 *            被打水印的图片
	 * @return 打好水印后的图片
	 * @author 刘星星
	 */
	public static Bitmap xxnewb = null;

	public static Bitmap doodlexx(Bitmap src, Bitmap circle, int number,
			int textSize, int textToLeft, int textToTop) {
		if (xxnewb != null) {
			xxnewb.recycle();
			xxnewb = null;
			System.gc();
		}
		xxnewb = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		xxnewb.eraseColor(Color.TRANSPARENT);
		Canvas canvas = new Canvas(xxnewb);
		canvas.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入原图片src
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(textSize);
		paint.setAntiAlias(true);
		// paint.setDither(true);
		/*
		 * paint.setStyle(Paint.Style.STROKE);
		 * paint.setStrokeCap(Paint.Cap.ROUND);
		 */
		int a = sc_MyApplication.getInstance().getMaintoleft();
		if (circle != null) {
			if (number > 9) {
				paint.setTextSize(textSize - 3);
				canvas.drawBitmap(circle, src.getWidth() - circle.getWidth()
						- a, 0, null);
				canvas.drawText(String.valueOf(number),
						src.getWidth() - circle.getWidth() / 2 - 12 - a,
						circle.getWidth() / 2 + 6, paint);
			} else {
				canvas.drawBitmap(circle, src.getWidth() - circle.getWidth()
						- a, 0, null);
				canvas.drawText(String.valueOf(number),
						src.getWidth() - circle.getWidth() / 2 - 6 - a,
						circle.getWidth() / 2 + 6, paint);
			}

		} else {
			// number = 22;
			if (number > 9) {
				paint.setTextSize(textSize - 2);
				canvas.drawText(String.valueOf(number), src.getWidth()
						- textToLeft - 3, textToTop, paint);
			} else {
				canvas.drawText(String.valueOf(number), src.getWidth()
						- textToLeft, textToTop, paint);
			}

		}
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return xxnewb;
	}

	public static Bitmap doobleView(Bitmap src,int number,int textSize){
		Bitmap xxnewb = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		xxnewb.eraseColor(Color.TRANSPARENT);
		Canvas canvas = new Canvas(xxnewb);
//		canvas.draw
		return xxnewb;
		
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
	public Bitmap scaleBit = null;

	public Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		/*
		 * // 图片源 // Bitmap bm = BitmapFactory.decodeStream(getResources() //
		 * .openRawResource(id)); // 获得图片的宽高 int width = bm.getWidth(); int
		 * height = bm.getHeight(); // 设置想要的大小 int newWidth1 = newWidth; int
		 * newHeight1 = newHeight; // 计算缩放比例 float scaleWidth = ((float)
		 * newWidth1) / width; float scaleHeight = ((float) newHeight1) /
		 * height; // 取得想要缩放的matrix参数 Matrix matrix = new Matrix();
		 * matrix.postScale(scaleWidth, scaleHeight); // 得到新的图片 // Bitmap newbm
		 * = null; if(scaleBit!=null && !scaleBit.isRecycled()){
		 * scaleBit.recycle(); scaleBit = null; System.gc(); } try { scaleBit =
		 * Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		 * bm.recycle(); bm = null; System.gc(); } catch (OutOfMemoryError e) {
		 * AlertDialog.Builder callDailog = new
		 * AlertDialog.Builder(MyApplication.getInstance().getContext());
		 * callDailog.setIcon(android.R.drawable.ic_dialog_info) .setTitle("提示")
		 * .setMessage("处理图片过程中出险异常，请稍后重试！").setPositiveButton("确定", null)
		 * .show(); return bm; } System.gc(); return scaleBit;
		 */

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
			try {
				if (scaleBit != null) {
					scaleBit.recycle();
					scaleBit = null;
				}
				scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
						true);
			} catch (OutOfMemoryError e) {
				if (scaleBit != null) {
					scaleBit.recycle();
					scaleBit = null;
				}
				scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
						true);
			}
			// Bitmap bmp = scaleBit;
			return scaleBit;
		} else {
			float scaleWidth = ((float) newWidth1) / width;
			newHeight1 = ((float) (height * (float) newWidth1) / width);
			float scaleHeight = ((float) newHeight1) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			try {
				if (scaleBit != null) {
					scaleBit.recycle();
					scaleBit = null;
				}
				scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
						true);
			} catch (OutOfMemoryError e) {
				if (scaleBit != null) {
					scaleBit.recycle();
					scaleBit = null;
				}
				scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
						true);
			}
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

	/**
	 * 拍照完成后保存该图片所有信息
	 */

	public void keepPictureInfo(File file) {
		ContentValues values = new ContentValues();
		FileInputStream fis;
		String claimid = "";
		if (sc_MyApplication.getInstance().getCasedescription_tag() == 1) {
			claimid="hurry0123456789";
		}else{
			claimid= sc_MyApplication.getInstance().getClaimid();
		}
		int fileLen = 0;
		try {
			fis = new FileInputStream(file);
			fileLen = fis.available(); // 这就是文件大小
			if (fileLen <= 0) {
				return;
			}
		} catch (final FileNotFoundException e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "保存文件时找不到文件："+e.getMessage());
				}
			}).start();
			e.printStackTrace();
		} catch (final IOException e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "保存文件文件出现IO错误："+e.getMessage());
				}
			}).start();
			e.printStackTrace();
		}
		int type = 0;
		if (sc_MyApplication.getInstance().getStepstate() == 0) {
			type = 1;
		} else {
			if(sc_MyApplication.getInstance().getServer_type()==1){
				type = 1;
			}else{
				type = 2;
			}
		}
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置显示格式
		String nowTime = df.format(date);
		values.put("legendid", sc_MyApplication.getInstance().getLegendid());
		values.put("claimid", claimid);
		values.put("filename", file.getName());
		values.put("savepath", file.getAbsolutePath());
		values.put("filesize", fileLen);
		values.put("status", -1);
		values.put("type", type);
		values.put("longitude", sc_MyApplication.getInstance().getLongitude());
		values.put("latitude", sc_MyApplication.getInstance().getLatitude());
		values.put("location", address);
		values.put("time",
				sc_StringUtils.formatCalendar(nowTime, sc_MyApplication.ERROR_VALUE));
		String sql = "select * from uploadinfo where claimid= '"
				+ claimid
				+ "' and legendid ='"
				+ sc_MyApplication.getInstance().getLegendid() + "' and type ="+type;
		List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
				.selectRow(sql, null);
		if (selectresult.size() > 0) {
			sc_DBHelper.getInstance().delete(
					"uploadinfo",
					"claimid = ? and legendid= ? and type= ?",
					new String[] { claimid,
							sc_MyApplication.getInstance().getLegendid() + "",type+"" });
			try {
				sc_DBHelper.getInstance().delete(
						"uploadblockinfo",
						"uploadid = ?",
						new String[] { selectresult.get(0).get("uploadid")
								.toString() });
			} catch (final Exception e) {
			}
		}
		sc_DBHelper.getInstance().insert("uploadinfo", values);
		values.clear();

		values.put("claimid", claimid);
		values.put("legendid", sc_MyApplication.getInstance().getLegendid());
		values.put("type", type);
		values.put("review_reason", "");
		String sql1 = "select * from claimphotoinfo where claimid= '"
				+ claimid
				+ "' and legendid ='"
				+ sc_MyApplication.getInstance().getLegendid() + "' and type ="+type;
		if (sc_DBHelper.getInstance().selectRow(sql1, null).size() <= 0) {
			values.put("review_result", -1);
			sc_DBHelper.getInstance().insert("claimphotoinfo", values);
		} else {
			values.put("review_result", -2);
			if (sc_DBHelper.getInstance().update(
					"claimphotoinfo",
					values,
					"claimid = ? and legendid= ? and type= ?",
					new String[] { claimid,
							sc_MyApplication.getInstance().getLegendid() + "" ,type+""})) {
			}
		}
		values.clear();
		values.put("overstate", 1);
		sc_DBHelper.getInstance().update(
				"claiminfo",
				values,
				"claimid = ?",
				new String[] {claimid});
		values.clear();
		values = null;
		sc_DBHelper.getInstance().close();
	}

	/**
	 * 组合涂鸦图片和源图片
	 * 
	 * @param src
	 *            源图片
	 * @param watermark
	 *            涂鸦图片
	 * @return
	 */
	public static Bitmap waterBitmap = null;

	public static Bitmap doodle(Bitmap src, Bitmap watermark) {
		if (waterBitmap != null) {
			waterBitmap.recycle();
			waterBitmap = null;
			System.gc();
		}
		waterBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				Config.RGB_565);// 创建一个新的和SRC长度宽度一样的位图
		Canvas canvas = new Canvas(waterBitmap);
		canvas.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入原图片src
		canvas.drawBitmap(watermark, waterBitmap.getWidth() / 2,
				waterBitmap.getHeight() / 2, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return waterBitmap;
	}

	/**
	 * 改变JPG格式的文件的Exif属性
	 * 
	 * @param filename
	 *            文件路径
	 * @param location
	 *            地址信息
	 * @author 刘星星
	 */
	public void setFileExif(String filename, Location location) {
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filename);
		} catch (IOException e) {

			e.printStackTrace();
		}
		// String latitudeStr = "90/1,12/1,30/1";
		double lat = location.getLatitude();
		double alat = Math.abs(lat);
		String dms = location.convert(alat, location.FORMAT_SECONDS);
		String[] splits = dms.split(":");
		String[] secnds = (splits[2]).split("\\.");
		String seconds;
		if (secnds.length == 0) {
			seconds = splits[2];
		} else {
			seconds = secnds[0];
		}

		String latitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds
				+ "/1";
		exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitudeStr);
		exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, lat > 0 ? "N"
				: "S");
		double lon = location.getLongitude();
		double alon = Math.abs(lon);
		dms = location.convert(alon, location.FORMAT_SECONDS);
		splits = dms.split(":");
		secnds = (splits[2]).split("\\.");

		if (secnds.length == 0) {
			seconds = splits[2];
		} else {
			seconds = secnds[0];
		}
		String longitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds
				+ "/1";
		exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitudeStr);
		exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lon > 0 ? "E"
				: "W");
		try {
			exif.saveAttributes();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 用当前时间给拍摄的图片命名
	 * 
	 * @author 刘星星
	 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'img'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
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
