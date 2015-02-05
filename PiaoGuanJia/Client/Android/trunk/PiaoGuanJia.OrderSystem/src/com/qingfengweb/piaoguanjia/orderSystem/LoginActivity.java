package com.qingfengweb.piaoguanjia.orderSystem;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingfengweb.piaoguanjia.orderSystem.model.UserInfo;
import com.qingfengweb.piaoguanjia.orderSystem.request.UserServlet;
import com.qingfengweb.piaoguanjia.orderSystem.util.JsonUtils;
import com.qingfengweb.piaoguanjia.orderSystem.util.MessageBox;
import com.qingfengweb.piaoguanjia.orderSystem.util.NetworkCheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.et_username)
	private EditText et_username;
	@ViewInject(R.id.et_password)
	private EditText et_password;
	@ViewInject(R.id.parentview)
	private View parentview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		parentview.setOnTouchListener(this);
		et_username.setText(MyApplication.username);
		et_password.setText(MyApplication.password);
	}

	@OnClick(R.id.btn_login)
	public void btnloginonClick(View v) {
			if (!validate()) {
				return;
			}
			
			new Thread(runnableLogin).start();
			super.onClick(v);
	}
	
	
	/***
	 * 登陆请求
	 */
	private Runnable runnableLogin = new Runnable() {
		@Override
		public void run() {
			if(NetworkCheck.IsHaveInternet(LoginActivity.this)){
				PROGRESSMSG = "正在登陆，请稍等...";
				handler.sendEmptyMessage(PROGRESSSTART_HANDLER);
				String username = et_username.getText().toString().trim();
				String password = et_password.getText().toString().trim();
				MyApplication.username = username;
				MyApplication.password = password;
				String response = UserServlet.actionLogin();
				handler.sendEmptyMessage(PROGRESSEND_HANDLER);
				if(checkResponse(response)){
					UserInfo userinfonew = null;
					try {
						userinfonew = MyApplication.db.findFirst(
									Selector.from(UserInfo.class).where("username", "=",
											MyApplication.username));
					} catch (DbException e1) {
						e1.printStackTrace();
					}
					UserInfo userinfo = JsonUtils.jsonObject(UserInfo.class, response);
					userinfo.setPassword(password);
					userinfo.setUsername(username);
					userinfo.setAutoLogin(1);
					if(userinfonew==null){
						MyApplication.userinfo = userinfo;
						try {
							MyApplication.db.saveOrUpdate(userinfo);
						} catch (DbException e) {
							e.printStackTrace();
						}
					}else{
						userinfonew.setPassword(userinfo.getPassword());
						userinfonew.setUsername(userinfo.getUsername());
						userinfonew.setAutoLogin(1);
						userinfonew.setUserid(userinfo.getUserid());
						userinfonew.setIsLimitOrder(userinfo.getIsLimitOrder());
						try {
							MyApplication.db.saveOrUpdate(userinfonew);
						} catch (DbException e) {
							e.printStackTrace();
						}
						MyApplication.userinfo = userinfonew;
					}
					MyApplication.userid = userinfo.userid;
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainTabFragment.class);
					startIntent(intent);
					finish();
					MyApplication.dbuser = DbUtils.create(getApplication(), "piaoguanjia_"+MyApplication.username);
				}
			}else{
				handler.sendEmptyMessage(NONETWORK_HANDLER);
			}
		}
	};

	/**
	 * 验证
	 */
	@Override
	public boolean validate() {
		String username = et_username.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		if (username.equals("")) {
			MessageBox.promptDialog("用户名不能为空！", this);
			return false;
		} else if (password.equals("")) {
			MessageBox.promptDialog("密码不能为空！", this);
			return false;
		}
		return true;
	}

}
