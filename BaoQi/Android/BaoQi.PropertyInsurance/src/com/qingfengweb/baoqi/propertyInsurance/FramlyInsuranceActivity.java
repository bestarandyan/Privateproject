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

public class FramlyInsuranceActivity extends Activity {
	private	Bitmap b = null; 
	private static Bitmap bb = null;
	private	CanvasWriteActivity canvas;
	private int back_tag = 0;
	private ScrollView step1,step2;
	private Spinner sp1,sp2,sp3,sp4,sp5,sp6,sp7,sp8,sp9;
	private Button guo_nextbtn;
	private LayoutInflater inflater;
	private LinearLayout layout;
	private ArrayAdapter<CharSequence> step2_adapter,step2_adapter1;
	Spinner step2_spinner1,step2_spinner2;
	private Button tab_btn1,tab_btn2,tab_btn3,tab_btn4;
	String[] strs = {"1","2","3","4","5"};
	String[] strs1 = {"1","2"};
	String[] strs2 = {"5","10","20","30","40","50"};
	String[] strs3 = {"不投保","0.2","0.5","1","2","5"};
	String[] strs4 = {"不投保","10","20","30","40","50"};
	String[] strs5 = {"不投保","投保"};
	LayoutParams lp=null;
	
	
	public FrameLayout guo_tab5 = null;
	public ImageView guo_image;
	private WritePath write = null;
	public ProgressDialog progressdialog;
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
		setContentView(R.layout.framlysurance);
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
		tab_btn1=(Button) findViewById(R.id.tab_btn1);
		tab_btn2=(Button) findViewById(R.id.tab_btn2);
		tab_btn3=(Button) findViewById(R.id.tab_btn3);
		tab_btn4=(Button) findViewById(R.id.tab_btn4);
		step1=(ScrollView) findViewById(R.id.part2_1);
		
		bb = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.ccx_xinxi));
		
		tab_btn1.setBackgroundResource(R.drawable.bj_tag2);
		tab_btn2.setBackgroundResource(R.drawable.leftbuttonbg);
		tab_btn3.setBackgroundResource(R.drawable.leftbuttonbg);
		tab_btn4.setBackgroundResource(R.drawable.leftbuttonbg);
		guo_tab1();
	}

	
	@Override
	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
			 if(back_tag == 0){
				 Intent intent = new Intent();
				 intent.setClass(FramlyInsuranceActivity.this, PropertyInsuranceMainActivity.class);
				 FramlyInsuranceActivity.this.startActivity(intent);
				 FramlyInsuranceActivity.this.finish();
			 }else if(back_tag == 1){
				 guo_tab1();
			 }else if(back_tag == 2){
				 guo_tab2();
			 }else if(back_tag == 3){
				 guo_tab5();
			 }
	  }
	  return true;
	}
	
	public void guo_tab1(){
		back_tag = 0;
		tab_btn1.setBackgroundResource(R.drawable.bj_tag2);
		tab_btn2.setBackgroundResource(R.drawable.leftbuttonbg);
		tab_btn3.setBackgroundResource(R.drawable.leftbuttonbg);
		tab_btn4.setBackgroundResource(R.drawable.leftbuttonbg);
		tab_btn1.setTextColor(Color.parseColor("#FFFFFF"));
		tab_btn2.setTextColor(Color.parseColor("#808080"));
		tab_btn3.setTextColor(Color.parseColor("#808080"));
		tab_btn4.setTextColor(Color.parseColor("#808080"));
		layout=(LinearLayout) findViewById(R.id.liuxingxing);
		layout.removeAllViews();
		sp1 = (Spinner) step1.findViewById(R.id.sp1);
		sp2 = (Spinner)  step1.findViewById(R.id.sp2);
		sp3 = (Spinner)  step1.findViewById(R.id.sp3);
		sp4 = (Spinner) step1. findViewById(R.id.sp4);
		sp5 = (Spinner)  step1.findViewById(R.id.sp5);
		sp6 = (Spinner)  step1.findViewById(R.id.sp6);
		sp7 = (Spinner) step1. findViewById(R.id.sp7);
		sp8 = (Spinner)  step1.findViewById(R.id.sp8);
		sp9 = (Spinner)  step1.findViewById(R.id.sp9);
		guo_nextbtn=(Button) step1.findViewById(R.id.guo_nextbtn);
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
		guo_nextbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				guo_tab2();
			}
		});
		
		layout.addView(step1);
	}
	
	public void guo_tab2(){
		tab_btn2.setBackgroundResource(R.drawable.bj_tag2);
		tab_btn1.setBackgroundResource(R.drawable.leftbuttonbg);
		tab_btn3.setBackgroundResource(R.drawable.leftbuttonbg);
		tab_btn4.setBackgroundResource(R.drawable.leftbuttonbg);
		tab_btn2.setTextColor(Color.parseColor("#FFFFFF"));
		tab_btn1.setTextColor(Color.parseColor("#808080"));
		tab_btn3.setTextColor(Color.parseColor("#808080"));
		tab_btn4.setTextColor(Color.parseColor("#808080"));
		back_tag = 1;
		layout=(LinearLayout) findViewById(R.id.liuxingxing);
		layout.removeAllViews();
		LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		step2= (ScrollView)lay.inflate(R.layout.framlysurance_tep2, null);

		Button btn = (Button)step2.findViewById(R.id.nextBtn);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				guo_tab5();
			}
		});

		step2_spinner1=(Spinner) step2.findViewById(R.id.fangwu_spinner1);
		step2_spinner2=(Spinner) step2.findViewById(R.id.fangwu_spinner2);
		step2_adapter=ArrayAdapter.createFromResource(FramlyInsuranceActivity.this, R.array.city, android.R.layout.simple_spinner_item);
		step2_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		step2_adapter1=ArrayAdapter.createFromResource(FramlyInsuranceActivity.this, R.array.city2, android.R.layout.simple_spinner_item);
		step2_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		step2_spinner1.setAdapter(step2_adapter);
		step2_spinner2.setAdapter(step2_adapter1);
		LayoutParams params = new LayoutParams(layout.getWidth(), layout.getHeight());
		step2.setLayoutParams(params);
		layout.addView(step2);
	}
	
	// 确认投保
			public void guo_tab5(){
				back_tag = 2;
				tab_btn3.setBackgroundResource(R.drawable.bj_tag2);
				tab_btn2.setBackgroundResource(R.drawable.leftbuttonbg);
				tab_btn1.setBackgroundResource(R.drawable.leftbuttonbg);
				tab_btn4.setBackgroundResource(R.drawable.leftbuttonbg);
				tab_btn3.setTextColor(Color.parseColor("#FFFFFF"));
				tab_btn2.setTextColor(Color.parseColor("#808080"));
				tab_btn1.setTextColor(Color.parseColor("#808080"));
				tab_btn4.setTextColor(Color.parseColor("#808080"));
				
				
				layout = (LinearLayout)findViewById(R.id.liuxingxing);
				layout.removeAllViews();
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
//									WritePath write = new WritePath(FramlyInsuranceActivity.this);
//									write.setFocusable(true);
									if(write!=null){
										write.clearFocus();
										write =null;
									}
									write = new WritePath(FramlyInsuranceActivity.this);
									guo_tab5.addView(write,1);
									guo_tab5.invalidate();
									
								}
							});
							guo_btn2.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									payInfo();
//									part2.setVisibility(View.VISIBLE);
//									layout.setVisibility(View.GONE);
//									part2.removeAllViews();
//									LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//									part2.addView((LinearLayout)lay.inflate(R.layout.sub_layout, null));
//									layout=(LinearLayout) part2.findViewById(R.id.layout);
//									right_new = (LinearLayout)  part2.findViewById(R.id.layout);
//									mViewTitle1 = (TextView) FramlyInsuranceActivity.this.findViewById(R.id.cusinfo);
//									mViewTitle2 = (TextView) FramlyInsuranceActivity.this.findViewById(R.id.insureItem);
//									mViewTitle3 = (TextView) FramlyInsuranceActivity.this.findViewById(R.id.auth);
//									mViewTitle4 = (TextView) FramlyInsuranceActivity.this.findViewById(R.id.notify);
//									mViewTitle5 = (TextView) FramlyInsuranceActivity.this.findViewById(R.id.payinfo);
//									mViewTitle6 = (TextView) FramlyInsuranceActivity.this.findViewById(R.id.sure);
//									mViewTitle1.setOnClickListener(FramlyInsuranceActivity.this);
//									mViewTitle2.setOnClickListener(FramlyInsuranceActivity.this);
//									mViewTitle3.setOnClickListener(FramlyInsuranceActivity.this);
//									mViewTitle4.setOnClickListener(FramlyInsuranceActivity.this);
//									mViewTitle5.setOnClickListener(FramlyInsuranceActivity.this);
//									mViewTitle6.setOnClickListener(FramlyInsuranceActivity.this);
//									FramlyInsuranceActivity.this.payInfo();
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
//										String fileName = "sbsc" + System.currentTimeMillis() + ".png";
										Bitmap bitmap = BitmapFactory.decodeStream(FramlyInsuranceActivity.this.getResources()
												.openRawResource(R.drawable.ccx_xinxi));
										MyApplication.getInstance().setBitmap2(bitmap);
										
										canvas = new CanvasWriteActivity(MyApplication.getInstance().getBitmap2(), MyApplication.getInstance().getBitmap1(), mCurrentPhotoFile, FramlyInsuranceActivity.this
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
									write = new WritePath(FramlyInsuranceActivity.this);
									guo_tab5.addView(write,1);
									guo_tab5.invalidate();
								}
							});
							
							layout.addView(guo_tab5);
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
						View view = ((LayoutInflater) FramlyInsuranceActivity.this
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
								.inflate(R.layout.shua_card_layout, null);
						LayoutParams params = new LayoutParams(layout.getWidth(), layout.getHeight());
						view.setLayoutParams(params);
						layout.addView(view);
						Button button = (Button) FramlyInsuranceActivity.this
								.findViewById(R.id.confirm);
						break;
					case 2:
						layout.removeAllViews();
						View view1 = ((LayoutInflater) FramlyInsuranceActivity.this
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
								.inflate(R.layout.succes_layout, null);
						LayoutParams param = new LayoutParams(layout.getWidth(), layout.getHeight());
						view1.setLayoutParams(param);
						layout.addView(view1);
						Button btn = (Button) FramlyInsuranceActivity.this
								.findViewById(R.id.confirm);
						btn.setOnClickListener(new OnClickListener() {
		
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(FramlyInsuranceActivity.this,
										PropertyInsuranceMainActivity.class);
								FramlyInsuranceActivity.this.startActivity(intent);
								FramlyInsuranceActivity.this.finish();
							}
						});
		
						break;
					}
				}
			};
			
			public void payInfo(){
				back_tag = 3;
				tab_btn4.setBackgroundResource(R.drawable.bj_tag2);
				tab_btn2.setBackgroundResource(R.drawable.leftbuttonbg);
				tab_btn3.setBackgroundResource(R.drawable.leftbuttonbg);
				tab_btn1.setBackgroundResource(R.drawable.leftbuttonbg);
				tab_btn4.setTextColor(Color.parseColor("#FFFFFF"));
				tab_btn2.setTextColor(Color.parseColor("#808080"));
				tab_btn3.setTextColor(Color.parseColor("#808080"));
				tab_btn1.setTextColor(Color.parseColor("#808080"));
				
				layout = (LinearLayout)findViewById(R.id.liuxingxing);
				layout.removeAllViews();
				LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				LinearLayout  guo_tab6= (LinearLayout)lay.inflate(R.layout.pay_layout, null);
				
				LayoutParams params = new LayoutParams(layout.getWidth(), layout.getHeight());
				guo_tab6.setLayoutParams(params);
				layout.addView(guo_tab6);
				Button btn = (Button) guo_tab6.findViewById(R.id.zhifu);
				btn.setOnClickListener(new OnClickListener() {
		
					@Override
					public void onClick(View v) {
						layout.removeAllViews();
						View view = ((LayoutInflater) FramlyInsuranceActivity.this
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
								.inflate(R.layout.next1_layout, null);
						LayoutParams params = new LayoutParams(layout.getWidth(), layout.getHeight());
						view.setLayoutParams(params);
						layout.addView(view);
						Button view1 = (Button) FramlyInsuranceActivity.this
								.findViewById(R.id.carsh);
						view1.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								layout.removeAllViews();
								View view = ((LayoutInflater) FramlyInsuranceActivity.this
										.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
										.inflate(R.layout.wait_layout, null);
								LayoutParams params = new LayoutParams(layout.getWidth(), layout.getHeight());
								view.setLayoutParams(params);
								layout.addView(view);
								TextView view2 = (TextView) FramlyInsuranceActivity.this
										.findViewById(R.id.ka);
								view2.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										layout.removeAllViews();
										View view = ((LayoutInflater) FramlyInsuranceActivity.this
												.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
												.inflate(R.layout.shua_card_layout,
														null);
										LayoutParams params = new LayoutParams(layout.getWidth(), layout.getHeight());
										view.setLayoutParams(params);
										layout.addView(view);
										TextView view2 = (TextView) FramlyInsuranceActivity.this
												.findViewById(R.id.ka);
										view2.setOnClickListener(new OnClickListener() {
		
											@Override
											public void onClick(View v) {
												layout.removeAllViews();
												View view = ((LayoutInflater) FramlyInsuranceActivity.this
														.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
														.inflate(
																R.layout.confirm_layout,
																null);
												LayoutParams params = new LayoutParams(layout.getWidth(), layout.getHeight());
												view.setLayoutParams(params);
												layout.addView(view);
												Button button = (Button) FramlyInsuranceActivity.this
														.findViewById(R.id.confirm);
												Spinner s1 = (Spinner) FramlyInsuranceActivity.this
														.findViewById(R.id.s1);
												Spinner s2 = (Spinner) FramlyInsuranceActivity.this
														.findViewById(R.id.s2);
												ArrayAdapter<String> arrayadapter = new ArrayAdapter<String>(
														FramlyInsuranceActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														cardtype);
												s1.setAdapter(arrayadapter);
												arrayadapter = new ArrayAdapter<String>(
														FramlyInsuranceActivity.this,
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
