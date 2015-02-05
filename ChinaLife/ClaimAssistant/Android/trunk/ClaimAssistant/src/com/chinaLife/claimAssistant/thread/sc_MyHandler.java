package com.chinaLife.claimAssistant.thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chinaLife.Interface.sc_ProgressBarInterface;
import com.chinaLife.claimAssistant.sc_CaseHandlerFlowActivity;
import com.chinaLife.claimAssistant.sc_CaseListActivity;
import com.chinaLife.claimAssistant.sc_CaseManageActivity;
import com.chinaLife.claimAssistant.sc_CaseOfOnlyOneActivity;
import com.chinaLife.claimAssistant.sc_ClaimRevokeActivity;
import com.chinaLife.claimAssistant.sc_ConfirmServiceActivity;
import com.chinaLife.claimAssistant.sc_HelpActivity;
import com.chinaLife.claimAssistant.sc_MainActivity;
import com.chinaLife.claimAssistant.sc_MessageListActivity;
import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.sc_Splash;
import com.chinaLife.claimAssistant.bean.sc_AppInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.google.gson.Gson;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import com.sqlcrypt.database.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class sc_MyHandler extends Handler {

	private static sc_MyHandler mInstance = null;
	private static Thread mythread = null;
	public AlertDialog.Builder callDailog = null;
	private ProgressDialog	progressdialog = null;
	public static String mymsg = "";

	public sc_MyHandler() {
	}

	public static sc_MyHandler getInstance() {
		if (mInstance == null) {
			mInstance = new sc_MyHandler();
		}
		return mInstance;
	}

	@Override
	public void handleMessage(Message msg) {
		try {
			switch (msg.what) {
			case 1:
				sc_Splash splasha = (sc_Splash) sc_MyApplication.getInstance()
						.getContext();
				splasha.checkVersion();
				break;
			case 0:// 成功
				Bundle data = msg.getData();
				if (data.getInt("type") == 1) {// 进入加载页面时获取版本信息成功
//					Splash a = (Splash) MyApplication.getInstance()
//							.getContext();
					parseJson();
				} else if (data.getInt("type") == 2) {// 获取案件列表成功
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_CaseManageActivity a = (sc_CaseManageActivity) sc_MyApplication
							.getInstance().getContext();
					a.parseJson(1,data.getString("response"));
				} else if (data.getInt("type") == 3) {// 赔案撤销操作成功
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_ClaimRevokeActivity a = (sc_ClaimRevokeActivity) sc_MyApplication
							.getInstance().getContext();
					a.parseJson(1);
				} else if (data.getInt("type") == 4) {// 确认服务方式成功
					sc_ConfirmServiceActivity a = (sc_ConfirmServiceActivity) sc_MyApplication
							.getInstance().getContext();
					a.parseJson(1, data.getString("response"));
				} else if (data.getInt("type") == 5) {// 自助查勘获取自助查勘图例信息
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_CaseOfOnlyOneActivity a = (sc_CaseOfOnlyOneActivity) sc_MyApplication
							.getInstance().getContext();
				} else if (data.getInt("type") == 6) {// 自助理赔获取自助查勘图例信息
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_CaseHandlerFlowActivity a = (sc_CaseHandlerFlowActivity) sc_MyApplication
							.getInstance().getContext();
					a.notifyView();
				} else if (data.getInt("type") == 7) {// 获取理赔状态---------
				} else if (data.getInt("type") == 8) {// 自助查勘获取理赔照片信息列表 ，不合格的
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_CaseOfOnlyOneActivity a = (sc_CaseOfOnlyOneActivity) sc_MyApplication
							.getInstance().getContext();
					a.parseJsonClaimImage(1, data.getString("response"));
				} else if (data.getInt("type") == 9) {// 上传图片成功
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_MyApplication.getInstance().setUploadon(false);
					try {
						showUpdataDialog("尊敬的客户：您已成功上传照片，请耐心等待我司审核。", true);
					} catch (final Exception e) {
						e.printStackTrace();
					}

				} else if (data.getInt("type") == 99) {
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_MyApplication.getInstance().setUploadon(false);
					String dialogmsg = "尊敬的客户：您已成功上传所缺单证，请耐心等待我司审核。";
					if (sc_MyApplication.getInstance().getStepstate() == 2) {
						if ((!sc_MyApplication.getInstance().isagree)
								&& (sc_MyApplication.getInstance()
										.getClaimidstate() & 32) == 32) {
							dialogmsg = "尊敬的客户：您已成功上传单证照片，但对我司核定赔款存在异议，请耐心等待我司复核。";
						} else if (sc_MyApplication.getInstance().isagree
								&& (sc_MyApplication.getInstance()
										.getClaimidstate() & 32) == 32) {
							dialogmsg = "尊敬的客户：您已同意我司核定赔款，且成功上传单证照片，请耐心等待我司审核。";
						}
					}
					new AlertDialog.Builder(sc_MyApplication.getInstance()
							.getContext())
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle("提示：")
							.setMessage(dialogmsg)
							.setCancelable(false)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											sc_MyApplication.getInstance()
													.getProgressdialog()
													.dismiss();
											sc_MyApplication.getInstance()
													.setUploadon(false);
											Intent i = new Intent(
													sc_MyApplication.getInstance()
															.getContext(),
													sc_CaseListActivity.class);
											sc_MyApplication.getInstance()
													.getContext()
													.startActivity(i);
											sc_MyApplication.getInstance()
													.getContext().finish();
										}
									}).show();
				} else if (data.getInt("type") == 11) {// 获取消息通知列表
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_MessageListActivity a = (sc_MessageListActivity) sc_MyApplication
							.getInstance().getContext();
					a.parseJson();
				} else if (data.getInt("type") == 12) {// 获取帮助列表成功
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_HelpActivity a = (sc_HelpActivity) sc_MyApplication.getInstance()
							.getContext();
					a.parseJson();
				} else if (data.getInt("type") == 13) {// 自助查勘中获取图例
//					CaseOfOnlyOneActivity a = (CaseOfOnlyOneActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJosnLegendids(1, data.getString("response"));
				} else if (data.getInt("type") == 14) {// 获取不合格和合格的图片
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJsonClaimImage(1, data.getString("response"));
				} else if (data.getInt("type") == 15) {// 获取理赔模式
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseMode(1, data.getString("response"));
				} else if (data.getInt("type") == 16) {// 获取赔款价格
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJosnPrice(1, data.getString("response"));
				} else if (data.getInt("type") == 17) {// 获取案件列表成功
					try {
						sc_CaseListActivity a = (sc_CaseListActivity) sc_MyApplication
								.getInstance().getContext();
						a.parseJson(1, data.getString("response"));
					} catch (Exception e) {

					}

				} else if (data.getInt("type") == 18) {// 确定银行账号和价格
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJosnBank(1, data.getString("response"));
				} else if (data.getInt("type") == 19) {// 获取拍照图片图例
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJosnLegendids(1, data.getString("response"));
				} else if (data.getInt("type") == 20) {// 获取单证图片图例
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJosnCertificates(1, data.getString("response"));
				} else if (data.getInt("type") == 21) {// 获取所有提醒信息
				} else if (data.getInt("type") == 22) {// 更新信件状态，未读和已读
				} else if (data.getInt("type") == 23) {// 删除信件
				} else if (data.getInt("type") == 24) {// 添加提醒
				} else if (data.getInt("type") == 25) {// 向服务器搜索服务网点，医疗机构和银行
				} else if (data.getInt("type") == 26) {// 向服务器获取收件人列表
				} else if (data.getInt("type") == 27) {// 向服务器获取所有文章类别
				} else if (data.getInt("type") == 28) {// 向服务器获取该类别的文章
				} else if (data.getInt("type") == 29) {// 删除客户
				} else if (data.getInt("type") == 30) {// 删除提醒
				} else if (data.getInt("type") == 31) {// 删除活动
				} else if (data.getInt("type") == 32) {// 常见问题
				} else if (data.getInt("type") == 33) {// 关联现有客户
				} else if (data.getInt("type") == 34) {// 关联新增客户
				} else if (data.getInt("type") == 35) {// 获取服务器对活动的增删改
				} else if (data.getInt("type") == 100) {// 获取服务器对活动的增删改
					sc_CaseHandlerFlowActivity c = (sc_CaseHandlerFlowActivity) sc_MyApplication
							.getInstance().getContext();
					c.operate_tag = true;
				}
				break;
			case -1:// 与服务器交互失败
				Bundle data1 = msg.getData();
				// if (MyApplication.getInstance().getProgressdialog() != null
				// && MyApplication.getInstance().getProgressdialog()
				// .isShowing()) {
				// MyApplication.getInstance().getProgressdialog().dismiss();
				// }
				if (data1.getInt("type") == 1) {// 进入加载页面时获取版本信息失败
					sc_MyApplication.ERROR_VALUE = 0L;
					/*
					 * SharedPreferences sharedata =
					 * MyApplication.getInstance().
					 * getContext().getSharedPreferences("data", 0);
					 * if(sharedata!=null){
					 */
					/*
					 * String data2 = sharedata.getString("item", null);
					 * if(data2!=null && "hello".equals(data2)){
					 */
//					Splash a = (Splash) MyApplication.getInstance()
//							.getContext();
//					Intent i = new Intent(a, MainActivity.class);
//					a.startActivity(i);
//					a.finish();
					/*
					 * }else{ Editor sharedata1 =
					 * MyApplication.getInstance().getContext
					 * ().getSharedPreferences("data", 0).edit();
					 * sharedata1.putString("item","hello");
					 * sharedata1.commit(); Splash a = (Splash)
					 * MyApplication.getInstance().getContext(); Intent i = new
					 * Intent(a, ExampleActivity.class); a.startActivity(i);
					 * a.finish();
					 * 
					 * }
					 */
					/*
					 * }else{ Editor sharedata1 =
					 * MyApplication.getInstance().getContext
					 * ().getSharedPreferences("data", 0).edit();
					 * sharedata1.putString("item","hello");
					 * sharedata1.commit(); >>>>>>> .r5061 Splash a = (Splash)
					 * MyApplication.getInstance().getContext(); Intent i = new
					 * Intent(a, ExampleActivity.class); a.startActivity(i);
					 * a.finish(); <<<<<<< .mine // } }
					 */
				} else if (data1.getInt("type") == 2) {// 获取案件列表失败
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_CaseManageActivity a = (sc_CaseManageActivity) sc_MyApplication
							.getInstance().getContext();
					a.parseJson(2,data1.getString("servermsg"));

				} else if (data1.getInt("type") == 3) {//
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_ClaimRevokeActivity a = (sc_ClaimRevokeActivity) sc_MyApplication
							.getInstance().getContext();
					a.parseJson(2);
				} else if (data1.getInt("type") == 4) {
					sc_ConfirmServiceActivity a = (sc_ConfirmServiceActivity) sc_MyApplication
							.getInstance().getContext();
					a.parseJson(2, "");
				} else if (data1.getInt("type") == 5) {
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
				} else if (data1.getInt("type") == 6) {
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
				} else if (data1.getInt("type") == 7) {
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
				} else if (data1.getInt("type") == 8) {
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_CaseOfOnlyOneActivity a = (sc_CaseOfOnlyOneActivity) sc_MyApplication
							.getInstance().getContext();
					a.parseJsonClaimImage(2, "");
				} else if (data1.getInt("type") == 9) {
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_MyApplication.getInstance().setUploadon(false);
					try {
						showUpdataDialog("很抱歉，您的现场照片上传失败，请你确定网络畅通后，尝试重新提交",
								false);
					} catch (final Exception e) {
						e.printStackTrace();
					}

				} else if (data1.getInt("type") == 99) {
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
					sc_MyApplication.getInstance().setUploadon(false);
					try {
						showUpdataDialog("很抱歉，您的单证上传失败，请你确定网络畅通后，尝试重新提交", false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (data1.getInt("type") == 10) {// 搜索所有客户
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
				} else if (data1.getInt("type") == 11) {// 获取消息通知失败
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
				} else if (data1.getInt("type") == 12) {// 获取帮助失败
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
				} else if (data1.getInt("type") == 13) {// 获取拍照图片图例失败
//					MyApplication.getInstance().getProgressdialog().dismiss();
//					CaseOfOnlyOneActivity a = (CaseOfOnlyOneActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJosnLegendids(2, "");
				} else if (data1.getInt("type") == 14) {// 获取不合格和合格的图片失败
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJsonClaimImage(1, "");
				} else if (data1.getInt("type") == 15) {// 获取理赔模式失败
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseMode(2, "");
				} else if (data1.getInt("type") == 16) {// 获取赔款价格失败
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJosnPrice(2, "");
				} else if (data1.getInt("type") == 17) {// 获取案件列表成功失败
					sc_CaseListActivity a = (sc_CaseListActivity) sc_MyApplication
							.getInstance().getContext();
					a.parseJson(2, "");
				} else if (data1.getInt("type") == 18) {// 确定银行账号和价格失败
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJosnBank(2, "");
				} else if (data1.getInt("type") == 19) {// 获取拍照图片图例失败
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJosnLegendids(2, "");
				} else if (data1.getInt("type") == 20) {// 获取单证图片图例失败
//					CaseHandlerFlowActivity a = (CaseHandlerFlowActivity) MyApplication
//							.getInstance().getContext();
//					a.parseJosnCertificates(2, "");
				} else if (data1.getInt("type") == 28) {// 向服务器获取该类别的文章
				} else if (data1.getInt("type") == 29) {// 删除客户
				} else if (data1.getInt("type") == 30) {// 删除提醒
				} else if (data1.getInt("type") == 31) {// 删除活动
				} else if (data1.getInt("type") == 32) {// 常见问题
				} else if (data1.getInt("type") == 33) {// 关联现有客户
				} else if (data1.getInt("type") == 34) {// 关联新增客户
				} else if (data1.getInt("type") == 35) {// 获取服务器对活动的增删改
				} else if (data1.getInt("type") == 100) {// 获取服务器对活动的增删改
					sc_CaseHandlerFlowActivity c = (sc_CaseHandlerFlowActivity) sc_MyApplication
							.getInstance().getContext();
					c.operate_tag = false;
				}
				// if(data1.getString("servermsg").equals("-1000")){
				// mymsg = "获取数据失败";
				// }else if(data1.getString("servermsg").equals("-404")){
				// mymsg = "找不到网址";
				// }else{
				// mymsg = "获取数据失败";
				// }
				// Toast.makeText(MyApplication
				// .getInstance().getContext(), mymsg,
				// Toast.LENGTH_LONG).show();
				break;
			case -2:
				mythread = new Thread(runable2);
				mythread.start();
				// new Thread(runable2).start();
				// System.out.println("上传任务已经开始");
				// 处理拍照时处理文件上传线程任务
				break;
			case -3:
				try {
					Bundle data2 = msg.getData();
					sc_ProgressBarInterface a = (sc_ProgressBarInterface) sc_MyApplication
							.getInstance().getContext();
					a.startProgressBar(data2.getString("legendid"),
							data2.getInt("max"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case -4:
				try {
					Bundle data3 = msg.getData();
					sc_ProgressBarInterface a1 = (sc_ProgressBarInterface) sc_MyApplication
							.getInstance().getContext();
					a1.updateProgressBar(data3.getString("legendid"),
							data3.getInt("position"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case -5:
				try {
					Bundle data4 = msg.getData();
					sc_ProgressBarInterface a2 = (sc_ProgressBarInterface) sc_MyApplication
							.getInstance().getContext();
					a2.closeProgressBar(data4.getString("legendid"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case -6:
				new Thread(runable1).start();
				break;
			case -7:

				break;
			case -8:// 显示dialog
				if (sc_MyApplication.getInstance().getProgressdialog() != null
						&& !sc_MyApplication.getInstance().getProgressdialog()
								.isShowing())
					sc_MyApplication.getInstance().getProgressdialog().show();
				break;
			case -9:// 关闭dialog
				if (sc_MyApplication.getInstance().getProgressdialog() != null
						&& sc_MyApplication.getInstance().getProgressdialog()
								.isShowing())
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
				callDailog = new AlertDialog.Builder(sc_MyApplication
						.getInstance().getContext());
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示").setMessage("与服务器失去连接")
						.setPositiveButton("确定", null);
				if (callDailog != null) {
					callDailog.show();
				}
				sc_MyApplication.switch_tag = false;
				break;
			case -14:// 关闭dialog
				if (sc_MyApplication.getInstance().getProgressdialog() != null
						&& sc_MyApplication.getInstance().getProgressdialog()
								.isShowing())
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
				callDailog = new AlertDialog.Builder(sc_MyApplication
						.getInstance().getContext());
				callDailog
						.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示")
						.setMessage("由于网络原因，您的上传已暂停，请点击继续上传按钮完成上传。")
						.setPositiveButton("继续上传",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										progressdialog = sc_MyApplication.getInstance().getProgressdialog();
										if(progressdialog==null){
											progressdialog = new ProgressDialog(sc_MyApplication.getInstance().getContext());
											progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
											progressdialog.setCanceledOnTouchOutside(false);
											progressdialog.setMessage("正在上传照片，请稍等");
											progressdialog.setCancelable(false);
											sc_MyApplication.getInstance().setProgressdialog(progressdialog);
										}else{
											progressdialog.setMessage("正在上传照片，请稍等");
										}
										progressdialog.show();
										mythread = new Thread(runable2);
										mythread.start();
									}
								}).setNegativeButton("取消", null);
				if (callDailog != null) {
					callDailog.show();
				}
				sc_MyApplication.switch_tag = false;
				break;
			case -10:// 刷新主界面
				sc_MainActivity a = (sc_MainActivity) sc_MyApplication.getInstance()
						.getContext();
				a.initViewFun();
				break;
			case -11:// 关闭dialog
				if (sc_MyApplication.getInstance().getProgressdialog() != null
						&& sc_MyApplication.getInstance().getProgressdialog()
								.isShowing())
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
				break;
			case -12://
				// if (MyApplication.getInstance().getProgressdialog() != null
				// && MyApplication.getInstance().getProgressdialog()
				// .isShowing())
				// MyApplication.getInstance().getProgressdialog().dismiss();
				// Toast.makeText(MyApplication
				// .getInstance().getContext(), "网络中断，请检查网络是否连接",
				// Toast.LENGTH_LONG).show();
				sc_MyApplication.switch_tag = false;
				break;
			case -13://
				sc_MyApplication.getInstance().getProgressdialog().dismiss();
				sc_MyApplication.getInstance().setUploadon(false);
				String dialogmsg = "尊敬的客户：您对我司核定赔款存在异议，请耐心等待我司复核。";
				new AlertDialog.Builder(sc_MyApplication.getInstance()
						.getContext())
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示：")
						.setMessage(dialogmsg)
						.setCancelable(false)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										sc_MyApplication.getInstance()
												.getProgressdialog().dismiss();
										sc_MyApplication.getInstance()
												.setUploadon(false);
										Intent i = new Intent(sc_MyApplication
												.getInstance().getContext(),
												sc_CaseListActivity.class);
										sc_MyApplication.getInstance()
												.getContext().startActivity(i);
										sc_MyApplication.getInstance()
												.getContext().finish();
									}
								}).show();
				break;
			case -16:
				Bundle b = msg.getData();
				new AlertDialog.Builder(sc_MyApplication.getInstance()
						.getContext())
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示：").setMessage(b.getString("msg"))
						.setCancelable(false).setPositiveButton("知道了", null)
						.show();
				break;
			case 100:

				break;
			case -101:
				if (sc_MyApplication.getInstance().getProgressdialog() != null
				&& sc_MyApplication.getInstance().getProgressdialog()
						.isShowing())
					sc_MyApplication.getInstance().getProgressdialog().dismiss();
				try {
					sc_ProgressBarInterface a1 = (sc_ProgressBarInterface) sc_MyApplication
							.getInstance().getContext();
					a1.showInputDialog(sc_MyApplication.getInstance().getPassword());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Runnable runable1 = new Runnable() {

		@Override
		public void run() {
			sc_ThreadDemo.startMyThread();
		}
	};

	/**
	 * 提示用户上传图片是否成功
	 * 
	 * @param str
	 *            提示的文字
	 * @param type
	 *            提示的类型 true代表上传成功的提示 false代表上传失败的提示
	 */
	public void showUpdataDialog(String str, final boolean upload) {
		AlertDialog.Builder ab = new AlertDialog.Builder(sc_MyApplication
				.getInstance().getContext());
		/*
		 * Dialog dialog = new Dialog(MyApplication.getInstance().getContext());
		 * dialog.setTitle("提示："); LayoutInflater mLayoutInflater =
		 * (LayoutInflater)
		 * MyApplication.getInstance().getContext().getSystemService
		 * (Context.LAYOUT_INFLATER_SERVICE); View reNameView =
		 * mLayoutInflater.inflate(R.layout.dialog_image, null); LayoutParams
		 * params = new
		 * LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		 * dialog.addContentView(reNameView, params); dialog.show(); // dialog.s
		 */
		ab.setCancelable(false);
		ab.setIcon(android.R.drawable.ic_dialog_alert);
		ab.setTitle("提示：");
		ab.setMessage(str);
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!upload) {// 上传失败
					dialog.cancel();
					return;
				} else if (upload) {
					dialog.cancel();
					Intent i = new Intent(sc_MyApplication.getInstance()
							.getContext(), sc_CaseListActivity.class);
					sc_MyApplication.getInstance().getContext().startActivity(i);
					sc_MyApplication.getInstance().getContext().finish();
				}
			}
		}).show();

	}

	private Runnable runable2 = new Runnable() {

		@Override
		public void run() {
			sc_UploadPhotoFile.startMyThread();
		}
	};
	
	/**
	 * 显示是否要下载新版本的对话框
	 */
	public void showDownloadDialog(final int type, final String url,
			final ContentValues values) {
		try{
			if (type == 1) {
				sc_DBHelper.getInstance().execSql("delete from appinfo");

			}
			sc_DBHelper.getInstance().insert("appinfo", values);
			new AlertDialog.Builder(sc_MyApplication.getInstance().getContext())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("提示")
					.setMessage("有新版本是否立即更新？")
					.setCancelable(false)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							Uri content_url =null;
							try {
								content_url = Uri.parse(url);
								intent.setData(content_url);
								sc_MyApplication.getInstance().getContext().startActivity(intent);
							} catch (Exception e) {
								content_url = Uri.parse("http://"+url);
								intent.setData(content_url);
								sc_MyApplication.getInstance().getContext().startActivity(intent);
							}
							sc_MyApplication.getInstance().getContext().finish();
						}
					})
					.setNegativeButton("取消", null
//							new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							/*if(!boo){
//								Intent intent = new Intent(Splash.this,
//										ExampleActivity.class);
//								startActivity(intent);
//								Splash.this.finish();
//							}else{*/
//								Intent intent = new Intent(Splash.this,
//										MainActivity.class);
//								startActivity(intent);
//								Splash.this.finish();
////							}
//							
//						}
//					}
					)
					.show();
		}catch(Exception e){
			
		}
		
	}

	/**
	 * 处理获取的json数据
	 */

	public void parseJson() {
		List<Map<String, Object>> selectresult = sc_DBHelper.getInstance().selectRow(
				"select * from appinfo", null);
		Gson gson = new Gson();// 创建Gson对象，
		ContentValues values = new ContentValues();
		sc_AppInfo appinfo = null;
		try {
			appinfo = gson.fromJson(sc_MyApplication.getInstance()
					.getResponseword(), sc_AppInfo.class);
		} catch (Exception e) {
			
			/*if(!boo){
				Intent intent = new Intent(Splash.this,
						ExampleActivity.class);
				startActivity(intent);
				Splash.this.finish();
			}else{*/
//				Intent intent = new Intent(Splash.this,
//						MainActivity.class);
//				startActivity(intent);
//				Splash.this.finish();
//			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		long l1 = 0;
		long l2 = 0;
		long error_value = 0;
		try {
			d = sdf.parse(appinfo.getServertime());
			l1 = d.getTime();
			java.util.Date d2 = new Date();
			l2 = d2.getTime();
			error_value = l1-l2;
		} catch (final ParseException e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "解析服务器时间出现错误："+e.getMessage());
				}
			}).start();
		}
		sc_MyApplication.ERROR_VALUE = error_value;
		values.put("name", appinfo.getName());
		values.put("version", appinfo.getVersion());
		values.put("url", appinfo.getUrl());
		values.put("error_value", error_value);
		values.put("update_time", appinfo.getUpdatetime());
		values.put("isupdate", appinfo.getWetherupdate());
		sc_MyApplication.isqiangzhi = appinfo.getWetherupdate();
		String appversion1 = sc_PhoneInfo.getAppVersionName(sc_MyApplication.getInstance().getContext());
		String appversion2 = appinfo.getVersion();
		//System.out.println("是否强制更新"+MyApplication.isqiangzhi+"现在程序的版本为："+appversion1);
		if (selectresult.size() > 0) {
			if (appversion1.compareTo(appversion2)<0) {
				sc_MyApplication.hasnewverson = true;
				showDownloadDialog(1, appinfo.getUrl(), values);
				return;
			}
		} else {
			if (appversion1.compareTo(appversion2)<0) {
				sc_MyApplication.hasnewverson = true;
				showDownloadDialog(2, appinfo.getUrl(), values);
				return;
			}
		}
	/*	if(!boo){
			Intent intent = new Intent(Splash.this,
					ExampleActivity.class);
			startActivity(intent);
			Splash.this.finish();
		}else{*/
//			Intent intent = new Intent(Splash.this,
//					MainActivity.class);
//			startActivity(intent);
//			Splash.this.finish();
//		}
	}

}
