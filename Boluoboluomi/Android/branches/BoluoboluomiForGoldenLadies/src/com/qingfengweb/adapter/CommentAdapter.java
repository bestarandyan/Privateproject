package com.qingfengweb.adapter;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;

public class CommentAdapter  extends BaseAdapter{
	Context context;
	List<Map<String,Object>> list ; 
//	private ImageDownloader imageDownloader = null;
	int width = MyApplication.getInstant().getWidthPixels() / 4;
	int height = width;
	public CommentAdapter(Context context,List<Map<String,Object>> list ) {
		this.list = list;
		this.context = context;
//		imageDownloader = new ImageDownloader(context,20);
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			LayoutInflater layout = LayoutInflater.from(context);
			holder = new ViewHolder();
			convertView = layout.inflate(R.layout.item_comment, null);
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.parent);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.gonghao = (TextView) convertView.findViewById(R.id.gonghao);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.dengji = (TextView) convertView.findViewById(R.id.dengji);
			holder.style = (TextView) convertView.findViewById(R.id.takePicStyle);
			holder.xuanyan = (TextView) convertView.findViewById(R.id.serviceXuanYan);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position%2 == 1){
			holder.layout.setBackgroundColor(Color.rgb(241, 241, 241));
		}else{
			holder.layout.setBackgroundColor(Color.rgb(255, 255, 255));
		}
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//				Integer.parseInt(list.get(position).get("pixelswidth")
//						.toString()), Integer.parseInt(list.get(position)
//						.get("pixelsheight").toString()));
//		holder.image.setLayoutParams(params);
		Map<String,Object> map = list.get(position);
		if(map!=null){
			String employeeid = map.get("employeeid").toString();
			String name = map.get("name").toString();
			String level = map.get("level").toString();
			String style = map.get("style").toString();
			String declaration = map.get("declaration").toString();
			holder.gonghao.setText(employeeid);
			holder.name.setText(name);
			holder.dengji.setText(level);
			holder.style.setText(style);
			holder.xuanyan.setText(declaration);
			
			String thumb = map.get("photoid").toString();
			if(thumb==null || thumb.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
				holder.image.setImageResource(R.drawable.photolist_defimg);
			}else{//如果id存在
				String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.VALUATION_IMG_URL+thumb+".png";
				if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
					holder.image.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
				}else{//如果不存在 则去服务器下载
					holder.image.setImageResource(R.drawable.photolist_defimg);
					RequestServerFromHttp.downImage(context,holder.image,thumb,ImageType.valuationPersons.getValue(),ImgDownType.ThumbBitmap.getValue(),
							 width+"",height+"",false,ConstantsValues.VALUATION_IMG_URL,R.drawable.photolist_defimg);
				}
			}
		}
		return convertView;
	}
	class ViewHolder{
		RelativeLayout layout;
		ImageView image;
		TextView gonghao,name,dengji,style,xuanyan;
		public void setViewContent(int position){
			
			
		}
		}
}
