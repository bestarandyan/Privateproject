package com.chinaLife.claimAssistant.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinaLife.claimAssistant.Interface.sc_ProgressBarInterface;
import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.activity.Sc_CaseHandlerFlowActivity;
import com.chinaLife.claimAssistant.activity.Sc_MyApplication;
import com.chinaLife.claimAssistant.thread.sc_PicHandler;
import com.chinaLife.claimAssistant.tools.sc_AsyncImageLoader;
import com.chinaLife.claimAssistant.tools.sc_BitmapCache;
import com.chinaLife.claimAssistant.tools.sc_EncryptPhoto;
import com.chinaLife.claimAssistant.tools.sc_MD5;
import com.chinaLife.claimAssistant.tools.sc_AsyncImageLoader.ImageCallback;

public class sc_CaseHandlerFlowAdapter extends BaseAdapter {
	ArrayList<HashMap<String, Object>> list = null;
	public Sc_CaseHandlerFlowActivity c;
	private ViewHolder holder = null;
	private int mPosition=0;
	private sc_AsyncImageLoader asyncImageLoader;
	sc_PicHandler ph = null;

	public sc_CaseHandlerFlowAdapter(Sc_CaseHandlerFlowActivity context,ArrayList<HashMap<String, Object>> list) {
		this.c = context;
		this.list = list;
		File file = new File(Sc_MyApplication.cache);
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
			
			image = (ImageView) layout.findViewById(R.id.image);
			t = (TextView) layout.findViewById(R.id.text);
			p = (ProgressBar) layout.findViewById(R.id.progressBar);
			numberUpload = (TextView) layout
					.findViewById(R.id.numberUpload);
			gou = (ImageView) layout.findViewById(R.id.gou);
			p.getProgressDrawable().setAlpha(50);
			layout.setOnClickListener(null);
		}

		public void setContent(HashMap<String, Object> map) {
			image.setId(mPosition);
			p.getProgressDrawable().setAlpha(50);
			
			image.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					ContentValues values = new ContentValues();
					values.put("status", 0);
					Sc_CaseHandlerFlowActivity.listPosition = v.getId();
					Sc_CaseHandlerFlowActivity.legendid = (Integer) list
							.get(Sc_CaseHandlerFlowActivity.listPosition)
							.get("legendid");
					final int state = (Integer) list
							.get(Sc_CaseHandlerFlowActivity.listPosition)
							.get("state");
					final String file = list
							.get(Sc_CaseHandlerFlowActivity.listPosition)
							.get("savepath").toString();
					try {
						if (!list.get(Sc_CaseHandlerFlowActivity.listPosition)
								.get("reviewreason").toString().equals("")) {
							AlertDialog.Builder callDailog = new AlertDialog.Builder(
									c);
							callDailog
									.setIcon(android.R.drawable.ic_dialog_info)
									.setTitle("不合格的原因")
									.setMessage(
											list.get(
													Sc_CaseHandlerFlowActivity.listPosition)
													.get("reviewreason")
													.toString())
									.setPositiveButton(
											"重拍",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													c.showQianDaoDialog(state,
															file);
												}
											}).setNegativeButton("取消", null)
									.show();
						} else {
							c.showQianDaoDialog(state, file);
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
					String name = sc_MD5.getMD5(imageUrl)
							+ imageUrl.substring(imageUrl.lastIndexOf("."));
					File file = new File(Sc_MyApplication.cache, name);
					// 如果图片存在本地缓存目录，则不去服务器下载
					if (file.exists()) {
						BitmapFactory.Options opts = new BitmapFactory.Options();
						opts.inJustDecodeBounds = true;
						BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
						opts.inSampleSize = ph.computeSampleSize(opts, -1,
								130 * 130);
						opts.inJustDecodeBounds = false;
						if (BitmapFactory.decodeFile(file.getAbsolutePath(),
								opts) != null) {
							image.setImageBitmap(BitmapFactory.decodeFile(
									file.getAbsolutePath(), opts));
							map.put("savepath", file.getAbsoluteFile());
						} else {
							Bitmap bitmap = sc_EncryptPhoto.decode(file);
							if(bitmap!=null){
								map.put("savepath", file.getAbsoluteFile());
								image.setImageBitmap(bitmap);
							}else{
								image.setImageResource(R.drawable.sc_icon_default_photo);
							}
						}
					} else {
						Drawable cachedImage = asyncImageLoader.loadDrawable(
								photoid, imageUrl, new ImageCallback() {
									public void imageLoaded(
											Drawable imageDrawable,
											String imageUrl) {
										image.setImageDrawable(imageDrawable);
									}
								});
						if (cachedImage == null) {
							image.setImageResource(R.drawable.sc_icon_default_photo);
						} else {
							image.setImageDrawable(cachedImage);
						}
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
				Sc_CaseHandlerFlowActivity.myprogressbars.remove(mPosition);
				Sc_CaseHandlerFlowActivity.myprogressbars.add(mPosition, map1);
			} catch (Exception e) {
				Sc_CaseHandlerFlowActivity.myprogressbars.add(map1);
			}
		}
	}
}