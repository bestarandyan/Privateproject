package com.qingfengweb.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.id.biluomiV2.ImagePreViewActivity;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.id.biluomiV2.SelectMyCustomActivity;
public class SelectMyCustomAdapter extends BaseAdapter {
	SelectMyCustomActivity context;
	List<Map<String, Object>> list;
	List<Map<String, Object>> list2;
	List<Map<String, Object>> imageList = new ArrayList<Map<String, Object>>();
//	private ImageDownloader imageDownloader = null;

	public SelectMyCustomAdapter(SelectMyCustomActivity context,
			List<Map<String, Object>> list,
			List<Map<String, Object>> list2) {
		this.context = context;
		this.list = list;
		this.list2 = list2;
//		imageDownloader = new ImageDownloader(context,20);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater layout = LayoutInflater.from(context);
			holder = new ViewHolder();
			convertView = layout.inflate(R.layout.item_selectmycustom, null);
			holder.image = (ImageView) convertView.findViewById(R.id.imageView);
			holder.gou = (ImageView) convertView.findViewById(R.id.gou);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// if(imageList.size()>0 && position != imageList.get(position).getId())
		holder.setViewContent(position);
		return convertView;
	}

	class ViewHolder {
		ImageView image;
		ImageView gou;
		CheckBox checkBox;

		public void setViewContent(int position) {
			boolean boo = false;
			if (imageList.size() > 0) {
				for (int i = 0; i < imageList.size(); i++) {
					if (position == i) {
						boo = true;
						return;
					}
				}
			}
			if (boo) {
				return;
			}
			
			gou.setId(position);
			checkBox.setId(position);
			String str = list.get(position).get("name").toString();
			if (str.length() > 4) {
				str = str.substring(0, 4) + "...";
			}
			checkBox.setText(str);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("gou", gou);
			map.put("checkBox", checkBox);
			imageList.add(map);
			image.setId(position);
			String logo = (map.get("imageid")!=null)?map.get("imageid").toString():"";
			if(logo==null || logo.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
				image.setImageResource(R.drawable.photolist_defimg);
			}else{//如果id存在
				String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.CUSTOM_IMG_URL+logo+".png";
				if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
					image.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
				}else{//如果不存在 则去服务器下载
					image.setImageResource(R.drawable.photolist_defimg);
					RequestServerFromHttp.downImage(context,image,logo,ImageType.customType.getValue(),ImgDownType.ThumbBitmap.getValue(),
							 MyApplication.getInstant().getWidthPixels()/3-10+"",
							 MyApplication.getInstant().getHeightPixels()/4-10+"",
							 false,ConstantsValues.CUSTOM_IMG_URL,R.drawable.photolist_defimg);
				}
			}
			checkBox.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					int id = v.getId();
					context.setId("");
					for (int i = 0; i < imageList.size(); i++) {
						if (id == ((ImageView) imageList.get(i).get("gou"))
								.getId()
								&& ((ImageView) imageList.get(i).get("gou"))
										.isShown()) {
							((ImageView) imageList.get(i).get("gou"))
									.setVisibility(View.GONE);
							((CheckBox) imageList.get(i).get("checkBox"))
									.setChecked(false);
						} else if (id == ((ImageView) imageList.get(i).get(
								"gou")).getId()
								&& !((ImageView) imageList.get(i).get("gou"))
										.isShown()) {
							((ImageView) imageList.get(i).get("gou"))
									.setVisibility(View.VISIBLE);
							context.setId(list.get(id).get("id").toString());
							((CheckBox) imageList.get(i).get("checkBox"))
									.setChecked(true);
						} else {
							((ImageView) imageList.get(i).get("gou"))
									.setVisibility(View.GONE);
							((CheckBox) imageList.get(i).get("checkBox"))
									.setChecked(false);
						}
					}

				}
			});
			image.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					int id = v.getId();
					String logo = (list.get(id).get("imageid")!=null)?list.get(id).get("imageid").toString():"";
					if(logo!=null && !logo.equals("")){
						String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.CUSTOM_IMG_URL+logo+".png";
						if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在 
							Intent intent = new Intent(context,
									ImagePreViewActivity.class);
							intent.putExtra("url", firstPhotoUrl);
							context.startActivity(intent);
						}
					}

				}
			});
		}

	}
}
