package com.zhihuigu.sosoOffice;

import java.util.Calendar;

import com.zhihuigu.sosoOffice.SoftwareSetActivity.ScrollViewTouchListener;
import com.zhihuigu.sosoOffice.View.ScrollLayoutView;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class MessageInfromActivity extends BaseActivity{
	private Button backBtn;//返回按钮
	private ScrollLayoutView scrollView;
	private float currentX = 0;
	private boolean boo = true;
	private TextView startTimeText,stopTimeText;
	private RelativeLayout startTimeLayout,stopTimeLayout;
	private Button submitBtn;
	ImageView menImage = null;
	private int mHour;
	private int mMinute;
	
	private int timeType = 0;//0代表开始时间  1代表结束时间
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_messageinform);
		findView();
		initData();
	}
	private void findView(){
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		menImage = (ImageView) findViewById(R.id.menImage);
		scrollView = (ScrollLayoutView) findViewById(R.id.scrollView);
		scrollView.setOnTouchListener(new ScrollViewTouchListener());
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.setMargins(0, 0, 0, 0);
		startTimeText = (TextView) findViewById(R.id.startTime);
		stopTimeText = (TextView) findViewById(R.id.stopTime);
		startTimeLayout = (RelativeLayout) findViewById(R.id.startTimeLinear);
		stopTimeLayout = (RelativeLayout) findViewById(R.id.stopTimeLinear);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		startTimeLayout.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		stopTimeLayout.setOnClickListener(this);
	}
	private void initData(){
		Calendar c=Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}
	/**
	 * 滚动条的滑动事件，用来控制信息列表显示图片的开关
	 * @createDate 2012年12月13日
	 * @author 刘星星 
	 * 
	 */
	class ScrollViewTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			try {
				int width = menImage.getWidth();
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					currentX = event.getX();
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if(currentX< event.getX()){
						scrollView.scrollTo(0, 0);
					}else if(currentX > event.getX()){
						scrollView.scrollTo(width, 0);
					}
					currentX = event.getX();
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					currentX = event.getX();
				}
				return false;

			} catch (Exception e) {
				return false;
			}
		}
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}else if(v == submitBtn){
			finish();
		}else if(v == startTimeLayout){
			timeType = 0;
			showDialog(0);
		}else if(v == stopTimeLayout){
			timeType = 1;
			showDialog(0);
		}
		super.onClick(v);
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		
		return new TimePickerDialog(this,timerPicDialog,mHour,mMinute,false);
	}
	
	TimePickerDialog.OnTimeSetListener timerPicDialog = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour=hourOfDay;
			mMinute=minute;
			if(timeType == 0){
				startTimeText.setText(((mHour<10)?("0"+mHour):mHour) + ":" + ((mMinute<10)?("0"+mMinute):mMinute));
			}else if(timeType ==  1){
				stopTimeText.setText(((mHour<10)?("0"+mHour):mHour) + ":" + ((mMinute<10)?("0"+mMinute):mMinute));
			}
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return true;
	}
}
