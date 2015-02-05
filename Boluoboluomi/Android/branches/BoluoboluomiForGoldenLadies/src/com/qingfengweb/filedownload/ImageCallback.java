package com.qingfengweb.filedownload;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface ImageCallback {
	public void setViewBackgroundDrawable(Bitmap bitmap);
	public void setViewDrawable(Bitmap bitmap,boolean height,int width);
	public void setViewResource(int resId);
}
