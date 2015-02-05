package com.zhihuigu.sosoOffice;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.zhihuigu.sosoOffice.View.ScrollLayoutView;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
/**
 * 软件设置类
 * @author 刘星星
 * @createDate 2013/1/7
 *ScrollLayoutView 为自定义的水平滚动条
 *
 */
public class SoftwareSetActivity extends BaseActivity{
	private Button backBtn;//返回按钮
	private ScrollLayoutView scrollView;
	private float currentX = 0;
	private boolean boo = true;
	ImageView menImage = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_softwareset);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		scrollView = (ScrollLayoutView) findViewById(R.id.scrollView);
		scrollView.setOnTouchListener(new ScrollViewTouchListener());
		menImage = (ImageView) findViewById(R.id.menImage);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.setMargins(0, 0, 0, 0);
		if(MyApplication.getInstance().getDisplayRoomPhoto()==1){
			scrollView.scrollTo(0, 0);
		}else{
			new Thread(runnable).start();
		}
	}
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendEmptyMessage(0);
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			int width = menImage.getWidth();
			scrollView.scrollTo(width, 0);
			super.handleMessage(msg);
		}
		
	};
	/**
	 * 滚动条的滑动事件，用来控制信息列表显示图片的开关
	 * @createDate 2012年12月13日
	 * @author 刘星星 
	 * 
	 */
	class ScrollViewTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			String userid = "";
			if((MyApplication.getInstance().getSosouserinfo()==null
					||MyApplication.getInstance().getSosouserinfo().getUserID()==null)){
				userid = "0";
			}else{
				userid =  MyApplication.getInstance(SoftwareSetActivity.this)
						.getSosouserinfo(SoftwareSetActivity.this).getUserID() ;
			}
			try {
				int width = menImage.getWidth();
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					currentX = event.getX();
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					/*if (event.getX() > currentX) {// 向右滑动
						if(scrollView.getScrollX()<width/2){// 向右滚动
							boo = true;
						}else{// 向左滚动
							boo = false;
						}
					} else if (event.getX() < currentX) {// 向左滑动
						if(scrollView.getScrollX()<width/2){// 向右滚动
							boo = true;
						}else{// 向左滚动
							boo = false;
						}
					}*/
					if(currentX< event.getX()){
						scrollView.scrollTo(0, 0);
						MyApplication.getInstance().setDisplayRoomPhoto(1);
						ContentValues values = new ContentValues();
						values.put("isshowimage", 1);
						DBHelper.getInstance(SoftwareSetActivity.this).update(
								"sososettinginfo",
								values,
								"userid = ?",
								new String[] {userid});
						if (values != null) {
							values.clear();
							values = null;
						}
					}else if(currentX > event.getX()){
						scrollView.scrollTo(width, 0);
						MyApplication.getInstance().setDisplayRoomPhoto(2);
						ContentValues values = new ContentValues();
						values.put("isshowimage", 2);
						DBHelper.getInstance(SoftwareSetActivity.this).update(
								"sososettinginfo",
								values,
								"userid = ?",
								new String[] {userid});
						if (values != null) {
							values.clear();
							values = null;
						}
					}
					currentX = event.getX();
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					/*if(boo){// 向右滚动
						scrollView.scrollTo(0, 0);
					}else{// 向左滚动
						scrollView.scrollTo(width-10, 0);
					}*/
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
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return true;
	}
}
