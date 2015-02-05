/**
 * 
 */
package com.qingfengweb.adapter;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;

/**
 * @author 刘星星 武国庆
 * @createDate 2013/8/26
 * 活动分享列表适配器
 *
 */
public class ActiveShareAdapter extends BaseAdapter{
	List<Map<String,Object>> list = null;
	Context context;
	public ActiveShareAdapter(Context context,List<Map<String,Object>> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_activeshare, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.photo);
//			holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
//			holder.oldMoney = (TextView) convertView.findViewById(R.id.itemOldMoney);
//			holder.nowMoney = (TextView) convertView.findViewById(R.id.itemNowMoney);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		String imgId = list.get(position).get("imageid").toString();
		String fileUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.ACTIVE_IMG_URL+imgId+".png";
		File file = new File(fileUrl);
		if(file.exists()){
			Bitmap bitmap = BitmapFactory.decodeFile(fileUrl);
			holder.img.setImageBitmap(bitmap);
		}else{//如果图片不存在则从服务器下载图片
			if(imgId!=null && !imgId.equals("")){
				RequestServerFromHttp.downImage(context,holder.img,imgId,ImageType.ActiveImg.getValue(),ImgDownType.BigBitmap.getValue(),
						MyApplication.getInstant().getWidthPixels()+"","0",false,ConstantsValues.ACTIVE_IMG_URL,R.drawable.id_logo);
			}
		}
		return convertView;
	}
	class ViewHolder{
		ImageView img;
//		TextView title,oldMoney,nowMoney;
	}
}
