/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.ReleaseNewRoomActivity;
import com.zhihuigu.sosoOffice.Adapter.RoomManagerForListAdapter.ViewHolder;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.utils.BitmapCache;
import com.zhihuigu.sosoOffice.utils.ImageDownloaderId;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/1/25
 * 搜索房源后  房源列表的适配器
 *
 */
public class SearchRoomsListAdapter extends BaseAdapter implements OnClickListener{
	private Context context;
	private ArrayList<HashMap<String,Object>> list;
//	private ImageDownloaderId imageDownloader = null;
	public SearchRoomsListAdapter(Context context,ArrayList<HashMap<String,Object>> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_searchroomslist, null);
			holder.imageState = (ImageView) convertView.findViewById(R.id.state_image);
			holder.roomName = (TextView) convertView.findViewById(R.id.roomName);
			holder.roomNumberText = (TextView) convertView.findViewById(R.id.roomNumber);
			holder.roomMoney = (TextView) convertView.findViewById(R.id.roomMoney);
			holder.roomAcreage = (TextView) convertView.findViewById(R.id.roomAcreage);
			holder.callImg = (ImageView) convertView.findViewById(R.id.callImg);
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
		ImageView imageState;// 房源状态的图片
		TextView roomName;// 房源名称
		TextView roomNumberText;// 房间号
		TextView roomMoney;// 房间没平米米每天的价格
		TextView roomAcreage;// 房间面积
		ImageView callImg;

		public void setContent(int position) {
			int view_state = MyApplication.getInstance(context)
					.getRoom_list_state();
			int room_state = MyApplication.getInstance(context)
					.getRoom_state_for_examine();
			HashMap<String, Object> map = list.get(position);// 根据position得到相应布局的数据
			int state = Integer.parseInt(map.get("state").toString());
			String roomNumber = map.get("roomNumber").toString();
			String name = map.get("name").toString().trim();
			String money = "￥" + map.get("money").toString() + "元/平米/天";
			String acreage = map.get("acreage").toString() + "m²";
			String phone = map.get("phone").toString();
			if (room_state == 1) {// 审核通过的房源
				if (state == 0) {// 还没有出租的房子
					imageState.setImageResource(R.drawable.soso_house_weizu);
				} else {// 已经出租的房子
					imageState.setImageResource(R.drawable.soso_house_yizu);
				}
			} else if (room_state == 2) {
				imageState.setImageResource(R.drawable.soso_house_daisheng);
			}
			if (roomNumber != null && roomNumber.length() > 5) {
				roomNumber = roomNumber.substring(0, 5) + "...";
			}
			roomName.setText(name);
			roomNumberText.setText(roomNumber);
			roomMoney.setText(money);
			roomAcreage.setText(acreage);
			callImg.setOnClickListener(SearchRoomsListAdapter.this);
			callImg.setTag(phone);

			if (MyApplication.getInstance().getDisplayRoomPhoto() == 1) {
				roomImage.setVisibility(View.VISIBLE);
				imageState.setVisibility(View.GONE);
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
				imageState.setVisibility(View.VISIBLE);
			}
		}
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.callImg){
			String phone = v.getTag().toString();
			 //传入服务， parse（）解析号码
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            //通知activtity处理传入的call服务
            context.startActivity(intent);

		}
	}
	
}
