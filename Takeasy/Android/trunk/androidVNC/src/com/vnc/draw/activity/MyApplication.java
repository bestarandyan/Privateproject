package com.vnc.draw.activity;

import java.io.IOException;
import java.net.Socket;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;

public class MyApplication {
	public static MyApplication myApplication = null;
	public MyApplication() {
		
	}
	public static MyApplication getInstance(){
		if(myApplication == null){
			myApplication = new MyApplication();
		}
		return myApplication;
	}
	public int EtLeftX;
	public int EtLeftY;
	public Context context;
	public int paintsize;//控制画笔的大小
	public int typeShape;//控制形状
	public int screenw;//屏幕的宽
	public int screenh;//屏幕的高
	public String KeyBoardEtText;
	public Bitmap bitmap;//拍照得到的图片
	public int bitmapLeft;
	public int bitmapTop;//拍照得到的图片的想要定位的顶部位置
	public int topLinearHeight;//代表顶部控件的高度
	public int leftLinearWidth;//代表左边控件层的宽度
	public int pic_flag;
	public int back_pic = 0;
	public int pic_back_flag;//通知画Bitmap的类 是否正在返回   1代表返回  0代表不是正在点击返回的状态
	public int back_text = 0;
	public int text_back_flag;
	public int screenWidth;
	public int screenHeight;
	public byte[] dataByte=null;
	public int picId = 0;
	public int textId = 0;
	public int lineId = 0;
	public int picW = 0;
	public int picH = 0;
	public static String padIp = "191.168.1.1";
	public Socket socket = null;
	public Parcelable parcelable = null;
	public SendToServiceToDraw sendToService;
	public MainActivity mainActivity;
	
	public MainActivity getMainActivity() {
		return mainActivity;
	}
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	public SendToServiceToDraw getSendToService() {
	/*	if(MyApplication.getInstance().getSocket().isConnected() && MyApplication.getInstance().getSocket().isClosed()){
			sendToService = new SendToServiceToDraw();
			setSendToService(sendToService);
		}else{
			try{
				MyApplication.getInstance().getSocket().sendUrgentData(0xFF);
				}catch(IOException e){
					try {
						MyApplication.getInstance().getSocket().close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
										
										
				}
		}*/
		
		return sendToService;
	}
	public void setSendToService(SendToServiceToDraw sendToService) {
		this.sendToService = sendToService;
	}
	public Parcelable getParcelable() {
		return parcelable;
	}
	public void setParcelable(Parcelable parcelable) {
		this.parcelable = parcelable;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public int getLineId() {
		return lineId;
	}
	public void setLineId(int lineId) {
		this.lineId = lineId;
	}
	public int getPicW() {
		return picW;
	}
	public void setPicW(int picW) {
		this.picW = picW;
	}
	public int getPicH() {
		return picH;
	}
	public void setPicH(int picH) {
		this.picH = picH;
	}
	public int getPicId() {
		return picId;
	}
	public void setPicId(int picId) {
		this.picId = picId;
	}
	public int getTextId() {
		return textId;
	}
	public void setTextId(int textId) {
		this.textId = textId;
	}
	public byte[] getDataByte() {
		return dataByte;
	}
	public void setDataByte(byte[] dataByte) {
		this.dataByte = dataByte;
	}
	public int getScreenWidth() {
		return screenWidth;
	}
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	public int getScreenHeight() {
		return screenHeight;
	}
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	public int getText_back_flag() {
		return text_back_flag;
	}
	public void setText_back_flag(int text_back_flag) {
		this.text_back_flag = text_back_flag;
	}
	public int getPic_back_flag() {
		return pic_back_flag;
	}
	public void setPic_back_flag(int pic_back_flag) {
		this.pic_back_flag = pic_back_flag;
	}
	public int getPic_flag() {
		return pic_flag;
	}
	public void setPic_flag(int pic_flag) {
		this.pic_flag = pic_flag;
	}
	public int getTopLinearHeight() {
		return topLinearHeight;
	}
	public void setTopLinearHeight(int topLinearHeight) {
		this.topLinearHeight = topLinearHeight;
	}
	public int getLeftLinearWidth() {
		return leftLinearWidth;
	}
	public void setLeftLinearWidth(int leftLinearWidth) {
		this.leftLinearWidth = leftLinearWidth;
	}
	public int getBitmapLeft() {
		return bitmapLeft;
	}
	public void setBitmapLeft(int bitmapLeft) {
		this.bitmapLeft = bitmapLeft;
	}
	public int getBitmapTop() {
		return bitmapTop;
	}
	public void setBitmapTop(int bitmapTop) {
		this.bitmapTop = bitmapTop;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public int getEtLeftX() {
		return EtLeftX;
	}
	public void setEtLeftX(int etLeftX) {
		EtLeftX = etLeftX;
	}
	public int getEtLeftY() {
		return EtLeftY;
	}
	public void setEtLeftY(int etLeftY) {
		EtLeftY = etLeftY;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public String getKeyBoardEtText() {
		return KeyBoardEtText;
	}
	public void setKeyBoardEtText(String keyBoardEtText) {
		KeyBoardEtText = keyBoardEtText;
	}
	public int getScreenw() {
		return screenw;
	}
	public void setScreenw(int screenw) {
		this.screenw = screenw;
	}
	public int getScreenh() {
		return screenh;
	}
	public void setScreenh(int screenh) {
		this.screenh = screenh;
	}
	public int getTypeShape() {
		return typeShape;
	}
	public void setTypeShape(int typeShape) {
		this.typeShape = typeShape;
	}
	public int getPaintsize() {
		return paintsize;
	}
	public void setPaintsize(int paintsize) {
		this.paintsize = paintsize;
	}
//以下为键盘输入的变量
	public String msg;
	public int msgColor;
	public int msgLeft;
	public int msgTop;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getMsgColor() {
		return msgColor;
	}
	public void setMsgColor(int msgColor) {
		this.msgColor = msgColor;
	}
	public int getMsgLeft() {
		return msgLeft;
	}
	public void setMsgLeft(int msgLeft) {
		this.msgLeft = msgLeft;
	}
	public int getMsgTop() {
		return msgTop;
	}
	public void setMsgTop(int msgTop) {
		this.msgTop = msgTop;
	}
	
}
