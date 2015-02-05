/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.zhihuigu.sosoOffice.DetailDemandActivity;
import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.ReleaseNewRoomActivity;
import com.zhihuigu.sosoOffice.RoomManagerActivity;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.BitmapCache;
import com.zhihuigu.sosoOffice.utils.ImageDownloaderId;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 刘星星
 * @createDate 2013/1/23
 * 房源管理的文字形式的列表的适配器
 *
 */
public class RoomManagerForListAdapter extends BaseAdapter implements OnClickListener{
	public ArrayList<HashMap<String,Object>> list;
	public Activity context;
	
	
	
	private SoSoUploadData uploaddata;// 服务器请求对象
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private ProgressDialog progressdialog = null;
	private int position = 0;
	private int tag = 1;//1 推送 2 删除待审核的房源
//	private ImageDownloaderId imageDownloader = null;
	
	public RoomManagerForListAdapter(Activity context,ArrayList<HashMap<String,Object>> list) {
		this.context  = context;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_roommanager_forlist, null);
			holder.imageState = (ImageView) convertView.findViewById(R.id.state_image);
			holder.roomName = (TextView) convertView.findViewById(R.id.room_name);
//			holder.roomNumberText = (TextView) convertView.findViewById(R.id.roomNumber);
			holder.roomMoney = (TextView) convertView.findViewById(R.id.roomMoney);
			holder.roomAcreage = (TextView) convertView.findViewById(R.id.roomAcreage);
			holder.jianTou = (ImageView) convertView.findViewById(R.id.jiantou);
			holder.bianji = (ImageView) convertView.findViewById(R.id.bianji);
			holder.tuiSong = (ImageView) convertView.findViewById(R.id.tuisong);
			holder.setState = (ImageView) convertView.findViewById(R.id.setState);
			holder.deleteRoomBtn = (ImageView) convertView.findViewById(R.id.deleteBtn);
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
	class ViewHolder{
		ImageView imageState;//房源状态的图片
		TextView roomName;//房源名称
//		TextView roomNumberText;//房间号
		TextView roomMoney;//房间没平米米每天的价格
		TextView roomAcreage;//房间面积
		ImageView jianTou;//最右边的箭头
		ImageView bianji;//编辑按钮
		ImageView tuiSong;//推送按钮
		ImageView setState;//设置房源状态的按钮
		ImageView deleteRoomBtn;
		ImageView roomImage;//房源图片
		public void setContent(int position){
			int view_state = MyApplication.getInstance(context).getRoom_list_state();
			int room_state = MyApplication.getInstance(context).getRoom_state_for_examine();
			HashMap<String,Object> map = list.get(position);//根据position得到相应布局的数据
			String stateStr = map.get("officestate").toString();
			String roomNumber = map.get("floors").toString()+position+"号";
			String name = map.get("officemc").toString();
			String moneyDown = map.get("pricedown").toString();
			String moneyUp = map.get("priceup").toString();
			String acreage = map.get("areaup").toString();
			if(room_state == 1){//审核通过的房源
				if(stateStr.equals("1")){//已经出租的房子
					imageState.setImageResource(R.drawable.soso_house_yizu);
					
				}else{//还没有出租的房子
					imageState.setImageResource(R.drawable.soso_house_weizu);
				}
				String ispush = map.get("ispushed").toString();
				if(ispush.equals("1")){
//					String pushstate = list.get(position).get("pushstate").toString();
//					if (pushstate.equals("1")) {
//						tuiSong.setImageResource(R.drawable.soso_house_tesong);
//					}else{
						tuiSong.setImageResource(R.drawable.soso_house_yitesong);
//					}
				}else{
					tuiSong.setImageResource(R.drawable.soso_house_tesong);
				}
				
			}else if(room_state == 2){//待审核的房子
				imageState.setImageResource(R.drawable.soso_house_daisheng);
			}
			if(name.length()>8){
				name = name.substring(0, 8)+"...";
			}
			String money = "";
			if(moneyDown!=null && !moneyDown.equals("")){
				money = moneyDown+"-"+moneyUp+"元/㎡/天";
			}else{
				money = moneyUp+"元/㎡/天";
			}
			
//			if(acreage.length()>4){
//				acreage = acreage.substring(0,4);
//			}
			acreage = acreage+"㎡";
			roomName.setText(name);
//			roomNumberText.setText(name);
//			String str = money + " "+ acreage;
//			if(str.length()>16){
//				str = str.substring(0, 16)+"···";
//			}
			roomMoney.setText("￥"+money);
//			roomAcreage.setVisibility(View.GONE);
			roomAcreage.setText(acreage);
			if(view_state == 1){//非编辑状态
				bianji.setVisibility(View.GONE);
				tuiSong.setVisibility(View.GONE);
				setState.setVisibility(View.GONE);
				jianTou.setVisibility(View.VISIBLE);
			}else if(view_state == 2){//编辑状态
				bianji.setVisibility(View.VISIBLE);
				bianji.setTag(position);
				bianji.setOnClickListener(RoomManagerForListAdapter.this);
				jianTou.setVisibility(View.GONE);
				if(room_state == 1){//审核通过的房源
					tuiSong.setVisibility(View.VISIBLE);
					setState.setVisibility(View.VISIBLE);
					if(stateStr.equals("1")){//已经出租的房子
						setState.setImageResource(R.drawable.soso_house_yes);
						tuiSong.setVisibility(View.GONE);
					}else{//还没有出租的房子
						setState.setImageResource(R.drawable.soso_house_no);
					}
					tuiSong.setTag(position);
					setState.setTag(position);
					tuiSong.setOnClickListener(RoomManagerForListAdapter.this);
					setState.setOnClickListener(RoomManagerForListAdapter.this);
				}else if(room_state == 2){//待审核房源
					deleteRoomBtn.setVisibility(View.VISIBLE);
					deleteRoomBtn.setTag(position);
					deleteRoomBtn.setOnClickListener(RoomManagerForListAdapter.this);
				}
			}
			if (MyApplication.getInstance().getDisplayRoomPhoto() == 1) {//是否显示图片，0提示，1显示,2不显示
				roomImage.setVisibility(View.VISIBLE);
				imageState.setVisibility(View.GONE);
//				roomImage.setImageResource(R.drawable.soso_gray_logo);
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
		if(v.getId() == R.id.bianji){
			int position = (Integer) v.getTag();
			HashMap<String,Object> map = list.get(position);//根据position得到相应布局的数据
//			Bundle bundle = new Bundle();
//			bundle.putString("name", map.get("officemc").toString());
//			bundle.putString("money", map.get("priceup").toString());
//			bundle.putString("acreage", map.get("areaup").toString());
//			bundle.putString("roomNumber", map.get("floors").toString());
//			bundle.putString("loupan", "长城大厦");
//			bundle.putString("layer", map.get("floors").toString());
//			bundle.putString("wuye", map.get("wymanagementfees").toString());
//			bundle.putString("defanglv", map.get("roomrate").toString());
//			bundle.putString("jianjie", map.get("fyjj").toString());
//			bundle.putString("roomtype", map.get("officetype").toString());
			MyApplication.getInstance().setAddRoomFlag(1);
			Intent intent = new Intent(context,ReleaseNewRoomActivity.class);
			intent.putExtra("officeid", map.get("officeid").toString());
			intent.putExtra("activityFlag", 1);
			context.startActivity(intent);
		}else if(v.getId() == R.id.tuisong){// 推送
//			Toast.makeText(context, "这是推送按钮的第"+v.getTag().toString(), 3000).show();
			position = (Integer) v.getTag();
			showDialog(0,position);
		}else if(v.getId() == R.id.setState){//设置状态
			position = (Integer) v.getTag();
			showDialog(2,position);
		}else if(v.getId() == R.id.deleteBtn){//删除房源
			position = (Integer) v.getTag();
			showDialog(1,position);
		}else if(v.getId() == R.id.submitBtn){// 确认按钮
			position = (Integer) v.getTag();
			if (dialog_flag == 0) {// 推送
				String ispush = list.get(position).get("ispushed").toString();
				if (ispush.equals("1")) {
//					String pushstate = list.get(position).get("pushstate").toString();
//					if (pushstate.equals("1")) {
//						tag = 5;
//						new Thread(runnable).start();
//					}else{
						tag = 4;
						new Thread(runnable).start();
//					}
				} else {
					tag = 1;
					new Thread(runnable).start();
				}

			}else if(dialog_flag == 1){//删除
				tag = 2;
				new Thread(runnable).start();
			}else if(dialog_flag == 2){//更新房源状态
				tag = 3;
				new Thread(runnable).start();
			}
			dialog.dismiss();
		}else if(v.getId() == R.id.cancleBtn){//取消操作
			dialog.dismiss();
		}
	}
	Dialog dialog = null;
	private int dialog_flag = 0;//0代表为推送的对话框 1代表为删除的对话框 2，代表改变房源状态
	/**
	 * 提示对话框
	 * @param dialog_flag 0代表为推送的对话框 1代表为删除的对话框 2，代表改变房源状态
	 * @param position 列表中的位置
	 * @author 刘星星
	 * @createDate 2013/2/19
	 */
	private void showDialog(int dialog_flag,int position){
		 dialog = new Dialog(context,R.style.dialog);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_roommanagerlist, null);
//		dialog.setView(view);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		dialog.addContentView(view, params);
//		Window window = dialog.getWindow();
//		WindowManager.LayoutParams param = window.getAttributes();
//		param.alpha = 0.9f;//设置透明度
//		window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,   
//				 WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
//		window.setAttributes(param);
		TextView titleText = (TextView) view.findViewById(R.id.titleText);
		TextView contentText = (TextView) view.findViewById(R.id.contentText);
		Button submitBtn = (Button) view.findViewById(R.id.submitBtn);
		Button cancleBtn = (Button) view.findViewById(R.id.cancleBtn);
		submitBtn.setOnClickListener(RoomManagerForListAdapter.this);
		cancleBtn.setOnClickListener(RoomManagerForListAdapter.this);
		submitBtn.setTag(position);
		if(dialog_flag == 1){
			this.dialog_flag = 1;
			titleText.setText("删除房源");
			contentText.setText("确定是否删除该房源？");
			submitBtn.setText("确认删除");
			cancleBtn.setText("取消动作");
		}else if(dialog_flag == 0){
			this.dialog_flag = 0;
			titleText.setText("房源推送");
			String ispush = list.get(position).get("ispushed").toString();
			if(ispush.equals("1")){
//				String pushstate = list.get(position).get("pushstate").toString();
//				if (pushstate.equals("1")) {
//					contentText.setText("是否继续推送？");
//					submitBtn.setText("确认继续");
//				}else{
					contentText.setText("是否取消推送？");
					submitBtn.setText("确认取消");
//				}
			}else{
				contentText.setText("是否将房源推送至中介及合作网站？");
				submitBtn.setText("确认推送");
			}
			cancleBtn.setText("取消动作");
		}else if(dialog_flag == 2){
			this.dialog_flag = 2;
			titleText.setText("更新房源状态");
			String stateStr = list.get(position).get("officestate").toString();
			if(stateStr.equals("1")){
				contentText.setText("是否将该房源状态设置为未租？");
			}else if(stateStr.equals("0")){
				contentText.setText("是否将该房源状态设置为已租？");
			}
			submitBtn.setText("确认设置");
			cancleBtn.setText("取消动作");
		}
		dialog.show();
	}
	
	
	
	/**
	 * 推送房源
	 * true,推送成功，false 推送失败
	 */
	public boolean pushRoom(int index) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				context).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				context).getAPPKEY()));
		params.add(new BasicNameValuePair("OfficeID", list.get(index).get("officeid").toString()));
		params.add(new BasicNameValuePair("UserID", MyApplication.getInstance(context).getSosouserinfo(context).getUserID()));
		params.add(new BasicNameValuePair("PushTarget", "1"));
		params.add(new BasicNameValuePair("pushContent", ""));
		params.add(new BasicNameValuePair("PushResult", "1"));
		params.add(new BasicNameValuePair("PushState", "0"));
		uploaddata = new SoSoUploadData(context, "PushAdd.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse1(index,list.get(index).get("officeid").toString(),1+"");
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 暂停推送
	 * true,更新成功，false 更新失败
	 */
	public boolean recommendPause(int index) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				context).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				context).getAPPKEY()));
		params.add(new BasicNameValuePair("OfficeID", list.get(index).get("officeid").toString()));
		SoSoUploadData uploaddata = new SoSoUploadData(context, "PushStop.aspx", params);
		uploaddata.Post();
		if(uploaddata!=null){
			reponse = uploaddata.getReponse();
		}
		dealReponse1(index,list.get(index).get("officeid").toString(),1+"");
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 继续推送
	 * true,更新成功，false 更新失败
	 */
	public boolean recommendAgain(int index) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				context).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				context).getAPPKEY()));
		params.add(new BasicNameValuePair("OfficeID", list.get(index).get("officeid").toString()));
		SoSoUploadData uploaddata = new SoSoUploadData(context, "PushGoOn.aspx", params);
		uploaddata.Post();
		if(uploaddata!=null){
			reponse = uploaddata.getReponse();
		}
		dealReponse1(index,list.get(index).get("officeid").toString(),0+"");
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 删除待审核的房源
	 * true,删除成功，false 删除失败
	 */
	public boolean deleteOfficeInfo(int index) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				context).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				context).getAPPKEY()));
		params.add(new BasicNameValuePair("OfficeID", list.get(index).get("officeid").toString()));
		uploaddata = new SoSoUploadData(context, "OfficeDelete.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(index,list.get(index).get("officeid").toString());
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	/***
	 * author by Ring 处理请求后的响应值
	 */

	public void dealReponse(int index,String officeid) {
		boolean b = false;
			if (StringUtils.CheckReponse(reponse)) {
				b=DBHelper.getInstance(context).delete("soso_officeinfo", "officeid=?", new String[]{officeid});
				if(index<=list.size()-1&&
						list.get(index).get("buildid")!=null){
					List<Map<String, Object>> selectresult = null;
					String sql = "select * from soso_officeinfo where buildid = '"
							+ list.get(index).get("buildid").toString()
							+ "' and createuserid='"
							+ MyApplication.getInstance(context).getSosouserinfo(context)
									.getUserID() + "'";
					selectresult = DBHelper.getInstance(context).selectRow(sql, null);
					if(selectresult!=null&&selectresult.size()<=0){
						DBHelper.getInstance(context).delete("soso_builduserinfo", "buildid=?", new String[]{list.get(index).get("buildid").toString()});
						
					}
				}
				if(b){
					list.remove(index);
				}
			}
	}
	
	/**
	 * 更新房源状态
	 * true,更新成功，false 更新失败
	 */
	public boolean updateOfficeState(int index) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				context).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				context).getAPPKEY()));
		String officestate = (list.get(index).get("officestate").equals("1"))?"0":"1";
		params.add(new BasicNameValuePair("OfficeID", list.get(index).get("officeid").toString()));
		params.add(new BasicNameValuePair("OfficeState", officestate));
		uploaddata = new SoSoUploadData(context, "OfficeUpdateState.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse1(index,list.get(index).get("officeid").toString(),officestate);
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	/***
	 * author by Ring 处理请求后的响应值
	 */

	public void dealReponse1(int index, String officeid, String officestate) {
		boolean b = false;
		if (StringUtils.CheckReponse(reponse)) {
			ContentValues values = new ContentValues();
			if (tag == 1) {
				values.put("ispushed", 1);
				b = DBHelper.getInstance(context).update("soso_officeinfo",
						values, "officeid=?", new String[] { officeid });
				values.put("pushstate", 0);
				DBHelper.getInstance(context).update("soso_pushinfo", values,
						"officeid=?", new String[] { officeid });
				if (b) {
					list.get(index).put("ispushed", 1);// 推送成功
				}
				list.get(index).put("pushstate", 0);// 推送成功
			} else if (tag == 4) {
				values.put("ispushed", 0);
				b = DBHelper.getInstance(context).update("soso_officeinfo",
						values, "officeid=?", new String[] { officeid });
				values.put("pushstate", officestate);
				DBHelper.getInstance(context).update("soso_pushinfo", values,
						"officeid=?", new String[] { officeid });
				list.get(index).put("ispushed", 0);// 暂停推送
				list.get(index).put("pushstate", officestate);
			} else if (tag == 5) {
				values.put("ispushed", 1);
				b = DBHelper.getInstance(context).update("soso_officeinfo",
						values, "officeid=?", new String[] { officeid });
				values.put("pushstate", officestate);
				b = DBHelper.getInstance(context).update("soso_pushinfo",
						values, "officeid=?", new String[] { officeid });
				list.get(index).put("ispushed", 1);// 暂停推送
				list.get(index).put("pushstate", officestate);
			} else if (tag == 3) {
				values.put("isrent", officestate);
				b = DBHelper.getInstance(context).update("soso_officeinfo",
						values, "officeid=?", new String[] { officeid });
				if (b) {
					list.get(index).put("officestate", officestate);// 更新房源状态
				}
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
				if(tag==1){//新添加的房源进行推送
					handler.sendEmptyMessage(5);
					boolean b = pushRoom(position);
					handler.sendEmptyMessage(6);
					if (runnable_tag) {
						runnable_tag = true;
						click_limit = true;
						return;
					}
					if (b) {
						handler.sendEmptyMessage(7);// 推送成功
					} else {
						handler.sendEmptyMessage(3);// 推送失败
					}
				}else if(tag==2){//删除房源
					handler.sendEmptyMessage(5);
					boolean b = deleteOfficeInfo(position);
					handler.sendEmptyMessage(6);
					if (runnable_tag) {
						runnable_tag = true;
						click_limit = true;
						return;
					}
					if (b) {
						handler.sendEmptyMessage(1);// 删除成功
					} else {
						handler.sendEmptyMessage(8);// 删除失败
					}
				}else if(tag ==3){//更新房源状态
					handler.sendEmptyMessage(5);
					boolean b = updateOfficeState(position);
					handler.sendEmptyMessage(6);
					if (runnable_tag) {
						runnable_tag = true;
						click_limit = true;
						return;
					}
					if (b) {
						handler.sendEmptyMessage(9);//更新房源状态成功
					} else {
						handler.sendEmptyMessage(10);//更新房源状态失败
					}
				}else if(tag ==4){//暂停推送
					handler.sendEmptyMessage(5);
					boolean b = recommendPause(position);
					handler.sendEmptyMessage(6);
					if (runnable_tag) {
						runnable_tag = true;
						click_limit = true;
						return;
					}
					if (b) {
						handler.sendEmptyMessage(9);//更新房源状态成功
					} else {
						handler.sendEmptyMessage(10);//更新房源状态失败
					}
				}else if(tag ==5){//推送过的房源继续推送
					handler.sendEmptyMessage(5);
					boolean b = recommendAgain(position);
					handler.sendEmptyMessage(6);
					if (runnable_tag) {
						runnable_tag = true;
						click_limit = true;
						return;
					}
					if (b) {
						handler.sendEmptyMessage(9);//更新房源状态成功
					} else {
						handler.sendEmptyMessage(10);//更新房源状态失败
					}
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
				MessageBox.CreateAlertDialog(context.getResources().getString(R.string.deleteoffice_success),
						context);
				RoomManagerForListAdapter.this.notifyDataSetChanged();
				break;
			case 2:// 从注册界面跳转到登录界面
//				i.setClass(context, LoginActivity.class);
//				context.startActivity(i);
//				context.finish();
				break;
			case 3:// 推送失败给用户提示
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					message = context.getResources().getString(
							R.string.progress_timeout);
				} else {
					message = context.getResources().getString(R.string.pushroom_failure);
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
			case 7:// 推送成功
				MessageBox.CreateAlertDialog(context.getResources().getString(
						R.string.pushroom_success),
						context);
				RoomManagerForListAdapter.this.notifyDataSetChanged();
				break;
			case 8:// 删除房源失败
				String message1 = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					message1 = context.getResources().getString(
							R.string.progress_timeout);
				} else {
					message1 = context.getResources().getString(R.string.deleteoffice_failure);
				}
				MessageBox.CreateAlertDialog(message1,
						context);
				break;
			case 9:// 更新房源状态成功
				MessageBox.CreateAlertDialog(context.getResources().getString(
						R.string.updateoffice_success),
						context);
				RoomManagerForListAdapter.this.notifyDataSetChanged();
				break;
			case 10:// 更新房源状态失败
				String message2 = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					message2 = context.getResources().getString(
							R.string.progress_timeout);
				} else {
					message2 = context.getResources().getString(R.string.updateoffice_failure);
				}
				MessageBox.CreateAlertDialog(message2,
						context);
				break;
			}
		};
	};
	
}























