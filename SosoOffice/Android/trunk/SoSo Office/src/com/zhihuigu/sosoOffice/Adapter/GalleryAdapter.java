package com.zhihuigu.sosoOffice.Adapter;

import java.util.List;
import java.util.Vector;

import com.zhihuigu.sosoOffice.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter{
	private Context myContext;
	private int mGalleryItemBackground;
	private int ImgSelectIndex;
	private List<Bitmap> list;
	private int picW=0,picH=0;
	public GalleryAdapter(Context c,List<Bitmap> list) {
		this.myContext = c;
		this.list=list;
		/* TypedArray a =myContext.obtainStyledAttributes(R.styleable.Gallery1);
         mGalleryItemBackground = a.getResourceId(
                 R.styleable.Gallery1_android_galleryItemBackground, 0);
         a.recycle();*/
     //    System.gc();


	}
	public void setUpdataVec(List<Bitmap> $list)
	{
		this.list=$list;
	}
	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
	     ViewHolder holder = null;
	     if(convertView == null){
	    	 holder = new ViewHolder();
	    	 convertView = LayoutInflater.from(myContext).inflate(R.layout.item_gallery, null);
	    	 holder.galleryImage = (ImageView) convertView.findViewById(R.id.galleryImage);
	    	 holder.galleryCancleImage = (ImageView) convertView.findViewById(R.id.galleryCancleImage);
	    	 convertView.setTag(holder);
	     }else{
	    	 holder = (ViewHolder) convertView.getTag();
	     }
	     convertView.setLayoutParams(new Gallery.LayoutParams(160, 160));
	     holder.setContent(position);
	      return convertView;
	}
	class ViewHolder{
		ImageView galleryImage;
		ImageView galleryCancleImage;
		public void setContent(int position){
			Bitmap bm  = (Bitmap) list.get(position);
			galleryImage.setBackgroundDrawable(new BitmapDrawable(bm));
			if(position == 0){
				galleryCancleImage.setVisibility(View.GONE);
			}else{
				galleryCancleImage.setVisibility(View.VISIBLE);
			}
			System.gc();
		}
	}
}
