/**
 * 
 */
package com.qingfengweb.lottery.activity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.fragment.FragmentForLotteryType;
import com.qingfengweb.lottery.util.DeviceTool;
import com.qingfengweb.lottery.util.NetworkCheck;
import com.qingfengweb.lottery.util.ShakeListener;
import com.qingfengweb.lottery.util.ShakeListener.OnShakeListener;
import com.qingfengweb.lottery.util.StringUtils;
import com.qingfengweb.lottery.view.KeyboardListenRelativeLayout;
import com.qingfengweb.lottery.view.KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener;

/**
 * @author 刘星星
 * @createDate 2013、11、22
 *
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint({ "ResourceAsColor", "ShowToast", "HandlerLeak", "SimpleDateFormat" })
public class KuaiSanActivity extends Activity implements OnClickListener,IOnKeyboardStateChangedListener{
	ViewPager xuanLayout;
	public ArrayList<View> pageViews;
//	TextView hezhiTv,santongTv,ertongTv,sanbutongTv,erbutongTv;
//	TextView[] tvArray;//用数组处理按钮样式
	LinearLayout hzTv1,hzTv2,hzTv3,hzTv4,hzTv5,hzTv6,hzTv7,hzTv8,hzTv9,hzTv10,hzTv11,hzTv12,hzTv13,hzTv14;//和值的选择按钮
	TextView hzDaTv,hzXiaoTv,hzDanTv,hzShuangTv;//和值的大小单双按钮
	LinearLayout[] hzTvArray;//和值数组
	int[] hzTvBonus;//和值的奖金数组
	int[] hzNumbers;//和值的号码
	TextView santongLayout1,santongLayout2,santongLayout3,santongLayout4,santongLayout5,santongLayout6;
	LinearLayout tongxuanLayout;
	View[] stTvArray;//三同数组
	int[] stNumbers;//三同的号码
	TextView ertongLayout1,ertongLayout2,ertongLayout3,ertongLayout4,ertongLayout5,ertongLayout6,ertongLayout7,ertongLayout8,ertongLayout9,ertongLayout10,
	ertongLayout11,ertongLayout12,ertongLayout13,ertongLayout14,ertongLayout15,ertongLayout16,ertongLayout17,ertongLayout18,ertongLayout19,ertongLayout20,
	ertongLayout21,ertongLayout22,ertongLayout23,ertongLayout24,ertongLayout25,ertongLayout26,ertongLayout27,ertongLayout28,ertongLayout29,ertongLayout30,
	ertongLayout31,ertongLayout32,ertongLayout33,ertongLayout34,ertongLayout35,ertongLayout36;
	TextView[] etTvArray;//二同数组
	int[] ertNumbers;//二同的号码
	TextView sanbutongLayout1,sanbutongLayout2,sanbutongLayout3,sanbutongLayout4,sanbutongLayout5,
			sanbutongLayout6,sanbutongLayout7,sanbutongLayout8,sanbutongLayout9,sanbutongLayout10,
			sanbutongLayout11,sanbutongLayout12,sanbutongLayout13,sanbutongLayout14,sanbutongLayout15,
			sanbutongLayout16,sanbutongLayout17,sanbutongLayout18,sanbutongLayout19,sanbutongLayout20;
	TextView[] sbtTvArray;//三不同数组
	int[] sbtNumbers;//三不同号码
	TextView erbutongLayout1,erbutongLayout2,erbutongLayout3,erbutongLayout4,erbutongLayout5,
		erbutongLayout6,erbutongLayout7,erbutongLayout8,erbutongLayout9,erbutongLayout10,
		erbutongLayout11,erbutongLayout12,erbutongLayout13,erbutongLayout14,erbutongLayout15;
	TextView[] erbtTvArray;// 二不同数组
	int[] erbtNumbers;//二不同号码
	View[] sLianArray;//三连号控件数组
	int[] sLianNumbers;//三连号号码
	LinearLayout sLianTongxuan;
	TextView sLianLayout1,sLianLayout2,sLianLayout3,sLianLayout4;
	LinearLayout zhuiLayout;
	Button zhuihaoBtn,payBtn;//追号按钮   付款按钮
	TextView zhuNumberTv,payMoneyTv,bonuxInfoTv;//头的注数，我要支付的钱，得奖信息
//	RelativeLayout bonuxLayout;
	LinearLayout payLayout;
	int zhuNumber = 0;//当前选中的注的数量
	int payMoney = 0;//当前选择的彩票，我应该支付的钱
	int minBonux = 0;//最小奖金
	int maxBonux = 0;//最大奖金
	int typeSelect = 1;//选择方式  1：和值  2：三同  3：二同  4：三不同  5:三连号通选   6：二不同
	View hzView,santongView,ertongView,sanBuTongView,erBuTongView,sLianView;
	ImageView szImg1,szImg2,szImg3;
	ImageView szImg11,szImg12,szImg13,szImg14,szImg15;
	ImageView[] szImg1Array=null;
	ImageView szImg21,szImg22,szImg23;
	ImageView[] szImg2Array=null;
	ImageView szImg31,szImg32,szImg33;
	ImageView[] szImg3Array=null;
	int[] saImgSrcArray = null;
	SensorManager mSensorManager01;
	Sensor sensor;
	
	ShakeListener mShakeListener = null;
	RotateAnimation diceAnimation;
	// public Button button, button1;
	private Random random1, random2, random3;
	private int dicenumbe1, dicenumbe2, dicenumbe3;
	public long lasttime;
	public final long timeDifference = 1500;
	private LinearLayout infoLayout1;
	RelativeLayout infoLayout2;//开奖和截止时间信息的布局
	int m = 0;
	boolean isOver = true;
	ImageButton yaoImg;
	private EditText qiEt,beiEt;
	int fuxuanNumber = 0;//二同中的复选号
	int danxuanNumber = 0;//二同号中的单选号
	int selectedNumber = 0;//三不同号中 选择三不同号的住数
	TextView timeTV;//倒计时
	RelativeLayout szLayout;//骰子的布局
	Timer timer = new Timer();  
//	public int time = 600000;
	String timeStr = "";//当前剩的时间
	TextView qiandbeiTv;
	private ProgressDialog progressdialog;//进度条
	TextView jiezhiTV,preQi;//截止期数
	public static int countQishu = 1;//总共期数
	public static int countBeishu = 1;//总共倍数
	public static int countZhushu = 1;//总共注数
	public static int countPayMoney = 2;//总共要支付的钱
	public static KuaiSanActivity kuaiSanActivity = null;
	Dialog alert = null;
	int[] ertongId=null;//二同控件id数组
	int[] sanbutongId=null;//二同控件id数组
	int[] erbutongId=null;
	int[] sanLianId=null;
	RelativeLayout bottomLayout;
	ScrollView scrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_kuaisan);
		findview();
		initData();
		kuaiSanActivity = this;
//		mSensorManager01 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//		sensor = mSensorManager01.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//		mSensorManager01.registerListener(lsn, sensor,SensorManager.SENSOR_DELAY_UI);
		
		mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				mShakeListener.stop();
//				sndPool.play(soundPoolMap.get(0), (float) 1, (float) 1, 0, 0,(float) 1.2);
				new Handler().postDelayed(new Runnable(){
					public void run(){
						//Toast.makeText(getApplicationContext(), "抱歉，暂时没有找到\n在同一时刻摇一摇的人。\n再试一次吧！", 500).setGravity(Gravity.CENTER,0,0).show();
//						sndPool.play(soundPoolMap.get(1), (float) 1, (float) 1, 0, 0,(float) 1.0);
//						Toast mtoast;
							   //mtoast.setGravity(Gravity.CENTER, 0, 0);
						mShakeListener.start();
						long current = System.currentTimeMillis();
						long difftime = current - lasttime;
						if (difftime < timeDifference || !isOver)
							return;
						yaodong(current);
//							   mVibrator.cancel();
							   
							   
					}
				}, 100);
			}
		});
		if(NetworkCheck.IsHaveInternet(this)){
			timeStr = StringUtils.calculatTimeNoHour(MyApplication.getInstance().getCurrentSurplusTime());
			timeTV.setText(timeStr+"");
			jiezhiTV.setText(FragmentForLotteryType.qishu);
			int preQinumber = Integer.parseInt(FragmentForLotteryType.currentQi.getText().toString().substring(FragmentForLotteryType.currentQi.getText().toString().length()-2));
			preQi.setText("0"+preQinumber);
			timer.schedule(task, 1000, 1000);       // timeTask  
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("没有检测到网络，请检查您的网络连接！")
			.setTitle("提示！")
			       .setCancelable(false)
			       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   alert.dismiss();
			           }
			       });
			 alert = builder.create();
			alert.show();
		}
		
	}
	 TimerTask task = new TimerTask() {  
	        @Override  
	        public void run() {  
	          handler.sendEmptyMessage(2);
	        }  
	    };  
	    
	    @Override
		protected void onDestroy() {
			super.onDestroy();
			if (mShakeListener != null) {
				mShakeListener.stop();
			}
			DeviceTool.disShowSoftKey(this, qiEt);
			DeviceTool.disShowSoftKey(this, beiEt);
		}
	private final SensorEventListener lsn = new SensorEventListener() {
		public void onSensorChanged(SensorEvent e) {
			// if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float x = e.values[0];
			float y = e.values[1];
			float z = e.values[2];
			long current = System.currentTimeMillis();
			long difftime = current - lasttime;
			if (difftime < timeDifference || !isOver)
				return;
			 int medumValue = 10;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了  
	            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {  
	            
//	            }  
//			if (Math.abs(x) + Math.abs(y) + Math.abs(z) > 30) {
				System.out.println("2222222222222222222222");
				yaodong(current);
			} else {
				if (m == 1) {
					try {
						//TODO abcd
						stopTheDice();
						m = 0;
					} catch (Exception e1) {
						// TODO 异常处理
						e1.printStackTrace();
					}

				}
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};
	@SuppressWarnings("static-access")
	private void yaodong(long current){
		lasttime = current;
		szImg1.setVisibility(View.VISIBLE);
		szImg2.setVisibility(View.VISIBLE);
		if(typeSelect!=5){
			szImg3.setVisibility(View.VISIBLE);
		}else{
			szImg3.setVisibility(View.INVISIBLE);
		}
		media = new MediaPlayer().create(KuaiSanActivity.this, R.raw.rotate);
		if(media!=null && !media.isPlaying()){
			media.start();
		}else{
			media = null;
			media = new MediaPlayer().create(KuaiSanActivity.this, R.raw.rotate);
			if(media!=null && !media.isPlaying()){
				media.start();
			}
		}
		rollTheDice();
		m = 1;
		isOver = false;
	}
	public void setBitmap(ImageView img, int point) {
		Drawable d = null;
		if (point == 1) {
			d = getResources().getDrawable(R.drawable.dice1);
		} else if (point == 2) {
			d = getResources().getDrawable(R.drawable.dice2);
		} else if (point == 3) {
			d = getResources().getDrawable(R.drawable.dice3);
		} else if (point == 4) {
			d = getResources().getDrawable(R.drawable.dice4);
		} else if (point == 5) {
			d = getResources().getDrawable(R.drawable.dice5);
		} else if (point == 6) {
			d = getResources().getDrawable(R.drawable.dice6);
		}
		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap bitmap = bd.getBitmap();
		img.setImageBitmap(bitmap);
	}
	MediaPlayer media = null;
	/**
	 * 旋转动画
	 */
	public void rollTheDice() {
		random1 = new Random();
		dicenumbe1 = random1.nextInt(6) + 1;

		random2 = new Random();
		dicenumbe2 = random2.nextInt(6) + 1;

		random3 = new Random();
		dicenumbe3 = random3.nextInt(6) + 1;
		if(typeSelect == 1 && dicenumbe1 == dicenumbe2 && dicenumbe2 == dicenumbe3){//和值
			rollTheDice();
			return;
		}else if(typeSelect == 2){//三同
			dicenumbe2 = dicenumbe3 = dicenumbe1;
		}else if(typeSelect == 3){//二同
			dicenumbe2 = dicenumbe1;
			if(dicenumbe3 == dicenumbe1){
				rollTheDice();
				return;
			}
		}else if(typeSelect == 4){//三不同
			if(dicenumbe1 == dicenumbe2 ||dicenumbe1 == dicenumbe3 || dicenumbe2 == dicenumbe3){
				rollTheDice();
				return;
			}
		}else if(typeSelect == 5){//三连号
			int[] sanlianArray = new int[]{dicenumbe1,dicenumbe2,dicenumbe3};
			Arrays.sort(sanlianArray);
			if((sanlianArray[0]+sanlianArray[2]) != sanlianArray[1]*2 || (sanlianArray[0]+2)!=sanlianArray[2]){
				rollTheDice();
				return;
			}else{
				dicenumbe1 = sanlianArray[0];dicenumbe2 = sanlianArray[1];dicenumbe3 = sanlianArray[2];
			}
		}else if(typeSelect == 6){//二不同
			if(dicenumbe1 == dicenumbe2){
				rollTheDice();
				return;
			}
		}
//		setViewLayout();
		shakePhone(600);//震动手机
		new Thread(animationRunnable).start();
	}
	Runnable animationRunnable = new Runnable(){
	
		@Override
		public void run() {
			handler.sendEmptyMessageDelayed(5, 1000);
			handler.sendEmptyMessage(1);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendEmptyMessage(1);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendEmptyMessage(1);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendEmptyMessage(1);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendEmptyMessage(1);
		}
	};
	private void setViewScroll(final ImageView img){
		Random random1 = new Random();
		int index = random1.nextInt(8);
		img.setImageResource(saImgSrcArray[index]);
		AnimationSet sa = new AnimationSet(false);
		RotateAnimation ra = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.4f, Animation.RELATIVE_TO_SELF,
				0.4f);
		sa.addAnimation(ra);
		ra.setDuration(200);
		ra.setFillAfter(true);
		ra.setFillBefore(false);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setRepeatMode(Animation.RESTART);
		Random random = new Random();
		int f = random.nextInt(8);
		int tox = 0;
		int toy = 0;
		int[]  location = new int[2];
		img.getLocationInWindow(location);
		System.out.println("f======"+f);
		if(f == 0){
			tox = 0;
			toy = -80;
		}else if(f == 1){
			tox = -80;
			toy = -80;
		}else if(f == 2){
			tox = -80;
			toy = 0;
		}else if(f == 3){
			tox = -80;
			toy = 80;
		}else if(f == 4){
			tox = 0;
			toy = 120;
		}else if(f == 5){
			tox = 100;
			toy = 100;
		}else if(f == 6){
			tox = 100;
			toy = 0;
		}else if(f == 7){
			tox = 100;
			toy = -100;
		} 
	    Animation mTranslateAnimation = new TranslateAnimation(0, tox,0,toy);// 移动
	    mTranslateAnimation.setDuration(200);
	    sa.addAnimation(mTranslateAnimation);
	    sa.setDuration(200);
		mTranslateAnimation.setFillAfter(true);
		img.startAnimation(sa);
		sa.setAnimationListener(new Animation.AnimationListener() {
	        @Override
	        public void onAnimationStart(Animation animation) {
	        }
	        
	        @Override
	        public void onAnimationRepeat(Animation animation) {
	        }
	        
	        @Override
	        public void onAnimationEnd(Animation animation) {
	        	img.clearAnimation();
//	        	handler.sendEmptyMessageDelayed(0, 1000);
//	        	handler.sendEmptyMessageDelayed(1, 200);
	        }
	    });
	}
	
	public void slideview(final View fromview,final View toView) {
		Animation mScaleAnimation = new ScaleAnimation(1f, 0.0f, 1f,
                0.0f,// 整个屏幕就0.0到1.0的大小//缩放
                Animation.INFINITE, 0.5f,
                Animation.INFINITE, 0.5f);
        mScaleAnimation.setDuration(1000);
        mScaleAnimation.setFillAfter(true);
        int[] locationArray = new int[2];
        toView.getLocationInWindow(locationArray);
        int tox = locationArray[0]-fromview.getLeft()+toView.getWidth()/2;
        final int toy = locationArray[1]-fromview.getTop();
        
        Animation mTranslateAnimation = new TranslateAnimation(0, tox,0,toy);// 移动
        mTranslateAnimation.setDuration(1000);
        AnimationSet mAnimationSet=new AnimationSet(false);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.setFillAfter(true);
        mAnimationSet.addAnimation(mTranslateAnimation);
        fromview.startAnimation(mAnimationSet);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }
        
        @Override
        public void onAnimationRepeat(Animation animation) {
        }
        
        @Override
        public void onAnimationEnd(Animation animation) {
        	if(media!=null){
        		media.stop();
        	}
            fromview.setVisibility(View.INVISIBLE);
            Message msg = handler.obtainMessage(8, toy, 0);
            handler.sendMessageDelayed(msg, 300);
        }
    });
   
}
	Handler handler = new Handler(){

		@SuppressWarnings("static-access")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				if(media!=null){
					media.stop();
					media = null;
				}
				media = new MediaPlayer().create(KuaiSanActivity.this, R.raw.location);
				if(media!=null){
					media.start();
				}else{
					media = null;
					media = new MediaPlayer().create(KuaiSanActivity.this, R.raw.location);
					if(media!=null){
						media.start();
					}
				}
				if(typeSelect == 1){
//					Toast.makeText(KuaiSanActivity.this, dicenumbe1+"+"+dicenumbe2+"+"+dicenumbe3+"="+(dicenumbe1+dicenumbe2+dicenumbe3), Toast.LENGTH_LONG).show();
				}
				
				if(typeSelect == 1){//和值
					int hezhi = dicenumbe1+dicenumbe2+dicenumbe3;
					slideview(szImg1,hzTvArray[hezhi-4]);
					slideview(szImg2,hzTvArray[hezhi-4]);
					slideview(szImg3,hzTvArray[hezhi-4]);
					setLayoutBg(hzTvArray,hezhi-4,-1,-1);
				}else if(typeSelect == 2){//三同号
					slideview(szImg1,stTvArray[dicenumbe1-1]);
					slideview(szImg2,stTvArray[dicenumbe1-1]);
					slideview(szImg3,stTvArray[dicenumbe1-1]);
					setLayoutBg(stTvArray,dicenumbe1-1,-1,-1);
				}else if(typeSelect == 3){//二同号
					slideview(szImg1,etTvArray[locationView()]);
					slideview(szImg2,etTvArray[locationView()]);
					slideview(szImg3,etTvArray[locationView()]);
					setLayoutBg(etTvArray,locationView(),-1,-1);
				}else if(typeSelect == 4){//三不同号
					slideview(szImg1,sbtTvArray[locationSBTView()]);
					slideview(szImg2,sbtTvArray[locationSBTView()]);
					slideview(szImg3,sbtTvArray[locationSBTView()]);
					setLayoutBg(sbtTvArray,locationSBTView(),locationSBTView(),locationSBTView());
				}else if(typeSelect == 5){//三连号
					slideview(szImg1,sLianArray[dicenumbe1]);
					slideview(szImg2,sLianArray[dicenumbe1]);
					slideview(szImg3,sLianArray[dicenumbe1]);
					setLayoutBg(sLianArray,dicenumbe1,dicenumbe1,dicenumbe1);
				}else if(typeSelect == 6){//二不同号
					slideview(szImg1,erbtTvArray[locationEBTView()]);
					slideview(szImg2,erbtTvArray[locationEBTView()]);
//					slideview(szImg3,erbtTvArray[dicenumbe3-1]);
					setLayoutBg(erbtTvArray,locationEBTView(),locationEBTView(),locationEBTView());
				}
			}else if(msg.what == 1){
				szImg1.clearAnimation();
	        	szImg2.clearAnimation();
	        	setViewScroll(szImg1);
	        	setViewScroll(szImg2);
	        	if(typeSelect == 6){
	        		szImg3.setVisibility(View.INVISIBLE);
	        	}else{
	        		szImg3.setVisibility(View.VISIBLE);
	        		szImg3.clearAnimation();
		        	setViewScroll(szImg3);
	        	}
			}else if(msg.what == 2){//倒计时   每一秒执行这里一遍
//				if(time<=0){
//					timeTV.setText("20131118013期停止投注");
//				}else{
//					time-=1000;
					timeStr = StringUtils.calculatTimeNoHour(MyApplication.getInstance().getCurrentSurplusTime());
					timeTV.setText(timeStr+"");
					if(FragmentForLotteryType.qishu!=null && FragmentForLotteryType.qishu.length()==10){
						jiezhiTV.setText(FragmentForLotteryType.qishu);
						int preQinumber = Integer.parseInt(FragmentForLotteryType.currentQi.getText().toString().substring(FragmentForLotteryType.currentQi.getText().toString().length()-2));
						preQi.setText("0"+preQinumber);
					}
//				}
				
			}else if(msg.what == 3){//获取订单成功跳入订单详情界面
//				progressdialog.dismiss();
				getOrder();
			}else if(msg.what == 4){
				progressdialog.dismiss();
				Toast.makeText(KuaiSanActivity.this, "生成订单失败", 2000).show();
			}else if(msg.what == 5){
				stopTheDice();
				handler.sendEmptyMessageDelayed(0, 500);
				handler.sendEmptyMessageDelayed(7, 1500);
			}else if(msg.what == 6){//设置骰子的布局信息，
				RelativeLayout.LayoutParams paramLayout = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						MyApplication.getInstance().getScreenH()-bottomLayout.getHeight());
				szLayout.setLayoutParams(paramLayout);
				
				RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				param1.setMargins(MyApplication.getInstance().getScreenW()/2-szImg1.getWidth()-10,
						MyApplication.getInstance().getScreenH()/2-szImg1.getHeight()-10, 0, 0);
				szImg1.setLayoutParams(param1);
				
				RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				param2.setMargins(MyApplication.getInstance().getScreenW()/2+10, MyApplication.getInstance().getScreenH()/2-szImg1.getHeight()-10, 0, 0);
				szImg2.setLayoutParams(param2);
				
				RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				param3.setMargins(MyApplication.getInstance().getScreenW()/2-(szImg3.getWidth()/2), szImg3.getHeight()+MyApplication.getInstance().getScreenH()/2-szImg1.getHeight()-10, 0, 0);
				szImg3.setLayoutParams(param3);
			}else if(msg.what == 7){
				isOver = true;
			}else if(msg.what == 8){
//				hzView,santongView,ertongView,sanBuTongView,erBuTongView,sLianView
				if(typeSelect == 1){
					scrollView = (ScrollView) hzView.findViewById(R.id.scrollView);
				}else if(typeSelect == 2){
					scrollView = (ScrollView) santongView.findViewById(R.id.scrollView);
				}else if(typeSelect == 3){
					scrollView = (ScrollView) ertongView.findViewById(R.id.scrollView);
				}else if(typeSelect == 4){
					scrollView = (ScrollView) sanBuTongView.findViewById(R.id.scrollView);
				}else if(typeSelect == 5){
					scrollView = (ScrollView) sLianView.findViewById(R.id.scrollView);
				}else if(typeSelect == 6){
					scrollView = (ScrollView) erBuTongView.findViewById(R.id.scrollView);
				}
				scrollView.scrollTo(0, msg.arg1);
			}else if(msg.what == 102){//token超时 或者错误
				progressdialog.dismiss();
				Toast.makeText(getApplicationContext(), getString(R.string.again_login_str), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(KuaiSanActivity.this,LoginActivity.class);
				intent.putExtra("username", MyApplication.getInstance().getCurrentUserName());
				startActivity(intent);
			}
			super.handleMessage(msg);
		}
		
	};
	/**
	 * 根据当前随机号计算二同号单选中控件的位置
	 * @return
	 */
	private int locationView(){
		int location = 0;
		String number = "";
		if(dicenumbe1 > dicenumbe3){
			number = dicenumbe3+""+dicenumbe1+""+dicenumbe2;
		}else{
			number = dicenumbe2+""+dicenumbe1+""+dicenumbe3;
		}
		for(int i=0;i<etTvArray.length;i++){
			if(number.equals(etTvArray[i].getText().toString())){
				location = i;
				return location;
			}
		}
		return location;
	}
	/**
	 * 根据当前随机号计算三不同号中控件的位置
	 * @return
	 */
	@SuppressWarnings("unused")
	private int locationSBTView(){
		int location = 0;
		String number = "";
		int temp = 0;
		int[] dicenumbe = new int[]{dicenumbe1,dicenumbe2,dicenumbe3};
		Arrays.sort(dicenumbe);
		number = dicenumbe[0]+""+dicenumbe[1]+""+dicenumbe[2];
		for(int i=0;i<sbtTvArray.length;i++){
			if(number.equals(sbtTvArray[i].getText().toString())){
				location = i;
				return location;
			}
		}
		return location;
	}
	/**
	 * 根据当前随机号计算二不同号中控件的位置
	 * @return
	 */
	@SuppressWarnings("unused")
	private int locationEBTView(){
		int location = 0;
		String number = "";
		if(dicenumbe1>dicenumbe2){
			number = dicenumbe2+""+dicenumbe1;
		}else{
			number = dicenumbe1+""+dicenumbe2;
		}
		for(int i=0;i<erbtTvArray.length;i++){
			if(number.equals(erbtTvArray[i].getText().toString())){
				return i;
			}
		}
		
		return location;
	}
	/**
	 * 设置按钮的背景色  和是否被选中
	 * @param tvArray
	 * @param index1
	 * @param index2
	 * @param index3
	 */
	private void setLayoutBg(View[] layoutArray,int index1,int index2,int index3){
		for(int i=0;i<layoutArray.length;i++){//这一步的目的是先将所有按钮设置为未选中状态，防止在没来得及改变的时候，前面被选中过的号码就参与运算了
			layoutArray[i].setSelected(false);
		}
		for(int i=0;i<layoutArray.length;i++){
			if(i == index1 || i==index2 || i==index3){
				layoutArray[i].setSelected(true);
				layoutArray[i].setBackgroundColor(Color.parseColor("#71A93A"));
//				tvArray[i].setTextColor(Color.parseColor("#FED700"));
				setAllLayoutView(layoutArray[i]);
			}else{
				layoutArray[i].setSelected(false);
				layoutArray[i].setBackgroundColor(Color.parseColor("#29915E"));
//				tvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
			}
			
		}
	}
	public void stopTheDice() {
		szImg1.clearAnimation();
		setBitmap(szImg1, dicenumbe1);
		szImg2.clearAnimation();
		setBitmap(szImg2, dicenumbe2);
		szImg3.clearAnimation();
		setBitmap(szImg3, dicenumbe3);
		media.stop();
		mVibrator.cancel();
	}

	
	private void findview(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		timeTV = (TextView) findViewById(R.id.timeTV);
		xuanLayout =  (ViewPager) findViewById(R.id.xuanLayout);
		zhuihaoBtn = (Button) findViewById(R.id.zhuihaoBtn);
		payBtn = (Button) findViewById(R.id.payBtn);
		zhuNumberTv = (TextView) findViewById(R.id.zhunumberTv);
		payMoneyTv = (TextView) findViewById(R.id.payMoney);
		bonuxInfoTv = (TextView) findViewById(R.id.bonuxTv);
		payLayout = (LinearLayout) findViewById(R.id.payLayout);
		infoLayout1 = (LinearLayout) findViewById(R.id.infoLayout1);
		infoLayout2 = (RelativeLayout) findViewById(R.id.infoLayout2);
		yaoImg = (ImageButton) findViewById(R.id.yaoImg);
		qiandbeiTv = (TextView) findViewById(R.id.qiandbeiTv);
		yaoImg.setOnClickListener(this);
		zhuiLayout = (LinearLayout) findViewById(R.id.zhuiLayout);
		qiEt = (EditText) findViewById(R.id.qiEt);
		beiEt = (EditText) findViewById(R.id.beiEt);
		qiEt.addTextChangedListener(tWatcher);
		beiEt.addTextChangedListener(tWatcher);
		szLayout = (RelativeLayout) findViewById(R.id.szLayout);
		jiezhiTV = (TextView) findViewById(R.id.jiezhiTV);
		preQi = (TextView) findViewById(R.id.kaijiangTv);
		bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
//		bonuxLayout = (RelativeLayout) findViewById(R.id.zhongjiangLayout);
		zhuihaoBtn.setOnClickListener(this);
		payBtn.setOnClickListener(this);
		findViewById(R.id.zhuiHaoBtn).setOnClickListener(this);
		szImg1 = (ImageView) findViewById(R.id.img1);
		szImg2 = (ImageView) findViewById(R.id.img2);
		szImg3 = (ImageView) findViewById(R.id.img3);
		KeyboardListenRelativeLayout layout = (KeyboardListenRelativeLayout) findViewById(R.id.parent);
		layout.setOnKeyboardStateChangedListener(this);
//		scrollView = (ScrollView) findViewById(R.id.scrollView);
//		szImg11 = (ImageView) findViewById(R.id.img11);
//		szImg12 = (ImageView) findViewById(R.id.img12);
//		szImg13 = (ImageView) findViewById(R.id.img13);
//		szImg14 = (ImageView) findViewById(R.id.img14);
//		szImg15 = (ImageView) findViewById(R.id.img15);
//		szImg21 = (ImageView) findViewById(R.id.img21);
//		szImg22 = (ImageView) findViewById(R.id.img22);
//		szImg23 = (ImageView) findViewById(R.id.img23);
//		szImg31 = (ImageView) findViewById(R.id.img31);
//		szImg32 = (ImageView) findViewById(R.id.img32);
//		szImg33 = (ImageView) findViewById(R.id.img33);
	}
	TextWatcher tWatcher = new TextWatcher(){
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void afterTextChanged(Editable s) {
			if(qiEt.getText().toString().startsWith("0")){
				qiEt.setText("1");
			}
			if(beiEt.getText().toString().startsWith("0")){
				beiEt.setText("1");
			}
			int qishu = 0;
			int beishu = 0;
			if(qiEt.getText().toString().trim().length() == 0){
				qishu = 1;
			}else{
				qishu = Integer.parseInt(qiEt.getText().toString().trim());
			}
			if(beiEt.getText().toString().trim().length() == 0){
				beishu = 1;
			}else{
				beishu = Integer.parseInt(beiEt.getText().toString().trim());
			}
			if(qishu>82){
				qiEt.setText("82");
				qishu = 82;
			}
			if(beishu>99){
				beiEt.setText("99");
				beishu = 99;
			}
		}
	};
	@Override
	protected void onResume() {
		isOver = true;
		handler.sendEmptyMessageDelayed(6, 500);
		super.onResume();
	}
	/**
	 * 初始化数据
	 */
	private void initData(){
		pageViews = new ArrayList<View>();  
		szImg1Array = new ImageView[]{szImg1,szImg11,szImg12,szImg13,szImg14,szImg15};
		szImg2Array = new ImageView[]{szImg2,szImg21,szImg22,szImg23};
		szImg3Array = new ImageView[]{szImg3,szImg31,szImg32,szImg33};
		saImgSrcArray = new int[]{R.drawable.dice_f1,R.drawable.dice_f2,R.drawable.dice_f3,R.drawable.dice_f4,
				R.drawable.dice_f2,R.drawable.dice_f2,R.drawable.dice_f1,R.drawable.dice_f3};
		hzTvBonus = new int[]{80,40,25,16,12,10,9,9,10,12,16,25,40,80};
		hzNumbers = new int[]{4,5,6,7,8,9,10,11,12,13,14,15,16,17};
		stNumbers = new int[]{111,222,333,444,555,666};
		ertNumbers = new int[]{112,113,114,115,116,122,223,224,225,226,133,233,334,335,336,144,244,344,445,446,155,255,355,
				455,556,166,266,366,466,566,11,22,33,44,55,66};
		sbtNumbers= new int[]{123,124,125,126,134,135,136,145,146,156,234,235,236,245,246,256,345,346,356,456};
		sLianNumbers = new int[]{123,123,234,345,456};//第一个元素代表三连号通选，“123”这个数字本身没有意义
		erbtNumbers  = new int[]{12,13,14,15,16,23,24,25,26,34,35,36,45,46,56};
		initTypeCai();//根据传过来的类型加入对应的布局
		countBeishu = 1;
		countPayMoney = 2;
		countQishu = 1;
		countZhushu = 1;
	}
	/**
	 * 初始化和值界面
	 * @return
	 */
	private View getHeZhiLayout(){
		View view = LayoutInflater.from(this).inflate(R.layout.layout_hezhi, null);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		hzTv1 = (LinearLayout) view.findViewById(R.id.hzLayout1);
		hzTv2 = (LinearLayout) view.findViewById(R.id.hzLayout2);
		hzTv3 = (LinearLayout) view.findViewById(R.id.hzLayout3);
		hzTv4 = (LinearLayout) view.findViewById(R.id.hzLayout4);
		hzTv5 = (LinearLayout) view.findViewById(R.id.hzLayout5);
		hzTv6 = (LinearLayout) view.findViewById(R.id.hzLayout6);
		hzTv7 = (LinearLayout) view.findViewById(R.id.hzLayout7);
		hzTv8 = (LinearLayout) view.findViewById(R.id.hzLayout8);
		hzTv9 = (LinearLayout) view.findViewById(R.id.hzLayout9);
		hzTv10 = (LinearLayout) view.findViewById(R.id.hzLayout10);
		hzTv11 = (LinearLayout) view.findViewById(R.id.hzLayout11);
		hzTv12 = (LinearLayout) view.findViewById(R.id.hzLayout12);
		hzTv13 = (LinearLayout) view.findViewById(R.id.hzLayout13);
		hzTv14 = (LinearLayout) view.findViewById(R.id.hzLayout14);
		hzDaTv = (TextView) view.findViewById(R.id.daTv);
		hzXiaoTv = (TextView) view.findViewById(R.id.xiaoTv);
		hzDanTv = (TextView) view.findViewById(R.id.danTv);
		hzShuangTv = (TextView) view.findViewById(R.id.shuangTv);
		hzDaTv.setOnClickListener(this);
		hzXiaoTv.setOnClickListener(this);
		hzDanTv.setOnClickListener(this);
		hzShuangTv.setOnClickListener(this);
		hzTvArray = new LinearLayout[]{hzTv1,hzTv2,hzTv3,hzTv4,hzTv5,hzTv6,hzTv7,hzTv8,hzTv9,hzTv10,hzTv11,hzTv12,hzTv13,hzTv14};
		for(int i=0;i<hzTvArray.length;i++){
			hzTvArray[i].setOnClickListener(new LayoutClickLinear());
//			((TextView)hzTvArray[i].getChildAt(0)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
//			((TextView)hzTvArray[i].getChildAt(0)).getPaint().setFakeBoldText(false);//加粗
//			"<font size='3' color='red'>"+(i+3)+"<font size='3' color='red'>"+hzTvBonus[i]+"</font></font>"
//			String html="<html><head><title></title></head><body><font color=\"#cccccc\" size=\"30px\">"+(i+3)+"</font><br/><font color=\"#cccccc\">奖金"+hzTvBonus[i]+"元</font></body></html>";
//			hzTvArray[i].setText(Html.fromHtml(html));
		}
		return view;
	}
	/**
	 * 和值按钮点击是的事件监听
	 * @author 刘星星
	 *
	 */
	class LayoutClickLinear implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			View view =  v;
			if(v.isSelected()){//已经被选中，如果再点击则设置为不选中
				view.setSelected(false);
				view.setBackgroundColor(Color.parseColor("#29915C"));
//				tv.setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
			}else{//设置为选中
				view.setSelected(true);
				view.setBackgroundColor(Color.parseColor("#71A93C"));
//				tv.setTextColor(Color.parseColor("#FED700"));
			}
			shakePhone(150);//震动手机
			setAllLayoutView(view);
		}
		
	}
	/**
	 * 设置所有的控件
	 */
	private void setAllLayoutView(View layout){
		if(typeSelect == 1){//和值
			hzDaTv.setSelected(false);
			hzDaTv.setBackgroundColor(Color.parseColor("#29915E"));
			hzXiaoTv.setSelected(false);
			hzXiaoTv.setBackgroundColor(Color.parseColor("#29915E"));
			hzDanTv.setSelected(false);
			hzDanTv.setBackgroundColor(Color.parseColor("#29915E"));
			hzShuangTv.setSelected(false);
			hzShuangTv.setBackgroundColor(Color.parseColor("#29915E"));
			setHzBonuxInfo();
		}else if(typeSelect == 2){//三同号
			setSanTongBonuxInfo();
		}if(typeSelect == 3){//二同号
			setErTongBonuxInfo();
		}if(typeSelect == 4){//三不同
			setSanBuTongBonuxInfo();
		}if(typeSelect == 5){//三连号
			setSanLianBonuxInfo();
		}if(typeSelect == 6){//二不同
			setErBuTongBonuxInfo();
		}
	}
	/**
	 * 设置和值中奖信息
	 */
	private void setHzBonuxInfo(){
		zhuNumber = 0;
		payMoney = 0;
		minBonux = 0;
		maxBonux = 0;
		for(int i=0;i<hzTvArray.length;i++){
			if(hzTvArray[i].isSelected()){
				zhuNumber++;
				if(hzTvBonus[i]>maxBonux){//将当前选中的最大奖金找出了
					maxBonux = hzTvBonus[i]*countBeishu;
				}
				if(hzTvBonus[i]<minBonux || minBonux == 0){//将当前选中的最小奖金找出来
					minBonux = hzTvBonus[i]*countBeishu;
				}
			}
		}
		payMoney = zhuNumber*2*countBeishu;
		zhuNumberTv.setText(zhuNumber+"注/");
		payMoneyTv.setText(payMoney+"元");
		if(zhuNumber>1){
			bonuxInfoTv.setVisibility(View.VISIBLE);
			payLayout.setVisibility(View.VISIBLE);
			bonuxInfoTv.setText("若中奖:奖金"+minBonux+"至"+maxBonux+"元,盈利"+(minBonux-payMoney)+"至"+(maxBonux-payMoney)+"元");
		}else if(zhuNumber==1){
			bonuxInfoTv.setVisibility(View.VISIBLE);
			payLayout.setVisibility(View.VISIBLE);
			int values = maxBonux-payMoney;
			if(values>0){
				bonuxInfoTv.setText("若中奖:奖金"+maxBonux+"元,盈利"+values+"元");
			}else{
				bonuxInfoTv.setText("若中奖:奖金"+maxBonux+"元,亏损"+Math.abs(values)+"元");
			}
		}else{
			bonuxInfoTv.setVisibility(View.GONE);
			payLayout.setVisibility(View.GONE);
		}
		
	}
	/**
	 * 设置三通中奖信息
	 */
	private void setSanTongBonuxInfo(){
		zhuNumber = 0;
		payMoney = 0;
		minBonux = 0;
		maxBonux = 0;
		for(int i=0;i<stTvArray.length;i++){
			if(stTvArray[i].isSelected()){
				zhuNumber++;
				
				if(i==6 && zhuNumber>1){//将当前选中的最大奖金找出了
					maxBonux = 280*countBeishu;
					minBonux = 40*countBeishu;
				}else if(i == 6 && zhuNumber == 1){
					minBonux = 0;
					maxBonux = 40*countBeishu;
				}else{
					maxBonux = 240*countBeishu;
					minBonux = 0;
				}
			}
		}
		payMoney = zhuNumber*2*countBeishu;
		zhuNumberTv.setText(zhuNumber+"注/");
		payMoneyTv.setText(payMoney+"元");
		if(zhuNumber<7 && maxBonux == 280*countBeishu){//这样就能保证通选按钮选中的，而且其他6个按钮没有全部选中，这样就有最大值和最小值
			bonuxInfoTv.setVisibility(View.VISIBLE);
			payLayout.setVisibility(View.VISIBLE);
			bonuxInfoTv.setText("若中奖:奖金"+minBonux+"至"+maxBonux+"元,盈利"+(minBonux-payMoney)+"至"+(maxBonux-payMoney)+"元");
		}else if((zhuNumber == 7 && maxBonux == 280*countBeishu) || maxBonux == 240*countBeishu  || maxBonux == 40*countBeishu){
			bonuxInfoTv.setVisibility(View.VISIBLE);
			payLayout.setVisibility(View.VISIBLE);
			int values = maxBonux-payMoney;
			if(values>0){
				bonuxInfoTv.setText("若中奖:奖金"+maxBonux+"元,盈利"+values+"元");
			}else{
				bonuxInfoTv.setText("若中奖:奖金"+maxBonux+"元,亏损"+Math.abs(values)+"元");
			}
		}else{
			bonuxInfoTv.setVisibility(View.GONE);
			payLayout.setVisibility(View.GONE);
		}
		
	}
	
	/**
	 * 设置二同中奖信息
	 */
	private void setErTongBonuxInfo(){
		zhuNumber = 0;
		payMoney = 0;
		minBonux = 0;
		maxBonux = 0;
		fuxuanNumber = 0;
		danxuanNumber = 0;
		for(int i=0;i<etTvArray.length;i++){
			if(etTvArray[i].isSelected()){
				zhuNumber++;
			}
			if(i>29 && etTvArray[i].isSelected()){
				fuxuanNumber++;
			}else if(i<=29 && etTvArray[i].isSelected()){
				danxuanNumber++;
			}
		}
		payMoney=zhuNumber*2*countBeishu;
		if(danxuanNumber>0){//有的单选
			boolean isHaveSame = false;
			if(fuxuanNumber>0){//单选复选都有
				for(int i=30;i<35;i++){
					if(etTvArray[i].isSelected()){
						for(int j=0;j<=29;j++){
							if(etTvArray[j].isSelected() 
									&& String.valueOf(ertNumbers[j]).contains(String.valueOf(ertNumbers[i]))){
								isHaveSame = true;
								break;
							}
						}
					}
				}
				if(isHaveSame){
					maxBonux = 95*countBeishu;
				}else{
					maxBonux = 80*countBeishu;
				}
				minBonux = 15*countBeishu;
			}else{
				minBonux = 0;
				maxBonux = 80*countBeishu;
			}
		}else{//没单选
			if(fuxuanNumber>0){//有复选
				minBonux = 0;
				maxBonux = 15*countBeishu;
			}else{
				minBonux = 0;
				maxBonux = 0;
			}
		}
		zhuNumberTv.setText(zhuNumber+"注/");
		payMoneyTv.setText(payMoney+"元");
		if(zhuNumber>0 ){
			bonuxInfoTv.setVisibility(View.VISIBLE);
			payLayout.setVisibility(View.VISIBLE);
			if(minBonux>0){//最小奖金大于0
				int values1 = minBonux-payMoney;
				int values2 = maxBonux-payMoney;
				if(values1>0){//如果最小值减去支付的钱都大于0的话  那么最大值肯定大于0  那么肯定是盈利了
					bonuxInfoTv.setText("若中奖:奖金"+minBonux+"至"+maxBonux+"元,盈利"+values1+"至"+values2+"元");
				}else{
					if(values2>0){
						bonuxInfoTv.setText("若中奖:奖金"+minBonux+"至"+maxBonux+"元,亏损"+Math.abs(values1)+"元或盈利"+values2+"元");
					}else{
						bonuxInfoTv.setText("若中奖:奖金"+minBonux+"至"+maxBonux+"元,亏损"+values1+"至"+values2+"元");
					}
					
				}
			}else{//最小奖金等于0
				if(maxBonux>0){//最大奖金大于0
					int values2 = maxBonux-payMoney;
					if(values2>0){
						bonuxInfoTv.setText("若中奖:奖金"+maxBonux+"元,盈利"+values2+"元");
					}else{
						bonuxInfoTv.setText("若中奖:奖金"+maxBonux+"元,亏损"+values2+"元");
					}
				}else{
					bonuxInfoTv.setVisibility(View.GONE);
					payLayout.setVisibility(View.GONE);
					}
			}
		}else{
			bonuxInfoTv.setVisibility(View.GONE);
			payLayout.setVisibility(View.GONE);
		}
	}
	
	
	/**
	 * 设置三不同中奖信息
	 */
	private void setSanBuTongBonuxInfo(){
		zhuNumber = 0;
		payMoney = 0;
		minBonux = 0;
		maxBonux = 0;
		selectedNumber = 0;
		for(int i=0;i<sbtTvArray.length;i++){
			if(sbtTvArray[i].isSelected()){
				zhuNumber++;
			}
		}
		if(zhuNumber>0){
			payMoney = zhuNumber*2*countBeishu;
			maxBonux = 40*countBeishu;
			zhuNumberTv.setText(zhuNumber+"注/");
			payMoneyTv.setText(payMoney+"元");
			bonuxInfoTv.setVisibility(View.VISIBLE);
			payLayout.setVisibility(View.VISIBLE);
			int values = maxBonux-payMoney;
			if(values>0){
				bonuxInfoTv.setText("若中奖:奖金"+maxBonux+"元,盈利"+values+"元");
			}else{
				bonuxInfoTv.setText("若中奖:奖金"+maxBonux+"元,亏损"+Math.abs(values)+"元");
			}
		}else{
			bonuxInfoTv.setVisibility(View.GONE);
			payLayout.setVisibility(View.GONE);
		}
		
	}
	/**
	 * 设置三连号信息
	 */
	private void setSanLianBonuxInfo(){
		zhuNumber = 0;
		payMoney = 0;
		minBonux = 0;
		maxBonux = 0;
		selectedNumber = 0;
		for(int i=0;i<sLianArray.length;i++){
			if(sLianArray[i].isSelected()){
				zhuNumber++;
			}
			if(i>0 && sLianArray[i].isSelected()){
				selectedNumber++;
			}
		}
		if(zhuNumber>0){
			payMoney = zhuNumber*2*countBeishu;
			if(sLianArray[0].isSelected() && selectedNumber==0){//选中了通选 没有选择单选
				maxBonux = 10*countBeishu;
			}else if(sLianArray[0].isSelected() && selectedNumber>0){//选中了通选也选中了单选
				maxBonux = 40*countBeishu;
				minBonux = 10*countBeishu;
			}else if(!sLianArray[0].isSelected() && selectedNumber>0){//只选中了单选
				maxBonux = 40*countBeishu;
			}
			zhuNumberTv.setText(zhuNumber+"注/");
			payMoneyTv.setText(payMoney+"元");
			bonuxInfoTv.setVisibility(View.VISIBLE);
			payLayout.setVisibility(View.VISIBLE);
			int values = maxBonux-payMoney;
			int values1 = minBonux-payMoney;
			if(minBonux == 0){
				bonuxInfoTv.setText("若中奖:奖金"+maxBonux+"元,盈利"+values+"元");
			}else{
				bonuxInfoTv.setText("若中奖:奖金"+minBonux+"至"+maxBonux+"元,盈利"+Math.abs(values1)+"至"+values+"元");
			}
		}else{
			bonuxInfoTv.setVisibility(View.GONE);
			payLayout.setVisibility(View.GONE);
		}
		
	}
	/**
	 * 设置二不同中奖信息
	 */
	private void setErBuTongBonuxInfo(){
		zhuNumber = 0;
		payMoney = 0;
		minBonux = 0;
		maxBonux = 0;
		for(int i=0;i<erbtTvArray.length;i++){
			if(erbtTvArray[i].isSelected()){
				zhuNumber++;
			}
		}
		if(zhuNumber>0){
			payMoney = zhuNumber*2*countBeishu;
			maxBonux = 8*countBeishu;
			zhuNumberTv.setText(zhuNumber+"注/");
			payMoneyTv.setText(payMoney+"元");
			bonuxInfoTv.setVisibility(View.VISIBLE);
			payLayout.setVisibility(View.VISIBLE);
			int values = maxBonux-payMoney;
			if(values>0){
				bonuxInfoTv.setText("若中奖:奖金"+maxBonux+"元,盈利"+values+"元");
			}else{
				bonuxInfoTv.setText("若中奖:奖金"+maxBonux+"元,亏损"+Math.abs(values)+"元");
			}
		}else{
			bonuxInfoTv.setVisibility(View.GONE);
			payLayout.setVisibility(View.GONE);
		}
		
	}
	Vibrator mVibrator = null;
	/**
	 * 震动手机
	 */
	private void shakePhone(int time){
		 mVibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);//获取振动器
		mVibrator.vibrate(time);
	}
	/**
	 * 初始化三同界面
	 * @return
	 */
	private View getSanTongLayout(){
		View view = LayoutInflater.from(this).inflate(R.layout.layout_santong, null);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		santongLayout1 = (TextView) view.findViewById(R.id.santongLayout1);
		santongLayout2 = (TextView) view.findViewById(R.id.santongLayout2);
		santongLayout3 = (TextView) view.findViewById(R.id.santongLayout3);
		santongLayout4 = (TextView) view.findViewById(R.id.santongLayout4);
		santongLayout5 = (TextView) view.findViewById(R.id.santongLayout5);
		santongLayout6 = (TextView) view.findViewById(R.id.santongLayout6);
		tongxuanLayout = (LinearLayout) view.findViewById(R.id.tongxuanLayout);
		stTvArray = new View[]{santongLayout1,santongLayout2,santongLayout3,santongLayout4,santongLayout5,santongLayout6,tongxuanLayout};
		for(int i=0;i<stTvArray.length;i++){
			stTvArray[i].setOnClickListener(new LayoutClickLinear());
		}
		return view;
	}
	/**
	 * 初始化二同号界面
	 * @return
	 */
	private View getErTongLayout(){
		View view = LayoutInflater.from(this).inflate(R.layout.layout_ertong, null);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		ertongId = new int[]{R.id.ertongLayout1,R.id.ertongLayout2,R.id.ertongLayout3,R.id.ertongLayout4,R.id.ertongLayout5,R.id.ertongLayout6,R.id.ertongLayout7,R.id.ertongLayout8,R.id.ertongLayout9,R.id.ertongLayout10
				,R.id.ertongLayout11,R.id.ertongLayout12,R.id.ertongLayout13,R.id.ertongLayout14,R.id.ertongLayout15,R.id.ertongLayout16,R.id.ertongLayout17,R.id.ertongLayout18,R.id.ertongLayout19,R.id.ertongLayout20
				,R.id.ertongLayout21,R.id.ertongLayout22,R.id.ertongLayout23,R.id.ertongLayout24,R.id.ertongLayout25,R.id.ertongLayout26,R.id.ertongLayout27,R.id.ertongLayout28,R.id.ertongLayout29,R.id.ertongLayout30
				,R.id.ertongLayout31,R.id.ertongLayout32,R.id.ertongLayout33,R.id.ertongLayout34,R.id.ertongLayout35,R.id.ertongLayout36};
		
		etTvArray = new TextView[]{ertongLayout1,ertongLayout2,ertongLayout3,ertongLayout4,ertongLayout5,ertongLayout6,ertongLayout7,ertongLayout8,ertongLayout9,ertongLayout10,
				ertongLayout11,ertongLayout12,ertongLayout13,ertongLayout14,ertongLayout15,ertongLayout16,ertongLayout17,ertongLayout18,ertongLayout19,ertongLayout20,
				ertongLayout21,ertongLayout22,ertongLayout23,ertongLayout24,ertongLayout25,ertongLayout26,ertongLayout27,ertongLayout28,ertongLayout29,ertongLayout30,
				ertongLayout31,ertongLayout32,ertongLayout33,ertongLayout34,ertongLayout35,ertongLayout36};
		for(int i=0;i<etTvArray.length;i++){
			etTvArray[i]=(TextView) view.findViewById(ertongId[i]);
			etTvArray[i].setTag(i+1);
			etTvArray[i].setOnClickListener(new LayoutClickLinear());
		}
		return view;
	}
	/**
	 * 初始化三不同号界面
	 * @return
	 */
	private View getSanBuTongLayout(){
		View view = LayoutInflater.from(this).inflate(R.layout.layout_sanbutong, null);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		sanbutongId = new int[]{R.id.sanbutongLayout1,R.id.sanbutongLayout2,R.id.sanbutongLayout3,R.id.sanbutongLayout4,R.id.sanbutongLayout5,
				R.id.sanbutongLayout6,R.id.sanbutongLayout7,R.id.sanbutongLayout8,R.id.sanbutongLayout9,R.id.sanbutongLayout10
				,R.id.sanbutongLayout11,R.id.sanbutongLayout12,R.id.sanbutongLayout13,R.id.sanbutongLayout14,R.id.sanbutongLayout15,
				R.id.sanbutongLayout16,R.id.sanbutongLayout17,R.id.sanbutongLayout18,R.id.sanbutongLayout19,R.id.sanbutongLayout20
				};
		
		sbtTvArray = new TextView[]{sanbutongLayout1,sanbutongLayout2,sanbutongLayout3,sanbutongLayout4,sanbutongLayout5,
				sanbutongLayout6,sanbutongLayout7,sanbutongLayout8,sanbutongLayout9,sanbutongLayout10,
				sanbutongLayout11,sanbutongLayout12,sanbutongLayout13,sanbutongLayout14,sanbutongLayout15,
				sanbutongLayout16,sanbutongLayout17,sanbutongLayout18,sanbutongLayout19,sanbutongLayout20};
		for(int i=0;i<sbtTvArray.length;i++){
			sbtTvArray[i]=(TextView) view.findViewById(sanbutongId[i]);
			sbtTvArray[i].setOnClickListener(new LayoutClickLinear());
		}
		return view;
	}
	/**
	 * 初始化二不同号界面
	 * @return
	 */
	private View getErBuTongLayout(){
		View view = LayoutInflater.from(this).inflate(R.layout.layout_erbutong, null);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		erbutongId = new int[]{R.id.erbutongLayout1,R.id.erbutongLayout2,R.id.erbutongLayout3,R.id.erbutongLayout4,R.id.erbutongLayout5,
				R.id.erbutongLayout6,R.id.erbutongLayout7,R.id.erbutongLayout8,R.id.erbutongLayout9,R.id.erbutongLayout10
				,R.id.erbutongLayout11,R.id.erbutongLayout12,R.id.erbutongLayout13,R.id.erbutongLayout14,R.id.erbutongLayout15
				};
		erbtTvArray = new TextView[]{erbutongLayout1,erbutongLayout2,erbutongLayout3,erbutongLayout4,erbutongLayout5,
				erbutongLayout6,erbutongLayout7,erbutongLayout8,erbutongLayout9,erbutongLayout10,
				erbutongLayout11,erbutongLayout12,erbutongLayout13,erbutongLayout14,erbutongLayout15};
		for(int i=0;i<erbtTvArray.length;i++){
			erbtTvArray[i]=(TextView) view.findViewById(erbutongId[i]);
			erbtTvArray[i].setOnClickListener(new LayoutClickLinear());
		}
		return view;
	}
	/**
	 * 初始化三连界面
	 * @return
	 */
	private View getSLianLayout(){
		View view = LayoutInflater.from(this).inflate(R.layout.layout_sanlianhao, null);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		sanLianId = new int[]{R.id.slianLayout1,R.id.slianLayout2,R.id.slianLayout3,R.id.slianLayout4,R.id.slianLayout5};
		sLianArray = new View[]{sLianTongxuan,sLianLayout1,sLianLayout2,sLianLayout3,sLianLayout4};
		for(int i=0;i<sLianArray.length;i++){
			sLianArray[i]=(View)view.findViewById(sanLianId[i]);
			sLianArray[i].setOnClickListener(new LayoutClickLinear());
		}
		return view;
	}
	@Override
	protected void onStop() {
		
		super.onStop();
	}
	@Override
	protected void onPause() {
		isOver = false;
		super.onPause();
	}
	/**
	 * 生成订单成功后跳入订单详情界面
	 */
	private void getOrder(){
		Intent intent  = new Intent(this,SumbitSuccessActivity.class);
		String qishu  = FragmentForLotteryType.qishu;
		String result = getCurrentResult();
		intent.putExtra("qishu", qishu);
		intent.putExtra("result", result);
		intent.putExtra("typeSelect", typeSelect+"");
		if(typeSelect == 1){//和值
			String number = "";
			for(int i=0;i<hzTvArray.length;i++){
				if(hzTvArray[i].isSelected()){
					number+=(hzNumbers[i]+",");
				}
			}
			intent.putExtra("number", number.substring(0, number.length()-1));
		}else if(typeSelect == 2){//三同
			String number = "";
			for(int i=0;i<stTvArray.length-1;i++){
				if(stTvArray[i].isSelected()){
					number+=(stNumbers[i]+",");
				}
			}
			if(number.length() == 0 && stTvArray[6].isSelected()){
				number="三同号通选";
			}else if(number.length() > 0 && stTvArray[6].isSelected()){
				number = number.substring(0, number.length()-1);
				number+= ";三同号通选";
			}
			intent.putExtra("number", number);
		}else if(typeSelect == 3){//二同
			String number = "";
			for(int i=0;i<etTvArray.length;i++){
				if(etTvArray[i].isSelected()){
					if(i>=30){
						number+=(ertNumbers[i]+"*,");
//						number = number.substring(0, number.length()-1);
					}else if(i==29){
						number+=(ertNumbers[i]);
					}else{
						number+=(ertNumbers[i]+",");
					}
				}
				if(i == 29 && number.length()>0){
					number+=";";
				}
			}
			if(number.contains("*")){
				intent.putExtra("number", number.substring(0, number.length()-1));
			}else{
				intent.putExtra("number", number.substring(0, number.length()));
			}
			
		}else if(typeSelect == 4){//三不同
			String number = "";
			for(int i=0;i<sbtTvArray.length;i++){
				if(sbtTvArray[i].isSelected()){
					number+=(sbtNumbers[i]+",");
				}
			}
			intent.putExtra("number", number);
		}else if(typeSelect == 5){//三连号
			String number = "";
			for(int i=0;i<sLianArray.length;i++){
				if(sLianArray[i].isSelected()){
					if(i==0){
						number+= "三连号通选;";
					}else{
						number+=(sLianNumbers[i]+",");
					}
					
				}
			}
			intent.putExtra("number", number);
		}else if(typeSelect == 6){//二不同
			String number = "";
			for(int i=0;i<erbtTvArray.length;i++){
				if(erbtTvArray[i].isSelected()){
					number+=(erbtNumbers[i]+",");
				}
			}
			intent.putExtra("number", number.substring(0, number.length()-1));
			
		}
		intent.putExtra("payMoney", payMoney);
		intent.putExtra("zhuNumber",zhuNumber+"");
		startActivity(intent);
	}
	/**
	 * 根据当前选中的号制作出服务器所需要的数据模式
	 * @return
	 */
	public String getCurrentResult(){
		String result = "";
		if(typeSelect == 1){//和值
			result = "1,";
			for(int i=0;i<hzTvArray.length;i++){
				if(hzTvArray[i].isSelected()){
					result+=(hzNumbers[i]+"|");
				}
			}
			result = result.substring(0,result.length()-1);
		}else if(typeSelect == 2){//三同
			for(int i=0;i<stTvArray.length-1;i++){
				if(stTvArray[i].isSelected()){
					result+=(stNumbers[i]+"|");
				}
			}
			if(result.length()>0){
				result = "3,"+result.substring(0,result.length()-1);
				if(stTvArray[6].isSelected()){
					result = result+";2";
				}
			}else{
				if(stTvArray[6].isSelected()){
					result = result+"2";
				}
			}
		}else if(typeSelect == 3){//二同
			for(int i=0;i<6;i++){
				if(etTvArray[i].isSelected()){
					result = result+ertNumbers[i]+"|";
				}
			}
			if(result.length()>0){
				result = "5,"+result.substring(0,result.length()-1)+"#";
			}
			for(int i=6;i<12;i++){
				if(etTvArray[i].isSelected()){
					result = result+ertNumbers[i]+"|";
				}
			}
			if(result.length()>0){
				result = result.substring(0,result.length()-1);
			}
			String fuxuan = "";
			for(int i=12;i<18;i++){
				if(etTvArray[i].isSelected()){
					fuxuan = fuxuan+ertNumbers[i]+"|";
				}
			}
			
			if(fuxuan.length()>0){
				fuxuan = "4,"+fuxuan.substring(0,fuxuan.length()-1);
				result = result+";"+fuxuan;
			}
			
		}else if(typeSelect == 4){//三不同
			for(int i=0;i<6;i++){
				if(sbtTvArray[i].isSelected()){
//					result+=(sbtTvArray[i].getText().toString()+"|");
				}
			}
			if(result.length()>0){
				result = "6,"+result.substring(0, result.length()-1);
				if(sbtTvArray[6].isSelected()){
					result = result+";8";
				}
			}else{
				if(sbtTvArray[6].isSelected()){
					result = result+"8";
				}
			}
			
		}else if(typeSelect == 5){//三连号
			
		}else if(typeSelect == 6){//二不同
			for(int i=0;i<erbtTvArray.length;i++){
				if(erbtTvArray[i].isSelected()){
//					result+=(erbtTvArray[i].getText().toString()+"|");
				}
			}
			if(result.length()>0){
				result = "7,"+result.substring(0, result.length()-1);
			}
		}
		return result;
	}
	/**
	 * 根据传过来的值判断是那种类型
	 */
	public void initTypeCai(){
		typeSelect = getIntent().getIntExtra("type", 1);
		hzView = getHeZhiLayout();
		pageViews.add(hzView);
		santongView = getSanTongLayout();
		pageViews.add(santongView);
		ertongView = getErTongLayout();
		pageViews.add(ertongView);
		sanBuTongView = getSanBuTongLayout();
		pageViews.add(sanBuTongView);
		sLianView = getSLianLayout();
		pageViews.add(sLianView);
		erBuTongView = getErBuTongLayout();
		pageViews.add(erBuTongView);
		notifyPager();
		xuanLayout.setCurrentItem(typeSelect-1, true);
//		if(typeSelect == 1){//和值
//			setHzBonuxInfo();//设置选中的号码的奖金信息
//		}else if(typeSelect == 2){//三同
//			
//			setSanTongBonuxInfo();//设置三同号的奖金信息
//		}else if(typeSelect == 3){//二同
//			
//			setErTongBonuxInfo();
//		}else if(typeSelect == 4){//三不同
//			
//			setSanBuTongBonuxInfo();
//		}else if(typeSelect == 5){//三连号
//			
//			setSanLianBonuxInfo();
//		}else if(typeSelect == 6){//二不同
//			
//			setErBuTongBonuxInfo();
//		}
	}
	private void notifyPager(){
		xuanLayout.setAdapter(new pagerAdapter());  
		xuanLayout.setOnPageChangeListener(new GuidePageChangeListener());  
	}
	// 指引页面更改事件监听器
    class GuidePageChangeListener implements OnPageChangeListener {  
    	  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
        	if(arg0 == 0){
        		typeSelect = 1;
        		setHzBonuxInfo();
        	}else if(arg0 == 1){
        		typeSelect = 2;
        		setSanTongBonuxInfo();
        	}else if(arg0 == 2){
        		typeSelect = 3;
        		setErTongBonuxInfo();
        	}else if(arg0 == 3){
        		typeSelect = 4;
        		setSanBuTongBonuxInfo();
        	}else if(arg0 == 4){
        		typeSelect = 5;
        		setSanLianBonuxInfo();
        	}else if(arg0 == 5){
        		typeSelect = 6;
        		setErBuTongBonuxInfo();
        	}
        }  
    }  
    private void setBonuxInfo(){
    	if(typeSelect == 1){//和值
			setHzBonuxInfo();
		}else if(typeSelect == 2){//三同号
			setSanTongBonuxInfo();
		}if(typeSelect == 3){//二同号
			setErTongBonuxInfo();
		}if(typeSelect == 4){//三不同
			setSanBuTongBonuxInfo();
		}if(typeSelect == 5){//三连号
			setSanLianBonuxInfo();
		}if(typeSelect == 6){//二不同
			setErBuTongBonuxInfo();
		}
    }
	public class pagerAdapter extends PagerAdapter{
    	@Override
    	public int getCount() {
    		// TODO Auto-generated method stub
    		return pageViews.size();
    	}

    	@Override
    	public boolean isViewFromObject(View arg0, Object arg1) {
    		// TODO Auto-generated method stub
    		return arg0 == arg1;  
    	}
    	@Override  
        public int getItemPosition(Object object) {  
            // TODO Auto-generated method stub  
            return super.getItemPosition(object);  
        }  

        @Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).removeView(pageViews.get(arg1));  
        }  

        @Override  
        public Object instantiateItem(View arg0, int arg1) {  
//        	ImageView imageView = (ImageView) pageViews.get(arg1);
            ((ViewPager) arg0).addView(pageViews.get(arg1));  
            return pageViews.get(arg1);  
        }  

        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
            // TODO Auto-generated method stub  

        }  

        @Override  
        public Parcelable saveState() {  
            // TODO Auto-generated method stub  
            return null;  
        }  

        @Override  
        public void startUpdate(View arg0) {  
            // TODO Auto-generated method stub  

        }  

        @Override  
        public void finishUpdate(View arg0) {  
            // TODO Auto-generated method stub  

        }  
    }
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){//返回按钮
			DeviceTool.disShowSoftKey(this, qiEt);
			DeviceTool.disShowSoftKey(this, beiEt);
			finish();
		}else if(v == zhuihaoBtn){//追号按钮
			zhuiLayout.setVisibility(View.VISIBLE);
			DeviceTool.showSoftKey(qiEt);
		}else if(v.getId() == R.id.zhuiHaoBtn){//确定追号按钮
			zhuiLayout.setVisibility(View.GONE);
			DeviceTool.disShowSoftKey(this,beiEt);
			countBeishu = Integer.parseInt(beiEt.getText().toString().length()>0?beiEt.getText().toString():"1");
			countQishu = Integer.parseInt(qiEt.getText().toString().length()>0?qiEt.getText().toString():"1");
			countZhushu = zhuNumber*countQishu;
			countPayMoney = countZhushu*countBeishu*2;
			qiandbeiTv.setText(countQishu+"期/"+countBeishu+"倍");
			zhuNumberTv.setText(countZhushu+"注/");
			payMoneyTv.setText(countPayMoney+"元");
			setBonuxInfo();
		}else if(v == yaoImg){
			long current = System.currentTimeMillis();
			long difftime = current - lasttime;
			if (difftime < timeDifference || !isOver)
				return;
			yaodong(current);
		}else if(v == payBtn){//确定按钮
			if(payMoney == 0){
				long current = System.currentTimeMillis();
				yaodong(current);
				return;
			}else if(typeSelect == 3 && zhuNumber == 0){
				long current = System.currentTimeMillis();
				yaodong(current);
				Toast.makeText(this, "请选择至少一注", 3000).show();
				return;
			}else if(typeSelect == 4 && zhuNumber == 0){
				long current = System.currentTimeMillis();
				yaodong(current);
				Toast.makeText(this, "请选择至少一注", 3000).show();
				return;
			}
//			showDialog("正在生成订单，请稍候...");
			if(countPayMoney<=payMoney){
				countBeishu = 1;
				countPayMoney = payMoney;
				countQishu = 1;
				countZhushu = zhuNumber;
			}
			handler.sendEmptyMessage(3);
//			new Thread(getOrderRunnable).start();
			
		}else if(v == hzDaTv){//大
			if(hzDaTv.isSelected()){//已经被选中，如果再点击则设置为不选中
				hzDaTv.setSelected(false);
				hzDaTv.setBackgroundColor(Color.parseColor("#29915E"));
			}else{//设置为选中
				hzDaTv.setSelected(true);
				hzDaTv.setBackgroundColor(Color.parseColor("#71A93B"));
				if(hzXiaoTv.isSelected()){
					hzXiaoTv.setSelected(false);
					hzXiaoTv.setBackgroundColor(Color.parseColor("#29915E"));
				}
			} 
			if(hzDanTv.isSelected()){//如果已经选中了单
				for(int i=0;i<hzTvArray.length;i++){
					if(i>=(hzDaTv.isSelected()?7:0) && i%2==1){
						hzTvArray[i].setSelected(true);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//						hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
					}else{
						hzTvArray[i].setSelected(false);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//						hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
					}
					
				}
			}else if(hzShuangTv.isSelected()){//如果已经选中了双数
				for(int i=0;i<hzTvArray.length;i++){
					if(i>=(hzDaTv.isSelected()?7:0) && i%2==0){
						hzTvArray[i].setSelected(true);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//						hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
					}else{
						hzTvArray[i].setSelected(false);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//						hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
					}
					
				}
			}else{//如果单数和双数都没有选择
					if(hzDaTv.isSelected()){
						for(int i=0;i<hzTvArray.length;i++){
							if(i>=7){
								hzTvArray[i].setSelected(true);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//								hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
							}else{
								hzTvArray[i].setSelected(false);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//								hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
							}
						}
					}else{
						for(int i=0;i<hzTvArray.length;i++){
								hzTvArray[i].setSelected(false);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//								hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
						}
					}
			}
			setHzBonuxInfo();//设置奖金信息
		}else if(v == hzXiaoTv){//小
			if(hzXiaoTv.isSelected()){//已经被选中，如果再点击则设置为不选中
				hzXiaoTv.setSelected(false);
				hzXiaoTv.setBackgroundColor(Color.parseColor("#29915E"));
			}else{//设置为选中
				hzXiaoTv.setSelected(true);
				hzXiaoTv.setBackgroundColor(Color.parseColor("#71A93B"));
				if(hzDaTv.isSelected()){
					hzDaTv.setSelected(false);
					hzDaTv.setBackgroundColor(Color.parseColor("#29915E"));
				}
			}
			
			if(hzDanTv.isSelected()){//如果已经选中了单
				for(int i=0;i<hzTvArray.length;i++){
					if(i<=(hzXiaoTv.isSelected()?6:14) && i%2==1){
						hzTvArray[i].setSelected(true);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//						hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
					}else{
						hzTvArray[i].setSelected(false);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//						hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
					}
					
				}
			}else if(hzShuangTv.isSelected()){//如果已经选中了双数
				for(int i=0;i<hzTvArray.length;i++){
					if(i<=(hzXiaoTv.isSelected()?7:13) && i%2==0){
						hzTvArray[i].setSelected(true);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//						hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
					}else{
						hzTvArray[i].setSelected(false);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//						hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
					}
					
				}
			}else{//如果单数和双数都没有选择
					if(hzXiaoTv.isSelected()){
						for(int i=0;i<hzTvArray.length;i++){
							if(i<=6){
								hzTvArray[i].setSelected(true);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//								hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
							}else{
								hzTvArray[i].setSelected(false);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//								hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
							}
						}
					}else{
						for(int i=0;i<hzTvArray.length;i++){
								hzTvArray[i].setSelected(false);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//								hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
						}
					}
			}
			setHzBonuxInfo();//设置奖金信息
		}else if(v == hzDanTv){//单
			if(hzDanTv.isSelected()){//已经被选中，如果再点击则设置为不选中
				hzDanTv.setSelected(false);
				hzDanTv.setBackgroundColor(Color.parseColor("#29915E"));
			}else{//设置为选中
				hzDanTv.setSelected(true);
				hzDanTv.setBackgroundColor(Color.parseColor("#71A93B"));
				if(hzShuangTv.isSelected()){
					hzShuangTv.setSelected(false);
					hzShuangTv.setBackgroundColor(Color.parseColor("#29915E"));
				}
			}
			
			if(hzDaTv.isSelected()){//如果已经选中了大
				for(int i=0;i<hzTvArray.length;i++){
					if((hzDanTv.isSelected()?(i%2==1):(i%2==1 || i%2==0)) && i>=7){
						hzTvArray[i].setSelected(true);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//						hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
					}else{
						hzTvArray[i].setSelected(false);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//						hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
					}
					
				}
			}else if(hzXiaoTv.isSelected()){//如果已经选中了小数
				for(int i=0;i<hzTvArray.length;i++){
					if((hzDanTv.isSelected()?(i%2==1):(i%2==1 || i%2==0)) && i<=6){
						hzTvArray[i].setSelected(true);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//						hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
					}else{
						hzTvArray[i].setSelected(false);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//						hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
					}
					
				}
			}else{//如果大数和小数都没有选择
					if(hzDanTv.isSelected()){
						for(int i=0;i<hzTvArray.length;i++){
							if(i%2==1){
								hzTvArray[i].setSelected(true);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//								hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
							}else{
								hzTvArray[i].setSelected(false);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//								hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
							}
						}
					}else{
						for(int i=0;i<hzTvArray.length;i++){
								hzTvArray[i].setSelected(false);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//								hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
						}
					}
			}
			setHzBonuxInfo();//设置奖金信息
		}else if(v == hzShuangTv){//双
			if(hzShuangTv.isSelected()){//已经被选中，如果再点击则设置为不选中
				hzShuangTv.setSelected(false);
				hzShuangTv.setBackgroundColor(Color.parseColor("#29915E"));
			}else{//设置为选中
				hzShuangTv.setSelected(true);
				hzShuangTv.setBackgroundColor(Color.parseColor("#71A93B"));
				if(hzDanTv.isSelected()){
					hzDanTv.setSelected(false);
					hzDanTv.setBackgroundColor(Color.parseColor("#29915E"));
				}
			}
			
			
			if(hzDaTv.isSelected()){//如果已经选中了大
				for(int i=0;i<hzTvArray.length;i++){
					if((hzShuangTv.isSelected()?(i%2==0):(i%2==1 || i%2==0)) && i>=7){
						hzTvArray[i].setSelected(true);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//						hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
					}else{
						hzTvArray[i].setSelected(false);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//						hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
					}
					
				}
			}else if(hzXiaoTv.isSelected()){//如果已经选中了小数
				for(int i=0;i<hzTvArray.length;i++){
					if((hzShuangTv.isSelected()?(i%2==0):(i%2==1 || i%2==0)) && i<=7){
						hzTvArray[i].setSelected(true);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//						hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
					}else{
						hzTvArray[i].setSelected(false);
						hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//						hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
					}
					
				}
			}else{//如果大数和小数都没有选择
					if(hzShuangTv.isSelected()){
						for(int i=0;i<hzTvArray.length;i++){
							if(i%2==0){
								hzTvArray[i].setSelected(true);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#71A93C"));
//								hzTvArray[i].setTextColor(Color.parseColor("#FED700"));
							}else{
								hzTvArray[i].setSelected(false);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//								hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
							}
						}
					}else{
						for(int i=0;i<hzTvArray.length;i++){
								hzTvArray[i].setSelected(false);
								hzTvArray[i].setBackgroundColor(Color.parseColor("#29915C"));
//								hzTvArray[i].setTextColor(getResources().getColor(R.color.kuaisan_xuanbtn_bg));
						}
					}
			}
			setHzBonuxInfo();//设置奖金信息
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("按了返回键");
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onKeyboardStateChanged(int state) {
		System.out.println("操作软键盘的返回值为===="+state);
		if(state == 1){
			zhuiLayout.setVisibility(View.VISIBLE);
		}else if(state == -1){
			zhuiLayout.setVisibility(View.GONE);
		}
		
	}
	private void showDialog(String msg){
		progressdialog = new ProgressDialog(this);
		progressdialog.setMessage(msg);
		progressdialog.setCanceledOnTouchOutside(false);
		progressdialog.setCancelable(true);
		progressdialog.show();
	}
}
