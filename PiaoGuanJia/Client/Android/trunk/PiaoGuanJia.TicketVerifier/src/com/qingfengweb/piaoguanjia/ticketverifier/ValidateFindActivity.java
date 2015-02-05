package com.qingfengweb.piaoguanjia.ticketverifier;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingfengweb.piaoguanjia.ticketverifier.bean.ValidateInfo;
import com.qingfengweb.piaoguanjia.ticketverifier.request.HandleData;
import com.qingfengweb.piaoguanjia.ticketverifier.request.RequestServerFromHttp;
import com.qingfengweb.piaoguanjia.ticketverifier.util.NetworkCheck;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ValidateFindActivity extends MainFrameActivity {

	public static final int ID_BUTTON = 0x7f44725;// button
	public EditText et_num;// 输入订单号，身份证号，手机号
	public TextView tv_name;
	public MediaPlayer mp ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加第一个滚动视图
		ScrollView scrollView1 = getScrollView();
		addCenterView(scrollView1);
		mp = MediaPlayer.create(this, R.raw.noticket);
		setBottomEnable(false);
		initView("订单信息验证", "查找", true);
		et_num = (EditText) lock.findViewById(R.id.et_num);
		et_num.setInputType(InputType.TYPE_CLASS_TEXT);
		tv_name = (TextView) lock.findViewById(R.id.tv_name);
		if(TicketApplication.productname!=null
				&&!TicketApplication.productname.equals("")){
			tv_name.setText(TicketApplication.productname);
		}else {
			tv_name.setText("");
		}
		
		if(tv_name.getText().toString().equals("")){
			lock.findViewById(ID_BUTTON).setEnabled(false);
			lock.findViewById(ID_BUTTON).setClickable(false);
		}else{
			lock.findViewById(ID_BUTTON).setEnabled(true);
			lock.findViewById(ID_BUTTON).setClickable(true);
		}
	}

	/**
	 * 
	 * @param 登陆的视图
	 * @return
	 */

	public ScrollView getScrollView() {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout linearLayout = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
				android.support.v4.view.ViewPager.LayoutParams.WRAP_CONTENT);
		linearLayout.setLayoutParams(params);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		View view = LayoutInflater.from(this).inflate(
				R.layout.item_verification, null);
		linearLayout.addView(view);

		Button btn = new Button(this);
		btn.setBackgroundResource(R.drawable.btn_corner_round);
		btn.setOnTouchListener(btnviewOntouch);
		params.setMargins(0,
				(int) (2 * getResources().getDimension(R.dimen.marginsizeh)),
				0, 0);
		btn.setLayoutParams(params);
		btn.setText("查找");
		btn.setTextSize(23);
		btn.setTextColor(Color.WHITE);
		btn.setId(ID_BUTTON);
		btn.setOnClickListener(this);
		linearLayout.addView(btn);
		scrollView.addView(linearLayout);
		return scrollView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ID_BUTTON) {
			if (validate()) {
				new Thread(this).start();
			}
		}
		super.onClick(v);
	}

	@Override
	public boolean validate() {
		String num = et_num.getText().toString().trim();
		if (num.equals("")) {
			CreateAlertDialog("请输入查询的编号", ValidateFindActivity.this);
			return false;
		}
		return true;
	}

	/**
	 * 页面逻辑处理
	 */
	public Handler runnablehandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case RESPONSE_HANDLER:
				Bundle b = msg.getData();
				String type = b.getString(KEY_RESPONSE);
				i.setClass(ValidateFindActivity.this, VerificationSingleActivity.class);
				i.putExtra("type", 1);//代表需要验证
				i.putExtra("ordernumber", type.split(",")[0]);
				i.putExtra("orderid", type.split(",")[1]);
				et_num.setText("");
				startActivity(i);
				break;
			case 1:
				i.putExtra("id", et_num.getText().toString());
				i.setClass(ValidateFindActivity.this, VerificationListActivity.class);
				et_num.setText("");
				startActivity(i);
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
			PROGRESSMSG = "正在查询，请稍等...";
			handler.sendEmptyMessage(PROGRESSSTART_HANDLER);// 开启进度条
			String num = et_num.getText().toString().trim();
			String response = RequestServerFromHttp.orderNumber(num);// 查询
			String type = HandleData.handleOrderNumber(response,0);
			Message msg = new Message();
			msg.what = RESPONSE_HANDLER;
			Bundle b = new Bundle();
			b.putString(KEY_RESPONSE, response);
			msg.setData(b);
			if ((!type.equals("-1"))&&(!type.equals("0"))) {
				msg.what = RESPONSE_HANDLER;
				b.putString(KEY_RESPONSE, type);
				msg.setData(b);
				runnablehandler.sendMessage(msg);
			}else if (type.equals("0")) {
				runnablehandler.sendEmptyMessage(1);
			} else if (response.equals("-12")) {
				mp.start();
				handler.sendMessage(msg);
			} else if (response.equals("-9")) {
				mp.start();
				handler.sendMessage(msg);
			}  else{
				handler.sendMessage(msg);
			}
			handler.sendEmptyMessage(PROGRESSEND_HANDLER);
		} else {
			handler.sendEmptyMessage(NONETWORK_HANDLER);
		}
		clicked = false;
	}
	
	/***
	 * 处理查询的东西
	 * 
	 */
	
	/**
	 * 验证列表
	 */

	public static int handlerData(String response) {
		if (!response.startsWith("["))
			return -1;
		Type listType = new TypeToken<LinkedList<ValidateInfo>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<ValidateInfo> beans = null;
		ValidateInfo bean = null;
		try {
			beans = gson.fromJson(response, listType);
		} catch (Exception e) {
		}
		if (beans == null) {
			return -1;
		}
		List<Map<String, Object>> selectresult = null;
		if (beans != null && beans.size() == 1) {
			return 0;
		}else{
			return 1;
		}
	}

}
