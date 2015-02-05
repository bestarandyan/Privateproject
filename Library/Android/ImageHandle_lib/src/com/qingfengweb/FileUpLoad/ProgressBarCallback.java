package com.qingfengweb.FileUpLoad;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarCallback implements CallBackInterface{
	ProgressBar progressBar;
	TextView tv;
	public ProgressBarCallback(ProgressBar bar,TextView tv) {
		this.progressBar = bar;
		this.tv = tv;
	}
	@SuppressLint("NewApi")
	@Override
	public void setBarPro(int bar) {
		progressBar.setProgress(progressBar.getProgress()+bar);
//		tv.setText("aa");
//		tv.setVisibility(View.VISIBLE);
	}
	
}
