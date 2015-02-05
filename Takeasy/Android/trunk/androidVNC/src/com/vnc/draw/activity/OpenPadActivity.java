package com.vnc.draw.activity;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.vnc.draw.tools.CompassImage;

import android.androidVNC.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.RadioButton;

public class OpenPadActivity extends Activity{
private GridView gv;
private ProgressDialog progress;
private int mItemwidth = 0 ;
private int mItemHerght = 0;
private ArrayList<HashMap<String, Object>> imageList = new ArrayList<HashMap<String, Object>>();
private ArrayList<String> paths = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.openpad);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mItemwidth = dm.widthPixels;
		mItemHerght = dm.heightPixels;
		MyApplication.getInstance().setScreenWidth(mItemwidth);
		MyApplication.getInstance().setScreenHeight(mItemHerght);
		setTitle("白板预览");
		ExitApplication.getInstance().context = OpenPadActivity.this;
		ExitApplication.getInstance().addActivity(OpenPadActivity.this);
		gv = (GridView) findViewById(R.id.gv);
		//gv.setColumnWidth(mItemwidth/5);
		progress = new ProgressDialog(this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setMessage("正在加载图片，请稍候。。。");
		progress.show();
		Thread thread = new Thread(myRunnable);
		thread.start();
		
	}
	private Runnable myRunnable = new Runnable(){
		public void run() {
			getListData();
		}
	};
	private Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				notifyGv();
			}
			super.handleMessage(msg);
		}
		
	};
	private void notifyGv(){
		gv.setAdapter(new GridViewAdapter(this, imageList));
		gv.setOnItemClickListener(new gvItemListener());
		gv.setOnItemLongClickListener(new gvItemLongClickListener());
		progress.dismiss();
	}
	/**
	 * 冒泡排序法讲文件夹内的文件按照创建时间的先后顺排列
	 * @param file 需要被排序的数据
	 * @author 刘星星
	 */
	private File[] sortFile(File[] file){
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss"); // 这个是最后修改时间
		for (int a = 0; a < file.length - 1; a++) { // 最多做n-1趟排序    //冒泡排序
			for (int j = 0; j < file.length - a - 1; j++) { // 对当前无序区间score[0......length-i-1]进行排序(j的范围很关键，这个范围是在逐步缩小的)
				long time1 = Long.parseLong(date.format(file[j].lastModified()));
				long time2 = Long.parseLong(date.format(file[j + 1].lastModified()));
				if (time1 < time2) { // 把小的值交换到后面
					File temp = file[j];
					file[j] = file[j + 1];
					file[j + 1] = temp;
				}
			}
		}
		return file;
	}
	private void getListData(){
//		String root = Environment.getExternalStorageDirectory() + "/DCIM/Takeasy";
		String root = this.getIntent().getStringExtra("paths");
		File rootfile = new File(root);
		File[] file = rootfile.listFiles();
		file = sortFile(file);
		imageList.clear();
		for (int i = 0; i < file.length; i++) {
			String name = file[i].getName();
			if (!name.endsWith(".png") && !name.endsWith(".jpg") && !name.endsWith(".bmp")) {
				continue;
			}
			paths.add(file[i].getAbsolutePath());
			Bitmap bitmap = null;
			BitmapFactory.Options opts =  new  BitmapFactory.Options();
	        opts.inJustDecodeBounds =  true ;
	         BitmapFactory.decodeFile(file[i].getAbsolutePath(), opts);
	         opts.inSampleSize = CompassImage.computeSampleSize(opts, -1 , 1380 * 1380 ); 
	         opts.inJustDecodeBounds =  false ;
	         try  {
	              Bitmap bmp = BitmapFactory.decodeFile(file[i].getAbsolutePath(), opts);
	              bitmap = bmp;
				  bitmap = CompassImage.scaleImg(bitmap,200,200);
	              }catch  (OutOfMemoryError err) {
	              //	Toast.makeText(context, "内存溢出了、。。。。。", 3000).show();
	              }
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("bm", bitmap);
			imageList.add(map);
		}
		Message msg = new Message();
		msg.what = 0;
		myHandler.sendMessage(msg);
	}
	class gvItemListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		/*	ArrayList<String> arrayList = new ArrayList<String>();
			arrayList.add(paths.get(arg2));*/
			Intent it = new Intent(OpenPadActivity.this, ImageSwitcher.class);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			it.putStringArrayListExtra("pathes", paths);
			it.putExtra("index", arg2);
			startActivity(it);
		}

	}
	class gvItemLongClickListener implements OnItemLongClickListener{

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			showRootDialog(arg2);
			return true;
		}
		
	}
	public void showRootDialog(final int position) {// 保存图片弹出框
		LayoutInflater layout = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = layout.inflate(R.layout.alert_dialog, null);
		RadioButton rb1 = (RadioButton) v.findViewById(R.id.rb1);
		rb1.setText("编辑");
		RadioButton rb2 = (RadioButton) v.findViewById(R.id.rb2);
		rb2.setText("删除");
		RadioButton rb3 = (RadioButton) v.findViewById(R.id.rb3);
		final AlertDialog	alertDialog = new AlertDialog.Builder(this).setView(v)
				.setCancelable(false).create();
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setTitle("选择：");
		alertDialog.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					alertDialog.dismiss();
				}
				return false;
			}
		});
		alertDialog.show();
		rb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 编辑图片
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				alertDialog.dismiss();
				Intent intent = new Intent();
				intent.putExtra("bgPic", paths.get(position));
				intent.putExtra("back_type",1);
				intent.putExtra("bg_flag", 1);
//				startActivity(intent);
				setResult(100, intent);
				finish();
			}
		});
		rb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 删除图片
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				alertDialog.dismiss();
				
				File file = new File(paths.get(position));
				if(file.exists()){
					file.delete();
				}
				imageList.remove(position);
				paths.remove(position);
				notifyGv();
			}

		});
		rb3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
			intent.putExtra("back_type",0);
			setResult(100, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
