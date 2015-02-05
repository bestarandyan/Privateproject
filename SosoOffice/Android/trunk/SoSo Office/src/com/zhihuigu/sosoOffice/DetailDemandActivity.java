/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.UserType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoCusReleaseInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

/**
 * @author 刘星星
 * @createDate 2013/2/21 需求详情类
 * 
 */
public class DetailDemandActivity extends BaseActivity implements
		Activity_interface {
	private Button backBtn;// 返回按钮
	private TextView titleText;// 标题
	private TextView phoneText;// 联系方式
	private TextView roomTypeText;// 房源类型
	private TextView moneyText;// 房租金额
	private TextView acreageText;// 房源面积
	private TextView simpleIntroText;// 简介说明
	private LinearLayout deleteDemand;// 删除需求
	private ArrayList<HashMap<String, Object>> list;

	private SoSoUploadData uploaddata;// 服务器请求对象
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;

	private String ReleaseID = "";// 需求id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detaildemand);
		findView();
		initView();

	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			CommonUtils.hideSoftKeyboard(this);
			finish();
		} else if (v == deleteDemand) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					DetailDemandActivity.this);
			builder.setMessage("确定要删除该需求吗？")
					.setTitle(getResources().getString(R.string.prompt))
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									new Thread(runnable).start();
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									return;
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		titleText = (TextView) findViewById(R.id.titleText);
		phoneText = (TextView) findViewById(R.id.phone);
		roomTypeText = (TextView) findViewById(R.id.roomTypeText);
		moneyText = (TextView) findViewById(R.id.moneyText);
		acreageText = (TextView) findViewById(R.id.acreageText);
		simpleIntroText = (TextView) findViewById(R.id.jianJieText);
		deleteDemand = (LinearLayout) findViewById(R.id.deleteLayout);
		if(MyApplication.getInstance().getRoleid()==UserType.UserTypeCustomer.getValue()){
			deleteDemand.setVisibility(View.VISIBLE);
		}else{
			deleteDemand.setVisibility(View.GONE);
		}
	}

	@Override
	public void initView() {
		backBtn.setOnClickListener(this);
		deleteDemand.setOnClickListener(this);
		Bundle bundle = getIntent().getBundleExtra("bundle");
		String title = bundle.getString("name");
		String phone = bundle.getString("phone");
		String roomType = bundle.getString("officetype");
		String pricedown = bundle.getString("pricedown");
		String priceup = bundle.getString("priceup");
		String areadown = bundle.getString("areadown");
		String areaup = bundle.getString("areaup");
		String money = pricedown + "到" + priceup + "元/平米/天";
		String acreage = areadown + "到" + areaup + "m²";
		String simpleIntro = bundle.getString("description");
		ReleaseID = bundle.getString("releaseid");
		if (roomType.equals("0")) {
			roomType = "纯写字楼";
		} else if (roomType.equals("1")) {
			roomType = "商住楼";
		}else if (roomType.equals("2")) {
			roomType = "酒店式公寓";
		}else if (roomType.equals("3")) {
			roomType = "园区";
		}else if (roomType.equals("4")) {
			roomType = "商务中心";
		}
		if (pricedown.equals("") && priceup.equals("")) {
			money = "";
		} else if (pricedown.equals("") || priceup.equals("")) {
			money = pricedown + priceup + "元/平米/天";
		}
		if (areadown.equals("") && areaup.equals("")) {
			acreage = "";
		} else if (areadown.equals("") || areaup.equals("")) {
			acreage = pricedown + priceup + "m²";
		}
		titleText.setText(title);
		phoneText.setText(phone);
		roomTypeText.setText(roomType);
		moneyText.setText(money);
		acreageText.setText(acreage);
		final String sText = "<font color=\"#666666\">简介说明：</font>"+simpleIntro;
		simpleIntroText.setText(Html.fromHtml(sText));
	}

	@Override
	public void initData() {

	}

	@Override
	public void notifiView() {

	}

	/**
	 * 删除需求
	 * 
	 * @author Ring
	 * @since 2013-01-25 15:14
	 * @return true 发布成功，false 发布失败
	 */
	public boolean deleteReleaseInfo() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("RepleaseID", ReleaseID));
		uploaddata = new SoSoUploadData(this, "CurReleaseDelete.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse();
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author Ring
	 * @since 2013-01-25 15:13
	 */
	public void dealReponse() {
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(DetailDemandActivity.this).delete(
					"soso_cusreleaseinfo", "releaseid = ?",
					new String[] { ReleaseID });
			DBHelper.getInstance(this).close();
		}
	}

	/**
	 * author by Ring 处理耗时操作
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(DetailDemandActivity.this)) {
				handler.sendEmptyMessage(5);// 开启进度条
				boolean b = false;// 添加房源信息是否成功的标识
				b = deleteReleaseInfo();
				handler.sendEmptyMessage(6);// 关闭进度条
				if (b) {
					handler.sendEmptyMessage(1);// 删除成功

				} else {
					handler.sendEmptyMessage(3);// 删除失败
				}
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}
			click_limit = true;
		}
	};

	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 从登录界面跳转到主界面
				AlertDialog.Builder builder = new AlertDialog.Builder(
						DetailDemandActivity.this);
				builder.setMessage(
						getResources().getString(R.string.deletereleaseroom_success))
						.setTitle(getResources().getString(R.string.prompt))
						.setCancelable(false)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
										finish();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
				break;
			case 2:// 从登录界面跳转到注册界面
				break;
			case 3:// 登录失败给用户提示
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				} else {
					errormsg = getResources().getString(
							R.string.deletereleaseroom_failure);
				}
				MessageBox.CreateAlertDialog(errormsg,
						DetailDemandActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(DetailDemandActivity.this
						);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(
						DetailDemandActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_submit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						click_limit = true;
						if (uploaddata != null) {
							uploaddata.overReponse();
						}
						return false;
					}
				});
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		};
	};

}
