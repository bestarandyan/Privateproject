package com.chinaLife.claimAssistant.activity;
//package com.chinaLife.claimAssistant;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.apache.log4j.DailyRollingFileAppender;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PatternLayout;
//
//import android.app.Application;
//import android.os.Environment;
//import android.widget.Toast;
//
//import com.baidu.mapapi.*;
//import com.baidu.mapapi.map.MKEvent;
//
//import de.mindpipe.android.logging.log4j.LogConfigurator;
//public class BMapApiDemoApp extends Application {
//	
//	@Override
//    public void onCreate() {
//		String filename = Environment.getExternalStorageDirectory()  
//                + File.separator + "MyApp" + File.separator + "logs"  
//                + File.separator + "client_";
//		File a = android.os.Environment.getExternalStorageDirectory();
//		File b = android.os.Environment.getRootDirectory();
//		if (android.os.Environment.getExternalStorageState().equals(
//				android.os.Environment.MEDIA_MOUNTED)){
//			LogConfigurator logConfigurator = new LogConfigurator();  
//	        logConfigurator.setFileName(filename);  
//	        logConfigurator.setRootLevel(Level.INFO);  
//	        //logConfigurator.setLevel("org.apache", Level.ERROR);  
//	       // logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n"); 
//	        PatternLayout laout=new PatternLayout("%d{yyyy-MM-dd HH\\:mm\\:ss}  [%t] %-5p %c %x - %m%n");
//	        org.apache.log4j.DailyRollingFileAppender fileappender = null;
//			try {
//				fileappender = new DailyRollingFileAppender(laout,filename, "yyyy-MM-dd'.log'");
//				fileappender.setAppend(true);
//			} catch (IOException e) {
//				e.printStackTrace();
//				return;
//				
//			}
//	       // logConfigurator.setMaxFileSize(1024 * 1024 * 5);  
//	        logConfigurator.setImmediateFlush(true);  
//	        logConfigurator.configure();  
//	        Logger log = Logger.getLogger(MyApplication.class); 
//	        log.addAppender(fileappender);
//	        MyApplication.opLogger = log;
//		}
//			
//		super.onCreate();
//		
//	}
//	@Override
//	//建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
//	public void onTerminate() {
//		super.onTerminate();
//	}
//
//}
