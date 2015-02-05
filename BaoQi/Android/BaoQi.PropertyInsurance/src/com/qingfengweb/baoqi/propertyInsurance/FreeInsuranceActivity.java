package com.qingfengweb.baoqi.propertyInsurance;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class FreeInsuranceActivity extends Activity {
	private static Bitmap bb = null;
	private	Bitmap b = null; 
	private	CanvasWriteActivity canvas;
	private int back_tag = 0;
	private int tag = 0;
	private Spinner sp1,sp2,sp3,sp4,sp5,sp6,sp7,sp8,sp9,sp10;
	private Button guo_nextbtn;
	private ScrollView sview;
	private Button h_btn1,h_btn2,h_btn3,h_btn4;
	public FrameLayout guo_tab5 = null;
	public ImageView guo_image;
	private LinearLayout contents = null;
	private WritePath write = null;
	private ScrollView free_step2;
	public ProgressDialog progressdialog;
	private LayoutInflater inflater;
	private Button step2_nextBtn;
	private ProgressDialog readWaitProgressDialog;
	private String[] bank = { "中国建设银行", "中国工商银行", "中国农业银行", "中国招商银行" };

	private String[] cardtype = { "信用卡", "存储卡" };

	public void confirmShowWaitDialog() {
		if (readWaitProgressDialog == null) {
			readWaitProgressDialog = new ProgressDialog(this);
			readWaitProgressDialog
					.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			readWaitProgressDialog.setMessage("正在执行支付,请稍后...");
			readWaitProgressDialog.setCancelable(true);
			readWaitProgressDialog.show();
		} else {
			readWaitProgressDialog.show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.freeinsurance);
		inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		Intent intent = getIntent();
		tag = intent.getIntExtra("tag", 0);
		h_btn1 = (Button)findViewById(R.id.h_btn1);
		h_btn2 = (Button)findViewById(R.id.h_btn2);
		h_btn3 = (Button)findViewById(R.id.h_btn3);
		bb = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.ywx_xinxi));
		h_btn4 = (Button)findViewById(R.id.h_btn4);
		sview = (ScrollView)findViewById(R.id.part2_1);
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
				}
				return true;
			}

		});
		guo_tab1();
	}
	public void guo_tab2(){
		back_tag = 2;
		h_btn2.setBackgroundResource(R.drawable.bj_tag2);
		h_btn1.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn3.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn4.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn2.setTextColor(Color.parseColor("#FFFFFF"));
		h_btn1.setTextColor(Color.parseColor("#808080"));
		h_btn3.setTextColor(Color.parseColor("#808080"));
		h_btn4.setTextColor(Color.parseColor("#808080"));
		contents = (LinearLayout)findViewById(R.id.part2);
		contents.removeAllViews();
		free_step2=(ScrollView) inflater.inflate(R.layout.free_step2, null);
		step2_nextBtn=(Button) free_step2.findViewById(R.id.nextBtn);
		step2_nextBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				guo_tab5();
			}
		});
		LayoutParams param = new LayoutParams(contents.getWidth(), contents.getHeight());
		free_step2.setLayoutParams(param);
		contents.addView(free_step2);
		
	}
	public void guo_tab1(){
		back_tag = 0;
		h_btn1.setBackgroundResource(R.drawable.bj_tag2);
		h_btn2.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn3.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn4.setBackgroundResource(R.drawable.leftbuttonbg);
		h_btn1.setTextColor(Color.parseColor("#FFFFFF"));
		h_btn2.setTextColor(Color.parseColor("#808080"));
		h_btn3.setTextColor(Color.parseColor("#808080"));
		h_btn4.setTextColor(Color.parseColor("#808080"));
		contents = (LinearLayout)findViewById(R.id.part2);
		contents.removeAllViews();
		sp1 = (Spinner) sview.findViewById(R.id.sp1);
		sp2 = (Spinner) sview.findViewById(R.id.sp2);
		sp3 = (Spinner) sview.findViewById(R.id.sp3);
		sp4 = (Spinner) sview.findViewById(R.id.sp4);
		sp5 = (Spinner) sview.findViewById(R.id.sp5);
		sp6 = (Spinner) sview.findViewById(R.id.sp6);
		sp7 = (Spinner) sview.findViewById(R.id.sp7);
		sp8 = (Spinner) sview.findViewById(R.id.sp8);
		sp9 = (Spinner) sview.findViewById(R.id.sp9);
		sp10 = (Spinner) sview.findViewById(R.id.sp10);
		
		
		guo_nextbtn = (Button)sview.findViewById(R.id.guo_nextbtn);
		
		guo_nextbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				guo_tab2();//lxx
			}
		});
		
		
		
		
		String[] strs = {
			"1","2","3","4","5"	
		};
		String[] strs1 = {
				"1","2"	
			};
		String[] strs2 = {
				"5","10","20","30","40","50"
			};
		String[] strs3 = {
				"不投保","0.2","0.5","1","2","5"
			};
		String[] strs4 = {
				"不投保","10","20","30","40","50"
			};
		String[] strs5 = {
				"不投保","投保"
			};
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,strs);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,strs1);
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,strs2);
		ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,strs3);
		ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,strs4);
		ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,strs5);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp1.setAdapter(adapter1);
		sp2.setAdapter(adapter2);
		sp3.setAdapter(adapter3);
		sp4.setAdapter(adapter4);
		sp5.setAdapter(adapter5);
		sp6.setAdapter(adapter5);
		sp7.setAdapter(adapter6);
		sp8.setAdapter(adapter6);
		sp9.setAdapter(adapter6);
		sp10.setAdapter(adapter6);
		contents.addView(sview);
	}
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		 if(tag == 0&&back_tag == 0){
			 Intent intent = new Intent();
			 intent.setClass(FreeInsuranceActivity.this, ProductCenter.class);
			 FreeInsuranceActivity.this.startActivity(intent);
			 FreeInsuranceActivity.this.finish();
		 }else if(tag == 1&&back_tag == 0){
			 Intent intent = new Intent();
			 intent.putExtra("infos", MyApplication.getInstance().getBundle());
			 intent.setClass(FreeInsuranceActivity.this, ProductInfoActivity.class);
			 FreeInsuranceActivity.this.startActivity(intent);
			 FreeInsuranceActivity.this.finish();
		 }else if(back_tag ==2){
			 guo_tab1();
		 }else if(back_tag ==3){
			 guo_tab2();
		 }else if(back_tag ==4){
			 guo_tab5();
		 }
	  }
	  return true;
	}
	
	// 确认投保
		public void guo_tab5(){
			back_tag = 3;
			h_btn3.setBackgroundResource(R.drawable.bj_tag2);
			h_btn2.setBackgroundResource(R.drawable.leftbuttonbg);
			h_btn1.setBackgroundResource(R.drawable.leftbuttonbg);
			h_btn4.setBackgroundResource(R.drawable.leftbuttonbg);
			h_btn3.setTextColor(Color.parseColor("#FFFFFF"));
			h_btn2.setTextColor(Color.parseColor("#808080"));
			h_btn1.setTextColor(Color.parseColor("#808080"));
			h_btn4.setTextColor(Color.parseColor("#808080"));
			
			
			contents = (LinearLayout)findViewById(R.id.part2);
			contents.removeAllViews();
			LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			guo_tab5 = (FrameLayout)lay.inflate(R.layout.guo_tab5, null);
			final LinearLayout guoLayout1 = (LinearLayout)guo_tab5.findViewById(R.id.guo_linear1);
			final LinearLayout guobtnlayout = (LinearLayout)guo_tab5.findViewById(R.id.guo_btnlayout);
						
			guoLayout1.setVisibility(View.VISIBLE);
			guobtnlayout.setVisibility(View.INVISIBLE);
						
						Button guo_btn1 =(Button)guo_tab5.findViewById(R.id.guo_btn1);
						Button guo_btn2 =(Button)guo_tab5.findViewById(R.id.guo_btn2);
						Button guo_btn3 =(Button)guo_tab5.findViewById(R.id.guo_btn3);			
						Button guo_btn4 =(Button)guo_tab5.findViewById(R.id.guo_btn4);
						guo_image =(ImageView)guo_tab5.findViewById(R.id.guo_image);
						guo_image.setImageBitmap(bb);
					
						guo_btn1.setOnClickListener(new View.OnClickListener() {																		
							@Override
							public void onClick(View v) {
								guoLayout1.setVisibility(View.INVISIBLE);
								guo_tab5.requestFocus();
								guobtnlayout.setVisibility(View.VISIBLE);
//								WritePath write = new WritePath(FreeInsuranceActivity.this);
//								write.setFocusable(true);
								if(write!=null){
									write.clearFocus();
									write =null;
								}
								write = new WritePath(FreeInsuranceActivity.this);
								guo_tab5.addView(write,1);
								guo_tab5.invalidate();
								
							}
						});
						guo_btn2.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								payInfo();
//								part2.setVisibility(View.VISIBLE);
//								contents.setVisibility(View.GONE);
//								part2.removeAllViews();
//								LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//								part2.addView((LinearLayout)lay.inflate(R.layout.sub_layout, null));
//								contents=(LinearLayout) part2.findViewById(R.id.contents);
//								right_new = (LinearLayout)  part2.findViewById(R.id.contents);
//								mViewTitle1 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.cusinfo);
//								mViewTitle2 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.insureItem);
//								mViewTitle3 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.auth);
//								mViewTitle4 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.notify);
//								mViewTitle5 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.payinfo);
//								mViewTitle6 = (TextView) FreeInsuranceActivity.this.findViewById(R.id.sure);
//								mViewTitle1.setOnClickListener(FreeInsuranceActivity.this);
//								mViewTitle2.setOnClickListener(FreeInsuranceActivity.this);
//								mViewTitle3.setOnClickListener(FreeInsuranceActivity.this);
//								mViewTitle4.setOnClickListener(FreeInsuranceActivity.this);
//								mViewTitle5.setOnClickListener(FreeInsuranceActivity.this);
//								mViewTitle6.setOnClickListener(FreeInsuranceActivity.this);
//								FreeInsuranceActivity.this.payInfo();
							}
						});
						guo_btn3.setOnClickListener(new View.OnClickListener() {
				
							private String getPhotoFileName() {  
						        Date date = new Date(System.currentTimeMillis());  
						        SimpleDateFormat dateFormat = new SimpleDateFormat(  
						                "'IMG'_yyyyMMdd_HHmmss");  
						        return dateFormat.format(date) + ".jpg";  
						    } 
								@Override
								public void onClick(View v) {
									guo_tab5.getChildAt(1).setDrawingCacheEnabled(true);
									guo_tab5.getChildAt(1).buildDrawingCache();
									b = guo_tab5.getChildAt(1).getDrawingCache();
									MyApplication.getInstance().setBitmap1(b);
									File mCurrentPhotoFile;//照相机拍照得到的图片 
									String SCREEN_SHOTS_LOCATION = Environment
											.getExternalStorageDirectory()
											+ "/baoqi/baodan";
									File f = new File(SCREEN_SHOTS_LOCATION);
						            f.mkdirs();// 创建照片的存储目录  
						            mCurrentPhotoFile = new File(f, getPhotoFileName());// 给新照的照片文件命名  
						            MyApplication.getInstance().setmCurrentPhotoFile(mCurrentPhotoFile);
//									String fileName = "sbsc" + System.currentTimeMillis() + ".png";
									Bitmap bitmap = BitmapFactory.decodeStream(FreeInsuranceActivity.this.getResources()
											.openRawResource(R.drawable.ywx_xinxi));
									MyApplication.getInstance().setBitmap2(bitmap);
									
									canvas = new CanvasWriteActivity(MyApplication.getInstance().getBitmap2(), MyApplication.getInstance().getBitmap1(), mCurrentPhotoFile, FreeInsuranceActivity.this
											,guo_tab5,guo_image,progressdialog);
									Thread thread = new Thread(canvas);
									thread.start();
									
									guoLayout1.setVisibility(View.VISIBLE);
									guobtnlayout.setVisibility(View.INVISIBLE);
									
									progressdialog.setMessage("系统正在处理中，请稍等");
						            progressdialog.show();
									
						            guo_tab5.setVisibility(View.INVISIBLE);
								}
						});
						guo_btn4.setOnClickListener(new View.OnClickListener() {
				
							@Override
							public void onClick(View v) {
								guo_tab5.removeViewAt(1);
								if(write!=null){
									write.clearFocus();
									write =null;
								}
								write = new WritePath(FreeInsuranceActivity.this);
								guo_tab5.addView(write,1);
								guo_tab5.invalidate();
							}
						});
						
						contents.addView(guo_tab5);
		}
		
		public Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					if (readWaitProgressDialog != null) {
						readWaitProgressDialog.dismiss();
					}
					View view = ((LayoutInflater) FreeInsuranceActivity.this
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
							.inflate(R.layout.shua_card_layout, null);
					LayoutParams params = new LayoutParams(contents.getWidth(), contents.getHeight());
					view.setLayoutParams(params);
					contents.addView(view);
					Button button = (Button) FreeInsuranceActivity.this
							.findViewById(R.id.confirm);
					break;
				case 2:
					contents.removeAllViews();
					View view1 = ((LayoutInflater) FreeInsuranceActivity.this
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
							.inflate(R.layout.succes_layout, null);
					LayoutParams param = new LayoutParams(contents.getWidth(), contents.getHeight());
					view1.setLayoutParams(param);
					contents.addView(view1);
					Button btn = (Button) FreeInsuranceActivity.this
							.findViewById(R.id.confirm);
					btn.setOnClickListener(new OnClickListener() {
	
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(FreeInsuranceActivity.this,
									PropertyInsuranceMainActivity.class);
							FreeInsuranceActivity.this.startActivity(intent);
							FreeInsuranceActivity.this.finish();
						}
					});
	
					break;
				}
			}
		};
		
		
		
		
		public void payInfo(){
			back_tag = 4;
			h_btn4.setBackgroundResource(R.drawable.bj_tag2);
			h_btn2.setBackgroundResource(R.drawable.leftbuttonbg);
			h_btn3.setBackgroundResource(R.drawable.leftbuttonbg);
			h_btn1.setBackgroundResource(R.drawable.leftbuttonbg);
			h_btn4.setTextColor(Color.parseColor("#FFFFFF"));
			h_btn2.setTextColor(Color.parseColor("#808080"));
			h_btn3.setTextColor(Color.parseColor("#808080"));
			h_btn1.setTextColor(Color.parseColor("#808080"));
			
			contents = (LinearLayout)findViewById(R.id.part2);
			contents.removeAllViews();
			LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			LinearLayout  guo_tab6= (LinearLayout)lay.inflate(R.layout.pay_layout, null);
			
			LayoutParams params = new LayoutParams(contents.getWidth(), contents.getHeight());
			guo_tab6.setLayoutParams(params);
			contents.addView(guo_tab6);
			Button btn = (Button) guo_tab6.findViewById(R.id.zhifu);
			btn.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					contents.removeAllViews();
					View view = ((LayoutInflater) FreeInsuranceActivity.this
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
							.inflate(R.layout.next1_layout, null);
					LayoutParams params = new LayoutParams(contents.getWidth(), contents.getHeight());
					view.setLayoutParams(params);
					contents.addView(view);
					Button view1 = (Button) FreeInsuranceActivity.this
							.findViewById(R.id.carsh);
					view1.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							contents.removeAllViews();
							View view = ((LayoutInflater) FreeInsuranceActivity.this
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
									.inflate(R.layout.wait_layout, null);
							LayoutParams params = new LayoutParams(contents.getWidth(), contents.getHeight());
							view.setLayoutParams(params);
							contents.addView(view);
							TextView view2 = (TextView) FreeInsuranceActivity.this
									.findViewById(R.id.ka);
							view2.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									contents.removeAllViews();
									View view = ((LayoutInflater) FreeInsuranceActivity.this
											.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
											.inflate(R.layout.shua_card_layout,
													null);
									LayoutParams params = new LayoutParams(contents.getWidth(), contents.getHeight());
									view.setLayoutParams(params);
									contents.addView(view);
									TextView view2 = (TextView) FreeInsuranceActivity.this
											.findViewById(R.id.ka);
									view2.setOnClickListener(new OnClickListener() {
	
										@Override
										public void onClick(View v) {
											contents.removeAllViews();
											View view = ((LayoutInflater) FreeInsuranceActivity.this
													.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
													.inflate(
															R.layout.confirm_layout,
															null);
											LayoutParams params = new LayoutParams(contents.getWidth(), contents.getHeight());
											view.setLayoutParams(params);
											contents.addView(view);
											Button button = (Button) FreeInsuranceActivity.this
													.findViewById(R.id.confirm);
											Spinner s1 = (Spinner) FreeInsuranceActivity.this
													.findViewById(R.id.s1);
											Spinner s2 = (Spinner) FreeInsuranceActivity.this
													.findViewById(R.id.s2);
											ArrayAdapter<String> arrayadapter = new ArrayAdapter<String>(
													FreeInsuranceActivity.this,
													android.R.layout.simple_dropdown_item_1line,
													cardtype);
											s1.setAdapter(arrayadapter);
											arrayadapter = new ArrayAdapter<String>(
													FreeInsuranceActivity.this,
													android.R.layout.simple_dropdown_item_1line,
													bank);
											s2.setAdapter(arrayadapter);
											button.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													new Thread() {
														public void run() {
															try {
																sleep(3000);
																handler.sendEmptyMessage(1);
																handler.sendEmptyMessage(2);
															} catch (InterruptedException e) {
																e.printStackTrace();
															}
														}
													}.start();
													confirmShowWaitDialog();
												}
											});
										}
	
									});
								}
							});
						}
					});
				}
			});
		}
		
		
		@Override
		protected void onDestroy() {
			if(bb != null){
				bb.recycle();
				bb = null;
			}
			
			if(CanvasWriteActivity.newb != null){
				CanvasWriteActivity.newb.recycle();
				CanvasWriteActivity.newb = null;
			}
			
			if(WritePath.mBitmap!= null){
				WritePath.mBitmap.recycle();
				WritePath.mBitmap = null;
			}
			
			if(WritePath.mCanvas!= null){
				WritePath.mCanvas = null;
			}
			System.gc();
			super.onDestroy();
		}
}
