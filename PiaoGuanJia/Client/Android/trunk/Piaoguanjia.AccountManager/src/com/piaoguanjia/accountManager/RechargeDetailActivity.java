package com.piaoguanjia.accountManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.piaoguanjia.accountManager.database.DBHelper;
import com.piaoguanjia.accountManager.database.TableCreate;
import com.piaoguanjia.accountManager.request.HandleData;
import com.piaoguanjia.accountManager.request.RequestServerFromHttp;
import com.piaoguanjia.accountManager.util.CommonUtils;
import com.piaoguanjia.accountManager.util.ImageDownloaderId;
import com.piaoguanjia.accountManager.util.MD5;
import com.piaoguanjia.accountManager.util.MessageBox;
import com.piaoguanjia.accountManager.util.NetworkCheck;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

public class RechargeDetailActivity extends MainFrameActivity {

	public int usertype = 1;// 1 总帐户，2专用账户
	public int rechargetype = 1;// 有凭证，2无凭证

	public static final int ID_IMAGEVIEW = 0x7f44763;// 凭证图片id
	public static final int ID_SCROLLVIEW1 = 0x7f44764;// 滚动视图1
	public static final int ID_SCROLLVIEW2 = 0x7f44765;// 滚动视图2
	private String chargid = "";// 充值id

	private ArrayList<TextView> views = new ArrayList<TextView>();// 装载Textview集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加第一个滚动视图
		ScrollView scrollView1 = getListScrollView();
		scrollView1.setId(ID_SCROLLVIEW1);
		addCenterView(scrollView1);
		rl1.setBackgroundColor(Color.parseColor("#3ABFDE"));
		// 添加第二个滚动视图
		ScrollView scrollView2 = getScrollView();
		scrollView2.setId(ID_SCROLLVIEW2);
		addCenterView(scrollView2);

		setBottomEnable(false);
		scrollView2.setVisibility(View.GONE);
		initData();
		new Thread(this).start();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		List<Map<String, Object>> selectresult = null;
		chargid = getIntent().getStringExtra("id");
		selectresult = DBHelper.getInstance().selectRow(
				"select * from " + TableCreate.TABLENAME_CHARGEINFO
						+ " where chargid = '" + chargid + "'", null);
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
//			((View) views.get(11).getTag()).setVisibility(View.GONE);
			// 充值产品（专用账户所有）
			views.get(5).setVisibility(View.VISIBLE);
			if (selectresult.get(0).get("warrantor") != null) {
				views.get(11).setText(
						selectresult.get(0).get("warrantor").toString());
			}
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
			initView("充值详情", "充值详情", "充值凭证", "", "", true);
			// 有充值凭证
			((View) views.get(11).getTag()).setVisibility(View.GONE);
			ImageView imageview = (ImageView) findViewById(ID_IMAGEVIEW);
			Bitmap bm = null;
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/piaoguanjia/image");
			if (!file.exists()) {
				file.mkdirs();
			}
			File imagefile = new File(file, MD5.getMD5(chargid) + ".png");
			bm = CommonUtils.getDrawable(imagefile.getAbsolutePath(), null);
			if (bm != null) {
				imageview.setImageBitmap(bm);
				imageview.setTag(imagefile.getAbsolutePath());
			} else {
				ImageDownloaderId imagedownloaderid = new ImageDownloaderId(
						this, 10);
				imagedownloaderid.download(imagefile, chargid, imageview);
			}
		} else {
			initView("充值详情", "", "", "", "", true);
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
				views.get(3)
						.setText(selectresult.get(0).get("type").toString());
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
	 * 
	 * @param 详情列表
	 * @return
	 */

	public ScrollView getListScrollView() {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout linearLayout = new LinearLayout(this);
		LinearLayout.LayoutParams prarms = new LinearLayout.LayoutParams(
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
				android.support.v4.view.ViewPager.LayoutParams.WRAP_CONTENT);
		linearLayout.setLayoutParams(prarms);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		String[] names = { "编号", "来源", "用户名", "充值方式", "充值账户", "充值产品", "充值时间",
				"充值金额", "累计金额", "账户余额", "资金来源", "授权管理员", "申请发票", "备注" };
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
		imageView
				.setImageBitmap(CommonUtils.getDrawable(
						Environment.getExternalStorageDirectory() + "/1.jpg",
						imageView));
		imageView.setId(ID_IMAGEVIEW);
		imageView.setOnClickListener(this);
		linearLayout.addView(imageView);
		scrollView.addView(linearLayout);
		return scrollView;
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
				if (response.startsWith("{") && response.endsWith("}")) {
					initData();
					break;
				} else if (response.equals("-7")) {
					errormsg = "用户名或者密码不正确！";
				} else if (response.equals("-37")) {
					errormsg = "没有权限";
				} else if (response.equals("-38")) {
					errormsg = "您被限制了充值客户端登录，如有问题，请联系票管家！";
				}
				// else if (response.equals("-1000")) {
				// errormsg = "请求超时，请稍后重试！";
				// } else {
				// errormsg = "请求失败，错误编号为"+response;
				// }
				if (!errormsg.equals("")) {
					MessageBox.CreateAlertDialog(errormsg,
							RechargeDetailActivity.this);
				}
				break;
			}
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
			String response = RequestServerFromHttp.charge(chargid);// 充值详情
			HandleData.handleChargeInfo(response);
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
	
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rl1) {
			findViewById(ID_SCROLLVIEW1).setVisibility(View.VISIBLE);
			findViewById(ID_SCROLLVIEW2).setVisibility(View.GONE);
		} else if (v.getId() == R.id.rl2) {
			findViewById(ID_SCROLLVIEW1).setVisibility(View.GONE);
			findViewById(ID_SCROLLVIEW2).setVisibility(View.VISIBLE);
		} else if(v.getId() == ID_IMAGEVIEW){
			if(v.getTag()!=null){
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
}
