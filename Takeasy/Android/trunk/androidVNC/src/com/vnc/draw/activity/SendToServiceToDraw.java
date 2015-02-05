package com.vnc.draw.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vnc.draw.tools.CompassImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class SendToServiceToDraw {
	private static   String connectStr = "180.171.117.115";
	/*public SendToServiceToDraw() {
		connectStr = MyApplication.padIp;
	}*/
	public static Socket client;
	DataOutputStream  out1 = null;
	byte[] buf;
	public SendToServiceToDraw(){
		connectStr = MyApplication.padIp;
		client = new Socket();
		SocketAddress socketAddress = new InetSocketAddress(connectStr,25000); //获取sockaddress对象
		try {
			client.connect(socketAddress,10000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void connectServer(byte[] data){
		try {
		    out1 =new DataOutputStream(client.getOutputStream());
					if (client.isConnected()) {                   
						if (!client.isOutputShutdown()) {     
							try {
		    						out1.write(data);
		    						out1.flush();
							} catch (Exception e) {   
								e.printStackTrace();
							}
							}                 
						}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{/*
			if(out1!=null){
				try {
					out1.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		*/}
		
	}
	public static byte[] intToBytes(int origin,int length) {  
        byte[] bytes = new byte[length];  
        for (int i = 0; i < bytes.length; i++) {  
            bytes[i] = (byte) (origin >>> (i * 8));  
        }  
        return bytes;  
    }  
	
	
	/**
	 * 向服务器发送请求画曲线
	 */
	public void sDrawQuXian(int[] dataInt){
		byte[] lines = null;
		lines = new byte[dataInt[2]];//直线数据包
	for(int i=0;i<dataInt.length;i++){
		if(i<=2){
				//lines[a] = intToBytes(dataInt[i], 4)[a];
				System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
		}else if(i == 3){
			//lines[i] = intToBytes(dataInt[i], 1)[i];
			System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
		}else if(i == 4){
			System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
		}else if(i>4){
			System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4-6, 4);
		}
	}
		connectServer(lines);
	}
	/**
	 * 设置画笔风格
	 */
	public void setDrawStyle(int pensize,int color){
		byte[] lines = null;
		lines = new byte[26];//直线数据包
		 int red = (color & 0xff0000) >> 16; 
	      int green = (color & 0x00ff00) >> 8; 
	      int blue = (color & 0x0000ff); 
		int[] dataInt = {1025,1,26,0,241,0,pensize,red,green,blue,0};
		for(int i=0;i<dataInt.length;i++){
			if(i<=2){
					//lines[a] = intToBytes(dataInt[i], 4)[a];
					System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
			}else if(i == 3){
				//lines[i] = intToBytes(dataInt[i], 1)[i];
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
			}else if(i == 4){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
			}else if(i>4 && i<7){
				System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4-6, 4);
			}else if(i == 7){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 22, 1);
			}else if(i == 8){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 23, 1);
			}else if(i == 9){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 24, 1);
			}else if(i == 10){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 25, 1);
			}
		}
		connectServer(lines);
	}
	
	/**
	 * 设置画笔风格
	 */
	public void setBgColor(int color){
		byte[] lines = null;
		lines = new byte[18];//直线数据包
		 int red = (color & 0xff0000) >> 16; 
	      int green = (color & 0x00ff00) >> 8; 
	      int blue = (color & 0x0000ff); 
		int[] dataInt = {1027,1,18,0,241,red,green,blue,0};
		for(int i=0;i<dataInt.length;i++){
			if(i<=2){
					//lines[a] = intToBytes(dataInt[i], 4)[a];
					System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
			}else if(i == 3){
				//lines[i] = intToBytes(dataInt[i], 1)[i];
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
			}else if(i == 4){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
			}else if(i == 5){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 14, 1);
			}else if(i == 6){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 15, 1);
			}else if(i == 7){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 16, 1);
			}else if(i == 8){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 17, 1);
			}
		}
		connectServer(lines);
	}
	/**
	 * 设置画笔风格
	 */
	public void sFill(int[] dataInt){
		byte[] lines = null;
		lines = new byte[dataInt[2]];//直线数据包
		for(int i=0;i<dataInt.length;i++){
			if(i<=2){
					//lines[a] = intToBytes(dataInt[i], 4)[a];
					System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
			}else if(i == 3){
				//lines[i] = intToBytes(dataInt[i], 1)[i];
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
			}else if(i == 4){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
			}else if(i>4 && i<=7){
				System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4-6, 4);
			}else if(i == 8){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 26, 1);
			}else if(i == 9){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 27, 1);
			}else if(i == 10){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 28, 1);
			}else if(i == 11){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 29, 1);
			}
		}
		connectServer(lines);
	}
	
	public void drawBrush(){
		byte[] lines = null;
		lines = new byte[22];//直线数据包
		int[] dataInt = {1026,1,22,0,241,0,5,72,151,0};
		for(int i=0;i<dataInt.length;i++){
			if(i<=2){
					//lines[a] = intToBytes(dataInt[i], 4)[a];
					System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
			}else if(i == 3){
				//lines[i] = intToBytes(dataInt[i], 1)[i];
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
			}else if(i == 4){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
			}else if(i == 5){
				System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4-6, 4);
			}else if(i == 6){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 18, 1);
			}else if(i == 7){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 19, 1);
			}else if(i == 8){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 20, 1);
			}else if(i == 9){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 21, 1);
			}
		}
		connectServer(lines);
	}
	/**
	 * 多功能画图
	 * 1，画直线
	 * 2，画矩形
	 * 3,画椭圆
	 * 4,画圆角矩形
	 */
	public void sDraw(int[] dataInt){
		byte[] lines = null;
			lines = new byte[dataInt[2]];//直线数据包
    	for(int i=0;i<dataInt.length;i++){
			if(i<=2){
					//lines[a] = intToBytes(dataInt[i], 4)[a];
					System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
			}else if(i == 3){
				//lines[i] = intToBytes(dataInt[i], 1)[i];
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
			}else if(i == 4){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
			}else if(i>4){
				System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4-6, 4);
			}
		}
    	connectServer(lines);
    }
	/**
	 * 画文本
	 */
	int a = 1;
	public void sDrawText(int cmd,int id){
		int[] dataInt = {cmd,1,26,0,241,id,
				MyApplication.getInstance().getMsgLeft(),
				MyApplication.getInstance().getMsgTop()};
		byte[] textByte = StringToByte(MyApplication.getInstance().getMsg(), "UTF-8");
		byte[] lines = null;
		lines = new byte[(dataInt[2]+textByte.length)+4];//直线数据包
	for(int i=0;i<dataInt.length;i++){
		if(i<=1){
				//lines[a] = intToBytes(dataInt[i], 4)[a];
				System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
		}else if(i == 2){
			System.arraycopy(intToBytes(dataInt[2]+textByte.length+4, 4), 0, lines, i*4, 4);
		}else if(i == 3){
			//lines[i] = intToBytes(dataInt[i], 1)[i];
			System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
		}else if(i == 4){
			System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
		}else if(i>4){
			System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4-6, 4);
		}
	}
	System.arraycopy(intToBytes(textByte.length, 4), 0, lines, 26, 4);
	System.arraycopy(textByte, 0, lines, 30, textByte.length);
	connectServer(lines);
	a++;
	}
	
	
	/**
	 * 用当前时间做各种命令的ID
	 * 
	 * @author  刘星星
	 */
	public static int getId() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"HHmmssSSS");
		return Integer.parseInt(dateFormat.format(date));
	}
	public static byte[] Bitmap2Bytes(Bitmap bm){  
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); 
		return baos.toByteArray();  
		 }  

	/**
	 * 画图片
	 */
	public void sDrawPic(){
		int l = MyApplication.getInstance().getBitmapLeft();
		int t = MyApplication.getInstance().getBitmapTop();
		int id = MyApplication.getInstance().getPicId();
		Bitmap b = null;
		b = MyApplication.getInstance().getBitmap();
		int[] dataInt = {1224,1,34,0,241,id,l,t,b.getWidth(),b.getHeight()};
		byte[] fileNameByte = StringToByte(CompassImage.getStrokeFileName(), "UTF-8");
		byte[] lines = null;
		byte[] fileContentByte = Bitmap2Bytes(BitmapFactory.decodeByteArray(
				MyApplication.getInstance().getDataByte(), 0, 
				MyApplication.getInstance().getDataByte().length));
		lines = new byte[(dataInt[2]+fileNameByte.length)+8+fileContentByte.length];
	for(int i=0;i<dataInt.length;i++){
		if(i<=1){
				//lines[a] = intToBytes(dataInt[i], 4)[a];
				System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
		}else if(i == 2){
			System.arraycopy(intToBytes(dataInt[2]+fileNameByte.length+8+fileContentByte.length, 4), 0, lines, i*4, 4);
		}else if(i == 3){
			//lines[i] = intToBytes(dataInt[i], 1)[i];
			System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
		}else if(i == 4){
			System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
		}else if(i>4){
			System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4-6, 4);
		}
	}
	System.arraycopy(intToBytes(fileNameByte.length, 4), 0, lines, 34, 4);
	System.arraycopy(fileNameByte, 0, lines, 38, fileNameByte.length);
	System.arraycopy(intToBytes(fileContentByte.length, 4), 0, lines, 38+fileNameByte.length, 4);
	System.arraycopy(fileContentByte, 0, lines, 38+fileNameByte.length+4, fileContentByte.length);
	connectServer(lines);
	}
	/**
	 * 更新图片
	 */
	public void sUpdatePic(){
		int l = MyApplication.getInstance().getBitmapLeft();
		int t = MyApplication.getInstance().getBitmapTop();
		int id = MyApplication.getInstance().getPicId();
		int picw = MyApplication.getInstance().getPicW();
		int pich = MyApplication.getInstance().getPicH();
		int[] dataInt = {1225,1,34,0,241,id,l,t,picw,pich};
//		byte[] fileNameByte = StringToByte(CompassImage.getStrokeFileName(), "UTF-8");
		byte[] lines = null;
//		byte[] fileContentByte = Bitmap2Bytes(b);
		lines = new byte[dataInt[2]];//直线数据包
	for(int i=0;i<dataInt.length;i++){
		if(i<=1){
				//lines[a] = intToBytes(dataInt[i], 4)[a];
				System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
		}else if(i == 2){
			System.arraycopy(intToBytes(dataInt[2], 4), 0, lines, i*4, 4);
		}else if(i == 3){
			//lines[i] = intToBytes(dataInt[i], 1)[i];
			System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
		}else if(i == 4){
			System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
		}else if(i>4){
			System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4-6, 4);
		}
	}
	/*System.arraycopy(intToBytes(fileNameByte.length, 4), 0, lines, 34, 4);
	System.arraycopy(fileNameByte, 0, lines, 38, fileNameByte.length);
	System.arraycopy(intToBytes(fileContentByte.length, 4), 0, lines, 38+fileNameByte.length, 4);
	System.arraycopy(fileContentByte, 0, lines, 38+fileNameByte.length+4, fileContentByte.length);*/
	connectServer(lines);
	}
	/**
	* UTF-8 一个汉字占三个字节
	* @param str 源字符串
	* 转换成字节数组的字符串
	* @return
	*/
	public static byte[] StringToByte(String str, String charEncode) {
		byte[] destObj = null;
		try {
			if (null == str || str.trim().equals("")) {
				destObj = new byte[0];
				return destObj;
			} else {
				destObj = str.getBytes(charEncode);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return destObj;
	}
	/**
	 * 给服务器一个指令，让当前服务器的对象画入真实画板
	 */
	public void sendToReal(){
		byte[] lines = null;
		lines = new byte[14];//直线数据包
		int[] dataInt = {1030,1,14,0,241};
		for(int i=0;i<dataInt.length;i++){
			if(i<=2){
					//lines[a] = intToBytes(dataInt[i], 4)[a];
					System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
			}else if(i == 3){
				//lines[i] = intToBytes(dataInt[i], 1)[i];
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
			}else if(i == 4){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
			}
		}
		connectServer(lines);
	}
	/**
	 * Undo 指令
	 */
	public void sendToUndo(){
		byte[] lines = null;
		lines = new byte[14];//直线数据包
		int[] dataInt = {1031,1,14,0,241};
		for(int i=0;i<dataInt.length;i++){
			if(i<=2){
					//lines[a] = intToBytes(dataInt[i], 4)[a];
					System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
			}else if(i == 3){
				//lines[i] = intToBytes(dataInt[i], 1)[i];
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
			}else if(i == 4){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
			}
		}
		connectServer(lines);
	}
	/**
	 * Redo 指令
	 */
	public void sendToRedo(){
		byte[] lines = null;
		lines = new byte[14];//直线数据包
		int[] dataInt = {1032,1,14,0,241};
		for(int i=0;i<dataInt.length;i++){
			if(i<=2){
					//lines[a] = intToBytes(dataInt[i], 4)[a];
					System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
			}else if(i == 3){
				//lines[i] = intToBytes(dataInt[i], 1)[i];
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
			}else if(i == 4){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
			}
		}
		connectServer(lines);
	}
	/**
	 * 取消图片指令
	 */
	public void canclePic(){
		byte[] lines = null;
		lines = new byte[14];//直线数据包
		int[] dataInt = {1033,1,14,0,241};
		for(int i=0;i<dataInt.length;i++){
			if(i<=2){
					//lines[a] = intToBytes(dataInt[i], 4)[a];
					System.arraycopy(intToBytes(dataInt[i], 4), 0, lines, i*4, 4);
			}else if(i == 3){
				//lines[i] = intToBytes(dataInt[i], 1)[i];
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, i*4, 1);
			}else if(i == 4){
				System.arraycopy(intToBytes(dataInt[i], 1), 0, lines, 13, 1);
			}
		}
		connectServer(lines);
	}
	/**
	 * 向服务器发送数据
	 * @param data 向服务器发送的数据包
	 */
	/*public void sendData(byte[] data){
		Socket socket=null;
		DataOutputStream  out1 = null;
		byte[] data1 = new byte[1024];
		try {
			socket=new Socket(connectStr,25000);
			//socket.setSoTimeout(2);
		} catch (UnknownHostException e1) {
			return;
//			e1.printStackTrace();
		} catch (IOException e1) {
			return;
		}
		try {
//			if(socket == null){
				
//				socket.setSoTimeout(4000);
//			}
			//向服务器发送信息
		    out1 =new DataOutputStream(socket.getOutputStream());
			if (socket.isConnected()) {                   
				if (!socket.isOutputShutdown()) {     
				//	String str = android.os.Environment.getExternalStorageDirectory()+"/DCIM/bbb.txt";
					try {
						//byte[] buffer = {100, 4, 0, 0, 1, 0, 0, 0, 34, 0, 0, 0, 0, -15, 20, 0, 0, 0, 79, 0, 0, 0, 33, 1, 0, 0, -13, 2, 0, 0, -50, 0, 0, 0};//getBytes(str);
    						out1.write(data);
    						DataInputStream dis = new DataInputStream(socket.getInputStream());
    						dis.read(data1);
    						int a = data1.length;
					} catch (Exception e) {   
						e.printStackTrace();
					}
					}                 
				}           
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e){
			//Log.e(DEBUG_TAG,e.toString());
		}finally{
			closeSocket(socket, out1);
		}
	}*/
	/**
	 * 关闭socket
	 * @param socket
	 * @param out
	 */
	public void closeSocket(){
		if(out1 !=  null){
			try {
				out1.flush();
				out1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(MyApplication.getInstance().getSocket()!=null){
			try {
				MyApplication.getInstance().getSocket().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(client != null){
			try {
				client.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
