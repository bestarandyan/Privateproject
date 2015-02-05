package com.piaoguanjia.accountManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.piaoguanjia.accountManager.bean.WayStatus;
import com.piaoguanjia.accountManager.database.DBHelper;
import com.piaoguanjia.accountManager.database.TableCreate;
import com.piaoguanjia.accountManager.request.HandleData;
import com.piaoguanjia.accountManager.request.RequestServerFromHttp;
import com.piaoguanjia.accountManager.util.CommonUtils;
import com.piaoguanjia.accountManager.util.ImageDownloaderId;
import com.piaoguanjia.accountManager.util.MD5;
import com.piaoguanjia.accountManager.util.MessageBox;
import com.piaoguanjia.accountManager.util.NetworkCheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

public class CheckManagerInfoActivity extends MainFrameActivity {
	public int activitytype = 0;//
	public int usertype = 1;// 1 总帐户，2专用账户
	public int rechargetype = 1;// 有凭证，2无凭证
	public int status;// 0,待审核，1历史记录

	public int auditstatus;// 审核的状态值
	public String auditreason;// 审核不通过原因

	public static final int ID_IMAGEVIEW = 0x7f44763;// 凭证图片id
	public static final int ID_SCROLLVIEW1 = 0x7f44764;// 滚动视图1
	public static final int ID_SCROLLVIEW2 = 0x7f44765;// 滚动视图2
	private ArrayList<TextView> views = new ArrayList<TextView>();// 装载Textview集合
	private String id;// 详情id
	String btnstr1 = "";
	String btnstr2 = "";
	private boolean clicked1 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rl1.setBackgroundColor(Color.parseColor("#3ABFDE"));
		activitytype = getIntent().getIntExtra("activitytype", 0);
		status = getIntent().getIntExtra("status", 1);
		// 添加第一个滚动视图
		String[] names = null;
		if (status == 0) {
			btnstr1 = "审核拒绝";
			btnstr2 = "审核通过";
			setBottomEnable(true);
		} else {
			setBottomEnable(false);
		}
		if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI) {
			names = new String[] { "编号", "来源", "用户名", "充值方式", "充值账户", "充值产品",
					"充值时间", "充值金额", "累计金额", "账户余额", "资金来源", "授权管理员", "申请发票",
					"备注" };
			initView("充值审核", "充值详情", "充值凭证", btnstr1, btnstr2, true);
			setTopEnable(true);
		} else if (activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
			names = new String[] { "用户名", "分销商姓名", "分销产品", "充值限制", "余额提醒",
					"提醒方式", "直充额度" };
			setTopEnable(false);
			initView("专用账户审核", "", "", btnstr1, btnstr2, true);
		} else if (activitytype == CheckManagerActivity.TYPE_EDU) {
			names = new String[] { "用户名", "帐户类型", "分销商姓名", "分销产品", "信用额度",
					"增加原因" };
			setTopEnable(false);
			initView("额度审核", "", "", btnstr1, btnstr2, true);
		}
		ScrollView scrollView1 = getListScrollView(names);
		scrollView1.setId(ID_SCROLLVIEW1);
		addCenterView(scrollView1);

		// 添加第二个滚动视图
		ScrollView scrollView2 = getScrollView();
		scrollView2.setId(ID_SCROLLVIEW2);
		addCenterView(scrollView2);

		scrollView2.setVisibility(View.GONE);
		initData();
		if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI
				|| activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
			new Thread(this).start();
		}
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI) {
			initChongzhiData();
		} else if (activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
			initZhuanYongData();
		} else if (activitytype == CheckManagerActivity.TYPE_EDU) {
			initEduData();
		}
	}

	/**
	 * 初始化充值数据
	 */
	private void initChongzhiData() {
		List<Map<String, Object>> selectresult = null;
		id = getIntent().getStringExtra("id");
		selectresult = DBHelper.getInstance().selectRow(
				"select * from " + TableCreate.TABLENAME_CHARGEINFO
						+ " where chargid = '" + id + "'", null);
		if (selectresult == null || selectresult.size() <= 0) {
			return;
		}

		// 充值账户
		if (selectresult.get(0).get("accounttype") != null
				&& selectresult.get(0).get("accounttype").equals("1")) {
			views.get(4).setText("总帐户");
			// 授权管理员
			views.get(11).setVisibility(View.VISIBLE);
			((View) views.get(5).getTag()).setVisibility(View.GONE);
			if (selectresult.get(0).get("warrantor") != null) {
				views.get(11).setText(
						selectresult.get(0).get("warrantor").toString());
			}
		} else {
			// 授权管理员
			views.get(11).setVisibility(View.VISIBLE);
			if (selectresult.get(0).get("warrantor") != null) {
				views.get(11).setText(
						selectresult.get(0).get("warrantor").toString());
			}
			// 充值产品（专用账户所有）
			views.get(5).setVisibility(View.VISIBLE);
			String productid = "";
			if (selectresult.get(0).get("productid") != null) {
				productid = selectresult.get(0).get("productid").toString();
			}

			if (selectresult.get(0).get("productname") != null) {
				views.get(5).setText(
						selectresult.get(0).get("productname").toString() + "【"
								+ productid + "】");
			}

			views.get(4).setText("专用账户");
		}

		if (selectresult.get(0).get("iscertificate") != null
				&& selectresult.get(0).get("iscertificate").toString()
						.equals("1")) {
			initView("充值详情", "充值详情", "充值凭证", btnstr1, btnstr2, true);
			((View) views.get(11).getTag()).setVisibility(View.GONE);
			// 有充值凭证
			ImageView imageview = (ImageView) findViewById(ID_IMAGEVIEW);
			Bitmap bm = null;
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/piaoguanjia/image");
			if (!file.exists()) {
				file.mkdirs();
			}
			File imagefile = new File(file, MD5.getMD5(id) + ".png");
			bm = CommonUtils.getDrawable(imagefile.getAbsolutePath(), null);
			if (bm != null) {
				imageview.setImageBitmap(bm);
			} else {
				ImageDownloaderId imagedownloaderid = new ImageDownloaderId(
						this, 10);
				imagedownloaderid.download(imagefile, id, imageview);
			}
		} else {
			initView("充值详情", "", "", btnstr1, btnstr2, true);
			setTopEnable(false);
		}

		if (views != null) {
			// 编号
			if (selectresult.get(0).get("chargid") != null) {
				views.get(0).setText(
						selectresult.get(0).get("chargid").toString());
			}
			// 来源
			if (selectresult.get(0).get("source") != null) {
				views.get(1).setText(
						selectresult.get(0).get("source").toString());
			}
			// 用户名
			if (selectresult.get(0).get("username") != null) {
				views.get(2).setText(
						selectresult.get(0).get("username").toString());
			}
			// 充值方式
			if (selectresult.get(0).get("type") != null) {
				int id = Integer.parseInt(selectresult.get(0).get("type").toString());
				views.get(3)
						.setText(WayStatus.getName(id));
				if(id==1){
					setBottomEnable(false);
				}
			}
			// 充值时间
			if (selectresult.get(0).get("createtime") != null) {
				views.get(6).setText(
						selectresult.get(0).get("createtime").toString());
			}
			// 充值金额
			if (selectresult.get(0).get("amount") != null) {
				views.get(7).setText(
						"￥" + selectresult.get(0).get("amount").toString());
			}
			// 累计金额
			if (selectresult.get(0).get("totalamount") != null) {
				views.get(8)
						.setText(
								"￥"
										+ selectresult.get(0)
												.get("totalamount").toString());
			}
			// 账户余额
			if (selectresult.get(0).get("balance") != null) {
				views.get(9).setText(
						"￥" + selectresult.get(0).get("balance").toString());
			}
			// 资金来源
			if (selectresult.get(0).get("balancesource") != null) {
				if (selectresult.get(0).get("balancesource").toString()
						.equals("1")) {
					views.get(10).setText("支付宝");
				} else if (selectresult.get(0).get("balancesource").toString()
						.equals("2")) {
					views.get(10).setText("银行账号");
				} else if (selectresult.get(0).get("balancesource").toString()
						.equals("3")) {
					views.get(10).setText("现金");
				}
			}
			// 申请发票
			if (selectresult.get(0).get("isreceipt") != null) {
				views.get(12).setText(
						selectresult.get(0).get("isreceipt").toString()
								.equals("1") ? "是" : "否");
			}
			// 备注
			if (selectresult.get(0).get("remark") != null) {
				views.get(13).setText(
						selectresult.get(0).get("remark").toString());
			}
		}
	}

	/**
	 * 初始化充值数据
	 */
	private void initZhuanYongData() {
		List<Map<String, Object>> selectresult = null;
		id = getIntent().getStringExtra("id");
		selectresult = DBHelper.getInstance().selectRow(
				"select * from " + TableCreate.TABLENAME_ACCOUNTINFO
						+ " where accountid = '" + id + "'", null);
		if (selectresult == null || selectresult.size() <= 0) {
			return;
		}
		if (views != null) {
			// 用户名
			if (selectresult.get(0).get("username") != null) {
				views.get(0).setText(
						selectresult.get(0).get("username").toString());
			}
			// 分销商姓名
			if (selectresult.get(0).get("name") != null) {
				views.get(1)
						.setText(selectresult.get(0).get("name").toString());
			}
			// 分销产品
			if (selectresult.get(0).get("productname") != null) {
				views.get(2).setText(
						selectresult.get(0).get("productname").toString());
			}
			// 充值限制
			if (selectresult.get(0).get("chargelimit") != null) {
				views.get(3)
						.setText(
								"￥"
										+ selectresult.get(0)
												.get("chargelimit").toString());
			}
			// 余额提醒
			if (selectresult.get(0).get("remindamount") != null) {
				views.get(4).setText(
						"￥"
								+ selectresult.get(0).get("remindamount")
										.toString());
			}
			// 提醒方式
			if (selectresult.get(0).get("remindtype") != null) {
				if (selectresult.get(0).get("remindtype").equals("1")) {
					views.get(5).setText("短信");
				} else if (selectresult.get(0).get("remindtype").equals("2")) {
					views.get(5).setText("通知");
				} else {
					views.get(5).setText("其它");
				}
			}
			// 直充额度
			if (selectresult.get(0).get("onlinechargelimit") != null) {
				views.get(6).setText(
						"￥"
								+ selectresult.get(0).get("onlinechargelimit")
										.toString());
			}

		}
	}

	/**
	 * 初始化额度数据
	 */
	private void initEduData() {

		// names = new String[] { "用户名", "帐户类型", "分销商姓名", "分销产品", "信用额度",
		// "增加原因" };
		List<Map<String, Object>> selectresult = null;
		id = getIntent().getStringExtra("id");
		selectresult = DBHelper.getInstance().selectRow(
				"select * from " + TableCreate.TABLENAME_CREDITINFO
						+ " where creditid = '" + id + "'", null);
		if (selectresult == null || selectresult.size() <= 0) {
			return;
		}
		// names = new String[] { "用户名","帐户类型", "分销商姓名", "分销产品", "信用额度", "增加原因"
		// };
		if (views != null) {
			// 用户名
			if (selectresult.get(0).get("username") != null) {
				views.get(0).setText(
						selectresult.get(0).get("username").toString());
			}
			// 账户类型
			if (selectresult.get(0).get("accounttype") != null
					&& selectresult.get(0).get("accounttype").equals("1")) {
				views.get(1).setText("总帐户");
				((View) views.get(3).getTag()).setVisibility(View.GONE);
			} else {
				views.get(1).setText("专用帐户");
			}
			// 分销商姓名
			if (selectresult.get(0).get("name") != null) {
				views.get(2)
						.setText(selectresult.get(0).get("name").toString());
			}
			// 分销产品
			if (selectresult.get(0).get("productname") != null) {
				views.get(3).setText(
						selectresult.get(0).get("productname").toString());
			}
			// 信用额度
			if (selectresult.get(0).get("creditlimit") != null) {
				views.get(4)
						.setText(
								"￥"
										+ selectresult.get(0)
												.get("creditlimit").toString());
			}
			// 增加原因
			if (selectresult.get(0).get("reason") != null) {
				views.get(5).setText(
						selectresult.get(0).get("reason").toString());
			}
		}
	}

	/**
	 * 
	 * @param 详情列表
	 * @return
	 */

	public ScrollView getListScrollView(String[] names) {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout linearLayout = new LinearLayout(this);
		LinearLayout.LayoutParams prarms = new LinearLayout.LayoutParams(
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
				android.support.v4.view.ViewPager.LayoutParams.WRAP_CONTENT);
		linearLayout.setLayoutParams(prarms);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		if (views == null) {
			views = new ArrayList<TextView>();
		}
		views.clear();
		for (int i = 0; i < names.length; i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.listitem1,
					null);
			if (names[i].equals("充值金额")) {
				((TextView) (view.findViewById(R.id.tv2)))
						.setTextColor(getResources()
								.getColor(R.color.xuehongse));
			}
			((TextView) (view.findViewById(R.id.tv1))).setText(names[i]);
			((TextView) (view.findViewById(R.id.tv2))).setTag(view);
			((TextView) (view.findViewById(R.id.tv1)))
					.setWidth(AccountApplication.widthPixels / 3);
			if (names.length <= 1) {
				view.setBackgroundResource(R.drawable.app_list_corner_round);
			} else {
				if (i == 0) {
					view.setBackgroundResource(R.drawable.app_list_corner_round_top);
				} else if (i == names.length - 1) {
					view.setBackgroundResource(R.drawable.app_list_corner_round_bottom);
				} else if (i == names.length - 2) {
					view.setBackgroundResource(R.drawable.linearbg1);
				} else {
					view.setBackgroundResource(R.drawable.linearbg);
				}
			}
			views.add((TextView) (view.findViewById(R.id.tv2)));
			linearLayout.addView(view);
		}
		scrollView.addView(linearLayout);
		return scrollView;
	}

	/**
	 * 
	 * @param 获取凭证
	 * @return
	 */

	public ScrollView getScrollView() {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout linearLayout = new LinearLayout(this);
		LinearLayout.LayoutParams prarms = new LinearLayout.LayoutParams(
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT);
		linearLayout.setLayoutParams(prarms);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(0x11);
		ImageView imageView = new ImageView(this);
//		imageView
//				.setImageBitmap(CommonUtils.getDrawable(
//						Environment.getExternalStorageDirectory() + "/1.jpg",
//						imageView));
		imageView.setId(ID_IMAGEVIEW);
		imageView.setOnClickListener(this);
		linearLayout.addView(imageView);
		scrollView.addView(linearLayout);
		return scrollView;
	}

	public void ShowConnectDialog(int type, String msg) {
		LinearLayout loginLayout1 = (LinearLayout) getLayoutInflater().inflate(
				R.layout.dialog_layout, null);
		Button dialog_btn1 = (Button) loginLayout1
				.findViewById(R.id.dialog_btn1);
		Button dialog_btn2 = (Button) loginLayout1
				.findViewById(R.id.dialog_btn2);
		TextView tv_content = (TextView) loginLayout1
				.findViewById(R.id.tv_content);
		EditText et_content = (EditText) loginLayout1
				.findViewById(R.id.et_content);
		dialog_btn1.setOnClickListener(this);
		dialog_btn2.setOnClickListener(this);
		tv_content.setText(msg);
		if (type == 1) {
			et_content.setVisibility(View.VISIBLE);
			et_content.setHint("【注意：原因必填项，最多100个字符】");
		} else {
			et_content.setVisibility(View.GONE);
		}

		Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(loginLayout1);
		dialog.show();
		dialog_btn1.setTag(dialog);
		dialog_btn2.setTag(dialog);
	}

	@Override
	public void onClick(View v) {
		if (v == btn1) {
			if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI) {
				auditstatus = 2;
			} else if (activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
				auditstatus = 4;
			} else if (activitytype == CheckManagerActivity.TYPE_EDU) {
				auditstatus = 4;
			}
			ShowConnectDialog(1, "审核不通过的原因：");
		} else if (v == btn2) {
			if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI) {
				auditstatus = 1;
			} else if (activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
				auditstatus = 2;
			} else if (activitytype == CheckManagerActivity.TYPE_EDU) {
				auditstatus = 2;
			}
			ShowConnectDialog(2, "确定要审核通过吗？");
		} else if (v.getId() == R.id.dialog_btn1) {
			((Dialog) (v.getTag())).dismiss();
		} else if (v.getId() == R.id.dialog_btn2) {
			EditText et = ((EditText) (((Dialog) (v.getTag()))
					.findViewById(R.id.et_content)));
			if (et.isShown()) {
				auditreason = et.getText().toString();
				if (auditreason != null && !auditreason.equals("")) {
					((Dialog) (v.getTag())).dismiss();
					new Thread(runnableCheckAudit).start();
				}
			} else {
				((Dialog) (v.getTag())).dismiss();
				new Thread(runnableCheckAudit).start();
			}
		} else if (v == rl1) {
			findViewById(ID_SCROLLVIEW1).setVisibility(View.VISIBLE);
			findViewById(ID_SCROLLVIEW2).setVisibility(View.GONE);
		} else if (v == rl2) {
			findViewById(ID_SCROLLVIEW1).setVisibility(View.GONE);
			findViewById(ID_SCROLLVIEW2).setVisibility(View.VISIBLE);
		} else if (v.getId() == ID_IMAGEVIEW) {
			if (v.getTag() != null) {
				File file = new File(v.getTag().toString());
				if (file != null && file.exists()) {
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(file), "image/*");
					startActivity(intent);
				}
			}
		}
		super.onClick(v);
	}

	/**
	 * 页面逻辑处理
	 */
	public Handler runnablehandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RESPONSE_HANDLER:
				String errormsg = "";
				Bundle b = msg.getData();
				String response = b.getString(KEY_RESPONSE);
				if (response.equals("1")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							CheckManagerInfoActivity.this);
					builder.setMessage("操作成功")
							.setTitle("提示")
							.setCancelable(false)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											finish();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
					break;
				} else if (response.startsWith("{") && response.endsWith("}")) {
					initData();
					break;
				} else if (response.equals("-7")) {
					errormsg = "用户名或者密码不正确！";
				} else if (response.equals("-24")) {
					errormsg = "充值编号不存在";
				} else if (response.equals("-25")) {
					errormsg = "充值编号不存在";
				} else if (response.equals("-33")) {
					errormsg = "审核不通过必须填写理由";
				} else if (response.equals("-34")) {
					errormsg = "审核不通过必须填写理由";
				} else if (response.equals("-35")) {
					errormsg = "额度编号为空，或者格式不正确";
				} else if (response.equals("-36")) {
					errormsg = "额度编号不存在";
				} else if (response.equals("-40")) {
					errormsg = "审核是否通过为空";
				} else if (response.equals("-37")) {
					errormsg = "没有权限";
				} else if (response.equals("-38")) {
					errormsg = "您被限制了充值客户端登录，如有问题，请联系票管家！";
				} else if (response.equals("-28")) {
					errormsg = "当前状态已经无法进行此操作（代表额度已经被人审核了）";
				}{
					errormsg = "请求失败，错误编号为" + response;
				}
				// else if (response.equals("-1000")) {
				// errormsg = "请求超时，请稍后重试！";
				// } else {
				// errormsg = "请求失败，错误编号为"+response;
				// }
				if (!errormsg.equals("")) {
					MessageBox.CreateAlertDialog(errormsg,
							CheckManagerInfoActivity.this);
				}
				break;
			}
		}
	};

	/***
	 * 审核充值，专用账户，额度
	 */

	public Runnable runnableCheckAudit = new Runnable() {

		@Override
		public void run() {
			if (!clicked1) {
				clicked1 = true;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(CheckManagerInfoActivity.this)) {
				handler.sendEmptyMessage(PROGRESSSTART_HANDLER);
				PROGRESSMSG = "正在审核，请稍等...";
				String response = "";
				if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI) {
					response = RequestServerFromHttp.auditCharge(id,
							auditstatus, auditreason);//
					HandleData.handleChargeStatus(id, auditstatus, auditreason);
				} else if (activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
					response = RequestServerFromHttp.auditAccount(id,
							auditstatus, auditreason);//
					HandleData
							.handleAccountStatus(id, auditstatus, auditreason);
				} else {
					response = RequestServerFromHttp.auditCredit(id,
							auditstatus, auditreason);//
					HandleData.handleCreditStatus(id, auditstatus, auditreason);
				}
				HandleData.handlePendingCount(RequestServerFromHttp
						.pendingCount());
				Message msg = new Message();
				msg.what = RESPONSE_HANDLER;
				Bundle b = new Bundle();
				b.putString(KEY_RESPONSE, response);
				msg.setData(b);
				runnablehandler.sendMessage(msg);
				handler.sendEmptyMessage(PROGRESSEND_HANDLER);
			} else {
				runnablehandler.sendEmptyMessage(NONETWORK_HANDLER);
			}
			clicked1 = false;
		}
	};

	/**
	 * 数据逻辑处理
	 */
	@Override
	public void run() {
		if (!clicked) {
			clicked = true;
		} else {
			return;
		}
		if (NetworkCheck.IsHaveInternet(this)) {
			PROGRESSMSG = "正在获取充值记录，请稍等...";
			String response = "";
			if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI) {
				response = RequestServerFromHttp.charge(id);// 充值详情
				HandleData.handleChargeInfo(response);
			} else if (activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
				response = RequestServerFromHttp.account(id);// 专用账户详情
				HandleData.handleAccountInfo(response);
			}
			Message msg = new Message();
			msg.what = RESPONSE_HANDLER;
			Bundle b = new Bundle();
			b.putString(KEY_RESPONSE, response);
			msg.setData(b);
			runnablehandler.sendMessage(msg);
		} else {
			runnablehandler.sendEmptyMessage(NONETWORK_HANDLER);
		}
		clicked = false;
	}
}
