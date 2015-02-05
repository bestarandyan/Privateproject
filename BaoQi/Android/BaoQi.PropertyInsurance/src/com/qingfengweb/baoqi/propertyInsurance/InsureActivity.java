//package com.qingfengweb.baoqi.propertyInsurance;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import com.qingfengweb.baoqi.bean.TypeBean;
//import com.qingfengweb.baoqi.ext.AddTypeAdapter;
//import com.qingfengweb.baoqi.ext.TouBaoDanZhuangTaiAdapter;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnKeyListener;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListAdapter;
//import android.widget.Spinner;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class InsureActivity extends Activity implements OnClickListener {
//	private TextView mView1 = null;
//	private TextView mView2 = null;
//	private TextView mView3 = null;
//	private TextView mView4 = null;
//	List<Map<String, String>> tab_list, huancun_list;
//	private TextView mViewTitle1 = null;
//	private TextView mViewTitle2 = null;
//	private TextView mViewTitle3 = null;
//	private TextView mViewTitle4 = null;
//	private TextView mViewTitle5 = null;
//	private TextView mViewTitle6 = null;
//	private LinearLayout mLayout = null;
//	private TextView zhuantai_t = null;
//	private TextView toubaoT = null;
//	private EditText name_jihuashu;
//	private Button jixu;
//	private Button Lxx_next_btn;
//	private Spinner guoji_spinner;
//	private Spinner guoji_spinner2;
//	private Spinner spinner3;
//	private ArrayAdapter<CharSequence> adapter1;
//	private ArrayAdapter<CharSequence> adapter2;
//	LayoutInflater layout_new;
//	private WritePath write = null;
//	View view;
//	public ProgressDialog progressdialog;
//	public FrameLayout guo_tab5 = null;
//	public ImageView guo_image;
//     
//	private LinearLayout Lxx_right_top;
//	LinearLayout linear_right_bottom, linear_new, right_new, xiamian ,part2;
//	private ProgressDialog readWaitProgressDialog;
//
//	public void confirmShowWaitDialog() {
//		if (readWaitProgressDialog == null) {
//			readWaitProgressDialog = new ProgressDialog(this);
//			readWaitProgressDialog
//					.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			readWaitProgressDialog.setMessage("正在执行支付,请稍后...");
//			readWaitProgressDialog.setCancelable(true);
//			readWaitProgressDialog.show();
//		} else {
//			readWaitProgressDialog.show();
//		}
//	}
//
//	private ListView tab_listView, zhuangtai_listView, toubao_listView;
//	// LinearLayout linear_new,right_new,xiamian;
//	RelativeLayout pro_relative, zhuangtai, toubaodan;
//	PAdapter padapter;
//	TouBaoDanZhuangTaiAdapter zhuangtaiAdapter, toubaoAdapter;
//	private Button query_jihushu, fuzhi, shuxin, chaxun, jiaofei;
//	private Button toubao_query, toubao_shuaxin;
//
//	public Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 1:
//				if (readWaitProgressDialog != null) {
//					readWaitProgressDialog.dismiss();
//				}
//				View view = ((LayoutInflater) InsureActivity.this
//						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//						.inflate(R.layout.shua_card_layout, null);
//				mLayout.addView(view);
//				Button button = (Button) InsureActivity.this
//						.findViewById(R.id.confirm);
//				break;
//			case 2:
//				mLayout.removeAllViews();
//				View view1 = ((LayoutInflater) InsureActivity.this
//						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//						.inflate(R.layout.succes_layout, null);
//				mLayout.addView(view1);
//				Button btn = (Button) InsureActivity.this
//						.findViewById(R.id.confirm);
//				btn.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						Intent intent = new Intent(InsureActivity.this,
//								PropertyInsuranceMainActivity.class);
//						InsureActivity.this.startActivity(intent);
//						InsureActivity.this.finish();
//					}
//				});
//
//				break;
//			}
//		}
//	};
//
//	List<TypeBean> list = new ArrayList<TypeBean>();
//
//	public List<TypeBean> getList() {
//		Random random = new Random();
//		TypeBean bean = new TypeBean();
//		bean.setBf("1" + random.nextInt(9) + "w");
//		bean.setCode(random.nextInt(9) + "" + random.nextInt(9) + ""
//				+ random.nextInt(9) + random.nextInt(9));
//		bean.setEbf(random.nextInt(9) + "" + random.nextInt(9) + ""
//				+ random.nextInt(9) + random.nextInt(9));
//		if (list.size() % 2 == 0) {
//			bean.setFs("年交");
//		} else {
//			bean.setFs("一次交付");
//		}
//		if (random.nextInt(9) < 3) {
//			bean.setName("分红险");
//		} else if (random.nextInt(9) < 5) {
//			bean.setName("保障险");
//		} else if (random.nextInt(9) < 7) {
//			bean.setName("两全险");
//		} else {
//			bean.setName("意外险");
//		}
//		bean.setNumber("1" + random.nextInt(9) + random.nextInt(9)
//				+ random.nextInt(9) + random.nextInt(9));
//		bean.setYear("1" + random.nextInt(9));
//		list.add(bean);
//		return list;
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.insure_layout);
//		mView1 = (TextView) this.findViewById(R.id.bulidIsure);
//		mView2 = (TextView) this.findViewById(R.id.maintainbook);
//		mView3 = (TextView) this.findViewById(R.id.searchcard);
//		mView4 = (TextView) this.findViewById(R.id.searchcardback);
//		mView1.setOnClickListener(this);
//		mView2.setOnClickListener(this);
//		mView3.setOnClickListener(this);
//		mView4.setOnClickListener(this);
//		// right_new = (LinearLayout) findViewById(R.id.Ptab2);
//		 part2=(LinearLayout) findViewById(R.id.Ptab2);
//		right_new = (LinearLayout) findViewById(R.id.contents);
//		mViewTitle1 = (TextView) this.findViewById(R.id.cusinfo);
//		mViewTitle2 = (TextView) this.findViewById(R.id.insureItem);
//		mViewTitle3 = (TextView) this.findViewById(R.id.auth);
//		mViewTitle4 = (TextView) this.findViewById(R.id.notify);
//		mViewTitle5 = (TextView) this.findViewById(R.id.payinfo);
//		mViewTitle6 = (TextView) this.findViewById(R.id.sure);
//		mViewTitle1.setOnClickListener(this);
//		mViewTitle2.setOnClickListener(this);
//		mViewTitle3.setOnClickListener(this);
//		mViewTitle4.setOnClickListener(this);
//		mViewTitle5.setOnClickListener(this);
//		mViewTitle6.setOnClickListener(this);
//		mLayout = (LinearLayout) this.findViewById(R.id.contents);
//		LxxFun();
//		MaLongFun();
//		progressdialog = new ProgressDialog(this);
//		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		progressdialog.setOnKeyListener(new OnKeyListener() {
//
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
//				if (KeyEvent.KEYCODE_BACK == keyCode) {
//				}
//				return true;
//			}
//
//		});
//	}
//
//	private void LxxFun() {
//		tab_list = new ArrayList<Map<String, String>>();
//		huancun_list = new ArrayList<Map<String, String>>();
//		Map<String, String> map1 = new HashMap<String, String>();
//		map1.put("jihuashu", "1");
//		map1.put("time", "刘星星");
//		map1.put("toubaoren", "梁先生");
//		map1.put("nianling", "2012-2-2");
//		map1.put("beibaoren", "30000");
//		map1.put("beibaonianling", "未上传");
//		Map<String, String> map2 = new HashMap<String, String>();
//		map2.put("jihuashu", "2");
//		map2.put("time", "武国庆");
//		map2.put("toubaoren", "刘星星");
//		map2.put("nianling", "2012-3-4");
//		map2.put("beibaoren", "3333");
//		map2.put("beibaonianling", "已上传");
//		Map<String, String> map3 = new HashMap<String, String>();
//		map3.put("jihuashu", "3");
//		map3.put("time", "马龙");
//		map3.put("toubaoren", "毛豆豆");
//		map3.put("nianling", "2012-3-6");
//		map3.put("beibaoren", "222");
//		map3.put("beibaonianling", "未上传");
//		Map<String, String> map4 = new HashMap<String, String>();
//		map4.put("jihuashu", "4");
//		map4.put("time", "谢先生");
//		map4.put("toubaoren", "翟先生");
//		map4.put("nianling", "2012-3-15");
//		map4.put("beibaoren", "555");
//		map4.put("beibaonianling", "已上传");
//		tab_list.add(map1);
//		tab_list.add(map2);
//		tab_list.add(map3);
//		tab_list.add(map4);
//		huancun_list.add(map1);
//		huancun_list.add(map2);
//		huancun_list.add(map3);
//		huancun_list.add(map4);
//
//		layout_new = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//		linear_new = (LinearLayout) layout_new.inflate(R.layout.new_toubaoren,
//				null);
//
//		layout_new = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//		linear_new = (LinearLayout) layout_new.inflate(R.layout.new_toubaoren,
//				null);
//		pro_relative = (RelativeLayout) layout_new.inflate(
//				R.layout.protect_toubaoshu, null);
//		zhuangtai = (RelativeLayout) layout_new.inflate(
//				R.layout.zhuangtai_query, null);
//		toubaodan = (RelativeLayout) layout_new.inflate(
//				R.layout.toubaodan_back, null);
//		Lxx_right_top = (LinearLayout) findViewById(R.id.right);
//		jixu = (Button) pro_relative.findViewById(R.id.jixu);
//		guoji_spinner=(Spinner) linear_new.findViewById(R.id.guoji1);
//		guoji_spinner2=(Spinner) linear_new.findViewById(R.id.guoji_spinner2);
//		spinner3=(Spinner) linear_new.findViewById(R.id.spinner3);
//		adapter1=ArrayAdapter.createFromResource(getApplicationContext(), R.array.guoji, android.R.layout.simple_spinner_item);
//		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		adapter2=ArrayAdapter.createFromResource(getApplicationContext(), R.array.guanxi, android.R.layout.simple_spinner_item);
//		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinner3.setAdapter(adapter2);
//		guoji_spinner.setAdapter(adapter1);
//		guoji_spinner2.setAdapter(adapter1);
//		Lxx_next_btn = (Button) linear_new.findViewById(R.id.new_next);
//		Lxx_next_btn.setOnClickListener(new JixuListener());
//		jixu.setOnClickListener(new JixuListener());
//		toubao_query = (Button) toubaodan.findViewById(R.id.query_jihua);
//		toubao_shuaxin = (Button) toubaodan.findViewById(R.id.shuaxin);
//		toubaoT = (TextView) toubaodan.findViewById(R.id.textView);
//		toubao_query.setOnClickListener(new taobaoQListener());
//		toubao_shuaxin.setOnClickListener(new touBaoSListener());
//		zhuangtai_listView = (ListView) zhuangtai.findViewById(R.id.tab_list);
//		toubao_listView = (ListView) toubaodan.findViewById(R.id.tab_list);
//		zhuantai_t = (TextView) zhuangtai.findViewById(R.id.textView);
//		fuzhi = (Button) pro_relative.findViewById(R.id.fuzhi);
//		shuxin = (Button) zhuangtai.findViewById(R.id.shuaxin);
//		chaxun = (Button) zhuangtai.findViewById(R.id.query_jihua);
//		jiaofei = (Button) zhuangtai.findViewById(R.id.jiaofei);
//		xiamian = (LinearLayout) pro_relative.findViewById(R.id.xiamian);
//		name_jihuashu = (EditText) pro_relative
//				.findViewById(R.id.name_jihuashu);
//		fuzhi.setOnClickListener(new BtnCopyListener());
//		right_new.removeAllViews();
//		right_new.addView(linear_new);
//		tab_listView = (ListView) pro_relative.findViewById(R.id.tab_list);
//		query_jihushu = (Button) pro_relative.findViewById(R.id.query_jihua);
//		padapter = new PAdapter(tab_list);
//		tab_listView.setAdapter(padapter);
//		tab_listView.setCacheColorHint(0);
//		query_jihushu.setOnClickListener(new QueryJiHuaShuListener());
//		zhuangtaiAdapter = new TouBaoDanZhuangTaiAdapter(layout_new);
//		zhuangtai_listView.setAdapter(zhuangtaiAdapter);
//		zhuangtai_listView.setCacheColorHint(0);
//		toubaoAdapter = new TouBaoDanZhuangTaiAdapter(layout_new);
//		toubao_listView.setAdapter(zhuangtaiAdapter);
//		toubao_listView.setCacheColorHint(0);
//		shuxin.setOnClickListener(new ShuaXinListener());
//		chaxun.setOnClickListener(new ChaXunListener());
//		jiaofei.setOnClickListener(new JiaoFeiListener());
//	}
//
//	public void reset() {
//
//		mViewTitle2.setBackgroundResource(R.drawable.single_tag01_on);
//		mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle4.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
//		mLayout.removeAllViews();
//
//		View view = ((LayoutInflater) InsureActivity.this
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
//				R.layout.item_layout, null);
//		mLayout.addView(view);
//		Spinner s1 = (Spinner) InsureActivity.this.findViewById(R.id.s1);
//		Spinner s2 = (Spinner) InsureActivity.this.findViewById(R.id.s2);
//		Spinner s3 = (Spinner) InsureActivity.this.findViewById(R.id.s3);
//		Spinner s4 = (Spinner) InsureActivity.this.findViewById(R.id.s4);
//		Spinner s5 = (Spinner) InsureActivity.this.findViewById(R.id.s5);
//		Spinner s6 = (Spinner) InsureActivity.this.findViewById(R.id.s6);
//		Spinner s7 = (Spinner) InsureActivity.this.findViewById(R.id.s7);
//		Spinner s8 = (Spinner) InsureActivity.this.findViewById(R.id.s8);
//		Spinner s9 = (Spinner) InsureActivity.this.findViewById(R.id.s9);
//		Spinner s10 = (Spinner) InsureActivity.this.findViewById(R.id.s10);
//		// Spinner s11 = (Spinner) this.findViewById(R.id.s11);
//		ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//				InsureActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str1);
//		s1.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str32);
//		s2.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str32);
//		s3.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str4);
//		s4.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str52);
//		s5.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str51);
//		s6.setAdapter(arrayadapter);
//
//		arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str52);
//		s7.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str53);
//		s8.setAdapter(arrayadapter);
//
//		arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str52);
//		s9.setAdapter(arrayadapter);
//
//		arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
//				android.R.layout.simple_dropdown_item_1line, str54);
//		s10.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
//				android.R.layout.simple_dropdown_item_1line,
//				new String[] { "纸质保单" });
//		// s11.setAdapter(arrayadapter);
//		final ListView mlistview = (ListView) InsureActivity.this
//				.findViewById(R.id.mlistview);
//
//		Button btn = (Button) InsureActivity.this.findViewById(R.id.liyi_btn1);
//		btn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				AddTypeAdapter madapter = new AddTypeAdapter(
//						InsureActivity.this, getList());
//				mlistview.setAdapter(madapter);
//				madapter.notifyDataSetChanged();
//			}
//
//		});
//		Button btn2 = (Button) InsureActivity.this.findViewById(R.id.liyi_btn2);
//		btn2.setOnClickListener(new OnClickListener() {// next投保事项
//			@Override
//			public void onClick(View v) {
//				touBao();
//			}
//		});
//	}
//
//	public void forwardAccount() {// 转账授权
//		mLayout.removeAllViews();
//		View view = ((LayoutInflater) this
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
//				R.layout.auth_data_layout, null);
//		mLayout.addView(view);
//		Spinner s1 = (Spinner) this.findViewById(R.id.s1);
//		Spinner s2 = (Spinner) this.findViewById(R.id.s2);
//		Spinner s3 = (Spinner) this.findViewById(R.id.s3);
//		Spinner s4 = (Spinner) this.findViewById(R.id.s4);
//		Spinner s5 = (Spinner) this.findViewById(R.id.s5);
//		Spinner s6 = (Spinner) this.findViewById(R.id.s6);
//		ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, bank);
//		s1.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, type);
//		s2.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, bank);
//		s3.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, type);
//		s4.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, bank);
//		s5.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, type);
//		s6.setAdapter(arrayadapter);
//
//		Button btn = (Button) this.findViewById(R.id.liyi_btn1);
//		btn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {// 相关告知
//				InsureActivity.this.guo_tab4();
////				mViewTitle1 = (TextView) InsureActivity.this
////						.findViewById(R.id.cusinfo);
////				mViewTitle2 = (TextView) InsureActivity.this
////						.findViewById(R.id.insureItem);
////				mViewTitle3 = (TextView) InsureActivity.this
////						.findViewById(R.id.auth);
////				mViewTitle4 = (TextView) InsureActivity.this
////						.findViewById(R.id.notify);
////				mViewTitle5 = (TextView) InsureActivity.this
////						.findViewById(R.id.payinfo);
////				mViewTitle6 = (TextView) InsureActivity.this
////						.findViewById(R.id.sure);
////				mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////				mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
////				mViewTitle4.setBackgroundResource(R.drawable.single_tag01_on);
////				mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
////				mViewTitle6.setBackgroundResource(R.drawable.single_tag01);
////				mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
////				LinearLayout contents = (LinearLayout) findViewById(R.id.contents);
////				contents.removeAllViews();
////				mLayout.removeAllViews();
////				mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////				mViewTitle4.setBackgroundResource(R.drawable.single_tag01_on);
////				mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
////				mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
////				// mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////				mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
////				LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
////				LinearLayout guo_tab4 = (LinearLayout) lay.inflate(
////						R.layout.guo_tab4, null);
////				String[] str1 = { "一般职员", "医生", "护士", "清洁工", "老师", "部门经理" };
////				String[] str2 = { "工薪", "福利", "提成", "外快", "加班费", "奖金" };
////				Spinner guo_s1 = (Spinner) guo_tab4.findViewById(R.id.guo_s1);
////				Spinner guo_s2 = (Spinner) guo_tab4.findViewById(R.id.guo_s2);
////				Spinner guo_s3 = (Spinner) guo_tab4.findViewById(R.id.guo_s3);
////				Spinner guo_s4 = (Spinner) guo_tab4.findViewById(R.id.guo_s4);
////				guo_s1.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
////						android.R.layout.simple_dropdown_item_1line, str1));
////				guo_s2.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
////						android.R.layout.simple_dropdown_item_1line, str2));
////				guo_s3.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
////						android.R.layout.simple_dropdown_item_1line, str1));
////				guo_s4.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
////						android.R.layout.simple_dropdown_item_1line, str2));
////				contents.addView(guo_tab4);
//			}
//		});
//
//		mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle3.setBackgroundResource(R.drawable.single_tag01_on);
//		mViewTitle4.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle6.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
//	}
//
//	public void XGnotify(){
//					LinearLayout contents = (LinearLayout) findViewById(R.id.contents);
//					contents.removeAllViews();
//					mLayout.removeAllViews();
//					mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
//					mViewTitle4.setBackgroundResource(R.drawable.single_tag01_on);
//					mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
//					mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
//					mViewTitle6.setBackgroundResource(R.drawable.single_tag01);
//					mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
//					LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//					LinearLayout guo_tab4 = (LinearLayout) lay.inflate(
//							R.layout.guo_tab4, null);
//					String[] str1 = { "一般职员", "医生", "护士", "清洁工", "老师", "部门经理" };
//					String[] str2 = { "工薪", "福利", "提成", "外快", "加班费", "奖金" };
//					Spinner guo_s1 = (Spinner) guo_tab4.findViewById(R.id.guo_s1);
//					Spinner guo_s2 = (Spinner) guo_tab4.findViewById(R.id.guo_s2);
//					Spinner guo_s3 = (Spinner) guo_tab4.findViewById(R.id.guo_s3);
//					Spinner guo_s4 = (Spinner) guo_tab4.findViewById(R.id.guo_s4);
//					guo_s1.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str1));
//					guo_s2.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str2));
//					guo_s3.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str1));
//					guo_s4.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
//							android.R.layout.simple_dropdown_item_1line, str2));
//					contents.addView(guo_tab4);
//					Button button = (Button) guo_tab4.findViewById(R.id.next);
//					button.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//
//							// 确认投保
//							LinearLayout contents = (LinearLayout) findViewById(R.id.Ptab2);
//							contents.removeAllViews();
//							LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//							guo_tab5 = (FrameLayout) lay.inflate(R.layout.guo_tab5,
//									null);
//							final LinearLayout guoLayout1 = (LinearLayout) guo_tab5
//									.findViewById(R.id.guo_linear1);
//							final LinearLayout guobtnlayout = (LinearLayout) guo_tab5
//									.findViewById(R.id.guo_btnlayout);
//
//							guoLayout1.setVisibility(View.VISIBLE);
//							guobtnlayout.setVisibility(View.INVISIBLE);
//
//							Button guo_btn1 = (Button) guo_tab5
//									.findViewById(R.id.guo_btn1);
//							Button guo_btn2 = (Button) guo_tab5
//									.findViewById(R.id.guo_btn2);
//							Button guo_btn3 = (Button) guo_tab5
//									.findViewById(R.id.guo_btn3);
//							Button guo_btn4 = (Button) guo_tab5
//									.findViewById(R.id.guo_btn4);
//							guo_image = (ImageView) guo_tab5
//									.findViewById(R.id.guo_image);
//
//							guo_btn1.setOnClickListener(new View.OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									guoLayout1.setVisibility(View.INVISIBLE);
//									guo_tab5.setFocusable(true);
//									guobtnlayout.setVisibility(View.VISIBLE);
//									guo_tab5.addView(
//											new WritePath(InsureActivity.this), 1);
//									guo_tab5.invalidate();
//
//								}
//							});
//							guo_btn2.setOnClickListener(new View.OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//
//									mLayout.removeAllViews();
//
//									// View view = ((LayoutInflater) this
//									// .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//									// .inflate(R.layout.pay_layout, null);
//									// mLayout.addView(view);
//									// Button btn = (Button)
//									// this.findViewById(R.id.zhifu);
//									// btn.setOnClickListener(new OnClickListener() {
//
//									View view = ((LayoutInflater) InsureActivity.this
//											.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//											.inflate(R.layout.pay_layout, null);
//									mLayout.removeAllViews();
//									mViewTitle2
//											.setBackgroundResource(R.drawable.single_tag01);
//									mViewTitle5
//											.setBackgroundResource(R.drawable.single_tag01_on);
//									mViewTitle4
//											.setBackgroundResource(R.drawable.single_tag01);
//									mViewTitle3
//											.setBackgroundResource(R.drawable.single_tag01);
//									// mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
//									mViewTitle1
//											.setBackgroundResource(R.drawable.single_tag01);
//									mLayout.addView(view);
//									Button btn = (Button) InsureActivity.this
//											.findViewById(R.id.zhifu);
//									btn.setOnClickListener(new OnClickListener() {
//
//										@Override
//										public void onClick(View v) {
//											mLayout.removeAllViews();
//											View view = ((LayoutInflater) InsureActivity.this
//													.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//													.inflate(R.layout.next1_layout,
//															null);
//											mLayout.addView(view);
//											Button view1 = (Button) InsureActivity.this
//													.findViewById(R.id.carsh);
//											view1.setOnClickListener(new OnClickListener() {
//												@Override
//												public void onClick(View v) {
//													mLayout.removeAllViews();
//													View view = ((LayoutInflater) InsureActivity.this
//															.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//															.inflate(
//																	R.layout.wait_layout,
//																	null);
//													mLayout.addView(view);
//													TextView view2 = (TextView) InsureActivity.this
//															.findViewById(R.id.ka);
//													view2.setOnClickListener(new OnClickListener() {
//														@Override
//														public void onClick(View v) {
//															mLayout.removeAllViews();
//															View view = ((LayoutInflater) InsureActivity.this
//																	.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//																	.inflate(
//																			R.layout.shua_card_layout,
//																			null);
//															mLayout.addView(view);
//															TextView view2 = (TextView) InsureActivity.this
//																	.findViewById(R.id.ka);
//															view2.setOnClickListener(new OnClickListener() {
//
//																@Override
//																public void onClick(
//																		View v) {
//																	mLayout.removeAllViews();
//																	View view = ((LayoutInflater) InsureActivity.this
//																			.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//																			.inflate(
//																					R.layout.confirm_layout,
//																					null);
//																	mLayout.addView(view);
//																	Button button = (Button) InsureActivity.this
//																			.findViewById(R.id.confirm);
//																	Spinner s1 = (Spinner) InsureActivity.this
//																			.findViewById(R.id.s1);
//																	Spinner s2 = (Spinner) InsureActivity.this
//																			.findViewById(R.id.s2);
//																	ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//																			InsureActivity.this,
//																			android.R.layout.simple_dropdown_item_1line,
//																			cardtype);
//																	s1.setAdapter(arrayadapter);
//																	arrayadapter = new ArrayAdapter<String>(
//																			InsureActivity.this,
//																			android.R.layout.simple_dropdown_item_1line,
//																			bank);
//																	s2.setAdapter(arrayadapter);
//																	button.setOnClickListener(new OnClickListener() {
//																		@Override
//																		public void onClick(
//																				View v) {
//																			new Thread() {
//																				public void run() {
//																					try {
//																						sleep(3000);
//																						handler.sendEmptyMessage(1);
//																						handler.sendEmptyMessage(2);
//																					} catch (InterruptedException e) {
//																						e.printStackTrace();
//																					}
//																				}
//																			}.start();
//																			confirmShowWaitDialog();
//																		}
//																	});
//																}
//
//															});
//														}
//													});
//												}
//											});
//										}
//									});
//
//								}
//							});
//							guo_btn3.setOnClickListener(new View.OnClickListener() {
//								private String getPhotoFileName() {
//									Date date = new Date(System.currentTimeMillis());
//									SimpleDateFormat dateFormat = new SimpleDateFormat(
//											"'IMG'_yyyyMMdd_HHmmss");
//									return dateFormat.format(date) + ".jpg";
//								}
//
//								@Override
//								public void onClick(View v) {
//									guo_tab5.getChildAt(1).setDrawingCacheEnabled(true);
//									guo_tab5.getChildAt(1).buildDrawingCache();
//									Bitmap b = guo_tab5.getChildAt(1).getDrawingCache();
//									MyApplication.getInstance().setBitmap1(b);
//									File mCurrentPhotoFile;
//									String SCREEN_SHOTS_LOCATION = Environment
//											.getExternalStorageDirectory()
//											+ "/baoqi/baodan";
//									File f = new File(SCREEN_SHOTS_LOCATION);
//									f.mkdirs();// 创建照片的存储目录
//									mCurrentPhotoFile = new File(f, getPhotoFileName());// 给新照的照片文件命名
//									MyApplication.getInstance().setmCurrentPhotoFile(
//											mCurrentPhotoFile);
//									// String fileName = "sbsc" +
//									// System.currentTimeMillis() + ".png";
//									Bitmap bitmap = BitmapFactory
//											.decodeStream(InsureActivity.this
//													.getResources().openRawResource(
//															R.drawable.insure_viedw_tu));
//									MyApplication.getInstance().setBitmap2(bitmap);
//
//									CanvasWriteActivity canvas = new CanvasWriteActivity(
//											MyApplication.getInstance().getBitmap2(),
//											MyApplication.getInstance().getBitmap1(),
//											mCurrentPhotoFile, InsureActivity.this);
//									Thread thread = new Thread(canvas);
//									thread.start();
//
//									guoLayout1.setVisibility(View.VISIBLE);
//									guobtnlayout.setVisibility(View.INVISIBLE);
//
//									progressdialog.setMessage("系统正在处理中，请稍等");
//									progressdialog.show();
//
//									guo_tab5.setVisibility(View.INVISIBLE);
//								}
//							});
//							guo_btn4.setOnClickListener(new View.OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									guo_tab5.removeViewAt(1);
//									guo_tab5.addView(
//											new WritePath(InsureActivity.this), 1);
//									guo_tab5.invalidate();
//								}
//							});
//
//							contents.addView(guo_tab5);
//						}
//					});				
//	}
//	
//	public void guo_tab4(){
//		
//		mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle4.setBackgroundResource(R.drawable.single_tag01_on);
//		mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle6.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
//		
//		String[] str1 = {
//				"一般职员","医生","护士","清洁工"
//				,"老师","部门经理"
//		};
//		String[] str2 = {
//			"工薪","福利","提成",
//			"外快","加班费","奖金"
//		};
//		LinearLayout contents = (LinearLayout)findViewById(R.id.contents);
//		contents.removeAllViews();
//		LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//		LinearLayout guo_tab4 = (LinearLayout)lay.inflate(R.layout.guo_tab4, null);
//		Spinner guo_s1 = (Spinner) guo_tab4.findViewById(R.id.guo_s1);
//		Spinner guo_s2 = (Spinner) guo_tab4.findViewById(R.id.guo_s2);
//		Spinner guo_s3 = (Spinner) guo_tab4.findViewById(R.id.guo_s3);
//		Spinner guo_s4 = (Spinner) guo_tab4.findViewById(R.id.guo_s4);
//		
//		guo_s1.setAdapter(new ArrayAdapter<String>(InsureActivity.this, android.R.layout.simple_spinner_item,str1));
//		guo_s2.setAdapter(new ArrayAdapter<String>(InsureActivity.this, android.R.layout.simple_spinner_item,str2));
//		guo_s3.setAdapter(new ArrayAdapter<String>(InsureActivity.this, android.R.layout.simple_spinner_item,str1));
//		guo_s4.setAdapter(new ArrayAdapter<String>(InsureActivity.this, android.R.layout.simple_spinner_item,str2));
//		Button button = (Button) guo_tab4.findViewById(R.id.next);
//		button.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//		 InsureActivity.this.guo_tab5();
//			}
//			});
//		contents.addView(guo_tab4);
//	}
//	 LinearLayout contents = null;
//	// 确认投保
//	public void guo_tab5(){
//		
//		contents = (LinearLayout)findViewById(R.id.Ptab3);
//		contents.setVisibility(View.VISIBLE);
//		contents.removeAllViews();
//		part2.setVisibility(View.GONE);
//		LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//		guo_tab5 = (FrameLayout)lay.inflate(R.layout.guo_tab5, null);
//		final LinearLayout guoLayout1 = (LinearLayout)guo_tab5.findViewById(R.id.guo_linear1);
//		final LinearLayout guobtnlayout = (LinearLayout)guo_tab5.findViewById(R.id.guo_btnlayout);
//					
//		guoLayout1.setVisibility(View.VISIBLE);
//		guobtnlayout.setVisibility(View.INVISIBLE);
//					
//					Button guo_btn1 =(Button)guo_tab5.findViewById(R.id.guo_btn1);
//					Button guo_btn2 =(Button)guo_tab5.findViewById(R.id.guo_btn2);
//					Button guo_btn3 =(Button)guo_tab5.findViewById(R.id.guo_btn3);			
//					Button guo_btn4 =(Button)guo_tab5.findViewById(R.id.guo_btn4);
//					guo_image =(ImageView)guo_tab5.findViewById(R.id.guo_image);
//				
//					guo_btn1.setOnClickListener(new View.OnClickListener() {																		
//						@Override
//						public void onClick(View v) {
//							guoLayout1.setVisibility(View.INVISIBLE);
//							guo_tab5.requestFocus();
//							guobtnlayout.setVisibility(View.VISIBLE);
////							WritePath write = new WritePath(InsureActivity.this);
////							write.setFocusable(true);
//							if(write!=null){
//								write.clearFocus();
//								write =null;
//							}
//							write = new WritePath(InsureActivity.this);
//							guo_tab5.addView(write,1);
//							guo_tab5.invalidate();
//							
//						}
//					});
//					guo_btn2.setOnClickListener(new View.OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							part2.setVisibility(View.VISIBLE);
//							contents.setVisibility(View.GONE);
////							part2.removeAllViews();
////							LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
////							part2.addView((LinearLayout)lay.inflate(R.layout.sub_layout, null));
////							mLayout=(LinearLayout) part2.findViewById(R.id.contents);
////							right_new = (LinearLayout)  part2.findViewById(R.id.contents);
////							mViewTitle1 = (TextView) InsureActivity.this.findViewById(R.id.cusinfo);
////							mViewTitle2 = (TextView) InsureActivity.this.findViewById(R.id.insureItem);
////							mViewTitle3 = (TextView) InsureActivity.this.findViewById(R.id.auth);
////							mViewTitle4 = (TextView) InsureActivity.this.findViewById(R.id.notify);
////							mViewTitle5 = (TextView) InsureActivity.this.findViewById(R.id.payinfo);
////							mViewTitle6 = (TextView) InsureActivity.this.findViewById(R.id.sure);
////							mViewTitle1.setOnClickListener(InsureActivity.this);
////							mViewTitle2.setOnClickListener(InsureActivity.this);
////							mViewTitle3.setOnClickListener(InsureActivity.this);
////							mViewTitle4.setOnClickListener(InsureActivity.this);
////							mViewTitle5.setOnClickListener(InsureActivity.this);
////							mViewTitle6.setOnClickListener(InsureActivity.this);
//							InsureActivity.this.payInfo();
//						}
//					});
//					guo_btn3.setOnClickListener(new View.OnClickListener() {
//			
//						private String getPhotoFileName() {  
//					        Date date = new Date(System.currentTimeMillis());  
//					        SimpleDateFormat dateFormat = new SimpleDateFormat(  
//					                "'IMG'_yyyyMMdd_HHmmss");  
//					        return dateFormat.format(date) + ".jpg";  
//					    } 
//							@Override
//							public void onClick(View v) {
//								guo_tab5.getChildAt(1).setDrawingCacheEnabled(true);
//								guo_tab5.getChildAt(1).buildDrawingCache();
//								Bitmap b = guo_tab5.getChildAt(1).getDrawingCache();
//								MyApplication.getInstance().setBitmap1(b);
//								File mCurrentPhotoFile;//照相机拍照得到的图片 
//								String SCREEN_SHOTS_LOCATION = Environment
//										.getExternalStorageDirectory()
//										+ "/baoqi/baodan";
//								File f = new File(SCREEN_SHOTS_LOCATION);
//					            f.mkdirs();// 创建照片的存储目录  
//					            mCurrentPhotoFile = new File(f, getPhotoFileName());// 给新照的照片文件命名  
//					            MyApplication.getInstance().setmCurrentPhotoFile(mCurrentPhotoFile);
////								String fileName = "sbsc" + System.currentTimeMillis() + ".png";
//								Bitmap bitmap = BitmapFactory.decodeStream(InsureActivity.this.getResources()
//										.openRawResource(R.drawable.insure_viedw_tu));
//								MyApplication.getInstance().setBitmap2(bitmap);
//								
//								CanvasWriteActivity canvas = new CanvasWriteActivity(MyApplication.getInstance().getBitmap2(), MyApplication.getInstance().getBitmap1(), mCurrentPhotoFile, InsureActivity.this);
//								Thread thread = new Thread(canvas);
//								thread.start();
//								
//								guoLayout1.setVisibility(View.VISIBLE);
//								guobtnlayout.setVisibility(View.INVISIBLE);
//								
//								progressdialog.setMessage("系统正在处理中，请稍等");
//					            progressdialog.show();
//								
//					            guo_tab5.setVisibility(View.INVISIBLE);
//							}
//					});
//					guo_btn4.setOnClickListener(new View.OnClickListener() {
//			
//						@Override
//						public void onClick(View v) {
//							guo_tab5.removeViewAt(1);
//							if(write!=null){
//								write.clearFocus();
//								write =null;
//							}
//							write = new WritePath(InsureActivity.this);
//							guo_tab5.addView(write,1);
//							guo_tab5.invalidate();
//						}
//					});
//					
//					contents.addView(guo_tab5);
//	}
//	
//	public void payInfo(){
//		
//		
//		mLayout.removeAllViews();
//		View view = ((LayoutInflater) InsureActivity.this
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//				.inflate(R.layout.pay_layout, null);
//		mLayout.removeAllViews();
//		mLayout.addView(view);
//		mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle5.setBackgroundResource(R.drawable.single_tag01_on);
//		mViewTitle4.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
//		// mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
//		Button btn = (Button) mLayout.findViewById(R.id.zhifu);
//		btn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				mLayout.removeAllViews();
//				View view = ((LayoutInflater) InsureActivity.this
//						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//						.inflate(R.layout.next1_layout, null);
//				mLayout.addView(view);
//				Button view1 = (Button) InsureActivity.this
//						.findViewById(R.id.carsh);
//				view1.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						mLayout.removeAllViews();
//						View view = ((LayoutInflater) InsureActivity.this
//								.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//								.inflate(R.layout.wait_layout, null);
//						mLayout.addView(view);
//						TextView view2 = (TextView) InsureActivity.this
//								.findViewById(R.id.ka);
//						view2.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								mLayout.removeAllViews();
//								View view = ((LayoutInflater) InsureActivity.this
//										.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//										.inflate(R.layout.shua_card_layout,
//												null);
//								mLayout.addView(view);
//								TextView view2 = (TextView) InsureActivity.this
//										.findViewById(R.id.ka);
//								view2.setOnClickListener(new OnClickListener() {
//
//									@Override
//									public void onClick(View v) {
//										mLayout.removeAllViews();
//										View view = ((LayoutInflater) InsureActivity.this
//												.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//												.inflate(
//														R.layout.confirm_layout,
//														null);
//										mLayout.addView(view);
//										Button button = (Button) InsureActivity.this
//												.findViewById(R.id.confirm);
//										Spinner s1 = (Spinner) InsureActivity.this
//												.findViewById(R.id.s1);
//										Spinner s2 = (Spinner) InsureActivity.this
//												.findViewById(R.id.s2);
//										ArrayAdapter arrayadapter = new ArrayAdapter<String>(
//												InsureActivity.this,
//												android.R.layout.simple_dropdown_item_1line,
//												cardtype);
//										s1.setAdapter(arrayadapter);
//										arrayadapter = new ArrayAdapter<String>(
//												InsureActivity.this,
//												android.R.layout.simple_dropdown_item_1line,
//												bank);
//										s2.setAdapter(arrayadapter);
//										button.setOnClickListener(new OnClickListener() {
//											@Override
//											public void onClick(View v) {
//												new Thread() {
//													public void run() {
//														try {
//															sleep(3000);
//															handler.sendEmptyMessage(1);
//															handler.sendEmptyMessage(2);
//														} catch (InterruptedException e) {
//															e.printStackTrace();
//														}
//													}
//												}.start();
//												confirmShowWaitDialog();
//											}
//										});
//									}
//
//								});
//							}
//						});
//					}
//				});
//			}
//		});
//	}
//	
//	
//	class JixuListener implements View.OnClickListener {
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			if (v == jixu) {
//				right_new.removeAllViews();
//				right_new.addView(linear_new);
//			} else if (v == Lxx_next_btn) {//到投保
//				InsureActivity.this.touBao();
//				
//				
//				
//			}
//		}
//	}
//
//	private String[] str1 = { "(分红险)", "(万能险)", "(投连险)", "(少儿险)", "(健康险)",
//			"(养老险)", "(保障险)", "(两全险)", "(意外险)", "(附加险)", "(连生险)", "(团体寿险)",
//			"(机构代理寿险)" };
//	private String[] str32 = { "国寿福禄鑫尊两全保险", "国寿安欣无忧两全保险", "国国寿安欣无忧长期意外伤害保险",
//			"国寿附加安欣无忧提前给付重大疾病保险", "国寿附加绿荫呵护少儿重大疾病保险", "国寿绿荫呵护少儿两全保险",
//			"国寿松鹤颐年年金保险", "国寿美满人生至尊版年金保险", "国寿福禄宝宝两全保险", "国寿福满一生两全保险",
//			"国寿附加重大疾病保险", "国寿福禄呈祥两全保险", "国寿福禄金尊两全保险", "国寿新鸿泰两全保险",
//			"国寿附加康友重大疾病保险", "国寿安鑫保险计划", "国寿育才少儿两全保险", "国寿新鸿泰金典版两全保险",
//			"国寿福禄尊享两全保险", "国寿福瑞人生两全保险", "国寿附加长久呵护意外伤害保险", "国寿长久呵护住院费用补偿医疗保险",
//			"国寿长久呵护意外伤害费用补偿医疗保险", "国寿长久呵护意外伤害定额给付医疗保险", "国寿福禄满堂养老年金保险" };
//	private String[] str52 = { "5年", "10年", "15年" };
//	private String[] str51 = { "一次性", "分期" };
//	private String[] str53 = { "月领", "年领" };
//	private String[] str50 = { "50岁", "55岁", "60岁" };
//	private String[] str54 = { "人民币", "美元" };
//	private String[] str4 = { "年交", "分为一次性交付和分期交付（年交）两种，由投保人在投保时选择" };
//	private String[] jf = { "年交", "分期交付" };
//	private String[] bank = { "中国建设银行", "中国工商银行", "中国农业银行", "中国招商银行" };
//	private String[] type = { "身份证", "军人证", "教师证" };
//
//	private String[] cardtype = { "信用卡", "存储卡" };
//
//	class taobaoQListener implements View.OnClickListener {
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			toubaoT.setVisibility(View.VISIBLE);
//			toubao_listView.setVisibility(View.GONE);
//		}
//	}
//
//	class touBaoSListener implements View.OnClickListener {
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Toast.makeText(InsureActivity.this, "刷新成功，目前为最新数据。。。。。",
//					Toast.LENGTH_LONG).show();
//		}
//	}
//
//	class ShuaXinListener implements View.OnClickListener {
//		@Override
//		public void onClick(View v) {
//			Toast.makeText(InsureActivity.this, "刷新成功，目前为最新数据。。。。。",
//					Toast.LENGTH_LONG).show();
//		}
//	}
//
//	class ChaXunListener implements View.OnClickListener {
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			zhuantai_t.setVisibility(View.VISIBLE);
//			zhuangtai_listView.setVisibility(View.GONE);
//		}
//	}
//
//	public void dialogFun(CharSequence str, Context context) {
//		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
//				.getSystemService(LAYOUT_INFLATER_SERVICE);
//		View view = inflater.inflate(R.layout.worningdialog, null);
//		TextView tv = (TextView) view.findViewById(R.id.worningTv);
//		tv.setText(str);
//		AlertDialog.Builder alert = new AlertDialog.Builder(context);
//		alert.setView(view);
//		alert.setTitle("提示")
//				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						Toast.makeText(InsureActivity.this, "交费成功。。。。",
//								Toast.LENGTH_LONG).show();
//					}
//				})
//				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						return;
//					}
//				}).create().show();
//	}
//
//	class JiaoFeiListener implements View.OnClickListener {
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			dialogFun("确定要交费吗？？？", InsureActivity.this);
//		}
//	}
//
//	class QueryListener implements View.OnClickListener {
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			tab_list.clear();
//			padapter = new PAdapter(tab_list);
//			tab_listView.setAdapter(padapter);
//			tab_listView.setCacheColorHint(0);
//		}
//	}
//
//	class BtnCopyListener implements View.OnClickListener {
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			if (tab_list.size() != 0 && xiamian.getChildAt(0) == tab_listView) {
//				Map<String, String> map2 = new HashMap<String, String>();
//				map2.put("jihuashu", "2");
//				map2.put("time", "武国庆");
//				map2.put("toubaoren", "刘星星");
//				map2.put("nianling", "2012-3-4");
//				map2.put("beibaoren", "3333");
//				map2.put("beibaonianling", "已上传");
//				tab_list.add(map2);
//				padapter = new PAdapter(tab_list);
//				tab_listView.setAdapter(padapter);
//				tab_listView.setCacheColorHint(0);
//			} else {
//				xiamian.removeAllViews();
//				TextView tt = new TextView(InsureActivity.this);
//				tt.setText("没有可复制的记录。。。。。");
//				tt.setTextColor(Color.RED);
//				tt.setTextSize(20);
//				xiamian.setPadding(20, 20, 20, 20);
//				xiamian.addView(tt);
//			}
//		}
//	}
//
//	class QueryJiHuaShuListener implements View.OnClickListener {
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			String jiezhi = name_jihuashu.getText().toString();
//			int r = Integer.parseInt(jiezhi);
//			if (r > 4) {
//				xiamian.removeAllViews();
//				TextView tt = new TextView(InsureActivity.this);
//				tt.setText("没有您要查询的记录。。。。。");
//				tt.setTextColor(Color.RED);
//				tt.setTextSize(20);
//				xiamian.setPadding(20, 20, 20, 20);
//				xiamian.addView(tt);
//			} else {
//
//				padapter = new PAdapter(huancun_list);
//				tab_listView.setAdapter(padapter);
//				tab_listView.setCacheColorHint(0);
//				xiamian.removeAllViews();
//				xiamian.addView(tab_listView);
//			}
//		}
//	}
//
//	private void MaLongFun() {
//		view = ((LayoutInflater) this
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
//				R.layout.item_layout, null);
//		Spinner s1 = (Spinner) view.findViewById(R.id.s1);
//		Spinner s2 = (Spinner) view.findViewById(R.id.s2);
//		Spinner s3 = (Spinner) view.findViewById(R.id.s3);
//		Spinner s4 = (Spinner) view.findViewById(R.id.s4);
//		Spinner s5 = (Spinner) view.findViewById(R.id.s5);
//		Spinner s6 = (Spinner) view.findViewById(R.id.s6);
//		Spinner s7 = (Spinner) view.findViewById(R.id.s7);
//		Spinner s8 = (Spinner) view.findViewById(R.id.s8);
//		Spinner s9 = (Spinner) view.findViewById(R.id.s9);
//		Spinner s10 = (Spinner) view.findViewById(R.id.s10);
//		Spinner s11 = (Spinner) view.findViewById(R.id.s11);
//		ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str1);
//		s1.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str32);
//		s2.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str32);
//		s3.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str4);
//		s4.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str52);
//		s5.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str51);
//		s6.setAdapter(arrayadapter);
//
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str52);
//		s7.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str53);
//		s8.setAdapter(arrayadapter);
//
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str52);
//		s9.setAdapter(arrayadapter);
//
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str54);
//		s10.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line,
//				new String[] { "纸质保单" });
//		s11.setAdapter(arrayadapter);
//	}
//
//	@Override
//	public void onClick(View v) {
//		if(	part2.getVisibility()==View.GONE){
//			part2.setVisibility(View.VISIBLE);
//			if(contents!=null){
//			contents.setVisibility(View.GONE);
//			}
//		}
//		if (v.getId() == R.id.bulidIsure) {// 新建投保书
//			right_new.removeAllViews();
//			mLayout.removeAllViews();
//			mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
//			mViewTitle1.setBackgroundResource(R.drawable.single_tag01_on);
//			mViewTitle4.setBackgroundResource(R.drawable.single_tag01);
//			mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
//			// mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
//			mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
//			Lxx_right_top.setVisibility(View.VISIBLE);
//			right_new.addView(linear_new);
//		} else if (v.getId() == R.id.maintainbook) {// 维护电子投保书
//			right_new.removeAllViews();
//			Lxx_right_top.setVisibility(View.GONE);
//			right_new.addView(pro_relative);
//		} else if (v.getId() == R.id.searchcard) {// 投保单状态查询
//			right_new.removeAllViews();
//			zhuantai_t.setVisibility(View.GONE);
//			Lxx_right_top.setVisibility(View.GONE);
//			zhuangtai_listView.setVisibility(View.VISIBLE);
//			right_new.addView(zhuangtai);
//		} else if (v.getId() == R.id.searchcardback) {// 投保单交回查询
//			right_new.removeAllViews();
//			toubaoT.setVisibility(View.GONE);
//			Lxx_right_top.setVisibility(View.GONE);
//			toubao_listView.setVisibility(View.VISIBLE);
//			right_new.addView(toubaodan);
//		} else if (v.getId() == R.id.cusinfo) {// 客户资料
//			this.reset();
//			right_new.removeAllViews();
//			mLayout.removeAllViews();
//			mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
//			mViewTitle1.setBackgroundResource(R.drawable.single_tag01_on);
//			mViewTitle4.setBackgroundResource(R.drawable.single_tag01);
//			mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
//			// mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
//			mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
//			Lxx_right_top.setVisibility(View.VISIBLE);
//			right_new.addView(linear_new);
////			mLayout.removeAllViews();
////			mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle4.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
////			// mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle1.setBackgroundResource(R.drawable.single_tag01_on);
////			View view = ((LayoutInflater) this
////					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////					.inflate(R.layout.new_toubaoren, null);
////			mLayout.addView(view);
//		} else if (v.getId() == R.id.insureItem) {
//			touBao();
//		} else if (v.getId() == R.id.auth) {// 转账授权
//			this.forwardAccount();
////			mLayout.removeAllViews();
////			View view = ((LayoutInflater) this
////					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////					.inflate(R.layout.auth_data_layout, null);
////			mLayout.addView(view);
////			Spinner s1 = (Spinner) this.findViewById(R.id.s1);
////			Spinner s2 = (Spinner) this.findViewById(R.id.s2);
////			Spinner s3 = (Spinner) this.findViewById(R.id.s3);
////			Spinner s4 = (Spinner) this.findViewById(R.id.s4);
////			Spinner s5 = (Spinner) this.findViewById(R.id.s5);
////			Spinner s6 = (Spinner) this.findViewById(R.id.s6);
////			ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
////					android.R.layout.simple_dropdown_item_1line, bank);
////			s1.setAdapter(arrayadapter);
////			arrayadapter = new ArrayAdapter<String>(this,
////					android.R.layout.simple_dropdown_item_1line, type);
////			s2.setAdapter(arrayadapter);
////			arrayadapter = new ArrayAdapter<String>(this,
////					android.R.layout.simple_dropdown_item_1line, bank);
////			s3.setAdapter(arrayadapter);
////			arrayadapter = new ArrayAdapter<String>(this,
////					android.R.layout.simple_dropdown_item_1line, type);
////			s4.setAdapter(arrayadapter);
////			arrayadapter = new ArrayAdapter<String>(this,
////					android.R.layout.simple_dropdown_item_1line, bank);
////			s5.setAdapter(arrayadapter);
////			arrayadapter = new ArrayAdapter<String>(this,
////					android.R.layout.simple_dropdown_item_1line, type);
////			s6.setAdapter(arrayadapter);
////
////			Button btn = (Button) this.findViewById(R.id.liyi_btn1);
////			btn.setOnClickListener(new OnClickListener() {
////				@Override
////				public void onClick(View v) {// 相关告知
////				// LinearLayout contents = (LinearLayout)
////				// InsureActivity.this.findViewById(R.id.contents);
////				// contents.removeAllViews();
////				// LayoutInflater lay = (LayoutInflater)
////				// getSystemService(LAYOUT_INFLATER_SERVICE);
////				// LinearLayout guo_tab4 = (LinearLayout) lay.inflate(
////				// R.layout.guo_tab4, null);
////				// contents.addView(guo_tab4);
////					mViewTitle1 = (TextView) InsureActivity.this
////							.findViewById(R.id.cusinfo);
////					mViewTitle2 = (TextView) InsureActivity.this
////							.findViewById(R.id.insureItem);
////					mViewTitle3 = (TextView) InsureActivity.this
////							.findViewById(R.id.auth);
////					mViewTitle4 = (TextView) InsureActivity.this
////							.findViewById(R.id.notify);
////					mViewTitle5 = (TextView) InsureActivity.this
////							.findViewById(R.id.payinfo);
////					mViewTitle6 = (TextView) InsureActivity.this
////							.findViewById(R.id.sure);
////					mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////					mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
////					mViewTitle4
////							.setBackgroundResource(R.drawable.single_tag01_on);
////					mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
////					mViewTitle6.setBackgroundResource(R.drawable.single_tag01);
////					mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
////					LinearLayout contents = (LinearLayout) findViewById(R.id.contents);
////					contents.removeAllViews();
////					mLayout.removeAllViews();
////					mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////					mViewTitle4
////							.setBackgroundResource(R.drawable.single_tag01_on);
////					mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
////					mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
////					// mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////					mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
////					LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
////					LinearLayout guo_tab4 = (LinearLayout) lay.inflate(
////							R.layout.guo_tab4, null);
////					String[] str1 = { "一般职员", "医生", "护士", "清洁工", "老师", "部门经理" };
////					String[] str2 = { "工薪", "福利", "提成", "外快", "加班费", "奖金" };
////					Spinner guo_s1 = (Spinner) guo_tab4
////							.findViewById(R.id.guo_s1);
////					Spinner guo_s2 = (Spinner) guo_tab4
////							.findViewById(R.id.guo_s2);
////					Spinner guo_s3 = (Spinner) guo_tab4
////							.findViewById(R.id.guo_s3);
////					Spinner guo_s4 = (Spinner) guo_tab4
////							.findViewById(R.id.guo_s4);
////					guo_s1.setAdapter(new ArrayAdapter<String>(
////							InsureActivity.this,
////							android.R.layout.simple_dropdown_item_1line, str1));
////					guo_s2.setAdapter(new ArrayAdapter<String>(
////							InsureActivity.this,
////							android.R.layout.simple_dropdown_item_1line, str2));
////					guo_s3.setAdapter(new ArrayAdapter<String>(
////							InsureActivity.this,
////							android.R.layout.simple_dropdown_item_1line, str1));
////					guo_s4.setAdapter(new ArrayAdapter<String>(
////							InsureActivity.this,
////							android.R.layout.simple_dropdown_item_1line, str2));
////					contents.addView(guo_tab4);
////				}
////			});
////
////			mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle3.setBackgroundResource(R.drawable.single_tag01_on);
////			mViewTitle4.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle6.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
////			// View viewAuth=((LayoutInflater)
////			// this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.auth_data_layout,
////			// null);
////			// mLayout.addView(viewAuth);
//		} else if (v.getId() == R.id.notify) {// 相关告知
//			this.guo_tab4();
////			LinearLayout contents = (LinearLayout) findViewById(R.id.contents);
////			contents.removeAllViews();
////			mLayout.removeAllViews();
////			mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle4.setBackgroundResource(R.drawable.single_tag01_on);
////			mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
////			// mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
////			LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
////			LinearLayout guo_tab4 = (LinearLayout) lay.inflate(
////					R.layout.guo_tab4, null);
////			String[] str1 = { "一般职员", "医生", "护士", "清洁工", "老师", "部门经理" };
////			String[] str2 = { "工薪", "福利", "提成", "外快", "加班费", "奖金" };
////			Spinner guo_s1 = (Spinner) guo_tab4.findViewById(R.id.guo_s1);
////			Spinner guo_s2 = (Spinner) guo_tab4.findViewById(R.id.guo_s2);
////			Spinner guo_s3 = (Spinner) guo_tab4.findViewById(R.id.guo_s3);
////			Spinner guo_s4 = (Spinner) guo_tab4.findViewById(R.id.guo_s4);
////			guo_s1.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
////					android.R.layout.simple_dropdown_item_1line, str1));
////			guo_s2.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
////					android.R.layout.simple_dropdown_item_1line, str2));
////			guo_s3.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
////					android.R.layout.simple_dropdown_item_1line, str1));
////			guo_s4.setAdapter(new ArrayAdapter<String>(InsureActivity.this,
////					android.R.layout.simple_dropdown_item_1line, str2));
////			contents.addView(guo_tab4);
////			Button button = (Button) guo_tab4.findViewById(R.id.next);
////			button.setOnClickListener(new OnClickListener() {
////
////				@Override
////				public void onClick(View v) {
////
////					// 确认投保
////					LinearLayout contents = (LinearLayout) findViewById(R.id.Ptab2);
////					contents.removeAllViews();
////					LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
////					guo_tab5 = (FrameLayout) lay.inflate(R.layout.guo_tab5,
////							null);
////					final LinearLayout guoLayout1 = (LinearLayout) guo_tab5
////							.findViewById(R.id.guo_linear1);
////					final LinearLayout guobtnlayout = (LinearLayout) guo_tab5
////							.findViewById(R.id.guo_btnlayout);
////
////					guoLayout1.setVisibility(View.VISIBLE);
////					guobtnlayout.setVisibility(View.INVISIBLE);
////
////					Button guo_btn1 = (Button) guo_tab5
////							.findViewById(R.id.guo_btn1);
////					Button guo_btn2 = (Button) guo_tab5
////							.findViewById(R.id.guo_btn2);
////					Button guo_btn3 = (Button) guo_tab5
////							.findViewById(R.id.guo_btn3);
////					Button guo_btn4 = (Button) guo_tab5
////							.findViewById(R.id.guo_btn4);
////					guo_image = (ImageView) guo_tab5
////							.findViewById(R.id.guo_image);
////
////					guo_btn1.setOnClickListener(new View.OnClickListener() {
////
////						@Override
////						public void onClick(View v) {
////							guoLayout1.setVisibility(View.INVISIBLE);
////							guo_tab5.setFocusable(true);
////							guobtnlayout.setVisibility(View.VISIBLE);
////							guo_tab5.addView(
////									new WritePath(InsureActivity.this), 1);
////							guo_tab5.invalidate();
////
////						}
////					});
////					guo_btn2.setOnClickListener(new View.OnClickListener() {
////
////						@Override
////						public void onClick(View v) {
////
////							mLayout.removeAllViews();
////
////							// View view = ((LayoutInflater) this
////							// .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////							// .inflate(R.layout.pay_layout, null);
////							// mLayout.addView(view);
////							// Button btn = (Button)
////							// this.findViewById(R.id.zhifu);
////							// btn.setOnClickListener(new OnClickListener() {
////
////							View view = ((LayoutInflater) InsureActivity.this
////									.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////									.inflate(R.layout.pay_layout, null);
////							mLayout.removeAllViews();
////							mViewTitle2
////									.setBackgroundResource(R.drawable.single_tag01);
////							mViewTitle5
////									.setBackgroundResource(R.drawable.single_tag01_on);
////							mViewTitle4
////									.setBackgroundResource(R.drawable.single_tag01);
////							mViewTitle3
////									.setBackgroundResource(R.drawable.single_tag01);
////							// mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////							mViewTitle1
////									.setBackgroundResource(R.drawable.single_tag01);
////							mLayout.addView(view);
////							Button btn = (Button) InsureActivity.this
////									.findViewById(R.id.zhifu);
////							btn.setOnClickListener(new OnClickListener() {
////
////								@Override
////								public void onClick(View v) {
////									mLayout.removeAllViews();
////									View view = ((LayoutInflater) InsureActivity.this
////											.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////											.inflate(R.layout.next1_layout,
////													null);
////									mLayout.addView(view);
////									Button view1 = (Button) InsureActivity.this
////											.findViewById(R.id.carsh);
////									view1.setOnClickListener(new OnClickListener() {
////										@Override
////										public void onClick(View v) {
////											mLayout.removeAllViews();
////											View view = ((LayoutInflater) InsureActivity.this
////													.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////													.inflate(
////															R.layout.wait_layout,
////															null);
////											mLayout.addView(view);
////											TextView view2 = (TextView) InsureActivity.this
////													.findViewById(R.id.ka);
////											view2.setOnClickListener(new OnClickListener() {
////												@Override
////												public void onClick(View v) {
////													mLayout.removeAllViews();
////													View view = ((LayoutInflater) InsureActivity.this
////															.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////															.inflate(
////																	R.layout.shua_card_layout,
////																	null);
////													mLayout.addView(view);
////													TextView view2 = (TextView) InsureActivity.this
////															.findViewById(R.id.ka);
////													view2.setOnClickListener(new OnClickListener() {
////
////														@Override
////														public void onClick(
////																View v) {
////															mLayout.removeAllViews();
////															View view = ((LayoutInflater) InsureActivity.this
////																	.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////																	.inflate(
////																			R.layout.confirm_layout,
////																			null);
////															mLayout.addView(view);
////															Button button = (Button) InsureActivity.this
////																	.findViewById(R.id.confirm);
////															Spinner s1 = (Spinner) InsureActivity.this
////																	.findViewById(R.id.s1);
////															Spinner s2 = (Spinner) InsureActivity.this
////																	.findViewById(R.id.s2);
////															ArrayAdapter arrayadapter = new ArrayAdapter<String>(
////																	InsureActivity.this,
////																	android.R.layout.simple_dropdown_item_1line,
////																	cardtype);
////															s1.setAdapter(arrayadapter);
////															arrayadapter = new ArrayAdapter<String>(
////																	InsureActivity.this,
////																	android.R.layout.simple_dropdown_item_1line,
////																	bank);
////															s2.setAdapter(arrayadapter);
////															button.setOnClickListener(new OnClickListener() {
////																@Override
////																public void onClick(
////																		View v) {
////																	new Thread() {
////																		public void run() {
////																			try {
////																				sleep(3000);
////																				handler.sendEmptyMessage(1);
////																				handler.sendEmptyMessage(2);
////																			} catch (InterruptedException e) {
////																				e.printStackTrace();
////																			}
////																		}
////																	}.start();
////																	confirmShowWaitDialog();
////																}
////															});
////														}
////
////													});
////												}
////											});
////										}
////									});
////								}
////							});
////
////						}
////					});
////					guo_btn3.setOnClickListener(new View.OnClickListener() {
////						private String getPhotoFileName() {
////							Date date = new Date(System.currentTimeMillis());
////							SimpleDateFormat dateFormat = new SimpleDateFormat(
////									"'IMG'_yyyyMMdd_HHmmss");
////							return dateFormat.format(date) + ".jpg";
////						}
////
////						@Override
////						public void onClick(View v) {
////							guo_tab5.getChildAt(1).setDrawingCacheEnabled(true);
////							guo_tab5.getChildAt(1).buildDrawingCache();
////							Bitmap b = guo_tab5.getChildAt(1).getDrawingCache();
////							MyApplication.getInstance().setBitmap1(b);
////							File mCurrentPhotoFile;
////							String SCREEN_SHOTS_LOCATION = Environment
////									.getExternalStorageDirectory()
////									+ "/baoqi/baodan";
////							File f = new File(SCREEN_SHOTS_LOCATION);
////							f.mkdirs();// 创建照片的存储目录
////							mCurrentPhotoFile = new File(f, getPhotoFileName());// 给新照的照片文件命名
////							MyApplication.getInstance().setmCurrentPhotoFile(
////									mCurrentPhotoFile);
////							// String fileName = "sbsc" +
////							// System.currentTimeMillis() + ".png";
////							Bitmap bitmap = BitmapFactory
////									.decodeStream(InsureActivity.this
////											.getResources().openRawResource(
////													R.drawable.insure_viedw_tu));
////							MyApplication.getInstance().setBitmap2(bitmap);
////
////							CanvasWriteActivity canvas = new CanvasWriteActivity(
////									MyApplication.getInstance().getBitmap2(),
////									MyApplication.getInstance().getBitmap1(),
////									mCurrentPhotoFile, InsureActivity.this);
////							Thread thread = new Thread(canvas);
////							thread.start();
////
////							guoLayout1.setVisibility(View.VISIBLE);
////							guobtnlayout.setVisibility(View.INVISIBLE);
////
////							progressdialog.setMessage("正在加载图片，请稍等");
////							progressdialog.show();
////
////							guo_tab5.setVisibility(View.INVISIBLE);
////						}
////					});
////					guo_btn4.setOnClickListener(new View.OnClickListener() {
////
////						@Override
////						public void onClick(View v) {
////							guo_tab5.removeViewAt(1);
////							guo_tab5.addView(
////									new WritePath(InsureActivity.this), 1);
////							guo_tab5.invalidate();
////						}
////					});
////
////					contents.addView(guo_tab5);
////
////				}
////
////			});
//		} else if (v.getId() == R.id.payinfo) {// 支付信息
//			this.payInfo();
////			mLayout.removeAllViews();
////			View view = ((LayoutInflater) this
////					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////					.inflate(R.layout.pay_layout, null);
////			mLayout.removeAllViews();
////			mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle5.setBackgroundResource(R.drawable.single_tag01_on);
////			mViewTitle4.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
////			// mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////			mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
////			mLayout.addView(view);
////			Button btn = (Button) this.findViewById(R.id.zhifu);
////			btn.setOnClickListener(new OnClickListener() {
////
////				@Override
////				public void onClick(View v) {
////					mLayout.removeAllViews();
////					View view = ((LayoutInflater) InsureActivity.this
////							.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////							.inflate(R.layout.next1_layout, null);
////					mLayout.addView(view);
////					Button view1 = (Button) InsureActivity.this
////							.findViewById(R.id.carsh);
////					view1.setOnClickListener(new OnClickListener() {
////						@Override
////						public void onClick(View v) {
////							mLayout.removeAllViews();
////							View view = ((LayoutInflater) InsureActivity.this
////									.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////									.inflate(R.layout.wait_layout, null);
////							mLayout.addView(view);
////							TextView view2 = (TextView) InsureActivity.this
////									.findViewById(R.id.ka);
////							view2.setOnClickListener(new OnClickListener() {
////								@Override
////								public void onClick(View v) {
////									mLayout.removeAllViews();
////									View view = ((LayoutInflater) InsureActivity.this
////											.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////											.inflate(R.layout.shua_card_layout,
////													null);
////									mLayout.addView(view);
////									TextView view2 = (TextView) InsureActivity.this
////											.findViewById(R.id.ka);
////									view2.setOnClickListener(new OnClickListener() {
////
////										@Override
////										public void onClick(View v) {
////											mLayout.removeAllViews();
////											View view = ((LayoutInflater) InsureActivity.this
////													.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////													.inflate(
////															R.layout.confirm_layout,
////															null);
////											mLayout.addView(view);
////											Button button = (Button) InsureActivity.this
////													.findViewById(R.id.confirm);
////											Spinner s1 = (Spinner) InsureActivity.this
////													.findViewById(R.id.s1);
////											Spinner s2 = (Spinner) InsureActivity.this
////													.findViewById(R.id.s2);
////											ArrayAdapter arrayadapter = new ArrayAdapter<String>(
////													InsureActivity.this,
////													android.R.layout.simple_dropdown_item_1line,
////													cardtype);
////											s1.setAdapter(arrayadapter);
////											arrayadapter = new ArrayAdapter<String>(
////													InsureActivity.this,
////													android.R.layout.simple_dropdown_item_1line,
////													bank);
////											s2.setAdapter(arrayadapter);
////											button.setOnClickListener(new OnClickListener() {
////												@Override
////												public void onClick(View v) {
////													new Thread() {
////														public void run() {
////															try {
////																sleep(3000);
////																handler.sendEmptyMessage(1);
////																handler.sendEmptyMessage(2);
////															} catch (InterruptedException e) {
////																e.printStackTrace();
////															}
////														}
////													}.start();
////													confirmShowWaitDialog();
////												}
////											});
////										}
////
////									});
////								}
////							});
////						}
////					});
////				}
////			});
//
//		} else if (v.getId() == R.id.sure) {
//			this.guo_tab5();
////			// 确认投保
////			LinearLayout contents = (LinearLayout) findViewById(R.id.Ptab2);
////			contents.removeAllViews();
////			LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
////			guo_tab5 = (FrameLayout) lay.inflate(R.layout.guo_tab5, null);
////			final LinearLayout guoLayout1 = (LinearLayout) guo_tab5
////					.findViewById(R.id.guo_linear1);
////			final LinearLayout guobtnlayout = (LinearLayout) guo_tab5
////					.findViewById(R.id.guo_btnlayout);
////
////			guoLayout1.setVisibility(View.VISIBLE);
////			guobtnlayout.setVisibility(View.INVISIBLE);
////
////			Button guo_btn1 = (Button) guo_tab5.findViewById(R.id.guo_btn1);
////			Button guo_btn2 = (Button) guo_tab5.findViewById(R.id.guo_btn2);
////			Button guo_btn3 = (Button) guo_tab5.findViewById(R.id.guo_btn3);
////			Button guo_btn4 = (Button) guo_tab5.findViewById(R.id.guo_btn4);
////			guo_image = (ImageView) guo_tab5.findViewById(R.id.guo_image);
////
////			guo_btn1.setOnClickListener(new View.OnClickListener() {
////
////				@Override
////				public void onClick(View v) {
////					guoLayout1.setVisibility(View.INVISIBLE);
////					guo_tab5.setFocusable(true);
////					guobtnlayout.setVisibility(View.VISIBLE);
////					guo_tab5.addView(new WritePath(InsureActivity.this), 1);
////					guo_tab5.invalidate();
////
////				}
////			});
////			guo_btn2.setOnClickListener(new View.OnClickListener() {
////
////				@Override
////				public void onClick(View v) {
////
////					mLayout.removeAllViews();
////
////					// View view = ((LayoutInflater) this
////					// .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////					// .inflate(R.layout.pay_layout, null);
////					// mLayout.addView(view);
////					// Button btn = (Button) this.findViewById(R.id.zhifu);
////					// btn.setOnClickListener(new OnClickListener() {
////
////					View view = ((LayoutInflater) InsureActivity.this
////							.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////							.inflate(R.layout.pay_layout, null);
////					mLayout.removeAllViews();
////					mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////					mViewTitle5
////							.setBackgroundResource(R.drawable.single_tag01_on);
////					mViewTitle4.setBackgroundResource(R.drawable.single_tag01);
////					mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
////					// mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////					mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
////					mLayout.addView(view);
////					Button btn = (Button) InsureActivity.this
////							.findViewById(R.id.zhifu);
////					btn.setOnClickListener(new OnClickListener() {
////
////						@Override
////						public void onClick(View v) {
////							mLayout.removeAllViews();
////							View view = ((LayoutInflater) InsureActivity.this
////									.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////									.inflate(R.layout.next1_layout, null);
////							mLayout.addView(view);
////							Button view1 = (Button) InsureActivity.this
////									.findViewById(R.id.carsh);
////							view1.setOnClickListener(new OnClickListener() {
////								@Override
////								public void onClick(View v) {
////									mLayout.removeAllViews();
////									View view = ((LayoutInflater) InsureActivity.this
////											.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////											.inflate(R.layout.wait_layout, null);
////									mLayout.addView(view);
////									TextView view2 = (TextView) InsureActivity.this
////											.findViewById(R.id.ka);
////									view2.setOnClickListener(new OnClickListener() {
////										@Override
////										public void onClick(View v) {
////											mLayout.removeAllViews();
////											View view = ((LayoutInflater) InsureActivity.this
////													.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////													.inflate(
////															R.layout.shua_card_layout,
////															null);
////											mLayout.addView(view);
////											TextView view2 = (TextView) InsureActivity.this
////													.findViewById(R.id.ka);
////											view2.setOnClickListener(new OnClickListener() {
////
////												@Override
////												public void onClick(View v) {
////													mLayout.removeAllViews();
////													View view = ((LayoutInflater) InsureActivity.this
////															.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////															.inflate(
////																	R.layout.confirm_layout,
////																	null);
////													mLayout.addView(view);
////													Button button = (Button) InsureActivity.this
////															.findViewById(R.id.confirm);
////													Spinner s1 = (Spinner) InsureActivity.this
////															.findViewById(R.id.s1);
////													Spinner s2 = (Spinner) InsureActivity.this
////															.findViewById(R.id.s2);
////													ArrayAdapter arrayadapter = new ArrayAdapter<String>(
////															InsureActivity.this,
////															android.R.layout.simple_dropdown_item_1line,
////															cardtype);
////													s1.setAdapter(arrayadapter);
////													arrayadapter = new ArrayAdapter<String>(
////															InsureActivity.this,
////															android.R.layout.simple_dropdown_item_1line,
////															bank);
////													s2.setAdapter(arrayadapter);
////													button.setOnClickListener(new OnClickListener() {
////														@Override
////														public void onClick(
////																View v) {
////															new Thread() {
////																public void run() {
////																	try {
////																		sleep(3000);
////																		handler.sendEmptyMessage(1);
////																		handler.sendEmptyMessage(2);
////																	} catch (InterruptedException e) {
////																		e.printStackTrace();
////																	}
////																}
////															}.start();
////															confirmShowWaitDialog();
////														}
////													});
////												}
////
////											});
////										}
////									});
////								}
////							});
////						}
////					});
////
////				}
////			});
////			guo_btn3.setOnClickListener(new View.OnClickListener() {
////				private String getPhotoFileName() {
////					Date date = new Date(System.currentTimeMillis());
////					SimpleDateFormat dateFormat = new SimpleDateFormat(
////							"'IMG'_yyyyMMdd_HHmmss");
////					return dateFormat.format(date) + ".jpg";
////				}
////
////				@Override
////				public void onClick(View v) {
////					guo_tab5.getChildAt(1).setDrawingCacheEnabled(true);
////					guo_tab5.getChildAt(1).buildDrawingCache();
////					Bitmap b = guo_tab5.getChildAt(1).getDrawingCache();
////					MyApplication.getInstance().setBitmap1(b);
////					File mCurrentPhotoFile;
////					String SCREEN_SHOTS_LOCATION = Environment
////							.getExternalStorageDirectory() + "/baoqi/baodan";
////					File f = new File(SCREEN_SHOTS_LOCATION);
////					f.mkdirs();// 创建照片的存储目录
////					mCurrentPhotoFile = new File(f, getPhotoFileName());// 给新照的照片文件命名
////					MyApplication.getInstance().setmCurrentPhotoFile(
////							mCurrentPhotoFile);
////					// String fileName = "sbsc" + System.currentTimeMillis() +
////					// ".png";
////					Bitmap bitmap = BitmapFactory
////							.decodeStream(InsureActivity.this
////									.getResources()
////									.openRawResource(R.drawable.insure_viedw_tu));
////					MyApplication.getInstance().setBitmap2(bitmap);
////
////					CanvasWriteActivity canvas = new CanvasWriteActivity(
////							MyApplication.getInstance().getBitmap2(),
////							MyApplication.getInstance().getBitmap1(),
////							mCurrentPhotoFile, InsureActivity.this);
////					Thread thread = new Thread(canvas);
////					thread.start();
////
////					guoLayout1.setVisibility(View.VISIBLE);
////					guobtnlayout.setVisibility(View.INVISIBLE);
////
////					progressdialog.setMessage("正在加载图片，请稍等");
////					progressdialog.show();
////
////					guo_tab5.setVisibility(View.INVISIBLE);
////				}
////			});
////			guo_btn4.setOnClickListener(new View.OnClickListener() {
////
////				@Override
////				public void onClick(View v) {
////					guo_tab5.removeViewAt(1);
////					guo_tab5.addView(new WritePath(InsureActivity.this), 1);
////					guo_tab5.invalidate();
////				}
////			});
////
////			contents.addView(guo_tab5);
//
//		}
//	}
//
//	class PAdapter extends BaseAdapter {
//		List<Map<String, String>> list;
//
//		public PAdapter(List<Map<String, String>> list1) {
//			this.list = list1;
//		}
//
//		@Override
//		public int getCount() {
//			return list.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			LinearLayout li = (LinearLayout) layout_new.inflate(
//					R.layout.tab_child, null);
//			CheckBox ch = (CheckBox) li.findViewById(R.id.che_id);
//			TextView t1 = (TextView) li.findViewById(R.id.text_1);
//			TextView t2 = (TextView) li.findViewById(R.id.text_2);
//			TextView t3 = (TextView) li.findViewById(R.id.text_3);
//			TextView t4 = (TextView) li.findViewById(R.id.text_4);
//			TextView t5 = (TextView) li.findViewById(R.id.text_5);
//			TextView t6 = (TextView) li.findViewById(R.id.text_6);
//			Map<String, String> map = list.get(position);
//			t1.setText(map.get("jihuashu"));
//			t2.setText(map.get("time"));
//			t3.setText(map.get("toubaoren"));
//			t4.setText(map.get("nianling"));
//			t5.setText(map.get("beibaoren"));
//			t6.setText(map.get("beibaonianling"));
//			return li;
//		}
//
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		switch (keyCode) {
//		case KeyEvent.KEYCODE_BACK:
//			Intent intent = new Intent(this, PropertyInsuranceMainActivity.class);
//			startActivity(intent);
//			finish();
//			break;
//		}
//
//		return super.onKeyDown(keyCode, event);
//	}
//
//	public void touBao() {
//		mViewTitle2.setBackgroundResource(R.drawable.single_tag01_on);
//		mViewTitle3.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle4.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
//		mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
//		mLayout.removeAllViews();
//
//		View view = ((LayoutInflater) this
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
//				R.layout.item_layout, null);
//		mLayout.addView(view);
//		Spinner s1 = (Spinner) this.findViewById(R.id.s1);
//		Spinner s2 = (Spinner) this.findViewById(R.id.s2);
//		Spinner s3 = (Spinner) this.findViewById(R.id.s3);
//		Spinner s4 = (Spinner) this.findViewById(R.id.s4);
//		Spinner s5 = (Spinner) this.findViewById(R.id.s5);
//		Spinner s6 = (Spinner) this.findViewById(R.id.s6);
//		Spinner s7 = (Spinner) this.findViewById(R.id.s7);
//		Spinner s8 = (Spinner) this.findViewById(R.id.s8);
//		Spinner s9 = (Spinner) this.findViewById(R.id.s9);
//		Spinner s10 = (Spinner) this.findViewById(R.id.s10);
//		 Spinner s11 = (Spinner) this.findViewById(R.id.s11);
//		 Spinner s13 = (Spinner) this.findViewById(R.id.s13);
//		ArrayAdapter arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str1);
//		s1.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str32);
//		s2.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str32);
//		s3.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str4);
//		s4.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str52);
//		s5.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str51);
//		s6.setAdapter(arrayadapter);
//
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str52);
//		s7.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str53);
//		s8.setAdapter(arrayadapter);
//
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str52);
//		s9.setAdapter(arrayadapter);
//
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, str54);
//		s10.setAdapter(arrayadapter);
//		arrayadapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line,
//				new String[] { "银行转账","pos机","实时代扣" });
//		 s11.setAdapter(arrayadapter);
//		 arrayadapter = new ArrayAdapter<String>(this,
//					android.R.layout.simple_dropdown_item_1line,
//					new String[] { "纸质保单" });
//			 s13.setAdapter(arrayadapter);
//		final ListView mlistview = (ListView) this.findViewById(R.id.mlistview);
//
//		Button btn = (Button) this.findViewById(R.id.liyi_btn1);
//		btn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				AddTypeAdapter madapter = new AddTypeAdapter(
//						InsureActivity.this, getList());
//				mlistview.setAdapter(madapter);
//				madapter.notifyDataSetChanged();
//				setListViewHeightBasedOnChildren(mlistview);
//			}
//
//		});
//		Button btn2 = (Button) this.findViewById(R.id.liyi_btn2);
//		btn2.setOnClickListener(new OnClickListener() {// next
//
//			@Override
//			public void onClick(View v) {
//				InsureActivity.this.forwardAccount();
////				mLayout.removeAllViews();
////				View view = ((LayoutInflater) InsureActivity.this
////						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////						.inflate(R.layout.auth_data_layout, null);
////				mLayout.addView(view);
////				mViewTitle2.setBackgroundResource(R.drawable.single_tag01);
////				mViewTitle3.setBackgroundResource(R.drawable.single_tag01_on);
////				mViewTitle4.setBackgroundResource(R.drawable.single_tag01);
////				mViewTitle5.setBackgroundResource(R.drawable.single_tag01);
////				mViewTitle1.setBackgroundResource(R.drawable.single_tag01);
////				mViewTitle6.setBackgroundResource(R.drawable.single_tag01);
////				Spinner s1 = (Spinner) InsureActivity.this
////						.findViewById(R.id.s1);
////				Spinner s2 = (Spinner) InsureActivity.this
////						.findViewById(R.id.s2);
////				Spinner s3 = (Spinner) InsureActivity.this
////						.findViewById(R.id.s3);
////				Spinner s4 = (Spinner) InsureActivity.this
////						.findViewById(R.id.s4);
////				Spinner s5 = (Spinner) InsureActivity.this
////						.findViewById(R.id.s5);
////				Spinner s6 = (Spinner) InsureActivity.this
////						.findViewById(R.id.s6);
////				ArrayAdapter arrayadapter = new ArrayAdapter<String>(
////						InsureActivity.this,
////						android.R.layout.simple_dropdown_item_1line, bank);
////				s1.setAdapter(arrayadapter);
////				arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
////						android.R.layout.simple_dropdown_item_1line, type);
////				s2.setAdapter(arrayadapter);
////				arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
////						android.R.layout.simple_dropdown_item_1line, bank);
////				s3.setAdapter(arrayadapter);
////				arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
////						android.R.layout.simple_dropdown_item_1line, type);
////				s4.setAdapter(arrayadapter);
////				arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
////						android.R.layout.simple_dropdown_item_1line, bank);
////				s5.setAdapter(arrayadapter);
////				arrayadapter = new ArrayAdapter<String>(InsureActivity.this,
////						android.R.layout.simple_dropdown_item_1line, type);
////				s6.setAdapter(arrayadapter);
////
////				Button btn = (Button) InsureActivity.this
////						.findViewById(R.id.liyi_btn1);
////				// Button btn2 = (Button)InsureActivity.
////				// this.findViewById(R.id.liyi_btn2);
////				btn.setOnClickListener(new OnClickListener() {
////					@Override
////					public void onClick(View v) {// 相关告知
////						LinearLayout contents = (LinearLayout) findViewById(R.id.contents);
////						contents.removeAllViews();
////						LayoutInflater lay = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
////						LinearLayout guo_tab4 = (LinearLayout) lay.inflate(
////								R.layout.guo_tab4, null);
////						contents.addView(guo_tab4);
////					}
////				});
//			}
//		});
//	}
//	
//	public void setListViewHeightBasedOnChildren(ListView listView) {
//		ListAdapter listAdapter = listView.getAdapter();
//		if (listAdapter == null) {
//		return;
//		}
//
//		int totalHeight = 0;
//		for (int i = 0; i < listAdapter.getCount(); i++) {
//		View listItem = listAdapter.getView(i, null, listView);
//		listItem.measure(0, 0);
//		totalHeight += listItem.getMeasuredHeight();
//		}
//
//		ViewGroup.LayoutParams params = listView.getLayoutParams();
//		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//		params.height += 5;//if without this statement,the listview will be a little short
//		listView.setLayoutParams(params);
//		}

//}
