package com.qingfengweb.filedownload;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CallbackImpl implements ImageCallback{
	ImageView imageView;
	Context context;
	public CallbackImpl(Context context,ImageView imageView) {
		super();
		this.imageView = imageView;
		this.context = context;
	}

	@Override
	public void setViewDrawable(Bitmap bitmap,boolean height,int screenW) {
		imageView.setImageBitmap(bitmap);
		if(height){
			View v = (View) imageView.getParent();
			if(v!=null && bitmap.getHeight()>v.getHeight()){
				int h = bitmap.getHeight();
				int w = bitmap.getWidth();
				int currentW = screenW-20;
				int currentH = h*currentW/w;
				RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, currentH);
				v.setLayoutParams(param);
			}
			
		}
	}
	@Override
	public void setViewBackgroundDrawable(Bitmap bitmap) {
		BitmapDrawable bd= new BitmapDrawable(context.getResources(), bitmap);   
		imageView.setBackgroundDrawable(bd);
	}

	@Override
	public void setViewResource(int resId) {
		imageView.setImageResource(resId);
	}
}
