package com.qingfengweb.baoqi.collectInfo;

import java.util.ArrayList;

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
	private ArrayList<String> imagePathes;

	public ArrayList<String> getImagePathes() {
		return imagePathes;
	}
	public void setImagePathes(ArrayList<String> imagePathes) {
		this.imagePathes = imagePathes;
	}

}
