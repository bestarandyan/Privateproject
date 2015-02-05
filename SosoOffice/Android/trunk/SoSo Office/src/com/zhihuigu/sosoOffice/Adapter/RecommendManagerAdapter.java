/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.zhihuigu.sosoOffice.DetailRoomInfoActivity;
import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.BitmapCache;
import com.zhihuigu.sosoOffice.utils.ImageDownloaderId;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

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
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 刘星星
 *@createDate 2013.1.31
 *这个就是推送管理里面的离别的适配器啦
 */
public class RecommendManagerAdapter extends BaseAdapter implements OnClickListener{
	private Context context;
	private ArrayList<HashMap<String,Object>> list;
	int listType = 1;//这个就是为了区分是推送详情 还是 有效性验证的列表啦 1代表前者
//	private ImageDownloaderId imageDownloader = null;
	
	
	
	private SoSoUploadData uploaddata;// 服务器请求对象
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private ProgressDialog progressdialog = null;
	private int position = 0;
	private int tag = 1;//1 设置为已出租，2，继续推送 3暂停推送
	
	public RecommendManagerAdapter(Context context,
			ArrayList<HashMap<String,Object>> list,int list_type) {
		this.context = context;
		this.list = list;
		this.listType = list_type;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_recommendmanager, null);
			holder.roomName = (TextView) convertView.findViewById(R.id.roomName);
			holder.roomNumberText = (TextView) convertView.findViewById(R.id.roomNumber);
			holder.roomMoney = (TextView) convertView.findViewById(R.id.roomMoney);
			holder.roomAcreage = (TextView) convertView.findViewById(R.id.roomAcreage);
//			holder.recommendAgency = (TextView) convertView.findViewById(R.id.zhongjieText);
//			holder.recommendWebsite = (TextView) convertView.findViewById(R.id.websiteText);
//			holder.textLayout = (LinearLayout) convertView.findViewById(R.id.textLayout);
//			holder.btnLayout = (LinearLayout) convertView.findViewById(R.id.btnLayout);
			holder.continueBtn = (Button) convertView.findViewById(R.id.continueRecommendBtn);
			holder.hireBtn = (Button) convertView.findViewById(R.id.chuzuedBtn);
			holder.pauseBtn = (Button) convertView.findViewById(R.id.pauseRecommendBtn);
			holder.jiantou = (ImageView) convertView.findViewById(R.id.jiantou);
			holder.parentLayout = (LinearLayout) convertView.findViewById(R.id.parentLayout);
			holder.roomPhoto = (ImageView) convertView.findViewById(R.id.roomPhoto);
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
		LinearLayout parentLayout;
		TextView roomName;//房源名称
		TextView roomNumberText;//房间号
		TextView roomMoney;//房间没平米米每天的价格
		TextView roomAcreage;//房间面积
//		TextView recommendAgency,recommendWebsite;//这里就是显示那个推送至中介和网站的数字显示控件啦  只显示数据就可以了的哦。。。。
//		LinearLayout textLayout;//这个是推送的文本布局
//		LinearLayout btnLayout;//这个是按钮的布局，
		Button continueBtn,hireBtn,pauseBtn;
		ImageView jiantou;//这个就是右边的箭头啦
		ImageView roomPhoto;
		public void setContent(int position){
			HashMap<String,Object> map = list.get(position);//根据position得到相应布局的数据
			String roomNumber = map.get("number").toString();
			String name = map.get("name").toString();
			String money = "￥"+map.get("money").toString()+"元/㎡/天";
			String acreage = map.get("acreage").toString()+"㎡";
			roomName.setText(name);
			roomNumberText.setText(roomNumber);
			roomMoney.setText(money);
			roomAcreage.setText(acreage);
			if(MyApplication.getInstance().getDisplayRoomPhoto() == 1){
				roomPhoto.setVisibility(View.VISIBLE);
				boolean b = true;
				File file = (File) list.get(position).get("file");
				if(file.exists()&&file.isFile()){
					Bitmap bitmap = BitmapCache.getInstance().getBitmap(file, context);
					if(bitmap!=null){
						roomPhoto.setImageBitmap(bitmap);
						b=false;
					}
				}
				if(b){
//					roomPhoto.setImageResource(R.drawable.soso_gray_logo);
					MyApplication.getInstance(context).getImageDownloaderId().download((File) list.get(position).get("file"), list
							.get(position).get("sql").toString(),
							list.get(position).get("id").toString(), list.get(position)
									.get("pixelswidth").toString(),
							list.get(position).get("pixelsheight").toString(),
							list.get(position).get("request_name").toString(),
							roomPhoto,"0","0",list.get(position)
							.get("pixelswidth").toString(),list.get(position).get("pixelsheight").toString());
				}
			}else{
				roomPhoto.setVisibility(View.GONE);
			}
			
			String ispush = "";
			
			try {
				ispush = list.get(position).get("pushstate").toString();
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(ispush.equals("0")){
				pauseBtn.setBackgroundResource(R.drawable.soso_recommend_item_btn);
				pauseBtn.setPadding(20, 0, 20, 0);
				pauseBtn.setText("暂停推送");
			}else{
				pauseBtn.setBackgroundResource(R.drawable.soso_lianxiren_btn);
				pauseBtn.setText("继续推送");
				pauseBtn.setPadding(20, 0, 20, 0);
			}
			continueBtn.setOnClickListener(RecommendManagerAdapter.this);
			hireBtn.setOnClickListener(RecommendManagerAdapter.this);
			pauseBtn.setOnClickListener(RecommendManagerAdapter.this);
			parentLayout.setOnClickListener(RecommendManagerAdapter.this);
			continueBtn.setTag(position);
			hireBtn.setTag(position);
			pauseBtn.setTag(position);
			parentLayout.setTag(R.id.parentLayout, position);
			if(listType == 1){//推送详情列表的控件赋值
				continueBtn.setVisibility(View.GONE);
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				param.setMargins(0, 0, 0, 0);
				hireBtn.setLayoutParams(param);
				LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				param1.setMargins(10, 0, 0, 0);
				pauseBtn.setLayoutParams(param1);
//				textLayout.setVisibility(View.VISIBLE);
//				btnLayout.setVisibility(View.GONE);
//				String agencynumber = map.get("agency").toString();
//				String websitenumber = map.get("website").toString();
//				recommendAgency.setText(agencynumber);
//				recommendWebsite.setText(websitenumber);
			}else if(listType == 2){//有效性验证的列表的控件赋值
				pauseBtn.setVisibility(View.GONE);
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				param.setMargins(0, 0, 0, 0);
				hireBtn.setLayoutParams(param);
				LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				param.setMargins(10, 0, 0, 0);
				continueBtn.setLayoutParams(param1);
//				textLayout.setVisibility(View.GONE);
//				btnLayout.setVisibility(View.VISIBLE);
//				jiantou.setVisibility(View.GONE);
				
			}
			}
		}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.continueRecommendBtn){//继续推送
			position = (Integer)v.getTag();
			AlertDialog.Builder alert = new  AlertDialog.Builder(context);
			alert.setTitle("提示：");
			alert.setIcon(android.R.drawable.ic_dialog_alert);
			alert.setMessage("确定要继续推送该房源吗？");
			alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					tag =2;
					new Thread(runnable1).start();
				}
			});
			alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			Dialog dialog = alert.create();
			dialog.show();
		}else if(v.getId() == R.id.chuzuedBtn){//已经出租
			position = (Integer)v.getTag();
			AlertDialog.Builder alert = new  AlertDialog.Builder(context);
			alert.setTitle("提示：");
			alert.setIcon(android.R.drawable.ic_dialog_alert);
			alert.setMessage("确定该房源已经出租吗？");
			alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					tag =1;
					new Thread(runnable1).start();
				}
			});
			alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			Dialog dialog = alert.create();
			dialog.show();
			
		}else if(v.getId() == R.id.pauseRecommendBtn){//暂停推送
			position = (Integer)v.getTag();
			String ispush = "";
			
			try {
				ispush = list.get(position).get("pushstate").toString();
			} catch (Exception e) {
				// TODO: handle exception
			}
			String msg ="";
			if(ispush.equals("0")){
				msg = "确定要暂停推送该房源吗？";
				tag = 3;
			}else{
				msg = "确定要继续推送该房源吗？";
				tag = 2;
			}
			AlertDialog.Builder alert = new  AlertDialog.Builder(context);
			alert.setTitle("提示：");
			alert.setIcon(android.R.drawable.ic_dialog_alert);
			alert.setMessage(msg);
			alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new Thread(runnable1).start();
				}
			});
			alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			Dialog dialog = alert.create();
			dialog.show();
		}else if(v.getId() == R.id.parentLayout){//查看详情
			if(list.size()>0){
				int position = (Integer)v.getTag(R.id.parentLayout);
				MyApplication.getInstance().setOfficeid(list.get(position).get("officeid").toString());
				Intent intent = new Intent(context,DetailRoomInfoActivity.class);
				context.startActivity(intent);
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
		String officestate = (list.get(index).get("state").equals("1"))?"0":"1";
		params.add(new BasicNameValuePair("OfficeID", list.get(position).get("officeid").toString()));
		params.add(new BasicNameValuePair("OfficeState", officestate));
		SoSoUploadData uploaddata = new SoSoUploadData(context, "OfficeUpdateState.aspx", params);
		uploaddata.Post();
		if(uploaddata!=null){
			reponse = uploaddata.getReponse();
		}
		dealReponse1(index,list.get(index).get("officeid").toString(),officestate);
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
	/***
	 * author by Ring 处理请求后的响应值
	 */

	public void dealReponse1(int index,String xgid,String officestate) {
		boolean b = false;
			if (StringUtils.CheckReponse(reponse)) {
				ContentValues values = new ContentValues();
//				String tablename = "";
				if(tag ==1){
//					if(listType == 1){//推送详情列表的控件赋值
//						tablename = "soso_pushinfo";
//					}else if(listType == 2){//有效性验证的列表的控件赋值
//						tablename = "";
//					}
//					values.put("isrent", officestate);
//					b=DBHelper.getInstance(context).update("soso_pushinfo", values, "officeid=?", new String[]{xgid});
					b=DBHelper.getInstance(context).delete("soso_pushinfo", "officeid=?", new String[]{xgid});
					if(b){
//						list.get(index).put("officestate",officestate);//更新房源状态
						list.remove(index);
					}
			} else if (tag == 2) {
				if (listType == 2) {// 有效性验证的列表的控件赋值
					values.put("pushtag", 0);
					list.remove(index);
				}
				values.put("pushstate", officestate);
				b = DBHelper.getInstance(context).update("soso_pushinfo",
						values, "officeid=?", new String[] { xgid });
				if(b){
					list.get(index).put("pushstate",officestate);//更新房源状态
				}
			}else if(tag == 3){
					values.put("pushstate", officestate);
					b=DBHelper.getInstance(context).update("soso_pushinfo", values, "officeid=?", new String[]{xgid});
					boolean b1=DBHelper.getInstance(context).delete("soso_pushinfo", "officeid=?", new String[]{xgid});
					if(b){
						list.get(index).put("pushstate",officestate);//更新房源状态
					}
					if(b1){
						list.remove(index);
					}
					
				}
				
			}
	}
	
	/**
	 * author by Ring 处理耗时操作
	 */
	public Runnable runnable1 = new Runnable() {
		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (uploaddata != null) {
				uploaddata.overReponse();
				uploaddata = null;
			}
			if (NetworkCheck.IsHaveInternet(context)) {
				if(tag == 1){//设置已出租的请求
					boolean b = false;
					handler.sendEmptyMessage(5);
					b=updateOfficeState(position);
					handler.sendEmptyMessage(6);
					if (runnable_tag) {
						runnable_tag = false;
						click_limit = true;
						return;
					}
					if(b){
						handler.sendEmptyMessage(1);
					}else{
						handler.sendEmptyMessage(3);
					}
				}else if(tag==2){//继续推送的请求
					boolean b = false;
					handler.sendEmptyMessage(5);
					b=recommendAgain(position);
					handler.sendEmptyMessage(6);
					if (runnable_tag) {
						runnable_tag = false;
						click_limit = true;
						return;
					}
					if(b){
						handler.sendEmptyMessage(1);
					}else{
						handler.sendEmptyMessage(3);
					}
				}else if(tag ==3){//暂停推送的请求
					boolean b = false;
					handler.sendEmptyMessage(5);
					b=recommendPause(position);
					handler.sendEmptyMessage(6);
					if (runnable_tag) {
						runnable_tag = false;
						click_limit = true;
						return;
					}
					if(b){
						handler.sendEmptyMessage(1);
					}else{
						handler.sendEmptyMessage(3);
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
				MessageBox.CreateAlertDialog(context.getResources().getString(R.string.updateofficestate_success),
						context);
				RecommendManagerAdapter.this.notifyDataSetChanged();
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
					message = context.getResources().getString(R.string.updateofficestate_failure);
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
			}
		};
	};
	
}
 