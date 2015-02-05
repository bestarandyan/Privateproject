package com.qingfengweb.id.blm_goldenLadies;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.MyApplication;

public class TemplateInfoActivity extends BaseActivity {
	private Button backBtn;
	private EditText shouyaoName;// 受邀人
	private EditText xinlangName, xinniangName;// 新郎 新娘
	private TextView year, month, ri, shijian, longliyue, longliri,week;// 年 月 日 星期 农历 时间
	private Calendar cal = Calendar.getInstance(); //当前日期设置
	private boolean isLongLi = false;//判断当前日期控件是否是用来设置农历时间
	private EditText dianming, address, phone;// 店名 地址 电话
	private RelativeLayout bottomLinear;// 选择照片部分
	private LinearLayout linearLayoutText;
	private ImageView bottomImage;// 照片
	private Button createBtn;// 创建请帖模板
	private int type = 0;// 用来标记是那一个模板
	private TextView line1, line2, line3, line4, line5;
	private LinearLayout xingqiLinear, longliLinear, shijianLinear,
			dianmingLinear, addressLinear, phoneLinear;
	private String template = "";
	private int templateId = 0;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private final String TAG = "TemplateInfoActivity";
	private String path = "";// 照片路径
	private int photo_tag = 0;// 0控件未装填图片，1代表控件已装填图片了
	private LinearLayout shouyaorenLienar;
	private LinearLayout dateSelectLayout;
	public Bundle bundle = new Bundle();
	
	private Dialog weekDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_templateone);
		findView();
		initView();
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		shouyaoName = (EditText) findViewById(R.id.shouyao);
		xinlangName = (EditText) findViewById(R.id.xinlang);
		xinniangName = (EditText) findViewById(R.id.xinniang);
		year = (TextView) findViewById(R.id.year);
		month = (TextView) findViewById(R.id.month);
		ri = (TextView) findViewById(R.id.ri);
		week = (TextView) findViewById(R.id.week);
		longliyue = (TextView) findViewById(R.id.longliyue);
		longliri = (TextView) findViewById(R.id.longliri);
		shijian = (TextView) findViewById(R.id.shijian);
		dianming = (EditText) findViewById(R.id.dianming);
		address = (EditText) findViewById(R.id.address);
		phone = (EditText) findViewById(R.id.phone);
		bottomLinear = (RelativeLayout) findViewById(R.id.bottomLinear);
		bottomImage = (ImageView) findViewById(R.id.image);
		bottomImage.setClickable(false);
		bottomLinear.setOnClickListener(this);
		createBtn = (Button) findViewById(R.id.createBtn);
		backBtn.setOnClickListener(this);
		createBtn.setOnClickListener(this);
		shouyaorenLienar = (LinearLayout) findViewById(R.id.shouyaorenLinear);
		shouyaoName.setOnFocusChangeListener(textfocuschange);
		xinlangName.setOnFocusChangeListener(textfocuschange);
		xinniangName.setOnFocusChangeListener(textfocuschange);
		year.setOnFocusChangeListener(textfocuschange);
		month.setOnFocusChangeListener(textfocuschange);
		ri.setOnFocusChangeListener(textfocuschange);
		week.setOnFocusChangeListener(textfocuschange);
		longliyue.setOnFocusChangeListener(textfocuschange);
		longliri.setOnFocusChangeListener(textfocuschange);
		shijian.setOnFocusChangeListener(textfocuschange);
		dianming.setOnFocusChangeListener(textfocuschange);
		address.setOnFocusChangeListener(textfocuschange);
		phone.setOnFocusChangeListener(textfocuschange);
		
		shouyaoName.addTextChangedListener(textWatcher);
		shouyaoName.addTextChangedListener(textWatcher);
		xinlangName.addTextChangedListener(textWatcher);
		xinniangName.addTextChangedListener(textWatcher);
		year.addTextChangedListener(textWatcher);
		month.addTextChangedListener(textWatcher);
		ri.addTextChangedListener(textWatcher);
		week.addTextChangedListener(textWatcher);
		longliyue.addTextChangedListener(textWatcher);
		longliri.addTextChangedListener(textWatcher);
		shijian.addTextChangedListener(textWatcher);
		dianming.addTextChangedListener(textWatcher);
		address.addTextChangedListener(textWatcher);
		phone.addTextChangedListener(textWatcher);
		
		
		createBtn.setTextColor(Color.GRAY);
		xingqiLinear = (LinearLayout) findViewById(R.id.xingqiLinear);
		xingqiLinear.setOnClickListener(this);
		longliLinear = (LinearLayout) findViewById(R.id.longliLinear);
		longliLinear.setOnClickListener(this);
		shijianLinear = (LinearLayout) findViewById(R.id.shijianLinear);
		shijianLinear.setOnClickListener(this);
		dianmingLinear = (LinearLayout) findViewById(R.id.xisheLinear);
		addressLinear = (LinearLayout) findViewById(R.id.addressLinear);
		phoneLinear = (LinearLayout) findViewById(R.id.phoneLinear);
		line1 = (TextView) findViewById(R.id.line1);
		line2 = (TextView) findViewById(R.id.line2);
		line3 = (TextView) findViewById(R.id.line3);
		line4 = (TextView) findViewById(R.id.line4);
		line5 = (TextView) findViewById(R.id.line5);
		linearLayoutText = (LinearLayout) findViewById(R.id.bottomLinearText);
		
		dateSelectLayout = (LinearLayout) findViewById(R.id.dateSelectLayout);
		dateSelectLayout.setOnClickListener(this);
	}

	private void initView() {
		type = Integer.parseInt(MyApplication.getInstant().getTemplateid());
		switch(type){
				case 1:
					longliLinear.setVisibility(View.GONE);
					line2.setVisibility(View.GONE);
					bottomLinear.setVisibility(View.GONE);
					xingqiLinear.setVisibility(View.GONE);
					line1.setVisibility(View.GONE);
					template = "f1.jpg";
//					templateId = R.drawable.f1;
					break;
				case 2:
					phoneLinear.setVisibility(View.GONE);		
					line5.setVisibility(View.GONE);
					addressLinear.setVisibility(View.GONE);
					line4.setVisibility(View.GONE);
					template = "f2.png";
//					templateId = R.drawable.f2;
					break;
				case 3:
					xingqiLinear.setVisibility(View.GONE);
					line1.setVisibility(View.GONE);
					phoneLinear.setVisibility(View.GONE);
					line5.setVisibility(View.GONE);
					longliLinear.setVisibility(View.GONE);
					line2.setVisibility(View.GONE);
					template = "f3.png";
//					templateId = R.drawable.f3;
					break;
				case 4:
					phoneLinear.setVisibility(View.GONE);		
					line5.setVisibility(View.GONE);
					bottomLinear.setVisibility(View.GONE);
					template = "f4.jpg";
//					templateId = R.drawable.f4;
					break;
				case 5:
					phoneLinear.setVisibility(View.GONE);		
					line5.setVisibility(View.GONE);
					addressLinear.setVisibility(View.GONE);
					line4.setVisibility(View.GONE);
					template = "f5.png";
//					templateId = R.drawable.f5;
					break;
				case 6:
					phoneLinear.setVisibility(View.GONE);		
					line5.setVisibility(View.GONE);
					addressLinear.setVisibility(View.GONE);
					line4.setVisibility(View.GONE);
					longliLinear.setVisibility(View.GONE);
					line2.setVisibility(View.GONE);
					template = "f6.png";
//					templateId = R.drawable.f6;
					xingqiLinear.setVisibility(View.GONE);
					line1.setVisibility(View.GONE);
					break;
				case 7:
					phoneLinear.setVisibility(View.GONE);		
					line5.setVisibility(View.GONE);
					dianmingLinear.setVisibility(View.GONE);
					line4.setVisibility(View.GONE);
					longliLinear.setVisibility(View.GONE);
					line2.setVisibility(View.GONE);
					bottomLinear.setVisibility(View.GONE);
					shijianLinear.setVisibility(View.GONE);
					line3.setVisibility(View.GONE);
					template = "f7.jpg";
//					templateId = R.drawable.f7;
					xingqiLinear.setVisibility(View.GONE);
					line1.setVisibility(View.GONE);
					break;
				case 8:
					shouyaorenLienar.setVisibility(View.GONE);
					bottomLinear.setVisibility(View.VISIBLE);
					phoneLinear.setVisibility(View.GONE);		
					line5.setVisibility(View.GONE);
					dianmingLinear.setVisibility(View.GONE);
					line4.setVisibility(View.GONE);
					longliLinear.setVisibility(View.GONE);
					line2.setVisibility(View.GONE);
					template = "f8.png";
//					templateId = R.drawable.f8;
					xingqiLinear.setVisibility(View.GONE);
					line1.setVisibility(View.GONE);
					break;
		}

//		if (MyApplication.getInstance(this).getFilepath() != null
//				&& !MyApplication.getInstance(this).getFilepath().equals("")) {
//			photo_tag = 1;
//			linearLayoutText.setVisibility(View.GONE);
//			Bitmap bitmap = BitmapFactory.decodeFile(MyApplication.getInstance(
//					this).getFilepath());
//			bottomImage.setImageBitmap(bitmap);
//			path = MyApplication.getInstance(this).getFilepath();
//		}
		Bundle bundle1 = getIntent().getBundleExtra("bundle1");
		if(bundle1!=null){
			//shouyaoName xinlangName xinniangName year  month ri week
			//longliyue longliri shijian dianming address phone
			shouyaoName.setText(bundle1.getString("shouyaoName"));
			xinlangName.setText(bundle1.getString("xinlangName"));
			xinniangName.setText(bundle1.getString("xinniangName"));
			year.setText(bundle1.getString("year"));
			month.setText(bundle1.getString("month"));
			ri.setText(bundle1.getString("ri"));
			week.setText(bundle1.getString("week"));
			longliyue.setText(bundle1.getString("longliyue"));
			longliri.setText(bundle1.getString("longliri"));
			shijian.setText(bundle1.getString("shijian"));
			dianming.setText(bundle1.getString("dianming"));
			address.setText(bundle1.getString("address"));
			phone.setText(bundle1.getString("phone"));
			if(bundle1.getString("image")!=null&&!bundle1.getString("image").equals("")){
				Bitmap bm = getDrawable(bundle1.getString("image"));
				if(bm!=null){
					photo_tag = 1;
					linearLayoutText.setVisibility(View.GONE);
					bottomImage.setImageBitmap(bm);
				}
			}
			bundle = bundle1;
			path = bundle1.getString("image");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			Log.e(TAG, "ActivityResult resultCode error");
			return;
		}
		Bitmap bm = null;
		// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
		ContentResolver resolver = getContentResolver();
		// 此处的用于判断接收的Activity是不是你想要的那个
		if (requestCode == IMAGE_CODE) {
				Uri originalUri = data.getData(); // 获得图片的uri
//				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片
			
				photo_tag = 1;
				// 这里开始的第二部分，获取图片的路径：
				String[] proj = { MediaStore.Images.Media.DATA };
				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				Cursor cursor = managedQuery(originalUri, proj, null, null,	null);
//				InputStream inputStream = resolver.openInputStream(originalUri);
				
				// 按我个人理解 这个是获得用户选择的图片的索引值
				if(cursor==null){
					path = data.getDataString();
				}else{
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					// 将光标移至开头 ，这个很重要，不小心很容易引起越界
					cursor.moveToFirst();
					// 最后根据索引值获取图片路径www.2cto.com
					path = cursor.getString(column_index);
				}
				if(path==null || path.equals("") || !new File(path).exists()){
					Toast.makeText(this, "图片不存在", 3000).show();
				}else{
					bm = getDrawable(path);
					photo_tag = 1;
					linearLayoutText.setVisibility(View.GONE);
					bottomImage.setImageBitmap(bm);
				}
				
		}

	}
	
	@Override
	protected void onDestroy() {
		if(bottomImage!=null){
			BitmapDrawable bitmapDrawable = (BitmapDrawable)bottomImage.getDrawable();   
	        //如果图片还未回收，先强制回收该图片    
	        if(bitmapDrawable !=null &&bitmapDrawable.getBitmap()!=null && !bitmapDrawable.getBitmap().isRecycled()){   
	            bitmapDrawable.getBitmap().recycle();   
	        }  
		}
		super.onDestroy();
	}
	
	private Bitmap getDrawable(String pathName) {
		BitmapFactory.Options opts =  new  BitmapFactory.Options();
		Bitmap   bmp = null;
		try {
	        opts.inJustDecodeBounds =  true ;
	        opts.inPreferredConfig = Bitmap.Config.RGB_565;   
	        BitmapFactory.decodeFile(pathName, opts);
	         opts.inSampleSize = computeSampleSize(opts, -1 , 1000 * 1000 ); 
	         opts.inJustDecodeBounds =  false ;
	     	BitmapDrawable bitmapDrawable = (BitmapDrawable)bottomImage.getDrawable();   
	        //如果图片还未回收，先强制回收该图片    
	        if(bitmapDrawable !=null && !bitmapDrawable.getBitmap().isRecycled()){   
	            bitmapDrawable.getBitmap().recycle();   
	        }   
	              bmp = BitmapFactory.decodeFile(pathName, opts);
//	        	  bmp = BitmapCache.getInstance().getBitmap(id, MyCameraActivity.this,opts);
	              bmp = scaleImg(bmp,bmp.getWidth(),bmp.getHeight());
		} catch (OutOfMemoryError e) {
			bmp = scaleImg(bmp,800,600);
		}
		
			return bmp;
	}
	
	
	/**
	 * 等比压缩图片
	 * 
	 * @param bm
	 *            被压缩的图片
	 * @param newWidth
	 *            压缩后的宽度
	 * @param newHeight
	 *            压缩后的图片高度
	 * @return 一张新的图片
	 * @author 刘星星
	 */
	

	public Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		// 图片源
		// Bitmap bm = BitmapFactory.decodeStream(getResources()
		// .openRawResource(id));
		// 获得图片的宽高
		// Bitmap newbm = null;
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		float newWidth1 = newWidth;
		float newHeight1 = newHeight;
		/*
		 * if(width<newWidth1 && height <newHeight1){ return bm; }
		 */
		// 计算缩放比例
		if (height > width) {
			float scaleHeight = ((float) newHeight1) / height;
			newWidth1 = ((float) (width * (float) newHeight1) / height);
			float scaleWidth = ((float) newWidth1) / width;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			Bitmap	scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
			return scaleBit;
		} else {
			float scaleWidth = ((float) newWidth1) / width;
			newHeight1 = ((float) (height * (float) newWidth1) / width);
			float scaleHeight = ((float) newHeight1) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
		
			Bitmap	scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,	true);
			// Bitmap bmp = scaleBit;
			return scaleBit;
		}

	}
	/**
	 * 动态计算出图片的inSampleSize
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return 返回图片的最佳inSampleSize
	 */
	public int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (textValidate()) {
				handler.sendEmptyMessage(1);
			}else{
				handler.sendEmptyMessage(2);
			}
		}
	};
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				createBtn.setTextColor(Color.WHITE);
				createBtn.setOnClickListener(TemplateInfoActivity.this);
			}else if(msg.what == 2){
				createBtn.setTextColor(Color.GRAY);
			}
		};
	};
	
	@Override
	protected void onResume() {
		new Thread(runnable).start();
		super.onResume();
	}

	/**
	 * author by Ring 登录前对提交信息进行验证 return true 验证成功，false 验证失败
	 */

	// private EditText shouyaoName;//受邀人
	// private EditText xinlangName,xinniangName;//新郎 新娘
	// private EditText year,month,ri,week,longliyue,longliri,shijian;//年 月 日 星期
	// 农历 时间
	// private EditText dianming,address,phone;//店名 地址 电话
	public boolean textValidate() {
		if (shouyaoName.isShown()&&shouyaoName.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (xinlangName.isShown()&&xinlangName.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (xinniangName.isShown()&&xinniangName.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (year.isShown()&&year.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (month.isShown()&&month.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (ri.isShown()&&ri.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (week.isShown()&&week.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (longliyue.isShown()&&longliyue.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (longliri.isShown()&&longliri.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (shijian.isShown()&&shijian.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (dianming.isShown()&&dianming.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (address.isShown()&&address.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if (phone.isShown()&&phone.getText().toString().trim().equals("")) {
			System.out.println(false);
			return false;
		} else if ((type == 2 || type == 3 || type == 5 || type == 6||type ==8)
				&& photo_tag == 0) {
			System.out.println(false);
			return false;
		}
		return true;
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (textValidate()) {
//			createBtn.setClickable(true);
//			createBtn.setTextColor(Color.WHITE);
//			createBtn.setOnClickListener(this);
//		}
//		return false;
//	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			finish();
		} else if (v == createBtn) {
			if(!textValidate()){
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				callDailog
						.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle(R.string.prompt)
						.setMessage("您还有未填写的信息，请将信息填写完整后创建请帖！")
						.setPositiveButton(R.string.comfrim,null);
				callDailog.show();
				return;
			}
			bundle.putString("receiver", shouyaoName.getText().toString());
			bundle.putString("mr", xinlangName.getText().toString());
			bundle.putString("miss", xinniangName.getText().toString());
			bundle.putString("marry_date_year", year.getText().toString());
			bundle.putString("marry_date_month", month.getText().toString());
			bundle.putString("marry_date_date", ri.getText().toString());
			// if(xingqiLinear.isShown()){
			bundle.putString("week", week.getText().toString());
			// }
			// if(longliLinear.isShown()){
			bundle.putString("marry_nongli_month", longliyue.getText().toString());
			bundle.putString("marry_nongli_date", longliri.getText().toString());
			// }
			// if(shijianLinear.isShown()){
			bundle.putString("time", shijian.getText().toString());
			// }
			// if(dianmingLinear.isShown()){
			bundle.putString("location", dianming.getText().toString());
			// }
			// if(addressLinear.isShown()){
			bundle.putString("address", address.getText().toString());
			// }
			// if(phoneLinear.isShown()){
			bundle.putString("phone_number", phone.getText().toString());
			// }
			// if(bottomLinear.isShown()){
			bundle.putString("image", path);
			// }
//			bundle.putInt("imageId", imageId);
			bundle.putString("template", template);
			bundle.putInt("templateId", templateId);
			Intent i = new Intent(this, TemplateImagePreviewActivity.class);
			i.putExtra("bundle", bundle);
			i.putExtra("type", type);
			i.putExtra("activity_type", 1);//用于给照片位置赋予初始值做条件判断
			startActivity(i);
			finish();
		} else if (v == bottomLinear) {
			if (textValidate()) {
				createBtn.setTextColor(Color.WHITE);
				createBtn.setOnClickListener(this);
			}else{
				createBtn.setTextColor(Color.GRAY);
			}
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType(IMAGE_TYPE);
			startActivityForResult(getAlbum, IMAGE_CODE);
		} else if(v == dateSelectLayout){
			isLongLi = false;
			showDatePickerDialog();//显示日期控件
		}else if(v == shijianLinear){
			showTimePickerDialog();//显示时间控件
		}else if(v == longliLinear){
			isLongLi = true;
			showDatePickerDialog();//显示日期控件设置农历日期
		}else if(v == xingqiLinear){
			showWeekDialog();
		}else if(v.getId() == R.id.shareMoreClose){
			weekDialog.dismiss();
		}
//		else if (v == shouyaoName || v == xinlangName || v == xinniangName
//				|| v == year || v == month || v == ri || v == week
//				|| v == longliyue || v == longliri || v == shijian) {
//			
//		}
		super.onClick(v);
	}
	/**
	 * 显示星期选择框
	 */
	public void showWeekDialog(){
		weekDialog = new Dialog(this, R.style.myDialogStyle);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_week, null);
		view.findViewById(R.id.shareMoreClose).setOnClickListener(this);
		ListView listView = (ListView) view.findViewById(R.id.listView);
		List<Map<String,String>> data = new ArrayList<Map<String,String>>();
		for(int i=0;i<7;i++){
			Map<String,String> map = new HashMap<String, String>();
			map.put("week", "星期"+ConstantsValues.weeks[i]);
			data.add(map);
		}
		listView.setAdapter(new SimpleAdapter(this, data, R.layout.item_simpleadapter, new String[]{"week"}, new int[]{R.id.item_tv}));
		listView.setOnItemClickListener(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(MyApplication.getInstant().getWidthPixels()*0.9),
				LayoutParams.WRAP_CONTENT);
		weekDialog.addContentView(view, params);
		weekDialog.setCanceledOnTouchOutside(false);
		weekDialog.show();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		weekDialog.dismiss();
		week.setText(ConstantsValues.weeks[arg2]);
		super.onItemClick(arg0, arg1, arg2, arg3);
	}
	/**
	 * 日期控件监听器
	 */
	    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() { 
	        @Override 
	   
	        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) { 
	            cal.set(Calendar.YEAR, year); 
	            cal.set(Calendar.MONTH, monthOfYear); 
	            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth); 
	            if(isLongLi){
	            	longliyue.setText(convertDate(cal.get(Calendar.MONTH)+1));
	            	longliri.setText(convertDate(cal.get(Calendar.DAY_OF_MONTH)));
	            }else{
	            	setDateView(); 
	            }
	        } 
	    }; 
	    /**
	     * 将十二个月的数字转化成为汉字 
	     * @param number
	     * @return 农历日期格式
	     */
	    private String convertDate(int number){
	    	String hanzi[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五",
	    			"十六","十七","十八","十九","二十","二十一","二十二","二十三","二十四","二十五","二十六","二十七","二十八","二十九","三十","三十一"};
	    	return hanzi[number-1];
	    }
	    /**
	     * 时间选择器监听器
	     */
	    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
				setTimeView();
			}
		};
	    /**
	     * 显示日期控件
	     */
	    public void showDatePickerDialog(){ 
	        new DatePickerDialog(this,datePickerListener, 
	        		cal.get(Calendar.YEAR),
	                cal.get(Calendar.MONTH), 
	                cal.get(Calendar.DAY_OF_MONTH) 
	                ).show(); 
	    } 
	    /**
	     * 显示时间控件
	     */
	    public void showTimePickerDialog(){
	    	new TimePickerDialog(this, timePickerListener, cal.get(Calendar.HOUR_OF_DAY),
	    			cal.get(Calendar.MINUTE), true).show();
	    }
	   /**
	    * 设置日期控件值
	    */
	    private void setDateView(){ 
	    	year.setText(cal.get(Calendar.YEAR)+"");
	    	month.setText((cal.get(Calendar.MONTH)+1)>10?(cal.get(Calendar.MONTH)+1+""):("0"+(cal.get(Calendar.MONTH)+1)));
	    	ri.setText((cal.get(Calendar.DAY_OF_MONTH))>10?cal.get(Calendar.DAY_OF_MONTH)+"":"0"+cal.get(Calendar.DAY_OF_MONTH));
	    } 
	    /**
	     * 设置事件控件值
	     */
	    private void setTimeView(){
	    	shijian.setText(((cal.get(Calendar.HOUR_OF_DAY)<10)?("0"+cal.get(Calendar.HOUR_OF_DAY)):cal.get(Calendar.HOUR_OF_DAY))+
	    			":"+((cal.get(Calendar.MINUTE)<10)?("0"+cal.get(Calendar.MINUTE)):cal.get(Calendar.MINUTE)));
	    }
	private OnFocusChangeListener textfocuschange = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (textValidate()) {
				createBtn.setTextColor(Color.WHITE);
				createBtn.setOnClickListener(TemplateInfoActivity.this);
			}else{
				createBtn.setTextColor(Color.GRAY);
			}
		}
	};
	
	private TextWatcher textWatcher = new TextWatcher() {  
        
        @Override    
        public void afterTextChanged(Editable s) {
        	
        }   
          
        @Override 
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
        }  
 
         @Override    
        public void onTextChanged(CharSequence s, int start, int before,     
                int count) {     
        	 if (textValidate()) {
 				createBtn.setTextColor(Color.WHITE);
 				createBtn.setOnClickListener(TemplateInfoActivity.this);
 			}else{
 				createBtn.setTextColor(Color.GRAY);
 			}
        }                    
    };
}
