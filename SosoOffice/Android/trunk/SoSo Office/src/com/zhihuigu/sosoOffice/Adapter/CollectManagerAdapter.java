/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.WriteNewLetterActivity;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.BitmapCache;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

/**
 * @author 刘星星
 * @createDate 2013/2/1
 * 收藏管理的适配器
 *
 */
public class CollectManagerAdapter  extends BaseAdapter implements OnClickListener{
	public ArrayList<HashMap<String,Object>> list;
	public Context context;
	public int type = 0;//0代表非编辑状态  1代表编辑状态
	Dialog dialog = null;
	
	
	private SoSoUploadData uploaddata;// 服务器请求对象
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private ProgressDialog progressdialog = null;
	private int position = 0;
	
//	private ImageDownloaderId imageDownloader = null;
	
	public CollectManagerAdapter(Context context,ArrayList<HashMap<String,Object>> list,int type) {
		this.context  = context;
		this.list = list;
		this.type =  type;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_collectmanager, null);
			holder.imageState = (ImageView) convertView.findViewById(R.id.state_image);
			holder.roomName = (TextView) convertView.findViewById(R.id.room_name);
			holder.roomNumberText = (TextView) convertView.findViewById(R.id.roomNumber);
			holder.roomMoney = (TextView) convertView.findViewById(R.id.roomMoney);
			holder.roomAcreage = (TextView) convertView.findViewById(R.id.roomAcreage);
			holder.jianTou = (ImageView) convertView.findViewById(R.id.jiantou);
			holder.sendMessage = (ImageView) convertView.findViewById(R.id.sendMessage);
			holder.delete = (ImageView) convertView.findViewById(R.id.delete);
			holder.telImg = (ImageView) convertView.findViewById(R.id.telImg);
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
		ImageView jianTou;// 最右边的箭头
		ImageView sendMessage;// 发送站内信
		ImageView delete;// 取消收藏
		ImageView telImg;// 打电话

		public void setContent(int position) {
			HashMap<String, Object> map = list.get(position);// 根据position得到相应布局的数据
			int state = Integer.parseInt(map.get("state").toString());
			String roomNumber = map.get("roomNumber").toString();
			String name = map.get("name").toString();
			String money = "￥" + map.get("money").toString() + "元/平米/天";
			String acreage = map.get("acreage").toString() + "m²";
			String phone = map.get("phone").toString();
			
			if (state == 1) {// 还没有出租的房子
				imageState.setImageResource(R.drawable.soso_house_weizu);
			} else if (state == 2) {// 已经出租的房子
				imageState.setImageResource(R.drawable.soso_house_yizu);
			} else if (state == 4) {// 还没有出租的房子
				imageState.setImageResource(R.drawable.soso_house_weizu);
			}
			if (roomNumber.length() > 0) {
				roomNumberText.setText(roomNumber + "号");
			} else {
				roomNumberText.setVisibility(View.GONE);
			}
			if (type == 0) {// 非编辑状态
				sendMessage.setVisibility(View.GONE);
				delete.setVisibility(View.GONE);
				jianTou.setVisibility(View.VISIBLE);
			} else if (type == 1) {// 编辑状态
				sendMessage.setVisibility(View.VISIBLE);
				sendMessage.setTag(position);
				sendMessage.setOnClickListener(CollectManagerAdapter.this);
				jianTou.setVisibility(View.GONE);
				delete.setVisibility(View.VISIBLE);
				delete.setTag(position);
				
				delete.setOnClickListener(CollectManagerAdapter.this);
			}
			if (MyApplication.getInstance().getDisplayRoomPhoto() == 1) {
				roomImage.setVisibility(View.VISIBLE);
				imageState.setVisibility(View.GONE);
				if(type == 1){
					if (name.length() > 3)
						name = name.substring(0, 3) + "...";
				}
				roomImage.setImageResource(R.drawable.soso_gray_logo);
				boolean b = true;
				File file = (File) list.get(position).get("file");
				if (file.exists() && file.isFile()) {
					Bitmap bitmap = BitmapCache.getInstance().getBitmap(file,	context);
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
				if(type == 1){
					if (name.length() > 5)
						name = name.substring(0, 5) + "...";
				}
			}
			roomName.setText(name);
			roomMoney.setText(money);
			roomAcreage.setText(acreage);
			telImg.setTag(position);
			telImg.setOnClickListener(CollectManagerAdapter.this);
		}

	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.delete){
			int position = (Integer) v.getTag();
			showDialog(position);
		}else if(v.getId() == R.id.telImg){
			int position = (Integer) v.getTag();
			String phone = list.get(position).get("phone").toString();
			 //传入服务， parse（）解析号码
           Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
           //通知activtity处理传入的call服务
           context.startActivity(intent);
		}else if(v.getId() == R.id.sendMessage){
			int position = (Integer) v.getTag();
			Bundle bundle = new Bundle();
			bundle.putString("shoujianren", list.get(position).get("ocreateusername").toString());
			bundle.putString("shoujianrenid", list.get(position).get("ocreateuserid").toString());
//			bundle.putString("shoujianren", "");
//			bundle.putString("shoujianrenid", "");
			bundle.putString("theme", "");
			bundle.putString("content", "");
			bundle.putString("WhoUserID", "");
			Intent intent  = new Intent(context,WriteNewLetterActivity.class);
			intent.putExtra("bundle", bundle);
			context.startActivity(intent);
		}else if(v.getId() == R.id.submitBtn&&click_limit){
			dialog.dismiss();
			position = (Integer) v.getTag();
			new Thread(runnable).start();
		}else if(v.getId() == R.id.cancleBtn){
			dialog.dismiss();
		}
	}
	
	private void showDialog(int position){
		dialog = new Dialog(context,R.style.dialog);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_roommanagerlist, null);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		dialog.addContentView(view, params);
		TextView titleText = (TextView) view.findViewById(R.id.titleText);
		TextView contentText = (TextView) view.findViewById(R.id.contentText);
		Button submitBtn = (Button) view.findViewById(R.id.submitBtn);
		Button cancleBtn = (Button) view.findViewById(R.id.cancleBtn);
		submitBtn.setTag(position);//将对应的集合下标设值给取消收藏按钮
		titleText.setText("取消收藏");
		contentText.setText("是否取消对改房源的收藏？");
		submitBtn.setText("取消收藏");
		cancleBtn.setText("放弃操作");
		submitBtn.setOnClickListener(CollectManagerAdapter.this);
		cancleBtn.setOnClickListener(CollectManagerAdapter.this);
		dialog.show();
	}
	
	/**
	 * 删除收藏
	 * true,删除成功，false 删除失败
	 */
	public boolean deleteCollect(int index) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				context).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				context).getAPPKEY()));
		params.add(new BasicNameValuePair("FavID", list.get(index).get("favoriteid").toString()));
		uploaddata = new SoSoUploadData(context, "FavoritesDelete.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(index,list.get(index).get("favoriteid").toString());
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	/***
	 * author by Ring 处理删除信件请求后的响应值
	 */

	public void dealReponse(int index,String favoriteid) {
		boolean b = false;
			if (StringUtils.CheckReponse(reponse)) {
				b=DBHelper.getInstance(context).delete("soso_favoriteinfo", "favoriteid=?", new String[]{favoriteid});
				if(b){
					list.remove(index);
				}
			}
	}
	/**
	 * author by Ring 处理删除信件耗时操作
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(context)) {
				handler.sendEmptyMessage(5);
				boolean b = deleteCollect(position);
				handler.sendEmptyMessage(6);
				if (runnable_tag) {
					runnable_tag = true;
					click_limit = true;
					return;
				}
				if (b) {
					handler.sendEmptyMessage(7);// 取消收藏成功
				} else {
					handler.sendEmptyMessage(3);// 取消收藏失败
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}

			click_limit = true;
		}
	};
	
	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 删除成功，跳出对话框提示用户
//				MessageBox.CreateAlertDialog(context.getResources().getString(R.string.deletelettlesuccess),
//						context);
//				notifyView();
				break;
			case 2:// 从注册界面跳转到登录界面
//				i.setClass(context, LoginActivity.class);
//				context.startActivity(i);
//				context.finish();
				break;
			case 3:// 注册失败给用户提示
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					message = context.getResources().getString(
							R.string.progress_timeout);
				} else {
					message = context.getResources().getString(R.string.deletecollectroom_failure);
				}
				MessageBox.CreateAlertDialog(message,
						context);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(context);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(context);
				progressdialog.setMessage(context.getResources().getString(
						R.string.progress_message_submit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setCancelable(true);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						if (uploaddata != null) {
							uploaddata.overReponse();
						}
						return false;
					}
				});
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 7:// 取消房源收藏成功
				MessageBox.CreateAlertDialog(context.getResources().getString(
						R.string.deletecollectroom_success),
						context);
				CollectManagerAdapter.this.notifyDataSetChanged();
				break;
			case 8:// 取消房源收藏失败
				MessageBox.CreateAlertDialog(context.getResources().getString(
						R.string.deletecollectroom_failure),
						context);
				break;
			}
		};
	};
}






















