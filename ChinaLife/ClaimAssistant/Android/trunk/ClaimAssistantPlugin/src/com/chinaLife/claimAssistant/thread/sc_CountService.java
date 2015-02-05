package com.chinaLife.claimAssistant.thread;

import com.chinaLife.claimAssistant.activity.Sc_MessageListActivity;
import com.chinaLife.claimAssistant.activity.Sc_MyApplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class sc_CountService extends Service {

    private boolean threadDisable;

    private int count;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {

            public void run() {
                while (!threadDisable) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    	e.printStackTrace();
                    }
                    count++;
                    Log.v("CountService", "Count is " + count);
                    addNotificaction("根据规定，请选择不同意赔款的原因。。。。");
                }
                NotificationManager manager = (NotificationManager) sc_CountService.this.getSystemService(Context.NOTIFICATION_SERVICE);
        		manager.cancelAll();
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.threadDisable = true;
        Log.v("CountService", "on destroy");
    }

    public int getCount() {
        return count;
    }
    /**
	 * 添加一个notification
	 */
	private void addNotificaction(String msg) {
		NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancelAll();
		// 创建一个Notification
		Notification notification = new Notification();
		// 设置显示在手机最上边的状态栏的图标
		notification.icon = android.R.drawable.ic_notification_overlay;
		// 当当前的notification被放到状态栏上的时候，提示内容
		notification.tickerText = "您有新的消息通知。。。";
		
		/***
		 * notification.contentIntent:一个PendingIntent对象，当用户点击了状态栏上的图标时，该Intent会被触发
		 * notification.contentView:我们可以不在状态栏放图标而是放一个view
		 * notification.deleteIntent 当当前notification被移除时执行的intent
		 * notification.vibrate 当手机震动时，震动周期设置
		 */
		// 添加声音提示
		notification.defaults=Notification.DEFAULT_SOUND;
		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		notification.audioStreamType= android.media.AudioManager.ADJUST_LOWER;
		
		//下边的两个方式可以添加音乐
		//notification.sound = Uri.parse("file:///sdcard/notification/ringer.mp3"); 
		//notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6"); 
		Intent intent = new Intent(this, Sc_MessageListActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		// 点击状态栏的图标出现的提示信息设置
		if(msg.length()>10){
			msg = msg.substring(0, 10)+"....";
		}
		notification.setLatestEventInfo(this, "内容提示：", msg, pendingIntent);
		manager.notify(1, notification);
		
	}

}
