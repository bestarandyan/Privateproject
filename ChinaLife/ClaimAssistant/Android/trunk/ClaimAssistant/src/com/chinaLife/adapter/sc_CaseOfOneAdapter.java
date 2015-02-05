package com.chinaLife.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinaLife.claimAssistant.sc_CaseOfOnlyOneActivity;
import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.sc_MyCameraActivity;
import com.chinaLife.claimAssistant.R;
import com.chinaLife.claimAssistant.thread.sc_PicHandler;
import com.chinaLife.claimAssistant.tools.sc_AsyncImageLoader;
import com.chinaLife.claimAssistant.tools.sc_BitmapCache;
import com.chinaLife.claimAssistant.tools.sc_CommonUtils;
import com.chinaLife.claimAssistant.tools.sc_EncryptPhoto;
import com.chinaLife.claimAssistant.tools.sc_ImageDownloaderId;
import com.chinaLife.claimAssistant.tools.sc_MD5;

public class sc_CaseOfOneAdapter extends BaseAdapter {
	ArrayList<HashMap<String, Object>> list = null;
	public sc_CaseOfOnlyOneActivity c;
	private ViewHolder holder = null;
	private int mPosition=0;
	private sc_AsyncImageLoader asyncImageLoader;
	sc_PicHandler ph = null;
	private String filepath;

	public sc_CaseOfOneAdapter(sc_CaseOfOnlyOneActivity context,ArrayList<HashMap<String, Object>> list) {
		this.c = context;
		this.list = list;
		File file = new File(sc_MyApplication.cache);
		file.mkdirs();
		ph = new sc_PicHandler();
		asyncImageLoader = new sc_AsyncImageLoader();

	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		mPosition = position;
		if (convertView == null) {
			convertView = LayoutInflater.from(c).inflate(R.layout.sc_image_item,
					null);
			holder = new ViewHolder(convertView);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.t = (TextView) convertView.findViewById(R.id.text);
			holder.p = (ProgressBar) convertView.findViewById(R.id.progressBar);
			holder.numberUpload = (TextView) convertView
					.findViewById(R.id.numberUpload);
			holder.gou = (ImageView) convertView.findViewById(R.id.gou);
			holder.p.getProgressDrawable().setAlpha(50);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setContent(list.get(position));
		return convertView;
	}

	private class ViewHolder {
		ImageView image;
		TextView t;
		ProgressBar p;
		TextView numberUpload;
		ImageView gou;

		public void gouCheck(int state) {
			if (state == 1) {
				gou.setBackgroundResource(R.drawable.sc_kg_tip);
				gou.setVisibility(View.VISIBLE);
			} else if (state == 2) {
				gou.setBackgroundResource(R.drawable.sc_cp_tip);
				gou.setVisibility(View.VISIBLE);
			} else if (state == 3) {
				gou.setBackgroundResource(R.drawable.sc_check);
				gou.setVisibility(View.VISIBLE);
			} else if (state == 4) {
				gou.setBackgroundResource(R.drawable.sc_bc_tip);
				gou.setVisibility(View.VISIBLE);
			} else {
				gou.setVisibility(View.GONE);
			}
			gou.setDrawingCacheEnabled(false);
			gou.destroyDrawingCache();
		}
		
		public ViewHolder(View layout) {
			
			
		}

		public void setContent(HashMap<String, Object> map) {
			image.setId(mPosition);
			image.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					ContentValues values = new ContentValues();
					values.put("status", 0);
					sc_CaseOfOnlyOneActivity.listPosition = v.getId();
					sc_CaseOfOnlyOneActivity.legendid = (Integer) list
							.get(sc_CaseOfOnlyOneActivity.listPosition)
							.get("legendid");
					final int state = (Integer) list
							.get(sc_CaseOfOnlyOneActivity.listPosition)
							.get("state");
					if(state == 1){
						String photoid = list
								.get(sc_CaseOfOnlyOneActivity.listPosition).get("photoid").toString();
						if (photoid != null && !photoid.equals("")) {
							Bitmap bm = null;
							File files = new File(android.os.Environment
									.getExternalStorageDirectory() + "/DCIM/ChinaLife");
							if (!files.exists()) {
								files.mkdirs();
							}
							File file = new File(files, sc_MD5.getMD5(photoid) + ".png");
							if(!file.exists()){
								filepath = list
										.get(sc_CaseOfOnlyOneActivity.listPosition)
										.get("savepath").toString();
							}else{
								filepath = file.getAbsolutePath();
							}
							
						}
					}else{
						filepath = list
								.get(sc_CaseOfOnlyOneActivity.listPosition)
								.get("savepath").toString();
					}
					try {
						if (!list.get(sc_CaseOfOnlyOneActivity.listPosition)
								.get("reviewreason").toString().equals("")) {
							AlertDialog.Builder callDailog = new AlertDialog.Builder(
									c);
							callDailog
									.setIcon(android.R.drawable.ic_dialog_info)
									.setTitle("不合格的原因")
									.setMessage(
											list.get(
													sc_CaseOfOnlyOneActivity.listPosition)
													.get("reviewreason")
													.toString())
									.setPositiveButton(
											"重拍",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													c.showQianDaoDialog(state,
															filepath);
												}
											}).setNegativeButton("取消", null)
									.show();
						} else {
							if(sc_MyApplication.getInstance().getCasedescription_tag() == 1){
								if(filepath.endsWith(".jpg")){
									c.showQianDaoDialog(3, filepath);
								}else{
									sc_MyApplication.getInstance().setLegendid((Integer) list
											.get(sc_CaseOfOnlyOneActivity.listPosition)
											.get("legendid"));
									sc_MyApplication.getInstance().setHandPicFlag(1);// 设置标记为拍摄的图片
									Intent i = new Intent(c,
											sc_MyCameraActivity.class);
									c.startActivityForResult(i, 1);
								}
								
							}else{
								c.showQianDaoDialog(state, filepath);
							}
						}
					} catch (Exception e) {

					}

				}
			});
			if (map.get("savepath").toString().contains(".")) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(map.get("savepath").toString(), opts);
				opts.inSampleSize = ph.computeSampleSize(opts, -1, 130 * 130);
				opts.inJustDecodeBounds = false;
				if (BitmapFactory.decodeFile(map.get("savepath").toString(),
						opts) != null) {
					image.setImageBitmap(BitmapFactory.decodeFile(
							map.get("savepath").toString(), opts));
				}else {
					image.setImageBitmap(sc_EncryptPhoto.decode(new File(map.get("savepath").toString())));
				}
			} else {
				if (Integer.parseInt(map.get("savepath").toString()) == R.drawable.sc_icon_default_photo) {
					String imageUrl = map.get("uploadpath").toString();
					String photoid = map.get("photoid").toString();
					if (photoid != null && !photoid.equals("")) {
						Bitmap bm = null;
						File file = new File(android.os.Environment
								.getExternalStorageDirectory() + "/DCIM/ChinaLife");
						if (!file.exists()) {
							file.mkdirs();
						}
						File headFile = new File(file, sc_MD5.getMD5(photoid) + ".png");
						bm = sc_CommonUtils.getDrawable(headFile.getAbsolutePath(), null);
						if (bm != null) {
							image.setImageBitmap(bm);
						} else {
							sc_ImageDownloaderId imagedownloaderid = new sc_ImageDownloaderId(
								c, 10);
							imagedownloaderid.download(headFile, photoid, image);
						}
					} else {
						image.setImageResource(R.drawable.sc_icon_default_photo);
					}
				} else {
					try {
						image.setImageResource(Integer.parseInt(map.get(
								"savepath").toString()));
					} catch (OutOfMemoryError e) {
						image.setImageBitmap(sc_BitmapCache.getInstance()
								.getBitmap(
										Integer.parseInt(map.get("savepath")
												.toString()), c, null));
					}
				}
			}
			String text1 = (String) map.get("name");
			t.setText(text1);
			int state;
			try {
				state = Integer.parseInt(map.get("pic_state").toString());
			} catch (Exception e) {
				state = -1;
			}
			if(state ==1 ||state == 3){
				map.put("state", 1);
				map.put("checked", 2);
			}
			gouCheck(state);
			if (Integer.parseInt(map.get("checked").toString()) == 1) {
				image.setClickable(false);
			} else {
				image.setClickable(true);
			}
			
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("legendid", map.get("legendid"));
			map1.put("progressbar", p);
			map1.put("image", image);
			map1.put("numberupload", numberUpload);
			try {
				sc_CaseOfOnlyOneActivity.myprogressbars.remove(mPosition);
				sc_CaseOfOnlyOneActivity.myprogressbars.add(mPosition, map1);
			} catch (Exception e) {
				sc_CaseOfOnlyOneActivity.myprogressbars.add(map1);
			}
		}
	}
}