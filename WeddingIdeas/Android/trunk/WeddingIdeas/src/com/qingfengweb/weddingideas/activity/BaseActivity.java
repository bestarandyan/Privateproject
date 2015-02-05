/**
 * 
 */
package com.qingfengweb.weddingideas.activity;

import org.json.JSONObject;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.data.MyApplication;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author视频封面
 * @createDate 2013/12/26
 *
 */
public class BaseActivity extends Activity implements OnClickListener{
	public PopupWindow selectPopupWindow = null;//分享弹出框或者是设置弹出框
//	public LinearLayout parent;
	public Tencent tencent;
	public ProgressDialog progressDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_base);
//		parent = (LinearLayout) findViewById(R.id.parent);
	}
	/**
	 * 进度条弹出框
	 * @param message
	 */
	public void showProgressDialog(String message){
		if(progressDialog!=null && progressDialog.isShowing()){
			progressDialog.dismiss();
			progressDialog = null;
		}
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(message);
		progressDialog.show();
	}
	/**
	 * 初始化分享弹出框
	 */
	public void showShareDialog(View parent) {
		// PopupWindow浮动下拉框布局
		View bottomLayout = (View) this.getLayoutInflater().inflate(
				R.layout.dialog_share, null);
		bottomLayout.findViewById(R.id.sharelayout1).setOnClickListener(this);
		bottomLayout.findViewById(R.id.sharelayout2).setOnClickListener(this);
		bottomLayout.findViewById(R.id.sharelayout3).setOnClickListener(this);
		bottomLayout.findViewById(R.id.sharelayout4).setOnClickListener(this);
		bottomLayout.findViewById(R.id.sharelayout5).setOnClickListener(this);
		bottomLayout.findViewById(R.id.sharelayout6).setOnClickListener(this);
		bottomLayout.findViewById(R.id.closeWindowBtn1).setOnClickListener(this);
		selectPopupWindow = new PopupWindow(bottomLayout, parent.getWidth(),
				LayoutParams.WRAP_CONTENT, true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		selectPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setAnimationStyle(R.style.ShareDialogPopupAnimation);  
		selectPopupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.BOTTOM, 0, 0);  
		selectPopupWindow.update();  
	}
	
	public Dialog alertDialog = null;
	public void showCallDialog(final String tell) {
		alertDialog = new Dialog(this, R.style.sc_FullScreenDialog);
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View reNameView = mLayoutInflater.inflate(R.layout.dialog_phone, null);
		((TextView)reNameView.findViewById(R.id.tellTv)).setText("是否拨打"+tell);
		LayoutParams params = new LayoutParams((int) (MyApplication.getInstance().getScreenW()*0.8),LayoutParams.WRAP_CONTENT);
		alertDialog.addContentView(reNameView, params);
		alertDialog.show();
		Button  rb1 = (Button) reNameView.findViewById(R.id.rb1);
		Button rb2 = (Button) reNameView.findViewById(R.id.rb2);
		
		rb1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					alertDialog.dismiss();
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:"+tell));
					startActivity(intent);
				} catch (final Exception e) {
					
				}
			}
		});
		rb2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			return;
			}
		});

	}
	Dialog wechatDialog;
	public void showWechatShareDialog(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("发送名片");
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_wechat_share, null);
		view.findViewById(R.id.shareToGoodFriend).setOnClickListener(this);
		view.findViewById(R.id.shareToFriends).setOnClickListener(this);
		view.findViewById(R.id.cancle).setOnClickListener(this);
		alert.setView(view);
		wechatDialog = alert.create();
		wechatDialog.show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
