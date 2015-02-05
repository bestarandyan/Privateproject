package com.qingfengweb.baoqi.collectInfo;

import java.util.Vector;

import com.qingfengweb.baoqi.propertyInsurance.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter{
	private Context myContext;
	private int mGalleryItemBackground;
	
	private Vector v;
	public GalleryAdapter(Context c,Vector $v) {
		
		this.myContext = c;
		this.v=$v;
	    
		 TypedArray a =myContext.obtainStyledAttributes(R.styleable.Gallery1);
         mGalleryItemBackground = a.getResourceId(
                 R.styleable.Gallery1_android_galleryItemBackground, 0);
         a.recycle();


	}
	public void setUpdataVec(Vector $v)
	{
		this.v=$v;
	}
	public int getCount() {
		return v.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	

	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView imageView = new ImageView(this.myContext);

		 Bitmap bm  = (Bitmap) v.elementAt(position);
		imageView.setImageBitmap(bm);
		 // 填充ImageView
	      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	      /* 设置布局参数*/
	      imageView.setLayoutParams(new Gallery.LayoutParams(450, 450));
	      /* 设置背景资源 */
	      imageView.setBackgroundResource(mGalleryItemBackground);
	      
	      
	      
//	      <?xml version="1.0" encoding="utf-8"?>
//	      <resources>
//	      <declare-styleable name="Gallery">
//	          <attr name="android:galleryItemBackground" />
//	      </declare-styleable> 
//	      </resources>


	      
	      
	      
	      
	      
	      
	      return imageView;

	}

}
