/**
 * 
 */
package com.qingfengweb.adapter;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;

/**
 * @author 刘星星 武国庆
 * 
 *
 */
public class RecommendSeriesAdapter extends BaseAdapter{
	List<Map<String,Object>> list = null;
	Context context;
	int width = MyApplication.getInstant().getWidthPixels()/4;
	int height = width;
	public RecommendSeriesAdapter(Context context,List<Map<String,Object>> list) {
		this.context = context;
		this.list = list;
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
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_recommendseries, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.photo);
			holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
			holder.oldMoney = (TextView) convertView.findViewById(R.id.itemOldMoney);
			holder.nowMoney = (TextView) convertView.findViewById(R.id.itemNowMoney);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		RelativeLayout.LayoutParams param= new RelativeLayout.LayoutParams(width, height);
		holder.img.setLayoutParams(param);
		Map<String,Object> map = list.get(position);
		String title = map.get("name").toString();
		String price1 = map.get("price1").toString();
		String price2 = map.get("price2").toString();
		holder.title.setText(title);
		holder.oldMoney.setText("原价："+price1);
		holder.nowMoney.setText("限时特价："+price2);
		
		String imgId = map.get("imageid").toString();
		if(imgId==null || imgId.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
			holder.img.setImageResource(R.drawable.photolist_defimg);
		}else{//如果id存在
			String photoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.RECOMMENDSERIES_IMG_URL_THUMB+imgId+".png";
			if(new File(photoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
				holder.img.setImageBitmap(BitmapFactory.decodeFile(photoUrl));
			}else{//如果不存在 则去服务器下载
				holder.img.setImageResource(R.drawable.photolist_defimg);
				RequestServerFromHttp.downImage(context,holder.img,imgId,ImageType.recommendSeries.getValue(),ImgDownType.ThumbBitmap.getValue(),
						 width+"",height+"",false,ConstantsValues.RECOMMENDSERIES_IMG_URL_THUMB,R.drawable.photolist_defimg);
			}
		}
		return convertView;
	}
	class ViewHolder{
		ImageView img;
		TextView title,oldMoney,nowMoney;
	}
}
