package android.androidVNC;

import java.util.ArrayList;

public class MyApplication {
	private static MyApplication mInstance=null;
	private int mCWidth=0;
	private int mCHeight=0;
	private int mPWidth=0;
	private int mPHeight=0;
	private boolean mIsSymbol=true;
	public boolean ismIsSymbol() {
		return mIsSymbol;
	}
	public void setmIsSymbol(boolean mIsSymbol) {
		this.mIsSymbol = mIsSymbol;
	}
	public int getmCWidth() {
		return mCWidth;
	}
	public int getmPWidth() {
		return mPWidth;
	}
	public void setmPWidth(int mPWidth) {
		this.mPWidth = mPWidth;
	}
	public int getmPHeight() {
		return mPHeight;
	}
	public void setmPHeight(int mPHeight) {
		this.mPHeight = mPHeight;
	}
	public void setmCWidth(int mCWidth) {
		this.mCWidth = mCWidth;
	}
	public int getmCHeight() {
		return mCHeight;
	}
	public void setmCHeight(int mCHeight) {
		this.mCHeight = mCHeight;
	}
	private  MyApplication(){	
	}
	public static MyApplication  getInstance(){
		if(mInstance==null){
			mInstance=new MyApplication();
		}
		return mInstance;
	}
	
	public  VncDatabase database;
	public  ArrayList<ConnectionBean> connections=new ArrayList<ConnectionBean>();
	public VncDatabase getDatabase() {
		return database;
	}
	public void setDatabase(VncDatabase database) {
		this.database = database;
	}
	public ArrayList<ConnectionBean> getConnections() {
		return connections;
	}
	public void setConnections(ArrayList<ConnectionBean> connections) {
		this.connections = connections;
	}

}
