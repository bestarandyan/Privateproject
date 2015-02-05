/**
 * 
 */
package com.boluomi.children.activity;

import com.boluomi.children.R;
import com.boluomi.children.data.MyApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013、8、20
 *所有Activity的基类
 */
public class BaseActivity extends Activity implements OnClickListener,OnItemClickListener{
	private PopupWindow selectPopupWindow = null;//分享弹出框或者是设置弹出框
	Dialog moreShareDialog = null;//更多选项弹出框
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
	}
	
	 /**
	 * 退出系统时的提示函数 @author 刘星星
	 */
	public void showExitDialog(){
		AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
		  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
		  			.setTitle("提示")
					.setMessage("您确定要退出系统吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									android.os.Process.killProcess(android.os.Process.myPid());
									System.exit(0);
									finish();
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return ;
								}
							});
		  callDailog.show();
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
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dialog_border));
		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setAnimationStyle(R.style.PopupAnimation);  
		selectPopupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.BOTTOM, 0, 0);  
		selectPopupWindow.update();  
	}
	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
