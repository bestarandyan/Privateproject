/**
 * 
 */
package com.qingfengweb.lottery.payment;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.qingfengweb.lottery.activity.ChargeMoneyActivity;

/**
 * @author 刘星星
 * @createDate 2013/12/25
 * 支付宝支付
 *
 */
@SuppressLint("SimpleDateFormat")
public class PaymentMain {
	public static final String TAG = "alipay-sdk";
	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;
	public  Activity context;
	public  Handler mHandler;
	@SuppressWarnings("static-access")
	public PaymentMain(Activity context,Handler mHandler) {
		this.context = context;
		this.mHandler = mHandler;
	}
	
	@SuppressWarnings("deprecation")
	public void payMoney(String subject,String body , String price,String tradeNo){
		try {
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo(subject,body,price,tradeNo);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i("ExternalPartner", "start pay");
			// start the pay.
			Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(context, mHandler);
					//设置为沙箱模式，不设置默认为线上环境
					//alipay.setSandBox(true);
					String result = alipay.pay(orderInfo);
					Log.i(TAG, "result = " + result);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();
//			resultStatus={4000};memo={};result={success="false"}
		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(context, "Failure calling remote service",Toast.LENGTH_SHORT).show();
		}
	}
	@SuppressWarnings("deprecation")
	public  String getNewOrderInfo(String subject,String body , String price,String tradeNo) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(tradeNo);
		sb.append("\"&subject=\"");
		sb.append(subject);
		sb.append("\"&body=\"");
		sb.append(body);
		sb.append("\"&total_fee=\"");
		sb.append(price);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	public  String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		Log.d(TAG, "outTradeNo: " + key);
		return key;
	}

	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
