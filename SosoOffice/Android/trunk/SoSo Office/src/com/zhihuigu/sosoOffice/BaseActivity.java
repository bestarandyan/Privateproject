package com.zhihuigu.sosoOffice;

import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class BaseActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 退出系统时的提示函数 @author 刘星星
	 */
	public void showExitDialog(){
		AlertDialog.Builder callDailog = new AlertDialog.Builder(getParent());
		  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
		  			.setTitle("提示")
					.setMessage("您确定要退出系统吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									String userid = "0";
									ContentValues values = new ContentValues();
									values.put("roleid", MyApplication.getInstance().getRoleid());
									DBHelper.getInstance(BaseActivity.this).update(
											"sososettinginfo",
											values,
											"userid = ?",
											new String[] {userid});
									if (values != null) {
										values.clear();
										values = null;
									}
									android.os.Process.killProcess(android.os.Process.myPid());
									finish();
									System.exit(0);
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
	 * 退出系统时的提示函数 @author 刘星星
	 */
	public void showExitDialog(Context context){
		AlertDialog.Builder callDailog = new AlertDialog.Builder(context);
		  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
		  			.setTitle("提示")
					.setMessage("您确定要退出系统吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									String userid = "0";
									ContentValues values = new ContentValues();
									values.put("roleid", MyApplication.getInstance().getRoleid());
									DBHelper.getInstance(BaseActivity.this).update(
											"sososettinginfo",
											values,
											"userid = ?",
											new String[] {userid});
									if (values != null) {
										values.clear();
										values = null;
									}
									android.os.Process.killProcess(android.os.Process.myPid());
									finish();
									System.exit(0);
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return ;
								}
							});
		  callDailog.show();
	}
	/*--------------------------------------------------------对话框处理处  start----------------------------------------------------------------------------
	 * ---------------------------------------------------------------------------------------------------------------------------------------------------------
	 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------
	 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------*/		
			public LinearLayout parent = null;
			public PopupWindow selectPopupWindow = null;
			public LinearLayout messageLayout;
			public Button displayImgBtn,hideImgBtn;
			/**
			 * 初始化弹出框窗口的布局
			 * @author 刘星星
			 * @creageDate 2013/1/30
			 */
			public void displayRoomImgDialog(){
				int width = 0;
				int height = 0;
				LinearLayout dialogwindow = null;
				height = 200;
				try{
				dialogwindow = (LinearLayout) this.getParent().getLayoutInflater().
							inflate(R.layout.dialog_recommend, null);
				}catch(Exception e){
					dialogwindow = (LinearLayout) this.getLayoutInflater().
							inflate(R.layout.dialog_recommend, null);
				}
				messageLayout = (LinearLayout) dialogwindow.findViewById(R.id.messageLayout);
				displayImgBtn = (Button) dialogwindow.findViewById(R.id.displayImgBtn);
				hideImgBtn = (Button) dialogwindow.findViewById(R.id.hideImgBtn);
						dialogwindow.getBackground().setAlpha(100);
				parent = (LinearLayout) findViewById(R.id.parent);
				displayImgBtn.setOnClickListener(this);
				hideImgBtn.setOnClickListener(this);
				width = (int)(parent.getWidth()*0.8f);
				selectPopupWindow = new PopupWindow(dialogwindow, width,	LayoutParams.WRAP_CONTENT, true);
				// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
				// 没有这一句则效果不能出来，但并不会影响背景
				selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.dialog_recommend_border));
				selectPopupWindow.getBackground().setAlpha(180);
				selectPopupWindow.setOutsideTouchable(true);
				selectPopupWindow.showAsDropDown(parent, (parent.getWidth()-width)/2, -(parent.getHeight()-(parent.getHeight()-height)/2));
				selectPopupWindow.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						dismiss();
					}
				});
			}
			
			/**
			 * PopupWindow消失
			 */
			public void dismiss() {
				if(selectPopupWindow!=null && selectPopupWindow.isShowing()){
					selectPopupWindow.dismiss();
				}
			}	
			/*--------------------------------------------------------对话框处理处  end----------------------------------------------------------------------------
			 * ---------------------------------------------------------------------------------------------------------------------------------------------------------
			 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------
			 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------*/		
}
