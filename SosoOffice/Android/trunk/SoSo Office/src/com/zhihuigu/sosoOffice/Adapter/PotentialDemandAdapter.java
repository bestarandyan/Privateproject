/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.utils.BitmapCache;
import com.zhihuigu.sosoOffice.utils.ImageDownloaderId;

/**
 * @author 刘星星
 * @createDate 2013/1/31
 * 潜在需求适配器
 *
 */
public class PotentialDemandAdapter extends BaseAdapter implements OnClickListener{
	private Context context;
	private ArrayList<HashMap<String,Object>> list;
//	private ImageDownloaderId imageDownloader = null;
	public PotentialDemandAdapter(Context context,
			ArrayList<HashMap<String,Object>> list) {
		this.context = context;
		this.list = list;
//		imageDownloader = new ImageDownloaderId(context, 10);
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_potentiallistview, null);
			holder.roomName = (TextView) convertView.findViewById(R.id.roomName);
			holder.roomNumberText = (TextView) convertView.findViewById(R.id.roomNumber);
			holder.roomMoney = (TextView) convertView.findViewById(R.id.roomMoney);
			holder.roomAcreage = (TextView) convertView.findViewById(R.id.roomAcreage);
			holder.people = (TextView) convertView.findViewById(R.id.people);
			holder.roomImage = (ImageView) convertView.findViewById(R.id.room_image);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position%2==0){
			convertView.setBackgroundColor(Color.rgb(242,242,242));
		}else{
			convertView.setBackgroundColor(Color.rgb(248,248,248));
		}
		holder.setContent(position);
		return convertView;
	}

	class ViewHolder {
		ImageView roomImage;// 房源图片
		TextView roomName;// 房源名称
		TextView roomNumberText;// 房间号
		TextView roomMoney;// 房间没平米米每天的价格
		TextView roomAcreage;// 房间面积
		TextView people;

		public void setContent(int position) {
			HashMap<String, Object> map = list.get(position);// 根据position得到相应布局的数据
			String roomNumber = map.get("number").toString();
			String name = map.get("name").toString();
			String money = map.get("money").toString();
			String acreage = map.get("acreage").toString();
			String peopleStr = map.get("people").toString();
			if (money.length() > 0)
				money = "￥" + money + "元/㎡/天";
			if (roomNumber.length() > 0)
				roomNumber = roomNumber + "号";
			if (acreage.length() > 0)
				acreage += "㎡";
			roomName.setText(name);
			roomNumberText.setText(roomNumber);
			roomMoney.setText(money);
			roomAcreage.setText(acreage);
			people.setSingleLine(true);
			people.setText(peopleStr);
			if (MyApplication.getInstance().getDisplayRoomPhoto() == 1) {
				roomImage.setImageResource(R.drawable.soso_gray_logo);
				boolean b = true;
				File file = (File) list.get(position).get("file");
				if (file.exists() && file.isFile()) {
					Bitmap bitmap = BitmapCache.getInstance().getBitmap(file,
							context);
					if (bitmap != null) {
						roomImage.setImageBitmap(bitmap);
						b = false;
					}
				}
				if (b) {
//					roomImage.setImageResource(R.drawable.soso_gray_logo);
					MyApplication.getInstance(context).getImageDownloaderId().download(
							(File) list.get(position).get("file"),
							list.get(position).get("sql").toString(),
							list.get(position).get("id").toString(),
							list.get(position).get("pixelswidth").toString(),
							list.get(position).get("pixelsheight").toString(),
							list.get(position).get("request_name").toString(),
							roomImage, "0", "0",
							list.get(position).get("pixelswidth").toString(),
							list.get(position).get("pixelsheight").toString());
				}
			} else {
				roomImage.setVisibility(View.GONE);
			}
		}
	}
	@Override
	public void onClick(View v) {
	}
	
}
