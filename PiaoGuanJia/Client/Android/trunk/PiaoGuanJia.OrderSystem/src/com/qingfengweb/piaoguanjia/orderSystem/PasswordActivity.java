package com.qingfengweb.piaoguanjia.orderSystem;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.qingfengweb.piaoguanjia.orderSystem.model.UserInfo;
import com.qingfengweb.piaoguanjia.orderSystem.request.UserServlet;
import com.qingfengweb.piaoguanjia.orderSystem.util.JsonUtils;
import com.qingfengweb.piaoguanjia.orderSystem.util.MessageBox;
import com.qingfengweb.piaoguanjia.orderSystem.util.NetworkCheck;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

@ContentView(R.layout.activity_password)
public class PasswordActivity extends BaseActivity {

	@ViewInject(R.id.parent)
	private View parent;
	@ViewInject(R.id.et1)
	private EditText et1;
	@ViewInject(R.id.et2)
	private EditText et2;
	@ViewInject(R.id.et3)
	private EditText et3;
	@ViewInject(R.id.linearbtn)
	private LinearLayout linearbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		parent.setOnTouchListener(this);
	}

	@OnClick({ R.id.linear_back, R.id.btn_keep, R.id.checkBox })
	public void btnonClick(View v) {
		if (v.getId() == R.id.linear_back) {
			finish();
		} else if (v.getId() == R.id.btn_keep) {
			if (!validate()) {
				return;
			}
			new Thread(runnableUpdatePassword).start();
		} else if (v.getId() == R.id.checkBox) {
			CheckBox checkbox = (CheckBox) v;
			if (checkbox.isChecked()) {
				linearbtn.setVisibility(View.GONE);
				et1.setInputType(0x00000001);
				et2.setInputType(0x00000001);
			} else {
				et1.setInputType(0x00000081);
				et2.setInputType(0x00000081);
				linearbtn.setVisibility(View.VISIBLE);
			}
		}
		super.onClick(v);
	}
	
	@Override
	public boolean validate() {
		if(et1.getText().toString().trim().equals("")){
			MessageBox.promptDialog("请输入原密码", this);
			return false;
		}else if(et2.getText().toString().trim().equals("")){
			MessageBox.promptDialog("请输入新密码", this);
			return false;
		}else if(et3.isShown()&&!(et3.getText().toString().trim().equals(et2.getText().toString().trim()))){
			MessageBox.promptDialog("两次密码不一致", this);
			return false;
		}
		return true;
	}
	
	
	/***
	 * 修改密码
	 */
	private Runnable runnableUpdatePassword = new Runnable() {
		
		@Override
		public void run() {
			if(NetworkCheck.IsHaveInternet(PasswordActivity.this)){
				PROGRESSMSG = "正在修改密码，请稍等...";
				handler.sendEmptyMessage(PROGRESSSTART_HANDLER);
				String oldPassword = et1.getText().toString().trim();
				String newPassword = et2.getText().toString().trim();
				String response = UserServlet.actionUpdatepassword(oldPassword,newPassword);
				handler.sendEmptyMessage(PROGRESSEND_HANDLER);
				if(checkResponse(response)){
					try {
						MyApplication.userinfo.setPassword(newPassword);
						MyApplication.dbuser.save(MyApplication.userinfo);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
			}else{
				handler.sendEmptyMessage(NONETWORK_HANDLER);
			}
		}
	};

}
