/**
 * 
 */
package com.boluomi.children.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.boluomi.children.R;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.UserBeanInfo;
import com.boluomi.children.database.DBHelper;
import com.boluomi.children.model.GrowUpImgInfo;
import com.boluomi.children.model.GrowUpInfo;
import com.boluomi.children.util.CommonUtils;
import com.boluomi.children.view.HorizontalImageGallery;
import com.qingfengweb.imagehandle.PicHandler;

/**
 * @author 刘星星
 * 发布成长经历
 *
 */
public class GrowUpPhotoSendActivity extends BaseActivity{
	private Button backBtn,sendBtn;
	private EditText niCheng,age,saySomeThing;
	private HorizontalImageGallery gallery;
	private List<Map<String, Object>> imgList;
	private PopupWindow selectPopupWindow = null;
	LinearLayout parent;
	
	private String fileName = "";
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//路径   
	private File sdcardTempFile = null;
	private String path = "";//图片的最终路径
	private final String IMAGE_TYPE = "image/*";
	public String currentFilePath = "";//当前正在上传的文件路径
	DBHelper dbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_growupphotosend);
		findview();
		initData();
	}
	private void initData(){
		dbHelper  = DBHelper.getInstance(this);
		imgList = new ArrayList<Map<String,Object>>();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.soso_house_paijiao);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("path", "");
		map.put("img", bitmap);
		imgList.add(0, map);
		notifiView();
	}
	public void notifiView() {
		gallery.setAdapter(new GalleryAdapter());
		if(imgList.size()>1){
			gallery.setSelection(1);
		}
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}else if(v.getId() == R.id.paizhaoLayout){//拍照
			dismiss();
			Intent intent = new Intent(this,TakePhotoActivity.class);
			intent.putExtra("photoType", 1);
			startActivityForResult(intent, 1);
//			takePhoto();
		}else if(v.getId() == R.id.selectPhotoLayout){//从手机中选择
			dismiss();
			Intent intent = new Intent(this,TakePhotoActivity.class);
			intent.putExtra("photoType", 2);
			startActivityForResult(intent, 1);
//			getAlbumPhoto();
		}else if(v == sendBtn){
			String id = CommonUtils.getFileName();
			ContentValues values = new ContentValues();
			values.put("id", id);
			values.put("nicheng", niCheng.getText().toString());
			values.put("age", age.getText().toString());
			values.put("remark", saySomeThing.getText().toString());
			values.put("userid", UserBeanInfo.getInstant().getUserid());
			dbHelper.insert(GrowUpInfo.tablename, values);
			for(int i=1;i<imgList.size();i++){
				Map<String,Object> map = imgList.get(i);
				ContentValues values1 = new ContentValues();
				values1.put("id", "");
				values1.put("growupid", id);
				values1.put("imgLocalUrl", map.get("imgpath").toString());
				values1.put("photoname", map.get("imgInfo").toString());
				values1.put("userid", UserBeanInfo.getInstant().getUserid());
				dbHelper.insert(GrowUpImgInfo.tablename, values1);
			}
//			List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
//			for(int i=0;i<imgList.size();i++){
//				Map<String,Object> map = new HashMap<String, Object>();
//				map.put("imgpath", imgList.get(i).get("imgpath"));
//				map.put("imageid", "1");
//				list1.add(map);
//			}
			Intent intent = new Intent();
//			intent.putExtra("photoList", (Serializable)list1);
			setResult(1, intent);
			finish();
		}
		super.onClick(v);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1){
			if(data == null){
				return;
			}
			Map<String,Object> imgInfoMap = (Map<String, Object>) data.getSerializableExtra("imgInfo");
			String imgUrl = imgInfoMap.get("imgUrl").toString();
			if(imgUrl!=null && !imgUrl.equals("")){//拍照返回处理
					currentFilePath = imgUrl;
					Bitmap bitmap = null;
					int size = (MyApplication.getInstant().getWidthPixels()-4)/3;
					bitmap = PicHandler.getSquareImg(imgUrl,size);
					if(bitmap !=null){
						bitmap = PicHandler.cutImg(bitmap,size);
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("img", bitmap);
					map.put("imgpath", currentFilePath);
					map.put("imageid", "1");
					map.put("imgInfo", imgInfoMap.get("photoname").toString());
					imgList.add(1,map);
					notifiView();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg2 == 0){
			popupWindwShowing();
		}
		super.onItemClick(arg0, arg1, arg2, arg3);
	}
	/**
	 * 
	 * @author 刘星星
	 * @createDate 2013/1/23 获取图片的适配器类
	 * 
	 */
	class GalleryAdapter extends BaseAdapter {
//		private ImageDownloaderId imageDownloader = null;

		public GalleryAdapter() {
//			imageDownloader = new ImageDownloaderId(GrowUpPhotoSendActivity.this,
//					10);
		}

		public int getCount() {
			return imgList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("deprecation")
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(GrowUpPhotoSendActivity.this)
						.inflate(R.layout.item_gallery, null);
				holder.galleryImage = (ImageView) convertView
						.findViewById(R.id.galleryImage);
				holder.galleryCancleImage = (ImageView) convertView
						.findViewById(R.id.galleryCancleImage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Map<String,Object> map = imgList.get(position);
			Bitmap bitmap = (Bitmap) map.get("img");
			if(position == 0){
				holder.galleryImage.setBackgroundResource(R.drawable.soso_house_paijiao);
			}else{
				holder.galleryImage.setImageBitmap(bitmap);
			}
			
			int size = (MyApplication.getInstant().getWidthPixels()-4)/3;
			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(size, size);
			param.setMargins(0, 10, 10, 0);
			holder.galleryImage.setLayoutParams(param);
			Gallery.LayoutParams param1 = new Gallery.LayoutParams(size, size);
			convertView.setLayoutParams(param1);
			if(position == 0){
				holder.galleryCancleImage.setVisibility(View.INVISIBLE);
			}else{
				holder.galleryCancleImage.setVisibility(View.VISIBLE);
			}
			holder.galleryCancleImage.setTag(position);
			holder.galleryCancleImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final int position = (Integer) v.getTag();
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							GrowUpPhotoSendActivity.this);
					dialog.setTitle("提示！");
					dialog.setIcon(android.R.drawable.ic_dialog_alert);
					dialog.setMessage("确定要删除该图片吗？");
					dialog.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									imgList.remove(position);
									notifiView();
								}
							});
					dialog.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									return;
								}
							});
					dialog.create().show();
				}
			});
//			convertView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			return convertView;
		}

		class ViewHolder {
			ImageView galleryImage;
			ImageView galleryCancleImage;
		}
	}
	private void findview(){
		backBtn = (Button) findViewById(R.id.backBtn);
		sendBtn = (Button) findViewById(R.id.sendBtn);
		niCheng = (EditText) findViewById(R.id.nicheng);
		age = (EditText) findViewById(R.id.ageTV);
		saySomeThing = (EditText) findViewById(R.id.saySomethingEt);
		gallery = (HorizontalImageGallery) findViewById(R.id.gallery);
		parent = (LinearLayout) findViewById(R.id.parent);
		backBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		gallery.setOnItemClickListener(this);
		gallery.setSelection(200);
		
	}
	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}
	/**
	 * 显示PopupWindow窗口
	 * 
	 * @param popupwindow
	 */
	public void popupWindwShowing() {
		// 将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
		// 这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
		// （是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
//		selectPopupWindow.showAsDropDown(parent, 0,
//				-(parent.getHeight() - chazhi));
		View layout = (View) this.getLayoutInflater().inflate(
				R.layout.menu_myalbum, null);
		LinearLayout bottomLayout = (LinearLayout) layout.findViewById(R.id.bottomLayout);
		layout.findViewById(R.id.paizhaoLayout).setOnClickListener(this);
		layout.findViewById(R.id.selectPhotoLayout).setOnClickListener(this);
		layout.findViewById(R.id.closeWindowBtn).setOnClickListener(this);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (MyApplication.getInstant().getHeightPixels()*0.3));
		bottomLayout.setLayoutParams(param);
		selectPopupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dialog_border));
		selectPopupWindow.setOutsideTouchable(false);
		selectPopupWindow.setAnimationStyle(R.style.PopupAnimation);  
		selectPopupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.BOTTOM, 0, 0);  
		selectPopupWindow.update();  
	}
}
