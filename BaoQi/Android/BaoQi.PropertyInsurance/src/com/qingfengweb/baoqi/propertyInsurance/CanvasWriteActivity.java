package com.qingfengweb.baoqi.propertyInsurance;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CanvasWriteActivity implements Runnable {
	
	private Bitmap bitmap = null;
	public static Bitmap newb = null;
	private Bitmap watermark = null;
	private File path = null;
	private Context context;
	public FrameLayout guo_tab5 = null;
	public ImageView guo_image;
	public ProgressDialog progressdialog;
	
	public CanvasWriteActivity(Bitmap bitmap,Bitmap watermark,File path,Context context,FrameLayout guo_tab5
			,ImageView guo_image,ProgressDialog progressdialog) {
		this.bitmap = bitmap;
		this.path = path;
		this.context = context;
		this.watermark = watermark;
		this.progressdialog = progressdialog;
		this.guo_image = guo_image;
		this.guo_tab5 = guo_tab5;
	}

	@Override
	public void run() {
		bitmap = doodle(bitmap,watermark);
        try {
        	BufferedOutputStream out =  new BufferedOutputStream(new FileOutputStream(path));
		       bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		       out.flush();
		       out.close();
		} catch (Exception e) {
		       e.printStackTrace();
		}
        		
        Message msg = new Message();
		msg.what = 1;
		handler.sendMessage(msg);
	}
	
	
	
	 /** 
     * ���ͿѻͼƬ��ԴͼƬ 
     * @param src ԴͼƬ 
     * @param watermark ͿѻͼƬ 
     * @return 
     */  
    public Bitmap doodle(Bitmap src,Bitmap watermark)  
    {  
        // ���ⴴ��һ��ͼƬ  
    	
    	int bmpWidth = watermark.getWidth();
    	int bmpHeight = watermark.getHeight();
    	
    	float scaleHeight = (float) 100/bmpHeight;
    	
    	Matrix matrix = new Matrix();
    	matrix.postScale(scaleHeight, scaleHeight);
    	
    	Bitmap resizeBitmap = Bitmap.createBitmap(watermark, 0, 0, bmpWidth, bmpHeight, matrix, false);
    	
    	if(newb != null){
    		newb.recycle();
    		newb = null;
    	}
        newb = Bitmap.createBitmap(src.getWidth(), src.getHeight()+resizeBitmap.getHeight(), Config.ARGB_8888);// ����һ���µĺ�SRC���ȿ��һ����λͼ  
        Canvas canvas = new Canvas(newb);
        canvas.drawBitmap(src, 0, 0, null);// �� 0��0���꿪ʼ����ԭͼƬsrc  
        
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, src.getHeight(), src.getWidth(), src.getHeight()+100, paint);
        
        canvas.drawBitmap(resizeBitmap, 50,src.getHeight(), null);
//        canvas.drawText("Ͷ����ǩ��:", 0, y, paint)(resizeBitmap, 50,src.getHeight(), null);
        canvas.save(Canvas.ALL_SAVE_FLAG);  
        canvas.restore(); 
          
        return newb;  
    }  
    
    public Handler handler = new Handler(){
    	public void handleMessage(Message msg) {
    		if(msg.what == 1){
    			guo_tab5.setVisibility(View.VISIBLE);
    			progressdialog.dismiss();
    			guo_tab5.removeViewAt(1);
    			guo_image.setImageBitmap(bitmap);
    			guo_image.invalidate();
    			guo_tab5.invalidate();
    		}else if(msg.what ==0){
    			
    		}
    	}
    };

}
