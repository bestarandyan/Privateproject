package com.qingfengweb.id.blm_goldenLadies;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.util.CommonUtils;
import com.qingfengweb.util.FileTools;
import com.qingfengweb.util.MD5;

public class DetailTreasureChestActivity extends BaseActivity implements
		OnGestureListener {
	private Button backBtn;// 返回百宝箱按钮
	private TextView title;// 顶部的标签
	private GestureDetector detector;// 控件的触屏事件监听
	private ViewFlipper flipper;// 滑屏控件
	private ArrayList<HashMap<String, Object>> list;// 装载整个滑屏中的数据
	ImageView image;// 每一个页面的顶部图片
	private WebView wv;// 每一个页面的文字描述区域
	private LinearLayout bottomLinear;// 底部小图片区域
	private LayoutInflater layout = null;
	private String partnerid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailtreasurechest);
		findView();
		initData();
	}

	/**
	 * 初始化控件
	 */
	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		flipper = (ViewFlipper) findViewById(R.id.vf);
		bottomLinear = (LinearLayout) findViewById(R.id.bottomLinear);
		layout = LayoutInflater.from(this);
	}

	/**
	 * 初始化数据
	 * 
	 */
	private void initData() {
//		partnerid = MyApplication.getInstance(this).getPartnerid();
		detector = new GestureDetector(this);
		list = new ArrayList<HashMap<String, Object>>();
		getVfData1();
		bottomLinear.removeAllViews();
		title.setText(getIntent().getStringExtra("partnername"));
		for (int i = 0; i < list.size(); i++) {
			flipper.addView(addView(i));
			flipper.getChildAt(i).setId(i);
			bottomLinear.addView(addBottomImageView());
			bottomLinear.getChildAt(i).setBackgroundResource(
					R.drawable.stores_dot);
		}
		if(bottomLinear.getChildCount()>0){
			bottomLinear.getChildAt(0).setBackgroundResource(
					R.drawable.stores_dot_on);
		}
	}

	/**
	 * 得到一个底部的小图片按钮 动态添加
	 * 
	 * @return
	 */
	private ImageView addBottomImageView() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 0, 0, 0);
		ImageView imageView = (ImageView) layout.inflate(
				R.layout.item_imageview, null);
		imageView.setLayoutParams(lp);
		return imageView;
	}

	/**
	 * 获取整个页面的数据
	 */
	private void getVfData() {
//		for (int i = 0; i < 5; i++) {
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("image", BitmapFactory.decodeResource(getResources(),
//					R.drawable.stores_view_tu));
//			map.put("content",
//					"auihsdf松岛枫哈的办法金卡hi都是附件为发到房间啊款到即发挨饿的房间爱打飞机啊快圣诞节反抗军阿斯顿发爱的疯狂将阿司法局抗拉伸的卷发房间啊考虑到九分裤");
//			list.add(map);
//		}
	}

	/**
	 * author Ring 获取整个页面的数据
	 */
	private void getVfData1() {
//		String name = "";
//		File file = null;
//		HashMap<String, Object> map = null;
//		List<Map<String, Object>> selectresult = DBHelper
//				.getInstance(this)
//				.selectRow(
//						"select id,name,partner_imageurl,link,partner_text from marketgoosinfo where partnerid='"
//								+ partnerid + "'", null);
//		if (list == null) {
//			list = new ArrayList<HashMap<String, Object>>();
//		}
//		for (int i = 0; i < list.size(); i++) {
//			if (list.get(i).get("image") != null) {
//				((Bitmap) list.get(i).get("image")).recycle();
//			}
//		}
//		list.clear();
//		if (selectresult != null && selectresult.size() > 0) {
//			int i;
//			for (i = 0; i < selectresult.size(); i++) {
//				map = new HashMap<String, Object>();
//				// 获取大图的方式
//				if (selectresult.get(i) != null
//						&& selectresult.get(i).get("id") != null
//						&& selectresult.get(i).get("name") != null
//						&& selectresult.get(i).get("link") != null
//						&& selectresult.get(i).get("partner_text") != null) {
//					name = MD5.getMD5(selectresult.get(i).get("id")
//							.toString()
//							+ ".jpg")
//							+ ".jpg";
//					try {
//						map.put("content",
//								selectresult.get(i).get("partner_text")
//										.toString());
//					} catch (Exception e) {
//						map.put("content","");
//					}
//					try {
//						map.put("link", selectresult.get(i).get("link")
//								.toString());
//					} catch (Exception e) {
//						map.put("link", "");
//					}
//					
//					if (selectresult.get(i).get("partner_imageurl") != null) {
//						file = new File(selectresult.get(i).get("partner_imageurl")
//								.toString());
//						if (!(file.exists() && file.isFile())) {
//							file = FileTools.getFile(
//									getResources().getString(
//											R.string.root_directory),
//									getResources().getString(
//											R.string.market_pictrues), name);
//						}
//					}else{
//						file = FileTools.getFile(
//								getResources().getString(
//										R.string.root_directory),
//								getResources().getString(
//										R.string.market_pictrues), name);
//					}
//					map.put("title", selectresult.get(i).get("name").toString());
//					map.put("image", null);
//					map.put("id", selectresult.get(i).get("id")
//							.toString());
//					map.put("file", file);
//					map.put("pixelswidth",MyApplication.getInstance(this).getWidthPixels());
//					map.put("pixelsheight", MyApplication.getInstance(this).getHeightPixels() / 3 - 10);
//					map.put("sql", "update marketgoosinfo set partner_imageurl='"+file.getAbsolutePath()+"' where id='"+selectresult.get(i).get("id")
//							.toString()+"'");
//					map.put("request_name", "PARTNER_IMAGE");
//					list.add(map);
//				}
//			}
//		}
//		if(selectresult!=null){
//			selectresult.clear();
//			selectresult =null;
//		}
	}
	@Override
	protected void onDestroy() {
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("image") != null) {
					((Bitmap) list.get(i).get("image")).recycle();
				}
			}
			list.clear();
		}
		super.onDestroy();
	}

	/**
	 * 根据list集合下标增加页面
	 * 
	 * @param position
	 *            list的下标
	 * @return
	 */
	private View addView(int position) {
		LinearLayout linear = (LinearLayout) layout.inflate(
				R.layout.item_viewflipper, null);
		image = (ImageView) linear.findViewById(R.id.imageView);
		wv = (WebView) linear.findViewById(R.id.content);
		wv.setBackgroundColor(0);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				Integer.parseInt(list.get(position).get("pixelswidth")
						.toString()), Integer.parseInt(list.get(position)
						.get("pixelsheight").toString()));
		image.setLayoutParams(params);
		boolean b = true;
		File file = (File) list.get(position).get("file");
		if (file.exists() && file.isFile()) {
			Bitmap bitmap = CommonUtils.getDrawable(file.getAbsolutePath(),
					null);
			if (bitmap != null) {
				image.setImageBitmap(bitmap);
				b= false;
			}
		}
		if (b) {
//			imageDownloader.download((File) list.get(position).get("file"),
//					list.get(position).get("sql").toString(), list
//							.get(position).get("id").toString(),
//					list.get(position).get("pixelswidth").toString(),
//					list.get(position).get("pixelsheight").toString(), list
//							.get(position).get("request_name").toString(),
//							image);
		}
		image.setId(position);
		image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String activelink = list.get(v.getId()).get("link").toString();
				if(activelink!=null&&
						!activelink.equals("")){
					if(!activelink.contains("http://")){
						activelink = "http://"+activelink;
					}
					try {
						Uri uri = Uri.parse(activelink);
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		});
		image.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				return DetailTreasureChestActivity.this.detector.onTouchEvent(event);
			}
		});
		wv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return DetailTreasureChestActivity.this.detector.onTouchEvent(event);
			}
		});
		wv.getSettings().setDefaultTextEncodingName("utf-8");
		wv.loadDataWithBaseURL("",
				list.get(position).get("content").toString(), "text/html",
				"utf-8", "");
		return linear;
	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			Intent i = new Intent(this, TreasureChestActivity.class);
			startActivity(i);
			finish();
		}
		super.onClick(v);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(this, TreasureChestActivity.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(list!=null&&list.size()>=2){

			if (e1.getX() - e2.getX() > 120) {
				this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_out));
				this.flipper.showNext();
				for (int i = 0; i < list.size(); i++) {
					if (i == this.flipper.getCurrentView().getId()) {
						bottomLinear.getChildAt(i).setBackgroundResource(
								R.drawable.stores_dot_on);
					} else {
						bottomLinear.getChildAt(i).setBackgroundResource(
								R.drawable.stores_dot);
					}
				}
				return true;
			} else if (e1.getX() - e2.getX() < -120) {
				this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_out));
				this.flipper.showPrevious();
				for (int i = 0; i < list.size(); i++) {
					if (i == this.flipper.getCurrentView().getId()) {
						bottomLinear.getChildAt(i).setBackgroundResource(
								R.drawable.stores_dot_on);
					} else {
						bottomLinear.getChildAt(i).setBackgroundResource(
								R.drawable.stores_dot);
					}
				}
				return true;
			}
		}
		return false;
	}
}
