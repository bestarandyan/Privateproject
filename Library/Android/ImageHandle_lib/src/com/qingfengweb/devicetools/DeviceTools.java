/**
 * 
 */
package com.qingfengweb.devicetools;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author 刘星星
 * @createDate 2013、12、31
 * 设备工具箱
 *
 */
public class DeviceTools {
	/**
	 * 显示软键盘
	 * @param et
	 */
	public static void showSoftKey(EditText et){
		et.setFocusable(true);
		et.setFocusableInTouchMode(true);
		et.requestFocus();
		InputMethodManager inputManager =(InputMethodManager)et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(et, 0);
	}
	/**
	 * 隐藏软键盘
	 * @param context
	 */
	public static void disShowSoftKey(Context context,View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	/**
	 * 获取北京时间
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	public static String getBeiJingTime(){
		URL url;
		try {
			url = new URL("http://www.bjtime.cn");
			URLConnection uc=url.openConnection();//生成连接对象
		       uc.connect(); //发出连接
		       long ld=uc.getDate(); //取得网站日期时间
		       Date date=new Date(ld); //转换为标准时间对象
		       //分别取得时间中的小时，分钟和秒，并输出
		       System.out.println(getStrTime(date.getTime()/1000+""));
		       return getStrTime(date.getTime()/1000+"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		}
	// 将字符串转为时间戳

	public static String getTime(String user_time) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d;
		try {
	
		d = sdf.parse(user_time);
		long l = d.getTime();
		String str = String.valueOf(l);
		re_time = str.substring(0, 10);
	
		} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		return re_time;
	}

	/**
	 * 将时间戳转为字符串
	 * @param cc_time
	 * @return
	 */
	public static String getStrTime(String cc_time) {
		String re_StrTime = null;
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
	
		return re_StrTime;

	}
	
	
	 public static String getDate(String month,String day){
	   	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
	  		     java.util.Date d = new java.util.Date(); ;
	  		     String str = sdf.format(d);
	  		    String nowmonth = str.substring(5, 7);
				 String nowday = str.substring(8,10 );
	   	   String result = null;
	   	   
	   		 int temp =   Integer.parseInt(nowday)-Integer.parseInt(day);
	   	     switch (temp) {
				case 0:
					result="今天";
					break;
				case 1:
					result = "昨天";
					break;
				case 2:
					result = "前天";
					break;
				default:
					StringBuilder sb = new StringBuilder();
					sb.append(Integer.parseInt(month)+"月");
					sb.append(Integer.parseInt(day)+"日");
					result  = sb.toString();
					break;
				}
	   	   return result;
	      }
	      public static String getTime(int timestamp){
	   	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  			String time= null;
	  		try {
	  			java.util.Date currentdate = new java.util.Date();//当前时间
	  			
	  			 long i = (currentdate.getTime()/1000-timestamp)/(60);
	  			 System.out.println(currentdate.getTime());
	  			 System.out.println(i);
	  			 Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间
	  			 System.out.println("now-->"+now);//返回结果精确到毫秒。
	  			 
	  			 String str = sdf.format(new Timestamp(IntToLong(timestamp)));
	  			time = str.substring(11, 16);
	  			 
	  			 String month = str.substring(5, 7);
	  			 String day = str.substring(8,10 );
	  			 System.out.println(str);
	  			 System.out.println(time);
	  			 System.out.println(getDate(month, day));
	  			time =getDate(month, day)+ time;
	  		} catch (Exception e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  	 return time;
	      }
	      //java Timestamp构造函数需传入Long型
	      public static long IntToLong(int i){
	    	   	 long result = (long)i;
	    	   	   result*=1000;
	    	   	  return  result;
	    	      }
	      /**
	       * 获取手机当前时间
	       * @return
	       */
	      public static String getCurrentTime(){
	    	  Calendar ca = Calendar.getInstance();
		      int year = ca.get(Calendar.YEAR);//获取年份
		      int month=ca.get(Calendar.MONTH)+1;//获取月份
		      int day=ca.get(Calendar.DATE);//获取日
		      int hour=ca.get(Calendar.HOUR_OF_DAY);//小时
		      int minute=ca.get(Calendar.MINUTE);//分
		      int second=ca.get(Calendar.SECOND);//秒
		  return year+"-"+(month<10?"0"+month:month)+"-"+(day<10?"0"+day:day)+"  "+(hour<10?"0"+hour:hour)+":"+(minute<10?"0"+minute:minute)+":"+(second<10?"0"+second:second);
	      }
	      /**
	       * 获取软件版本号
	       * @param context
	       * @return
	       * @throws Exception
	       */
	      public static String getVersionName(Context context) throws Exception{
	              // 获取packagemanager的实例
	              PackageManager packageManager = context.getPackageManager();
	              // getPackageName()是你当前类的包名，0代表是获取版本信息
	              PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
	              String version = packInfo.versionName;
	      return version;
	      }
	      
	      @SuppressWarnings("deprecation")
		public static void copyText(Context context,String msg){
	    	  if(DeviceTools.getAndroidSDKVersion() >= 11){
					ClipboardManager clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
					 //clip.getText(); // 粘贴
					ClipData clipData = ClipData.newPlainText("copy",msg);
					 clip.setPrimaryClip(clipData);// 复制
				}else{
					android.text.ClipboardManager clip = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
					clip.setText(msg);
				}
	      }
	/**
	 * 获取手机系统中SDK的版本号
	 * @return
	 */
	public static int getAndroidSDKVersion() {
		 int version_sdk = Build.VERSION.SDK_INT; // 设备SDK版本    
		
	 return version_sdk;
    }
	/**
	 * 获取手机的设备型号
	 * @return
	 */
	public static String getDeviceModel(){
		 String device_model = Build.MODEL; // 设备型号    
		return device_model;
	}
	/**
	 * 获取系统版本
	 * @return
	 */
	public static String getDeviceRelease(){
		 String version_release = Build.VERSION.RELEASE; // 设备的系统版本 
		 return version_release;
	}
}
