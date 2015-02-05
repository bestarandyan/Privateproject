/**
 * 
 */
package com.qingfengweb.weddingideas.devicetools;

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
 * @author ������
 * @createDate 2013��12��31
 * �豸������
 *
 */
public class DeviceTools {
	/**
	 * ��ʾ�����
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
	 * ���������
	 * @param context
	 */
	public static void disShowSoftKey(Context context,View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	/**
	 * ��ȡ����ʱ��
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	public static String getBeiJingTime(){
		URL url;
		try {
			url = new URL("http://www.bjtime.cn");
			URLConnection uc=url.openConnection();//������Ӷ���
		       uc.connect(); //��������
		       long ld=uc.getDate(); //ȡ����վ����ʱ��
		       Date date=new Date(ld); //ת��Ϊ��׼ʱ�����
		       //�ֱ�ȡ��ʱ���е�Сʱ�����Ӻ��룬�����
		       System.out.println(getStrTime(date.getTime()/1000+""));
		       return getStrTime(date.getTime()/1000+"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		}
	// ���ַ�תΪʱ���

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
	 * ��ʱ���תΪ�ַ�
	 * @param cc_time
	 * @return
	 */
	public static String getStrTime(String cc_time) {
		String re_StrTime = null;
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// ���磺cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
	
		return re_StrTime;

	}
	
	
	 public static String getDate(String month,String day){
	   	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24Сʱ��
	  		     java.util.Date d = new java.util.Date(); ;
	  		     String str = sdf.format(d);
	  		    String nowmonth = str.substring(5, 7);
				 String nowday = str.substring(8,10 );
	   	   String result = null;
	   	   
	   		 int temp =   Integer.parseInt(nowday)-Integer.parseInt(day);
	   	     switch (temp) {
				case 0:
					result="����";
					break;
				case 1:
					result = "����";
					break;
				case 2:
					result = "ǰ��";
					break;
				default:
					StringBuilder sb = new StringBuilder();
					sb.append(Integer.parseInt(month)+"��");
					sb.append(Integer.parseInt(day)+"��");
					result  = sb.toString();
					break;
				}
	   	   return result;
	      }
	      public static String getTime(int timestamp){
	   	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  			String time= null;
	  		try {
	  			java.util.Date currentdate = new java.util.Date();//��ǰʱ��
	  			
	  			 long i = (currentdate.getTime()/1000-timestamp)/(60);
	  			 System.out.println(currentdate.getTime());
	  			 System.out.println(i);
	  			 Timestamp now = new Timestamp(System.currentTimeMillis());//��ȡϵͳ��ǰʱ��
	  			 System.out.println("now-->"+now);//���ؽ��ȷ�����롣
	  			 
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
	      //java Timestamp���캯���贫��Long��
	      public static long IntToLong(int i){
	    	   	 long result = (long)i;
	    	   	   result*=1000;
	    	   	  return  result;
	    	      }
	      /**
	       * ��ȡ�ֻ�ǰʱ��
	       * @return
	       */
	      public static String getCurrentTime(){
	    	  Calendar ca = Calendar.getInstance();
		      int year = ca.get(Calendar.YEAR);//��ȡ���
		      int month=ca.get(Calendar.MONTH)+1;//��ȡ�·�
		      int day=ca.get(Calendar.DATE);//��ȡ��
		      int hour=ca.get(Calendar.HOUR_OF_DAY);//Сʱ
		      int minute=ca.get(Calendar.MINUTE);//��
		      int second=ca.get(Calendar.SECOND);//��
		  return year+"-"+(month<10?"0"+month:month)+"-"+(day<10?"0"+day:day)+"  "+(hour<10?"0"+hour:hour)+":"+(minute<10?"0"+minute:minute)+":"+(second<10?"0"+second:second);
	      }
	      /**
	       * ��ȡ����汾��
	       * @param context
	       * @return
	       * @throws Exception
	       */
	      public static String getVersionName(Context context) throws Exception{
	              // ��ȡpackagemanager��ʵ��
	              PackageManager packageManager = context.getPackageManager();
	              // getPackageName()���㵱ǰ��İ���0����ǻ�ȡ�汾��Ϣ
	              PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
	              String version = packInfo.versionName;
	      return version;
	      }
	      
	      @SuppressWarnings("deprecation")
		public static void copyText(Context context,String msg){
	    	  if(DeviceTools.getAndroidSDKVersion() >= 11){
					ClipboardManager clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
					 //clip.getText(); // ճ��
					ClipData clipData = ClipData.newPlainText("copy",msg);
					 clip.setPrimaryClip(clipData);// ����
				}else{
					android.text.ClipboardManager clip = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
					clip.setText(msg);
				}
	      }
	/**
	 * ��ȡ�ֻ�ϵͳ��SDK�İ汾��
	 * @return
	 */
	public static int getAndroidSDKVersion() {
		 int version_sdk = Build.VERSION.SDK_INT; // �豸SDK�汾    
		
	 return version_sdk;
    }
	/**
	 * ��ȡ�ֻ���豸�ͺ�
	 * @return
	 */
	public static String getDeviceModel(){
		 String device_model = Build.MODEL; // �豸�ͺ�    
		return device_model;
	}
	/**
	 * ��ȡϵͳ�汾
	 * @return
	 */
	public static String getDeviceRelease(){
		 String version_release = Build.VERSION.RELEASE; // �豸��ϵͳ�汾 
		 return version_release;
	}
}
