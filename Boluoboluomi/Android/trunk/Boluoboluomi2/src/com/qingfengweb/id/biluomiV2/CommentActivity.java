package com.qingfengweb.id.biluomiV2;

import java.io.File;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.network.NetworkCheck;
import com.qingfengweb.network.UploadData;
import com.qingfengweb.util.MessageBox;

public class CommentActivity extends BaseActivity implements
		OnRatingBarChangeListener {
	private Button backBtn;
	private Button affirmSubmitBtn;// 确认提交按钮
	private EditText et;// 备注
	private ImageView image;
	private TextView gonghao, name, dengji, fengge, xuanyan;
	public int position = 0;// 标记当前被点评者为化妆师还是摄影师 1代表摄影师 2代表化妆师
	private ProgressDialog progressdialog;
	private RatingBar ratingBar1, ratingBar2, ratingBar3;
	private TextView text1,text2,text3;//用来描述被点评者的评分对象
	private int value1 = 0;// 评分1
	private int value2 = 0;// 评分2
	private int value3 = 0;// 评分3
	private String staffid = "";// 摄影师或化妆师id

	private TextView title;
	
//	private UploadData uploaddata = null;
	private boolean runnable_tag = true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_commentactivity);
		findView();
		initData();
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		HashMap<String, Object> map = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
		staffid = map.get("id").toString();
		gonghao.setText(map.get("employeeid").toString());
		name.setText(map.get("name").toString());
		dengji.setText(map.get("level").toString());
		fengge.setText(map.get("style").toString());
		xuanyan.setText(map.get("declaration").toString());
		position = Integer.parseInt(map.get("position").toString());
		if(position==1){
			title.setText("摄影师");
			text1.setText("拍摄手法");
			text2.setText("拍摄风格");
		}else{
			title.setText("化妆师");
			text1.setText("妆面妆容");
			text2.setText("服装造型");
		}
		
		
		String thumb = map.get("photoid").toString();
		if(thumb==null || thumb.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
			image.setImageResource(R.drawable.photolist_defimg);
		}else{//如果id存在
			int width = MyApplication.getInstant().getWidthPixels() / 3;
			int height = width;
			String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.VALUATION_IMG_URL+thumb+".png";
			if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
				image.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
			}else{//如果不存在 则去服务器下载
				image.setImageResource(R.drawable.photolist_defimg);
				RequestServerFromHttp.downImage(this,image,thumb,ImageType.valuationPersons.getValue(),ImgDownType.ThumbBitmap.getValue(),
						 width+"",height+"",false,ConstantsValues.VALUATION_IMG_URL,R.drawable.photolist_defimg);
			}
		}
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		affirmSubmitBtn = (Button) findViewById(R.id.affirmSubmitBtn);
		et = (EditText) findViewById(R.id.et);
		affirmSubmitBtn.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		image = (ImageView) findViewById(R.id.image);
		gonghao = (TextView) findViewById(R.id.gonghao);
		name = (TextView) findViewById(R.id.name);
		dengji = (TextView) findViewById(R.id.dengji);
		fengge = (TextView) findViewById(R.id.fengge);
		xuanyan = (TextView) findViewById(R.id.xuanyan);
		ratingBar1 = (RatingBar) findViewById(R.id.room_ratingbar1);
		ratingBar2 = (RatingBar) findViewById(R.id.room_ratingbar2);
		ratingBar3 = (RatingBar) findViewById(R.id.room_ratingbar3);
		ratingBar1.setOnRatingBarChangeListener(this);
		ratingBar2.setOnRatingBarChangeListener(this);
		ratingBar3.setOnRatingBarChangeListener(this);
		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		text3 = (TextView) findViewById(R.id.text3);
		
	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			Intent intent = new Intent(this,CommentListActivity.class);
			intent.putExtra("position", position);
			startActivity(intent);
			finish();
		} else if (v == affirmSubmitBtn) {
			if (textValidate()) {
				handler.sendEmptyMessage(5);
				new Thread(submitRunnable).start();
			}
		}
		super.onClick(v);
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		Intent intent = new Intent(this,CommentListActivity.class);
			intent.putExtra("position", position);
			startActivity(intent);
			finish();
    	}
    	return super.onKeyDown(keyCode, event);
    }
	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		if (ratingBar == ratingBar1) {
			value1 = (int) rating;
		} else if (ratingBar == ratingBar2) {
			value2 = (int) rating;
		} else if (ratingBar == ratingBar3) {
			value3 = (int) rating;
		}
	}

	/**
	 * author by Ring 提交前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		String msg = "";
		if (value1 == 0) {
			if (position == 1) {
				msg = getResources().getString(R.string.value1_null);
			} else {
				msg = getResources().getString(R.string.value11_null);
			}
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), msg,
					CommentActivity.this);
			return false;
		} else if (value2 == 0) {
			if (position == 1) {
				msg = getResources().getString(R.string.value2_null);
			} else {
				msg = getResources().getString(R.string.value21_null);
			}
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), msg,
					CommentActivity.this);
			return false;
		} else if (value3 == 0) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.value3_null),
					CommentActivity.this);
			return false;
		}/* else if(getCommentState()){
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.coment_message),
					CommentActivity.this);
			return false;
		}*/
		return true;
	}
	
	

	/**
	 * author by Ring 处理耗时操作
	 */
	public Runnable submitRunnable = new Runnable() {

		@Override
		public void run() {
			if (NetworkCheck.IsHaveInternet(CommentActivity.this)) {//检查是否有网络连接
				String msgStr = RequestServerFromHttp.submitValuation(staffid, value1+"", value2+"", value3+"", et.getText().toString());
				if(msgStr.startsWith("0")){
					handler.sendEmptyMessage(1);
				}else if(msgStr.startsWith("-1004")){
					handler.sendEmptyMessage(3);
				}else if(msgStr.equals("404")){
					
				}else{
					handler.sendEmptyMessage(7);
				}
			}else{
				handler.sendEmptyMessage(4);
			}
		}
	};
	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 跳转到微信成功界面
				handler.sendEmptyMessage(6);
				i.setClass(CommentActivity.this, CommentSuccessActivity.class);
				i.putExtra("position", position);
				CommentActivity.this.startActivity(i);
				CommentActivity.this.finish();
				break;
			// case 2:// 从登录界面跳转到注册界面
			// i.setClass(WeiXinActivity.this, RegisterActivity.class);
			// WeiXinActivity.this.startActivity(i);
			// WeiXinActivity.this.finish();
			// break;
			case 3:// 提交失败给用户提示
				handler.sendEmptyMessage(6);
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						"您已经点评过"+(CommentActivity.this.position==1?"摄影师!":"化妆师!"),
						CommentActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				handler.sendEmptyMessage(6);
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.person_error_net),
						CommentActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(CommentActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_sumbit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
//						runnable_tag = false;
//						if (uploaddata != null) {
//							uploaddata.overReponse();
//						}
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
			case 7:// 提交失败给用户提示
				handler.sendEmptyMessage(6);
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						"点评失败，您试着重新提交吧！",
						CommentActivity.this);
				break;
			}
		};
	};
}
