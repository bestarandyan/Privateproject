package com.qingfengweb.baoqi.collectInfo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CanvasPhotoActivity implements Runnable {
	
	private Bitmap bitmap = null;
	private File path = null;
	private PictureInfo context;
	private Context con1;
	private String dizhixinxi=null;
	public CanvasPhotoActivity(Bitmap bitmap,File path,PictureInfo context,String ddzz,Context context1) {
		this.bitmap = bitmap;
		this.path = path;
		this.context = context;
		this.con1=context1;
		this.dizhixinxi=ddzz;
	}

	
	public void run() {
		bitmap = doodle(bitmap,null);
        	BufferedOutputStream bos = null;
            try {
            	bos = new BufferedOutputStream(new FileOutputStream(path));
            	bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				bos.close();      
			} catch (IOException e) {
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
        Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);// ����һ���µĺ�SRC���ȿ��һ����λͼ  
        Canvas canvas = new Canvas(newb);
        canvas.drawBitmap(src, 0, 0, null);// �� 0��0���꿪ʼ����ԭͼƬsrc  
        Date dt= new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//������ʾ��ʽ
        String nowTime="";
        nowTime= df.format(dt);
        Paint paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.WHITE);
        canvas.drawText(nowTime, 20, src.getHeight()-50, paint);
        if(dizhixinxi==null || dizhixinxi.equals("")){
        	dizhixinxi="δ�õ�����ǰ��λ�á���������";
        }else{
        }
        paint.setTextSize(30);
    	canvas.drawText(dizhixinxi, src.getWidth()-500, src.getHeight()-50, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);  
        canvas.restore();   
          
        return newb;  
    }  
    
    public Handler handler = new Handler(){
    	public void handleMessage(Message msg) {
    		if(msg.what == 1){
    			/*context.listview.setVisibility(View.VISIBLE);
    			context.progressdialog.dismiss();
    			context.btn_submit.setVisibility(View.VISIBLE);*/
    		}else if(msg.what ==0){
    			
    		}
    	}
    };
    public void dialogWronFun(Context context){
      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
      	alert.setMessage("δ�ܶ�λ�����ĵ�ַ��Ϣ�������������硣��������");
      	alert.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			}).create().show();
      	
      }


}
