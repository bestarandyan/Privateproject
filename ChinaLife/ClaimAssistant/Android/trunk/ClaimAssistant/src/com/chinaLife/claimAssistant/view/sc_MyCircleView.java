package com.chinaLife.claimAssistant.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.R;

public class sc_MyCircleView extends View{
	public sc_MyCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	Context context;
	public sc_MyCircleView(Context context) {
		super(context);
		this.context = context;
	}
	public static int centerX = 0;//圆心x坐标
	public static int centerY = 0;//圆心Y坐标
	public static int radius = 0;
	
	@Override
	protected void onDraw(Canvas canvas) {
		centerX = sc_MyApplication.getInstance().getSw()/2;
		if(sc_MyApplication.getInstance().getSw() <= 320){
			centerY = sc_MyApplication.getInstance().getSh()/2-60;
		}else{
			centerY = sc_MyApplication.getInstance().getSh()/2-90;
		}
		
		radius = centerX-20;
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		canvas.drawCircle(centerX, centerY, radius+5, paint);
		paint.setColor(Color.rgb(220,236,249));
		canvas.drawCircle(centerX, centerY, radius, paint);
		paint.setColor(Color.WHITE);
		canvas.drawLine(centerX, centerY, centerX, centerY-radius, paint);
		canvas.save();
		canvas.rotate(72, centerX, centerY);
		canvas.drawLine(centerX, centerY, centerX, centerY-radius, paint);
		canvas.save();
		canvas.rotate(72, centerX, centerY);
		canvas.drawLine(centerX, centerY, centerX, centerY-radius, paint);
		canvas.save();
		canvas.rotate(72, centerX, centerY);
		canvas.drawLine(centerX, centerY, centerX, centerY-radius, paint);
		canvas.save();
		canvas.rotate(72, centerX, centerY);
		canvas.drawLine(centerX, centerY, centerX, centerY-radius, paint);
		canvas.restore();
		paint.setColor(Color.rgb(67,140,208));
		canvas.drawCircle(centerX, centerY, sc_MyApplication.getInstance().getSw()/5, paint);
//		canvas.save();
//		canvas.rotate(46, centerX, centerY);
//		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG)); 
//		canvas.drawBitmap(getBM(R.drawable.home_btn1,46), centerX, centerY-radius, paint);
//		canvas.save();
//		canvas.rotate(72, centerX, centerY);
//		canvas.drawBitmap(getBM(R.drawable.home_btn2,72), centerX, centerY-radius, paint);
//		canvas.save();
//		canvas.rotate(72, centerX, centerY);
//		canvas.drawBitmap(getBM(R.drawable.home_btn3,72), centerX, centerY-radius, paint);
//		canvas.save();
//		canvas.rotate(72, centerX, centerY);
//		canvas.drawBitmap(getBM(R.drawable.home_btn4,72), centerX, centerY-radius, paint);
//		canvas.save();
//		canvas.rotate(72, centerX, centerY);
//		canvas.drawBitmap(getBM(R.drawable.home_btn5,72), centerX, centerY-radius, paint);
		super.onDraw(canvas);
	}
	public Bitmap getBM(int id,int degrees){
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), id);
		bm = rotate(bm,degrees);
		return bm;
	}
	public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees,(float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();  //Android开发网再次提示Bitmap操作完应该显示的释放
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
            }
        }
        return b;
    }


}
