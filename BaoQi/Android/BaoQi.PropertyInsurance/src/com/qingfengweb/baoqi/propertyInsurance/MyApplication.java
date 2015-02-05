package com.qingfengweb.baoqi.propertyInsurance;

import java.io.File;
import java.util.ArrayList;

import com.baidu.mapapi.GeoPoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TabHost;


public class MyApplication {
	private static MyApplication mInstance=null;
	
	private  MyApplication(){	
	}
	public static MyApplication  getInstance(){
		if(mInstance==null){
			mInstance=new MyApplication();
		}
		return mInstance;
	}
	
	
	public Bundle bundle;

	public Bundle getBundle() {
		return bundle;
	}
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	
	public Bitmap bitmap1;
	public Bitmap bitmap2;

	public Bitmap getBitmap2() {
		return bitmap2;
	}
	public void setBitmap2(Bitmap bitmap2) {
		this.bitmap2 = bitmap2;
	}
	public Bitmap getBitmap1() {
		return bitmap1;
	}
	public void setBitmap1(Bitmap bitmap1) {
		this.bitmap1 = bitmap1;
	}
	
	public File mCurrentPhotoFile;

	public File getmCurrentPhotoFile() {
		return mCurrentPhotoFile;
	}
	public void setmCurrentPhotoFile(File mCurrentPhotoFile) {
		this.mCurrentPhotoFile = mCurrentPhotoFile;
	}
	

}
