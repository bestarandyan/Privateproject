package com.vnc.draw.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

final class CameraManager {

	private static CameraManager cameraManager;
	public Camera camera;
	private final Context context;

	private boolean previewing;

	private Handler autoFocusHandler;
	private Handler previewHandler;
	private int previewMessage;

	public static void init(Context context) {
		if (cameraManager == null) {
			cameraManager = new CameraManager(context);
		}
	}

	public static CameraManager get() {
		return cameraManager;
	}

	private CameraManager(Context context) {
		this.context = context;
		camera = null;
		previewing = false;

	}

	public String openDriver(SurfaceHolder holder)
			throws IOException {
		String result = null;
		if (camera == null) {
			camera = Camera.open();
			camera.setPreviewDisplay(holder);
			
			/**
			 * 在android模拟器设置回出现错误，在真机上可以设置，如果在真机上不设置，在拍照后获取照片会很慢
			 * */
			try {
				Camera.Parameters parameters = camera.getParameters();
			    List<Size> sizes = parameters.getSupportedPreviewSizes();
			    Size optimalSize = getOptimalPreviewSize(sizes,800, 600);
			    parameters.setPreviewSize(optimalSize.width, optimalSize.height);
			    parameters.setPictureFormat(PixelFormat.JPEG);
			    camera.setParameters(parameters);
			} catch (Exception e) {
				Toast.makeText(context, "相机初始化失败。。", 5000).show();
				if(camera!=null){
					camera.release();
					camera = null;
					System.gc();
				}
			}
		}
		return result;
	}
	/**
	 * 获取最合适的相机参数
	 * @param sizes
	 * @param w
	 * @param h
	 * @return
	 */
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
	      final double ASPECT_TOLERANCE = 0.05;
	      double targetRatio = (double) w / h;
	      if (sizes == null) return null;

	      Size optimalSize = null;
	      double minDiff = Double.MAX_VALUE;

	      int targetHeight = h;

	      // Try to find an size match aspect ratio and size
	      for (Size size : sizes) {
	          double ratio = (double) size.width / size.height;
	          if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
	          if (Math.abs(size.height - targetHeight) < minDiff) {
	              optimalSize = size;
	              minDiff = Math.abs(size.height - targetHeight);
	          }
	      }

	      // Cannot find the one match the aspect ratio, ignore the requirement
	      if (optimalSize == null) {
	          minDiff = Double.MAX_VALUE;
	          for (Size size : sizes) {
	              if (Math.abs(size.height - targetHeight) < minDiff) {
	                  optimalSize = size;
	                  minDiff = Math.abs(size.height - targetHeight);
	              }
	          }
	      }
	      return optimalSize;
	  }
	

	public void closeDriver() {
		if (camera != null) {
			camera.release();
			camera = null;
		}
	}

	public void startPreview() {
		if (camera != null && !previewing) {
			camera.startPreview();
			previewing = true;
		}
	}

	public void stopPreview() {
		if (camera != null && previewing) {
			// if (!useOneShotPreviewCallback) {
			// camera.setPreviewCallback(null);
			// }
			
			
			camera.stopPreview();
			previewHandler = null;
			autoFocusHandler = null;
			previewing = false;
		}
	}

	public void requestPreviewFrame(Handler handler, int message) {
		if (camera != null && previewing) {
			previewHandler = handler;
			previewMessage = message;
			// if (useOneShotPreviewCallback) {
			// camera.setOneShotPreviewCallback(previewCallback);
			// } else {
			// camera.setPreviewCallback(previewCallback);
			// }
			camera.takePicture(null, null,jpegCallback);
		}
	}

	public void requestAutoFocus() {
		if (camera != null && previewing) {
//			camera.cancelAutoFocus();
			camera.autoFocus(new Camera.AutoFocusCallback(){
						@Override
						public void onAutoFocus(boolean success, Camera camera) {
							Log.d("HOME", "isAutofoucs " + Boolean.toString(success));					
						}
					} );
		}
	}
	
	private ShutterCallback shuuterCallback = new ShutterCallback() {

		public void onShutter() {

		}

	};
	
	ProgressDialog alertDialog;
	public Bitmap $bitmap = null;
	private PictureCallback jpegCallback = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			if($bitmap != null){
				$bitmap.recycle();
				$bitmap = null;
			}
			if(previewHandler!=null){
				try{
				$bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				int sizew=800;
				int sizeh=600;
				float scaleWidth = ((float) sizew) / $bitmap.getWidth();  
				float scaleHeight = ((float) sizeh) / $bitmap.getHeight();
				Matrix matrix = new Matrix();  
		        matrix.postScale(scaleWidth, scaleHeight);  
		        $bitmap = Bitmap.createBitmap($bitmap, 0, 0, $bitmap.getWidth(),  
		        		$bitmap.getHeight(), matrix, true); 
				ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
				$bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
				byte[] array= out.toByteArray();
				out.flush();
				out.close();
				Message message = previewHandler.obtainMessage(previewMessage, array);
			    message.sendToTarget();
			    previewHandler=null;
			    System.gc();
				}catch(Exception ex)
				{
					
				}
			}
		}

	};
	
	
	
	
	
	
	private PictureCallback rawCallback = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {

		}

	};


}
