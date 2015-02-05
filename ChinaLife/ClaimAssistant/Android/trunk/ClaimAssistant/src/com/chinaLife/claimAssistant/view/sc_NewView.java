package com.chinaLife.claimAssistant.view;

import com.chinaLife.claimAssistant.sc_CaseHandlerFlowActivity;
import com.chinaLife.claimAssistant.sc_CaseManageActivity;
import com.chinaLife.claimAssistant.sc_CaseOfOnlyOneActivity;
import com.chinaLife.claimAssistant.sc_HelpActivity;
import com.chinaLife.claimAssistant.sc_MainActivity;
import com.chinaLife.claimAssistant.sc_MessageListActivity;
import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.R;
import com.chinaLife.claimAssistant.tools.sc_BigStone;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class sc_NewView extends View {
	// stone列表
	private sc_BigStone[] mStones;
	// 数目
	private static int STONE_COUNT = 5;

	private static int MENUS = 4;
	// 圆心坐标
	private float mPointX = 0, mPointY = 0;
	// 半径
	private int mRadius = 0;
	// 每两个点间隔的角度
	private int mDegreeDelta;
	public Context context;
	public sc_NewView(Context context, int px, int py, int radius) {
		super(context);
		this.context = context;
		mPointX = px;
		mPointY = py;
		mRadius = radius;

		setBackgroundColor(Color.TRANSPARENT);
		setupStones();
		computeCoordinates();

	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent e) {
		switch (e.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			
			for (int i = 0; i < STONE_COUNT; i++) {
				if (e.getX() >= mStones[i].x - 40
						&& e.getX() <= mStones[i].x + 60
						&& e.getY() >= mStones[i].y - 40
						&& e.getY() <= mStones[i].y + 60) {
					switch (i) {
					case 3:
						showCallDialog();
						break;
					case 4:
						sc_MyApplication.getInstance().setSelfHelpFlag(0);
						sc_MyApplication.getInstance().setCasedescription_tag(1);
						sc_MyApplication.getInstance().setPhoto_tag(2);
						Intent i2 = new Intent(context,
								sc_CaseOfOnlyOneActivity.class);
						context.startActivity(i2);
						((Activity) context).finish();
						break;
					case 2:
						sc_MyApplication.getInstance().setSelfHelpFlag(1);
						Intent i3 = new Intent(context,
								sc_CaseManageActivity.class);
						context.startActivity(i3);
						((Activity) context).finish();
						break;
					case 1:
						sc_MyApplication.getInstance().setSelfHelpFlag(0);
						Intent i4 = new Intent(context,
								sc_MessageListActivity.class);
						context.startActivity(i4);
						((Activity) context).finish();

						break;
					case 0:
						sc_MyApplication.getInstance().setSelfHelpFlag(0);
						Intent i5 = new Intent(context, sc_HelpActivity.class);
						context.startActivity(i5);
						((Activity) context).finish();

						break;
					}
				}
			}
			if (e.getX() >= mPointX - 40
					&& e.getX() <= mPointX + 60
					&& e.getY() >= mPointY -40
					&& e.getY() <= mPointY + 60) {
				sc_MyApplication.getInstance().setSelfHelpFlag(0);
				Intent i1 = new Intent(context,sc_CaseManageActivity.class);
				context.startActivity(i1);
				((Activity) context).finish();
			}
			return true;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		return false;
	}
	
	public void showCallDialog() {
		Dialog alertDialog = new Dialog(context, R.style.sc_FullScreenDialog);
		LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View reNameView = mLayoutInflater.inflate(R.layout.sc_dialog_phone, null);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		alertDialog.addContentView(reNameView, params);
		alertDialog.show();
		LinearLayout rb1 = (LinearLayout) reNameView.findViewById(R.id.rb1);
		LinearLayout rb2 = (LinearLayout) reNameView.findViewById(R.id.rb2);
		
		rb1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:95519"));
					context.startActivity(intent);
				} catch (Exception e) {
					Log.e("SampleApp", "Failed to invoke call", e);
				}
			}
		});
		rb2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:4008695519"));
					context.startActivity(intent);
				} catch (Exception e) {
					Log.e("SampleApp", "Failed to invoke call", e);
				}
			}
		});

	}
	@Override
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		paint.setAlpha(0x30);
		for (int index = 0; index < STONE_COUNT; index++) {
			drawInCenter(canvas, mStones[index].bitmap, mStones[index].x,mStones[index].y,index);
		}
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.sc_home_btn6);
		canvas.drawBitmap(bm, mPointX - bm.getWidth() / 2,
				mPointY - bm.getHeight() / 2, null);
		sc_MyApplication.getInstance().setmStones(mStones);
	}
	void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top,int index) {
		canvas.drawBitmap(bitmap, left - bitmap.getWidth() / 2,
				top - bitmap.getHeight() / 2, null);
		int number = sc_MyApplication.getInstance().getMessageNumber();
		if(index == 1 && number>0){
//			Bitmap bmap = BitmapFactory.decodeResource(getResources(), R.drawable.home_dot);
			canvas.drawBitmap(getBitmap(number), left,
					top - bitmap.getHeight() / 2, null);
		}

	}
	public Bitmap getBitmap(int number){
		View inflater = LayoutInflater.from(context).inflate(R.layout.sc_mybtn, null);
		Button btn = (Button) inflater.findViewById(R.id.btn);
		btn.setBackgroundResource(R.drawable.sc_home_dot);
		btn.setText(number+"");
		return convertViewToBitmap(btn);
	}
	/**
	 * 浠嶸iew涓緱鍒癇itmap
	 * @param view
	 * @return
	 */
	public  Bitmap convertViewToBitmap(View view){
	   view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        view.layout(0,0, view.getMeasuredWidth(), view.getMeasuredHeight());
	        view.buildDrawingCache();
	 return view.getDrawingCache();
	}
	private void computeCoordinates() {
		sc_BigStone stone;
		for (int index = 0; index < STONE_COUNT; index++) {
			stone = mStones[index];
			stone.x = mPointX
					+ (float) (mRadius * Math.cos(stone.angle * Math.PI / 180));
			stone.y = mPointY
					+ (float) (mRadius * Math.sin(stone.angle * Math.PI / 180));
		}
	}

	private void setupStones() {
		mStones = new sc_BigStone[STONE_COUNT];
		sc_BigStone stone;
		int angle = 18;
		mDegreeDelta = 360 / STONE_COUNT;
		for (int index = 0; index < STONE_COUNT; index++) {
			stone = new sc_BigStone();
			stone.angle = angle;
			stone.bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.sc_home_btn1+index);
			angle += mDegreeDelta;
			mStones[index] = stone;
		}

	}

}
