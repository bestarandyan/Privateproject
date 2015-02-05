package com.qingfengweb.network;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class CallbackImpl implements ImageCallback{
	ImageView imageView;
	public CallbackImpl(ImageView imageView) {
		super();
		this.imageView = imageView;
	}

	@Override
	public void setViewDrawable(Bitmap bitmap) {
		imageView.setImageBitmap(bitmap);
	}
}
